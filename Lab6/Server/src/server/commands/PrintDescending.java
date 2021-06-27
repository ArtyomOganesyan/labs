package server.commands;

import data.Route;
import data.Routes;
import interaction.Response;
import interaction.ResponseCode;
import server.utility.ResponseOutputer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PrintDescending extends Command{

    public PrintDescending() {
        commandName = "print_descending";
        description = "Prints elements in reversed order by distance";
        help = "";
    }

    @Override
    public Response execute(Routes collection, String[] args) {
        try {
            List<Route> list = new ArrayList<>(collection.getHashOfRoutes().values());
            list.sort(Comparator.reverseOrder());
            String out = "";

            for (Route r : list){
                out += r.toString();
            }
            ResponseOutputer.appendln(out);
            return new Response(collection, ResponseCode.OK, ResponseOutputer.getAndClear());
        } catch (Exception e) {
            ResponseOutputer.appendln("Something went wrong");
        }
        return new Response(collection, ResponseCode.ERROR, ResponseOutputer.getAndClear());
    }
}
