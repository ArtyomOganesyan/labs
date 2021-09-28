package server.commands;

import exceptions.NoArgumentException;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.CollectionFileManager;
import server.utility.CollectionManager;
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
    public Response execute(CollectionManager collection, String[] args) throws NoArgumentException {

        try {
            //System.out.println(collection.getListOfRoutes().toString());
            System.out.println(collection.getHashOfRoutes().toString());
            CollectionFileManager.generateXML(collection.getRoutes());
            ResponseOutputer.appendln("Collection successfully saved!");
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (IOException e) {
            ResponseOutputer.appenderror("Access denied or invalid path, IOException");
            e.printStackTrace();
        } catch (JAXBException e) {
            ResponseOutputer.appenderror("Access denied or invalid path");
            e.printStackTrace();
        }

        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
