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
public class TemaGelap {
    private TemaGelap() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
        // select Look and Feel
        try{
            Properties props = new Properties();
            props.put("logoString", "my company");
            props.put("textShadow", "off");
            props.put("systemTextFont", "Arial PLAIN 13");
            props.put("controlTextFont", "Arial PLAIN 13");
            props.put("menuTextFont", "Arial PLAIN 13");
            props.put("userTextFont", "Arial PLAIN 13");
            props.put("subTextFont", "Arial PLAIN 12");
            props.put("windowTitleFont", "Arial BOLD 13");
            com.jtattoo.plaf.hifi.HiFiLookAndFeel.setCurrentTheme(props);
            javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
             } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(LoginUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
