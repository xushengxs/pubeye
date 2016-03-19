package com.smart.pubeyead.controller;

import com.itextpdf.text.*;
import com.smart.pubeyead.model.IncomeModel;
import com.smart.pubeyead.utils.Constants;
import com.smart.pubeyead.utils.MiscUtils;
import com.smart.pubeyead.utils.SmartPdfCreater;
import com.smart.pubeyead.utils.SystemCmdUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private void pdfPageSetting(SmartPdfCreater writer) {
        Document document = writer.getDocument();

        document.setPageSize(PageSize.A4);
        // unit: pixel
        //document.setMargins(1.0f, 1.0f, 1.0f, 1.0f);
        return;
    }

    private void writePdfHeader(SmartPdfCreater writer, Map<String,String> args, Map<String,String> person) throws Exception {
        Document document = writer.getDocument();
        Font curFont = SmartPdfCreater.getFontFollowChinese(prop.getProperty(Constants.PROPERTY_CERTIFICATE_FONT_HEADER));

        String titleString = "个人经济收入证明";
        Paragraph p0 = new Paragraph(titleString, curFont);
        p0.setAlignment(TextElementArray.ALIGN_CENTER);
        document.add(p0);

        return;
    }


    private void writePdfBody(SmartPdfCreater writer, Map<String,String> args, Map<String,String> person) throws Exception  {
        Document document = writer.getDocument();
        Font curFont = SmartPdfCreater.getFontFollowChinese(prop.getProperty(Constants.PROPERTY_CERTIFICATE_FONT_BODY));
        String indentStr = prop.getProperty(Constants.PROPERTY_CERTIFICATE_BODY_FIRST_LINE_INDENT, "20");
        float firstLineIndent = Float.parseFloat(indentStr);

        Paragraph p0 = new Paragraph(args.get(Constants.TAG_TO_COMPANY)+":", curFont);
        p0.setAlignment(TextElementArray.ALIGN_LEFT);
        document.add(p0);

        String body = String.format("兹证明%s（身份证：%s）为本单位正式职工",
                person.get(Constants.TAG_NAME),
                person.get(Constants.TAG_IDCARD));
        if(MiscUtils.getMapBoolean(args, Constants.TAG_HAS_ENTRY_DATE)) {
            String date_cn = MiscUtils.ConvertDateToCn(person.get(Constants.TAG_ENTRY_DATE));
            body += String.format("，该同志于%s入司", date_cn);
        }
        if(MiscUtils.getMapBoolean(args, Constants.TAG_HAS_DEPARTMENT)) {
            body += String.format("，所在部门%s", person.get(Constants.TAG_DEPARTMENT));
        }
        if(MiscUtils.getMapBoolean(args, Constants.TAG_HAS_POSITION)) {
            body += String.format("，职务为%s", person.get(Constants.TAG_POSITION));
        }
        boolean useAfterTax = Boolean.parseBoolean(prop.getProperty(Constants.PROPERTY_CERTIFICATE_USE_AFTER_TAX, "false"));
        String taxAdd = "税后";
        String y = null;
        if(MiscUtils.getMapBoolean(args, Constants.TAG_USE_YEAR_INCOME)) {
            if(useAfterTax) {
                y =  person.get(Constants.TAG_YEAR_INCOME_AFTER_TAX);
            } else {
                y =  person.get(Constants.TAG_YEAR_INCOME);
            }
            String x = MiscUtils.ConvertInteger(y);
            body += String.format("，该同志近1年%s收入为%s元人民币（大写：%s）",
                    taxAdd, x, MiscUtils.digitUppercase(x));
        } else {
            if(useAfterTax) {
                y = person.get(Constants.TAG_MONTH_INCOME_AFTER_TAX);
            } else {
                y = person.get(Constants.TAG_MONTH_INCOME);
            }
            String x = MiscUtils.ConvertInteger(y);
            body += String.format("，该同志%s月平均收入为%s元人民币（大写：%s）",
                    taxAdd, x, MiscUtils.digitUppercase(x));
        }
        body += "。";
        if(MiscUtils.getMapBoolean(args, Constants.TAG_HAS_HEALTH)) {
            body += String.format("目前该职工的身体状况%s。", person.get(Constants.TAG_HEALTH));
        }
        Paragraph p1 = new Paragraph(body, curFont);
        p1.setAlignment(TextElementArray.ALIGN_LEFT);
        p1.setFirstLineIndent(firstLineIndent);
        document.add(p1);

        String body1 = "特此证明。";
        Paragraph p2 = new Paragraph(body1, curFont);
        p2.setAlignment(TextElementArray.ALIGN_LEFT);
        p2.setFirstLineIndent(firstLineIndent);
        document.add(p2);

        return;
    }

    private void writePdfEnd(SmartPdfCreater writer, Map<String,String> args, Map<String,String> person)  throws Exception {
        Document document = writer.getDocument();
        Font curFont = SmartPdfCreater.getFontFollowChinese(prop.getProperty(Constants.PROPERTY_CERTIFICATE_FONT_BODY));

        String ending = "\n";
        ending += String.format("单位名称：%s\n", "中国银联股份有限公司");
        ending += String.format("地址：%s\n", args.get(Constants.TAG_COMPANY_ADRESS));
        ending += String.format("联系人：%s\n", args.get(Constants.TAG_CONTACT_PERSON));
        ending += String.format("电话：%s\n", args.get(Constants.TAG_CONTACT_TELEPHONE));

        Paragraph p0 = new Paragraph(ending, curFont);
        p0.setAlignment(TextElementArray.ALIGN_LEFT);
        document.add(p0);

        String date_cn = "\n\n\n";
        date_cn += MiscUtils.ConvertDateToCn(args.get(Constants.TAG_CERTIFICATE_DATE));
        Paragraph p1 = new Paragraph(date_cn, curFont);
        p1.setAlignment(TextElementArray.ALIGN_RIGHT);
        document.add(p1);
    }

    private int buildCertificate(Map<String,String> args, Map<String,String> person, String outputPath) {
        int ret = 0;
        SmartPdfCreater writer = null;
        try {
            writer = new SmartPdfCreater(outputPath);
            pdfPageSetting(writer);
            writePdfHeader(writer, args, person);
            writePdfBody(writer, args, person);
            writePdfEnd(writer, args, person);
            writer.close();
        } catch (Exception e) {
            errMsg = e.getMessage();
            ret = -1;
        } finally {
            if(writer!=null) {
                writer.close();
            }
        }
        return ret;
    }

    public int generateCertificate(Map<String,String> args) {
        int ret = 0;
        String outputPath = args.get(Constants.TAG_OUTPUT_PATH);

        Map<String,String> person = getPersonelInfo(args);
        ret = buildCertificate(args, person, outputPath);
        if(ret!=0) {
            return -1;
        }

        String os = System.getProperties().getProperty("os.name");
        String cmd = prop.getProperty(Constants.PROPERTY_CERTIFICATE_OPENPDF_WIN);
        if(!os.startsWith("win") && !os.startsWith("Win")) {
            cmd = prop.getProperty(Constants.PROPERTY_CERTIFICATE_OPENPDF_LINUX);
        }
        ret = SystemCmdUtils.runCmd(cmd + " " + outputPath, true);
        if(ret!=0) {
            errMsg = SystemCmdUtils.getLatestError();
        }

        return 0;
    }

    public String searchFile(String photoPath, String id) {
        String tgt = null;
        File fileTarget = new File(photoPath);
        if(!fileTarget.isDirectory()) {
            errMsg = "照片设置目录";
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
        ret = SystemCmdUtils.runCmd(cmd + " " + fullPath, true);
        if(ret!=0) {
            errMsg = SystemCmdUtils.getLatestError();
        }

        return ret;
    }

    public String getLatestError() {
        return errMsg;
    }
}
