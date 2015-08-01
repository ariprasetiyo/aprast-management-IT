/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author arprast
 */
public class Sys_Running_Background_StartUp {
    int JumlahRow;
    public Sys_Running_Background_StartUp(){
        
    }
    public void Running_Load_Master_Item(final JProgressBar  jProgressBar1){
        Sistem.DB_MYSQL_Koneksi MySql   = new Sistem.DB_MYSQL_Koneksi();
        JumlahRow                   = MySql.MenghitungRowTables("master_barang", "item_code");
       
        Runnable runner = new Runnable() {
            public void run() {
                
                jProgressBar1.setMaximum(JumlahRow);
                jProgressBar1.setMinimum(0);
                jProgressBar1.setStringPainted(true);
                
                String Query2 = "select item_code, item_name from master_barang "; 
                arprast.all.Master_Harga.item_code.setModel(new javax.swing.DefaultComboBoxModel( app_search_data_all_2_item.getData(Query2, "item_code", "item_name", jProgressBar1).toArray()));
                
            }
         };
        SwingUtilities.invokeLater(runner);
    }
}
