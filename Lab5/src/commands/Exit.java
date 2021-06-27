package commands;

import data.Routes;
import soundProcessing.Sound;

public class Exit extends Command{

    public Exit(){
        commandName = "exit";
        description = "Closes the program";
        help = "";
    }

    @Override
    public void execute(Routes collection, String[] args) {
        System.out.println("Shutting down...");
        Sound.playExit();
        System.exit(0);
    }
}
