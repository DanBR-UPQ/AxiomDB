public class Column {
    private final String name;
    private final DataType type;
    private final int length;

    public Column(String name, DataType type, int length){

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Column name can't be empty");
        }
        if (type == DataType.INT){
            if (length != 4) { throw new IllegalArgumentException("Int size can't be different from 4"); }
        }
        if (type == DataType.STRING){
            if (length <= 0) { throw new IllegalArgumentException("String size can't be lower or equal than 0"); }
        }

        this.name = name;
        this.type = type;
        this.length = length;
    }


    public String getName() { return name; }

    public DataType getType() { return type; }

    public int getLength() { return length; }
    public int getSize() { return length; }

}