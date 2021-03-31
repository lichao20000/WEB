package com.linkage.module.gtms.config.serv;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.linkage.module.gtms.config.dao.ParamNodeBatchConfigDAO;

public interface ParamNodeBatchConfigServ {

	public String doConfigAll(String[] deviceIds, String serviceId, String[] paramArr,
			String gwType);
	public int addTask(String template_id, String queryType,String task_name,String device_id,String file_name,String city_id,String vendor,String model,String devicetype_id,String isBind, String type, long id,String starttime,String endtime,String isnow);
	public int addTaskStb(String template_id, String queryType,String task_name,String device_id,String file_name,String city_id,String vendor,String model,String devicetype_id,String isBind, String type, long id,String starttime,String endtime,String isnow);
	public String doConfigAllStb(String[] deviceIds, String serviceId, String[] paramArr,
			String gwType);
	public int queryUndoNum();
	public String parNodeBatch(long accOid, String deviceIds, String gwType,
			 String pathvalue,String paramvalues,String filename,String city_ids,String usernames);
	public int checkRepeatName(String checkRepeatname);
	public int getParNodeCount(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime);
	public List<Map> getParNodeList(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime);
	public void saveListToTxt(List<Map> list,String strFileName);
	public ParamNodeBatchConfigDAO getDao();
	public String deleteFile(String file_path);
	public void download(String filepath, HttpServletResponse response);
	public int queryCustomNum();
	public String getPreResult(String deviceId,String gw_type);
}
