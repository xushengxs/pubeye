package com.smart.pubeyead;

public class PubEyeAd {
    static public void main(String[] args) {
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
