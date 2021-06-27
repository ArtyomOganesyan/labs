package commands;

import data.Routes;
import reader.InvalidInputException;
import reader.NoArgumentException;
import soundProcessing.Sound;

public class RemoveKey extends Command{

    public RemoveKey() {
        commandName = "remove_key";
        description = "Deletes element by id";
        help = "id (integer)";
    }

    @Override
    public void execute(Routes collection, String[] args) throws InvalidInputException, NoArgumentException {
        if (args.length < 1) {
            throw new NoArgumentException();
        }
        Integer key = Integer.valueOf(args[0]);
        if (collection.getHashOfRoutes().containsKey(key)){
            collection.getHashOfRoutes().remove(key);
            System.out.println("Done!");
            Sound.playDone();
        } else {
            // FIXME: 31.03.2021
            throw new InvalidInputException();
        }
    }
}
