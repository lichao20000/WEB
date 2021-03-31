
package com.linkage.module.gwms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.module.gwms.resource.act.BatchResumeThread;
import com.linkage.module.gwms.resource.dao.BatchResumeDAO;
import com.linkage.module.gwms.util.StringUtil;

public class BatchResumeBIO
{

	private static Logger logger = LoggerFactory.getLogger(BatchResumeBIO.class);
	private BatchResumeDAO dao;
	
	public BatchResumeDAO getDao() {
		return dao;
	}

	public void setDao(BatchResumeDAO dao) {
		this.dao = dao;
	}

	public String importTask(long taskid,String task_name,long accoid, String deviceIds, String param){
		try {
			ArrayList<HashMap<String, String>> devList=new ArrayList<HashMap<String,String>>();
			
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
				
				devList = dao.getDevIds(sqlSpell);
				//插入任务
				dao.addTask(task_name,taskid, accoid, sqlSpell, 2,0);
				
			}else{
				List<String> netAccounts = new ArrayList<String>();
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
				devList = dao.getTaskDevList(filenameTmp);
				//插入任务
				dao.addTask(task_name,taskid, accoid, "", 1,0);
			}
			
			if(null==devList || devList.size()==0){
				logger.warn("==[{}]设备为空，程序结束==", taskid);
				return "false";
			}else if(devList.size()>1000){
				logger.warn("taskid[{}] 定制设备超过1000条，程序结束==", taskid);
				return "false1000";
			}else {
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
				logger.warn("==[{}]插入gw_device_resume_task_dev表[{}]条数据==", taskid, count);
				//异步执行恢复出厂
				BatchResumeThread thread=new BatchResumeThread(taskid);
				thread.start();
				return "true";
			}
			
		} catch (Exception e) {
			logger.warn("批量定制任务执行失败e[{}]", e);
			return "false";
		}
	}
	
}
