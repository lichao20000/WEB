/**
 * 
 */
package com.linkage.module.gwms.service.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.service.dao.ServStrategyConfigDAO;
import com.linkage.module.gwms.service.obj.ServStrategyConfigOBJ;

/**
 * @author OneLineSky E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2008-12-18
 * @category dao.confTaskView
 * 
 */
public class ServStrategyConfigBIO {
	
	private ServStrategyConfigDAO dao = null;
	
	public List getServStrategy(int curPage_splitPage, int num_splitPage,
			String cityId, String time_start, String time_end,
			String device_serialnumber, String username,String taskId,int type) {
		
		List<ServStrategyConfigOBJ> rsList = new ArrayList<ServStrategyConfigOBJ>();
		
		List list0 = dao.getServStrategy(curPage_splitPage, num_splitPage,
				cityId, time_start, time_end, device_serialnumber, username, taskId,type);
		List<String> deviceList = new ArrayList<String>();
		for(int i=0;i<list0.size();i++){
			Map map = (Map) list0.get(i);
			String deviceId_ = (String) map.get("device_id");
			String taskId_ = (String) map.get("task_id");
			String oui_ = String.valueOf(map.get("oui"));
			String deviceSn_ = String.valueOf(map.get("device_serialnumber"));
			String username_ = String.valueOf(map.get("username"));
			if(null==username_ || "null".equals(username_)){
				username_ = "";
			}
			deviceList.add(deviceId_);
			ServStrategyConfigOBJ obj = new ServStrategyConfigOBJ();
			obj.setDeviceId(deviceId_);
			obj.setTaskId(taskId_);
			obj.setOui(oui_);
			obj.setDeviceSerialnumber(deviceSn_);
			obj.setUsername(username_);
			rsList.add(obj);
		}
		if(deviceList.size()>0){

			List list1 = dao.getServStrategyInfo(deviceList,type);
			for(int i=0;i<list1.size();i++){
				Map map = (Map) list1.get(i);
				String deviceId_ = (String) map.get("device_id");
				String id_ = (String) map.get("id");
				String taskId_ = (String) map.get("task_id");
				String username_ = (String) map.get("username");
				if(null==username_ || "null".equals(username_)){
					username_ = "";
				}
				String status = (String) map.get("status");
				String resultId_ = (String) map.get("result_id");
				String resultDesc_ = (String) map.get("result_desc");
				String time_ = (String) map.get("time");
				String startTime_ = (String) map.get("start_time");
				String endTime_ = (String) map.get("end_time");
				String serviceName_ = (String) map.get("service_name");
				for(int j=0;j<rsList.size();j++){
					ServStrategyConfigOBJ obj = rsList.get(j);
					if(null==obj.getTaskId()){
						if(obj.getDeviceId().equals(deviceId_) && obj.getUsername().equals(username_)){
							obj.setId(id_);
							List<Map<String,String>> infoList = obj.getList();
							if(null==infoList){
								infoList = new ArrayList<Map<String,String>>();
								obj.setList(infoList);
							}
							Map<String,String> infoMap = new HashMap<String,String>();
							infoMap.put("device_id", deviceId_);
							infoMap.put("status", status);
							infoMap.put("result_id", resultId_);
							infoMap.put("result_desc", resultDesc_);
							infoMap.put("time", time_);
							infoMap.put("start_time", startTime_);
							infoMap.put("end_time", endTime_);
							infoMap.put("service_name", serviceName_);
							infoList.add(infoMap);
							obj.setListSize(infoList.size());
							break ;
						}
					}else{
						if(obj.getDeviceId().equals(deviceId_) && obj.getTaskId().equals(taskId_)){
							obj.setId(id_);
							List<Map<String,String>> infoList = obj.getList();
							if(null==infoList){
								infoList = new ArrayList<Map<String,String>>();
								obj.setList(infoList);
							}
							Map<String,String> infoMap = new HashMap<String,String>();
							infoMap.put("device_id", deviceId_);
							infoMap.put("status", status);
							infoMap.put("result_id", resultId_);
							infoMap.put("result_desc", resultDesc_);
							infoMap.put("time", time_);
							infoMap.put("start_time", startTime_);
							infoMap.put("end_time", endTime_);
							infoMap.put("service_name", serviceName_);
							infoList.add(infoMap);
							obj.setListSize(infoList.size());
							break ;
						}
					}
				}
			}
		}
		
		return rsList;
	}

	public int getServStrategyCount(int curPage_splitPage, int num_splitPage,
			String cityId, String time_start, String time_end,
			String device_serialnumber, String username,String taskId,int strategyType) {
		return dao.getServStrategyCount(curPage_splitPage, num_splitPage, 
				cityId, time_start, time_end, device_serialnumber, username,taskId,strategyType);
	}

	public int cancelData(String deviceId,String taskId,String id,int type){
		return dao.cancelData(deviceId, taskId,id,type);
	}
	
	public int resetData(String deviceId,String taskId,String id,int type){
		return dao.resetData(deviceId, taskId,id,type);
	}
	
	/**
	 * @return the dao
	 */
	public ServStrategyConfigDAO getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(ServStrategyConfigDAO dao) {
		this.dao = dao;
	}
	
}
