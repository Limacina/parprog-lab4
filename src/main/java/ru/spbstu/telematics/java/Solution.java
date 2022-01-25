package ru.spbstu.telematics.java;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class Solution {
    static final Random r = new Random();
    static int MAX = 100, bound = 30;

    public static double[][] createMatrix(int n, int p) {
        double[][] mtx = new double[n][n];
        int f = -2, s = 0, t = 1;
        for (int i = 0; i < n; i++) {
            if(++f > -1) mtx[i][f] = r.nextInt(bound) + 1;
            mtx[i][s++] = r.nextInt(bound) + 1;
            if(t < n) mtx[i][t++] =  r.nextInt(bound) + 1;
        }
        return mtx;
    }

    public static double[] createVector(int n) {
        double[] vec = new double[n];
        for (int i = 0; i < n; i++)
            vec[i] = r.nextInt(bound) + 1;
        return vec;
    }

    public static void printMatrix(double [][]mtx, int n){
        System.out.println("Matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(String.format("%.3f", mtx[i][j]) + "\t");
            }
            System.out.print("\n");
        }
    }

    public static void printVector(double []vec, int n){
        for (int i = 0; i < n; i++) System.out.print(String.format("%.3f", vec[i]) + "\n");
    }

    public static void main(String[] arr) throws Exception {
        int p = 10;
        for (int n = 500; n < 1000; n+= 100) {
            System.out.print("n = " + n + "\n");
            System.out.print("p = " + p + "\n");
            double[] X = new double[n];
            int t1 = 0, t2 = 0;
            double[][] mtx = createMatrix(n, p);
            //printMatrix(mtx, n);
            double[] vec = createVector(n);
            //System.out.println("Vector:");
            //printVector(vec, n);
            Instant begin_par = Instant.now();
            Parallel.solve(mtx, vec, n, p, X);
            Instant end_par = Instant.now();
            t1 += Duration.between(begin_par, end_par).toMillis();
            //System.out.println("Solutions parallel:");
            //printVector(X, n);
            System.out.printf("Time parallel: %d milliseconds%n", t1);
            for (int i = 0; i < n; i++)
                X[i] = 0;
            Instant begin_cons = Instant.now();
            Consequent.solve(mtx, vec, n, p, X);
            Instant end_cons = Instant.now();
            t2 += Duration.between(begin_cons, end_cons).toMillis();
            //System.out.println("Solutions consequent:");
            //printVector(X, n);
            System.out.printf("Time consequent: %d milliseconds%n", t2);
            for (int i = 0; i < n; i++)
                X[i] = 0;
        }
    }
}