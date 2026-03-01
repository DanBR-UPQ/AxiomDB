public class Table {
    
    private final DiskManager diskManager;

    public Table(String fileName){
        this.diskManager = new DiskManager(fileName);
    }

    public RecordId insert(byte[] data){

        for (int i = 0; i < diskManager.getPageCount(); i++){

            Page page = diskManager.loadPage(i);
            SlottedPage slottedPage = new SlottedPage(page);

            if (slottedPage.hasSpace(data.length)){ // pretending data.length is it's actual size for now
            //Todo: Add proper space detection
                
                int SlotId = slottedPage.insertRecord(data);
            
                diskManager.writePage(page);

                return new RecordId(page.getPageId(), SlotId);
            }
        }

        Page newPage = diskManager.makeNewPage();
        SlottedPage slottedPage = new SlottedPage(newPage);

        int slotId = slottedPage.insertRecord(data);
        diskManager.writePage(newPage);
        return new RecordId(newPage.getPageId(), slotId);
        
    }


    public byte[] read(RecordId recordId, int recordSize){

        Page page = diskManager.loadPage(recordId.getPageId());
        SlottedPage slottedPage = new SlottedPage(page);

        byte[] data = slottedPage.readRecord(recordId.getSlotId(), recordSize);

        return data;
    }

}
