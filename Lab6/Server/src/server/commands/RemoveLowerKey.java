package server.commands;

import data.Routes;
import exceptions.NoArgumentException;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.ResponseOutputer;

import java.util.Map;

public class RemoveLowerKey extends Command{

    public RemoveLowerKey() {
        commandName = "remove_lower_key";
        description = "Deletes elements those id is less than the given one";
        help = "id (integer)";
    }

    @Override
    public Response execute(Routes collection, String[] args) throws NoArgumentException  {
        try {
            if (args.length < 1) {
                throw new NoArgumentException();
            }
            for (Map.Entry entry: collection.getHashOfRoutes().entrySet()) {
                int ikey = (int) entry.getKey();
                if (ikey < Integer.valueOf(args[0])){
                    collection.getHashOfRoutes().remove(ikey);
                }
            }
            ResponseOutputer.appendln("Elements successfully removed!");
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (NoArgumentException e) {
            ResponseOutputer.appendln("Using '" + commandName  + "' " + help);
        } catch (NumberFormatException e) {
            ResponseOutputer.appendln("Invalid number format");
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
