package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.config.dao.BatchAddIptvNodeDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.CreateObjectFactory;

public class BatchAddIptvNodeBIO {
	
	private BatchAddIptvNodeDAO dao;
	
	public static String SERVICE_ID_HBIPTVNODE_ADD = "3701";

	private static Logger logger = LoggerFactory.getLogger(BatchAddIptvNodeBIO.class);
	

	/**
	 * 获取今天总数
	 */
	public long getTodayCount() { 
		return dao.getTodayCount();		
	}
	
	public String addTaskHBLT(long taskid, long accoid, String deviceIds, String param, String strategy_type){
		try {
			logger.warn("BatchAddIptvNodeBIO.addTask()");
			
			String serviceId = SERVICE_ID_HBIPTVNODE_ADD;
			
			List<String> netAccounts = new ArrayList<String>();
			
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
				
				if(LipossGlobals.inArea(Global.HBLT) && sqlSpell.contains("[")){
					sqlSpell = sqlSpell.replaceAll("\\[", "\\'");
				}
				
				ArrayList<HashMap<String, String>> devList = dao.getDevIdsHBLT(sqlSpell);
				if(null==devList || devList.size()==0){
					logger.warn("==[{}]设备为空，程序结束==", taskid);
					return "false";
				}
				
				int num = dao.addTaskHBLT(taskid, accoid, sqlSpell, 2, serviceId, strategy_type);
				if(num>0){
					return "true";
				}else{
					return "false";
				}
			}
			else
			{
			
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
			}
			
			if(null==netAccounts || netAccounts.size()==0){
				logger.warn("==[{}]设备查询结果为空，程序结束==", taskid);
				return "false";
			}
			// 批量插入设备ID到临时表
			String filenameTmp = taskid + "";
			dao.insertTmp(filenameTmp, netAccounts);
			// 根据设备序列号获取设备信息
			ArrayList<HashMap<String, String>> devList = dao.getTaskDevListHBLT(filenameTmp);
			List<HashMap<String, String>> devListNew = new ArrayList<HashMap<String, String>>();
			
			if(null!=devList && devList.size()>0){
				for(HashMap<String, String> map : devList)
				{
					String aDeviceId = StringUtil.getStringValue(map, "a_device_id", "");
					
					if(StringUtil.IsEmpty(map.get("device_id")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到设备", taskid, aDeviceId);
					}
					else if(StringUtil.IsEmpty(map.get("username")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到loid", taskid, aDeviceId);
					}
					else if(!"1".equals(map.get("wan_type")) && !"2".equals(map.get("wan_type")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]既不是路由也不是桥接", taskid, aDeviceId);
					}
					else{
						devListNew.add(map);
					}
				}
				if(null==devListNew || devListNew.size()==0)
				{
					logger.warn("taskid[{}] devListNew is null", taskid);
					return "false";
				}
			}else{
				logger.warn("taskid[{}] devList is null", taskid);
				return "false";
			}
			
			logger.warn("taskid[{}]-devListNew.size[{}]", taskid, devListNew.size());
			if(devListNew.size()>10000){
				logger.warn("taskid[{}] 定制设备超过10000条，程序结束==", taskid);
				return "false10000";
			}
			// 获取sqlList
			ArrayList<String> sqlList = dao.sqlList(devListNew, taskid, serviceId);
			ArrayList<String> sqlListTmp = new ArrayList<String>();
			int count = 0;
			
			// 批量插入设备信息
			if(null!=sqlList && sqlList.size()>0){
				for(String sql : sqlList){
					sqlListTmp.add(sql);
					if(sqlListTmp.size()>=200){
						int res = dao.insertTaskDevHBLT(sqlListTmp);
						if(res>0){
							count += sqlListTmp.size();
						}
						sqlListTmp.clear();
					}
				}
			}
			if(sqlListTmp.size()>0){
				int res = dao.insertTaskDevHBLT(sqlListTmp);
				if(res>0){
					count += sqlListTmp.size();
				}
				sqlListTmp.clear();
			}
			
			logger.warn("==[{}]插入tab_addiptvnode_task_dev表[{}]条数据==", taskid, count);
			
			int num = dao.addTaskHBLT(taskid, accoid, "", 1, serviceId, strategy_type);
			List<String> devIdList = new ArrayList<String>();
			for(Map<String,String> map : devListNew){
				devIdList.add(StringUtil.getStringValue(map,"device_id",""));
			}
			
			String[] array = new String[devIdList.size()];
			if(num>0){
				if (1 == CreateObjectFactory.createPreProcess("1").processDeviceStrategy(
						devIdList.toArray(array), serviceId,
						new String[] { StringUtil.getStringValue(taskid)})) {
					logger.warn("调用后台预读模块成功");
					return "true";
				} else {
					logger.warn("调用后台预读模块失败");
					return "false";
				}
			}else{
				return "false";
			}
			
		} catch (Exception e) {
			logger.warn("批量定制任务执行失败e[{}]", e);
			return "false";
		}
	}
	

	public static Logger getLogger() {
		return logger;
	}

	public BatchAddIptvNodeDAO getDao() {
		return dao;
	}

	public void setDao(BatchAddIptvNodeDAO dao) {
		this.dao = dao;
	}

	
}
