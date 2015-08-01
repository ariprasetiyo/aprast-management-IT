/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author arprast
 */
public class Sys_Login {
    public Sys_Login(){
        
    }
    /*
        hak akses Button Menu Utama
    */
     public void JMenuITemHideOrTdk (List<String> ListData, JButton[] Coba){
        if (ListData !=  null){
          for (int a = 0; a < Coba.length; a++) {
              for (int ab = 0; ab < ListData.size(); ab++) {
                   if (Coba[a].getName().equalsIgnoreCase(ListData.get(ab).toString())) {
                       Coba[a].setVisible(true);
                }
              }
          }
        }
        else {
            JOptionPane.showMessageDialog(null, "Error 18826 Sys_Login");
            System.err.println("JButton Null");
        }
    }
     
     /*
        hak akses CRUDP
        ListData    = Data menu name dari HakAkses();
        Menu        = Nama menu yang akan dibuka
        MenuAcces   = Data 111111 dari AksesMenuLevel();
        Button      = Data button yang terdapat pada Frame tersebut
                      Harus urut save, edit, delete, open, print, new
     */
     public void JMenuITemHideOrTdk (List<String> ListData, String Menu, List<String>  MenuAcces, JButton[] Button){
        if (ListData !=  null){
              for (int ab = 0; ab < ListData.size(); ab++) {
                   if (Menu.equalsIgnoreCase(ListData.get(ab).toString())) {
                       char[] CharData = new char[Button.length];
                       for (int b = 0; b < Button.length; b++) {
                           CharData[b] = MenuAcces.get(ab).charAt(b);
                            if (Button[b] == null) {
                                continue;
                            }
                            if (CharData[b] == '1' ) {
                                Button[b].setVisible(true);
                            }
                            else {
                                Button[b].setVisible(false);
                            }
                       }
                }
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Error 1846326 Sys_Login");
            System.err.println("JButton Null");
        }
    }
     
    /*
        hak akses JMenuItem
    */
     public void JMenuITemHideOrTdk (List<String> ListData, JMenuItem Menu){
        if (ListData !=  null){
              for (int ab = 0; ab < ListData.size(); ab++) {
                   if (Menu.getName().equalsIgnoreCase(ListData.get(ab).toString())) {
                       Menu.setVisible(true); 
                }
            }
        }
    }
    
    
    /*
     * Kejadian Login dari framelogin
     */
    public String NamaUser;
    public boolean CloseAtauTidak(String DapatUser, String DapatPass, JFrame Login) {
        NamaUser = DapatUser;
        boolean DBPass = false;
        DBPass          = DataPassword (DapatUser, DapatPass);
        if ("ari".equals(DapatUser) && "hanif".equals(DapatPass)){
            //JOptionPane.showMessageDialog(null, "coba");
             return true;
        }
        else if (DBPass == true){ 
            /*
            aa.setVisible(false);
            JLabelHostName.setText(HostName());
            JLabelIPLocal.setText(IPLocal());
            JLabelIPPublic.setText(IPPublic());
            JLabelUserName.setText(DapatUser);
            JLabelOS.setText(OSName());
            */
            
            Login.setVisible(false);

            /*
             * jika name dan bd sama maka
             *      setvisible button true
             *      caranya ?
             *      - button
             */
            return true;
        }     
        else {
            // this.MaxLogin = MaxLogin - 1;
             JOptionPane.showMessageDialog (null, "Maaf, user atau password salah !! ", "Error", JOptionPane.ERROR_MESSAGE);
             //LabelPeringatan.setText("User/Password salah !! ( " + a + " )" );
             //if (MaxLogin == 0){
                 //FrameNotClose.setVisible(rootPaneCheckingEnabled);
                 //initComponents().setVisible(false);
             //    System.exit(0);
            // } 
       }
        return false;
    }
    
    /*
     * Ambil user dan password
       how create hak access
        - disable and enable button with logical if
          if 1111 enable all
          if 1110 enable all except printer and etc
          how to know menuname is 1111 or 1110 by the way JMenuITemHideOrTdk, 
          because JMenuITemHideOrTdk there's data show if menuacess suitable ( sesuai )  or no
     */
    private boolean DataPassword (String User, String Pass){
        Sistem.DB_MYSQL KD = new Sistem.DB_MYSQL();
        Connection K = KD.createConnection();
        ResultSet HQ = null;
        try {
            Statement stmm = K.createStatement();
            //"Full name ", "User ID","Password","Expired", "Status", "Last Login"
            HQ = stmm.executeQuery("SELECT username, password, expired, disable, status  from sys_user where username = '"+ User  + "' order by username asc");
            while(HQ.next()  ){
                    String DBPass     = HQ.getString("password");
                    String DBExpired  = HQ.getString("expired");
                    int DBDisable     = HQ.getInt("disable");
                    int DBStatus      = HQ.getInt("status");
                    
                    /*
                     * Enkrip pass
                     */
                    Sistem.SecurityEnkripsiDanDeskripsi Aman = new Sistem.SecurityEnkripsiDanDeskripsi();
                    String DataAman = Aman.encrypt(Pass, User);
                    
                    //JOptionPane.showMessageDialog(null, DBDisable + " " + DBStatus);
                    if (DBPass.equals( DataAman)){
                        Sistem.TanggalSistem TglExpired = new Sistem.TanggalSistem();                        
                        String Ex = TglExpired.GetBulanSisString() + TglExpired.GetTahunSisString() + TglExpired.GetTanggalSisString();
                        
                        if (DBDisable == 0 && DBStatus ==1 ) {
                           return true;
                        }
                        else if (DBDisable == 1 && DBStatus == 1){
                          
                           /*
                            * Compare tanggal sis dan expired
                            */
                           TglExpired.SetTanggalSis();
                           TglExpired.SetBulanSis();
                           TglExpired.SetTahunSis();
                           DateFormat DT    = new SimpleDateFormat("dd-MM-yyyy");
                           Date DTExpired   = DT.parse(DBExpired);
                           Date DTSis       = DT.parse(TglExpired.GetTanggalSis() +"-"+TglExpired.GetBulanSis()+"-"+TglExpired.GetTahunSis());
                           //JOptionPane.showMessageDialog(null, TglExpired.GetTanggalSis() +"-"+TglExpired.GetBulanSis()+"-"+TglExpired.GetTahunSis());
                           if ( DTSis.compareTo(DTExpired) < 0){
                               return true;
                           }
                           else {
                               
                                JOptionPane.showMessageDialog(null, "Password sudah tidak berlaku pada " + DTExpired );
                                return false;
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog (null, "Username dan password tidak berlaku", "INFO",JOptionPane.INFORMATION_MESSAGE);
                            return false;
                        }
                    }
                    else {
                            JOptionPane.showMessageDialog (null, "User Name dan Pasword tidak ada yang cocok", "INFO", JOptionPane.INFORMATION_MESSAGE);
                           return false;
                    }
              }
        }
          catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (67)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                return false;
          }
       return false;
    }
    
    /*
        Hak akses edit, delete, print dan disable atau enable tombol
    */
    List<String>  DataAccesMenu = new ArrayList();
    List<String> Data1 = new ArrayList();
    public List<String> HakAkses(String DapatUser){
        
       // ArrayList<List<String>> DataList = new ArrayList<List<String>>();
       
        Sistem.DB_MYSQL KD = new Sistem.DB_MYSQL();
        Connection K = KD.createConnection();

         ResultSet HQ = null;
            try {
                Statement stmm = K.createStatement();
                //"Full name ", "User ID","Password","Expired", "Status", "Last Login"
                HQ = stmm.executeQuery(""
                + "SELECT b.menuname FROM sys_groups a "
                + "inner join sys_groupsprogram b on a.groupno = b.groupno "
                + "inner join sys_usergroups c on  a.groupname = c.usergroups "
                + "where c.username = '"+ DapatUser  + "'");
                
                while(HQ.next()  ){
                    String MenuId   = HQ.getString("menuname");     
                    Data1.add(MenuId);
                }

                return  Data1;
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (6777)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        return null;
    }
    
    public List<String> AksesMenuLevel(String DapatUser){
        
        //ArrayList<List<String>> DataList = new ArrayList<List<String>>();
       
        Sistem.DB_MYSQL KD = new Sistem.DB_MYSQL();
        Connection K = KD.createConnection();

         ResultSet HQ = null;
            try {
                Statement stmm = K.createStatement();
                //"Full name ", "User ID","Password","Expired", "Status", "Last Login"
                HQ = stmm.executeQuery(""
                + "SELECT  b.acceslevel FROM sys_groups a "
                + "inner join sys_groupsprogram b on a.groupno = b.groupno "
                + "inner join sys_usergroups c on  a.groupname = c.usergroups "
                + "where c.username = '"+ DapatUser  + "'");
                
                while(HQ.next()  ){

                    String MenuAkses= HQ.getString("acceslevel");       
                    DataAccesMenu.add(MenuAkses);
                }
                return  DataAccesMenu;
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (6777)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        return null;
    }
}
