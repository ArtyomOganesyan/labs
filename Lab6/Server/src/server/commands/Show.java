package server.commands;

import data.Route;
import data.Routes;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.ResponseOutputer;

import java.util.Map;

public class Show extends Command{

    public Show() {
        commandName = "show";
        description = "Shows all elements of collection";
        help = "";
    }

    public Response execute(Routes collection, String[] args){
        try {
            String out = "";
            for (Map.Entry entry: collection.getHashOfRoutes().entrySet()) {
                Route route = collection.getHashOfRoutes().get(entry.getKey());
                out += route.toString();
            }
            ResponseOutputer.appendln(out);
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (Exception e) {
            ResponseOutputer.appendln("Something went wrong");
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
