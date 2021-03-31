
package com.linkage.module.gwms.resource.bio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.resource.dao.DeviceE8CImportDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class DeviceE8CImportBIO
{

	private static Logger logger = LoggerFactory.getLogger(DeviceE8CImportBIO.class);
	private DeviceE8CImportDAO dao;

	public String analyse(String fileName)
	{
		if (StringUtil.IsEmpty(fileName))
		{
			return "未上传文件";
		}
		String result = "";
		String fileName_ = fileName.substring(fileName.length() - 3, fileName.length());
		if (!"xls".equals(fileName_))
		{
			return "上传的文件格式不正确！";
		}
		ArrayList<Map<String, String>> dataList = null;
		try
		{
			dataList = getImportDataByXLS(fileName);
			logger.warn("行数："+dataList.size());
		}
		catch (FileNotFoundException e)
		{
			logger.error("{}文件没找到！", fileName);
			return "文件没找到！";
		}
		catch (IOException e)
		{
			logger.error("{}文件解析出错！{}", fileName, e);
			return "文件解析出错！";
		}
		catch (Exception e)
		{
			logger.error("{}文件解析出错！{}", fileName, e);
			return "文件解析出错！";
		}
		if (dataList.size() < 1)
		{
			return "文件未解析到合法数据！";
		}
		else
		{
			result = dao.addDevice(dataList);
		}
		return result;
	}

	public ArrayList<Map<String, String>> getImportDataByXLS(String fileName)
			throws BiffException, IOException
	{
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		File f = new File(getFilePath() + fileName);
		Workbook workbook = Workbook.getWorkbook(f);
		Sheet sheet=workbook.getSheet(0);
         // 获取Sheet表中所包含的总行数
        int rsRows = sheet.getRows();
        // 获取Sheet表中所包含的总列数
        int rsColumns = sheet.getColumns();
        for (int i = 1; i < rsRows; i++) {
            Map<String, String> map = new HashMap<String, String>();
            String temp="";
            for (int j = 1; j < rsColumns; j++) {
                Cell cell = sheet.getCell(j, i);
                map.put(sheet.getCell(j, 0).getContents(),cell.getContents().trim());
                temp+=cell.getContents().trim();
            }
            if (!StringUtil.IsEmpty(temp)) {
            	list.add(map);
			}
        }
		workbook.close();
		return list;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath()
	{
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try
		{
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	public DeviceE8CImportDAO getDao() {
		return dao;
	}

	public void setDao(DeviceE8CImportDAO dao) {
		this.dao = dao;
	}

	public static void main(String[] args) throws BiffException, IOException {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		File f = new File("C://Users/Google/Desktop/123.xls");
		Workbook workbook = Workbook.getWorkbook(f);
		Sheet sheet=workbook.getSheet(0);
         // 获取Sheet表中所包含的总行数
        int rsRows = sheet.getRows();
        // 获取Sheet表中所包含的总列数
        int rsColumns = sheet.getColumns();
        for (int i = 1; i < rsRows; i++) {
            Map<String, String> map = new HashMap<String, String>();
            String temp="";
            for (int j = 1; j < rsColumns; j++) {
                Cell cell = sheet.getCell(j, i);
                map.put(sheet.getCell(j, 0).getContents(),cell.getContents().trim());
                temp+=cell.getContents().trim();
            }
            if (!StringUtil.IsEmpty(temp)) {
            	System.out.println(map);
            	list.add(map);
			}
        }
		workbook.close();
	}
}
