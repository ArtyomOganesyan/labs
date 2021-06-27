import commands.CommandManager;
import reader.*;
import data.Routes;
import soundProcessing.Sound;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

public class Application {

    public static void main(String[] args) {

        boolean isXMLNotRead = true;

        try {
            Reader.start();
        } catch (SoundException e) {
            System.err.println("Sound doesn't supported on this device");
            System.err.println("Program will run without sounds");
        }

        Routes r = null;
        while (isXMLNotRead) {
            r = null;
            try {
                File env = Reader.askForEnvVar();
                r = JAXBWorker.listToHash(env);
                isXMLNotRead = false;
            } catch (JAXBException | NumberFormatException e) {
                System.err.println("Reading file error, try again");
                Sound.playError();
            } catch (IOException e) {
                System.err.println("File not found, try again");
                Sound.playError();
            } catch (NullPointerException e) {
                System.err.println("Variable doesn't found, try again");
                Sound.playError();
            } catch (NoSuchElementException e) {
                System.err.println("You shouldn't have done it...");
                System.err.println("Program now will be closed...");
                Sound.playEOF();
                System.exit(0);
            }
        }


        while (true) {
            String response = null;
            try {
                response = Reader.requestCommand();
                CommandManager.getInstance().executeCommand(r, response);
            } catch(NoSuchCommandException e) {
                System.err.println("Unknown command, use 'help' to see available commands");
                Sound.playError();
            } catch (InvalidInputException e) {
                System.err.println("Invalid input");
                Sound.playError();
            } catch (NoArgumentException e) {
                System.err.println("This command requires argument");
                Sound.playError();
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format");
                e.printStackTrace();
                Sound.playError();
            } catch (NoSuchElementException e) {
                System.err.println("You shouldn't have done it...");
                System.err.println("Program now will be closed...");
                Sound.playEOF();
                System.exit(0);
            } catch(Exception e) {
                System.err.println("Undefined error");
                Sound.playError();
                e.printStackTrace();
            }
        }
    }
}
