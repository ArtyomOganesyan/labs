package client.utility;

import client.ClientApp;
import data.Route;
import exceptions.CommandUsageException;
import exceptions.IncorrectInputInScriptException;
import exceptions.InvalidInputException;
import interaction.Request;
import interaction.Response;
import interaction.ResponseCode;
import utility.Outputer;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class UserHandler {
    private final int maxRewriteAttempts = 1;

    private Scanner userScanner;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();

    public UserHandler(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    public Request handle(Response serverResponse) {
        String userInput;
        String [] userCommand = new String[0];
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (fileMode() && (serverResponse.getResponseCode() == ResponseCode.ERROR ||
                            serverResponse.getResponseCode() == ResponseCode.SERVER_EXIT))
                        throw new IncorrectInputInScriptException();
                    while (fileMode() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        Outputer.println("Возвращаюсь к скрипту '" + scriptStack.pop().getName() + "'...");
                    }
                    if (fileMode()) {
                        userInput = userScanner.nextLine();
                        if (!userInput.isEmpty()) {
                            Outputer.print(ClientApp.PS1);
                            Outputer.println("userInput");
                        }
                    } else {
                        Outputer.print(ClientApp.PS1);
                        userInput = userScanner.nextLine();
                    }
                    userCommand = userInput.trim().split("\\s+");
                } catch (NoSuchElementException | IllegalStateException e) {
                    Outputer.println();
                    Outputer.printerr("Произошла ошибка при вводе команды!");
                    userInput = "";
                    rewriteAttempts++;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        Outputer.printerr("Превышено количество попыток ввода!");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userInput); // FIXME: 21.06.2021
            } while (processingCode == ProcessingCode.ERROR && !fileMode() || userInput.isEmpty());

            if (fileMode() && (processingCode == ProcessingCode.ERROR || serverResponse.getResponseCode() == ResponseCode.ERROR))
                throw new IncorrectInputInScriptException();
            switch (processingCode) {
                case OBJECT:
                    RouteAsker routeAsker = new RouteAsker(userScanner);
                    Route route = routeAsker.askForRoute(serverResponse.getCollection()); // FIXME: 26.06.2021

                    Integer key = Integer.valueOf(userCommand[1]);
                    if (!serverResponse.getCollection().getHashOfRoutes().containsKey(key)) {
                        serverResponse.getCollection().getHashOfRoutes().put(Integer.valueOf(key), route);
                        serverResponse.getCollection().getListOfRoutes().add(route);
                        Outputer.println("New route successfully added to collection!");
                    } else {
                        throw new InvalidInputException();
                    }
                    return new Request(serverResponse.getCollection(), userInput, route);
                case REPLACE_OBJECT:
                    RouteAsker routeAsker1 = new RouteAsker(userScanner);

                    Integer oldKey = Integer.valueOf(userCommand[1]);
                    if (serverResponse.getCollection().getHashOfRoutes().containsKey(oldKey)) {
                        Route route1 = routeAsker1.askForRoute(serverResponse.getCollection());
                        if (route1.getId() > oldKey){
                            serverResponse.getCollection().getHashOfRoutes().replace(oldKey, route1);
                        }
                    }
                case UPDATE_OBJECT:
                    RouteAsker routeAsker2 = new RouteAsker(userScanner);

                    Route route2 = routeAsker2.askForRoute(serverResponse.getCollection());
                    Integer key1 = Integer.valueOf(userCommand[1]);
                    if (serverResponse.getCollection().getHashOfRoutes().containsKey(key1)) {
                        serverResponse.getCollection().getHashOfRoutes().replace(key1, route2);
                        Outputer.println("Element is updated");
                    } else {
                        throw new InvalidInputException();
                    }
                }

        } catch (IncorrectInputInScriptException e) {
            Outputer.printerr("Выполнение скрипта прервано!");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scriptStack.clear();
            return new Request(serverResponse.getCollection());
        }
        return new Request(serverResponse.getCollection(), userInput);
    }

    private ProcessingCode processCommand(String userInput) {
        String [] userCommand = (userInput.trim() + " ").split(" ", 2);
        userCommand[1] = userCommand[1].trim();
        String command = userCommand[0];
        String commandArgument = userCommand[1];
        try {
            switch (command) {
                case "":
                    return ProcessingCode.ERROR;
                case "help":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "info":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "show":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "insert":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID> {element}");
                    return ProcessingCode.OBJECT;
                case "update":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID> {element}");
                    return ProcessingCode.UPDATE_OBJECT;
                case "remove_key":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID>");
                    break;
                case "clear":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "save":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "execute_script":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                case "exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "replace_if_greater":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID> {element}");
                    return ProcessingCode.REPLACE_OBJECT;
                case "remove_lower_key":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "history":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "print_descending":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "max_by_melee_weapon":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "filter_starts_with_name":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<String>");
                    break;
                case "server_exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                default:
                    Outputer.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                    return ProcessingCode.ERROR;
            }
        } catch (CommandUsageException exception) {
            if (exception.getMessage() != null) command += " " + exception.getMessage();
            Outputer.println("Использование: '" + command + "'");
            return ProcessingCode.ERROR;
        }
        return ProcessingCode.OK;
    }

    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }
}
