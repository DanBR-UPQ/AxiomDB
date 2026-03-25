package schema;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Schema { 
    private final List<Column> columns;

    public Schema(List<Column> columns){
        
        if (columns == null){
            throw new IllegalArgumentException("Schema cannot be null");
        }
        if (columns.isEmpty()){
            throw new IllegalArgumentException("Schema cannot be empty");
        }
        
        Set<String> names = new HashSet<>();

        for (Column column : columns) {

            if (column == null) {
                throw new IllegalArgumentException("Column cannot be null");
            }

            String name = column.getName();

            if (names.contains(name)) {
                throw new IllegalArgumentException("Duplicate column name: " + name);
            }

            names.add(name);
        }
        
        this.columns = List.copyOf(columns);
    }


    public int getRecordSize(){ // size in bytes
        int recordSize = 0;

        for (Column column : columns){
            recordSize += column.getLength();
        }

        return recordSize;
    }

    public int getColumnOffset(int index){
        if (index < 0 || index >= columns.size()){
            throw new IllegalArgumentException("Invalid column index");
        }

        int offset = 0;

        for (int i = 0; i < index; i++){
            offset += columns.get(i).getLength();
        }

        return offset;
    }


    public int getColumnIndex(String name){

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be empty");
        }

        for (int i = 0; i < columns.size(); i++){

            if (columns.get(i).getName().equals(name)){
                return i;
            }
        }

        throw new IllegalArgumentException("Column name was not found: " + name);
    }

    public Column getColumn(String name){
        int index = getColumnIndex(name);
        return columns.get(index);     
    }

    public List<Column> getColumns() { return columns; }

    public Column getColumn(int index) { return columns.get(index); }

    public int getColumnCount() { return columns.size(); }
}
