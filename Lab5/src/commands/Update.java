package commands;

import data.Route;
import data.Routes;
import reader.InvalidInputException;
import reader.NoArgumentException;
import reader.Reader;
import soundProcessing.Sound;

public class Update extends Command {

    public Update() {
        commandName = "update";
        description = "Updates element by id";
        help = "id (integer) {element}";
    }

    @Override
    public void execute(Routes collection, String[] args) throws InvalidInputException, NoArgumentException {
        if (args.length < 1) {
            throw new NoArgumentException();
        }
        Route route = Reader.askForRoute(collection);
        Integer key = Integer.valueOf(args[0]);
        if (collection.getHashOfRoutes().containsKey(key)){
            collection.getHashOfRoutes().replace(key, route);
            Sound.playDone();
        } else {
            // FIXME: 31.03.2021
            throw new InvalidInputException();
        }
    }
}
