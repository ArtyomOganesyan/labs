package server.commands;

import data.Routes;
import exceptions.InvalidInputException;
import exceptions.NoArgumentException;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.ResponseOutputer;

public class Insert extends Command{

    public Insert() {
        commandName = "insert";
        description = "Inserts a new element with given key into collection";
        help = "id (integer) {element}";
    }

    @Override
    public Response execute(Routes collection, String[] args) throws InvalidInputException, NoArgumentException {

        try {
            if (args.length < 1) {
                throw new NoArgumentException();
            }
            Integer key = Integer.valueOf(args[0]);
            if (!collection.getHashOfRoutes().containsKey(key)) {
                return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
            } else {
                throw new InvalidInputException();
            }
        } catch (NoArgumentException e) {
            ResponseOutputer.appendln("Using '" + commandName  + "' " + help);
        } catch (NumberFormatException e) {
            ResponseOutputer.appendln("Invalid number format");
        } catch (InvalidInputException e) {
            ResponseOutputer.appendln("Invalid input");
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
