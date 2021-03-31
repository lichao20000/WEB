/**
 * 
 */
package com.linkage.module.gtms.stb.report.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gtms.stb.report.bio.BatchImpQryStbInfoBIO;
import com.linkage.module.gwms.share.act.FileUploadAction;

/**
 * 工单报表
 * fanjm
 * @author HP (AILK No.)
 * @version 1.0
 * @since 2018-4-20
 * @category com.linkage.module.gtms.stb.report.act
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class BatchImpQryStbInfoACT extends splitPageAction implements SessionAware, ServletResponseAware
  {

	//	日志记录
	private static Logger logger = LoggerFactory.getLogger(BatchImpQryStbInfoACT.class);
	
	private Map session;
	
	private HttpServletResponse response;
	
	// bio
	private BatchImpQryStbInfoBIO bio;
	
	/** 导入的类型 **/
	private String importQueryField;
	
	/** 结束时间 **/
	private String msg;
	
	private int total;
	
	/** 终端类型 **/
	private String gwShare_fileName;
	
	private List<Map> statsReportList;
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

	/**
	 * 查询数据
	 * @return
	 */
	public String impStb()
	{
		msg = "succ";
		if(null!=gwShare_fileName){
			gwShare_fileName.trim();
		}

		if(gwShare_fileName.length()<4){
			this.msg = "上传的文件名不正确！";
			return "list";
		}
		String fileName_ = gwShare_fileName.substring(gwShare_fileName.length()-3, gwShare_fileName.length());
		logger.warn("fileName_;{}",fileName_+",gwShare_fileName="+gwShare_fileName);
		if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
			this.msg = "上传的文件格式不正确！";
			return "list";
		}
		List<String> dataList = new ArrayList<String>();
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXTStb(gwShare_fileName);
			}else{
				dataList = getImportDataByXLSStb(gwShare_fileName);
			}
			logger.warn("...................文件解析完成，size="+dataList.size());
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！",gwShare_fileName);
			this.msg = "文件没找到！";
			return "list";
		}catch(IOException e){
			logger.warn("{}文件解析出错！",gwShare_fileName);
			this.msg = "文件解析出错！";
			return "list";
		}catch(Exception e){
			logger.warn("{}文件解析出错！",gwShare_fileName);
			this.msg = "文件解析出错！";
			return "list";
		}

		this.total = dataList.size();
		if(total>2000){
			this.msg = "导入文件中数据量应小于2000！";
			return "list";
		}
		else if(total<=0){
			this.msg = "导入文件中数据量为0！";
			return "list";
		}
		
		//导入数据到临时表
		try
		{
			bio.insertDate(dataList);
		}
		catch (Exception e)
		{
			this.msg = e.getMessage();
			return "list";
		}
		session.put("importQueryField",importQueryField);
		statsReportList = bio.getResultPage(curPage_splitPage,num_splitPage,importQueryField);
		logger.warn("statsReportList="+statsReportList.size());
		maxPage_splitPage = bio.getCount(curPage_splitPage,num_splitPage,importQueryField);
		return "list";
	}
	
	/**
	 * 非导入的时候，即分页的时候查询
	 * @return
	 */
	public String queryStb()
	{
		msg = "succ";
		importQueryField = (String) session.get("importQueryField");
		statsReportList = bio.getResultPage(curPage_splitPage,num_splitPage,importQueryField);
		logger.warn("statsReportList="+statsReportList.size());
		maxPage_splitPage = bio.getCount(curPage_splitPage,num_splitPage,importQueryField);
		return "list";
	}
	
	
	
	
	/**
	 * 查询数据
	 * @return
	 */
	public String imp()
	{
		msg = "succ";
		if(null!=gwShare_fileName){
			gwShare_fileName.trim();
		}

		if(gwShare_fileName.length()<4){
			this.msg = "上传的文件名不正确！";
			return "listItms";
		}
		String fileName_ = gwShare_fileName.substring(gwShare_fileName.length()-3, gwShare_fileName.length());
		logger.warn("fileName_;{}",fileName_+",gwShare_fileName="+gwShare_fileName);
		if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
			this.msg = "上传的文件格式不正确！";
			return "listItms";
		}
		List<String> dataList = new ArrayList<String>();
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(gwShare_fileName);
			}else{
				dataList = getImportDataByXLS(gwShare_fileName);
			}
			logger.warn("...................文件解析完成，size="+dataList.size());
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！",gwShare_fileName);
			this.msg = "文件没找到！";
			return "listItms";
		}catch(IOException e){
			logger.warn("{}文件解析出错！",gwShare_fileName);
			this.msg = "文件解析出错！";
			return "listItms";
		}catch(Exception e){
			logger.warn("{}文件解析出错！",gwShare_fileName);
			this.msg = "文件解析出错！";
			return "listItms";
		}

		this.total = dataList.size();
		if(total>2000){
			this.msg = "导入文件中数据量应小于2000！";
			return "listItms";
		}
		else if(total<=0){
			this.msg = "导入文件中数据量为0！";
			return "listItms";
		}
		
		//导入数据到临时表
		try
		{
			bio.insertDate(dataList);
		}
		catch (Exception e)
		{
			this.msg = e.getMessage();
			return "listItms";
		}
		session.put("importQueryField",importQueryField);
		statsReportList = bio.getResultPageItms(curPage_splitPage,num_splitPage,importQueryField);
		logger.warn("statsReportList="+statsReportList.size());
		maxPage_splitPage = bio.getCountItms(curPage_splitPage,num_splitPage,importQueryField);
		return "listItms";
	}
	
	
	public String query()
	{
		msg = "succ";
		importQueryField = (String) session.get("importQueryField");
		logger.warn("importQueryField="+importQueryField);
		statsReportList = bio.getResultPageItms(curPage_splitPage,num_splitPage,importQueryField);
		logger.warn("statsReportList="+statsReportList.size());
		maxPage_splitPage = bio.getCountItms(curPage_splitPage,num_splitPage,importQueryField);
		return "listItms";
	}
	
	
	
	/**
	 * 导出设备列表
	 * @return
	 */
	public String getExcelStb() {
		logger.warn("getExcel()");
		statsReportList = bio.getResult(importQueryField);
		logger.warn("statsReportList.size="+statsReportList.size());
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "IPTV账号#设备序列号#属地#厂商#型号#软件版本#硬件版本#最近上线时间";
		excelCol = "serv_account#device_serialnumber#city_id#vendor_name#device_model#softwareversion#hardwareversion#cpe_currentupdatetime";
		
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "devList";
		data = statsReportList;
		return "excel";
	}
	
	
	/**
	 * 导出设备列表
	 * @return
	 */
	public String getExcel() {
		logger.warn("getExcel()");
		statsReportList = bio.getResultItms(importQueryField);
		logger.warn("statsReportList.size="+statsReportList.size());
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "LOID#设备序列号#宽带账号#属地#厂商#型号#软件版本#硬件版本#最近上线时间";
		excelCol = "loid#device_serialnumber#broadbandname#city_id#vendor_name#device_model#softwareversion#hardwareversion#cpe_currentupdatetime";
		
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "devList";
		data = statsReportList;
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
	public List<String> getImportDataByTXTStb(String fileName) throws FileNotFoundException,IOException{
		logger.debug("getImportDataByTXT{}",fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
		 * 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if(null!=line && "设备序列号".equals(line)){
			this.importQueryField = "device_serialnumber";
		}else{
			this.importQueryField = "username";
		}
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if(!"".equals(line.trim())){
				list.add(line.trim());
			}
		}
		in.close();
		in = null;

		return list;
	}
	
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,IOException{
		logger.debug("getImportDataByTXT{}",fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
		 * 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if(null!=line && "设备序列号".equals(line)){
			this.importQueryField = "device_serialnumber";
		}
		else if(null!=line && "loid".equals(line.toLowerCase())){
			this.importQueryField = "loid";
		}else{
			this.importQueryField = "username";
		}
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if(!"".equals(line.trim())){
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
		Workbook wwb = Workbook.getWorkbook(f);
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
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
				 * 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if(null!=line && "设备序列号".equals(line)){
					this.importQueryField = "device_serialnumber";
				}
				else if(null!=line && "loid".equals(line.toLowerCase())){
					this.importQueryField = "loid";
				}
				else{
					this.importQueryField = "username";
				}
			}

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
	
	
	public List<String> getImportDataByXLSStb(String fileName) throws BiffException, IOException{
		logger.debug("getImportDataByXLS{}",fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath()+fileName);
		Workbook wwb = Workbook.getWorkbook(f);
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
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
				 * 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if(null!=line && "设备序列号".equals(line)){
					this.importQueryField = "device_serialnumber";
				}else{
					this.importQueryField = "username";
				}
			}

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
	
	/**
	 * 统计结果excel导出
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	/*public String getExcel() throws Exception
	{
		logger.debug("getExcel");
		this.setTime();
		statsReportList = bio.getStatsReport(deviceType, cityId, servTypeId, startOpenDate, endOpenDate);
		ExportExcelUtil util = new ExportExcelUtil("工单统计报表查询结果", new String[]{"属地", "成功数", "失败数", "未做数", "成功率"});
		util.export(response, new ArrayList(statsReportList), new String[]{"cityName", "succNum", "failNum", "notNum", "percent" }, "stats");
		return null;
	}*/
	
	/**
	 * 详细信息excel导出
	 * @return
	 * @throws Exception 
	 */
	/*public String getDetailExcel() throws Exception
	{
		logger.debug("getDetailExcel");
		//this.setTime();
		detailReportList = bio.getDetailReport(cityId,openStatus,deviceType, servTypeId, startOpenDate, endOpenDate);
		
		// 转换数据,方便导出
		bio.transData(detailReportList);
		ExportExcelUtil util = new ExportExcelUtil("工单统计报表详细查询", new String[]{"逻辑SN", "属地", "BSS受理时间", "业务类型", "设备序列号", "开通状态", "BSS终端类型"});
		util.export(response, detailReportList, new String[]{"username", "city_name", "dealdate", "serv_type_name", "device_serialnumber", "open_status", "type_id" }
			, "detail");
		return null;
	}*/
	
	
	
	//** getters and setters **//
	
	public BatchImpQryStbInfoBIO getBio() {
		return bio;
	}

	public void setBio(BatchImpQryStbInfoBIO bio) {
		this.bio = bio;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}


	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}


	public List<Map> getStatsReportList() {
		return statsReportList;
	}

	public void setStatsReportList(List<Map> statsReportList) {
		this.statsReportList = statsReportList;
	}


	public void setServletResponse(HttpServletResponse resp) {
		this.response = resp;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}


	
	public String getImportQueryField()
	{
		return importQueryField;
	}


	
	public void setImportQueryField(String importQueryField)
	{
		this.importQueryField = importQueryField;
	}


	
	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}


	
	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}


	
	public List<Map> getData()
	{
		return data;
	}


	
	public void setData(List<Map> data)
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

	
	
	
}
