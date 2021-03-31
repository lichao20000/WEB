package com.linkage.module.itms.resource.bio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.share.dao.GwDeviceQueryDAO;
import com.linkage.module.itms.resource.dao.BatchRestartClientDAO;

public class BatchRestartClientBIO {
	private static Logger logger = LoggerFactory
			.getLogger(BatchRestartClientBIO.class);

	private BatchRestartClientDAO dao;
	private GwDeviceQueryDAO gwDeviceDao = null;
	//回传消息
	private String msg = null;
	//查询条件
	private String importQueryField = "username";
	public String saveBatchRestartClientTask(long taskId, String taskName, long acc_oid,
			long addTime, String accountFilePath, File uploadCustomer,
			String uploadFileName4Customer, String acc_cityId, String startTime, String endTime, Integer batchType,
			Integer netServNum, Integer netWanType) {

		logger.warn(
				"saveBatchRestartClientTask(taskId={},taskName={},acc_oid={},addTime={},accountFilePath={},batchType={})",
				new Object[] { taskId, taskName, acc_oid, addTime, accountFilePath, batchType });
		String filePath = "";
		if (null != uploadCustomer && uploadCustomer.isFile()
				&& uploadCustomer.length() > 0) {
			// 如果是导入帐号，将导入帐号的文件放到指定的目录底下
			String targetDirectory = "";
			filePath = "/accountFile";
			String targetFileName = "";
			HttpServletRequest request = null;
			try {
				// 将文件存放到指定的路径中
				request = ServletActionContext.getRequest();
				targetDirectory = ServletActionContext.getServletContext()
						.getRealPath(filePath);
				// 由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
				targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() + "_"
						+ uploadFileName4Customer;
				File target = new File(targetDirectory, targetFileName);
				FileUtils.copyFile(uploadCustomer, target);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("批量导入升级，上传文件时出错");
			}
			filePath = "http://" + request.getLocalAddr() + ":"
					+ request.getServerPort() + request.getContextPath()
					+ filePath + "/" + targetFileName;
		}
		int ier = dao.saveBatchRestartClientTask(taskId, taskName, acc_oid, addTime,
				filePath, acc_cityId, startTime, endTime, batchType, netServNum, netWanType);
		if (ier == 1) {
			return "任务定制成功!";
		} else {
			return "任务定制失败！";
		}
	}

	//简单和高级查询
	public String saveBatchRestartClient(long taskId, String taskName,long acc_oid, long addTime,String acc_cityId,  
			Integer batchType, Integer netServNum, String queryType,String onlinestatus,String vendor_id,String device_model_id,String cpe_allocatedstatus,String deviceSerialnumber)
	{
		logger.warn("saveBatchRestartClient============>");
		int ier = dao.saveBatchRestartClient(taskId, taskName, acc_oid, addTime, acc_cityId,batchType, netServNum, queryType, onlinestatus, vendor_id, device_model_id, cpe_allocatedstatus, deviceSerialnumber);
		if (ier == 1) {
			return "任务定制成功!";
		} else {
			return "任务定制失败！";
		}
	}
	/**
	 * 批量重启终端
	 * @param taskId
	 * @param acc_oid
	 * @param addTime
	 * @param acc_cityId
	 * @param startTime
	 * @param endTime
	 * @param batchType
	 * @param queryType
	 * @param deviceId
	 * @param onlinestatus
	 * @param vendor_id
	 * @param device_model_id
	 * @param cpe_allocatedstatus
	 * @param deviceSerialnumber
	 * @return
	 */
	public String BacthRestartTerminal(long taskId,long acc_oid, long addTime,
			String acc_cityId, String startTime, String endTime, Integer batchType, String queryType,String onlinestatus,String vendor_id,String device_model_id,String cpe_allocatedstatus,String deviceSerialnumber)
	{
		int ier=dao.BacthRestartTerminal(taskId, acc_oid, addTime, acc_cityId, startTime, endTime, batchType, queryType, onlinestatus, vendor_id, device_model_id, cpe_allocatedstatus, deviceSerialnumber);
		if (ier == 1) {
			return "任务重启成功!";
		} else {
			return "任务重启失败！";
		}
	}
	public String BatchChangeNetPlayMode(long taskId,long acc_oid, long addTime,
			String acc_cityId, String startTime, String endTime, Integer batchType, String queryType,String onlinestatus,String vendor_id,
			String device_model_id,String cpe_allocatedstatus,String deviceSerialnumber,Integer netWanType)
	{
		int ier=dao.BatchChangeNetPlayMode(taskId, acc_oid, addTime, acc_cityId, startTime, endTime, batchType, queryType,  onlinestatus, vendor_id, device_model_id, cpe_allocatedstatus, deviceSerialnumber, netWanType);
		if (ier == 1) {
			return "变更上网方式成功!";
		} else {
			return "变更上网方式失败！";
		}
	}
	public List<String> getDevicetype(String[] deviceId_array)
	{
		String deviceId="";
		List<String> deviceId1=null;
		int size = deviceId_array.length;
		logger.warn("deviceId_array========="+deviceId_array);
		logger.warn("size========="+size);
		for (int i=0;i<size;i++)
		{
			logger.warn("数组======="+deviceId_array[i]);
			 deviceId=dao.getDevicetypeId(deviceId_array[i]);
			 logger.warn("数组返回值====="+deviceId);
			 deviceId1.add(deviceId);
		}
		logger.warn("deviceId==============="+deviceId);
		return deviceId1;
	}
	public List getOrderTaskList(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,
			long acc_oid, String accName, Integer batchType) {

		return dao.getOrderTaskList(curPage_splitPage, num_splitPage,
				startTime, endTime, cityId, taskName, acc_oid, accName, batchType);
	}

	public int countOrderTask(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,
			long acc_oid, String accName, Integer batchType) {
		return dao.countOrderTask(curPage_splitPage, num_splitPage, startTime,
				endTime, cityId, taskName, acc_oid, accName, batchType);
	}

	public List getTaskResult(String taskId, int curPage_splitPage,
			int num_splitPage) {
		return dao.getTaskResult(taskId, curPage_splitPage, num_splitPage);
	}

	public int countDeviceTask(int curPage_splitPage, int num_splitPage,
			String taskId) {
		return dao.countTaskResult(curPage_splitPage, num_splitPage, taskId);
	}

	public String doDelete(String taskId) {
		return dao.doDelete(taskId);
	}
	/**
	 * 插入临时表
	 * @param deviceId_array
	 * @param taskId
	 * @return
	 */
	public int InsertDeviceId(String[] deviceId_array,long taskId)
	{
		return dao.InsertDeviceId(deviceId_array, taskId);
	}
	/**
	 * 查询devicetype_id
	 * @return
	 */
	public List devicetypeid(long taskId)
	{
		return dao.devicetypeid(taskId);
	}
	/**
	 * 删除临时数据
	 */
	public int delecttable(long taskId)
	{
		return dao.delecttable(taskId);
	}
	/**
	 * 查询属地
	 * @param cityId
	 * @return
	 */
	public String getCity(String cityId){
		logger.debug("BatchRestartClientBIO=>getCity(cityId:{})",cityId);
		List list = CityDAO.getNextCityListByCityPid(cityId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("city_id"));
			bf.append("$");
			bf.append(map.get("city_name"));
		}
		return bf.toString();
	}
	/**
	 * 查询设备厂商
	 * @return
	 */
	public String getVendor(){
		logger.debug("BatchRestartClientBIO=>getVendor()");
		List list = dao.getVendor();
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("vendor_id"));
			bf.append("$");
			bf.append(map.get("vendor_add"));
			bf.append("(");
			bf.append(map.get("vendor_name"));
			bf.append(")");
		}
		return bf.toString();
	}
	/**
	 * 查询设备型号
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel(String vendorId){
		logger.debug("BatchRestartClientBIO=>getDeviceModel(vendorId:{})",vendorId);
		List list = dao.getDeviceModel(vendorId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("device_model_id"));
			bf.append("$");
			bf.append(map.get("device_model"));
		}
		return bf.toString();
	}
	/**
	 * 查询设备版本
	 * @param deviceModelId
	 * @param isBatch
	 * @return
	 */
	public String getDevicetype(String deviceModelId, String isBatch){
		logger.debug("BatchRestartClientBIO=>getDevicetype(deviceModelId:{})",deviceModelId);
		List list = dao.getVersionList(deviceModelId,isBatch);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("devicetype_id"));
			bf.append("$");
			if("1".equals(isBatch)){
				bf.append(map.get("hardwareversion")+"("+map.get("softwareversion")+")");
			}else{
				//bf.append(map.get("softwareversion"));
				bf.append(map.get("hardwareversion")+"("+map.get("softwareversion")+")");
			}
		}
		return bf.toString();
	}
	/**
	 * 查询设备（带列表）（针对导入查询）
	 * 
	 * @param areaId				登录人的areaId
	 * @param queryType				查询类型(1:简单查询；2:高级查询)
	 * @param cityId				属地过滤
	 * @param fileName				文件名
	 * @return
	 *//*
	public List getDeviceList(String gw_type,long areaId,String queryType,String cityId,String fileName){
		logger.debug("getDeviceList({},{},{},{})",new Object[]{areaId,queryType,cityId,fileName});
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
			logger.warn("{}文件没找到！",fileName);
			this.msg = "文件没找到！";
			return null;
		}catch(IOException e){
			logger.warn("{}文件解析出错！",fileName);
			this.msg = "文件解析出错！";
			return null;
		}catch(Exception e){
			logger.warn("{}文件解析出错！",fileName);
			this.msg = "文件解析出错！";
			return null;
		}

		List list = null;
		if(dataList.size()<1){
			this.msg = "文件未解析到合法数据！";
			return null;
		}else{
			gwDeviceDao.insertTmp(fileName,dataList,importQueryField);
			if("username".equals(importQueryField)){
				list = dao.queryDeviceByImportUsername(gw_type,areaId, cityId, dataList,fileName);
			}else if("kdusername".equals(importQueryField)){
				list = dao.queryDeviceByImportKDUsername(gw_type, areaId, cityId, dataList, fileName);
			}else{
				list = dao.queryDeviceByImportDevicesn(gw_type,areaId, cityId, dataList,fileName);
			}
		}

		if(null==list || list.size()<1){
			this.msg = "账号不存在或账号未绑定设备";
		}
		return list;
	}*/
	public BatchRestartClientDAO getDao() {
		return dao;
	}

	public void setDao(BatchRestartClientDAO dao) {
		this.dao = dao;
	}

	
	public GwDeviceQueryDAO getGwDeviceDao()
	{
		return gwDeviceDao;
	}

	
	public void setGwDeviceDao(GwDeviceQueryDAO gwDeviceDao)
	{
		this.gwDeviceDao = gwDeviceDao;
	}

	
	public String getMsg()
	{
		return msg;
	}

	
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	
	public String getImportQueryField()
	{
		return importQueryField;
	}

	
	public void setImportQueryField(String importQueryField)
	{
		this.importQueryField = importQueryField;
	}
	
}
