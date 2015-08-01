/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belajar;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.File;

public class input {

@SuppressWarnings("empty-statement")
public static void main(String[] args) throws FileNotFoundException {

//membaca dan menulis file
String npm = new String("");
String nama = new String("");
double ipk;
int i = 1;
{

System.out.println("PROGRAM UNTUK :");
System.out.println("================================");
System.out.println("1. INPUT DATA MAHASISWA");
System.out.println("2. VIEW DATA MAHASISWA");
System.out.println("3. EXIT");
System.out.println("================================");

Scanner masukanMenu = new Scanner(System.in);
int a = masukanMenu.nextInt();
switch (a){

case 1:
        PrintStream diskWriter = new PrintStream("d:/mhs.txt"); 
        Scanner sc = new Scanner(System.in);
        System.out.println("=============================");
        System.out.println("INPUT DATA MAHASISWA");
        System.out.println("=============================");
        System.out.print("\nNIM = ");
        npm = sc.next();
        System.out.print("Nama = ");
        nama = sc.next();
        System.out.print("IPK = ");
        ipk = sc.nextDouble();
        diskWriter.println(npm + " " + nama + " " + ipk);
break;
case 2:
    Scanner diskScanner = new Scanner(new File("d:/mhs.txt"));
    while (diskScanner.hasNext()){
        npm = diskScanner.next();
        nama = diskScanner.next();
        ipk = diskScanner.nextDouble();
        System.out.println("[" + npm + "] " + nama + " (" + ipk + ")");
    }
break;

case 3:
System.exit(0);

}
} 
}
}