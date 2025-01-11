package Utils;

import java.io.*;
import java.util.Scanner;

/**
 * Class that contains utility methods for file operations.
 */
public class File_Utils {
    /**
     * Method to get the CSV files from a folder.
     * @param folderPath The path to the folder.
     * @return An array of CSV files.
     */
    public static File[] getCsvFiles(String folderPath) {
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Erro: Pasta não encontrada ou não é um diretório: " + folderPath);
            return new File[0];
        }

        // Filter CSV files manually
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("Erro: A pasta está vazia ou não contém arquivos válidos: " + folderPath);
            return new File[0];
        }

        // CSV file count
        int csvCount = 0;
        for (File file : files) {
            if (file.getName().endsWith(".csv")) {
                csvCount++;
            }
        }

        if (csvCount == 0) {
            System.out.println("Erro: Nenhum arquivo CSV encontrado na pasta: " + folderPath);
            return new File[0];
        }

        // Create an array to store CSV files
        File[] csvFiles = new File[csvCount];
        int index = 0;
        for (File file : files) {
            if (file.getName().endsWith(".csv")) {
                csvFiles[index++] = file;
            }
        }

        return csvFiles;
    }

    /**
     * Method to get the number of lines in a CSV file.
     * @param csvFile The CSV file.
     * @return The number of lines in the CSV file.
     */
    public static int getNumberOfLines(File csvFile) {
        int lineCount = 0;
        try (Scanner scanner = new Scanner(csvFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    lineCount++;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + csvFile.getName() + " - " + e.getMessage());
        }
        return lineCount;
    }

    /**
     * Method to process CSV files and convert them to a matrix of vectors, where each column is a vector.
     * @param files The CSV files.
     * @param matrixSize The size of the matrix.
     * @return A matrix of vectors.
     */
    public static double[][] processCsvFiles(File[] files, int matrixSize) {
        double[][] matrixVectors = new double[matrixSize * matrixSize][files.length];

        for (int i = 0; i < files.length; i++) {
            File fileCSV = files[i];
            try {
                double[] vector = convertCSVtoVector(fileCSV, matrixSize);

                for (int j = 0; j < vector.length; j++) {
                    matrixVectors[j][i] = vector[j];
                }
            } catch (IOException e) {
                System.err.println("Erro ao processar o arquivo: " + fileCSV.getName());
                e.printStackTrace();
            }
        }
        return matrixVectors;
    }

    /**
     * Method to convert a CSV file to a vector.
     * @param fileCSV The CSV file.
     * @param matrixSize The size of the matrix.
     * @return The converted vector.
     * @throws IOException If an I/O error occurs.
     */
    public static double[] convertCSVtoVector(File fileCSV, int matrixSize) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileCSV));

        double[] array = new double[matrixSize * matrixSize];
        int index = 0;

        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] elements = line.split(",");

            for (String element : elements) {
                if (index < array.length) {
                    array[index] = Double.parseDouble(element.trim());
                    index++;
                }
            }
        }
        br.close();

        if (index != matrixSize * matrixSize) {
            throw new IllegalArgumentException("O número de elementos no CSV não corresponde ao tamanho da matriz especificado.");
        }
        return array;
    }

    /**
     * Method to read a matrix from a CSV file.
     * @param csvPath The path to the CSV file.
     * @return The matrix read from the CSV file.
     */
    public static double[][] getMatrixFromCSV(String csvPath) {
        double[][] matrix = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            String line = reader.readLine();
            if (line == null) {
                System.out.println("Erro - O CSV está vazio.");
                return null;
            }

            String[] values = line.split(",");
            int columnCount = values.length;

            int rowCount = 1;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    rowCount++;
                }
            }

            reader.close();
            BufferedReader newReader = new BufferedReader(new FileReader(csvPath));

            matrix = new double[rowCount][columnCount];
            int currentRow = 0;

            while ((line = newReader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] rowValues = line.split(",");
                    if (rowValues.length != columnCount) {
                        System.out.println("Erro - número de colunas na linha " + (currentRow + 1) + " não corresponde ao esperado.");
                        System.out.println("A matriz não é quadrada.");
                        return null;
                    }

                    for (int i = 0; i < columnCount; i++) {
                        try {
                            matrix[currentRow][i] = Double.parseDouble(rowValues[i].trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Erro ao converter valor - Linha " + (currentRow + 1) + ", Coluna " + (i + 1) + ": '" + rowValues[i] + "'");
                        }
                    }
                    currentRow++;
                }
            }

            newReader.close();

        } catch (IOException e) {
            System.out.println("Erro – a leitura do arquivo CSV estava incorreta: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro - a leitura foi feita incorretamente: " + e.getMessage());
        }
        return matrix;
    }

    /**
     * Method to save a matrix to a CSV file.
     * @param matrix The matrix to save.
     * @param outputPath The path to save the CSV file.
     */
    public static void saveMatrixToCSV(double[][] matrix, String outputPath) {
        if (matrix == null || matrix.length == 0) {
            System.out.println("\nMatriz inválida. Não foi possível salvar no CSV.");
            return;
        }

        File outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            if (outputDir.mkdir()) {
                System.out.println("Pasta criada com sucesso.");
            } else {
                System.out.println("Erro ao criar a pasta.");
                return;
            }
        }

        clearFolderFiles(outputDir);

        String outputFilename = "matriz_F1.csv";
        File outputFile = new File(outputDir, outputFilename);

        try (PrintWriter writer = new PrintWriter(outputFile)) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    writer.print(matrix[i][j]);
                    if (j < matrix[i].length - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
            }
            System.out.println("\nMatriz salva no arquivo CSV com sucesso: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Erro ao guardar a matriz no CSV: " + e.getMessage());
        }
    }

    /**
     * Method to clear the files in a folder.
     * @param outputFolder The folder to clear.
     */
    public static void clearFolderFiles(File outputFolder) {
        File[] files = outputFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.delete()) {
                    System.out.println("Erro ao eliminar ficheiro: " + file.getName());
                }
            }
        }
    }


}
