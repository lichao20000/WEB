/**
 * 
 */
package com.linkage.module.gtms.config.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenjie
 * 
 */
public interface ParamNodeBatchConfigDAO {

	public int queryUndoNum();
	public int addBatParamTask(long taskId, long accOid, int operType, int servType,
			int operStatus, int gwType,  long addTime,String pathvalue,String filename);
	public int[] addBatParamDev(List<Map> list);
	public int queryRepeatName(String checkRepeatname);
	public int getParNodeCount(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime);
	public List<Map> getParNodeList(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime);
	public List<Map> getFileMsg(String task_id);
	public int queryCustomNum(long todayTimeMillis);
	public void init1();
	public void updateStatus(long task_id);
	public List<HashMap<String, String>> queryDevices(String query_sql);
	public List<HashMap<String, String>> queryUnDoneTask();
	public void insertDev(List<HashMap<String, String>> deviceIdsNew,long task_id);
}
