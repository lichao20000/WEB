package com.linkage.module.itms.resource.bio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.obj.BatchSoftUpBean;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.itms.resource.dao.BatchSetTempManaDAO;

/**
 * 
 * @author zzd (Ailk No.)
 * @version 1.0
 * @since 2016-10-13
 * @category com.linkage.module.gwms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BatchSetTempManaBIO{
	private static Logger logger = LoggerFactory.getLogger(BatchSetTempManaBIO.class);
	private BatchSetTempManaDAO dao;

	public List<Map> queryTaskList(String task_name_query, String status_query,
			String expire_time_start, String expire_time_end,int curPage_splitPage,
			int num_splitPage,String gw_type){
		int status = -1;
		if(!StringUtil.IsEmpty(status_query)){
			status = Integer.valueOf(status_query);
		}
		long expireTimeStart;
		long expireTimeEnd;
		if(null==expire_time_start||"".equals(expire_time_start)){
			expireTimeStart = -1;
		}else{
			expireTimeStart = dealTime(expire_time_start);
		}
		if(null==expire_time_end||"".equals(expire_time_end)){
			expireTimeEnd = -1;
		}else{
			expireTimeEnd = dealTime(expire_time_end);
		}
		
		return dao.queryTaskList(task_name_query, status, expireTimeStart, expireTimeEnd, curPage_splitPage, num_splitPage,gw_type);
	}
	
	/**
	 * 根据条件查询设备列表
	 * @param task_id 任务id
	 * @param city_id 属地id
	 * @param type 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryDevList(String task_id, String type, int curPage_splitPage, int num_splitPage){
		return dao.queryDevList(task_id, type, curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 设备列表最大页数
	 * @param countNum
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryDevCount(String countNum, int curPage_splitPage, int num_splitPage)
	{
		int total = StringUtil.getIntegerValue(countNum);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	public Map queryTaskById(String taskId) 
	{
		return dao.queryTaskById(taskId);
	}
	
	/*public int queryTaskGyCityCount(String task_id, String expire_time_end, int curPage_splitPage,
			int num_splitPage) {
		return dao.queryTaskGyCityCount(task_id, curPage_splitPage, num_splitPage);
	}*/
	
	public int queryTaskCount(String task_name_query, String status_query,
			String expire_time_start, String expire_time_end, int curPage_splitPage,
			int num_splitPage,String gw_type) {
		int status = -1;
		long expireTimeStart;
		long expireTimeEnd;
		if(!StringUtil.IsEmpty(status_query)){
			status = Integer.valueOf(status_query);
		}
		if(null==expire_time_start||"".equals(expire_time_start)){
			expireTimeStart = -1;
		}else{
			expireTimeStart = dealTime(expire_time_start);
		}
		if(null==expire_time_end||"".equals(expire_time_end)){
			expireTimeEnd = -1;
		}else{
			expireTimeEnd = dealTime(expire_time_end);
		}
		String instArea = Global.instAreaShortName;
		return dao.queryTaskCount(task_name_query, status, expireTimeStart, expireTimeEnd, curPage_splitPage, num_splitPage,gw_type);
	}

	public String update(String task_id,String type){
		String res = dao.update(task_id,type)+"";
		if(!"1".equals(type)){
			//恢复调用配置模块，发消息通知
			ArrayList<HashMap<String, String>> deviceList = dao.continueIds(task_id);
					
			String[] deviceId_array = new String[deviceList.size()];
			for(int i=0;i<deviceList.size();i++){
				deviceId_array[i] = deviceList.get(i).get("device_id");
			}
			//deviceId_array = deviceList.toArray(deviceId_array);
			logger.warn("准备调配置添加缓存，deviceList.size()="+deviceList.size());
			logger.warn("准备调配置添加缓存，size="+deviceId_array.length);
			PreProcessInterface softUpgradeCorba = CreateObjectFactory.createPreProcess("1");
			softUpgradeCorba.processSetStrategyMem(deviceId_array,"batch");
		}
		return res;

	}
	
	
	public int updateCount(String task_id,String type){
		return dao.updateCount(task_id,type);
	}
	
	public String del(String task_id){
		return(dao.del(task_id)+"");
	}
	
	public int delCount(String task_id){
		return(dao.delCount(task_id));
	}
	
	public String updateStatus(String status,String taskName){
		int statusUp = -1;
		if("1".equals(status)){
			statusUp = 0;
		}else{
			statusUp = 1;
		}
		return(dao.updateStatus(statusUp, taskName)+"");

	}
	

	public String batchSoftwareUp(BatchSoftUpBean bsuBean){
		long add_time = System.currentTimeMillis() / 1000L;
		bsuBean.setAdd_time(String.valueOf(add_time));
		int randomNum=(int)(Math.random()*900)+100; 
		long task_id = Long.parseLong(add_time+""+randomNum);
		bsuBean.setTask_id(String.valueOf(task_id));
		int status_a = 0;


		int service_id =5;
		bsuBean.setService_id(String.valueOf(service_id));
		bsuBean.setUp_begin(bsuBean.getUp_begin().replace(":", ""));
		bsuBean.setUp_end(bsuBean.getUp_end().replace(":", ""));

		long expire_time=dealTime(bsuBean.getExpire_t());
		bsuBean.setExpire_t(String.valueOf(expire_time));
		int records1 = 0;
		int repeatNum = dao.repeatTaskName(bsuBean.getTask_name());
		if (0!=repeatNum){
			return "repeat";
		}
		records1 = dao.addSoftwareUp(bsuBean);


		String[] deviceIdArray = bsuBean.getDeviceIds().split(",");
		String[] devicetypeIdArray = bsuBean.getDevicetypeIds().split(",");
		List<String> deviceIdlist = Arrays.asList(deviceIdArray);
		List<String> devicetypeIdlist = Arrays.asList(devicetypeIdArray);
		ArrayList<String> sqlList = new ArrayList<String>();
		int records2 = 0;
		for(int i=0;i<deviceIdlist.size();i++){
			String deviceId = deviceIdlist.get(i);
			String devicetypeId = devicetypeIdlist.get(i);
			String addSoftwareUpSql = dao.addSoftwareUp(task_id, deviceId, Integer.parseInt(devicetypeId), status_a);
			sqlList.add(addSoftwareUpSql);
			if(sqlList.size()>400){
				records2 += dao.recordDev(sqlList);
				sqlList.clear();
			}
		}
		if(sqlList.size() != 0){
			records2 += dao.recordDev(sqlList);
		}
		if(records1 == 1 && records2 !=0){
			return "success";
		}else{
			return "fail";
		}

	}
	public long dealTime(String time) {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date str = new Date();
		try {
			str = date.parse(time);
		}
		catch (ParseException e) {
			logger.warn("选择开始或者结束的时间格式不对:" + time);
		}

		return str.getTime() / 1000L;
	}

	public BatchSetTempManaDAO getDao()
	{
		return dao;
	}

	public void setDao(BatchSetTempManaDAO dao)
	{
		this.dao = dao;
	}


}
