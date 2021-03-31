package com.linkage.module.gwms.resource.utils;

import com.linkage.module.gwms.util.StringUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songxq
 * @version 1.0
 * @category
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * @since 2020/7/10.
 */
public class FileUtil {


    private static Logger logger = Logger.getLogger(FileUtil.class);

    private int size;

    public FileUtil(int size)
    {
        this.size =size;
    }

    //Excel
    private HSSFWorkbook workbook;
    private HSSFWorkbook outWorkbook;
    /**
     * 读取Excel文件并将文件列表放到list中
     *
     * @param sheetNumber
     * @param dir         excel文件所在目录
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public List<String> getDatasInSheet(int sheetNumber, File file,String tar) throws FileNotFoundException, IOException {

        List<String> result = new ArrayList<String>();
        workbook = new HSSFWorkbook(new FileInputStream(file));

        //获得指定的表
        HSSFSheet sheet = workbook.getSheetAt(sheetNumber);
        String sheetName = sheet.getSheetName();
        //获得数据总行数
        int rowCount = sheet.getLastRowNum();
        if (rowCount < 1) {
            return result;
        }
        if(rowCount > size )
        {
            rowCount = size;
        }
        HSSFRow rowHead = sheet.getRow(0);
        HSSFCell cellhead = rowHead.getCell(0);
        //获得指定单元格中数据
        String head = StringUtil.getStringValue(this.getCellString(cellhead));
        System.out.println("                " + head);
        //逐行读取数据
        for (int rowIndex = 1; rowIndex <= rowCount; rowIndex++) {
            //获得行对象
            HSSFRow row = sheet.getRow(rowIndex);
            if (row != null) {
                List<Object> rowData = new ArrayList<Object>();
                //获得本行中各单元格中的数据
                HSSFCell cell = row.getCell(0);
                //获得指定单元格中数据
                String str = StringUtil.getStringValue(this.getCellString(cell));
                if (str != null && str.length() > 1)
                    result.add(str);
            }
        }

        FileOutputStream out = null;

        File target =new File(tar);
        if(!target.exists())
        {
            try {
                createExcel(tar,sheetName,head);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HSSFWorkbook outWorkbook = new HSSFWorkbook(new FileInputStream(tar));
        HSSFSheet sheetout = outWorkbook.getSheet(sheetName);
        try {
            // 获得表头行对象
            HSSFRow titleRow = sheetout.getRow(0);
            if(titleRow!=null){
                for(int rowId=0;rowId< result.size();rowId++){
                    HSSFRow newRow=sheetout.createRow(rowId+1);
                    HSSFCell cell1 = newRow.createCell(0);
                    cell1.setCellValue(result.get(rowId));
                }
            }
            out = new FileOutputStream(tar);
            outWorkbook.write(out);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());
        } finally {
            try {
                assert out != null;
                out.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }


        return result;
    }


    /**
     * copy str to destPath
     *
     * @param str
     * @param sourcePath
     * @param destPath
     * @return boolean isFile return true;else return false;
     * @throws IOException
     */

    /**
     * 获得单元格中的内容
     *
     * @param cell
     * @return
     */
    protected Object getCellString(HSSFCell cell) {
        Object result = null;
        if (cell != null) {
            int cellType = cell.getCellType();
            switch (cellType) {
                case HSSFCell.CELL_TYPE_STRING:
                    result = cell.getRichStringCellValue().getString();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    result =cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    result = cell.getNumericCellValue();
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    result = null;
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    result = null;
                    break;
            }
        }
        return result;
    }


    /*public static void main(String[] args) {
        FileUtil fileUtil = new FileUtil(200);
        File file = new File("D:/sxq/1.xls");
        try {
            fileUtil.getDatasInSheet(0,file,"D:/sxq/2.xls");
        } catch (IOException e) {
            e.printStackTrace();
        }
  }*/

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public static void createExcel(String fileDir,String sheetName,String titleRow) throws Exception{
        //创建workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        HSSFSheet sheet1 = workbook.createSheet(sheetName);
        //新建文件
        FileOutputStream out = null;
        try {
            //添加表头
            HSSFRow row = workbook.getSheet(sheetName).createRow(0);    //创建第一行
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(titleRow);
            out = new FileOutputStream(fileDir);
            workbook.write(out);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

