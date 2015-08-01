/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belajar;


import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
 
public class txt_input_same_txt_append 
{
    static FileHandler fileTxt;
static SimpleFormatter formatterTxt;
    public static void main( String[] args )
    {	
    	try{
    		txt_input_same_txt_append xx = new txt_input_same_txt_append();
 
    	}catch(Exception e){
            System.out.println("cek");
            txt_input_same_txt_append.class.getName();
            new loggerError( txt_input_same_txt_append.class.getName(), e);
            e.printStackTrace();
    	}
    }
    
    private void input(){
        int a = 1 + 2 ;
                
        int i;

        Scanner sc = new Scanner(System.in);
        System.out.print("Jumlah data = ");
        i = sc.nextInt();

        i  = i * 100;
    }
    
    private  txt_input_same_txt_append() throws IOException{
        
        
        String data = " This content will append to the end of the file 2 \n";
 
    		File file =  new File("javaio-appendfile.txt");
 
    		//if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
                
                input();
 
    		//true = append file
    		FileWriter fileWritter = new FileWriter(file.getName(),true);
    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    	        fileWritter.write(data);
    	        bufferWritter.close();
 
	        System.out.println("Done");
                
                Logger logger = Logger.getLogger("error");
    logger.setLevel(Level.INFO);//Loget Info, Warning dhe Severe do ruhen
    fileTxt = new FileHandler("SimleTaskEvents.txt");
    formatterTxt = new SimpleFormatter();
    fileTxt.setFormatter(formatterTxt);
    logger.addHandler(fileTxt);
    }
}