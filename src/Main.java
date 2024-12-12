import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;


import java.util.Scanner;
import java.io.File;

public class Main {
    private static double[][] matrizLida;
    static String caminhoCsv = "exampleInputFunc1.csv";
    static Scanner scanner = new Scanner(System.in);




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
                    //DecomposicaoPropria();
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

    }


    /*
    public static void DecomposicaoPropria(){
        // Decomposição própria
        EigenDecomposition eigenDecomposition = new EigenDecomposition(A);

        // Matriz dos vetores próprios (P)
        RealMatrix P = eigenDecomposition.getV();

        // Matriz diagonal dos autovalores (D)
        RealMatrix D = MatrixUtils.createRealDiagonalMatrix(eigenDecomposition.getRealEigenvalues());

        System.out.println("Matriz P (vetores próprios (Características Principais)):");
        printMatrix(P);

        System.out.println("Matriz D (autovalores (Características Mais Significativas)):");
        printMatrix(D);

    }
    */

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

    /**
     * Função para imprimir uma matriz para efeitos de teste e visualização
     *
     * @param matrix
     */
    public static void printMatrix(RealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                System.out.print(matrix.getEntry(i, j) + " ");
            }
            System.out.println();
        }
    }

    public static double[][] transpose(double[][] matrix, int k) {
        double[][] transposedmatrix = new double[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if(i==j){
                    transposedmatrix[i][j] = matrix[i][j];
                }
                else{
                    transposedmatrix[j][i] = matrix[i][j];
                }
            }
        }

        return transposedmatrix;
    }
    public static double[][] matrixMultiplication(double[][] P, double[][] D, int k) {
        double[][] calculatedMatrix = new double[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                for(int l=0; l<k;l++){
                    calculatedMatrix[i][j] += P[i][l] * D[l][j];
                }
            }

        }
        return calculatedMatrix;
    }

}