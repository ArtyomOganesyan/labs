package server.utility;

import interaction.Request;
import interaction.Response;

public class RequestHandler {
    CommandManager commandManager;
    static CollectionManager collectionManager;

    public RequestHandler(CommandManager commandManager, CollectionManager collectionManager) {
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    public Response handle(Request request) {
        Response response = commandManager.executeCommand(collectionManager, request.getInputLine());
        return response;
    }
}
