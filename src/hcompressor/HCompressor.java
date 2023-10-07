package hcompressor;

import java.io.*;
import java.util.Scanner;

public class HCompressor {
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        if(args.length!=2){
            System.out.println("command line arguements missing!");
            System.out.println("usage: /java HCompressor <filename> <c or d>");
        }else{
            String filename=args[0];
            char choice = args[1].charAt(0);
            if(choice=='c'){
                compress(filename);
            }else if(choice=='d'){
                decompress(filename);
            }else{
                System.out.println("incorrect usage");
            }
        }
    }
    
    public static void compress(String filename)throws FileNotFoundException, IOException{
        FileInputStream input = new FileInputStream(filename);
        int[] count = new int[256];
        int z = input.read();
        while (z != -1) {
            count[z]++;
            z = input.read();
        }
        HuffmanTree tree = new HuffmanTree(count);
        String outFile = filename.substring(0, filename.indexOf('.'))+".palm";
        PrintStream output = new PrintStream(outFile);
        String newName=filename;
        int index =-1;
        for(int i=0; i<newName.length(); i++){
            if(newName.charAt(i)=='/' || newName.charAt(i)=='\\'){
                index=i;
            }
        }
        if(index!=-1){
            newName=newName.substring(index+1);
        }
        output.println(newName);
        tree.write(output);
        output.print('-');
        String[] codes = tree.codes(257);
            
        // open source file, open output, encode
        FileInputStream input1 = new FileInputStream(filename);
        BitWriter out = new BitWriter(output);
        int n = input1.read();
        while (n != -1) {
            if (codes[n] == null) {
                System.out.println("Your code file has no code for " + n +
                                   " (the character '" + (char)n + "')");
                System.out.println("exiting...");
                System.exit(1);
            }
            writeString(codes[n], out);
            n = input1.read();
        }
        writeString(codes[256], out);
        out.writeToFile();
        output.close();
        System.out.println(outFile+" was created.");
    }
    
    public static void decompress(String filename)throws FileNotFoundException, IOException{
        BitReader reader = new BitReader(filename);
        byte[] data = reader.getEverything();
        String fileString = "";
        for(int i=0; i<data.length; i++){
            fileString+=(char)data[i];
        }
        Scanner scan = new Scanner(fileString);
        String original = scan.nextLine();
        String code="";
        boolean running=true;
        while(running){
            String str = scan.nextLine();
            if(str.charAt(0)=='-'){
                running = false;
            }else{
                code+=str+"\n";
            }
        }
        Scanner codeScan = new Scanner(code);
        scan.close();
        HuffmanTree tree = new HuffmanTree(codeScan);
        int index=-1;
        for(int i=0; i<data.length; i++){
            if(data[i]==(byte)'-'){
                index=i;
                i=data.length;
            }
        }
        reader.jumpToByte(index+1);
        PrintStream output = new PrintStream(original);
        tree.decode(reader, output, 256);
        output.close();
        System.out.println(original+" was created.");
    }
    
    public static void writeString(String s, BitWriter output) {
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i)=='0'){
                output.writeBit(false);
            }else{
                output.writeBit(true);
            }
        }
    }
}
