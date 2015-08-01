/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import javax.swing.JOptionPane;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 *  Cara penggunaannya
 *  SecretKey key = KeyGenerator.getInstance("DES").generateKey();
    DesEncrypter encrypter = new DesEncrypter(key);
    String encrypted = encrypter.encrypt("Don't tell anybody!!");
    String decrypted = encrypter.decrypt(encrypted);
    System.out.println(encrypted  + " dan " + decrypted);
 */

/**
 * Penggunaan deskripsi saja
 * SecretKey key = KeyGenerator.getInstance("DES").generateKey();
   SistemPro.SecurityEnkripsiDanDeskripsi Deskripsi = new SistemPro.SecurityEnkripsiDanDeskripsi(key);
   String decrypted =Deskripsi.decrypt(Pass);
 */

public class SecurityEnkripsiDanDeskripsi {


  public String encrypt(String str, String key) throws Exception {
      //String FilterStr = "[]";
      String FilterKey = "^[a-zA-Z0-9]{3,50}$";
      if (key.matches(FilterKey) && !key.matches("[\\s\n]") ){;
          if (str.matches(FilterKey))
          {
             
          }
          else {
              JOptionPane.showMessageDialog(null, "Error: tidak boleh ada karakter aneh dan spasi");
              System.err.print("Error: tidak boleh ada karakter aneh dan spasi");
              System.exit(1);
          }
      }
      else {
          
          System.err.print("Error: key terdapat karakter aneh");
          System.exit(1);
      }
      
    String Hasil1       = EnkripsiCrypto(str);
    String encodedKey   = EnkripsiCrypto(key); 
    
    /*
     * Encode base64
     */
    BASE64Encoder encoder = new BASE64Encoder();
    String encodedBytes = encoder.encodeBuffer(Hasil1.getBytes());
    
    String RegrexData1 = null;
    String RegrexData2 = null;     
    String FilterEncode = encodedBytes.substring(0, 1);  
     switch (FilterEncode.toLowerCase()){
         case "q":
             RegrexData1 = "0";
             RegrexData2 = "dxcsdr";
             break;
         case "w":
             RegrexData1 = "5";
             RegrexData2 = "fdjhfa";
             break;
         case "e":
             RegrexData1 = "6";
             RegrexData2 = "dghjc";
             break;
         case "r":
             RegrexData1 = "9";
             RegrexData2 = "7dgsah";
             break;
         case "t":
             RegrexData1 = "7";
             RegrexData2 = "asdskv";
             break;
         case "y":
             RegrexData1 = "3";
             RegrexData2 = "casafg";
             break;
         case "u":
             RegrexData1 = "4";
             RegrexData2 = "hgkfyl";
             break;
         case "i":
             RegrexData1 = "2";
             RegrexData2 = "zklgxs";
             break;
         case "o":
             RegrexData1 = "8";
             RegrexData2 = "rdssda";
             break;
         case "p":
             RegrexData1 = "g";
             RegrexData2 = "cjhkas";
             break;
         case "a":
             RegrexData1 = "q";
             RegrexData2 = "wdfwsd";
             break;
         case "s":
             RegrexData1 = "e";
             RegrexData2 = "rsaqpw";
             break;
         case "d":
             RegrexData1 = "t";
             RegrexData2 = "yadwsd";
             break;
         case "f":
             RegrexData1 = "a";
             RegrexData2 = "lasdrj";
             break;
         case "g":
             RegrexData1 = "d";
             RegrexData2 = "vsdad";
             break;
         case "h":
             RegrexData1 = "f";
             RegrexData2 = "vvdsa";
             break;
         case "j":
             RegrexData1 = "m";
             RegrexData2 = "gadui";
             break;
         case "k":
             RegrexData1 = "o";
             RegrexData2 = "khjuk";
             break;
         case "l":
             RegrexData1 = "p";
             RegrexData2 = "gjhuy";
             break;
         case "m":
             RegrexData1 = "z";
             RegrexData2 = "xhjty";
             break;
         case "n":
             RegrexData1 = "c";
             RegrexData2 = "mhtasr";
             break;
         case "b":
             RegrexData1 = "s";
             RegrexData2 = "hhvsed";
             break;
         case "v":
             RegrexData1 = "1";
             RegrexData2 = "adtgb";
             break;
         case "c":
             RegrexData1 = "n";
             RegrexData2 = "ndtrv";
             break;
         case "x":
             RegrexData1 = "v";
             RegrexData2 = "qfdgrt";
             break;
         case "z":
             RegrexData1 = "w";
             RegrexData2 = "djhmnp";
             break;
         case "1":
             RegrexData1 = "x";
             RegrexData2 = "zasadv";
             break;
         case "2":
             RegrexData1 = "r";
             RegrexData2 = "pfrtex";
             break;
         case "3":
             RegrexData1 = "b";
             RegrexData2 = "jasad";
             break;
         case "4":
             RegrexData1 = "h";
             RegrexData2 = "xghnc";
             break;
         case "5":
             RegrexData1 = "j";
             RegrexData2 = "bahopgf";
             break;
         case "6":
             RegrexData1 = "u";
             RegrexData2 = "qdhmhgb";
             break;
         case "7":
             RegrexData1 = "y";
             RegrexData2 = "ksfipn";
             break;
         case "8":
             RegrexData1 = "i";
             RegrexData2 = "oadjlpu";
             break;
         case "9":
             RegrexData1 = "k";
             RegrexData2 = "vophfv";
             break;
         case "0":
             RegrexData1 = "l";
             RegrexData2 = "tdfthg";
             break;
     }

     encodedBytes = encodedBytes.replaceAll("(\\s\n)?", "");
     String Data = RegrexData1.concat(encodedBytes).concat(RegrexData2).concat(encodedKey);
     return Data;
  }
  
  private String EnkripsiCrypto(String str) {
      
      /*
       * Index di mulai dari nol
       * Total karakter 67, variabel JmlhChar di hitungan dari index 0
       * Setingan awal geser adalah 1
       */
       char[] kr ={ '0','1','2','3','4','5','6','7','8','9',' ','.','□',+
                    'a','b','c','d','e','f','g','h','i','j','k','l','m',+
                    'n','o','p','q','r','s','t','u','v','w','x','y','z', +
                   '\'','!','?','A','B','C','D','E','F','G','H','I','J', +
                    'K','L','M','N','O','P','Q','R','S','T','U','V','W',+
                    'X','Y','Z',',','_','/','@','#','$','%','^','&'                  
                   };
        String Hasil1 = "";
        char[] cArray1 =str.toCharArray();
        int JmlhChar = kr.length - 1;
        int Geser = 1;
        for (char c1 : cArray1){
            for(int i=0; i<=JmlhChar; i++){
                if(c1 == kr[i]){
                   i = i+(Geser);
                   if(i>=(JmlhChar + 1)){
                       i = i-(JmlhChar + 1);
                    }
                    c1 = kr[i];
                    Hasil1 = Hasil1 + c1;
                 }
            }
        }
        return Hasil1;
  }

  public String decrypt(String str, String key) throws Exception {
     
    String RegrexData2 = null;     
    String FilterEncode = str.substring(0, 1);  
     switch (FilterEncode.toLowerCase()){
         case "0":
             //RegrexData1 = "0";
             RegrexData2 = "dxcsdr";
             break;
         case "5":
             //RegrexData1 = "5";
             RegrexData2 = "fdjhfa";
             break;
         case "6":
             //RegrexData1 = "6";
             RegrexData2 = "dghjc";
             break;
         case "9":
             //RegrexData1 = "9";
             RegrexData2 = "7dgsah";
             break;
         case "7":
             //RegrexData1 = "7";
             RegrexData2 = "asdskv";
             break;
         case "3":
             //RegrexData1 = "3";
             RegrexData2 = "casafg";
             break;
         case "4":
             //RegrexData1 = "4";
             RegrexData2 = "hgkfyl";
             break;
         case "2":
             //RegrexData1 = "2";
             RegrexData2 = "zklgxs";
             break;
         case "8":
             //RegrexData1 = "8";
             RegrexData2 = "rdssda";
             break;
         case "g":
             //RegrexData1 = "g";
             RegrexData2 = "cjhkas";
             break;
         case "q":
             //RegrexData1 = "q";
             RegrexData2 = "wdfwsd";
             break;
         case "e":
             //RegrexData1 = "e";
             RegrexData2 = "rsaqpw";
             break;
         case "t":
             //RegrexData1 = "t";
             RegrexData2 = "yadwsd";
             break;
         case "a":
             //RegrexData1 = "a";
             RegrexData2 = "lasdrj";
             break;
         case "d":
             //RegrexData1 = "d";
             RegrexData2 = "vsdad";
             break;
         case "f":
             //RegrexData1 = "f";
             RegrexData2 = "vvdsa";
             break;
         case "m":
             //RegrexData1 = "m";
             RegrexData2 = "gadui";
             break;
         case "o":
             //RegrexData1 = "o";
             RegrexData2 = "khjuk";
             break;
         case "p":
             //RegrexData1 = "p";
             RegrexData2 = "gjhuy";
             break;
         case "z":
             //RegrexData1 = "z";
             RegrexData2 = "xhjty";
             break;
         case "c":
             //RegrexData1 = "c";
             RegrexData2 = "mhtasr";
             break;
         case "s":
             //RegrexData1 = "s";
             RegrexData2 = "hhvsed";
             break;
         case "1":
             //RegrexData1 = "1";
             RegrexData2 = "adtgb";
             break;
         case "n":
             //RegrexData1 = "n";
             RegrexData2 = "ndtrv";
             break;
         case "v":
             //RegrexData1 = "v";
             RegrexData2 = "qfdgrt";
             break;
         case "w":
             //RegrexData1 = "w";
             RegrexData2 = "djhmnp";
             break;
         case "x":
             //RegrexData1 = "x";
             RegrexData2 = "zasadv";
             break;
         case "r":
             //RegrexData1 = "r";
             RegrexData2 = "pfrtex";
             break;
         case "b":
             //RegrexData1 = "b";
             RegrexData2 = "jasad";
             break;
         case "h":
             //RegrexData1 = "h";
             RegrexData2 = "xghnc";
             break;
         case "j":
             //RegrexData1 = "j";
             RegrexData2 = "bahopgf";
             break;
         case "u":
             //RegrexData1 = "u";
             RegrexData2 = "qdhmhgb";
             break;
         case "y":
             //RegrexData1 = "y";
             RegrexData2 = "ksfipn";
             break;
         case "i":
             //RegrexData1 = "i";
             RegrexData2 = "oadjlpu";
             break;
         case "k":
             //RegrexData1 = "k";
             RegrexData2 = "vophfv";
             break;
         case "l":
             //RegrexData1 = "1";
             RegrexData2 = "tdfthg";
             break;
     }
     //System.out.println(str + " awal");
     String RegrexAmbilKey = "^("+FilterEncode+")+[a-zA-Z0-9\\+_\\-\\=\\,\\./^/*&]+("+RegrexData2+")+";
     String Replace = str.replaceAll(RegrexAmbilKey, "");
     
     /*
      * decode key
      */
     /*
      untuk sementara seperti ini dlu
        - ini berpengaruh saat mendiskripkan password. 
          ketidak cocokan key
     */
     String HasilKey = DeskripsiCrypto (Replace);
     if (HasilKey.contains(key) ){
         
     }
     else {
         System.out.print(HasilKey + " dan " + key + ", Error: not match key : securityenkripsideskripsi");
         System.exit(1);
     }
     /*
      * Mencari karater base64
      */
     String RegrexAmbilPass = "^("+FilterEncode+")+";
     String ReplacePass     = str.replaceAll(RegrexAmbilPass, "");
     String ReplacePass2    = ReplacePass.replaceAll("("+RegrexData2+")+("+Replace+")$", "");
     
      /*
       * Decode base64
       */
      BASE64Decoder decoder = new BASE64Decoder();
      byte[] decodedBytes = decoder.decodeBuffer(ReplacePass2);
      String Hasil2 = new String(decodedBytes);

      /*
       * decode password cypro
       */   
      String HasilPass = DeskripsiCrypto (Hasil2);
      //System.out.println(HasilPass + " hasil pass");
      return HasilPass;
      
  }
  private String DeskripsiCrypto (String Hasil2){
      /*
       * Index di mulai dari nol
       * Total karakter 66, variabel JmlhChar
       * Setingan awal geser adalah 1
       */
        char[] kr ={ '0','1','2','3','4','5','6','7','8','9',' ','.','□',+
                    'a','b','c','d','e','f','g','h','i','j','k','l','m',+
                    'n','o','p','q','r','s','t','u','v','w','x','y','z', +
                   '\'','!','?','A','B','C','D','E','F','G','H','I','J', +
                    'K','L','M','N','O','P','Q','R','S','T','U','V','W',+
                    'X','Y','Z',',','_','/','@','#','$','%','^','&'                 
                   };
        String bantu2 = "";
        String Hasil3 = new String(Hasil2);
         //enkripsi
        char[] cArray1 =Hasil3.toCharArray();
        int JmlhChar = kr.length - 1;
        for (char c1 : cArray1){
            for(int i=0; i<=(JmlhChar - 1); i++){
                if(c1 == kr[i]){
                   i = i-(1);
                   if(i<=-1){
                       i = i+JmlhChar;
                    }
                    c1 = kr[i];
                    bantu2 = bantu2 + c1;
                 }
            }
        }
    return bantu2;
  }
}
