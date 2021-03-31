
package com.linkage.module.gwms.resource.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.resource.bio.BatchConfigBIO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 软件升级
 * 
 * @author 王森博
 */
public class BatchConfigACT extends splitPageAction implements SessionAware
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(BatchConfigACT.class);
	private String ajax;
	private BatchConfigBIO bio;
	// session
	private Map session;
	private String softStrategyHTML;
	private String param;
	
	private String response;
	private String path;
	private String importQueryField;
	private String gwShare_queryType;
	private String gwShare_queryType_this;
	private String starttime;
	private String endtime;
	
	private String taskId;
	private String mode;
	private String maxActive;
	
	private String gwShare_vendorId = null;

	private String gwShare_deviceModelId = null;

	private String gwShare_devicetypeId = null;
	
	private List<Map<String,String>> devList = new ArrayList<Map<String,String>>();
	
	private String cqSoftCitys;
	
	private String paramType;
	
	private String paramPath;
	
	private String paramValue;
	
	private String res;
	
	private String desc;

	/** 开始时间 **/
	private String startOpenDate;
	
	/** 结束时间 **/
	private String endOpenDate;
	
	private List<Map> taskList = new ArrayList<Map>();
	
	private String task_name_query;
	private String status_query;
	private String city_id;
	private String type;//查询的什么类型的设备详情 未作/失败/成功
	private String countNum;//查询详情时直接将数量传过来
	private String task_id;
	private String task_name;
	private List<Map> taskByCityList;
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	/**
	 * 定制页面初始化
	 * @return
	 */
	public String init4CQ()
	{
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		softStrategyHTML = bio.getStrategyCQList("softStrategy_type");
		return "batch4cq";
	}

	
	/**
	 * 批量任务配置 重庆
	 * 
	 * @author fanjm
	 * @date 2018-7-24
	 * @return String
	 */
	public String batchConfig()
	{
		logger.warn("batchConfig begin()");
		
		if(paramValue.split(",").length !=paramPath.split(",").length || paramValue.split(",").length!=paramType.split(",").length){
			res = "失败";
			desc = "参数路径、值、类型数量不匹配";
			logger.warn("................结果："+res+",描述："+desc);
			return "result";
		}
		
		UserRes curUser = (UserRes) session.get("curUser");
		maxActive = LipossGlobals.getLipossProperty("cqBatchConfMaxActive");
		if(null==cqSoftCitys||"".equals(cqSoftCitys)){
			cqSoftCitys = curUser.getCityId();
		}
		logger.warn("cqSoftCitys="+cqSoftCitys);
		String[] citys = cqSoftCitys.substring(0, cqSoftCitys.length()-1).split(",");
		cqSoftCitys = "";
		for(int i=0;i<citys.length;i++){
			cqSoftCitys = cqSoftCitys + "'" + citys[i] + "',";
		}
		cqSoftCitys = cqSoftCitys.substring(0, cqSoftCitys.length()-1);
		logger.warn("mode="+mode);
		mode = mode.substring(0, mode.length()-1);
		logger.warn("mode="+mode);
		logger.warn("cqSoftCitys="+cqSoftCitys);
		
		devList = bio.queryDevice(gwShare_devicetypeId,gwShare_deviceModelId,gwShare_vendorId,cqSoftCitys, startOpenDate, endOpenDate);
		logger.warn("devList.size="+devList.size());
		if(null!=devList && devList.size()!=0){
			if(devList.size()>StringUtil.getIntegerValue(maxActive)){
				res = "失败";
				desc = "数量超过最大限制"+maxActive;
			}
			else{
				try
				{
					taskId = insertTask(curUser.getUser().getId(),curUser.getUser().getAccount());
					desc = "定制成功，任务id为："+taskId+",后台正在进行操作";
					res = "成功";
				}
				catch (Exception e)
				{
					desc = e.getMessage();
					res = "异常";
				}
			}
		}
		else{
			res = "失败";
			desc = "未查询到符合条件的设备";
		}
		
		logger.warn("................结果："+res+",描述："+desc);
		return "result";
	}
	
	
	/**
	 * 入任务表、明细表
	 * @param mode 模式
	 * @param id 定制人id
	 */
	private String insertTask(long id,String name) throws Exception
	{
		long time = new DateTimeUtil().getLongTime();
		String task_id = StringUtil.getStringValue(id)+StringUtil.getStringValue(time*3);
		String task_name = name+StringUtil.getStringValue(time);
		paramValue = paramValue.substring(1);
		paramPath = paramPath.substring(1);
		paramType = paramType.substring(1);
		logger.warn("....................paramValue="+paramValue+",paramPath="+paramPath+",paramType="+paramType);
		bio.insertTask(mode,devList,paramValue,paramPath,paramType,time,task_id,task_name,StringUtil.getStringValue(id));
		return task_id;
	}
	
	/**
	 * 管理页面初始化
	 * @return
	 */
	public String initMana4CQ()
	{
		DateTimeUtil dt = new DateTimeUtil();
		this.endtime = dt.getDate();
		this.starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(this.endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 86400L - 1L) * 1000L);
		this.endtime = dt.getLongDate();
		dt = new DateTimeUtil(this.starttime);
		this.starttime = dt.getLongDate();
		return "initMana4cq";
	}
	
	/**
	 * 任务查询
	 * @return
	 */
	public String queryList() {

		taskList = bio.queryTaskList(task_name_query, status_query, startOpenDate, endOpenDate,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryTaskCount(task_name_query, status_query, startOpenDate, endOpenDate,
				curPage_splitPage, num_splitPage);

		return "batchList4cq";
	}
	
	/**
	 * 根据属地分组查询统计情况
	 * @return
	 */
	public String queryTaskGyCity() {
		taskByCityList = bio.queryTaskGyCityList(task_id, task_name, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryTaskGyCityCount(task_id, task_name, curPage_splitPage, num_splitPage);
		return "batchConfigListGyCity4cq";
	}
	
	public String queryDevList4CQ() {
		taskList = bio.queryDevList4CQ(task_id, city_id, type, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryDevCount4CQ(countNum, curPage_splitPage, num_splitPage);
		return "batchConfigDevList4cq";
	}
	
	public String update() {
		ajax = bio.update(task_id,type);
		return "ajax";
	}
	
	public String updateCount() {
		int num = bio.updateCount(task_id,type);
		ajax = "ok";
		if(num>400000){
		//if(num>2){
			ajax = "tooMuch";
		}
		
		return "ajax";
	}
	
	
	public String del() {
		ajax = bio.del(task_id);
		return "ajax";
	}
	
	public String delCount() {
		int num = bio.delCount(task_id);
		ajax = "ok";
		if(num>400000){
			ajax = "tooMuch";
		}
		
		return "ajax";
	}
	
	
	/**
	 * 导出任务属地分组列表
	 * @return
	 */
	public String queryTaskGyCityExcel() {
		logger.debug("queryTaskGyCityExcel()");
		taskByCityList = bio.queryTaskGyCityList(task_id, task_name, curPage_splitPage, num_splitPage);
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "任务名称#属地#总数#成功数#失败数#未做数";
		excelCol = "task_name#city_name#totalNum#succNum#failNum#unDoneNum";
		
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "taskGyCity";
		data = taskByCityList;
		logger.warn("....."+data.toString());
		return "excel";
	}
	
	/**
	 * 导出设备列表
	 * @return
	 */
	public String queryDevListExcel4CQ() {
		logger.debug("queryDevListExcel4CQ()");
		taskList = bio.queryDevList4CQ(task_id, city_id, type, 0, 0);
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "设备序列号#厂商#型号#区域#版本#执行时间#执行结果#结果描述";
		excelCol = "device_serialnumber#vendor_id#device_model_id#city_name#version#settime#result_id#result_desc";
		
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "devList";
		data = taskList;
		logger.warn("....."+data.toString());
		return "excel";
	}
	
	private String getStartDate()
	{
		GregorianCalendar   now   =   new   GregorianCalendar(); 
		SimpleDateFormat   fmtrq   =   new   SimpleDateFormat( "yyyy-MM-dd HH:mm:ss",Locale.US); 
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time =  fmtrq.format(now.getTime());
		return time;
	}
	
	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar   now   =   new   GregorianCalendar(); 
		SimpleDateFormat   fmtrq   =   new   SimpleDateFormat( "yyyy-MM-dd HH:mm:ss",Locale.US); 
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time =  fmtrq.format(now.getTime());
		return time;
	}


	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}


	
	public String getAjax()
	{
		return ajax;
	}


	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}


	
	public BatchConfigBIO getBio()
	{
		return bio;
	}


	
	public void setBio(BatchConfigBIO bio)
	{
		this.bio = bio;
	}


	
	public String getSoftStrategyHTML()
	{
		return softStrategyHTML;
	}


	
	public void setSoftStrategyHTML(String softStrategyHTML)
	{
		this.softStrategyHTML = softStrategyHTML;
	}


	
	public String getParam()
	{
		return param;
	}


	
	public void setParam(String param)
	{
		this.param = param;
	}


	
	public String getResponse()
	{
		return response;
	}


	
	public void setResponse(String response)
	{
		this.response = response;
	}


	
	public String getPath()
	{
		return path;
	}


	
	public void setPath(String path)
	{
		this.path = path;
	}

	public String getFileName()
	{
		return fileName;
	}


	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}


	
	public String getImportQueryField()
	{
		return importQueryField;
	}


	
	public void setImportQueryField(String importQueryField)
	{
		this.importQueryField = importQueryField;
	}


	
	public String getGwShare_queryType()
	{
		return gwShare_queryType;
	}


	
	public void setGwShare_queryType(String gwShare_queryType)
	{
		this.gwShare_queryType = gwShare_queryType;
	}


	
	public String getGwShare_queryType_this()
	{
		return gwShare_queryType_this;
	}


	
	public void setGwShare_queryType_this(String gwShare_queryType_this)
	{
		this.gwShare_queryType_this = gwShare_queryType_this;
	}


	
	public String getStarttime()
	{
		return starttime;
	}


	
	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}


	
	public String getEndtime()
	{
		return endtime;
	}


	
	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}


	
	public String getTaskId()
	{
		return taskId;
	}


	
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}


	
	public String getMode()
	{
		return mode;
	}


	
	public void setMode(String mode)
	{
		this.mode = mode;
	}


	
	public String getMaxActive()
	{
		return maxActive;
	}


	
	public void setMaxActive(String maxActive)
	{
		this.maxActive = maxActive;
	}


	
	public String getGwShare_vendorId()
	{
		return gwShare_vendorId;
	}


	
	public void setGwShare_vendorId(String gwShare_vendorId)
	{
		this.gwShare_vendorId = gwShare_vendorId;
	}


	
	public String getGwShare_deviceModelId()
	{
		return gwShare_deviceModelId;
	}


	
	public void setGwShare_deviceModelId(String gwShare_deviceModelId)
	{
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}


	
	public String getGwShare_devicetypeId()
	{
		return gwShare_devicetypeId;
	}


	
	public void setGwShare_devicetypeId(String gwShare_devicetypeId)
	{
		this.gwShare_devicetypeId = gwShare_devicetypeId;
	}


	
	public List<Map<String, String>> getDevList()
	{
		return devList;
	}


	
	public void setDevList(List<Map<String, String>> devList)
	{
		this.devList = devList;
	}


	
	public String getCqSoftCitys()
	{
		return cqSoftCitys;
	}


	
	public void setCqSoftCitys(String cqSoftCitys)
	{
		this.cqSoftCitys = cqSoftCitys;
	}


	
	public String getParamType()
	{
		return paramType;
	}


	
	public void setParamType(String paramType)
	{
		this.paramType = paramType;
	}


	
	public String getParamPath()
	{
		return paramPath;
	}


	
	public void setParamPath(String paramPath)
	{
		this.paramPath = paramPath;
	}


	
	public String getParamValue()
	{
		return paramValue;
	}


	
	public void setParamValue(String paramValue)
	{
		this.paramValue = paramValue;
	}


	
	public String getRes()
	{
		return res;
	}


	
	public void setRes(String res)
	{
		this.res = res;
	}


	
	public String getDesc()
	{
		return desc;
	}


	
	public void setDesc(String desc)
	{
		this.desc = desc;
	}


	
	public String getStartOpenDate()
	{
		return startOpenDate;
	}


	
	public void setStartOpenDate(String startOpenDate)
	{
		this.startOpenDate = startOpenDate;
	}


	
	public String getEndOpenDate()
	{
		return endOpenDate;
	}


	
	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}


	
	public List<Map> getTaskList()
	{
		return taskList;
	}

	
	public String getTask_name_query()
	{
		return task_name_query;
	}


	
	public void setTask_name_query(String task_name_query)
	{
		this.task_name_query = task_name_query;
	}


	
	public String getStatus_query()
	{
		return status_query;
	}


	
	public void setStatus_query(String status_query)
	{
		this.status_query = status_query;
	}


	
	public String getCity_id()
	{
		return city_id;
	}


	
	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}


	
	public String getType()
	{
		return type;
	}


	
	public void setType(String type)
	{
		this.type = type;
	}


	
	public String getCountNum()
	{
		return countNum;
	}


	
	public void setCountNum(String countNum)
	{
		this.countNum = countNum;
	}


	
	public void setTaskList(List<Map> taskList)
	{
		this.taskList = taskList;
	}


	
	public String getTask_id()
	{
		return task_id;
	}


	
	public void setTask_id(String task_id)
	{
		this.task_id = task_id;
	}


	
	public String getTask_name()
	{
		return task_name;
	}


	
	public void setTask_name(String task_name)
	{
		this.task_name = task_name;
	}


	
	public List<Map> getTaskByCityList()
	{
		return taskByCityList;
	}


	
	public void setTaskByCityList(List<Map> taskByCityList)
	{
		this.taskByCityList = taskByCityList;
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
	

	

}
