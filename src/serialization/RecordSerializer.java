package serialization;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import schema.Column;
import schema.DataType;
import schema.Schema;


public class RecordSerializer {
    private Schema schema;

    public RecordSerializer(Schema schema){
        this.schema = schema;
    }
    

    // ------------------  DESERIALIZE   ------------------
    public List<Object> deserialize(byte[] record) {

        if (record == null) {
            throw new IllegalArgumentException("Record cannot be null");
        }

        if (record.length != schema.getRecordSize()) {
            throw new IllegalArgumentException("Invalid record size");
        }

        List<Object> values = new ArrayList<>();

        for (int i = 0; i < schema.getColumnCount(); i++) {

            Column column = schema.getColumn(i);
            int offset = schema.getColumnOffset(i);
    
            if (column.getType() == DataType.INT) {

                ByteBuffer buffer = ByteBuffer.wrap(record, offset, 4);
                int value = buffer.getInt();

                values.add(value);
            } else if (column.getType() == DataType.STRING) {

                int length = column.getSize();

                byte[] stringBytes = new byte[length];
                System.arraycopy(record, offset, stringBytes, 0, length);

                String value = new String(stringBytes).trim(); // trim as we padded with bytes of 0

                values.add(value);
            }
        }

        return values;
    }


    // ------------------  SERIALIZE   ------------------
    public byte[] serialize(List<Object> data){

        if (data == null){
            throw new IllegalArgumentException("Data cannot be empty");
        }

        if (data.size() != schema.getColumnCount()){
            throw new IllegalArgumentException("Data count does not match schema size");
        }

        int recordSize = schema.getRecordSize();
        byte[] record = new byte[recordSize];

        for (int i = 0; i < schema.getColumnCount(); i++) {

            Column column = schema.getColumn(i);
            Object value = data.get(i);
            int offset = schema.getColumnOffset(i);

        
            if (column.getType() == DataType.INT) {

                if (!(value instanceof Integer)) {
                    throw new IllegalArgumentException("Expected INT value for column: " + column.getName());
                }

                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.putInt((Integer) value);

                System.arraycopy(buffer.array(), 0, record, offset, 4);

            } else if (column.getType() == DataType.STRING) {

                if (!(value instanceof String)) {
                    throw new IllegalArgumentException("Expected STRING value for column: " + column.getName());
                }

                int maxLength = column.getSize();

                byte[] stringBytes = ((String) value).getBytes();

                
                byte[] fixedBytes = new byte[maxLength];

                // we truncate if we can't fit the whole string
                int copyLength = Math.min(stringBytes.length, maxLength);
                System.arraycopy(stringBytes, 0, fixedBytes, 0, copyLength);

                // we don't need to pad, remaining bytes are 0

                System.arraycopy(fixedBytes, 0, record, offset, maxLength);
            }
        }

        return record;
    }
}
