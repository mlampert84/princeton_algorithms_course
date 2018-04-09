/***********************
  Name: Marcus
  Date: 05/03/2018
  
  Implements a double-ended queue or deque.
  Exact specifications can be found at:
  http://coursera.cs.princeton.edu/algs4/assignments/queues.html
  ******************************************************/

import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item>{
    
    
    
    private Node first, last;
    private int size;
    
    private class Node{
        private Item item;
        private Node previous;
        private Node next;
    }
    
    public Deque(){
        first= null;
        last = null;
        size = 0;
    }
    
    public boolean isEmpty(){
//    StdOut.println("first: " + first + ", last: " + last);
        return (first == null || last==null);
    }
    
    public int size(){
        return size;
    }
    
//    public Node returnLast() {return last; }
    
    public void addFirst(Item item){
        if(item==null)
            throw new IllegalArgumentException();
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.previous = null;
//      StdOut.println("Test isEmpty on addFirst: " + isEmpty());
        if(isEmpty()) {
            
            last = first;
//          StdOut.println("Initialized list: " + last.item);
        }
        else    oldfirst.previous = first;
        
        size++;
    }
    
    public void addLast(Item item){
        if(item==null)
            throw new IllegalArgumentException(); 
        Node oldlast = last;
//      StdOut.println("Old last item: " + oldlast.item);
        
        last = new Node();
        last.item = item;
        last.next = null;
        last.previous = oldlast;
        if(isEmpty()) first=last;
        else  oldlast.next = last;  
        size++;
    }
    
    public Item removeFirst(){
        if(isEmpty())
            throw new java.util.NoSuchElementException();
        Item item = first.item;
        first = first.next;
        if(isEmpty())
            last=first;
        else
            first.previous = null;
        
        size--;
        return item;
    };
    
    public Item removeLast(){
        if(isEmpty())
            throw new java.util.NoSuchElementException();
        
        Item item = last.item;
        last = last.previous;
        if(isEmpty())
            first=last;
        else
            last.next = null;
        
        size--;
        return item;
    }
    
    public Iterator<Item> iterator(){return new DequeIterator();}
    
    private class DequeIterator implements Iterator<Item>{
        private Node current = first;
        public boolean hasNext() {return current!=null; }
        public void remove(){throw new UnsupportedOperationException();}
        public Item next() {
            if(isEmpty())
                throw new java.util.NoSuchElementException();
            
            Item item = current.item;
            current=current.next;
            return item;
        }
    }
    
    
    public static void main(String[] args){
        
        
//        StdOut.println("Hello World!");
        Deque<Integer> conseq = new Deque<Integer>();
        conseq.addFirst(2); 
        conseq.addFirst(1); 
        conseq.addLast(3); 
        conseq.addLast(4);
        conseq.addFirst(0);
        conseq.removeFirst();
        conseq.removeLast();
        conseq.addFirst(7);
        conseq.addLast(5);
        conseq.removeFirst();
        conseq.removeLast();
        conseq.removeLast();
        conseq.removeLast();
        conseq.removeLast();
        conseq.removeFirst();
        
        
        for(int n:conseq)
            StdOut.print(n);
        StdOut.print("\n");
    }
    
}