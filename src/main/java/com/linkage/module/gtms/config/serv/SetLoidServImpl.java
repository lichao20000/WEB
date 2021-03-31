package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.Global;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.config.dao.SetLoidDAO;
import com.linkage.module.gtms.obj.SetLoidTask;
import com.linkage.module.gwms.resource.obj.MQPublisher;
import com.linkage.module.gwms.util.StringUtil;

public class SetLoidServImpl implements SetLoidServ {

	private static Logger logger = LoggerFactory
			.getLogger(SetLoidServImpl.class);

	// 调用mq
//	public static final String MQ_LOID_TASK_ENAB = LipossGlobals
//			.getLipossProperty("mqLoid.enab");
//
//	public static final String MQ_LOID_TASK_URL = LipossGlobals
//			.getLipossProperty("mqLoid.url");
//
//	public static final String MQ_LOID_TASK_TOPIC = LipossGlobals
//			.getLipossProperty("mqLoid.topic");
	private SetLoidDAO dao;

	/**
	 * 参看设备回填情况
	 */
	@SuppressWarnings("unchecked")
	public List queryDeviceDetail(String loid, String deviceNumber,
			String startTime, String endTime, String statu) {
		return dao.queryDeviceDetail(loid, deviceNumber, startTime, endTime,
				statu);
	}

	@SuppressWarnings("unchecked")
	public List queryDeviceDetail(String loid, String deviceNumber,
			String startTime, String endTime, String statu,
			int curPage_splitPage, int num_splitPage) {
		return dao.queryDeviceDetail(loid, deviceNumber, startTime, endTime,
				statu, curPage_splitPage, num_splitPage);
	}

	/**
	 * 参看设备回填情况总共的页数
	 */
	public int getCount(String loid, String deviceNumber, String startTime,
			String endTime, String statu, int curPage_splitPage,
			int num_splitPage) {

		return dao.getCount(loid, deviceNumber, startTime, endTime, statu,
				curPage_splitPage, num_splitPage);
	}

	@Override
	public String doConfig(String cityId, long userId,
			List<Map<String, String>> list) {
		// 当前的时间
		// 获取配置参数(XML)字符串
		ArrayList<String> sqllist = new ArrayList<String>();
		// taskId 为当前时间的数字格式
		Long taskId = 0l;
		DateTimeUtil dt = null;
		dt = new DateTimeUtil();
		taskId = dt.getLongTime();
		// 导入任务表
		int size = list.size();
		// 增加定制任务sql
		dao.addSetLoidTask(taskId, cityId, userId);
		// 查询设备表，将deviceId过滤出来
		Map tempMap = null;
		String oui = "";
		String device_serialnumber = "";

		// 拼装SQL条件 形如('1','2','3' eg.)
		StringBuffer deviceStrs = new StringBuffer(100);
		if (size != 0) {
			deviceStrs.append("('")
					.append(list.get(0).get("device_serialnumber")).append("'");
			if (size > 1) {
				for (int i = 1; i < size - 1; i++) {
					tempMap = list.get(i);
					if (null != tempMap) {
						device_serialnumber = StringUtil.getStringValue(tempMap
								.get("device_serialnumber"));
						deviceStrs.append(",'").append(device_serialnumber)
								.append("'");
					}
					// 生成入软件升级任务关联设备表的sql
				}
			}
			deviceStrs.append(",'")
					.append(list.get(size - 1).get("device_serialnumber"))
					.append("')");
		}
		List reList = dao.getDeviceList(deviceStrs.toString());

		Map tMap = null;
		String tDeviceSN = "";
		String tLoid = "";
		String deviceId = "";
		if (null != reList && reList.size() > 0) {
			for (int j = 0; j < reList.size(); j++) {
				tMap = (Map) reList.get(j);
				tDeviceSN = StringUtil.getStringValue(tMap
						.get("device_serialnumber"));
				deviceId = StringUtil.getStringValue(tMap.get("device_id"));
				if (!StringUtil.IsEmpty(tDeviceSN)) {
					for (int k = 0; k < list.size(); k++) {
						tempMap = list.get(k);
						if (null != tempMap) {
							tLoid = StringUtil.getStringValue(tempMap
									.get("loid"));
							device_serialnumber = StringUtil
									.getStringValue(tempMap
											.get("device_serialnumber"));
							if (tDeviceSN.equals(device_serialnumber)) {
								sqllist.add(this.createLoidTaskSQL(taskId,
										deviceId, device_serialnumber, tLoid));
							}
						}
					}
				}
			}
		}
		logger.warn("sqllist.size()==" + sqllist.size());
		logger.warn("sqllist.toString()==" + sqllist.toString());
		// 入库
		// LipossGlobals.ALL_SQL_IPTV.addAll(sqllist);
		dao.doInsertDevices(sqllist);
		// 发MQ消息
		//本需求一开始是专为安徽做的，现江苏也实现类似的需求，但是又略有不同
		this.sendTask(taskId);
		return "任务定制成功";
	}

	// 定制的设备
	public String createLoidTaskSQL(Long taskId, String deviceId,
			String device_serialnumber, String loid) {
		String sql = "";
		sql = "insert into gw_setloid_device values (" + taskId + ",'"
				+ deviceId + "','" + device_serialnumber + "','" + loid + "',"
				+ 0 + "," + taskId + ")";
		logger.debug(sql);
		logger.warn("sql===" + sql.toString());
		return sql;
	}

	public void sendTask(long taskId) {
		// 通知回填模块，借用下软件升级的实体类
		SetLoidTask task = new SetLoidTask("WEB",
				StringUtil.getIntegerValue(taskId), 1);
		try {
			
//			MQPublisher publisher = new MQPublisher(MQ_LOID_TASK_ENAB,
//					MQ_LOID_TASK_URL, MQ_LOID_TASK_TOPIC);
			MQPublisher publisher = new MQPublisher("mqLoid.topic");			
			publisher.publishMQ(task);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MQ消息发布失败， mesg({})", e.getMessage());
		}
	}

	public void setDao(SetLoidDAO dao) {
		this.dao = dao;
	}
}
