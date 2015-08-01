/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belajar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.imageio.IIOException;

public class Temp {
    public static void main(String[] args) throws IOException {
        Temp main = new Temp();
        main.createTempFileOldWay();
        main.createTempFileWithDirOldWay();
        main.createTempFile();
        main.createTempFileWithDir();
        main.createTempFileShutdownHook();
        main.createTempFileDeleteOnExit();
        main.createTempFileDeleteOnClose();
        main.mykong();
    }
    private void createTempFileOldWay() throws IOException {
        File tempFile = File.createTempFile("tempfile-old", ".tmp");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(tempFile));
            writer.println("Line1");
            writer.println("Line2");
        } finally {
            if (writer != null) {
                writer.close();
                }
        }
            System.out.printf("Wrote text to temporary file %s%n", tempFile.toString());
    }
    private void createTempFileWithDirOldWay() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir", null), "tempdir-old");
        if (!tempDir.exists() && !tempDir.mkdir()) {
            throw new IIOException("Failed to create temporary directory " + tempDir);
        }

        File tempFile = File.createTempFile("tempfile-old", ".tmp", tempDir);
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(tempFile));
            writer.println("Line1");
            writer.println("Line2");
        } 

        finally {
            if (writer != null) {
                writer.close();
            }
        }
        System.out.printf("Wrote text to temporary file %s%n", tempFile.toString());
    }
    private void createTempFile() throws IOException {
        Path tempFile = Files.createTempFile("tempfiles", ".tmp");
        
        List<String> lines = Arrays.asList("Line1", "Line2", "line3");
        Files.write(tempFile, lines, Charset.defaultCharset(), StandardOpenOption.WRITE);
        System.out.printf("Wrote text to temporary file %s%n", tempFile.toString());
        
        Scanner diskScanner = new Scanner(new File(tempFile.toString())); //mencetak print stream pada file mhs.txt
//bentuk penulisanna/cetakannya seperti ini
        String npm, nama, ipk;
        while (diskScanner.hasNext()){
            npm = diskScanner.next();
            System.out.println("[" + npm + "]  ");
        }
        
        //read contents
        String str;
        BufferedReader br = new BufferedReader(new FileReader(tempFile.toString())); 
        while((str = br.readLine()) != null) {
           System.out.println(str);
        } 
        br.close();
        
    }
    private void createTempFileWithDir() throws IOException {
        Path tempDir = Files.createTempDirectory("tempfiles");
        Path tempFile = Files.createTempFile(tempDir, "tempfiles", ".tmp");
        List<String> lines = Arrays.asList("Line1", "Line2");
        Files.write(tempFile, lines, Charset.defaultCharset(), StandardOpenOption.WRITE);
        System.out.printf("Wrote text to temporary file %s%n", tempFile.toString());
        
        

    }
    private void createTempFileShutdownHook() throws IOException {
        final Path tempFile = Files.createTempFile("tempfiles-shutdown-hook", ".tmp");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    Files.delete(tempFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        List<String> lines = Arrays.asList("Line1", "Line2");
        Files.write(tempFile, lines, Charset.defaultCharset(), StandardOpenOption.WRITE);
        System.out.printf("Wrote text to temporary file %s%n", tempFile.toString());
    }
    private void createTempFileDeleteOnExit() throws IOException {
        Path tempFile = Files.createTempFile("tempfiles-delete-on-exit", ".tmp");
        tempFile.toFile().deleteOnExit();
        List<String> lines = Arrays.asList("Line1", "Line2");
        Files.write(tempFile, lines, Charset.defaultCharset(), StandardOpenOption.WRITE);
        System.out.printf("Wrote text to temporary file %s%n", tempFile.toString());
    }
    private void createTempFileDeleteOnClose() throws IOException {
        Path tempFile = Files.createTempFile("tempfiles-delete-on-close", ".tmp");
        List<String> lines = Arrays.asList("Line1", "Line2");
        Files.write(tempFile, lines, Charset.defaultCharset(), StandardOpenOption.WRITE, StandardOpenOption.DELETE_ON_CLOSE);
        System.out.printf("Wrote text to temporary file %s%n", tempFile.toString());
    }
    
    void mykong() {
        try{
 
    		//create a temp file
    		File temp = File.createTempFile("temp-file-name", ".tmp"); 
 
    		System.out.println("Temp file : " + temp.getAbsolutePath());
 
		//Get tempropary file path
    		String absolutePath = temp.getAbsolutePath();
    		String tempFilePath = absolutePath.
    		    substring(0,absolutePath.lastIndexOf(File.separator));
 
    		System.out.println("Temp file path : " + tempFilePath);
                
               //read contents
                String str;
                BufferedReader br = new BufferedReader(new FileReader(temp)); 
                while((str = br.readLine()) != null) {
                   System.out.println(str);
                } 
                br.close();
 
    	}catch(IOException e){

    		e.printStackTrace();
 
    	}
        
        
    
}
}

