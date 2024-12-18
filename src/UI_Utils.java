import java.io.File;
import java.util.Scanner;

public class UI_Utils {

    public static void showMenuOptions() {
        System.out.println("""
                
                  ╔═════════════════════════════════════════════════════════════════════════╗
                  ║                               MENU OPTIONS                              ║
                  ╠═════════════════════════════════════════════════════════════════════════╣
                  ║                                                                         ║
                  ║    1. Decomposição Própria de Matriz Simétrica                          ║
                  ║    2. Reconstrução e representação de imagens utilizando EigenFaces     ║
                  ║    3. Identificação de Imagens utilizando EigenFaces                    ║
                  ║    4. Know Delevopment Team                                             ║
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
                          ║                         4. Know Delevopment Team                        ║
                          ╚═════════════════════════════════════════════════════════════════════════╝
                        """);
                break;
        }
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

        while (!isValid) {
            System.out.println("Enter the path of the file: ");
            filePath = scanner.nextLine();
            File file = new File(filePath);

            if (file.exists() && file.isFile() && filePath.endsWith(".csv")) {
                isValid = true;
            } else {
                System.out.println("Invalid file path. Ensure the file exist and has a .csv extension.");
            }
        }
        return filePath;
    }

    public static String selectFolderToRead() {
        Scanner scanner = new Scanner(System.in);
        String folderPath = null;
        boolean isValid = false;

        while (!isValid) {
            System.out.println("Enter the path of the folder: ");
            folderPath = scanner.nextLine();
            File folder = new File(folderPath);

            if (folder.exists() && folder.isDirectory()) {
                isValid = true;
            } else {
                System.out.println("Invalid folder path. Please enter a valid path.");
            }
        }
        return folderPath;
    }

    public static void printVetorMedia(double[] mediaImagens) {
        System.out.print("\nVetor µ: [");
        for (int i = 0; i < mediaImagens.length; i++) {
            System.out.print(String.format("%.2f", mediaImagens[i]).replace(',', '.'));
            if (i < mediaImagens.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
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
}
