/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arprast.hrdpro;

import Sistem.ButtonJTable;
import Sistem.ButtonJTableKeDuaLocal;
import Sistem.ComponentHanyaAngka;
import Sistem.RenderingTengah;
import Sistem.SatuUntukSemua;
import Sistem.app_search1;
import Sistem.app_search_data_pegawai;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author pein
 */
public class Hrd_Form extends javax.swing.JPanel {

    /**
     * Creates new form Hrd_Form
     */
    private DefaultTableModel   Modeltabel2             = new DefaultTableModel();
    private TableCellRenderer   tengah= new RenderingTengah();
    private SatuUntukSemua      Satu = new SatuUntukSemua();
    private Sistem.DB_MYSQL KD = new Sistem.DB_MYSQL();
    private Connection K = KD.createConnection();
    private Sistem.TanggalSistem N       = new Sistem.TanggalSistem();
    private Sistem.TransNo TN = new Sistem.TransNo();
    private boolean Edit;
    private ComponentHanyaAngka AntiHuruf = new ComponentHanyaAngka();
    int NilaiSave = 0 ;
    public Hrd_Form() {
        initComponents();
        Tabel();
        Tampilan();
        Aksi();
        AmbilDataDariDatabase(Periode(), "", "", "");
        
    }
    
    private void Tampilan(){
        /*
         * Static JPanel
         * Di Gunakan agar jika ada penambahan componet tidak berubah
         * biar tidak bergerak
         */

        
        AntiHuruf.SetAntiAngkaLimit(JTextJumlahCuti, 3);
        AntiHuruf.SetAntiAngkaLimit(JTextNoHPDihub, 15);
        
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserCombo1.setDateFormat(dt1); 
        dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserCombo2.setDateFormat(dt1); 
        dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserCombo3.setDateFormat(dt1); 
        dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserCombo4.setDateFormat(dt1); 
        dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserCombo5.setDateFormat(dt1); 
        dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserCombo6.setDateFormat(dt1); 
        
          
        
    }
    
    private String AmbilDataPeriod1(){
        String PeriodRefresh   = kazaoCalendarDate1.getKazaoCalendar().getShortDate();
                
        N.SetKazaoToBlnIndo(PeriodRefresh);
        N.SetKazaoToThnIndo(PeriodRefresh);

        PeriodRefresh = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
        return PeriodRefresh;
    }
    
    private void Aksi(){
        
        jComboBox1.setSelectedIndex(-1);
        final JTextField JTextFieldItem = (JTextField)jComboBox1.getEditor().getEditorComponent();
        JTextFieldItem.setText("");
        JTextFieldItem.addKeyListener(new app_search1(jComboBox1));
        
        jComboBox1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Reset();
                
                AmbilDataSearch((String)jComboBox1.getSelectedItem());
               // new javax.swing.DefaultComboBoxModel( app_search_data_pegawai.getData().toArray());
            }
        });
        
        
         jTabbedPane1.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent X) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) X.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                String coba = sourceTabbedPane.getTitleAt(index);
                
                if (sourceTabbedPane.getTitleAt(index).equals("Cuti") ){
                       NilaiSave = 0;
                }
                else if (sourceTabbedPane.getTitleAt(index).equalsIgnoreCase("Lembur") ){                  
                      NilaiSave = 1;
                }
              }
        });

        
        JButtonPelamarSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                if (NilaiSave ==  0){
                    if (Validasi (0)){               
                        DisabelEnable(false);
                        Component[] Tombol = {JButtonPelamarNew, JButtonPelamarSave, JButtonPelamarEdit};
                        boolean[] Benar = {true, false, true};
                        Satu.LogikaComponent(Tombol, Benar);
                        SaveDB(NilaiSave);
                        Edit = false;
                    }
                }
                else if (NilaiSave == 1){
                    if (Validasi (1)){               
                        DisabelEnable(false);
                        Component[] Tombol = {JButtonPelamarNew, JButtonPelamarSave, JButtonPelamarEdit};
                        boolean[] Benar = {true, false, true};
                        Satu.LogikaComponent(Tombol, Benar);
                        SaveDB(NilaiSave);
                        Edit = false;
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "Tidak dapat di save : 29663 ");
                }
                
            }
        });
        
        JButtonPelamarPrintForm.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                if (NilaiSave ==  0){
                    if (Validasi (0)){               
                        PrintOutCuti();
                    }
                }
                else if (NilaiSave == 1){
                    if (Validasi (1)){               
                       PrintOutLembur();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "Tidak dapat di save : 29663 ");
                }
                
            }
        });
        
        JButtonPelamarNew.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                DisabelEnable(true);         
                Component[] Tombol = {JButtonPelamarNew, JButtonPelamarSave, JButtonPelamarEdit};
                boolean[] Benar = {true, true, true};
                Satu.LogikaComponent(Tombol, Benar);
                Reset();
                Edit = false;
            }
        });
        
        JButtonPelamarEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                DisabelEnable(true);        
                Component[] Tombol = {JButtonPelamarNew, JButtonPelamarSave, JButtonPelamarEdit};
                boolean[] Benar = {true, true, true};
                Satu.LogikaComponent(Tombol, Benar);
                Edit = true;
                LabelPeringantan.setText("Status Edit");
            }
        });

        JButtonPelamarPrintForm.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                PrintOut();
            }
        });
        jButtonRefresh1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                AmbilDataDariDatabase(AmbilDataPeriod1(), (String) jComboBox2.getSelectedItem(), (String)BoxJabatan1.getSelectedItem(), (String)BoxCabang2.getSelectedItem());
            }
        });
        
        jTable2.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    
                    Component[] data  = {dateChooserCombo1, dateChooserCombo2,dateChooserCombo3,dateChooserCombo4,dateChooserCombo5,dateChooserCombo6};
                    Satu.LogikaComponent(data, true);
                    ViewDataDatabasePelamar((String )jTable2.getValueAt(jTable2.getSelectedRow(), 2),(String )jTable2.getValueAt(jTable2.getSelectedRow(), 1));
                    LabelPeringantan.setText("");
                    
                    Edit = false;
                    DisabelEnable(false); 
                }
            }   
        });
    }
    
    private void PrintOutCuti(){
        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();
        hashMap.put("nama", JTextNama.getText());
        hashMap.put("jabatan", JTextJabatan.getText());
        hashMap.put("no_induk", JLabelNoNik.getText());
        
        hashMap.put("periode_cuti", JTextPeriode.getText());
        hashMap.put("keperluan", JTextKerperluan.getText());
        hashMap.put("hari_tanggal", dateChooserCombo1.getText());
        hashMap.put("hari_tanggal_masuk", dateChooserCombo2.getText());
        hashMap.put("tanggal_cuti", JTextNama.getText());
        hashMap.put("jumlah_cuti", JTextJumlahCuti.getText());
        hashMap.put("sisa_cuti", JTextJumlahCuti1.getText());
        hashMap.put("tlpn_dihu", JTextNoHPDihub.getText());

        try {
            String x    = System.getProperty("user.dir")+"\\PrintOut\\FormCuti.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap,  new    JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }
   }
    private void PrintOutLembur(){
        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();
        
        hashMap.put("nama", JTextNama.getText());
        hashMap.put("jabatan", JTextJabatan.getText());
        hashMap.put("no_induk", JLabelNoNik.getText());
        
        hashMap.put("tugas1", JTextAreaAlamat.getText());
        hashMap.put("tugas2", JTextAreaAlamat1.getText());
        hashMap.put("tanggal1", kazaoCalendarTime1.getCalendar().getTime() + " "+ dateChooserCombo3.getText());
        hashMap.put("tanggal2", kazaoCalendarTime2.getCalendar().getTime() + " "+dateChooserCombo4.getText());
        hashMap.put("tanggal3", kazaoCalendarTime3.getCalendar().getTime() + " "+dateChooserCombo5.getText());
        hashMap.put("tanggal4", kazaoCalendarTime4.getCalendar().getTime() + " "+dateChooserCombo6.getText());

        try {
            String x    = System.getProperty("user.dir")+"\\PrintOut\\FormLembur.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap,  new    JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }
   }
    
    private void AmbilDataSearch(String Data){ 
        try {
            Reset();
            Data = Data.replaceAll("--.*", "");  
            Data = Data.replaceAll("[\\s]$", "");  
            AmbilDatabaseSearch(Data, 0);
        }
        catch (Exception X){
        }
    }
    
    private String TanggalSekarang(){
        return N.GetTglNow() + " - " +  N.GetBlnNow() + " - " + N.GetThnNow();
    }
    
    /*
     * a = 0 => new data
     * a = 1 => data edit cuti
     */
    private void AmbilDatabaseSearch(String Data, int a){
        
        /*
         * Isi data ke Tabel dari database
         * "No", "No Fomulir","Nama ", "Jenis Kelamin","Agama","No Hp", "No Telphone", "Email","periode ", "Action"
         */      
        int baris;       
        ResultSet HQ = null;
        ResultSet HQ2 = null;

           try {
               
               String Nama = null, Query, Jabatan = null, Tanggal = null, Nik = null;
               int SisaCuti = 0, Cuti = 0;
               
               Statement stm = K.createStatement();
               if (a == 0 ){
                   Query = "SELECT a.no_pelamar, a.jabatan, a.cuti,  b.nama, a.nik   "
                           + "from header_pegawai as a inner join header_pelamar as b where a.nik = '"+Data +"'  ";
               }
               else if (a == 1){
                    Query = "SELECT a.cuti  from header_pegawai as a  where a.nik = '"+Data +"'  ";
               }
               else {
                   JOptionPane.showMessageDialog(null, "Program error 398293");
                   Query = "";
               }
               
               Statement stm2 = K.createStatement();
               String Query2 = "SELECT sum(jumlah_cuti) as jumlah FROM header_cuti WHERE nik = '"+Data +"' and periode = '"+Periode()+ "'";
               
               HQ2 = stm2.executeQuery(Query2);              
               while(HQ2.next()  ){
                   Cuti         = HQ2.getInt("jumlah");
               }
               
               HQ = stm.executeQuery(Query);              
               baris = HQ.getRow();

               while(HQ.next()  ){
                   if (a == 0 ){
                        Nama         = HQ.getString("nama");
                        Jabatan      = HQ.getString("jabatan");
                        Nik          = HQ.getString("nik");
                        Tanggal      = TanggalSekarang();
                   }

                   SisaCuti     = HQ.getInt("cuti");
               }
               
               /*
                * Hitung sisa cuti
                */
               SisaCuti = SisaCuti - Cuti;
               if (a == 0 ){
                    JTextNama.setText(Nama);
                    JTextJabatan.setText(Jabatan);
                    JLabelNoNik.setText(Nik);
                    JLabelTanggal.setText(Tanggal);
               }
               
               JTextJumlahCuti1.setText(SisaCuti+"");
               
           }
           catch(Exception ex){
               JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
    }
    /*
     * a = 0 => cuti
     * b = 1 => lembur
     * 
     */
     private boolean Validasi (int a){
         
        if (JLabelNoNik.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Nik Kosong");
                    return false;
        }

        switch (a){
            case 0 :
                JTextField[] DataField = {JTextNama,JTextPeriode, JTextKerperluan , JTextJumlahCuti, JTextJabatan , JTextNoHPDihub };
                String[] DataError = {"Nama Kosong", "Periode Kosong", "Keperluan Kosong", "Jumlah Cuti Kosong", "Jabatan Kosong", "No yang Bisa di Hub Kosong"
                };
                if ( JTextJumlahCuti1.getText().equals("") ){
                    JOptionPane.showMessageDialog(null, "Data jumlah cuti kosong");
                    return false;
                }
                
                if (Integer.valueOf(JTextJumlahCuti1.getText()).intValue() <= 0){
                   JOptionPane.showMessageDialog(null, "Cuti anda sudah habis");
                   return false;
                }
                return Satu.ValidasiData(DataField, DataError);
            case 1 :
                if (JTextAreaAlamat.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Rencana lembur kosong");
                    return false;
                }
                else if (JTextAreaAlamat.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Relialisasi lembur kosong");
                    return false;
                }
                
                return true;
        }
            return false;
        
    }
     private void DisabelEnable(boolean BenarSalah){
        Component DataComponet [] = {dateChooserCombo1, dateChooserCombo2, dateChooserCombo3, dateChooserCombo4, 
            dateChooserCombo5, dateChooserCombo6, jComboBox1, JTextPeriode,JTextKerperluan,JTextJumlahCuti,JTextNoHPDihub, JTextAreaAlamat, JTextAreaAlamat1
          
        };
        Satu.LogikaComponent(DataComponet, BenarSalah);
    }
    
     void Reset(){
        Edit = false;
        JTextField[] DataField = {JTextNama, JTextPeriode, JTextKerperluan, 
            JTextJumlahCuti, JTextNoHPDihub };
        JLabel Label[] = {LabelPeringantan, JLabelNoNik};
        Satu.ResetTampilan(DataField, Label);
        Satu.HapusDataJTabel(jTable2);
     }
     
     void PrintOut(){
         
     }
     private void Tabel(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No", "Jenis","No Form","NIK", "Nama","Cabang","Periode", "Action"};
        Modeltabel2 = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 7 ) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        jTable2.setModel(Modeltabel2);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel2);
        jTable2.setRowSorter(sorter);
        
        /*
         * Rata tengah atau kanan table
         */
        jTable2.getColumnModel().getColumn(0).setCellRenderer( tengah );
        jTable2.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        jTable2.getColumnModel().getColumn(2).setCellRenderer( tengah );
        jTable2.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        jTable2.getColumnModel().getColumn(4).setCellRenderer( tengah ); 
        jTable2.getColumnModel().getColumn(5).setCellRenderer( tengah ); 
        jTable2.getColumnModel().getColumn(6).setCellRenderer( tengah ); 
        jTable2.getColumnModel().getColumn(7).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,60,200,200,100,100, 100,100 };
        Sistem.Colom_table ukuran_colom = new Sistem.Colom_table();
        ukuran_colom.ukuran_colom(jTable2, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        jTable2.setName("form");
        jTable2.getColumnModel().getColumn(7).setCellRenderer(  new ButtonJTable("Delete"));
        jTable2.getColumnModel().getColumn(7).setCellEditor( new  ButtonJTableKeDuaLocal(new JCheckBox(),Modeltabel2, jTable2));
       
        /*
         * Disable drag colum tabel
         */       
        jTable2.getTableHeader().setReorderingAllowed(false); 
        
    }
     
     private String Periode(){
        N.SetTahunSis();
        N.SetBulanSis();
        N.SetTanggalSis();
        return    N.GetTahunSisString()+ N.GetBulanSisString();
    }
      private String NoCuti(){       
        /*
         * SELECT trans_no , periode FROM header_pembelian where periode  = WhereSyarat order by  key_no desc limit 0,1
         */       
        TN.SetTransNoPo("CT", "key_no", "periode", "header_cuti", Periode());
        return TN.GetTransNoPo();
    }
      private String NoLembur(){       
        /*
         * SELECT trans_no , periode FROM header_pembelian where periode  = WhereSyarat order by  key_no desc limit 0,1
         */       
        TN.SetTransNoPo("LB", "key_no", "periode", "header_lembur", Periode());
        return TN.GetTransNoPo();
    }
     void ViewDataDatabasePelamar(String TransNo, String Jenis){
        //Reset(); 
        
        int baris;       
        ResultSet HQ = null;
           try {

               String Nama    ;
               String NIK     ;
               String NoForm  ;
               String Period, TanggalMasuk, TanggalKeluar, Telpone, Keperluan, Tanggal1, Tanggal2 ,Tanggal3, Tanggal4, CreateDate , JumlahCuti ;
               String Jabatan  ;
               Statement stm = K.createStatement();
               String Query = null;

               /*
                * "No", "Jenis",no form "NIK", "Nama","Cabang","Periode", "Action"};
                */
               if (Jenis.equals("Cuti")){
                   Query = "SELECT a.nama, a.nik, a.no_cuti as no_form ,  a.periode_cuti, b.jabatan ,a.jumlah_cuti, a.telphone, a.tanggal_keluar, a.tanggal_masuk, a.keperluan, a.create_dated "
                           + "from header_cuti as a inner join header_pegawai as b  on a.nik = b.nik "
                           + "where a.no_cuti = '" +TransNo + "' ";
               }
               else if (Jenis.equals("Lembur")){
                   Query = "SELECT a.nama, a.nik, a.no_lembur as no_form,  a.periode, b.jabatan,"
                           + " a.ket1, a.ket2, a.tanggal1, a.tanggal2, a.tanggal3, a.tanggal4, a.create_dated ,"
                           + " a.ket1, a.ket2, a.jam1 , a.jam2, a.jam3, a.jam4"
                           + " from header_lembur as a inner join header_pegawai as b on a.nik = b.nik "
                           + " where a.no_lembur = '" +TransNo + "' ";
               }    
               else {
                   Query = "SELECT a.nama, a.nik, a.no_lembur as no_form,  a.periode, b.cabang_lokasi "
                           + "from header_lembur as a inner join header_pegawai as b on a.nik = b.nik "
                           + "where a.nik = '" +TransNo + "' ";
                   //Reset();
               }
               
               HQ = stm.executeQuery(Query);              
               baris = HQ.getRow();

               while(HQ.next()  ){
                   if ( Jenis.equals("Cuti") ) {
                        Nama      = HQ.getString("nama");
                        NIK       = HQ.getString("nik");
                        NoForm    = HQ.getString("no_form");
                        Period    = HQ.getString("periode_cuti");
                        Jabatan   = HQ.getString("Jabatan");
                        TanggalMasuk    = HQ.getString("tanggal_masuk");
                        TanggalKeluar   = HQ.getString("tanggal_keluar");
                        Telpone         = HQ.getString("telphone");
                        Keperluan       = HQ.getString("keperluan");
                        CreateDate      = HQ.getString("create_dated");
                        JumlahCuti      = HQ.getString("jumlah_cuti");
                        
                        JLabelNoNik.setText(NIK);
                        JLabelTanggal.setText(CreateDate);
                        JLabelForm.setText(NoForm);
                        JTextNama.setText(Nama);
                        JTextJabatan.setText(Jabatan);
                        JTextPeriode.setText(Period);
                        JTextKerperluan.setText(Keperluan);
                        JTextNoHPDihub.setText(Telpone);
                        JTextJumlahCuti.setText(JumlahCuti);
                        
                        AmbilDatabaseSearch(NIK, 1);
                        
                        /*
                         * Set Tanggal
                         */
                        Calendar Tgl = Calendar.getInstance();
                        Tgl.set(N.ConvertTglBlnThnToTahun(TanggalKeluar), N.ConvertTglBlnThnToBulan(TanggalKeluar) - 1, N.ConvertTglBlnThnToTanggal(TanggalKeluar));
                       
                        dateChooserCombo1.setSelectedDate(Tgl);
                        
                        Tgl.set(N.ConvertTglBlnThnToTahun(TanggalMasuk), N.ConvertTglBlnThnToBulan(TanggalMasuk) - 1, N.ConvertTglBlnThnToTanggal(TanggalMasuk));
                        dateChooserCombo2.setSelectedDate(Tgl);

                   }
                   else if (Jenis.equals("Lembur")) {
                       
                        Nama      = HQ.getString("nama");
                        NIK       = HQ.getString("nik");
                        NoForm    = HQ.getString("no_form");
                        CreateDate= HQ.getString("create_dated");
                        Jabatan   = HQ.getString("Jabatan");
                        Tanggal1      = HQ.getString("Tanggal1");
                        Tanggal2      = HQ.getString("Tanggal2");
                        Tanggal3      = HQ.getString("Tanggal3");
                        Tanggal4      = HQ.getString("Tanggal4");
                        
                        /*
                         * Set Tanggal
                         */
                        Calendar Tgl = Calendar.getInstance();
                        Tgl.set(N.ConvertTglBlnThnToTahun(Tanggal1), N.ConvertTglBlnThnToBulan(Tanggal1) - 1, N.ConvertTglBlnThnToTanggal(Tanggal1));
                        dateChooserCombo3.setSelectedDate(Tgl);
                        
                        Tgl.set(N.ConvertTglBlnThnToTahun(Tanggal2), N.ConvertTglBlnThnToBulan(Tanggal2) - 1, N.ConvertTglBlnThnToTanggal(Tanggal2));
                        dateChooserCombo4.setSelectedDate(Tgl);
                        
                        Tgl.set(N.ConvertTglBlnThnToTahun(Tanggal3), N.ConvertTglBlnThnToBulan(Tanggal3) - 1, N.ConvertTglBlnThnToTanggal(Tanggal3));
                        dateChooserCombo5.setSelectedDate(Tgl);
                        
                        Tgl.set(N.ConvertTglBlnThnToTahun(Tanggal4), N.ConvertTglBlnThnToBulan(Tanggal4) - 1, N.ConvertTglBlnThnToTanggal(Tanggal4));
                        dateChooserCombo6.setSelectedDate(Tgl);

                        
                        JLabelNoNik.setText(NIK);
                        JLabelTanggal.setText(CreateDate);
                        JLabelForm.setText(NoForm);
                        JTextNama.setText(Nama);
                        JTextJabatan.setText(Jabatan);
                        
                        // Set Jam
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(HQ.getLong("jam1"));
                        kazaoCalendarTime1.setCalendar(cal);
                        
                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTimeInMillis(HQ.getLong("jam2"));
                        kazaoCalendarTime2.setCalendar(cal2);
                        
                        Calendar cal3 = Calendar.getInstance();
                        cal3.setTimeInMillis(HQ.getLong("jam3"));
                        kazaoCalendarTime3.setCalendar(cal3);
                        
                        Calendar cal4 = Calendar.getInstance();
                        cal4.setTimeInMillis(HQ.getLong("jam4"));
                        kazaoCalendarTime4.setCalendar(cal4);
                        
                        JTextAreaAlamat.setText(HQ.getString("ket1"));
                        JTextAreaAlamat1.setText(HQ.getString("ket2"));
                   }
                   else {
                       
                   } 
              }

           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
           
        jTable2.setModel(Modeltabel2);      
      }
     /*
      * int a = 0 => cuti
      * int a = 1 => lembur
      */
     int Urut;
     private void SaveDB(int a){
         
        String Nama, Nik, Tanggal, Jabatan;
        
        Nama            = JTextNama.getText();
        Nik             = JLabelNoNik.getText();
        Tanggal         = JLabelTanggal.getText();
        Jabatan         = JTextJabatan.getText();
        if (a == 0){
            
            String   Keperluan, Tanggal1, Tanggal2, JumlahCuti, Telpon, Periode;
            String NoCuti   = NoCuti();
            Keperluan   = JTextKerperluan.getText();
            Tanggal1    = dateChooserCombo1.getText();
            Tanggal2    = dateChooserCombo2.getText();
            JumlahCuti  = JTextJumlahCuti.getText();
            Telpon      = JTextNoHPDihub.getText();
            Periode     = JTextPeriode.getText();
            
            if (Edit){
                NoCuti = JLabelForm.getText();
                Satu.DeleteData(NoCuti, "header_cuti", "no_cuti");
            }
            else {
                Urut = TN.GetNoUrut();
            }
              try {
                  /*
                   * INSERT INTO `header_cuti`(`no_cuti`, `key_no`, `nik`, `periode_cuti`, `keperluan`, `nama`, `tanggal_keluar`, `tanggal_masuk`, `jumlah_cuti`, `telphone`, `create_dated`, `periode`) VALUES 
                   */
                        Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        Stm.executeUpdate("insert into header_cuti values"
                                + " ('" + NoCuti + "','" + Urut + "','" + Nik 
                                + "','"+ Keperluan + "','" + Nama 
                                + "','"+Tanggal1+ "','"+ Tanggal2 + "','" + JumlahCuti + "','"+ Telpon+ "', now() ,'"+Periode()+ "','"+ Periode +"')");   

                     }
                     catch (Exception Ex){
                         System.out.println(Ex);
                         LabelPeringantan.setText("Data Sudah Ada" + Ex);
                         LabelPeringantan.setText("Gagal Saved");
                        //JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                  }
             // AmbilDataDariDatabase("");
         }
         else {
            String Tanggal1 , Tanggal2, Tanggal3, Tanggal4, NoLembur;
            
            Tanggal1    = dateChooserCombo3.getText();
            Tanggal2    = dateChooserCombo4.getText();
            Tanggal3    = dateChooserCombo5.getText();
            Tanggal4    = dateChooserCombo6.getText();
            long jam1 = 0;
            jam1 = kazaoCalendarTime1.getCalendar().getTimeInMillis();
            long jam2 = 0 ;
            jam2 = kazaoCalendarTime2.getCalendar().getTimeInMillis();
            long jam3 = 0 ;
            jam3 = kazaoCalendarTime3.getCalendar().getTimeInMillis();
            long jam4 = 0;
            jam4 = kazaoCalendarTime4.getCalendar().getTimeInMillis();
            
            NoLembur =  NoLembur();
            if (Edit){
                NoLembur = JLabelForm.getText();
                Satu.DeleteData(NoLembur, "header_lembur", "no_lembur");
            }
            else {
                Urut = TN.GetNoUrut();
            }
              try {
                  /*
                   * INSERT INTO `header_lembur`(`key_no`, `no_lembur`, `nik`, `ket1`, `ket2`, `tanggal1`, `tanggal2`, `tanggal3`, `tanggal4`, `periode`, `create_dated`)
                   * VALUES ([value-1],[value-2],[value-3],[value-4],[value-5],[value-6],[value-7],[value-8],[value-9],[value-10],[value-11])
                   */
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    Stm.executeUpdate("insert into header_lembur values"
                            + " ('" + Urut + "','"+ NoLembur + "','" + Nik + "','" + JTextNama.getText() + "','"+ JTextAreaAlamat.getText() 
                            + "','" + JTextAreaAlamat1.getText() + "','"+ Tanggal1 + "','" + Tanggal2 
                            + "','"+Tanggal3+ "','"+ Tanggal4 + "','" + Periode() + "', now(), '"+jam1+"','" +jam2+"','"+jam3+"','"+jam4+"')");   
                     }
                     catch (Exception Ex){
                         System.out.println(Ex);
                         LabelPeringantan.setText("Data Sudah Ada" + Ex);
                         LabelPeringantan.setText("Gagal Saved");
                        //JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                  }
             
         }
        AmbilDataDariDatabase(AmbilDataPeriod1(), (String) jComboBox2.getSelectedItem(), (String)BoxJabatan1.getSelectedItem(), (String)BoxCabang2.getSelectedItem());
     }
      /*
     * a = 0 => ambildatabase cuti
     * a = 1 => ambildatabase lembur
     * 
     */
    private void AmbilDataDariDatabase(String Periode, String Status, String Jabatan, String Cabang){
        
        Reset();
        //Satu.HapusDataJTabel(jTable2);
         
        /*
         * Isi data ke Tabel dari database
         * {"No", "Jenis","No Form","NIK", "Nama","Cabang","Periode", "Action"};
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               String Nama, Jenis = ""     ;
                   String NIK       ;
                   String NoForm    ;
                   String Period    ;
                   String Cabanga   ;
               Statement stm = K.createStatement();
               String Query = null;
               if (Jabatan.equals(" ")){
                   Jabatan = "";
               }
               if (Cabang.equals(" ")){
                   Cabang= "";
               }
               /*
                * "No", "Jenis",no form "NIK", "Nama","Cabang","Periode", "Action"};
                */
               if (Status.equals("Cuti")){
                   Query = "SELECT a.nama, a.nik, a.no_cuti as no_form ,  a.periode, b.cabang_lokasi "
                           + "from header_cuti as a inner join header_pegawai as b  on a.nik = b.nik "
                           + "where a.periode = '" +Periode + "' and b.jabatan like  '%"+Jabatan + "%' and b.cabang like '%"+Cabang + "%' order by create_dated desc ";
                   Jenis = "Cuti";
               }
               else if (Status.equals("Lembur")){
                   Query = "SELECT a.nama, a.nik, a.no_lembur as no_form,  a.periode, b.cabang_lokasi "
                           + "from header_lembur as a inner join header_pegawai as b on a.nik = b.nik "
                           + "where a.periode = '" +Periode + "' and b.jabatan like  '%"+Jabatan + "%' and b.cabang like '%"+Cabang + "%' order by create_dated desc ";
                   Jenis = "Lembur";
               }    
               else {
                   Query = "SELECT a.nama, a.nik, a.no_lembur as no_form,  a.periode, b.cabang_lokasi "
                           + "from header_lembur as a inner join header_pegawai as b on a.nik = b.nik "
                           + "where a.periode = '" +Periode + "' and b.jabatan like  '%"+Jabatan + "%' and b.cabang like '%"+Cabang + "%' order by create_dated desc ";
                   Reset();
               }

               HQ = stm.executeQuery(Query);              
               baris = HQ.getRow();

               while(HQ.next()  ){
                   Nama      = HQ.getString("nama");
                   NIK       = HQ.getString("nik");
                   NoForm    = HQ.getString("no_form");
                   Period    = HQ.getString("periode");
                   Cabanga   = HQ.getString("cabang_lokasi");

                   String[] add         = {String.valueOf(HQ.getRow()).toString(),Jenis,NoForm,NIK, Nama, Cabanga, Periode  };
                   Modeltabel2.addRow(add);    
               
              }

           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
           
        jTable2.setModel(Modeltabel2);      
     }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooserDialog1 = new datechooser.beans.DateChooserDialog();
        kazaoCalendarDialog1 = new org.kazao.calendar.KazaoCalendarDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        JLabelNoNik = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        JLabelTanggal = new javax.swing.JLabel();
        LabelPeringantan = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        JTextFieldSearch2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        BoxJabatan1 = new javax.swing.JComboBox();
        Cabang2 = new javax.swing.JLabel();
        BoxCabang2 = new javax.swing.JComboBox();
        jButtonRefresh1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        kazaoCalendarDate1 = new org.kazao.calendar.KazaoCalendarDate();
        JLabelForm = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        JTextNama = new javax.swing.JTextField();
        label24 = new java.awt.Label();
        label25 = new java.awt.Label();
        JTextJabatan = new javax.swing.JTextField();
        label26 = new java.awt.Label();
        jComboBox1 = new javax.swing.JComboBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        label35 = new java.awt.Label();
        label36 = new java.awt.Label();
        label37 = new java.awt.Label();
        label38 = new java.awt.Label();
        label39 = new java.awt.Label();
        label40 = new java.awt.Label();
        label41 = new java.awt.Label();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        dateChooserCombo2 = new datechooser.beans.DateChooserCombo();
        JTextPeriode = new javax.swing.JTextField();
        JTextKerperluan = new javax.swing.JTextField();
        JTextJumlahCuti = new javax.swing.JTextField();
        JTextNoHPDihub = new javax.swing.JTextField();
        JTextJumlahCuti1 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        label27 = new java.awt.Label();
        label28 = new java.awt.Label();
        label30 = new java.awt.Label();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTextAreaAlamat = new javax.swing.JTextArea();
        label31 = new java.awt.Label();
        label32 = new java.awt.Label();
        jScrollPane3 = new javax.swing.JScrollPane();
        JTextAreaAlamat1 = new javax.swing.JTextArea();
        label34 = new java.awt.Label();
        dateChooserCombo3 = new datechooser.beans.DateChooserCombo();
        kazaoCalendarTime1 = new org.kazao.calendar.KazaoCalendarTime();
        kazaoCalendarTime2 = new org.kazao.calendar.KazaoCalendarTime();
        dateChooserCombo4 = new datechooser.beans.DateChooserCombo();
        dateChooserCombo5 = new datechooser.beans.DateChooserCombo();
        kazaoCalendarTime3 = new org.kazao.calendar.KazaoCalendarTime();
        dateChooserCombo6 = new datechooser.beans.DateChooserCombo();
        kazaoCalendarTime4 = new org.kazao.calendar.KazaoCalendarTime();
        label33 = new java.awt.Label();
        label42 = new java.awt.Label();
        JButtonPelamarNew = new javax.swing.JButton();
        JButtonPelamarSave = new javax.swing.JButton();
        JButtonPelamarEdit = new javax.swing.JButton();
        JButtonPelamarPrintForm = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setText("NIK");

        JLabelNoNik.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabelNoNik.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel11.setText("Tanggal");

        JLabelTanggal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabelTanggal.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        LabelPeringantan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setText("Search  ");

        jLabel9.setText("Jabatan");

        BoxJabatan1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Direktur", "Marketing", "Umum", "Teller" }));

        Cabang2.setText("Cabang");

        BoxCabang2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Bekasi - Bantar Gebang", "Bekasi - Pondok Ungu", "Bekasi - Kranji", "Jakpus - Johar", "Jaktim - Cibubur", "Cikampek - Cikampek", "DKI - Johar", "Jawa Barat - Karawang" }));

        jButtonRefresh1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Refresh.png"))); // NOI18N
        jButtonRefresh1.setText("Refresh");

        jLabel10.setText("Periode");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cuti", "Lembur" }));

        kazaoCalendarDate1.setFormat("mm/yyyy");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(JTextFieldSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kazaoCalendarDate1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BoxJabatan1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Cabang2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BoxCabang2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonRefresh1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(JTextFieldSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)))
                    .addComponent(kazaoCalendarDate1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(BoxJabatan1)
                        .addComponent(Cabang2)
                        .addComponent(BoxCabang2))
                    .addComponent(jButtonRefresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );

        JLabelForm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabelForm.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(JLabelTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLabelForm, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JLabelNoNik, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelPeringantan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JLabelNoNik, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11)
                            .addComponent(JLabelTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JLabelForm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(LabelPeringantan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        JTextNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextNama.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextNama.setEnabled(false);

        label24.setText("Nama Lengkap");

        label25.setText("Jabatan");

        JTextJabatan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextJabatan.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextJabatan.setEnabled(false);

        label26.setText("Pilih Data Pegawai");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel( app_search_data_pegawai.getData().toArray()));
        jComboBox1.setEditable(true);

        label35.setText("Periode Cuti");

        label36.setText("Keperluan");

        label37.setText("Tanggal");

        label38.setText("Tanggal Masuk");

        label39.setText("Jumlah Cuti");

        label40.setText("Sisa Cuti");

        label41.setText("Telpon Yang Dapat Di Hub");

        dateChooserCombo1.setEnabled(false);

        dateChooserCombo2.setEnabled(false);

        JTextPeriode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextPeriode.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextPeriode.setEnabled(false);

        JTextKerperluan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextKerperluan.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextKerperluan.setEnabled(false);

        JTextJumlahCuti.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextJumlahCuti.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextJumlahCuti.setEnabled(false);

        JTextNoHPDihub.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextNoHPDihub.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextNoHPDihub.setEnabled(false);

        JTextJumlahCuti1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextJumlahCuti1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextJumlahCuti1.setEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(label35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label41, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(label40, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label39, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label38, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label37, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label36, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextPeriode)
                    .addComponent(JTextKerperluan, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(JTextJumlahCuti, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextJumlahCuti1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextNoHPDihub))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextPeriode, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextKerperluan, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextJumlahCuti, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextJumlahCuti1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextNoHPDihub, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(115, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cuti", jPanel4);

        label27.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        label27.setText("Rencana Lembur");

        label28.setText("Waktu");

        label30.setText("Mengerjakan tugas sbb :");

        JTextAreaAlamat.setColumns(20);
        JTextAreaAlamat.setRows(5);
        JTextAreaAlamat.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextAreaAlamat.setEnabled(false);
        jScrollPane1.setViewportView(JTextAreaAlamat);

        label31.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        label31.setText("Realisasi Lembur");

        label32.setText("Waktu");

        JTextAreaAlamat1.setColumns(20);
        JTextAreaAlamat1.setRows(5);
        JTextAreaAlamat1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextAreaAlamat1.setEnabled(false);
        jScrollPane3.setViewportView(JTextAreaAlamat1);

        label34.setText("Yang dikerjakan sbb :");

        kazaoCalendarTime1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        kazaoCalendarTime2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        kazaoCalendarTime3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        kazaoCalendarTime4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        label33.setText("-");

        label42.setText("-");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(label27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(228, 228, 228))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(label34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(label28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dateChooserCombo3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(2, 2, 2)
                                    .addComponent(kazaoCalendarTime1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(label33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(5, 5, 5)
                                    .addComponent(dateChooserCombo4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(2, 2, 2)
                                    .addComponent(kazaoCalendarTime2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(label30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(label32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dateChooserCombo5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(2, 2, 2)
                                    .addComponent(kazaoCalendarTime3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(label42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(5, 5, 5)
                                    .addComponent(dateChooserCombo6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(2, 2, 2)
                                    .addComponent(kazaoCalendarTime4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 14, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(label28, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(kazaoCalendarTime1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dateChooserCombo3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)))
                            .addComponent(dateChooserCombo4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(label30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(kazaoCalendarTime2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(label31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(kazaoCalendarTime3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dateChooserCombo5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(kazaoCalendarTime4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateChooserCombo6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Lembur", jPanel5);

        JButtonPelamarNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonPelamarNew.setText("New");

        JButtonPelamarSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/save_icon2.png"))); // NOI18N
        JButtonPelamarSave.setText("Save");
        JButtonPelamarSave.setEnabled(false);

        JButtonPelamarEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/edit_icon2.png"))); // NOI18N
        JButtonPelamarEdit.setText("Edit");

        JButtonPelamarPrintForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/print_icon2.png"))); // NOI18N
        JButtonPelamarPrintForm.setText("Print Form");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(label24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JTextJabatan)
                            .addComponent(JTextNama)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(JButtonPelamarNew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonPelamarSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonPelamarEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonPelamarPrintForm)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JTextNama, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(label25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(JTextJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JButtonPelamarPrintForm, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonPelamarEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JButtonPelamarNew)
                        .addComponent(JButtonPelamarSave)))
                .addContainerGap())
        );

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox BoxCabang2;
    private javax.swing.JComboBox BoxJabatan1;
    private javax.swing.JLabel Cabang2;
    private javax.swing.JButton JButtonPelamarEdit;
    private javax.swing.JButton JButtonPelamarNew;
    private javax.swing.JButton JButtonPelamarPrintForm;
    private javax.swing.JButton JButtonPelamarSave;
    private javax.swing.JLabel JLabelForm;
    private javax.swing.JLabel JLabelNoNik;
    private javax.swing.JLabel JLabelTanggal;
    private javax.swing.JTextArea JTextAreaAlamat;
    private javax.swing.JTextArea JTextAreaAlamat1;
    private javax.swing.JTextField JTextFieldSearch2;
    private javax.swing.JTextField JTextJabatan;
    private javax.swing.JTextField JTextJumlahCuti;
    private javax.swing.JTextField JTextJumlahCuti1;
    private javax.swing.JTextField JTextKerperluan;
    private javax.swing.JTextField JTextNama;
    private javax.swing.JTextField JTextNoHPDihub;
    private javax.swing.JTextField JTextPeriode;
    private javax.swing.JLabel LabelPeringantan;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserCombo dateChooserCombo2;
    private datechooser.beans.DateChooserCombo dateChooserCombo3;
    private datechooser.beans.DateChooserCombo dateChooserCombo4;
    private datechooser.beans.DateChooserCombo dateChooserCombo5;
    private datechooser.beans.DateChooserCombo dateChooserCombo6;
    private datechooser.beans.DateChooserDialog dateChooserDialog1;
    private javax.swing.JButton jButtonRefresh1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDate1;
    private org.kazao.calendar.KazaoCalendarDialog kazaoCalendarDialog1;
    private org.kazao.calendar.KazaoCalendarTime kazaoCalendarTime1;
    private org.kazao.calendar.KazaoCalendarTime kazaoCalendarTime2;
    private org.kazao.calendar.KazaoCalendarTime kazaoCalendarTime3;
    private org.kazao.calendar.KazaoCalendarTime kazaoCalendarTime4;
    private java.awt.Label label24;
    private java.awt.Label label25;
    private java.awt.Label label26;
    private java.awt.Label label27;
    private java.awt.Label label28;
    private java.awt.Label label30;
    private java.awt.Label label31;
    private java.awt.Label label32;
    private java.awt.Label label33;
    private java.awt.Label label34;
    private java.awt.Label label35;
    private java.awt.Label label36;
    private java.awt.Label label37;
    private java.awt.Label label38;
    private java.awt.Label label39;
    private java.awt.Label label40;
    private java.awt.Label label41;
    private java.awt.Label label42;
    // End of variables declaration//GEN-END:variables
}
