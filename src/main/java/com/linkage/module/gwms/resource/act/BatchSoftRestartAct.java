package com.linkage.module.gwms.resource.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.bio.BatchSoftRestartBIO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.share.bio.GwDeviceQueryBIO;
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class BatchSoftRestartAct extends ActionSupport implements SessionAware {

	private static Logger logger = LoggerFactory
			.getLogger(BatchSoftRestartAct.class);
	private HttpServletRequest request;
	private String deviceIds;
	private String ajax;
	private String starttime;
	private String fileName;
	private Map session;
	private String gwShare_queryType;
	private BatchSoftRestartBIO bio;
	private String importQueryField;
	private String param;
	private GwDeviceQueryBIO gwDeviceQueryBio;
	private String gw_type;
	private String task_desc;
	private String reboot_time;
	

	public String importTask() {
		logger.warn("@@starttime="+starttime);
		long currTime = new Date().getTime() / 1000L;
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		String[] deviceId_array = null;
		DateTimeUtil dt = new DateTimeUtil();
		dt = new DateTimeUtil(starttime);
		
		ajax = "定制失败！";
		// 定制人
		if ("4".equals(gwShare_queryType)) {
			deviceId_array = parseFile();
		} else {
			if (true == StringUtil.IsEmpty(deviceIds)) {
				logger.debug("任务中没有设备");
			}
			// 获取deviceId
			String matchSQL = "";
			long total = 0l;
			String[] _param = param.split("\\|");
			int len = _param.length;
			if (len > 11) {
				matchSQL = _param[10];
				total = StringUtil.getLongValue(_param[11]);
			}
			if (!"0".equals(deviceIds)) {
				deviceId_array = deviceIds.split(",");
			} else {
				if (StringUtil.IsEmpty(param)) {
					logger.warn("param为空");
				}
				List list = null;
				if(LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(reboot_time)){
					list = gwDeviceQueryBio.getDeviceListForGS(gw_type, curUser.getAreaId(), param, reboot_time);
				}else{
					list = gwDeviceQueryBio.getDeviceList(gw_type,
							curUser.getAreaId(), param);
				}
				deviceId_array = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					deviceId_array[i] = StringUtil.getStringValue(map
							.get("device_id"));
				}
			}
		}
		
		if (deviceId_array.length > 5 * 10000)
		{
			ajax = "设备数量超过50000，请重新选择！";
		}else
		{
			bio.insertTask(currTime,currTime,dt.getLongTime(),accoid,task_desc,0);
			bio.insertTmp(currTime,deviceId_array,currTime);
			bio.deleteSameData();
			bio.exportDataToBatch();
			bio.truncateTmp();
			bio.updateBatchSuccess(currTime);
			ajax = "定制成功！";
		}
		return "result";
	}

	
	
	private String[] parseFile()
	{
		String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
		List<String> dataList = null;

		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
			}
		}catch(FileNotFoundException e){
			logger.error("文件没找到！");
			return null;
		}catch(IOException e){
			logger.error("文件解析出错！");
			return null;
		}catch(Exception e){
			logger.error("文件解析出错！");
			return null;
		}
		
		return (String[]) dataList.toArray();
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
		File f = new File(getFilePath()+fileName);
		f.delete();
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
		f.delete();
		f = null;
		return list;
	}
	
	public String getGwShare_queryType() {
		return gwShare_queryType;
	}

	public void setGwShare_queryType(String gwShare_queryType) {
		this.gwShare_queryType = gwShare_queryType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the session
	 */
	public Map getSession() {
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session) {
		this.session = session;
	}

	public BatchSoftRestartBIO getBio() {
		return bio;
	}

	public void setBio(BatchSoftRestartBIO bio) {
		this.bio = bio;
	}

	public String getImportQueryField() {
		return importQueryField;
	}

	public void setImportQueryField(String importQueryField) {
		this.importQueryField = importQueryField;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public GwDeviceQueryBIO getGwDeviceQueryBio() {
		return gwDeviceQueryBio;
	}

	public void setGwDeviceQueryBio(GwDeviceQueryBIO gwDeviceQueryBio) {
		this.gwDeviceQueryBio = gwDeviceQueryBio;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getTask_desc() {
		return task_desc;
	}

	public void setTask_desc(String task_desc) {
		this.task_desc = task_desc;
	}


	public String getReboot_time()
	{
		return reboot_time;
	}

	public void setReboot_time(String reboot_time)
	{
		this.reboot_time = reboot_time;
	}
}
