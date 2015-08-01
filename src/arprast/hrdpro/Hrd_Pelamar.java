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
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.PatternSyntaxException;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
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
public class Hrd_Pelamar extends javax.swing.JPanel {

    /**
     * Creates new form Hrd_Pelamar
     */
    private SatuUntukSemua      Satu = new SatuUntukSemua();
    private DefaultTableModel   Modeltabel2             = new DefaultTableModel();
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
    
    String AA;
    public Hrd_Pelamar() {
        initComponents();
        Tabel1();
        TabelPendidikan();
        TabelNoPendidikan();
        Tampilan ();
        Aksi();
        AmbilDataDariDatabase("");
    }
    
    private void Tampilan (){
         GroupsStatus.add(StatusDitolak);
         GroupsStatus.add(StatusDiterima);
         GroupsStatus.add(StatusMenunggu);
         
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
        JTextTabelPelamar.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    ViewDataDatabasePelamar((String )JTextTabelPelamar.getValueAt(JTextTabelPelamar.getSelectedRow(), 1));
                    Edit = false;
                    DisabelEnable(false); 
                }
            }   
        });
        
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
        
        jButtonRefresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
               int a  = jComboBoxStatusPelamar.getSelectedIndex();
               String data = null;
               if (a == 0 ){
                   data = "";
               }
               else if (a == 1 ) {
                   data = "0";
               }
               else if (a == 2 ) {
                   data = "1";
               }
               else if (a == 3 ) {
                   data = "2";
               }
               AmbilDataDariDatabase(data);
            }
        });
        
        JButtonPelamarSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                
                if (Validasi ()){
                    
                    DisabelEnable(false);
                    
                    Component[] Tombol = {JButtonPelamarNew, JButtonPelamarSave, JButtonPelamarEdit};
                    boolean[] Benar = {true, false, true};
                    Satu.LogikaComponent(Tombol, Benar);
                    SaveDB();
                    Edit = false;
                    
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
                
            }
        });
        
        JButtonPelamarEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                DisabelEnable(true);        
                
                Component[] Tombol = {JButtonPelamarNew, JButtonPelamarSave, JButtonPelamarEdit};
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
        
        JButtonPelamarPrintForm.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                PrintOut();
            }
        });
        
        StatusDiterima.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                PelamarDIterima();
            }
        });
        
        AntiHuruf.SetAntiAngkaLimit(Cuti, 3);
        AntiHuruf.SetAntiAngka(Gaji);
    }
    JTextField Cuti = new JTextField() , Gaji = new JTextField(); JComboBox PilihJab = new JComboBox() , Kantor = new JComboBox(), KantorLokasi = new JComboBox();
    String PilihJabStr, KantorStr, InputCuti, InputGaji, KantorCab;
    private void  PelamarDIterima (){
        
        Cuti.setText("");
        Gaji.setText("");
        
        Gaji.setHorizontalAlignment(JTextField.RIGHT);

        Kantor.addItem("Kantor Cabang Pembantu (KCP)");
        Kantor.addItem("Kantor Kas");
        
        KantorLokasi.addItem("");
        Kantor.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                AA =  e.getActionCommand();
                if (Kantor.getSelectedIndex() == 0){
                    String[] Data = {"DKI - Johar","Jawa Barat - Karawang"};
                    KantorLokasi.removeAllItems();
                    for (int a = 0 ; a < Data.length ; a++){
                        KantorLokasi.addItem(Data[a]);
                    }
                }
                else if (Kantor.getSelectedIndex() == 1) {
                    KantorLokasi.removeAllItems();
                    String[] Data = {"Bekasi - Bantar Gebang",
                            "Bekasi - Pondok Ungu",
                            "Bekasi - Kranji",
                            "Jakpus - Johar",
                            "Jaktim - Cibubur",
                            "Cikampek - Cikampek"};
                    for (int a = 0 ; a < Data.length ; a++){
                        KantorLokasi.addItem(Data[a]);
                    }
                }
            }
        });
        

        PilihJab.addItem("Direktur");
        PilihJab.addItem("Marketing");
        PilihJab.addItem("Umum");
        PilihJab.addItem("Teller");
        
         Object[] Object ={
          "Pilih departement    : ", Kantor,
          "Pilih kantor cab     : ", KantorLokasi,
          "Pilih jabatan        : ", PilihJab,
          "Input jumlah cuti    : ", Cuti,
          "Input gaji karyawan  : ", Gaji
        };
        
        int Pilih = JOptionPane.showConfirmDialog(null , Object , "Pilih dep&jab ", JOptionPane.OK_CANCEL_OPTION);
        if (Pilih == JOptionPane.OK_OPTION){
            this.KantorStr = (String ) Kantor.getSelectedItem();
            this.PilihJabStr = (String ) PilihJab.getSelectedItem();
            this.InputCuti   =  Cuti.getText();
            this.InputGaji   =  Gaji.getText();
            this.KantorCab   = (String ) KantorLokasi.getSelectedItem();
        }
    }
    private void Reset(){
        StatusMenunggu.setSelected(true);
        StatusLabel.setText("");
        Edit = false;
        JTextField[] DataField = {JTextNama, JTextKelahiran, JTextAgama, 
            JTextKewarganegaraan, JTextKTP, JTextSIM, 
            JTextAlamatKodePos, JTextAlamatTelpon, JTextAsalTelpon1,
            JTextEmail, JTextAsalKodePos, JTextAlamatKodePos3, JTextAlamatKodePos1, JTextAlamatKodePos2, 
            JTextAlamatKodePos4, JTextAlamatKodePos5 };
        JLabel Label[] = {StatusLabel, JLabelNoFomulir};
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
            StatusDiterima, StatusDitolak, StatusMenunggu, dateChooserComboPelamar,
            jButtonPendidikan, JButtonNonFormal,
            JTextAlamatKodePos3, JTextAlamatKodePos1, JTextAlamatKodePos2, 
            JTextAlamatKodePos4, JTextAlamatKodePos5, dateChooserComboPelamar2,
            dateChooserComboPelamar1, dateChooserComboPelamar3
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
   
   private Color evenBackColor = new Color(80, 80, 80);
   private Color evenTextColor = Color.white;
   private Color oddBackColor = new Color(96, 96, 96);
   private Color oddTextColor = Color.white;
   private Color rolloverBackColor = new Color(128, 128, 128);
   private Color rolloverTextColor = Color.white;
   private int rolloverRowIndex = -1;
         
   private void RubahWarnaTable(JTable tabel, int row ){

         
   }
   
   private void Tabel1(){
       
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No", "No Fomulir","Nama ", "Jenis Kelamin","Agama","No Hp", "No Telphone", "Email","periode ", "Action"};
        Modeltabel2 = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 9) {return true ;} 
                        return false;   //Disallow the editing of any cellJTextTabelPelamar.
                 }

        };

        JTextTabelPelamar.setModel(Modeltabel2);
        

       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel2);
        JTextTabelPelamar.setRowSorter(sorter);
        JTextFieldSearch.addKeyListener(new KeyListener(){
            @Override
            public void keyReleased(KeyEvent e) {
               String text = JTextFieldSearch.getText();
               if (text.length() == 0) {
                 sorter.setRowFilter(null);
               } else {
                 try {
                   sorter.setRowFilter(
                       RowFilter.regexFilter("(?i)"+text));
                 } catch (PatternSyntaxException pse) {
                   System.err.println("Bad regex pattern");
                 }
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
        JTextTabelPelamar.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTextTabelPelamar.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTextTabelPelamar.getColumnModel().getColumn(2).setCellRenderer( tengah );
        JTextTabelPelamar.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        JTextTabelPelamar.getColumnModel().getColumn(4).setCellRenderer( tengah ); 
        JTextTabelPelamar.getColumnModel().getColumn(5).setCellRenderer( tengah ); 
        JTextTabelPelamar.getColumnModel().getColumn(6).setCellRenderer( tengah ); 
        JTextTabelPelamar.getColumnModel().getColumn(7).setCellRenderer( tengah );
        JTextTabelPelamar.getColumnModel().getColumn(8).setCellRenderer( tengah );
        JTextTabelPelamar.getColumnModel().getColumn(9).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,100,200,40,100,100,100, 120,70, 100 };
        Sistem.Colom_table ukuran_colom = new Sistem.Colom_table();
        ukuran_colom.ukuran_colom(JTextTabelPelamar, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTextTabelPelamar.setName("Pelamar");
        JTextTabelPelamar.getColumnModel().getColumn(9).setCellRenderer(  new ButtonJTable("Delete"));
        JTextTabelPelamar.getColumnModel().getColumn(9).setCellEditor( new  ButtonJTableKeDuaLocal(new JCheckBox(),Modeltabel2, JTextTabelPelamar));
       
        /*
         * Disable drag colum tabel
         */       
        JTextTabelPelamar.getTableHeader().setReorderingAllowed(false); 
        
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
       
    private int   DeteksiStatusPelamar(){
        if (StatusDiterima.isSelected()){
            return 0;
        }
        else if (StatusDitolak.isSelected()){
            return 1;
        }
        else if (StatusMenunggu.isSelected()){
            return 2;
        }
        else {
            JOptionPane.showMessageDialog(null, "Tidak ada pilihan status" );
            return 3;
        }
    }   
    private void ViewDataDatabasePelamar(String TransNo){
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
                int status_nikah = 0;
                String agama = null ;
                String kewarganegaraan = null  ;
                int no_ktp = 0;
                int no_sim = 0;
                String alamat = null;
                int kode_pos = 0, Kelamin   ;
                int status_tempat_tinggal = 0 ;
                String hobby ;
                int status_pelamar = 0 ;
                String tanggal_dibuat = null;
                   
               Statement stm = K.createStatement();
               String Query = "SELECT kelahiran_tanggal, kelahiran_tempat, status_nikah, "
               + " kewarganegaraan, no_ktp, no_sim, alamat, kode_pos,"
               + " status_tempat_tinggal, hobby, status_pelamar, tanggal_dibuat, key_no "
               + "from header_pelamar where trans_no = '"+TransNo +"'  order by created_date desc ";
  
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
               }
               int SelectRow = JTextTabelPelamar.getSelectedRow();
               JTextNama.setText((String)JTextTabelPelamar.getValueAt(SelectRow, 2));
               JLabelNoFomulir.setText((String)JTextTabelPelamar.getValueAt(SelectRow, 1));
               
               if (((String)JTextTabelPelamar.getValueAt(SelectRow, 3) ).equals("L")){
                   Kelamin = 0;
               }
               else {
                    Kelamin = 1;
               }
               
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
                
               jComboBoxStatusKelamin.setSelectedIndex(Kelamin);
               JTextKelahiran.setText(kelahirian_tempat);
               
               jComboBoxStatusMenikah.setSelectedIndex(status_nikah);
               
               JTextAgama.setText((String)JTextTabelPelamar.getValueAt(SelectRow, 4));
               JTextKewarganegaraan.setText(kewarganegaraan);
               JTextKTP.setText(no_ktp+"");
               JTextSIM.setText(no_sim+"");
               JTextAreaAlamat.setText(alamat);
               JTextAlamatKodePos.setText(kode_pos+"");
               JTextAlamatTelpon.setText((String)JTextTabelPelamar.getValueAt(SelectRow, 5));
               JTextAsalTelpon1.setText((String)JTextTabelPelamar.getValueAt(SelectRow, 6));
               JTextEmail.setText((String)JTextTabelPelamar.getValueAt(SelectRow, 7));
               JTextAsalKodePos.setText(alamat);
               JComboStatucRumah.setSelectedIndex(status_tempat_tinggal);
               JLabelTanggal.setText(tanggal_dibuat);
               
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
               
               JButtonPelamarEdit.setEnabled(true);
               if (status_pelamar == 0){
                   StatusDiterima.setSelected(true);
                   JButtonPelamarEdit.setEnabled(false);
               }
               else if (status_pelamar ==1){
                   StatusDitolak.setSelected(true);
               }
               else if  (status_pelamar == 2){
                   StatusMenunggu.setSelected(true);
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
        int JenisKelamin, StatusMenikah, StatusTempatTinggal, StatusPelamar;
        String Nama, KelahiranTempat, KelahiranTanggal , Kelahiran, Agama, Kewarganegaraan,
        KTP, SIM,Alamat, KodePos, NoTelpon, NoHp, Email, Hoby, TanggalDibuat;
        
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
        
        JenisKelamin    = jComboBoxStatusKelamin.getSelectedIndex();
        StatusMenikah   = jComboBoxStatusMenikah.getSelectedIndex();
        StatusTempatTinggal   = JComboStatucRumah.getSelectedIndex();
        StatusPelamar   =  DeteksiStatusPelamar();
        
        String NoPel            = NoPelamar();
        String NoPeg = null;
        if (Edit){
            NoPel = JLabelNoFomulir.getText();
            Satu.DeleteData(NoPel, "header_pelamar");
        }
        else {
            Urut = TN.GetNoUrut();
        }
        
        int NoPegg = 0;
        if (StatusDiterima.isSelected()){
            NoPel           = JLabelNoFomulir.getText();
            Satu.DeleteData(NoPel, "header_pelamar");
            StatusDiterimaKaryawan = true;
            NoPeg            = NoPegawai();
            NoPegg = TN.GetNoUrut();
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
                            + " VALUES ('"+NoPel+ "','"+NoPegg+"','" +NoPeg + "','" + KantorStr + "','" + KantorCab
                            + "','"+ PilihJabStr + "','"+ InputCuti +"','"+ InputGaji +"','"+ Periode()+ "','"+0+"')"); 
                    }

                 }
                 catch (Exception Ex){
                     System.out.println(Ex);
                     LabelPeringantan.setText("Data Sudah Ada" + Ex);
                     StatusLabel.setText("Gagal Saved");
                    //JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
              }
          AmbilDataDariDatabase("");
    }
    
    /*
     * a = 0 => ambildatabase header
     * a = 1 => ambildatabase untuk di view
     * 
     */
    private void AmbilDataDariDatabase(String Stat){
        Reset();
        if (StatusSave){
            StatusLabel.setText("Saved");
            StatusSave = false;
        }
        Satu.HapusDataJTabel(JTextTabelPelamar);
         
        /*
         * Isi data ke Tabel dari database
         * "No", "No Fomulir","Nama ", "Jenis Kelamin","Agama","No Hp", "No Telphone", "Email","periode ", "Action"
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               String Query = null;
               if (Stat.equals("")){
                   Query = "SELECT  trans_no, nama, jenis_kelamain, agama, no_hp, no_telphone , email, periode "
                            + "from header_pelamar  order by created_date desc ";
               }
               else {
                   Query = "SELECT  trans_no, nama, jenis_kelamain, agama, no_hp, no_telphone , email, periode "
                            + "from header_pelamar where status_pelamar = '"+Stat +"'  order by created_date desc ";
               }
               
               HQ = stm.executeQuery(Query);              
               baris = HQ.getRow();

               while(HQ.next()  ){
                   String NoFormulir    = HQ.getString("trans_no");
                   String Nama          = HQ.getString("nama");
                   String Period        = HQ.getString("periode");
                   int JnsKelamin       = HQ.getInt("jenis_kelamain");;
                   String Agama         = HQ.getString("agama");
                   String NoHp          = HQ.getString("no_hp");
                   String NoTlpn        = HQ.getString("no_telphone");
                   String Email         = HQ.getString("email");
                   
                   String JenisKelamin = null;
                   switch (JnsKelamin){
                       case 0 :
                           JenisKelamin = "L";
                       break;
                       case 1 :
                           JenisKelamin = "P";
                       break;
                       default :
                           JOptionPane.showMessageDialog(null, "error jenis kelamin");
                       break;
                   }
                    String[] add         = {String.valueOf(HQ.getRow()).toString(), NoFormulir , Nama,  JenisKelamin,Agama,
                     NoHp, NoTlpn, Email, Period };
                  
                  
                   Modeltabel2.addRow(add);    
                   
                   //ResetDataPelamar();
                   
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
           
           JTextTabelPelamar.setDefaultRenderer(Object.class,new TableCellRenderer(){

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = (Component) table.getCellRenderer(row, column);
                c.setBackground(row%2==0 ? Color.white : Color.yellow);                        
                return c;
            };

        });
         
        JTextTabelPelamar.setModel(Modeltabel2);      
        
     }
    
     public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
         
            Component c = JTextTabelPelamar.prepareRenderer(renderer, row, column);
            if (JTextTabelPelamar.isRowSelected(row)) {
                c.setForeground(JTextTabelPelamar.getSelectionForeground());
                c.setBackground(JTextTabelPelamar.getSelectionBackground());
            }
            else if (row == rolloverRowIndex) {
                c.setForeground(rolloverTextColor);
                c.setBackground(rolloverBackColor);
            }
            else if (row % 2 == 0) {
                c.setForeground(evenTextColor);
                c.setBackground(evenBackColor);
            }
            else {
                c.setForeground(oddTextColor);
                c.setBackground(oddBackColor);
            }
            return c;
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
        StatusLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        JLabelNoFomulir = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        JLabelTanggal = new javax.swing.JLabel();
        LabelPeringantan = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        JButtonPelamarNew = new javax.swing.JButton();
        JButtonPelamarEdit = new javax.swing.JButton();
        JButtonPelamarPrintForm = new javax.swing.JButton();
        JButtonPelamarSave = new javax.swing.JButton();
        sas = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        JTextFieldSearch = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBoxStatusPelamar = new javax.swing.JComboBox();
        jButtonRefresh = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        JTextTabelPelamar = new javax.swing.JTable();
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
        jLabel3 = new javax.swing.JLabel();
        StatusDiterima = new javax.swing.JRadioButton();
        StatusDitolak = new javax.swing.JRadioButton();
        StatusMenunggu = new javax.swing.JRadioButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jComboBoxStatusKelamin = new javax.swing.JComboBox();
        jComboBoxStatusMenikah = new javax.swing.JComboBox();
        JTextKewarganegaraan = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        JTextSIM = new javax.swing.JTextField();
        JTextAsalTelpon1 = new javax.swing.JTextField();
        JComboStatucRumah = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
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

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        StatusLabel.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        StatusLabel.setForeground(new java.awt.Color(255, 51, 51));

        jLabel2.setText("No Fomulir :");

        JLabelNoFomulir.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabelNoFomulir.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel11.setText("Tanggal");

        JLabelTanggal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabelTanggal.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(294, 294, 294)
                .addComponent(LabelPeringantan, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addGap(575, 575, 575))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JLabelNoFomulir, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLabelTanggal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(StatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(JLabelNoFomulir, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(JLabelTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(StatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addComponent(LabelPeringantan, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonPelamarNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonPelamarNew.setText("New");

        JButtonPelamarEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/edit_icon2.png"))); // NOI18N
        JButtonPelamarEdit.setText("Edit");

        JButtonPelamarPrintForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/print_icon2.png"))); // NOI18N
        JButtonPelamarPrintForm.setText("Print Form");

        JButtonPelamarSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/save_icon2.png"))); // NOI18N
        JButtonPelamarSave.setText("Save");
        JButtonPelamarSave.setEnabled(false);

        sas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/print_icon2.png"))); // NOI18N
        sas.setText("Print Pelamar");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(JButtonPelamarNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPelamarSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPelamarEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPelamarPrintForm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JButtonPelamarPrintForm, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JButtonPelamarNew)
                        .addComponent(JButtonPelamarEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JButtonPelamarSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(sas, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Search  :");

        jLabel5.setText("Status :");

        jComboBoxStatusPelamar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Diterima", "Ditolak", "Menunggu" }));

        jButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Refresh.png"))); // NOI18N
        jButtonRefresh.setText("Refresh");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxStatusPelamar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonRefresh))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jComboBoxStatusPelamar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonRefresh))
                .addGap(4, 4, 4))
        );

        JTextTabelPelamar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextTabelPelamar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(JTextTabelPelamar);

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
        JTextAlamatTelpon.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextAlamatTelpon.setEnabled(false);

        JTextAgama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextAgama.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextAgama.setEnabled(false);

        JTextAlamatKodePos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextAlamatKodePos.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextAlamatKodePos.setDragEnabled(true);
        JTextAlamatKodePos.setEnabled(false);

        JTextEmail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextEmail.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextEmail.setEnabled(false);

        JTextAsalKodePos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JTextAsalKodePos.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        JTextAsalKodePos.setEnabled(false);

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

    jLabel3.setText("Status");

    StatusDiterima.setText("Diterima");
    StatusDiterima.setEnabled(false);

    StatusDitolak.setText("Ditolak");
    StatusDitolak.setEnabled(false);

    StatusMenunggu.setText("Menunggu");
    StatusMenunggu.setEnabled(false);

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

    jLabel23.setText("Hp");

    JTextSIM.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    JTextSIM.setDisabledTextColor(new java.awt.Color(0, 0, 0));
    JTextSIM.setEnabled(false);

    JTextAsalTelpon1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    JTextAsalTelpon1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
    JTextAsalTelpon1.setEnabled(false);

    JComboStatucRumah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sewa", "Kontrak", "Ikut Orang Tua", "Ikut Wali" }));
    JComboStatucRumah.setEnabled(false);

    jLabel24.setText("Hobby");

    jLabel25.setText("Nama Lengkap");

    jLabel26.setText("Tempat Tanggal Lahir");

    jLabel27.setText("Jenis Kelamin");

    jLabel28.setText("Agama");

    jLabel29.setText("Kewarganegaraan");

    jLabel30.setText("Alamat Lengkap");

    jLabel31.setText("Nomor KTP ( DKI / Daerah ) *");

    jLabel32.setText("Nomor SIM ( A / B / C ) *");

    jLabel33.setText("Status *");

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGap(5, 5, 5))
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(JTextAlamatKodePos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextAlamatTelpon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextAsalKodePos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JComboStatucRumah, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel23)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(StatusDiterima)
                                .addComponent(StatusDitolak)
                                .addComponent(StatusMenunggu)))
                        .addComponent(JTextAsalTelpon1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)))
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jComboBoxStatusKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(JTextKelahiran, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextAgama, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextKewarganegaraan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(dateChooserComboPelamar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBoxStatusMenikah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(jScrollPane1)
                .addComponent(JTextSIM)
                .addComponent(JTextKTP)
                .addComponent(JTextNama))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGap(34, 34, 34)
                            .addComponent(jLabel26))
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addComponent(jLabel25)))
                    .addGap(13, 13, 13)
                    .addComponent(jLabel27)
                    .addGap(12, 12, 12)
                    .addComponent(jLabel28)
                    .addGap(12, 12, 12)
                    .addComponent(jLabel29)
                    .addGap(15, 15, 15)
                    .addComponent(jLabel31)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel32)
                    .addGap(9, 9, 9)
                    .addComponent(jLabel30)
                    .addGap(39, 39, 39)
                    .addComponent(jLabel21)
                    .addGap(12, 12, 12)
                    .addComponent(jLabel19)
                    .addGap(9, 9, 9)
                    .addComponent(jLabel20)
                    .addGap(13, 13, 13)
                    .addComponent(jLabel22)
                    .addGap(12, 12, 12)
                    .addComponent(jLabel24))
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(4, 4, 4)
                    .addComponent(JTextNama, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(dateChooserComboPelamar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextKelahiran, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(5, 5, 5)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBoxStatusMenikah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel33)))
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jComboBoxStatusKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(5, 5, 5)
                            .addComponent(JTextAgama, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(5, 5, 5)
                            .addComponent(JTextKewarganegaraan, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(5, 5, 5)
                    .addComponent(JTextKTP, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(JTextSIM, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(JTextAlamatKodePos, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JTextAlamatTelpon, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23)
                        .addComponent(JTextAsalTelpon1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(5, 5, 5)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(JTextEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(5, 5, 5)
                            .addComponent(JComboStatucRumah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(5, 5, 5)
                            .addComponent(JTextAsalKodePos, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(StatusDiterima))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(StatusDitolak)
                            .addGap(3, 3, 3)
                            .addComponent(StatusMenunggu)))))
            .addContainerGap())
    );

    jTabbedPane1.addTab("Data Pelamar", jPanel8);

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
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(JTextAlamatKodePos3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel7))
                .addComponent(JTextAlamatKodePos2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(jLabel9)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dateChooserComboPelamar2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(2, 2, 2)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(2, 2, 2)
                    .addComponent(dateChooserComboPelamar1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButtonPendidikan))
                .addComponent(JTextAlamatKodePos1))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(JTextAlamatKodePos3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(JTextAlamatKodePos1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(JTextAlamatKodePos2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addGap(4, 4, 4))
                        .addComponent(dateChooserComboPelamar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(dateChooserComboPelamar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(2, 2, 2))
                        .addComponent(jButtonPendidikan, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
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
            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 78, Short.MAX_VALUE))
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
            .addGap(5, 5, 5)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextAlamatKodePos5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16))
                    .addComponent(dateChooserComboPelamar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(1, 1, 1)
                    .addComponent(JButtonNonFormal)))
            .addGap(5, 5, 5))
    );

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGap(30, 30, 30))
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
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jTabbedPane1.addTab("Riwayat Pendidikan Pelamar", jPanel1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, 0)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE))))
    );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonNonFormal;
    public javax.swing.JButton JButtonPelamarEdit;
    public javax.swing.JButton JButtonPelamarNew;
    public javax.swing.JButton JButtonPelamarPrintForm;
    public javax.swing.JButton JButtonPelamarSave;
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
    private javax.swing.JTextField JTextEmail;
    private javax.swing.JTextField JTextFieldSearch;
    private javax.swing.JTextField JTextKTP;
    private javax.swing.JTextField JTextKelahiran;
    private javax.swing.JTextField JTextKewarganegaraan;
    private javax.swing.JTextField JTextNama;
    private javax.swing.JTextField JTextSIM;
    private javax.swing.JTable JTextTabelPelamar;
    private javax.swing.JLabel LabelPeringantan;
    private javax.swing.JRadioButton StatusDiterima;
    private javax.swing.JRadioButton StatusDitolak;
    private javax.swing.JLabel StatusLabel;
    private javax.swing.JRadioButton StatusMenunggu;
    private datechooser.beans.DateChooserCombo dateChooserComboPelamar;
    private datechooser.beans.DateChooserCombo dateChooserComboPelamar1;
    private datechooser.beans.DateChooserCombo dateChooserComboPelamar2;
    private datechooser.beans.DateChooserCombo dateChooserComboPelamar3;
    private javax.swing.JButton jButtonPendidikan;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JComboBox jComboBoxStatusKelamin;
    private javax.swing.JComboBox jComboBoxStatusMenikah;
    private javax.swing.JComboBox jComboBoxStatusPelamar;
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
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton sas;
    // End of variables declaration//GEN-END:variables
}
