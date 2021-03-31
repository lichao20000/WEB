
package com.linkage.module.bbms.report.bio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.bbms.report.dao.BindwidthtopNDAO;

@SuppressWarnings("unchecked")
public class BandwidthTopNBIO
{
	private BindwidthtopNDAO bindwidthtopNDao;
	private String message;

	/**
	 * 统计topN报表
	 * 
	 * @author wangsenbo
	 * @date Mar 18, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getTopNReport(String reportType, String cityId, String stat_time,
			String countDesc)
	{
		String tableName = "";
		String starttime = "";
		String endtime = "";
		DateTimeUtil dt = new DateTimeUtil(stat_time);
		if ("day".equals(reportType))
		{
			tableName = "flux_day_stat_" + dt.getYear() + "_" + dt.getMonth();
			starttime = StringUtil.getStringValue(dt.getLongTime());
			endtime = StringUtil.getStringValue(dt.getLongTime() + (24 * 60 * 60));
		}
		if ("week".equals(reportType))
		{
			tableName = "flux_week_stat_" + dt.getYear();
			String starttime1 = dt.getFirstDayOfWeek("CN");
			dt = new DateTimeUtil(starttime1);
			starttime = StringUtil.getStringValue(dt.getLongTime());
			String endtime1 = dt.getLastDayOfWeek("CN");
			dt = new DateTimeUtil(endtime1);
			endtime = StringUtil.getStringValue(dt.getLongTime() + (24 * 60 * 60));
		}
		if ("month".equals(reportType))
		{
			tableName = "flux_month_stat_" + dt.getYear();
			String starttime1 = dt.getFirtDayOfMonth();
			dt = new DateTimeUtil(starttime1);
			starttime = StringUtil.getStringValue(dt.getLongTime());
			String endtime1 = dt.getLastDayOfMonth();
			dt = new DateTimeUtil(endtime1);
			endtime = StringUtil.getStringValue(dt.getLongTime() + (24 * 60 * 60));
		}
		int ret = bindwidthtopNDao.isHaveTable(tableName);
		if (ret == 0)
		{
			message = "没有相关报表信息";
			List list = new ArrayList();
			return list;
		}
		return bindwidthtopNDao.getTopNReport(tableName, cityId, countDesc, starttime,
				endtime);
	}

	/**
	 * @return the bindwidthtopNDao
	 */
	public BindwidthtopNDAO getBindwidthtopNDao()
	{
		return bindwidthtopNDao;
	}

	/**
	 * @param bindwidthtopNDao
	 *            the bindwidthtopNDao to set
	 */
	public void setBindwidthtopNDao(BindwidthtopNDAO bindwidthtopNDao)
	{
		this.bindwidthtopNDao = bindwidthtopNDao;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
}
