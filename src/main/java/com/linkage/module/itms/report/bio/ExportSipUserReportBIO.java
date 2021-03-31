package com.linkage.module.itms.report.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.report.dao.ExportSipUserReportDAO;

/**
 * 
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2014-6-30
 * @category com.linkage.module.itms.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
@SuppressWarnings("rawtypes")
public class ExportSipUserReportBIO
{

	// 日志记录
		private static Logger logger = LoggerFactory.getLogger(ExportSipUserReportBIO.class);
		private ExportSipUserReportDAO reportDao;


	public List<Map> getHgwList(String starttime1, String endtime1, String cityId,
			 int curPage_splitPage, int num_splitPage, String userType)
	{
		logger.debug("ExportSipUserReportBIO.getHgwList()");
		return reportDao.getHgwList(starttime1, endtime1, cityId, curPage_splitPage, num_splitPage, userType);
	}

	public int getHgwCount(String starttime1, String endtime1, String cityId,
			 int curPage_splitPage, int num_splitPage, String userType)
	{
		logger.debug("ExportSipUserReportBIO.getHgwCount()");
		return reportDao.getHgwCount(starttime1, endtime1, cityId, curPage_splitPage, num_splitPage, userType);
	}

	public List<Map> getHgwExcel(String starttime1, String endtime1, String cityId,
			String usertype)
	{
		logger.debug("ExportSipUserReportBIO.getHgwExcel()");
		return reportDao.getHgwExcel(starttime1, endtime1, cityId, usertype);
	}

	
	public ExportSipUserReportDAO getReportDao()
	{
		return reportDao;
	}

	
	public void setReportDao(ExportSipUserReportDAO reportDao)
	{
		this.reportDao = reportDao;
	}
	
}
