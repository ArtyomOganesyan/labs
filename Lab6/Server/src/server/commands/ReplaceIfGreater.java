package server.commands;

import data.Routes;
import exceptions.NoArgumentException;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.ResponseOutputer;

public class ReplaceIfGreater extends Command{

    public ReplaceIfGreater() {
        commandName = "replace_if_greater";
        description = "Replaces element by id if new id is bigger";
        help = "id (integer) {element}";
    }

    @Override
    public Response execute(Routes collection, String[] args) throws NoArgumentException {
        try {
            if (args.length < 1) {
                throw new NoArgumentException();
            }
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (NoArgumentException e) {
            ResponseOutputer.appendln("Using '" + commandName  + "' " + help);
        } catch (NumberFormatException e) {
            ResponseOutputer.appendln("Invalid number format");
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
