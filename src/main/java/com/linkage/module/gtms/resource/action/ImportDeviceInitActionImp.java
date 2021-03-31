package com.linkage.module.gtms.resource.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.resource.serv.ImportDeviceInitServ;


/**
 * 
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since Apr 19, 2013 10:12:10 AM
 * @category com.linkage.module.gtms.resource.action
 * @copyright 南京联创科技 网管科技部
 *
 * 安徽电信 导入需要采购的设备  
 *
 */
public class ImportDeviceInitActionImp implements ImportDeviceInitAction, ServletRequestAware {
	
	private static Logger logger = LoggerFactory.getLogger(ImportDeviceInitActionImp.class);
	
	/** 需要导入的文件（Excel2003 或者 TXT文本文件） */
	private File file = null;
	
	/** 只解析前rowNum行数据 */
	private int rowNum = 0;
	
	/** 回参 **/
	private String retResult = null;
	
	/** 所导入文件的文件名 */
	private String fileName = null;
	
	/** request取登陆帐号使用 */
	private HttpServletRequest request;
	
	/** 网关标志 1：家庭网关  2：企业网关 */
	private String gw_type = null;
	
	/** 上传的文件类型 */
	private String fileType = null;
	
	private ImportDeviceInitServ bio = null ;
	
	
	
	/**
	 * 初始化导入界面
	 */
	public String execute() {	
		logger.debug("ImportDeviceInitImp==>execute()");
		return "init";
	}

	
	/**
	 * 解析导入的文件（Excel2003 或则 TXT文本）
	 */
	public String readUploadFile() {
		
		logger.debug("ImportDeviceInitImp==>readUploadFile()");
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		
		retResult = bio.readUploadFile(this.file, this.rowNum, curUser, gw_type, fileType, fileName);
		
		return "init";
	}
	
	
	/**
	 * 下载Excel模板
	 * @return
	 */
	public String downloadTemplate(){
		logger.debug("ImportDeviceInitImp==>downloadTemplate()");
		return "toExport";
	}
	
	
	
	/**
	 * 读取Excel2003模板文件
	 * @return
	 */
	public InputStream getExportExcelStream() {
		
		logger.debug("ImportDeviceInitImp==>getExportExcelStream()");
		
		FileInputStream fio = null;
		
		String filePath = LipossGlobals.getLipossHome() +File.separator+ "temp" + File.separator + "batchImportTemplate.xls";
		
		try {
			fio = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			logger.warn("读取文件异常", e);
		}
		return fio;
	}



	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	
	public ImportDeviceInitServ getBio() {
		return bio;
	}

	public void setBio(ImportDeviceInitServ bio) {
		this.bio = bio;
	}

	public String getRetResult() {
		return retResult;
	}

	public void setRetResult(String retResult) {
		this.retResult = retResult;
	}


	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}


	public String getGw_type() {
		return gw_type;
	}
	
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}


	
	public String getFileName() {
		return fileName;
	}


	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	
	public String getFileType() {
		return fileType;
	}


	
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
