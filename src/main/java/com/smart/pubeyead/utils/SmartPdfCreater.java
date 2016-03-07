package com.smart.pubeyead.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class SmartPdfCreater {
    private Document document = null;

    public SmartPdfCreater(String fileName) throws Exception {
        document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
    }

    public Document getDocument() {
        return document;
    }

    public void close() {
        if(document!=null)
            document.close();
    }

    static public Font setChineseFont(String fontDesc) throws Exception {
        // solution 1: use windows trueType
        // fontDesc = "C:/Windows/Fonts/SIMYOU.TTF"
        //BaseFont basefont = BaseFont.createFont(fontDesc, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        // solution 2: iTextAsia.jar
        // fontDesc = "STSong-Light"
        BaseFont basefont = BaseFont.createFont(fontDesc, "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

        // solution 3: classpath
        // fontDesc = "/SIMYOU.TTF"
        //BaseFont basefont = BaseFont.createFont(fontDesc, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font font = new Font(basefont);
        return font;
    }
}
