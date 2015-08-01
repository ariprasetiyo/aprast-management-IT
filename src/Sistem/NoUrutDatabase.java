/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author LANTAI3
 */
public class NoUrutDatabase {
    DB_MYSQL KoneksiDb = new DB_MYSQL();
    Connection koneksi = KoneksiDb.createConnection();
    private int DataNoUrut;
    public int GetNoUrutDB (){
        return DataNoUrut;
    }
    public void SetNoUrutDB (  String Database ,String OrderByNo , String AscOrDesc){
        ResultSet hasilquery = null;
        try {
            int no = 0;
            Statement stm = koneksi.createStatement();
                //
                //SELECT groupno FROM `sys_groups` order by groupno asc limit 0,1
                //select * from tbheaderpo where period = 'asas'  order by period desc LIMIT 0,1
                hasilquery = stm.executeQuery("SELECT "+OrderByNo +" FROM "+ Database
                        +" order by " + OrderByNo + " " + AscOrDesc + " limit 0,1");
                while(hasilquery.next()){
                    no = hasilquery.getInt(OrderByNo);
                    //this.DataNoUrut = no + 1;
                }
                this.DataNoUrut = no + 1;
        }
        catch ( Exception Ari){
            JOptionPane.showMessageDialog(null, " TransNo.java Error : 22245 : "+ Ari, "Error ", JOptionPane.ERROR_MESSAGE);
          
            System.exit(1);
        }
    }
}
