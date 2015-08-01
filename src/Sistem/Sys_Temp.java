/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import java.util.ArrayList;

/**
 *
 * @author arprast
 */
public class Sys_Temp {
    DB_SQLite sql_lite = new DB_SQLite();
    public Sys_Temp(){
        
    }
    
    /*
    Cara penggunaan :
        - Panggil terlebih dahulu AmbilTemp();
        - baru Simpan TempFile
    */
    
   /*
    Menyipan temp ke sqlite
    */
    public String TempFile(String File, String TabelDb){
        if (File == null ) {
            
        }
        else {
            sql_lite.Query("delete from " + TabelDb);
            sql_lite.Query("insert into "+ TabelDb +" values ('"+ File +"')");
            return File;
        }
        return "";
    }
    
    /*
    Mengambil temp dari sqlite
    */
    public String AmbilTemp(String ColumnDb, String TabelDb){
        String[] Column = {ColumnDb};
        ArrayList Data = new ArrayList();
        Data = sql_lite.SelectQuery("select temp_import_xls from "+ TabelDb, Column);
        
        try {
            if (Data.get(0).toString() != null || !Data.get(0).toString().equalsIgnoreCase("")){
                return Data.get(0).toString();
            }
            else{
                return "";
            }
        }
        catch (Exception x) {
            if (x.toString().contains("Index: 0, Size: 0")) {
                return "";
            }
            else {
                System.err.println("error: 86638");
                x.printStackTrace();
            }
        }
        return "";
    }
}
