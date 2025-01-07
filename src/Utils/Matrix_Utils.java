package Utils;

public class Matrix_Utils {
    public static double[][] transpose(double[][] matrix) {
        double[][] transposedmatrix = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                    transposedmatrix[j][i] = matrix[i][j];
            }
        }

        return transposedmatrix;
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
        //esta matriz é basicamente
        //C = (1/n) * ATA
        for (int i = 0; i < matrixATA.length; i++) {
            for (int j = 0; j < matrixATA[i].length; j++) {
                covarianceMatrix[i][j] = matrixATA[i][j] / n;
            }
        }
        return covarianceMatrix;
    }
}
