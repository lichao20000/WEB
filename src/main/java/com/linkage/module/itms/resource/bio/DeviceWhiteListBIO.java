
package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.DeviceWhiteListDAO;

public class DeviceWhiteListBIO
{

	private static Logger logger = LoggerFactory.getLogger(DeviceWhiteListBIO.class);
	private DeviceWhiteListDAO dao;
	
	public void setDao(DeviceWhiteListDAO dao)
	{
		this.dao = dao;
	}

	public DeviceWhiteListDAO getDao()
	{
		return dao;
	}
	
	public String addWhiteListTask(long taskid,String task_name, String task_desc,long accoid, String deviceIds, String param){
		try {
			
			List<String> netAccounts = new ArrayList<String>();
			
			//高级查询
			if(StringUtil.IsEmpty(deviceIds) || "0".equals(deviceIds)){
				String sqlSpell = null;
				if(!StringUtil.IsEmpty(param)){
					String[] paramArr = param.split("\\|");
					if(null!=paramArr &&  paramArr.length>=11){
						sqlSpell = paramArr[10];
					}
				}
				
				if(StringUtil.IsEmpty(sqlSpell)){
					logger.warn("==[{}]设备为空，查询sql为空，程序结束==", taskid);
					return "false";
				}
				
				if(sqlSpell.contains("[")){
					sqlSpell = sqlSpell.replaceAll("\\[", "\\'");
				}
				
				ArrayList<HashMap<String, String>> devList = dao.getDevIds(sqlSpell);
				if(null==devList || devList.size()==0){
					logger.warn("==[{}]设备为空，程序结束==", taskid);
					return "false";
				}
				
				int num = dao.addTask(task_name,taskid, accoid, sqlSpell, 2,0);
				if(num>0){
					return "trueThread";
				}else{
					return "false";
				}
			}else{
				
				String[] deviceIdsArr = null==deviceIds ? null : deviceIds.split(",");
				if(null==deviceIdsArr || 0==deviceIdsArr.length){
					logger.warn("==[{}]设备为空，程序结束==", taskid);
					return "false";
				}
			
				for(String deviceId : deviceIdsArr){
					if(!StringUtil.IsEmpty(deviceId)){
						netAccounts.add(deviceId);
					}
				}
				if(null==netAccounts || netAccounts.size()==0){
					logger.warn("==[{}]设备查询结果为空，程序结束==", taskid);
					return "false";
				}
				
				// 批量插入设备ID到临时表
				String filenameTmp = taskid + "";
				dao.insertTmp(filenameTmp, netAccounts);
				// 根据设备序列号获取设备信息
				ArrayList<HashMap<String, String>> devList = dao.getTaskDevList(filenameTmp);
				
				logger.warn("taskid[{}]-devListNew.size[{}]", taskid, devList.size());
				if(devList.size()>10000){
					logger.warn("taskid[{}] 定制设备超过10000条，程序结束==", taskid);
					return "false10000";
				}
				
				ArrayList<String> sqlList = dao.sqlList(devList, taskid);
				ArrayList<String> sqlListTmp = new ArrayList<String>();
				int count = 0;
				
				// 批量插入设备信息
				if(null!=sqlList && sqlList.size()>0){
					for(String sql : sqlList){
						sqlListTmp.add(sql);
						if(sqlListTmp.size()>=200){
							int res = dao.insertTaskDev(sqlListTmp);
							if(res>0){
								count += sqlListTmp.size();
							}
							sqlListTmp.clear();
						}
					}
				}
				if(sqlListTmp.size()>0){
					int res = dao.insertTaskDev(sqlListTmp);
					if(res>0){
						count += sqlListTmp.size();
					}
					sqlListTmp.clear();
				}
				
				logger.warn("==[{}]插入tab_whitelist_dev表[{}]条数据==", taskid, count);
				//入任务表
				int num = dao.addTask(task_name,taskid, accoid, "", 1,1);
				
				//入黑名单表
				dao.addWhiteList(taskid);
				
				if(num>0){
					return "true";
				}else {
					return "false";
				}
			}
			
		} catch (Exception e) {
			logger.warn("批量定制任务执行失败e[{}]", e);
			return "false";
		}
	}
	
	public List<Map> getWhiteDeviceList(int curPage_splitPage, int num_splitPage,
			String cityId,String vendor_id,String device_model_id,String device_type_id,String deviceSerialnumber)
	{
		List<Map> list = dao.getWhiteDeviceList(curPage_splitPage,num_splitPage,cityId,vendor_id,device_model_id,
				device_type_id,deviceSerialnumber);
		return list;
	}
	
	public int getWhiteDeviceListCount(String cityId,String vendor_id,String device_model_id,String device_type_id,String deviceSerialnumber)
	{
		int count = dao.getWhiteDeviceListCount(cityId,vendor_id,device_model_id,
				device_type_id,deviceSerialnumber);
		return count;
	}
	
	public String delete(String device_serialnumber)
	{
		int flag = dao.deleteDevice(device_serialnumber);
		if (flag > 0) {
			return "删除成功！";
		} else {
			return "删除失败，请重新操作！";
		}
	}
}
