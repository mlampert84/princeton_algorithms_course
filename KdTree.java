/***********************
 Name: Marcus
 Date: 29/03/2018
  
 Uses a 2d-tree to represent a set of points in the unit square and
 support efficient range search (find all of the points 
 contained in a query rectangle) and nearest-neighbor search (find a closest 
 point to a query point).
 Exact specifications can be found at:
 http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
******************************************************/


import java.util.LinkedList;
import java.util.Arrays;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private int size;
    private Node root;
   
    private static class PandD{
        private Point2D p;
        private Double d;
        
        public PandD(Point2D p, Double d){
          this.p = p;
          this.d = d;
        }
    }
    private static class Node {
      private Point2D p;
      private RectHV rect;
      private Node lb;
      private Node rt;
            
      public Node(Point2D p, Node parent, boolean isVertical, boolean isLB){
       this.p = p;
       
       if(parent == null)
          this.rect = new RectHV(0.0,0.0,1.0,1.0);
       else if(isVertical){
           RectHV r = parent.rect;  
           if(isLB)
               this.rect = new RectHV(r.xmin(),r.ymin(),r.xmax(),parent.p.y());
           else
               this.rect = new RectHV(r.xmin(),parent.p.y(),r.xmax(),r.ymax());
       }
       else{
          RectHV r = parent.rect;  
          if(isLB)
              this.rect = new RectHV(r.xmin(),r.ymin(),parent.p.x(),r.ymax());
          else
              this.rect = new RectHV(parent.p.x(),r.ymin(),r.xmax(),r.ymax());
       }
          
      
      }
    }
    
    public KdTree(){
      size = 0;
      root = null; 
    }
    
    public boolean isEmpty(){ return root == null; }
    
    public int size(){ return size; }
   
    public void insert(Point2D p){
       if(p == null)
         throw new java.lang.IllegalArgumentException("Argument cannot be null");
 
        
        root = insert(root, null, p, true, true);
    }
    
    private Node insert(Node n, Node parent, Point2D p, boolean isVertical, boolean isLB){
        if( n == null){
            size++;
            return new Node(p, parent, isVertical, isLB);}

        if(p.equals(n.p))
            return n;
        
       boolean isLeftOrBelow;
       
       if(isVertical)
           isLeftOrBelow = p.x() < n.p.x();
       else
           isLeftOrBelow = p.y() < n.p.y();
       
       if(isLeftOrBelow)
           n.lb = insert(n.lb, n, p, !isVertical, true);
       else
           n.rt = insert(n.rt, n, p, !isVertical, false);
       
       return n;
    }
    
    public boolean contains(Point2D p){
        if(p == null)
            throw new java.lang.IllegalArgumentException("Argument cannot be null");
 
      return contains(root, p, true);
    }

    private boolean contains(Node n, Point2D p, boolean isVertical){
        
        if(n == null) return false;
        if(p.equals(n.p)) return true;
        
        boolean isLeftOrBelow;
  
        if( isVertical ) isLeftOrBelow = p.x() < n.p.x();
        else             isLeftOrBelow = p.y() < n.p.y();
        
        if(isLeftOrBelow)
            return contains(n.lb, p, !isVertical);
        return contains(n.rt, p, !isVertical);
           
    }
    
    public void draw(){  
        draw(root, true);
    }
    
    private void draw(Node n, boolean isVertical){
         if( n == null ) return;
   
         RectHV rect = n.rect;
         
         StdDraw.setPenColor(StdDraw.BLACK);
         StdDraw.setPenRadius(0.01);
         n.p.draw();
         
         StdDraw.setPenRadius(0.001);
         if(isVertical){
             Double x = n.p.x(); 
             StdDraw.setPenColor(StdDraw.RED);
             StdDraw.line(x, rect.ymin(), x, rect.ymax()); 
         }
         else{
             Double y = n.p.y(); 
             StdDraw.setPenColor(StdDraw.BLUE);
             StdDraw.line(rect.xmin(), y, rect.xmax(), y);
         }
//         StdOut.println("The Rectangle: " + rect.toString());
         draw(n.lb, !isVertical);
         draw(n.rt, !isVertical);
    }

    public Iterable<Point2D> range(RectHV rect){
       if(rect == null)
         throw new java.lang.IllegalArgumentException("Argument cannot be null");
 
        LinkedList<Point2D> rangeHits = new LinkedList<Point2D>();    
        
        range(root, rect, rangeHits);
        
        return  rangeHits;
            
    }
    
    private void range(Node n, RectHV rect,LinkedList<Point2D> rangeHits){
        if(n == null) return;

        if(!rect.intersects(n.rect)) return;
                  
        if(rect.contains(n.p))
            rangeHits.add(n.p);
        
        range(n.lb, rect, rangeHits);
        range(n.rt, rect, rangeHits);
     
    }
        
    public Point2D nearest(Point2D p){
        if(p == null)
            throw new java.lang.IllegalArgumentException("Argument cannot be null");
       
        if(root == null) return null;
              
      PandD nearest = new PandD(root.p, p.distanceSquaredTo(root.p));
      nearest = nearest(root, p, nearest, true);
      return nearest.p;
    }
    
    private PandD nearest(Node n, Point2D p, PandD nearest, boolean isVertical){
        if(n == null) return nearest;
        
        if(nearest.d < Math.pow(n.rect.distanceTo(p),2)) return nearest;
            
//        StdOut.println("Testing distance in node " + n.p.toString()); 
        double newD = p.distanceSquaredTo(n.p);
       
        if( newD < nearest.d ){
            nearest.d = newD;
            nearest.p = n.p;
        }
        
        boolean goLeftBottomFirst;
        if(isVertical) goLeftBottomFirst = ( p.x() < n.p.x() );
        else goLeftBottomFirst = (p.y() < n.p.y() );    
        
        if(goLeftBottomFirst){
            nearest = nearest(n.lb, p, nearest, !isVertical);
            nearest = nearest(n.rt, p, nearest, !isVertical);
            
        }
        
        else {
            nearest = nearest(n.rt, p, nearest, !isVertical);
            nearest = nearest(n.lb, p, nearest, !isVertical);
        }
        
        return nearest;
    }

//For debegging purposes - breaks the Princeton API.
    public void stringify(){
     LinkedList<Point2D> list = new LinkedList<Point2D>();
     addNodeToList(root, list);
     
     StdOut.println(Arrays.toString(list.toArray()));            
     
    }
    
    private void addNodeToList(Node x, LinkedList<Point2D> list){
      if( x == null ) return;
      addNodeToList(x.lb, list);
      addNodeToList(x.rt, list);
      list.add(x.p);
    }
    
    public static void main(String[] args){
    
    }

}