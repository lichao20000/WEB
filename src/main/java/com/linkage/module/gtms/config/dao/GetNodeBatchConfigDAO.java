/**
 * 
 */
package com.linkage.module.gtms.config.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenjie
 * 
 */
public interface GetNodeBatchConfigDAO {
	public int getNodeBatchCount(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime);
	public List<Map> getNodeBatchList(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime);
	public Map<String, String> getLoginName();
	public int recordTask(long acc_oid, long add_time, String gather_path, String file_name, String file_path, int do_status, String result_file);
	public String getDevSql(long add_time,String device_id, String oui, String device_serialnumber, String loid,
			String gather_path1, String gather_path2, String gather_path3,String gather_path4, 
			long gather_status, String gather_times, String city_id, String city_name);
	public int queryCustomNum(long todayTimeMillis);
	public int queryRepeatName(String checkRepeatname);
	public int recordDev(ArrayList<String> devSqlList);
}
