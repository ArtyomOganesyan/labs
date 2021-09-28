package server.commands;

import interaction.Response;
import interaction.ResponseCode;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class Info extends Command{

    public Info() {
        commandName = "info";
        description = "Returns information about collection";
        help = "";
    }

    @Override
    public Response execute (CollectionManager collection, String[] args) {
        
        try {
            ResponseOutputer.appendln("\nInformation: \t\t" + collection.getHashOfRoutes().getClass().toString() +
                    "\nCreation Date: \t\t" + collection.getCreationDateTime() +
                    "\nSize of collection: " + collection.getHashOfRoutes().size());
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (Exception e) {
            ResponseOutputer.appendln("Something went wrong");
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}