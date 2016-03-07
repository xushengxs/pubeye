package com.smart.pubeyead.utils;

import java.io.*;
import java.util.Properties;

public class PropertyOperator {
    static private String lastErrMsg = "";
    static public Properties readProperties(String filePath) {
        try {
            InputStream ins = new BufferedInputStream(new FileInputStream(filePath));
            Properties props = new Properties();
            props.load(ins);
            ins.close();
        } catch (Exception e) {
            lastErrMsg = e.getMessage();
        }
        return null;
    }

    static public boolean writeProperties(Properties prop, String filePath) {
        try {
            OutputStream fos = new FileOutputStream(filePath);
            prop.store(fos, "update @ ");
            fos.close();
        } catch(Exception e ) {
            lastErrMsg = e.getMessage();
        }
        return false;
    }

    static public String getLastError() {
        return lastErrMsg;
    }
}
