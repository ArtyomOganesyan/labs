package commands;

import data.Route;
import data.Routes;
import reader.NoArgumentException;

import java.util.Map;

public class FilterStartsWithName extends Command{

    public FilterStartsWithName(){
        commandName = "filter_starts_with_name";
        description = "Returns elements with substrings at the beginning";
        help = "name";
    }

    public void execute(Routes collection, String[] args) throws NoArgumentException {
        if (args.length < 1) {
            throw new NoArgumentException();
        }
        String out = "";
        for (Map.Entry entry: collection.getHashOfRoutes().entrySet()) {
            Route route = collection.getHashOfRoutes().get(entry.getKey());
            if (route.getName().startsWith(args[0]))
                out += route.toString();
        }
        System.out.println(out);
    }
}
