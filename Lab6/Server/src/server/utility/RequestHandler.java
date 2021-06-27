package server.utility;

import data.Routes;
import interaction.Request;
import interaction.Response;

public class RequestHandler {
    CommandManager commandManager;
    Routes collectionManager;

    public RequestHandler(CommandManager commandManager, Routes collectionManager) {
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    public Response handle(Request request) {
        Response response = commandManager.executeCommand(collectionManager, request.getInputLine());
        return response;
    }
/*
    public Response handle(Request request, Request request1){
        Code code = commandManager.executeCommand(collectionManager, request.getInputLine(), request1);
        Response response = new Response(collectionManager, code, request.getInputLine());
        return response;
    }
*/
}
