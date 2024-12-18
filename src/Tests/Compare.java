package Tests;

public class Compare {
    public static boolean compareInt(int expected, int actual) {
        return expected == actual;
    }

    public static boolean compareDouble(double expected, double actual) {
        return expected == actual;
    }

    public static boolean compareString(String expected, String actual) {
        return expected.equals(actual);
    }
}
