/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import java.awt.Component;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LANTAI3
 */
public class SatuUntukSemua {
    public SatuUntukSemua(){
        
    }
    public void ResetTampilan(JTextField[] Field){
        for (int a = 0; a < Field.length; a++){
            Field[a].setText("");
        }
    }
    public void ResetTampilan(JTextField[] Field, JLabel[] Label){
        for (int a = 0; a < Field.length; a++){
            Field[a].setText("");
        }
        for (int a = 0; a < Label.length; a++){
            Label[a].setText("");
        }
    }
    
    public void HapusDataJTabel(JTable Data){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) Data.getModel();
        dtm.setRowCount(0); 
     }
    
    public void DeleteData(String Data, String Table){       
        Sistem.DB_MYSQL_Koneksi MYSQL = new Sistem.DB_MYSQL_Koneksi();
        MYSQL.MysqlDelete("delete from "+ Table+ " where trans_no = '"+ Data + "'");
        //DeleteGambar (Data);       
     } 
    public void DeleteData(String Data, String Table, String Syarat){       
        Sistem.DB_MYSQL_Koneksi MYSQL = new Sistem.DB_MYSQL_Koneksi();
        MYSQL.MysqlDelete("delete from "+ Table+ " where "+ Syarat +" = '"+ Data + "'");
        //DeleteGambar (Data);       
     } 
    
    /*
     * Untuk Cek Data Kosong
     */
    public void ValidasiData(JTextField[] Data, String DataPesanError[], String[] DiRubahKe){
        if (Data.length == DataPesanError.length){
            for (int a = 0; a < Data.length; a++){
                if (Data[a].getText().equals("")){
                     JOptionPane.showMessageDialog(null, "Data error : " + DataPesanError[a] );
                     Data[a].setText(DiRubahKe[a]);
                     continue;
                }
            }           
        }
        else {
            System.out.println("tidak sama data length");
            System.exit(1);
        }
    }
    public boolean ValidasiData(JTextField[] Data, String DataPesanError[]){
        if (Data.length == DataPesanError.length){
            for (int a = 0; a < Data.length; a++){
                if (Data[a].getText().equals("")){
                     JOptionPane.showMessageDialog(null, "Data error : " + DataPesanError[a] );
                     return false;
                }
            }           
        }
        else {
            System.out.println("tidak sama data length");
            System.exit(1);
        }
        return true;
    }
    public boolean ValidasiData(String[] Data, String DataPesanError[]){
        if (Data.length == DataPesanError.length){
            for (int a = 0; a < Data.length; a++){
                if (Data[a] == null){
                     JOptionPane.showMessageDialog(null, "Data error : " + DataPesanError[a] );
                     return false;
                }
                else if (Data[a].equals("")){
                     JOptionPane.showMessageDialog(null, "Data error : " + DataPesanError[a] );
                     return false;
                }
            }           
        }
        else {
            System.out.println("tidak sama data length");
            System.exit(1);
        }
        return true;
    }
    
    /*
     *  Untuk Transaksi misal WR921921 --  Nama
        Ambil item namenya
     */
     public String Filter_Item_Name(String Data, JLabel Keterangan){
        try {
            String PartRegex = "(.*)?(\\s)*(--)+(\\s)*";
            Data   = Data.replaceAll( PartRegex, "");
            return Data;
        }
        catch (Exception  x){
            Keterangan.setText("Tidak Bisa Di Filter");
        }
        return "xxx";   
    }
    
    /*
     *  Untuk Transaksi misal WR921921 --  Nama
        Ambil item code
     */
    public String Filter_Item_Code(String Data, JLabel Keterangan){
        try {
            String PartRegex = "(\\s)*(--)+(\\s)*(.*)?";
            Data   = Data.replaceAll( PartRegex, "");
            return Data;
        }
        catch (Exception  x){
            Keterangan.setText("Tidak Bisa Di Filter");
        }
        return "xxx";   
    }
    public String FilterTransNoSaja(String Data, JLabel Keterangan){
        try {
            String PartRegex = ".*(\\s)*(--)+(\\s)";
            Data   = Data.replaceAll( PartRegex, "");
            return Data;
        }
        catch (Exception  x){
            Keterangan.setText("Tidak Bisa Di Filter");
        }
        return "xxx";   
    }
    /*
     * Yang di atas asalah semua
     */
    public String AmbilBelakang(String Data, JLabel Keterangan){
        try {
            String PartRegex = ".*(\\s)(--)+(\\s)";
            Data   = Data.replaceAll( PartRegex, "");
            return Data;
        }
        catch (Exception  x){
            Keterangan.setText("Tidak Bisa Di Filter");
        }
        return "xxx";   
    }
    public String AmbilDepan(String Data, JLabel Keterangan){
        try {
            String PartRegex = "(\\s)(--)+(\\s).*";
            Data   = Data.replaceAll( PartRegex, "");
            return Data;
        }
        catch (Exception  x){
            Keterangan.setText("Tidak Bisa Di Filter");
        }
        return "xxx";   
    }
     
    
    /*
     * Merubah String menjadi Data double dan Integer
     */
    public double BersihDataKeDoubel(String Data){
        if (Data.equals("null") || Data.equals("") ){
            Data = "0";
        }
        
        Data = Data.replaceAll("\\s.*", "");
        return Double.valueOf(Data).doubleValue();
    }
    public int BersihDataKeInt (String Data){
        if (Data.equals("null") || Data.equals("") ){
            Data = "0";
        }
        
        Data = Data.replaceAll("\\s.*", "");
        return Integer.valueOf(Data).intValue();
    }
    
    /*
     * Logika tutup dan buka tombol
     */
    public void LogikaComponent(Component[]  Field, boolean[] TutupTidak){
        for (int a = 0; a < Field.length; a++){
            Field[a].setEnabled(TutupTidak[a]);
            continue;
        }
    }
    public void LogikaComponent(Component[]  Field, boolean TutupTidak){
        for (int a = 0; a < Field.length; a++){
            Field[a].setEnabled(TutupTidak);
            continue;
        }
    }
    
    /*
     * Logika ambil jam saja dari Kazao
     * jam1 = kazaoCalendarTime1.getCalendar().getTime().toGMTString();
     */
    public String GetJamKazao(String Data){
        Pattern MY_PATTERN = Pattern.compile("\\s([0-9][0-9]:[0-9][0-9]:[0-9][0-9])\\s");
            Matcher m = MY_PATTERN.matcher(Data);
            String s = null ;
               while (m.find()) {
                   s = m.group(1);

             }
        return s;
    }
    
    /*
    Random int ada min dan maxnya
    */
    public int randInt(int min, int max) {

    Random rand = new Random();

    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
    }
}
