package com.linkage.module.ids.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.ids.bio.ReportPeroidUpateBIO;
import com.linkage.module.ids.util.WSClientUtil;

import action.splitpage.splitPageAction;

public class ReportPeroidUpateACT extends splitPageAction implements
		ServletRequestAware, SessionAware {

	// 唯一序列号
	private static final long serialVersionUID = -1780054687279361896L;

	private static Logger logger = LoggerFactory
			.getLogger(ReportPeroidUpateACT.class);

	private Map session;

	private HttpServletRequest request;
	/**task add time*/
	private String add_time;
	/**report time peroid*/
	private String reporttimelist;
	/**target time peroid*/
	private String targettimelist;
	/**name of upload file*/
	private String gwShare_fileName;
	/**deviceSN*/
	private String deviceSN;
	/**user's ID who order the task*/
	private String acc_oid;
	/**one device or batch*/
	private String excute_type;
	/**start of query time*/
	private String starttime;
	/**end of query time*/
	private String endtime;
	/**time convert from starttme*/
	private String starttime1;
	/**time convert from endtime*/
	private String endtime1;
	/**the ID for task*/
	private String taskId;
	/**the date that export to excel*/
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	
	private String[] column; // 需要导出的列名，对应data中的键值
	
	private String[] title; // 显示在导出文档上的列名
	
	private String fileName; // 导出的文件名（不包括后缀名）
	/**show the list of order task*/
	private List IdsList;
	
	private List devList;

	private String ajax;

	private ReportPeroidUpateBIO  bio;
	/**
	 * order the task
	 */
	public String execute() throws Exception {

		logger.warn(
				"ReportPeroidUpateACT-->execute:fileName:{},reporttimelist:{},deviceSN:{},excute_type:{}",
				new Object[] { gwShare_fileName, reporttimelist, deviceSN,
						excute_type });

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		acc_oid = StringUtil.getStringValue(curUser.getUser().getId());
		List<String> deviceList = new ArrayList<String>();
		long currTime = new Date().getTime() / 1000L;
		// 单台设备导入
		if ("0".equals(excute_type)) {
			deviceList.add(deviceSN);
		} else {
			// 批量文件导入
			String fileName_ = gwShare_fileName.substring(
					gwShare_fileName.length() - 3, gwShare_fileName.length());
			try {
				if ("txt".equals(fileName_)) {
					deviceList = getImportDataByTXT(gwShare_fileName);
				} else {
					deviceList = getImportDataByXLS(gwShare_fileName);
				}
				logger.warn("********deviceList" + deviceList.toString());
			} catch (FileNotFoundException e) {
				logger.warn("e=" + e.getMessage());
				logger.warn("{}文件没找到!", fileName_);
				this.ajax = "文件没找到!";
				return SUCCESS;
			} catch (IOException e) {
				logger.warn("{}文件解析出错！", fileName_);
				this.ajax = "{}文件解析出错！";
				return SUCCESS;
			} catch (Exception e) {
				logger.warn("{}文件解析出错！", fileName_);
				this.ajax = "文件解析出错！";
				return SUCCESS;
			}
		}
		if (deviceList.size() > 1001) {
			ajax = "导入的设备数量超过1000，请重新导入！";
			return SUCCESS;
		}
		//任务入库
		bio.importReportTask(acc_oid, currTime, reporttimelist,targettimelist);
		//设备入库
		bio.insertTaskDev(currTime, deviceList);
		//调用IDSService
		String xmlStr = bio.getSendXMLStr(currTime,reporttimelist,targettimelist,deviceList);
		String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		String resultString = WSClientUtil.callItmsService(url,
				xmlStr, "reportPeroid");
		logger.warn("接收的参数为=" + resultString);
		//处理调用IDSService返回的处理结果
		ajax = bio.getResultMeg(resultString);
		
		return SUCCESS;
	}
	/**
	 * get all information for task
	 * @return
	 */
	public String getTaskInfo(){
		logger.warn("getTaskInfo()");
		this.setTime();
		IdsList = bio.getTaskInfo(curPage_splitPage,num_splitPage,starttime1,endtime1);
		maxPage_splitPage=bio.getTaskInfoCount(num_splitPage,starttime1,endtime1);
		return "queryList";
	}
	/**
	 * export task by excel
	 * @return
	 */
	public String getTaskInfoExl(){
		logger.warn("getTaskInfoExl()");
		this.setTime();
		IdsList = bio.getTaskInfoExl(starttime1,endtime1);
		String excelCol = "acc_loginname#add_time#enable#timelist#serverurl#tftp_port#paralist#devCount";
		String excelTitle = "定制人#定制时间#开启或关闭#上报周期#文件上传域名#端口#监控参数#设备数";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "定制信息查询结果";
		data = IdsList;
		return "excel";
	}
	/**
	 * get information of  device
	 * @return
	 */
	public String getDevInfo(){
		logger.warn("getDevInfo()");
		devList = bio.getDevInfo(curPage_splitPage,num_splitPage,taskId);
		maxPage_splitPage=bio.getDevInfoCount(num_splitPage,taskId);
		return "devInfo";
	}
	/**
	 * export data of device by excel
	 * @return
	 */
	public String getDevInfoExcel(){
		logger.warn("getDevInfoExcel()");
		devList = bio.getDevInfoExcel(taskId);
		String excelCol = "oui#device_serialnumber#result_id#status";
		String excelTitle = "oui#设备序列号#下发结果#状态";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "定制信息查询结果详细信息";
		data = devList;
		return "excel";
	}
	/**
	 * 解析excel中的设备序列号
	 * @param fileName
	 * @return 
	 * @throws BiffException
	 * @throws IOException
	 */
	public List<String> getImportDataByXLS(String fileName)
			throws BiffException, IOException {
		logger.debug("getImportDataByXLS{}", fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		;
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++) {
			ws = wwb.getSheet(m);

			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			// int columeCount = ws.getColumns();

			if (null != ws.getCell(0, 0).getContents()) {
				String line = ws.getCell(0, 0).getContents().trim();

				// 取当前页所有值放入list中
				for (int i = 1; i < rowCount; i++) {
					String temp = ws.getCell(0, i).getContents();
					if (null != temp && !"".equals(temp)) {
						if (!"".equals(ws.getCell(0, i).getContents().trim())) {
							list.add(ws.getCell(0, i).getContents().trim());
						}
					}
				}
			}
		}
		f.delete();
		f = null;
		return list;
	}
  /**
   * 解析txt中的设备序列号
   * @param fileName
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
	public List<String> getImportDataByTXT(String fileName)
			throws FileNotFoundException, IOException {
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()
				+ fileName));

		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		String line = in.readLine();
		while ((line = in.readLine()) != null) {
			if (!"".equals(line.trim())) {
				list.add(line.trim());
			}
		}
		in.close();
		in = null;

		return list;
	}
	/**
	 * 获取系统的绝对路径
	 * @return
	 */
	public String getFilePath() {
		logger.debug("getFilePath");
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try {
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	/**
	 *  时间转化
	 */
	private void setTime(){
		logger.debug("ReportPeroidUpateACT---》setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime)){
			starttime1 = null;
		}else{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime)){
			endtime1 = null;
		}else{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	public Map getSession() {
		return session;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getReporttimelist() {
		return reporttimelist;
	}
	public void setReporttimelist(String reporttimelist) {
		this.reporttimelist = reporttimelist;
	}
	public String getTargettimelist() {
		return targettimelist;
	}
	public void setTargettimelist(String targettimelist) {
		this.targettimelist = targettimelist;
	}
	public String getGwShare_fileName() {
		return gwShare_fileName;
	}
	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}
	public String getDeviceSN() {
		return deviceSN;
	}
	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}
	public String getAcc_oid() {
		return acc_oid;
	}
	public void setAcc_oid(String acc_oid) {
		this.acc_oid = acc_oid;
	}
	public String getExcute_type() {
		return excute_type;
	}
	public void setExcute_type(String excute_type) {
		this.excute_type = excute_type;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getStarttime1() {
		return starttime1;
	}
	public void setStarttime1(String starttime1) {
		this.starttime1 = starttime1;
	}
	public String getEndtime1() {
		return endtime1;
	}
	public void setEndtime1(String endtime1) {
		this.endtime1 = endtime1;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List getIdsList() {
		return IdsList;
	}
	public void setIdsList(List idsList) {
		IdsList = idsList;
	}
	public List getDevList() {
		return devList;
	}
	public void setDevList(List devList) {
		this.devList = devList;
	}
	public String getAjax() {
		return ajax;
	}
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	public void setBio(ReportPeroidUpateBIO bio) {
		this.bio = bio;
	}
	
}
