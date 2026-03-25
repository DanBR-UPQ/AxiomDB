package execution;

import sql.SQLCommand;

public class Executor {
    private final TableManager tableManager;

    public Executor(TableManager tableManager){
        if (tableManager == null) {
            throw new IllegalArgumentException("TableManager cannot be null");
        }
        this.tableManager = tableManager;
    }

    public Result execute(SQLCommand command){
        if (command == null) {
            throw new IllegalArgumentException("SQLCommand cannot be null");
        }

        return command.execute(tableManager);
    }
}
