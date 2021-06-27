package commands;

import data.Routes;

import java.util.Map;

public class Help extends Command{

    public Help() {
        commandName = "help";
        description = "Prints all commands";
        help = "";
    }

    @Override
    public void execute(Routes collection, String[] args) {
        for (Map.Entry entry: CommandManager.getInstance().getCommands().entrySet()) {
            Command now = CommandManager.getInstance().getCommand(entry.getKey().toString());
            System.out.println(now.getCommandName() + " " + now.getHelp() + ": " + now.getDescription());
        }
    }
}
