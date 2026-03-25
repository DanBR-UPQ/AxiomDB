package sql;

public class CreateTableCommand implements SQLCommand {
    private final String tableName;
    private final Schema schema;

    public CreateTableCommand(String tableName, Schema schema){
        if (tableName == null || tableName.isBlank()){ 
            throw new IllegalArgumentException("Table name cannot be empty");
         }
        if (schema == null){
            throw new IllegalArgumentException("Schema cannot be empty");
        }
        this.tableName = tableName;
        this.schema = schema;
    }

    @Override
    public Result execute(TableManager tableManager){
        tableManager.createTable(tableName, schema);
        return null;
    }

    public String getTableName() { return tableName; }
    public Schema getSchema() { return schema; }
}
