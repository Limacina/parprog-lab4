package ru.spbstu.telematics.java;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.CountDownLatch;

public class Parallel {
    static double sum = 0;

    public static void DoThread(double [][]mtx1, double [][]mtx2, int i, int j, int p, int max) throws Exception{
        int min = Math.max(0, j - p);
        int tmp = (max - min) / 2;
        if(min != max){
            CountDownLatch countDown = new CountDownLatch(2);
            ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
            Thread firstThread = new Thread(new Multiplication(i, j, min, min + tmp, mtx1, mtx2, countDown, lock));
            firstThread.start();
            Thread secondThread = new Thread(new Multiplication(i, j, min + tmp, max, mtx1, mtx2, countDown, lock));
            secondThread.start();
            countDown.await();
        }
    }

    public static void solve(double [][]mtx, double []vec, int n, int p, double []res) throws Exception{
        double []Y = new double[n];
        double [][]lower = new double[n][n], upper = new double[n][n];
        for (int i = 0; i < n; i++){
            int min = Math.min(n, p + i + 1);
            for (int j = i; j < min; j++) {
                sum = 0;
                int max = Math.max(0, j - p);
                if(max != i)
                    DoThread(lower, upper, i, j, p, i);
                upper[i][j] = mtx[i][j] - sum;
            }
            for (int j = i; j < min; j++) {
                sum = 0;
                int max = Math.max(0, j - p);
                if(max != i)
                    DoThread(lower, upper, j, i, p, i);
                lower[j][i] = (mtx[j][i] - sum) / upper[i][i];
            }
        }
        for(int i = 0; i < n; i++){
            sum = 0;
            int max = Math.max(0, i - p);
            for(int k = max; k < i; k++)
                sum += lower[i][k] * Y[k];
            Y[i] = vec[i] - sum;
        }

        for(int k = n-1; k >= 0; k--){
            sum = 0;
            int min = Math.min(n, p + k);
            for(int j = k; j < min; j++)
                sum += upper[k][j] * res[j];
            res[k] = (Y[k] - sum) / upper[k][k];
        }
    }

    private static class Multiplication implements Runnable {
        private final int i, j, min, max;
        private final double[][] m1, m2;
        CountDownLatch countDown;
        ReentrantReadWriteLock locker;

        Multiplication(int i, int j, int min, int max, double[][] m1, double[][] m2,
                       CountDownLatch countDown, ReentrantReadWriteLock lock) {
            this.i = i;
            this.j = j;
            this.min = min;
            this.max = max;
            this.m1 = m1;
            this.m2 = m2;
            this.countDown = countDown;
            this.locker = lock;
        }

        public void run() {
            //System.out.println(Thread.currentThread().getName());
            double tmp = 0;
            for (int k = min; k < max; k++)
                tmp += m1[i][k] * m2[k][j];
            locker.writeLock().lock();
            try{
                sum += tmp;
            }
            finally{
                locker.writeLock().unlock();
            }
            countDown.countDown();
        }
    }
}