package server.commands;

import interaction.Response;
import interaction.ResponseCode;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class Clear extends Command{

    public Clear(){
        commandName = "clear";
        description = "Deletes all elements from collection";
        help = "";
    }

    @Override
    public Response execute (CollectionManager collection, String[] args) {
        try {
            collection.getHashOfRoutes().clear();
            //collection.getListOfRoutes().clear();
            ResponseOutputer.appendln("Collection cleared!");
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (Exception e) {
            ResponseOutputer.appendln("Something went wrong");
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
