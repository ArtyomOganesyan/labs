package server.commands;

import exceptions.InvalidInputException;
import interaction.Response;
import server.utility.CollectionManager;

public abstract class Command {

    protected String commandName;
    protected String description;
    protected String help;

    public abstract Response execute(CollectionManager collection, String[] args) throws InvalidInputException;



    public String getCommandName(){
        return commandName;
    }

    public String getDescription(){
        return description;
    }

    public String getHelp() {
        return help;
    }
}
