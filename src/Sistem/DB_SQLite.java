/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LANTAI3
 */
public class DB_SQLite {
    
    public Connection Koneksi= null;
    public Statement stmt = null;
    
    public void SQLite(){
        //CreateDatabase();
    }
     public void ConnectSqlite(){
        
        try {
            Class.forName("org.sqlite.JDBC");
            Koneksi = DriverManager.getConnection("jdbc:sqlite:ip.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        //System.out.println("Opened database successfully");
    }
     
     /*
     Save data
     Editt 
     Delete
     */
     public void Query (String sql){
        ConnectSqlite();
        try {
            stmt = Koneksi.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Save successfully");
        } catch (SQLException ex) {
            Logger.getLogger(DB_SQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
     
     /*
     Ambil data
     Cara ambil data dari ArrayList
     Data.get(0).toString()
     
     Contoh
     ArrayList Data = new ArrayList();
        SQLite x = new SQLite();
        String [] Col = {"ip", "user", "password","dbname"};
        Data = x.SelectQuery("select * from server ", Col);
        try {
            jTextField2.setText(Data.get(0).toString());
            jTextField3.setText(Data.get(1).toString());
            jPasswordField4.setText(Data.get(2).toString());
            jTextField4.setText(Data.get(3).toString());
        }
        catch (Exception xx) {
        }
     */
    public ArrayList SelectQuery (String Query, String[] Column){
        ConnectSqlite();
        //CreateDatabase();
        String Data;
        ArrayList ListData = new ArrayList();
        
        int a = 0;
        try {
          stmt = Koneksi.createStatement();
          ResultSet rs = stmt.executeQuery(Query );
          while ( rs.next() ) {   
              for (a = 0; a < Column.length; a++) {
                  Data = rs.getString(Column[a].toString() );
                  ListData.add(Data);
              }
                
                //System.out.println("Get data successfully");
          }
          rs.close();
          stmt.close();
          Koneksi.close();
          return  ListData;
        } catch ( Exception e ) {
              System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return null;
    }
    public void CreateDatabase() {
        String sql4 = "CREATE TABLE server  ( ip  TEXT   primary key , user  TEXT , password  TEXT, dbname  TEXT   )"; 
        String temp = "CREATE TABLE temp  ( temp_import_xls  TEXT  )"; 
       
        Query (temp);
        System.out.println("coba " + temp);
    }

}
