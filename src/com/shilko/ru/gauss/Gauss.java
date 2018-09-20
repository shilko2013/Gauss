package com.shilko.ru.gauss;

import java.io.PrintStream;
import java.util.Random;

public class Gauss {
    private double[][] matrix;
    private final PrintStream out;

    public static void main(String ... args) {
        new Gauss(5).printMatrix();
    }

    public Gauss(int n) {
        this(n, true, System.out);
    }

    public Gauss(int n, boolean randomInit) {
        this(n, randomInit, System.out);
    }

    public Gauss(int n, boolean randomInit, PrintStream printStream) {
        if (n < 1 || n > 20)
            throw new IllegalArgumentException();
        matrix = new double[n+1][n];
        out = printStream;
        if (randomInit)
            randomInit();
    }

    public void printMatrix() {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j)
                out.print(matrix[i][j]+" ");
            out.println();
        }
    }

    public void randomInit() {
        final int MAX_RANDOM_VALUE = 100;
        var random = new Random();
        for (int i = 0; i < matrix.length; ++i)
            for (int j = 0; j < matrix[0].length; ++j) {
                matrix[i][j] = random.nextDouble() * MAX_RANDOM_VALUE;
                if (random.nextBoolean())
                    matrix[i][j] *= -1;
            }
    }
}
