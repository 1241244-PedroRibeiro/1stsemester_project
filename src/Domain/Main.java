package Domain;

import Utils.*;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

import static Utils.Matrix_Utils.*;
import static Utils.File_Utils.*;
import static Utils.UI_Utils.*;
import static Utils.NonInteractive_Utils.*;


public class Main {
    public static final String OUTPUT_PATH = "funcionalidade_1";
    public static final String OUTPUT_PATH_F2_REBUILT_IMAGES = "funcionalidade_2/imagensReconstruidas/";
    public static final String OUTPUT_PATH_F2_EIGENFACES = "funcionalidade_2/eigenfaces/";
    public static final String FILE_EIGENFACE_PATTERN = "eigenface_";
    public static final String FILE_EIGENFACE_TRANSFORMED_PATTERN = "eigenface_transformada_";
    public static final String FILE_RECONSTRUCTED_IMAGE_PATTERN = "imagem_reconstruida_";
    public static final String FILE_RECONSTRUCTED_IMAGE_TRANSFORMED_PATTERN = "imagem_reconstruida_transformada_";
    public static final String ARGUMENT_PATTERN = "-[a-zA-Z]";

    public static Scanner scanner = new Scanner(System.in);
    public static boolean interactive;
    public static StringBuilder txtContent;

    public static void main(String[] args) {

        String filePath, folderPath;
        int numEigenFaces, menuOption;

        //MODO NAO INTERATIVO
        if (args.length > 0) {
            boolean foundTxtFile = false;

            for (int i = 0; i < args.length; i++) {
                if (args[i].endsWith(".txt")) {
                    foundTxtFile = true;
                }
            }

            if (!foundTxtFile) {
                System.out.println("Nenhum argumento termina com .txt.\nA encerrar o programa...");
                return;
            }

            System.out.println(args[args.length - 1]);
            if (args[args.length - 1].matches(ARGUMENT_PATTERN)) {
                System.out.println("Argumentos inválidos. Insira os argumentos no formato correto.\nA encerrar o programa...");
                return;
            }
            for (int i = 0; i < args.length - 1; i++) {

                if (args[i].matches(ARGUMENT_PATTERN) && args[i + 1].matches(ARGUMENT_PATTERN)) {
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
            String txtFile = null;

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
                    default:
                        if (args[i].endsWith(".txt")) {
                            txtFile = args[i];
                            break;
                        } else {
                            System.out.println("Parâmetro desconhecido: " + args[i]);
                            return;
                        }
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
                if (caminhoCSV == null || !validateK(numEigenfaces) || !validateFilePath(caminhoCSV)) {
                    System.out.println("A encerrar o programa...");
                    return;
                } else {

                    double[][] readMatrix = getMatrixFromCSV(caminhoCSV);

                    if (readMatrix != null) {
                        if (validateMatrix(readMatrix)) {
                            int k = Integer.parseInt(numEigenfaces);
                            if (k == -1 || k > readMatrix.length) k = readMatrix.length;
                            execFunc1(readMatrix, k);
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {
                                writer.write(txtContent.toString());
                                System.out.println("Conteúdo salvo com sucesso no arquivo: " + txtFile);
                            } catch (IOException e) {
                                System.err.println("Erro ao guardar o conteúdo no arquivo: " + e.getMessage());
                            }
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
                if (!selectFolderToRead(caminhoImagens) || !validateK(numEigenfaces)) {
                    System.out.println("A encerrar o programa...");
                    return;
                } else {
                    File[] files = getCsvFiles(caminhoImagens);
                    if (files.length == 0) {
                        System.out.println("A encerrar o programa...");
                        return;
                    } else {
                        int k = Integer.parseInt(numEigenfaces);
                        execFunc2(files, k);
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {
                            writer.write(txtContent.toString());
                            System.out.println("Conteúdo salvo com sucesso no arquivo: " + txtFile);
                        } catch (IOException e) {
                            System.err.println("Erro ao guardar o conteúdo no arquivo: " + e.getMessage());
                        }
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
                if (!validateK(numEigenfaces) || !selectFolderToRead(caminhoImagens) || !validateFilePath(caminhoCSV)) {
                    System.out.println("A encerrar o programa...");
                    return;
                } else {
                    File[] files = getCsvFiles(caminhoImagens);
                    if (files.length == 0) {
                        System.out.println("A encerrar o programa...");
                        return;
                    } else {
                        int k = Integer.parseInt(numEigenfaces);
                        if (k > files.length || k == -1) {
                            k = files.length;
                            System.out.println("O número de EigenFaces a utilizar foi adaptado para o máximo: " + k);
                        }
                        execFunc3(caminhoImagens, k, caminhoCSV);
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {
                            writer.write(txtContent.toString());
                            System.out.println("Conteúdo salvo com sucesso no arquivo: " + txtFile);
                        } catch (IOException e) {
                            System.err.println("Erro ao guardar o conteúdo no arquivo: " + e.getMessage());
                        }
                        System.out.println("Funcionalidade executada com sucesso.");
                    }
                }
            } else if (func == 4) {
                if (numEigenfaces == null || caminhoImagens == null) {
                    System.out.println("Por favor, indique todos os argumentos necessários para a funcionalidade: " + func);
                    System.out.println("""
                            
                            Parâmetros necessários:
                              - k (número de eigenfaces)
                              - d (caminho para a pasta da base de imagens)
                            
                            """);
                    return;
                }
                if (!selectFolderToRead(caminhoImagens) || !validateK(numEigenfaces)) {
                    System.out.println("A encerrar o programa...");
                    return;
                } else {
                    File[] files = getCsvFiles(caminhoImagens);
                    if (files.length == 0) {
                        System.out.println("A encerrar o programa...");
                        return;
                    } else {
                        int k = Integer.parseInt(numEigenfaces);
                        execFunc4(caminhoImagens, k);
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {
                            writer.write(txtContent.toString());
                            System.out.println("Conteúdo salvo com sucesso no arquivo: " + txtFile);
                        } catch (IOException e) {
                            System.err.println("Erro ao guardar o conteúdo no arquivo: " + e.getMessage());
                        }
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
                    showMenuOptions();

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
                        showMenuTitle(1);
                        filePath = selectFileToRead();
                        double[][] readMatrix = getMatrixFromCSV(filePath);
                        if (readMatrix != null) {
                            if (validateMatrix(readMatrix)) {
                                int k = askNumberOfEigenvectors(readMatrix);
                                execFunc1(readMatrix, k);
                            } else System.out.println("\nInsira uma matriz válida.");
                        } else System.out.println("A matriz ainda não foi lida.");
                        break;
                    case 2:
                        showMenuTitle(2);
                        File[] files;

                        do {
                            folderPath = selectFolderToRead();
                            files = getCsvFiles(folderPath);
                        } while (files.length == 0);
                        numEigenFaces = askNumberOfEigenfaces();
                        if (numEigenFaces > files.length) numEigenFaces = files.length;
                        execFunc2(files, numEigenFaces);
                        break;
                    case 3:
                        showMenuTitle(3);
                        do {
                            folderPath = selectFolderToRead();
                            files = getCsvFiles(folderPath);
                        } while (files.length == 0);
                        numEigenFaces = askNumberOfEigenfaces();
                        if (numEigenFaces > files.length || numEigenFaces == -1) {
                            numEigenFaces = files.length;
                            System.out.println("O número de EigenFaces a utilizar foi adaptado para o máximo: " + numEigenFaces);
                        }


                        String newImagePath = selectFileToRead();
                        double[][] matrix3 = getMatrixFromCSV(newImagePath);
                        if (matrix3 != null) {
                            execFunc3(folderPath, numEigenFaces, newImagePath);
                        } else System.out.println("A matriz ainda não foi lida.");
                        break;
                    case 4:
                        do {
                            folderPath = selectFolderToRead();
                            files = getCsvFiles(folderPath);
                        } while (files.length == 0);
                        numEigenFaces = askNumberOfEigenfaces();
                        if (numEigenFaces > files.length) numEigenFaces = files.length;
                        execFunc4(folderPath, numEigenFaces);
                        break;
                    case 5:
                        showMenuTitle(4);
                        showDevelopmentTeam();
                        break;
                    case 0:
                        System.out.println("A fechar a aplicação...");
                        break;
                }

            } while (menuOption != 0);

        }
        scanner.close();
    }

    public static void execFunc4(String folderPath, int numEigenFaces) {
        File[] files = getCsvFiles(folderPath);
        int matrixSize = getNumberOfLines(files[0]);
        double[][] matrixVectors = processCsvFiles(files, matrixSize);
        double[] averageImages = calculateAverage(matrixVectors);

        double[][] matrixA = calculateMatrixA(matrixSize * matrixSize, files.length, matrixVectors, averageImages);
        double[][] transposedMatrixA = transpose(matrixA);
        double[][] matrixATA = matrixMultiplication(transposedMatrixA, matrixA);
        EigenDecomposition decomposition = new EigenDecomposition(MatrixUtils.createRealMatrix(matrixATA));
        double[][] eigenVectorsATA = decomposition.getV().getData();
        double[][] matrixEigenVectorsC = calculateEigenVectorsC(matrixA, eigenVectorsATA, numEigenFaces);
        double[][] matrixEigenFaces = normalizeVectorsC(matrixEigenVectorsC);

        double[] newImage = new double[matrixSize * matrixSize];
        for (int i = 0; i < newImage.length; i++) {
            newImage[i] = averageImages[i];
        }

        Random random = new Random();
        for (int i = 0; i < numEigenFaces; i++) {
            double lambda = decomposition.getRealEigenvalue(i);
            double w = random.nextDouble() * 2 * Math.sqrt(lambda) - Math.sqrt(lambda);
            double[] eigenFace = getMatrixVectorAtPosition(matrixEigenFaces, i);
            for (int j = 0; j < newImage.length; j++) {
                newImage[j] += w * eigenFace[j];
            }
        }

        newImage = normalizeImage(newImage);

        saveImageAsJPG("output/test_data/", newImage, matrixSize, 0, "new_image");

        if (interactive) System.out.println("\nNova imagem salva na pasta test_data com o nome new_image");
        else txtContent.append("\nNova imagem salva na pasta test_data com o nome new_image");
    }

    public static double[] normalizeImage(double[] image) {
        double min = Arrays.stream(image).min().orElse(0);
        double max = Arrays.stream(image).max().orElse(255);

        double[] normalizedImage = new double[image.length];
        for (int i = 0; i < image.length; i++) {
            normalizedImage[i] = 255 * (image[i] - min) / (max - min);
        }

        return normalizedImage;
    }

    public static void execFunc3(String folderPath, int numEigenFaces, String newImagePath) {
        int matrixSize;
        double[][] matrixA, eigenVectorsATA, matrixEigenFaces, matrixVectors, matrixATA, matrixEigenVectorsC, transposedMatrixA;
        double[] averageImages;
        File[] files;

        files = getCsvFiles(folderPath);

        matrixSize = getNumberOfLines(files[0]);
        matrixVectors = processCsvFiles(files, matrixSize);

        // Ler a nova imagem
        double[][] newImage = getMatrixFromCSV(newImagePath);

        int imageSize = getNumberOfLines(files[0]);

        if (imageSize != newImage.length) {
            System.out.println("A imagem lida não tem o mesmo tamanho que as imagens da base de dados.");
            return;
        }

        averageImages = calculateAverage(matrixVectors);
        if (interactive) printVector(averageImages, "Vetor µ: ");
        else txtContent.append(saveVector(averageImages, "Vetor µ: "));
        matrixA = calculateMatrixA(matrixSize * matrixSize, files.length, matrixVectors, averageImages);
        transposedMatrixA = transpose(matrixA);
        matrixATA = matrixMultiplication(transposedMatrixA, matrixA);
        eigenVectorsATA = getEigenVectors(matrixATA);
        matrixEigenVectorsC = calculateEigenVectorsC(matrixA, eigenVectorsATA, numEigenFaces);
        matrixEigenFaces = normalizeVectorsC(matrixEigenVectorsC);

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
            for (int i = 0; i < distances.length; i++) System.out.println("Distância de Ω" + i + ": " + distances[i]);

            System.out.println("Número de Eigenfaces utilizado: " + numEigenFaces);
            System.out.println("Novo Ω: " + Arrays.toString(newOmega));
            for (int i = 0; i < files.length; i++)
                System.out.println("Ω" + i + ": " + Arrays.toString(omegaMatrixDatabase[i]) + ", Distance: " + distances[i]);
        } else {
            for (int i = 0; i < distances.length; i++)
                txtContent.append("Distância de Ω").append(i).append(": ").append(distances[i]).append("\n");

            txtContent.append("Número de Eigenfaces utilizado: ").append(numEigenFaces).append("\n");
            txtContent.append("Novo Ω: ").append(Arrays.toString(newOmega)).append("\n");

            for (int i = 0; i < files.length; i++)
                txtContent.append("Ω").append(i).append(": ").append(Arrays.toString(omegaMatrixDatabase[i])).append(", Distance: ").append(distances[i]).append("\n");
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
            getImageAsJPG(files[closestImageIndices[i]], "output/test_data/imagem_mais_proxima_" + closestImageIndices[i] + ".jpg");
        }
    }

    public static void execFunc2(File[] files, int numEigenFaces) {
        double[][] matrixEigenVectorsC, transposedMatrixA, matrixVectors, matrixATA, eigenVectorsATA, matrixA, matrixEigenFaces;
        int matrixSize;

        double[] averageImages;
        matrixSize = getNumberOfLines(files[0]);
        matrixVectors = processCsvFiles(files, matrixSize);

        if (numEigenFaces == -1 || numEigenFaces > matrixVectors[0].length) {
            numEigenFaces = matrixVectors[0].length;
            System.out.println("O número de EigenFaces a utilizar foi adaptado para o máximo: " + numEigenFaces);
        }

        averageImages = calculateAverage(matrixVectors);
        if (interactive) printVector(averageImages, "Vetor µ:");
        else txtContent.append(saveVector(averageImages, "Vetor µ:"));
        matrixA = calculateMatrixA(matrixSize * matrixSize, files.length, matrixVectors, averageImages);
        transposedMatrixA = transpose(matrixA);
        matrixATA = matrixMultiplication(transposedMatrixA, matrixA);
        double[][] covarianceMatrix = calculateCovarianceMatrix(matrixATA, matrixVectors[0].length);
        if (interactive) {
            System.out.println("\nMatriz de Covariâncias:");
            printMatrix(covarianceMatrix);
        } else txtContent.append(saveMatrix(covarianceMatrix, "Matriz de Covariâncias:"));
        eigenVectorsATA = getEigenVectors(matrixATA);
        matrixEigenVectorsC = calculateEigenVectorsC(matrixA, eigenVectorsATA, numEigenFaces);
        matrixEigenFaces = normalizeVectorsC(matrixEigenVectorsC);
        if (interactive) System.out.println("\nNúmero de Eigenfaces utilizadas: " + numEigenFaces + "\n");
        else txtContent.append("\nNúmero de Eigenfaces utilizadas: " + numEigenFaces + "\n");
        saveEigenFaces(matrixEigenFaces, matrixSize, numEigenFaces);
        rebuildImages(files, matrixA, matrixEigenFaces, averageImages, numEigenFaces, matrixSize, matrixVectors);
    }

    public static void execFunc1(double[][] readMatrix, int k) {
        if (interactive) {
            System.out.println("\nNúmero de eigenfaces utilizadas:");
            System.out.printf("%d\n", k);

        } else {
            txtContent.append("Número de eigenfaces utilizado:" + k);

        }

        RealMatrix[] result = ownDecomposition(readMatrix);

        double[][] matrixP = result[0].getData();
        double[][] matrixD = result[1].getData();

        double[][][] decomposed = compressedMatrix(matrixP, matrixD, k);

        double[][] matrixPk = decomposed[0];
        double[][] matrixDk = decomposed[1];

        double[][] matrixAk = matrixMultiplication(matrixMultiplication(matrixPk, matrixDk), transpose(matrixPk));
        double eam = calculateEAM(readMatrix, matrixAk);

        if (interactive) {
            System.out.println("\nValores próprios utilizados:\n");
            printMatrix(matrixDk);
            System.out.println("\nVetores próprios utilizados:\n");
            printMatrix(matrixPk);
            System.out.println("\nErro absoluto médio:\n");
            System.out.printf("%.2f\n", eam);
            ;
            System.out.println("\nMatriz Reconstruída:\n");
            printMatrix(matrixAk);
        } else {
            txtContent.append(saveMatrix(matrixDk, "\nValores próprios utilizados"));
            txtContent.append(saveMatrix(matrixPk, "\nVetores próprios utilizados"));

            txtContent.append("\nErro absoluto médio:" + eam + "\n");
            txtContent.append(saveMatrix(matrixAk, "Matriz Reconstruída:"));
        }
        saveMatrixToCSV(matrixAk, OUTPUT_PATH);
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
            double[][] imageMatrix = getMatrixFromCSV(csvFile.getAbsolutePath());
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
            System.out.println("Imagem guardada como JPG: " + output.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro ao guardar a imagem JPG: " + e.getMessage());
        }
    }


    public static double[][] calculateMatrixA(int lines, int rows, double[][] matrixVectors, double[] averageImages) {
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
            if (interactive) printVector(weightsVector, "Vetor dos pesos da imagem " + i + ": ");
            else
                txtContent.append(saveVector(weightsVector, "Vetor dos pesos da imagem " + i + ": "));

            double[] originalImageVector = getMatrixVectorAtPosition(matrixVectors, i);
            double eam = calculateEAM(originalImageVector, reconstructedImage);

            if (interactive) System.out.printf("Erro Absoluto Médio (EAM) para a imagem %d: %.2f%n%n", i, eam);
            else txtContent.append(String.format("Erro Absoluto Médio (EAM) para a imagem %d: %.2f%n%n", i, eam));

            saveIntMatrixToCSV(reconstructedImage, OUTPUT_PATH_F2_REBUILT_IMAGES, matrixSize, i, FILE_RECONSTRUCTED_IMAGE_PATTERN);

            if (!validateVectorPixels(reconstructedImage)) {
                reconstructedImage = pixelScalingEigenFaces(reconstructedImage);
                saveIntMatrixToCSV(reconstructedImage, OUTPUT_PATH_F2_REBUILT_IMAGES, matrixSize, i, FILE_RECONSTRUCTED_IMAGE_TRANSFORMED_PATTERN);
            }
            saveImageAsJPG(OUTPUT_PATH_F2_REBUILT_IMAGES, reconstructedImage, matrixSize, i, FILE_RECONSTRUCTED_IMAGE_PATTERN);

        }
    }

    public static boolean validateVectorPixels(double[] reconstructedImage) {
        for (int i = 0; i < reconstructedImage.length; i++) {
            if (reconstructedImage[i] < 0 || reconstructedImage[i] > 255) {
                return false;
            }
        }
        return true;
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

    public static void saveIntMatrixToCSV(double[] vector, String outputPath, int matrixSize, int counter, String filePattern) {
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
            System.err.println("Erro ao guardar imagem como CSV: " + e.getMessage());
        }
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

                pixelValue = Math.max(0, Math.min(255, pixelValue));
                array[i][j] = pixelValue;
            }
        }

        GreyscaleImageWriter writer = new GreyscaleImageWriter();
        try {
            writer.writeArrayAsImage(array, savePath + filePattern + counter + ".jpg");
        } catch (IOException e) {
            System.err.println("Erro ao guardar imagem como JPG: " + e.getMessage());
        }
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
                    System.out.println("O número de Vetores Próprios a utilizar foi adaptado para o máximo: " + k);

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