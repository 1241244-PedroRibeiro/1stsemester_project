A decomposição própria (Larson, 2012; Ghaoui L., 2024) de uma matriz é um processo utilizado para expressar uma matriz através do produto de três matrizes, combinando vetores próprios e valores próprios. É utilizada em áreas como a Resolução de Sistemas Lineares, a Modelação de Sistemas Dinâmicos e a Compressão de Imagem.

A decomposição própria de uma matriz simétrica \( A \) pode ser definida como:  
\[
A = P D P^T,
\]
em que \( P \) é a matriz cujas colunas são os vetores próprios unitários de \( A \); \( D \) é uma matriz diagonal cujos elementos são os valores próprios associados aos vetores próprios de \( A \); e \( P^T \) é a matriz transposta de \( P \).

Em algumas áreas de aplicação, como a compressão de imagem, por vezes é necessário reduzir a dimensão da representação e faz-se a decomposição própria utilizando um subconjunto de vetores próprios unitários. Esta técnica tem por objetivo aproximar a matriz original utilizando apenas os vetores próprios associados aos valores próprios de maior valor absoluto. Ao utilizar este processo, reduzimos a complexidade computacional preservando as características principais da matriz.

Se utilizarmos apenas um subconjunto de \( k \) vetores próprios (associados aos valores próprios de maior valor), obtemos uma aproximação de \( A \) representada por  
\[
A_k = P_k D_k P_k^T,
\]
em que \( P_k \) contém apenas os \( k \) vetores próprios unitários selecionados; \( D_k \) é a matriz diagonal contendo os \( k \) valores próprios com maior valor; e \( P_k^T \) é a matriz transposta de \( P_k \).

Para calcular a qualidade da reconstrução/aproximação utilizamos uma métrica como o Erro Absoluto Médio (EAM):  
\[
EAM(A, A_k) = \frac{1}{MN} \sum_{i=1}^M \sum_{j=1}^N |A(i, j) - A_k(i, j)|,
\]
em que \( A \) e \( A_k \) são as matrizes que representam, respetivamente, a matriz original e a matriz reconstruída com os \( k \) valores e vetores próprios selecionados. As variáveis \( M \) e \( N \) representam, respetivamente, o número de linhas e colunas da matriz.


funcionalidade 1:

- **Decomposição própria de uma matriz simétrica** (conforme descrito na Secção 2.1.1).  
  O cálculo dos valores e vetores próprios deve ser realizado com o auxílio da biblioteca commons apache math

- A aplicação deve permitir ao utilizador:
    - Definir o número de valores próprios a utilizar na decomposição.
    - Definir a localização do ficheiro CSV que contém a matriz de entrada.

- Após efetuar a decomposição, a aplicação deve:
    - Apresentar na consola:
        - O número, os valores e os vetores próprios utilizados na decomposição.
        - O Erro Absoluto Médio (EAM).
    - Apresentar na consola a matriz reconstruída.
    - Guardar a matriz reconstruída numa pasta com a designação `Output`, num ficheiro com o formato CSV.
