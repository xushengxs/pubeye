package com.smart.pubeyead.utils;

public class SystemCmdUtils {
    static public String errMsg = "";

    static public int runCmd(String cmd, boolean backgroud) {
        int ret = 0;

//        if(backgroud) {
//            String os = System.getProperties().getProperty("os.name");
//            if(os.startsWith("win") || os.startsWith("Win")) {
//                cmd = "start /b " + cmd;
//            } else {
//                cmd = cmd;
//            }
//        }

        try {
            Runtime run = Runtime.getRuntime();
            Process p = run.exec(cmd);
//            if (p.waitFor() != 0) {
//                if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束
//                    ret = -1;
//            }
        } catch(Exception e) {
            ret = -1;
            errMsg = e.getMessage();
        }
        return ret;
    }

    static public String getLatestError() {
        return errMsg;
    }
}
