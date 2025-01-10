package Tests;

import Utils.NonInteractive_Utils;
import Utils.UI_Utils;

public class NonInteractive_Utils_Test {
    public static final String VALID_FOLDER_PATH = "resources/exemploMatrizesImagensCSVFuncionalidades2e3";
    public static final String INVALID_FOLDER_PATH = "path/to/invalid/folder";
    public static final String VALID_FILE_PATH = "resources/exampleInputFunc1.csv";
    public static final String INVALID_FILE_PATH = "path/to/invalid/file.txt";
    public static final String VALID_FOLDER_PATH_BUT_NO_CSV = "resources/test_data/not_empty_no_csv_files";
    public static final String VALID_PATH_BUT_NOT_CSV = "resources/test_data/not_empty_no_csv_files/this_is_not_csv.txt";

    public static void main(String[] args) {
        testValidateK_ValidK();
        testValidateK_InvalidK_Negative();
        testValidateK_InvalidK_Zero();
        testValidateK_InvalidK_NonNumeric();
        testValidateK_PositiveGreaterThanOne();
        testValidateK_NegativeNotMinusOne();
        testSelectFolderToRead_ValidFolder();
        testSelectFolderToRead_InvalidFolder();
        testValidateFilePath_ValidFile();

        testValidateFilePath_InvalidFile();
        testSelectFolderToRead_NoCSVFiles();
        testValidateFilePath_WithoutCSVFile();
    }
    public static void testSelectFolderToRead_NoCSVFiles() {
        UI_Utils.showTestTitle("testSelectFolderToRead_NoCSVFiles");
        boolean expected = false;
        boolean actual = NonInteractive_Utils.selectFolderToRead(VALID_FOLDER_PATH_BUT_NO_CSV);
        UI_Utils.showTestResult(expected == actual);
    }

    public static void testValidateFilePath_WithoutCSVFile() {
        UI_Utils.showTestTitle("testValidateFilePath_ExistingNonCSVFile");
        boolean expected = false;
            boolean actual = NonInteractive_Utils.validateFilePath(VALID_PATH_BUT_NOT_CSV);
        UI_Utils.showTestResult(expected == actual);
    }
    public static void testValidateFilePath_InvalidFile() {
        UI_Utils.showTestTitle("testValidateFilePath_InvalidFile");
        boolean expected = false;
        boolean actual = NonInteractive_Utils.validateFilePath(INVALID_FILE_PATH);
        UI_Utils.showTestResult(expected == actual);
    }
    public static void testValidateFilePath_ValidFile() {
        UI_Utils.showTestTitle("testValidateFilePath_ValidFile");

        boolean expected = true;
        boolean actual = NonInteractive_Utils.validateFilePath(VALID_FILE_PATH);
        UI_Utils.showTestResult(expected == actual);
    }
    public static void testSelectFolderToRead_InvalidFolder() {
        UI_Utils.showTestTitle("testSelectFolderToRead_InvalidFolder");

        boolean expected = false;
        boolean actual = NonInteractive_Utils.selectFolderToRead(INVALID_FOLDER_PATH);
        UI_Utils.showTestResult(expected == actual);
    }
    public static void testSelectFolderToRead_ValidFolder() {
        UI_Utils.showTestTitle("testSelectFolderToRead_ValidFolder");
        boolean expected = true;
        boolean actual = NonInteractive_Utils.selectFolderToRead(VALID_FOLDER_PATH);
        UI_Utils.showTestResult(expected == actual);
    }

    public static void testValidateK_ValidK() {
        UI_Utils.showTestTitle("testValidateK_ValidK");
        boolean expected = true;
        boolean actual = NonInteractive_Utils.validateK("5");
        UI_Utils.showTestResult(expected == actual);
    }

    public static void testValidateK_InvalidK_Negative() {
        UI_Utils.showTestTitle("testValidateK_InvalidK_Negative");
        boolean expected = false;
        boolean actual = NonInteractive_Utils.validateK("-2");
        UI_Utils.showTestResult(expected == actual);
    }

    public static void testValidateK_InvalidK_Zero() {
        UI_Utils.showTestTitle("testValidateK_InvalidK_Zero");
        boolean expected = false;
        boolean actual = NonInteractive_Utils.validateK("0");
        UI_Utils.showTestResult(expected == actual);
    }
    public static void testValidateK_InvalidK_NonNumeric() {
        UI_Utils.showTestTitle("testValidateK_InvalidK_NonNumeric");
        boolean expected = false;
        boolean actual = NonInteractive_Utils.validateK("abc");
        UI_Utils.showTestResult(expected == actual);
    }
    public static void testValidateK_PositiveGreaterThanOne() {
        UI_Utils.showTestTitle("testValidateK_PositiveGreaterThanOne");
        boolean expected = true;
        boolean actual = NonInteractive_Utils.validateK("10");
        UI_Utils.showTestResult(expected == actual);
    }
    public static void testValidateK_NegativeNotMinusOne() {
        UI_Utils.showTestTitle("testValidateK_NegativeNotMinusOne");
        boolean expected = false;
        boolean actual = NonInteractive_Utils.validateK("-5");
        UI_Utils.showTestResult(expected == actual);
    }



}