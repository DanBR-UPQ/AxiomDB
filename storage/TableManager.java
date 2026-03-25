import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TableManager {
    private final Map<String, Table> tables;

    public TableManager() {
        this.tables = new HashMap<>();
    }


    public Table getTable(String tableName) {
        if (tables.containsKey(tableName)) {
            return tables.get(tableName);
        }

        TableMetadata metadata = TableMetadata.load(tableName);
        Schema schema = metadata.getSchema();

        // Table object is what has the .data file
        String dataFileName = tableName + ".data";
        Table table = new Table(dataFileName);

        tables.put(tableName, table);
        return table;
    }


    public void createTable(String tableName, Schema schema){

        if (tables.containsKey(tableName)) {
            throw new RuntimeException("Table already exists: " + tableName);
        }

        TableMetadata metadata = new TableMetadata(tableName, schema);
        metadata.saveSchema();

        String dataFileName = tableName + ".data";
        File file = new File(dataFileName);

        try {
            if (!file.exists()){
                file.createNewFile();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create data file for table: " + tableName);
        }

        Table table = new Table(dataFileName);
        tables.put(tableName, table);
    }
}
