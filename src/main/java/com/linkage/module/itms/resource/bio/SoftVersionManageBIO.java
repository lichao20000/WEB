package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.resource.obj.MQPublisher;
import com.linkage.module.gwms.resource.obj.SoftTask;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.SoftVersionManageDAO;

public class SoftVersionManageBIO {

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(SoftVersionManageBIO.class);

	private SoftVersionManageDAO softVersionManageDao;
	// 调用mq
//	public static final String MQ_SOFT_UP_TASK_ENAB = LipossGlobals.getLipossProperty("mqSoftUpTask.enab");
//
//	public static final String MQ_SOFT_UP_TASK_URL = LipossGlobals.getLipossProperty("mqSoftUpTask.url");
//
//	public static final String MQ_SOFT_UP_TASK_TOPIC = LipossGlobals.getLipossProperty("mqSoftUpTask.topic");

	/**
	 * 查询列表
	 * 
	 * @param vendor
	 * @param device_model
	 * @param devicetypeId
	 * @return
	 */
	public List<Map> getVersionList(String cityId,String vendor, String device_model,
			String devicetypeId, String tempId, String hardwareversion,
			int curPage_splitPage, int num_splitPage) {

		logger.debug("SoftVersionManageBIO==>getVersionList({},{},{},{},{})",
				new Object[] { vendor, device_model, devicetypeId, tempId,
						hardwareversion });

		return softVersionManageDao.getVersionList(cityId,vendor, device_model,
				devicetypeId, tempId, hardwareversion, curPage_splitPage,
				num_splitPage);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> parseExcel(String vendor,
			String device_model, String devicetypeId, String tempId,
			String hardwareversion) {
		logger.debug("SoftVersionManageBIO==>parseExcel({},{},{},{},{})",
				new Object[] { vendor, device_model, devicetypeId, tempId,
						hardwareversion });
		return softVersionManageDao.parseExcel(vendor, device_model, devicetypeId, tempId, hardwareversion);
	}
	
	/**
	 * 查询日志列表
	 * 
	 * @param vendor
	 * @param device_model
	 * @param devicetypeId
	 * @return
	 */
	public List<Map> getVersionLogList(String vendor, String device_model,
			String devicetypeId, String tempId, String hardwareversion,
			int curPage_splitPage, int num_splitPage) {

		logger.debug("SoftVersionManageBIO==>getVersionList({},{},{},{},{})",
				new Object[] { vendor, device_model, devicetypeId, tempId,
						hardwareversion });

		return softVersionManageDao.getVersionOperateLogList(vendor,
				device_model, devicetypeId, tempId, hardwareversion,
				curPage_splitPage, num_splitPage);
	}

	/**
	 * 新增
	 * 
	 * @param vendor_add
	 * @param device_model_add
	 * @param devicetypeIdOld
	 * @param devicetypeIdNew
	 * @return
	 */
	public String addInfo(String devicetypeIdOld, String devicetypeIdNew,
			String tempId, long acc_oid,String cityId_add) {

		logger.debug("SoftVersionManageBIO==>addInfo({},{},{})", new Object[] {
				devicetypeIdOld, devicetypeIdNew, tempId });

		
		String  resultStr = softVersionManageDao.addInfo(devicetypeIdOld, devicetypeIdNew,tempId, acc_oid,cityId_add);
		/*String[] results = resultStr.split(";");
		if ("1".equals(results[0]))
		{
			this.sendTask(StringUtil.getIntegerValue(devicetypeIdOld));
		}*/
		return resultStr;
	}
	public void sendTask(long taskId) {
		//通知软件升级模块
		SoftTask task = new SoftTask("WEB",StringUtil.getIntegerValue(taskId), 2);
		 try{
//			 MQPublisher publisher = new MQPublisher(MQ_SOFT_UP_TASK_ENAB,
//			 MQ_SOFT_UP_TASK_URL, MQ_SOFT_UP_TASK_TOPIC);
			 MQPublisher publisher = new MQPublisher("picUp.task");
			 publisher.publishMQ(task);
			 logger.warn("通知软件升级模块更新内存成功");
		 }catch(Exception e ){
			 e.printStackTrace();
			 logger.error("MQ消息发布失败， mesg({})", e.getMessage());
			 logger.warn("通知失败");
		 }
	}

	/**
	 * 删除
	 * 
	 * @param devicetypeIdOld
	 * @return
	 */
	public String deleInfo(String devicetypeIdOld, String devicetypeIdNew,
			String tempId, long acc_oid,String cityId) {
		logger.debug("SoftVersionManageBIO==>deleInfo({})",
				new Object[] { devicetypeIdOld });
		String resultStr = softVersionManageDao.deleInfo(devicetypeIdOld, devicetypeIdNew,
				tempId, acc_oid,cityId);
		/*String[] results = resultStr.split(";");
		if ("1".equals(results[0]))
		{
			this.sendTask(StringUtil.getIntegerValue(devicetypeIdOld));
		}*/
		return resultStr;
	}

	/**
	 * 获取原软件版本的复选框 和 目标软件版本的下拉框
	 * 
	 * @param vendor_add
	 * @param device_model_add
	 * @return
	 */
	public String getVersionCheckList(String vendor_add, String device_model_add) {

		logger.debug("SoftVersionManageBIO==>getVersionList({},{})",
				new Object[] { vendor_add, device_model_add });
		return softVersionManageDao.getVersionCheckList(vendor_add,
				device_model_add);
	}
	
	public String getVersion(String vendor_add, String device_model_add,String devicetypeIdNew) {

		logger.debug("SoftVersionManageBIO==>getVersion({},{})",
				new Object[] { vendor_add, device_model_add,devicetypeIdNew });
		return softVersionManageDao.getVersion(vendor_add,
				device_model_add,devicetypeIdNew);
	}

	public SoftVersionManageDAO getSoftVersionManageDao() {
		return softVersionManageDao;
	}

	public void setSoftVersionManageDao(
			SoftVersionManageDAO softVersionManageDao) {
		this.softVersionManageDao = softVersionManageDao;
	}

	public int getMaxPage(String cityId,String vendor, String device_model,
			String devicetypeId, String tempId, String hardwareversion,
			int curPage_splitPage, int num_splitPage) {
		// TODO Auto-generated method stub

		return softVersionManageDao.getMaxPage(cityId,vendor, device_model,
				devicetypeId, tempId, hardwareversion, curPage_splitPage,
				num_splitPage);
	}

	public int getVersionLogMaxPage(String vendor, String device_model,
			String devicetypeId, String tempId, String hardwareversion,
			int curPage_splitPage, int num_splitPage) {
		// TODO Auto-generated method stub
		return softVersionManageDao.getVersionOperateLogMaxPage(vendor,
				device_model, devicetypeId, tempId, hardwareversion,
				curPage_splitPage, num_splitPage);
	}
}
