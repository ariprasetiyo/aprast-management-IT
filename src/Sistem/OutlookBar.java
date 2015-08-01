/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OutlookBar extends JPanel {
        OutlookBarLayout layout = new OutlookBarLayout();
        Image separatorImage = new ImageIcon(OutlookBar.class.getResource("add.png")).getImage();

        SelectedButtonColor selectedColor = SelectedButtonColor.outlookBarColorBlue;

    public void setLayout(String CENTER) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

        public static enum SelectedButtonColor {
                outlookBarColorYellow, outlookBarColorBlue, outlookBarColorRed, outlookBarColorGreen
        }

        public OutlookBar() {
                this.setLayout(layout);
        }

        public void setSelectedButtonColor(SelectedButtonColor color) {
                for (Object o : layout.components.entrySet()) {
                        Entry<String, Component> entry = (Entry<String, Component>) o;
                        if (entry.getValue() instanceof JButton) {
                                JButton b = (JButton) entry.getValue();

                                if (color == SelectedButtonColor.outlookBarColorYellow) {
                                        b.putClientProperty("buttonType", "outlookBarColorYellow");
                                } else if (color == SelectedButtonColor.outlookBarColorBlue) {
                                        b.putClientProperty("buttonType", "outlookBarColorBlue");
                                } else if (color == SelectedButtonColor.outlookBarColorRed) {
                                        b.putClientProperty("buttonType", "outlookBarColorRed");
                                } else if (color == SelectedButtonColor.outlookBarColorGreen) {
                                        b.putClientProperty("buttonType", "outlookBarColorGreen");
                                }
                                selectedColor = color;
                        }
                }
        }

        public void add(Component comp, Object constraints) {
                if (comp instanceof JPanel) {
                        if (layout.components.size() == 0) {
                                JLabel separator = new JLabel() {
                                        @Override
                                        public void paintComponent(Graphics g) {
                                                super.paintComponent(g);
                                                g.drawImage(((ImageIcon) getIcon()).getImage(), 0, 0, getWidth(), getHeight(), null);
                                        }
                                };
                                separator.setIcon(new ImageIcon(separatorImage));
                                super.addImpl(separator, "separator", -1);
                        }

                        final JButton button = new JButton((String) constraints);
                        if (selectedColor == SelectedButtonColor.outlookBarColorYellow) {
                                button.putClientProperty("buttonType", "outlookBarColorYellow");
                        } else if (selectedColor == SelectedButtonColor.outlookBarColorBlue) {
                                button.putClientProperty("buttonType", "outlookBarColorBlue");
                        } else if (selectedColor == SelectedButtonColor.outlookBarColorRed) {
                                button.putClientProperty("buttonType", "outlookBarColorRed");
                        } else if (selectedColor == SelectedButtonColor.outlookBarColorGreen) {
                                button.putClientProperty("buttonType", "outlookBarColorGreen");
                        }
                        ((JComponent) comp).putClientProperty("button", button);
                        super.addImpl(comp, constraints, -1);
                        super.addImpl(button, constraints + "-button", -1);
                        button.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                        setSelectedButton(button);
                                }
                        });
                } else {
                        throw new IllegalArgumentException("add JPanel only");
                }
        }

        private void setSelectedButton(JButton button) {
                layout.setSelectedButton(button);
                this.revalidate();
        }

        public void setSelectedComponent(Component c) {
                layout.setSelectedComponent(c);
                this.revalidate();
        }

        public JButton getJButton(String constraints) {
                for (Object o : layout.components.entrySet()) {
                        Entry<String, Component> entry = (Entry<String, Component>) o;
                        if (entry.getValue() instanceof JButton) {
                                JButton b = (JButton) entry.getValue();
                                if (b.getText().equals(constraints)) {
                                        return b;
                                }
                        }
                }
                return null;
        }
}
