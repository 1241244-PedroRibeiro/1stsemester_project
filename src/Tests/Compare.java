package Tests;

public class Compare {
    public static boolean compareInt(int expected, int actual) {
        return expected == actual;
    }

    public static boolean compareDouble(double expected, double actual) {
        return expected == actual;
    }

    public static boolean compareMatrix(double[][] expected, double[][] actual) {
        if (expected.length != actual.length) {
            return false;
        }

        for (int i = 0; i < expected.length; i++) {
            if (expected[i].length != actual[i].length) {
                return false;
            }

            for (int j = 0; j < expected[i].length; j++) {
                if (expected[i][j] != actual[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean compareVector(double[] expected, double[] actual) {
        if (expected.length != actual.length) {
            return false;
        }

        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean compareMatrixWithTolerance(double[][] expected, double[][] actual, double tolerance) {
        if (expected.length != actual.length || expected[0].length != actual[0].length) {
            return false;
        }
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                if (Math.abs(expected[i][j] - actual[i][j]) > tolerance) {
                    return false;
                }
            }
        }
        return true;
    }
}
