/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class OutlookBarLayout implements LayoutManager2, java.io.Serializable, Runnable {
        public Hashtable<String, Component> components = new Hashtable<String, Component>();
        public int buttonHeight = 31;
        private int separatorHeight = 8;

        private int totalHeight;
        private int noOfButtonLeft;
        private int top;
        private int left;
        private int right;
        private Container target;
        private int currentY;
        private Thread thread;
        private boolean shouldStop = true;

        public OutlookBarLayout() {
        }

        public void addLayoutComponent(Component comp, Object constraints) {
                synchronized (comp.getTreeLock()) {
                        if (constraints != null || constraints instanceof String) {
                                addLayoutComponent((String) constraints, comp);
                        } else {
                                throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
                        }
                }
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
                synchronized (comp.getTreeLock()) {
                        components.put(name, comp);
                }
        }

        public void removeLayoutComponent(Component comp) {
                synchronized (comp.getTreeLock()) {
                        for (Object o : components.entrySet()) {
                                Entry<String, Component> entry = (Entry<String, Component>) o;
                                if (comp == entry.getValue()) {
                                        components.remove(entry.getKey());
                                }
                        }
                }
        }

        public Component getLayoutComponent(Object constraints) {
                throw new IllegalArgumentException("cannot get component: unknown constraint: " + constraints);
        }

        public Component getLayoutComponent(Container target, Object constraints) {
                throw new IllegalArgumentException("cannot get component: invalid constraint: " + constraints);
        }

        public Object getConstraints(Component comp) {
                return null;
        }

        public void setSelectedButton(JButton button) {
                for (Object o : components.entrySet()) {
                        Entry<String, Component> entry = (Entry<String, Component>) o;
                        JComponent comp = (JComponent) entry.getValue();
                        if (entry.getValue() instanceof JPanel && comp.getClientProperty("button") == button) {
                                comp.putClientProperty("selected", true);
                        } else {
                                comp.putClientProperty("selected", null);
                        }
                }
                button.putClientProperty("selected", true);
        }

        public void setSelectedComponent(Component c) {
                for (Object o : components.entrySet()) {
                        Entry<String, Component> entry = (Entry<String, Component>) o;
                        if (entry.getValue() instanceof JComponent) {
                                ((JComponent) entry.getValue()).putClientProperty("selected", null);
                        }
                }
                if (c instanceof JComponent) {
                        ((JComponent) c).putClientProperty("selected", true);
                }
        }

        public Dimension minimumLayoutSize(Container target) {
                synchronized (target.getTreeLock()) {
                        Dimension dim = new Dimension(0, 0);

                        //                      boolean ltr = target.getComponentOrientation().isLeftToRight();
                        //                      Component c = null;

                        //                      for (Object o : components.entrySet()) {
                        //                              Entry<String, Component> entry = (Entry<String, Component>) o;
                        //                              Component c = entry.getValue();
                        //                              if (c instanceof JPanel) {
                        //                                      Dimension d = c.getMinimumSize();
                        //                                      dim.width += d.width;
                        //                                      dim.height = Math.max(d.height, dim.height);
                        //                              }
                        //                      }
                        //                      Insets insets = target.getInsets();
                        //                      dim.width += insets.left + insets.right;
                        //                      dim.height += insets.top + insets.bottom;

                        return dim;
                }
        }

        public Dimension preferredLayoutSize(Container target) {
                synchronized (target.getTreeLock()) {
                        Dimension dim = new Dimension(0, 0);

                        for (Object o : components.entrySet()) {
                                Entry<String, Component> entry = (Entry<String, Component>) o;
                                Component c = entry.getValue();
                                Dimension d = c.getPreferredSize();
                                dim.width += d.width;
                                dim.height = Math.max(d.height, dim.height);
                        }
                        Insets insets = target.getInsets();
                        dim.width += insets.left + insets.right;
                        dim.height += insets.top + insets.bottom;

                        return dim;
                }
        }

        public Dimension maximumLayoutSize(Container target) {
                return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        public float getLayoutAlignmentX(Container parent) {
                return 0.5f;
        }

        public float getLayoutAlignmentY(Container parent) {
                return 0.5f;
        }

        public void invalidateLayout(Container target) {
        }

        public void layoutContainer(Container target) {
                if (shouldStop) {
                        shouldStop = false;
                        thread = new Thread(this);
                        thread.start();
                        this.target = target;
                }
        }

        private void draw() {
                synchronized (target.getTreeLock()) {
                        Insets insets = target.getInsets();
                        top = insets.top;
                        left = insets.left;
                        right = target.getWidth() - insets.right;
                        totalHeight = target.getHeight();
                        noOfButtonLeft = components.size() / 2;

                        top += totalHeight - ((components.size() / 2) * buttonHeight) - separatorHeight;

                        JLabel separator = (JLabel) components.get("separator");
                        separator.setBounds(0, top, right - left, separatorHeight);
                        separator.setVisible(true);
                        top += separatorHeight;

                        for (Object o : components.entrySet()) {
                                Entry<String, Component> entry = (Entry<String, Component>) o;
                                Component c = entry.getValue();
                                if (c instanceof JComponent) {
                                        JComponent comp = (JComponent) c;
                                        if (c instanceof JButton) {
                                                Dimension d = c.getPreferredSize();
                                                c.setBounds(0, top, right - left, d.height);
                                                c.setSize(right - left, buttonHeight);
                                                top += buttonHeight;

                                                noOfButtonLeft--;

                                                c.getParent().setComponentZOrder(c, 0);
                                        } else if (c instanceof JPanel) {
                                                if (comp.getClientProperty("selected") != null && (Boolean) comp.getClientProperty("selected")) {
                                                        layoutPanel((JPanel) c, top);
                                                }
                                        }
                                }
                        }
                }
        }

        private int layoutPanel(JPanel selectJPanel, int top) {
                for (Object o : components.entrySet()) {
                        Entry<String, Component> entry = (Entry<String, Component>) o;
                        Component c = entry.getValue();
                        if (c instanceof JComponent) {
                                JComponent comp = (JComponent) c;
                                // finding the panel
                                if (comp == selectJPanel) {
                                        Dimension d = c.getPreferredSize();
                                        int noOfButton = components.size() / 2;
                                        int noOfButtonAbove = components.size() / 2 - noOfButtonLeft;
                                        int noOfButtonBelow = noOfButtonLeft;

                                        if (currentY <= 0) {
                                                currentY = d.height;
                                        } else {
                                                if (currentY >= 1) {
                                                        currentY -= 1;
                                                } else {
                                                        currentY = 0;
                                                }
                                        }
                                        if (currentY <= 0) {
                                                //timer.stop();
                                                shouldStop = true;
                                        }
                                        c.setBounds(0, currentY, right - left, totalHeight - (noOfButton * buttonHeight) - separatorHeight);
                                        //                                      c.setSize(right - left, c.getHeight());
                                        //                                      top += d.height + 5;
                                        comp.setVisible(true);

                                        //                                      top = totalHeight - (buttonHeight * noOfButtonBelow);
                                } else if (comp instanceof JPanel) {
                                        c.setVisible(false);
                                }
                        }
                }
                return top;
        }

        /*private void drawOld() {
                synchronized (target.getTreeLock()) {
                        Insets insets = target.getInsets();
                        top = insets.top;
                        left = insets.left;
                        right = target.getWidth() - insets.right;
                        totalHeight = target.getHeight();
                        noOfButtonLeft = components.size() / 2;

                        for (Object o : components.entrySet()) {
                                Entry<String, Component> entry = (Entry<String, Component>) o;
                                Component c = entry.getValue();
                                if (c instanceof JComponent) {
                                        JComponent comp = (JComponent) c;
                                        if (c instanceof JButton) {
                                                Dimension d = c.getPreferredSize();
                                                c.setBounds(0, top, right - left, d.height);
                                                c.setSize(right - left, buttonHeight);
                                                top += buttonHeight;

                                                noOfButtonLeft--;
                                                if (comp.getClientProperty("selected") != null && (Boolean) comp.getClientProperty("selected")) {
                                                        top = layoutPanel((JButton) c, top);
                                                }
                                        }
                                }
                        }
                }
        }*/

        private int layoutPanelOld(JButton button, int top) {
                //timer.start();
                for (Object o : components.entrySet()) {
                        Entry<String, Component> entry = (Entry<String, Component>) o;
                        Component c = entry.getValue();
                        if (c instanceof JComponent) {
                                JComponent comp = (JComponent) c;
                                if (comp.getClientProperty("button") != null && comp.getClientProperty("button") == button) {
                                        Dimension d = c.getPreferredSize();
                                        int noOfButton = components.size() / 2;
                                        int noOfButtonAbove = components.size() / 2 - noOfButtonLeft;
                                        int noOfButtonBelow = noOfButtonLeft;

                                        c.setBounds(0, top, right - left, totalHeight - (noOfButton * buttonHeight));
                                        //                                      c.setSize(right - left, c.getHeight());
                                        //                                      top += d.height + 5;
                                        comp.setVisible(true);

                                        top = totalHeight - (buttonHeight * noOfButtonBelow);
                                } else if (comp instanceof JPanel) {
                                        c.setVisible(false);
                                }
                        }
                }
                return top;
        }

        public String toString() {
                return getClass().getName();
        }

        @Override
        public void run() {
                while (!shouldStop) {
                        draw();
                        try {
                                Thread.currentThread().sleep(0);
                                // sleep(0) is funny, but sleep (1) is too slow
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
        }

}

