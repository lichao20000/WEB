/**
 * 
 */
package com.linkage.module.gtms.stb.diagnostic.serv;

import java.math.BigDecimal;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * ������
 * @author chenjie
 * @version 1.0
 * @since 2013-4-24
 */
public class CommonUtil {
	
	/**
	 * ����excel�ļ��е�cell����
	 * @param cell
	 * @return
	 */
	public static String getCellString(HSSFCell cell) 
	{
        // TODO Auto-generated method stub
        String result = null;
        if(cell != null)
        {
            //��Ԫ�����ͣ�Numeric:0,String:1,Formula:2,Blank:3,Boolean:4,Error:5
            int cellType = cell.getCellType();
            switch (cellType) 
            {
	            case HSSFCell.CELL_TYPE_STRING:
	                result = cell.getRichStringCellValue().getString();
	                break;
	            case HSSFCell.CELL_TYPE_NUMERIC:
	                // ��ȡ�����ֿ����ǿ�ѧ���㷨��������Ҫת��
	                result = new BigDecimal(cell.getNumericCellValue()).toPlainString(); 
	                break;
	            case HSSFCell.CELL_TYPE_FORMULA:
	                result = cell.getCellFormula();
	                break;
	            case HSSFCell.CELL_TYPE_BOOLEAN:
	                break;
	            case HSSFCell.CELL_TYPE_BLANK:
	                break;
	            case HSSFCell.CELL_TYPE_ERROR:
	                break;
	            default:
	                break;
            }
        }
        return result;
    }
}
