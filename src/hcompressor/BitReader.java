/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hcompressor;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Evan
 */
import java.io.*;
public class BitReader{
    private byte[] data;
    private static int BUFFER=1000;
    private int currentIndex;
    private int[] currentByte;
    private int bitIndex;
    
    public BitReader(String filename)throws FileNotFoundException, IOException{
        data = new byte[BUFFER];
        int size=0;
        FileInputStream stream = new FileInputStream(new File(filename));
        boolean running=true;
        while(running){
            int temp = stream.read();
            if(temp!=-1){
                data[size]=(byte)temp;
                size++;
                if(size==data.length){
                    byte[] newArray = new byte[data.length+BUFFER];
                    for(int i=0; i<data.length; i++){
                        newArray[i]=data[i];
                    }
                    data=newArray;
                }
            }else{
                running=false;
            }
        }
        if(size!=data.length){
            byte[] someData = new byte[size];
            for(int i=0; i<size; i++){
                someData[i]=data[i];
            }
            data=someData;
        }
        currentIndex=-1;
        bitIndex=0;
        nextByte();
    }
    
    public byte[] getEverything(){
        return data;
    }
    
    public int nextBit(){
        int val = currentByte[bitIndex];
        bitIndex++;
        if(bitIndex==8){
            nextByte();
        }
        return val;
    }
    
    private void nextByte(){
        currentIndex++;
        if(currentIndex==data.length){
            currentIndex--;
            currentByte = new int[8];
            return;
        }
        int tempByte = data[currentIndex]+256;
        bitIndex=0;
        currentByte = new int[8];
        for(int i=7; i>=0; i--){
            currentByte[i] = tempByte%2;
            tempByte/=2;
        }
    }
    
    public void jumpToByte(int index){
        currentIndex=index-1;
        nextByte();
    }
    
    private int pow(int a, int e){
        int num=1;
        for(int i=0; i<e; i++){
            num = num*a;
        }
        return num;
    }
}
