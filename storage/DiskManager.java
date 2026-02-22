
import java.io.RandomAccessFile;


public class DiskManager {
    private final RandomAccessFile file;

    public DiskManager(String fileName){
        try {
            this.file = new RandomAccessFile(fileName, "rw");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
