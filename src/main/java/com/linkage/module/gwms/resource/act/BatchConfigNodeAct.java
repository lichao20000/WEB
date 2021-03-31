package com.linkage.module.gwms.resource.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.resource.bio.BatchConfigNodeBIO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.share.bio.GwDeviceQueryBIO;
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class BatchConfigNodeAct extends ActionSupport implements SessionAware {

	private static Logger logger = LoggerFactory
			.getLogger(BatchConfigNodeAct.class);
	private HttpServletRequest request;
	private String deviceIds;
	private String ajax;
	private String starttime;
	private String fileName;
	private Map session;
	private String gwShare_queryType;
	private BatchConfigNodeBIO bio;
	private String importQueryField;
	private String param;
	private GwDeviceQueryBIO gwDeviceQueryBio;
	private String gw_type;
	private String task_desc;
	private List nodeList = new ArrayList();
	private List taskList = new ArrayList();
	private String deviceFileName;
	private String total;
	private String nodeIds;
	private String stopUserName;
	private String stopPass;
	private String task_id;
	private String cityId;
	private String startOpenDate;
	private String endOpenDate;
	private String instAreaName;
	private List<Map<String, String>> cityList;

	// 导出数据
	private List<Map<String, String>> data;

	// 导出文件列标题
	private String[] title;

	// 导出文件列
	private String[] column;


	public String importTask() {
		
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		String[] deviceId_array = null;
		DateTimeUtil dt = new DateTimeUtil();
		
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
				List list = gwDeviceQueryBio.getDeviceList(gw_type,
						curUser.getAreaId(), param);
				deviceId_array = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					deviceId_array[i] = StringUtil.getStringValue(map
							.get("device_id"));
				}
			}
		}
		if (deviceId_array.length > 5000)
		{
			ajax = "设备数量超过5000，请重新选择！";
			return "result";
		}
		String[] nodeId = nodeIds.split(";");
		if (nodeId.length > 50)
		{
			ajax = "导入节点超过50，请重新选择！";
			return "result";
		}
		bio.insertDevice(dt.getLongTime(),dt.getLongTime(),deviceId_array);
		bio.insertNode(dt.getLongTime(),nodeId);
		bio.insertTask(dt.getLongTime(),accoid,1);
		ajax = "定制成功！";

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

	/*新疆电信批量自动采集*/

	public String queryTaskList4XJ() {
		taskList = bio.queryTaskList4XJ();
		return "batchConfigList4XJ";
	}

	public String checkStopUser() {
		UserRes currentUser = WebUtil.getCurrentUser();
		String username = currentUser.getUser().getAccount();
		String pass = currentUser.getUser().getPasswd();
		ajax="1";
		if(!username.equals(stopUserName)){
			ajax="用户名错误";
			return "ajax";
		}
		if(!pass.equals(stopPass)){
			ajax="密码错误";
			return "ajax";
		}
		return "ajax";
	}

	/**
	 * 停止自动采集任务
	 * @return
	 */
	public String stopTask() {
		ajax = bio.stopTask(task_id);
		return "ajax";
	}

	/**
	 *
	 * 启动采集任务
	 * @return
	 */
	public String startTask() {
		ajax = bio.startTask(task_id);
		return "ajax";
	}

	public String init()
	{
		this.instAreaName =Global.instAreaShortName;
		UserRes curUser = (UserRes)this.session.get("curUser");
		long areaId = curUser.getAreaId();
		Map cityMap= bio.queryCityIdByAreaId(com.linkage.commons.util.StringUtil.getStringValue(areaId));

		cityId = com.linkage.commons.util.StringUtil.getStringValue(cityMap.get("city_id"));
		if("00".equals(cityId))
		{
			cityList = CityDAO.getNextCityListByCityPid(cityId);
		}
		else
		{
			cityList =  new ArrayList<Map<String, String>>();
			cityList.add(cityMap);
		}
		Map map = getWeekDate();
		this.startOpenDate = StringUtil.getStringValue(map.get("mondayDate"));
		this.endOpenDate = StringUtil.getStringValue(map.get("sundayDate"));
		return "init";
	}

	public String wifiinit()
	{
		this.instAreaName =Global.instAreaShortName;
		UserRes curUser = (UserRes)this.session.get("curUser");
		long areaId = curUser.getAreaId();
		Map cityMap= bio.queryCityIdByAreaId(com.linkage.commons.util.StringUtil.getStringValue(areaId));

		cityId = com.linkage.commons.util.StringUtil.getStringValue(cityMap.get("city_id"));
		if("00".equals(cityId))
		{
			cityList = CityDAO.getNextCityListByCityPid(cityId);
		}
		else
		{
			cityList =  new ArrayList<Map<String, String>>();
			cityList.add(cityMap);
		}
		Map map = getWeekDate();
		this.startOpenDate = StringUtil.getStringValue(map.get("mondayDate"));
		this.endOpenDate = StringUtil.getStringValue(map.get("sundayDate"));
		return "wifiInit";
	}


	public String getBatchDevList() {
		taskList = bio.getBatchDevList(cityId,startOpenDate,endOpenDate);
		return "gatherDevList";
	}

	public String getBatchWifiDevList() {
		taskList = bio.getBatchWifiDevList(cityId,startOpenDate,endOpenDate);
		return "gatherWifiDevList";
	}

	public String downloadDevInfo() {
		this.data = this.bio.downloadDevInfo(cityId,startOpenDate,endOpenDate);
		this.fileName = "支持wifi终端量";
		this.title = new String[] { "属地","LOID","宽带账号","型号","厂家"};
		this.column = new String[] { "city_name", "loid", "serusername", "device_model", "vendor_name" };
		return "excel";
	}


	/**
	 * 获取当前时间所在周的周一和周日的日期时间
	 * @return
	 */
	public Map<String,String> getWeekDate() {
		Map<String,String> map = new HashMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

		Calendar cal = Calendar.getInstance();
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if(dayWeek==1){
			dayWeek = 8;
		}

		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date mondayDate = cal.getTime();
		String weekBegin = sdf.format(mondayDate);

		cal.add(Calendar.DATE, 4 +cal.getFirstDayOfWeek());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 59);
		Date sundayDate = cal.getTime();
		String weekEnd = sdf.format(sundayDate);

		map.put("mondayDate", weekBegin);
		map.put("sundayDate", weekEnd);
		return map;
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
	
	public String getDeviceFileName() {
		return deviceFileName;
	}

	public void setDeviceFileName(String deviceFileName) {
		this.deviceFileName = deviceFileName;
	}

	public List getNodeList() {
		return nodeList;
	}

	public void setNodeList(List nodeList) {
		this.nodeList = nodeList;
	}

	
	public BatchConfigNodeBIO getBio() {
		return bio;
	}

	public void setBio(BatchConfigNodeBIO bio) {
		this.bio = bio;
	}



	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}



	public String getNodeIds() {
		return nodeIds;
	}



	public void setNodeIds(String nodeIds) {
		this.nodeIds = nodeIds;
	}

	public List getTaskList() {
		return taskList;
	}

	public void setTaskList(List taskList) {
		this.taskList = taskList;
	}

	public String getStopUserName() {
		return stopUserName;
	}

	public void setStopUserName(String stopUserName) {
		this.stopUserName = stopUserName;
	}

	public String getStopPass() {
		return stopPass;
	}

	public void setStopPass(String stopPass) {
		this.stopPass = stopPass;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public String getEndOpenDate() {
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate) {
		this.endOpenDate = endOpenDate;
	}

	public String getInstAreaName() {
		return instAreaName;
	}

	public void setInstAreaName(String instAreaName) {
		this.instAreaName = instAreaName;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}


	public List<Map<String, String>> getData() {
		return data;
	}

	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}
}
