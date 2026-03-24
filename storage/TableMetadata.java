import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TableMetadata {
    private final String tableName;
    private final Schema schema;

    public TableMetadata(String tableName, Schema schema){
        this.tableName = tableName;
        this.schema = schema;
    }

    public void saveSchema(){
        String fileName = tableName + ".schema";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            for (Column column : schema.getColumns()) {

                String line = column.getName() + " " +
                              column.getType() + " " +
                              column.getLength();

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to save schema", e);
        }

    }

    public String getTableName() { return tableName; }
    public Schema getSchema() { return schema; }
}
