package com.smart.pubeyead;

import com.smart.pubeyead.view.JFontDialog;
import com.smart.pubeyead.view.MultiTaskView;

import java.awt.*;

public class PubEyeAd {
    static public void main(String[] args) {
        if(true) {
            //MultiTaskView mainWindow = new MultiTaskView();
            //mainWindow.show();

            JFontDialog fdlg = new JFontDialog();
            Font x = fdlg.openDialog();
            System.out.println(x.getName());
            System.out.println(x.getSize());
            System.out.println(x.getStyle());
            return;
        }

        try {
            String pdfFile = "/home/foobar/temp/rub/1.pdf";
            String excelFile = "/media/sf_vms_share/rub/abc.xls";
            IncomeListCreater creater = new IncomeListCreater(pdfFile, excelFile);

            int ret_val = creater.buildArchive("01010008");
            if(ret_val!=0) {
                System.out.println(creater.getErrMsg());
            }

            creater.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        return;
    }
}
