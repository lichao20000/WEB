package com.linkage.module.gwms.resource.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.bio.BatchRestartBIO;
import com.linkage.module.gwms.share.act.FileUploadAction;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-10-11
 * @category com.linkage.module.gwms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings("rawtypes")
public class BatchRestartACT  implements ServletRequestAware, ServletResponseAware,SessionAware
{
	private static Logger logger = LoggerFactory.getLogger(BatchRestartACT.class);
	private HttpServletResponse response;
	private BatchRestartBIO bio;
	/** case选择 */
	private String caseDownload;
	private String gwShare_fileName;
	// 查询条件
	private String importQueryField = "username";
	// 回传消息
	private String msg = null;
	private String ajax;
	private Map session;
	private String batchType;
	
	public String query()
	{
		logger.warn("query()方法入口");
		UserRes curUser = (UserRes) session.get("curUser");
		String user_Account = String.valueOf(curUser.getUser().getAccount());
		if (null != gwShare_fileName){
			gwShare_fileName.trim();
		}
		String fileName_ = gwShare_fileName.substring(gwShare_fileName.length() - 3,
				gwShare_fileName.length());
		logger.debug("fileName_:{}", fileName_);
		if (!"xls".equals(fileName_) && !"txt".equals(fileName_))
		{
			this.msg = "上传的文件格式不正确！";
			return null;
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
			return null;
		}catch(Exception e){
			logger.warn("{}文件解析出错！",gwShare_fileName);
			this.msg = "文件解析出错！";
			return null;
		}
		
		if(StringUtil.IsEmpty(importQueryField)){
			ajax="-9";
		}else{
			ajax=bio.insert(dataList, importQueryField,user_Account,batchType);
		}
		return "ajax";
	}
	
	public String fileRow()
	{
		String total=LipossGlobals.getLipossProperty("batchRestart.fileRow");
		ajax=total;
		return "ajax";
	}
	
	/**
	 * 查看今日剩余数
	 */
	public String test()
	{
		logger.warn("test()方法入口");
		ajax=bio.test(batchType);
		return "ajax";
	}
	
	/**
	 * 下载模板
	 */
	public String downModle()
	{
		logger.warn("downModle()方法入口");
		if (!StringUtil.IsEmpty(caseDownload))
		{
			// 文件路径
			String storePath = LipossGlobals.getLipossProperty("uploaddir");
			
			String xlsPath=storePath + "/批量重启XLS模板.xls";
			String txtPath=storePath + "/批量重启TXT模板.txt";
			if("4".equals(batchType) 
					&& Global.NXDX.equals(Global.instAreaShortName))
			{
				storePath+="/stbTemp";
				xlsPath=storePath + "/batchRestartStbTemplate_XLS.xls";
				txtPath=storePath + "/batchRestartStbTemplate_TXT.txt";
			}
			
			if ("0".equals(caseDownload)){
				bio.download(xlsPath, response);
			}else{
				bio.download(txtPath, response);
			}
		}
		return null;
	}
	
	/**
	 * 获取文件路径
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

	/**
	 * 解析文件（xls格式）
	 */
	public List<String> getImportDataByXLS(String fileName) throws BiffException,IOException
	{
		logger.debug("getImportDataByXLS{}", fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++)
		{
			ws = wwb.getSheet(m);
			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			// int columeCount = ws.getColumns();
			if (null != ws.getCell(0, 0).getContents())
			{
				String line = ws.getCell(0, 0).getContents().trim();
				if(Global.NXDX.equals(Global.instAreaShortName) 
						&& "4".equals(batchType)){
					if(!"userName".equals(line)){
						importQueryField=null;
					}
				}else{
					if ("设备序列号".equals(line)){
						importQueryField = "device_serialnumber";
					}else if("LOID".equals(line) || "device_id".equals(line)){
						importQueryField = "device_id";
					}else{
						importQueryField=null;
					}
				}
			}
			
			if(importQueryField==null){
				return null;
			}
			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++)
			{
				String temp = ws.getCell(0, i).getContents();
				if (!StringUtil.IsEmpty((temp)) && !list.contains(temp.trim()))
				{
					list.add(temp.trim());
				}
			}
		}
		f = null;
		return list;
	}

	/**
	 * 解析文件（txt格式）
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,IOException
	{
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + fileName));
		String line = in.readLine();
		
		if(Global.NXDX.equals(Global.instAreaShortName) 
				&& "4".equals(batchType)){
			if(!"userName".equals(line)){
				importQueryField=null;
			}
		}else{
			if ("设备序列号".equals(line)){
				importQueryField = "device_serialnumber";
			}else if("LOID".equals(line) || "device_id".equals(line)){
				importQueryField = "device_id";
			}else{
				importQueryField = null;
			}
		}
		
		if(importQueryField == null){
			return null;
		}
		
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null)
		{
			if (!"".equals(line.trim()) && !list.contains(line.trim())){
				list.add(line.trim());
			}
		}
		
		in.close();
		in = null;
		return list;
	}

	public HttpServletResponse getResponse(){
		return response;
	}

	public void setResponse(HttpServletResponse response){
		this.response = response;
	}

	public String getCaseDownload(){
		return caseDownload;
	}

	public void setCaseDownload(String caseDownload){
		this.caseDownload = caseDownload;
	}

	public String getGwShare_fileName(){
		return gwShare_fileName;
	}

	public BatchRestartBIO getBio(){
		return bio;
	}

	public void setBio(BatchRestartBIO bio){
		this.bio = bio;
	}

	public void setGwShare_fileName(String gwShare_fileName){
		this.gwShare_fileName = gwShare_fileName;
	}

	@Override
	public void setServletResponse(HttpServletResponse response){
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request){
	}

	public String getMsg(){
		return msg;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getImportQueryField(){
		return importQueryField;
	}

	public void setImportQueryField(String importQueryField){
		this.importQueryField = importQueryField;
	}
	
	public String getAjax(){
		return ajax;
	}
	
	public void setAjax(String ajax){
		this.ajax = ajax;
	}
	
	public Map getSession(){
		return session;
	}
	
	public void setSession(Map session){
		this.session = session;
	}
	
	public String getBatchType() {
		return batchType;
	}
	
	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}
	
}
