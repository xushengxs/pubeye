package com.smart.pubeyead.view;

import com.smart.pubeyead.controller.IncomeController;
import com.smart.pubeyead.model.IncomeModel;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class IncomeCertificateView {
    Properties prop = null;
    IncomeModel model = null;
    IncomeController controller = null;

    public IncomeCertificateView(Properties prop) {
        this.prop = prop;
        model = new IncomeModel();
        controller = new IncomeController();
    }

    public JPanel build() {
        JPanel panel = new JPanel();
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

        buildLineWithLabelAndText(panel, layout, 1, "员工编号", "");
        buildLineWithLabelAndText(panel, layout, 2, "联系人", "");
        buildLineWithLabelAndText(panel, layout, 3, "联系电话", "");
        buildLineWithLabelAndText(panel, layout, 4, "公司地址", "");
        buildLineWithLabelAndText(panel, layout, 5, "日期", "");
        buildLineWithLabelAndText(panel, layout, 6, "姓名", "");
        buildLineWithLabelAndText(panel, layout, 7, "收入", "");
        buildLineWithLabelAndText(panel, layout, 8, "职务", "");
        buildLineWithLabelAndText(panel, layout, 9, "入司时间", "");
        buildLineWithLabelAndText(panel, layout, 10, "对方公司", "");

        String[] listBoxString = {"部门", "职务", "入职时间", "健康状况",  "公司地址",  "年收入"};
        buildCheckBoxes(panel, 10, listBoxString);

        buildLineWithLabelAndText(panel, layout, 11, "人员库", "");
        buildLineWithLabelAndText(panel, layout, 12, "照片库", "");
        buildLineWithLabelAndText(panel, layout, 13, "输出文件", "");

        buildButtonView("查看", 14);
        buildButtonCertificate("收入证明", 14);
        buildButtonPhoto("证件提取", 14);

        return panel;
    }

    private void buildLineWithLabelAndText(JPanel panel, GridBagLayout layout, int line, String labelString, String defaultText) {
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

    private void buildCheckBoxes(JPanel panel, int line, String[] checkBoxNames) {

    }

    private void buildButtonView(String name, int line) {

    }

    private void buildButtonCertificate(String name, int line) {

    }

    private void buildButtonPhoto(String name, int line) {

    }
}
