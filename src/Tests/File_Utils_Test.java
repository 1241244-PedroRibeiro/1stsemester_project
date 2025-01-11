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
        testConvertCSVtoVector();
        testGetMatrixFromCSV();
        testSaveMatrixToCSV();
        testGetCsvFilesWithInvalidPath();
        testGetNumberOfLinesWithInvalidFile();
        testProcessCsvFilesWithEmptyFolder();
        testConvertCSVtoVectorWithInvalidFile();
        testGetMatrixFromCSVWithInvalidFile();
        testGetCsvFilesWithNonCsvFiles();
        testGetNumberOfLinesWithEmptyFile();
        testProcessCsvFilesWithDifferentSizes();
        testConvertCSVtoVectorWithNonNumericValues();
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
        int matrixSize = 3;
        double[][] matrixVectors = File_Utils.processCsvFiles(csvFiles, matrixSize);
        boolean result = matrixVectors.length == matrixSize * matrixSize && matrixVectors[0].length == csvFiles.length;
        UI_Utils.showTestResult(result);
    }

    public static void testConvertCSVtoVector() {
        UI_Utils.showTestTitle("testConvertCSVtoVector");
        File csvFile = new File(F1_CSV_FILE);
        int matrixSize = 8;
        try {
            double[] vector = File_Utils.convertCSVtoVector(csvFile, matrixSize);
            boolean result = vector.length == matrixSize * matrixSize;
            UI_Utils.showTestResult(result);
        } catch (IOException e) {
            System.out.println("testConvertCSVtoVector: Failed with exception " + e.getMessage());
        }
    }

    public static void testGetMatrixFromCSV() {
        UI_Utils.showTestTitle("testGetMatrixFromCSV");
        String csvPath = F1_CSV_FILE;
        double[][] matrix = File_Utils.getMatrixFromCSV(csvPath);
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

    public static void testConvertCSVtoVectorWithInvalidFile() {
        UI_Utils.showTestTitle("testConvertCSVtoVectorWithInvalidFile");
        File csvFile = new File(INVALID_PATH + "/sample.csv");
        int matrixSize = 3;
        try {
            double[] vector = File_Utils.convertCSVtoVector(csvFile, matrixSize);
            System.out.println("Result: Falhou!");
            UI_Utils.drawLine();
        } catch (IOException e) {
            System.out.println("Result: Aprovado!");
            UI_Utils.drawLine();
        }
    }

    public static void testGetMatrixFromCSVWithInvalidFile() {
        UI_Utils.showTestTitle("testGetMatrixFromCSVWithInvalidFile");
        String csvPath = INVALID_PATH + "/any.csv";
        double[][] matrix = File_Utils.getMatrixFromCSV(csvPath);
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
        int matrixSize = 64;
        double[][] matrixVectors = File_Utils.processCsvFiles(csvFiles, matrixSize);
        boolean result = matrixVectors.length == matrixSize * matrixSize && matrixVectors[0].length == csvFiles.length;
        UI_Utils.showTestResult(result);
    }

    public static void testConvertCSVtoVectorWithNonNumericValues() {
        UI_Utils.showTestTitle("testConvertCSVtoVectorWithNonNumericValues");
        File csvFile = new File(MATRIZ_COM_LETRAS_FILE);
        int matrixSize = 3;
        try {
            double[] vector = File_Utils.convertCSVtoVector(csvFile, matrixSize);
            System.out.println("Result: Falhou!");
            UI_Utils.drawLine();
        } catch (IOException | NumberFormatException e) {
            System.out.println("Result: Aprovado!");
            UI_Utils.drawLine();
        }
    }

    public static void testGetMatrixFromCSVWithDifferentLineSizes() {
        UI_Utils.showTestTitle("testGetMatrixFromCSVWithDifferentLineSizes");
        String csvPath = MATRIZ_NAO_QUADRADA_FILE;
        double[][] matrix = File_Utils.getMatrixFromCSV(csvPath);
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