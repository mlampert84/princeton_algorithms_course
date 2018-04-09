/******************************************************************************
 * 
 *  Name: Marcus
 *  Date: 17/03/2018
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    java BruteCollinearPoints
 *  Dependencies: Point.java, LineSegment.java
 *  
 *  Solution for Coursera, Algorithms Part I programming assignment.
 *  Exact specifications can be found at:
 *  http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *
 ******************************************************************************/

import java.util.Arrays;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    
    private int segCount;
    private int segmentArraySize;
    private LineSegment[] lineSegments;
    
    private boolean orderedPoints(Point a, Point b, Point c, Point d){
        return ( (a.compareTo(b) < 0 ) && 
                (b.compareTo(c) < 0 ) &&
                (c.compareTo(d) < 0 ));
    }
    
    private boolean sameSlopes(Point a, Point b, Point c, Point d){
        
        return( (a.slopeTo(b) == a.slopeTo(c)) && (a.slopeTo(b) == a.slopeTo(d)));
        
    }
    
    public BruteCollinearPoints(Point[] points){
        if(points == null)
            throw new IllegalArgumentException("constructor was passed a null value");
        
        
        int n = points.length;
        
        
        for(int i = 0; i<n; i++)
            if(points[i] == null)
            throw new IllegalArgumentException("Array of points contains a null value");
      
        //The only reason I'm sorting here is to search for duplicates
        //so I can throw an exception if any is found.
        Point[] data = new Point[n];
        for(int i = 0 ; i < n ; i++)
            data[i] = points[i];
    
        Arrays.sort(data);
        
        for(int i = 0; i<n-1; i++){
            
            if(data[i].compareTo(data[i+1]) == 0)
            throw new IllegalArgumentException("Array of points contains duplicate values.");
        }
       segCount = 0;
        
        if(n>=4){
            
           int numberOfCombinations;
           if ( n == 4){
             numberOfCombinations = 1;
           }
           else
            numberOfCombinations = ((n-1)*(n-2)*(n-3)*(n-4))/24;
        lineSegments = new LineSegment[numberOfCombinations];
        
        for(int i = 0; i < n-3; i++){
            for(int j = i+1; j < n-2; j++){
                for(int k = j+1; k < n-1; k++){
                    for(int l = k+1; l < n; l++){
                        if (orderedPoints(data[i],data[j],data[k],data[l])){
                            if(sameSlopes(data[i],data[j],data[k],data[l])){
                                
                                lineSegments[segCount] = new LineSegment(data[i],data[l]);      
                                
                                segCount++;
                            }
                        }
                    }
                }
            }
        }
    }
    }
    public int numberOfSegments(){
        return segCount;
        
    }
    
    public LineSegment[] segments(){
        
        LineSegment[] answer = new LineSegment[segCount];
        for(int i=0;i<segCount;i++)
            answer[i] = lineSegments[i];
        return answer;
    }
}