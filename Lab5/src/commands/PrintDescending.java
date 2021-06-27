package commands;

import data.Route;
import data.Routes;

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
    public void execute(Routes collection, String[] args) {
        List<Route> list = new ArrayList<>(collection.getHashOfRoutes().values());
        list.sort(Comparator.reverseOrder());

        for (Route r : list){
            System.out.println(r.toString());
        }
    }
}
