/*****************************************************
  Name: Marcus
  Date: 25/2/2018
  
  This program takes as input 
  -the size of a grid
  -which elements in the grid to open
  -connects open elements
  -determines if a grid is filled from the top on down
  -determines if water/current/etc. would percolate through top to bottom
  
  Exact specifications can be found at:
  http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
  
  ******************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;     //size of the grid
 
    private boolean[][] openSite; //returns whether [row][column] is open
    private int openSiteCount;    
    private boolean[] connectedToBottom;
    
    private WeightedQuickUnionUF connectedSites;
    
    public Percolation(int n)
    {
        if(n <= 0)
          throw new java.lang.IllegalArgumentException("size must be gerater than 0");  
        size = n;    
        openSite = new boolean[size+1][size+1];     
        connectedSites = new WeightedQuickUnionUF((size*size) + 1);
        connectedToBottom = new boolean[(size*size) +1];
    }
    
    
//  converts from (row,col) to 1-dim array, where (1,1) -> 1 and rows
//  are lined up one after the other  
    private int xyTo1D(int x, int y)
    {      
        return ((x-1) * size + y);
    }
// returns [row,col] from 1-dim array index    
    private int[] toXY(int ind)
    {
        int[] twoD = new int[2];
        twoD[0] = ((ind-1) /size) + 1;
        twoD[1] = ((ind - 1) % size) + 1;
        return twoD;   
    }
    
    private void validIndices(int row, int col)
    {
        if (row < 1 || row > size)
            throw new IllegalArgumentException("row index "+row+" out of bounds");
        if (col < 1 || col > size)
            throw new IllegalArgumentException("col index "+col+" out of bounds");
    }
    
    private boolean isOpenNeighbor(int row, int col)
    {
        boolean validLoc = !(row < 1 || row > size || col < 1 || col > size);
        return (validLoc && openSite[row][col]); 
    }
    
    
    public void open(int row, int col)
    {
        validIndices(row, col);
        
        if (openSite[row][col])
            return;
        
        openSite[row][col] = true;
        openSiteCount++;
        int[] neighbors = new int[4];
        boolean connectToBottom = false;
        
        //Note:if a neighbor doesn't exist, neighbor[ind] has default value of 0
        
        for (int i = 0; i < 2; i++) {
            if (isOpenNeighbor(row + (2*i)-1, col))
                neighbors[i] = xyTo1D(row+(2*i)-1, col);     
            
            if (isOpenNeighbor(row, col+(2*i)-1))
                neighbors[i+2] = xyTo1D(row, col+(2*i)-1);             
        }
        
        for (int i:neighbors) {
            if (i != 0) {
                if (connectedToBottom[connectedSites.find(i)])
                    connectToBottom = true;
            }
        }
    
        if (row == 1)
            connectedSites.union(xyTo1D(row, col), 0);
        
        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i] != 0) {
                int[] twoD = toXY(neighbors[i]); 
                if (isOpen(twoD[0], twoD[1]))
                    connectedSites.union(xyTo1D(row, col), neighbors[i]);
            }
        }
        if (row == size || connectToBottom)
            connectedToBottom[connectedSites.find(xyTo1D(row, col))] = true;                   
    }
    
    public boolean isOpen(int row, int col)
    {
        validIndices(row, col); 
        return openSite[row][col];
    }
    
    public boolean isFull(int row, int col)
    {
        validIndices(row, col);    
        return connectedSites.connected(0, xyTo1D(row, col));
        
    }
    
    public int numberOfOpenSites()
    {
        return openSiteCount;
    }
    
    public boolean percolates()
    {
        return connectedToBottom[connectedSites.find(0)];
    }         
    
 //For degugging purposes   
    public static void main(String[] args)
    {
 
//        Percolation perc = new Percolation(2);
//       perc.open(4,2);
//        perc.open(3,2);
//       perc.open(1,2);
//      System.out.println(perc.isFull(4,2));
//      System.out.println(perc.isFull(4,4));        
//       try{
//       perc.isOpen(2,4);
//       }catch(IndexOutOfBoundsException e){
//        System.out.println("Caught the error!");   
//       }
//       System.out.println(Arrays.deepToString(perc.returnNeighbors(2,1)));
//    System.out.println(perc.connectedSites.connected(perc.xyTo1D(1,2),5));   
//    System.out.println(    perc.percolates());
//    System.out.println(    perc.numberOfOpenSites());
        
    }
}