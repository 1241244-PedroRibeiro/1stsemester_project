package Tests;

import Utils.Matrix_Utils;
import Utils.UI_Utils;

public class Matrix_Utils_Test {

    public static void main(String[] args) {
        testTranspose();
        testMatrixMultiplication();
        testTransposeLargeMatrix();
        testMatrixMultiplicationLargeMatrix();
        testMatrixMultiplicationRectangularMatrix();
    }

    public static void testTranspose() {
        UI_Utils.showTestTitle("testTranspose");
        double[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double[][] expected = {
                {1, 4, 7},
                {2, 5, 8},
                {3, 6, 9}
        };
        double[][] actual = Matrix_Utils.transpose(matrix);
        boolean result = compareMatrices(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testMatrixMultiplication() {
        UI_Utils.showTestTitle("testMatrixMultiplication");
        double[][] matrixA = {
                {1, 2},
                {3, 4}
        };
        double[][] matrixB = {
                {5, 6},
                {7, 8}
        };
        double[][] expected = {
                {19, 22},
                {43, 50}
        };
        double[][] actual = Matrix_Utils.matrixMultiplication(matrixA, matrixB);
        boolean result = compareMatrices(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testTransposeLargeMatrix() {
        UI_Utils.showTestTitle("testTransposeLargeMatrix");
        double[][] matrix = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        double[][] expected = {
                {1, 5, 9, 13},
                {2, 6, 10, 14},
                {3, 7, 11, 15},
                {4, 8, 12, 16}
        };
        double[][] actual = Matrix_Utils.transpose(matrix);
        boolean result = compareMatrices(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testMatrixMultiplicationLargeMatrix() {
        UI_Utils.showTestTitle("testMatrixMultiplicationLargeMatrix");
        double[][] matrixA = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double[][] matrixB = {
                {9, 8, 7},
                {6, 5, 4},
                {3, 2, 1}
        };
        double[][] expected = {
                {30, 24, 18},
                {84, 69, 54},
                {138, 114, 90}
        };
        double[][] actual = Matrix_Utils.matrixMultiplication(matrixA, matrixB);
        boolean result = compareMatrices(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testMatrixMultiplicationRectangularMatrix() {
        UI_Utils.showTestTitle("testMatrixMultiplicationRectangularMatrix");

        double[][] matrixA = {
                {1, 2, 3, 4, 5},
                {2, 3, 4, 5, 6},
                {3, 4, 5, 6, 7},
                {4, 5, 6, 7, 8},
                {5, 6, 7, 8, 9}
        };

        double[][] matrixB = {
                {1, 2, 3},
                {2, 3, 4},
                {3, 4, 5},
                {4, 5, 6},
                {5, 6, 7}
        };

        double[][] expected = {
                {55, 70, 85},
                {70, 90, 110},
                {85, 110, 135},
                {100, 130, 160},
                {115, 150, 185}

        };

        double[][] actual = Matrix_Utils.matrixMultiplication(matrixA, matrixB);
        boolean result = compareMatrices(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testMatrixMultiplicationTwoRectangularMatrix() {
        UI_Utils.showTestTitle("testMatrixMultiplicationTwoRectangularMatrix");

        double[][] matrixA = {
                {1, 2, 3, 4, 5},
                {2, 3, 4, 5, 6},
                {3, 4, 5, 6, 7},
                {4, 5, 6, 7, 8}
        };

        double[][] matrixB = {
                {1, 2, 3},
                {2, 3, 4},
                {3, 4, 5},
                {4, 5, 6},
                {5, 6, 7}
        };

        double[][] expected = {
                {55, 70, 85},
                {70, 90, 110},
                {85, 110, 135},
                {100, 130, 160}

        };

        double[][] actual = Matrix_Utils.matrixMultiplication(matrixA, matrixB);
        boolean result = compareMatrices(expected, actual);
        UI_Utils.showTestResult(result);
    }

    private static boolean compareMatrices(double[][] expected, double[][] actual) {
        if (expected.length != actual.length || expected[0].length != actual[0].length) {
            return false;
        }
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                if (expected[i][j] != actual[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }


}