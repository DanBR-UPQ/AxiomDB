package sql;

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


}
