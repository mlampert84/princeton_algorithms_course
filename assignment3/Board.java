/***********************
 Name: Marcus
 Date: 19/03/2018
  
 Represents an 8-puzzle problem.
 Exact specifications can be found at:
 http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
******************************************************/

import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;
//import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
//import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;


public class Board {
    
    private int[] board;
    private final int size;
    
    public Board(int[][] blocks){
        
        size = blocks.length;
        board = new int [size * size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i*size + j] = blocks[i][j];
            }
        }
        
    }
    
    public int dimension(){ return size; }
    
    //If boolean hamming is false, the Manhattan distance is returned
    private int distance(boolean hamming){
        int distance = 0;
        
        for (int i = 0; i < size * size; i++) {
            if(board[i] != i+1 && board[i] !=0){
                if(hamming) 
                    distance ++;                                                 
                
                else{
                    
                    int expRow = (board[i] - 1) / size;
                    int expCol = (board[i] - 1) % size;
                    int actRow = i / size;
                    int actCol = i % size;
//                    int delta = Math.abs(i + 1 - board[i]);
                    int rowOff = Math.abs(expRow - actRow);
                    int colOff = Math.abs(expCol - actCol);
                    distance += (rowOff + colOff);
//                    StdOut.println(board[i] + " is in row " + actRow + " col " +
//                                   actCol + ".  It should be in row " + expRow +
//                                   " col " + expCol + ". Rows off are " + rowOff + 
//                                   " Cols off are " + colOff + ".Manhattan distance is "
//                                       + (rowOff + colOff));
                } //Manhattan condition
            } //close i for-loop
        } //Close function
        
        return distance;
    }
    
    public int hamming(){ return distance(true); }
    
    public int manhattan(){ return distance(false); }
    
    public boolean isGoal(){ return (hamming() == 0); }
    
    //Returns a array of non-zero neighbors. Row and Columns count from zero.
    private int[] getNeighbors(int row, int col){
      Queue<Integer> neighbors = new LinkedList<Integer> ();
      
      //left neighbor
      if(row > 0)
          neighbors.add((row - 1) * size + col );
      //right neighbor
      if(row < size-1)
          neighbors.add((row + 1) * size + col);
     //top neighbor
      if(col > 0)
          neighbors.add(row * size + col - 1);
      //bottom neighbor
      if(col < size-1)
          neighbors.add(row * size + col + 1);

      Queue<Integer> noZeros = new LinkedList<Integer>();
      
        for(Integer i : neighbors)
            if (board[i] != 0) noZeros.add(i);
      
      return noZeros.stream().mapToInt(i->i).toArray();
  }
    
    private Board swapOne(int a, int b){
      
        int[][] boardTwoDim = new int[size][size];
          for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                boardTwoDim[i][j] = board[i * size + j];
          }
       
        Board swappedBoard = new Board(boardTwoDim);
        swappedBoard.board[a] = board[b];
        swappedBoard.board[b] = board[a];
    
        return swappedBoard;
      
    }
    
    public Board twin(){     
       
        if(board[0] !=0){
          if(board[1] !=0)
              return swapOne(0,1);
          else
              return swapOne(0,size);
        }
       
        
           return swapOne(1,size+1);
    }
    
    public boolean equals(Object y){
        if(this == y) return true;
        if(y == null) return false;
        if(this.getClass() != y.getClass()) return false;
        
        final Board that = (Board) y;
        if(this.size != that.size) return false;
        
        boolean equalBoards = true;
        for (int i = 0; i < size * size; i++) {
            if( this.board[i] != that.board[i]){
                equalBoards = false;
                break;
            }
        }
        
        return equalBoards;
        
    }
    
    public Iterable<Board> neighbors(){
        int i = 0;
        do{
        if(board[i] == 0)
            break;
        i++;
        }while(i < size * size);

        int[] moves = getNeighbors(i / size, i % size);
        
        Queue<Board> neighbors = new LinkedList<Board> ();
        
        for ( int m : moves)
            neighbors.add(swapOne(i,m));
                          
        return neighbors;
  
    }
    
    public String toString(){
        
        StringBuilder s = new StringBuilder();
        s.append(size + "\n");
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                s.append(String.format("%2d ", board[i*size + j]));
            }
            s.append("\n");
        }
        return s.toString();
        
    }
    public static void main(String[] args){
        
//        read in the board      
//        Board initial = BoardTests.readInBoard(args);

        //print the board
//        StdOut.print(initial.toString());
        
        //Print the hamming number
//        StdOut.println("Hamming number: " + initial.hamming());
        
        //Print the hamming number
//        StdOut.println("Manhattan number: " + initial.manhattan());
        
//      //Read in a second board (assuming file has one!!) to test equality
//        Board secondBoard = BoardTests.readSecondBoard(args);
//        StdOut.print(secondBoard.toString());
//        StdOut.println("Boards equal = " + initial.equals(secondBoard));
//   
//       //Print when the goal is the goal
//        StdOut.println("This is the goal: " + initial.isGoal());
       
//        //Print the twin
//            StdOut.println(initial.twin().toString());
//        
//        Print the neighbors
//        StdOut.println("Neighbors: ");
//        for(Board i : initial.neighbors())
//            StdOut.println(i.toString());
    
    }
    
}



