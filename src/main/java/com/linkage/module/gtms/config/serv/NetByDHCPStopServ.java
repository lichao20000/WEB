package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gtms.config.dao.NetByDHCPStopDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;

public class NetByDHCPStopServ {
	private NetByDHCPStopDAO dao;

	private static Logger logger = LoggerFactory
			.getLogger(NetByDHCPStopDAO.class);

	/**
	 * 查询是否绑定
	 */
	public String isBind(String gwShare_queryField, String gwShare_queryParam) {
		logger.warn("NetByDHCPStopServ.isBind()");
		return dao.isBind(gwShare_queryField, gwShare_queryParam);
	}

	/**
	 * 查询宽带帐号
	 */
	/*
	 * public String getUsername(String deviceIds) { return
	 * dao.getUsername(deviceIds); }
	 */

	public String doConfig(long userId, List<String> list,
			String strategy_type, String username, String gwType) {
		logger.warn("NetByDHCPStopServ.doConfig()");
		String res = "0";
		long time = new DateTimeUtil().getLongTime();
		String serviceId = "2004";
		try {
			dao.doConfig(userId, list, gwType, serviceId, strategy_type,
					username, time);
			res = "1";
		} catch (Exception e) {
			res = "-1";
		}
		String[] array = new String[list.size()];
		if ("1".equals(res)) {
			if (1 == CreateObjectFactory.createPreProcess(gwType).processDeviceStrategy(
					list.toArray(array), serviceId,
					new String[] { StringUtil.getStringValue(time) })) {
				res = "1";
			} else {
				res = "-4";
			}
		}

		return res;
	}

	/*
	 * 每页绑定数据
	 */
	public List<Map> getDetailsForPage(List<Map> device_idList,
			String starttime, String endtime, String openState,
			int curPage_splitPage, int num_splitPage) {
		logger.warn("NetByDHCPStopServ.getDetailsForPage()");
		List<Map> list = new ArrayList<Map>();
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if (!"".equals(starttime) && starttime != null) {
			starttimeCount = DateUtil.getTimeInSecond(starttime,
					"yyyy-MM-dd HH:mm:ss");
		}
		if (!"".equals(endtime) && endtime != null) {
			endtimeCount = DateUtil.getTimeInSecond(endtime,
					"yyyy-MM-dd HH:mm:ss");
		}
		String device_id = "'";
		if (null != device_idList && device_idList.size() > 0) {
			device_id += StringUtil.getStringValue(device_idList.get(0),
					"device_id");
		}
		device_id += "'";
		list = dao.getDetailsForPage(device_id, starttimeCount, endtimeCount,
				openState, curPage_splitPage, num_splitPage);
		return list;

	}

	/*
	 * 获取最大页数
	 */
	public int getDetailsCount(List<Map> device_idList, String starttime,
			String endtime, String openState, int num_splitPage) {
		logger.warn("NetByDHCPStopServ.getDetailsCount()");
		int maxPage = 1;
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if (!"".equals(starttime) && starttime != null) {
			starttimeCount = DateUtil.getTimeInSecond(starttime,
					"yyyy-MM-dd HH:mm:ss");
		}
		if (!"".equals(endtime) && endtime != null) {
			endtimeCount = DateUtil.getTimeInSecond(endtime,
					"yyyy-MM-dd HH:mm:ss");
		}
		String device_id = "'";
		if (null != device_idList && device_idList.size() > 0) {
			device_id += StringUtil.getStringValue(device_idList.get(0),
					"device_id");
		}
		device_id += "'";
		maxPage = dao.getDetailsCount(device_id, starttimeCount, endtimeCount,
				openState, num_splitPage);
		return maxPage;

	}

	/*
	 * 开始日期格式转换 yyyy-mm-dd --> 毫秒
	 */
	public static long startTimeOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		System.out.println(cal.getTime());

		return cal.getTimeInMillis() / 1000;
	}

	/*
	 * 结束日期转换 yyyy-mm-dd --> 毫秒
	 */
	public static long endTimeOfMonth(Date date) {
		// 将当前月设置为第一天后，加1个月，然后-1秒。
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		System.out.println(cal.getTime());

		return cal.getTimeInMillis() / 1000 - 1;
	}

	public NetByDHCPStopDAO getDao() {
		return dao;
	}

	public void setDao(NetByDHCPStopDAO dao) {
		this.dao = dao;
	}

	public static Logger getLogger() {
		return logger;
	}
}
