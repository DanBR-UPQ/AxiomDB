package sql;

import java.util.ArrayList;
import java.util.List;
import schema.Column;
import schema.DataType;
import schema.Schema;

public class SQLParser {
    

    public SQLCommand parse(String query){
        if (query == null || query.isBlank()){
            throw new IllegalArgumentException("Query cannot be empty");
        }

        query = query.trim();

        if (query.endsWith(";")) {
            query = query.substring(0, query.length() - 1);
        }   

        String upper = query.toUpperCase();

        if (upper.startsWith("CREATE TABLE")){
            return parseCreateTable(query);
        } else if (upper.startsWith("INSERT INTO")){
            return parseInsert(query);
        } else if (upper.startsWith("SELECT")){
            return parseSelect(query);
        } else {
            throw new IllegalArgumentException("Unknown command");
        }
    }

    // --------------------- ACTUAL COMMANDS --------------------- 
    private SQLCommand parseCreateTable(String query){
        String rest = query.substring("CREATE TABLE".length()).trim();

        int openParen = rest.indexOf("(");
        int closeParen = rest.lastIndexOf(")");

        if (openParen == -1 || closeParen == -1){
            throw new IllegalArgumentException("Invalid CREATE TABLE syntax, missing parenthesis");
        }

        String tableName = rest.substring(0, openParen).trim();
        String columnsPart = rest.substring(openParen + 1, closeParen).trim();

        String[] columnDefs = columnsPart.split(",");
        List<Column> columns = new ArrayList<>();

        for (String colDef : columnDefs){
            colDef = colDef.trim();

            String[] parts = colDef.split(" ");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid column definition: " + colDef);
            }

            String colName = parts[0];
            String typePart = parts[1].toUpperCase();

            if (typePart.startsWith("INT")){
                columns.add(new Column(colName, DataType.INT, 4));

            } else if (typePart.startsWith("STRING")) {

                int start = typePart.indexOf("(");
                int end = typePart.indexOf(")");

                if (start == -1 || end == -1) {
                    throw new IllegalArgumentException("Invalid STRING definition: " + colDef);
                }

                int length = Integer.parseInt(typePart.substring(start + 1, end));
                columns.add(new Column(colName, DataType.STRING, length));

            } else {
                throw new IllegalArgumentException("Unknown type: " + typePart);
            }
        }
        Schema schema = new Schema(columns);
        return new CreateTableCommand(tableName, schema);
    }


    private SQLCommand parseInsert(String query){
        String rest = query.substring("INSERT INTO".length()).trim();

        int valuesIndex = rest.toUpperCase().indexOf("VALUES");
        if (valuesIndex == -1) {
            throw new IllegalArgumentException("Invalid INSERT syntax");
        }

        String tableName = rest.substring(0, valuesIndex).trim();
        String valuesPart = rest.substring(valuesIndex + "VALUES".length()).trim();

        int openParen = valuesPart.indexOf("(");
        int closeParen = valuesPart.lastIndexOf(")");

        if (openParen == -1 || closeParen == -1) {
            throw new IllegalArgumentException("Invalid VALUES syntax, missing parenthesis");
        }

        String inside = valuesPart.substring(openParen + 1, closeParen);
        String[] rawValues = inside.split(",");

        List<Object> values = new ArrayList<>();

        for (String raw : rawValues) {
            raw = raw.trim();

            
            if (raw.startsWith("\"") && raw.endsWith("\"")) {
                String str = raw.substring(1, raw.length() - 1);
                values.add(str);

            } else {
                try {
                    int val = Integer.parseInt(raw);
                    values.add(val);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid value: " + raw);
                }
            }
        }

        return new InsertCommand(tableName, values);
    }


    private SQLCommand parseSelect(String query) {
        String upper = query.toUpperCase();
        
        if (!upper.startsWith("SELECT * FROM")) {
            throw new IllegalArgumentException("Only SELECT * FROM is supported");
        }

        String rest = query.substring("SELECT * FROM".length()).trim();

        if (rest.isEmpty()) {
            throw new IllegalArgumentException("Missing table name in SELECT");
        }

        return new SelectCommand(rest);
    }
    
}
