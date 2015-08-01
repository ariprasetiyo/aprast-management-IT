/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arprast.all;

import Sistem.ButtonJTable;
import Sistem.RenderingKanan;
import Sistem.RenderingTengah;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author arprast
 */
public class Master_Barang extends javax.swing.JPanel {

    /**
     * Creates new form MasterBarangSatuanConvert
     */
    private final TableCellRenderer kanan = new RenderingKanan();
    private final TableCellRenderer tengah= new RenderingTengah();
    private DefaultTableModel   Modeltabel  = new DefaultTableModel();
    private final Sistem.DB_MYSQL KD = new Sistem.DB_MYSQL();
    private final Connection K = KD.createConnection();
    private int DataNoTerakhirTabel;
    static int tableWidth = 0; // set the tableWidth
    static int tableHeight = 0; // set the tableHeight
    static JFileChooser jChooser;
    Sistem.KomaToString Convert = new Sistem.KomaToString();
    Sistem.ComponentHanyaAngka HanyaAngka    = new Sistem.ComponentHanyaAngka ();
    boolean Berhenti = true;

    
    private String AA,BB,CC,DD, EE, FF, GG;
    private int _intAA, _intBB, _intCC, _intDD;
    boolean Edit = false, Edit2, ADD_Boleh = false, editTable = false, BolehSave = false ;
    Sistem.DB_SQLite sql_lite = new Sistem.DB_SQLite();
    private String UserName;
    int JumlahRow, NilaiMax, NilaiAwal, BerapaKali, NilaiKekurangan, Hasil, Add, Add2;
    
    /*
        Membuat combobox pada jtable
    */
    DataComboTabel_Consigment Consigment[] = { 
    new DataComboTabel_Consigment("Yes"), new DataComboTabel_Consigment("No")};
    int counter = 0, minValue = 0, maxValue =100; 
    
    DataComboTabel Status[] = { 
    new DataComboTabel("Aktiv"), new DataComboTabel("Non Aktiv") };
        
    DataComboTabel_Lisensi Jenis[] = { 
    new DataComboTabel_Lisensi("Barang"), new DataComboTabel_Lisensi("Lisensi"),
    new DataComboTabel_Lisensi("Jasa")};

    DataComboTabel_Unit Unit[] = { 
    new DataComboTabel_Unit("PCS"), new DataComboTabel_Unit("Buah"),
    new DataComboTabel_Unit("Meter")};
    
    JComboBox _Combo_Consigment = new JComboBox(Consigment);
    JComboBox _Combo_Status = new JComboBox(Status);
    JComboBox _Combo_Unit = new JComboBox(Unit);
    JComboBox _Combo_Jenis = new JComboBox(Jenis);
    
    String Query;
    
    /*
    End
    */
    
    public Master_Barang(String User) {
        initComponents();
        this.UserName = User;
        Aksi ();
        Tabel(null,null);
    }
    
    /*
    add, save, import
    */
    private void Button(boolean Data[]){
        JButtonAdd.setEnabled(Data[0]);
        JButtonSave.setEnabled(Data[1]);
        JButtonImportXls.setEnabled(Data[2]);
        JButtonEdit.setEnabled(Data[3]);
    }
    
    public void Aksi (){
        JButtonFilter.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e) {
               
                 AmbilDariDatabase();
            }
        });

         _Combo_Consigment.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Di COba");
            }
        });
        JButtonEdit.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e) {
                boolean Data[] = {true, true, true, true};
                Button(Data);
                Edit =true;
                editTable = true;
                JButtonEditCancel.setEnabled(true);
                
            }
        });
        
        JButtonSave.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e) {
                if (JButtonEdit.isEnabled() == true && BolehSave == true) {
                    JTextFieldSearch1.setText("");
                    try {
                        Thread.sleep(1000);
                    } 
                    catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                   
                    SearchTable(jTable1,Modeltabel);
                    //SaveDatabase();
                    
                    try {
                        Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    } catch (SQLException ex) {
                        Logger.getLogger(Master_Barang.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    InsertDatabase(Stm);
                    
                }
                else {
                    JOptionPane.showMessageDialog(null, "Tekan tombol edit terlebih dahulu atau \n Anda tidak tekan tombol add/import xls\nProgram mengangap tidak ada perubahan data sama sekali", AA, WIDTH, null);
                }
                editTable = false;
                BolehSave = false;
            }
        });
        JButtonAdd.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e){
                AddItemHarga();
                ADD_Boleh = true;
                BolehSave = true;
            }
        });
        JButtonEditCancel.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e){
                Edit =false;
                editTable = false;
                editTable = false;
                BolehSave = false;
                JButtonEditCancel.setEnabled(false);
                /*   add, save, import, edit */
                boolean Data[] = {false, false, false, true};
                Button(Data);
            }
        });
       
        
        /*
        Menyipan pada temp
        */
        final Sistem.Sys_Temp Temp = new Sistem.Sys_Temp();
        jChooser = new JFileChooser(Temp.AmbilTemp("temp_import_xls","temp"));
        
        /*
         * Action untuk import ke xls
         */
        JButtonImportXls.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Edit= true;
                ADD_Boleh = false;
                BolehSave = true;
                jChooser.showOpenDialog(null);
    
                File file = jChooser.getSelectedFile();

                if(!file.getName().endsWith("xls")){
                    JOptionPane.showMessageDialog(null, 
                      "Please select only Excel file.",
                      "Error",JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    jTable1.removeAll();
                    HapusDataJTabel(jTable1);
                    CekXlsDenganMasterBarang();
                    fillData(file);
                    Tabel(headers,data );
                    JButtonSave.setEnabled(true);
                    //JButtonEdit.setEnabled(false);
                    /*
                    Modeltabel = new DefaultTableModel(data, 
                      headers);
                    tableWidth = Modeltabel.getColumnCount() 
                      * 150;
                    tableHeight = Modeltabel.getRowCount() 
                      * 25;
                    JTabelBarang.setPreferredSize(new Dimension(
                      tableWidth, tableHeight));

                    JTabelBarang.setModel(Modeltabel);
                    */
                }
                
                /*
                Menyipan pada temp
                */
                 Temp.TempFile(jChooser.getSelectedFile().toString(),"temp");
            }
        });
    }
    
    private void AddItemHarga(){
        JTextField Nama_Item       = new JTextField();
        JTextField Item_Code       = new JTextField();
        JComboBox IsStock          = new JComboBox();
        JComboBox Status           = new JComboBox();
        JComboBox Consignment      = new JComboBox();
        JComboBox Unit             = new JComboBox();
        
        Status.addItem("Non Aktiv");
        Status.addItem("Aktiv");
        IsStock.addItem("Jasa");
        IsStock.addItem("Barang");
        IsStock.addItem("Lisensi");
        Consignment.addItem("No");
        Consignment.addItem("Yes");
        Unit.addItem("PCS");
        Unit.addItem("Buah");
        Unit.addItem("Meter");

        Object[] Object ={
          "Nama Item ", Nama_Item,
          "Item Code ", Item_Code,
          "Jenis ", IsStock,
          "Status ", Status,
          "Consignment ", Consignment,
          "Unit ", Unit
        };
        
        int Pilih = JOptionPane.showConfirmDialog(null , Object , "Input Data Convert", JOptionPane.OK_CANCEL_OPTION);
        if (Pilih == JOptionPane.OK_OPTION){
            Object obj[] = new Object[10];        
            obj[0] = DataNoTerakhirTabel;
            DataNoTerakhirTabel = DataNoTerakhirTabel + 1;
            obj[1] = Item_Code.getText();
            obj[2] = Nama_Item.getText();
            obj[3] = Status.getSelectedItem();
            obj[4] = IsStock.getSelectedItem();
            obj[5] = Unit.getSelectedItem();
            obj[6] = Consignment.getSelectedItem();
            
            obj[1] = obj[1].toString().replaceAll(".*--\\s", "");
            Add = jTable1.getRowCount();
            Object[] data = {obj[1],obj[2],obj[3],obj[4],obj[5], obj[6]};
            if (ValidasiInputDataConvert(data)){
                 Modeltabel.addRow(obj);
                 Edit = true;
                 ADD_Boleh = true;
            } 
        }
    }
    
    boolean ValidasiInputDataConvert(Object[] Data){
        for (int a = 0; a < Data.length; a++){
            if (Data[a] == null){
                Data[a] = "";
            }
            if (Data[a].toString().equalsIgnoreCase("")){
                JOptionPane.showMessageDialog(null, "Data ada yang kosong");
                return false;
            }          
        }
        return true;
    }
    
    /*
     *  Mengisi data xls ke JTabel
        Format xls
        "item code","item name", "status","unit","consigment"
    */
    static Vector headers = new Vector();
    static Vector data = new Vector();
    void fillData(File file) {
        
        Workbook workbook = null;
        try {
            try {
             workbook = Workbook.getWorkbook(file);
            } 
            catch (IOException ex) {
                Logger.getLogger(Master_Barang.class.getName()).log(Level.SEVERE, null, ex);
         }
         Sheet sheet = workbook.getSheet(0);

         headers.clear();
         for (int i = 0; i < sheet.getColumns(); i++) {
            Cell cell1 = sheet.getCell(i, 0);
            headers.add(cell1.getContents());
         }
         
         /*
         j = row
         i = column
         */
         data.clear();
         for (int j = 1; j < sheet.getRows(); j++) {
            Vector d = new Vector();
            for (int i = 0; i < sheet.getColumns(); i++) {

                /*
                 * Membuat no urut
                 */
                Cell cell = sheet.getCell(i, j);
                if (i == 0 ){
                    d.add(j);
                }
                
                /*
                 * Cek Apakah ada item name pada master barang atau tidak
                 *
                boolean Cek = true;
                if (i == 0 ){
                    Object[] ol = list.toArray();
                    for (int a = 0; a <= ol.length - 1; a++){
                       if (ol[a].toString().contains(cell.getContents())){
                           AA = cell.getContents();
                           Cek = true;
                           Berhenti = false;
                           break;
                       }
                       Cek = false;                       
                    }
                    
                    if (Cek == false){
                        JOptionPane.showMessageDialog(null, "Nama Item xls tidak ada di Master Barang : " + cell.getContents());
                        data.clear();
                        headers.clear();
                        Berhenti = false;
                        break;
                     }                   
                }
                
                /*
                    cek Status
                */
                if( i == 2){
                    AA = cell.getContents().toString().toUpperCase();
                    String[] DataPembanding = { "Aktiv",
                                                "Non Aktiv",
                                                };
                    
                    for (int a = 0; a < DataPembanding.length; a++){
                        if (AA.equalsIgnoreCase(DataPembanding[a]))
                        {
                            Berhenti = true;   
                            break;
                        }
                        Berhenti = false;
                    }
                    if (Berhenti != true){
                        JOptionPane.showMessageDialog(null, "Status  salah : " + AA);
                        Berhenti = false;
                    }
                }
                
                
                /*
                    cek Jenis
                */
                if( i == 3){
                    AA = cell.getContents().toString().toUpperCase();
                    String[] DataPembanding = { "Jasa",
                                                "Barang",
                                                "Lisensi",
                                                "-"
                                                };
                    
                    for (int a = 0; a < DataPembanding.length; a++){
                        if (AA.equalsIgnoreCase(DataPembanding[a]))
                        {
                            Berhenti = true;   
                            break;
                        }
                        Berhenti = false;
                    }
                    if (Berhenti != true){
                        JOptionPane.showMessageDialog(null, "Jenis salah : " + AA);
                        Berhenti = false;
                    }
                }
                
                /*
                 * Cek apakah unit sudah seuai atau tidak
                 */               
                if( i == 4){
                    AA = cell.getContents().toString().toUpperCase();
                    
                    String[] DataPembanding = { "Buah",
                                                "PCS",
                                                "Meter"
                                                };
                    
                    for (int a = 0; a < DataPembanding.length; a++){
                        if (AA.equalsIgnoreCase(DataPembanding[a]))
                        {
                            Berhenti = true;   
                            break;
                        }
                        Berhenti = false;
                    }
                    if (Berhenti != true){
                        JOptionPane.showMessageDialog(null, "Unit salah : " + AA);
                        Berhenti = false;
                    }
                }
                
                /*
                    cek congsingment
                */
                if( i == 5){
                    AA = cell.getContents().toString().toUpperCase();
                    
                    String[] DataPembanding = { "yes",
                                                "no"
                                                };
                    
                    for (int a = 0; a < DataPembanding.length; a++){
                        if (AA.equalsIgnoreCase(DataPembanding[a]))
                        {
                            Berhenti = true;   
                            break;
                        }
                        Berhenti = false;
                    }
                    if (Berhenti != true){
                        JOptionPane.showMessageDialog(null, "Status consigment salah : " + AA);
                        Berhenti = false;
                    }
                }
                
                /*
                 * Cek apakah Ada colum yang kosong
                 */              
                if (cell.getContents().equalsIgnoreCase("")){
                    JOptionPane.showMessageDialog(null, "Data column xls ada yang kosong : " + AA);
                    data.clear();
                    headers.clear();
                    Berhenti = false;
                    break;
                }
                
                /*
                 * Cek satuan Benar atau tidak
                 */
                d.add(cell.getContents().toUpperCase()); 
            }
            
            /*
             * Berhenti
             */
            if (Berhenti == false){
                data.clear();
                headers.clear();
                break;
            }
            d.add("\n");
            data.add(d);
            }
        } 
        catch (BiffException e) {
            e.printStackTrace();
        }
 }
    
    List list = new ArrayList();
    private void CekXlsDenganMasterBarang(){
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT item_name from master_barang ");              
               while(HQ.next()  ){
                   AA       = HQ.getString("item_name");
                   list.add(AA);
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
    }
    
    /*
        digunakan untuk jcombobox pada jtable
    */
    public class DataComboTabel {
        String cname;
        public DataComboTabel(String name) {
          cname = name;
        }
        public String toString() {
          return cname;
        }
    }
    
    public class DataComboTabel_Lisensi {
        String cname;
        public DataComboTabel_Lisensi(String name) {
          cname = name;
        }
        public String toString() {
          return cname;
        }
    }
    
    public class DataComboTabel_Unit {
        String cname;
        public DataComboTabel_Unit(String name) {
          cname = name;
        }
        public String toString() {
          return cname;
        }
    }
    
    public class DataComboTabel_Consigment {
        String cname;
        public DataComboTabel_Consigment(String name) {
          cname = name;
        }
        public String toString() {
          return cname;
        }
    }
    
    private void Tabel(Vector Header, Vector Data){ 

        String header[] = {"No", "item code","item name", "status","Jenis","unit","consigment", "created date", "Action"}; 
        /*
         * Jika import null
         */     
        if (Header == null){
            Modeltabel = new DefaultTableModel(null,header) {
                @Override
                public boolean isCellEditable(int rowIndex, int colIndex) {
                                    //if(colIndex == 1) {return false ;} //  nilai false agar tidak bisa di edit
                                    if(colIndex == 2) {return Edit ;}
                                    if(colIndex == 3) {return Edit ;}
                                    if(colIndex == 4) {return Edit ;}
                                    if(colIndex == 5) {return Edit ;}
                                    if(colIndex == 6) {return Edit ;}
                                    //if(colIndex == 7) {return Edit ;}
                                    if(colIndex == 8) {return Edit ;}
                                    return false;   //Disallow the editing of any cell
                            }
                /*
                Nilai balik untuk jcombobox
                */
                 public Class getColumnClass(int c) {
                        if (c == 3) {
                            return DataComboTabel.class;
                        }
                        if (c == 4 ){
                            return DataComboTabel_Lisensi.class;
                        }
                        if (c == 5 ){
                            return DataComboTabel_Unit.class;
                        }
                        if (c == 6 ){
                            return DataComboTabel_Consigment.class;
                        }
                           return String.class;
                    }
            };

            /*
            JCombo Box
            */
            jTable1.setDefaultEditor(DataComboTabel.class, new DefaultCellEditor(
            _Combo_Status));
            jTable1.setDefaultEditor(DataComboTabel_Lisensi.class, new DefaultCellEditor(
            _Combo_Jenis));
             jTable1.setDefaultEditor(DataComboTabel_Unit.class, new DefaultCellEditor(
            _Combo_Unit));
            jTable1.setDefaultEditor(DataComboTabel_Consigment.class, new DefaultCellEditor(
           _Combo_Consigment));
            
            jTable1.setModel(Modeltabel);
            Tabel2 (jTable1, Modeltabel,1);
        }
        else {
            
            if ((header.length - 3 ) == Header.size()  ){           
                Header.clear();        
                for (int b = 0; b < header.length  ; b++){
                    Header.add(header[b]);
                }
                Modeltabel = new DefaultTableModel(Data,Header) {
                    @Override
                    public boolean isCellEditable(int rowIndex, int colIndex) {
                                //nilai false agar tidak bisa di edit
                                if(colIndex == 2) {return Edit ;}
                                if(colIndex == 3) {return Edit ;}
                                if(colIndex == 4) {return Edit ;}
                                if(colIndex == 5) {return Edit ;}
                                if(colIndex == 6) {return Edit ;}
                                //if(colIndex == 7) {return Edit ;}
                                if(colIndex == 8) {return Edit ;}
                                return false;   
                         }
                     public Class getColumnClass(int c) {
                        if (c == 3) {
                            return DataComboTabel.class;
                        }
                        if (c == 4 ){
                            return DataComboTabel_Lisensi.class;
                        }
                        if (c == 5 ){
                            return DataComboTabel_Unit.class;
                        }
                        if (c == 6 ){
                            return DataComboTabel_Consigment.class;
                        }
                        return String.class;
                    }         
               };
                jTable1.setDefaultEditor(DataComboTabel.class, new DefaultCellEditor(
                new JComboBox(Status)));
                jTable1.setDefaultEditor(DataComboTabel_Lisensi.class, new DefaultCellEditor(
                new JComboBox(Jenis)));
                 jTable1.setDefaultEditor(DataComboTabel_Unit.class, new DefaultCellEditor(
                new JComboBox(Unit)));
                jTable1.setDefaultEditor(DataComboTabel_Consigment.class, new DefaultCellEditor(
               _Combo_Consigment));
                
                jTable1.setModel(Modeltabel);
                Tabel2 (jTable1,Modeltabel ,2);         
            }
            else {
                /*
                Jika error xls tidak sesuai format maka cek
                header tabel sistem = atau tidak dengan header tabel xls
                System.out.println(header.length + " dan tabel xls " + Header.size());
                
                solusi
                sesuaikan - 3
                (header.length - 3 ) == Header.size() 
                */
                JOptionPane.showMessageDialog(null, "xls tidak sesuai format");
                System.out.println("xls tidak sesuai format");
            }                                 
        }
        /*
        Edit table jika listener
        */
        jTable1.getModel().addTableModelListener(new TableModelListener() {
        public void tableChanged(TableModelEvent e) {
            if (editTable == true){
                int Row  = jTable1.getSelectedRow();
                //System.out.println(e + " data row : " + data);
                try {
                    String[] Data = { 
                         jTable1.getValueAt(Row, 1).toString(),
                         jTable1.getValueAt(Row, 2).toString(),
                         jTable1.getValueAt(Row, 3).toString(),
                         jTable1.getValueAt(Row, 4).toString(),
                         jTable1.getValueAt(Row, 5).toString(),
                         jTable1.getValueAt(Row, 6).toString(),
                        };
                    SaveDatabase_InlineEditing(Data);
                }
                catch(Exception X){
                   if ( X.toString().contains("-1") ) {
                       JOptionPane.showMessageDialog(null, "Data kosong\nSolusi : Klik cancel edit kemudian klik filter kembali, agar edit berjalan dengan baik");
                       editTable = false;
                   }
                   else {
                       Logger.getLogger(Master_Barang.class.getName()).log(Level.SEVERE, null, X) ;
                   }
                }                
               }
            
            }
        });
    }
     
     public void SearchTable(final JTable JTabelBarang, final DefaultTableModel Modeltabel){
         String text = JTextFieldSearch1.getText();
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
     private void Tabel2 (final JTable JTabelBarang, final DefaultTableModel Modeltabel, int a  ){

       /*
        * Membuat sort pada tabel
        * Search Data
        * SearchTable(jTable1,Modeltabel);
        */     
        Edit2 = true;
        JTextFieldSearch1.addKeyListener(new KeyListener(){
            @Override
            public void keyReleased(KeyEvent e) {
               SearchTable(JTabelBarang,Modeltabel);
               if (JTextFieldSearch1.getText().equals("")){
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
        JTabelBarang.getColumnModel().getColumn(1).setCellRenderer( tengah );
        JTabelBarang.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(4).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(5).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(6).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(7).setCellRenderer( tengah ); 

        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */

        int jarak_colom[] = {40,200,300,100,100, 80, 80,100, 100 };
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
         *  Memasukan tombol ke jtable
            cek bokm model tabel,tabel,1, tatabel DB, column DB
         */
        JTabelBarang.getColumnModel().getColumn(8).setCellRenderer(  new ButtonJTable("delete"));
        JTabelBarang.getColumnModel().getColumn(8).setCellEditor( new  Sistem.ButtonJTableKeDua_All(
                new JCheckBox(),Modeltabel, JTabelBarang,1, "master_barang","item_code"));

        /*
         * Disable drag colum tabel
         */       
        JTabelBarang.getTableHeader().setReorderingAllowed(false);
     }
     
     private void HapusDataJTabel(JTable Data){
        /*
         * Logika hapus semua data di jtable
         */
        try {
            DefaultTableModel dtm = (DefaultTableModel) Data.getModel();
            dtm.setRowCount(0); 
        }
        catch (Exception X){
            
        }
     }
      private void AmbilDariDatabase(){
        HapusDataJTabel(jTable1);
        
        /*
         * Isi data ke Tabel dari database
         */         
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               
               AA = _Convert_Master("status", jComboStatus.getSelectedItem().toString() );
               BB = _Convert_Master("jenis", jComboJenis.getSelectedItem().toString() );
               CC = _Convert_Master("unit", jComboUnit.getSelectedItem().toString() );
               DD = _Convert_Master("consignment", jComboConsignment.getSelectedItem().toString() );
              
               HQ = stm.executeQuery("select item_code, item_name, status, jenis, unit, consignment, created_date from master_barang "
                       + " where status like '%" +AA+"%' and jenis like '%" +BB+ "%' and unit like '%"+CC+"%' and consignment like '%"+DD+"%' order by item_code asc; ");              
               while(HQ.next()  ){
                   int No   = HQ.getRow();
                   AA       = HQ.getString("item_code");
                   BB       = HQ.getString("item_name");
                   _intAA       = HQ.getInt("status");
                   _intBB       = HQ.getInt("jenis");
                   _intCC       = HQ.getInt("unit");
                   _intDD       = HQ.getInt("consignment");
                   GG       = HQ.getString("created_date");
                   
                   //BB = Convert.NilaiRupiah(BB);
                   //CC = Convert.NilaiRupiah(CC);
                   String[] add         = {
                       HQ.getRow() + "", AA,BB
                       ,_Convert_Master_Ke_Asal("status",_intAA) 
                       ,_Convert_Master_Ke_Asal("jenis",_intBB) 
                       ,_Convert_Master_Ke_Asal("unit",_intCC)
                       ,_Convert_Master_Ke_Asal("consignment",_intDD) ,GG};
                   Modeltabel.addRow(add);      
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
           try {
               DataNoTerakhirTabel = 1+ Integer.valueOf( (String ) jTable1.getValueAt(jTable1.getRowCount() -1, 0)).intValue();
           }
           catch (Exception X){
               
           }
     }
     
      private boolean Validasi(String[] c){
        for (int a = 0; a < c.length; a ++){
            if (c.equals("") ){
                JOptionPane.showMessageDialog(null, "Data kosong");
                return false;
            }
        }
        return true;
    }
     private void DeleteData(String Data, String Table){       
        Sistem.DB_MYSQL_Koneksi MYSQL = new Sistem.DB_MYSQL_Koneksi();
        MYSQL.MysqlDelete("delete from "+ Table+ " where no = '"+ Data + "'");
        //DeleteGambar (Data);       
     } 
     
     void SaveDatabase_InlineEditing(String[] Data){
        try {
            Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Stm.executeUpdate(" update master_barang "
                    + "set item_code ='"+ Data[0] +"',"
                    + " item_name   ='"+Data[1] + "' , "
                    + " isSn        ='1' , "
                    + " isStock     ='1' , "
                    + " status      ='"+_Convert_Master("status", Data[2]) + "' , "
                    + " Jenis       ='"+_Convert_Master("jenis", Data[3]) + "' , "
                    + " unit        ='"+_Convert_Master("unit", Data[4]) + "' , "
                    + " consignment ='"+_Convert_Master("consignment", Data[5]) + "' , "
                    + " userid ='"+UserName + "' , "
                    + " created_date = now() "
                    + "where item_code='"+Data[0]+"' ");
            //continue;
            //MYSQL.MysqlDelete("delete from master_barang ");    
        } catch (SQLException ex1) {
            JOptionPane.showMessageDialog(null, "Gagal update item : "+ AA );
            Logger.getLogger(Master_Barang.class.getName()).log(Level.SEVERE, null, ex1);
        }
     }
    
     int NilaiAwalProgress =0, Setuju;
     boolean Lanjut = true;
     Statement Stm = null;
     private void SaveDatabase() {
        
                /*
                 * validasi
                 */
                String[] Valid  = {jTable1.getValueAt(0, 1).toString()};
                if (Validasi(Valid)) {

                    final int a = jTable1.getRowCount() ;
                    
                    try {
                        Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    } catch (SQLException ex) {
                        Logger.getLogger(Master_Barang.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    /*
                        Jika add master barang secara manual
                        Supaya tidak update data keseluruhan
                        add = jumlah row awal sblm klik add
                        a   = jumlah row akhir
                    */
                    System.out.println(Add + " dan " + a + " dan " + ADD_Boleh);
                    if (Add < a && ADD_Boleh == true){
                        
                         /*
                        rumus progres bar
                        pb = max jumlah row / 8
                        */
                        JumlahRow           = a - Add;
                        NilaiMax            = JumlahRow * 10;
                        NilaiAwal           = NilaiMax /10 ;
                        BerapaKali          = (NilaiMax - NilaiAwal ) / JumlahRow ;
                        /*
                        rumus selanjutnya = NilaiAwal + ( BerapaKali * Looping i );
                        */
                        jProgressBar1.setMaximum(NilaiMax);
                        jProgressBar1.setMinimum(0);
                        jProgressBar1.setStringPainted(true);

                        final String[][] stringArray =  new String[a][6] ;
                        for(int i = Add - 1; i < a; i++){
                            stringArray[i][0] = jTable1.getValueAt(i, 1).toString();
                            stringArray[i][1] = jTable1.getValueAt(i, 2).toString();
                            stringArray[i][2] = jTable1.getValueAt(i, 3).toString();
                            stringArray[i][3] = jTable1.getValueAt(i, 4).toString();
                            stringArray[i][4] = jTable1.getValueAt(i, 5).toString();
                            stringArray[i][5] = jTable1.getValueAt(i, 6).toString();
                        }
                        Thread runner = new Thread() {
                            public void run() {
                                /*
                                    Stm, jumlah row table ,data jika xls, data jika add manual, true jika xls /false bukan xls
                                */
                                InsertDatabase(Stm, JumlahRow, stringArray, true);
                                AmbilDariDatabase();
                                Edit = false;
                                /*   add, save, import, edit */
                                boolean Data[] = {false, false, false, true};
                                Button(Data);
                            }
                        };
                        runner.start();
                    }
                    /*
                    Save data jika import dari xls
                    */
                    if (ADD_Boleh == false) {
                       /*
                        rumus progres bar
                        pb = max jumlah row / 8
                        */
                        JumlahRow           = jTable1.getRowCount();
                        NilaiMax            = JumlahRow * 10;
                        NilaiAwal           = NilaiMax /10 ;
                        BerapaKali          = (NilaiMax - NilaiAwal ) / JumlahRow ;
                        /*
                        rumus selanjutnya = NilaiAwal + ( BerapaKali * Looping i );
                        */

                        jProgressBar1.setMaximum(NilaiMax);
                        jProgressBar1.setMinimum(0);
                        jProgressBar1.setStringPainted(true);
                        
                        final String[][] stringArray =  new String[a][6] ;
                        for(int i=0;i< a;i++){
                            stringArray[i][0] = jTable1.getValueAt(i, 1).toString();
                            stringArray[i][1] = jTable1.getValueAt(i, 2).toString();
                            stringArray[i][2] = jTable1.getValueAt(i, 3).toString();
                            stringArray[i][3] = jTable1.getValueAt(i, 4).toString();
                            stringArray[i][4] = jTable1.getValueAt(i, 5).toString();
                            stringArray[i][5] = jTable1.getValueAt(i, 6).toString();
                        }
           
                        Thread runner = new Thread() {
                        public void run() {
                            /*
                                Stm, jumlah row table , data jika xls, data jika add manual, true jika xls / false bukan xls
                            */
                            InsertDatabase(Stm, a, stringArray, false );
                            //System.out.println("coba");
                            AmbilDariDatabase();
                             //jProgressBar1.setValue(Hasil);                  
                            //JButtonSave.setEnabled(false);
                            //JButtonEdit.setEnabled(true);
                            Edit = false;
                            /*   add, save, import, edit */
                            boolean Data[] = {false, false, false, true};
                            Button(Data);
                         }
                    };
                    runner.start();
                    //ADD_Boleh = false;
                    }                         
               }                 
     }
     
     
     void InsertDatabase(Statement Stm) {
         String[] Stat  = {"aktiv", "non aktive"};
         String[] Jen  = {"barang", "lisensi", "jasa"};
         String[] Un  = {"buah", "pcs", "meter"};
         String[] Co  = {"yes", "no"};
         
         Sistem.SatuUntukSemua Random = new Sistem.SatuUntukSemua();
         for (int a = 65555; a < 1000000; a++ ){
             try {
                Stm.executeUpdate("INSERT INTO master_barang "
                        + " (item_code, item_name, isSn, isStock, status, jenis, unit, consignment, userid, created_date ) "
                        + " values ('"+a +"','"+ "cd "+a + "ab" + "','"+ 1+"','"+ 1+"','"
                        +_Convert_Master("status", Stat[Random.randInt(0, 1)]) +"','"
                        +_Convert_Master("jenis", Jen[Random.randInt(0, 2)])+"','"
                        +_Convert_Master("unit", Un[Random.randInt(0, 2)]) + "','"
                        +_Convert_Master("consignment", Co[Random.randInt(0, 1)]) +"','"+ UserName + "',now() ); ");
            } catch (SQLException ex) {
               
            }
         }
     }
     
     /*
     Insert Database
     ada 2 pilihan
        Pilih false Ambil data String[][] stringArray
        Pilih true ambil data String[] Data
     
     Untuk  true = Data saat add item manual
            false   = Data saat di add scr xls
     a  = Jumlah rowtable
     */
     void InsertDatabase(Statement Stm, int a, String[][] stringArray, boolean pilih ){
         int i=0;
         if (pilih){
             i = Add - 1;
             a = jTable1.getRowCount();
         }
         else{
             i = 0;
         }
         
         System.out.println(i+","+a);
         for( i = i;i< a ;i++){
           
            AA =stringArray[i][0];
            BB =stringArray[i][1];
            CC =stringArray[i][2];
            DD =stringArray[i][3];
            EE =stringArray[i][4];
            FF =stringArray[i][5]; 
           
            try {
                Stm.executeUpdate("INSERT INTO master_barang "
                        + " (item_code, item_name, isSn, isStock, status, jenis, unit, consignment, userid, created_date ) "
                        + " values ('"+AA +"','"+BB + "','"+ 1+"','"+ 1+"','"
                        +_Convert_Master("status", CC) +"','"
                        +_Convert_Master("jenis", DD)+"','"
                        +_Convert_Master("unit", EE) + "','"
                        +_Convert_Master("consignment", FF) +"','"+ UserName + "',now() ); ");
            } catch (SQLException ex) {
                //System.out.println(ex.toString() + ex.toString().contains("PRIMARY") + ex.toString().contains("Duplicate entry") );
                if (ex.toString().contains("PRIMARY") || ex.toString().contains("Duplicate entry")){

                    if (Lanjut == true){
                        Setuju = JOptionPane.showConfirmDialog(null, "Data sebelumnya sudah ada, apakah anda yakin untuk mengupdatenya ?", "Perhatian ", JOptionPane.YES_NO_OPTION);
                        Lanjut = false;
                    }
                    if (Setuju == JOptionPane.YES_OPTION  && Lanjut == false){

                        try {
                             //JOptionPane.showMessageDialog(null, "Ada nama item yang sama, Tidak berhasil di save :" + AA);
                             //Sistem.koneksiMYSQL MYSQL = new Sistem.koneksiMYSQL();
                             Stm.executeUpdate(" update master_barang "
                                     + "set item_code ='"+ AA +"',"
                                     + " item_name   ='"+BB + "' , "
                                     + " isSn        ='1' , "
                                     + " isStock     ='1' , "
                                     + " status      ='"+_Convert_Master("status", CC) + "' , "
                                     + " Jenis       ='"+_Convert_Master("jenis", DD) + "' , "
                                     + " unit        ='"+_Convert_Master("unit", EE) + "' , "
                                     + " consignment ='"+_Convert_Master("consignment", FF) + "' , "
                                     + " userid ='"+UserName + "' , "
                                     + " created_date = now() "
                                     + "where item_code='"+AA +"' ");
                             //continue;
                             //MYSQL.MysqlDelete("delete from master_barang ");    
                         } catch (SQLException ex1) {
                             JOptionPane.showMessageDialog(null, "Gagal update item : "+ AA );
                             Logger.getLogger(Master_Barang.class.getName()).log(Level.SEVERE, null, ex1);
                             continue;
                         }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Tidak Jadi di Update item : " + AA);
                        Lanjut = true;
                        break;
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Item ini tidak berhasil di save :" + AA);
                     Logger.getLogger(Master_Barang.class.getName()).log(Level.SEVERE, null, ex);
                     continue;
                }

            }

            NilaiAwalProgress = i + 1;
            Hasil = NilaiAwal + ( BerapaKali * NilaiAwalProgress );
            Runnable runme = new Runnable() {
            public void run() {
              jProgressBar1.setValue(Hasil);
            }
          };
          SwingUtilities.invokeLater(runme);

          /*
          Rekasa menunda save progress

            try {
                Thread.sleep(10 );
              } catch (Exception ex) {
          } */
        }
     }
     
     /*
     Pilihan adalah
     jika return 123 adalah error
     */
     String _Convert_Master (String Pilihan, String Data){
         switch (Pilihan) {
             case "status" :
                 if (Data.equalsIgnoreCase("aktiv")) {
                     return "1";
                 }
                 else if (Data.equalsIgnoreCase("non aktiv"))  {
                     return "0";
                 }
                     return "";
             case "jenis" :
                 if (Data.equalsIgnoreCase("jasa")) {
                     return "0";
                 }
                 else if (Data.equalsIgnoreCase("barang"))  {
                     return "1";
                 }
                 else if (Data.equalsIgnoreCase("lisensi")){
                     return "2";
                 }
                 return "";
             case "unit" :
                 if (Data.equalsIgnoreCase("PCS")) {
                     return "0";
                 }
                 else if (Data.equalsIgnoreCase("Buah"))  {
                     return "1";
                 }
                 else if (Data.equalsIgnoreCase("Meter")){
                     return "2";
                 }
                 return "";
                 
             case "consignment" :
                 if (Data.equalsIgnoreCase("no")) {
                     return "0";
                 }
                 else if (Data.equalsIgnoreCase("yes"))  {
                     return "1";
                 }
                 return "";
             case " ":
                 return "";
             default :
             return "12";
         }  
     }
     
     String _Convert_Master_Ke_Asal (String Pilihan, int Data){
         switch (Pilihan) {
             case "status" :
                 if (Data == 1 ){
                     return "aktiv";
                 }
                 else if (Data == 0)  {
                     return "non aktiv";
                 }
                     return "error";
             case "jenis" :
                 if (Data == 0 ) {
                     return "jasa";
                 }
                 else if (Data == 1)  {
                     return "barang";
                 }
                 else if (Data == 2 ){
                     return "lisensi";
                 }
                 return "error";
             case "unit" :
                 if (Data == 0 ) {
                     return "PCS";
                 }
                 else if (Data == 1)  {
                     return "Buah";
                 }
                 else if (Data== 2){
                     return "Meter";
                 }
                 return "Error";
                 
             case "consignment" :
                 if (Data== 0) {
                     return "no";
                 }
                 else if (Data == 1)  {
                     return "yes";
                 }
                 return "error";
             default :
             return "errpr";
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

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        JButtonAdd = new javax.swing.JButton();
        JButtonSave = new javax.swing.JButton();
        JButtonEdit = new javax.swing.JButton();
        JButtonPrint = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        JTextFieldSearch1 = new javax.swing.JTextField();
        JButtonImportXls = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        JButtonFilter = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboJenis = new javax.swing.JComboBox();
        jComboConsignment = new javax.swing.JComboBox();
        jComboUnit = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboStatus = new javax.swing.JComboBox();
        JButtonEditCancel = new javax.swing.JButton();

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

        JButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/add.png"))); // NOI18N
        JButtonAdd.setText("Add");
        JButtonAdd.setEnabled(false);

        JButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/kecil_save_icon2.png"))); // NOI18N
        JButtonSave.setText("Save");
        JButtonSave.setEnabled(false);

        JButtonEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/kecil_edit_icon2.png"))); // NOI18N
        JButtonEdit.setText("Edit");

        JButtonPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/kecil_print_icon2.png"))); // NOI18N
        JButtonPrint.setText("Print");

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/View.png"))); // NOI18N
        jLabel16.setText("Search  :");

        JButtonImportXls.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/xls kecil.png"))); // NOI18N
        JButtonImportXls.setText("Import Xls");
        JButtonImportXls.setEnabled(false);

        JButtonFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/kecil_filter.png"))); // NOI18N
        JButtonFilter.setText("Filter");

        jLabel1.setText("Jenis :");

        jLabel2.setText("Consignment :");

        jLabel3.setText("Unit :");

        jComboJenis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "lisensi", "barang", "jasa" }));

        jComboConsignment.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "yes", "no" }));

        jComboUnit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Unit", "PCS", "Meter", "Buah" }));

        jLabel4.setText("Status :");

        jComboStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Non Aktiv", "Aktiv" }));

        JButtonEditCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/kecil_Delete.png"))); // NOI18N
        JButtonEditCancel.setText("Cancel Edit");
        JButtonEditCancel.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(83, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonEditCancel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(JButtonAdd)
                        .addGap(6, 6, 6)
                        .addComponent(JButtonSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonPrint)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel16))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboConsignment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JTextFieldSearch1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(JButtonFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JButtonImportXls, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jComboConsignment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jComboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(JButtonPrint)
                                .addComponent(JButtonEdit)
                                .addComponent(JButtonSave)
                                .addComponent(JButtonAdd)
                                .addComponent(jLabel16)
                                .addComponent(JTextFieldSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JButtonEditCancel))
                            .addComponent(JButtonImportXls, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonAdd;
    private javax.swing.JButton JButtonEdit;
    private javax.swing.JButton JButtonEditCancel;
    private javax.swing.JButton JButtonFilter;
    private javax.swing.JButton JButtonImportXls;
    private javax.swing.JButton JButtonPrint;
    private javax.swing.JButton JButtonSave;
    private javax.swing.JTextField JTextFieldSearch1;
    private javax.swing.JComboBox jComboConsignment;
    private javax.swing.JComboBox jComboJenis;
    private javax.swing.JComboBox jComboStatus;
    private javax.swing.JComboBox jComboUnit;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
