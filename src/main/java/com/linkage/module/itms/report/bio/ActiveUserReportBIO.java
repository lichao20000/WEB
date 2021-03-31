
package com.linkage.module.itms.report.bio;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.report.dao.ActiveUserReportDAO;

/**
 * @author zszhao6 (Ailk No.78987)
 * @version 1.0
 * @since 2018-8-13
 * @category com.linkage.module.itms.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ActiveUserReportBIO
{

	private ActiveUserReportDAO dao = null;
	private static Logger logger = LoggerFactory.getLogger(ActiveUserReportBIO.class);

	public List<Map> getActiveUserInfo(int curPage_splitPage, int num_splitPage, String startOpenDate1,
			String endOpenDate1, String gwShare_cityId, String gwShare_vendorId, String gwShare_deviceModelId,
			String gwShare_devicetypeId) {
		return dao.getActiveUserInfo(curPage_splitPage, num_splitPage,startOpenDate1,
				endOpenDate1,gwShare_cityId,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId);
	}
	public int getActiveUserCount(int num_splitPage, String startOpenDate1, String endOpenDate1, String gwShare_cityId,
			String gwShare_vendorId, String gwShare_deviceModelId, String gwShare_devicetypeId) {
		return dao.getActiveUserCount(num_splitPage,startOpenDate1,
				endOpenDate1,gwShare_cityId,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId);
	}

	public List<Map> getActiveUserInfo4Excel(String startOpenDate1, String endOpenDate1, String gwShare_cityId,
			String gwShare_vendorId, String gwShare_deviceModelId, String gwShare_devicetypeId) {
		return dao.getAllActiveUserInfo(startOpenDate1,
				endOpenDate1,gwShare_cityId,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId);
	}

	public Map<String,Long> getQueryTime()
	{
		Map<String,Long> timeMap = new HashMap<String,Long>();
		// 当前时间Date
		Date endDate = new Date(); // 结束时间
		Date startDate = null; // 开始时间
		Calendar calc = Calendar.getInstance();
		calc.setTime(endDate);
		// 当前时间减30天
		calc.add(calc.DATE, -30);
		startDate = calc.getTime();
		// 获取的为13位时间戳，需要转换为10位
		Long startime = Long
				.parseLong(String.format("%010d", startDate.getTime() / 1000));
		Long endtime = Long.parseLong(String.format("%010d", endDate.getTime() / 1000));
		timeMap.put("st", startime);
		timeMap.put("et", endtime);
		return timeMap;
	}
	/**
	 * @return the dao
	 */
	public ActiveUserReportDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(ActiveUserReportDAO dao)
	{
		this.dao = dao;
	}

}
