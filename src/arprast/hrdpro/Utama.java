/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arprast.hrdpro;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;


/**
 *
 * @author LANTAI3
 */

class VerticalMenuBar extends JMenuBar {
  private static final LayoutManager grid = new GridLayout(0,1);
  public VerticalMenuBar() {
    setLayout(grid);
  }
}

public class Utama extends javax.swing.JFrame {
    /*
     * Get size screen
     * Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       double width = screenSize.getWidth();
       double height = screenSize.getHeight();
     * Gek multi screen 
     */
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int width = gd.getDisplayMode().getWidth();
    int height = gd.getDisplayMode().getHeight();

    public JButton UtamaHrd, UtamaManagementIT, Setup;
    public JButton TombolKe1a, TombolKe1b,TombolKe1c, TombolKe1d;
    boolean BukaTutup = false;
    boolean ButtonTutup = false;
    
    LoginUser LoginFrame = new LoginUser();
    private void AksiPadaLogin(){
         LoginFrame.LoginPass.addKeyListener(new KeyListener(){
             @Override
             public void keyTyped(KeyEvent e) {
                 //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
             }

             @Override
             public void keyPressed(KeyEvent e) {
                String s = KeyEvent.getKeyText(e.getKeyCode());
                if (s.equals("Enter")){
                   FrameLogi();
                }
             }

             @Override
             public void keyReleased(KeyEvent e) {
                 //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
             }
             
         });
         LoginFrame.ButtonLogin.addActionListener(new ActionListener (){
            @Override
              public void actionPerformed (ActionEvent e) {
                  String s = e.getActionCommand();
                  if (s.equals("Login")) {
                      FrameLogi();
                  }
              }
        });
    }
    
    int maxlogin = 3;
    List<String> ListMenuAkses = new ArrayList() ;
    Sistem.Sys_Login Sys_Login = new Sistem.Sys_Login();
    String DapatPass;
    public String  DapatUser;
   
    void FrameLogi(){
        DapatPass    = LoginFrame.LoginPass.getText();
        //Utama ProInventory = new Utama();
        DapatUser    = LoginFrame.LoginUser.getText();
        this.ListMenuAkses = Sys_Login.HakAkses(DapatUser);
        //System.out.println("coba " + ListMenuAkses.toString());
        if (Sys_Login.CloseAtauTidak(DapatUser, DapatPass, LoginFrame)){
            
            /*
                disable enable menu ada isini 
                contoh UtamaHrd = Jbutton
            */
            LoginFrame.setVisible(false);

            /*
            buka tutup menu utama
            */
            this.setVisible(true);
            JButton[] Button = {UtamaHrd,UtamaManagementIT, Setup};  
            for (int a = 0; a < Button.length; a++) {
                Button[a].setVisible(false);
            }
            Sys_Login.JMenuITemHideOrTdk(ListMenuAkses,Button );
        }
        else {
            maxlogin = maxlogin - 1;
            if (maxlogin == 0){
               System.exit(1);
            }
        }
    }
   
    public Utama() {   
            initComponents(); 
            jSplitPane1.setOneTouchExpandable(true);
            this.setVisible(false);
            LoginFrame.setVisible(true);
            AksiPadaLogin();   
            
            //UtamaHrd.setName("HrdMain");
            //jSplitPane1.setDividerLocation(1.0);
            TombolUtama();
            Aksi();
            
            /*
             * Add gambar pada jpanel
             */
            ImagePanel panel = new ImagePanel(new ImageIcon("Gambar/bluevertical.png").getImage());
            
            JPanelMenu.add(panel);

            Sistem.Sys_Running_Background_StartUp RunningBg = new Sistem.Sys_Running_Background_StartUp();
            RunningBg.Running_Load_Master_Item(jProgressBar1);
            Status();
            
    }
    
    private void Status(){
        Sistem.Sys_Status Status = new Sistem.Sys_Status();
        StatusOs.setText( DapatUser );
        Status.StatusDatabase();
        StatusDatabase.setText(Status.Database);
        StatusServer1.setText(Status.IPServer);
        StatusIPPublic.setText(Status.IPPublic());
        StatusIPLocal.setText(Status.IPLocal());
        StatusOs.setText(Status.OSName());
    }
 
    JPopupMenu popupMenu = new JPopupMenu();   
    JMenuItem m ;
    JMenuItem m2 ;
    
    
    
    public void Uji_Coba_Event (ActionEvent E){
        
        List<String> MenuAksesLevel = Sys_Login.AksesMenuLevel(DapatUser);
        Hrd_Pelamar ccc = new Hrd_Pelamar();
        
        //save, edit, delete, open, print, new
        JButton[] buttoOnOff = {null, ccc.JButtonPelamarEdit, null, null,null,  ccc.JButtonPelamarNew};
        Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, "Pelamar", MenuAksesLevel, buttoOnOff);
        
        Sistem.TambahTab TabNewViewData = new Sistem.TambahTab();
        TabNewViewData.createTabButtonActionPerformed(E, JTabMain,
        "Pelamar "  , ccc,
                                // ( Panjang, Tinggi )
         new Dimension(30, 20), new Dimension(width - (width/ 10) , height - (height /5 )));
    }
     private void showPopupMenu1(JButton invoker, int IndexMenu) {     
        ActionListener al = new ActionListener() {
        public void actionPerformed(ActionEvent E) {
            String DetekTombol = E.getActionCommand();
            if (DetekTombol.equalsIgnoreCase("pelamar")){
                Uji_Coba_Event ( E);
            }
            else if (DetekTombol.equalsIgnoreCase("Data Karyawan")){
                Hrd_DataKaryawan ccc = new Hrd_DataKaryawan();
                Sistem.TambahTab TabNewViewData = new Sistem.TambahTab();
                TabNewViewData.createTabButtonActionPerformed(E, JTabMain,
                                        "Data Karyawan "  , ccc,
                                        // ( Panjang, Tinggi )
                                        new Dimension(30, 20), new Dimension(width - (width/ 10) , height - (height /5 )));
                //System.out.println(DetekTombol);
            }
            else if (DetekTombol.equalsIgnoreCase("Formulir")){
                Hrd_Form ccc = new Hrd_Form();
                Sistem.TambahTab TabNewViewData = new Sistem.TambahTab();
                TabNewViewData.createTabButtonActionPerformed(E, JTabMain,
                                        "Formulir "  , ccc,
                                        // ( Panjang, Tinggi )
                                        new Dimension(30, 20), new Dimension(width - (width/ 10) , height - (height /5 )));
                //System.out.println(DetekTombol);
            }
            else if (DetekTombol.equalsIgnoreCase("Absensi")){
                Hrd_Absensi ccc = new Hrd_Absensi();
                Sistem.TambahTab TabNewViewData = new Sistem.TambahTab();
                TabNewViewData.createTabButtonActionPerformed(E, JTabMain,
                                        "Absensi "  , ccc,
                                        // ( Panjang, Tinggi )
                                        new Dimension(30, 20), new Dimension(width - (width/ 10) , height - (height /5 )));
                //System.out.println(DetekTombol);
            }
            else if (DetekTombol.equalsIgnoreCase("Pengajian")){
                Hrd_Pegajian ccc = new Hrd_Pegajian();
                Sistem.TambahTab TabNewViewData = new Sistem.TambahTab();
                TabNewViewData.createTabButtonActionPerformed(E, JTabMain,
                                        "Pengajian "  , ccc,
                                        // ( Panjang, Tinggi )
                                        new Dimension(30, 20), new Dimension(width - (width/ 10) , height - (height /5 )));
            }
            else if (DetekTombol.equalsIgnoreCase("Penerimaan Barang")){
                arprast.itpro.PenerimaanBarang1 ccc = new  arprast.itpro.PenerimaanBarang1();
                Sistem.TambahTab TabNewViewData = new Sistem.TambahTab();
                TabNewViewData.createTabButtonActionPerformed(E, JTabMain,
                                        "Penerimaan Barang "  , ccc,
                                        // ( Panjang, Tinggi )
                                        new Dimension(30, 20), new Dimension(width - (width/ 10) , height - (height /5 )));
            }
            else if (DetekTombol.equalsIgnoreCase("Master Barang")){
                arprast.all.Master_Barang ccc = new  arprast.all.Master_Barang(DapatUser);
                Sistem.TambahTab TabNewViewData = new Sistem.TambahTab();
                TabNewViewData.createTabButtonActionPerformed(E, JTabMain,
                                        "Master Barang "  , ccc,
                                        // ( Panjang, Tinggi )
                                        new Dimension(30, 20), new Dimension(width - (width/ 10) , height - (height /5 )));
            }
             else if (DetekTombol.equalsIgnoreCase("Master Harga")){
                arprast.all.Master_Harga ccc = new  arprast.all.Master_Harga(DapatUser);
                Sistem.TambahTab TabNewViewData = new Sistem.TambahTab();
                TabNewViewData.createTabButtonActionPerformed(E, JTabMain,
                                        "Master Harga "  , ccc,
                                        // ( Panjang, Tinggi )
                                        new Dimension(30, 20), new Dimension(width - (width/ 10) , height - (height /5 )));
            }
                
            /*
            else if (DetekTombol.equalsIgnoreCase("Setting User & Password")){
                 SettingUserPass ccc = new SettingUserPass();
                 Sistem.TambahTab TabNewViewData = new Sistem.TambahTab();
                 TabNewViewData.createTabButtonActionPerformed(E, JTabMain,
                                        "User & Password "  , ccc,
                                        // ( Panjang, Tinggi )
                                        new Dimension(30, 20), new Dimension(width - (width/ 10) , height - (height /5 )));
            }  */
            
            else if (DetekTombol.equalsIgnoreCase("Setting User & Password")){
                 Sys_UserAdmin ccc = new Sys_UserAdmin();
                 
                 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize() ;
                 
                 Sistem.TambahTab TabNewViewData = new Sistem.TambahTab();
                 TabNewViewData.createTabButtonActionPerformed(E, JTabMain,
                                        "User & Password "  , ccc,
                                        // ( Panjang, Tinggi )
                                        new Dimension(30, 20), screenSize );
            } 
            else if (DetekTombol.equalsIgnoreCase("Setting Pro")){
                 Sys_SettingPro ccc = new Sys_SettingPro();
                 
                 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize() ;
                 
                 Sistem.TambahTab TabNewViewData = new Sistem.TambahTab();
                 TabNewViewData.createTabButtonActionPerformed(E, JTabMain,
                                        "Setting Pro"  , ccc,
                                        // ( Panjang, Tinggi )
                                        new Dimension(30, 20), screenSize );
            } 
          }
        };

        
        popupMenu.removeAll();
        popupMenu.setLayout(new BoxLayout(popupMenu, BoxLayout.PAGE_AXIS));
        if (IndexMenu == 0 ){
            
            //System.out.println(ListMenuAkses);
            m = new JMenuItem("Pelamar");
            m.setName("Pelamar");
            m.setVisible(false);
            Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, m);
            m.addActionListener(al);
            popupMenu.add(m );
            
            m = new JMenuItem("Data Karyawan");
            m.setName("Data Karyawan");
            m.setVisible(false);
            Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, m);
            m.addActionListener(al);
            popupMenu.add(m );
            
            m = new JMenuItem("Formulir");
            m.setName("Formulir");
            m.setVisible(false);
            Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, m);
            m.addActionListener(al);
            popupMenu.add(m );
            
            m = new JMenuItem("Absensi");
            m.setName("Absensi");
            m.setVisible(false);
            Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, m);
            m.addActionListener(al);
            popupMenu.add(m );
            
            m = new JMenuItem("Pengajian");
            m.setName("Pegajian");
            m.setVisible(false);
            Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, m);
            m.addActionListener(al);
            popupMenu.add(m );  
            
        }
        else if (IndexMenu == 1 ){
            m = new JMenuItem("Penerimaan Barang");
            m.setName("Penerimaan Barang");
            m.setVisible(false);
            Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, m);
            m.addActionListener(al);
            popupMenu.add(m );
            
            m = new JMenuItem("Pengeluaran Barang");
            m.setName("Pengeluaran Barang");
            m.setVisible(false);
            Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, m);
            m.addActionListener(al);
            popupMenu.add(m );  
            
            m = new JMenuItem("Stock Movement");
            m.setName("Stock Movement");
            m.setVisible(false);
            Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, m);
            m.addActionListener(al);
            popupMenu.add(m );
            
            //sub menu
            JMenu sectionsMenu = new JMenu("Master");
            sectionsMenu.setVisible(false);
            sectionsMenu.setName("Master");
            sectionsMenu.setVisible(false);
            Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, sectionsMenu);
            
                m = new JMenuItem("Master Barang");
                m.setName("Master Barang");
                m.setVisible(false);
                Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, m);
                m.addActionListener(al);
                sectionsMenu .add(m );

                m = new JMenuItem("Master Harga");
                m.setName("Master Harga");
                m.setVisible(false);
                Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, m);
                m.addActionListener(al);
                sectionsMenu .add(m );

            popupMenu.add(sectionsMenu);

            //regular menu item
            /*
            menuItem2 = new JMenuItem("Numbers");
            popupMenu.add(menuItem2);
            */
            
            /*
            membuat sub menu jpopupmenu
            JPopupMenu pmenu = new JPopupMenu("Menu");

            //sub menu
            JMenu sectionsMenu = new JMenu("Sections");
            JMenuItem menuItem1 = new JMenuItem("Item1");
            sectionsMenu .add(menuItem1 );
            JMenuItem menuItem2 = new JMenuItem("Item2");
            sectionsMenu .add(menuItem2 );

            pmenu.add(menuItem);

            //regular menu item
            menuItem = new JMenuItem("Numbers");
            Pmenu.add(menuItem);
            */
            
        }
        
        else if (IndexMenu == 2 ){
            m = new JMenuItem("Setting User & Password");
            m.setName("Setting User & Password");
            m.setVisible(false);
            Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, m);
            m.addActionListener(al);
            popupMenu.add(m );
            
            m = new JMenuItem("Setting Pro");
            m.setName("Setting Pro");
            m.setVisible(false);
            Sys_Login.JMenuITemHideOrTdk(ListMenuAkses, m);
            m.addActionListener(al);
            popupMenu.add(m );
        }
      
        /*
         * 0                    = posisi x
         * invoker.getHeight()  = posisi y
         *  popupMenu.show(invoker, 0, invoker.getHeight());
         */        
        popupMenu.show(invoker, invoker.getWidth() , 0);
}
     
    private String folder;
    private String LokasiGambar() {
        folder = System.getProperty("user.dir") + File.separator + "Gambar/HRDIcon2.png";
        return folder ;
    }
    
    private void Aksi(){

     //final String[] data = {"utama-Hrd","SubMain-HRD","Sub-Karyawan","Sub-Main","Sub-Pelamar","Utama-Laporan", "Sub-Gaji"};
        UtamaHrd.addMouseListener(new MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //boolean[] Data = {true, true, true, true};
                //DisableButtonMain(Data);
                showPopupMenu1(UtamaHrd, 0);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                //showPopupMenu1(UtamaManagementIT);
                //popupMenu.removeAll();
            }
        });
        UtamaManagementIT.addMouseListener(new MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //boolean[] Data = {true, true, true, true};
                //DisableButtonMain(Data);
                showPopupMenu1(UtamaManagementIT, 1);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                //showPopupMenu1(UtamaManagementIT);
                //popupMenu.removeAll();
            }
        });
        Setup.addMouseListener(new MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //boolean[] Data = {true, true, true, true};
                //DisableButtonMain(Data);
                showPopupMenu1(Setup, 2);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                //showPopupMenu1(UtamaManagementIT);
                //popupMenu.removeAll();
            }
        });     
    }

    private void  PanggilHrd_Pelamar(ActionEvent E){
        Hrd_Pelamar ccc = new Hrd_Pelamar();
        Sistem.TambahTab TabNewViewData = new Sistem.TambahTab();
        TabNewViewData.createTabButtonActionPerformed(E, JTabMain,
                                "Pelamar "  , ccc,
                                // ( Panjang, Tinggi )
                                new Dimension(30, 20), new Dimension(2000, 2000));
    }
    
    private void DisableButtonMain(boolean[] x ){
        /*
         * Rancangan
         */
        TombolKe1a.setVisible(x[0]);
        TombolKe1b.setVisible(x[0]);
        TombolKe1c.setVisible(x[0]);
        TombolKe1d.setVisible(x[0]);
    }
    
    private void TombolUtama(){

        /*
         * Laporaan, BLaporanLabaRugi 
         */
       UtamaHrd = new Sistem.ClButtonTransparant("");
       UtamaHrd.setHorizontalTextPosition(SwingConstants.CENTER);
       UtamaHrd.setVerticalTextPosition(SwingConstants.BOTTOM);
       UtamaHrd.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       UtamaHrd.setBounds(5, 10, 100, 35);
       UtamaHrd.setName("HRD");
       UtamaHrd.setVisible(ButtonTutup);
       UtamaHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/SimbolHrd.png")));
       JPanelMenu.add(UtamaHrd);
       
       UtamaManagementIT = new Sistem.ClButtonTransparant("");
       UtamaManagementIT.setHorizontalTextPosition(SwingConstants.CENTER);
       UtamaManagementIT.setVerticalTextPosition(SwingConstants.BOTTOM);
       UtamaManagementIT.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       UtamaManagementIT.setBounds(5, 10 + (35 * 1), 100,35);
       UtamaManagementIT.setVisible(true);
       UtamaManagementIT.setName("Management IT");
       UtamaManagementIT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/absensiicon.png")));
       
       JPanelMenu.add(UtamaManagementIT);
       
       Setup = new Sistem.ClButtonTransparant("");
       Setup.setHorizontalTextPosition(SwingConstants.CENTER);
       Setup.setVerticalTextPosition(SwingConstants.BOTTOM);
       Setup.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       Setup.setBounds(5, 10 + (35 * 2), 100, 35);
       Setup.setVisible(true);
       Setup.setName("Setup");
       Setup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/ManageUsersIcon.png")));
       
       JPanelMenu.add(Setup);
    }
    
       public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        // TemaWarnaPutih ();

           //TemaGelap();
           
           //</editor-fold>
           Tema.Aero Tema = new Tema.Aero();
           //Tema.TemaSystem Tema = new Tema.TemaSystem();
           
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Utama();
                }
            });
       
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenu2 = new javax.swing.JMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jPopupMenu4 = new javax.swing.JPopupMenu();
        jPopupMenu5 = new javax.swing.JPopupMenu();
        jMenu3 = new javax.swing.JMenu();
        popupMenu1 = new java.awt.PopupMenu();
        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        menuBar2 = new java.awt.MenuBar();
        menu3 = new java.awt.Menu();
        menu4 = new java.awt.Menu();
        jCheckBox1 = new javax.swing.JCheckBox();
        menuBar3 = new java.awt.MenuBar();
        menu5 = new java.awt.Menu();
        menu6 = new java.awt.Menu();
        menuBar4 = new java.awt.MenuBar();
        menu7 = new java.awt.Menu();
        menu8 = new java.awt.Menu();
        menuBar5 = new java.awt.MenuBar();
        menu9 = new java.awt.Menu();
        menu10 = new java.awt.Menu();
        menuBar6 = new java.awt.MenuBar();
        menu11 = new java.awt.Menu();
        menu12 = new java.awt.Menu();
        menuBar7 = new java.awt.MenuBar();
        menu13 = new java.awt.Menu();
        menu14 = new java.awt.Menu();
        popupMenu2 = new java.awt.PopupMenu();
        popupMenu3 = new java.awt.PopupMenu();
        jSplitPane1 = new javax.swing.JSplitPane();
        JPanelMenu = new javax.swing.JPanel();
        StatusAuthorization1 = new javax.swing.JLabel();
        JTabMain = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        StatusDatabase = new javax.swing.JLabel();
        StatusIPLocal = new javax.swing.JLabel();
        StatusOs = new javax.swing.JLabel();
        IPLocal = new javax.swing.JLabel();
        StatusServer1 = new javax.swing.JLabel();
        IPLocal1 = new javax.swing.JLabel();
        StatusIPPublic = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jMenu2.setText("jMenu2");

        jMenu3.setText("jMenu3");

        popupMenu1.setLabel("popupMenu1");

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        menu3.setLabel("File");
        menuBar2.add(menu3);

        menu4.setLabel("Edit");
        menuBar2.add(menu4);

        jCheckBox1.setText("jCheckBox1");

        menu5.setLabel("File");
        menuBar3.add(menu5);

        menu6.setLabel("Edit");
        menuBar3.add(menu6);

        menu7.setLabel("File");
        menuBar4.add(menu7);

        menu8.setLabel("Edit");
        menuBar4.add(menu8);

        menu9.setLabel("File");
        menuBar5.add(menu9);

        menu10.setLabel("Edit");
        menuBar5.add(menu10);

        menu11.setLabel("File");
        menuBar6.add(menu11);

        menu12.setLabel("Edit");
        menuBar6.add(menu12);

        menu13.setLabel("File");
        menuBar7.add(menu13);

        menu14.setLabel("Edit");
        menuBar7.add(menu14);

        popupMenu2.setLabel("popupMenu2");

        popupMenu3.setLabel("popupMenu3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Arprast-Soft");
        setBackground(new java.awt.Color(209, 222, 222));

        jSplitPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        JPanelMenu.setBackground(new java.awt.Color(209, 222, 222));
        JPanelMenu.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        JPanelMenu.setToolTipText("");

        StatusAuthorization1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        StatusAuthorization1.setText("Ver 0.0.1");

        javax.swing.GroupLayout JPanelMenuLayout = new javax.swing.GroupLayout(JPanelMenu);
        JPanelMenu.setLayout(JPanelMenuLayout);
        JPanelMenuLayout.setHorizontalGroup(
            JPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(StatusAuthorization1, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
        );
        JPanelMenuLayout.setVerticalGroup(
            JPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelMenuLayout.createSequentialGroup()
                .addContainerGap(422, Short.MAX_VALUE)
                .addComponent(StatusAuthorization1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(JPanelMenu);

        JTabMain.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Load Master Item");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(384, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );

        JTabMain.addTab("Status Load", jPanel2);

        jSplitPane1.setRightComponent(JTabMain);

        jLabel1.setText("Server : ");

        jLabel2.setText("Database :");

        jLabel3.setText("Os :");

        IPLocal.setText("Ip Public : ");

        IPLocal1.setText("Ip Local :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StatusServer1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IPLocal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StatusIPPublic, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IPLocal1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StatusIPLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StatusDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StatusOs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(IPLocal1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(StatusOs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(StatusIPLocal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(StatusDatabase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(StatusServer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(StatusIPPublic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(IPLocal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1063, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setSize(new java.awt.Dimension(1073, 508));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IPLocal;
    private javax.swing.JLabel IPLocal1;
    private javax.swing.JPanel JPanelMenu;
    public javax.swing.JTabbedPane JTabMain;
    private javax.swing.JLabel StatusAuthorization1;
    private javax.swing.JLabel StatusDatabase;
    private javax.swing.JLabel StatusIPLocal;
    private javax.swing.JLabel StatusIPPublic;
    private javax.swing.JLabel StatusOs;
    private javax.swing.JLabel StatusServer1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JPopupMenu jPopupMenu4;
    private javax.swing.JPopupMenu jPopupMenu5;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JSplitPane jSplitPane1;
    private java.awt.Menu menu1;
    private java.awt.Menu menu10;
    private java.awt.Menu menu11;
    private java.awt.Menu menu12;
    private java.awt.Menu menu13;
    private java.awt.Menu menu14;
    private java.awt.Menu menu2;
    private java.awt.Menu menu3;
    private java.awt.Menu menu4;
    private java.awt.Menu menu5;
    private java.awt.Menu menu6;
    private java.awt.Menu menu7;
    private java.awt.Menu menu8;
    private java.awt.Menu menu9;
    private java.awt.MenuBar menuBar1;
    private java.awt.MenuBar menuBar2;
    private java.awt.MenuBar menuBar3;
    private java.awt.MenuBar menuBar4;
    private java.awt.MenuBar menuBar5;
    private java.awt.MenuBar menuBar6;
    private java.awt.MenuBar menuBar7;
    private java.awt.PopupMenu popupMenu1;
    private java.awt.PopupMenu popupMenu2;
    private java.awt.PopupMenu popupMenu3;
    // End of variables declaration//GEN-END:variables
    class ImagePanel extends JPanel {

        private Image img;

        public ImagePanel(String img) {
          this(new ImageIcon(img).getImage());
        }

        public ImagePanel(Image img) {
          this.img = img;
          Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
          setPreferredSize(size);
          setMinimumSize(size);
          setMaximumSize(size);
          setSize(size);
          setLayout(null);
        }

        public void paintComponent(Graphics g) {
          g.drawImage(img, 0, 0, null);
        }
    }
    
}
