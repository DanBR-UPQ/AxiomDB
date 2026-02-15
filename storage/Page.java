

public class Page {
    public static final int PAGE_SIZE = 4096;
    
    private int pageId = 0;
    private byte[] data;
    private boolean dirty;
    private int pinCount;

    public Page(int pageId){
        this.pageId = pageId;
        this.data = new byte[PAGE_SIZE];
        this.dirty = false;
        this.pinCount = 0;
    }



    public void pin(){
        pinCount++;
    }
    public void unpin(){
        pinCount--;
    }



    public void markDirty(){
        dirty = true;
    }

    // get methods
    public int getPageId(){
        return pageId;
    }
    public byte[] getData(){
        return data;
    }
    public boolean isDirty(){
        return dirty;
    }
    public int getPinCount(){
        return pinCount;
    }
}