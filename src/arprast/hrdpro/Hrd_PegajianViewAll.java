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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.JOptionPane;
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
public class Hrd_PegajianViewAll extends javax.swing.JDialog {

    /**
     * Creates new form Hrd_PegajianViewAll
     */
    private DefaultTableModel   Modeltabel   = new DefaultTableModel();
    private TableCellRenderer   tengah= new RenderingTengah();
    private TableCellRenderer   kanan = new RenderingKanan();
    private SatuUntukSemua      Satu = new SatuUntukSemua();
    private Sistem.DB_MYSQL KD = new Sistem.DB_MYSQL();
    private Connection K = KD.createConnection();
    
    public Hrd_PegajianViewAll(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        Tabel();
        JButtonrPrintForm.addActionListener(new ActionListener (){

            @Override
            public void actionPerformed(ActionEvent e) {
                PrintOut();
            }
        });
        
    }
    
     private void Tabel(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No", "Perincian Gaji","Nominal"};
        Modeltabel = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        //if(colIndex == 4 ) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        jTable1.setModel(Modeltabel);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
        jTable1.setRowSorter(sorter);
        
        /*
         * Rata tengah atau kanan table
         */
        jTable1.getColumnModel().getColumn(0).setCellRenderer( tengah );
        //jTable1.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        jTable1.getColumnModel().getColumn(2).setCellRenderer( kanan );
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,200,100 };
        Sistem.Colom_table ukuran_colom = new Sistem.Colom_table();
        ukuran_colom.ukuran_colom(jTable1, jarak_colom);

        /*
         * Disable drag colum tabel
         */       
        jTable1.getTableHeader().setReorderingAllowed(false); 
        
    }
     
     String Sakit, Izin, Alpha, Cuti, Telat;
     private void AmbilDataABsensi(String NIK, String Period){
          int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               String Query = null;             
                   Query = "SELECT absen, izin_cuti, sakit from header_absensi where nik = '"+ NIK+ "' and periode = '"+ Period+"'";

               HQ = stm.executeQuery(Query);              
               baris = HQ.getRow();

               while(HQ.next()  ){
                   Sakit      = HQ.getString("sakit");
                   Izin       = HQ.getString("izin_cuti");
                   Alpha      = HQ.getString("absen");

              }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           } 
     }

     int Lembur = 0, Bonus = 0, gaji_pokok = 0;
     int t_jabatan = 0, t_makan = 0, t_transport = 0, t_jamsostek = 0, t_kesehatan = 0, t_lainnya = 0;
     int p_pajak = 0, p_pinjaman_karywan = 0, p_pinjaman_lainnya = 10, p_jamsostek = 0, p_asr = 0, p_denda_kedisipliman = 0;
     int tot_tunjangan, tot_potongan, gaji_bersih, gaji_bruto;
     
      public void AmbilDariDatabase(String NIK, String Periode, String Data[]){
        AmbilDataABsensi(NIK, Periode);
        Nama.setText(Data[0]);
        Nik.setText(Data[1]);
        Jabatan.setText(Data[2]);
        
        Satu.HapusDataJTabel(jTable1);
        
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
               Query  = "SELECT  nik, nama, jk as jenis_kelamain, jabatan, "
                           + "gaji_pokok, lembur, bonus,t_jabatan, t_makan,"
                           + " t_transport,t_jamsostek, t_kesehatan, t_lainnya,"
                           + " pajak, pinjaman_karyawan, pinjaman_lainnya, pot_jamsostek, "
                           + "asr_kesehatan, denda_kedisipliman, periode "
                           + " from header_gaji where periode = '"+ Periode+ "' and Nik = '"+ NIK+ "'";
 
               HQ = stm.executeQuery(Query);              
               while(HQ.next()  ){
                    int No   = HQ.getRow();
                    gaji_pokok   = HQ.getInt("gaji_pokok");
                    String[] add         = {1 + "", "<html><font size='4' color='red'><strong>Gaji Pokok</stong></font></html>","<html><font size='4'><strong>"+ gaji_pokok +"</stong></font></html>"};
                    Modeltabel.addRow(add); 
                    
                    Lembur       = HQ.getInt("lembur"); 
                    
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
                    
                    tot_tunjangan    = t_jabatan + t_makan + t_transport + t_jamsostek + t_kesehatan + t_lainnya;

                    String[] add2         = {2 + "", "<html><font size='4'><strong>Tunjangan</stong></font></html>", ""};
                    Modeltabel.addRow(add2); 
                    
                    String[] add3         = {3 + "", "    Jabatan", t_jabatan+""};
                    Modeltabel.addRow(add3); 
                    
                    String[] add4         = {4 + "", "    Makan", t_makan+""};
                    Modeltabel.addRow(add4); 
                    
                    String[] add5         = {5 + "", "    Transport",t_transport+ ""};
                    Modeltabel.addRow(add5); 
                    
                    String[] add6         = {6+ "", "    Jamsostek", t_jamsostek+ ""};
                    Modeltabel.addRow(add6); 
                    
                    String[] add7         = {7 + "", "    Kesehatan",t_kesehatan+ ""};
                    Modeltabel.addRow(add7); 
                    
                    String[] add8         = {8 + "", "    Lainnya", t_lainnya+ ""};
                    Modeltabel.addRow(add8); 
                    
                    String[] add9         = {9 + "", "<html><font size='4' color='red'><strong>Jumlah Tunjangan</stong></font></html>", "<html><font size='4'><strong>"+ tot_tunjangan +"</stong></font></html>"};
                    Modeltabel.addRow(add9); 
                    
                    String[] add10         = {10 + "", "<html><font size='4' ><strong>Penerimaan</stong></font></html>", ""};
                    Modeltabel.addRow(add10); 
                    
                    String[] add11         = {11 + "", "    Jumlah Lembur", Lembur + ""};
                    Modeltabel.addRow(add11); 
                    
                    String[] add12         = {12 + "", "    Bonus / Insentif",Bonus +  ""};
                    Modeltabel.addRow(add12); 
                    
                    gaji_bruto = gaji_pokok + Lembur + Bonus + tot_tunjangan ;
                            
                    String[] add13         = {13 + "", "<html><font size='4' color ='red'><strong>Penerimaan Bruto</stong></font></html>","<html><font size='4'><strong>"+ gaji_bruto +"</stong></font></html>"};
                    Modeltabel.addRow(add13); 
                    
                    tot_potongan     = p_pajak + p_pinjaman_karywan + p_pinjaman_lainnya + p_jamsostek + p_asr + p_denda_kedisipliman;
                    gaji_bruto       = gaji_bruto - tot_potongan;
                    
                    //System.out.println(p_pajak + "  " + p_pinjaman_karywan + "  " + p_pinjaman_lainnya + "  " +p_jamsostek + "  " + p_asr + "  " + p_denda_kedisipliman + " coba ");
                    //System.out.println(gaji_pokok + "  " + tot_tunjangan + "  " + Lembur + "  " +Bonus + "  " + tot_potongan + "  " + gaji_bersih + " " + gaji_bruto);
                    
                    String[] add14         = {14 + "", "<html><font size='4'><strong>Potongan</stong></font></html>", ""};
                    Modeltabel.addRow(add14); 
                    
                    String[] add15         = {15 + "", "   Pajak", p_pajak +""};
                    Modeltabel.addRow(add15); 
                    
                    String[] add16         = {16 + "", "   Pinjaman Karyawan", p_pinjaman_karywan + ""};
                    Modeltabel.addRow(add16); 
                    
                    String[] add1166         = {17 + "", "   Pinjaman Lainnya", p_pinjaman_lainnya + ""};
                    Modeltabel.addRow(add1166); 
                    
                    String[] add17         = {18+ "", "   Jamsostek", p_jamsostek + ""};
                    Modeltabel.addRow(add17); 
                    
                    String[] add18         = {19 + "", "   Asr Kesehatan",p_asr + ""};
                    Modeltabel.addRow(add18); 
                    
                    String[] add19         = {20 + "", "   Denda Kedisipliman", p_denda_kedisipliman+ ""};
                    Modeltabel.addRow(add19); 
                    
                    String[] add20         = {21 + "", "<html><font size='4' color='red'><strong>Jumlah Potongan</stong></font></html>", "<html><font size='4'><strong>"+ tot_potongan+"</stong></font></html>"};
                    Modeltabel.addRow(add20); 
                    
                    String[] add21         = {22 + "", "<html><font size='4' color='red'><strong>Penerimaan Bersih</stong></font></html>", "<html><font size='4'><strong>"+ gaji_bruto +"</stong></font></html> "};
                    Modeltabel.addRow(add21);

               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
      }
      private void PrintOut(){
          String Semua;
          int jumlahRow = jTable1.getRowCount();
        for (int a = 0 ; a < jumlahRow ; a++){
             Semua = (String )jTable1.getValueAt(a, 2);
             if (Semua.contains("<html>")){
                 Semua = Semua.replaceAll("<html><font size='4' color='red'><strong>", "");
                 Semua = Semua.replaceAll("</stong></font></html>", "");
                 Semua = Semua.replaceAll("<html><font size='4'>", "");
                 Semua = Semua.replaceAll("<strong>", "");
                 
                         jTable1.setValueAt(Semua, a, 2);
             }
             /*
             Semua = (String )JTabelLabaRugi.getValueAt(a, 4);
             if (Semua.contains("0") || Semua.equals("")){
                 Semua = Semua.replaceAll("0", "");
                         JTabelLabaRugi.setValueAt(Semua, a, 4);
             }
             */
             Semua = (String )jTable1.getValueAt(a, 2);
             if (Semua == null ){
                         jTable1.setValueAt(" ", a, 2);
             }
        }
        
        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();
        hashMap.put("nama", Nama.getText());
        hashMap.put("jabatan", Jabatan.getText());
        hashMap.put("nik", Nik.getText());
        
        hashMap.put("gaji_pokok", jTable1.getValueAt(0, 2));
        hashMap.put("jabatan", jTable1.getValueAt(2, 2));
        hashMap.put("makanan", jTable1.getValueAt(3, 2));
        hashMap.put("transport", jTable1.getValueAt(4, 2));
        hashMap.put("jamsostek", jTable1.getValueAt(5, 2));
        hashMap.put("kesehatan", jTable1.getValueAt(6, 2));
        hashMap.put("lainnya", jTable1.getValueAt(7, 2));
        hashMap.put("jumlah_tunjangan", jTable1.getValueAt(8, 2));
        int tot = gaji_pokok +tot_tunjangan;
        hashMap.put("penerimaan", tot +"");
        
        hashMap.put("lemburan", jTable1.getValueAt(10, 2));
        hashMap.put("bonus",jTable1.getValueAt(11, 2));
        hashMap.put("penerimaan_bruto", jTable1.getValueAt(12, 2));
        hashMap.put("pajak", jTable1.getValueAt(14, 2));
        hashMap.put("pinjaman_karyawan", jTable1.getValueAt(15, 2));
        hashMap.put("pinjaman_lainnya", jTable1.getValueAt(16, 2));
        hashMap.put("pot_jamsostek", jTable1.getValueAt(17, 2));
        hashMap.put("asr_kesehatan", jTable1.getValueAt(18, 2));
        hashMap.put("denda_kedisiplinan", jTable1.getValueAt(19, 2));
        hashMap.put("jumlah_potongan", jTable1.getValueAt(20, 2));
        hashMap.put("penerimaan_bersih", jTable1.getValueAt(21, 2));
        hashMap.put("jumlah_dibayarkan", jTable1.getValueAt(21, 2));
        hashMap.put("sakit", Sakit);
        hashMap.put("alpha", Alpha);
        hashMap.put("izin", Izin);
        //System.out.print(jTable1.getValueAt(22, 2));

        try {
            String x    = System.getProperty("user.dir")+"\\PrintOut\\SlipGaji.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap,  new    JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }
        this.setVisible(false);
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
        jLabel1 = new javax.swing.JLabel();
        JButtonrPrintForm = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Nama = new javax.swing.JLabel();
        Nik = new javax.swing.JLabel();
        Jabatan = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("View Detail Gaji Karyawan ");

        JButtonrPrintForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/print_icon2.png"))); // NOI18N
        JButtonrPrintForm.setText("Print Slip");

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel2.setText("Nama");

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel3.setText("Nik");

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel4.setText("Jabatan");

        Nama.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N

        Nik.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N

        Jabatan.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel5.setText(":");

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel6.setText(":");

        jLabel7.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel7.setText(":");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(JButtonrPrintForm))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Nama, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                            .addComponent(Nik, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Jabatan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(Nama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(Nik, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(Jabatan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonrPrintForm)
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-361)/2, (screenSize.height-578)/2, 361, 578);
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
            java.util.logging.Logger.getLogger(Hrd_PegajianViewAll.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Hrd_PegajianViewAll.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Hrd_PegajianViewAll.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Hrd_PegajianViewAll.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Hrd_PegajianViewAll dialog = new Hrd_PegajianViewAll(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton JButtonrPrintForm;
    private javax.swing.JLabel Jabatan;
    private javax.swing.JLabel Nama;
    private javax.swing.JLabel Nik;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
