package commands;

import data.Routes;
import reader.NoArgumentException;
import soundProcessing.Sound;

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
    public void execute(Routes collection, String[] args) throws NoArgumentException {
        if (args.length < 1) {
            throw new NoArgumentException();
        }
        String scriptPath = args[0];
        Path pathToScript = Paths.get(scriptPath);
        System.out.println("Executing script from " + pathToScript.getFileName() + "...");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToScript.toFile()));
            String line = reader.readLine();
            while (line != null) {
                System.out.println("Read string: " + line);
                String [] list = line.trim().split("\\s+");
                if (!list[0].equals("execute_script")) {
                    CommandManager.getInstance().executeCommand(collection, line);
                } else {
                    System.err.println("You can't call 'execute_script' from script");
                    Sound.playError();
                    break;
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            Sound.playError();
        } catch (Exception e) {
            System.err.println("Error during script execution");
            e.printStackTrace();
            Sound.playError();
        }
    }
}
