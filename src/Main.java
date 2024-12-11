import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int menuOption;

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
                    //TODO: Implementar esta funcionalidade
                    System.out.println("Functionality 1");
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
}