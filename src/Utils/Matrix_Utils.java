package Utils;

/**
 * This class contains utility methods for matrix operations.
 */
public class Matrix_Utils {
    /**
     * This method transpose a matrix.
     * @param matrix The matrix to be transposed.
     * @return The transposed matrix.
     */
    public static double[][] transpose(double[][] matrix) {
        double[][] transposedMatrix = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                    transposedMatrix[j][i] = matrix[i][j];
            }
        }

        return transposedMatrix;
    }

    /**
     * This method calculates the matrix multiplication of two matrices.
     * @param P The first matrix.
     * @param D The second matrix.
     * @return The result of the matrix multiplication.
     */
    public static double[][] matrixMultiplication(double[][] P, double[][] D) {
        double[][] calculatedMatrix = new double[P.length][D[0].length];
        for (int i = 0; i < P.length; i++) {
            for (int j = 0; j < D[0].length; j++) {

                for (int l = 0; l < P[0].length; l++) {
                    calculatedMatrix[i][j] += P[i][l] * D[l][j];
                }
            }
        }
        return calculatedMatrix;
    }

    /**
     * Method that calculates the Covariance Matrix. This method is used only for the print matter. Dividing the actual ATA Matrix by n.
     * @param matrixATA The matrix ATA.
     * @param n The number of images.
     * @return The Covariance Matrix.
     */
    public static double[][] calculateCovarianceMatrix(double[][] matrixATA, int n) {
        double[][] covarianceMatrix = new double[matrixATA.length][matrixATA[0].length];
        //The matrix is the C = (1/n) * ATA
        for (int i = 0; i < matrixATA.length; i++) {
            for (int j = 0; j < matrixATA[i].length; j++) {
                covarianceMatrix[i][j] = matrixATA[i][j] / n;
            }
        }
        return covarianceMatrix;
    }

    /**
     * Method that calculates the average of a matrix.
     * @param vectorMatrix The matrix to calculate the average.
     * @return The average vector.
     */
    public static double[] calculateAverage(double[][] vectorMatrix) {
        int numRows = vectorMatrix.length;
        int numCols = vectorMatrix[0].length;

        double[] averageVector = new double[numRows];

        for (int i = 0; i < numRows; i++) {
            double sum = 0.0;
            for (int j = 0; j < numCols; j++) {
                sum += vectorMatrix[i][j];
            }
            averageVector[i] = sum / numCols;
        }

        return averageVector;
    }

    /**
     * Method that returns a specific column of a matrix.
     * @param matrix The matrix to get the column.
     * @param j The column to get.
     * @return The column of the matrix.
     */
    public static double[] getMatrixVectorAtPosition(double[][] matrix, int j) {
        double[] vector = new double[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            vector[i] = matrix[i][j];
        }
        return vector;
    }

    /**
     * Method that automatically returns a transposed vector of a given matrix in a specific column.
     * @param matrix The matrix to get the transposed vector.
     * @param j The column to get the transposed vector.
     * @return The transposed vector.
     */
    public static double[] getUjTransposed(double[][] matrix, int j) {
        double[] ujTransposed = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            ujTransposed[i] = matrix[i][j];
        }
        return ujTransposed;
    }
}
