import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;


import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    private static double[][] matrizLida, matrizP, matrizD;
    static String caminhoCsv = "out/production/lapr1-24-25_DIJ_04/exampleInputFunc1.csv";
    static Scanner scanner = new Scanner(System.in);
    static int k;


    public static void main(String[] args) {
        //Declaração de Variáveis
        int menuOption;


        //Leitura da Opção do Menu e Redirecionamento para os diferentes casos
        do {
            menuOption = -1;

            while (menuOption < 0 || menuOption > 5) {
                showMenuOptions();

                if (scanner.hasNextInt()) {
                    menuOption = scanner.nextInt();
                    if (menuOption < 0 || menuOption > 5) {
                        System.out.println("Invalid option. Please choose a valid option.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                }
            }

            System.out.println("\nYou selected option: " + menuOption);

            switch (menuOption) {
                case 1:
                    //TODO: Implementar esta funcionalidade
                    System.out.println("Import matrix from csv file");
                    GetMatrixFromCSV();
                    break;
                case 2:
                    if (matrizLida != null) {
                        k = askNumberOfEigenvectors(matrizLida);

                        RealMatrix[] resultado = decomposicaoPropria();
                        matrizP = resultado[0].getData();
                        matrizD = resultado[1].getData();

                    } else {
                        System.out.println("A matriz ainda não foi lida.");


                    }
                    break;
                case 3:
                    //TODO: Implementar esta funcionalidade
                    System.out.println("Functionality 2");
                    break;
                case 4:
                    //TODO: Implementar esta funcionalidade
                    System.out.println("Functionality 3");
                    break;
                case 5:
                    //TODO: Implementar esta funcionalidade
                    System.out.println("Know Development Team");
                    break;
                case 0:
                    System.out.println("Closing application...");
                    break;
            }

        } while (menuOption != 0);

        scanner.close();
    }

    public static void showMenuOptions() {
        System.out.println("""
                
                ===============================================
                |                MENU OPTIONS                 |
                ===============================================
                |                                             |
                |   1. Import matrix from csv file            |
                |   2. Functionality 1                        |
                |   3. Functionality 2                        |
                |   4. Functionality 1                        |
                |   5. Know Development Team                  |
                |   0. Exit                                   |
                |                                             |
                ===============================================
                """);
        System.out.print("  Choose Option: ");
    }

    public static void GetMatrixFromCSV() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(caminhoCsv));
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Erro - O CSV está vazio.");
            }

            String[] values = line.split(",");
            int columnCount = values.length;

            int rowCount = 0;
            while ((line = reader.readLine()) != null) {
                rowCount++;
            }

            reader.close();
            reader = new BufferedReader(new FileReader(caminhoCsv));

            matrizLida = new double[rowCount][columnCount];

            int currentRow = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] rowValues = line.split(",");
                if (rowValues.length != columnCount) {
                    throw new IllegalArgumentException("Erro - número de colunas na linha " + (currentRow + 1) + " não corresponde ao número de colunas.");
                }

                for (int i = 0; i < rowValues.length; i++) {
                    try {
                        matrizLida[currentRow][i] = Double.parseDouble(rowValues[i].trim());
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException("Erro ao converter valor - Linha " + (currentRow + 1) + ", Coluna " + (i + 1) + ": '" + rowValues[i] + "'");
                    }
                }
                currentRow++;
            }

            reader.close();

            System.out.println("Matriz lida com sucesso:");
            for (int i = 0; i < matrizLida.length; i++) {
                for (int j = 0; j < matrizLida[i].length; j++) {
                    System.out.print(matrizLida[i][j] + " ");
                }
                System.out.println();
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro - leitura do ficheiro CSV foi feita incorretamente: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro - leitura realizada incorretamente: " + e.getMessage());
        }
    }


    public static RealMatrix[] decomposicaoPropria() {
        // Decomposição própria
        RealMatrix[] result = new RealMatrix[2];
        EigenDecomposition eigenDecomposition = new EigenDecomposition(MatrixUtils.createRealMatrix(matrizLida));

        // Matriz dos vetores próprios (P)
        RealMatrix matrizP = eigenDecomposition.getV();
        result[0] = matrizP;
        // Matriz diagonal dos autovalores (D)
        RealMatrix matrizD = MatrixUtils.createRealDiagonalMatrix(eigenDecomposition.getRealEigenvalues());
        result[1] = matrizD;

        return result;
    }

    /**
     * Função que faz a validação da matriz. Verifica se a matriz é quadrada, simétrica e se não contém valores negativos, retornando o erro específico caso exista.
     *
     * @param matrix
     * @return verdadeiro se a matriz é válida ou falso se não for
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

    private static int askNumberOfEigenvectors(double[][] matrix) {
        int k = 0;

        while (k <= 0 && k != -1) {
            System.out.println("Qual é o número de vetores próprios (k)?");

            if (scanner.hasNextInt()) {
                k = scanner.nextInt();
                if (k <= 0 && k != -1) {
                    System.out.println("""
                            Invalid number of eigenvectors.
                               Valid inputs:
                                    - A positive integer
                                    - -1 to use all eigenvectors
                            """);
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }

        if (k == -1) {
            k = matrix.length;
        }

        return k;
    }

    /**
     * Função para imprimir uma matriz (object real matrix) para efeitos de teste e visualização
     *
     * @param matrix
     */
    public static void printRealMatrix(RealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                System.out.printf(matrix.getEntry(i, j) + " ");
            }
            System.out.println();
        }
    }

    /**
     * Função para imprimir uma matriz (double[][]) para efeitos de teste e visualização
     *
     * @param matrix
     */
    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%.2f ", matrix[i][j]);
            }
            System.out.println();
        }
    }

    public static double[][] transpose(double[][] matrix) {
        double[][] transposedmatrix = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (i == j) {
                    transposedmatrix[i][j] = matrix[i][j];
                } else {
                    transposedmatrix[j][i] = matrix[i][j];
                }
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

    /**
     * Função para calcular o erro absoluto médio entre duas matrizes, a original e a reconstruída
     * @param original
     * @param reconstructed
     * @return erro absoluto médio
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

}