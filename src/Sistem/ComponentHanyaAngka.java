/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

/**
 *
 * @author LANTAI3
 */
public class ComponentHanyaAngka {
    
    public ComponentHanyaAngka(){
        
    }
    public void  SetAntiAngka(JTextField x){
        x.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e)
                {
                  char c = e.getKeyChar();
                  if (!((c >= '0') && (c <= '9') ||    
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE))) {
                        //getToolkit().beep();
                        e.consume();
                  }
                }
        });
    }
    
    public void  SetAntiAngkaLimit(final JTextField x, final int Limit){
        x.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e)
                {
                  String Data = x.getText();
                  int Jml  =Data.length();
                  char c = e.getKeyChar();
                  if (!((c >= '0') && (c <= '9') && (Jml < Limit)||    
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE))) {
                        //getToolkit().beep();
                        e.consume();
                  }
            }
        });
    }
    
    int Jml;
    public void  SetAntiAngkaLimit(final JFormattedTextField x, final int Limit){
        x.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e)
                {
                  Jml =x.getText().length();
                  char c = e.getKeyChar();
                  if (!((c >= '0') && (c <= '9') && (Jml < Limit)||    
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE))) {
                        //getToolkit().beep();
                        e.consume();
                  }
                }
        });
    }

   
    public void  SetAntiAngkaPakeTitik (final JTextField x){
        x.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e)
                {
                  char c = e.getKeyChar();
                  if (!( (c  == '.') || (c >= '0') && (c <= '9')  ||
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE)  )) {
                      e.consume();
                  }
                  
                }
        });
    }
    
   /*
    Format tanggal
    10/10/2014
    */
    String Dat;
    public void  SetAntiAngkaUntukTanggal (final JTextField x){
        x.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e)
                {
                  char c = e.getKeyChar();
                  Jml  = x.getText().length();
                  if (!( (c  == '/') || (c >= '0') && (c <= '9') && (Jml <= 9)  ||
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE)  )) {
                      e.consume();
                  }
                  else {
                        Dat = x.getText();
                        Dat = Dat.replaceAll(".", "");

                        BigInteger harga = new BigInteger( Dat);
            
                        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                        formatRp.setCurrencySymbol("Rp.");
                        formatRp.setMonetaryDecimalSeparator(',');
                        formatRp.setGroupingSeparator('.');

                        kursIndonesia.setDecimalFormatSymbols(formatRp);
                        System.out.printf("Harga Rupiah: %s %n", kursIndonesia.format(harga));
                        x.setText(kursIndonesia.format(harga));
                      
                      /*
                      try {
                          Dat = x.getText().substring(2, 3).replaceAll("[0-9]", "/");
                          x.getText(1, 2);
                          //Dat = x.getText().substring(0,1) + Dat;       
                          System.out.println(x.getText().substring(2, 3));
                          System.out.println( x.getText().substring(2, 3).replaceAll("[0-9]", "/"));
                          x.setText(x.getText() + "/");
                      }
                      catch (Exception X){
                          
                      }
                          
                       if (Jml == 5){
                          x.setText(x.getText() + "/");
                      } */
                       //x.setText(Dat);
                  }
              }
        });
    }
    
    /*
    JTextField acction key direct change value format Rupiah
    */
    public void  SetAngkaFormatRupiah (final JTextField x){
        /*
        x.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                Dat = x.getText();
                char c = e.getKeyChar();
                  Jml  = x.getText().length();
                  if (!(  (c >= '0') && (c <= '9')  ||
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE)  )) {
                      e.consume();
                  }
                  else {
                     Dat = x.getText();
                  }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                     BigInteger harga = new BigInteger(Dat);

                    DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                    DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                    formatRp.setCurrencySymbol("Rp. ");
                    formatRp.setMonetaryDecimalSeparator(',');
                    formatRp.setGroupingSeparator('.');

                    kursIndonesia.setDecimalFormatSymbols(formatRp);
                    System.out.printf("Harga Rupiah: %s %n", kursIndonesia.format(harga));
                    x.setText(kursIndonesia.format(harga));
                }
                catch (Exception X){
                    
                }
               
            }
            
        });
        
        */
        x.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e)
                {
                  char c = e.getKeyChar();
                  Jml  = x.getText().length();
                  if (!(  (c >= '0') && (c <= '9')  ||
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE)  )) {
                      e.consume();
                  }
                  else {
                        Dat = x.getText();
                        System.out.println(Dat);
                        
                        if (Dat.length() % 3 == 0) {
                           
                        }
/*
                        Dat = Dat.replaceAll("Rp.|((,[0-9])|(,[0-9][0-9])|(,[0-9][0-9][0-9]))|\\s|\\.", "");
                        System.out.println("xx"+Dat + "xx ");
                        try {
                           BigInteger harga = new BigInteger(Dat);
                        
                           x.setText("");

                           DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                           DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                           formatRp.setCurrencySymbol("Rp. ");
                           formatRp.setMonetaryDecimalSeparator(',');
                           formatRp.setGroupingSeparator('.');

                           kursIndonesia.setDecimalFormatSymbols(formatRp);
                           System.out.printf("Harga Rupiah: %s %n", kursIndonesia.format(harga));
                           x.setText(kursIndonesia.format(harga));
                            
                        }
                        catch(Exception X){
                           
                        }*/
                  }
              }
        });
    }
    
    public void SetAntiAngka(TextField x) {
       x.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e)
                {
                  char c = e.getKeyChar();
                  if (!((c >= '0') && (c <= '9') ||    
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE))) {
                        //getToolkit().beep();
                        e.consume();
                  }
                }
        });
    }

     public void LimitCharInput (final TextField x, final int Batas){
         x.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e)
                {
                    String Data = x.getText();
                    int Limit = Data.length();
                    if (Limit > Batas){
                        e.consume();  
                        x.setText("");
                    }
                }
        });
        
    }
     
     /*
      * Koma boleh
      */
     public void  SetAntiAngkaPakeKoma (JTextField x){
        x.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e)
                {
                  char c = e.getKeyChar();
                  if (!( (c  == ',') || (c >= '0') && (c <= '9')  ||
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE)  )) {
                        //getToolkit().beep();
                        e.consume();
                  }
                  
                }
        });
    }
}
