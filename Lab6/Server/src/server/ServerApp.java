package server;

import data.Routes;
import interaction.Response;
import server.utility.CollectionFileManager;
import server.utility.CollectionManager;
import server.utility.CommandManager;
import server.utility.RequestHandler;

import java.util.Scanner;

public class ServerApp {
    public static final int PORT = 2631;
    public static final int CONNECTION_TIMEOUT = 120 * 1000;
    public static final String ENV_VARIABLE = "USER";

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        CollectionFileManager collectionFileManager = new CollectionFileManager(ENV_VARIABLE);
        Routes routes = collectionFileManager.generateObject();
        CollectionManager collectionManager = new CollectionManager(routes.getHashOfRoutes());

        RequestHandler requestHandler = new RequestHandler(CommandManager.getInstance(), collectionManager);

        Thread thread = new Thread() {
            @Override
            public void run() {
                String command = "";
                do {
                    if (isInterrupted()) break;
                    command = scanner.nextLine();
                    if (command.trim().equals("save")) {
                        System.out.println(command);
                        Response response = CommandManager.getInstance().executeCommand(collectionManager, command);
                        System.out.println(response.getResponseBody());
                    }
                } while (!command.trim().equals("exit"));
                System.exit(0);
            }
        };
        thread.start();

        Server server = new Server(PORT, CONNECTION_TIMEOUT, requestHandler);
        server.run();
    }
}
