package server.commands;

import exceptions.InvalidInputException;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class ServerExit extends Command{

    public ServerExit(){
        commandName = "exit";
        description = "Closes the program";
        help = "";
    }

    @Override
    public Response execute(CollectionManager collection, String[] args) throws InvalidInputException {
        return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
    }
}
