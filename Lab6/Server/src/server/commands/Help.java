package server.commands;

import interaction.Response;
import interaction.ResponseCode;
import server.utility.CollectionManager;
import server.utility.CommandManager;
import server.utility.ResponseOutputer;

import java.util.Map;

public class Help extends Command{

    public Help() {
        commandName = "help";
        description = "Prints all commands";
        help = "";
    }

    @Override
    public Response execute(CollectionManager collection, String[] args) {
        try {
            for (Map.Entry entry: CommandManager.getInstance().getCommands().entrySet()) {
                Command now = CommandManager.getInstance().getCommand(entry.getKey().toString());
                ResponseOutputer.appendln(now.getCommandName() + " " + now.getHelp() + ": " + now.getDescription());
            }
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (Exception e) {
            ResponseOutputer.appendln("Something went wrong");
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
