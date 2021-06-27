package server.commands;

import data.Routes;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.ResponseOutputer;

public class Clear extends Command{

    public Clear(){
        commandName = "clear";
        description = "Deletes all elements from collection";
        help = "";
    }

    @Override
    public Response execute (Routes collection, String[] args) {
        try {
            collection.getHashOfRoutes().clear();
            ResponseOutputer.appendln("Collection cleared!");
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (Exception e) {
            ResponseOutputer.appendln("Something went wrong");
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
