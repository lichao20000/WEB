package com.linkage.module.gwms.resource.bio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.dao.ImportLoidDAO;



/**
 * 
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-9-21 下午02:26:08
 * @category com.linkage.module.gwms.resource.bio
 * @copyright 南京联创科技 网管科技部
 *
 */
public class ImportLoidBIO
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ImportLoidBIO.class);
	
	private ImportLoidDAO  importLoidDAO;
	
	
	/**
	 * 输入excel文件，解析后返回ArrayList
	 * @param file 输入的excel文件
	 * @return ArrayList<String>
	 */
	public List<String> getDeviceByImportLoid(File file){
		
		//初始化返回值和字段名数组
		ArrayList<String> arr = new ArrayList<String>();
		Workbook wwb = null;
		Sheet ws = null;
		
		try{
			//读取excel文件
			
			
			wwb = Workbook.getWorkbook(file);
			
			//总sheet数
			//int sheetNumber = wwb.getNumberOfSheets();
			int sheetNumber = 1;
			logger.warn("sheetNumber:" + sheetNumber);
			
			for (int m=0;m<sheetNumber;m++){
				ws = wwb.getSheet(m);
	       
				//当前页总记录行数和列数
				int rowCount = ws.getRows();
				int columeCount = ws.getColumns();
				if(101<rowCount){
					rowCount = 101;
				}
				
				//第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0){
					
					//取当前页所有值放入list中
					for (int i=1;i<rowCount;i++){
						String temp = ws.getCell(0, i).getContents().trim();
						if(null!=temp && !"".equals(temp)){
							arr.add(ws.getCell(0, i).getContents().trim());
						}
					}
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				wwb.close();
			}catch(Exception e){
				logger.debug(e.getMessage());
			}
			
		}
		return arr;
	}
	/**
	 * 输入excel文件，解析后返回ArrayList
	 * @param file 输入的excel文件
	 * @return ArrayList<String>
	 */
	public List<String> getDeviceByImportLoid4Nx(File file){
		
		//初始化返回值和字段名数组
		ArrayList<String> arr = new ArrayList<String>();
		Workbook wwb = null;
		Sheet ws = null;
		
		try{
			//读取excel文件
			
			
			wwb = Workbook.getWorkbook(file);
			
			//总sheet数
			//int sheetNumber = wwb.getNumberOfSheets();
			int sheetNumber = 1;
			logger.warn("sheetNumber:" + sheetNumber);
			
			for (int m=0;m<sheetNumber;m++){
				ws = wwb.getSheet(m);
	       
				//当前页总记录行数和列数
				int rowCount = ws.getRows();
				int columeCount = ws.getColumns();
				if(101<rowCount){
					rowCount = 101;
				}
				
				//第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0){
					
					//取当前页所有值放入list中
					for (int i=1;i<rowCount;i++){
						String temp = ws.getCell(0, i).getContents().trim();
						if(null!=temp && !"".equals(temp)){
							if("是".equals(ws.getCell(5, i).getContents().trim()) && !arr.contains(ws.getCell(0, i).getContents().trim()))
							{
								arr.add(ws.getCell(0, i).getContents().trim());
							}
						}
					}
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				wwb.close();
			}catch(Exception e){
				logger.debug(e.getMessage());
			}
			
		}
		return arr;
	}
	
	
	public List getDeviceExceptionList(String pcity_id,List<String> loidList){
		return importLoidDAO.getDeviceExceptionList(pcity_id,loidList);
	}
	
	public List<Map<String,String>> getResult(String pcity_id, List<String> loidList){
		if(Global.CQDX.equals(Global.instAreaShortName)){
			return importLoidDAO.getResult4CQ(pcity_id, loidList);
		}
		return importLoidDAO.getResult(pcity_id, loidList);
	}
	
	public boolean forward(String map_id,String device_idNormalStr,String task_name ){
		return	importLoidDAO.forward(map_id,device_idNormalStr,task_name);
	}
	
	public List<Map<String,String>> getDigitMap(String queryCity,List<Map<String,String>> list){
		return	importLoidDAO.getDigitMap(queryCity,list);
	}
	
	
	
	public ImportLoidDAO getImportLoidDAO()
	{
		return importLoidDAO;
	}

	
	public void setImportLoidDAO(ImportLoidDAO importLoidDAO)
	{
		this.importLoidDAO = importLoidDAO;
	}

}
