package com.linkage.module.gwms.report.bio;

import com.linkage.commons.thread.ThreadPoolCommon;
import com.linkage.module.gwms.report.dao.MulticastDownReportDAO;
import com.linkage.module.gwms.report.thread.MulticastDownThread;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.StringUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class MulticastDownReportBIO {
	Logger logger = LoggerFactory.getLogger(MulticastDownReportBIO.class);
	// 持久层
	MulticastDownReportDAO dao;

	//回传消息
	private String msg = null;



	public List<Map> queryDeviceList(String fileName) {

		if(fileName.length()<4){
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
		logger.debug("fileName_;{}",fileName_);
		if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
			}
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！{}",fileName,e);
			this.msg = "文件没找到！";
			return null;
		}catch(IOException e){
			logger.warn("{}文件解析出错！{}",fileName,e);
			this.msg = "文件解析出错！";
			return null;
		}catch(Exception e){
			logger.warn("{}文件解析出错！{}",fileName,e);
			this.msg = "文件解析出错！";
			return null;
		}
		Vector<Map> vector = new Vector<Map>();
		List<Map> list = new ArrayList<Map>();
		if(dataList.isEmpty() || null == dataList)
		{
			logger.warn("[{}]文件为空，直接返回",fileName);
			return null;
		}
		if(dataList.size() > 2000)
		{
			logger.warn("[{}]文件超过2000行，返回空",fileName);
			msg = StringUtil.getStringValue(dataList.size());
			return null;
		}
		logger.warn("查询数据库，数据处理");
		ThreadPoolCommon threadPool = ThreadPoolCommon.getFixedThreadPool(5);
		for(String loid:dataList)
		{
			threadPool.execute(new MulticastDownThread(loid,"1",vector));
		}
		threadPool.shutdown();
		while (true)
		{
			if(threadPool.isTerminated())
			{
				break;
			}
		}


		for (Map result: vector ) {
			list.add(result);
		}

		return list;
	}

	public List<Map> queryBusinessList(String fileName) {
		if(fileName.length()<4){
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
		logger.debug("fileName_;{}",fileName_);
		if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
			}
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！{}",fileName,e);
			this.msg = "文件没找到！";
			return null;
		}catch(IOException e){
			logger.warn("{}文件解析出错！{}",fileName,e);
			this.msg = "文件解析出错！";
			return null;
		}catch(Exception e){
			logger.warn("{}文件解析出错！{}",fileName,e);
			this.msg = "文件解析出错！";
			return null;
		}
		Vector<Map> vector = new Vector<Map>();
		List<Map> list = new ArrayList<Map>();
		if(dataList.isEmpty() || null == dataList)
		{
			logger.warn("[{}]文件为空，直接返回",fileName);
			return null;
		}
		if(dataList.size() > 2000)
		{
			logger.warn("[{}]文件超过2000行，返回空",fileName);
			msg = StringUtil.getStringValue(dataList.size());
			return null;
		}
		logger.warn("查询数据库，数据处理");
		ThreadPoolCommon threadPool = ThreadPoolCommon.getFixedThreadPool(5);
		for(String loid:dataList)
		{
			threadPool.execute(new MulticastDownThread(loid,"2",vector));
		}
		threadPool.shutdown();
		while (true)
		{
			if(threadPool.isTerminated())
			{
				break;
			}
		}

		logger.warn("                             " +vector.size());
		for (Map result: vector) {
			list.add(result);
		}
		logger.warn("                             " +list.size());

		for (Map map : list
			 ) {
			Set<String> set = map.keySet();
			for (String key :set
			) {
				logger.warn("key : [{}],value:[{}]",key,map.get(key));
			}
		}

		return list;
	}


	/**
	 * 解析文件（txt格式）
	 *
	 * @param fileName 文件名
	 * @return
	 * @throws FileNotFoundException
	 * 		   IOException
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,IOException{
		logger.debug("getImportDataByTXT{}",fileName);
		List<String> list = new ArrayList<String>();
		//读取文件不设置编码，导致为中文时显示乱码，无法识别
		//BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		FileInputStream in = new FileInputStream(getFilePath()+fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		String line = reader.readLine();
		logger.warn("第一行：[{}]", line);
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = reader.readLine()) != null) {
			if(!StringUtil.IsEmpty(line) && !list.contains(line.trim())){
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
			//int columeCount = ws.getColumns();

			if(null!=ws.getCell(0,0).getContents()){
				String line = ws.getCell(0,0).getContents().trim();
			}

			//取当前页所有值放入list中
			for (int i=1;i<rowCount;i++){
				String temp = ws.getCell(0, i).getContents();
				if(!com.linkage.commons.util.StringUtil.IsEmpty(temp) && !list.contains(temp.trim())){
					list.add(temp.trim());
				}
			}
		}
		//f.delete();
		f = null;
		return list;
	}

	public int getFileSize(String fileName) {
		if(fileName.length()<4){
			this.msg = "上传的文件名不正确！";
			return 0;
		}
		String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
		logger.debug("fileName_;{}",fileName_);
		if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
			this.msg = "上传的文件格式不正确！";
			return 0;
		}
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
			}
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！{}",fileName,e);
			this.msg = "文件没找到！";
			return 0;
		}catch(IOException e){
			logger.warn("{}文件解析出错！{}",fileName,e);
			this.msg = "文件解析出错！";
			return 0;
		}catch(Exception e){
			logger.warn("{}文件解析出错！{}",fileName,e);
			this.msg = "文件解析出错！";
			return 0;
		}

		return dataList.size();
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

	public MulticastDownReportDAO getDao() {
		return dao;
	}

	public void setDao(MulticastDownReportDAO dao) {
		this.dao = dao;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}



}
