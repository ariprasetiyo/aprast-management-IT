/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arprast.hrdpro;

import Sistem.ButtonJTable;
import Sistem.ButtonJTableKeDuaLocal;
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
import java.util.regex.PatternSyntaxException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
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
public class Hrd_DataKaryawan extends javax.swing.JPanel {

    /**
     * Creates new form Hrd_DataKaryawan
     */
    private SatuUntukSemua      Satu = new SatuUntukSemua();
    private DefaultTableModel   Modeltabel2             = new DefaultTableModel();
    private Sistem.DB_MYSQL KD = new Sistem.DB_MYSQL();
    private Connection K = KD.createConnection();
    private TableCellRenderer   kanan = new RenderingKanan();
    private TableCellRenderer   tengah= new RenderingTengah();
    private boolean BolehEdit = false; 

    
    public Hrd_DataKaryawan() {
        initComponents();
        Tampilan();
        Tabel1();
        Aksi();
        AmbilDataDariDatabase("","",0);
    }
    
    private void Aksi(){
        JButtonPegawaiPrintForm1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                PrintJasper();
            }
        });
        jButtonRefresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent A){
                AmbilDataDariDatabase((String )BoxJabatan.getSelectedItem(), (String )BoxCabang.getSelectedItem(), BoxStatusPekerjaan.getSelectedIndex());
            }
        });
    }
    
    private void PrintJasper(){
        DefaultTableModel de = (DefaultTableModel)JTableKaryawan.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();

        //hashMap.put("Judul", JLabelPemberitahuanPb.getText());
        
        try {
            String x    = System.getProperty("user.dir")+"\\PrintOut\\DataPegawai.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
    }
    
    private void  Tampilan(){
        BoxCabang.removeAllItems();
        String[] Data = {"","Kantor Kas","Kantor Cabang Pembantu (KCP)"};
        for (int a = 0 ; a < Data.length ; a++){
            BoxCabang.addItem(Data[a]);
        }
        BoxJabatan.removeAllItems();
        BoxJabatan.addItem("");
        BoxJabatan.addItem("Direktur");
        BoxJabatan.addItem("Marketing");
        BoxJabatan.addItem("Umum");
        BoxJabatan.addItem("Teller");
                
    }
    
     private void Tabel1(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No", "No Fomulir","NIK ", "Nama ","Alamat", 
            "Jenis Kelamin","Agama","status","Kewarganegaraan","No Ktp",
            "No Hp", "No Telphone","Email","Jabatan", "Cabang",
            "Cabang Lokasi","Cuti","Gaji Pokok","Periode Masuk ", "Action"};
        Modeltabel2 = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 19) {return true ;} 
                        return BolehEdit;   //Disallow the editing of any cell
                 }
        };
        JTableKaryawan.setModel(Modeltabel2);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel2);
        JTableKaryawan.setRowSorter(sorter);
        JTextFieldSearch1.addKeyListener(new KeyListener(){
            @Override
            public void keyReleased(KeyEvent e) {
               String text = JTextFieldSearch1.getText();
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
        for (int a  = 0 ; a < 20 ; a++ ){
            JTableKaryawan.getColumnModel().getColumn(a).setCellRenderer( tengah );
        }
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,100,200,150,200,
            100,100, 120,100, 100,
        100,100, 120,100, 100,
        100,100, 120,100, 100};
        Sistem.Colom_table ukuran_colom = new Sistem.Colom_table();
        ukuran_colom.ukuran_colom(JTableKaryawan, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTableKaryawan.setName("karyawan");
        JTableKaryawan.getColumnModel().getColumn(19).setCellRenderer(  new ButtonJTable("Edit"));
        JTableKaryawan.getColumnModel().getColumn(19).setCellEditor( new  ButtonJTableKeDuaLocal(new JCheckBox(),Modeltabel2, JTableKaryawan ));
        
        
        /*
         * Disable drag colum tabel
         */       
        JTableKaryawan.getTableHeader().setReorderingAllowed(false); 
        
    }
     
     private void AmbilDataDariDatabase(String Jabatan , String Cabang,int StsPeg){
        Satu.HapusDataJTabel(JTableKaryawan);
        
         
        /*
         * Isi data ke Tabel dari database
         * {"No", "No Fomulir","NIK ", "Nama ","Alamat", 
            "Jenis Kelamin","Agama","status","Kewarganegaraan","No Ktp",
            "No Hp", "No Telphone","Email","Jabatan", "Cabang",
            "Cabang Lokasi","Cuti","Gaji Pokok","Periode Masuk ", "Action"};
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               String Query = null;

               if (StsPeg <= 1){
                   Query = "SELECT  hp.no_pelamar, hs.nama, hs.jenis_kelamain, hs.agama, hs.no_hp,"
                           + " hs.no_telphone , hs.email,  hp.nik, hp.cabang,"
                           + " hp.cabang_lokasi, hp.jabatan, hp.cuti, hp.gaji_pokok, hp.periode,"
                           + " hs.kewarganegaraan, hs.no_ktp, hs.status_nikah, hs.alamat "
                           + "from header_pegawai as hp inner join header_pelamar as hs "
                           + "on hs.trans_no = hp.no_pelamar where hp.jabatan "
                           + "like '%"+Jabatan +"%' and hp.cabang like '%"+Cabang +"%' and status_pekerja ='"+StsPeg+"'  order by hs.nama "; 
               }
               else {
                   Query = "SELECT  hp.no_pelamar, hs.nama, hs.jenis_kelamain, hs.agama, hs.no_hp,"
                           + " hs.no_telphone , hs.email,  hp.nik, hp.cabang,"
                           + " hp.cabang_lokasi, hp.jabatan, hp.cuti, hp.gaji_pokok, hp.periode,"
                           + " hs.kewarganegaraan, hs.no_ktp, hs.status_nikah, hs.alamat "
                           + "from header_pegawai as hp inner join header_pelamar as hs "
                           + "on hs.trans_no = hp.no_pelamar where hp.jabatan like '%"+Jabatan +"%' and hp.cabang like '%"+Cabang +"%'  order by hs.nama "; 
               }

               
               HQ = stm.executeQuery(Query);              
               baris = HQ.getRow();

               while(HQ.next()  ){
                   String NoFormulir    = HQ.getString("no_pelamar");
                   String Nama          = HQ.getString("nama");
                   String Period        = HQ.getString("periode");
                   int JnsKelamin       = HQ.getInt("jenis_kelamain");;
                   String Agama         = HQ.getString("agama");
                   String NoHp          = HQ.getString("no_hp");
                   String NoTlpn        = HQ.getString("no_telphone");
                   String Email         = HQ.getString("email");
                   String cuti          = HQ.getString("cuti");
                   String gaji          = HQ.getString("gaji_pokok");
                   String jabatan         = HQ.getString("jabatan");
                   String cabang_lokasi         = HQ.getString("cabang_lokasi");
                   String cabang         = HQ.getString("cabang");
                   String nik         = HQ.getString("nik");
                   int status         = HQ.getInt("status_nikah");
                   String no_ktp         = HQ.getString("no_ktp");
                   String kewarganegaraan         = HQ.getString("kewarganegaraan");
                   String alamat         = HQ.getString("alamat");
                   String gaji_pokok    = HQ.getString("gaji_pokok");
                   
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
                   
                   String StatusNikah = null;
                   switch (status){
                       case 0 :
                           StatusNikah = "Menikah";
                       break;
                       case 1 :
                           StatusNikah = "Lajang";
                       break;
                       default :
                           JOptionPane.showMessageDialog(null, "Status Nikah Error");
                       break;
                   }
                   
                   String[] add         = {String.valueOf(HQ.getRow()).toString(), NoFormulir ,nik, Nama, alamat,  JenisKelamin,Agama,
                       StatusNikah, kewarganegaraan, no_ktp, NoHp, NoTlpn, Email,jabatan, cabang, cabang_lokasi,cuti, gaji_pokok, Period };
                   Modeltabel2.addRow(add);    
                   //ResetDataPelamar();
                   
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
           
        JTableKaryawan.setModel(Modeltabel2);      
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
        JTableKaryawan = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        JTextFieldSearch1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        BoxJabatan = new javax.swing.JComboBox();
        Cabang = new javax.swing.JLabel();
        BoxCabang = new javax.swing.JComboBox();
        jButtonRefresh = new javax.swing.JButton();
        Cabang1 = new javax.swing.JLabel();
        BoxStatusPekerjaan = new javax.swing.JComboBox();
        JButtonPegawaiPrintForm1 = new javax.swing.JButton();

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

        JTableKaryawan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(JTableKaryawan);

        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setText("Search  :");

        jLabel7.setText("Jabatan");

        BoxJabatan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Diterima", "Ditolak", "Menunggu" }));

        Cabang.setText("Cabang");

        BoxCabang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Diterima", "Ditolak", "Menunggu" }));

        jButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Refresh.png"))); // NOI18N
        jButtonRefresh.setText("Refresh");

        Cabang1.setText("Status Pegawai");

        BoxStatusPekerjaan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aktiv", "Resign", "" }));

        JButtonPegawaiPrintForm1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/print_icon2.png"))); // NOI18N
        JButtonPegawaiPrintForm1.setText("Print");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BoxJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Cabang, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BoxCabang, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Cabang1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BoxStatusPekerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonRefresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPegawaiPrintForm1)
                .addGap(95, 95, 95))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(JTextFieldSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(BoxJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Cabang)
                        .addComponent(BoxCabang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Cabang1)
                        .addComponent(BoxStatusPekerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JButtonPegawaiPrintForm1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 1029, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox BoxCabang;
    private javax.swing.JComboBox BoxJabatan;
    private javax.swing.JComboBox BoxStatusPekerjaan;
    private javax.swing.JLabel Cabang;
    private javax.swing.JLabel Cabang1;
    private javax.swing.JButton JButtonPegawaiPrintForm1;
    private javax.swing.JTable JTableKaryawan;
    private javax.swing.JTextField JTextFieldSearch1;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
