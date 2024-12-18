import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class File_Utils {
    public static File[] getCsvFiles(String folderPath) {
        try {
            File folder = new File(folderPath);

            if (!folder.exists() || !folder.isDirectory()) {
                throw new IOException("Pasta não encontrada: " + folderPath);
            }

            // Filtra arquivos CSV manualmente
            File[] files = folder.listFiles();
            if (files == null || files.length == 0) {
                throw new IOException("A pasta está vazia ou não contém arquivos válidos: " + folderPath);
            }

            // Contagem de arquivos CSV
            int csvCount = 0;
            for (File file : files) {
                if (file.getName().endsWith(".csv")) {
                    csvCount++;
                }
            }

            if (csvCount == 0) {
                throw new IOException("Nenhum arquivo CSV encontrado na pasta: " + folderPath);
            }

            // Cria um array para armazenar os arquivos CSV
            File[] csvFiles = new File[csvCount];
            int index = 0;
            for (File file : files) {
                if (file.getName().endsWith(".csv")) {
                    csvFiles[index++] = file;
                }
            }

            return csvFiles;

        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return new File[0];
        }
    }

    public static int getNumberOfLines(File csvFile) {
        int lineCount = 0;
        try (Scanner scanner = new Scanner(csvFile)) {
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                lineCount++;
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + csvFile.getName() + " - " + e.getMessage());
        }
        return lineCount;
    }

    public static double[][] processCsvFiles(File[] arquivos, int tamanhoMatriz) {
        double[][] matrizVetores = new double[tamanhoMatriz * tamanhoMatriz][arquivos.length];

        for (int i = 0; i < arquivos.length; i++) {
            File arquivoCSV = arquivos[i];
            try {
                double[] vetor = converterCSVparaVetor(arquivoCSV, tamanhoMatriz);

                for (int j = 0; j < vetor.length; j++) {
                    matrizVetores[j][i] = vetor[j];
                }
            } catch (IOException e) {
                System.err.println("Erro ao processar o arquivo: " + arquivoCSV.getName());
                e.printStackTrace();
            }
        }
        return matrizVetores;
    }

    public static double[] converterCSVparaVetor(File arquivoCSV, int tamanhoMatriz) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(arquivoCSV));

        double[] vetor = new double[tamanhoMatriz * tamanhoMatriz];
        int index = 0;

        String linha;

        while ((linha = br.readLine()) != null) {
            if (linha.trim().isEmpty()) {
                continue;
            }

            String[] elementos = linha.split(",");

            for (String elemento : elementos) {
                if (index < vetor.length) {
                    vetor[index] = Double.parseDouble(elemento.trim());
                    index++;
                }
            }
        }
        br.close();

        if (index != tamanhoMatriz * tamanhoMatriz) {
            throw new IllegalArgumentException("O número de elementos no CSV não corresponde ao tamanho da matriz especificado.");
        }
        return vetor;
    }

    public static double[][] getMatrixFromCSV(String caminhoCsv) {
        double[][] matrix = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoCsv))) {
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
            BufferedReader newReader = new BufferedReader(new FileReader(caminhoCsv));

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
            System.out.println("Erro - leitura do ficheiro CSV foi feita incorretamente: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro - leitura realizada incorretamente: " + e.getMessage());
        }
        return matrix;
    }

    public static void saveMatrixToCSV(double[][] matrix, String output_path) {
        if (matrix == null || matrix.length == 0) {
            System.out.println("\nMatriz inválida. Não foi possível salvar no CSV.");
            return;
        }


        File outputDir = new File(output_path);
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
