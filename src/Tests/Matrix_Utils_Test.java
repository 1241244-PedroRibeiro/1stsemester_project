package Tests;

import Domain.Main;
import Utils.Matrix_Utils;
import Utils.UI_Utils;

import java.util.Arrays;

import static Tests.Compare.compareVector;

public class Matrix_Utils_Test {

    public static void main(String[] args) {
        testTranspose();
        testMatrixMultiplication();
        testTransposeLargeMatrix();
        testMatrixMultiplicationLargeMatrix();
        testMatrixMultiplicationRectangularMatrix();
        testMatrixMultiplicationTwoRectangularMatrix();
        testTransposeNonSquareMatrix();
        testMatrixMultiplicationWithIdentityMatrix();
        testMatrixMultiplicationWithZeroMatrix();
        testCalculateAverage();
        testCalculateAverage15x15();
        testGetMatrixVectorAtPosition();
        testGetUjTransposed();
        testCalculateCovarianceMatrix();
        testCalculateCovarianceMatrixLarge();

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

    public static void testTransposeNonSquareMatrix() {
        UI_Utils.showTestTitle("testTransposeNonSquareMatrix");
        double[][] matrix = {
                {1, 2, 3},
                {4, 5, 6}
        };
        double[][] expected = {
                {1, 4},
                {2, 5},
                {3, 6}
        };
        double[][] actual = Matrix_Utils.transpose(matrix);
        boolean result = compareMatrices(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testCalculateCovarianceMatrix() {
        UI_Utils.showTestTitle("testCalculateCovarianceMatrix");
        double[][] matrixATA = {
                {4, 2},
                {2, 3}
        };
        int n = 2;
        double[][] expected = {
                {2, 1},
                {1, 1.5}
        };
        double[][] actual = Matrix_Utils.calculateCovarianceMatrix(matrixATA, n);
        boolean result = compareMatrices(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testMatrixMultiplicationWithIdentityMatrix() {
        UI_Utils.showTestTitle("testMatrixMultiplicationWithIdentityMatrix");
        double[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double[][] identityMatrix = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        double[][] expected = matrix;
        double[][] actual = Matrix_Utils.matrixMultiplication(matrix, identityMatrix);
        boolean result = compareMatrices(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testMatrixMultiplicationWithZeroMatrix() {
        UI_Utils.showTestTitle("testMatrixMultiplicationWithZeroMatrix");
        double[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double[][] zeroMatrix = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        double[][] expected = zeroMatrix;
        double[][] actual = Matrix_Utils.matrixMultiplication(matrix, zeroMatrix);
        boolean result = compareMatrices(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testCalculateAverage() {
        UI_Utils.showTestTitle("testCalculateAverage");

        double[][] vectorMatrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double[] expected = {2, 5, 8};
        double[] actual = Matrix_Utils.calculateAverage(vectorMatrix);

        boolean result = compareVector(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testCalculateAverage15x15() {
        UI_Utils.showTestTitle("testCalculateAverage15x15");

        double[][] vectorMatrix = {
                {1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5},
                {2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5, 16.5},
                {3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5},
                {4.5, 5.5, 6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5, 18.5},
                {5.5, 6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5, 18.5, 19.5},
                {6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5, 18.5, 19.5, 20.5},
                {7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5, 18.5, 19.5, 20.5, 21.5},
                {8.5, 9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5, 18.5, 19.5, 20.5, 21.5, 22.5},
                {9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5, 18.5, 19.5, 20.5, 21.5, 22.5, 23.5},
                {10.5, 11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5, 18.5, 19.5, 20.5, 21.5, 22.5, 23.5, 24.5},
                {11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5, 18.5, 19.5, 20.5, 21.5, 22.5, 23.5, 24.5, 25.5},
                {12.5, 13.5, 14.5, 15.5, 16.5, 17.5, 18.5, 19.5, 20.5, 21.5, 22.5, 23.5, 24.5, 25.5, 26.5},
                {13.5, 14.5, 15.5, 16.5, 17.5, 18.5, 19.5, 20.5, 21.5, 22.5, 23.5, 24.5, 25.5, 26.5, 27.5},
                {14.5, 15.5, 16.5, 17.5, 18.5, 19.5, 20.5, 21.5, 22.5, 23.5, 24.5, 25.5, 26.5, 27.5, 28.5},
                {15.5, 16.5, 17.5, 18.5, 19.5, 20.5, 21.5, 22.5, 23.5, 24.5, 25.5, 26.5, 27.5, 28.5, 29.5}
        };

        double[] expectedAverageVector = {
                8.5,
                9.5,
                10.5,
                11.5,
                12.5,
                13.5,
                14.5,
                15.5,
                16.5,
                17.5,
                18.5,
                19.5,
                20.5,
                21.5,
                22.5
        };

        double[] actualAverageVector = Matrix_Utils.calculateAverage(vectorMatrix);
        boolean result = compareVector(expectedAverageVector, actualAverageVector);

        UI_Utils.showTestResult(result);
    }

    public static void testGetMatrixVectorAtPosition() {
        UI_Utils.showTestTitle("testGetMatrixVectorAtPosition");

        double[][] eigenFacesMatrix = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        int j = 1;
        double[] expected = {2.0, 5.0, 8.0};
        double[] actual = Matrix_Utils.getMatrixVectorAtPosition(eigenFacesMatrix, j);

        boolean result = compareVector(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testGetUjTransposed() {
        UI_Utils.showTestTitle("testGetUjTransposed");

        double[][] eigenFacesMatrix = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        int j = 1;
        double[] expected = {2.0, 5.0, 8.0};
        double[] actual = Matrix_Utils.getUjTransposed(eigenFacesMatrix, j);

        boolean result = compareVector(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testCalculateCovarianceMatrixLarge() {
        UI_Utils.showTestTitle("testCalculateCovarianceMatrixLarge");
        double[][] matrixATA = {
                {4, 2, 0, 1},
                {2, 3, 1, 0},
                {0, 1, 2, 1},
                {1, 0, 1, 3}
        };
        int n = 4;
        double[][] expected = {
                {1, 0.5, 0, 0.25},
                {0.5, 0.75, 0.25, 0},
                {0, 0.25, 0.5, 0.25},
                {0.25, 0, 0.25, 0.75}
        };
        double[][] actual = Matrix_Utils.calculateCovarianceMatrix(matrixATA, n);
        boolean result = compareMatrices(expected, actual);
        UI_Utils.showTestResult(result);
    }

}