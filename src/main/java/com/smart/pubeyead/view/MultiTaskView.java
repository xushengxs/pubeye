package com.smart.pubeyead.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MultiTaskView {
    public MultiTaskView() {

    }

    public void show() {
        JFrame frame = new JFrame("Smart Mini Tools");
        Container contentPane = frame.getContentPane();

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        JPanel incomeTaskPanel = (new IncomeCertificateView(null)).build();
        tabbedPane.addTab("收入证明", incomeTaskPanel);

        contentPane.add(tabbedPane);
        frame.setVisible(true);
        frame.setSize(450, 500);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            };
        });
    }
}
