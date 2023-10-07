package hcompressor;

/*
HuffmanNode.java
by Evan Palm
8/11/2018
 */

public class HuffmanNode implements Comparable<HuffmanNode>{
    //instance variables
    public HuffmanNode left;
    public HuffmanNode right;
    public int frequency;
    public int value;
    
    public HuffmanNode(){//empty constructor
        
    }

    @Override
    public int compareTo(HuffmanNode t) {//ours - theirs
        return frequency-t.frequency;
    }
}
