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
public class Hrd_Absensi extends javax.swing.JPanel {
    
    private TableCellRenderer   kanan = new RenderingKanan();
    private TableCellRenderer   tengah= new RenderingTengah();
    private SatuUntukSemua      Satu = new SatuUntukSemua();
    private DefaultTableModel   Modeltabel             = new DefaultTableModel();
    private Sistem.DB_MYSQL KD = new Sistem.DB_MYSQL();
    private Connection K = KD.createConnection();
    private Sistem.TanggalSistem N       = new Sistem.TanggalSistem();
    /*
     * disable endable edit colum
     */
    boolean Edit3 = true;

    /**
     * Creates new form Hrd_Absensi
     */
    public Hrd_Absensi() {
        initComponents();
        Tabel(null , null);
        AmbilDariDatabase(Periode(), 0);
        Aksi();
    }
    private void  Aksi(){
        JButtonPrintForm.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 PrintOut();
            }
        });
        JButtonPelamarSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 //Edit= true;
                 JTextFieldSearch.setText("");
                 SearchTable(jTable2,Modeltabel);
                 
                 try {
                 Thread.sleep(1000);
                 } 
                 catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                 }
                 
                SaveDatabase();
            }
        });
        
        JButtonPelamarNew.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 Edit = false;
                 Edit3 = true;
                 AmbilDariDatabase(Periode(), 0);
                 JButtonPelamarSave.setEnabled(true);
                 jLabel1.setText("New data absen periode " + Periode());
            }
        });
        
        jButtonDelete.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println(Period);
                 if (Period.equalsIgnoreCase(Periode())){
                      DeleteData(AmbilDataPeriod1()); 
                 }
                 else {
                     JOptionPane.showMessageDialog(null, "Periode tidak sesuai dengan periode sekarang \nTidak bisa diHapus");
                 }
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
        
        JButtonPelamarEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 AmbilDariDatabase(AmbilDataPeriod1(), 1);
                 if (Period.equalsIgnoreCase(Periode())){
                      Edit = true;
                      Edit3 = true;
                      jLabel1.setText("Status Edit");
                 }
                 else {
                     JOptionPane.showMessageDialog(null, "Periode tidak sesuai dengan periode sekarang \nTidak bisa diEdit");
                 }
            }
        });
    }
    private void PrintOut(){
        DefaultTableModel de = (DefaultTableModel)jTable2.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();

        //hashMap.put("Judul", JLabelPemberitahuanPb.getText());
        
        try {
            String x    = System.getProperty("user.dir")+"\\PrintOut\\DataAbsensi.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
    }
    
     private String AmbilDataPeriod1(){
        String PeriodRefresh   = kazaoCalendarDate1.getKazaoCalendar().getShortDate();
                
        N.SetKazaoToBlnIndo(PeriodRefresh);
        N.SetKazaoToThnIndo(PeriodRefresh);

        PeriodRefresh = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
        return PeriodRefresh;
    }
    
    private String Periode(){
        N.SetTahunSis();
        N.SetBulanSis();
        N.SetTanggalSis();
        return    N.GetTahunSisString()+ N.GetBulanSisString();
    }
    
     private void Tabel(Vector Header, Vector Data){ 
        
        String header[] = {"No", "NIK", "Nama","Jenis Kelamin","Jabatan", "Cabang", "Lokasi","Masuk", "Absen","Izin Cuti","Lembur","Sakit"}; 
        /*","",
         * Jika import null
         */     
        if (Header == null){
            Modeltabel = new DefaultTableModel(null,header) {
                @Override
                public boolean isCellEditable(int rowIndex, int colIndex) {
                                    if(colIndex == 7) {return Edit3 ;}
                                    if(colIndex == 8) {return Edit3 ;} //  nilai false agar tidak bisa di edit
                                    if(colIndex == 9) {return false ;}
                                    if(colIndex == 10) {return false ;}
                                    if(colIndex == 11) {return Edit3 ;}
                                    return false;   //Disallow the editing of any cell
                            }
            };
            jTable2.setModel(Modeltabel);
            Tabel2 (jTable2, Modeltabel,1);
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
                jTable2.setModel(Modeltabel);
                Tabel2 (jTable2,Modeltabel ,2);         
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
        * SearchTable(jTable2,Modeltabel);
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
        JTabelBarang.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelBarang.getColumnModel().getColumn(2).setCellRenderer( tengah );
        JTabelBarang.getColumnModel().getColumn(3).setCellRenderer( tengah );
        JTabelBarang.getColumnModel().getColumn(1).setCellRenderer( tengah );
        JTabelBarang.getColumnModel().getColumn(4).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(5).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(6).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(7).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(8).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(9).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(10).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(11).setCellRenderer( tengah ); 

        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */

        int jarak_colom[] = {40,100,150,100, 100, 200, 200, 50, 50 ,70 ,70 ,50};
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
        int ab = jTable2.getRowCount() ;
        for(int i=0;i< ab;i++){
            GG          =jTable2.getValueAt(i, 7).toString();
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
     
     private boolean CekDataAbsensi(String Periode){
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               String Query; 
               
               Query  = "select count(periode) as jumlah_row from header_absensi where periode = '"+ Periode + "'";
               
               HQ = stm.executeQuery(Query);              
               while(HQ.next()  ){
                   int AAA       = HQ.getInt("jumlah_row");
                   if (AAA == 0 ){
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
         MYSQL.MysqlDelete("delete from header_absensi where periode ='" + periode + "'");  
     }
     
     String AA,BB,CC,DD,EE,FF,GG, HH, II, JJ, KK, Period;
     private void SaveDatabase(){           
          try {
                /*
                 * validasi jika table kosong
                 */
                if (Edit == true){
                   DeleteData(Periode());                             
                 }
                
                if (Validasi() && CekDataAbsensi(Periode()) ) {
                    
                    int a = jTable2.getRowCount() ;
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    for(int i=0;i< a;i++){

                        int no      =Integer.valueOf(jTable2.getValueAt(i, 0).toString()).intValue();
                        AA          =jTable2.getValueAt(i, 1).toString();
                        BB          =jTable2.getValueAt(i, 2).toString();
                        CC          =jTable2.getValueAt(i, 3).toString();
                        DD          =jTable2.getValueAt(i, 4).toString();
                        EE          =jTable2.getValueAt(i, 5).toString();
                        FF          =jTable2.getValueAt(i, 6).toString();
                        GG          =jTable2.getValueAt(i, 7).toString();
                        HH          =jTable2.getValueAt(i, 8).toString();
                        II          =jTable2.getValueAt(i, 9).toString();
                        JJ          =jTable2.getValueAt(i, 10).toString();
                        KK          =jTable2.getValueAt(i, 11).toString();
                                               
                        Stm.executeUpdate("INSERT INTO header_absensi "
                                + " values ('"+AA +"','"+BB +"','"+CC +"','"+DD
                                +"','"+ EE + "','"+ FF + "','"+ RubahKeNol(GG) + "','"+  RubahKeNol(HH )+ "','"+ RubahKeNol( II )
                                + "','"+  RubahKeNol(JJ) + "','"+  RubahKeNol(KK) + "','"+Periode()+"', now())");
                        }             
                     AmbilDariDatabase(Periode(), 1);
                     JButtonPelamarSave.setEnabled(false);
                     JButtonPelamarEdit.setEnabled(true);
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
      * a = 1 = ambil data yang sudah disimpan pada header_absensi
      */
     private void AmbilDariDatabase(String Periode, int a){
        Satu.HapusDataJTabel(jTable2);
        
        /*
         * SELECT   hs.nama, hs.jenis_kelamain,  hp.nik, hp.cabang, hp.cabang_lokasi, hp.jabatan , 
        ( SELECT sum(jumlah_cuti)  FROM header_cuti WHERE nik = hp.nik and periode = ' ' ) as cuti, 
        ( SELECT count(nik)  FROM header_lembur WHERE nik = hp.nik and periode = ' ' ) as lembur
         from  header_pelamar as hs inner join header_pegawai as hp   on hp.no_pelamar = hs.trans_no  
         where hp.status_pekerja = '0'  order by hs.nama 
         */

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
                   Query  = "SELECT   hs.nama, hs.jenis_kelamain,  hp.nik, hp.cabang, hp.cabang_lokasi, hp.jabatan , " +
                    " ( SELECT sum(jumlah_cuti)  FROM header_cuti WHERE nik = hp.nik and periode = '"+Periode+" ' ) as cuti, " +
                    " ( SELECT count(nik)  FROM header_lembur WHERE nik = hp.nik and periode = '"+Periode+" ' ) as lembur " +
                    " from  header_pelamar as hs inner join header_pegawai as hp   on hp.no_pelamar = hs.trans_no  " +
                    " where hp.status_pekerja = '0'  order by hs.nama ";
               }
               else {
                   Query  = "SELECT  nik, nama, jenis_kelamin as jenis_kelamain, jabatan, cabang, cabang_lokasi, masuk, absen, izin_cuti as cuti , lembur, sakit, periode "
                           + " from header_absensi where periode = '"+ Periode+ "' ";
               }
               
               
               HQ = stm.executeQuery(Query);              
               while(HQ.next()  ){
                   int No   = HQ.getRow();
                   AA       = HQ.getString("nik");
                   BB       = HQ.getString("nama");
                   EE       = HQ.getString("jenis_kelamain");
                   CC       = HQ.getString("jabatan");
                   DD       = HQ.getString("cabang");
                   FF       = HQ.getString("cabang_lokasi");
                   GG       = HQ.getString("cuti"); 
                   HH       = HQ.getString("lembur"); 
                   
                   II = ""; JJ = "";KK = "";
                   if (a == 1){
                       II =  HQ.getString("masuk"); 
                       JJ =  HQ.getString("absen"); 
                       KK =  HQ.getString("sakit");
                       Period = HQ.getString("periode"); 
                   }
                   
                   if (EE.equals("0")){
                       EE = "L";
                   }
                   else {
                       EE = "P";
                   }
                   
                   if (GG == null){
                       GG = "0";
                   }
                   
                   String[] add         = {HQ.getRow() + "", AA,BB,EE, CC,DD, FF, II,JJ, GG, HH,KK};
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        JButtonPelamarNew = new javax.swing.JButton();
        JButtonPelamarSave = new javax.swing.JButton();
        JButtonPelamarEdit = new javax.swing.JButton();
        JButtonPrintForm = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        JTextFieldSearch = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        kazaoCalendarDate1 = new org.kazao.calendar.KazaoCalendarDate();
        jLabel1 = new javax.swing.JLabel();
        jButtonDelete = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jPanel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonPelamarNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonPelamarNew.setText("New");

        JButtonPelamarSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/save_icon2.png"))); // NOI18N
        JButtonPelamarSave.setText("Save");

        JButtonPelamarEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/edit_icon2.png"))); // NOI18N
        JButtonPelamarEdit.setText("Edit");

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

        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Delete.png"))); // NOI18N
        jButtonDelete.setText("Delete");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPelamarNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPelamarSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPelamarEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                        .addGap(2, 10, Short.MAX_VALUE)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(kazaoCalendarDate1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(JButtonPelamarNew, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JButtonPelamarEdit)
                                .addComponent(JButtonPrintForm, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JButtonPelamarSave)
                                .addComponent(jLabel10)
                                .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButtonRefresh)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonPelamarEdit;
    private javax.swing.JButton JButtonPelamarNew;
    private javax.swing.JButton JButtonPelamarSave;
    private javax.swing.JButton JButtonPrintForm;
    private javax.swing.JTextField JTextFieldSearch;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDate1;
    // End of variables declaration//GEN-END:variables
}
