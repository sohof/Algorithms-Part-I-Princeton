/**
 * Programm.Assignment course algorithms part one.
 * The class receives a grid size N and nr of experiments from command line
 * and does Monte Carlo simulation To estimate the percolation threshold.
 * Depends on class Percolation and stdlib.jar from algorithms part 1
 * website.
 *@author Sohof Dastmard
 *@version 1.0
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    /**
     * Constant used for calculating 95 % confidence intervals.
     */
    private static final double CONFIDCONST = 1.96;
    /**
     * array to hold percolation threshold values.
     */
    private double[] pvalues;

    /**
     *  number of experiments.
     */
    private int t;

    /**
     * Initialzes number of experiment t and the pvalues array to hold the percolation.
     * threshold data values. Calls performOneExp t times.
     * perform t independent experiments on an n-by-n grid
     *@param n size of grid integer
     *@param t number of experiments to perform
     * @throws IllegalArgumentException if n or
     */
    public PercolationStats(final int n, final int t) {   

	if (n <= 0 || t <= 0) {
	    throw new IllegalArgumentException(Integer.toString(n) + " " + Integer.toString(t));
	}
	pvalues = new double[t];
	this.t = t;

	for (int i = 0; i < t; i++){

	    double pval = performOneExp(n);
	    pvalues[i] = pval;
	}
    }

    /**
     * Performs one experiment by opening one site at a time till grid reaches percolation.
     *@return percolation threshold which is nr opened sited / size of grid
     *@param n size of grid integer
     */
    private double performOneExp(final int n) {

	Percolation percolation = new Percolation(n);
	int nrOpenedSites = 0;
	double threshold  = 0;
	boolean percolates = false;
	while (!percolates) {

	    int i = StdRandom.uniform(n) + 1;
	    int j = StdRandom.uniform(n) + 1;
	    percolation.open(i, j);
	    percolates = percolation.percolates();
	}
	for (int i = 1; i <= n; i++) {
	    for (int j = 1; j <= n; j++) {
		if (percolation.isOpen(i, j)) {
		    nrOpenedSites++;
		}
		    }
	}
	threshold = (nrOpenedSites * 1.0) / (n * n);
	// multiply with 1.0 to auto.convert expression to double value.

	return threshold;
    }

    /**
     * sample mean of percolation threshold
     * @return mean of all experiments
     */
    public double mean() {
	return StdStats.mean(pvalues);
    }


    /**
     * sample standard deviation of percolation threshold
     * @return standard deviation
     */
    public double stddev() {
      	return StdStats.stddev(pvalues);
    }
    /**
     * low  endpoint of 95% confidence interval
     * @return endpoint of 95% confidence interval
     */
    public double confidenceLo() {
	return (mean() - ((CONFIDCONST * stddev()) / Math.sqrt(t)));
    }
    /**
     *  high endpoint of 95% confidence interval
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
	return (mean() + ((CONFIDCONST * stddev()) / Math.sqrt(t)));
    }

    
    public static void main(String[] args) {    // test client (described below)
    

	int gridSize = Integer.parseInt(args[0]);
        int nrOfExperiments = Integer.parseInt(args[1]);

	PercolationStats pstat = new PercolationStats(gridSize, nrOfExperiments);
	System.out.println("mean   \t \t \t \t = " + pstat.mean());
       	System.out.println("stddev \t \t \t \t = " + pstat.stddev());
       	System.out.println("95% confidence interval = " + pstat.confidenceLo() + ", " + pstat.confidenceHi());

    }
}
