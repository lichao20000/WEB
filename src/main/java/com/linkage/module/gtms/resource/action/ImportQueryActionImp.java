package com.linkage.module.gtms.resource.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.resource.serv.ImportQueryServImp;

public class ImportQueryActionImp extends splitPageAction implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory
			.getLogger(ImportQueryActionImp.class);

	/** 需要导入的文件（Excel2003 或者 TXT文本文件） */
	private File file = null;

	/** 只解析前rowNum行数据 */
	private int rowNum = 0;

	/** 回参 **/
	private List<Map> retResult = null;

	private List<Map> errorResult = null;

	/** 所导入文件的文件名 */
	private String fileName = null;

	/** request取登陆帐号使用 */
	private HttpServletRequest request;

	/** 上传的文件类型 */
	private String fileType = null;

	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名

	private ImportQueryServImp bio = null;

	/**
	 * 解析导入的文件（Excel2003 或则 TXT文本）
	 * @throws IOException 
	 */
	public String readUploadFile() throws IOException {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		logger.warn("file:" + this.file);
		Map tmp = bio.readUploadFile(curPage_splitPage,num_splitPage,this.file, this.rowNum, curUser,fileType, fileName,"","readUploadFile");
		retResult = (List<Map>)tmp.get("list");
		maxPage_splitPage = StringUtil.getIntValue(tmp, "maxPage");
		String field = StringUtil.getStringValue(tmp,"fieldname");
		session.setAttribute(this.fileName, field);
		session.setAttribute(this.fileName + "maxPage", maxPage_splitPage);
		errorResult = bio.getErrorData();
		return "init";
	}
	
	/**
	 * 分页
	 * @return
	 * @throws IOException
	 */
	public String goPage() throws IOException {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String field = (String)session.getAttribute(this.fileName);
		Map tmp = bio.readUploadFile(curPage_splitPage,num_splitPage,this.file, this.rowNum, curUser,fileType, fileName,field,"goPage");
		retResult = (List<Map>)tmp.get("list");
		logger.warn("---" + session.getAttribute(this.fileName + "maxPage"));
		maxPage_splitPage = (Integer)session.getAttribute(this.fileName + "maxPage");//StringUtil.getIntValue(tmp, "maxPage");
		errorResult = bio.getErrorData();
		return "init";
	}
	

	public String getExcel() {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String field = (String)session.getAttribute(this.fileName);
		if((Integer)session.getAttribute(this.fileName + "maxPage") == 0){
			retResult = new ArrayList<Map>();
		}else {
			curPage_splitPage = 1;
			num_splitPage = (Integer)session.getAttribute(this.fileName + "maxPage") * num_splitPage;
			Map tmp = bio.readUploadFile(curPage_splitPage,num_splitPage,this.file, this.rowNum, curUser,fileType, fileName,field,"getExcel");
			retResult = (List<Map>)tmp.get("list");
		}
		//retResult = bio.readUploadFile(this.file, this.rowNum, curUser,fileType, fileName);
		String excelCol = "default#city_name#device_serialnumber#username#vendor#device_model#hardwareversion#softwareversion#dev_spec_id#user_spec_id";
		String excelTitle = "原始文件类容#属地#设备序列号#用户LOID#厂商#型号#硬件版本#软件版本#终端规格#用户规格";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "批量导入查询详细信息";
		data = retResult;
		return "excel";
	}
	
	
	public String getExcelBak() {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		retResult = bio.readUploadFile(this.file, this.rowNum, curUser,fileType, fileName);
		String excelCol = "default#city_name#device_serialnumber#username#vendor#device_model#hardwareversion#softwareversion#dev_spec_id#user_spec_id";
		String excelTitle = "原始文件类容#属地#设备序列号#用户LOID#厂商#型号#硬件版本#软件版本#终端规格#用户规格";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "批量导入查询详细信息";
		data = retResult;
		return "excel";
	}

	/**
	 * 下载Excel模板
	 * 
	 * @return
	 */
	public String downloadTemplate() {
		return "toExport";
	}

	/**
	 * 读取Excel2003模板文件
	 * 
	 * @return
	 */
	public InputStream getExportExcelStream() {
		logger.debug("ImportDeviceInitImp==>getExportExcelStream()");
		FileInputStream fio = null;
		String filePath = LipossGlobals.getLipossHome() + File.separator
				+ "temp" + File.separator + "ImportQuery.xls";
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

	public ImportQueryServImp getBio() {
		return bio;
	}

	public void setBio(ImportQueryServImp bio) {
		this.bio = bio;
	}

	public List<Map> getRetResult() {
		return retResult;
	}

	public void setRetResult(List<Map> retResult) {
		this.retResult = retResult;
	}

	public List<Map> getErrorResult() {
		return errorResult;
	}

	public void setErrorResult(List<Map> errorResult) {
		this.errorResult = errorResult;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
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

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

}
