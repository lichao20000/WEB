
package com.linkage.module.gwms.resource.bio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.resource.dao.BatchConfigDAO;

public class BatchConfigBIO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(BatchConfigBIO.class);
	private BatchConfigDAO dao;
	

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(BatchConfigDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * @return the dao
	 */
	public BatchConfigDAO getDao()
	{
		return dao;
	}
	
	

	public String deleteTask(String taskId) {
		String message = "";
		dao.deleteTask(taskId);
		message = "删除成功";
		return message;
	}

	/**
	 * 定制任务
	 */
	public void insertTask(String mode,List<Map<String,String>> devList,String paramValue,String paramPath,
			String paramType,long time, String task_id, String task_name, String id) throws Exception
	{
		dao.insertTask(mode,devList,paramValue,paramPath,paramType,time, task_id, task_name, id);
	}

	public List<Map<String, String>> queryDevice(String gwShare_devicetypeId,
			String gwShare_deviceModelId, String gwShare_vendorId, String cqSoftCitys,
			String startOpenDate, String endOpenDate)
	{
		return dao.queryDevice(gwShare_devicetypeId,gwShare_deviceModelId,gwShare_vendorId,cqSoftCitys, startOpenDate, endOpenDate);
	}
	
	
	public String getStrategyCQList(String name)
	{
		StringBuffer tmpBufer = new StringBuffer();
		tmpBufer.append("<INPUT type=\"checkbox\" >Inform周期上报信息");
		tmpBufer.append("<INPUT type=\"checkbox\" >终端启动");
		return tmpBufer.toString();
	}
	
	
	/**
	 * 查询任务列表
	 */
	public List<Map> queryTaskList(String task_name_query, String status_query,
			String expire_time_start, String expire_time_end,int curPage_splitPage,
			int num_splitPage){
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
		
		return dao.queryTaskList4CQ(task_name_query, status, expireTimeStart, expireTimeEnd, curPage_splitPage, num_splitPage);
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
	
	/**
	 * 根据条件查询设备列表
	 * @param task_id 任务id
	 * @param city_id 属地id
	 * @param type 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryDevList4CQ(String task_id, String city_id, String type, int curPage_splitPage, int num_splitPage){
		return dao.queryDevList4CQ(task_id, city_id, type, curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 设备列表最大页数
	 * @param countNum
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryDevCount4CQ(String countNum, int curPage_splitPage, int num_splitPage)
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
	
	public List<Map> queryTaskGyCityList(String task_id,String task_name, int curPage_splitPage, int num_splitPage){
		return dao.queryTaskGyCityList(task_id, task_name, curPage_splitPage, num_splitPage);
	}
	
	public int queryTaskGyCityCount(String task_id,String task_name, int curPage_splitPage, int num_splitPage){
		int total =  dao.queryTaskGyCityCount(task_id);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/*public int queryTaskGyCityCount(String task_id, String expire_time_end, int curPage_splitPage,
			int num_splitPage) {
		return dao.queryTaskGyCityCount(task_id, curPage_splitPage, num_splitPage);
	}*/
	
	public int queryTaskCount(String task_name_query, String status_query,
			String expire_time_start, String expire_time_end, int curPage_splitPage,
			int num_splitPage) {
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
		return dao.queryTaskCount4CQ(task_name_query, status, expireTimeStart, expireTimeEnd, curPage_splitPage, num_splitPage);
	}

	public String update(String task_id,String type){
		String res = dao.update(task_id,type)+"";
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
	
	
}
