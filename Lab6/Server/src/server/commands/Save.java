package server.commands;

import data.Routes;
import exceptions.NoArgumentException;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.CollectionFileManager;
import server.utility.ResponseOutputer;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Save extends Command {

    public Save() {
        commandName = "save";
        description = "Save collection in XML to given path";
        help = "path";
    }

    @Override
    public Response execute(Routes collection, String[] args) throws NoArgumentException {
        try {
            if (args.length < 1) {
                throw new NoArgumentException();
            }
            try {
                CollectionFileManager.generateXML(collection, args[0]);
                ResponseOutputer.appendln("Collection successfully saved!");
                return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
            } catch (IOException e) {
                ResponseOutputer.appenderror("Access denied or invalid path, IOException");
                e.printStackTrace();
            } catch (JAXBException e) {
                ResponseOutputer.appenderror("Access denied or invalid path");
                e.printStackTrace();
            }
        } catch (NoArgumentException e) {
            ResponseOutputer.appendln("Using '" + commandName  + "' " + help);
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
