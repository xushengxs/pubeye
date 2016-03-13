package com.smart.pubeyead.model;

import com.smart.pubeyead.utils.Constants;
import com.smart.pubeyead.utils.SmartMsExcelReader;
import org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

public class IncomeModel {
    Properties prop = null;
    String excelPath = null;
    String worksheetName = null;
    Map<String, Map<String, String>> dataStore;
    String errMsg = null;

    public IncomeModel(Properties prop) {
        this.prop = prop;
        {
            String x = prop.getProperty(Constants.PROPERTY_CERTIFICATE_DATA_PATH);
            if(x!=null) {
                String[] parts = x.split("#");
                excelPath = parts[0];
                if(parts.length>1) {
                    worksheetName = parts[1];
                }
            }
        }
        buildDataStore();
    }

    private int buildDataStore() {
        int ret = 0;
        SmartMsExcelReader reader = null;
        try {
            reader = new SmartMsExcelReader(excelPath);
            Workbook wb = reader.getWorkbook();
            Sheet sheet = null;
            if(worksheetName!=null) {
                sheet = wb.getSheet(worksheetName);
            } else {
                sheet = wb.getSheetAt(0);
            }
            ret = readAllData(sheet);
        } catch(Exception e) {
            errMsg = e.getMessage();
            ret = -1;
        } finally {
            reader.close();
        }
        return ret;
    }

    private int readAllData(Sheet sheet) {
        int ret = 0;
        int rowNum = sheet.getLastRowNum();
        if(rowNum<2) {
            errMsg = "表格中没有数据!";
            return -1;
        }

        try {
            List<String> listTags = readHeader(sheet);
            readData(sheet, listTags);
        } catch(Exception e) {
            errMsg = e.getMessage();
            ret = -1;
        }

        return ret;
    }

    final String[] mappingCn2Tag = {
            "姓名", Constants.TAG_NAME,
            "员工编号", Constants.TAG_ID,
            "身份证号", Constants.TAG_IDCARD,
            "职务", Constants.TAG_POSITION,
            "年收入", Constants.TAG_YEAR_INCOME,
            "入司时间", Constants.TAG_ENTRY_DATE,
            "月平均", Constants.TAG_MONTH_INCOME,
            "身体状况", Constants.TAG_HEALTH,
            "部门", Constants.TAG_DEPARTMENT
    };
    private String searchForTag(String fieldName) {
        String result = null;
        for(int i=0; i<mappingCn2Tag.length; i=i+2) {
            if(mappingCn2Tag[i].equals(fieldName)) {
                result = mappingCn2Tag[i+1];
                break;
            }
        }
        return result;
    }

    private List<String> readHeader(Sheet sheet) {
        int ret = 0;
        List<String> tagList = new ArrayList<String>();

        Row rowContent = sheet.getRow(0);
        int colNum = rowContent.getLastCellNum();
        for(int col=0; col<colNum; col++) {
            String curFieldName = SmartMsExcelReader.getStringCellValue(rowContent.getCell(col));
            String tagName = searchForTag(curFieldName);
            tagList.add(tagName);
        }

        return tagList;
    }

    private int readData(Sheet sheet, List<String> listTags) {
        int ret = 0;
        Map<String, Map<String, String>> workingDataStore = new HashMap<String, Map<String, String>>();

        int rowNum = sheet.getLastRowNum();
        for(int row=1; row<=rowNum; row++) {
            Row rowContent = sheet.getRow(row);
            if(rowContent==null)
                continue;

            Map<String, String> person = new HashMap<String, String>();
            int colNum = rowContent.getPhysicalNumberOfCells();
            for(int col=0; col<colNum; col++) {
                String tag = listTags.get(col);
                if(tag==null)
                    continue;
                String curFieldValue = SmartMsExcelReader.getCellFormatValue(rowContent.getCell(col));
                person.put(tag, curFieldValue);
            }

            String id = person.get(Constants.TAG_ID);
            if(id==null)
                continue;
            workingDataStore.put(id, person);
        }
        if(workingDataStore.size()>0)
            dataStore = workingDataStore;
        return ret;
    }

    public Map<String, String> getPerson(String dataPath, String id) {
        Map<String, String> person = null;
        String[] parts = dataPath.split("#");
        String cur_excelPath = parts[0];
        String cur_worksheetName = null;
        if(parts.length>1) {
            cur_worksheetName = parts[1];
        }
        boolean rebuildStore = false;
        if(!cur_excelPath.equals(excelPath))
            rebuildStore = true;
        if(!rebuildStore && (cur_worksheetName!=null || worksheetName!=null) ) {
            if(cur_worksheetName==null && worksheetName!=null)
                rebuildStore = true;
            if(cur_worksheetName!=null && worksheetName==null)
                rebuildStore = true;
            if(!rebuildStore) {
                if(!cur_worksheetName.equals(worksheetName))
                    rebuildStore = true;
            }
        }
        if(rebuildStore){
            excelPath = cur_excelPath;
            worksheetName = cur_worksheetName;
            buildDataStore();
        }
        person = dataStore.get(id);
        return person;
    }
}
