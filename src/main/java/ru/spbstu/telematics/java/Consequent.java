package ru.spbstu.telematics.java;

public class Consequent {
    static int MAX = 100;
    public static void solve(double [][]mtx, double []vec, int n, int p, double []res){
        double []Y = new double[n];
        double [][]lower = new double[n][n], upper = new double[n][n];
        for (int i = 0; i < n; i++){
            for (int j = i; j < Math.min(n, p + i + 1); j++) {
                double sum = 0;
                for (int k = Math.max(0, j - p); k < i; k++){
                    sum += (lower[i][k] * upper[k][j]);
                }
                upper[i][j] = mtx[i][j] - sum;
            }
            for (int j = i; j < Math.min(n, p + i + 1); j++) {
                double sum = 0;
                for (int k = Math.max(0, j - p); k < i; k++)
                    sum += (lower[j][k] * upper[k][i]);
                lower[j][i] = (mtx[j][i] - sum) / upper[i][i];
            }
        }
        for(int i = 0; i < n; i++){
            double sum = 0;
            for(int k = Math.max(0, i - p); k < i; k++)
                sum += lower[i][k] * Y[k];
            Y[i] = vec[i] - sum;
        }
        for(int k = n-1; k >= 0; k--){
            double sum = 0;
            for(int j = k; j < Math.min(n, p + k); j++)
                sum += upper[k][j] * res[j];
            res[k] = (Y[k] - sum) / upper[k][k];
        }
    }

}