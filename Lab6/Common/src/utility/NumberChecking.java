package utility;

public class NumberChecking {
    public static boolean check(double s, int min, int max) {
        return ((min < 0 || s > min) && (max < 0 || s <= max));
    }
}
