## Reconstrução de Imagens utilizando Eigenfaces

### Reconstrução de cada uma das imagens disponíveis numa pasta utilizando Eigenfaces

A aplicação deve permitir ao utilizador definir:
- O número de eigenfaces a utilizar na reconstrução.
- A pasta onde estão localizadas as imagens a reconstruir.

#### Output da aplicação:
1. Apresentar na consola:
    - O vetor **µ**.
    - A matriz de covariâncias **C**.
    - O número de eigenfaces utilizadas na reconstrução de cada imagem.
    - Para cada imagem **I** da base de imagens, o vetor com os pesos **w** utilizados na reconstrução da imagem, gerando a imagem reconstruída **¯I**.
2. As eigenfaces e cada uma das imagens reconstruídas devem ser gravadas simultaneamente nos formatos:
    - **CSV**
    - **JPG**

As imagens devem ser armazenadas em pastas denominadas:
- **Eigenfaces**
- **ImagensReconstruidas**

---

## Reconstrução e Identificação de Imagens utilizando Eigenfaces

A reconstrução e identificação de imagens digitais são tarefas importantes que podem ser realizadas automaticamente utilizando algoritmos baseados em eigenfaces.

### Imagem Digital

Uma imagem digital é uma representação discreta de uma imagem bidimensional utilizando um conjunto finito de números. Cada elemento da imagem (um pixel) possui um valor para uma determinada localização.

Uma imagem digital pode ser representada por uma matriz onde cada coeficiente representa a intensidade da imagem em uma dada localização. Por exemplo, na escala de cinzentos, os coeficientes variam entre **0** e **255**.

---

### Reconstrução/Representação de Imagens utilizando Eigenfaces

Reconstruir uma imagem digital num espaço de menor dimensão é uma operação útil para armazenar ou transmitir imagens de grandes dimensões. Este processo pode ser feito utilizando diversos métodos, como:
- Transformada Discreta de Cosseno.
- Compressão Fratal.
- Compressão por Wavelets.
- Decomposição em Valores Singulares (SVD).

Um método simples e eficaz é a utilização de **eigenfaces**. Este método consiste nos seguintes passos:

#### Passos para a reconstrução com Eigenfaces:

1. **Transformar as imagens da base para o formato vetor**:
   Cada imagem na base de imagens (com **N imagens**) é convertida para o formato vetor.

2. **Construir a matriz de imagens**:
   Construir a matriz **M**, onde cada coluna representa uma imagem da base.

3. **Construir a matriz de covariâncias C**:
    - Calcular o vetor (coluna) médio, **µ**, contendo a média de todas as imagens.
    - Para cada imagem **Iᵢ**, calcular:
      ```
      Φᵢ = Iᵢ − µ
      ```
    - Construir a matriz **A**:
      ```
      A = [Φ₁, ..., Φₙ]
      ```
    - Calcular:
      ```
      C = (1 / N) * A Aᵀ
      ```

4. **Calcular os valores e vetores próprios de C**:
    - Para otimização, usar a matriz **AᵀA** (menor dimensão) e os seus vetores próprios.
    - Obter os valores próprios de **A Aᵀ** através da relação:
      ```
      A vᵢ = uᵢ
      ```
      Onde **vᵢ** são os vetores próprios de **AᵀA**.

5. **Normalizar os vetores próprios**:
    - Normalizar para que:
      ```
      ||uᵢ|| = 1
      ```
    - Estes vetores próprios normalizados são as **eigenfaces**.

6. **Reconstruir a imagem utilizando as eigenfaces**:
    - Reconstruir uma imagem **Iᵢ** com **k eigenfaces**:
      ```
      Iᵢ = µ + ∑ (de j=1 até k) uⱼ wⱼ
      ```
      Onde:
      ```
      wⱼ = uⱼᵀ Φᵢ
      ```

---

### Métrica de Qualidade da Reconstrução

Para calcular a qualidade da reconstrução, utiliza-se o **Erro Absoluto Médio (EAM)**:
EAM(I, ¯I), em que I e ¯I s˜ ao as matrizes que representam, respetivamente, a imagem original e
a imagem reconstruida com k valores pr´oprios.
