/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arprast.hrdpro;

import Sistem.RenderingKanan;
import Sistem.RenderingTengah;
import Sistem.SatuUntukSemua;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author pein
 */
public class Hrd_Pegajian extends javax.swing.JPanel {

    /**
     * Creates new form Hrd_Pegajian
     */
    private TableCellRenderer   kanan = new RenderingKanan();
    private TableCellRenderer   tengah= new RenderingTengah();
    private SatuUntukSemua      Satu = new SatuUntukSemua();
    private DefaultTableModel   Modeltabel             = new DefaultTableModel();
    private Sistem.DB_MYSQL KD = new Sistem.DB_MYSQL();
    private Connection K = KD.createConnection();
    private Sistem.TanggalSistem N       = new Sistem.TanggalSistem();
    boolean Edit3 = true;
    
    public Hrd_Pegajian() {
        initComponents();
        
        Tabel(null , null);
        //AmbilDariDatabase(Periode(0), 0);
        AmbilDariDatabase(AmbilDataPeriod1(), 1);
        Aksi();
    }
     private void  Aksi(){
         
        JButtonPrintForm.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 PrintOut();
            }
        });
         
        jTable1.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                   ViewDataDetailGaji(e);
                }
            }   
        });
        
        JButtonPelamarSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 //Edit= true;
                 JTextFieldSearch.setText("");
                 SearchTable(jTable1,Modeltabel);
                 
                 try {
                 Thread.sleep(1000);
                 } 
                 catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                 }
                 Edit = true;
                SaveDatabase();
            }
        });
        
        JButtonPelamarNew.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 Edit = false;
                 Edit3 = true;
                 AmbilDariDatabase(Periode(0), 0);
                 JButtonPelamarSave.setEnabled(true);
                 jLabel1.setText("New data gaji periode " + Periode(1));
            }
        });
        
        
        jButtonRefresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 Edit = false;
                 Edit3 = false;
                 AmbilDariDatabase(AmbilDataPeriod1(), 1);
                 JButtonPelamarSave.setEnabled(true);
            }
        });
       
    }
     
     private void PrintOut(){
         
        
        DefaultTableModel de = (DefaultTableModel)jTable1.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();

        //hashMap.put("Judul", JLabelPemberitahuanPb.getText());
        
        try {
            String x    = System.getProperty("user.dir")+"\\PrintOut\\DataGaji.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }
     }
     
     private void  ViewDataDetailGaji (MouseEvent e){
        Hrd_PegajianViewAll xx = new Hrd_PegajianViewAll(new javax.swing.JFrame(), true );
        String NIk = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
        String[] Data = {(String) jTable1.getValueAt(jTable1.getSelectedRow(), 2),NIk, (String) jTable1.getValueAt(jTable1.getSelectedRow(), 4) };
        xx.AmbilDariDatabase(NIk, Periode2, Data );
        System.out.println(Periode2 + " dan " + (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1));
        xx.setVisible(true);               
    } 
    
     private String AmbilDataPeriod1(){
        String PeriodRefresh   = kazaoCalendarDate1.getKazaoCalendar().getShortDate();
                
        N.SetKazaoToBlnIndo(PeriodRefresh);
        N.SetKazaoToThnIndo(PeriodRefresh);

        PeriodRefresh = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
        return PeriodRefresh;
    }
    
    private String Periode(int a){
        N.SetTahunSis();
        N.SetBulanSis();
        N.SetTanggalSis();
        if (a == 0){
             return    N.GetTahunSisString()+ N.GetBulanSisString();
        }
        return     N.GetBulanSisString() + " " + N.GetTahunSisString();
    }
    
     private void Tabel(Vector Header, Vector Data){ 
        
        String header[] = {"No", "NIK", "Nama","JK","Jabatan","Gaji Pokok", "Jumlah Tunjangan","Jumlah Lembur","Bonus / Insentif","Potongan", "Gaji Bersih"}; 
        /*","",
         * Jika import null
         */     
        if (Header == null){
            Modeltabel = new DefaultTableModel(null,header) {
                @Override
                public boolean isCellEditable(int rowIndex, int colIndex) {
                                    /*
                                    if(colIndex == 12) {return Edit3 ;} */
                                    return false;   //Disallow the editing of any cell
                            }
            };
            jTable1.setModel(Modeltabel);
            Tabel2 (jTable1, Modeltabel,1);
        }
        else {
            if ((header.length -2 ) == Header.size()  ){           
                Header.clear();        
                for (int b = 0; b < header.length  ; b++){
                    Header.add(header[b]);
                }
                Modeltabel = new DefaultTableModel(Data,Header) {
                    @Override
                    public boolean isCellEditable(int rowIndex, int colIndex) {
                                //nilai false agar tidak bisa di edit
                                if(colIndex ==8) {return true ;} 
                                return false;   //Disallow the editing of any cell
                         }
                };
                jTable1.setModel(Modeltabel);
                Tabel2 (jTable1,Modeltabel ,2);         
            }
            else {
                JOptionPane.showMessageDialog(null, "xls tidak sesuai format");
                System.out.println("xls tidak sesuai format");
            }                                 
        }     
    }
     
     public void SearchTable(final JTable JTabelBarang, final DefaultTableModel Modeltabel){
         String text = JTextFieldSearch.getText();
               final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
                JTabelBarang.setRowSorter(sorter);
               if (text.length() == 0) {
                 sorter.setRowFilter(null);
               } else {
                 try {
                   sorter.setRowFilter(
                       RowFilter.regexFilter("(?i)"+text));
                       //System.out.println(sorter.getViewRowCount());
                 } catch (PatternSyntaxException pse) {
                   System.err.println("Bad regex pattern");
                 }
               }
     }
       
     /*
      * Jika a = 1 dari database
      * jika a = 2 dari xls
      */
     boolean Edit2 = false , Edit = false;
     private void Tabel2 (final JTable JTabelBarang, final DefaultTableModel Modeltabel, int a  ){

       /*
        * Membuat sort pada tabel
        * Search Data
        * SearchTable(jTable1,Modeltabel);
        */     
        Edit2 = true;
        JTextFieldSearch.addKeyListener(new KeyListener(){
            @Override
            public void keyReleased(KeyEvent e) {
               SearchTable(JTabelBarang,Modeltabel);
               if (JTextFieldSearch.getText().equals("")){
                    Edit2 = true;
               }
               else{
                    Edit2 = false;
               }    
            }     
            @Override
            public void keyTyped(KeyEvent e) {
                }
           @Override
           public void keyPressed(KeyEvent e) {
            }
        });

        /*
         * Rata tengah atau kanan table
         */
        for (int ab = 0; ab <= 10 ; ab++){
            JTabelBarang.getColumnModel().getColumn(ab).setCellRenderer( tengah );
            if (ab >= 5 ){
                JTabelBarang.getColumnModel().getColumn(ab).setCellRenderer( kanan );
            }
        }


        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */

        int jarak_colom[] = {40,100,150,40, 200, 100, 100 ,100 ,100 ,100, 100};
        Sistem.Colom_table ukuran_colom = new Sistem.Colom_table();
        ukuran_colom.ukuran_colom(JTabelBarang, jarak_colom); 

       /*
        * Biar ukuran column JTable sesuai column
        *
       tableWidth  = Modeltabel.getColumnCount() * 150;
       tableHeight = Modeltabel.getRowCount() * 25;
       JTabelBarang.setPreferredSize(new Dimension(
       tableWidth, tableHeight));               


        /*
         * Memasukan tombol ke jtable
         */
        JTabelBarang.setName("absensi");
        //JTabelBarang.getColumnModel().getColumn(12).setCellRenderer(  new ButtonJTable("delete"));
        //JTabelBarang.getColumnModel().getColumn(12).setCellEditor( new  ButtonJTableKeDua(new JCheckBox(),Modeltabel, JTabelBarang));

        /*
         * Disable drag colum tabel
         */       
        JTabelBarang.getTableHeader().setReorderingAllowed(false);
     }
     
     private boolean Validasi(){
        int ab = jTable1.getRowCount() ;
        for(int i=0;i< ab;i++){
            GG          =jTable1.getValueAt(i, 7).toString();
            if (GG.equals("") ){
                JOptionPane.showMessageDialog(null, "data absensi masuk ada yang kosong");
                return false;
           }
        }
        return true;
    }
     
     private int RubahKeNol(String Data){
         if (Data.equals("")){
             return 0;
         }
         return Integer.valueOf(Data).intValue();
     }
     
     boolean TutupCek = true;
     private boolean CekDataAbsensi(String Periode){
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               String Query; 
               
               Query  = "select count(periode) as jumlah_row from header_gaji where periode = '"+ Periode + "'";
               if (TutupCek ==  false ){
                   return true;
               }
               HQ = stm.executeQuery(Query);              
               while(HQ.next()  ){
                   int AAA       = HQ.getInt("jumlah_row");
                   if (AAA == 0 ){
                       TutupCek = false;
                       return true;
                   }
                    JOptionPane.showMessageDialog (null, "Data absensi bulan ini sudah ada", "Error" , JOptionPane.ERROR_MESSAGE);
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
           return false;
     }
     private void DeleteData(String periode){
         Sistem.DB_MYSQL_Koneksi MYSQL = new Sistem.DB_MYSQL_Koneksi();
         MYSQL.MysqlDelete("delete from header_gaji where periode ='" + periode + "'");  
     }
     
     String AA,BB,CC,DD,EE,FF,GG, HH, II, JJ, KK, Period;
     private void SaveDatabase(){           
          try {
                /*
                 * validasi jika table kosong
                 */
                if (Edit == true){
                   DeleteData(Periode(0));                             
                 }
                
                if (Validasi() && CekDataAbsensi(Periode(0)) ) {
                    
                    int a = jTable1.getRowCount() ;
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    for(int i=0;i< a;i++){

                        int no      =Integer.valueOf(jTable1.getValueAt(i, 0).toString()).intValue();
                        AA          =jTable1.getValueAt(i, 1).toString();
                        BB          =jTable1.getValueAt(i, 2).toString();
                        CC          =jTable1.getValueAt(i, 3).toString();
                        DD          =jTable1.getValueAt(i, 4).toString();
                        EE          =jTable1.getValueAt(i, 5).toString(); //gaji pokok
                        FF          =jTable1.getValueAt(i, 6).toString();
                        GG          =jTable1.getValueAt(i, 7).toString();
                        HH          =jTable1.getValueAt(i, 8).toString(); //bonus
                        II          =jTable1.getValueAt(i, 9).toString();
                        JJ          =jTable1.getValueAt(i, 10).toString();
                        
                       tot_tunjangan    = t_jabatan + t_makan + t_transport + t_jamsostek + t_kesehatan + t_lainnya;
                       tot_potongan     = p_pajak + p_pinjaman_karywan + p_pinjaman_lainnya + p_jamsostek + p_asr + p_denda_kedisipliman;
                       gaji_bersih      = gaji_pokok + tot_tunjangan + Lembur + Bonus - tot_potongan;
                       
                        Stm.executeUpdate("INSERT INTO header_gaji "
                                + " values ('"+AA +"','"+Periode(0) +"','"+BB +"','"+CC
                                + "','"+ DD + "','"+ EE + "','"+ t_jabatan + "','"+  t_makan+ "','"+ t_transport
                                + "','"+ t_jamsostek + "','"+  t_kesehatan + "','"+t_lainnya+"','"+ GG + "','"+ HH + "','"+ p_pajak 
                                + "','"+ p_pinjaman_karywan + "','"+ p_pinjaman_lainnya + "','"+ p_jamsostek + "','"+ p_asr + "','"+ p_denda_kedisipliman + "',now() )");
                        }
                    
                     //AmbilDariDatabase(Periode(), 1);
                     JButtonPelamarSave.setEnabled(false);
                     Edit = false;
                     jLabel1.setText("Berhasil di saved");
                    }
                 }
                 catch (Exception Ex){
                     if (Ex.toString().contains("PRIMARY")){
                         JOptionPane.showMessageDialog(null, "Ada nama data yang sama: Tidak berhasil di save");
                         Sistem.DB_MYSQL_Koneksi MYSQL = new Sistem.DB_MYSQL_Koneksi();
                         MYSQL.MysqlDelete("delete from master_barang_convert_satuan ");    
                     }
                     jLabel1.setText("Gagal di saved");
                     System.out.println(Ex + " 1i78861987");
                     //LabelPeringantan.setText("Data error " + Ex);                  
              }                   
     }
     
     /*
      * a = 0 = new
      * a = 1 = ambil data yang sudah disimpan pada header_gaji
      */
     int Lembur = 100000, Bonus = 0, gaji_pokok = 0;
     int t_jabatan = 10000, t_makan = 10000, t_transport = 10000, t_jamsostek = 10000, t_kesehatan = 10000, t_lainnya = 0;
     int p_pajak = 0, p_pinjaman_karywan = 0, p_pinjaman_lainnya = 10, p_jamsostek = 0, p_asr = 0, p_denda_kedisipliman = 0;
     int tot_tunjangan, tot_potongan, gaji_bersih, gaji_bruto;
     String Periode2;
     private void AmbilDariDatabase(String Periode, int a){
        Satu.HapusDataJTabel(jTable1);
        
        Lembur = 10000; Bonus = 0; gaji_pokok = 0;
        t_jabatan = 0; t_makan = 66000; t_transport = 66000; t_jamsostek = 123800; t_kesehatan = 13800; t_lainnya = 0;
        p_pajak = 0; p_pinjaman_karywan = 0; p_pinjaman_lainnya = 10; p_jamsostek = 0; p_asr = 0; p_denda_kedisipliman = 0;
     
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               String Query = null; 
               /* = "SELECT   hs.nama, hs.jenis_kelamain, "
                           + " hp.nik, hp.cabang,"
                           + " hp.cabang_lokasi, hp.jabatan "
                           + "from  header_pelamar as hs inner join header_pegawai as hp  "
                           + " on hp.no_pelamar = hs.trans_no  "
                           + " where hp.status_pekerja = '"+ 0 +"'  order by hs.nama "; */
               if (a == 0){
                   Query  = "SELECT   hs.nama, hs.jenis_kelamain,  hp.nik,  hp.jabatan , hp.gaji_pokok,  " +
                    " ( SELECT count(nik)  FROM header_lembur WHERE nik = hp.nik and periode = '"+Periode+" ' ) * "+ Lembur + " as lembur   " +
                    " from  header_pelamar as hs inner join header_pegawai as hp   on hp.no_pelamar = hs.trans_no  " +
                    " where hp.status_pekerja = '0'  order by hs.nama ";
               }
               else {
                   Query  = "SELECT  nik, nama, jk as jenis_kelamain, jabatan, "
                           + "gaji_pokok, lembur, bonus,t_jabatan, t_makan,"
                           + " t_transport,t_jamsostek, t_kesehatan, t_lainnya,"
                           + " pajak, pinjaman_karyawan, pinjaman_lainnya, pot_jamsostek, "
                           + "asr_kesehatan, denda_kedisipliman, periode "
                           + " from header_gaji where periode = '"+ Periode+ "' ";
               }
               
               HQ = stm.executeQuery(Query);              
               while(HQ.next()  ){
                   int No       = HQ.getRow();
                   AA           = HQ.getString("nik");
                   BB           = HQ.getString("nama");
                   EE           = HQ.getString("jenis_kelamain");
                   CC           = HQ.getString("jabatan");
                   gaji_pokok   = HQ.getInt("gaji_pokok");
                   //GG       = HQ.getString("cuti"); 
                   Lembur       = HQ.getInt("lembur"); 

                   if (a == 1){
                       t_jabatan = HQ.getInt("t_jabatan");
                       t_makan= HQ.getInt("t_makan");
                       t_transport= HQ.getInt("t_transport");
                       t_jamsostek= HQ.getInt("t_jamsostek");
                       t_kesehatan= HQ.getInt("t_kesehatan");
                       t_lainnya= HQ.getInt("t_lainnya");
                       p_pajak  = HQ.getInt("pajak");
                       p_pinjaman_karywan= HQ.getInt("pinjaman_karyawan");
                       p_pinjaman_lainnya= HQ.getInt("pinjaman_lainnya");
                       p_jamsostek= HQ.getInt("pot_jamsostek");
                       p_asr= HQ.getInt("asr_kesehatan");
                       p_denda_kedisipliman= HQ.getInt("denda_kedisipliman");
                       Periode2      = HQ.getString("periode");
                   }
                   if (a == 0) {
                        if (EE.equals("0")){
                        EE = "L";
                        }
                        else {
                            EE = "P";
                        }
                   }
                   
                   tot_tunjangan    = t_jabatan + t_makan + t_transport + t_jamsostek + t_kesehatan + t_lainnya;
                   tot_potongan     = p_pajak + p_pinjaman_karywan + p_pinjaman_lainnya + p_jamsostek + p_asr + p_denda_kedisipliman;
                   gaji_bruto       = gaji_pokok + + tot_tunjangan + Lembur + Bonus;
                   
                   //System.out.println(p_pajak + "  " + p_pinjaman_karywan + "  " + p_pinjaman_lainnya + "  " +p_jamsostek + "  " + p_asr + "  " + p_denda_kedisipliman + " coba ");
                   gaji_bersih      = gaji_bruto - tot_potongan;
                   
                   //System.out.println(gaji_pokok + "  " + tot_tunjangan + "  " + Lembur + "  " +Bonus + "  " + tot_potongan + "  " + gaji_bersih + " " + gaji_bruto);
                   
                   String[] add         = {HQ.getRow() + "", AA,BB,EE, CC,gaji_pokok +"",tot_tunjangan +"", Lembur+"",String.valueOf(Bonus).toString(), tot_potongan + "", gaji_bersih+""};
                   Modeltabel.addRow(add);      

               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
     }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bevelBorder1 = (javax.swing.border.BevelBorder)javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED);
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        JButtonPelamarNew = new javax.swing.JButton();
        JButtonPelamarSave = new javax.swing.JButton();
        JButtonPrintForm = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        JTextFieldSearch = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        kazaoCalendarDate1 = new org.kazao.calendar.KazaoCalendarDate();
        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonPelamarNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonPelamarNew.setText("New");

        JButtonPelamarSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/save_icon2.png"))); // NOI18N
        JButtonPelamarSave.setText("Save");

        JButtonPrintForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/print_icon2.png"))); // NOI18N
        JButtonPrintForm.setText("Print");

        jButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Refresh.png"))); // NOI18N
        jButtonRefresh.setText("Refresh");

        jLabel4.setText("Search  :");

        jLabel10.setText("Periode :");

        kazaoCalendarDate1.setFormat("mm/yyyy");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JButtonPelamarNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPelamarSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JButtonPrintForm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JTextFieldSearch)
                    .addComponent(kazaoCalendarDate1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRefresh)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGap(2, 11, Short.MAX_VALUE)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(kazaoCalendarDate1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(JButtonPelamarNew, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JButtonPrintForm, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JButtonPelamarSave)
                                .addComponent(jLabel10))
                            .addComponent(jButtonRefresh)))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonPelamarNew;
    private javax.swing.JButton JButtonPelamarSave;
    private javax.swing.JButton JButtonPrintForm;
    private javax.swing.JTextField JTextFieldSearch;
    private javax.swing.border.BevelBorder bevelBorder1;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDate1;
    // End of variables declaration//GEN-END:variables
}
