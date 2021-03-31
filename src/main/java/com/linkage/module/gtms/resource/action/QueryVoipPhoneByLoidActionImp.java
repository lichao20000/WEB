package com.linkage.module.gtms.resource.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.resource.serv.QueryVoipPhoneByLoidServ;


public class QueryVoipPhoneByLoidActionImp implements QueryVoipPhoneByLoidAction{
	
	private static Logger logger = LoggerFactory
			.getLogger(QueryVoipPhoneByLoidActionImp.class);
	
	/** 导入的Excel文件 */
	private File file = null;
	
	/** 只解析前 rowNum 行数据 */
	private int rowNum = 0;
	
	/** 未关联LOID.txt */
	private String fileName1 = "";
	
	/** 查询结果文件.xls */
	private String fileName2 = "";
	
	private String retResult = "";
	
	private InputStream exportExcelStream;
	
	/**  */
	private QueryVoipPhoneByLoidServ bio ; 
	
	
	/**
	 * 初始化界面
	 */
	public String execute(){
		
		logger.debug("QueryVoipPhoneByLoidActionImp==>init()");
		
		return "init";
	}
	
	
	/**
	 * 下载Excel模板
	 * @return
	 */
	public String downloadTemplate(){
         return "toExport";
	}
	
	
	/**
	 * 根据LOID查询VOIP语音电话号码
	 */
	public String queryVoipPhoneByLoid(){
		
		logger.debug("QueryVoipPhoneByLoidActionImp==>queryVoipPhoneByLoid()");
		
		String result = bio.queryVoipPhoneByLoid(this.file, this.rowNum);
		String [] restArr = result.split(",");
		if ("0".equals(restArr[0])) {
			setRetResult(restArr[0]);
			setFileName1(restArr[1]);
			setFileName2(restArr[2]);
		} else {
			setRetResult(restArr[0]);
		}
		
		return "init";
	}

	
	public InputStream getExportExcelStream() {
		
		FileInputStream fio = null;
		
		String filePath = LipossGlobals.getLipossHome() +File.separator+ "temp" + File.separator + "batchImportTemplate.xls";
		
		try {
			fio = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			logger.warn("读取文件异常", e);
		}
		return fio;
	}

	public void setExportExcelStream(InputStream exportExcelStream) {
		this.exportExcelStream = exportExcelStream;
	}
	
	
	
	public File getFile() {
		return file;
	}


	
	public void setFile(File file) {
		this.file = file;
	}


	
	public QueryVoipPhoneByLoidServ getBio() {
		return bio;
	}


	
	public void setBio(QueryVoipPhoneByLoidServ bio) {
		this.bio = bio;
	}


	
	public int getRowNum() {
		return rowNum;
	}


	
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}


	
	public String getFileName1() {
		return fileName1;
	}


	
	public void setFileName1(String fileName1) {
		this.fileName1 = fileName1;
	}


	
	public String getFileName2() {
		return fileName2;
	}


	
	public void setFileName2(String fileName2) {
		this.fileName2 = fileName2;
	}


	
	public String getRetResult() {
		return retResult;
	}


	
	public void setRetResult(String retResult) {
		this.retResult = retResult;
	}
	
}
