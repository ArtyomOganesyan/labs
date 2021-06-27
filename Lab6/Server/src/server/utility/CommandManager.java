package server.utility;

import data.Routes;
import exceptions.NoSuchCommandException;
import interaction.Request;
import interaction.Response;
import server.commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandManager {

    private static CommandManager commandManager;

    public static CommandManager getInstance() {
        if (commandManager == null) {
            commandManager = new CommandManager();
        }
        return commandManager;
    }

    public final HashMap<String, Command> commands = new HashMap<>();
    private static ArrayList<Command> history = new ArrayList<>();

    private void addCommand(Command command) {
        commands.put(command.getCommandName(), command);
    }

    public CommandManager() {
        addCommand(new Clear());
        addCommand(new ExecuteScript());
        addCommand(new ServerExit());
        addCommand(new FilterStartsWithName());
        addCommand(new Help());
        addCommand(new History());
        addCommand(new Info());
        addCommand(new Insert());
        addCommand(new PrintDescending());
        addCommand(new RemoveKey());
        addCommand(new RemoveLowerKey());
        addCommand(new ReplaceIfGreater());
        //addCommand(new Save());
        addCommand(new Show());
        addCommand(new Update());
    }

    public Command getCommand(String s) throws NoSuchCommandException {
        if (!commands.containsKey(s)) {
            throw new NoSuchCommandException();
        } else
            return commands.get(s);
    }

    public Response executeCommand(Routes collection, String line) throws NoSuchCommandException {
        String[] parsedString = parseCommand(line);
        if (parsedString.length == 0)
            throw new NoSuchCommandException();
        Command command = getCommand(parsedString[0]);
        String[] args = Arrays.copyOfRange(parsedString, 1, parsedString.length);

        Response response = command.execute(collection, args);
        history.add(command);
        return response;
    }

    public String[] parseCommand(String line) {
        String[] parsed = line.trim().split("\\s+");
        return parsed;
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public List<Command> getHistory(int cnt) {
            ArrayList<Command> list = new ArrayList<>();
            if (cnt > history.size()) cnt = history.size();
            for (int i = history.size() - cnt; i < history.size(); i++) {
                list.add(history.get(i));
            }
            return list;
    }
}
