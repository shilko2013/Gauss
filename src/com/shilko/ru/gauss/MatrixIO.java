package com.shilko.ru.gauss;

import java.io.*;
import java.util.*;

public class MatrixIO {

    public static void main(String... args) {
        MatrixIO.printHeader();
        while (true) {
            GaussMatrix gaussMatrix = readMatrix();
            System.out.println("Введенная матрица: ");
            MatrixIO.printMatrix(gaussMatrix);
            MatrixIO.gauss(gaussMatrix);
        }
    }

    public static void printHeader() {
        System.out.println("Программа для решения СЛАУ методом Гаусса с выбором главного элемента.");
    }

    public static GaussMatrix readMatrix() {
        Scanner in = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Для начала работы введите" +
                        "\n 0 чтобы ввести матрицу с клавиатуры," +
                        "\n 1 для заполнения случайными значениями," +
                        "\n 2 для считывания из файла " +
                        "\n q или 3 для выхода из программы: ");
                String token = in.next();
                switch (token) {
                    case "0":
                        return readMatrix(in);
                    case "1":
                        return new GaussMatrix(readSize(in));
                    case "2":
                        GaussMatrix gaussMatrix = null;
                        while (gaussMatrix == null) {
                            System.out.print("Введите путь к файлу(файл должен состоять из n(n+1) чисел, где n - количество неизвестных): ");
                            gaussMatrix = readMatrix(in.next());
                        }
                        return gaussMatrix;
                    case "3":
                    case "q":
                        System.exit(0);
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                System.out.println("Неверный символ, повторите ввод.");
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
                    matrix[i][j] = Double.parseDouble(in.next().replaceAll(",", "."));
            } catch (Exception e) {
                System.out.println("Произошла ошибка ввода, пожалуйста, введите последнюю строчку заново.");
                in.nextLine();
                i--;
            }
        return new GaussMatrix(matrix);
    }

    /*
    формат файла:
    n(n+1) чисел через любые пробельные символы, в конце файла комментарий после символов //
     */
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
            numbers = in.tokens().takeWhile(s -> !s.startsWith("//")).mapToDouble(num -> Double.parseDouble(num.replaceAll(",", "."))).toArray();
            count = numbers.length;
            n = (int) Math.ceil((-1 + Math.sqrt(1 + 4 * count)) / 2);
            count = 0;
            var matrix = new double[n][n + 1];
            for (int i = 0; i < n; ++i)
                for (int j = 0; j < n + 1; ++j)
                    matrix[i][j] = numbers[count++];
            System.out.println("Данные успешно введены.");
            in.close();
            return new GaussMatrix(matrix);
        } catch (Exception e) {
            System.out.println("Неверный формат файла!");
            in.close();
            return null;
        }
    }

    public static void printMatrix(GaussMatrix gaussMatrix) {
        printMatrix(gaussMatrix, System.out);
    }

    public static void printMatrix(GaussMatrix gaussMatrix, PrintStream out) {
        final int fieldWidth = 11;
        final int precision = 5;
        for (int i = 0; i < gaussMatrix.getMatrix().length; ++i) {
            String temp = "x" + (gaussMatrix.getEqualVars()[i] + 1);
            out.printf("%" + fieldWidth + "s", temp);
        }
        out.printf("%" + fieldWidth + "c", 'b');
        out.println();
        for (int i = 0; i < gaussMatrix.getMatrix().length; ++i) {
            for (int j = 0; j < gaussMatrix.getMatrix()[0].length; ++j)
                out.printf("%" + fieldWidth + "." + precision + "f", gaussMatrix.getMatrix()[i][j]);
            out.println();
        }
        out.println();
    }

    public static void printMatrixOrdered(GaussMatrix gaussMatrix) {
        printMatrixOrdered(gaussMatrix, System.out);
    }

    public static void printMatrixOrdered(GaussMatrix gaussMatrix, PrintStream out) {
        var matrix = gaussMatrix.getMatrix();
        var equalVars = gaussMatrix.getEqualVars();
        for (int i = 0; i < matrix.length; ++i) {
            int j;
            for (j = 0; j < matrix.length; ++j)
                if (equalVars[j] == i)
                    break;
            int temp = equalVars[i];
            equalVars[i] = equalVars[j];
            equalVars[j] = temp;
            for (int k = 0; k < matrix.length; ++k) {
                double temp1 = matrix[k][j];
                matrix[k][j] = matrix[k][i];
                matrix[k][i] = temp1;
            }
        }
        var result = new GaussMatrix(matrix);
        printMatrix(result);
        out.println();
    }

    public static void gauss(GaussMatrix gaussMatrix) {
        gaussMatrix.triangleMatrix();
        final double determinant = gaussMatrix.determinant();
        if (determinant == 0 || Double.isNaN(determinant)) {
            System.out.println("Данную СЛАУ невозможно решить методом главных элементов, повторите ввод!");
            return;
        }
        System.out.println("Определитель матрицы = " + determinant);
        printMatrix(gaussMatrix);
        printValues(gaussMatrix.roots(), true);
        printValues(gaussMatrix.discrepancies(), false);
    }

    /*
    @param areRoots - если true, то печатает корни, иначе - невязки
     */
    public static void printValues(double[] roots, boolean areRoots) {
        String token = areRoots ? "x" : "ε";
        for (int i = 0; i < roots.length; ++i)
            //System.out.printf(token + (i + 1) + " = %5.18f\n", roots[i]);
            System.out.println(token + (i + 1)+ " = " + roots[i]);
        System.out.println();
    }

}
