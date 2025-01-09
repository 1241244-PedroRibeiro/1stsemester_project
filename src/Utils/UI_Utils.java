package Utils;

import jdk.swing.interop.SwingInterOpUtils;

import java.io.File;
import java.util.Scanner;

public class UI_Utils {

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
        System.out.print("     Choose Option: ");
    }

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

    public static void showTestTitle(String testName) {
        System.out.print("""
                ════════════════════
                Test:""");
        System.out.println(" " + testName);
    }

    public static void showTestResult(boolean result) {
        if (result) {
            System.out.println("Result: Passed!");
        } else {
            System.out.println("Result: Failed!");
        }
        System.out.println("════════════════════");
    }

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

    public static void printVector(double[] vector, String title) {
        if (!title.isEmpty()) System.out.print("\n" + title + "\n[");
        for (int i = 0; i < vector.length; i++) {
            System.out.printf("%.2f", vector[i]);
            if (i < vector.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public static String saveVector(double[] vector, String title) {
        StringBuilder result = new StringBuilder();

        if (!title.isEmpty()) {
            result.append("\n").append(title).append("\n").append("[");
        } else {
            result.append("[");
        }

        for (int i = 0; i < vector.length; i++) {
            result.append(String.format("%.2f", vector[i]));
            if (i < vector.length - 1) {
                result.append(", ");
            }
        }
        result.append("]");

        return result.toString();
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

    public static String saveMatrix(double[][] matrix, String title) {
        StringBuilder result = new StringBuilder();
        if (!title.isEmpty()) {
            result.append("\n").append(title).append("\n[");
        } else {
            result.append("[");
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result.append(String.format("%.2f ", matrix[i][j]));
            }
            result.append("\n");
        }

        return result.toString();
    }

}
