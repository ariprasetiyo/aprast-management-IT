/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import arprast.hrdpro.Hrd_DataKaryawanView;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ButtonJTableKeDuaLocal extends DefaultCellEditor {
    private String label;
    protected JButton button;
    DefaultTableModel ModelTabel2;
    JTable Tabel2;
    public ButtonJTableKeDuaLocal(JCheckBox checkBox,  final DefaultTableModel ModelTabel,final JTable Tabel  ) {
        super(checkBox);
        this.ModelTabel2 = ModelTabel;
        this.Tabel2 = Tabel; 


    }
    
    
    public Component getTableCellEditorComponent(final JTable table, final Object value,
        boolean isSelected, final int row, int column) {
        if (Tabel2.getName().equals("karyawan")){
            label = (value == null) ? "Edit" + row: value.toString();
        }
        else {
            label = (value == null) ? "Delete" + row: value.toString();
        }
        
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                fireEditingStopped(); // agar tidak error saat di hapus jtable         
                try { 
                    if (Tabel2.getName().equals("karyawan")){
                         EditData (e);
                    }
                    else {
                         int Pilih = JOptionPane.showConfirmDialog(null, "Apakah anda yakin untuk menghapusnya ?" , " Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                         if (Pilih == JOptionPane.YES_OPTION){
                                
                                if (Tabel2.getName() .equals("Pelamar")){
                                    String DataSelect = (String )Tabel2.getValueAt(Tabel2.getSelectedRow(), 1);
                                    ModelTabel2.removeRow(row ); 
                                    DeleteDataAlokasiWaktu(DataSelect, "header_pelamar", "trans_no");
                                }
                                else if (Tabel2.getName().equalsIgnoreCase("form")){
                                    String DataSelect = (String )Tabel2.getValueAt(Tabel2.getSelectedRow(), 2);
                                    System.out.println("zz"+DataSelect+"sss");
                                    ModelTabel2.removeRow(row ); 
                                    DeleteDataAlokasiWaktu(DataSelect, "header_cuti", "no_cuti");
                                    DeleteDataAlokasiWaktu(DataSelect, "header_lembur", "no_lembur");
                                }
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
        MYSQL.MysqlDelete("delete from "+Tabel +" where "+SyartKolom + " = '"+ Data + "'");
     }  
    private void  EditData (ActionEvent e){
        Hrd_DataKaryawanView xx = new Hrd_DataKaryawanView(new javax.swing.JFrame(), true );
        xx.ViewDataDatabasePelamar((String)Tabel2.getValueAt(Tabel2.getSelectedRow(), 1));
        xx.setVisible(true);               
    }   
}