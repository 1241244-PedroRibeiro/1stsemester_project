import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //Declaração de Variáveis
        Scanner scanner = new Scanner(System.in);
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

}