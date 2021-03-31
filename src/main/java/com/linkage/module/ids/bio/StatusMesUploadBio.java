package com.linkage.module.ids.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.act.StatusMesUploadAct;
import com.linkage.module.ids.dao.StatusMesUploadDao;

public class StatusMesUploadBio {
	private static Logger logger = LoggerFactory
			.getLogger(StatusMesUploadAct.class);

	private StatusMesUploadDao dao;

	public StatusMesUploadDao getDao() {
		return dao;
	}

	public void setDao(StatusMesUploadDao dao) {
		this.dao = dao;
	}

	public List getQueryStatusList(long taskId) {
		return dao.getQueryStatusList(taskId);
	}
	
	public int importReportTask(String acc_oid,long currTime,String enable,String timelist,String serverurl,String paralist,String tftp_port)
	{
		return dao.importReportTask(acc_oid,currTime,enable,timelist,serverurl,paralist,tftp_port);
	}
	
	public Map getOuiByDeviceSN(String deviceSn)
	{
		return dao.getOuiByDeviceSN(deviceSn);
	}
	
	public void insertTaskDev(long taskId,List<String> deviceSN)
	{
		 dao.insertTaskDev(taskId,deviceSN);
	}
	
	public void updateTaskDev(String taskid,String deviceSn,String result)
	{
		dao.updateTaskDev(taskid,deviceSn,result);
	}
	
	/**
	 * 
	 * 
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String getServerUrl() {
		StringBuffer tmpBufer = new StringBuffer();
		Map<String, String> map = dao.getServerUrl();
		tmpBufer.append("<SELECT name=\"" + "serverurl"
				+ "\" class=\"bk\" style='width:150px'>");
		tmpBufer.append("<OPTION value='").append(map.get("dir_id"));
		tmpBufer.append("'");
		tmpBufer.append(map.get("outter_url")).append("</OPTION>");
		tmpBufer.append("</SELECT>");
		return tmpBufer.toString();
	}
	
	
	/**
	 * 查询设备（带列表）
	 * 
	 * @param cityId				高级查询属地过滤
	 * @param onlineStatus			高级查询是否在线过滤
	 * @param vendorId				高级查询厂商过滤
	 * @param deviceModelId			高级查询设备型号过滤
	 * @param devicetypeId			高级查询软件版本过滤
	 * @param bindType				高级查询是否绑定过滤
	 * @param deviceSerialnumber	高级查询设备序列号模糊匹配
	 * @return
	 */
	public int getDeviceListCount(String gw_type,String cityId,String onlineStatus,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,String deviceSerialnumber){
		logger.debug("StatusMesUploadBio=>getDeviceListCount");
		int count = 0;
		
		if(null!=onlineStatus && !"".equals(onlineStatus) && !"-1".equals(onlineStatus)){
			count = dao.queryDeviceByLikeStatusCount(gw_type,cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, onlineStatus);
		}else{
			count = dao.queryDeviceCount(gw_type,cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber);
		}
		return count;
	}
	
	/**
	 * 查询设备（带列表）（针对数据量小于一定量时）
	 * 
	 * @param cityId				高级查询属地过滤
	 * @param onlineStatus			高级查询是否在线过滤
	 * @param vendorId				高级查询厂商过滤
	 * @param deviceModelId			高级查询设备型号过滤
	 * @param devicetypeId			高级查询软件版本过滤
	 * @param bindType				高级查询是否绑定过滤
	 * @param deviceSerialnumber	高级查询设备序列号模糊匹配
	 * @return
	 */
	public ArrayList<HashMap<String,String>> getDeviceList(String gw_type,String cityId,String onlineStatus,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,String deviceSerialnumber){
		logger.debug("StatusMesUploadBio=>getDeviceList");
		ArrayList<HashMap<String,String>> list = null;
		
		if(null!=onlineStatus && !"".equals(onlineStatus) && !"-1".equals(onlineStatus)){
			list = dao.queryDeviceByLikeStatus(gw_type,cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, onlineStatus);
		}else{
			list = dao.queryDevice(gw_type,cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber);
		}
		
		return list;
	}
	
	/**
	 *  更新 tab_gw_device_open(已开启状态信息上报功能设备表)
	 * 
	 * @param deviceList 设备序列号
	 * @param enable 是否开启：1,开启 ； 0,关闭
	 * @return
	 */
	public int updateOpenedDevices(List<String> deviceList, String enable){
		logger.debug("StatusMesUploadBio=>updateOpenedDevices");
		
		return dao.updateOpenedDevices(deviceList, enable);
	}
}
