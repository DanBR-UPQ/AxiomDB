package sql;

import execution.Result;
import execution.Table;
import execution.TableManager;
import execution.TableMetadata;
import java.util.List;
import schema.Schema;
import serialization.RecordSerializer;

public class InsertCommand implements SQLCommand {
    private final String tableName;
    private final List<Object> values;

    public InsertCommand(String tableName, List<Object> values) {
        if (tableName == null || tableName.isBlank()) {
            throw new IllegalArgumentException("Table name cannot be empty");
        }
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Values cannot be empty");
        }

        this.tableName = tableName;
        this.values = List.copyOf(values);
    }

    @Override
    public Result execute(TableManager tableManager) {
        Table table = tableManager.getTable(tableName);

        TableMetadata metadata = TableMetadata.load(tableName);
        Schema schema = metadata.getSchema();

        RecordSerializer serializer = new RecordSerializer(schema);
        byte[] record = serializer.serialize(values);

        table.insert(record);
        return Result.withMessage("OK");
    }


    public String getTableName() { return tableName; }
    public List<Object> getValues() { return values; }
}
