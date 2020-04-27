import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double meanVal;
    private final double stddevVal;
    private final double confidenceLoVal;
    private final double confidenceHiVal;
    private static final double CONFIDENCE_95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        double n2 = n * n;
        double[] x = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                percolation.open(row, col);
            }
            x[i] = percolation.numberOfOpenSites() / n2;
        }
        meanVal = StdStats.mean(x);
        stddevVal = StdStats.stddev(x);
        confidenceLoVal = meanVal - (CONFIDENCE_95 * Math.sqrt(stddevVal) / trials);
        confidenceHiVal = meanVal + (CONFIDENCE_95 * Math.sqrt(stddevVal) / trials);
    }

    // sample mean of percolation threshold
    public double mean() {
        return meanVal;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddevVal;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLoVal;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHiVal;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = 20;  // number of particles (default 20)
        int t = 100;

        if (args.length == 1) {
            n = Integer.parseInt(args[0]);
        }
        else if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);
        }
        if (n < 1 || t < 1) {
            throw new IllegalArgumentException("invalid argument size");
        }

        PercolationStats percolationStats = new PercolationStats(n, t);

        System.out.print("mean: " + percolationStats.mean() + "\n");
        System.out.print("stddev: " + percolationStats.stddev() + "\n");
        System.out.print("95% confidence interval: [" + percolationStats.confidenceLo() + ", "
                                 + percolationStats.confidenceHi() + "]\n");
    }

}