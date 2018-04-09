/***********************
  Name: Marcus Lampert
  Date: 25/2/2018
  
  This program runs a Monte Carlo simulation to 
  to caculate when in a given nxn square, percolation has occured.
  Exact specifications can be found at:
  http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
  
  ******************************************************/


import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats
{
    
    private double[] trialResults;
    private final int size;
    private final int trialQuant;
    private boolean stdDevInit;
    private double stdDev;
    private boolean meanInit;
    private double mean;
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Values must be greater than 0");
        
        
        trialResults = new double[trials];
        size = n;
        trialQuant = trials; 
        
        for (int i = 0; i < trials; i++) {
            Percolation trialPerc = new Percolation(size);
            while (!trialPerc.percolates()) {
                openRandomSite(trialPerc);
            }
            
            double fracOpen = 
                ((double) trialPerc.numberOfOpenSites())/(size * size);
            trialResults[i] = fracOpen;
        }  
    }
    
    private void openRandomSite(Percolation trialPerc)
    {
        int row = StdRandom.uniform(1, size+1);
        int col = StdRandom.uniform(1, size+1);
        trialPerc.open(row, col);
    }
    
    // sample mean of percolation threshold 
    public double mean()
    {   if(!meanInit)
        {
          mean = StdStats.mean(trialResults);
          meanInit = true;
    }
        return mean;
    }

    public double stddev()
    {   if(!stdDevInit){
          stdDev = StdStats.stddev(trialResults);
    stdDevInit = true;
    }
        return stdDev;
    }
    
    private double confidence(boolean high){
      double confidenceValue = 1.96;
   
      if(!high) confidenceValue = -confidenceValue;
      
      return (mean() + confidenceValue * stddev() / Math.sqrt(trialQuant));
          
    }
    
    // 95% confidence interval  
    public double confidenceLo()
    {   
        return confidence(false);
    }
    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return confidence(true);     
    }
    
    public static void main(String[] args)
    {
        int gridSize = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        
        PercolationStats trial = new PercolationStats(gridSize, trials);
        StdOut.println("mean:\t\t\t\t= " + trial.mean());
        StdOut.println("stddev:\t\t\t\t= " + 
                       StdStats.stddev(trial.trialResults));
        StdOut.println("95% confidence interval\t= [" +
                       trial.confidenceLo() + ", " +
                       trial.confidenceHi() + "]");
        
    }
}