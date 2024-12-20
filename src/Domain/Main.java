package Domain;

import Utils.File_Utils;
import Utils.GreyscaleImageWriter;
import Utils.Matrix_Utils;
import Utils.UI_Utils;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Arrays;

import java.io.File;
import java.io.IOException;


public class Main {
    public static final String OUTPUT_PATH = "output/funcionalidade_1";
    public static final String OUTPUT_PATH_F2 = "output/funcionalidade_2/imagensReconstruidas";
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {


        double[][] matrixVectors, matrixA, matrixATA, transposedMatrixA, eigenVectorsATA, matrixEigenVectorsC, matrixEigenFaces;
        double[] averageImages;
        String filePath, folderPath;
        File[] files;
        int matrixSize, numEigenFaces;


        int menuOption;

        do {
            menuOption = -1;

            while (menuOption < 0 || menuOption > 5) {
                UI_Utils.showMenuOptions();

                if (scanner.hasNextInt()) {
                    menuOption = scanner.nextInt();
                    if (menuOption < 0 || menuOption > 5) {
                        System.out.println("Opção inválida. Escolha uma opção válida.");
                    }
                } else {
                    System.out.println("Entrada inválida. Insira um número.");
                    scanner.next();
                }
            }

            switch (menuOption) {
                case 1:
                    UI_Utils.showMenuTitle(1);
                    filePath = UI_Utils.selectFileToRead();


                    double[][] readMatrix = File_Utils.getMatrixFromCSV(filePath);

                    if (readMatrix != null) {

                        if (validateMatrix(readMatrix)) {
                            System.out.println("\nMatriz Lida:");
                            UI_Utils.printMatrix(readMatrix);

                            int k = askNumberOfEigenvectors(readMatrix);
                            RealMatrix[] result = ownDecomposition(readMatrix);

                            double[][] matrixP = result[0].getData();
                            double[][] matrixD = result[1].getData();

                            double[][][] decomposed = compressedMatrix(matrixP, matrixD, k);

                            double[][] matrixPk = decomposed[0];
                            double[][] matrixDk = decomposed[1];

                            double[][] matrixAk = Matrix_Utils.matrixMultiplication(Matrix_Utils.matrixMultiplication(matrixPk, matrixDk), Matrix_Utils.transpose(matrixPk));

                            System.out.println("\nMatriz Reconstruída\n:");
                            UI_Utils.printMatrix(matrixAk);

                            File_Utils.saveMatrixToCSV(matrixAk, OUTPUT_PATH);

                        } else {
                            System.out.println("\nInsira uma matriz válida.");
                        }
                    } else {

                        System.out.println("A matriz ainda não foi lida.");
                    }
                    break;
                case 2:
                    UI_Utils.showMenuTitle(2);

                    folderPath = UI_Utils.selectFolderToRead();
                    files = File_Utils.getCsvFiles(folderPath);
                    matrixSize = File_Utils.getNumberOfLines(files[0]);
                    matrixVectors = File_Utils.processCsvFiles(files, matrixSize);
                    averageImages = calculateAverage(matrixVectors);
                    UI_Utils.printVetorMedia(averageImages);
                    matrixA = calculateMatrixA(matrixSize * matrixSize, files.length, matrixVectors, averageImages);
                    System.out.println("\nDimensões da Matriz (A) Φ: Linhas = " + matrixA.length + ", Colunas = " + matrixA[0].length + "\n");
                    transposedMatrixA = Matrix_Utils.transpose(matrixA);
                    matrixATA = Matrix_Utils.matrixMultiplication(transposedMatrixA, matrixA);
                    System.out.println("Matriz Aᵀ x A:");
                    UI_Utils.printMatrix(matrixATA);
                    eigenVectorsATA = getEigenVectors(matrixATA);
                    System.out.println("\nQuantos Eigenfaces deseja usar?");
                    numEigenFaces = scanner.nextInt();
                    matrixEigenVectorsC = calculateEigenVectorsC(matrixA, eigenVectorsATA, numEigenFaces);
                    matrixEigenFaces = normalizeVectorsC(matrixEigenVectorsC);
                    rebuildImage(matrixVectors, files, matrixA, matrixEigenFaces, averageImages, numEigenFaces, matrixSize);

                    break;
                case 3:
                    UI_Utils.showMenuTitle(3);

                    folderPath = UI_Utils.selectFolderToRead();
                    files = File_Utils.getCsvFiles(folderPath);
                    matrixSize = File_Utils.getNumberOfLines(files[0]);
                    matrixVectors = File_Utils.processCsvFiles(files, matrixSize);
                    averageImages = calculateAverage(matrixVectors);
                    UI_Utils.printVetorMedia(averageImages);
                    matrixA = calculateMatrixA(matrixSize * matrixSize, files.length, matrixVectors, averageImages);
                    transposedMatrixA = Matrix_Utils.transpose(matrixA);
                    matrixATA = Matrix_Utils.matrixMultiplication(transposedMatrixA, matrixA);
                    UI_Utils.printMatrix(matrixATA);
                    eigenVectorsATA = getEigenVectors(matrixATA);
                    System.out.println("\nQuantos Eigenfaces deseja usar?");
                    numEigenFaces = scanner.nextInt();
                    matrixEigenVectorsC = calculateEigenVectorsC(matrixA, eigenVectorsATA, numEigenFaces);
                    matrixEigenFaces = normalizeVectorsC(matrixEigenVectorsC);

                    break;
                case 4:
                    UI_Utils.showMenuTitle(4);
                    UI_Utils.showDevelopmentTeam();
                    break;
                case 0:
                    System.out.println("A fechar a aplicação...");
                    break;
            }

        } while (menuOption != 0);

        scanner.close();
    }

    private static double[][] calculateMatrixA(int lines, int rows, double[][] matrixVectors, double[] averageImages) {
        double[][] matrixA = new double[lines][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < lines; j++) {
                matrixA[j][i] = matrixVectors[j][i] - averageImages[j];
            }
        }

        return matrixA;
    }

    public static void rebuildImage(double[][] orginalMatrix, File[] files, double[][] matrixA, double[][] eigenFacesMatrix, double[] averageImages, int numEigenFaces, int MatrixSize) {
        File outputFolder = new File(OUTPUT_PATH_F2);

        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        for (int i = 0; i < files.length; i++) {
            double[] phi = getPhiInPosition(matrixA, i);
            double[] sumArray = new double[eigenFacesMatrix.length];
            double[] reconstructedImage = new double[eigenFacesMatrix.length];

            for (int j = 0; j < numEigenFaces; j++) {
                double[] ujTransposed = getUjTransposed(eigenFacesMatrix, j);
                double weight = calculateWeight(ujTransposed, phi);
                double[] uj = getMatrixVectorAtPosition(eigenFacesMatrix, j);

                //Multiply vector uj by weight
                for (int k = 0; k < uj.length; k++) {
                    uj[k] *= weight;
                }

                //add the vector uj to the sumArray
                for (int k = 0; k < uj.length; k++) {
                    sumArray[k] += uj[k];
                }
            }

            //add sumArray to the average
            for (int j = 0; j < averageImages.length; j++) {
                reconstructedImage[j] = averageImages[j] + sumArray[j];
            }
            saveIntMatrixToCSV(reconstructedImage, OUTPUT_PATH_F2, MatrixSize, i);
            saveImageAsJPG(OUTPUT_PATH_F2, reconstructedImage, MatrixSize, i);
        }
    }

    private static void saveIntMatrixToCSV(double[] reconstructedImage, String outputPath, int matrixSize, int counter) {
        try {
            PrintWriter writer = new PrintWriter(outputPath + "/imagem_reconstruida_" + counter + ".csv");
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    int pixelValue = (int) Math.round(reconstructedImage[i * matrixSize + j]);
                    writer.print(pixelValue);
                    if (j < matrixSize - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar imagem como CSV: " + e.getMessage());
        }
    }


    public static double[] getMatrixVectorAtPosition(double[][] matrixEigenFaces, int j) {
        double[] vector = new double[matrixEigenFaces.length];

        for (int i = 0; i < matrixEigenFaces.length; i++) {
            vector[i] = matrixEigenFaces[i][j];
        }
        return vector;
    }


    public static double calculateWeight(double[] ujTransposed, double[] phi) {
        double weight = 0.0;

        for (int i = 0; i < ujTransposed.length; i++) {
            weight += ujTransposed[i] * phi[i];
        }
        return weight;
    }

    public static void saveImageAsJPG(String savePath, double[] imageVector, int matrixSize, int counter) {
        int[][] array = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int pixelValue = (int) Math.round(imageVector[i * matrixSize + j]);

                pixelValue = Math.max(0, Math.min(255, pixelValue));
                array[i][j] = pixelValue;
            }
        }

        GreyscaleImageWriter writer = new GreyscaleImageWriter();
        try {
            writer.writeArrayAsImage(array, savePath + "/imagem_reconstruida_" + counter + ".jpg");
        } catch (IOException e) {
            System.err.println("Erro ao salvar imagem como JPG: " + e.getMessage());
        }
    }

    public static double[] getUjTransposed(double[][] EigenFacesmatrix, int j) {
        double[] ujTransposed = new double[EigenFacesmatrix.length];
        for (int i = 0; i < EigenFacesmatrix.length; i++) {
            ujTransposed[i] = EigenFacesmatrix[i][j];
        }
        return ujTransposed;
    }

    public static double[] getPhiInPosition(double[][] matrixA, int index) {
        double[] phi = new double[matrixA.length];

        //This method places column i of matrix A in phi
        for (int i = 0; i < matrixA.length; i++) {
            phi[i] = matrixA[i][index];
        }
        return phi;
    }

    public static double[][] calculateEigenVectorsC(double[][] matrixA, double[][] eigenVectorsATA, int numEigenFaces) {
        int numLinesA = matrixA.length;
        int numColumnsA = matrixA[0].length;

        double[][] eigenVectorsMatrixC = new double[numLinesA][numEigenFaces];

        for (int i = 0; i < numEigenFaces; i++) {
            double[] eigenVector = new double[numColumnsA];

            for (int j = 0; j < numColumnsA; j++) {
                eigenVector[j] = eigenVectorsATA[j][i];
            }

            double[] eigenVectorC = new double[numLinesA];

            for (int j = 0; j < numLinesA; j++) {
                for (int k = 0; k < numColumnsA; k++) {
                    eigenVectorC[j] += matrixA[j][k] * eigenVector[k];
                }
            }

            for (int j = 0; j < numLinesA; j++) {
                eigenVectorsMatrixC[j][i] = eigenVectorC[j];
            }
        }
        return eigenVectorsMatrixC;
    }

    public static RealMatrix[] ownDecomposition(double[][] matrixRead) {
        // Own decomposition
        RealMatrix[] result = new RealMatrix[2];
        EigenDecomposition eigenDecomposition = new EigenDecomposition(MatrixUtils.createRealMatrix(matrixRead));

        // Eigenvector matrix (P)
        RealMatrix matrixP = eigenDecomposition.getV();
        result[0] = matrixP;
        // Diagonal matrix of eigenvalues ​​(D)
        RealMatrix matrixD = MatrixUtils.createRealDiagonalMatrix(eigenDecomposition.getRealEigenvalues());
        result[1] = matrixD;

        return result;
    }

    public static double[][][] compressedMatrix(double[][] matrixP, double[][] matrixD, int k) {
        double[][] auxP = matrixP;
        double[][] auxD = matrixD;

        int n = matrixD.length;
        double[] diagonal = new double[n];
        for (int i = 0; i < n; i++) {
            diagonal[i] = auxD[i][i];
        }

        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, (i, j) -> Double.compare(diagonal[j], diagonal[i]));

        double[][] sortedD = new double[n][n];
        double[][] sortedP = new double[n][n];
        for (int i = 0; i < n; i++) {
            sortedD[i][i] = diagonal[indices[i]];
            for (int j = 0; j < n; j++) {
                sortedP[j][i] = auxP[j][indices[i]];
            }
        }

        for (int i = 0; i < n; i++) {
            auxD[i][i] = sortedD[i][i];
            for (int j = 0; j < n; j++) {
                auxP[j][i] = sortedP[j][i];
            }
        }

        double[][] finalPk = new double[auxP[0].length][k];
        double[][] finalDk = new double[k][k];

        for (int i = 0; i < auxP[0].length; i++) {
            for (int j = 0; j < k; j++) {
                finalPk[i][j] = auxP[i][j];
            }
        }

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (i == j) finalDk[i][j] = auxD[i][j];
            }
        }

        return new double[][][]{finalPk, finalDk};
    }

    /**
     * Function that validates the matrix. Checks if the matrix is square, symmetric and does not contain negative values, returning the specific error if it exists.
     *
     * @param matrix
     * @return true if the matrix is valid or false if it is not
     */
    public static boolean validateMatrix(double[][] matrix) {

        if (matrix.length != matrix[0].length) {
            System.out.println("A matriz não é quadrada.");
            return false;
        }

        String[] negativeValues = new String[matrix.length * matrix[0].length];
        boolean hasNegatives = false;
        int negativeIndex = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != matrix[j][i]) {
                    System.out.println("A matriz não é simétrica.");
                    return false;
                }
                if (matrix[i][j] < 0) {
                    hasNegatives = true;
                    negativeValues[negativeIndex++] = "Linha: " + i + ", Coluna: " + j + " (" + matrix[i][j] + ")";
                }
            }
        }
        if (hasNegatives) {
            System.out.println("A matriz contém valores negativos.");
            for (int i = 0; i < negativeValues.length; i++) {
                if (negativeValues[i] != null) {
                    System.out.println(negativeValues[i]);
                }
            }
            return false;
        }
        return true;
    }

    public static int askNumberOfEigenvectors(double[][] matrix) {
        int k = 0;

        while (k <= 0 || k > matrix.length) {
            System.out.println("\nQual é o número de vetores próprios (k)?");

            if (scanner.hasNextInt()) {
                k = scanner.nextInt();

                if (k == -1) {
                    k = matrix.length;
                    System.out.println("Opção -1: Usando todos os Vetores Próprios.");
                    return k;
                }

                if (k <= 0) {
                    System.out.println("""
                            Número inválido de autovetores.
                               Entradas válidas:
                                    - Um número inteiro positivo
                                    - -1 para usar todos os vetores próprios
                            """);
                } else if (k > matrix.length) {
                    System.out.printf("O número máximo de vetores próprios é %d. A definir k para %d.%n", matrix.length, matrix.length);
                    k = matrix.length;
                }
            } else {
                System.out.println("Entrada inválida. Insira um número.");
                scanner.next();
            }
        }
        return k;
    }

    /**
     * Function to calculate the average absolute error between two matrices, the original and the reconstructed one
     *
     * @param original
     * @param reconstructed
     * @return mean absolute error
     */
    public static double calculateEAM(double[][] original, double[][] reconstructed) {
        double sum = 0;
        int rows = original.length;
        int cols = original[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum += Math.abs(original[i][j] - reconstructed[i][j]);
            }
        }
        return sum / (rows * cols);
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

    public static void sortEigenvectors(double[] eigenvalues, double[][] eigenvectors) {

        for (int i = 0; i < eigenvalues.length; i++) {
            for (int j = i + 1; j < eigenvalues.length; j++) {
                if (eigenvalues[i] < eigenvalues[j]) {
                    double temp = eigenvalues[i];
                    eigenvalues[i] = eigenvalues[j];
                    eigenvalues[j] = temp;

                    double[] tempVector = eigenvectors[i];
                    eigenvectors[i] = eigenvectors[j];
                    eigenvectors[j] = tempVector;
                }
            }
        }
    }

    public static double[][] getEigenVectors(double[][] matrixATA) {
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixATA);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(matrix);

        double[][] eigenvectors = eigenDecomposition.getV().getData();
        double[] eigenvalues = eigenDecomposition.getRealEigenvalues();

        System.out.println("\nVetores Próprios:");
        for (double value : eigenvalues) {
            System.out.println(value);
        }

        sortEigenvectors(eigenvalues, eigenvectors);

        return eigenvectors;
    }


    public static double[][] normalizeVectorsC(double[][] eigenvectorMatrixC) {
        int numRows = eigenvectorMatrixC.length;      // Number of rows
        int numCols = eigenvectorMatrixC[0].length;  // Number of columns (number of eigenvectors)

        double[][] eigenFacesMatrix = new double[numRows][numCols];

        // Normalize each column (eigenvector)
        for (int j = 0; j < numCols; j++) {
            double sum = 0;

            // Calculate the norm of the vector (column j)
            for (int i = 0; i < numRows; i++) {
                sum += Math.pow(eigenvectorMatrixC[i][j], 2);
            }
            double norm = Math.sqrt(sum);

            // Normalize column j
            for (int i = 0; i < numRows; i++) {
                eigenFacesMatrix[i][j] = eigenvectorMatrixC[i][j] / norm;
            }
        }
        return eigenFacesMatrix;
    }
}