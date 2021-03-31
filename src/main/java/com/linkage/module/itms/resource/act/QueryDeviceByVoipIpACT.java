package com.linkage.module.itms.resource.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.itms.resource.bio.QueryDeviceByVoipIpBIO;
import com.opensymphony.xwork2.Action;

/**
 * 
 * @author Reno (Ailk NO.)
 * @version 1.0
 * @since 2015年1月29日
 * @category action.resource
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class QueryDeviceByVoipIpACT extends splitPageAction
{
	private static Logger logger = LoggerFactory.getLogger(QueryDeviceByVoipIpACT.class);
	private static final long serialVersionUID = 1L;
	
	private QueryDeviceByVoipIpBIO bio;
	
	private String gwShare_fileName;
	private String msg;
	private List<Map<String, Object>> data;
	private String[] column;
	private String[] title;
	private String fileName;
	private String ajax;
	
	public String init() throws Exception{
		logger.warn("init() execute...");
		return "init";
	}
	
	@Override
	public String execute() throws Exception
	{
		String fileName_ = gwShare_fileName.substring(gwShare_fileName.length()-3, gwShare_fileName.length());
		logger.warn("gwShare_fileName = {}, fileName_ = {}", gwShare_fileName, fileName_);
		
		if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
			this.msg = "上传的文件格式不正确！";
			return Action.SUCCESS;
		}
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(gwShare_fileName);
			}else{
				dataList = getImportDataByXLS(gwShare_fileName);
			}
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！",gwShare_fileName);
			this.msg = "文件没找到！";
			return Action.SUCCESS;
		}catch(IOException e){
			logger.warn("{}文件解析出错！",gwShare_fileName);
			this.msg = "文件解析出错！";
			return Action.SUCCESS;
		}catch(Exception e){
			logger.warn("{}文件解析出错！",gwShare_fileName);
			this.msg = "文件解析出错！";
			return Action.SUCCESS;
		}
		logger.warn("数据分析完毕，"+dataList);
		dataList = dataList == null?new ArrayList<String>():dataList;
		if(dataList.size()>1000){
			this.msg = "单次只能导入1000行，请分次导入！";
			return Action.SUCCESS;
		}
		// 清空临时表
		bio.truncate();
		// 将文件分析出来的数据插入到临时表中
		bio.insertTempTable(dataList);
		// 分页查询
		data = bio.dealQueryPaged(curPage_splitPage, num_splitPage);
		totalRowCount_splitPage = bio.queryCount();
		logger.warn("分页查询结果：总共{}条，当前第{}页，每页展示{}行， data={}",new Object[]{ totalRowCount_splitPage, curPage_splitPage, num_splitPage, data});
		return Action.SUCCESS;
	}
	/**
	 * 获得前台导入的IP个数
	 * @return
	 */
	public String ipCount(){
		String fileName_ = gwShare_fileName.substring(gwShare_fileName.length()-3, gwShare_fileName.length());
		logger.warn("gwShare_fileName = {}, fileName_ = {}", gwShare_fileName, fileName_);
		
		if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
			// this.msg = "上传的文件格式不正确！";
			ajax = "-1";
		}
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(gwShare_fileName);
			}else{
				dataList = getImportDataByXLS(gwShare_fileName);
			}
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！",gwShare_fileName);
			// this.msg = "文件没找到！";
			ajax = "-2";
		}catch(IOException e){
			logger.warn("{}文件解析出错！",gwShare_fileName);
			// this.msg = "文件解析出错！";
			ajax = "-3";
		}catch(Exception e){
			logger.warn("{}文件解析出错！",gwShare_fileName);
			// this.msg = "文件解析出错！";
			ajax = "-4";
		}
		dataList = dataList == null?new ArrayList<String>():dataList;
		ajax = dataList.size()+"";
		return "ajax";
	}
	
	public String getExcel(){
		String fileName_ = gwShare_fileName.substring(gwShare_fileName.length()-3, gwShare_fileName.length());
		logger.warn("gwShare_fileName = {}, fileName_ = {}", gwShare_fileName, fileName_);
		
		if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
			this.msg = "上传的文件格式不正确！";
			return Action.SUCCESS;
		}
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(gwShare_fileName);
			}else{
				dataList = getImportDataByXLS(gwShare_fileName);
			}
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！",gwShare_fileName);
			this.msg = "文件没找到！";
			return Action.SUCCESS;
		}catch(IOException e){
			logger.warn("{}文件解析出错！",gwShare_fileName);
			this.msg = "文件解析出错！";
			return Action.SUCCESS;
		}catch(Exception e){
			logger.warn("{}文件解析出错！",gwShare_fileName);
			this.msg = "文件解析出错！";
			return Action.SUCCESS;
		}
		logger.warn("数据分析完毕，"+dataList);
		dataList = (dataList == null)?new ArrayList<String>():dataList;
		if(dataList.size()>1000){
			this.msg = "单次最多只能导入1000行，请分次导入！";
			return Action.SUCCESS;
		}
		// 清空临时表
		bio.truncate();
		// 将文件分析出来的数据插入到临时表中
		bio.insertTempTable(dataList);
		// 查询数据
		data = bio.dealQuery();
		fileName = "家庭网关语音信息查询";
		column = new String[]{"ipaddress", "username", "city_name", "device_serialnumber", "vendor_name", "device_model", "softwareversion"};
		title = new String[]{"语音IP", "LOID", "局向", "终端序列号", "终端厂家", "终端型号", "终端软件版本"};
		return "excel";
	}
	
	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws FileNotFoundException 
	 * 		   IOException
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,
	IOException
	{
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + fileName));
		String line = in.readLine();
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null)
		{
			if (!"".equals(line.trim()))
			{
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		return list;
	}
	
	/**
	 * 解析文件（xls格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws  
	 * 		   
	 */
	public List<String> getImportDataByXLS(String fileName) throws BiffException, IOException{
		logger.debug("getImportDataByXLS{}",fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath()+fileName);
		Workbook wwb = Workbook.getWorkbook(f);;
		Sheet ws = null;
		//总sheet数
		//int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m=0;m<sheetNumber;m++){
			ws = wwb.getSheet(m);
			
			//当前页总记录行数和列数
			int rowCount = ws.getRows();
			
			//取当前页所有值放入list中
			for (int i=1;i<rowCount;i++){
				String temp = ws.getCell(0, i).getContents();
				if(null!=temp && !"".equals(temp)){
					if(!"".equals(ws.getCell(0, i).getContents().trim())){
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
		}
		f = null;
		return list;
	}
	
	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径",lipossHome);
		return lipossHome + "/temp/";
	}

	
	public QueryDeviceByVoipIpBIO getBio()
	{
		return bio;
	}

	
	public void setBio(QueryDeviceByVoipIpBIO bio)
	{
		this.bio = bio;
	}

	
	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}

	
	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}

	
	public String getMsg()
	{
		return msg;
	}

	
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	
	public List<Map<String, Object>> getData()
	{
		return data;
	}

	
	public void setData(List<Map<String, Object>> data)
	{
		this.data = data;
	}

	
	public String[] getColumn()
	{
		return column;
	}

	
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	
	public String[] getTitle()
	{
		return title;
	}

	
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	
	public String getFileName()
	{
		return fileName;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	public String getAjax()
	{
		return ajax;
	}

	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	
	
}
