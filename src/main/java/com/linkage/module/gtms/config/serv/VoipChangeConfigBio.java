package com.linkage.module.gtms.config.serv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.config.dao.VoipChangeConfigDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;

public class VoipChangeConfigBio 
{
	private static Logger logger = LoggerFactory.getLogger(VoipChangeConfigBio.class);
	
	private VoipChangeConfigDAO dao;
	private String msg;
	

	public List<HashMap<String, String>> getDeviceList(String fileName, String faultList, String taskid, boolean isTh) 
	{
		faultList = "";
		
		if (fileName.length() < 4) {
			this.msg = "上传的文件名不正确！";
			return null;
		}
		
		String fileName_ = fileName.substring(fileName.length() - 3, fileName.length());
		if (!"xls".equals(fileName_) && !"lsx".equals(fileName_)) {
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		
		ArrayList<Map<String,String>> dataList = null;
		try {
			if ("xls".equals(fileName_)) {
				dataList = getImportDataByXLS(fileName, isTh);
			} else {
				dataList = getImportDataByXLSx(fileName, isTh);
			}
		} catch (FileNotFoundException e) {
			logger.warn("{}文件没找到！", fileName);
			this.msg = "文件没找到！";
			return null;
		}catch (Exception e) {
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return null;
		}
		
		if (null == dataList && !StringUtil.IsEmpty(this.msg)) {
			return null;
		}
		List<HashMap<String, String>> listNew = new ArrayList<HashMap<String, String>>();
		if (null == dataList || dataList.isEmpty()) {
			this.msg = "文件未解析到合法数据！";
			return null;
		} 
		else 
		{
			String loid;
			for (Map<String, String> map : dataList) 
			{
				loid = map.get("LOID");
				if(!StringUtil.IsEmpty(loid))
				{
					Map<String, String> infomap = dao.getInfoByLoid(loid);
					if(null == infomap || infomap.isEmpty()){
						faultList += loid+"##用户表中没有查询结果;";
					}
					else
					{
						HashMap<String, String> resmap = new HashMap<String,String>();
						resmap.put("device_id", infomap.get("device_id"));
						resmap.put("user_id", infomap.get("user_id"));
						resmap.put("username", loid);
						resmap.put("city_id", map.get("属地"));
						resmap.put("city_name", CityDAO.getCityName(map.get("属地")));
						resmap.put("voip_port", map.get("新IMS侧终端物理标识"));
						resmap.put("reg_id_type", map.get("终端标识类型：域名(DomainName)或IP"));
						resmap.put("prox_serv", map.get("主用MGC地址"));
						resmap.put("prox_port", map.get("主用MGC端口"));
						resmap.put("stand_prox_serv", map.get("备用MGC地址"));
						resmap.put("stand_prox_port", map.get("备用MGC端口"));
						
						resmap.put("rtp_prefix", StringUtil.getStringValue(map, "临时终结点标识前缀", ""));
						listNew.add(resmap);
					}
				}
			}
		}
		
		if (null == listNew || listNew.isEmpty()) {
			msg = "账号不存在";
		}else{
			dao.insertvoipChangeReport_tmp(taskid,listNew);
			
			HashMap<String,String> faultMap = new HashMap<String,String>();
			faultMap.put("faultMap", StringUtil.getStringValue(faultList));
			listNew.add(faultMap);
		}
		return listNew;
	}
	

	public int doConfigAll(String taskid, long userId) 
	{
		dao.insertvoipChangeReport(taskid);
		//生成task表数据
		return dao.inserttask(taskid,userId);
	}
	
	/**
	 * 解析文件（xls格式）
	 */
	public ArrayList<Map<String,String>> getImportDataByXLS(String fileName, boolean isTh)
			throws BiffException, IOException 
	{
		logger.debug("getImportDataByXLS{}", fileName);
		File file = new File(getFilePath() + fileName);
		//初始化返回值和字段名数组
		ArrayList<Map<String,String>> arr = new ArrayList<Map<String,String>>();
		String[] title;
		
		Workbook wwb = null;
		
		try{
			//读取excel文件
			wwb = Workbook.getWorkbook(file);
			
			//总sheet数
			int sheetNumber = wwb.getNumberOfSheets();
			logger.debug("sheetNumber:" + sheetNumber);
			
			for (int m=0;m<sheetNumber;m++){
				Sheet ws = wwb.getSheet(m);
				
				//当前页总记录行数和列数
				int rowCount = ws.getRows();
				if(rowCount >201){
					rowCount = 201;
				}
				int columeCount = ws.getColumns();
				logger.warn("rowCount:" + rowCount);
				logger.warn("columeCount:" + columeCount);
				if (isTh) {
					if (columeCount != 9) {
						this.msg = "请使用通化模板上传";
						return null;
					}
				}
				else {
					if (columeCount != 8) {
						this.msg = "请使用正确模板上传";
						return null;
					}
				}
				//第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0){
					//取第一列为字段名
					title = new String[columeCount];
					for (int k=0;k<columeCount;k++){
						title[k] = ws.getCell(k, 0).getContents();
					}
					
					//取当前页所有值放入list中
					for (int i=1;i<rowCount;i++){
						Map<String,String> dataMap = new HashMap<String,String>();
						if (isTh && columeCount == 9 && StringUtil.IsEmpty(ws.getCell(8, i).getContents())) {
							logger.warn("通化数据[临时终结点标识前缀]为空,过滤数据loid[{}],属地[{}]", ws.getCell(0, i).getContents(),
									ws.getCell(1, i).getContents());
							continue;
						}
						for (int j = 0; j < columeCount; j++){
							dataMap.put(title[j].trim(), ws.getCell(j, i).getContents().trim());
						}
						arr.add(dataMap);
					}
				}
			}
		}
		catch(Exception e){
			logger.error("解析文件错误:{}", ExceptionUtils.getStackTrace(e));
		}
		
		return arr;
	}
	
	/**
	 * 传入excel文件，解析后返回ArrayList。文件的第一行表示字段的名称
	 */
	public ArrayList<Map<String,String>> getImportDataByXLSx(String fileName, boolean isTh)
	{
		ArrayList<Map<String,String>> arr = new ArrayList<Map<String,String>>();
		String[] title;
		File file = new File(getFilePath() + fileName);
		FileInputStream readFile = null;
		XSSFWorkbook wb = null;
		XSSFRow row = null;
		XSSFSheet st = null;
		XSSFCell cell = null;
		try{
			readFile = new FileInputStream(file);
			wb = new XSSFWorkbook(readFile);
			
			//文档的页数
			int numOfSheets = wb.getNumberOfSheets();

			for (int k=0;k<numOfSheets;k++)
			{
				//获取当前的
				st = wb.getSheetAt(k);
				//当前页的行数
				int rows = st.getLastRowNum();
				
				//如果行数大于0，则先取第一行为字段名
				if (rows > 0){
					if(rows >200){
						rows = 200;
					}
					logger.warn("rowCount:" + rows);
					row = st.getRow(0);
					int cells = row.getLastCellNum();
					title = new String[cells];
					logger.warn("columeCount:" + cells);
					if (isTh) {
						if (cells != 9) {
							this.msg = "请使用通化模板上传";
							return null;
						}
					}
					else {
						if (cells != 8) {
							this.msg = "请使用正确模板上传";
							return null;
						}
					}
					//分列解析
					for (int j=0;j<cells;j++)
					{
						cell = row.getCell((short)j);
						if (cell == null){
							title[j] = "";
							continue;
						}
						
						switch (cell.getCellType()){
							case HSSFCell.CELL_TYPE_NUMERIC:{
								Integer num = new Integer((int) cell.getNumericCellValue());
								title[j] = String.valueOf(num);
								break;
							}
							case HSSFCell.CELL_TYPE_STRING:{
								title[j] = cell.getRichStringCellValue().toString();
								break;
							}
							default:title[j] = "";
						}
					}
					
					//分行解析
					for (int i=1;i<=rows;i++)
					{
						if (st.getRow(i) == null){
							continue;
						}
						row = st.getRow(i);
						
						Map<String, String> map = getCellMap(row, cells, title);
						String prefix = StringUtil.getStringValue(map, "临时终结点标识前缀");
						String loid = StringUtil.getStringValue(map, "LOID");
						String city = StringUtil.getStringValue(map, "属地");
						if (isTh && cells == 9 && StringUtil.IsEmpty(prefix)) {
							logger.warn("通化数据[临时终结点标识前缀]为空,过滤数据loid[{}],属地[{}]", loid, city);
							continue;
						}
						arr.add(map);
					}
				}
			}
		}
		catch(Exception e){
			logger.error("解析文件错误:{}", ExceptionUtils.getStackTrace(e));
		}
		finally{
			try{
				readFile.close();
			}
			catch(Exception e){
				logger.error("解析文件错误:{}", ExceptionUtils.getStackTrace(e));
			}
		}
		
		return arr;
	}
	
	/**
	 * 根据传入的excel行数据得到数据Map
	 */
	private Map<String,String> getCellMap(XSSFRow row,int cells,String[] title)
	{
		Map<String,String> data = new HashMap<String,String>();
		//分列
		XSSFCell cell = null;
		for (int j=0;j<cells;j++)
		{
			cell = row.getCell((short)j);
			//列为空则输入空字符串
			if (cell == null){
				data.put(title[j], "");
				continue;
			}
			
			//对字段分类处理
			switch (cell.getCellType())
			{
				case HSSFCell.CELL_TYPE_NUMERIC:{
					Integer num = new Integer((int) cell.getNumericCellValue());
					data.put(title[j], String.valueOf(num));
					break;
				}
				case HSSFCell.CELL_TYPE_STRING:{
					data.put(title[j].trim(), cell.getRichStringCellValue().toString().trim());
					break;
				}
				default:data.put(title[j], "");
			}
		}
		
		return data;
	}
	
	/**
	 * 获取文件路径
	 */
	public String getFilePath() 
	{
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try {
			lipossHome = java.net.URLDecoder.decode(a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	
	
	public VoipChangeConfigDAO getDao() {
		return dao;
	}

	public void setDao(VoipChangeConfigDAO dao) {
		this.dao = dao;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


}
