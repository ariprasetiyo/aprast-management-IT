/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema;

import arprast.hrdpro.LoginUser;
import com.jtattoo.plaf.smart.SmartLookAndFeel;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author arprast
 */
public class TemaSmart {
    private  TemaSmart(){
        try {
            Properties props = new Properties();
                
                props.put("logoString", "my company"); 
                props.put("licenseKey", "INSERT YOUR LICENSE KEY HERE");
                
                props.put("selectionBackgroundColor", "180 240 197"); 
                props.put("menuSelectionBackgroundColor", "180 240 197"); 
                
                props.put("controlColor", "218 254 230");
                props.put("controlColorLight", "218 254 230"); 
                props.put("controlColorDark", "180 240 197"); 

                props.put("buttonColor", "218 230 254");
                props.put("buttonColorLight", "255 255 255");
                props.put("buttonColorDark", "244 242 232");

                props.put("rolloverColor", "218 254 230"); 
                props.put("rolloverColorLight", "218 254 230"); 
                props.put("rolloverColorDark", "180 240 197"); 

                props.put("windowTitleForegroundColor", "0 0 0");
                props.put("windowTitleBackgroundColor", "180 240 197"); 
                props.put("windowTitleColorLight", "218 254 230"); 
                props.put("windowTitleColorDark", "180 240 197"); 
                props.put("windowBorderColor", "218 254 230");
                
                // set your theme
                SmartLookAndFeel.setCurrentTheme(props);
                // select the Look and Feel
                javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(LoginUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(LoginUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(LoginUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
