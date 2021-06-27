package commands;

import data.Route;
import data.Routes;
import reader.InvalidInputException;
import reader.NoArgumentException;
import reader.Reader;
import soundProcessing.Sound;

public class Insert extends Command{

    public Insert() {
        commandName = "insert";
        description = "Inserts a new element with given key into collection";
        help = "id (integer) {element}";
    }

    @Override
    public void execute(Routes collection, String[] args) throws InvalidInputException, NoArgumentException {
        if (args.length < 1) {
            throw new NoArgumentException();
        }
        Integer key = Integer.valueOf(args[0]);
        if (!collection.getHashOfRoutes().containsKey(key)) {
            Route route = Reader.askForRoute(collection, args);
            collection.getHashOfRoutes().put(Integer.valueOf(key), route);
            collection.getListOfRoutes().add(route);
            System.out.println("Done!");
            Sound.playDone();
        } else {
            // FIXME: 31.03.2021
            throw new InvalidInputException();
        }
    }
}
