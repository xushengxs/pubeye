package com.smart.pubeyead.controller;

import com.smart.pubeyead.model.IncomeModel;
import com.smart.pubeyead.utils.Constants;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.Properties;

public class IncomeController {

    Properties prop = null;
    IncomeModel model = null;
    String errMsg = "";

    public IncomeController(Properties prop) {
        this.prop = prop;
        model = new IncomeModel(prop);
    }

    public Map<String,String> getPersonelInfo(Map<String,String> args) {
        String excelPath = args.get(Constants.TAG_EXCEL_FILE);
        String id = args.get(Constants.TAG_ID);
        Map<String,String> person = model.getPerson(excelPath, id);

        return person;
    }

    public int generateCertificate(Map<String,String> args) {
        return 0;
    }

    public String searchFile(String photoPath, String id) {
        String tgt = null;
        File fileTarget = new File(photoPath);
        if(!fileTarget.isDirectory()) {
            errMsg = "照片设置不是目录";
            return null;
        }
        File[] fileLogs = fileTarget.listFiles();
        for(File x : fileLogs) {
            if(x.isDirectory())
                continue;
            String[] parts = x.getName().split(".");
            if(id.equals(parts[0])) {
                tgt = x.getName();
                break;
            }
        }
        return tgt;
    }

    public int selectPhoto(Map<String,String> args){
        int ret = 0;
        String id = args.get(Constants.TAG_ID);
        String photoPath = args.get(Constants.TAG_PHOTO_PATH);
        String photoFile = searchFile(photoPath, id);
        if(photoFile==null) {
            errMsg = "没有数据文件";
            return -1;
        }

        String os = System.getProperties().getProperty("os.name");
        String cmd = prop.getProperty(Constants.PROPERTY_CERTIFICATE_OPENIMAGE_WIN);
        if(!os.startsWith("win") && !os.startsWith("Win")) {
            cmd = prop.getProperty(Constants.PROPERTY_CERTIFICATE_OPENIMAGE_LINUX);
        }
        String fullPath = photoPath + System.getProperties().getProperty("file.separator") + photoFile;

        Runtime run = Runtime.getRuntime();
        try {
            Process p = run.exec(cmd + " " + fullPath);
            if (p.waitFor() != 0) {
                if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束
                    ret = -1;
            }
        } catch(Exception e) {
            ret = -1;
            errMsg = e.getMessage();
        }
        //"rundll32.exe" C:\WINDOWS\system32\shimgvw.dll,ImageView_Fullscreen C:\test.gif
        //rundll32.exe %Systemroot%System32\shimgvw.dll,ImageView_Fullscreen
        //@"C:\WINDOWS\system32\mspaint.exe",@"D:\\搜索sample.jpg");
        //eog 1.jpg
        return ret;
    }

    public String getLatestError() {
        return errMsg;
    }
}
