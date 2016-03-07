package com.smart.pubeyead.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.smart.pubeyead.utils.SmartMsExcelReader;
import com.smart.pubeyead.utils.SmartPdfCreater;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

public class IncomeListCreater {
    private SmartPdfCreater pdfCreater = null;
    private SmartMsExcelReader excelReader = null;
    private Map<String, String> tgtRecord;
    private Map<Integer, String> mapFieldsName;
    private String errMsg = null;
    private double totalAll;

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

        int ret_val = -1;
        tgtRecord = new HashMap<String, String>();
        for(int row=2; row<rowNum; row++) {
            Row rowContent = sheet.getRow(row);
            if(rowContent==null)
                continue;

            int colNum = rowContent.getPhysicalNumberOfCells();
            //员工编号 in colB
            String curID = SmartMsExcelReader.getStringSpecificFormatNumericCellValue(rowContent.getCell(1), 8);
            if(!curID.equals(ID))
                continue;

            for(int col=0; col<colNum; col++) {
                String curFieldValue = SmartMsExcelReader.getCellFormatValue(rowContent.getCell(col));
                String curFieldName = mapFieldsName.get(col);
                if("".equals(curFieldValue))
                    continue;
                tgtRecord.put(curFieldName, curFieldValue);
            }
            ret_val = 0;
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
        // unit: pixel
        //document.setMargins(1.0f, 1.0f, 1.0f, 1.0f);
        return 0;
    }

    private int pdfLogo() {
        int ret_val = 0;
        try {
            Document document = pdfCreater.getDocument();
            Image logo = Image.getInstance("/media/sf_vms_share/rub/elf.png");
            logo.setAlignment(Image.RIGHT);
            logo.scaleToFit(128.0f, 128.0f);
            document.add(logo);
        } catch(Exception e) {
            ret_val = -1;
            errMsg = e.getMessage();
        }
        return ret_val;
    }

    private final String titleString = "年度薪酬告知书";
    private final String unitString="单位：元";
    private int pdfHeader() {
        int ret_val = 0;
        try {
            Document document = pdfCreater.getDocument();
            Font curFont = SmartPdfCreater.setChineseFont("STSong-Light");

            Paragraph p0 = new Paragraph(titleString, curFont);
            p0.setAlignment(TextElementArray.ALIGN_CENTER);
            document.add(p0);

            p0 = new Paragraph(unitString, curFont);
            p0.setAlignment(TextElementArray.ALIGN_RIGHT);
            document.add(p0);
        } catch(Exception e) {
            ret_val = -1;
            errMsg = e.getMessage();
        }
        return ret_val;
    }

    double cvtString2Double(String x, double scale) {
        double y = Double.parseDouble(x);
        y = Math.round(y*scale)/scale;
        return y;
    }
    String double2String(double x, int decimalWidth) {
        String fmt = "%." + String.valueOf(decimalWidth) + "f";
        String result = String.format(fmt, x);
        return result;
    }
    PdfPCell buildDataCellWithValue(double x) {
        String y = double2String(x, 2);
        PdfPCell cell = new PdfPCell(new Phrase(y));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
//    PdfPCell buildDataBottomCellWithValue(double x) {
//        String y = double2String(x, 2);
//        PdfPCell cell = new PdfPCell(new Phrase(y));
//        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        cell.setBorder(Rectangle.BOTTOM);
//        cell.setBorderWidthBottom(3f);
//        return cell;
//    }
    PdfPCell buildDataCellWithString(String y, Font curFont) {
        PdfPCell cell = new PdfPCell(new Phrase(y, curFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
//    PdfPCell buildDataTopCellWithString(String y, Font curFont) {
//        PdfPCell cell = new PdfPCell(new Phrase(y, curFont));
//        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//        cell.setBorder(Rectangle.TOP);
//        cell.setBorderWidthTop(3f);
//        return cell;
//    }
//    PdfPCell buildDataBottomCellWithString(String y, Font curFont) {
//        PdfPCell cell = new PdfPCell(new Phrase(y, curFont));
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell.setBorder(Rectangle.BOTTOM);
//        cell.setBorderWidthBottom(3f);
//        return cell;
//    }

    private int make1stPart() {
        int ret_val = 0;
        try {
            PdfPTable tbl = new PdfPTable(5);
            tbl.setHorizontalAlignment(Element.ALIGN_CENTER);
            Font curFont = SmartPdfCreater.setChineseFont("STSong-Light");
            double totalBeforeTax, totalAfterTax, x;

            //line 1
            PdfPCell cell = new PdfPCell(new Phrase("全年现金性收入", curFont));
            cell.setRowspan(6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.NO_BORDER);
            tbl.addCell(cell);
            tbl.addCell(buildDataCellWithString("", curFont));
            tbl.addCell(buildDataCellWithString("应发", curFont));
            tbl.addCell(buildDataCellWithString("实发\n（扣除个税及5险1金后）", curFont));
            tbl.addCell(buildDataCellWithString("", curFont));

            totalBeforeTax = 0;
            totalAfterTax = 0;
            //line 2
            tbl.addCell(buildDataCellWithString("月收入:", curFont));
            x = cvtString2Double(tgtRecord.get("税前 月收入"), 100);
            totalBeforeTax += x;
            tbl.addCell(buildDataCellWithValue(x));
            x = cvtString2Double(tgtRecord.get("税后 月收入"), 100);
            totalAfterTax += x;
            tbl.addCell(buildDataCellWithValue(x));
            tbl.addCell(buildDataCellWithString("", curFont));

            //line 3
            tbl.addCell(buildDataCellWithString("年终奖:", curFont));
            x = cvtString2Double(tgtRecord.get("税前 年终奖"), 100);
            totalBeforeTax += x;
            tbl.addCell(buildDataCellWithValue(x));
            x = cvtString2Double(tgtRecord.get("税后 年终奖"), 100);
            totalAfterTax += x;
            tbl.addCell(buildDataCellWithValue(x));
            tbl.addCell(buildDataCellWithString("", curFont));

            //line 4
            tbl.addCell(buildDataCellWithString("开工红包：", curFont));
            x = cvtString2Double(tgtRecord.get("税前 开工红包"), 100);
            totalBeforeTax += x;
            tbl.addCell(buildDataCellWithValue(x));
            x = cvtString2Double(tgtRecord.get("税后 开工红包"), 100);
            totalAfterTax += x;
            tbl.addCell(buildDataCellWithValue(x));
            tbl.addCell(buildDataCellWithString("", curFont));

            //line 5
            tbl.addCell(buildDataCellWithString("董事会奖励：", curFont));
            x = cvtString2Double(tgtRecord.get("税前 董事会奖励"), 100);
            totalBeforeTax += x;
            tbl.addCell(buildDataCellWithValue(x));
            x = cvtString2Double(tgtRecord.get("税后 董事会奖励"), 100);
            totalAfterTax += x;
            tbl.addCell(buildDataCellWithValue(x));
            tbl.addCell(buildDataCellWithString("", curFont));

            //line 6
            tbl.addCell(buildDataCellWithString("小计：", curFont));
            tbl.addCell(buildDataCellWithValue(totalBeforeTax));
            tbl.addCell(buildDataCellWithValue(totalAfterTax));
            tbl.addCell(buildDataCellWithString("", curFont));


            PdfPTable tblExt = new PdfPTable(1);
            tblExt.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cellExt = new PdfPCell(tbl);
            cellExt.setBorder(Rectangle.BOTTOM|Rectangle.TOP);
            cellExt.setBorderWidth(2);
            cellExt.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellExt.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tblExt.addCell(cellExt);

            Document document = pdfCreater.getDocument();
            document.add(tblExt);
        } catch(Exception e) {
            ret_val = -1;
            errMsg = e.getMessage();
        }
        return ret_val;
    }

    private int make2ndPart() {
        int ret_val = 0;
        try {
            PdfPTable tbl = new PdfPTable(5);
            Font curFont = SmartPdfCreater.setChineseFont("STSong-Light");
            PdfPCell cell;

            //line 1
            cell = new PdfPCell(new Phrase("全年账户类收入"));
            cell.setRowspan(7);
            tbl.addCell(cell);
            tbl.addCell("");
            tbl.addCell("");
            tbl.addCell("当期");
            tbl.addCell("期末余额");

            //line 2
            tbl.addCell("住房公积金");
            tbl.addCell("(公司1：个人1)");
            tbl.addCell("xx");
            tbl.addCell("xx");


            //line 3
            tbl.addCell("企业年金");
            tbl.addCell("(公司10：个人1)");
            tbl.addCell("xx");
            tbl.addCell("xx");

            //line 4
            tbl.addCell("延期支付");
            tbl.addCell("(当年利息收入)");
            tbl.addCell("xx");
            tbl.addCell("xx");

            //line 5
            tbl.addCell("补充医疗");
            tbl.addCell("(上年度)");
            tbl.addCell("xx");
            tbl.addCell("xx");


            //line 6
            tbl.addCell("误餐");
            tbl.addCell("");
            tbl.addCell("xx");
            tbl.addCell("xx");


            //line 7
            tbl.addCell("小计：");
            tbl.addCell("");
            tbl.addCell("xx");
            tbl.addCell("xx");


            Document document = pdfCreater.getDocument();
            document.add(tbl);
        } catch(Exception e) {
            ret_val = -1;
            errMsg = e.getMessage();
        }
        return ret_val;
    }

    private int make3rdPart() {
        int ret_val = 0;
        try {
            PdfPTable tbl = new PdfPTable(5);
            Font curFont = SmartPdfCreater.setChineseFont("STSong-Light");
            PdfPCell cell;

            //line 1
            cell = new PdfPCell(new Phrase("外部奖励\n获批"));
            cell.setRowspan(4);
            tbl.addCell(cell);
            tbl.addCell("");
            tbl.addCell("应发");
            tbl.addCell("实发\n（扣除个税及5险1金后）");
            tbl.addCell("");

            //line 2
            tbl.addCell("1月入账");
            tbl.addCell("");
            tbl.addCell("xx");
            tbl.addCell("xxx");

            //line 3
            tbl.addCell("12月入账");
            tbl.addCell("");
            tbl.addCell("xx");
            tbl.addCell("xxx");

            //line 4
            tbl.addCell("小计：");
            tbl.addCell("");
            tbl.addCell("xx");
            tbl.addCell("");

            //line 5
            tbl.addCell("");
            tbl.addCell("合计：");
            tbl.addCell("");
            tbl.addCell("xx");
            tbl.addCell("");

            Document document = pdfCreater.getDocument();
            document.add(tbl);
        } catch(Exception e) {
            ret_val = -1;
            errMsg = e.getMessage();
        }
        return ret_val;
    }

    private int pdfTable() {
        int ret_val = 0;
        totalAll = 0;

        if(ret_val==0) {
            ret_val = make1stPart();
        }

        if(ret_val==0) {
            ret_val = make2ndPart();
        }

        if(ret_val==0) {
            ret_val = make3rdPart();
        }

        return ret_val;
    }

    private final String[] footerItem = {"账户信息查询:", "延期支付：", "企业年金："};
    private final String[] footerItemContents = {null, "可登录http://www.cj-pension.com.cn/Site/Home/CN查询", "可登录招行专业版查询"};
    private int pdfFooter() {
        int ret_val = 0;
        Font curFont;
        try {
            Document document = pdfCreater.getDocument();
            int itemTotal = footerItem.length;
            for(int itemNum=0; itemNum<itemTotal; itemNum++) {
                curFont = SmartPdfCreater.setChineseFont("STSong-Light");
                Paragraph title = new Paragraph(footerItem[itemNum], curFont);
                title.setAlignment(TextElementArray.ALIGN_LEFT);
                document.add(title);

                if(footerItemContents[itemNum]!=null) {
                    curFont = SmartPdfCreater.setChineseFont("STSong-Light");
                    Paragraph content = new Paragraph(footerItemContents[itemNum], curFont);
                    content.setAlignment(TextElementArray.ALIGN_LEFT);
                    document.add(content);
                }
            }
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
