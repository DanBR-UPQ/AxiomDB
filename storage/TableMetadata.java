import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public static TableMetadata load(String tableName) {
        String fileName = tableName + ".schema";
        List<Column> columns = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) { continue; }

                String[] parts = line.split(" "); /* parts[0] name, parts[1] type, parts[2] length */

                
                if (parts.length != 3) {
                    throw new RuntimeException("Invalid schema format in line: " + line);
                }

                String columnName = parts[0];
                String typeString = parts[1];
                int length;

                try { // id int abc
                    length = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid length in schema: " + parts[2]);
                }

                DataType type;
                try {
                    type = DataType.valueOf(typeString);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid data type in schema: " + typeString);
                }


                Column column = new Column(columnName, type, length);

                columns.add(column);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load schema, not found for table:" + tableName, e);
        }

        Schema schema = new Schema(columns);
        return new TableMetadata(tableName, schema);
    }

    public String getTableName() { return tableName; }
    public Schema getSchema() { return schema; }
}
