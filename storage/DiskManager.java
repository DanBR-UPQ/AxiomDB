
import java.io.RandomAccessFile;


public class DiskManager {
    private final RandomAccessFile file;

    public DiskManager(String fileName){
        try {
            this.file = new RandomAccessFile(fileName, "rw"); // RAF is basically just a cursor that lets u read and write
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }



    public Page loadPage(int pageId){
        try {
            long pageOffset = (long) pageId * Page.PAGE_SIZE;

            if (pageOffset >= file.length()){
                throw new IllegalArgumentException("Page doesn't exist");
            }

            file.seek(pageOffset); // File points at pageOffset

            Page page = new Page(pageId);
            file.readFully(page.getData()); // copies stuff from file to our page's data, from pageOffset to data.len

            return page;

        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writePage(Page page){
        try{
            long pageOffset = (long) page.getPageId() * Page.PAGE_SIZE;

            if (pageOffset > file.length()){
                throw new IllegalArgumentException("Page doesn't exist");
            }

            file.seek(pageOffset);

            file.write(page.getData());

        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}
