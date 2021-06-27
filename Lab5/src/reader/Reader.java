package reader;

import data.Coordinates;
import data.Location;
import data.Route;
import data.Routes;
import soundProcessing.Sound;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Set;

public class Reader {

    /** Метод вызывающий приветственное сообщение */
    public static void start() {
        String username = System.getProperty("user.name");
        System.out.println("Welcome "+ username + "!");
        Sound.play("beginning.wav");
    }

    public static File askForEnvVar() {
        Scanner start = new Scanner(System.in);
        System.out.println("Enter environment variable:");
        String ass = start.nextLine();

        File file = new File(System.getenv(ass));
        return file;
    }

    public static String requestCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter command > ");
        return scanner.nextLine();
    }

    public static String request() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        return scanner.nextLine();
    }

    public static String request(String msg) {
        System.out.println(msg);
        return request();
    }

    public static String request(String msg, boolean nullable) {
        String response = request(msg);
        if (nullable) {
            return response;
        } else if (response == null) {
            do {
                System.err.println("This field can't be 'null'");
                response = request(msg);
            } while (response == null);
        }
        return response;
    }

    public static String requestDouble(String msg, int min, int max) {
        String response;
        try {
            response = request(msg, false);
            if (!checkNumber(Double.parseDouble(response), min, max)) {
                System.err.println("Incorrect input, try again");
                Sound.playError();
                return requestDouble(msg, min, max);
            }
        } catch (NumberFormatException e) {
            System.err.println("Incorrect number, try again");
            Sound.playError();
            return requestDouble(msg, min, max);
        }
        return response;
    }

    public static String requestInteger(String msg, int min, int max) {
        String response;
        try {
            response = request(msg, false);
            if (!checkNumber(Integer.parseInt(response), min, max)) {
                System.err.println("Incorrect input, try again");
                Sound.playError();
                return requestInteger(msg, min, max);
            }
        } catch (NumberFormatException e) {
            System.err.println("Incorrect number, try again");
            Sound.playError();
            return requestInteger(msg, min, max);
        }
        return response;
    }

    public static boolean checkNumber(double s, int min, int max) {
        return ((min < 0 || s > min) && (max < 0 || s <= max));
    }

    public static Route askForRoute(Routes collection) {
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

    public static Route askForRoute(Routes collection, String[] args) {
        Route route = askForRoute(collection);
        route.setId(Integer.valueOf(args[0]));
        return route;
    }

    public static Coordinates requestForCoordinates() {
        System.out.println("Enter route's coordinates:");
        Long x;
        Double y;
        try {
            x = Long.parseLong(request("Enter X-coordinate:", false));
            y = Double.parseDouble(requestDouble("Enter Y-coordinate (y <= 248):", 0, 248));
        } catch (NumberFormatException e) {
            System.err.println("Incorrect format");
            Sound.playError();
            return requestForCoordinates();
        }
        return new Coordinates(x, y);
    }

    public static Location requestForLocation(String variableName) {
        System.out.println("Enter route's location " + "'" + variableName + "'" + ":");
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
            System.err.println("Incorrect format");
            Sound.playError();
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
