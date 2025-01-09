package Utils;

import java.io.*;
import java.util.Scanner;

public class File_Utils {
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

    public static double[][] processCsvFiles(File[] files, int matrixSize) {
        double[][] matrixVectors = new double[matrixSize * matrixSize][files.length];

        for (int i = 0; i < files.length; i++) {
            File fileCSV = files[i];
            try {
                double[] vector = converterCSVtoVetor(fileCSV, matrixSize);

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

    public static double[] converterCSVtoVetor(File fileCSV, int matrixSize) throws IOException {
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
                        System.out.println("Erro - número de colunas na linha" + (currentRow + 1) + " não corresponde ao esperado.");
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


    public static void saveMatrixToCSV(double[][] matrix, String outputPath) {
        if (matrix == null || matrix.length == 0) {
            System.out.println("\nMatriz inválida. Não foi possível salvar no CSV.");
            return;
        }

        File outputDir = new File(outputPath);
        System.out.println(outputDir);
        if (!outputDir.exists()) {
            if (outputDir.mkdir()) {
                System.out.println("Pasta criada com sucesso.");
            } else {
                System.out.println("Erro ao criar a pasta.");
                return;
            }
        }

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




}
