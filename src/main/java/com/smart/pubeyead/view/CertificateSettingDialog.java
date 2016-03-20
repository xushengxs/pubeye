package com.smart.pubeyead.view;

import com.smart.pubeyead.utils.Constants;
import com.smart.pubeyead.utils.MiscUtils;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CertificateSettingDialog extends JDialog implements ActionListener {
    Properties prop;
    boolean update = false;
    JTextField tfExcelPath, tfPhotoPath, tfOutPath;
    JTextField tfFontHeader, tfFontBody;

    public CertificateSettingDialog(JFrame parent, Properties prop) {
        super(parent, "Setting Dialog", true);

        this.prop = prop;

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        ScrollPane panel = buildPropList();
        JPanel panelButtons = buildButtons();

        container.add(panel, BorderLayout.CENTER);
        container.add(panelButtons, BorderLayout.SOUTH);
        setSize(450, 450);
        //setUndecorated(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

//        this.addWindowListener(new WindowAdapter() {
//            public void windowClosed(WindowEvent e) {
//                update = true;
//                synchronized (CertificateSettingDialog.this) {
//                    CertificateSettingDialog.this.notify();
//                }
//            }
//        });

        setVisible(false);
    }

    ScrollPane buildPropList() {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;

        int lineNum = 1;
        buildLineWithLabelAndText(panel, layout, lineNum++, "联系人",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_CONTACT_PERSON),
                Constants.PROPERTY_CERTIFICATE_CONTACT_PERSON);
        buildLineWithLabelAndText(panel, layout, lineNum++, "联系电话",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_CONTACT_TELEPHONE),
                Constants.PROPERTY_CERTIFICATE_CONTACT_TELEPHONE);
        buildLineWithLabelAndText(panel, layout, lineNum++, "公司地址",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_COMPANY_ADDRESS),
                Constants.PROPERTY_CERTIFICATE_COMPANY_ADDRESS);

        tfFontHeader = buildLineWithButtonAndText(panel, layout, lineNum++, "标题字体",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_FONT_HEADER),
                Constants.PROPERTY_CERTIFICATE_FONT_HEADER);
//        buildLineWithLabelAndText(panel, layout, lineNum++, "标题字体",
//                prop.getProperty(Constants.PROPERTY_CERTIFICATE_FONT_HEADER),
//                Constants.PROPERTY_CERTIFICATE_FONT_HEADER);
        tfFontBody = buildLineWithButtonAndText(panel, layout, lineNum++, "内容字体",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_FONT_BODY),
                Constants.PROPERTY_CERTIFICATE_FONT_BODY);
//        buildLineWithLabelAndText(panel, layout, lineNum++, "内容字体",
//                prop.getProperty(Constants.PROPERTY_CERTIFICATE_FONT_BODY),
//                Constants.PROPERTY_CERTIFICATE_FONT_BODY);
        buildLineWithLabelAndText(panel, layout, lineNum++, "首行缩进",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_BODY_FIRST_LINE_INDENT),
                Constants.PROPERTY_CERTIFICATE_BODY_FIRST_LINE_INDENT);

        buildLineWithLabelAndText(panel, layout, lineNum++, "Windows图片命令",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_OPENIMAGE_WIN),
                Constants.PROPERTY_CERTIFICATE_OPENIMAGE_WIN);
        buildLineWithLabelAndText(panel, layout, lineNum++, "Windows Pdf命令",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_OPENPDF_WIN),
                Constants.PROPERTY_CERTIFICATE_OPENPDF_WIN);
        buildLineWithLabelAndText(panel, layout, lineNum++, "Linux图片命令",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_OPENIMAGE_LINUX),
                Constants.PROPERTY_CERTIFICATE_OPENIMAGE_LINUX);
        buildLineWithLabelAndText(panel, layout, lineNum++, "Linux Pdf命令",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_OPENPDF_LINUX),
                Constants.PROPERTY_CERTIFICATE_OPENPDF_LINUX);

        tfExcelPath = buildLineWithButtonAndText(panel, layout, lineNum++, "人员库",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_DATA_PATH),
                Constants.PROPERTY_CERTIFICATE_DATA_PATH);
        tfPhotoPath = buildLineWithButtonAndText(panel, layout, lineNum++, "照片库",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_PHOTO_PATH),
                Constants.PROPERTY_CERTIFICATE_PHOTO_PATH);
        tfOutPath = buildLineWithButtonAndText(panel, layout, lineNum++, "输出文件",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_OUTPUT_PATH),
                Constants.PROPERTY_CERTIFICATE_OUTPUT_PATH);

        String[] listBoxString = {
                "部门", Constants.PROPERTY_CERTIFICATE_HAS_DEPARTMENT,
                    Constants.PROPERTY_CERTIFICATE_HAS_DEPARTMENT,
                "职务", Constants.PROPERTY_CERTIFICATE_HAS_POSITION,
                    Constants.PROPERTY_CERTIFICATE_HAS_POSITION,
                "入职时间", Constants.PROPERTY_CERTIFICATE_HAS_ENTRY_DATE,
                    Constants.PROPERTY_CERTIFICATE_HAS_ENTRY_DATE,
                "健康状况", Constants.PROPERTY_CERTIFICATE_HAS_HEALTH,
                    Constants.PROPERTY_CERTIFICATE_HAS_HEALTH,
                "税后", Constants.PROPERTY_CERTIFICATE_USE_AFTER_TAX,
                    Constants.PROPERTY_CERTIFICATE_USE_AFTER_TAX,
                "年收入", Constants.PROPERTY_CERTIFICATE_USE_YEAR_INCOME,
                    Constants.PROPERTY_CERTIFICATE_USE_YEAR_INCOME
        };
        buildCheckBoxes(panel, layout, lineNum++, listBoxString);

        ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
        scrollPane.add(panel);
        scrollPane.setSize(400, 500);
        return scrollPane;
    }

    private void buildCheckBoxes(JPanel panel, GridBagLayout layout, int line, String[] checkBoxNames) {
        JPanel checkPanel = new JPanel();
        GridLayout checkLayout = new GridLayout(2, 3);
        checkPanel.setLayout(checkLayout);

        for(int i=0; i<checkBoxNames.length; i=i+3) {
            JCheckBox checkBox = new JCheckBox(checkBoxNames[i]);
            checkBox.setName(checkBoxNames[i+1]);
            if(MiscUtils.getPropBoolean(prop, checkBoxNames[i+2]))
                checkBox.setSelected(true);
            else
                checkBox.setSelected(false);
            checkPanel.add(checkBox);
        }

        panel.add(checkPanel);
        layout.setConstraints(checkPanel, new GridBagConstraints(
                1, line,   //int gridx,int gridy,
                6, 1,   //int gridwidth,int gridheight,
                0, 0,   //double weightx,double weighty,
                GridBagConstraints.CENTER,   //int anchor,
                GridBagConstraints.HORIZONTAL,     // int fill,
                new Insets(0,0,0,0),    // Insets insets,
                0, 0//int ipadx,int ipady
        ));
    }

    private void buildLineWithLabelAndText(JPanel panel, GridBagLayout layout, int line, String labelString, String defaultText, String textComponentName) {
        JLabel label = new JLabel(labelString);
        panel.add(label);
        layout.setConstraints(label, new GridBagConstraints(
                0, line,   //int gridx,int gridy,
                1, 1,   //int gridwidth,int gridheight,
                10, 0,   //double weightx,double weighty,
                GridBagConstraints.CENTER,   //int anchor,
                GridBagConstraints.NONE,     // int fill,
                new Insets(0,0,0,0),    // Insets insets,
                0, 0//int ipadx,int ipady
        ));

        JTextField text = new JTextField(defaultText);
        text.setName(textComponentName);
        panel.add(text);
        layout.setConstraints(text, new GridBagConstraints(
                2, line,   //int gridx,int gridy,
                4, 1,   //int gridwidth,int gridheight,
                50, 0,   //double weightx,double weighty,
                GridBagConstraints.CENTER,   //int anchor,
                GridBagConstraints.HORIZONTAL,     // int fill,
                new Insets(0,0,0,0),    // Insets insets,
                0, 0//int ipadx,int ipady
        ));
    }

    private JTextField buildLineWithButtonAndText(JPanel panel, GridBagLayout layout, int line, String buttonString, String defaultText, String textComponentName) {
        JButton bt = new JButton(buttonString);
        panel.add(bt);
        bt.addActionListener(this);
        layout.setConstraints(bt, new GridBagConstraints(
                0, line,   //int gridx,int gridy,
                1, 1,   //int gridwidth,int gridheight,
                30, 0,   //double weightx,double weighty,
                GridBagConstraints.CENTER,   //int anchor,
                GridBagConstraints.NONE,     // int fill,
                new Insets(0,0,0,0),    // Insets insets,
                0, 0//int ipadx,int ipady
        ));

        JTextField text = new JTextField(defaultText);
        text.setName(textComponentName);
        panel.add(text);
        layout.setConstraints(text, new GridBagConstraints(
                2, line,   //int gridx,int gridy,
                5, 1,   //int gridwidth,int gridheight,
                50, 0,   //double weightx,double weighty,
                GridBagConstraints.CENTER,   //int anchor,
                GridBagConstraints.HORIZONTAL,     // int fill,
                new Insets(0,0,0,0),    // Insets insets,
                0, 0//int ipadx,int ipady
        ));

        return text;
    }

    public void actionPerformed(ActionEvent e) {
        String actionCmd = e.getActionCommand();
        if(actionCmd.equals("人员库") || actionCmd.equals("照片库") || actionCmd.equals("输出文件")) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setApproveButtonText("确定");
            fileChooser.setDialogTitle("Select File");
            int result = fileChooser.showOpenDialog(this);
            if(result == JFileChooser.APPROVE_OPTION) {
                if(actionCmd.equals("人员库") ) {
                    tfExcelPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
                if(actionCmd.equals("照片库") ) {
                    tfPhotoPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
                if(actionCmd.equals("输出文件") ) {
                    tfOutPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
            return;
        }

        if(actionCmd.equals("标题字体") || actionCmd.equals("内容字体")) {
            JFontDialog dlg = new JFontDialog(this);
            Font x = dlg.openDialog();
            String y = x.getFontName() + MiscUtils.buildFontStyleDescription(x.getStyle()) + "," + x.getSize();
            if(actionCmd.equals("标题字体")) {
                tfFontHeader.setText(y);
            }
            if(actionCmd.equals("内容字体")) {
                tfFontBody.setText(y);
            }
            return;
        }
    }

    JPanel buildButtons() {
        JButton buttonok = new JButton("确定");
        buttonok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                collectProps();
//                if(!propOrig.equals(prop)) {
//                    update = true;
//                    propOrig = (Properties) prop.clone();
//                }
                //JFontDialog.this.setVisible(false);
                //CertificateSettingDialog.this.notify();
                dispose();
            }
        });

        JButton buttoncancel = new JButton("取消");
        buttoncancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //JFontDialog.this.setVisible(false);
                //CertificateSettingDialog.this.notify();
                dispose();
            }
        });

        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new FlowLayout());
        panelButtons.add(buttonok);
        panelButtons.add(buttoncancel);

        return panelButtons;
    }

    void collectProps() {
        Container container = getContentPane();
        JPanel tgtPanel = null;
        boolean found = false;
        for(Component component: container.getComponents()) {
            if (component instanceof ScrollPane) {
                for (Component Xcomponent : ((ScrollPane) component).getComponents()) {
                    tgtPanel = (JPanel) ((Panel) Xcomponent).getComponents()[0];
                    found = true;
                    break;
                }
            }
            if(found)
                break;
        }

        for (Component Xcomponent : tgtPanel.getComponents()) {
            if(Xcomponent instanceof  JTextField) {
                String cmpName = Xcomponent.getName();
                prop.setProperty(cmpName, ((JTextField) Xcomponent).getText());
            }
            if (Xcomponent instanceof JPanel) {
                for(Component XXcomponent : ((JPanel) Xcomponent).getComponents()) {
                    if (XXcomponent instanceof JCheckBox) {
                        String cmpName = XXcomponent.getName();
                        prop.setProperty(cmpName, Boolean.toString(((JCheckBox) XXcomponent).isSelected()));
                    }
                }
            }
        }
    }
/*
    public boolean openDialog() {
        this.setVisiblerue);

        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return update;
    }
    */
    public Properties openDialog() {
        this.setVisible(true);
/*
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/
        return prop;
    }

}
