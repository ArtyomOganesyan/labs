package commands;

import data.Routes;

public abstract class Command {

    protected String commandName;
    protected String description;
    protected String help;

    public abstract void execute(Routes collection, String[] args);

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
