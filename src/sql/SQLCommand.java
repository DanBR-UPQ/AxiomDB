package sql;

import execution.Result;
import execution.TableManager;

public interface SQLCommand {
    Result execute(TableManager tablemanager);
}
