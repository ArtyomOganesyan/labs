package commands;
import data.*;
import soundProcessing.Sound;

public class Clear extends Command{

    public Clear(){
        commandName = "clear";
        description = "Deletes all elements from collection";
        help = "";
    }

    @Override
    public void execute (Routes collection, String[] args) {
        collection.getHashOfRoutes().clear();
        System.out.println("Done!");
        Sound.playDone();
    }
}
