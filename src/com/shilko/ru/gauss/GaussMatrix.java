package com.shilko.ru.gauss;

import java.util.*;

public class GaussMatrix {
    private double[][] matrix;

    public static void main(String... args) {
        MatrixIO.printMatrix(MatrixIO.readMatrix());
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

    public void randomInit() {
        final int MAX_RANDOM_VALUE = 100;
        randomInit(MAX_RANDOM_VALUE);
    }

    public void randomInit(int MAX_RANDOM_VALUE) {
        var random = new Random();
        for (int i = 0; i < matrix.length; ++i)
            for (int j = 0; j < matrix[0].length; ++j) {
                matrix[i][j] = random.nextDouble() * MAX_RANDOM_VALUE;
                if (random.nextBoolean())
                    matrix[i][j] *= -1;
            }
    }


}
