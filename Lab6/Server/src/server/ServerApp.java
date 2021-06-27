package server;

import data.Routes;
import server.utility.CollectionFileManager;
import server.utility.CommandManager;
import server.utility.RequestHandler;

public class ServerApp {
    public static final int PORT = 2631;
    public static final int CONNECTION_TIMEOUT = 60 * 1000;
    public static final String ENV_VARIABLE = "USER";

    public static void main(String[] args) {
        CollectionFileManager collectionFileManager = new CollectionFileManager(ENV_VARIABLE);
        Routes collectionManager = collectionFileManager.generateObject();

        RequestHandler requestHandler = new RequestHandler(CommandManager.getInstance(), collectionManager);
        Server server = new Server(PORT, CONNECTION_TIMEOUT, requestHandler);
        server.run();
    }
}
