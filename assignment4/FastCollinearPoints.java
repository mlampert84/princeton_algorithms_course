/******************************************************************************
 * 
 *  Name: Marcus
 *  Date: 17/03/2018
 *  
 *  Compilation:  javac FastCollinearPoints.java
 *  Execution:    java FastCollinearPoints
 *  Dependencies: Point.java, LineSegment.java
 *  
 *  Solution for Coursera, Algorithms Part I programming assignment.
 *  Looks for colinear points by 1) calculating the slopes all points make
 *  with a given point and then 2) sorting those slopes to look for duplicates.
 *  Exact specifications can be found at:
 *  http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *
 ******************************************************************************/


import java.util.Arrays;
import java.util.LinkedList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;


public class FastCollinearPoints {
    private final int n;
    private int segCount;
    private LinkedList<LineSegment> answers; 
    private double [] slopes;
    private Point [] sortP;
    
    
    private boolean pointsInOrder(Point[] collinearPts){
        boolean wellSorted = true;
        
//        StdOut.print("Testing points ");
//        for(Point p : collinearPts)
//            StdOut.print(p);
//        
//        StdOut.println();
        for(int i=0; i < collinearPts.length-1; i++){
//            PointTests.printComparison(collinearPts[i],collinearPts[i+1]);
            wellSorted = wellSorted && (collinearPts[i].compareTo(collinearPts[i+1])<0);
            
        }
        return wellSorted;
    }
    
    private void findFourOrMore(){
        
//collPs counts how many consequtive slopes after a given slope equal 
//that given slope. If collPs is >= 2, it means we have 4
//or more equal slopes in a row, mean we have found 4 or more collinear points       
        int collPs;
        
//Only need to move through the sorted array up to two before the end,
//because we are looking for at least two additional slope values that
//match the slope at our index.
        for(int i = 0; i < n-2;){
            collPs = 0;
            for(int k = 1; k < n-i;){
//                StdOut.println("Starting at index " + i + " and moving over " + k);
                if(slopes[i] == slopes[i+k]){
                    collPs++;
                    k++;   
                }
                else
                    break;            
            }
            
            if(collPs >=2){
                Point[] collinear = new Point[collPs + 2];
                collinear[0] = sortP[0];
                for(int h=0;h<=collPs;h++)
                    collinear[h+1] = sortP[i+h];
                
                if(pointsInOrder(collinear)){
                    LineSegment answer = new LineSegment(collinear[0],collinear[collinear.length-1]);
                    answers.add(answer);
                    segCount++;
                    
                }   //end if statement
                
//                  StdOut.println("Found " + (collPs +1) + 
//                               " collinear points from index " + i);
//                StdOut.print("Points: ");
//                for(Point p : collinear)
//                    StdOut.print(p);
//                
//                StdOut.println();
            }
            i += (1 + collPs);      
        }
    }
    
    
    public FastCollinearPoints(Point[] points){
        if(points == null)
            throw new IllegalArgumentException("constructor was passed a null value");
       
        n = points.length;
        
        for(int i = 0; i<n; i++)
            if(points[i] == null)
               throw new IllegalArgumentException("Array of points contains a null value");
        
        //Copy points into new array so that you can sort it.
        Point[] data = new Point[n];
        for(int i = 0; i<n; i++)
            data[i]=points[i];
        
        sortP = new Point[n]; 
        
        
        
        segCount = 0;
        answers = new LinkedList<LineSegment>();
        slopes = new double[n];
        
        //Sorting array puts the array in its natural order
        //guaranteeing that at least one of the permutations of
        //colinear points will be in correct order
        Arrays.sort(data);
        
        for(int i = 0; i<n-1; i++)
            if(data[i].compareTo(data[i+1]) == 0)
            throw new IllegalArgumentException("Array of points contains duplicate values.");
       
        
        for(int i = 0; i<n; i++){
            
            for(int g = 0; g<n; g++)
                sortP[g] = data[g];
            
            Arrays.sort(sortP, data[i].slopeOrder());
            
            for (int j = 0; j<n;j++)
                slopes[j] = sortP[0].slopeTo(sortP[j]);
            
            findFourOrMore();
            
//            for(double j : slopes)
//                StdOut.print(j + ", ");
//            StdOut.println();
//            for(Point k : sortP)
//                StdOut.print(k + ", ");
//            StdOut.println();
//            
        }
    }
    
    public int numberOfSegments(){
        return segCount;
    }
    
    public LineSegment[] segments(){
        
        return answers.toArray(new LineSegment[segCount]);
        
    }
    
    public static void main(String[] args){
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        
    }
}
