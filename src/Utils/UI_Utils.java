package Utils;

import java.io.File;
import java.util.Scanner;

/**
 * Class with utility functions to print messages and menus to the user
 */
public class UI_Utils {

    /**
     * Function to print the main menu options
     */
    public static void showMenuOptions() {
        System.out.println("""
                
                  ╔═════════════════════════════════════════════════════════════════════════╗
                  ║                                 MENU                                    ║
                  ╠═════════════════════════════════════════════════════════════════════════╣
                  ║                                                                         ║
                  ║    1. Decomposição Própria de Matriz Simétrica                          ║
                  ║    2. Reconstrução e representação de imagens utilizando EigenFaces     ║
                  ║    3. Identificação de Imagens utilizando EigenFaces                    ║
                  ║    4. Gerar novas imagens utilizando Eigenfaces                         ║
                  ║    5. Conhecer a equipa de desenvolvimento                              ║
                  ║                                                                         ║
                  ║    0. Exit                                                              ║
                  ║                                                                         ║
                  ╚═════════════════════════════════════════════════════════════════════════╝
                """);
        System.out.print("     Escolha uma opção: ");
    }

    /**
     * Function to print the title of the specific menu
     *
     * @param uiNumber - number of the menu
     */
    public static void showMenuTitle(int uiNumber) {
        switch (uiNumber) {
            case 1:
                System.out.println("""
                        
                          ╔═════════════════════════════════════════════════════════════════════════╗
                          ║             1. Decomposição Própria de Matriz Simétrica                 ║
                          ╚═════════════════════════════════════════════════════════════════════════╝
                        """);
                break;
            case 2:
                System.out.println("""
                        
                          ╔═════════════════════════════════════════════════════════════════════════╗
                          ║    2. Reconstrução e representação de imagens utilizando EigenFaces     ║
                          ╚═════════════════════════════════════════════════════════════════════════╝
                        """);
                break;
            case 3:
                System.out.println("""
                        
                          ╔═════════════════════════════════════════════════════════════════════════╗
                          ║           3. Identificação de Imagens utilizando EigenFaces             ║
                          ╚═════════════════════════════════════════════════════════════════════════╝
                        """);
                break;
            case 4:
                System.out.println("""
                        
                          ╔═════════════════════════════════════════════════════════════════════════╗
                          ║              4. Gerar novas imagens utilizando Eigenfaces               ║
                          ╚═════════════════════════════════════════════════════════════════════════╝
                        """);
                break;
            case 5:
                System.out.println("""
                        
                          ╔═════════════════════════════════════════════════════════════════════════╗
                          ║              5. Conhecer a equipa de desenvolvimento                    ║
                          ╚═════════════════════════════════════════════════════════════════════════╝
                        """);
                break;


        }
    }

    /**
     * Function to print the error message when the user doesn't provide all the necessary arguments
     *
     * @param func
     */
    public static void printErrorMsg(int func) {
        System.out.println("Por favor, indique todos os argumentos necessários para a funcionalidade" + func);
        System.out.println("""
                
                Parâmetros necessários:
                  - k (número de eigenfaces)
                  - d (caminho para a pasta da base de imagens)
                
                """);
    }

    /**
     * Method used to append the menu title to the non-interaction StringBuilder
     *
     * @param uiNumber number of the menu
     * @param sb       StringBuilder to append the menu title
     */
    public static void saveMenuTitle(int uiNumber, StringBuilder sb) {
        switch (uiNumber) {
            case 1:
                sb.append("""
                        
                          ╔═════════════════════════════════════════════════════════════════════════╗
                          ║             1. Decomposição Própria de Matriz Simétrica                 ║
                          ╚═════════════════════════════════════════════════════════════════════════╝
                        
                        """);
                break;
            case 2:
                sb.append("""
                        
                          ╔═════════════════════════════════════════════════════════════════════════╗
                          ║    2. Reconstrução e representação de imagens utilizando EigenFaces     ║
                          ╚═════════════════════════════════════════════════════════════════════════╝
                        
                        """);
                break;
            case 3:
                sb.append("""
                        
                          ╔═════════════════════════════════════════════════════════════════════════╗
                          ║           3. Identificação de Imagens utilizando EigenFaces             ║
                          ╚═════════════════════════════════════════════════════════════════════════╝
                        
                        """);
                break;
            case 4:
                sb.append("""
                        
                          ╔═════════════════════════════════════════════════════════════════════════╗
                          ║              4. Gerar novas imagens utilizando Eigenfaces               ║
                          ╚═════════════════════════════════════════════════════════════════════════╝
                        
                        """);
                break;
            case 5:
                sb.append("""
                        
                          ╔═════════════════════════════════════════════════════════════════════════╗
                          ║              5. Conhecer a equipa de desenvolvimento                    ║
                          ╚═════════════════════════════════════════════════════════════════════════╝
                        
                        """);
                break;
        }
    }

    /**
     * Method to show the test title
     *
     * @param testName
     */
    public static void showTestTitle(String testName) {
        System.out.println("Nome teste: " + testName);
    }

    /**
     * Method to show the test result in case of success or failure
     *
     * @param result boolean result of the test
     */
    public static void showTestResult(boolean result) {
        if (result) {
            System.out.println("Resultado: Aprovado!");
        } else {
            System.out.println("Resultado: Falhou!");
        }
        System.out.println("══════════════════════════════════════════");
    }

    /**
     * Method to show the development team
     */
    public static void showDevelopmentTeam() {
        System.out.println("""                    
                    LAPR1 - Projeto Integrador
                    Grupo 04 - BitMinds
                
                    Desenvolvido por:
                        Pedro Costa     - 1221790
                        Pedro Ribeiro   - 1241244
                        Afonso Mota     - 1241225
                        Diogo Oliveira  - 1241883
                """);
    }

    /**
     * Method that asks for the file path to read until a valid path is given
     *
     * @return String with the file path
     */
    public static String selectFileToRead() {
        Scanner scanner = new Scanner(System.in);
        String filePath = null;
        boolean isValid = false;
        System.out.println("Introduza o caminho para o ficheiro: ");
        while (!isValid) {
            filePath = scanner.nextLine();
            File file = new File(filePath);

            if (file.exists() && file.isFile() && filePath.endsWith(".csv")) {
                isValid = true;
            } else {
                System.out.println("Caminho inválido. Introduza novamente: ");
            }
        }
        return filePath;
    }

    /**
     * Method that asks for the folder path to read until a valid path is given
     *
     * @return String with the folder path
     */
    public static String selectFolderToRead() {
        Scanner scanner = new Scanner(System.in);
        String folderPath = null;
        boolean isValid = false;

        while (!isValid) {
            System.out.println("Introduza o caminho para a pasta: ");
            folderPath = scanner.nextLine();
            File folder = new File(folderPath);

            if (folder.exists() && folder.isDirectory()) {
                isValid = true;
            } else {
                System.out.println("Caminho inválido. Por favor introduza um caminho válido.");
            }
        }
        return folderPath;
    }

    /**
     * Method that prints a given vector on the console
     *
     * @param vector vector to print
     * @param title  title of the vector
     */
    public static void printVector(double[] vector, String title) {
        if (!title.isEmpty()) {
            System.out.print("\n" + title + "\n[");
        } else {
            System.out.print("[");
        }

        for (int i = 0; i < vector.length; i++) {
            System.out.printf("%.2f", vector[i]);
            if (i < vector.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }


    /**
     * Function to print a matrix (double[][]) for testing and viewing purposes
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

    /**
     * Print the line division between unitary tests
     */
    public static void drawLine() {
        System.out.println("══════════════════════════════════════════");
    }
}
