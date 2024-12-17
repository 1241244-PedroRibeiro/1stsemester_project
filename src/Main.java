import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

import java.io.*;
import java.util.Scanner;
import java.util.Arrays;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static final String OUTPUT_PATH = "output";
    public static double[][] matrizLida, matrizP, matrizD, matrizPk, matrizDk, matrizAk;
    static Scanner scanner = new Scanner(System.in);
    static int k;

    public static void main(String[] args) {
        //Declaração de Variáveis
        int menuOption;

        //Leitura da Opção do Menu e Redirecionamento para os diferentes casos
        do {
            menuOption = -1;

            while (menuOption < 0 || menuOption > 5) {
                showMenuOptions();

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
                    showMenuTitle(1);
                    String filePath = selectFileToRead();
                    matrizLida = GetMatrixFromCSV(filePath);
                    break;
                case 2:
                    showMenuTitle(2);
                    if (matrizLida != null) {
                        k = askNumberOfEigenvectors(matrizLida);
                        RealMatrix[] resultado = decomposicaoPropria();
                        matrizP = resultado[0].getData();
                        matrizD = resultado[1].getData();

                        double[][][] decompostas = compressedMatrix();

                        matrizPk = decompostas[0];
                        matrizDk = decompostas[1];
                        /*=======PRINTS DE TESTE======
                        printMatrix(matrizPk);
                        printMatrix(matrizDk);
                        */

                        matrizAk = matrixMultiplication(matrixMultiplication(matrizPk, matrizDk), transpose(matrizPk));
                        /*=======PRINTS DE TESTE======
                        printMatrix(matrizAk);
                        */
                        System.out.println("Matriz Reconstruída:");
                        printMatrix(matrizAk);

                        saveMatrixToCSV(matrizAk);
                    } else {

                        System.out.println("A matriz ainda não foi lida.");
                    }
                    break;
                case 3:
                    showMenuTitle(3);
                    String folderPath = selectFolderToRead();

                    System.out.print("Digite o número de imagens que deseja processar: ");
                    int numberOfEigenFaces = scanner.nextInt();

                    File[] arquivos = getCsvFiles(folderPath, numberOfEigenFaces);
                    int limite = Math.min(numberOfEigenFaces, arquivos.length);
                    int tamanhoMatriz = getMaxMatrixSize(arquivos);

                    double[][] matrizVetores = processCsvFiles(arquivos, tamanhoMatriz, limite);
                    double[] mediaImagens = calcularMedia(matrizVetores, limite);

                    double[][] matrizA = new double[tamanhoMatriz][limite];
                    for (int i = 0; i < limite; i++) {
                        for (int j = 0; j < tamanhoMatriz; j++) {
                            matrizA[j][i] = matrizVetores[j][i] - mediaImagens[j];
                        }
                    }
                    double[][] matrizATransposta = transpose(matrizA);
                    double[][] matrizCovariancia = divideMatrizByLimit(matrixMultiplication(matrizATransposta, matrizA), limite);

                    printMatrix(matrizCovariancia);

                    break;
                case 4:
                    showMenuTitle(4);
                    //TODO: Implementar esta funcionalidade

                    break;
                case 5:
                    showMenuTitle(5);
                    showDevelopmentTeam();
                    //TODO: Implementar esta funcionalidade
                    break;
                case 0:
                    System.out.println("Closing application...");
                    break;
            }

        } while (menuOption != 0);

        scanner.close();
    }




    public static double[][] GetMatrixFromCSV(String caminhoCsv) {
        double[][] matrix = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoCsv))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Erro - O CSV está vazio.");
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
                        throw new IllegalArgumentException(
                                "Erro - número de colunas na linha " + (currentRow + 1) + " não corresponde ao esperado."
                        );
                    }

                    for (int i = 0; i < columnCount; i++) {
                        try {
                            matrix[currentRow][i] = Double.parseDouble(rowValues[i].trim());
                        } catch (NumberFormatException e) {
                            throw new NumberFormatException(
                                    "Erro ao converter valor - Linha " + (currentRow + 1) + ", Coluna " + (i + 1) + ": '" + rowValues[i] + "'"
                            );
                        }
                    }
                    currentRow++;
                }
            }

            newReader.close();

            System.out.println("Matriz lida com sucesso:");
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro - leitura do ficheiro CSV foi feita incorretamente: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro - leitura realizada incorretamente: " + e.getMessage());
        }
        return matrix;
    }


    public static RealMatrix[] decomposicaoPropria() {
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

    public static int getMaxMatrixSize(File[] arquivos) {
        int tamanhoMatriz = 0;
        for (File arquivo : arquivos) {
            try {
                int tamanhoAtual = obterTamanhoMatriz(arquivo);
                if (tamanhoAtual > tamanhoMatriz) {
                    tamanhoMatriz = tamanhoAtual;
                }
            } catch (IOException e) {
                System.err.println("Erro ao determinar o tamanho da matriz para o arquivo: " + arquivo.getName());
                e.printStackTrace();
            }
        }
        return tamanhoMatriz;
    }

    public static double[][] processCsvFiles(File[] arquivos, int tamanhoMatriz, int limite) {
        double[][] matrizVetores = new double[tamanhoMatriz][limite];

        for (int i = 0; i < limite; i++) {
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

    public static double[][][] compressedMatrix() {
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
            System.out.println("Qual é o número de vetores próprios (k)?");

            if (scanner.hasNextInt()) {
                k = scanner.nextInt();
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

        if (k == -1) {
            k = matrix.length;
        }

        return k;
    }

    /**
     * Função para imprimir uma matriz (object real matrix) para efeitos de teste e visualização
     *
     * @param matrix
     */
    public static void printRealMatrix(RealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                System.out.printf(matrix.getEntry(i, j) + " ");
            }
            System.out.println();
        }
    }

    /**
     * Função para imprimir uma matriz (double[][]) para efeitos de teste e visualização
     *
     * @param matrix
     */
    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%.2f ", matrix[i][j]);
            }
            System.out.println();
        }
    }

    public static double[][] transpose(double[][] matrix) {
        double[][] transposedmatrix = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (i == j) {
                    transposedmatrix[i][j] = matrix[i][j];
                } else {
                    transposedmatrix[j][i] = matrix[i][j];
                }
            }
        }

        return transposedmatrix;
    }

    public static double[][] matrixMultiplication(double[][] P, double[][] D) {
        double[][] calculatedMatrix = new double[P.length][D[0].length];
        for (int i = 0; i < P.length; i++) {
            for (int j = 0; j < D[0].length; j++) {

                for (int l = 0; l < P[0].length; l++) {
                    calculatedMatrix[i][j] += P[i][l] * D[l][j];
                }
            }

        }
        return calculatedMatrix;
    }

    public static String selectFolderToRead() {
        Scanner scanner = new Scanner(System.in);
        String folderPath = null;
        boolean isValid = false;

        while (!isValid) {
            System.out.println("Enter the path of the folder: ");
            folderPath = scanner.nextLine();
            File folder = new File(folderPath);

            if (folder.exists() && folder.isDirectory()) {
                isValid = true;
            } else {
                System.out.println("Invalid folder path. Please enter a valid path.");
            }
        }
        return folderPath;
    }

    public static String selectFileToRead() {
        Scanner scanner = new Scanner(System.in);
        String filePath = null;
        boolean isValid = false;

        while (!isValid) {
            System.out.println("Enter the path of the file: ");
            filePath = scanner.nextLine();
            File file = new File(filePath);

            if (file.exists() && file.isFile() && filePath.endsWith(".csv")) {
                isValid = true;
            } else {
                System.out.println("Invalid file path. Ensure the file exist and has a .csv extension.");
            }
        }
        return filePath;
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

    public static void saveMatrixToCSV(double[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            System.out.println("Matriz inválida. Não foi possível salvar no CSV.");
            return;
        }


        File outputDir = new File(OUTPUT_PATH);
        if (!outputDir.exists()) {
            if (outputDir.mkdir()) {
                System.out.println("Pasta 'OutPut' criada com sucesso.");
            } else {
                System.out.println("Erro ao criar a pasta 'OutPut'. Verifique as permissões.");
                return;
            }
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String outputFilename = "matrix_" + timestamp + ".csv";
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
            System.out.println("Matriz salva no arquivo CSV com sucesso: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Erro ao guardar a matriz no CSV: " + e.getMessage());
        }
    }
    //TODO: =================== TESTAR METODOS getCsvFiles,readImagesFromFolder,getVectorFromMatrix ================

    public static File[] getCsvFiles(String folderPath, int numberOfFiles) {
        try {
            File folder = new File(folderPath);

            if (!folder.exists() || !folder.isDirectory()) {
                throw new IOException("Pasta não encontrada: " + folderPath);
            }

            File[] files = folder.listFiles();
            int count = 0;

            for (int i = 0; i < files.length && count < numberOfFiles; i++) {
                if (files[i].getName().endsWith(".csv")) {
                    count++;
                }
            }

            if (count == 0) {
                throw new IOException("Nenhum arquivo CSV encontrado na pasta: " + folderPath);
            }

            File[] csvFiles = new File[Math.min(count, numberOfFiles)];
            int index = 0;

            for (int i = 0; i < files.length && index < numberOfFiles; i++) {
                if (files[i].getName().endsWith(".csv")) {
                    csvFiles[index++] = files[i];
                }
            }
            return csvFiles;

        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return new File[0];
        }
    }


    /*public static double[][] readImagesFromFolder(String folderPath, int numberOfEigenFaces) {
        File[] files = getCsvFiles(folderPath, numberOfEigenFaces);

        if (files == null) {
            System.out.println("Erro ao ler a pasta de imagens.");
            return null;
        }

        if (files.length < numberOfEigenFaces) {
            numberOfEigenFaces = files.length;
            System.out.println("O número de imagens a serem processadas foi ajustado para " + numberOfEigenFaces + "pois a pasta contém apenas " + numberOfEigenFaces + " imagens.");
        }

        double[][] imagesMatrix = new double[256 * 256][numberOfEigenFaces];
        double[] currentVector;

        for (int j = 0; j < numberOfEigenFaces; j++) {
            currentVector = getVectorFromMatrix(GetMatrixFromCSV(files[j].getAbsolutePath()));

            for (int i = 0; i < currentVector.length; i++) {
                imagesMatrix[i][j] = currentVector[i];
            }
        }
        return imagesMatrix;
    }

    public static double[] getVectorFromMatrix(double[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            System.out.println("A matriz está vazia ou nula.");
            return null;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        int size = rows * cols;

        double[] vector = new double[size];

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                vector[index] = matrix[i][j];
                index++;
            }
        }

        return vector;
    }*/


    public static int obterTamanhoMatriz(File arquivoCSV) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            String linha = br.readLine();
            if (linha != null) {
                String[] valores = linha.split(",");
                return valores.length * valores.length;
            } else {
                throw new IOException("Arquivo vazio ou formato inválido.");
            }
        }
    }

    public static double[] converterCSVparaVetor(File arquivoCSV, int tamanhoMatriz) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            double[] vetor = new double[tamanhoMatriz];
            String linha;
            int indice = 0;

            while ((linha = br.readLine()) != null) {
                String[] valores = linha.split(",");
                for (String valor : valores) {
                    try {
                        vetor[indice++] = Double.parseDouble(valor.trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Valor inválido no CSV: " + valor);
                        throw e;
                    }
                }
            }

            if (indice != vetor.length) {
                throw new IOException("O arquivo CSV não contém o número esperado de elementos.");
            }

            return vetor;
        }
    }

    public static double[] calcularMedia(double[][] matrizVetores, int n) {
        double[] media = new double[matrizVetores.length];

        for (int i = 0; i < matrizVetores.length; i++) {
            double soma = 0;
            for (int j = 0; j < n; j++) {
                soma += matrizVetores[i][j];
            }
            media[i] = soma / n;
        }

        return media;
    }

    public static double[][] divideMatrizByLimit(double[][] matriz, int limite) {
        for (int i = 0; i < limite; i++) {
            for (int j = 0; j < limite; j++) {
                matriz[i][j] /= limite;
            }

        }
        return matriz;
    }

    public static double[][] normalizeVectors(double[][] matriz){
        int linhas = matriz.length;
        int colunas = matriz[0].length;
        double[][] matrizNormalizada=new double[linhas][colunas];
        for(int j=0; j<colunas; j++){
            double magnitude=0.0;
            for(int i=0; i<linhas; i++){
                magnitude+=matriz[i][j]*matriz[i][j];
            }
            magnitude=Math.sqrt(magnitude);
            if(magnitude!=0){
                for(int i=0;i<linhas;i++){
                    matrizNormalizada[i][j]=matriz[i][j]/magnitude;
                }
            }
            else{
                for (int i = 0; i < linhas; i++) {
                    matrizNormalizada[i][j] = Double.NaN;
                }
                System.out.printf("Aviso: A matriz de vetores na coluna %d tem magnitude=0\n",j);
            }
        }

        return matrizNormalizada;
    }



    public static void showMenuOptions() {
        System.out.println("""
                
                  ╔══════════════════════════════════════════════╗
                  ║                 MENU OPTIONS                 ║
                  ╠══════════════════════════════════════════════╣
                  ║                                              ║
                  ║   1. Import matrix from csv file             ║
                  ║   2. Functionality 1                         ║
                  ║   3. Functionality 2                         ║
                  ║   4. Functionality 1                         ║
                  ║   5. Know Development Team                   ║
                  ║   0. Exit                                    ║
                  ║                                              ║
                  ╚══════════════════════════════════════════════╝
                """);
        System.out.print("     Choose Option: ");
    }

    public static void showMenuTitle(int uiNumber) {
        switch (uiNumber) {
            case 1:
                System.out.println("""
                        
                          ╔══════════════════════════════════════════════╗
                          ║        1. Import matrix from csv file        ║
                          ╚══════════════════════════════════════════════╝
                        """);
                break;
            case 2:
                System.out.println("""
                        
                          ╔══════════════════════════════════════════════╗
                          ║              2. Functionality 1              ║
                          ╚══════════════════════════════════════════════╝
                        """);
                break;
            case 3:
                System.out.println("""
                        
                          ╔══════════════════════════════════════════════╗
                          ║              3. Functionality 2              ║
                          ╚══════════════════════════════════════════════╝
                        """);
                break;
            case 4:
                System.out.println("""
                        
                          ╔══════════════════════════════════════════════╗
                          ║              4. Functionality 3              ║
                          ╚══════════════════════════════════════════════╝
                        """);
                break;
            case 5:
                System.out.println("""
                        
                          ╔══════════════════════════════════════════════╗
                          ║           5. Know Development Team           ║
                          ╚══════════════════════════════════════════════╝
                        """);
                break;

        }
    }

    public static void showDevelopmentTeam() {
        System.out.println("""
                    LAPR1 - Projeto Integrador
                    Grupo 04 - BitMinds
                
                    Desenvolvido por:
                        Pedro Costa     - 1221790
                        Pedro Ribeiro   - 1241244
                        Afonso Mota     - 1241225
                        Diogo Oliveira  - 1241883
                """);
    }
}