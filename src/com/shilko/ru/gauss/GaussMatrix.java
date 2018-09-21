package com.shilko.ru.gauss;

import java.util.*;

import static java.lang.Math.*;

public class GaussMatrix {
    private double[][] matrix;
    private int[] equalVars;

    public double[][] getMatrix() {
        return matrix;
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
    }

    public GaussMatrix randomInit() {
        final int MAX_RANDOM_VALUE = 100;
        randomInit(MAX_RANDOM_VALUE);
        return this;
    }

    public GaussMatrix randomInit(int MAX_RANDOM_VALUE) {
        var random = new Random();
        for (int i = 0; i < matrix.length; ++i)
            for (int j = 0; j < matrix[0].length; ++j) {
                matrix[i][j] = random.nextDouble() * MAX_RANDOM_VALUE;
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

            for (int j = iteration; j < n + 1; ++j) { //формирование треугольной матрицы
                double temp = matrix[iteration][j];
                matrix[iteration][j] = matrix[maxi][j];
                matrix[maxi][j] = temp;
            }

            for (int i = iteration; i < n; ++i) {
                double temp = matrix[i][iteration];
                matrix[i][iteration] = matrix[i][maxj];
                matrix[i][maxj] = temp;
            }

            var temp = equalVars[iteration];
            equalVars[iteration] = equalVars[maxj];
            equalVars[maxj] = temp;

        }

        return this;
    }

    public double determinant() {
        double acc = 1;
        for (int i = 0; i < matrix.length; ++i)
            acc *= matrix[i][i];
        return acc;
    } //работает только после triangleMatrix()
}
