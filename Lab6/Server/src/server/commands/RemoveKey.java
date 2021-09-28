package server.commands;

import exceptions.InvalidInputException;
import exceptions.NoArgumentException;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class RemoveKey extends Command{

    public RemoveKey() {
        commandName = "remove_key";
        description = "Deletes element by id";
        help = "id (integer)";
    }

    @Override
    public Response execute(CollectionManager collection, String[] args) throws InvalidInputException, NoArgumentException {
        try {
            if (args.length < 1) {
                throw new NoArgumentException();
            }
            Integer key = Integer.valueOf(args[0]);
            if (collection.getHashOfRoutes().containsKey(key)){
                collection.getHashOfRoutes().remove(key);
                ResponseOutputer.appendln("Done!");
            } else {
                throw new InvalidInputException();
            }
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (NoArgumentException e) {
            ResponseOutputer.appendln("Using '" + commandName  + "' " + getHelp());
        } catch (NumberFormatException e) {
            ResponseOutputer.appendln("Invalid number format");
        } catch (InvalidInputException e) {
            ResponseOutputer.appendln("Invalid input");
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
