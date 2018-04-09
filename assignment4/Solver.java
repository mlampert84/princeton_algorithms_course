/***********************
 Name: Marcus
 Date: 19/03/2018
  
 Solves an 8-puzzle problem.
 Exact specifications can be found at:
 http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
******************************************************/

import java.util.Stack;
import java.util.Queue;

import java.util.LinkedList;
import java.util.Arrays;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;


public class Solver {
    
    private MinPQ<SearchNode> pq;
    private MinPQ<SearchNode> pqTwin;
    private SearchNode winningNode;
    private boolean solvable;
    
    private class SearchNode implements Comparable<SearchNode>{
        private Board board;
        private int moves;
        private int manhattan;
        private SearchNode previous;
        
        public int compareTo(SearchNode that)
        { return (this.moves + this.manhattan
                      - that.moves - that.manhattan);}
    }
    
    
    private SearchNode initialize(Board initial){
        SearchNode start = new SearchNode();
        start.board = initial;
        start.moves = 0;
        start.manhattan = initial.manhattan();
        start.previous = null;
        
        return start;
    }
    
    private void addNextMoves(SearchNode node, boolean twin){
       
        for(Board b : node.board.neighbors()){
            if(node.previous == null || !b.equals(node.previous.board)){
                SearchNode neighbor = new SearchNode();
                neighbor.board = b;
                neighbor.moves = node.moves + 1;
                neighbor.manhattan = b.manhattan();
                neighbor.previous = node;
                if (twin)
                    pqTwin.insert(neighbor);
                else
                    pq.insert(neighbor);
            }
            
        }
    }
    
    public Solver(Board initial){
        
        if(initial == null)
            throw new java.lang.IllegalArgumentException();
        
        SearchNode start = initialize(initial);
        SearchNode startTwin = initialize(initial.twin());
        
        pq = new MinPQ<SearchNode>();
        pq.insert(start);
        
        pqTwin = new MinPQ<SearchNode>();
        pqTwin.insert(startTwin);
        
        SearchNode min;
        SearchNode minTwin;
        do{
            min = pq.delMin();
            minTwin = pqTwin.delMin();
            
            addNextMoves(min, false);
            addNextMoves(minTwin, true);
            
        }while(!min.board.isGoal() && !minTwin.board.isGoal());
        
        if(minTwin.board.isGoal())
            solvable = false;
        else
            solvable = true;
        
        winningNode = min;
        
        
    }
    
    public boolean isSolvable(){ return solvable; }
    
    public int moves(){
        if (!solvable)
            return -1;
        
        return winningNode.moves;   
    }
    
    public Iterable<Board> solution(){
       LinkedList<Board> winningMoves = new LinkedList<Board>();
       
       if(!solvable)
           return null;
       
       SearchNode i = winningNode;
       do{
          winningMoves.push(i.board);
          i = i.previous;
       }while(i !=null);
       
       return winningMoves;
       
    }
    
    public static void main(String[] args){
//        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
//         print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
        }
    }
    
    
}