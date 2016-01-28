package com.smart.pubeyead;

import com.itextpdf.text.Document;
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
}
