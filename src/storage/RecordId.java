package storage;

public class RecordId {
    private final int PageId;
    private final int SlotId;

    public RecordId(int PageId, int SlotId){
        this.PageId = PageId;
        this.SlotId = SlotId;
    }

    public int getPageId() { return PageId;}
    public int getSlotId() { return SlotId;}
}
