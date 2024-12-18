import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

import java.util.Scanner;
import java.util.Arrays;

import java.io.File;
import java.io.IOException;


public class Main {
    public static final String OUTPUT_PATH = "output/funcionalidade_1";
    public static final String OUTPUT_PATH_F2 = "output/funcionalidade_2/imagensReconstruidas";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int menuOption;

        do {
            menuOption = -1;

            while (menuOption < 0 || menuOption > 5) {
                UI_Utils.showMenuOptions();

                if (scanner.hasNextInt()) {
                    menuOption = scanner.nextInt();
                    if (menuOption < 0 || menuOption > 5) {
                        System.out.println("Invalid option. Please choose a valid option.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                }
            }

            switch (menuOption) {
                case 1:
                    UI_Utils.showMenuTitle(1);
                    String filePath = UI_Utils.selectFileToRead();


                    double[][] matrizLida = File_Utils.getMatrixFromCSV(filePath);

                    if (matrizLida != null) {

                        if (validateMatrix(matrizLida)) {
                            System.out.println("\nMatriz Lida:");
                            UI_Utils.printMatrix(matrizLida);

                            int k = askNumberOfEigenvectors(matrizLida);
                            RealMatrix[] resultado = decomposicaoPropria(matrizLida);
                            double[][] matrizP = resultado[0].getData();
                            double[][] matrizD = resultado[1].getData();

                            double[][][] decompostas = compressedMatrix(matrizP, matrizD, k);

                            double[][] matrizPk = decompostas[0];
                            double[][] matrizDk = decompostas[1];

                            double[][] matrizAk = Matrix_Utils.matrixMultiplication(Matrix_Utils.matrixMultiplication(matrizPk, matrizDk), Matrix_Utils.transpose(matrizPk));

                            System.out.println("\nMatriz Reconstruída:");
                            UI_Utils.printMatrix(matrizAk);

                            File_Utils.saveMatrixToCSV(matrizAk, OUTPUT_PATH);

                        } else {
                            System.out.println("\nIntroduza uma matriz válida.");
                        }
                    } else {

                        System.out.println("A matriz ainda não foi lida.");
                    }
                    break;
                case 2:
                    UI_Utils.showMenuTitle(2);
                    String folderPath = UI_Utils.selectFolderToRead();

                    File[] arquivos = File_Utils.getCsvFiles(folderPath);

                    int tamanhoMatriz = File_Utils.getNumberOfLines(arquivos[0]);

                    double[][] matrizVetores = File_Utils.processCsvFiles(arquivos, tamanhoMatriz);

                    double[] mediaImagens = calcularMedia(matrizVetores);

                    UI_Utils.printVetorMedia(mediaImagens);

                    double[][] matrizA = new double[tamanhoMatriz * tamanhoMatriz][arquivos.length];

                    for (int i = 0; i < arquivos.length; i++) {
                        for (int j = 0; j < tamanhoMatriz * tamanhoMatriz; j++) {
                            matrizA[j][i] = matrizVetores[j][i] - mediaImagens[j];
                        }
                    }

                    System.out.println("\nDimensões da Matriz (A) Φ: Linhas = " + matrizA.length + ", Colunas = " + matrizA[0].length + "\n");

                    double[][] matrizATransposta = Matrix_Utils.transpose(matrizA);
                    double[][] matrizATA = Matrix_Utils.matrixMultiplication(matrizATransposta, matrizA);

                    System.out.println("Matriz Aᵀ x A:");
                    UI_Utils.printMatrix(matrizATA);

                    double[][] vetoresPropriosATA = obterVetoresProprios(matrizATA);

                    System.out.println("\nQual o número de Eigenfaces que pretende utilizar?");
                    int numEigenFaces = scanner.nextInt();

                    double[][] matrizVetoresPropriosC = calcularVetoresPropriosC(matrizA, vetoresPropriosATA, numEigenFaces);

                    double[][] matrizEigenFaces = normalizarVetoresC(matrizVetoresPropriosC);

                    reconstruirImagens(matrizVetores, arquivos, matrizA, matrizEigenFaces, mediaImagens, numEigenFaces, tamanhoMatriz);

                    break;
                case 3:
                    UI_Utils.showMenuTitle(3);
                    //TODO: Implementar esta funcionalidade

                    break;
                case 4:
                    UI_Utils.showMenuTitle(4);
                    UI_Utils.showDevelopmentTeam();
                    //TODO: Implementar esta funcionalidade
                    break;
                case 0:
                    System.out.println("Closing application...");
                    break;
            }

        } while (menuOption != 0);

        scanner.close();
    }

    public static void reconstruirImagens(double[][] matrizOrginal, File[] arquivos, double[][] matrizA, double[][] matrizEigenFaces, double[] mediaImagens, int numEigenFaces, int tamanhoMatriz) {
        File outputFolder = new File(OUTPUT_PATH_F2);

        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        for (int i = 0; i < arquivos.length; i++) {

            double[] phi = obterPhiNaPosicao(matrizA, i);

            double[] arrayDaSoma = new double[matrizEigenFaces.length];
            double[] imagemReconstruida = new double[matrizEigenFaces.length];

            for (int j = 0; j < numEigenFaces; j++) {
                double[] ujTransposto = obterUjTransposto(matrizEigenFaces, j);
                double peso = calcularPeso(ujTransposto, phi);
                double[] uj = obterVetorDaMatrizNaPosicao(matrizEigenFaces, j);

                //multiplica vetor uj pelo peso
                for (int k = 0; k < uj.length; k++) {
                    uj[k] *= peso;
                }

                //soma o vetor uj ao arrayDaSoma
                for (int k = 0; k < uj.length; k++) {
                    arrayDaSoma[k] += uj[k];
                }
            }

            //somar arrayDaSoma à media
            for (int j = 0; j < mediaImagens.length; j++) {
                imagemReconstruida[j] = mediaImagens[j] + arrayDaSoma[j];
            }
            saveImageAsPNG(imagemReconstruida, tamanhoMatriz, i);
        }
    }

    public static double[] obterVetorDaMatrizNaPosicao(double[][] matrizEigenFaces, int j) {
        double[] vetor = new double[matrizEigenFaces.length];

        for (int i = 0; i < matrizEigenFaces.length; i++) {
            vetor[i] = matrizEigenFaces[i][j];
        }
        return vetor;
    }

    public static double calcularPeso(double[] ujTransposto, double[] phi) {
        double peso = 0.0;

        for (int i = 0; i < ujTransposto.length; i++) {
            peso += ujTransposto[i] * phi[i];
        }
        return peso;
    }

    public static void saveImageAsPNG(double[] vetorImagem, int tamanhoMatriz, int contador) {
        int[][] array = new int[tamanhoMatriz][tamanhoMatriz];
        for (int i = 0; i < tamanhoMatriz; i++) {
            for (int j = 0; j < tamanhoMatriz; j++) {
                int pixelValue = (int) Math.round(vetorImagem[i * tamanhoMatriz + j]);

                pixelValue = Math.max(0, Math.min(255, pixelValue));
                array[i][j] = pixelValue;
            }
        }

        GreyscaleImageWriter writer = new GreyscaleImageWriter();
        try {
            writer.writeArrayAsImage(array, OUTPUT_PATH_F2 + "/imagem_reconstruida_" + contador + ".png");
        } catch (IOException e) {
            System.err.println("Erro ao salvar a imagem como PNG: " + e.getMessage());
        }
    }

    public static double[] obterUjTransposto(double[][] matrizEigenFaces, int j) {
        double[] ujTransposto = new double[matrizEigenFaces.length];
        for (int i = 0; i < matrizEigenFaces.length; i++) {
            ujTransposto[i] = matrizEigenFaces[i][j];
        }
        return ujTransposto;
    }

    public static double[] obterPhiNaPosicao(double[][] matrizA, int index) {
        double[] phi = new double[matrizA.length];

        //este metodo coloca a coluna i de matriz A em phi
        for (int i = 0; i < matrizA.length; i++) {
            phi[i] = matrizA[i][index];
        }
        return phi;
    }

    public static double[][] calcularVetoresPropriosC(double[][] matrizA, double[][] vetoresPropriosATA, int numEigenFaces) {
        int numLinhasA = matrizA.length;
        int numColunasA = matrizA[0].length;

        double[][] matrizVetoresPropriosC = new double[numLinhasA][numEigenFaces];

        for (int i = 0; i < numEigenFaces; i++) {
            double[] vetorProprio = new double[numColunasA];

            for (int j = 0; j < numColunasA; j++) {
                vetorProprio[j] = vetoresPropriosATA[j][i];
            }

            double[] vetorProprioC = new double[numLinhasA];

            for (int j = 0; j < numLinhasA; j++) {
                for (int k = 0; k < numColunasA; k++) {
                    vetorProprioC[j] += matrizA[j][k] * vetorProprio[k];
                }
            }

            for (int j = 0; j < numLinhasA; j++) {
                matrizVetoresPropriosC[j][i] = vetorProprioC[j];
            }
        }
        return matrizVetoresPropriosC;
    }

    public static RealMatrix[] decomposicaoPropria(double[][] matrizLida) {
        // Decomposição própria
        RealMatrix[] result = new RealMatrix[2];
        EigenDecomposition eigenDecomposition = new EigenDecomposition(MatrixUtils.createRealMatrix(matrizLida));

        // Matriz dos vetores próprios (P)
        RealMatrix matrizP = eigenDecomposition.getV();
        result[0] = matrizP;
        // Matriz diagonal dos autovalores (D)
        RealMatrix matrizD = MatrixUtils.createRealDiagonalMatrix(eigenDecomposition.getRealEigenvalues());
        result[1] = matrizD;

        return result;
    }


    public static double[][][] compressedMatrix(double[][] matrizP, double[][] matrizD, int k) {
        double[][] auxP = matrizP;
        double[][] auxD = matrizD;

        int n = matrizD.length;
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

    /**
     * Função que faz a validação da matriz. Verifica se a matriz é quadrada, simétrica e se não contém valores negativos, retornando o erro específico caso exista.
     *
     * @param matrix
     * @return verdadeiro se a matriz é válida ou falso se não for
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
                    System.out.println("Opção -1: Utilizando todos os vetores próprios.");
                    return k;
                }

                if (k <= 0) {
                    System.out.println("""
                            Invalid number of eigenvectors.
                               Valid inputs:
                                    - A positive integer
                                    - -1 to use all eigenvectors
                            """);
                } else if (k > matrix.length) {
                    System.out.printf("The maximum number of eigenvectors is %d. Setting k to %d.%n", matrix.length, matrix.length);
                    k = matrix.length;
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        return k;
    }


    /**
     * Função para calcular o erro absoluto médio entre duas matrizes, a original e a reconstruída
     *
     * @param original
     * @param reconstructed
     * @return erro absoluto médio
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


    public static double[] calcularMedia(double[][] matrizVetores) {
        int numLinhas = matrizVetores.length;
        int numColunas = matrizVetores[0].length;

        double[] vetorMedio = new double[numLinhas];

        for (int i = 0; i < numLinhas; i++) {
            double soma = 0.0;
            for (int j = 0; j < numColunas; j++) {
                soma += matrizVetores[i][j];
            }
            vetorMedio[i] = soma / numColunas;
        }

        return vetorMedio;
    }

    public static void ordenarVetoresProprios(double[] valoresProprios, double[][] vetoresProprios) {

        for (int i = 0; i < valoresProprios.length; i++) {
            for (int j = i + 1; j < valoresProprios.length; j++) {
                if (valoresProprios[i] < valoresProprios[j]) {
                    double temp = valoresProprios[i];
                    valoresProprios[i] = valoresProprios[j];
                    valoresProprios[j] = temp;

                    double[] tempVector = vetoresProprios[i];
                    vetoresProprios[i] = vetoresProprios[j];
                    vetoresProprios[j] = tempVector;
                }
            }
        }
    }

    public static double[][] obterVetoresProprios(double[][] matrizATA) {
        RealMatrix matriz = MatrixUtils.createRealMatrix(matrizATA);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(matriz);

        double[][] vetoresProprios = eigenDecomposition.getV().getData();
        double[] valoresProprios = eigenDecomposition.getRealEigenvalues();

        System.out.println("\nValores Próprios:");
        for (double valor : valoresProprios) {
            System.out.println(valor);
        }


        ordenarVetoresProprios(valoresProprios, vetoresProprios);

        return vetoresProprios;
    }

    public static double[][] normalizarVetoresC(double[][] matrizVetoresPropriosC) {
        int numLinhas = matrizVetoresPropriosC.length;      // Número de linhas
        int numColunas = matrizVetoresPropriosC[0].length; // Número de colunas (número de vetores próprios)

        double[][] matrizEigenFaces = new double[numLinhas][numColunas];

        // Normaliza cada coluna (vetor próprio)
        for (int j = 0; j < numColunas; j++) {
            double soma = 0;

            // Calcula a norma do vetor (coluna j)
            for (int i = 0; i < numLinhas; i++) {
                soma += Math.pow(matrizVetoresPropriosC[i][j], 2);
            }
            double norma = Math.sqrt(soma);

            // Normaliza a coluna j
            for (int i = 0; i < numLinhas; i++) {
                matrizEigenFaces[i][j] = matrizVetoresPropriosC[i][j] / norma;
            }
        }
        return matrizEigenFaces;
    }
}