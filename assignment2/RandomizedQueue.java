/***********************
  Name: Marcus
  Date: 04/03/2018
  
  Implements a RandomizedQueue, where elements are dequeed at random.	
  Exact specifications can be found at:
  http://coursera.cs.princeton.edu/algs4/assignments/queues.html
  ******************************************************/


import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;


public class RandomizedQueue<Item> implements Iterable<Item>{
    
//    public int countRandomCalls;
    private Item[] a;
    
    private int n, nWithNulls;
    
    public RandomizedQueue(){
        a = (Item[]) new Object[2];
        n = 0;
        nWithNulls = 0;
//        countRandomCalls = 0;
    }
    
    public boolean isEmpty(){
        return n == 0;
    }
    
    public int size(){
        return n;
    }
    
    private void resize(int capacity){
        Item[] temp = (Item[]) new Object[capacity];
        int counter = 0;
        for(int i = 0; i < nWithNulls; i++){
            if(a[i] != null)
                temp[counter++] = a[i];
             }
        a = temp;
        nWithNulls=n;
    }
    
    public void enqueue(Item item){
        if(item == null)
            throw new IllegalArgumentException("enqueue was passed a null argument");
        
        if (nWithNulls == a.length) resize(2*a.length);
        a[nWithNulls++] = item;
        n++;
    }
    
    private int getRandomIndex(){
        int randomIndex;
        do{ 
//            countRandomCalls++;
            randomIndex = StdRandom.uniform(nWithNulls);   
        }while(a[randomIndex] == null);
        
        return randomIndex;
    }
    
    public Item dequeue(){
       if(n == 0)
           throw new NoSuchElementException("Can't dequeue empty array");
//       StdOut.println("n is: " + n); 
       int randomIndex = getRandomIndex();
        Item item = a[randomIndex];
//        StdOut.println("Popping element " + randomIndex);
        a[randomIndex] = null;
        n--;
//        StdOut.println("n is: "  + n);
//        StdOut.println("n is " + n + " and a.length is " + a.length);
   
        if(n > 0 && n == a.length/4) resize(a.length/2);
        if(n == 0)
            nWithNulls = 0;
//        printArray(); 
//        StdOut.println();
        return item;
        
    }
    
    public Item sample(){
        if(n == 0)
            throw new NoSuchElementException("Can't sample empty array");
        return a[getRandomIndex()];
    }
  
    public Iterator<Item> iterator(){
        return new RandomArrayIterator();        
    }
    private class RandomArrayIterator implements Iterator<Item>{
        
        private Item[] randomSet = (Item[]) new Object[n];
        private int i;
        
        public RandomArrayIterator(){
            
            int ns = 0;
            for(int j=0;j<nWithNulls;j++){
                if(a[j] != null){
                    randomSet[ns]=a[j];
                    ns++;
                }
            }
//        StdOut.println("N is: " + n);  
//            for(int k=0;k<n;k++)
//                StdOut.print(k + ":" + randomSet[k] + " ");
//                
                
            i = n-1;
//        StdOut.println("Shuffling");
            StdRandom.shuffle(randomSet);
//            for(Item i:randomSet)
//                StdOut.print(i);
//        
        }
        
        public boolean hasNext(){
            return (i>=0);
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        public Item next(){
            if(!hasNext())
                throw new NoSuchElementException("The is no next element");
            return randomSet[i--];
        }
    }
  
    //for testing purposes
    public static void main(String[] args){
        RandomizedQueue<Integer> intStack = new RandomizedQueue<Integer>();
        
        for(int i = 0; i<10; i++)
            intStack.enqueue(i);
      
      
        for(int i = 0; i<10; i++)
        {   
            intStack.dequeue();
          }
        
               for(int l : intStack)
                StdOut.print(l);
   }
    
}