/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema;

import arprast.hrdpro.LoginUser;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author arprast
 */
public class TemaWarnaPutih {
    public  TemaWarnaPutih () throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        try {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             * 
             * for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
             */
            //javax.swing.UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
             //javax.swing.UIManager.installLookAndFeel("SeaGlass", "com.seaglasslookandfeel.SeaGlassLookAndFeel");
            //("com.jtattoo.plaf.smart.SmartLookAndFeel");
            
               //javax.swing.UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
             // setup the look and feel properties
            
            
            
            /*
             * Look and feel warna putih
             */
            Properties props = new Properties();
            props.put("logoString", "my comsdsdpany");
//            props.put("logoString", "\u00A0");
            props.put("backgroundPattern", "on");
            props.put("textAntiAliasing", "on");
            props.put("windowDecoration", "on");
            props.put("dynamicLayout", "on");
            props.put("toolbarDecorated", "on");
            props.put("linuxStyleScrollBar", "on");
              // props.put("dynamicLayout", "on");
            
            props.put("windowTexture", "on");
            props.put("backgroundTexture", "on");
            props.put("alterBackgroundTexture", "on");

            
            /*
             * bagian Frame
             */
            //props.put("windowTitleForegroundColor", "228 228 255");
            //props.put("windowTitleBackgroundColor", "0 0 96");
            //props.put("windowTitleColorLight", "0 0 96");
            //props.put("windowTitleColorDark", "0 0 64");
            props.put("windowBorderColor", "96 96 64");

            props.put("windowInactiveTitleForegroundColor", "228 228 255");
            props.put("windowInactiveTitleBackgroundColor", "0 0 96");
            props.put("windowInactiveTitleColorLight", "0 0 96");
            props.put("windowInactiveTitleColorDark", "0 0 64");
            props.put("windowInactiveBorderColor", "32 32 128");
             

            //props.put("menuForegroundColor", "228 228 255");
            //props.put("menuBackgroundColor", "0 0 64");
            //props.put("menuSelectionForegroundColor", "0 0 0");
            props.put("menuSelectionBackgroundColor", "255 192 48");
            //props.put("menuColorLight", "32 32 128");
            //props.put("menuColorDark", "16 16 96");
            
            props.put("toolbarColorLight", "32 32 128");
            props.put("toolbarColorDark", "16 16 96");

            //props.put("controlForegroundColor", "228 228 255");
            //props.put("controlBackgroundColor", "16 16 96");
            //props.put("controlColorLight", "16 16 96");
            //props.put("controlColorDark", "8 8 64");
            //props.put("controlHighlightColor", "32 32 128");
            props.put("controlShadowColor", "16 16 64");
            props.put("controlDarkShadowColor", "8 8 32");

            //props.put("buttonForegroundColor", "0 0 32");
            //props.put("buttonBackgroundColor", "196 196 196");
            props.put("buttonColorLight", "196 196 240");
            props.put("buttonColorDark", "164 164 228");
            

            

            /*
             * Warna text
             */
            props.put("foregroundColor", "0 0 0");
            /*
             * Waran background
             */
            //props.put("backgroundColor", "5 230 255");
            
            String W  = "154 156 156";
            props.put("backgroundColorLight", W);
            props.put("backgroundColorDark", W);
            //props.put("alterBackgroundColor", "255 0 0");
            //props.put("frameColor", "96 96 64");
            //props.put("gridColor", "96 96 64");
            
            props.put("focusCellColor", "240 0 0");

            //props.put("disabledForegroundColor", "128 128 164");
            //"223 243 247" biru
            /*
             * Warna text component jbox jika disable
             */
            props.put("disabledForegroundColor", "0 0 0");
            props.put("disabledBackgroundColor", "219 219 219");

            props.put("selectionForegroundColor", "0 0 0");
            //props.put("selectionBackgroundColor", "154 220 252");

            
            //props.put("inputForegroundColor", "228 228 255");
            //props.put("inputBackgroundColor", "0 0 96");
            
            
            props.put("rolloverColor", "240 168 0");
            props.put("rolloverColorLight", "240 168 0");
            props.put("rolloverColorDark", "196 137 0");
           
            /*
             *  controlTextFont
                systemTextFont
                userTextFont
                menuTextFont
                windowTitleFont
                subTextFont
             */
            props.put("systemTextFont", "Arial PLAIN 13");
            props.put("controlTextFont", "Arial PLAIN 13");
            props.put("menuTextFont", "Arial PLAIN 13");
            props.put("userTextFont", "Arial PLAIN 13");
            props.put("subTextFont", "Arial PLAIN 12");
            props.put("windowTitleFont", "Arial BOLD 13");
            
            //props.put("selectionForegroundColor", "237 241 242");
            

            // set your theme
            
            com.jtattoo.plaf.aluminium.AluminiumLookAndFeel.setCurrentTheme(props);
            javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
            
            
           //com.jtattoo.plaf.mcwin.McWinLookAndFeel.setCurrentTheme(props);
            //com.jtattoo.plaf.mcwin.mc;
            // select the Look and Feel
            //javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
               //.swing.UIManager.put("frameBaseActive", Color.DARK_GRAY.darker()); 
      
                    /*
                     * Pada bagian tengajh adalah transparasi 
                     * untk header pingiran form
                     */
                    //javax.swing.UIManager.put("nimbusBase", new Color(100, 30, 255));
                    
                    /*
                     * Tombol
                     * 40, 200, 255
                     * 200, 200, 255 => abu2
                     */
                    //javax.swing.UIManager.put("nimbusBlueGrey", new Color(200, 230, 255));
                    /*
                     javax.swing.UIManager.put("OptionPane.cancelButtonText", "Annuler");
                    javax.swing.UIManager.put("OptionPane.noButtonText", "Non");
                    javax.swing.UIManager.put("OptionPane.okButtonText", "D'accord");
                    javax.swing.UIManager.put("OptionPane.yesButtonText", "Oui");
                    
                    /*
                     * Background
                     * a,b,c
                     * a,b,transparasi kecerahan
                     */
                   // javax.swing.UIManager.put("control", new Color(160,200, 255));
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(LoginUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
