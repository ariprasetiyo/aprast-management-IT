package arprast.hrdpro;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 *
 * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * @author ges
 * @author kwalrath
 */
/* MenuLayoutDemo.java requires no other files. */

public class MenuLayoutDemo {
  public JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.PAGE_AXIS));
    menuBar.add(createMenu("Menu 1"));
    menuBar.add(createMenu("Menu 2"));
    menuBar.add(createMenu("Menu 3"));

    menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
    return menuBar;
  }

  // used by createMenuBar
  public JMenu createMenu(String title) {
    JMenu m = new HorizontalMenu(title);
    m.add("Menu item #1 in " + title);
    m.add("Menu item #2 in " + title);
    m.add("Menu item #3 in " + title);

    JMenu submenu = new HorizontalMenu("Submenu");
    submenu.add("Submenu item #1");
    submenu.add("Submenu item #2");
    m.add(submenu);

    return m;
  }

  /**
   * Create the GUI and show it. For thread safety, this method should be
   * invoked from the event-dispatching thread.
   *
  private static void createAndShowGUI() {
    // Create and set up the window.
    JFrame frame = new JFrame("MenuLayoutDemo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create and set up the content pane.
    MenuLayoutDemo demo = new MenuLayoutDemo();
    Container contentPane = frame.getContentPane();
    contentPane.setBackground(Color.WHITE); // contrasting bg
    
    JPanel asa = new JPanel();
    asa.add(demo.createMenuBar());
    contentPane.add(asa, BorderLayout.LINE_START);
    // Display the window.
    frame.setSize(300, 150);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }
*/
  class HorizontalMenu extends JMenu {
    HorizontalMenu(String label) {
      super(label);
      JPopupMenu pm = getPopupMenu();
      pm.setLayout(new BoxLayout(pm, BoxLayout.LINE_AXIS));
    }

    public Dimension getMinimumSize() {
      return getPreferredSize();
    }

    public Dimension getMaximumSize() {
      return getPreferredSize();
    }

    public void setPopupMenuVisible(boolean b) {
      boolean isVisible = isPopupMenuVisible();
      if (b != isVisible) {
        if ((b == true) && isShowing()) {
          // Set location of popupMenu (pulldown or pullright).
          // Perhaps this should be dictated by L&F.
          int x = 0;
          int y = 0;
          Container parent = getParent();
          if (parent instanceof JPopupMenu) {
            x = 0;
            y = getHeight();
          } else {
            x = getWidth();
            y = 0;
          }
          getPopupMenu().show(this, x, y);
        } else {
          getPopupMenu().setVisible(false);
        }
      }
    }
  }
}