package org.lioxa.ciel.test;

import java.util.Random;

/**
 *
 * @author xi
 * @since May 13, 2016
 */
public class TestJNI {

    static {
        System.loadLibrary("Test");
    }

    public native double dot(double[] x0, double[] x1);

    public double dot1(double[] x0, double[] x1) {
        if (x0.length != x1.length) {
            return -1;
        }
        double r = 0;
        // for (int i = 0; i < x0.length; i++) {
        // r += x0[i] * x1[i];
        // }
        return r;
    }

    public static final int N = 1000000;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Random rnd = new Random();
        TestJNI instance = new TestJNI();
        double[] arr = new double[100000];
        long t;
        t = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 10; j++) {
                int index = rnd.nextInt() % 100000;
                arr[index < 0 ? -index : index] = i;
            }
            instance.dot1(arr, arr);
        }
        t = System.currentTimeMillis() - t;
        System.out.println(t);
        t = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 10; j++) {
                int index = rnd.nextInt() % 100000;
                arr[index < 0 ? -index : index] = i;
            }
            instance.dot(arr, arr);
        }
        t = System.currentTimeMillis() - t;
        System.out.println(t);
    }

}
