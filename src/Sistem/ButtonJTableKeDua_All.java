/*
 *  Update by Ari Prasetiyo
    17-10-2014
    15:49
    Jakarta Utara, Kelapa Gading
 */
package Sistem;

import arprast.hrdpro.Hrd_DataKaryawanView;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ButtonJTableKeDua_All extends DefaultCellEditor {
    protected JButton button;
    DefaultTableModel ModelTabel2;
    JTable Tabel2;
    String TabelDb,  ColumnDbKey, label = "Delete";
    int EditOrDelete;
    
    /*
        keterangan
        EditOrDelete = 0 Edit
        EditOrDelete = 1 Delete
        Tabel        = Di Isi Table Database
        ColumnDb     = Di Isi dengan nama colum db yang mempunyai sifat unik / key
        DataSelect   = Data yang dipilih akan dihapus harus berada pada column index 1
    */
    
    /*
        Untuk Delete
    */
    public ButtonJTableKeDua_All(JCheckBox checkBox,  final DefaultTableModel ModelTabel,final JTable Tabel,int EditOrDelete, String TabelDb, String ColumnDb  ) {
        super(checkBox);
        this.ModelTabel2 = ModelTabel;
        this.Tabel2      = Tabel; 
        this.TabelDb     = TabelDb;
        this.ColumnDbKey = ColumnDb;
        this.EditOrDelete  = EditOrDelete;
    }
    
    /*
        Untuk Edit
    */
    public ButtonJTableKeDua_All(JCheckBox checkBox,  final DefaultTableModel ModelTabel,final JTable Tabel,int EditOrDelete) {
        super(checkBox);
        this.ModelTabel2 = ModelTabel;
        this.Tabel2      = Tabel; 
        this.TabelDb     = TabelDb;
    }
    
    public Component getTableCellEditorComponent(final JTable table, final Object value,
        boolean isSelected, final int row, int column ) {
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                // agar tidak error saat di hapus jtable      
                fireEditingStopped();    
                try { 
                    if (EditOrDelete == 0){
                         EditData (e);
                    }
                    else {
                         int Pilih = JOptionPane.showConfirmDialog(null, "Apakah anda yakin untuk menghapusnya ?" , " Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                         if (Pilih == JOptionPane.YES_OPTION){
                                    String DataSelect = (String )Tabel2.getValueAt(Tabel2.getSelectedRow(), 1);
                                    ModelTabel2.removeRow(row ); 
                                    DeleteDataAlokasiWaktu(DataSelect,TabelDb, ColumnDbKey);
                         }
                         else if ( Pilih == JOptionPane.NO_OPTION){
                        }
                    }
                }
                catch (Exception X){       
                }
              }
        });
        Tabel2.repaint();
        button.setText(label);
        return button;
    }
    public Object getCellEditorValue() {
        return new String(label);
    }
    private void DeleteDataAlokasiWaktu(String Data , String Tabel, String SyartKolom){
        Sistem.DB_MYSQL_Koneksi MYSQL = new Sistem.DB_MYSQL_Koneksi();
        //System.out.println("delete from "+Tabel +" where "+SyartKolom + " = '"+ Data + "'");
        MYSQL.MysqlDelete("delete from "+Tabel +" where "+SyartKolom + " = '"+ Data + "'");
     }  
    private void  EditData (ActionEvent e){
        /*
            Form edit harus dibuat manual seperti pada Hrd_DataKaryawanView
            Dan harus ada ViewDataDatabasePelamar
            
            Hrd_DataKaryawanView xx = new Hrd_DataKaryawanView(new javax.swing.JFrame(), true );
            xx.ViewDataDatabasePelamar((String)Tabel2.getValueAt(Tabel2.getSelectedRow(), 1));
            xx.setVisible(true);        
        */
        Hrd_DataKaryawanView xx = new Hrd_DataKaryawanView(new javax.swing.JFrame(), true );
        xx.ViewDataDatabasePelamar((String)Tabel2.getValueAt(Tabel2.getSelectedRow(), 1));
        xx.setVisible(true);               
    }   
}