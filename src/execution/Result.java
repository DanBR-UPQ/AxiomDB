package execution;

import java.util.List;

public class Result {
    private final String message;
    private final List<List<Object>> rows;

    private Result(String message, List<List<Object>> rows){
        this.message = message;
        this.rows = rows;
    }

    public static Result withMessage(String message){
        return new Result(message, null);
    }
    public static Result withRows(List<List<Object>> rows){
        return new Result(null, rows);
    }


    public boolean hasMessage() { return message != null; }
    public boolean hasRows() { return rows != null; }

    public String getMessage() { return message; }
    public List<List<Object>> getRows() { return rows; }
}
