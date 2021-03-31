package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.config.dao.GetNodeBatchConfigDAO;

public class GetNodeBatchConfigServImpl implements GetNodeBatchConfigServ{
	private static Logger logger = LoggerFactory
			.getLogger(GetNodeBatchConfigServImpl.class);

	private GetNodeBatchConfigDAO dao;
	
	
	public int getNodeBatchCount(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime)
	{
		return dao.getNodeBatchCount(curPage_splitPage, num_splitPage, customId, fileName, starttime, endtime);
	}
	
	public List<Map> getFileOperRecordList(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime){
		return dao.getNodeBatchList(curPage_splitPage, num_splitPage, customId, fileName, starttime, endtime);
	}
	
	public int recordTask(long add_time, String gather_path,String file_name,long acc_oid){
		
		int do_status = 0;
		StringBuffer storePath = new StringBuffer();
		storePath.append(LipossGlobals.getLipossProperty("nodeftp.localDir"));
		
		storePath.append(file_name);
		String result_file =storePath.toString();
		String file_path = "";
		int records = dao.recordTask(acc_oid, add_time, gather_path, file_name, file_path, do_status, result_file);
		return records;
	}
	
	public int recordDevs(long add_time, List<String> device_idlist,List<String> ouilist,List<String> device_serialnumberlist,List<String> loidlist,String gather_path,List<String> city_idlist){
		int records = 0;
		ArrayList<String> devSqlList = new ArrayList<String>();
		for(int i = 0;i<device_idlist.size();i++){
			String device_id = (String)device_idlist.get(i);
			String oui = (String)ouilist.get(i);
			String device_serialnumber = (String)device_serialnumberlist.get(i);
			String city_id = (String)city_idlist.get(i);
			String loid = (String)loidlist.get(i);
			String devSql = recordDev(add_time, device_id, oui, device_serialnumber, loid, gather_path, city_id);
			devSqlList.add(devSql);
			if(devSqlList.size() >= 400){
				records += dao.recordDev(devSqlList);
				devSqlList.clear();
			}
		}
		if(devSqlList.size() != 0){
			records += dao.recordDev(devSqlList);
			devSqlList.clear();
		}
		return records;
	}
	
	
	public String recordDev(long add_time,String device_id,String oui,String device_serialnumber,String loid,String gather_path,String city_id){
		
		String[] gather_pathArr = gather_path.split(";");
		Map<Integer,String> map = new HashMap<Integer, String>();
		for(int i = 0;i<gather_pathArr.length;i++){
			map.put(i+1, gather_pathArr[i]);
		}
		String gather_path1 = map.get(1);
		String gather_path2 = map.get(2);
		String gather_path3 = map.get(3);
		String gather_path4 = map.get(4);
		if(null == gather_path1){
			gather_path1 = "";
		}
		if(null == gather_path2){
			gather_path2 = "";
		}
		if(null == gather_path3){
			gather_path3 = "";
		}
		if(null == gather_path4){
			gather_path4 = "";
		}
		long gather_status = 0;
		String gather_times = "0";
		int records = 0;
		String city_name = CityDAO.getCityIdCityNameMap().get(city_id);
		String devSql = dao.getDevSql(add_time, device_id, oui, device_serialnumber, loid, gather_path1,
				gather_path2, gather_path3, gather_path4,gather_status, gather_times, city_id, city_name);
		return devSql;
	}
	
	public int queryCustomNum() {
		logger.debug("queryUndoNum()");
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY); 
		int minute = c.get(Calendar.MINUTE); 
		int second = c.get(Calendar.SECOND);
		long todayTimeMillis = System.currentTimeMillis()/1000 -(long)(hour*60*60+minute*60+second);
		return dao.queryCustomNum(todayTimeMillis);
	}
	
	public int checkRepeatName(String checkRepeatname) {
		
		return dao.queryRepeatName(checkRepeatname);
	}

	public GetNodeBatchConfigDAO getDao()
	{
		return dao;
	}


	public void setDao(GetNodeBatchConfigDAO dao)
	{
		this.dao = dao;
	}

	

}
