
package com.linkage.module.gwms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import PreProcess.PPManager;
import PreProcess.PPManagerHelper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.resource.dao.SoftwareDAO;
import com.linkage.module.gwms.resource.obj.MQPublisher;
import com.linkage.module.gwms.resource.obj.SoftTask;
import com.linkage.module.gwms.util.StringUtil;

public class SoftwareBIO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(SoftwareBIO.class);
	private SoftwareDAO dao;
	
//	public static final String MQ_SOFT_UP_TASK_ENAB = LipossGlobals.getLipossProperty("mqSoftUpTask.enab");
//
//	public static final String MQ_SOFT_UP_TASK_URL = LipossGlobals.getLipossProperty("mqSoftUpTask.url");
//
//	public static final String MQ_SOFT_UP_TASK_TOPIC = LipossGlobals.getLipossProperty("mqSoftUpTask.topic");

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(SoftwareDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * @return the dao
	 */
	public SoftwareDAO getDao()
	{
		return dao;
	}
	
	/**
	 * 设备批量升级
	 * 
	 * @author wangsenbo
	 * @date Dec 24, 2009
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void batchUp(long accoid, String[] deviceId_array, String softStrategy_type)
	{
		logger.debug("batchUp({},{},{})", new Object[] { accoid, deviceId_array,
				softStrategy_type });
		// 获取配置参数(XML)字符串
		ArrayList<String> sqllist = new ArrayList<String>();
		Map<String, Map> softParamMap = dao.getSoftFileInfo();
		Map softUpMap = dao.getSoftUp();
		String deviceTypeId = "";
		String devicetype_id = "";
		String softSheet_para = "";
		String taskId = "";
		int size = deviceId_array.length;
		// 软件升级策略ID数组
		String[] strategyId_array = new String[size];
		// 级策略ID
		long id = 0L;
		/** 入策略表 */
		int softType = StringUtil.getIntegerValue(softStrategy_type);
		for (int i = 0; i < size; i++)
		{
			devicetype_id = dao.getDevicetypeId(deviceId_array[i]);
			deviceTypeId = (String) softUpMap.get("1|" + devicetype_id);
			if (deviceTypeId == null || "".equals(deviceTypeId))
			{
				// 如果映射表没有该devicetype_id的纪录，默认不需要升级
				deviceTypeId = devicetype_id;
				logger.debug("deviceTypeId为空{}", devicetype_id);
			}
			// 目标版本和原设备的版本一致，不需要升级
			if (deviceTypeId.equals(devicetype_id))
			{
				logger.warn("[{}]目标版本和原设备版本一直，不需要升级",deviceId_array[i]);
			}
			else
			{
				softSheet_para = softUpXml(softParamMap.get(deviceTypeId));
				// 生成入策略的sql
				taskId = StringUtil.getStringValue(StaticTypeCommon.generateLongId());
				id = StrategyOBJ.createStrategyId();
				List sql = inStrategy(id, accoid, softType, deviceId_array[i], 5, 1, 2,
						softSheet_para, taskId, 5, 1);
				strategyId_array[i] = StringUtil.getStringValue(id);
				sqllist.addAll(sql);
				if(sqllist.size() >= 100){
					DBOperation.executeUpdate(sqllist);
					sqllist.clear();
				}
			}
		}
		
		DBOperation.executeUpdate(sqllist);
		// 启用一个新的线程来做入库
		//LipossGlobals.ALL_SQL_IPTV.addAll(sqllist);
	}
	/**
	 * 设备批量升级(安徽only)
	 * 
	 * @author zhangsb
	 * @date 2013年6月6日 
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void batchUpAh(long accoid, String[] deviceId_array, String softStrategy_type,
			String starttime,String endtime,String goal_devicetype_id)
	{
		logger.warn("batchUp({},{},{})", new Object[] { accoid, deviceId_array,
				softStrategy_type });
		// 获取配置参数(XML)字符串
		ArrayList<String> sqllist = new ArrayList<String>();
		//taskId 为当前时间的数字格式
		Long taskId = 0l;
		DateTimeUtil dt = null;
		dt = new DateTimeUtil();
		taskId  = dt.getLongTime();
		
		int size = deviceId_array.length;
		
		//增加定制任务sql
		dao.addSoftUpTask(taskId,starttime,endtime,goal_devicetype_id);
		//增加
		for (int i = 0; i < size; i++)
		{
				// 生成入软件升级任务关联设备表的sql
				List sql = dao.createSoftUpTaskSQL(taskId,deviceId_array[i]);
				sqllist.addAll(sql);
		}
		//入库
		LipossGlobals.ALL_SQL_IPTV.addAll(sqllist);
		//通知软件升级模块
		SoftTask task = new SoftTask("WEB",StringUtil.getIntegerValue(taskId), 1);
		 try{
//			 MQPublisher publisher = new MQPublisher(MQ_SOFT_UP_TASK_ENAB,
//			 MQ_SOFT_UP_TASK_URL, MQ_SOFT_UP_TASK_TOPIC);
			 MQPublisher publisher = new MQPublisher("picUp.task");
			 publisher.publishMQ(task);
		 }catch(Exception e ){
			 e.printStackTrace();
			 logger.error("MQ消息发布失败， mesg({})", e.getMessage());
		 }
	}
	/**
	 * 设备批量升级(安徽only)
	 * 
	 * @author zhangsb
	 * @date 2013年6月7日 
	 * @return void
	 */
	public void batchUpAhSQL(String deviceSQL, String softStrategy_type,
			String starttime,String endtime,String goal_devicetype_id)
	{
		logger.debug("batchUp({},{})", new Object[] { deviceSQL,
				softStrategy_type });
		// 获取配置参数(XML)字符串
		//taskId 为当前时间的数字格式
		Long taskId = 0l;
		DateTimeUtil dt = null;
		dt = new DateTimeUtil();
		taskId  = dt.getLongTime();
		
		//增加定制任务sql
		dao.addSoftUpTask(taskId,starttime,endtime,goal_devicetype_id);
		//增加设备关联表
		dao.addSoftUpRefDev(taskId,deviceSQL);
		//入库
		//通知软件升级模块
		SoftTask task = new SoftTask("WEB",StringUtil.getIntegerValue(taskId), 1);
		 try{
//			 MQPublisher publisher = new MQPublisher(MQ_SOFT_UP_TASK_ENAB,
//			 MQ_SOFT_UP_TASK_URL, MQ_SOFT_UP_TASK_TOPIC);
			 MQPublisher publisher = new MQPublisher("picUp.task");
			 publisher.publishMQ(task);
		 }catch(Exception e ){
			 e.printStackTrace();
			 logger.error("MQ消息发布失败， mesg({})", e.getMessage());
		 }
	}
	public String getDeviceTypeId(String deviceId)
	{
		return dao.getDevicetypeId(deviceId);
	}
	private String softUpXml(Map map)
	{
		logger.debug("softUpXml({})", map);
		String strXml = null;
		if (map == null || map.isEmpty())
		{
		}
		else
		{
			// new doc
			Document doc = DocumentHelper.createDocument();
			// root node: NET
			Element root = doc.addElement("SoftUpdate");
			root.addAttribute("flag", "1");
			root.addElement("CommandKey").addText("SoftUpdate");
			root.addElement("FileType").addText("1 Firmware Upgrade Image");
			root.addElement("URL").addText(StringUtil.getStringValue(map.get("file_url")));
			root.addElement("Username").addText("");
			root.addElement("Password").addText("");
			root.addElement("FileSize").addText(
					StringUtil.getStringValue(map.get("softwarefile_size")));
			root.addElement("TargetFileName").addText(
					StringUtil.getStringValue(map.get("softwarefile_name")));
			root.addElement("DelaySeconds").addText("");
			root.addElement("SuccessURL").addText("");
			root.addElement("FailureURL").addText("");
			strXml = doc.asXML();
		}
		return strXml;
	}

	public List<String> inStrategy(long id, long accoid, int strategyType,
			String deviceId, int serviceId, int orderId, int sheetType, String sheetPara,
			String taskId, int tempId, int isLastOne)
	{
		StrategyOBJ strategyObj = new StrategyOBJ();
		// 策略ID
		strategyObj.setId(id);
		// 策略配置时间
		strategyObj.setTime(TimeUtil.getCurrentTime());
		// 用户id
		strategyObj.setAccOid(accoid);
		// 立即执行
		strategyObj.setType(strategyType);
		// 设备ID
		strategyObj.setDeviceId(deviceId);
		// QOS serviceId
		strategyObj.setServiceId(serviceId);
		// 顺序,默认1
		strategyObj.setOrderId(orderId);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(sheetType);
		// 参数
		strategyObj.setSheetPara(sheetPara);
		// task_id
		strategyObj.setTaskId(taskId);
		strategyObj.setTempId(tempId);
		strategyObj.setIsLastOne(isLastOne);
		return dao.strategySQL(strategyObj);
	}

	/**
	 * 开始调用预读模块
	 * 
	 * @param invokeStruct
	 */
	@SuppressWarnings("unused")
	private void invokePreProcess(String[] idArr)
	{
		String ior = dao.getPreProcessIOR();
		logger.debug("IOR:" + ior);
		if (null != ior)
		{
			String[] args = null;
			ORB orb = ORB.init(args, null);
			org.omg.CORBA.Object objRef = orb.string_to_object(ior);
			PPManager ppm = PPManagerHelper.narrow(objRef);
			ppm.processOOBatch(idArr);
			orb = ORB.init(args, null);
		}
	}

	/**
	 * 0:立即执行 1：第一次连到系统 2：周期上报 3：重新启动 4：下次连到系统 5：终端启动 执行策略的方式
	 * 
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String getStrategyList(String name, String... typeIds)
	{
		StringBuffer tmpBufer = new StringBuffer();
		Map<String, String> map = dao.getStrategyType(typeIds);
		tmpBufer
				.append("<SELECT id=\"" + name + "\" name=\"" + name + "\" class=\"bk\" style='width:150px'>");
		Set<String> set = map.keySet();
		for (String tempid : set)
		{
			tmpBufer.append("<OPTION value='").append(tempid);
			if ("5".equals(tempid))
			{
				tmpBufer.append("' selected>");
			}
			else
			{
				tmpBufer.append("'>");
			}
			tmpBufer.append(map.get(tempid)).append("</OPTION>");
		}
		tmpBufer.append("</SELECT>");
		// tmpBufer.append("<OPTION value=\"0\">立即执行</OPTION>");
		// tmpBufer.append("<OPTION value=\"1\">第一次连到系统</OPTION>");
		// tmpBufer.append("<OPTION value=\"2\">周期上报</OPTION>");
		// tmpBufer.append("<OPTION value=\"3\">重新启动</OPTION>");
		// tmpBufer.append("<OPTION value=\"4\">下次连接到系统时自动配置</OPTION>");
		// tmpBufer.append("<OPTION value=\"5\" selected>终端启动</OPTION>");
		// tmpBufer.append("</SELECT>");
		return tmpBufer.toString();
	}
	
	
	public String getStrategyCQList(String name)
	{
		StringBuffer tmpBufer = new StringBuffer();
		tmpBufer.append("<SELECT name=\"" + name + "\" class=\"bk\" style='width:150px'>");
		/*tmpBufer.append("<OPTION value=\"5\" selected>下次连接到系统</OPTION>");
		tmpBufer.append("<OPTION value=\"5\" selected>初始安装第一次启动</OPTION>");
		tmpBufer.append("<OPTION value=\"5\" selected>其他事件触发时（如配置改变）</OPTION>");
		tmpBufer.append("<OPTION value=\"5\" selected>Inform上报信息</OPTION>");
		tmpBufer.append("<OPTION value=\"5\" selected>终端启动</OPTION>");*/
		/*tmpBufer.append("<INPUT type=\"checkbox\" >下次连接到系统");
		tmpBufer.append("<INPUT type=\"checkbox\" >初始安装第一次启动");
		tmpBufer.append("<INPUT type=\"checkbox\" >其他事件触发时（如配置改变）");
		tmpBufer.append("<INPUT type=\"checkbox\" >Inform上报信息");
		tmpBufer.append("<INPUT type=\"checkbox\" >终端启动");*/
		//tmpBufer.append("</SELECT>");
		// tmpBufer.append("<OPTION value=\"0\">立即执行</OPTION>");
		// tmpBufer.append("<OPTION value=\"1\">第一次连到系统</OPTION>");
		tmpBufer.append("<OPTION value=\"2\">周期上报</OPTION>");
		tmpBufer.append("<OPTION value=\"5\">重新启动</OPTION>");
		tmpBufer.append("<OPTION value=\"4\">下次连接到系统</OPTION>");
		// tmpBufer.append("<OPTION value=\"5\" selected>终端启动</OPTION>");
		tmpBufer.append("</SELECT>");
		return tmpBufer.toString();
	}
	
	public String getStrategySXLTList(String name)
	{
		StringBuffer tmpBufer = new StringBuffer();
		tmpBufer.append("<input type=\"checkbox\" name=\"" + name + "\" class=\"bk\" value=\"2\"><span>周期上报</span>");
		tmpBufer.append("<input type=\"checkbox\" name=\"" + name + "\" class=\"bk\" value=\"5\"><span>重新启动</span>");
		tmpBufer.append("<input type=\"checkbox\" name=\"" + name + "\" class=\"bk\" value=\"4\"><span>下次连接到系统</span>");
		tmpBufer.append("<input type=\"checkbox\" name=\"" + name + "\" class=\"bk\" value=\"6\"><span>参数改变</span>");
		return tmpBufer.toString();
	}

	@SuppressWarnings("unchecked")
	public List getAllTaskList() {
		List<Map> list  = new ArrayList<Map>();
		List lt = dao.getAllTaskList();
		Map map = null;
		if(null!=lt&&!lt.isEmpty()){
			for(int i=0;i<lt.size();i++){
				map = new HashMap();
				Map mp = (Map) lt.get(i);
				map.put("task_id", mp.get("task_id"));
				map.put("opertime", getDate(mp));
				map.put("starttime", mp.get("start_time"));
				map.put("endtime", mp.get("end_time"));
				map.put("goalVersion",dao.getSoftVersion().get(StringUtil.getStringValue(mp.get("devicetype_id"))));
				map.put("status", StringUtil.getIntegerValue(mp.get("status"))==1?"执行完成":"未执行完成");
				list.add(map);
			}
		}
		return list;
	}
	/**
	 * 转换时间
	 * @param map
	 * @return
	 */
	public String getDate(Map map){
		long opendate = StringUtil.getLongValue(map.get("task_id"));
		DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
		return dt.getLongDate();
	}

	public String deleteTask(String taskId) {
		String message = "";
		dao.deleteTask(taskId);
		//入库
		//通知软件升级模块
		SoftTask task = new SoftTask("WEB",StringUtil.getIntegerValue(taskId), 2);
		 try{
//			 MQPublisher publisher = new MQPublisher(MQ_SOFT_UP_TASK_ENAB,
//			 MQ_SOFT_UP_TASK_URL, MQ_SOFT_UP_TASK_TOPIC);
			 MQPublisher publisher = new MQPublisher("picUp.task");
			 publisher.publishMQ(task);
			 message = "删除成功";
		 }catch(Exception e ){
			 e.printStackTrace();
			 logger.error("MQ消息发布失败， mesg({})", e.getMessage());
			 message = "删除失败";
		 }
		return message;
	}

	public void insertTask(long time, String task_id, String task_name, String mode, String id, String gw_type)
	{
		dao.insertTask(time, task_id, task_name, mode, id, gw_type);
	}
	
	public void insertTask(long time, String task_id, String task_name, String mode, String id, String gw_type,String softStrategy_type)
	{
		dao.insertTask(time, task_id, task_name, mode, id, gw_type, softStrategy_type);
	}
	
	public void insertTaskNew(long time, String task_id, String task_name, String mode, String id, String gw_type,String softStrategy_type, String startTime, String endTime,
			String gwShare_queryType, String gwShare_queryField, String gwShare_queryParam, String gwShare_cityId, String gwShare_onlineStatus, 
			String gwShare_vendorId, String gwShare_deviceModelId, String gwShare_devicetypeId, String gwShare_bindType, String gwShare_deviceSerialnumber, String gwShare_fileName)
	{
		dao.insertTaskNew(time, task_id, task_name, mode, id, gw_type, softStrategy_type,startTime,endTime,
				gwShare_queryType,gwShare_queryField,gwShare_queryParam,gwShare_cityId,gwShare_onlineStatus,gwShare_vendorId,
				gwShare_deviceModelId,gwShare_devicetypeId,gwShare_bindType,gwShare_deviceSerialnumber,gwShare_fileName);
	}

	// 判断文件是否存在
	public boolean fileIsExist(String name) 
	{
		int num = dao.queryFileByName(name);
		if(num > 0) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 根据厂商名获取型号
	 * @param vendor_name
	 * @return map
	 */
	public Map<String, String> getModelMapByVendorName(String vendor_name) 
	{
		Map<String, String> modelMap = new HashMap<String, String>();
		String device_model;
		String device_model_id;
		
		List<Map<String, String>> list = dao.getModelListByVendorName(vendor_name);
		
		for (Map<String,String> map : list) 
		{
			device_model = StringUtil.getStringValue(map, "device_model");
			device_model_id = StringUtil.getStringValue(map, "device_model_id");
			modelMap.put(device_model, device_model_id);
		}
		return modelMap;
	}

	/**
	 * 获取设备类型List
	 * @return
	 */
	public List getDevTypeList() 
	{
		return dao.getDevTypeList();
	}

	/**
	 * 从版本文件信息表获取存在软件版本文件的设备类型id List<devicetype_id>
	 * @return
	 */
	public List<String> getDevTypeIdBySoftwareFileIsexist() 
	{
		List<String> deviceTypeIdList = new ArrayList<String>();
		String devicetype_id;
		
		List<Map<String, String>> list = dao.getDevTypeIdBySoftwareFileIsexist();
		
		for (Map<String,String> map : list) 
		{
			devicetype_id = StringUtil.getStringValue(map, "devicetype_id");
			deviceTypeIdList.add(devicetype_id);
		}
		return deviceTypeIdList;
	}

	public List<Map<String, String>> getVendorList(String vendor_name) 
	{
		return dao.getVendorList(vendor_name);
	}

	public Map queryTaskById(String taskId) 
	{
		return dao.queryTaskById(taskId);
	}


	/**
	 * 根据导入文件数据类型获取设备信息
	 * @param type
	 * @param gwType
	 * @return
	 */
	public List<Map<String,String>> getImportDeviceList(String type,String gwType){
		return dao.getImportDeviceList(type,gwType);
	}

	/**
	 * 写入任务设备明细表
	 * @param taskId
	 * @param deviceList
	 * @return
	 */
	public int[] insertSoftupTaskDev(String taskId,List<Map<String,String>> deviceList) {
		return dao.insertSoftupTaskDev(taskId,deviceList);
	}
}
