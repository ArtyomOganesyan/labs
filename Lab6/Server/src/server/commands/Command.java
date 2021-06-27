package server.commands;

import data.Routes;
import exceptions.InvalidInputException;
import interaction.Response;

public abstract class Command {

    protected String commandName;
    protected String description;
    protected String help;

    public abstract Response execute(Routes collection, String[] args) throws InvalidInputException;



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
