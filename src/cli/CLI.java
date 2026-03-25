package cli;

import execution.Executor;
import execution.Result;
import execution.TableManager;
import java.util.List;
import java.util.Scanner;
import sql.SQLCommand;
import sql.SQLParser;

public class CLI {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        SQLParser parser = new SQLParser();
        TableManager tableManager = new TableManager();
        Executor executor = new Executor(tableManager);

        System.out.println("AxiomDB CLI started. Type 'exit;' to quit.");

        while (true) {
            System.out.print("> ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit;")) {
                break;
            }

            try {
                SQLCommand command = parser.parse(input);
                Result result = executor.execute(command);

                if (result.hasMessage()) {
                    System.out.println(result.getMessage());
                } else if (result.hasRows()) {

                    for (List<Object> row : result.getRows()) {
                        for (Object value : row) {
                            System.out.print(value + "\t");
                        }
                        System.out.println();
                    }

                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
