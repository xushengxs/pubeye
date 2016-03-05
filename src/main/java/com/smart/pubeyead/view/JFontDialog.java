package com.smart.pubeyead.view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class JFontDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    JList fontpolics, fontstyle, fontsize, fontcolor;
    JTextField fontpolict, fontstylet, fontsizet, fontcolort;
    String example;
    JLabel FontResolvent;
    JButton buttonok, buttoncancel;
    Color[] colors = { Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY,
            Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW };

    private JDialog frame;
    Font resultFont;

//    public JFontDialog(JDialog owner, String title, boolean modal) {
      public JFontDialog() {
        //super(owner, title, modal);
        super();
        setTitle("Font Dialog");
        setModal(true);
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        //this.frame = owner;

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        JPanel FontSet, FontView;
        FontSet = new JPanel(new GridLayout(1, 4));
        FontView = new JPanel(new GridLayout(1, 2));
        example = "AaBbCcDdEe";
        FontResolvent = new JLabel(example, JLabel.CENTER);
        FontResolvent.setBackground(Color.WHITE);

        JPanel Fontpolic = buildFonts();
        JPanel Fontstyle = buildStyle();
        JPanel Fontsize = buildSize();
        JPanel Fontcolor = buildColor();

        FontSet.add(Fontpolic);
        FontSet.add(Fontstyle);
        FontSet.add(Fontsize);
        FontSet.add(Fontcolor);

        FontView.add(FontResolvent);
        panel.add(FontSet);
        panel.add(FontView);

        buttonok = new JButton("确定");
        buttoncancel = new JButton("取消");
        buttonok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.testLabel.setFont(FontResolvent.getFont());
                //frame.testLabel.setForeground(FontResolvent.getForeground());
                resultFont = FontResolvent.getFont();
                //JFontDialog.this.setVisible(false);
                dispose();
            }
        });
        buttoncancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resultFont = null;
                //JFontDialog.this.setVisible(false);
                dispose();
            }
        });

        JPanel panelnorth = new JPanel();
        panelnorth.setLayout(new FlowLayout());
        panelnorth.add(buttonok);
        panelnorth.add(buttoncancel);

        container.add(panel, BorderLayout.CENTER);
        container.add(panelnorth, BorderLayout.SOUTH);
        setSize(400, 300);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                synchronized (JFontDialog.this) {
                    JFontDialog.this.notify();
                }
            }
        });

        //this.setVisible(true);
    }

    JPanel buildFonts() {

        JPanel Fontpolic = new JPanel();
        Border border = BorderFactory.createLoweredBevelBorder();
        border = BorderFactory.createTitledBorder(border, "字体");
        Fontpolic.setBorder(border);

//        Font[] fonts = java.awt.GraphicsEnvironment
//                .getLocalGraphicsEnvironment().getAllFonts();
//        int taille = fonts.length;
//        String[] policnames = new String[taille];
//        for (int i = 0; i < taille; i++) {
//            policnames[i] = fonts[i].getName();
//        }
        String[] policnames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        fontpolict = new JTextField(policnames[0]);
        fontpolict.setName("polic");
        fontpolict.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                        FontResolvent.setFont(new Font(fontpolict.getText(),
                                FontResolvent.getFont().getStyle(),
                                FontResolvent.getFont().getSize()));
                }
            }
            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });

        fontpolics = new JList(policnames);
        fontpolics.setName("polic");
        fontpolics.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontpolics.setVisibleRowCount(6);
        fontpolics.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                    fontpolict.setText((String) fontpolics.getSelectedValue());
                    FontResolvent.setFont(new Font(fontpolict.getText(),
                            FontResolvent.getFont().getStyle(),
                            FontResolvent.getFont().getSize()));
            }
        });


        JScrollPane jspfontpolic = new JScrollPane(fontpolics);
        Fontpolic.setLayout(new BoxLayout(Fontpolic, BoxLayout.PAGE_AXIS));
        Fontpolic.add(fontpolict);
        Fontpolic.add(jspfontpolic);

        return Fontpolic;
    }

    JPanel buildStyle() {
        JPanel Fontstyle = new JPanel();
        Border border = BorderFactory.createLoweredBevelBorder();
        border = BorderFactory.createTitledBorder(border, "样式");
        Fontstyle.setBorder(border);

        String[] styles = { "PLAIN", "BOLD", "ITALIC", "BOLD ITALIC" };

        fontstylet = new JTextField(styles[0]);
        fontstylet.setName("style");
        fontstylet.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                        fontstylet.setText((String) fontstyle.getSelectedValue());
                        FontResolvent.setFont(new Font(FontResolvent.getFont().getFontName(),
                                fontstyle.getSelectedIndex(),
                                FontResolvent.getFont().getSize()));
                }
            }
            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });

        fontstyle = new JList(styles);
        fontstyle.setName("style");
        fontstyle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontstyle.setVisibleRowCount(6);
        fontstyle.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                fontstylet.setText((String) fontstyle.getSelectedValue());
                FontResolvent.setFont(new Font(FontResolvent.getFont().getFontName(),
                        fontstyle.getSelectedIndex(),
                        FontResolvent.getFont().getSize()));
            }
        });

        JScrollPane jspfontstyle = new JScrollPane(fontstyle);
        Fontstyle.setLayout(new BoxLayout(Fontstyle, BoxLayout.PAGE_AXIS));
        Fontstyle.add(fontstylet);
        Fontstyle.add(jspfontstyle);

        return Fontstyle;
    }

    JPanel buildSize() {
        JPanel Fontsize = new JPanel();
        Border border = BorderFactory.createLoweredBevelBorder();
        border = BorderFactory.createTitledBorder(border, "大小");
        Fontsize.setBorder(border);

        String[] sizes = { "10", "14", "18", "22", "26", "30" };
        fontsizet = new JTextField(sizes[0]);
        fontsizet.setName("size");
        fontsizet.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                        try {
                            Integer.parseInt(fontsizet.getText());
                        } catch (Exception excepInt) {
                            fontsizet.setText(FontResolvent.getFont().getSize()+ "");
                        }
                        FontResolvent.setFont(new Font(FontResolvent.getFont().getFontName(),
                                FontResolvent.getFont().getStyle(),
                                Integer.parseInt(fontsizet.getText())));
                }
            }
            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });

        fontsize = new JList(sizes);
        fontsize.setName("size");
        fontsize.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontsize.setVisibleRowCount(6);
        fontsize.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                    fontsizet.setText((String) fontsize.getSelectedValue());
                    try {
                        Integer.parseInt(fontsizet.getText());
                    } catch (Exception excepInt) {
                        fontsizet.setText(FontResolvent.getFont().getSize()+ "");
                    }
                    FontResolvent.setFont(new Font(FontResolvent.getFont().getFontName(),
                            FontResolvent.getFont().getStyle(),
                            Integer.parseInt(fontsizet.getText())));
                }
        });


        JScrollPane jspfontsize = new JScrollPane(fontsize);
        Fontsize.setLayout(new BoxLayout(Fontsize, BoxLayout.PAGE_AXIS));
        Fontsize.add(fontsizet);
        Fontsize.add(jspfontsize);

        return Fontsize;
    }

    JPanel buildColor() {
        JPanel Fontcolor = new JPanel();
        Border bordercolor = BorderFactory.createLoweredBevelBorder();
        bordercolor = BorderFactory.createTitledBorder(bordercolor, "颜色");
        Fontcolor.setBorder(bordercolor);

        final String[] colornames = { "BLACK", "BLUE", "CYAN", "DARK_GRAY", "GRAY",
                "GREEN", "LIGHT_GRAY", "MAGENTA", "ORANGE", "PINK", "RED", "WHITE",
                "YELLOW" };

        fontcolort = new JTextField(colornames[0].toString());
        fontcolort.setName("color");
        //fontcolort.addKeyListener(keyListener);

        fontcolor = new JList(colornames);
        fontcolor.setName("color");
        fontcolor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontcolor.setVisibleRowCount(6);
        fontcolor.addListSelectionListener( new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                    fontcolort.setText(colornames[fontcolor.getSelectedIndex()]);
                    FontResolvent.setForeground(colors[fontcolor.getSelectedIndex()]);
            }
        });

        JScrollPane jspfontcolor = new JScrollPane(fontcolor);
        Fontcolor.setLayout(new BoxLayout(Fontcolor, BoxLayout.PAGE_AXIS));
        Fontcolor.add(fontcolort);
        Fontcolor.add(jspfontcolor);

        return Fontcolor;
    }

    public Font openDialog() {
        this.setVisible(true);
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return resultFont;
    }
}
