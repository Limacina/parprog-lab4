package ru.spbstu.telematics.java;

public class Consequent {
    static int MAX = 100;
    public static void solve(double [][]mtx, double []vec, int n, int p, double []res){
        double []tmp = new double[n];
        double [][]lower = new double[n][n], upper = new double[n][n];
        for (int i = 0; i < n; i++){
            for (int j = i; j < Math.min(n, p + i + 1); j++) {
                double sum = 0;
                for (int k = Math.max(0, j - p); k < i; k++){
                    sum += (lower[i][k] * upper[k][j]);
                }
                upper[i][j] = mtx[i][j] - sum;
                lower[j][i] = (mtx[j][i] - sum) / upper[i][i];
            }
        }
        for(int i = 0; i < n; i++){
            double sum = 0;
            int max = Math.max(0, i - p);
            for(int j = max; j < i; j++)
                sum += lower[i][j] * tmp[j];
            tmp[i] = vec[i] - sum;
        }

        for(int i = n-1; i >= 0; i--){
            double sum = 0;
            int min = Math.min(n, p + i);
            for(int j = i; j < min; j++)
                sum += upper[i][j] * res[j];
            res[i] = (tmp[i] - sum) / upper[i][i];
        }
    }

}