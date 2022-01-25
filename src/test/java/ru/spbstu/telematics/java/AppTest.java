package ru.spbstu.telematics.java;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    private void printTest(int i, double[] res_exp, double[] res_cons, double[] res_par){
        System.out.println("Test " + i);
        System.out.println("Results expected, consequent and parallel: ");
        for(int j = 0; j < res_exp.length; j++){
            System.out.println(String.format("%.3f", res_exp[j]) + "\t" + String.format("%.3f", res_cons[j]) + "\t" + String.format("%.3f", res_par[j]));
        }
        Assert.assertArrayEquals(res_exp, res_cons, 0.001);
        Assert.assertArrayEquals(res_exp, res_par, 0.001);
    }

    public void test1() {
        double[][] mtx = { { 29, 8, 0, 0, 0},
                { 16, 28, 22, 0, 0},
                { 0, 23, 2, 27, 0},
                { 0, 0, 2, 7, 9},
                {0, 0, 0, 24, 14}};
        double[] vec = {30, 29, 2, 6, 17};
        double[] res = {1.4, -1.326, 1.987, 1.056, -0.597};
        double[] res_cons = new double[5];
        Consequent.solve(mtx, vec, 5, 3, res_cons);
        double[] res_par = new double[5];
        Consequent.solve(mtx, vec, 5, 3, res_par);
        printTest(1, res, res_cons, res_par);
    }

    public void test2() {
        double[][] mtx = { { 29, 8, 0, 0, 0, 0, 0},
                { 21, 16, 13, 0, 0, 0, 0},
                {0, 16, 24, 29, 0, 0, 0},
                { 0, 0, 1, 24, 10, 0, 0},
                {0, 0, 0, 10, 18, 9, 0},
                {0, 0, 0, 0, 22, 20, 13},
                {0, 0, 0, 0, 0, 17, 14}};
        double[] vec = {5, 3, 21, 29, 11, 9, 28};
        double[] res = {-3.359, 12.803, -10.100, 2.019, -0.936, 0.850, 0.968};
        double[] res_cons = new double[7];
        Consequent.solve(mtx, vec, 7, 4, res_cons);
        double[] res_par = new double[7];
        Consequent.solve(mtx, vec, 7, 4, res_par);
        printTest(2, res, res_cons, res_par);
    }
    public void test3() {
        double[][] mtx = { {3,	21},
                            {13,12}	};
        double[] vec = {12, 15};
        double[] res = {4.000, 0.468};
        double[] res_cons = new double[2];
        Consequent.solve(mtx, vec, 2, 1, res_cons);
        double[] res_par = new double[2];
        Consequent.solve(mtx, vec, 2, 1, res_par);
        printTest(3, res, res_cons, res_par);
    }
}
