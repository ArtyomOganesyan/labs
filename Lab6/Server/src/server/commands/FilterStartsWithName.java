package server.commands;

import data.Route;
import data.Routes;
import exceptions.NoArgumentException;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.ResponseOutputer;

import java.util.Map;

public class FilterStartsWithName extends Command{

    public FilterStartsWithName(){
        commandName = "filter_starts_with_name";
        description = "Returns elements with substrings at the beginning";
        help = "name";
    }

    public Response execute(Routes collection, String[] args) throws NoArgumentException {
        try {
            if (args.length < 1) {
                throw new NoArgumentException();
            }
            String out = "";
            for (Map.Entry entry: collection.getHashOfRoutes().entrySet()) {
                Route route = collection.getHashOfRoutes().get(entry.getKey());
                if (route.getName().startsWith(args[0]))
                    out += route.toString();
            }
            ResponseOutputer.appendln(out);
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (NoArgumentException e) {
            ResponseOutputer.appendln("Using '" + commandName  + "'" + help);
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
