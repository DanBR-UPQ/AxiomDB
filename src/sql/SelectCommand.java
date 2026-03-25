package sql;

import execution.Result;
import execution.Table;
import execution.TableManager;
import execution.TableMetadata;
import java.util.ArrayList;
import java.util.List;
import schema.Schema;
import serialization.RecordSerializer;
import storage.DiskManager;
import storage.Page;
import storage.SlottedPage;

public class SelectCommand implements SQLCommand {
    private final String tableName;

    public SelectCommand(String tableName) {

        if (tableName == null || tableName.isBlank()) {
            throw new IllegalArgumentException("Table name cannot be empty");
        }
        this.tableName = tableName;
    }

    @Override
    public Result execute(TableManager tableManager) {
        Table table = tableManager.getTable(tableName);

        TableMetadata metadata = TableMetadata.load(tableName);
        Schema schema = metadata.getSchema();

        RecordSerializer serializer = new RecordSerializer(schema);
        List<List<Object>> rows = new ArrayList<>();

        DiskManager dm = table.getDiskManager();

        for (int i = 0; i < dm.getPageCount(); i++){
            Page page = dm.loadPage(i);
            SlottedPage sp = new SlottedPage(page);

            for (int j = 0; j < sp.getSlotCount(); j++){
                byte[] record = sp.readRecord(j);
                List<Object> values = serializer.deserialize(record);
                rows.add(values);
            }
        }

        return Result.withRows(rows);
    }

    public String getTableName() { return tableName; }
}