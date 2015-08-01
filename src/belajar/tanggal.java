/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belajar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

/**
 *
 * @author arprast
 */
public class tanggal {
    public static void main ( String[] args){
        tanggal tgl = new tanggal ();
    }
    public tanggal (){
        cobaTanggalLagi();
        Date tanggal = tgl("09/04/2015","");
        System.out.println(tanggal);
       
    }
    
     public java.sql.Date tgl (String Data, String sampleDate){
                SimpleDateFormat formatter = new SimpleDateFormat(sampleDate);

                try {
                    java.sql.Date date = (java.sql.Date) formatter.parse(Data);
                    return date;

                } catch (ParseException e) {
                    return null;
                }
                
     }
     private void  cobaTanggalLagi (){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	String dateInString = "09/04/2015";
 
	try {
 
		java.util.Date date =  formatter.parse(dateInString);
		System.out.println(date);
		System.out.println(formatter.format(date));
 
	} catch (ParseException e) {
		e.printStackTrace();
	}
     }
 
}
