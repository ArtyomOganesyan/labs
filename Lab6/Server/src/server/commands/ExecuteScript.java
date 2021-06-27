package server.commands;

import data.Routes;
import exceptions.NoArgumentException;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.CommandManager;
import server.utility.ResponseOutputer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExecuteScript extends Command{

    public ExecuteScript() {
        commandName = "execute_script";
        description = "Reads and executes script from given path";
        help = "file_name";
    }

    @Override
    public Response execute(Routes collection, String[] args) throws NoArgumentException {
        if (args.length < 1) {
            throw new NoArgumentException();
        }
        String scriptPath = args[0];
        Path pathToScript = Paths.get(scriptPath);
        ResponseOutputer.appendln("Executing script from " + pathToScript.getFileName() + "...");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToScript.toFile()));
            String line = reader.readLine();
            while (line != null) {
                System.out.println("Read string: " + line);
                String[] list = line.trim().split("\\s+");
                if (!list[0].equals("execute_script")) {
                    CommandManager.getInstance().executeCommand(collection, line);
                } else {
                    ResponseOutputer.appenderror("You can't call 'execute_script' from script");
                    break;
                }
                line = reader.readLine();
            }
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (NoArgumentException e) {
            ResponseOutputer.appendln("Using '" + commandName  + "' " + help);
        } catch (FileNotFoundException e) {
            ResponseOutputer.appendln("File not found");
        } catch (Exception e) {
            ResponseOutputer.appendln("Something went wrong");
            //e.printStackTrace();
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
