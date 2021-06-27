package client.utility;

import client.ClientApp;
import data.Coordinates;
import data.Location;
import data.Route;
import data.Routes;
import utility.NumberChecking;
import utility.Outputer;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Set;

public class RouteAsker {
    private Scanner userScanner;
    private boolean fileMode;

    public RouteAsker(Scanner scanner) {
        this.userScanner = scanner;
        fileMode = false;
    }

    /**
     * Sets route asker mode to 'File Mode'.
     */
    public void setFileMode() {
        fileMode = true;
    }

    /**
     * Sets route asker mode to 'User Mode'.
     */
    public void setUserMode() {
        fileMode = false;
    }

    public String request() {
        Outputer.print(ClientApp.PS2);
        return userScanner.nextLine();
    }

    public String request(String msg) {
        Outputer.println(msg);
        return request();
    }

    public String request(String msg, boolean nullable) {
        String response = request(msg);
        if (nullable) {
            return response;
        } else if (response == null) {
            do {
                Outputer.printerr("This field can't be 'null'");
                response = request(msg);
            } while (response == null);
        }
        return response;
    }

    public String requestDouble(String msg, int min, int max) {
        String response;
        try {
            response = request(msg, false);
            if (!NumberChecking.check(Double.parseDouble(response), min, max)) {
                Outputer.printerr("Incorrect input, try again");
                return requestDouble(msg, min, max);
            }
        } catch (NumberFormatException e) {
            Outputer.printerr("Incorrect number, try again");
            return requestDouble(msg, min, max);
        }
        return response;
    }

    public String requestInteger(String msg, int min, int max) {
        String response;
        try {
            response = request(msg, false);
            if (!NumberChecking.check(Integer.parseInt(response), min, max)) {
                Outputer.printerr("Incorrect input, try again");
                return requestInteger(msg, min, max);
            }
        } catch (NumberFormatException e) {
            Outputer.printerr("Incorrect number, try again");
            return requestInteger(msg, min, max);
        }
        return response;
    }

    public Route askForRoute(Routes collection) {
        String name = request("Enter route's name:", false);
        Coordinates coordinates = requestForCoordinates();
        Location from = requestForLocation("from");
        Location to = requestForLocation("to");
        Integer distance = Integer.parseInt(requestInteger("Enter route's distance (distance > 1):", 1, -1));
        Route route = new Route(name, coordinates, from, to, distance);
        route.setId(idGenerator(collection));
        route.setCreationDate(LocalDateTime.now());
        return route;
    }

    public Route askForRoute(Routes collection, String[] args) {
        Route route = askForRoute(collection);
        route.setId(Integer.valueOf(args[0]));
        return route;
    }

    public Coordinates requestForCoordinates() {
        Outputer.printerr("Enter route's coordinates:");
        Long x;
        Double y;
        try {
            x = Long.parseLong(request("Enter X-coordinate:", false));
            y = Double.parseDouble(requestDouble("Enter Y-coordinate (y <= 248):", 0, 248));
        } catch (NumberFormatException e) {
            Outputer.printerr("Incorrect format");
            return requestForCoordinates();
        }
        return new Coordinates(x, y);
    }

    public Location requestForLocation(String variableName) {
        Outputer.println("Enter route's location " + "'" + variableName + "'" + ":");
        String name;
        float x;
        long y;
        Double z;
        try {
            name = request("Enter location's name", false);
            x = Float.parseFloat(request("Enter location's X", false));
            y = Long.parseLong(request("Enter location's Y", false));
            z = Double.parseDouble(request("Enter location's Z", false));
        } catch (NumberFormatException e) {
            Outputer.printerr("Incorrect format");
            return requestForLocation(variableName);
        }
        return new Location(name, x, y, z);
    }

    private static Integer idGenerator(Routes collection) {
        Set<Integer> keys = collection.getHashOfRoutes().keySet();
        Integer max = -1;
        for (Integer i: keys) {
            if (i > max)
                max = i;
        }
        return max + 1;
    }
}
