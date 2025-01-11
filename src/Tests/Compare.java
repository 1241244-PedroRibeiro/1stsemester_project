package Tests;

/**
 * Class for comparing expected and actual values of different types.
 */
public class Compare {
    /**
     * Compares two integers.
     *
     * @param expected The expected value.
     * @param actual   The actual value.
     * @return True if the values are equal, false otherwise.
     */
    public static boolean compareInt(int expected, int actual) {
        return expected == actual;
    }

    /**
     * Compares two doubles.
     *
     * @param expected The expected value.
     * @param actual   The actual value.
     * @return True if the values are equal, false otherwise.
     */
    public static boolean compareDouble(double expected, double actual) {
        return expected == actual;
    }

    /**
     * Compares two Matrices.
     *
     * @param expected The expected matrix.
     * @param actual   The actual matrix.
     * @return True if both matrices are equal, false otherwise.
     */
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

    /**
     * Compares two vectors.
     *
     * @param expected The expected vector.
     * @param actual   The actual vector.
     * @return True if the vectors are equal, false otherwise.
     */
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

    /**
     * Compares two matrices with a tolerance.
     *
     * @param expected  The expected matrix.
     * @param actual    The actual matrix.
     * @param tolerance The tolerance.
     * @return True if the matrices are equal within the tolerance, false otherwise.
     */
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
