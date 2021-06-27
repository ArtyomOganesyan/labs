package server.commands;

import data.Routes;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.CommandManager;
import server.utility.ResponseOutputer;

public class History extends Command{

    public History() {
        commandName = "history";
        description = "Shows last 14 server.commands";
        help = "";
    }

    @Override
    public Response execute(Routes collection, String[] args) {
        try {
            for (Command command: CommandManager.getInstance().getHistory(14)) {
                ResponseOutputer.appendln(command.getCommandName());
            }
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (Exception e) {
            ResponseOutputer.appendln("Something went wrong");
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
