/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belajar;

/**
 *
 * @author arprast
 */
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.File;

public class inputstream {
public static void main(String[] args) throws FileNotFoundException{
    
        
//membaca dan menulis file
String npm = new String("");
String nama = new String("");
double ipk;
PrintStream diskWriter = new PrintStream("mhs.txt"); //menyatakan print stream
int i;
Scanner sc = new Scanner(System.in);
System.out.print("Jumlah data = ");
i = sc.nextInt();
do{
    System.out.print("\nNIM = ");
    npm = sc.next();
    System.out.print("Nama = ");
    nama = sc.next();
    System.out.print("IPK = ");
    ipk = sc.nextDouble();
    diskWriter.println(npm + " " + nama + " " + ipk);
i--;
}while(i>0);

System.out.println("Isi File:");
Scanner diskScanner = new Scanner(new File("mhs.txt")); //mencetak print stream pada file mhs.txt
//bentuk penulisanna/cetakannya seperti ini
while (diskScanner.hasNext()){
    npm = diskScanner.next();
    nama = diskScanner.next();
    ipk = diskScanner.nextDouble();
    System.out.println("[" + npm + "] " + nama + " (" + ipk + ")");
}
}
}
