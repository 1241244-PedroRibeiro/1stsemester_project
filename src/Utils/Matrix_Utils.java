package Utils;

public class Matrix_Utils {
    public static double[][] transpose(double[][] matrix) {
        double[][] transposedMatrix = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                    transposedMatrix[j][i] = matrix[i][j];
            }
        }

        return transposedMatrix;
    }

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

    public static double[] getMatrixVectorAtPosition(double[][] matrixEigenFaces, int j) {
        double[] vector = new double[matrixEigenFaces.length];

        for (int i = 0; i < matrixEigenFaces.length; i++) {
            vector[i] = matrixEigenFaces[i][j];
        }
        return vector;
    }

    public static double[] getUjTransposed(double[][] EigenFacesmatrix, int j) {
        double[] ujTransposed = new double[EigenFacesmatrix.length];
        for (int i = 0; i < EigenFacesmatrix.length; i++) {
            ujTransposed[i] = EigenFacesmatrix[i][j];
        }
        return ujTransposed;
    }
}
