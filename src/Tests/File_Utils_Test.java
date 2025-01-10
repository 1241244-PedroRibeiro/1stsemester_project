package Tests;

import Utils.File_Utils;
import Utils.UI_Utils;

import java.io.File;
import java.io.IOException;

public class File_Utils_Test {

    public static final String F1_CSV_FILE = "resources/exampleInputFunc1.csv";
    public static final String F2_CORRECT_TEST_DATA_FOLDER = "resources/exemploMatrizesImagensCSVFuncionalidades2e3";
    public static final String MIXED_FILES_FOLDER = "resources/test_data/mixed_files";
    public static final String EMPTY_FOLDER = "resources/test_data/empty_folder";
    public static final String INVALID_PATH = "invalid_path";
    public static final String EMPTY_TXT_TEST_FILE = "resources/test_data/empty_txt_test.csv";
    public static final String MATRIZES_DIFERENTES_TAMANHOS_FOLDER = "resources/test_data/matrizes_diferentes_tamanhos";
    public static final String MATRIZ_COM_LETRAS_FILE = "resources/test_data/teste_matrizes_funcionalidades2e3/resources/test_data/matriz_com_letras.csv";
    public static final String MATRIZ_NAO_QUADRADA_FILE = "resources/test_data/matriz_nao_quadrada.csv";
    public static final String MATRIZ_COM_LINHAS_EM_BRANCO_FILE = "resources/test_data/matriz_com_linhas_em_branco.csv";
    public static final String SAVE_TEST_FOLDER = "resources/test_data/save_test";

    public static void main(String[] args) {
        testGetCsvFiles();
        testGetNumberOfLines();
        testProcessCsvFiles();
        testConverterCSVparaVetor();
        testGetMatrixFromCSV();
        testSaveMatrixToCSV();
        testGetCsvFilesWithInvalidPath();
        testGetNumberOfLinesWithInvalidFile();
        testProcessCsvFilesWithEmptyFolder();
        testConverterCSVparaVetorWithInvalidFile();
        testGetMatrixFromCSVWithInvalidFile();
        testGetCsvFilesWithNonCsvFiles();
        testGetNumberOfLinesWithEmptyFile();
        testProcessCsvFilesWithDifferentSizes();
        testConverterCSVparaVetorWithNonNumericValues();
        testGetMatrixFromCSVWithDifferentLineSizes();
        testGetCsvFilesWithMixedFiles();
        testGetNumberOfLinesWithBlankLines();
    }

    public static void testGetCsvFiles() {
        UI_Utils.showTestTitle("testGetCsvFiles");
        String folderPath = F2_CORRECT_TEST_DATA_FOLDER;
        File[] csvFiles = File_Utils.getCsvFiles(folderPath);
        boolean result = csvFiles.length > 0 && csvFiles[0].getName().endsWith(".csv");
        UI_Utils.showTestResult(result);
    }

    public static void testGetNumberOfLines() {
        UI_Utils.showTestTitle("testGetNumberOfLines");
        File csvFile = new File(F1_CSV_FILE);
        int expectedLines = 8;
        int actualLines = File_Utils.getNumberOfLines(csvFile);
        boolean result = Compare.compareInt(expectedLines, actualLines);
        UI_Utils.showTestResult(result);
    }

    public static void testProcessCsvFiles() {
        UI_Utils.showTestTitle("testProcessCsvFiles");
        String folderPath = F2_CORRECT_TEST_DATA_FOLDER;
        File[] csvFiles = File_Utils.getCsvFiles(folderPath);
        int tamanhoMatriz = 3;
        double[][] matrizVetores = File_Utils.processCsvFiles(csvFiles, tamanhoMatriz);
        boolean result = matrizVetores.length == tamanhoMatriz * tamanhoMatriz && matrizVetores[0].length == csvFiles.length;
        UI_Utils.showTestResult(result);
    }

    public static void testConverterCSVparaVetor() {
        UI_Utils.showTestTitle("testConverterCSVparaVetor");
        File csvFile = new File(F1_CSV_FILE);
        int tamanhoMatriz = 8;
        try {
            double[] vetor = File_Utils.converterCSVtoVetor(csvFile, tamanhoMatriz);
            boolean result = vetor.length == tamanhoMatriz * tamanhoMatriz;
            UI_Utils.showTestResult(result);
        } catch (IOException e) {
            System.out.println("testConverterCSVparaVetor: Failed with exception " + e.getMessage());
        }
    }

    public static void testGetMatrixFromCSV() {
        UI_Utils.showTestTitle("testGetMatrixFromCSV");
        String caminhoCsv = F1_CSV_FILE;
        double[][] matrix = File_Utils.getMatrixFromCSV(caminhoCsv);
        boolean result = matrix != null && matrix.length > 0 && matrix[0].length > 0;
        UI_Utils.showTestResult(result);
    }

    public static void testSaveMatrixToCSV() {
        UI_Utils.showTestTitle("testSaveMatrixToCSV");
        double[][] matrix = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        String outputPath = SAVE_TEST_FOLDER;
        File_Utils.saveMatrixToCSV(matrix, outputPath);
        File outputFile = new File(outputPath + "/matriz_F1.csv");
        boolean result = outputFile.exists();
        UI_Utils.showTestResult(result);
    }

    public static void testGetCsvFilesWithInvalidPath() {
        UI_Utils.showTestTitle("testGetCsvFilesWithInvalidPath");
        String folderPath = INVALID_PATH;
        File[] csvFiles = File_Utils.getCsvFiles(folderPath);
        boolean result = csvFiles.length == 0;
        UI_Utils.showTestResult(result);
    }

    public static void testGetNumberOfLinesWithInvalidFile() {
        UI_Utils.showTestTitle("testGetNumberOfLinesWithInvalidFile");
        File csvFile = new File(F1_CSV_FILE + "/invalid_path");
        int actualLines = File_Utils.getNumberOfLines(csvFile);
        boolean result = actualLines == 0;
        UI_Utils.showTestResult(result);
    }

    public static void testProcessCsvFilesWithEmptyFolder() {
        UI_Utils.showTestTitle("testProcessCsvFilesWithEmptyFolder");
        String folderPath = EMPTY_FOLDER;
        File[] csvFiles = File_Utils.getCsvFiles(folderPath);
        boolean result = csvFiles.length == 0;
        UI_Utils.showTestResult(result);
    }

    public static void testConverterCSVparaVetorWithInvalidFile() {
        UI_Utils.showTestTitle("testConverterCSVparaVetorWithInvalidFile");
        File csvFile = new File(INVALID_PATH + "/sample.csv");
        int tamanhoMatriz = 3;
        try {
            double[] vetor = File_Utils.converterCSVtoVetor(csvFile, tamanhoMatriz);
            System.out.println("Result: Failed!");
        } catch (IOException e) {
            System.out.println("Result: Passed!");
        }
    }

    public static void testGetMatrixFromCSVWithInvalidFile() {
        UI_Utils.showTestTitle("testGetMatrixFromCSVWithInvalidFile");
        String caminhoCsv = INVALID_PATH + "/any.csv";
        double[][] matrix = File_Utils.getMatrixFromCSV(caminhoCsv);
        boolean result = matrix == null;
        UI_Utils.showTestResult(result);
    }

    public static void testGetCsvFilesWithNonCsvFiles() {
        UI_Utils.showTestTitle("testGetCsvFilesWithNonCsvFiles");
        String folderPath = "resources/test_data/not_empty_no_csv_files";
        File[] csvFiles = File_Utils.getCsvFiles(folderPath);
        boolean result = csvFiles.length == 0;
        UI_Utils.showTestResult(result);
    }

    public static void testGetNumberOfLinesWithEmptyFile() {
        UI_Utils.showTestTitle("testGetNumberOfLinesWithEmptyFile");
        File csvFile = new File(EMPTY_TXT_TEST_FILE);
        int actualLines = File_Utils.getNumberOfLines(csvFile);
        boolean result = actualLines == 0;
        UI_Utils.showTestResult(result);
    }

    public static void testProcessCsvFilesWithDifferentSizes() {
        UI_Utils.showTestTitle("testProcessCsvFilesWithDifferentSizes");
        String folderPath = MATRIZES_DIFERENTES_TAMANHOS_FOLDER;
        File[] csvFiles = File_Utils.getCsvFiles(folderPath);
        int tamanhoMatriz = 64;
        double[][] matrizVetores = File_Utils.processCsvFiles(csvFiles, tamanhoMatriz);
        boolean result = matrizVetores.length == tamanhoMatriz * tamanhoMatriz && matrizVetores[0].length == csvFiles.length;
        UI_Utils.showTestResult(result);
    }

    public static void testConverterCSVparaVetorWithNonNumericValues() {
        UI_Utils.showTestTitle("testConverterCSVparaVetorWithNonNumericValues");
        File csvFile = new File(MATRIZ_COM_LETRAS_FILE);
        int tamanhoMatriz = 3;
        try {
            double[] vetor = File_Utils.converterCSVtoVetor(csvFile, tamanhoMatriz);
            System.out.println("Result: Failed!");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Result: Passed!");
        }
    }

    public static void testGetMatrixFromCSVWithDifferentLineSizes() {
        UI_Utils.showTestTitle("testGetMatrixFromCSVWithDifferentLineSizes");
        String caminhoCsv = MATRIZ_NAO_QUADRADA_FILE;
        double[][] matrix = File_Utils.getMatrixFromCSV(caminhoCsv);
        boolean result = matrix == null;
        UI_Utils.showTestResult(result);
    }

    public static void testGetCsvFilesWithMixedFiles() {
        UI_Utils.showTestTitle("testGetCsvFilesWithMixedFiles");
        File[] csvFiles = File_Utils.getCsvFiles(MIXED_FILES_FOLDER);
        boolean result = csvFiles.length > 0 && csvFiles[0].getName().endsWith(".csv");
        UI_Utils.showTestResult(result);
    }

    public static void testGetNumberOfLinesWithBlankLines() {
        UI_Utils.showTestTitle("testGetNumberOfLinesWithBlankLines");
        File csvFile = new File(MATRIZ_COM_LINHAS_EM_BRANCO_FILE);
        int expectedLines = 8;
        int actualLines = File_Utils.getNumberOfLines(csvFile);
        boolean result = Compare.compareInt(expectedLines, actualLines);
        UI_Utils.showTestResult(result);
    }
}