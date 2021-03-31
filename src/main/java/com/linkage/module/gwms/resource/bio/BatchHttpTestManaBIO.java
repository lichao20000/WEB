package com.linkage.module.gwms.resource.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.dao.BatchHttpTestManaDAO;
import com.linkage.module.gwms.util.StringUtil;

public class BatchHttpTestManaBIO {

	private BatchHttpTestManaDAO dao;
	
	
	public void setDao(BatchHttpTestManaDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param cityId
	 * @param taskName
	 * @param acc_oid
	 * @param accName
	 * @param isLocked
	 * @return
	 */
	public List<HashMap<String,String>> getOrderTaskList(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName,String type) {
		
		List<HashMap<String,String>> list = dao.getOrderTaskList(curPage_splitPage, num_splitPage,
				 startTime, endTime, cityId, taskName,acc_oid,accName,type);
		
		for (HashMap<String,String> map : list)
		{
			long add_time_l = StringUtil.getLongValue(map.get("add_time"));
			map.put("add_time", new DateTimeUtil(add_time_l * 1000).getYYYY_MM_DD_HH_mm_ss());
			String status = map.get("task_status");
			if("0".equals(status)){
				status = "未做";
			}
			else if("1".equals(status)){
				status = "已做";
			}
			else if("-1".equals(status)){
				status = "失效";
			}
			else if("2".equals(status)){
				status = "正在加载明细";
			}
			else if("3".equals(status) && Global.AHLT.equals(Global.instAreaShortName)){
				status = "已做";
			}
			else{
				status = "异常";
			}
			map.put("task_status", status);
			String desc = map.get("task_desc");
			desc = !StringUtil.IsEmpty(desc)&&desc.length()>20?desc.substring(0, 20)+"...":desc;
			map.put("task_desc_short", desc);
		}
		return list;
	}
	public int countOrderTask(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName,String type) {
		// TODO Auto-generated method stub
		return dao.countOrderTask(curPage_splitPage,
				num_splitPage,startTime, endTime, cityId, taskName,acc_oid,accName,type);
	}
	public int countDeviceTask(int curPage_splitPage, int num_splitPage,String taskId, String upResult) {
		return dao.countTaskResult(curPage_splitPage, num_splitPage, taskId, upResult);
	}
	public List getTaskResult(String taskId ,String upResult, int curPage_splitPage,int num_splitPage) {
		return dao.getTaskResult(taskId,upResult, curPage_splitPage, num_splitPage);
	}
	
	
	/**
	 * 失效操作
	 * @param taskId
	 * @return
	 */
	public String doDisable(String taskId) {
		return dao.doDisable(taskId);
	}
	
	
	/**
	 * 更新描述 
	 * @param taskId 任务id
	 * @param desc 描述
	 * @return 结果 1成功 0失败
	 */
	public String commitDesc(String taskId,String desc) {
		return dao.commitDesc(taskId,desc);
	}
	
	/**
	 * 删除操作
	 * @param taskId
	 * @return
	 */
	public String doDelete(String taskId) {
		return dao.doDelete(taskId);
	}

	public Map getTaskCount(String taskId) {
		int total = dao.countTaskResult(taskId);
		Map<String, String> map = dao.getCountResult(taskId);
		int succ = StringUtil.getIntegerValue(map.get("succ"));
		int undo = StringUtil.getIntegerValue(map.get("undo"));
		map.put("fail", StringUtil.getStringValue(total-succ-undo));
		map.put("all", StringUtil.getStringValue(total));
		return map;
	}


}
