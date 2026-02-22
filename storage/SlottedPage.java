
import java.nio.ByteBuffer;


public class SlottedPage {

    public static final int HEADER_SIZE = 8; // 4 Bytes for rowCount, 4 for freeSpaceOffset

    public static final int RECORD_COUNT_OFFSET = 0;
    public static final int FREE_SPACE_OFFSET_OFFSET = 4;

    private final Page page;

    public SlottedPage(Page page){
        this.page = page;
        initializeIfEmpty();
    }

    private void initializeIfEmpty(){
        if (getFreeSpaceOffset() == 0) { 
            setRecordCount(0);
            setFreeSpaceOffset(HEADER_SIZE);
        }
    }


    // ---------------- Buffer helpers ----------------
    private int readInt(int offset){
        ByteBuffer buffer = ByteBuffer.wrap(page.getData());
        return buffer.getInt(offset);
    }
    private void writeInt(int offset, int value){
        ByteBuffer buffer = ByteBuffer.wrap(page.getData());
        buffer.putInt(offset, value);
        page.markDirty();
    }


    // ---------------- Header helpers ----------------
    public int getRecordCount(){
        return readInt(RECORD_COUNT_OFFSET);
    }
    public int getFreeSpaceOffset(){
        return readInt(FREE_SPACE_OFFSET_OFFSET);
    }
    public void setRecordCount(int value){
        writeInt(RECORD_COUNT_OFFSET, value);
    }
    public void setFreeSpaceOffset(int value){
        writeInt(FREE_SPACE_OFFSET_OFFSET, value);
    }


    private int getPageSize() {
        return page.getData().length;
    }   


    // ---------------- Reading / Accesing helpers ----------------
    private int getSlotPosition(int slotIndex){ // slot 0 = 4096 - (0 + 1) * 4 = 4092, slot 0 is at 4092 bytes
        return getPageSize() - (slotIndex + 1) * 4;
    }

    private int getSlotOffset(int slotIndex){ // slot 0 = 8 record starts at 8 bytes for example
        int slotPosition = getSlotPosition(slotIndex);
        return readInt(slotPosition);
    }
    // slotIndex is our record ID



    // ---------------- PUBLIC FUNCTIONS ----------------
    public int insertRecord(byte[] recordData){
        int recordSize = recordData.length;
        int freeOffset = getFreeSpaceOffset();
        int recordCount = getRecordCount();


        int recordWriteEnd = freeOffset + recordSize;
        int slotPosition = getPageSize() - (recordCount + 1) * 4;

        if (recordWriteEnd > slotPosition){
            throw new RuntimeException("Page is full");
        }


        System.arraycopy(recordData, 0, page.getData(), freeOffset, recordSize);

        // slotPos = pageSize - (slotIndex + 1) * 4
        // eg slot 0 = 4096 - (0 + 1) * 4 = 4092
        writeInt(slotPosition, freeOffset);


        setRecordCount(recordCount + 1);
        setFreeSpaceOffset(recordSize + freeOffset);
        page.markDirty();
        return recordCount; // recordCount is our slot ID
    }


    public byte[] readRecord(int slotIndex, int recordSize){
        byte[] record = new byte[recordSize];

        if(slotIndex < 0 || slotIndex >= getRecordCount()){
            throw new IllegalArgumentException("Invalid index");
        }

        int recordOffset = getSlotOffset(slotIndex);

        System.arraycopy(page.getData(), recordOffset, record, 0, recordSize);

        return record;
    }


    public int getPageId(){
        return page.getPageId();
    }
    public int getSlotCount() {
        return getRecordCount();
    }
    public boolean hasSpace(int recordSize){
        int RecordWriteEnd = getFreeSpaceOffset() + recordSize;
        int slotPosition = getPageSize() - (getRecordCount() + 1) * 4;
        
        return RecordWriteEnd <= slotPosition;
    }
    public int getFreeSpaceSize(){
        int RecordWriteEnd = getFreeSpaceOffset();
        int slotPosition = getPageSize() - (getRecordCount() + 1) * 4;
        
        return slotPosition - RecordWriteEnd;
    }

}
