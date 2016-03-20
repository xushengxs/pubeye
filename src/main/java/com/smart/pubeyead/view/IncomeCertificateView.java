package com.smart.pubeyead.view;

import com.smart.pubeyead.controller.IncomeController;
import com.smart.pubeyead.utils.Constants;
import com.smart.pubeyead.utils.MiscUtils;
import com.smart.pubeyead.utils.PropertyOperator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class IncomeCertificateView {
    Properties prop = null;
    IncomeController controller = null;
    JPanel panel = new JPanel();
    JFrame parent = null;

    public IncomeCertificateView(JFrame parent, Properties prop) {
        this.parent = parent;
        this.prop = prop;
        controller = new IncomeController(prop);
    }

    public JPanel build() {
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;

        //GridBagConstraints
        //int gridx,int gridy,
        // 设置组件的位置，
        //　　　　 gridx设置为GridBagConstraints.RELATIVE代表此组件位于之前所加入组件的右边。
        //　　　　 gridy设置为GridBagConstraints.RELATIVE代表此组件位于以前所加入组件的下面。
        //　　　　 建议定义出gridx,gridy的位置以便以后维护程序。gridx=0,gridy=0时放在0行0列。
        //int gridwidth,int gridheight,
        // 用来设置组件所占的单位长度与高度，默认值皆为1。
        // 可以使用GridBagConstraints.REMAINDER常量，代表此组件为此行或此列的最后一个组件，而且会占据所有剩余的空间。
        //double weightx,double weighty,
        // 用来设置窗口变大时，各组件跟着变大的比例。
        //　当数字越大，表示组件能得到更多的空间，默认值皆为0。
        //int anchor,
        //当组件空间大于组件本身时，要将组件置于何处。
        //　有CENTER(默认值)、NORTH、NORTHEAST、EAST、SOUTHEAST、WEST、NORTHWEST选择。
        // int fill,
        //该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
        //NONE：不调整组件大小。
        //HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。
        //VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。
        //BOTH：使组件完全填满其显示区域。
        // Insets insets,
        //设置组件之间彼此的间距。
        //　它有四个参数，分别是上，左，下，右，默认为(0,0,0,0)。
        //int ipadx,int ipady
        //内部填充，是指在组件首选大小的基础上x方向上加上ipadx，y方向上加上ipady,
        // 这样做就可以保证组件不会收缩到ipadx,ipady所确定的大小以下，
        // 因此可以用ipadx,ipady的值来指定组件的大小，而不必指定组件的大小否则会有意想不到的效果
        int lineNum = 1;
        buildLineWithLabelAndText(panel, layout, lineNum++, "员工编号", "", Constants.TAG_ID);
        buildLineWithLabelAndText(panel, layout, lineNum++, "联系人",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_CONTACT_PERSON), Constants.TAG_CONTACT_PERSON);
        buildLineWithLabelAndText(panel, layout, lineNum++, "联系电话",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_CONTACT_TELEPHONE), Constants.TAG_CONTACT_TELEPHONE);
        buildLineWithLabelAndText(panel, layout, lineNum++, "公司地址",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_COMPANY_ADDRESS), Constants.TAG_COMPANY_ADRESS);
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String ending = format.format(new Date());
            buildLineWithLabelAndText(panel, layout, lineNum++, "日期",
                    ending, Constants.TAG_CERTIFICATE_DATE);
        }
        buildLineWithLabelAndText(panel, layout, lineNum++, "姓名", "", Constants.TAG_NAME);
        buildLineWithLabelAndText(panel, layout, lineNum++, "收入", "", Constants.TAG_YEAR_INCOME);
        buildLineWithLabelAndText(panel, layout, lineNum++, "职务", "", Constants.TAG_POSITION);
        buildLineWithLabelAndText(panel, layout, lineNum++, "入司时间", "", Constants.TAG_ENTRY_DATE);
        buildLineWithLabelAndText(panel, layout, lineNum++, "对方公司", "", Constants.TAG_TO_COMPANY);

        String[] listBoxString = {
                "部门", Constants.TAG_HAS_DEPARTMENT, Constants.PROPERTY_CERTIFICATE_HAS_DEPARTMENT,
                "职务", Constants.TAG_HAS_POSITION, Constants.PROPERTY_CERTIFICATE_HAS_POSITION,
                "入职时间", Constants.TAG_HAS_ENTRY_DATE, Constants.PROPERTY_CERTIFICATE_HAS_ENTRY_DATE,
                "健康状况", Constants.TAG_HAS_HEALTH, Constants.PROPERTY_CERTIFICATE_HAS_HEALTH,
                "税后", Constants.TAG_USE_AFTER_TAX, Constants.PROPERTY_CERTIFICATE_USE_AFTER_TAX,
                "年收入", Constants.TAG_USE_YEAR_INCOME, Constants.PROPERTY_CERTIFICATE_USE_YEAR_INCOME
        };
        buildCheckBoxes(panel, layout, lineNum++, listBoxString);

        buildLineWithLabelAndText(panel, layout, lineNum++, "人员库",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_DATA_PATH), Constants.TAG_EXCEL_FILE);
        buildLineWithLabelAndText(panel, layout, lineNum++, "照片库",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_PHOTO_PATH), Constants.TAG_PHOTO_PATH);
        buildLineWithLabelAndText(panel, layout, lineNum++, "输出文件",
                prop.getProperty(Constants.PROPERTY_CERTIFICATE_OUTPUT_PATH), Constants.TAG_OUTPUT_PATH);

        buildButtons(panel, layout, lineNum++);

        return panel;
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
                1, line,   //int gridx,int gridy,
                5, 1,   //int gridwidth,int gridheight,
                50, 0,   //double weightx,double weighty,
                GridBagConstraints.CENTER,   //int anchor,
                GridBagConstraints.HORIZONTAL,     // int fill,
                new Insets(0,0,0,0),    // Insets insets,
                0, 0//int ipadx,int ipady
        ));
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

    private void buildButtons(JPanel panel, GridBagLayout layout, int line) {
        JPanel checkPanel = new JPanel();
        GridLayout btLayout = new GridLayout(1, 4);
        checkPanel.setLayout(btLayout);

        JButton buttonView = new JButton("查看");
        checkPanel.add(buttonView);
        buttonView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doView();
            }
        });

        JButton buttonCertificate = new JButton("收入证明");
        checkPanel.add(buttonCertificate);
        buttonCertificate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doCertificate();
            }
        });

        JButton buttonPhoto = new JButton("证件提取");
        checkPanel.add(buttonPhoto);
        buttonPhoto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doPhoto();
            }
        });

        JButton buttonSetting = new JButton("缺省设置");
        checkPanel.add(buttonSetting);
        buttonSetting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSetting();
            }
        });

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

    private Map<String, String> buildAllMap() {
        Map<String, String> args = new HashMap<String, String>();
        for(Component component: panel.getComponents()) {
            if(component instanceof JTextField) {
                String cmpName = component.getName();
                args.put(cmpName, ((JTextField) component).getText());
            }
            if((component instanceof JPanel)) {
                for (Component Xcomponent : ((JPanel) component).getComponents()) {
                    if (Xcomponent instanceof JCheckBox) {
                        String cmpName = Xcomponent.getName();
                        args.put(cmpName, Boolean.toString(((JCheckBox) Xcomponent).isSelected()));
                    }
                }
            }
        }
        return args;
    }

    private void updateTextFields(Map<String, String> result) {
        for(Component component: panel.getComponents()) {
            if(!(component instanceof JTextField))
                continue;

            if (component instanceof JTextField) {
                String cmpName = component.getName();
                String val = result.get(cmpName);
                if (val != null) {
                    ((JTextField) component).setText(val);
                }
            }
        }
    }

    private void doView() {
        Map<String, String> args = buildAllMap();

        Map<String, String> result = controller.getPersonelInfo(args);
        if(result == null) {
            JOptionPane.showMessageDialog(null, "找不到人", "错误",JOptionPane.ERROR_MESSAGE);
            return;
        }

        updateTextFields(result);
    }

    private void doCertificate() {
        Map<String, String> args = buildAllMap();

        int ret = controller.generateCertificate(args);
        String msg = "成功输出";
        if(ret != 0) {
            msg = controller.getLatestError();
        }
        JOptionPane.showMessageDialog(null, msg, "提示",JOptionPane.ERROR_MESSAGE);
    }

    private void doPhoto() {
        Map<String, String> args = buildAllMap();
        int ret = controller.selectPhoto(args);
        if(ret != 0) {
            String msg = controller.getLatestError();
            JOptionPane.showMessageDialog(null, msg, "提示",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doSetting() {
        CertificateSettingDialog dlg = new CertificateSettingDialog(parent, prop);
//        boolean update = dlg.openDialog();

        Properties propMod = dlg.openDialog();
        boolean update = propMod.equals(prop);
        if(update) {
            String propFile = prop.getProperty(Constants.PROPERTY_FILE);
            PropertyOperator.writeProperties(propMod, propFile);
            prop = (Properties) propMod.clone();
        }

        return;
    }
}
