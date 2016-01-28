package com.smart.pubeyead;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import javafx.scene.control.Cell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class IncomeListCreater {
    private SmartPdfCreater pdfCreater = null;
    private SmartMsExcelReader excelReader = null;
    private Map<String, Object> tgtRecord;
    private Map<Integer, String> mapFieldsName;
    private String errMsg = null;

    public IncomeListCreater(String pdfName, String excelName) throws Exception {
        pdfCreater = new SmartPdfCreater(pdfName);
        excelReader = new SmartMsExcelReader(excelName);
    }

    private int excelHeaderFields(Sheet sheet) {
        int rowNum = sheet.getLastRowNum();
        if(rowNum<2) {
            return -1;
        }

        int ret_val = 0;
        mapFieldsName = new HashMap<Integer, String>();

        {
            Row rowContent = sheet.getRow(0);
            int colNum = rowContent.getLastCellNum();
            String prevFieldName = "";
            for(int col=0; col<colNum; col++) {
                String curFieldName = SmartMsExcelReader.getStringCellValue(rowContent.getCell(col));
                if("".equals(curFieldName)) {
                    mapFieldsName.put(col, prevFieldName);
                } else {
                    mapFieldsName.put(col, curFieldName);
                    prevFieldName = curFieldName;
                }
            }
        }

        {
            Row rowContent = sheet.getRow(1);
            int colNum = rowContent.getLastCellNum();
            for(int col=0; col<colNum; col++) {
                String curFieldName = SmartMsExcelReader.getStringCellValue(rowContent.getCell(col));
                if("".equals(curFieldName)) {
                    continue;
                }

                String level1 = mapFieldsName.get(col);
                mapFieldsName.put(col, level1+" "+curFieldName);
            }
        }

        return ret_val;
    }

    private int excelTargetRecord(Sheet sheet, String ID) {
        int rowNum = sheet.getLastRowNum();
        if(rowNum<2) {
            return -1;
        }

        int ret_val = 0;
        tgtRecord = new HashMap<String, Object>();
        for(int row=2; row<rowNum; row++) {
            Row rowContent = sheet.getRow(row);
            int colNum = rowContent.getPhysicalNumberOfCells();
            //员工编号 in colB
            String curID = SmartMsExcelReader.getCellFormatValue(rowContent.getCell(1));
            if(!curID.equals(ID))
                continue;

            for(int col=0; col<colNum; col++) {
                String curFieldValue = SmartMsExcelReader.getCellFormatValue(rowContent.getCell(col));
                String curFieldName = mapFieldsName.get(col);
                if("".equals(curFieldValue))
                    continue;
                tgtRecord.put(curFieldName, curFieldValue);
            }
            break;
        }
        return ret_val;
    }

    private int readExcel(String ID) {
        int ret_val = 0;

        Workbook wb = excelReader.getWorkbook();
        Sheet sheet = wb.getSheetAt(0);

        if(ret_val == 0) {
            ret_val = excelHeaderFields(sheet);
        }

        if(ret_val == 0) {
            ret_val = excelTargetRecord(sheet, ID);
        }

        return ret_val;
    }

    private int pdfPageSetting() {
        Document document = pdfCreater.getDocument();

        document.setPageSize(PageSize.A4);
        //document.setMargins(1.0f, 1.0f, 1.0f, 1.0f);
        return 0;
    }

    private int pdfLogo() {
        int ret_val = 0;
        try {
            Document document = pdfCreater.getDocument();
            Image logo = Image.getInstance(new URL("./1.jpg"));
            logo.setAlignment(Image.RIGHT);
            logo.scaleToFit(1.0f, 1.0f);
            document.add(logo);
        } catch(Exception e) {
            ret_val = -1;
            errMsg = e.getMessage();
        }
        return ret_val;
    }

    private int pdfHeader() {
        int ret_val = 0;
        try {
            Document document = pdfCreater.getDocument();
            Paragraph p0 = new Paragraph("xsxs", FontFactory.getFont(FontFactory.COURIER, 12));
            p0.setAlignment(TextElementArray.ALIGN_MIDDLE);
            document.add(p0);
        } catch(Exception e) {
            ret_val = -1;
            errMsg = e.getMessage();
        }
        return ret_val;
    }

    private int pdfTable() {
        int ret_val = 0;
        try {
            Document document = pdfCreater.getDocument();
            PdfPTable tbl = new PdfPTable(10);
            tbl.setWidths(new int[]{1, 1, 2});
            PdfPCell cell = new PdfPCell(new Phrase("xs"));
            cell.setRowspan(3);
            cell.setColspan(2);
            tbl.addCell(cell);
            document.add(tbl);
        } catch(Exception e) {
            ret_val = -1;
            errMsg = e.getMessage();
        }
        return ret_val;
    }

    private int pdfFooter() {
        int ret_val = 0;
        try {
            Document document = pdfCreater.getDocument();
            Paragraph p0 = new Paragraph("xsxs", FontFactory.getFont(FontFactory.COURIER, 12));
            p0.setAlignment(TextElementArray.ALIGN_MIDDLE);
            document.add(p0);
        } catch(Exception e) {
            ret_val = -1;
            errMsg = e.getMessage();
        }
        return ret_val;
    }

    private int buildPdf() {
        int ret_val = 0;

        if(ret_val==0) {
            ret_val = pdfPageSetting();
        }

        if(ret_val==0) {
            ret_val = pdfLogo();
        }

        if(ret_val==0) {
            ret_val = pdfHeader();
        }

        if(ret_val==0) {
            ret_val = pdfTable();
        }

        if(ret_val==0) {
            ret_val = pdfFooter();
        }

        return 0;
    }

    public int buildArchive(String ID) {
        int ret_val = 0;

        if(ret_val==0) {
            ret_val = readExcel(ID);
        }

        if(ret_val==0) {
            ret_val = buildPdf();
        }

        return 0;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void close() {
        if(pdfCreater!=null) {
            pdfCreater.close();
        }
        if(excelReader!=null) {
            excelReader.close();
        }
    }
}
