package commands;

import data.Routes;

public class History extends Command{

    public History() {
        commandName = "history";
        description = "Shows last 14 commands";
        help = "";
    }

    @Override
    public void execute(Routes collection, String[] args) {
        for (Command command: CommandManager.getInstance().getHistory(14)) {
            System.out.println(command.getCommandName());
        }
    }
}
