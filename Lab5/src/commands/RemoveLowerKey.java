package commands;

import data.*;
import reader.NoArgumentException;
import soundProcessing.Sound;

import java.util.Map;

public class RemoveLowerKey extends Command{

    public RemoveLowerKey() {
        commandName = "remove_lower_key";
        description = "Deletes elements those id is less than the given one";
        help = "id (integer)";
    }

    @Override
    public void execute(Routes collection, String[] args) throws NoArgumentException  {
        if (args.length < 1) {
            throw new NoArgumentException();
        }
        for (Map.Entry entry: collection.getHashOfRoutes().entrySet()) {
            int ikey = (int) entry.getKey();
            if (ikey < Integer.valueOf(args[0])){
                collection.getHashOfRoutes().remove(ikey);
            }
        }
        System.out.println("Done!");
        Sound.playDone();
    }
}
