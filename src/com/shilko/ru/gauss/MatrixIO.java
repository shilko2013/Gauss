package com.shilko.ru.gauss;

import java.io.*;
import java.util.*;

public class MatrixIO {

    public static GaussMatrix readMatrix() {
        Scanner in = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Введите 0 чтобы ввести матрицу с клавиатуры,\n 1 для заполнения случайными значениями,\n 2 для считывания из файла: ");
                int n = in.nextInt();
                switch (n) {
                    case 0:
                        return readMatrix(in);
                    case 1:
                        return new GaussMatrix(readSize(in));
                    case 2:
                        GaussMatrix gaussMatrix = null;
                        while (gaussMatrix == null) {
                            System.out.print("Введите путь к файлу(файл должен состоять из n(n+1) чисел): ");
                            gaussMatrix = readMatrix(in.next());
                        }
                        return gaussMatrix;
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                System.out.println("Неверное число, повторите ввод.");
                in.nextLine();
            }
        }
    }

    private static int readSize(Scanner in) {
        while (true) {
            System.out.print("Введите количество неизвестных(от 1 до 20): ");
            try {
                int n = in.nextInt();
                if (n < 1 || n > 20)
                    throw new IllegalArgumentException();
                return n;
            } catch (Exception e) {
                System.out.println("Неверное количество неизвестных!");
                in.nextLine();
            }
        }
    }

    public static GaussMatrix readMatrix(Scanner in) {
        int n = readSize(in);
        double[][] matrix = new double[n][n + 1];
        System.out.println("Введите коэффициенты при неизвестных и свободные члены по порядку в формате");
        System.out.println("a11 a12 a13 ... a1n b1");
        System.out.println("a21 a22 a23 ... a2n b2");
        System.out.println("и тд.");
        System.out.println("Ввод: ");
        for (int i = 0; i < n; ++i)
            try {
                for (int j = 0; j < n + 1; ++j)
                    matrix[i][j] = Double.parseDouble(in.next().replaceAll(",","."));
            } catch (Exception e) {
                System.out.println("Произошла ошибка ввода, пожалуйста, введите последнюю строчку заново.");
                in.nextLine();
                i--;
            }
        System.out.println("Данные успешно введены.");
        return new GaussMatrix(matrix);
    }

    public static GaussMatrix readMatrix(String fileName) {
        Scanner in;
        int count, n;
        double[] numbers;
        try {
            in = new Scanner(new File(fileName));
        } catch (Exception e) {
            System.out.println("Неверный путь к файлу!");
            return null;
        }
        try {
            numbers = in.tokens().mapToDouble(num->Double.parseDouble(num.replaceAll(",","."))).toArray();
            count = numbers.length;
            n = (int) Math.ceil((-1 + Math.sqrt(1 + 4 * count)) / 2);
            count = 0;
            var matrix = new double[n][n + 1];
            for (int i = 0; i < n; ++i)
                for (int j = 0; j < n + 1; ++j)
                    matrix[i][j] = numbers[count++];
            System.out.println("Данные успешно введены.");
            return new GaussMatrix(matrix);
        } catch (Exception e) {
            System.out.println("Неверный формат файла!");
            return null;
        }
    }

    public static void printMatrix(GaussMatrix gaussMatrix) {
        printMatrix(gaussMatrix, System.out);
    }

    public static void printMatrix(GaussMatrix gaussMatrix, PrintStream out) {
        for (int i = 0; i < gaussMatrix.getMatrix().length; ++i) {
            for (int j = 0; j < gaussMatrix.getMatrix()[0].length; ++j)
                out.print(gaussMatrix.getMatrix()[i][j] + " ");
            out.println();
        }
    }

}
