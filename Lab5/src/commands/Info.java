package commands;
import data.*;

public class Info extends Command{

    public Info() {
        commandName = "info";
        description = "Returns information about collection";
        help = "";
    }

    @Override
    public void execute (Routes collection, String[] args) {
        System.out.println("\nInformation: \t\t" + collection.getHashOfRoutes().getClass().toString() +
                "\nCreation Date: \t\t" + collection.getCreationDateTime() +
                "\nSize of collection: " + collection.getHashOfRoutes().size());
    }
}