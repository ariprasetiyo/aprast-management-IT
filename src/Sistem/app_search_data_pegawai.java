/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 *  Cara Penggunaannya
    JComboBoxItem.setSelectedIndex(-1);
    final JTextField JTextFieldItem = (JTextField)JComboBoxItem.getEditor().getEditorComponent();
    JTextFieldItem.setText("");
    JTextFieldItem.addKeyListener(new app_search1(JComboBoxItem));
    * 
    * Pada Jcombobox edit source kemudia set model dengan 
    * new javax.swing.DefaultComboBoxModel(app_search_data_fixed_asset.getData().toArray())
 */
package Sistem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class app_search_data_pegawai {
        

	private static List<String> list;
        public app_search_data_pegawai () {

        }   

	public static List<String> getData(){
             /*
        * Memanggil agar bisa terkoneksi dengan database
        * Start
        */
        DB_MYSQL cc = new DB_MYSQL();
        Connection koneksi = cc.createConnection();    
       
       
       /*
        * Endg   
        */
       
       ResultSet hasilquery = null;
       try {
           Statement stm = koneksi.createStatement();
           /*
           " SELECT header_penerimaan.judul_resep, header_resep.porsi_resep, header_resep.nama_resep " +
                                            " FROM header_resep " +
                                            "JOIN header_penerimaan ON header_resep.nama_resep = header_penerimaan.judul_resep " +
                                            "LIMIT 0 ,20" */
           hasilquery = stm.executeQuery("select a.nik,b.nama  from header_pegawai as a "
                   + "inner join header_pelamar as b on a.no_pelamar = b.trans_no where  a.status_pekerja = '0' order   by b.nama desc");
                                                   }
       catch (Exception ex){System.err.println ("app_search_data_fixed_error.javaError (3)"+ex);
       System.exit(1);
           
       }
       list = new ArrayList<String>();
       String Data_List_Trans_NO;
       Vector Vector_Id= new Vector();
       String Data_List_Item_Name;
       try {
           while(hasilquery.next()){
              //Data_List_Item_Part = hasilquery.getString("chart_no");
              //Vector_Id.addElement(No_Id);
              Data_List_Item_Name = hasilquery.getString("nik");
              Data_List_Trans_NO = hasilquery.getString("nama");
               //System.out.println(coba);
              list.add(Data_List_Item_Name + " -- " + Data_List_Trans_NO);
           }
           
       }
       catch (Exception ex){System.err.println ("Error (4)"+ex);
       System.exit(1);
           
       }
		//list.add("Abdul Kadir");
		//list.add("Abdul Muis");
                //bacadatanegara as = bacadatanegara();
		return list;
	} /*
        public static  List<Vector> getData(){
		return item_dijual;
	} */

    
    public void KonekDatabase() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

  

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 *  Cara Penggunaannya
    JComboBoxItem.setSelectedIndex(-1);
    final JTextField JTextFieldItem = (JTextField)JComboBoxItem.getEditor().getEditorComponent();
    JTextFieldItem.setText("");
    JTextFieldItem.addKeyListener(new app_search1(JComboBoxItem));
    * 
    * Pada Jcombobox edit source kemudia set model dengan 
    * new javax.swing.DefaultComboBoxModel(app_search_data_fixed_asset.getData().toArray())
 */
