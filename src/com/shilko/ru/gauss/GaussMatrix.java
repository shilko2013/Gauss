package com.shilko.ru.gauss;

import java.util.*;

import static java.lang.Math.*;

public class GaussMatrix {
    private double[][] matrix;

    public static void main(String... args) {
        MatrixIO.printMatrix(MatrixIO.readMatrix("matrix.txt").gauss());
    }

    public double[][] getMatrix() {
        return matrix;
    }

    private void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public GaussMatrix(double[][] matrix) {
        setMatrix(matrix);
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

    public GaussMatrix gauss() { //changes matrix

        var equalVars = new int[matrix.length];

        for (int i = 0; i < matrix.length; ++i) //заполнение таблицы соответствия столбца переменной
            equalVars[i] = i;

        for (int iteration = 0; iteration < matrix.length - 1; ++iteration) {

            int n = matrix.length - iteration;

            double max = matrix[0][0];
            int maxi = 0, maxj = 0;

            for (int i = 0; i < n; ++i) { //поиск наибольшего по модулю коэффициента
                for (int j = 0; j < n; ++j) {
                    if (abs(matrix[i][j]) > abs(max)) {
                        max = matrix[i][j];
                        maxi = i;
                        maxj = j;
                    }
                }
            }

            double[] koef = new double[n]; //нахождение коэффициентов для умножения строк
            for (int i = 0; i < n; ++i)
                koef[i] = -matrix[i][maxj] / max;

            for (int i = 0; i < n; ++i) { //сложение строк
                for (int j = 0; j < n + 1; ++j) {
                    if (i == maxi)
                        continue;
                    matrix[i][j] += matrix[maxi][j] * koef[i];
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

            MatrixIO.printMatrix(this);
            System.out.println();

        }

        return this;
    }
}
