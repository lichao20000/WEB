package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.obj.tr069.PicUp;
import com.linkage.module.gtms.stb.obj.tr069.Task;
import com.linkage.module.gtms.stb.resource.dao.PicTaskQueryDAO;
import com.linkage.module.gwms.resource.obj.MQPublisher;

/**
 * @author wuchao(工号) added zhangsibei
 * @version 1.0
 * @since 2012-3-30 上午10:04:49
 * @category com.linkage.module.lims.stb.resource.bio
 * @copyright 南京联创科技 网管科技部
 */
public class PicTaskQueryBIO {

	private static Logger logger = LoggerFactory
			.getLogger(PicTaskQueryBIO.class);

	public PicTaskQueryDAO dao;

//	public  final String MQ_PIC_UP_TASK_ENAB = LipossGlobals.getLipossProperty("mqPicUpTask.enab");
//	public   String MQ_PIC_UP_TASK_ENAB = null;
////	public  final String MQ_PIC_UP_TASK_URL = LipossGlobals.getLipossProperty("mqPicUpTask.url");
//	public   String MQ_PIC_UP_TASK_URL = null;
////	public  final String MQ_PIC_UP_TASK_TOPIC = LipossGlobals.getLipossProperty("mqPicUpTask.topic");
//	public   String MQ_PIC_UP_TASK_TOPIC = null;
	public List<Map> query(String priority, String status, String cityId,
			String groupId, String tradeId,String transactionId, int curPage_splitPage,
			int num_splitPage) {
		return dao.query(priority, status, cityId, groupId, tradeId,transactionId,
				curPage_splitPage, num_splitPage);
	}

	public int getCount(String priority, String status, String cityId,
			String groupId, String tradeId,String transactionId, int curPage_splitPage,
			int num_splitPage) {
		return dao.getCount(priority, status, cityId, groupId, tradeId, transactionId,
				curPage_splitPage, num_splitPage);
	}

	/**
	 * 
	 * @param taskId
	 *            任务编号
	 * @param transactionId
	 *            事物编号
	 * @param status
	 *            状态
	 * @param reason
	 *            手动失效原因
	 * @param accId
	 *            用户ID
	 * @return 更新状态是否成功
	 */
	public String updateStatus(String taskId, String transactionId,
			String status, String reason, long accId) {
		
//		MQ_PIC_UP_TASK_ENAB = LipossGlobals.getLipossProperty("mqPicUpTask.enab");
//		MQ_PIC_UP_TASK_URL = LipossGlobals.getLipossProperty("mqPicUpTask.url");
//		MQ_PIC_UP_TASK_TOPIC = LipossGlobals.getLipossProperty("mqPicUpTask.topic");
		// 改变数据库策略的状态
		int res = dao
				.updateStatus(taskId, transactionId, status, reason, accId);
		String priority = "";
		String city_id = "";
		String group_id = "";
		String trade_id = "";
		//查询数据库中行业，分组，优先级属地等
		Map map = dao.getByTaskId(taskId);
		priority = StringUtil.getStringValue(map.get("priority"));
		city_id = StringUtil.getStringValue(map.get("city_id"));
		trade_id = StringUtil.getStringValue(map.get("trade_id"));
		group_id = StringUtil.getStringValue(map.get("group_id"));
		
		 if(res > 0 ){
		 //通知配置模块
		 logger.warn("通知配置模块，改变策略状态，taskId="+taskId);
		 //Task(clientId,taskId,action) 其中action的值为1：新增 2：修改 3：删除
		 Task task = new Task("WEB",taskId, "2",priority,city_id,trade_id,group_id);
					
		 PicUp obj = new PicUp(task);
		 try{
//			 MQPublisher publisher = new MQPublisher(MQ_PIC_UP_TASK_ENAB,
//			 MQ_PIC_UP_TASK_URL, MQ_PIC_UP_TASK_TOPIC);
			 MQPublisher publisher = new MQPublisher("soft.task");
			 publisher.publishMQ(obj);
		 }catch(Exception e ){
			 e.printStackTrace();
			 logger.error(" MQ消息发布失败， mesg({})", e.getMessage());
		 }
		 }else{
		 return "-1";
		 }
		return res + "";
	}

	public String deleteStrategy(String taskId) {
		
//		MQ_PIC_UP_TASK_ENAB = LipossGlobals.getLipossProperty("mqPicUpTask.enab");
//		MQ_PIC_UP_TASK_URL = LipossGlobals.getLipossProperty("mqPicUpTask.url");
//		MQ_PIC_UP_TASK_TOPIC = LipossGlobals.getLipossProperty("mqPicUpTask.topic");
		// 删除策略
		int exist = dao.getStrategyByTaskId(taskId);
		int batchExist = dao.getBatchStrategyByTaskId(taskId);
		//查询数据库中行业，分组，优先级属地等
		String priority = "";
		String city_id = "";
		String group_id = "";
		String trade_id = "";
		Map<String,Object> map = dao.getByTaskId(taskId);
		priority = StringUtil.getStringValue(map.get("priority"));
		city_id = StringUtil.getStringValue(map.get("city_id"));
		trade_id = StringUtil.getStringValue(map.get("trade_id"));
		group_id = StringUtil.getStringValue(map.get("group_id"));
		
		int res = 0;
		if (exist > 0) {
			res = dao.deleteStrategyAndRecord(taskId);
		}
		//只有当优先级为导入帐号的时候才删除此表
		if("1".equals(priority)&&(batchExist>0)){
			dao.deleteBatchStrategy(taskId);
		}
		if (res > 0) {
			// 通知配置模块
			logger.warn("通知配置模块，删除该策略，taskId=" + taskId);
			Task task = new Task("WEB", taskId, "3",priority,city_id,trade_id,group_id);

			PicUp obj = new PicUp(task);

//			MQPublisher publisher = new MQPublisher(MQ_PIC_UP_TASK_ENAB,
//					MQ_PIC_UP_TASK_URL, MQ_PIC_UP_TASK_TOPIC);
			MQPublisher publisher = new MQPublisher("soft.task");

			publisher.publishMQ(obj);
		} else {
			return "-1";
		}
		return res + "";
	}

	public Map<String, String> detail(String taskId) {
		return dao.detail(taskId);
	}

	public Map<String, String> result(String taskId) {
		return dao.result(taskId);
	}

	public PicTaskQueryDAO getDao() {
		return dao;
	}

	public void setDao(PicTaskQueryDAO dao) {
		this.dao = dao;
	}

	public List<Map<String, String>> getAlllGroupName() {
		return dao.getAlllGroupName();
	}

	/**
	 * 查询一天当中生效失效操作的次数
	 * 
	 * @param taskId
	 * @return
	 */
	public String getCountOperate(String taskId) {

		String flag = "1";

		String initTime = new DateTimeUtil().getDate() + " 00:00:00";
		long starttime = new DateTimeUtil(initTime).getLongTime();
		long endtime = starttime + 24 * 60 * 60;
		int res1 = dao.getCountOper(taskId, starttime, endtime, flag);
		int res2 = dao.getCountOper(taskId, starttime, endtime, "");

		return res1 + "," + res2;
	}
	public List<Map> getSoftUpDetail(String taskId,String updateStatus,
			int curPage_splitPage, int num_splitPage,String sortName,String hdorsd){
		    return dao.getSoftUpDetail(taskId,updateStatus,curPage_splitPage,num_splitPage,sortName,hdorsd);
	}

	public int getSoftCount(String taskId, String updateStatus,
			int curPageSplitPage, int numSplitPage, String sortName,
			String hdorsd) {
		
		return dao.getSoftUpCount(taskId, updateStatus, curPageSplitPage, numSplitPage, sortName, hdorsd);
	}
}
