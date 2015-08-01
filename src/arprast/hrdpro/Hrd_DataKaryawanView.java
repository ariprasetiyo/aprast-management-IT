/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arprast.hrdpro;

import Sistem.ButtonJTable;
import Sistem.ButtonJTableKeDuaLocal;
import Sistem.ComponentHanyaAngka;
import Sistem.RenderingKanan;
import Sistem.RenderingTengah;
import Sistem.SatuUntukSemua;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
 * @author LANTAI3
 */
public class Hrd_DataKaryawanView extends javax.swing.JDialog {

    /**
     * Creates new form Hrd_DataKaryawanView
     */
    private SatuUntukSemua      Satu = new SatuUntukSemua();
    private DefaultTableModel   ModeltabelPendidikan    = new DefaultTableModel();
    private DefaultTableModel   ModeltabelNonFormal     = new DefaultTableModel();
    private TableCellRenderer   kanan = new RenderingKanan();
    private TableCellRenderer   tengah= new RenderingTengah();
    private ButtonGroup         GroupsStatus = new ButtonGroup();
    private Sistem.TanggalSistem N       = new Sistem.TanggalSistem();
    private Sistem.TransNo TN = new Sistem.TransNo();
    private Sistem.DB_MYSQL KD = new Sistem.DB_MYSQL();
    private Connection K = KD.createConnection();
    private boolean Edit = false, StatusSave = false;
    private ComponentHanyaAngka AntiHuruf = new ComponentHanyaAngka();
    String KantorStr , KantorCab , PilihJabStr , InputCuti , InputGaji;
    
    public Hrd_DataKaryawanView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        TabelPendidikan();
        TabelNoPendidikan();
        Tampilan ();
        Aksi();
    }
    
     private void Tampilan (){
         
         /*
         * Tampilan bagian Depreciation Procces
         * Perid yang diset ke bulan dan Tahunn
         */
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy");
        dateChooserComboPelamar2.setDateFormat(dt1);  
        dt1 = new SimpleDateFormat("yyyy");
        dateChooserComboPelamar1.setDateFormat(dt1);  
        dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserComboPelamar3.setDateFormat(dt1);  
        dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserComboPelamar.setDateFormat(dt1);  
        
        
        AntiHuruf.SetAntiAngkaLimit(JTextAsalTelpon1, 14);
        AntiHuruf.SetAntiAngkaLimit(JTextAlamatKodePos, 14);
        AntiHuruf.SetAntiAngkaLimit(JTextAlamatTelpon, 14);
        AntiHuruf.SetAntiAngkaLimit(JTextKTP, 50);
        AntiHuruf.SetAntiAngkaLimit(JTextSIM, 50);
        JLabelTanggal.setText(TanggalSekarang());

        jComboBoxStatusJabatan.addItem("Direktur");
        jComboBoxStatusJabatan.addItem("Marketing");
        jComboBoxStatusJabatan.addItem("Umum");
        jComboBoxStatusJabatan.addItem("Teller");
    }
    
    private String TanggalSekarang(){
        return N.GetTglNow() + " - " +  N.GetBlnNow() + " - " + N.GetThnNow();
    }
    private String Periode(){
        N.SetTahunSis();
        N.SetBulanSis();
        N.SetTanggalSis();
        return    N.GetTahunSisString()+ N.GetBulanSisString();
    }
    private String NoPelamar(){       
        /*
         * SELECT trans_no , periode FROM header_pembelian where periode  = WhereSyarat order by  key_no desc limit 0,1
         * 
         */       
        TN.SetTransNoPo("P", "key_no", "periode", "header_pelamar", Periode());
        return TN.GetTransNoPo();
    }
    private String NoPegawai(){       
        /*
         * SELECT trans_no , periode FROM header_pegawai where periode  = WhereSyarat order by  no desc limit 0,1
         */       
        TN.SetTransNoPo("NIK", "key_no", "periode", "header_pegawai", Periode());
        return TN.GetTransNoPo();
    }
    
    private void Aksi(){

        
        jButtonPendidikan.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
               JTextAlamatKodePos3.setText("");
               JTextAlamatKodePos1.setText("");
               JTextAlamatKodePos2.setText("");
            }
        });
        JButtonNonFormal.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
               JTextAlamatKodePos4.setText("");
               JTextAlamatKodePos5.setText("");
            }
        });
        
        JButtonPelamarSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                
                if (Validasi ()){
                    
                    DisabelEnable(false);
                    
                    Component[] Tombol = { JButtonPelamarSave, JButtonPelamarEdit};
                    boolean[] Benar = {true, false, true};
                    Satu.LogikaComponent(Tombol, Benar);
                    SaveDB();
                    Edit = false;
                    
                }
            }
        });
        
        
        JButtonPelamarEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                DisabelEnable(true);        
                
                Component[] Tombol = { JButtonPelamarSave, JButtonPelamarEdit};
                boolean[] Benar = {true, true, true};
                Satu.LogikaComponent(Tombol, Benar);
                
                Edit = true;
                StatusLabel.setText("Status Edit");
            }
        });
        
        jButtonPendidikan.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                 AddTotable ();
            }
        });
        
        JButtonNonFormal.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                AddTotable2 ();
            }
        });
        jComboBoxStatusMenikah1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                 String AA;
                AA =  e.getActionCommand();
                if (jComboBoxStatusMenikah1.getSelectedIndex() == 0){
                    String[] Data = {"DKI - Johar","Jawa Barat - Karawang"};
                    jComboBoxStatusMenikah2.removeAllItems();
                    for (int a = 0 ; a < Data.length ; a++){
                        jComboBoxStatusMenikah2.addItem(Data[a]);
                    }
                }
                else if (jComboBoxStatusMenikah1.getSelectedIndex() == 1) {
                    jComboBoxStatusMenikah2.removeAllItems();
                    String[] Data = {"Bekasi - Bantar Gebang",
                            "Bekasi - Pondok Ungu",
                            "Bekasi - Kranji",
                            "Jakpus - Johar",
                            "Jaktim - Cibubur",
                            "Cikampek - Cikampek"};
                    for (int a = 0 ; a < Data.length ; a++){
                        jComboBoxStatusMenikah2.addItem(Data[a]);
                    }
                }
            }
        });

        AntiHuruf.SetAntiAngkaLimit(JTextCuti, 3);
        AntiHuruf.SetAntiAngka(JTextGajiPokok);
    }
    private void Reset(){
        StatusLabel.setText("");
        Edit = false;
        JTextField[] DataField = {JTextNama, JTextKelahiran, JTextAgama, 
            JTextKewarganegaraan, JTextKTP, JTextSIM, 
            JTextAlamatKodePos, JTextAlamatTelpon, JTextAsalTelpon1,
            JTextEmail, JTextAsalKodePos, JTextAlamatKodePos3, JTextAlamatKodePos1, JTextAlamatKodePos2, 
            JTextAlamatKodePos4, JTextAlamatKodePos5 };
        JLabel Label[] = {StatusLabel, JLabelTanggal, JLabelNoFomulir};
        Satu.ResetTampilan(DataField, Label);
        
        JTextAreaAlamat.setText("");
        
        Satu.HapusDataJTabel(JTablePendidikan);
        Satu.HapusDataJTabel(JTableNonFormal);
    }
    private boolean Validasi (){
        
        JTextField[] DataField = {JTextNama, JTextKelahiran, JTextAgama, 
            JTextKewarganegaraan, JTextKTP, JTextSIM, 
            JTextAlamatKodePos, JTextAlamatTelpon, JTextAsalTelpon1,
            JTextEmail, JTextAsalKodePos };
        String[] DataError = {"Nama Kosong", "Tempat Kelahiran Kosong", "Agama Kosong"
                , "Kewarganegaraan Kosong", "KTP Kosong", "SIM Kosong"
                , "Kode Pos Kosong", "No Telphone Kosong", "No Hp Kosong"
                , "Email Kosong", "Hobby Kosong"
        };
        
        if (JTablePendidikan.getRowCount() <= 0 ){
            JOptionPane.showMessageDialog(null,"Tidak ada data Pendidikan");
            return false;
        }
        
        if ( JTextAreaAlamat.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Tidak ada data alamat");
            return false;
        }
        return Satu.ValidasiData(DataField, DataError);
    }
    
    private void DisabelEnable(boolean BenarSalah){
        Component DataComponet [] = {JTextNama, JTextKelahiran, JTextAgama, 
            JTextKewarganegaraan, JTextKTP, JTextSIM, 
            JTextAlamatKodePos, JTextAlamatTelpon, JTextAsalTelpon1,
            JTextEmail, JTextAsalKodePos, JTextAreaAlamat,
            jComboBoxStatusKelamin, jComboBoxStatusMenikah, JComboStatucRumah,
             dateChooserComboPelamar,
            jButtonPendidikan, JButtonNonFormal,
            JTextAlamatKodePos3, JTextAlamatKodePos1, JTextAlamatKodePos2, 
            JTextAlamatKodePos4, JTextAlamatKodePos5, dateChooserComboPelamar2,jComboBoxStatusJabatan,
            dateChooserComboPelamar1,jComboBoxStatusPekerja, dateChooserComboPelamar3, JTextGajiPokok, JTextCuti, jComboBoxStatusMenikah1, jComboBoxStatusMenikah2
        };
        Satu.LogikaComponent(DataComponet, BenarSalah);
    }
    
    private void  AddTotable (){
       String[] add = {"",JTextAlamatKodePos3.getText(), JTextAlamatKodePos1.getText(), JTextAlamatKodePos2.getText()
       , dateChooserComboPelamar2.getText() + " - " + dateChooserComboPelamar1.getText()};
       
       JTextField[] FieldCek = {JTextAlamatKodePos3, JTextAlamatKodePos1, JTextAlamatKodePos2 };
       String[] Error = {"Data tingkat belum di sisi", "Nama sekolah belum diisi", "Jurusan belum disi"};
       Satu.ValidasiData(FieldCek, Error);
       if (Satu.ValidasiData(FieldCek, Error) ){
            ModeltabelPendidikan.addRow(add);
       }
    }
    private void AddTotable2(){
       String[] add = {"",JTextAlamatKodePos4.getText(), JTextAlamatKodePos5.getText(), dateChooserComboPelamar3.getText()};
       String[] Error = {"Tema belum di isi", "Penyelengara belum di isi"};
       JTextField[] FieldCek = {JTextAlamatKodePos4, JTextAlamatKodePos5 };
       if (Satu.ValidasiData(FieldCek, Error) ){
            ModeltabelNonFormal.addRow(add);
       }
    }
   private void PrintOut(){
        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();

             
        try {
            String x    = System.getProperty("user.dir")+"\\PrintOut\\FormPelamar1.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap,  new    JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }
   }

     private void TabelPendidikan(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No", "Tingkat","Nama Sekolah ", "Jurusan","Periode", "Action"};
        ModeltabelPendidikan = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 5 ) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTablePendidikan.setModel(ModeltabelPendidikan);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(ModeltabelPendidikan);
        JTablePendidikan.setRowSorter(sorter);
        
        /*
         * Rata tengah atau kanan table
         */
        JTablePendidikan.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTablePendidikan.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTablePendidikan.getColumnModel().getColumn(2).setCellRenderer( tengah );
        JTablePendidikan.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        JTablePendidikan.getColumnModel().getColumn(4).setCellRenderer( tengah ); 
        JTablePendidikan.getColumnModel().getColumn(5).setCellRenderer( tengah ); 
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,60,200,200,100,100 };
        Sistem.Colom_table ukuran_colom = new Sistem.Colom_table();
        ukuran_colom.ukuran_colom(JTablePendidikan, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTablePendidikan.setName("xx");
        JTablePendidikan.getColumnModel().getColumn(5).setCellRenderer(  new ButtonJTable("Delete"));
        JTablePendidikan.getColumnModel().getColumn(5).setCellEditor( new  ButtonJTableKeDuaLocal(new JCheckBox(),ModeltabelPendidikan, JTablePendidikan));
       
        /*
         * Disable drag colum tabel
         */       
        JTablePendidikan.getTableHeader().setReorderingAllowed(false); 
        
    }
     private void TabelNoPendidikan(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No", "Tema","Penyelengara ", "Waktu", "Action"};
        ModeltabelNonFormal = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 4 ) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTableNonFormal.setModel(ModeltabelNonFormal);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(ModeltabelNonFormal);
        JTableNonFormal.setRowSorter(sorter);
        
        /*
         * Rata tengah atau kanan table
         */
        JTableNonFormal.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTableNonFormal.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTableNonFormal.getColumnModel().getColumn(2).setCellRenderer( tengah );
        JTableNonFormal.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        JTableNonFormal.getColumnModel().getColumn(4).setCellRenderer( tengah ); 
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,200,200,100,100 };
        Sistem.Colom_table ukuran_colom = new Sistem.Colom_table();
        ukuran_colom.ukuran_colom(JTableNonFormal, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTableNonFormal.setName("xx");
        JTableNonFormal.getColumnModel().getColumn(4).setCellRenderer(  new ButtonJTable("Delete"));
        JTableNonFormal.getColumnModel().getColumn(4).setCellEditor( new  ButtonJTableKeDuaLocal(new JCheckBox(),ModeltabelNonFormal, JTableNonFormal));
       
        /*
         * Disable drag colum tabel
         */       
        JTableNonFormal.getTableHeader().setReorderingAllowed(false); 
        
    }
       
    int status_pelamar = 0, Urut_Pegawai;
    public void ViewDataDatabasePelamar(String TransNo){
        Reset();
        /*
         * Isi data ke Tabel dari database
         * "No", "No Fomulir","Nama ", "Jenis Kelamin","Agama","No Hp", "No Telphone", "Email","periode ", "Action"
         */      
        int baris;       
        ResultSet HQ = null;
        ResultSet HQ2 = null;
        ResultSet HQ3 = null;
           try {
               String kelahiran_tanggal = null, tingkat, nama_sekolah, jurusan, tahun1 ;
                String kelahirian_tempat = null;
                int status_nikah = 0, status_peg = 1;
                String agama = null ;
                String kewarganegaraan = null  ;
                int no_ktp = 0;
                int no_sim = 0;
                String alamat = null;
                int kode_pos = 0, Kelamin   ;
                int status_tempat_tinggal = 0 ;
                String hobby ;
                int jenis_kelamain = 0;
                String tanggal_dibuat = null, nama     = null,
                   trans_no    = null,
                   Agama  = null,
                   email    = null,
                   no_hp   = null,
                   no_telphone = null;
                   
               Statement stm = K.createStatement();
               String Query = "SELECT a.kelahiran_tanggal, a.kelahiran_tempat, a.status_nikah, "
               + " a.kewarganegaraan, a.no_ktp, a.no_sim, a.alamat, a.kode_pos,"
               + " a.status_tempat_tinggal, a.hobby,a. status_pelamar, a.tanggal_dibuat, a.key_no, b.key_no as no_pegawai,"
                       + " a.nama, a.trans_no, a.jenis_kelamain, Agama , email , no_hp, a.no_telphone, "
                       + " cuti, gaji_pokok, b.periode, jabatan, cabang, cabang_lokasi, b.nik, b.status_pekerja "
               + "from header_pelamar  as a inner join header_pegawai as b on a.trans_no = b.no_pelamar "
                       + "where a.trans_no = '"+TransNo +"'  order by a.created_date desc ";
  
               HQ = stm.executeQuery(Query);              
               baris = HQ.getRow();

               while(HQ.next()  ){
                   kelahiran_tanggal     = HQ.getString("kelahiran_tanggal");
                   kelahirian_tempat     = HQ.getString("kelahiran_tempat");
                   status_nikah             = HQ.getInt("status_nikah");
                   kewarganegaraan       = HQ.getString("kewarganegaraan");
                   no_ktp                   = HQ.getInt("no_ktp");
                   no_sim                   = HQ.getInt("no_sim");
                   alamat                = HQ.getString("alamat");
                   kode_pos                 = HQ.getInt("kode_pos");
                   status_tempat_tinggal    = HQ.getInt("status_tempat_tinggal");
                   hobby                 = HQ.getString("hobby");
                   status_pelamar           = HQ.getInt("status_pelamar");
                   tanggal_dibuat        = HQ.getString("tanggal_dibuat");
                   Urut                =  HQ.getInt("key_no");
                   Urut_Pegawai         = HQ.getInt("no_pegawai");
                   nama                 = HQ.getString("nama");
                   trans_no         = HQ.getString("trans_no");
                   jenis_kelamain        = HQ.getInt("jenis_kelamain");
                   Agama        = HQ.getString("Agama");
                   email        = HQ.getString("email");
                   no_hp        = HQ.getString("no_hp");
                   no_telphone        = HQ.getString("no_telphone");
                   KantorStr = HQ.getString("cabang");
                   KantorCab = HQ.getString("cabang_lokasi");
                   PilihJabStr = HQ.getString("jabatan");
                   InputCuti = HQ.getString("cuti");
                   InputGaji = HQ.getString("gaji_pokok");
                   NIK.setText(HQ.getString("nik"));
                   status_peg = HQ.getInt("status_pekerja");
                   
               }
     
               JTextNama.setText(nama);
               JLabelNoFomulir.setText(trans_no);
               
               dateChooserComboPelamar.setEnabled(true);
               Calendar Tgl = Calendar.getInstance();
               try {
                    Tgl.set(N.ConvertTglBlnThnToTahun(kelahiran_tanggal), N.ConvertTglBlnThnToBulan(kelahiran_tanggal) - 1, N.ConvertTglBlnThnToTanggal(kelahiran_tanggal));
                    dateChooserComboPelamar.setSelectedDate(Tgl);
                }
                catch (Exception X){
                    dateChooserComboPelamar.setSelectedDate(null);
                }
                 dateChooserComboPelamar.setEnabled(false);
               
               dateChooserComboPelamar.setSelectedDate(Tgl);
                
               jComboBoxStatusKelamin.setSelectedIndex(jenis_kelamain  );
               JTextKelahiran.setText(kelahirian_tempat);
               
               jComboBoxStatusMenikah.setSelectedIndex(status_nikah);
               
               JTextAgama.setText(Agama);
               JTextKewarganegaraan.setText(kewarganegaraan);
               JTextKTP.setText(no_ktp+"");
               JTextSIM.setText(no_sim+"");
               JTextAreaAlamat.setText(alamat);
               JTextAlamatKodePos.setText(kode_pos+"");
               JTextAlamatTelpon.setText(no_telphone);
               JTextAsalTelpon1.setText(no_hp);
               JTextEmail.setText(email);
               JTextAsalKodePos.setText(alamat);
               JComboStatucRumah.setSelectedIndex(status_tempat_tinggal);
               JLabelTanggal.setText(tanggal_dibuat);
               jComboBoxStatusPekerja.setSelectedIndex(status_peg);
               
                   
               JTextGajiPokok.setText(InputGaji);
               JTextCuti.setText(InputCuti);
               jComboBoxStatusMenikah1.setSelectedItem(KantorStr);
               jComboBoxStatusMenikah2.setSelectedItem(KantorCab);
               jComboBoxStatusJabatan.setSelectedItem(PilihJabStr);
               
               Statement stm2 = K.createStatement();
               Query = "SELECT tingkat, nama_sekolah, jurusan, tahun1 from detail_pelamar_pendidikan  where trans_no = '"+TransNo +"'  order by created_dated desc ";
  
               HQ2 = stm2.executeQuery(Query);              
               baris = HQ2.getRow();
               while(HQ2.next()  ){
                   tingkat     = HQ2.getString("tingkat");
                   nama_sekolah= HQ2.getString("nama_sekolah");
                   jurusan     = HQ2.getString("jurusan");
                   tahun1      = HQ2.getString("tahun1");
                   
                   String[] add         = {HQ2.getRow()+ "",tingkat, nama_sekolah ,jurusan , tahun1};
                   ModeltabelPendidikan.addRow(add);
               }
               
               Statement stm3 = K.createStatement();
               Query = "SELECT tema, penyelengara, waktu from detail_pelamar_pendidikan_non_formal  where trans_no = '"+TransNo +"'  order by created_dated desc ";
  
               HQ3 = stm3.executeQuery(Query);              
               baris = HQ3.getRow();
               while(HQ3.next()  ){
                   tingkat     = HQ3.getString("tema");
                   nama_sekolah= HQ3.getString("penyelengara");
                   jurusan     = HQ3.getString("waktu");
                   
                   String[] add         = {HQ3.getRow()+ "",tingkat, nama_sekolah ,jurusan };
                   ModeltabelNonFormal.addRow(add);
               }
               

           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
    }
    int Urut; 
    boolean StatusDiterimaKaryawan = false;
    private void SaveDB(){
        int JenisKelamin, StatusMenikah, StatusTempatTinggal, StatusPelamar, StatusPekerja;
        String Nama, KelahiranTempat, KelahiranTanggal , Kelahiran, Agama, Kewarganegaraan,
        KTP, SIM,Alamat, KodePos, NoTelpon, NoHp, Email, Hoby, TanggalDibuat, GajiPokok;
        
        Nama            = JTextNama.getText();
        KelahiranTempat = JTextKelahiran.getText();
        KelahiranTanggal= dateChooserComboPelamar.getText();
        Agama           = JTextAgama.getText();
        Kewarganegaraan = JTextKewarganegaraan.getText();
        KTP             = JTextKTP.getText();
        SIM             = JTextSIM.getText();
        Alamat          = JTextAreaAlamat.getText();
        KodePos         = JTextAlamatKodePos.getText();
        NoTelpon        = JTextAlamatTelpon.getText();
        NoHp            = JTextAsalTelpon1.getText();
        Email           = JTextEmail.getText();
        Hoby            = JTextAsalKodePos.getText();
        TanggalDibuat   = JLabelTanggal.getText();
        GajiPokok        = JTextGajiPokok.getText();
        
        JenisKelamin    = jComboBoxStatusKelamin.getSelectedIndex();
        StatusMenikah   = jComboBoxStatusMenikah.getSelectedIndex();
        StatusTempatTinggal   = JComboStatucRumah.getSelectedIndex();
        StatusPelamar   =  status_pelamar;
        StatusPekerja   = jComboBoxStatusPekerja.getSelectedIndex();
        
        String NoPel            = NoPelamar();
        String NoPeg = null;

        NoPel = JLabelNoFomulir.getText();
        Satu.DeleteData(NoPel, "header_pelamar");


        if (status_pelamar == 0 ){
            NoPel           = JLabelNoFomulir.getText();
            Satu.DeleteData(NoPel, "header_pelamar");
            StatusDiterimaKaryawan = true;
            NoPeg            = NIK.getText();
        }

          try {
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    Stm.executeUpdate("INSERT INTO header_pelamar "
                            + " VALUES ('"+NoPel+ "','"+Nama+"','" +KelahiranTanggal + "','" + KelahiranTempat
                            + "','"+ JenisKelamin + "','"+ StatusMenikah +"','"+ Agama +"','"+ Kewarganegaraan+ "','"+KTP 
                            + "','"+ SIM + "','"+Alamat +"','"+ KodePos +"','"+ NoTelpon +"','"+ NoHp +"','"+ Email 
                            + "','"+ StatusTempatTinggal +"','"+ Hoby
                            + "','"+StatusPelamar + "',now(),'" + Urut +"','"+ Periode() + "','" +TanggalDibuat +"')");   

                    Statement Stm2 = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    for (int a = 0; a < JTablePendidikan.getRowCount(); a++){
                        Stm2.executeUpdate("insert into detail_pelamar_pendidikan "
                            + " values ('"
                            + NoPel 
                            + "','" + JTablePendidikan.getValueAt(a, 1) 
                            + "','" + JTablePendidikan.getValueAt(a, 2) 
                            + "','" + JTablePendidikan.getValueAt(a, 3) 
                            + "','" + JTablePendidikan.getValueAt(a, 4)+"', now() )");       
                    }
                    
                    Statement Stm3 = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    for (int a = 0; a < JTableNonFormal.getRowCount(); a++){
                        Stm3.executeUpdate("insert into detail_pelamar_pendidikan_non_formal "
                            + " values ('"
                            + NoPel + "','" 
                            + JTableNonFormal.getValueAt(a, 1) + "','" 
                            + JTableNonFormal.getValueAt(a, 2) 
                            + "','" + JTableNonFormal.getValueAt(a, 3) 
                            + "', now() )");       
                    }
                    StatusSave = true;
                    StatusLabel.setText("Saved");
                    
                    /*
                     * Karyawan diterima
                     * PilihJabStr, KantorStr, InputCuti, InputGaji
                     */                  
                    if (StatusDiterimaKaryawan){
                        Statement Stm4 = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        Stm4.executeUpdate("INSERT INTO header_pegawai "
                            + " VALUES ('"+NoPel+ "','"+Urut_Pegawai+"','" +NoPeg + "','" + KantorStr + "','" + KantorCab
                            + "','"+ PilihJabStr + "','"+ InputCuti +"','"+ GajiPokok +"','"+ Periode()+ "','"+StatusPekerja+"')"); 
                    }
                    JButtonPelamarSave.setEnabled(false);
                    JButtonPelamarEdit.setEnabled(true);

                 }
                 catch (Exception Ex){
                     System.out.println(Ex);
                     LabelPeringantan.setText("Data Sudah Ada" + Ex);
                     StatusLabel.setText("Gagal Saved");
                    //JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
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

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        JLabelNoFomulir = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        JLabelTanggal = new javax.swing.JLabel();
        LabelPeringantan = new javax.swing.JLabel();
        JButtonPelamarSave = new javax.swing.JButton();
        JButtonPelamarEdit = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        NIK = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        JTextNama = new javax.swing.JTextField();
        JTextKelahiran = new javax.swing.JTextField();
        JTextKTP = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTextAreaAlamat = new javax.swing.JTextArea();
        JTextAlamatTelpon = new javax.swing.JTextField();
        JTextAgama = new javax.swing.JTextField();
        JTextAlamatKodePos = new javax.swing.JTextField();
        JTextEmail = new javax.swing.JTextField();
        JTextAsalKodePos = new javax.swing.JTextField();
        dateChooserComboPelamar = new datechooser.beans.DateChooserCombo();
        label18 = new java.awt.Label();
        label24 = new java.awt.Label();
        label19 = new java.awt.Label();
        label20 = new java.awt.Label();
        label25 = new java.awt.Label();
        Kewarganegaraan = new java.awt.Label();
        label22 = new java.awt.Label();
        label23 = new java.awt.Label();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jComboBoxStatusKelamin = new javax.swing.JComboBox();
        jComboBoxStatusMenikah = new javax.swing.JComboBox();
        JTextKewarganegaraan = new javax.swing.JTextField();
        label26 = new java.awt.Label();
        jLabel23 = new javax.swing.JLabel();
        JTextSIM = new javax.swing.JTextField();
        JTextAsalTelpon1 = new javax.swing.JTextField();
        JComboStatucRumah = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        StatusLabel = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        JTextGajiPokok = new javax.swing.JTextField();
        JTextCuti = new javax.swing.JTextField();
        jComboBoxStatusMenikah1 = new javax.swing.JComboBox();
        jComboBoxStatusMenikah2 = new javax.swing.JComboBox();
        Jabatan = new javax.swing.JLabel();
        jComboBoxStatusJabatan = new javax.swing.JComboBox();
        jComboBoxStatusPekerja = new javax.swing.JComboBox();
        Jabatan1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTablePendidikan = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        JTextAlamatKodePos1 = new javax.swing.JTextField();
        dateChooserComboPelamar1 = new datechooser.beans.DateChooserCombo();
        jLabel10 = new javax.swing.JLabel();
        dateChooserComboPelamar2 = new datechooser.beans.DateChooserCombo();
        JTextAlamatKodePos2 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        JTextAlamatKodePos3 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButtonPendidikan = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        JTableNonFormal = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        JTextAlamatKodePos4 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        JTextAlamatKodePos5 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        dateChooserComboPelamar3 = new datechooser.beans.DateChooserCombo();
        JButtonNonFormal = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setText("No Fomulir :");

        JLabelNoFomulir.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabelNoFomulir.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel11.setText("Tanggal");

        JLabelTanggal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabelTanggal.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        JButtonPelamarSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/save_icon2.png"))); // NOI18N
        JButtonPelamarSave.setText("Save");
        JButtonPelamarSave.setEnabled(false);

        JButtonPelamarEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/edit_icon2.png"))); // NOI18N
        JButtonPelamarEdit.setText("Edit");

        jLabel3.setText("NIK : ");

        NIK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NIK.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(275, 275, 275)
                        .addComponent(LabelPeringantan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(575, 575, 575))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(JLabelNoFomulir, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                            .addComponent(JLabelTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NIK, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonPelamarSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonPelamarEdit)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(JLabelNoFomulir, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(NIK, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(JLabelTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JButtonPelamarEdit)
                        .addComponent(JButtonPelamarSave)))
                .addGap(30, 30, 30)
                .addComponent(LabelPeringantan, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        JTextNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextNama.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextNama.setEnabled(false);

        JTextKelahiran.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextKelahiran.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextKelahiran.setEnabled(false);

        JTextKTP.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextKTP.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextKTP.setEnabled(false);

        JTextAreaAlamat.setColumns(20);
        JTextAreaAlamat.setRows(5);
        JTextAreaAlamat.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextAreaAlamat.setEnabled(false);
        jScrollPane1.setViewportView(JTextAreaAlamat);

        JTextAlamatTelpon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextAlamatTelpon.setEnabled(false);

        JTextAgama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextAgama.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextAgama.setEnabled(false);

        JTextAlamatKodePos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextAlamatKodePos.setEnabled(false);

        JTextEmail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextEmail.setEnabled(false);

        JTextAsalKodePos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextAsalKodePos.setEnabled(false);

        dateChooserComboPelamar.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    dateChooserComboPelamar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
        (java.awt.Color)null,
        (java.awt.Color)null,
        (java.awt.Color)null,
        (java.awt.Color)null));
dateChooserComboPelamar.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
try {
    dateChooserComboPelamar.setDefaultPeriods(new datechooser.model.multiple.PeriodSet(new datechooser.model.multiple.Period(new java.util.GregorianCalendar(2014, 2, 7),
        new java.util.GregorianCalendar(2014, 2, 7))));
} catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
e1.printStackTrace();
}
dateChooserComboPelamar.setEnabled(false);
dateChooserComboPelamar.setLocale(new java.util.Locale("in", "ID", ""));

label18.setAlignment(java.awt.Label.RIGHT);
label18.setText("Status *");

label24.setText("Nama Lengkap");

label19.setText("Tempat dan Tanggal Lahir");

label20.setText("Jenis Kelamin");

label25.setText("Agama");

Kewarganegaraan.setText("Kewarganegaraan");

label22.setText("Nomor KTP ( DKI / Daerah ) *");

label23.setText("Alamat Lengkap");

jLabel19.setText("No Telpon");

jLabel21.setText("Kode Pos");

jLabel20.setText("Email");

jLabel22.setText("Status Tempat Tinggal *");

jComboBoxStatusKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "L", "P" }));
jComboBoxStatusKelamin.setEnabled(false);

jComboBoxStatusMenikah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Menikah", "Belum Menikah" }));
jComboBoxStatusMenikah.setEnabled(false);

JTextKewarganegaraan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
JTextKewarganegaraan.setDisabledTextColor(new java.awt.Color(0, 0, 0));
JTextKewarganegaraan.setEnabled(false);

label26.setText("Nomor SIM ( A / B / C ) *");

jLabel23.setText("Hp");

JTextSIM.setBorder(javax.swing.BorderFactory.createEtchedBorder());
JTextSIM.setEnabled(false);

JTextAsalTelpon1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
JTextAsalTelpon1.setEnabled(false);

JComboStatucRumah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sewa", "Kontrak", "Ikut Orang Tua", "Ikut Wali" }));
JComboStatucRumah.setEnabled(false);

jLabel24.setText("Hobby");

StatusLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
StatusLabel.setForeground(new java.awt.Color(255, 51, 51));
StatusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

jLabel25.setText("Cuti");

jLabel26.setText("Cabang");

jLabel27.setText("Cabang Lokasi");

jLabel28.setText("Gaji Pokok");

JTextGajiPokok.setBorder(javax.swing.BorderFactory.createEtchedBorder());
JTextGajiPokok.setEnabled(false);

JTextCuti.setBorder(javax.swing.BorderFactory.createEtchedBorder());
JTextCuti.setEnabled(false);

jComboBoxStatusMenikah1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kantor Cabang Pembantu (KCP)", "Kantor Kas" }));
jComboBoxStatusMenikah1.setEnabled(false);

jComboBoxStatusMenikah2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bekasi - Bantar Gebang", "Bekasi - Pondok Ungu", "Bekasi - Kranji", "Jakpus - Johar", "Jaktim - Cibubur", "Cikampek - Cikampek", "DKI - Johar", "Jawa Barat - Karawang" }));
jComboBoxStatusMenikah2.setEnabled(false);

Jabatan.setText("Jabatan");

jComboBoxStatusJabatan.setEnabled(false);

jComboBoxStatusPekerja.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aktiv", "Resign" }));
jComboBoxStatusPekerja.setEnabled(false);

Jabatan1.setText("Status");

javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
jPanel8.setLayout(jPanel8Layout);
jPanel8Layout.setHorizontalGroup(
    jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(jPanel8Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(label23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(label26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(label24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(label19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(label25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Kewarganegaraan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(label22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(label20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGap(5, 5, 5)
        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jComboBoxStatusKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(JTextAgama, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(JTextKewarganegaraan, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(JTextKelahiran, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateChooserComboPelamar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jComboBoxStatusMenikah, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 267, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBoxStatusMenikah1, javax.swing.GroupLayout.Alignment.LEADING, 0, 1, Short.MAX_VALUE)
                            .addComponent(JTextAsalKodePos, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JComboStatucRumah, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JTextEmail, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JTextAlamatTelpon, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JTextAlamatKodePos, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxStatusMenikah2, 0, 164, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Jabatan1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(JTextAsalTelpon1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(JTextCuti, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBoxStatusJabatan, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JTextGajiPokok, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBoxStatusPekerja, 0, 139, Short.MAX_VALUE))
                        .addGap(2, 2, 2)
                        .addComponent(StatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addComponent(JTextSIM)
                    .addComponent(JTextKTP)
                    .addComponent(JTextNama))
                .addContainerGap())))
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addGap(5, 5, 5)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addComponent(label24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(label19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addComponent(JTextNama, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(dateChooserComboPelamar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextKelahiran, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGap(5, 5, 5)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBoxStatusKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(5, 5, 5)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(JTextAgama, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(label25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(5, 5, 5)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Kewarganegaraan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextKewarganegaraan, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(2, 2, 2)
                    .addComponent(jComboBoxStatusMenikah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(5, 5, 5)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JTextKTP, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(label22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(5, 5, 5)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(label26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(JTextSIM, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(5, 5, 5)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JTextAlamatKodePos, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21))
                    .addGap(5, 5, 5)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JTextAlamatTelpon, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19)
                        .addComponent(jLabel23)
                        .addComponent(JTextAsalTelpon1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(4, 4, 4)
                    .addComponent(label23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(5, 5, 5)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(JTextEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel20)
                .addComponent(jLabel28)
                .addComponent(JTextGajiPokok, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(5, 5, 5)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(JComboStatucRumah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel22)
                .addComponent(jLabel25)
                .addComponent(JTextCuti, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(4, 4, 4)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel24)
                .addComponent(JTextAsalKodePos, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Jabatan)
                .addComponent(jComboBoxStatusJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(jComboBoxStatusMenikah1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jComboBoxStatusMenikah2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(StatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxStatusPekerja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Jabatan1)))
            .addContainerGap(84, Short.MAX_VALUE))
    );

    jTabbedPane1.addTab("Data Karyawan", jPanel8);

    JTablePendidikan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    JTablePendidikan.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    jScrollPane2.setViewportView(JTablePendidikan);

    jLabel6.setText("Data Pendidikan :");

    jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    JTextAlamatKodePos1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    JTextAlamatKodePos1.setEnabled(false);

    dateChooserComboPelamar1.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dateChooserComboPelamar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
    (java.awt.Color)null,
    (java.awt.Color)null,
    (java.awt.Color)null,
    (java.awt.Color)null));
    dateChooserComboPelamar1.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
    try {
        dateChooserComboPelamar1.setDefaultPeriods(new datechooser.model.multiple.PeriodSet(new datechooser.model.multiple.Period(new java.util.GregorianCalendar(2014, 2, 7),
            new java.util.GregorianCalendar(2014, 2, 7))));
} catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
    e1.printStackTrace();
    }
    dateChooserComboPelamar1.setEnabled(false);
    dateChooserComboPelamar1.setLocale(new java.util.Locale("in", "ID", ""));

    jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel10.setText("-");

    dateChooserComboPelamar2.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dateChooserComboPelamar2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
    (java.awt.Color)null,
    (java.awt.Color)null,
    (java.awt.Color)null,
    (java.awt.Color)null));
    dateChooserComboPelamar2.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
    try {
        dateChooserComboPelamar2.setDefaultPeriods(new datechooser.model.multiple.PeriodSet(new datechooser.model.multiple.Period(new java.util.GregorianCalendar(2014, 2, 7),
            new java.util.GregorianCalendar(2014, 2, 7))));
} catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
    e1.printStackTrace();
    }
    dateChooserComboPelamar2.setEnabled(false);
    dateChooserComboPelamar2.setLocale(new java.util.Locale("in", "ID", ""));

    JTextAlamatKodePos2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    JTextAlamatKodePos2.setEnabled(false);

    jLabel9.setText("Tahun");

    jLabel7.setText("Nama Sekolah");

    JTextAlamatKodePos3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    JTextAlamatKodePos3.setEnabled(false);

    jLabel12.setText("Tingkat");

    jLabel8.setText("Jurusan");

    jButtonPendidikan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/add.png"))); // NOI18N
    jButtonPendidikan.setText("add");
    jButtonPendidikan.setEnabled(false);

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(JTextAlamatKodePos3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel7))
                .addComponent(JTextAlamatKodePos2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(jLabel9)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dateChooserComboPelamar2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dateChooserComboPelamar1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonPendidikan))
                .addComponent(JTextAlamatKodePos1))
            .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
            .addGap(5, 5, 5)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(JTextAlamatKodePos1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7)
                .addComponent(JTextAlamatKodePos3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(JTextAlamatKodePos2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(9, 9, 9))
                .addComponent(dateChooserComboPelamar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonPendidikan)
                    .addComponent(dateChooserComboPelamar1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(6, 6, 6))
    );

    JTableNonFormal.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    jScrollPane4.setViewportView(JTableNonFormal);

    jLabel13.setText("Kursus / seminar ( berhubungan dengan pekerjaan )");

    jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabel14.setText("Tema");

    JTextAlamatKodePos4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    JTextAlamatKodePos4.setEnabled(false);

    jLabel15.setText("Penyelengara");

    JTextAlamatKodePos5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    JTextAlamatKodePos5.setEnabled(false);

    jLabel16.setText("Waktu");

    dateChooserComboPelamar3.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dateChooserComboPelamar3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
    (java.awt.Color)null,
    (java.awt.Color)null,
    (java.awt.Color)null,
    (java.awt.Color)null));
    dateChooserComboPelamar3.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
    try {
        dateChooserComboPelamar3.setDefaultPeriods(new datechooser.model.multiple.PeriodSet(new datechooser.model.multiple.Period(new java.util.GregorianCalendar(2014, 2, 7),
            new java.util.GregorianCalendar(2014, 2, 7))));
} catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
    e1.printStackTrace();
    }
    dateChooserComboPelamar3.setEnabled(false);
    dateChooserComboPelamar3.setLocale(new java.util.Locale("in", "ID", ""));

    JButtonNonFormal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/add.png"))); // NOI18N
    JButtonNonFormal.setText("add");
    JButtonNonFormal.setEnabled(false);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addComponent(JTextAlamatKodePos5, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel16)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dateChooserComboPelamar3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JButtonNonFormal))
                .addComponent(JTextAlamatKodePos4))
            .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel4Layout.createSequentialGroup()
            .addGap(5, 5, 5)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(JTextAlamatKodePos4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextAlamatKodePos5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addComponent(dateChooserComboPelamar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(JButtonNonFormal))
            .addGap(16, 16, 16))
    );

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 272, Short.MAX_VALUE))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING))
            .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addGap(5, 5, 5)
            .addComponent(jLabel6)
            .addGap(5, 5, 5)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jLabel13)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(75, Short.MAX_VALUE))
    );

    jTabbedPane1.addTab("Riwayat Pendidikan Karyawan", jPanel1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(jTabbedPane1)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTabbedPane1))
    );

    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((screenSize.width-663)/2, (screenSize.height-625)/2, 663, 625);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Hrd_DataKaryawanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Hrd_DataKaryawanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Hrd_DataKaryawanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Hrd_DataKaryawanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Hrd_DataKaryawanView dialog = new Hrd_DataKaryawanView(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonNonFormal;
    private javax.swing.JButton JButtonPelamarEdit;
    private javax.swing.JButton JButtonPelamarSave;
    private javax.swing.JComboBox JComboStatucRumah;
    private javax.swing.JLabel JLabelNoFomulir;
    private javax.swing.JLabel JLabelTanggal;
    private javax.swing.JTable JTableNonFormal;
    private javax.swing.JTable JTablePendidikan;
    private javax.swing.JTextField JTextAgama;
    private javax.swing.JTextField JTextAlamatKodePos;
    private javax.swing.JTextField JTextAlamatKodePos1;
    private javax.swing.JTextField JTextAlamatKodePos2;
    private javax.swing.JTextField JTextAlamatKodePos3;
    private javax.swing.JTextField JTextAlamatKodePos4;
    private javax.swing.JTextField JTextAlamatKodePos5;
    private javax.swing.JTextField JTextAlamatTelpon;
    private javax.swing.JTextArea JTextAreaAlamat;
    private javax.swing.JTextField JTextAsalKodePos;
    private javax.swing.JTextField JTextAsalTelpon1;
    private javax.swing.JTextField JTextCuti;
    private javax.swing.JTextField JTextEmail;
    private javax.swing.JTextField JTextGajiPokok;
    private javax.swing.JTextField JTextKTP;
    private javax.swing.JTextField JTextKelahiran;
    private javax.swing.JTextField JTextKewarganegaraan;
    private javax.swing.JTextField JTextNama;
    private javax.swing.JTextField JTextSIM;
    private javax.swing.JLabel Jabatan;
    private javax.swing.JLabel Jabatan1;
    private java.awt.Label Kewarganegaraan;
    private javax.swing.JLabel LabelPeringantan;
    private javax.swing.JLabel NIK;
    private javax.swing.JLabel StatusLabel;
    private datechooser.beans.DateChooserCombo dateChooserComboPelamar;
    private datechooser.beans.DateChooserCombo dateChooserComboPelamar1;
    private datechooser.beans.DateChooserCombo dateChooserComboPelamar2;
    private datechooser.beans.DateChooserCombo dateChooserComboPelamar3;
    private javax.swing.JButton jButtonPendidikan;
    private javax.swing.JComboBox jComboBoxStatusJabatan;
    private javax.swing.JComboBox jComboBoxStatusKelamin;
    private javax.swing.JComboBox jComboBoxStatusMenikah;
    private javax.swing.JComboBox jComboBoxStatusMenikah1;
    private javax.swing.JComboBox jComboBoxStatusMenikah2;
    private javax.swing.JComboBox jComboBoxStatusPekerja;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private java.awt.Label label18;
    private java.awt.Label label19;
    private java.awt.Label label20;
    private java.awt.Label label22;
    private java.awt.Label label23;
    private java.awt.Label label24;
    private java.awt.Label label25;
    private java.awt.Label label26;
    // End of variables declaration//GEN-END:variables
}
