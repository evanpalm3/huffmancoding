/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hcompressor;

/**
 *
 * @author Evan
 */
import java.io.*;
public class BitWriter {
    private static final int BUFFER_SIZE = 1000;
    private PrintStream stream;
    private int index;
    private int position;
    private int[] nums;
    
    public BitWriter(PrintStream stream){
        this.stream =stream;
        index=0;
        position=0;
        nums = new int[BUFFER_SIZE];
    }
    
    public void writeBit(int bit){
        if(bit==1){
            writeBit(true);
        }else{
            writeBit(false);
        }
    }
    
    public void writeByte(byte data){
        stream.write((int)data);
    }
    
    public void writeBytes(byte[] data)throws IOException{
        stream.write(data);
    }
    
    public void writeBit(boolean bit){
        if(bit)
            nums[index]+=pow(2, 7-position);
        position++;
        index+=position/8;
        position=position%8;
        if(index==nums.length){
            int[] temp = new int[nums.length+BUFFER_SIZE];
            for(int i=0; i<nums.length; i++){
                temp[i] = nums[i];
            }
            nums=temp;
        }
    }
    
    public void writeToFile()throws IOException{
        byte[] data = new byte[index+(position+7)/8];
        for(int i=0; i<data.length; i++){
            data[i] = (byte)nums[i];
        }
        stream.write(data);
    }
    
    private int pow(int a, int e){
        int num=1;
        for(int i=0; i<e; i++){
            num = num*a;
        }
        return num;
    }
}
