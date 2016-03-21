package com.smart.pubeyead.utils;

import com.itextpdf.awt.AsianFontMapper;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.FileOutputStream;

public class SmartPdfCreater {
    private Document document = null;
    PdfWriter writer = null;

    public SmartPdfCreater(String fileName) throws Exception {
        document = new Document();
        writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        document.add(new Chunk(""));
    }

    public Document getDocument() {
        return document;
    }

    public void close() {
//        if(writer!=null) {
//            writer.flush();
//            writer.close();
//        }
        if(document!=null) {
            System.out.println("pages=" + document.getPageNumber());
            document.close();
        }
    }

    static public Font setChineseFont(String fontDesc, int style, int size) throws Exception {
        // solution 1: use windows trueType
        // fontDesc = "C:/Windows/Fonts/SIMYOU.TTF"
        //BaseFont basefont = BaseFont.createFont(fontDesc, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        // solution 2: iTextAsia.jar
        // fontDesc = "STSong-Light"
        BaseFont basefont = BaseFont.createFont(fontDesc, "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

        // solution 3: classpath
        // fontDesc = "/SIMYOU.TTF"
        //BaseFont basefont = BaseFont.createFont(fontDesc, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        //Font FontChinese = new Font(bfChinese, 12, Font.NORMAL);
        Font font = new Font(basefont, size, style);
        return font;
    }

    static int buildStyle(String[] fields) {
        int style = 0;
        for(int i=1; i<fields.length-1; i++) {
            if("粗体".equals(fields[i])){
                style |= 0x01;
            }
            if("斜体".equals(fields[i])){
                style |= 0x02;
            }
            if("下划线".equals(fields[i])){
                style |= 0x04;
            }
            if("删除线".equals(fields[i])){
                style |= 0x08;
            }
        }
        return style;
    }

    //粗体：bit0，斜体：bit1，下划线：bit2，删除线： bit3
    static public Font getFontFollowChinese(String desc) throws Exception {
        String[] fields = desc.split(",");
        if(fields.length<3) {
            throw new RuntimeException("字体设置非法");
        }
        String fontDesc = fields[0];
        int size = Integer.parseInt(fields[fields.length-1]);
        int style = buildStyle(fields);

        String embedFontDesc = "STSong-Light";
        Font font = getFontFromEnv(fontDesc, style, size, embedFontDesc);
        return font;
    }

    static public Font getFontFromEnv(String fontDesc, int style, int size, String embedFontDesc) throws Exception  {
        Font font = null;

        String os = System.getProperties().getProperty("os.name");
//        if(os.startsWith("win") || os.startsWith("Win")) {
//            String winSysRoot = System.getenv("SystemRoot");
//            String fontPath = winSysRoot + "\\Fonts\\" + fontDesc + ".ttf";
//            BaseFont basefont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//            font = new Font(basefont, size, style);
//        } else
        {
            java.awt.Font orgFont = new java.awt.Font(fontDesc, style, size);
            AsianFontMapper mapper = new AsianFontMapper(embedFontDesc, "UniGB-UCS2-H");
            BaseFont basefont =  mapper.awtToPdf(orgFont);
            font = new Font(basefont, size, style);
        }

        return font;
    }
}
