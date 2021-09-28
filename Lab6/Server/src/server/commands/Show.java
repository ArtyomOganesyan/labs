package server.commands;

import interaction.Response;
import interaction.ResponseCode;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class Show extends Command{

    public Show() {
        commandName = "show";
        description = "Shows all elements of collection";
        help = "";
    }

    public Response execute(CollectionManager collection, String[] args){
        try {
            String out = "";
            out += collection.getHashOfRoutes().toString();
            /*for (Map.Entry entry: collection.getHashOfRoutes().entrySet()) {
                Route route = collection.getHashOfRoutes().get(entry.getKey());
                out += route.toString();
            }*/
            ResponseOutputer.appendln(out);
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (Exception e) {
            ResponseOutputer.appendln("Something went wrong");
            e.printStackTrace();
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
