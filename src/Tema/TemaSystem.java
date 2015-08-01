/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author arprast
 */
public class TemaSystem {
    public TemaSystem(){
        try {
            //com.jtattoo.plaf.custom.systemx.SystemXLookAndFeel.setTheme("Default");
            //com.sun.java.swing.plaf.windows.WindowsLookAndFeel
            //javax.swing.plaf.metal.MetalLookAndFeel
            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TemaSystem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(TemaSystem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TemaSystem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(TemaSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
