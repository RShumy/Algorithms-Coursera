import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
   private double[] trials;
   private static final double CONFIDENCE_95 = 1.92;

    public PercolationStats(int n, int trials) {
        this.trials = new double[trials];
        for (int trial=0; trial<trials; trial++){
            Percolation percolationTrial = new Percolation(n);
            while (!percolationTrial.percolates()) {
                int row = StdRandom.uniformInt(1,n+1);
                int col = StdRandom.uniformInt(1,n+1);
                if(!percolationTrial.isOpen(row, col))
                    percolationTrial.open(row,col);
            }
            //trial fraction
            this.trials[trial]= percolationTrial.numberOfOpenSites() / Math.pow(n,2);
        }
    }

    public double mean() {
        return StdStats.mean(trials);
    }

    public double stddev() {
        return StdStats.stddev(trials);
    }

    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(trials.length));
    }

    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(trials.length));
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = " + confidence);
    }

}
