package com.linkage.module.gtms.config.serv;

import java.util.List;
import java.util.Map;

public interface GetNodeBatchConfigServ {
	
	public List<Map> getFileOperRecordList(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime);
	public int getNodeBatchCount(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime);
	public int recordTask(long add_time, String gather_path,String file_name,long acc_oid);
	public String recordDev(long add_time,String device_id,String oui,String device_serialnumber,String loid,String gather_path,String city_id);
	public int recordDevs(long add_time, List<String> device_idlist,List<String> ouilist,List<String> device_serialnumberlist,List<String> loidlist,String gather_path ,List<String> city_idlist);
	public int queryCustomNum();
	public int checkRepeatName(String checkRepeatname);
}
