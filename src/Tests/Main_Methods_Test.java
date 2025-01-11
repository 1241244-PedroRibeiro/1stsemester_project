package Tests;

import Domain.Main;
import Utils.UI_Utils;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

import static Domain.Main.*;
import static Tests.Compare.*;


public class Main_Methods_Test {

    public static void main(String[] args) {
        testValidateMatrix();
        testCalculateEAM();
        testCalculateEAM_value_0444();
        testValidateMatrixNotSquare();
        testaskNumberOfEigenVectors();
        testValidateMatrixWithNegativeValues();
        testValidateMatrixNotSymmetrical();
        testCalcularPeso();
        testGetVectorPhiOnPosition();
        testSaveImageAsPNG();
        testCalcularVetoresPropriosC();
        testOrdenarVetoresProprios();
        testObterVetoresProprios();
        testNormalizarVetoresC();
        testaskNumberOfEigenVectors_ValidInput();
        testaskNumberOfEigenVectors_UseAllEigenvectors();
        testaskNumberOfEigenVectors_InvalidNegativeInput();
        testaskNumberOfEigenVectors_InvalidLargeInput();
        testCalculateEuclideanDistance();
        testCalculateEuclideanDistanceBiggerVectors();
        testValidateVectorPixels_Valid();
        testValidateVectorPixels_Invalid();
        testCalculateEAM_Vector();
        testCalculateEAM_VectorBigger();
        testGetImageAsJPG();
        testPixelScalingEigenFaces();
        testCalculateMatrixA();
        testNormalizeVectorsCLargerMatrix();
        testOwnDecomposition();
        testSaveIntMatrixToCSV();
        testCalculateEigenVectorsCLargerMatrix();
        testCompressedMatrix();

    }

    public static void testOwnDecomposition() {
        UI_Utils.showTestTitle("testOwnDecomposition");

        double[][] inputMatrix = {
                {4, -2},
                {-2, 1}
        };

        // Passo 1: Calcular os valores próprios (eigenvalues) da matriz
        // A matriz de entrada é:
        // | 4  -2 |
        // | -2  1 |
        // Os valores próprios são as raízes do polinômio característico det(A - λI) = 0
        // det | 4-λ  -2   |
        //     | -2   1-λ  |
        // (4-λ)(1-λ) - (-2)(-2) = 0
        // λ^2 - 5λ = 0
        // λ(λ - 5) = 0
        // λ1 = 5, λ2 = 0

        // Passo 2: Calcular os vetores próprios (eigenvectors) correspondentes
        // Para λ1 = 5:
        // (A - 5I)v = 0
        // | 4-5  -2 | | x |   | -1 -2 | | x |   | 0 |
        // | -2  1-5 | | y | = | -2 -4 | | y | = | 0 |
        // -x - 2y = 0  =>  x = -2y
        // Vetor próprio correspondente: v1 = [2, -1]

        // Para λ2 = 0:
        // (A - 0I)v = 0
        // | 4  -2 | | x |   | 4 -2 | | x |   | 0 |
        // | -2  1 | | y | = | -2  1 | | y | = | 0 |
        // 4x - 2y = 0  =>  y = 2x
        // Vetor próprio correspondente: v2 = [1, 2]

        // Passo 3: Normalizar os vetores próprios
        // v1 = [2, -1] / sqrt(2^2 + (-1)^2) = [2, -1] / sqrt(5) ≈ [0.8944271909999159, -0.4472135954999579]
        // v2 = [1, 2] / sqrt(1^2 + 2^2) = [1, 2] / sqrt(5) ≈ [0.4472135954999579, 0.8944271909999159]

        RealMatrix[] result = Main.ownDecomposition(inputMatrix);
        double[][] actualP = result[0].getData();
        double[][] actualD = result[1].getData();

        double[][] expectedP = {
                {0.8944271909999159, -0.4472135954999579},
                {-0.4472135954999579, -0.8944271909999159}
        };

        double[][] expectedD = {
                {5, 0},
                {0, 0}
        };

        boolean resultP = compareMatrixWithTolerance(expectedP, actualP, 0.0001);
        boolean resultD = compareMatrixWithTolerance(expectedD, actualD, 0.0001);

        UI_Utils.showTestResult(resultP && resultD);
    }

    public static void testCalculateMatrixA() {
        UI_Utils.showTestTitle("testCalculateMatrixA");

        double[][] matrixVectors = {
                {1, 2},
                {3, 4},
                {5, 6}
        };
        double[] averageImages = {2, 3, 4};
        double[][] expected = {
                {-1, 0},
                {0, 1},
                {1, 2}
        };

        double[][] actual = Main.calculateMatrixA(3, 2, matrixVectors, averageImages);

        boolean result = compareMatrix(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testPixelScalingEigenFaces() {
        UI_Utils.showTestTitle("testPixelScalingEigenFaces");

        double[] eigenFace = {-100.0, 0.0, 100.0, 200.0, 300.0, 400.0};
        double[] expected = {0.0, 51.0, 102.0, 153.0, 204.0, 255.0}; // Corrected expected values
        double[] actual = Main.pixelScalingEigenFaces(eigenFace);
        boolean result = compareVector(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testGetImageAsJPG() {
        UI_Utils.showTestTitle("testGetImageAsJPG");

        File csvFile = new File("resources/exampleInputFunc1.csv");
        String outputPath = "output/test_data/test_image.jpg";

        Main.getImageAsJPG(csvFile, outputPath);

        File outputFile = new File(outputPath);
        boolean result = outputFile.exists();
        UI_Utils.showTestResult(result);
    }


    public static void testValidateMatrix() {
        UI_Utils.showTestTitle("testValidateMatrix");

        double[][] validMatrix = {
                {1, 2, 3},
                {2, 4, 5},
                {3, 5, 6}
        };
        double[][] invalidMatrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        boolean resultValid = Main.validateMatrix(validMatrix);
        boolean resultInvalid = Main.validateMatrix(invalidMatrix);

        UI_Utils.showTestResult(resultValid && !resultInvalid);
    }

    public static void testValidateMatrixWithNegativeValues() {

        UI_Utils.showTestTitle("testValidateMatrixWithNegativeValues");

        double[][] invalidMatrix = {
                {1, 2, 3},
                {2, 5, 6},
                {3, 6, -9}
        };

        boolean resultInvalid = Main.validateMatrix(invalidMatrix);
        UI_Utils.showTestResult(!resultInvalid);
    }

    public static void testValidateMatrixNotSquare() {
        UI_Utils.showTestTitle("testValidateMatrixNotSquare");

        double[][] invalidMatrix = {
                {1, 2, 3},
                {4, 5, 6}
        };

        boolean resultInvalid = Main.validateMatrix(invalidMatrix);
        UI_Utils.showTestResult(!resultInvalid);
    }

    public static void testValidateMatrixNotSymmetrical() {

        UI_Utils.showTestTitle("testValidateMatrixNotSymmetrical");

        double[][] invalidMatrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        boolean resultInvalid = Main.validateMatrix(invalidMatrix);
        UI_Utils.showTestResult(!resultInvalid);
    }

    public static void testCalculateEAM() {
        UI_Utils.showTestTitle("testCalculateEAM");

        double[][] original = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double[][] reconstructed = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double expectedEAM = 0.0;
        double actualEAM = Main.calculateEAM(original, reconstructed);

        boolean result = expectedEAM == actualEAM;
        UI_Utils.showTestResult(result);
    }

    public static void testCalculateEAM_value_0444() {

        UI_Utils.showTestTitle("testCalculateEAM_value_3");

        double[][] original = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        double[][] reconstructed = {
                {1.6, 1.4, 3.2},
                {4.5, 4.2, 6.3},
                {6.9, 8.8, 8.9}
        };


        double expectedEAM = 0.44444;
        double actualEAM = Main.calculateEAM(original, reconstructed);
        actualEAM = Math.round(actualEAM * 100000.0) / 100000.0;
        System.out.println("Expected EAM: " + expectedEAM);
        System.out.println("Actual EAM: " + actualEAM);

        boolean result = expectedEAM == actualEAM;
        UI_Utils.showTestResult(result);
    }

    public static void testaskNumberOfEigenVectors() {
        UI_Utils.showTestTitle("testaskNumberOfEigenVectors");

        double[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        Main.scanner = new java.util.Scanner("2\n");
        int expectedK = 2;
        int actualK = Main.askNumberOfEigenVectors(matrix);

        boolean result = expectedK == actualK;
        UI_Utils.showTestResult(result);
    }


    public static double[][] generateSymmetricalMatrix(int size) {
        double[][] matrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                double randomValue = Math.random() * 100;
                matrix[i][j] = randomValue;
                matrix[j][i] = randomValue;
            }
        }
        return matrix;
    }


    public static void testCalcularPeso() {
        UI_Utils.showTestTitle("testCalcularPeso");

        double[] ujTransposto = {1.0, 2.0, 3.0};
        double[] phi = {4.0, 5.0, 6.0};
        double expectedPeso = 32.0; // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
        double actualPeso = Main.calculateWeight(ujTransposto, phi);

        boolean result = compareDouble(expectedPeso, actualPeso);
        UI_Utils.showTestResult(result);
    }

    public static void testSaveImageAsPNG() {
        UI_Utils.showTestTitle("testSaveImageAsPNG");

        double[] vetorImagem = {
                0, 50, 100,
                150, 200, 255,
                255, 200, 150
        };
        int tamanhoMatriz = 3;
        int contador = 1;
        String outputPath = "resources/test_data/save_test";

        File outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        Main.saveImageAsJPG(outputPath, vetorImagem, tamanhoMatriz, contador, "/imagem_reconstruida_");

        File outputFile = new File(outputPath + "/imagem_reconstruida_" + contador + ".jpg");
        boolean result = outputFile.exists();
        UI_Utils.showTestResult(result);
    }


    public static void testGetVectorPhiOnPosition() {
        UI_Utils.showTestTitle("testGetVectorPhiOnPosition");

        double[][] matrizA = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        int index = 1;
        double[] expected = {2.0, 5.0, 8.0};
        double[] actual = Main.getPhiInPosition(matrizA, index);

        boolean result = compareVector(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testCalcularVetoresPropriosC() {

        UI_Utils.showTestTitle("testCalcularVetoresPropriosC");
        double[][] matrizA = {
                {1, 2},
                {3, 4}
        };

        double[][] vetoresPropriosATA = {
                {0.5, 0.5},
                {0.5, 0.5}
        };

        int numEigenFaces = 2;

        double[][] expected = {
                {1.5, 1.5},
                {3.5, 3.5}
        };


        double[][] actual = Main.calculateEigenVectorsC(matrizA, vetoresPropriosATA, numEigenFaces);

        boolean result = compareMatrix(expected, actual);
        UI_Utils.showTestResult(result);

    }

    public static void testOrdenarVetoresProprios() {
        UI_Utils.showTestTitle("testOrdenarVetoresProprios");

        double[] valoresProprios = {3.0, 1.0, 2.0};
        double[][] vetoresProprios = {
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        };

        double[] expectedValoresProprios = {3.0, 2.0, 1.0};
        double[][] expectedVetoresProprios = {
                {1.0, 0.0, 0.0},
                {0.0, 0.0, 1.0},
                {0.0, 1.0, 0.0}
        };

        sortEigenvectors(valoresProprios, vetoresProprios);

        boolean result = compareVector(expectedValoresProprios, valoresProprios) &&
                compareMatrix(expectedVetoresProprios, vetoresProprios);
        UI_Utils.showTestResult(result);
    }

    public static void testObterVetoresProprios() {
        UI_Utils.showTestTitle("testObterVetoresProprios");

        double[][] matrizATA = {
                {4, 1},
                {1, 3}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrizATA);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(matrix);
        double[][] expectedVetoresProprios = eigenDecomposition.getV().getData();

        double[][] actualVetoresProprios = Main.getEigenVectors(matrizATA);

        boolean result = compareMatrix(expectedVetoresProprios, actualVetoresProprios);
        UI_Utils.showTestResult(result);
    }

    public static void testNormalizarVetoresC() {
        UI_Utils.showTestTitle("testNormalizarVetoresC");

        double[][] matrizVetoresPropriosC = {
                {1, 2},
                {3, 4}
        };

        double[][] expected = {
                {0.31622776601683794, 0.4472135954999579},
                {0.9486832980505138, 0.8944271909999159}
        };

        //For the first column: sqrt(1^2 + 3^2) = sqrt(1 + 9) = sqrt(10) ≈ 3.162277660168379
        //For the second column: sqrt(2^2 + 4^2) = sqrt(4 + 16) = sqrt(20) ≈ 4.47213595499958

        //Normalize each element of the array
        //First column: [1/3.162277660168379, 3/3.162277660168379] ≈ [0.31622776601683794, 0.9486832980505138]
        //Second column: [2/4.47213595499958, 4/4.47213595499958] ≈ [0.4472135954999579, 0.8944271909999159]

        double[][] actual = normalizeVectorsC(matrizVetoresPropriosC);

        boolean result = compareMatrix(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testaskNumberOfEigenVectors_ValidInput() {
        UI_Utils.showTestTitle("testaskNumberOfEigenVectors_ValidInput");

        double[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        String input = "2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Main.scanner = new java.util.Scanner(System.in);
        int expectedK = 2;
        int actualK = Main.askNumberOfEigenVectors(matrix);

        boolean result = compareInt(expectedK, actualK);
        UI_Utils.showTestResult(result);
    }

    public static void testaskNumberOfEigenVectors_UseAllEigenvectors() {
        UI_Utils.showTestTitle("testaskNumberOfEigenVectors_UseAllEigenvectors");

        double[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        String input = "-1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Main.scanner = new java.util.Scanner(System.in);
        int expectedK = matrix.length;
        int actualK = Main.askNumberOfEigenVectors(matrix);

        boolean result = compareInt(expectedK, actualK);
        UI_Utils.showTestResult(result);
    }

    public static void testaskNumberOfEigenVectors_InvalidNegativeInput() {
        UI_Utils.showTestTitle("testaskNumberOfEigenVectors_InvalidNegativeInput");

        double[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        String input = "-5\n3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Main.scanner = new Scanner(System.in);
        int expectedK = 3;
        int actualK = Main.askNumberOfEigenVectors(matrix);

        boolean result = compareInt(expectedK, actualK);
        UI_Utils.showTestResult(result);
    }

    public static void testaskNumberOfEigenVectors_InvalidLargeInput() {
        UI_Utils.showTestTitle("testaskNumberOfEigenVectors_InvalidLargeInput");

        double[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        String input = "10\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Main.scanner = new java.util.Scanner(System.in);
        int expectedK = matrix.length;
        int actualK = Main.askNumberOfEigenVectors(matrix);

        boolean result = compareInt(expectedK, actualK);
        UI_Utils.showTestResult(result);
    }

    public static void testCalculateEuclideanDistance() {
        UI_Utils.showTestTitle("testCalculateEuclideanDistance");

        double[] vector1 = {1.0, 2.0, 3.0};
        double[] vector2 = {4.0, 5.0, 6.0};
        double expectedDistance = 5.196152422706632;
        double actualDistance = Main.calculateEuclideanDistance(vector1, vector2);

        boolean result = compareDouble(expectedDistance, actualDistance);
        UI_Utils.showTestResult(result);
    }

    public static void testCalculateEuclideanDistanceBiggerVectors() {
        UI_Utils.showTestTitle("testCalculateEuclideanDistance2");

        double[] vector1 = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] vector2 = {6.0, 7.0, 8.0, 9.0, 10.0};
        double expectedDistance = 11.180339887498949;
        double actualDistance = Main.calculateEuclideanDistance(vector1, vector2);

        boolean result = compareDouble(expectedDistance, actualDistance);
        UI_Utils.showTestResult(result);
    }

    public static void testValidateVectorPixels_Valid() {
        UI_Utils.showTestTitle("testValidateVectorPixelsValid");

        double[] validVector = {0.0, 50.0, 100.0, 150.0, 200.0, 255.0};

        boolean resultValid = Main.validateVectorPixels(validVector);

        UI_Utils.showTestResult(resultValid);
    }

    public static void testValidateVectorPixels_Invalid() {
        UI_Utils.showTestTitle("testValidateVectorPixelsInvalid");

        double[] invalidVector = {-10.0, 50.0, 100.0, 150.0, 200.0, 300.0};

        boolean resultInvalid = Main.validateVectorPixels(invalidVector);

        UI_Utils.showTestResult(!resultInvalid);
    }

    public static void testCalculateEAM_Vector() {
        UI_Utils.showTestTitle("testCalculateEAM_Vector");

        double[] original = {1.0, 2.0, 3.0};
        double[] reconstructed = {1.1, 2.1, 3.1};

        double expectedEAM = (Math.abs(1.0 - 1.1) + Math.abs(2.0 - 2.1) + Math.abs(3.0 - 3.1)) / original.length;

        double actualEAM = Main.calculateEAM(original, reconstructed);

        boolean result = compareDouble(expectedEAM, actualEAM);
        UI_Utils.showTestResult(result);
    }

    public static void testCalculateEAM_VectorBigger() {
        UI_Utils.showTestTitle("testCalculateEAM_VectorBigger");

        double[] original = {1.0, 2.0, 3.3, 4.0, 5.4, 6.0};
        double[] reconstructed = {1.1, 2.1, 3.1, 4.1, 5.1, 6.1};

        double expectedEAM = (Math.abs(1.0 - 1.1) + Math.abs(2.0 - 2.1) + Math.abs(3.3 - 3.1) +
                Math.abs(4.0 - 4.1) + Math.abs(5.4 - 5.1) + Math.abs(6.0 - 6.1)) / original.length;

        double actualEAM = Main.calculateEAM(original, reconstructed);

        boolean result = compareDouble(expectedEAM, actualEAM);
        UI_Utils.showTestResult(result);
    }

    public static void testNormalizeVectorsCLargerMatrix() {
        UI_Utils.showTestTitle("testNormalizeVectorsC_LargerMatrix");

        double[][] matrizVetoresPropriosC = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        double[][] expected = {
                {0.12309149097933272, 0.20739033894608505, 0.2672612419124244},
                {0.4923659639173309, 0.5184758473652126, 0.5345224838248488},
                {0.8616404368553291, 0.8295613557843401, 0.8017837257372732}
        };

        double[][] actual = Main.normalizeVectorsC(matrizVetoresPropriosC);

        boolean result = compareMatrixWithTolerance(expected, actual, 0.0001);
        UI_Utils.showTestResult(result);
    }

    public static void testSaveIntMatrixToCSV() {
        UI_Utils.showTestTitle("testSaveIntMatrixToCSV");

        double[] vector = {1.0, 2.0, 3.0, 4.0};
        String outputPath = "output/test_data";
        int matrixSize = 2;
        int counter = 1;
        String filePattern = "/test_matrix_";

        Main.saveIntMatrixToCSV(vector, outputPath, matrixSize, counter, filePattern);

        File outputFile = new File(outputPath + filePattern + counter + ".csv");
        boolean result = outputFile.exists();
        UI_Utils.showTestResult(result);
    }

    public static void testCalculateEigenVectorsCLargerMatrix() {
        UI_Utils.showTestTitle("testCalculateEigenVectorsC_LargerMatrix");

        double[][] matrizA = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        double[][] vetoresPropriosATA = {
                {0.5, 0.5, 0.5},
                {0.5, 0.5, 0.5},
                {0.5, 0.5, 0.5}
        };

        int numEigenFaces = 3;

        double[][] expected = {
                {3, 3, 3},
                {7.5, 7.5, 7.5},
                {12, 12, 12}
        };

        double[][] actual = Main.calculateEigenVectorsC(matrizA, vetoresPropriosATA, numEigenFaces);

        boolean result = compareMatrix(expected, actual);
        UI_Utils.showTestResult(result);
    }

    public static void testCompressedMatrix() {
        UI_Utils.showTestTitle("testCompressedMatrix");

        double[][] matrixP = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        double[][] matrixD = {
                {3, 0, 0},
                {0, 2, 0},
                {0, 0, 1}
        };
        int k = 2;

        double[][][] expected = {
                {
                        {1, 0},
                        {0, 1},
                        {0, 0}
                },
                {
                        {3, 0},
                        {0, 2}
                }
        };

        double[][][] actual = Main.compressedMatrix(matrixP, matrixD, k);

        boolean result = compareMatrix(expected[0], actual[0]) && compareMatrix(expected[1], actual[1]);
        UI_Utils.showTestResult(result);
    }

}