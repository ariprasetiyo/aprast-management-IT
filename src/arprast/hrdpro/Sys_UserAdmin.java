/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arprast.hrdpro;

import Sistem.NoUrutDatabase;
import Sistem.RenderingKanan;
import Sistem.RenderingTengah;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListModel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author LANTAI3
 */
public class Sys_UserAdmin extends javax.swing.JPanel {

    /**
     * Creates new form Sys_UserAdmin
     */
   
    
    Sistem.DB_MYSQL KD = new Sistem.DB_MYSQL();
    Connection K = KD.createConnection();
    
    /*
     * Creates new form Sys_UserAdmin 
     * User Maintenance
     */
     DefaultListModel ModelListKiri;
 
    
    /*
     * END
     */
    
    /**
     * Creates new form Sys_UserAdmin 
     * Group Maintenance
     */
    DefaultTableModel TabelModelUserMenu;
    DefaultTableModel TabelModelAllowedModule;
    DefaultTableModel TabelModelUserNameGroups;
    DefaultTableModel TabelModelUserMaintenace;
    //DefaultListModel ModalJlist;
    
    /*
     * Rata tengah atau kiri table
     * Group Maintenance
     */
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah = new RenderingTengah();
    private boolean EditUserGroups = false;
    private boolean ButtonUserEdit = false;
    
    /*
     * End
     */
     public Sys_UserAdmin() {
        initComponents();
    
        
        /*
         * User Maintenance
         */
        
        /*
         * Model JlistKiri
         */
        ModelListKiri = new DefaultListModel();
        JListUserKiri.setModel(ModelListKiri);
        
        /*
         * Ambil data dari database untuk di masukan di JlistKanan
         */
        DataListKanan ();
        
        /*
         * Untuk ambil data dari database ke Tabel JTableUserMaintenance
         */
        final DefaultTableModel ModelUserMaintenace;
        String headerUser[] = {"Full name ", "User ID","Password","Expired", "Status", "Last Login"};
        ModelUserMaintenace = new DefaultTableModel(null, headerUser) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                                //if(colIndex == 4) {return false ;} //  nilai false agar tidak bisa di edit
                                //if(colIndex == 8) {return false ;}
                                //if(colIndex == 9) {return false ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
       
        JTableUserMaintenance.setModel(ModelUserMaintenace);
        
        /*
         * Ukura kolom table tabelallowedmodul
         */
        
        int jarak_colom_user[] = {140,100, 140, 80,80, 80};
        Sistem.Colom_table ukuran_colom_user= new Sistem.Colom_table();
        ukuran_colom_user.ukuran_colom(JTableUserMaintenance, jarak_colom_user);

        /*
         * Rata tengah atau kiri table
         */
         JTableUserMaintenance.getColumnModel().getColumn(3).setCellRenderer( tengah );
         JTableUserMaintenance.getColumnModel().getColumn(4).setCellRenderer( tengah );
         JTableUserMaintenance.getColumnModel().getColumn(5).setCellRenderer( tengah );
        
        /*
         * Disable drag colum tabel
         */
        JTableUserMaintenance.getTableHeader().setReorderingAllowed(false);
        
        /*
         * Isi data dari database di usergrouomaintenace
         */
        KembaliNormalUser(ModelUserMaintenace);
        
        /*
         * Event ketika ToggleONOFF ditekan
         */
        SetToggleOnOff(JToggleButtonExpired);
        
        /*
         * Cek box aktive atau tidak aktive untuk user
         */
        SetHasilCekBox (JCheckBoxUserAktive);
        
        /*
         * Action 
         */
        JTableUserMaintenance.addMouseListener(new MouseAdapter (){
            public void mousePressed (MouseEvent E){                
                int EE = E.getClickCount();
                if (EE > 1){
                    int row = JTableUserMaintenance.getSelectedRow();
                    if (row >= 0 ) {
                        String DataRow = JTableUserMaintenance.getModel().getValueAt(row, 1).toString();
                        try {
                            //DataDetailUser(DataRow);
                            DataDetailUser(row ,DataRow ,ModelListKiri );
                            
                        } catch (NoSuchAlgorithmException ex) {
                            Logger.getLogger(Sys_UserAdmin.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(Sys_UserAdmin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
        JListUserKanan.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mousePressed(MouseEvent e) {
                    int ee = e.getClickCount();
                    if (ee > 1){
                        TambahElementJlistKiri();
                    }
                    else {

                   }
	        }
	    });
        JButtonUserKiri.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent C){
                TambahElementJlistKiri();
            }
        });
        JListUserKiri.addMouseListener(new MouseAdapter (){
            @SuppressWarnings("empty-statement")
            @Override
            public void mousePressed ( MouseEvent A){
                if  (1 < A.getClickCount()){
                    int Data = (int) JListUserKiri.getSelectedIndex();               
                    if ( Data > -1 ){
                         ModelListKiri.remove(Data);                            
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Pilih group admin yang akan di hapus", "Info", JOptionPane.INFORMATION_MESSAGE);
                    };
                }
            }
        });
        JButtonUserKanan.addActionListener(new ActionListener(){
            public void actionPerformed ( ActionEvent Ari){
                int Data = (int) JListUserKiri.getSelectedIndex();               
                if ( Data > -1 ){
                     ModelListKiri.remove(Data);                            
                }
                else {
                    JOptionPane.showMessageDialog(null, "Pilih group admin yang akan di hapus", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        JButtonUserAdd.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent Ari){
                DisableAtauEnableUserMaintenance (true);
                JButtonUserAdd.setEnabled(false);
                JButtonUserSave.setEnabled(true);
                JButtonUserEdit.setEnabled(false);
                JButtonUserDelete.setEnabled(false);
                JTextUserFullName.setText("");
                JTextUserID.setText("");
                JPasswordUser1.setText("");
                JPasswordUser2.setText("");
                
                KazaoUserExpired.setEditable(false);
                KazaoUserExpired.setEnabled(false);
                JCheckBoxUserAktive.setEnabled(true);
                JCheckBoxUserAktive.setSelected(true);
                JTableUserMaintenance.setEnabled(false);
                
            }
        });
        JButtonUserEdit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent Ari){
                DisableAtauEnableUserMaintenance (true);
                ButtonUserEdit = true;
                JButtonUserAdd.setEnabled(false);
                JButtonUserDelete.setEnabled(false);
                JButtonUserSave.setEnabled(true);
                JTextUserID.setEnabled(false);;
                JTableUserMaintenance.setEnabled(false);
                if (JToggleButtonExpired.getText().equals("ON")){
                    KazaoUserExpired.setEnabled(true);
                }
                else {
                    KazaoUserExpired.setEnabled(false);
                }
            }
        });
        JButtonUserDelete.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent Ari){
                JTableUserMaintenance.setEnabled(true);
                int RowUser = JTableUserMaintenance.getSelectedRow();
                if (RowUser >= 0 ) {
                    String DataHapus = (String) JTableUserMaintenance.getValueAt(RowUser, 1);
                    JOptionPane.showMessageDialog(null, DataHapus);
                    //int DataNoGroups = Integer.valueOf((JTabelNameGroups.getValueAt(GetRowJTableUserGroups (), 1).toString())).intValue();
                    int Pilih = JOptionPane.showConfirmDialog(null, "Apakah anda yakin untuk menghapus : " + DataHapus + " ?", " Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if (Pilih == JOptionPane.YES_OPTION){
                            
                            try {
                                Statement S = K.createStatement();
                                S.executeUpdate("DELETE FROM `sys_user` WHERE `username` = '"+ DataHapus +"'");
                                String Da = "DELETE FROM `sys_user` WHERE `userid` = '"+ DataHapus +"'";
                                //SistemPro.koneksiMYSQL Hapus = new SistemPro.koneksiMYSQL();
                                //Hapus.MysqlDelete (Da);
                                KembaliNormalUser(ModelUserMaintenace);
                            }
                            catch (Exception X){
                                JOptionPane.showMessageDialog(null,  "Sys_UserAdmin.java : error : 123327 : "  +X, " Error delete", JOptionPane.ERROR_MESSAGE);
                                X.printStackTrace();
                            } 
                     }
                     else if ( Pilih == JOptionPane.NO_OPTION){

                     }
                }
                else {
                    JOptionPane.showMessageDialog(null,  "Mohon pilih data yang akan di hapus" , "Delete", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        JButtonUserSave.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent Ari){
                //String OnOff = JToggleButtonExpired.getActionCommand();
                //JOptionPane.showMessageDialog(null, ToggleOnOff + " Dan " + GetToggleOnOff());
                JTableUserMaintenance.setEnabled(true);
                String FullName = JTextUserFullName.getText();
                String UserID   = JTextUserID.getText();
                char[] Pass1    = JPasswordUser1.getPassword();
                char[] Pass2    = JPasswordUser2.getPassword();
                
                String DateKazao =  KazaoUserExpired.getKazaoCalendar().getShortDate();
                Sistem.TanggalSistem KazaoToIndo = new Sistem.TanggalSistem();
                KazaoToIndo.SetKazaoToTglIndo(DateKazao);
                String UserTgl  = KazaoToIndo.GetTglIndoStrKazao();
                KazaoToIndo.SetKazaoToBlnIndo(DateKazao);
                String UserBln  = KazaoToIndo.GetBlnIndoStrKazao();
                KazaoToIndo.SetKazaoToThnIndo(DateKazao);
                String UserThn = KazaoToIndo.GetThnIndoStKazao();
                String Tanggal = UserTgl+"-"+UserBln+"-"+UserThn;
                
                if (ButtonUserEdit == true){
                    if (FullName.isEmpty() == false ){
                        if (UserID.isEmpty() == false&& UserID.matches("^[a-zA-Z0-9]{3,50}$")){
                            if (Pass1.equals("") == false){
                                 if (JPasswordUser1.getText().equals(JPasswordUser2.getText())) {
                                     if (JListUserKiri.getLastVisibleIndex() > -1){
                                         try {

                                         /*
                                          * Prosess Enkripsi Data
                                          */
                                         Sistem.SecurityEnkripsiDanDeskripsi Security = new Sistem.SecurityEnkripsiDanDeskripsi();
                                         String DataEnkripsi    = Security.encrypt(JPasswordUser1.getText(), UserID);
                                         int DataList           = JListUserKiri.getLastVisibleIndex();
                                         ListModel  ModelList   = JListUserKiri.getModel();                                      
                                         UpdateDiDatabase (FullName,UserID,DataEnkripsi, Tanggal, GetToggleOnOff() ,ModelList, DataList, HasilCekBoxUserAktive);

                                         KembaliNormalUser(ModelUserMaintenace);

                                         } catch (Exception ex) {
                                             Logger.getLogger(Sys_UserAdmin.class.getName()).log(Level.SEVERE, null, ex);
                                         }
                                     }
                                     else {
                                         JOptionPane.showMessageDialog(null, "List user groups kosong", "Stop", JOptionPane.ERROR_MESSAGE);
                                     }                               
                                 }
                                 else {
                                     JOptionPane.showMessageDialog(null, "Maaf password tidak sama", "Warning", JOptionPane.ERROR_MESSAGE);
                                 }                          
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Password kosong", "Stop", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "User Id kosong, ada spasi atau terdapat karakter aneh", "Stop", JOptionPane.ERROR_MESSAGE);
                        }
                     }
                else {
                    JOptionPane.showMessageDialog(null, "Fullname kosong", "Stop", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                /*
                 * Cek Data kosong atau tidak 
                 */
                else {
                    if (FullName.isEmpty() == false ){
                        if (UserID.isEmpty() == false&& UserID.matches("^[a-zA-Z0-9]{3,50}$")){
                            if (Pass1.equals("") == false){
                                 if (JPasswordUser1.getText().equals(JPasswordUser2.getText())) {
                                     if (JListUserKiri.getLastVisibleIndex() > -1){
                                         try {

                                         /*
                                          * Prosess Enkripsi Data
                                          */
                                         Sistem.SecurityEnkripsiDanDeskripsi Security = new Sistem.SecurityEnkripsiDanDeskripsi();
                                         String DataEnkripsi    = Security.encrypt(JPasswordUser1.getText(), UserID);
                                         int DataList           = JListUserKiri.getLastVisibleIndex();
                                         ListModel  ModelList   = JListUserKiri.getModel();  
                                         SaveDiDatabase (FullName,UserID,DataEnkripsi, Tanggal, GetToggleOnOff() ,ModelList, DataList, HasilCekBoxUserAktive);
                                         KembaliNormalUser(ModelUserMaintenace);
                                         } catch (Exception ex) {
                                             Logger.getLogger(Sys_UserAdmin.class.getName()).log(Level.SEVERE, null, ex);
                                         }
                                     }
                                     else {
                                         JOptionPane.showMessageDialog(null, "List user groups kosong", "Stop", JOptionPane.ERROR_MESSAGE);
                                     }                               
                                 }
                                 else {
                                     JOptionPane.showMessageDialog(null, "Maaf password tidak sama", "Warning", JOptionPane.ERROR_MESSAGE);
                                 }                          
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Password kosong", "Stop", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "User Id kosong, ada spasi atau terdapat karakter aneh", "Stop", JOptionPane.ERROR_MESSAGE);
                        }
                     }
                     else {
                         JOptionPane.showMessageDialog(null, "Fullname kosong", "Stop", JOptionPane.ERROR_MESSAGE);
                     }
                }  
            }
        });
        JButtonUserRefresh.addActionListener(new ActionListener(){
            public void actionPerformed ( ActionEvent a){
                 KembaliNormalUser(ModelUserMaintenace);
            }
        });
         
        /*
         * User Maintenance END
         * Ccoding di bawah adalah untuk program Group Maintenance
         */
        
        /*
         * Untuk Tabel USER NAME GROUPS
         * Group Maintenance
         */
        final DefaultTableModel ModelUserNameGroups;
        String headeruser[] = {"Group Name", "No"};
        ModelUserNameGroups = new DefaultTableModel(null, headeruser) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                                //if(colIndex == 4) {return false ;} //  nilai false agar tidak bisa di edit
                                //if(colIndex == 8) {return false ;}
                                //if(colIndex == 9) {return false ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
       
        JTabelNameGroups.setModel(ModelUserNameGroups);
        
        /*
         * Ukura kolom table tabelallowedmodul
         */
        
        int jarak_colom_group[] = {360,30};
        Sistem.Colom_table ukuran_colom_group = new  Sistem.Colom_table();
        ukuran_colom_group.ukuran_colom(JTabelNameGroups, jarak_colom_group);

        /*
         * Rata tengah atau kiri table
         */
        JTabelNameGroups.getColumnModel().getColumn(1).setCellRenderer( tengah );
        
        /*
         * Disable drag colum tabel
         */
        JTabelNameGroups.getTableHeader().setReorderingAllowed(false);
                
        /*
         **********************************************************************************************************************
           END **************************************************END******************************************************END
         ***********************************************************************************************************************
         */
        
        /*
         * Untuk Tabel allowed module
         */
        final DefaultTableModel ModelAllowedModule;
        String headerallowed[] = {"Menu Name", "Menu ID","Acces Level","Menu By Asc"};
        ModelAllowedModule = new DefaultTableModel(null, headerallowed) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                                //if(colIndex == 4) {return false ;} //  nilai false agar tidak bisa di edit
                                //if(colIndex == 8) {return false ;}
                                //if(colIndex == 9) {return false ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
       
        TabelAllowedModul.setModel(ModelAllowedModule);
        
        /*
         * Ukura kolom table tabelallowedmodul
         */
        
        int jarak_colom_allowed[] = {140,160,60,60};
        Sistem.Colom_table ukuran_colom_allowed = new Sistem.Colom_table();
        ukuran_colom_allowed.ukuran_colom(TabelAllowedModul, jarak_colom_allowed);

        /*
         * Rata tengah atau kiri table
         */
        TabelAllowedModul.getColumnModel().getColumn(2).setCellRenderer( tengah );
       
        //ModifTabel( TabelAllowedModul);
        /*
         * Disable drag colum tabel
         */
        TabelAllowedModul.getTableHeader().setReorderingAllowed(false);
        TabelMenuProgram.getTableHeader().setReorderingAllowed(false);
                
        /*
         **********************************************************************************************************************
           END **************************************************END******************************************************END
         ***********************************************************************************************************************
         */
        

        /*
         * Untuk Tabel not allowed module
         */
        final DefaultTableModel ModalNotAllowedModul;
        String header[] = {"Menu Name", "Menu ID","Menu By Asc"};
        ModalNotAllowedModul = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                                //if(colIndex == 4) {return false ;} //  nilai false agar tidak bisa di edit
                                //if(colIndex == 8) {return false ;}
                                //if(colIndex == 9) {return false ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
        
        /*
         * Memodifikasi Jatabel agar warna warni
         */
       // SistemPro.ModifTable ModifTabel = new SistemPro.ModifTable(ModalNotAllowedModul);
        
        TabelMenuProgram.setModel(ModalNotAllowedModul);
        
       
        /*
         * Ukura table TabelOrderBarang
         */
        
        int jarak_colom[] = {160,160,160};
        Sistem.Colom_table ukuran_colom = new Sistem.Colom_table();
        ukuran_colom.ukuran_colom(TabelMenuProgram, jarak_colom);

        /*
         * Rata tengah atau kiri table
         */
        //TabelMenuProgram.getColumnModel().getColumn(1).setCellRenderer( tengah );

        
        /*
         * Event ketika JTabbedPane GroupMaintenace di change
         * memberikan data pada NotAllowed Module
         */
        JTabbedGroupMaintenance.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent ARI) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) ARI.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                String coba = sourceTabbedPane.getTitleAt(index);
                if (sourceTabbedPane.getTitleAt(index).equals("Group Maintenace") ){
                    
               IsiDataUserGroups (  ModalNotAllowedModul, ModelUserNameGroups );
                    //IsiDataUserGroups (  TabelModelUserMenu, TabelModelUserNameGroups );
                }
                else if (sourceTabbedPane.getTitleAt(index).equals("User Maintenace")){
                    DataListKanan ();
                    KembaliNormalUser(ModelUserMaintenace);
                }
              }
        });
        
        /*
         * Action untuk JCheck box
         */
        SetHasilCekBox(JCheckBoxOpen);
        SetHasilCekBox(JCheckBoxSave);
        SetHasilCekBox(JCheckBoxEdit);
        SetHasilCekBox(JCheckBoxDelete);
        SetHasilCekBox(JCheckBoxPrint); 
        SetHasilCekBox(JCheckBoxNew);    
                        
        /*
         * Set Data ke JTable untuk colum acces level
         */
        JButtonSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent A){
                String Ari = A.getActionCommand();
                if (Ari.equals("Set")){
                    TabelAllowedModul.setValueAt(GetHasilCekBoxSave()+""+
                    GetHasilCekBoxEdit () + "" + GetHasilCekBoxDelete () + "" +
                    GetHasilCekBoxOpen () + "" + GetHasilCekBoxPrint ()+ ""+ GetHasilCekBoxNew () , GetAmbilRowToActionCheckBox(), 2);
                    JOptionPane.showMessageDialog(null, "Finish","Info",JOptionPane.INFORMATION_MESSAGE);
                    for ( int a = 0; a < TabelMenuProgram.getRowCount(); a++){
                    }
                }
            }
        });
        JButtonSetAll.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent Ari){
                String AriPras = Ari.getActionCommand();
                    int Pras = TabelAllowedModul.getRowCount();
                    for (int a = 0; a <= Pras - 1; a ++){
                         TabelAllowedModul.setValueAt(GetHasilCekBoxSave()+""+
                        GetHasilCekBoxEdit () + "" + GetHasilCekBoxDelete () + "" +
                        GetHasilCekBoxOpen () + "" + GetHasilCekBoxPrint () + ""+ GetHasilCekBoxNew () , a, 2);
                    }
                    JOptionPane.showMessageDialog(null, "Finish","Info",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        
        /*
         * Action jika TabelAllowedModul di klik satu kali dan akan mengeset row yang di klik
         * Row disini akan di fungsikan untuk aktifitas checkbox disable atau enable
         */
        TabelAllowedModul.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mousePressed(MouseEvent e) {
                    int ee = e.getClickCount();
	            if (e.getClickCount() == 1) {
                         
                        /*
                         * Untuk set value darii jumlah row yang di klik
                         */
                        int row = TabelAllowedModul.getSelectedRow();
                        SetAmbilRowToActionCheckBox (row);
    
                        /*
                         * Ketika JTable di klik akan mempengaruhi checkbox
                         */
                        String Data = (String) TabelAllowedModul.getValueAt(row, 2);
                        String Data1 = String.valueOf( Data.charAt(0)).toString();
                        String Data2 = String.valueOf( Data.charAt(1)).toString();
                        String Data3 = String.valueOf( Data.charAt(2)).toString();
                        String Data4 = String.valueOf( Data.charAt(3)).toString();
                        String Data5 = String.valueOf( Data.charAt(4)).toString();
                        String Data6 = String.valueOf( Data.charAt(5)).toString();
                        
                        switch (Data1) {
                            case "1":
                                JCheckBoxSave.setSelected(true);
                                break;
                            case "0":
                                JCheckBoxSave.setSelected(false);
                                break;
                        }
                        switch (Data2) {
                            case "1":
                                JCheckBoxEdit.setSelected(true);
                                break;
                            case "0":
                                JCheckBoxEdit.setSelected(false);
                                break;
                        }
                        switch (Data3) {
                            case "1":
                                JCheckBoxDelete.setSelected(true);
                                break;
                            case "0":
                                JCheckBoxDelete.setSelected(false);
                                break;
                        }
                        switch (Data4) {
                            case "1":
                                JCheckBoxOpen.setSelected(true);
                                break;
                            case "0":
                                JCheckBoxOpen.setSelected(false);
                                break;
                        }
                        switch (Data5) {
                            case "1":
                                JCheckBoxPrint.setSelected(true);
                                break;
                            case "0":
                                JCheckBoxPrint.setSelected(false);
                                break;
                        }
                        switch (Data6) {
                            case "1":
                                JCheckBoxNew.setSelected(true);
                                break;
                            case "0":
                                JCheckBoxNew.setSelected(false);
                                break;
                        }
                    }
            }
        });
        
        /*
         * Action ketika tabel not allowed module pada row di double klik untuk di pindah ke allowed module
         */
        JTabelNameGroups.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mousePressed(MouseEvent e) {
                    int ee = e.getClickCount();
                    if (GetDisableKlikTabelUserGroup() == 100){
                        JOptionPane.showMessageDialog(null, "Anda dalam mode edit", "Warning" , JOptionPane.ERROR_MESSAGE);
                        
                    }
                    else {
                        if (e.getClickCount() >= 1) {
                            IsiAllowedModul (JTabelNameGroups,TabelAllowedModul, ModelAllowedModule  );
                            SetRowJTabelNameGroups(JTabelNameGroups.getSelectedRow());
                             
                      } 
                   }
	        }
	    });
        
        /*
         * Action ketika tabel not allowed module pada row di double klik untuk di pindah ke allowed module
         */
        TabelMenuProgram.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mousePressed(MouseEvent e) {
                    int ee = e.getClickCount();
	            if (e.getClickCount() > 1) {
                        try {
                        int row = TabelMenuProgram.getSelectedRow();

                        /*
                         * Clear focus di TabelAllowedModul
                         * Focus di TabelMenuProgram
                         */
                        TabelAllowedModul.clearSelection();
                        TabelMenuProgram.requestFocus();

                        /*
                         * Cek box untuk print, delete, open, edit penganturan
                         */

                        /*
                         JCheckBoxOpen.addActionListener(new ActionListener (){
                         public void actionPerformed(ActionEvent actionEvent) {
                        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
                        boolean selected = abstractButton.getModel().isSelected();
                        //System.out.println(selected);
                        // abstractButton.setText(newLabel);
                      }

                        }); */
                        //JCheckBoxOpen.addItemListener(null)
                        String SelectMenuByAsc  =(TabelMenuProgram.getModel().getValueAt(row, 2)).toString();
                        String SelectMenuID     =(TabelMenuProgram.getModel().getValueAt(row, 1)).toString();
                        String SelectMenuName   =(TabelMenuProgram.getModel().getValueAt(row, 0)).toString();
                        String[] add            = {SelectMenuName,SelectMenuID, "111111", SelectMenuByAsc};

                        /*
                         * Cek data di table
                         */
                        CekDataTabel(SelectMenuName,TabelAllowedModul );

                        if (CekTabel == true){
                            ModelAllowedModule.addRow(add);
                        }
                        else {
                            SearchDanSelectAntarJTable(TabelAllowedModul, SelectMenuName);

                        }

                        /*
                         * 
                         * Coding buat filter di JTable
                         * 
                        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(TabelModelAllowedModule);
                         TabelAllowedModul.setRowSorter(sorter);
                        if (SelectMenuName.length() == 0) {
                          sorter.setRowFilter(null);
                        } else {
                          try {
                                //TableStringConverter aaaa = sorter.
                                //System.out.println(aaaa + "  ");
                            sorter.setRowFilter(
                                RowFilter.regexFilter("(?i)"+SelectMenuName));

                            //System.out.println(sorter.getViewRowCount());
                          } catch (PatternSyntaxException pse) {
                            System.err.println("Bad regex pattern");
                          }
                        }
                        */

                        /*
                         * Logika select per  1 colom dan 1 row saja
                         *
                        TabelAllowedModul.setColumnSelectionAllowed(false);
                        TabelAllowedModul.setRowSelectionAllowed(true);

                        TabelAllowedModul.setCellSelectionEnabled(true);
                        /*
                         * 
                         */

                    //TabelAllowedModul.changeSelection(1, 1, false, false);
                    //TabelAllowedModul.requestFocus();
                    /*
                     * 
                     * Coding buat selecttion
                    TabelAllowedModul.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    TabelAllowedModul.setColumnSelectionAllowed(true);
                    TabelAllowedModul.setRowSelectionAllowed(true);

                    int rows = 2;
                    int col = 1;
                    boolean toggle = false;
                    boolean extend = false;
                    TabelAllowedModul.changeSelection(rows, col, toggle, extend);

                    */
	            }
                        catch (Exception X){
                        JOptionPane.showMessageDialog(null, "Anda harus masuk ke dalam mode Add atau Edit", "INFO", JOptionPane.INFORMATION_MESSAGE);
                    }
                  } 
	        }
	    });
        TabelAllowedModul.addMouseListener(new MouseAdapter() {
            @Override
	        public void mousePressed(MouseEvent e) {
                    int ee = e.getClickCount();
	            if (e.getClickCount() > 1) {
                        int Row = GetAmbilRowToActionCheckBox();
                    try {
                        ModelAllowedModule.removeRow(Row);
                    }
                    catch (Exception X){
                        JOptionPane.showMessageDialog(null, "Anda harus masuk ke dalam mode Add atau Edit", "INFO", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
         });
        JButtonRefreshAdm.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent CC){
                String ARI = CC.getActionCommand();
                if(ARI.equals("Refresh")){
                 IsiDataUserGroups (  ModalNotAllowedModul, ModelUserNameGroups );
                  //JOptionPane.showMessageDialog(null, "Refesh sudah ", "INFO", JOptionPane.INFORMATION_MESSAGE);
                }
            }});
        JButtonKeKiri.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent CC){
                String ARI = CC.getActionCommand();
                if(ARI.equals("<")){
                     int row = TabelMenuProgram.getSelectedRow();
                     try {
                        /*
                         * Clear focus di TabelAllowedModul
                         * Focus di TabelMenuProgram
                         */
                        TabelAllowedModul.clearSelection();
                        TabelMenuProgram.requestFocus();

                        String SelectMenuByAsc  =(TabelMenuProgram.getModel().getValueAt(row, 2)).toString();
                        String SelectMenuID     =(TabelMenuProgram.getModel().getValueAt(row, 1)).toString();
                        String SelectMenuName   =(TabelMenuProgram.getModel().getValueAt(row, 0)).toString();
                        String[] add            = {SelectMenuName,SelectMenuID, "11111", SelectMenuByAsc};

                        /*
                        * Cek data di table
                        */
                        CekDataTabel(SelectMenuName,TabelAllowedModul );

                        if (CekTabel == true){
                           ModelAllowedModule.addRow(add);
                        }
                        else {
                           SearchDanSelectAntarJTable(TabelAllowedModul, SelectMenuName);
                        }
                     }
                     catch ( Exception XC){
                          JOptionPane.showMessageDialog(null, "Select menu yang akan di dihilangkan", "INFO", JOptionPane.INFORMATION_MESSAGE);
                     }
                     
                }
            }
        });
        
        JButtonKeKanan.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent XC){
                String DT = XC.getActionCommand();
                if (DT.equals(">")){
                    int Row = GetAmbilRowToActionCheckBox();
                    try {
                        ModelAllowedModule.removeRow(Row);
                    }
                    catch (Exception X){
                        JOptionPane.showMessageDialog(null, "Select menu yang akan di dihilangkan", "INFO", JOptionPane.INFORMATION_MESSAGE);
                    }             
                }
            }
        });
        
        /*
         * Tombol save edit delete copy add untuk add user groups
         */
        JButtonAddAdm.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent Ari ){
                    EnableButtonUserGroups(true);
                    JikaDiKlikAddAtauEdit();
                    
                    /*
                    * Logika hapus semua data di jtable
                    */
                    DefaultTableModel dtm = (DefaultTableModel) TabelAllowedModul.getModel();
                    dtm.setRowCount(0);
                    JTextNameUserGroup.setText("");
                    EditUserGroups = false;
            }
        });
        
        JButtonEditAdm.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = JTabelNameGroups.getSelectedRow();
                if (row > -1 ){
                    //JTextNameUserGroup.setEnabled(true);
                    EnableButtonUserGroups(true);
                    JikaDiKlikAddAtauEdit();
                    EditUserGroups = true;
                    /*
                     * 100 set diable klik JTabelUserGroup
                     * 1 set enable klik
                     */
                    SetDisableKlikTabelUserGroup(100);
                }
                //IsiDataUserGroups (  TabelModelUserMenu, TabelModelUserNameGroups );
            }
        });
        
        JButtonDeleteAdm.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent Ari ){
                int RowData = GetRowJTableUserGroups ();
                int DataNoGroups = Integer.valueOf((JTabelNameGroups.getValueAt(GetRowJTableUserGroups (), 1).toString())).intValue();
                int Pilih = JOptionPane.showConfirmDialog(null, "Apakah anda yakin untuk menghapus user group no : " + DataNoGroups + " " +JTextNameUserGroup.getText() +" ?", " Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (Pilih == JOptionPane.YES_OPTION){
                        
                        try {
                            Statement S = K.createStatement();
                            S.executeUpdate("DELETE FROM `sys_groups` WHERE `groupno` = '"+ DataNoGroups +"'");
                            IsiDataUserGroups (  ModalNotAllowedModul, ModelUserNameGroups );
                        }
                        catch (Exception X){
                            JOptionPane.showMessageDialog(null,  "Sys_UserAdmin.java : error : 1227 : "  +X, " Error delete", JOptionPane.ERROR_MESSAGE);
                            X.printStackTrace();
                        } 
                 }
                 else if ( Pilih == JOptionPane.NO_OPTION){
                     
                 }
            }
        });
        JButtonSaveAdm.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent Ari ){
                    
                   /*
                    * 100 set diable klik JTabelUserGroup
                    * 1 set enable klik
                    */
                    SetDisableKlikTabelUserGroup(1);
                        
                    JTabelNameGroups.setEnabled(true);
                    JTabelNameGroups.clearSelection();
                    
                    EnableButtonUserGroups(false);
                    DisableButtonUserGroup();
                    NoUrutDatabase NoUrut = new NoUrutDatabase();
                    NoUrut.SetNoUrutDB("sys_groups", "groupno", "desc");
                    int NoUrutNya = NoUrut.GetNoUrutDB();
                    String NameUserGroups = JTextNameUserGroup.getText();
                    int dd = JTabelNameGroups.getSelectedRow();
                    //JOptionPane.showMessageDialog(null,  " Update "  + dd, " Info", JOptionPane.INFORMATION_MESSAGE);
                    if (NameUserGroups.isEmpty()){
                        JOptionPane.showMessageDialog(null,  " Name user groups kosong " , " Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        if ( EditUserGroups == true){
                            
                         /*
                          * Save headerpo ke database MYSQL jika tombol edit di pencet
                          */
                       
                         try {
                            int Row         = GetRowJTabelNameGroups();
                            int NoUpdateRow = Integer.valueOf(JTabelNameGroups.getValueAt(Row, 1).toString()).intValue();
                            JOptionPane.showMessageDialog(null," Update colum number : "+ NoUpdateRow);
                            Statement Stm       = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            Stm.executeUpdate("update sys_groups set groupname = '" + JTextNameUserGroup.getText() +"' where groupno='"+NoUpdateRow+"'");                  

                          /*
                           * Save DetailPo to database Mysql
                           */
                             try {
                                 int a              = TabelAllowedModul.getRowCount() ;
                                 Statement stm      = K.createStatement();
                                 
                                 stm.executeUpdate("delete from sys_groupsprogram where groupno = '"+NoUpdateRow+"'"); 
                                 Statement stm2      = K.createStatement();
                                 for(int i=0;i< a;i++){

                                    int GroupNo        = NoUrutNya;
                                    String MenuId      = TabelAllowedModul.getValueAt(i, 1).toString();
                                    String MenuName    = TabelAllowedModul.getValueAt(i, 0).toString();
                                    String MenuByAsc   = TabelAllowedModul.getValueAt(i, 3).toString();
                                    String AccesLevel  = TabelAllowedModul.getValueAt(i, 2).toString();

                                    MenuId      = MenuId.replaceAll("[\\s]", "");
                                    MenuName    = MenuName.replaceAll("^([\\s])+", "");
                                    stm2.executeUpdate("INSERT INTO sys_groupsprogram (groupno, menuid, menuname, menubyasc, acceslevel) VALUES ('"
                                            +NoUpdateRow+"','"+MenuId+"','"+MenuName+"','"+MenuByAsc+"','"+AccesLevel+"')");
                                    }
                                IsiDataUserGroups (  ModalNotAllowedModul, ModelUserNameGroups );
                             }
                             catch (Exception X){
                                  JOptionPane.showMessageDialog(null,  "sys_administrator : error : 12263 : "  +X, " Error", JOptionPane.ERROR_MESSAGE);
                                }           
                             }
                             catch (SQLException | HeadlessException Ex){
                             JOptionPane.showMessageDialog(null,  "sys_administrator : error : 12243 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                            } 
                        }
                        else {
                            
                        /*
                         * Save headerpo ke database MYSQL
                         */

                       
                        //JOptionPane.showMessageDialog(null,  JTextGrandTotPo.getText() + " dan " + supplier + " dan " + supplier_address);
                        //ResultSet HQ =null;
                        try {
                           Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                           Stm.executeUpdate("INSERT INTO sys_groups (groupno, groupname) VALUES('"+ NoUrutNya +"','" + JTextNameUserGroup.getText() +"')");                  

                         /*
                          * Save DetailPo to database Mysql
                          */
                            try {
                                int a = TabelAllowedModul.getRowCount() ;
                                Statement stm = K.createStatement();

                                // dbStatement=con.createStatement();

                                for(int i=0;i< a;i++){

                                    int GroupNo        = NoUrutNya;
                                    String MenuId      = TabelAllowedModul.getValueAt(i, 1).toString();
                                    String MenuName    = TabelAllowedModul.getValueAt(i, 0).toString();
                                    String MenuByAsc   = TabelAllowedModul.getValueAt(i, 3).toString();
                                    String AccesLevel  = TabelAllowedModul.getValueAt(i, 2).toString();

                                    MenuId      = MenuId.replaceAll("[\\s]", "");
                                    MenuName    = MenuName.replaceAll("^([\\s])+", "");
                                    stm.executeUpdate("INSERT INTO sys_groupsprogram (groupno, menuid, menuname, menubyasc, acceslevel) VALUES ('"
                                            +NoUrutNya+"','"+MenuId+"','"+MenuName+"','"+MenuByAsc+"','"+AccesLevel+"')");
                                    }
                                IsiDataUserGroups (  ModalNotAllowedModul, ModelUserNameGroups );
                            }
                            catch (Exception X){
                                 JOptionPane.showMessageDialog(null,  "sys_administrator : error : 1226 : "  +X, " Error", JOptionPane.ERROR_MESSAGE);
                               }           
                            }
                            catch (SQLException | HeadlessException Ex){
                            JOptionPane.showMessageDialog(null,  "sys_administrator : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                           }                           
                        }    
                    }
            } 
        });
        
    /*
     * Group Maintenance END
     */
    }
    
    /*
     * untuk menampilkan data ke tabel allowedmodul
     */
    
    private void IsiAllowedModul (JTable JTabelNameGroups, JTable TabelAllowedModul, DefaultTableModel TabelModelAllowedModule  ){
                        
        int row = JTabelNameGroups.getSelectedRow();
        SetRowJTableUserGroups (row);
        /*
         * Hapus JTabel
         */
        DefaultTableModel dtm = (DefaultTableModel) TabelAllowedModul.getModel();
        dtm.setRowCount(0);

        /*
         * Clear focus di TabelAllowedModul
         * Focus di TabelMenuProgram
         */
        //JTabelNameGroups.clearSelection();
        //TabelMenuProgram.requestFocus();

        String SelectUserGroup  =(JTabelNameGroups.getModel().getValueAt(row, 0)).toString();
        JTextNameUserGroup.setText(SelectUserGroup);
        int SelectNoGroup  = Integer.valueOf(JTabelNameGroups.getModel().getValueAt(row, 1).toString()).intValue();

        /*
        * Ambil data dari database ke Jtabel
        */
        
        
        //DefaultTableModel model = null;
        int baris;

        ResultSet HQ = null;
        try {
            /*
             * Load datababase user groups ke tabelallowedmodul
             */
            Statement stmm = K.createStatement();
            HQ = stmm.executeQuery("SELECT * from sys_groupsprogram where groupno = "+SelectNoGroup+" order by menubyasc asc");
            baris = HQ.getRow();
            //hasilquery.refreshRow();
            while(HQ.next()  ){
                //int DBMenuNoInt      = HQ.getInt("menuno");
                //String DBMenuNo      = String.valueOf(DBMenuNoInt).toString();
                String MenuId       = HQ.getString("menuid");
                String MenuName     = HQ.getString("menuname");
                String MenuByAsc    = HQ.getString("menubyasc");
                String AccesLevel   = HQ.getString("acceslevel");

                if (MenuByAsc.length() == 1){
                String[] add        = {MenuName, MenuId, AccesLevel, MenuByAsc};
                TabelModelAllowedModule.addRow(add);  
                    }
                else if (MenuByAsc.length() == 2){
                String[] add        = {"       "+MenuName,"       "+ MenuId, AccesLevel, MenuByAsc};
                TabelModelAllowedModule.addRow(add) ;
                     }
                else if (MenuByAsc.length() == 3){
                String[] add        = {"               "+MenuName, "              "+MenuId, AccesLevel, MenuByAsc};
                TabelModelAllowedModule.addRow(add);
                    }
                else if (MenuByAsc.length() == 4){
                String[] add        = {"                               "+MenuName, "                              "+MenuId, AccesLevel, MenuByAsc};
                TabelModelAllowedModule.addRow(add );
                    }

                 //model.fireTableDataChanged();
                //negara.addElement(nama_negara);
            }
        }
          catch (Exception ex){
           JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
           ex.printStackTrace();
          }
    }
    
    /*
     * Untuk refresh data dan load database isi di jtable usergroups dan jtable menu program
     */
    private void IsiDataUserGroups ( DefaultTableModel TabelModelUserMenu, DefaultTableModel TabelModelUserNameGroups ){
           
        /*
         * 100 set diable klik JTabelUserGroup
         * 1 set enable klik
         */
         SetDisableKlikTabelUserGroup(1);

         /*
          * Hapus JTabel
          */
         DefaultTableModel dtm = (DefaultTableModel) TabelAllowedModul.getModel();
         dtm.setRowCount(0);
         DefaultTableModel dtm2 = (DefaultTableModel) TabelMenuProgram.getModel();
         dtm2.setRowCount(0);
         DefaultTableModel dtm3 = (DefaultTableModel) JTabelNameGroups.getModel();
         dtm3.setRowCount(0);
         JTextNameUserGroup.setText("");

         /*
          * Disable enable program
          */
         EnableButtonUserGroups(false);
         DisableButtonUserGroup();
         JTabelNameGroups.setEnabled(true);

         /*
         * Ambil data dari database ke Jtabel
         */
         
         
         //DefaultTableModel model = null;
         int baris1;
         int baris;

         ResultSet HQ = null;

           try {
               Statement stm = K.createStatement();

                //insert  into menginlut ke database
                //  stm.executeUpdate("in kotak_pesan (no_id, no, Pengirim,Subject,Pesan,Hostname) VALUES"
                //        + "('9','9','"+ database +"','coba','coba','ari')");

              HQ = stm.executeQuery("SELECT  menuid, menuname, menubyasc from sys_program order by menubyasc asc");

                //stm.close();
               //hasilqueryambilno  = stm2.executeQuery("SELECT no ROM kotak_pesan "
                //       + "ORDER BY no_id DESC LIMIT 1");
                // String noo = hasilqueryambilno.getString("no");
                //   System.out.print(noo + " ini");

               //hasilquery.refreshRow();
               baris1 = HQ.getRow();
               //hasilquery.refreshRow();
               while(HQ.next()  ){
                   //int DBMenuNoInt      = HQ.getInt("menuno");
                   //String DBMenuNo      = String.valueOf(DBMenuNoInt).toString();

                   String DBMenuByASC   = HQ.getString("menubyasc");
                   String DBMenuId      = HQ.getString("menuid");
                   String DBMenuName    = HQ.getString("menuname");
                   
                   if (DBMenuByASC.length() == 1){
                     String[] add         = {DBMenuName, DBMenuId ,DBMenuByASC};
                     TabelModelUserMenu.addRow(add);  
                   }
                   else if (DBMenuByASC.length() == 2){
                     String[] add         = {"       " + DBMenuName, "       "+DBMenuId ,DBMenuByASC};
                     TabelModelUserMenu.addRow(add);
                   }
                   else if (DBMenuByASC.length() == 3){
                     String[] add         = {"               " + DBMenuName, "               "+DBMenuId ,DBMenuByASC};
                     TabelModelUserMenu.addRow(add);  
                   }
                    else if (DBMenuByASC.length() == 4){
                     String[] add         = {"                               " + DBMenuName, "                               "+DBMenuId ,DBMenuByASC};
                     TabelModelUserMenu.addRow(add);  
                   }
                   //String[] add         = {DBMenuName, DBMenuId,DBMenuByASC};
                   //TabelModelUserMenu.addRow(add);      
                    //model.fireTableDataChanged();
                   //negara.addElement(nama_negara);
               }

                /*
                 * Load datababase user groups
                 */
               Statement stmm = K.createStatement();
               HQ = stmm.executeQuery("SELECT * from sys_groups order by groupname asc");
               baris = HQ.getRow();
               //hasilquery.refreshRow();
               while(HQ.next()  ){
                   //int DBMenuNoInt      = HQ.getInt("menuno");
                   //String DBMenuNo      = String.valueOf(DBMenuNoInt).toString();
                   int GroupNo   = HQ.getInt("groupno");
                   String GroupName = HQ.getString("groupname");
                   String[] add     = {GroupName, String.valueOf(GroupNo).toString()};
                   TabelModelUserNameGroups.addRow(add);      
                    //model.fireTableDataChanged();
                   //negara.addElement(nama_negara);
               }
               int TotValue = baris1 + baris;
               //activity = new Sys_UserAdmin.SimulatedActivity(100);
               //activity.process(null,TotValue);
              //  activity.execute();
           }
           catch (Exception ex){
            JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
           }
    }
    
    /*
     * Enable tombol user groups
     */
    private void JikaDiKlikAddAtauEdit(){
        JButtonAddAdm.setEnabled(false);
        JButtonDeleteAdm.setEnabled(false);
        JButtonGroupsAdm.setEnabled(true);
        JButtonSaveAdm.setEnabled(true);
        JButtonEditAdm.setEnabled(false);
        TabelAllowedModul.setEnabled(true);
        TabelMenuProgram.setEnabled(true);
        JTabelNameGroups.setEnabled(false);
        JTabelNameGroups.clearSelection();
    }
    private void EnableButtonUserGroups(boolean BenarSalah){
        //boolean BenarSalah = true;
        JButtonKeKanan.setEnabled(BenarSalah);
        JButtonKeKiri.setEnabled(BenarSalah);
        TabelMenuProgram.setEnabled(BenarSalah);
        JTextNameUserGroup.setEnabled(BenarSalah);
        JButtonSet.setEnabled(BenarSalah);
        JButtonSetAll.setEnabled(BenarSalah);
        JCheckBoxOpen.setEnabled(BenarSalah);
        JCheckBoxEdit.setEnabled(BenarSalah);
        JCheckBoxDelete.setEnabled(BenarSalah);
        JCheckBoxPrint.setEnabled(BenarSalah);
        JCheckBoxSave.setEnabled(BenarSalah);
        JCheckBoxNew.setEnabled(BenarSalah);
        
    }
    private void DisableButtonUserGroup(){
        JButtonAddAdm.setEnabled(true);
        JButtonDeleteAdm.setEnabled(true);
        JButtonGroupsAdm.setEnabled(false);
        JButtonSaveAdm.setEnabled(false);
        JButtonEditAdm.setEnabled(true);
        JCheckBoxEdit.setSelected(false);
        JCheckBoxDelete.setSelected(false);
        JCheckBoxPrint.setSelected(false);
        JCheckBoxOpen.setSelected(false);
        JCheckBoxSave.setSelected(false);
        JCheckBoxNew.setSelected(false);
        TabelAllowedModul.setEnabled(false);
        TabelMenuProgram.setEnabled(false);
    }
    
    /*
     * Logika agar ketika di klik edit pada tabelallowedmodul,
     * Saat JTtabelUserGroups menerima klik atau sebuah kejarian
     * Maka tidak akan menghapus tabelallowedmodul karena select disable pada JtableUserGroups
     */
    int DisableKlikTabelUserGroup;
    private void SetDisableKlikTabelUserGroup (int Data){
        this.DisableKlikTabelUserGroup = Data;
    }
    private int GetDisableKlikTabelUserGroup(){
        return DisableKlikTabelUserGroup;
    }
    
    /*
     * Ambil data row dari JTableUserGrups untuk di ambil no
     */
    int RowJTableUserGroups;
    private void SetRowJTableUserGroups (int DataRow){
        this.RowJTableUserGroups = DataRow;
    }
    private int GetRowJTableUserGroups (){
        return RowJTableUserGroups; 
    }
    
    /*
     * Mengambil row dari JtableAlowwedModul
     */
    int JTableRow;
    private void SetAmbilRowToActionCheckBox (int DataRow){
        this.JTableRow = DataRow;
    }
    private int GetAmbilRowToActionCheckBox(){
        return JTableRow;
    }
    
   /*
    * Logika untuk mengambil row DARI JTabelNameGroups
    */
    int RowJTabelNameGroups;
    private void SetRowJTabelNameGroups(int DataRow){
        this.RowJTabelNameGroups = DataRow;
    }
    private int GetRowJTabelNameGroups(){
        return RowJTabelNameGroups;
    }
    /*
     * Void untuk mengambil nilai dari check box
     * tercentang tau tidak, kalau tercentang bernilai 1
     */
    int HasilCekBoxSave;
    int HasilCekBoxEdit;
    int HasilCekBoxDelete;
    int HasilCekBoxOpen;
    int HasilCekBoxPrint,  HasilCekBoxNew;
    int HasilCekBoxUserAktive = 1;
    
    private void SetHasilCekBox (JCheckBox Box){
        Box.addItemListener(new ItemListener (){
            @Override
            public void  itemStateChanged(ItemEvent itemEvent) {
                    AbstractButton abstractButton = (AbstractButton) itemEvent.getSource();
                    boolean selected = abstractButton.getModel().isSelected();
                    //System.out.println(selected);
                    String AriData = abstractButton.getActionCommand();
                    if ( AriData.equals("Save")){
                        if ( selected == true){
                            HasilCekBoxSave = 1;
                        }
                        else if (selected == false) {
                            HasilCekBoxSave = 0;
                        }
                    }
                    if ( AriData.equals("Edit")){
                        if ( selected == true){
                            HasilCekBoxEdit = 1;
                        }
                        else if (selected == false) {
                            HasilCekBoxEdit = 0;
                        }
                    }
                    if ( AriData.equals("Del")){
                        if ( selected == true){
                            HasilCekBoxDelete = 1;
                        }
                        else if (selected == false) {
                            HasilCekBoxDelete = 0;
                        }
                    }
                    if ( AriData.equals("Print")){
                        if ( selected == true){
                            HasilCekBoxPrint = 1;
                        }
                        else if (selected == false) {
                            HasilCekBoxPrint = 0;
                        }
                    }
                    if ( AriData.equals("Open")){
                        if ( selected == true){
                            HasilCekBoxOpen = 1;
                        }
                        else if (selected == false) {
                            HasilCekBoxOpen = 0;
                        }
                    }
                    
                    if ( AriData.equals("New")){
                        if ( selected == true){
                            HasilCekBoxNew = 1;
                        }
                        else if (selected == false) {
                            HasilCekBoxNew = 0;
                        }
                    }
                    
                   
                    /*
                     * CheckBox untuk user maintenace
                     */
                    if ( AriData.equals("Aktive")){
                        if ( selected == true){
                            HasilCekBoxUserAktive = 1;
                        }
                        else if (selected == false) {
                            HasilCekBoxUserAktive = 0;
                        }
                    }
                }
          });
    }

    int GetHasilCekBoxSave (){
        return HasilCekBoxSave;
    }
    int GetHasilCekBoxEdit (){
        return HasilCekBoxEdit;
    }
    int GetHasilCekBoxDelete (){
        return HasilCekBoxDelete;
    }
    int GetHasilCekBoxOpen (){
        return HasilCekBoxOpen;
    }
    int GetHasilCekBoxPrint (){
        return HasilCekBoxPrint;
    }
    int GetHasilCekBoxNew (){
        return HasilCekBoxNew;
    }

/*
 * Logika jika tabel 1 di klik maka tabel lainnya ikut ketahuan item apa yg di pilh
 */
    private void SearchDanSelectAntarJTable(JTable Table, String Target)
        {
            //String target = textField.getText();
            for(int row = 0; row < Table.getRowCount(); row++) {
                for(int col = 0; col < Table.getColumnCount(); col++)
                    {
                    String next = (String)Table.getValueAt(row, col);
                    if(next.equals(Target))
                        {
                        TabelAllowedModul.changeSelection(row, col, false, false);
                        TabelAllowedModul.requestFocus();
                        return;
                        }
                }
            }
    }
    
    /*
     * Logika JProgressBar
     */
    private Sys_UserAdmin.SimulatedActivity activity;
    class SimulatedActivity extends SwingWorker<Void, Integer>
   {
      /**
       * Constructs the simulated activity that increments a counter from 0 to a
       * given target.
       * @param t the target value of the counter.
       */
      public SimulatedActivity(int t)
      {
         current = 0;
         target = t;
      }

      protected Void doInBackground() throws Exception
      {
         try
         {
            while (current < target)
            {
               Thread.sleep(100);
               current++;
               publish(current);
            }
         }
         catch (InterruptedException e)
         {
         }
         return null;
      }

      protected void process(List<Integer> chunks )
      {
         for (Integer chunk : chunks)
         {
            //textArea.append(chunk + "\n");
            //JProgressBarAdm.setValue(chunk);
         }
      }
      
      protected void done()
      {
          
      }
      
      private int current;
      private int target;
   }
  
/*
 * Cek data dan search data di Table
 */
   boolean  CekTabel = false;
   void CekDataTabel (String DataDiPilih, JTable DataTabelterPilih){
       
            int a = DataTabelterPilih.getRowCount() -1 ;
            int b = DataTabelterPilih.getColumnCount()-1;
            String[][] DataTabelArrayDimensional;
            DataTabelArrayDimensional = new String[a+1][b+1];
            /*
             * Looping untuk mencari apakah ada item atau tidak di tabel dari data yang di input
             */
            if ( a == -1){
                this.CekTabel = true;
            }
            else {
                    for (int aa = 0; aa <= a; aa++){

                     /*
                      * Data tabel di ambil dari int row ke aa colom ke 0
                      */
                    DataTabelArrayDimensional[aa][0]= (String) DataTabelterPilih.getValueAt(aa, 0);          
                    if (DataDiPilih.matches( DataTabelArrayDimensional[aa][0]) ){
                         this.CekTabel = false;
                         break;
                        }
                    else
                    {
                         this.CekTabel = true;
                    }
                }
            }
   }
   
   /*
    * User Maintenance
    * 
    */
   private void DataDetailUser(int Row , String DataSelect ,DefaultListModel ListModel ) throws NoSuchAlgorithmException, Exception{
       String FullName  = (String) JTableUserMaintenance.getModel().getValueAt(Row, 0);
       String USerID    = (String) JTableUserMaintenance.getModel().getValueAt(Row, 1);
       String Pass      = (String) JTableUserMaintenance.getModel().getValueAt(Row, 2);
       String Expired   = (String) JTableUserMaintenance.getModel().getValueAt(Row, 3);
       String Status    = (String) JTableUserMaintenance.getModel().getValueAt(Row, 4);
       
       /* 
        * Deskripsi Password
        */
        Sistem.SecurityEnkripsiDanDeskripsi Security = new Sistem.SecurityEnkripsiDanDeskripsi();

        int DataList          = JListUserKiri.getLastVisibleIndex();
        ListModel  ModelList  = JListUserKiri.getModel();
        String D    = Security.decrypt(Pass,USerID);
        
        /*
         * Hapus JlistKiri terlebhih dahulu
         */
        ModelListKiri.removeAllElements();
        
        /*
         * Ambil data database untuk list kanan
         */
        
        ResultSet HQ = null;
        try {
            Statement stmm = K.createStatement();
            //"Full name ", "User ID","Password","Expired", "Status", "Last Login"
            HQ = stmm.executeQuery("SELECT username, usergroups  from sys_usergroups where username = '"+ USerID  + "' order by username asc");
            while(HQ.next()  ){               
                          String DBUserGroups     = HQ.getString("usergroups");
                          ModelListKiri.addElement(DBUserGroups);
     
              }
        }
          catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (67)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
          }
        
        /*
         * Set tanggal di database to kazao
         */
        Sistem.TanggalSistem TglNow = new Sistem.TanggalSistem();       
        Calendar Tgl = Calendar.getInstance();
        
        Tgl.set(Calendar.YEAR, TglNow.GetThnNow());
        Tgl.set(Calendar.MONTH, TglNow.GetBlnNow()); // di java range bulan 0..11
        Tgl.set(Calendar.DATE, TglNow.GetTglNow());
        
        Expired     =Expired.replaceAll("\\s", "");
        if (Expired.matches("-")){      
            JToggleButtonExpired.setSelected(true);
            KazaoUserExpired.setCalendar(Tgl);
            KazaoUserExpired.setEnabled(false);
            JToggleButtonExpired.setText("OFF");
            JToggleButtonExpired.setSelected(false);
        }
        else {
            Tgl.set(Calendar.YEAR, TglNow.ConvertTglBlnThnToTahun(Expired));
            Tgl.set(Calendar.MONTH, TglNow.ConvertTglBlnThnToBulan(Expired)); // di java range bulan 0..11
            Tgl.set(Calendar.DATE, TglNow.ConvertTglBlnThnToTanggal(Expired));
            KazaoUserExpired.setCalendar(Tgl); 
            JToggleButtonExpired.setText("ON");
            JToggleButtonExpired.setSelected(true);
            if (ButtonUserEdit == true){
                KazaoUserExpired.setEnabled(true);
            }
            else{
                KazaoUserExpired.setEnabled(false);
            }            
        }
        if (Status.equals("Aktive")){
            JCheckBoxUserAktive.setSelected(true);
        }
        else{
            JCheckBoxUserAktive.setSelected(false);
        }
        JTextUserFullName.setText(FullName);
        JTextUserID.setText(USerID);
        JPasswordUser1.setText(D);
        JPasswordUser2.setText(D);
   }
  
   private void DataUserMaintenance (DefaultTableModel Model){
       
   /*
    * Hapus JTabel
    */
    Model.setRowCount(0);
  
    ResultSet HQ = null;
    try {
        Statement stmm = K.createStatement();
        //"Full name ", "User ID","Password","Expired", "Status", "Last Login"
        HQ = stmm.executeQuery("SELECT userid, username, password, expired , status , lastlogin  from sys_user order by username asc");
        
        while(HQ.next()  ){
                      String DBUserID       = HQ.getString("userid");
                      String DBUserName     = HQ.getString("username");
                      String DBPass         = HQ.getString("password");
                      String DBExpired      = HQ.getString("expired");
                      String DBStatus       = HQ.getString("status");
                      String DBLastLogin    = HQ.getString("lastlogin");
                      String DatStatus      = null;
                      
                      switch ( DBStatus ) {
                          case "1":
                              DatStatus = "Aktive";
                              break;
                          case "0":
                              DatStatus = "Non Aktive";
                              break;     
                      }    
                      String[] add          = {DBUserID,DBUserName, DBPass,DBExpired,DatStatus,DBLastLogin};
                      Model.addRow(add);      
          }
    }
      catch (Exception ex){
       JOptionPane.showMessageDialog (null, "Error (67)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
       ex.printStackTrace();
      }
   }
   
   private void AmbilDataListKiri (){
       
   }
   
   DefaultListModel ModelJListKanan;
   
   /*
    * Ambil data dari database ke List Kanan
    */
   private void DataListKanan () {                        
   
    ResultSet HQ = null;
    try {
        Statement stmm = K.createStatement();
        HQ = stmm.executeQuery("SELECT groupname from sys_groups order by groupname asc");
        ModelJListKanan = new DefaultListModel();
        JListUserKanan.setModel(ModelJListKanan);
    
        while(HQ.next()  ){
            String GroupName    = HQ.getString("groupname");
            ModelJListKanan.addElement(GroupName);
        }
    }
      catch (Exception ex){
       JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
       ex.printStackTrace();
      }
   }
   
   private void UpdateDiDatabase (String FullName, String User, String Pass,
        String DateExpired, int DisableExpired,  ListModel ModelList, int JumlahList, int Status){
        
        if ( DisableExpired == 0){
            DateExpired = "-";
        }
        //System.out.println(Pass + " Pas di save");
        try {
           Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           //Stm.executeUpdate("INSERT INTO sys_user (userid, username, password, expired, disable, status, lastlogin, lastupdate)"
           //        + " VALUES('"+ FullName + "','" + User +"','" + Pass +"',' " +DateExpired + "','" + DisableExpired + "','"+ Status + "', now(), now())");                
           Stm.executeUpdate("update sys_user set userid = '"+FullName+"', password ='"+ Pass +"',expired ='"+
                   DateExpired + "', disable = '"+ DisableExpired + "', status = '"+ Status +"', lastlogin = now(), lastupdate = now() where username = '"+ User +"'");
           try {
                Statement StmDetail = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                Statement StmHapus = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                StmHapus.executeUpdate("Delete from sys_usergroups where username = '"+ User +"'");
                for (int a = 0; a <= JumlahList; a++){                                       
                    StmDetail.executeUpdate("INSERT INTO sys_usergroups ( username, usergroups)"
                        + " VALUES('"+ User + "','" + ModelList.getElementAt(a).toString() +"')"); 
                }           
           }                
           catch (Exception X){ 
                    JOptionPane.showMessageDialog (null, "Tidak berhasil di save 2", "Error" , JOptionPane.ERROR_MESSAGE);
                    X.printStackTrace();
                }
        }
        catch (SQLException | HeadlessException X){ 
            JOptionPane.showMessageDialog (null, "Tidak berhasil di save 1", "Error" , JOptionPane.ERROR_MESSAGE);
            X.printStackTrace();
        }       
   }
           
   /*
    * Save User dan password di database
    */
   private void SaveDiDatabase (String FullName, String User, String Pass,
        String DateExpired, int DisableExpired,  ListModel ModelList, int JumlahList, int Status){
        
        if ( DisableExpired == 0){
            DateExpired = "-";
        }
        //System.out.println(Pass + " Pas di save");
        try {
           Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           Stm.executeUpdate("INSERT INTO sys_user (userid, username, password, expired, disable, status, lastlogin, lastupdate)"
                   + " VALUES('"+ FullName + "','" + User +"','" + Pass +"',' " +DateExpired + "','" + DisableExpired + "','"+ Status + "', now(), now())");     
           
           try {
                Statement StmDetail = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                for (int a = 0; a <= JumlahList; a++){                                       
                    //System.out.println((String) ModelList.getElementAt(a) + " Data ");
                   
                    StmDetail.executeUpdate("INSERT INTO sys_usergroups ( username, usergroups)"
                        + " VALUES('"+ User + "','" + ModelList.getElementAt(a).toString() +"')"); 
                }           
           }                
           catch (Exception X){ 
                    JOptionPane.showMessageDialog (null, "Tidak berhasil di save 2", "Error" , JOptionPane.ERROR_MESSAGE);
                    X.printStackTrace();
                }
        }
        catch (SQLException | HeadlessException X){ 
            JOptionPane.showMessageDialog (null, "Tidak berhasil di save 1", "Error" , JOptionPane.ERROR_MESSAGE);
            X.printStackTrace();
        }
   }
   
   /*
    * Hapus Jlistuserkanan
    */
   private void TambahElementJlistKiri(){
        /*
         * Logika search data di Jlist 
         * Ada data atau tidak
         */
        int cc = JListUserKiri.getLastVisibleIndex();
         String Target = (String) JListUserKanan.getSelectedValue();
         for (int i = 0; i <= cc ; i++) {
               String sel = (String) JListUserKiri.getModel().getElementAt(i);
           if (sel.equals(Target)){
               JOptionPane.showMessageDialog(null, "Maaf data sudah ada : " + sel);
               JListUserKiri.setSelectedIndex(i);
               JListUserKiri.requestFocus();
               return;
           }
         }   
         String data2 = (String) JListUserKanan.getSelectedValue();
         ModelListKiri.addElement(data2);                                         
   }

   /*
    * Logika JToggleButtonExpired Jika on tau off
    */
   private int ToggleOnOff;
   private int GetToggleOnOff(){
       return ToggleOnOff;
   }
   private void SetToggleOnOff(JToggleButton JToogle){
       JToogle.addActionListener(new ActionListener(){
           @Override
            public void actionPerformed( ActionEvent A){
               switch (A.getActionCommand()) {
                   case "OFF":
                       JToggleButtonExpired.setText("ON");
                       KazaoUserExpired.setEditable(true);
                       KazaoUserExpired.setEnabled(true);
                       ToggleOnOff = 1;
                       //DataListKanan ();
                       break;
                   case "ON":
                       JToggleButtonExpired.setText("OFF");
                       KazaoUserExpired.setEditable(false);
                       KazaoUserExpired.setEnabled(false);
                       ToggleOnOff = 0;
                       //DataListKanan ();
                       break;
               }
            }
        } );
       
   }

   private void KembaliNormalUser(DefaultTableModel ModelUserMaintenace){
       /*
        * Disable Action save
        */
       ButtonUserEdit = false;
        DisableAtauEnableUserMaintenance (false);
        JButtonUserAdd.setEnabled(true);
        JButtonUserEdit.setEnabled(true);
        JButtonUserDelete.setEnabled(true);
        JButtonUserSave.setEnabled(false);
        JCheckBoxUserAktive.setEnabled(false);
        JCheckBoxUserAktive.setSelected(true);
        DataUserMaintenance (ModelUserMaintenace);
        JTextUserFullName.setText("");
        JTextUserID.setText("");
        JPasswordUser1.setText("");
        JPasswordUser1.setText("");  
        ModelListKiri.removeAllElements(); 
        JTableUserMaintenance.setEnabled(true);
        KazaoUserExpired.setEnabled(false);
   }
   /*
    * Enable atau disable pada user maintenace
    */
   private void DisableAtauEnableUserMaintenance (boolean Data){
        JTextUserFullName.setEnabled(Data);
        JTextUserID.setEnabled(Data);
        JPasswordUser1.setEnabled(Data);
        JPasswordUser2.setEnabled(Data);
        JToggleButtonExpired.setEnabled(Data);
        JButtonUserKiri.setEnabled(Data);
        JButtonUserKanan.setEnabled(Data);
        JListUserKiri.setEnabled(Data);
        JListUserKanan.setEnabled(Data);
        //KazaoUserExpired.setEnabled(Data);
        //JToggleButtonExpired.setText("OFF");
        //JToggleButtonExpired.setSelected(false);
   }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JTabbedGroupMaintenance = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTableUserMaintenance = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        JTextUserFullName = new javax.swing.JTextField();
        JTextUserID = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        JToggleButtonExpired = new javax.swing.JToggleButton();
        KazaoUserExpired = new org.kazao.calendar.KazaoCalendarDate();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JListUserKiri = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        JListUserKanan = new javax.swing.JList();
        jPanel8 = new javax.swing.JPanel();
        JButtonUserKiri = new javax.swing.JButton();
        JButtonUserKanan = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        JButtonUserDelete = new javax.swing.JButton();
        JButtonUserEdit = new javax.swing.JButton();
        JButtonUserSave = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        JButtonUserAdd = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        JPasswordUser1 = new javax.swing.JPasswordField();
        JPasswordUser2 = new javax.swing.JPasswordField();
        JButtonUserRefresh = new javax.swing.JButton();
        JCheckBoxUserAktive = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        JTabelNameGroups = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TabelAllowedModul = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        TabelMenuProgram = new javax.swing.JTable();
        JTextNameUserGroup = new javax.swing.JTextField();
        JButtonKeKanan = new javax.swing.JButton();
        JButtonKeKiri = new javax.swing.JButton();
        JButtonRefreshAdm = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        JCheckBoxOpen = new javax.swing.JCheckBox();
        JCheckBoxSave = new javax.swing.JCheckBox();
        JCheckBoxEdit = new javax.swing.JCheckBox();
        JCheckBoxDelete = new javax.swing.JCheckBox();
        JCheckBoxPrint = new javax.swing.JCheckBox();
        JButtonSet = new javax.swing.JButton();
        JButtonSetAll = new javax.swing.JButton();
        JCheckBoxNew = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        JButtonDeleteAdm = new javax.swing.JButton();
        JButtonEditAdm = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        JButtonAddAdm = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        JButtonGroupsAdm = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        JButtonSaveAdm = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();

        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTableUserMaintenance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTableUserMaintenance);

        jLabel2.setText("Full Name");

        jLabel3.setText("User ID");

        jLabel4.setText("Password");

        jLabel5.setText("Confirm Password");

        JTextUserFullName.setEnabled(false);

        JTextUserID.setEnabled(false);

        JToggleButtonExpired.setText("OFF");
        JToggleButtonExpired.setEnabled(false);

        KazaoUserExpired.setEditable(false);
        KazaoUserExpired.setEnabled(false);

        jLabel6.setText("Expired");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JToggleButtonExpired)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(KazaoUserExpired, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(JToggleButtonExpired)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(KazaoUserExpired, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("User's Group Maintenace");
        jLabel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("User's Group");
        jLabel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JListUserKiri.setEnabled(false);
        jScrollPane2.setViewportView(JListUserKiri);

        JListUserKanan.setEnabled(false);
        jScrollPane3.setViewportView(JListUserKanan);

        JButtonUserKiri.setText("<<");
        JButtonUserKiri.setEnabled(false);

        JButtonUserKanan.setText(">>");
        JButtonUserKanan.setEnabled(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JButtonUserKiri, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(JButtonUserKanan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(JButtonUserKiri)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonUserKanan)
                .addGap(0, 46, Short.MAX_VALUE))
        );

        JButtonUserDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/delete_icon.png"))); // NOI18N

        JButtonUserEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/edit_icon.png"))); // NOI18N

        JButtonUserSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/save_icon.png"))); // NOI18N
        JButtonUserSave.setEnabled(false);

        jLabel9.setText("    Delete");

        jLabel10.setText("      Edit");

        jLabel13.setText("    Save");

        JButtonUserAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/add_item.png"))); // NOI18N

        jLabel14.setText("Add");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 21, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonUserAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JButtonUserDelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonUserEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(JButtonUserSave, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JButtonUserAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(JButtonUserSave, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonUserEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonUserDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel13)
                            .addComponent(jLabel9)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel14)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        JPasswordUser1.setText("xxx");
        JPasswordUser1.setEnabled(false);

        JPasswordUser2.setText("xxx");
        JPasswordUser2.setEnabled(false);

        JButtonUserRefresh.setText("Refresh");

        JCheckBoxUserAktive.setText("Aktive");

        jLabel21.setText("Status user :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(JTextUserID, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(JPasswordUser1, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                                .addComponent(JTextUserFullName))
                            .addComponent(JPasswordUser2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(JButtonUserRefresh)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JCheckBoxUserAktive))
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(JTextUserFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JTextUserID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JPasswordUser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JPasswordUser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JButtonUserRefresh)
                        .addComponent(jLabel21)
                        .addComponent(JCheckBoxUserAktive))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(81, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        JTabbedGroupMaintenance.addTab("User Maintenace", jPanel4);

        jPanel7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTabelNameGroups.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(JTabelNameGroups);

        jLabel1.setText("Group Name");

        TabelAllowedModul.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TabelAllowedModul.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane5.setViewportView(TabelAllowedModul);

        TabelMenuProgram.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TabelMenuProgram.setEnabled(false);
        jScrollPane6.setViewportView(TabelMenuProgram);

        JTextNameUserGroup.setEnabled(false);

        JButtonKeKanan.setText(">");
        JButtonKeKanan.setEnabled(false);
        JButtonKeKanan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JButtonKeKanan.setMargin(new java.awt.Insets(2, 2, 2, 2));

        JButtonKeKiri.setText("<");
        JButtonKeKiri.setEnabled(false);
        JButtonKeKiri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JButtonKeKiri.setMargin(new java.awt.Insets(2, 2, 2, 2));

        JButtonRefreshAdm.setText("Refresh");

        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Allowed Button Options");
        jLabel15.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JCheckBoxOpen.setText("Open");
        JCheckBoxOpen.setEnabled(false);
        JCheckBoxOpen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCheckBoxOpenItemStateChanged(evt);
            }
        });

        JCheckBoxSave.setText("Save");
        JCheckBoxSave.setEnabled(false);

        JCheckBoxEdit.setText("Edit");
        JCheckBoxEdit.setEnabled(false);

        JCheckBoxDelete.setText("Del");
        JCheckBoxDelete.setEnabled(false);

        JCheckBoxPrint.setText("Print");
        JCheckBoxPrint.setEnabled(false);

        JButtonSet.setText("Set");
        JButtonSet.setEnabled(false);
        JButtonSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButtonSetActionPerformed(evt);
            }
        });

        JButtonSetAll.setText("Set All ");
        JButtonSetAll.setEnabled(false);
        JButtonSetAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButtonSetAllActionPerformed(evt);
            }
        });

        JCheckBoxNew.setText("New");
        JCheckBoxNew.setEnabled(false);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JCheckBoxOpen)
                    .addComponent(JCheckBoxSave))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(JCheckBoxDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JCheckBoxNew, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(JCheckBoxEdit)
                        .addGap(18, 18, 18)
                        .addComponent(JCheckBoxPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JButtonSet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JButtonSetAll, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                .addGap(22, 22, 22))
            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(JButtonSet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(40, 40, 40))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JCheckBoxOpen)
                            .addComponent(JCheckBoxEdit)
                            .addComponent(JCheckBoxPrint))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JCheckBoxSave)
                            .addComponent(JCheckBoxDelete)
                            .addComponent(JButtonSetAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JCheckBoxNew))
                        .addContainerGap())))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonDeleteAdm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/delete_icon.png"))); // NOI18N

        JButtonEditAdm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/edit_icon.png"))); // NOI18N

        jLabel16.setText("    Delete");

        jLabel17.setText("      Edit");

        JButtonAddAdm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/add_item.png"))); // NOI18N

        jLabel19.setText("Add");

        JButtonGroupsAdm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/copy_icon.png"))); // NOI18N
        JButtonGroupsAdm.setEnabled(false);

        jLabel20.setText("Copy Groups");

        JButtonSaveAdm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/save_icon.png"))); // NOI18N
        JButtonSaveAdm.setEnabled(false);

        jLabel18.setText("    Save");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonAddAdm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JButtonGroupsAdm, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JButtonDeleteAdm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonEditAdm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonSaveAdm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JButtonAddAdm)
                    .addComponent(JButtonEditAdm, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonDeleteAdm, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16)))
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(JButtonGroupsAdm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20))
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(JButtonSaveAdm, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextNameUserGroup)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonRefreshAdm)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JButtonKeKiri)
                    .addComponent(JButtonKeKanan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(191, 191, 191)
                        .addComponent(JButtonKeKanan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JButtonKeKiri)
                        .addGap(0, 118, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(JTextNameUserGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JButtonRefreshAdm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        JTabbedGroupMaintenance.addTab("Group Maintenace", jPanel6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JTabbedGroupMaintenance, javax.swing.GroupLayout.PREFERRED_SIZE, 873, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JTabbedGroupMaintenance)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void JCheckBoxOpenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCheckBoxOpenItemStateChanged

    }//GEN-LAST:event_JCheckBoxOpenItemStateChanged

    private void JButtonSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButtonSetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JButtonSetActionPerformed

    private void JButtonSetAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButtonSetAllActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JButtonSetAllActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonAddAdm;
    private javax.swing.JButton JButtonDeleteAdm;
    private javax.swing.JButton JButtonEditAdm;
    private javax.swing.JButton JButtonGroupsAdm;
    private javax.swing.JButton JButtonKeKanan;
    private javax.swing.JButton JButtonKeKiri;
    private javax.swing.JButton JButtonRefreshAdm;
    private javax.swing.JButton JButtonSaveAdm;
    private javax.swing.JButton JButtonSet;
    private javax.swing.JButton JButtonSetAll;
    private javax.swing.JButton JButtonUserAdd;
    private javax.swing.JButton JButtonUserDelete;
    private javax.swing.JButton JButtonUserEdit;
    private javax.swing.JButton JButtonUserKanan;
    private javax.swing.JButton JButtonUserKiri;
    private javax.swing.JButton JButtonUserRefresh;
    private javax.swing.JButton JButtonUserSave;
    private javax.swing.JCheckBox JCheckBoxDelete;
    private javax.swing.JCheckBox JCheckBoxEdit;
    private javax.swing.JCheckBox JCheckBoxNew;
    private javax.swing.JCheckBox JCheckBoxOpen;
    private javax.swing.JCheckBox JCheckBoxPrint;
    private javax.swing.JCheckBox JCheckBoxSave;
    private javax.swing.JCheckBox JCheckBoxUserAktive;
    private javax.swing.JList JListUserKanan;
    private javax.swing.JList JListUserKiri;
    private javax.swing.JPasswordField JPasswordUser1;
    private javax.swing.JPasswordField JPasswordUser2;
    private javax.swing.JTabbedPane JTabbedGroupMaintenance;
    private javax.swing.JTable JTabelNameGroups;
    private javax.swing.JTable JTableUserMaintenance;
    private javax.swing.JTextField JTextNameUserGroup;
    private javax.swing.JTextField JTextUserFullName;
    private javax.swing.JTextField JTextUserID;
    private javax.swing.JToggleButton JToggleButtonExpired;
    private org.kazao.calendar.KazaoCalendarDate KazaoUserExpired;
    private javax.swing.JTable TabelAllowedModul;
    private javax.swing.JTable TabelMenuProgram;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    // End of variables declaration//GEN-END:variables
}
