/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONF95 = 1.96;
    private final int trials;
    private double mean, stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        this.trials = trials;
        double[] fractions = new double[trials];
        for (int i = 0; i < trials; i++) {
            int sites = 0;
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int r = StdRandom.uniform(1, n + 1);
                int c = StdRandom.uniform(1, n + 1);
                if (!perc.isOpen(r, c)) {
                    perc.open(r, c);
                    sites++;
                }
            }

            double fraction = (double) sites / (n * n);
            fractions[i] = fraction;
        }

        mean = StdStats.mean(fractions);
        stddev = StdStats.stddev(fractions);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - (CONF95 * stddev) / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + (CONF95 * stddev) / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]), t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);
        double mean = ps.mean(), stddev = ps.stddev();
        double confidenceLo = ps.confidenceLo(), confidenceHi = ps.confidenceHi();
        String s1 = "mean                    = ";
        String s2 = "stddev                  = ";
        String s3 = "95% confidence interval = ";
        System.out.println(s1 + mean);
        System.out.println(s2 + stddev);
        System.out.println(s3 + "[" + confidenceLo + ", " + confidenceHi + "]");
    }
}
