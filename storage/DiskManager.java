
import java.io.RandomAccessFile;


public class DiskManager {
    private final RandomAccessFile file;

    public DiskManager(String fileName){
        try {
            this.file = new RandomAccessFile(fileName, "rw");
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }



    public Page loadPage(int pageId){
        try {
            long pageOffset = pageId * Page.PAGE_SIZE;

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
}
