package ru.spbstu.telematics.java;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.CountDownLatch;

public class Parallel {
    static double sum = 0;

    public static void solve(double [][]mtx, double []vec, int n, int p, double []res) throws Exception{
        double []tmp = new double[n];
        double [][]lower = new double[n][n], upper = new double[n][n];
        CountDownLatch cd = new CountDownLatch(n);
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
        for (int i = 0; i < n; i++){
            Thread t = new Thread(new Solver(i, p, n, lower, upper, mtx, cd, lock));
            t.start();
        }
        cd.await();

        for(int i = 0; i < n; i++){
            sum = 0;
            int max = Math.max(0, i - p);
            for(int j = max; j < i; j++)
                sum += lower[i][j] * tmp[j];
            tmp[i] = vec[i] - sum;
        }

        for(int i = n-1; i >= 0; i--){
            sum = 0;
            int min = Math.min(n, p + i);
            for(int j = i; j < min; j++)
                sum += upper[i][j] * res[j];
            res[i] = (tmp[i] - sum) / upper[i][i];
        }
    }

    private static class Solver implements Runnable {
        private final int p, n;
        private int i = 0;
        private final double[][] lower, upper, mtx;
        private final CountDownLatch cd;
        private final ReentrantReadWriteLock lock;

        Solver(int i, int p, int n, double[][] l, double[][] u, double[][] mtx, CountDownLatch cd, ReentrantReadWriteLock lock){
            this.i += i;
            this.p = p;
            this.n = n;
            this.lower = l;
            this.upper = u;
            this.mtx = mtx;
            this.cd = cd;
            this.lock = lock;
        }

        public void run() {
            for (int j = i; j < Math.min(n, p + i + 1); j++) {
                double s = 0;
                for (int k = Math.max(0, j - p); k < i; k++) {
                    s += (lower[i][k] * upper[k][j]);
                }
                if(lock.writeLock().tryLock()) {
                    upper[i][j] = mtx[i][j] - s;
                    lower[j][i] = (mtx[j][i] - s) / upper[i][i];
                    lock.writeLock().unlock();
                }
            }
            cd.countDown();
        }
    }
}
