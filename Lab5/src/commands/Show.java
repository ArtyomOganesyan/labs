package commands;

import data.Route;
import data.Routes;

import java.util.Map;

public class Show extends Command{

    public Show() {
        commandName = "show";
        description = "Shows all elements of collection";
        help = "";
    }

    public void execute(Routes collection, String[] args){
        String out = "";
        for (Map.Entry entry: collection.getHashOfRoutes().entrySet()) {
            Route route = collection.getHashOfRoutes().get(entry.getKey());
            out += route.toString();
        }
        System.out.println(out);
    }
}
