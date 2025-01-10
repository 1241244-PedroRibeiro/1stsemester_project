package Utils;

import java.io.File;

public class NonInteractive_Utils {

    public static boolean validateK(String kArgument) {
        try {
            int k = Integer.parseInt(kArgument);

            if (k < -1 || k == 0) {
                System.out.println("O valor de K introduzido (" + k + ") é inválido.");
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Insira um número");
            return false;
        }
    }

    public static boolean selectFolderToRead(String pathToFolder) {

        boolean isValid = false;
        File folder = new File(pathToFolder);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files == null || files.length == 0) {
                System.out.println("Erro: A pasta especificada vazia");
                return false;
            }

            int csvCount = 0;
            for (File file : files) {
                if (file.getName().endsWith(".csv")) {
                    csvCount++;
                }
            }

            if (csvCount == 0) {
                System.out.println("Erro: Nenhum arquivo CSV encontrado na pasta: " + pathToFolder);
                return false;
            }

            isValid = true;
        } else {
            System.out.println("Caminho de pasta inválido. Por favor, insira um caminho válido.");
        }
        return isValid;
    }

    public static boolean validateFilePath(String filePath) {
        File file = new File(filePath);

        if (file.exists() && file.isFile() && filePath.endsWith(".csv")) {
            return true;
        } else {
            System.out.println("Caminho de arquivo inválido. Por favor, insira um caminho válido.");
            return false;
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
            if(i==matrix.length-1){
                result.append("]");
            }
            result.append("\n");
        }
        return result.toString();
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


}
