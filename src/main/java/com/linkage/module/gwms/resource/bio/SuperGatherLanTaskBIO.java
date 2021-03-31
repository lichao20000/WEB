package com.linkage.module.gwms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.resource.dao.SuperGatherLanTaskDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.corba.ACSCorba;

public class SuperGatherLanTaskBIO {

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(SuperGatherLanTaskBIO.class);
	
	// dao
	private SuperGatherLanTaskDAO dao = new SuperGatherLanTaskDAO();
		
	/**
	 * 查询设备（带列表）
	 * 
	 * @param areaId				登录人的areaId
	 * @param queryParam			简单查询参数
	 * @param queryField			简单查询条件字段
	 * @param cityId				高级查询属地过滤
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDeviceIdList(long areaId,String queryParam,String queryField,String cityId){
		
		logger.debug("SuperGatherLanTaskBIO=>getDeviceIdList");
		List<Map> deviceIdList = null;
		if("deviceSn".equals(queryField)){
			deviceIdList = dao.queryDeviceIdList(areaId, cityId, null, null, null, queryParam, null);
		}else if("deviceIp".equals(queryField)){
			deviceIdList = dao.queryDeviceIdList(areaId, cityId, null, null, null, null, queryParam);
		}else if("username".equals(queryField)){
			deviceIdList = dao.queryDeviceIdList(areaId,cityId,queryParam);
		}else if("voipPhoneNum".equals(queryField)){
			deviceIdList = dao.queryDeviceIdList(cityId,queryParam);
		}else if("kdname".equals(queryField)){
			deviceIdList = dao.queryDeviceIdListByKdname(cityId,queryParam);
		}		
		return deviceIdList;
	}
	
	/**
	 * 采集设备信息
	 * 
	 * @param deviceId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map gatherDeviceLan1(String deviceId){
		logger.debug("SuperGatherLanTaskBIO=>gatherDeviceLan1");
		
		// 查询设备信息
		Map deviceMap = new HashMap();
		
		List<Map> deviceList = dao.queryDeviceMap(deviceId);
		if(null != deviceList && !deviceList.isEmpty() && null != deviceList.get(0)){
			deviceMap = deviceList.get(0);
		}
	
		DateTimeUtil date = new DateTimeUtil();
		
		deviceMap.put("lan1", "");
		deviceMap.put("status", "失败");
		
		// 采集设备信息
		ACSCorba acsCorba= new ACSCorba("1");
		ArrayList<ParameValueOBJ> result  = acsCorba.getValue(deviceId, "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1.MaxBitRate");
		if(null != result && !result.isEmpty() && null != result.get(0)){
			deviceMap.put("status", "成功");
			deviceMap.put("lan1", result.get(0).getValue());
		}
		deviceMap.put("gather_time", date.getYYYY_MM_DD_HH_mm_ss());
		
		return deviceMap;
	}
	
	/**
	 * 导入定制任务
	 * 
	 * @param fileName
	 * @return
	 */
	public String createFileTask(String fileName,String currUser){
		
		logger.debug("SuperGatherLanTaskBIO=>createFileTask");
		
		Map<String,Object> taskMap = new HashMap<String, Object>();
		
		// 获取当前时间
		DateTimeUtil date = new DateTimeUtil();
		
		taskMap.put("task_id", date.getYYYYMMDDHHMMSS());
		taskMap.put("task_type", 2);
		taskMap.put("status", 0);
		taskMap.put("execute_count", 0);
		taskMap.put("file_name", fileName);
		taskMap.put("create_time", date.getLongTime());
		taskMap.put("create_operator", currUser);
		
		int result = dao.createTask(taskMap);
		
		if(1 == result){
			return "";
		}
		return "定制失败，请检查数据库连接是否正常";
	}
	
	/**
	 * 高级定制任务
	 * 
	 * @param currUser
	 * @param cityId
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @param onlineStatus
	 * @return
	 */
	public String createTask(String currUser,String cityId,String vendorId,String deviceModelId,String deviceTypeId,String onlineStatus){
		
		logger.debug("SuperGatherLanTaskBIO=>createFileTask");
		
		Map<String,Object> taskMap = new HashMap<String, Object>();
		
		// 获取当前时间
		DateTimeUtil date = new DateTimeUtil();
		
		taskMap.put("task_id", date.getYYYYMMDDHHMMSS());
		taskMap.put("task_type", 1);
		taskMap.put("status", 0);
		taskMap.put("execute_count", 0);
		taskMap.put("city_id", cityId);
		taskMap.put("vendor_id", vendorId);
		taskMap.put("device_model_id", deviceModelId);
		taskMap.put("devicetype_id", deviceTypeId);
		taskMap.put("cpe_currentstatus", onlineStatus);
		taskMap.put("create_time", date.getLongTime());
		taskMap.put("create_operator", currUser);
		
		int result = dao.createTask(taskMap);
		
		if(1 == result){
			return "";
		}
		return "定制失败，请检查数据库连接是否正常";
	}

	/**
	 * 查询任务列表
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getTaskList(String startTime, String endTime, int curPage_splitPage, int num_splitPage){
		
		logger.debug("SuperGatherLanTaskBIO=>getTaskList");
		
		return dao.getTaskList(startTime, endTime, curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 获取最大页数
	 * 
	 * @param startTime
	 * @param endTime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getTaskMaxPage(String startTime, String endTime, int curPage_splitPage, int num_splitPage){
		logger.debug("SuperGatherLanTaskBIO=>getMaxPage");
		
		return dao.getMaxPage(startTime, endTime, curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 查询任务详情列表
	 * 
	 * @param taskId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDetailList(long taskId, int curPage_splitPage, int num_splitPage){
		
		logger.debug("SuperGatherLanTaskBIO=>getDetailList");
		
		return dao.getDetailList(taskId, curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 获取任务详情最大页数
	 * 
	 * @param taskId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getDetailMaxPage(long taskId, int curPage_splitPage, int num_splitPage){
		
		
		return dao.getDeatilMaxPage(taskId, curPage_splitPage, num_splitPage);
	}
	
	
	/**
	 * 删除任务
	 * 
	 * @param taskId
	 * @return
	 */
	public int delTask(long taskId){
		
		logger.debug("SuperGatherLanTaskBIO=>delTask");
		
		return dao.delTask(taskId);
	}
	
	/**
	 * 任务详情
	 * 
	 * @param taskId
	 * @return
	 */
	public List<Map> getTaskDetail(long taskId){
		return dao.getDetailList(taskId);
	}
	
	public SuperGatherLanTaskDAO getDao() {
		return dao;
	}

	public void setDao(SuperGatherLanTaskDAO dao) {
		this.dao = dao;
	}
}
