package commands;

import data.Route;
import data.Routes;
import reader.NoArgumentException;
import reader.Reader;
import soundProcessing.Sound;

public class ReplaceIfGreater extends Command{

    public ReplaceIfGreater() {
        commandName = "replace_if_greater";
        description = "Replaces element by id if new id is bigger";
        help = "id (integer) {element}";
    }

    @Override
    public void execute(Routes collection, String[] args) throws NoArgumentException {
        if (args.length < 1) {
            throw new NoArgumentException();
        }
        Integer oldKey = Integer.valueOf(args[0]);
        if (collection.getHashOfRoutes().containsKey(oldKey)) {
            Route route = Reader.askForRoute(collection);
            if (route.getId() > oldKey){
                collection.getHashOfRoutes().replace(oldKey, route);
            }
        }
        System.out.println("Done!");
        Sound.playDone();
    }
}
