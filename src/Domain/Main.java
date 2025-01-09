package Domain;

import Utils.*;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.Arrays;

import java.io.File;
import java.io.IOException;


public class Main {
    public static final String OUTPUT_PATH = "funcionalidade_1";
    public static final String OUTPUT_PATH_F2_REBUILT_IMAGES = "funcionalidade_2/imagensReconstruidas/";
    public static final String OUTPUT_PATH_F2_EIGENFACES = "funcionalidade_2/eigenfaces/";
    public static final String FILE_EIGENFACE_PATTERN = "eigenface_";
    public static final String FILE_EIGENFACE_TRANSFORMED_PATTERN = "eigenface_transformada_";
    public static final String FILE_RECONSTRUCTED_IMAGE_PATTERN = "imagem_reconstruida_";
    public static final String FILE_RECONSTRUCTED_IMAGE_TRANSFORMED_PATTERN = "imagem_reconstruida_transformada_";

    public static Scanner scanner = new Scanner(System.in);
    public static boolean interactive;
    public static StringBuilder txtContent;
    public static String argumentPattern = "-[a-zA-Z]";

    public static void main(String[] args) {

        String filePath, folderPath;
        int numEigenFaces, menuOption;

        //MODO NAO INTERATIVO
        if (args.length > 0) {
            System.out.println(args[args.length - 1]);
            if (args[args.length - 1].matches(argumentPattern)) {
                System.out.println("Argumentos inválidos. Insira os argumentos no formato correto.\nA encerrar o programa...");
                return;
            }
            for (int i = 0; i < args.length - 1; i++) {

                if (args[i].matches(argumentPattern) && args[i + 1].matches(argumentPattern)) {
                    System.out.println("Argumentos inválidos. Insira os argumentos no formato correto.\nA encerrar o programa...");
                    return;
                }

            }
            interactive = false;
            txtContent = new StringBuilder();


            String funcionalidade = null;
            String numEigenfaces = null;
            String caminhoCSV = null;
            String caminhoImagens = null;
            String caminhoSaida = null;

            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-f":
                        funcionalidade = args[++i];
                        break;
                    case "-k":
                        numEigenfaces = args[++i];
                        break;
                    case "-i":
                        caminhoCSV = args[++i];
                        break;
                    case "-d":
                        caminhoImagens = args[++i];
                        break;
                    case "-W":
                        caminhoSaida = args[++i];
                        break;
                    default:
                        System.out.println("Parâmetro desconhecido: " + args[i]);
                        return;
                }
            }
            if (funcionalidade == null) {
                return;
            }
            int func = Integer.parseInt(funcionalidade);

            if (func < 1 || func > 3) {
                System.out.println("Funcionalidade inválida. Deve ser 1, 2 ou 3.");
                return;
            }

            if (func == 1) {
                if (numEigenfaces == null || caminhoCSV == null) {
                    System.out.println("Por favor, indique todos os argumentos necessários para a funcionalidade: " + func);
                    System.out.println("""           
                            
                            Parâmetros necessários:
                              - k (número de vetores próprios)
                              - i (caminho para a imagem CSV)
                            
                            """);
                    return;
                }
                if (caminhoCSV == null || !NonInteractiveValidations.validateK(numEigenfaces) || !NonInteractiveValidations.validateFilePath(caminhoCSV)) {
                    System.out.println("A encerrar o programa...");
                    return;
                } else {
                    double[][] readMatrix = File_Utils.getMatrixFromCSV(caminhoCSV);

                    if (readMatrix != null) {
                        if (validateMatrix(readMatrix)) {
                            int k = Integer.parseInt(numEigenfaces);
                            if (k == -1 || k > readMatrix.length) k = readMatrix.length;
                            execFunc1(readMatrix, k);
                        } else {
                            System.out.println("A encerrar o programa...");
                            return;
                        }
                    } else {
                        System.out.println("A encerrar o programa...");
                        return;
                    }
                }
            } else if (func == 2) {
                if (numEigenfaces == null || caminhoImagens == null) {
                    System.out.println("Por favor, indique todos os argumentos necessários para a funcionalidade: " + func);
                    System.out.println("""
                            
                            Parâmetros necessários:
                              - k (número de eigenfaces)
                              - d (caminho para a pasta da base de imagens)
                            
                            """);
                    return;
                }
                if (!NonInteractiveValidations.selectFolderToRead(caminhoImagens) || !NonInteractiveValidations.validateK(numEigenfaces)) {
                    System.out.println("A encerrar o programa...");
                    return;
                } else {
                    File[] files = File_Utils.getCsvFiles(caminhoImagens);
                    if (files.length == 0) {
                        System.out.println("A encerrar o programa...");
                        return;
                    } else {
                        int k = Integer.parseInt(numEigenfaces);
                        execFunc2(files, k);
                        System.out.println("Funcionalidade executada com sucesso.");
                    }
                }
            } else if (func == 3) {
                if (numEigenfaces == null || caminhoImagens == null || caminhoCSV == null) {
                    System.out.println("Por favor, indique todos os argumentos necessários para a funcionalidade: " + func);
                    System.out.println("""
                            
                            Parâmetros necessários:
                              - k (número de eigenfaces)
                              - i (caminho para a imagem CSV)
                              - d (caminho para a pasta da base de imagens)
                            
                            """);
                    return;
                }
                if (!NonInteractiveValidations.validateK(numEigenfaces) || !NonInteractiveValidations.selectFolderToRead(caminhoImagens) || !NonInteractiveValidations.validateFilePath(caminhoCSV)) {
                    System.out.println("A encerrar o programa...");
                    return;
                } else {
                    File[] files = File_Utils.getCsvFiles(caminhoImagens);
                    if (files.length == 0) {
                        System.out.println("A encerrar o programa...");
                        return;
                    } else {
                        int k = Integer.parseInt(numEigenfaces);
                        execFunc3(caminhoImagens, k, caminhoCSV);
                        System.out.println("Funcionalidade executada com sucesso.");
                    }
                }
            }
        }
        //MODO INTERATIVO
        else {
            interactive = true;
            do {
                menuOption = -1;

                while (menuOption < 0 || menuOption > 5) {
                    UI_Utils.showMenuOptions();

                    if (scanner.hasNextInt()) {
                        menuOption = scanner.nextInt();
                        if (menuOption < 0 || menuOption > 5) {
                            System.out.println("Opção inválida. Escolha uma opção válida.");
                        }
                    } else {
                        System.out.println("Entrada inválida. Insira um número.");
                        scanner.next();
                    }
                }
                switch (menuOption) {
                    case 1:
                        UI_Utils.showMenuTitle(1);
                        filePath = UI_Utils.selectFileToRead();
                        double[][] readMatrix = File_Utils.getMatrixFromCSV(filePath);
                        if (readMatrix != null) {
                            if (validateMatrix(readMatrix)) {
                                int k = askNumberOfEigenvectors(readMatrix);
                                execFunc1(readMatrix, k);
                            } else System.out.println("\nInsira uma matriz válida.");
                        } else System.out.println("A matriz ainda não foi lida.");
                        break;
                    case 2:
                        UI_Utils.showMenuTitle(2);
                        File[] files;
                        do {
                            folderPath = UI_Utils.selectFolderToRead();
                            files = File_Utils.getCsvFiles(folderPath);
                        } while (files.length == 0);
                        numEigenFaces = askNumberOfEigenfaces();
                        if (numEigenFaces > files.length) numEigenFaces = files.length;
                        execFunc2(files, numEigenFaces);
                        break;
                    case 3:
                        UI_Utils.showMenuTitle(3);
                        do {
                            folderPath = UI_Utils.selectFolderToRead();
                            files = File_Utils.getCsvFiles(folderPath);
                        } while (files.length == 0);
                        numEigenFaces = askNumberOfEigenfaces();
                        if (numEigenFaces > files.length) numEigenFaces = files.length;
                        System.out.println("Enter the path of the new image (CSV format): ");
                        scanner.nextLine();


                        String newImagePath = UI_Utils.selectFileToRead();
                        double[][] matrix3 = File_Utils.getMatrixFromCSV(newImagePath);
                        if (matrix3 != null) {
                            execFunc3(folderPath, numEigenFaces, newImagePath);
                        } else System.out.println("A matriz ainda não foi lida.");
                        break;
                    case 4:
                        UI_Utils.showMenuTitle(4);
                        UI_Utils.showDevelopmentTeam();
                        break;
                    case 0:
                        System.out.println("A fechar a aplicação...");
                        break;
                }

            } while (menuOption != 0);

        }
        scanner.close();
    }

    private static void execFunc3(String folderPath, int numEigenFaces, String newImagePath) {
        int matrixSize;
        double[][] matrixA, eigenVectorsATA, matrixEigenFaces, matrixVectors, matrixATA, matrixEigenVectorsC, transposedMatrixA;
        double[] averageImages;
        File[] files;

        files = File_Utils.getCsvFiles(folderPath);

        matrixSize = File_Utils.getNumberOfLines(files[0]);
        matrixVectors = File_Utils.processCsvFiles(files, matrixSize);

        if (numEigenFaces == -1 || numEigenFaces > matrixVectors[0].length) {
            numEigenFaces = matrixVectors[0].length;
            System.out.println("O número de EigenFaces a utilizar foi adaptado para o máximo: " + numEigenFaces);
        }

        averageImages = calculateAverage(matrixVectors);
        if (interactive) UI_Utils.printVetorMedia(averageImages);
        matrixA = calculateMatrixA(matrixSize * matrixSize, files.length, matrixVectors, averageImages);
        transposedMatrixA = Matrix_Utils.transpose(matrixA);
        matrixATA = Matrix_Utils.matrixMultiplication(transposedMatrixA, matrixA);
        if (interactive) UI_Utils.printMatrix(matrixATA);
        eigenVectorsATA = getEigenVectors(matrixATA);
        matrixEigenVectorsC = calculateEigenVectorsC(matrixA, eigenVectorsATA, numEigenFaces);
        matrixEigenFaces = normalizeVectorsC(matrixEigenVectorsC);
        // Ler a nova imagem
        double[][] newImage = File_Utils.getMatrixFromCSV(newImagePath);

        int imageSize = File_Utils.getNumberOfLines(files[0]);

        // Calcular Φnova
        double[] phiNova = new double[imageSize * imageSize];
        for (int i = 0; i < phiNova.length; i++) {
            phiNova[i] = newImage[i / imageSize][i % imageSize] - averageImages[i];
        }

        double[] newOmega = new double[numEigenFaces];

        for (int j = 0; j < numEigenFaces; j++) {
            double[] transposedUj = getUjTransposed(matrixEigenFaces, j);
            newOmega[j] = calculateWeight(transposedUj, phiNova);
        }

        double[][] omegaMatrixDatabase = new double[files.length][numEigenFaces];

        for (int i = 0; i < files.length; i++) {
            double[] phi = getPhiInPosition(matrixA, i);
            double[] omegaVectorDatabase = new double[numEigenFaces];
            for (int j = 0; j < numEigenFaces; j++) {
                double[] transposedUj = getUjTransposed(matrixEigenFaces, j);
                double weight = calculateWeight(transposedUj, phi);
                omegaVectorDatabase[j] = weight;
            }
            omegaMatrixDatabase[i] = omegaVectorDatabase;
        }

        double[] distances = new double[omegaMatrixDatabase.length];
        for (int i = 0; i < omegaMatrixDatabase.length; i++) {
            distances[i] = calculateEuclideanDistance(newOmega, omegaMatrixDatabase[i]);
        }
        if (interactive) {
            for (int i = 0; i < distances.length; i++) {
                System.out.println("Distance to Ω" + i + ": " + distances[i]);
            }

            System.out.println("Number of eigenfaces used: " + numEigenFaces);
            System.out.println("New Ω: " + Arrays.toString(newOmega));
            for (int i = 0; i < files.length; i++) {
                System.out.println("Ω" + i + ": " + Arrays.toString(omegaMatrixDatabase[i]) + ", Distance: " + distances[i]);
            }
        }


        int[] closestImageIndices = new int[distances.length];
        int count = 0;
        double minDistance = distances[0];
        closestImageIndices[count++] = 0;

        for (int i = 1; i < distances.length; i++) {
            if (distances[i] < minDistance) {
                minDistance = distances[i];
                count = 0;
                closestImageIndices[count++] = i;
            } else if (distances[i] == minDistance) {
                closestImageIndices[count++] = i;
            }
        }

        for (int i = 0; i < count; i++) {
            getImageAsJPG(files[closestImageIndices[i]], "funcionalidade_3/closest_image_" + closestImageIndices[i] + ".jpg");
        }
    }

    private static void execFunc2(File[] files, int numEigenFaces) {
        double[][] matrixEigenVectorsC, transposedMatrixA, matrixVectors, matrixATA, eigenVectorsATA, matrixA, matrixEigenFaces;
        int matrixSize;

        double[] averageImages;
        matrixSize = File_Utils.getNumberOfLines(files[0]);
        matrixVectors = File_Utils.processCsvFiles(files, matrixSize);

        if (numEigenFaces == -1 || numEigenFaces > matrixVectors[0].length) {
            numEigenFaces = matrixVectors[0].length;
            System.out.println("O número de EigenFaces a utilizar foi adaptado para o máximo: " + numEigenFaces);
        }

        averageImages = calculateAverage(matrixVectors);
        if (interactive) UI_Utils.printVetorMedia(averageImages);
        matrixA = calculateMatrixA(matrixSize * matrixSize, files.length, matrixVectors, averageImages);
        transposedMatrixA = Matrix_Utils.transpose(matrixA);
        matrixATA = Matrix_Utils.matrixMultiplication(transposedMatrixA, matrixA);
        double[][] covarianceMatrix = Matrix_Utils.calculateCovarianceMatrix(matrixATA, matrixVectors[0].length);
        if (interactive) {
            System.out.println("Covariance Matrix:");
            UI_Utils.printMatrix(covarianceMatrix);
        }
        eigenVectorsATA = getEigenVectors(matrixATA);
        matrixEigenVectorsC = calculateEigenVectorsC(matrixA, eigenVectorsATA, numEigenFaces);
        matrixEigenFaces = normalizeVectorsC(matrixEigenVectorsC);
        if (interactive) {
            System.out.println("\nNúmero de Eigenfaces utilizadas: " + numEigenFaces + "\n");
        }
        saveEigenFaces(matrixEigenFaces, matrixSize, numEigenFaces);
        rebuildImages(files, matrixA, matrixEigenFaces, averageImages, numEigenFaces, matrixSize, matrixVectors);
    }

    private static void execFunc1(double[][] readMatrix, int k) {
        if (interactive) {
            System.out.println("\nMatriz Lida:");
            UI_Utils.printMatrix(readMatrix);
        }

        RealMatrix[] result = ownDecomposition(readMatrix);

        double[][] matrixP = result[0].getData();
        double[][] matrixD = result[1].getData();

        double[][][] decomposed = compressedMatrix(matrixP, matrixD, k);

        double[][] matrixPk = decomposed[0];
        double[][] matrixDk = decomposed[1];

        double[][] matrixAk = Matrix_Utils.matrixMultiplication(Matrix_Utils.matrixMultiplication(matrixPk, matrixDk), Matrix_Utils.transpose(matrixPk));
        if (interactive) {
            System.out.println("\nMatriz Reconstruída:\n");
            UI_Utils.printMatrix(matrixAk);
        }
        File_Utils.saveMatrixToCSV(matrixAk, OUTPUT_PATH);
    }

    public static double calculateEuclideanDistance(double[] vector1, double[] vector2) {
        double soma = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            soma += Math.pow(vector1[i] - vector2[i], 2);
        }
        return Math.sqrt(soma);
    }

    public static void getImageAsJPG(File csvFile, String outputPath) {
        try {
            double[][] imageMatrix = File_Utils.getMatrixFromCSV(csvFile.getAbsolutePath());
            int imageSize = imageMatrix.length;
            BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_3BYTE_BGR);

            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int pixelValue = (int) Math.round(imageMatrix[j][i]);
                    pixelValue = Math.max(0, Math.min(255, pixelValue));
                    int rgb = (pixelValue << 16) | (pixelValue << 8) | pixelValue;
                    image.setRGB(i, j, rgb);
                }
            }
            File output = new File(outputPath);
            File parentDir = output.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            ImageIO.write(image, "jpg", output);
            System.out.println("Image saved as JPG: " + output.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving image as JPG: " + e.getMessage());
        }
    }


    private static double[][] calculateMatrixA(int lines, int rows, double[][] matrixVectors, double[] averageImages) {
        double[][] matrixA = new double[lines][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < lines; j++) {
                matrixA[j][i] = matrixVectors[j][i] - averageImages[j];
            }
        }

        return matrixA;
    }

    public static void rebuildImages(File[] files, double[][] matrixA, double[][] eigenFacesMatrix, double[] averageImages, int numEigenFaces, int matrixSize, double[][] matrixVectors) {
        File outputFolder = new File(OUTPUT_PATH_F2_REBUILT_IMAGES);

        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        for (int i = 0; i < files.length; i++) {
            double[] phi = getPhiInPosition(matrixA, i);
            double[] sumArray = new double[eigenFacesMatrix.length];
            double[] reconstructedImage = new double[eigenFacesMatrix.length];
            double[] weightsVector = new double[numEigenFaces];

            for (int j = 0; j < numEigenFaces; j++) {
                double[] ujTransposed = getUjTransposed(eigenFacesMatrix, j);
                double weight = calculateWeight(ujTransposed, phi);
                weightsVector[j] = weight;
                double[] uj = getMatrixVectorAtPosition(eigenFacesMatrix, j);

                //Multiply vector uj by weight
                for (int k = 0; k < uj.length; k++) {
                    uj[k] *= weight;
                }

                //add the vector uj to the sumArray
                for (int k = 0; k < uj.length; k++) {
                    sumArray[k] += uj[k];
                }
            }

            //add sumArray to the average
            for (int j = 0; j < averageImages.length; j++) {
                reconstructedImage[j] = averageImages[j] + sumArray[j];
            }
            if (interactive) {
                System.out.println("Vetor dos pesos da imagem " + i + ": ");
                printVector(weightsVector);
            }

            double[] originalImageVector = getMatrixVectorAtPosition(matrixVectors, i);
            double eam = calculateEAM(originalImageVector, reconstructedImage);

            if (interactive) System.out.printf("Mean Absolute Error (EAM) for image %d: %.2f%n%n", i, eam);

            saveIntMatrixToCSV(reconstructedImage, OUTPUT_PATH_F2_REBUILT_IMAGES, matrixSize, i, FILE_RECONSTRUCTED_IMAGE_PATTERN);

            if (!validateVectorPixels(reconstructedImage)) {
                reconstructedImage = pixelScalingEigenFaces(reconstructedImage);
                saveIntMatrixToCSV(reconstructedImage, OUTPUT_PATH_F2_REBUILT_IMAGES, matrixSize, i, FILE_RECONSTRUCTED_IMAGE_TRANSFORMED_PATTERN);
            }
            saveImageAsJPG(OUTPUT_PATH_F2_REBUILT_IMAGES, reconstructedImage, matrixSize, i, FILE_RECONSTRUCTED_IMAGE_PATTERN);

        }
    }

    private static boolean validateVectorPixels(double[] reconstructedImage) {
        for (int i = 0; i < reconstructedImage.length; i++) {
            if (reconstructedImage[i] < 0 || reconstructedImage[i] > 255) {
                return false;
            }
        }
        return true;
    }

    public static void printVector(double[] vector) {
        System.out.print("[");
        for (int i = 0; i < vector.length; i++) {
            System.out.printf("%.2f", vector[i]);
            if (i < vector.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public static void saveEigenFaces(double[][] eigenFacesMatrix, int matrixSize, int numEigenFaces) {
        File outputFolder = new File(OUTPUT_PATH_F2_EIGENFACES);

        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        for (int i = 0; i < numEigenFaces; i++) {
            double[] eigenFace = getMatrixVectorAtPosition(eigenFacesMatrix, i);
            saveIntMatrixToCSV(eigenFace, OUTPUT_PATH_F2_EIGENFACES, matrixSize, i, FILE_EIGENFACE_PATTERN);
            double[] pixelScaledEigenFaces = pixelScalingEigenFaces(eigenFace);
            saveIntMatrixToCSV(pixelScaledEigenFaces, OUTPUT_PATH_F2_EIGENFACES, matrixSize, i, FILE_EIGENFACE_TRANSFORMED_PATTERN);
            saveImageAsJPG(OUTPUT_PATH_F2_EIGENFACES, pixelScaledEigenFaces, matrixSize, i, FILE_EIGENFACE_PATTERN);
        }
    }

    public static double[] pixelScalingEigenFaces(double[] eigenFace) {
        // Inicializamos as variáveis para o mínimo e máximo
        double min = eigenFace[0];
        double max = eigenFace[0];

        // Encontrar o valor mínimo e máximo no vetor
        for (int i = 1; i < eigenFace.length; i++) {
            if (eigenFace[i] < min) {
                min = eigenFace[i];
            }
            if (eigenFace[i] > max) {
                max = eigenFace[i];
            }
        }

        // Array para armazenar os valores escalados
        double[] scaledEigenFace = new double[eigenFace.length];

        // Escalar os valores para o intervalo [0, 255]
        for (int i = 0; i < eigenFace.length; i++) {
            scaledEigenFace[i] = 255 * (eigenFace[i] - min) / (max - min);
        }

        return scaledEigenFace;
    }

    public static double calculateEAM(double[] original, double[] reconstructed) {
        double sum = 0;
        int length = original.length;

        for (int i = 0; i < length; i++) {
            sum += Math.abs(original[i] - reconstructed[i]);
        }
        return sum / length;
    }

    private static void saveIntMatrixToCSV(double[] vector, String outputPath, int matrixSize, int counter, String filePattern) {
        int[][] matrix = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                matrix[i][j] = (int) Math.round(vector[i * matrixSize + j]);
            }
        }


        File outputFile = new File(outputPath + "/" + filePattern + counter + ".csv");
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            for (int[] row : matrix) {
                for (int j = 0; j < row.length; j++) {
                    writer.print(row[j]);
                    if (j < row.length - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar imagem como CSV: " + e.getMessage());
        }
    }

    public static double[] getMatrixVectorAtPosition(double[][] matrixEigenFaces, int j) {
        double[] vector = new double[matrixEigenFaces.length];

        for (int i = 0; i < matrixEigenFaces.length; i++) {
            vector[i] = matrixEigenFaces[i][j];
        }
        return vector;
    }

    public static double calculateWeight(double[] ujTransposed, double[] phi) {
        double weight = 0.0;

        for (int i = 0; i < ujTransposed.length; i++) {
            weight += ujTransposed[i] * phi[i];
        }
        return weight;
    }

    public static void saveImageAsJPG(String savePath, double[] imageVector, int matrixSize, int counter, String filePattern) {
        int[][] array = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int pixelValue = (int) Math.round(imageVector[i * matrixSize + j]);
                if (pixelValue < 0 || pixelValue > 255) {
                    System.out.println("Valor de pixel inválido: " + pixelValue);
                }

                pixelValue = Math.max(0, Math.min(255, pixelValue));
                array[i][j] = pixelValue;
            }
        }

        GreyscaleImageWriter writer = new GreyscaleImageWriter();
        try {
            writer.writeArrayAsImage(array, savePath + filePattern + counter + ".jpg");
        } catch (IOException e) {
            System.err.println("Erro ao salvar imagem como JPG: " + e.getMessage());
        }
    }

    public static double[] getUjTransposed(double[][] EigenFacesmatrix, int j) {
        double[] ujTransposed = new double[EigenFacesmatrix.length];
        for (int i = 0; i < EigenFacesmatrix.length; i++) {
            ujTransposed[i] = EigenFacesmatrix[i][j];
        }
        return ujTransposed;
    }

    public static double[] getPhiInPosition(double[][] matrixA, int index) {
        double[] phi = new double[matrixA.length];

        //This method places column i of matrix A in phi
        for (int i = 0; i < matrixA.length; i++) {
            phi[i] = matrixA[i][index];
        }
        return phi;
    }

    public static double[][] calculateEigenVectorsC(double[][] matrixA, double[][] eigenVectorsATA, int numEigenFaces) {
        int numLinesA = matrixA.length;
        int numColumnsA = matrixA[0].length;

        double[][] eigenVectorsMatrixC = new double[numLinesA][numEigenFaces];

        for (int i = 0; i < numEigenFaces; i++) {
            double[] eigenVector = new double[numColumnsA];

            for (int j = 0; j < numColumnsA; j++) {
                eigenVector[j] = eigenVectorsATA[j][i];
            }

            double[] eigenVectorC = new double[numLinesA];

            for (int j = 0; j < numLinesA; j++) {
                for (int k = 0; k < numColumnsA; k++) {
                    eigenVectorC[j] += matrixA[j][k] * eigenVector[k];
                }
            }

            for (int j = 0; j < numLinesA; j++) {
                eigenVectorsMatrixC[j][i] = eigenVectorC[j];
            }
        }
        return eigenVectorsMatrixC;
    }

    public static RealMatrix[] ownDecomposition(double[][] matrixRead) {
        // Own decomposition
        RealMatrix[] result = new RealMatrix[2];
        EigenDecomposition eigenDecomposition = new EigenDecomposition(MatrixUtils.createRealMatrix(matrixRead));

        // Eigenvector matrix (P)
        RealMatrix matrixP = eigenDecomposition.getV();
        result[0] = matrixP;
        // Diagonal matrix of eigenvalues ​​(D)
        RealMatrix matrixD = MatrixUtils.createRealDiagonalMatrix(eigenDecomposition.getRealEigenvalues());
        result[1] = matrixD;

        return result;
    }

    public static double[][][] compressedMatrix(double[][] matrixP, double[][] matrixD, int k) {
        double[][] auxP = matrixP;
        double[][] auxD = matrixD;

        int n = matrixD.length;
        double[] diagonal = new double[n];
        for (int i = 0; i < n; i++) {
            diagonal[i] = auxD[i][i];
        }

        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, (i, j) -> Double.compare(diagonal[j], diagonal[i]));

        double[][] sortedD = new double[n][n];
        double[][] sortedP = new double[n][n];
        for (int i = 0; i < n; i++) {
            sortedD[i][i] = diagonal[indices[i]];
            for (int j = 0; j < n; j++) {
                sortedP[j][i] = auxP[j][indices[i]];
            }
        }

        for (int i = 0; i < n; i++) {
            auxD[i][i] = sortedD[i][i];
            for (int j = 0; j < n; j++) {
                auxP[j][i] = sortedP[j][i];
            }
        }

        double[][] finalPk = new double[auxP[0].length][k];
        double[][] finalDk = new double[k][k];

        for (int i = 0; i < auxP[0].length; i++) {
            for (int j = 0; j < k; j++) {
                finalPk[i][j] = auxP[i][j];
            }
        }

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (i == j) finalDk[i][j] = auxD[i][j];
            }
        }

        return new double[][][]{finalPk, finalDk};
    }

    public static int askNumberOfEigenfaces() {

        int k = 0;

        while (k == 0 || k < -1) {
            System.out.println("\nQual é o número de Eigenfaces que pretende utilizar?");

            if (scanner.hasNextInt()) {
                k = scanner.nextInt();

                if (k == -1) {
                    return k;
                }

                if (k <= 0) {
                    System.out.println("""
                            Número inválido de autovetores.
                               Entradas válidas:
                                    - Um número inteiro positivo
                                    - -1 para usar todos os Eigenfaces disponíveis
                            """);
                }

            } else {
                System.out.println("Entrada inválida. Insira um número.");
                scanner.next();
            }
        }
        return k;
    }


    /**
     * Function that validates the matrix. Checks if the matrix is square, symmetric and does not contain negative values, returning the specific error if it exists.
     *
     * @param matrix
     * @return true if the matrix is valid or false if it is not
     */
    public static boolean validateMatrix(double[][] matrix) {

        if (matrix.length != matrix[0].length) {
            System.out.println("A matriz não é quadrada.");
            return false;
        }

        String[] negativeValues = new String[matrix.length * matrix[0].length];
        boolean hasNegatives = false;
        int negativeIndex = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != matrix[j][i]) {
                    System.out.println("A matriz não é simétrica.");
                    return false;
                }
                if (matrix[i][j] < 0) {
                    hasNegatives = true;
                    negativeValues[negativeIndex++] = "Linha: " + i + ", Coluna: " + j + " (" + matrix[i][j] + ")";
                }
            }
        }
        if (hasNegatives) {
            System.out.println("A matriz contém valores negativos.");
            for (int i = 0; i < negativeValues.length; i++) {
                if (negativeValues[i] != null) {
                    System.out.println(negativeValues[i]);
                }
            }
            return false;
        }
        return true;
    }

    public static int askNumberOfEigenvectors(double[][] matrix) {
        int k = 0;

        while (k <= 0 || k > matrix.length) {
            System.out.println("\nQual é o número de vetores próprios (k)?");

            if (scanner.hasNextInt()) {
                k = scanner.nextInt();

                if (k == -1) {
                    k = matrix.length;
                    System.out.println("Opção -1: Usando todos os Vetores Próprios.");
                    return k;
                }

                if (k <= 0) {
                    System.out.println("""
                            Número inválido de autovetores.
                               Entradas válidas:
                                    - Um número inteiro positivo
                                    - -1 para usar todos os vetores próprios
                            """);
                } else if (k > matrix.length) {
                    System.out.printf("O número máximo de vetores próprios é %d. A definir k para %d.%n", matrix.length, matrix.length);
                    k = matrix.length;
                }
            } else {
                System.out.println("Entrada inválida. Insira um número.");
                scanner.next();
            }
        }
        return k;
    }


    /**
     * Function to calculate the average absolute error between two matrices, the original and the reconstructed one
     *
     * @param original
     * @param reconstructed
     * @return mean absolute error
     */
    public static double calculateEAM(double[][] original, double[][] reconstructed) {
        double sum = 0;
        int rows = original.length;
        int cols = original[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum += Math.abs(original[i][j] - reconstructed[i][j]);
            }
        }
        return sum / (rows * cols);
    }

    public static double[] calculateAverage(double[][] vectorMatrix) {
        int numRows = vectorMatrix.length;
        int numCols = vectorMatrix[0].length;

        double[] averageVector = new double[numRows];

        for (int i = 0; i < numRows; i++) {
            double sum = 0.0;
            for (int j = 0; j < numCols; j++) {
                sum += vectorMatrix[i][j];
            }
            averageVector[i] = sum / numCols;
        }

        return averageVector;
    }

    public static void sortEigenvectors(double[] eigenvalues, double[][] eigenvectors) {

        for (int i = 0; i < eigenvalues.length; i++) {
            for (int j = i + 1; j < eigenvalues.length; j++) {
                if (eigenvalues[i] < eigenvalues[j]) {
                    double temp = eigenvalues[i];
                    eigenvalues[i] = eigenvalues[j];
                    eigenvalues[j] = temp;

                    double[] tempVector = eigenvectors[i];
                    eigenvectors[i] = eigenvectors[j];
                    eigenvectors[j] = tempVector;
                }
            }
        }
    }

    public static double[][] getEigenVectors(double[][] matrixATA) {
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixATA);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(matrix);

        double[][] eigenvectors = eigenDecomposition.getV().getData();
        double[] eigenvalues = eigenDecomposition.getRealEigenvalues();

        sortEigenvectors(eigenvalues, eigenvectors);

        return eigenvectors;
    }


    public static double[][] normalizeVectorsC(double[][] eigenvectorMatrixC) {
        int numRows = eigenvectorMatrixC.length;      // Number of rows
        int numCols = eigenvectorMatrixC[0].length;  // Number of columns (number of eigenvectors)

        double[][] eigenFacesMatrix = new double[numRows][numCols];

        // Normalize each column (eigenvector)
        for (int j = 0; j < numCols; j++) {
            double sum = 0;

            // Calculate the norm of the vector (column j)
            for (int i = 0; i < numRows; i++) {
                sum += Math.pow(eigenvectorMatrixC[i][j], 2);
            }
            double norm = Math.sqrt(sum);

            // Normalize column j
            for (int i = 0; i < numRows; i++) {
                eigenFacesMatrix[i][j] = eigenvectorMatrixC[i][j] / norm;
            }
        }
        return eigenFacesMatrix;
    }
}