package hcompressor;

/*
HuffmanTree.java
by Evan Palm
8/11/2018
 */

import java.io.*;
import java.util.*;

public class HuffmanTree {
    private HuffmanNode front;//top of the HuffmanTree
    
    //constructors
    public HuffmanTree(Scanner scan){
        while(scan.hasNext()){//traverses down the tree creating nodes where they are null until it arrives at the destination
            int n = Integer.parseInt(scan.nextLine());
            String code = scan.nextLine();
            front = addNode(front, code, n);
        }
    }
    
    public HuffmanTree(int[] count){
        int eof=count.length;//defining the end of file character
        Queue<HuffmanNode> q = new PriorityQueue();
        for(int i=0; i<count.length; i++){
            if(count[i]!=0){
                HuffmanNode current = new HuffmanNode();//creates a note for every character present in the file
                current.frequency=count[i];
                current.value = (char)i;
                q.add(current);//adds each new node to the priority queue
            }
        }
        HuffmanNode eofNode = new HuffmanNode();
        eofNode.frequency=1;
        eofNode.value=eof;
        q.add(eofNode);//add end of file character node
        while(q.size()>1){//runs until there is only one node (tree is complete)
            HuffmanNode node0 = q.remove();
            HuffmanNode node1 = q.remove();
            HuffmanNode newNode = new HuffmanNode();//attaches two nodes two new node with combined frequency
            newNode.frequency = node0.frequency+node1.frequency;
            newNode.left = node0;
            newNode.right = node1;
            q.add(newNode);
        }
        front = q.remove();
    }
    
    private HuffmanNode addNode(HuffmanNode current, String code, int character){//recursively traverses the tree to add nodes
        if(current==null){//checks to see if a new node is needed
            current = new HuffmanNode();
        }
        if(code.length()>0){
            if(code.charAt(0)=='0'){//0 for left node
                current.left = addNode(current.left, code.substring(1), character);
            }else{//1 for right node
                current.right = addNode(current.right, code.substring(1), character);
            }
        }else{//this case is ran if we have reached the leaf node
            current.value = character;
        }
        return current;//goes back up the tree
    }
    
    public String[] codes(int size){
        String[] codes = new String[size];
        makeCodes(front, "", codes);
        return codes;
    }
    
    private void makeCodes(HuffmanNode current, String code, String[] codes){
        if(current.left!=null){
            makeCodes(current.left, code+'0', codes);
            makeCodes(current.right, code+'1', codes);
        }else{
            codes[current.value] = code;
        }
    }
    
    public void write(PrintStream output){//public method for recursive pair
        writeNodes(front, "", output);
    }
    
    private void writeNodes(HuffmanNode current, String code, PrintStream output){
        if(current.left!=null){//checks if the current node is a branch node
            writeNodes(current.left, code+"0", output);
            writeNodes(current.right, code+"1", output);
        }else{//leaf node case
            output.println(current.value);//integer value of character
            output.println(code);//traversal path
        }
    }
    
    public void decode(BitReader input, PrintStream output, int eof){
        boolean running=true;
        while(running){//loops until eof is reached
            int nextChar = getChar(front, input);//traverses tree for character
            if(nextChar==eof){
                running=false;
            }else{
                output.write(nextChar);//writes the character to the file
            }
        }
    }
    
    private int getChar(HuffmanNode current, BitReader input){
        if(current.left==null){//checks if leaf node
            return current.value;
        }
        if(input.nextBit()==0){
            return getChar(current.left, input);//recurses left for 0
        }else{
            return getChar(current.right, input);//recurses right for 1
        }
    }
}
