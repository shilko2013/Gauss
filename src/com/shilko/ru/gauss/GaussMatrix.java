package com.shilko.ru.gauss;

import java.util.*;

import static java.lang.Math.*;

public class GaussMatrix {
    private double[][] matrix;
    private final double[][] originalMatrix;
    private int[] equalVars;

    public double[][] getMatrix() {
        return matrix;
    }

    public double[][] getOriginalMatrix() {
        return originalMatrix;
    }

    public int[] getEqualVars() {
        return equalVars;
    }

    private void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public GaussMatrix(double[][] matrix) {
        setMatrix(matrix);
        feelEqualVars();
        originalMatrix = matrix;
    }

    public GaussMatrix(int n) {
        this(n, true);
    }

    public GaussMatrix(int n, boolean randomInit) {
        if (n < 1 || n > 20)
            throw new IllegalArgumentException();
        setMatrix(new double[n][n + 1]);
        if (randomInit)
            randomInit();
        feelEqualVars();
        originalMatrix = matrix;
    }

    public GaussMatrix randomInit() {
        final int MAX_RANDOM_VALUE = 100;
        randomInit(MAX_RANDOM_VALUE);
        return this;
    }

    public GaussMatrix randomInit(int maxRandomValue) {
        var random = new Random();
        for (int i = 0; i < matrix.length; ++i)
            for (int j = 0; j < matrix[0].length; ++j) {
                matrix[i][j] = random.nextDouble() * maxRandomValue;
                if (random.nextBoolean())
                    matrix[i][j] *= -1;
            }
        return this;
    }

    private void feelEqualVars() {
        equalVars = new int[matrix.length];
        for (int i = 0; i < matrix.length; ++i) //заполнение таблицы соответствия столбца переменной
            equalVars[i] = i;
    }

    public GaussMatrix triangleMatrix() {
        for (int iteration = 0; iteration < matrix.length - 1; ++iteration) {

            int n = matrix.length;

            double max = matrix[iteration][iteration];
            int maxi = iteration, maxj = iteration;

            for (int i = iteration; i < n; ++i) { //поиск наибольшего по модулю коэффициента
                for (int j = iteration; j < n; ++j) {
                    if (abs(matrix[i][j]) > abs(max)) {
                        max = matrix[i][j];
                        maxi = i;
                        maxj = j;
                    }
                }
            }

            double[] koefs = new double[n]; //нахождение коэффициентов для умножения строк
            for (int i = iteration; i < n; ++i)
                koefs[i] = -matrix[i][maxj] / max;

            for (int i = iteration; i < n; ++i) { //сложение строк
                for (int j = iteration; j < n + 1; ++j) {
                    if (i == maxi)
                        continue;
                    matrix[i][j] += matrix[maxi][j] * koefs[i];
                }
            }

            for (int j = 0; j < n + 1; ++j) { //формирование треугольной матрицы
                double temp = matrix[iteration][j];
                matrix[iteration][j] = matrix[maxi][j];
                matrix[maxi][j] = temp;
            }

            for (int i = 0; i < n; ++i) {
                double temp = matrix[i][iteration];
                matrix[i][iteration] = matrix[i][maxj];
                matrix[i][maxj] = temp;
            }

            var temp = equalVars[iteration];
            equalVars[iteration] = equalVars[maxj];
            equalVars[maxj] = temp;

            //MatrixIO.printMatrix(this); for DEBUGGING!!!
        }

        return this;
    }

    public double determinant() { //работает только после triangleMatrix()
        double acc = 1;
        for (int i = 0; i < matrix.length; ++i)
            acc *= matrix[i][i];
        return acc;
    }

    public double[] roots() { //работает только после triangleMatrix()
        var roots = new double[matrix.length];

        for (int rootNumber = roots.length - 1; rootNumber >= 0; --rootNumber) { //подсчет корней
            double different = 0;
            for (int j = rootNumber; j < matrix[0].length - 1; ++j) {
                different += matrix[rootNumber][j] * roots[j];
            }
            roots[rootNumber] = (matrix[rootNumber][matrix[0].length - 1] - different) / matrix[rootNumber][rootNumber];
        }

        var equalTable = equalVars;
        for (int i = 0; i < roots.length; ++i) {
            int j;
            for (j = 0; j < roots.length; ++j)
                if (equalTable[j] == i)
                    break;
            int temp = equalTable[i];
            equalTable[i] = equalTable[j];
            equalTable[j] = temp;
            double temp1 = roots[j];
            roots[j] = roots[i];
            roots[i] = temp1;
        }

        return roots;
    }

    public double[] discrepancies() { //работает только после triangleMatrix()
        var roots = roots();
        var discrepancies = new double[roots.length];
        for (int i = 0; i < roots.length; ++i) {
            double leftSide = 0;
            for (int j = 0; j < originalMatrix.length; ++j)
                leftSide += originalMatrix[i][j] * roots[j];
            discrepancies[i] = originalMatrix[i][originalMatrix.length] - leftSide;
        }

        return discrepancies;
    }
}
