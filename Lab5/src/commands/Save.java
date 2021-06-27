package commands;

import reader.JAXBWorker;
import data.Routes;
import reader.NoArgumentException;
import soundProcessing.Sound;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Save extends Command {

    public Save() {
        commandName = "save";
        description = "Save collection in XML to given path";
        help = "path";
    }

    @Override
    public void execute(Routes collection, String[] args) throws NoArgumentException {
        if (args.length < 1) {
            throw new NoArgumentException();
        }
        try {
            JAXBWorker.generateXML(collection, args[0]);
            System.out.println("Collection successfully saved!");
            Sound.playDone();
        } catch (IOException e) {
            System.err.println("Access denied or invalid path, IOException");
            e.printStackTrace();
        } catch (JAXBException e) {
            System.err.println("Access denied or invalid path");
            e.printStackTrace();
            Sound.playError();
        }
    }
}
