
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
        if (getRecordCount() == 0 && getFreeSpaceOffset() == 0) { 
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


    public int insertRecord(byte[] recordData){
        int recordSize = recordData.length;
        int freeOffset = getFreeSpaceOffset();
        int recordCount = getRecordCount();

        System.arraycopy(
            recordData,
            0,
            page.getData(),
            freeOffset,
            recordSize
        );

        // slotPos = pageSize - (slotIndex + 1) * 4
        // eg slot 0 = 4096 - (0 + 1) * 4 = 4092
        int slotPosition = getPageSize() - (recordCount + 1) * 4;
        writeInt(slotPosition, freeOffset);

        setRecordCount(recordCount + 1);
        setFreeSpaceOffset(recordSize + freeOffset);
        page.markDirty();
        return freeOffset; // recordCount is our ID rn
    }

    private int getPageSize() {
        return page.getData().length;
    }   

}
