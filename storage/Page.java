

public class Page {
    public static final int PAGE_SIZE = 4096;
    
    private int pageId = 0;
    private byte[] data;

    public Page(int pageId){
        this.pageId = pageId;
        this.data = new byte[PAGE_SIZE];
    }



    // get methods
    public int getPageId(){
        return pageId;
    }
    public byte[] getPageData(){
        return data;
    }
}