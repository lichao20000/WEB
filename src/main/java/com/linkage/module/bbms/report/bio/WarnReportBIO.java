/**
 * 
 */
package com.linkage.module.bbms.report.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.bbms.report.dao.WarnReportDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;

/**
 * 需求挖掘和预警BIO
 * @author chenjie
 * @date 2012-12-04
 */
public class WarnReportBIO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(WarnReportBIO.class);
	
	// dao 
	private WarnReportDAO warnReportDao;
	
	private int maxPage_splitPage;
	
	public int getMaxPage_splitPage() {
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage) {
		this.maxPage_splitPage = maxPage_splitPage;
	}
	
	/**
	 * 获取厂商信息
	 * @return
	 */
	public List<Map<String, String>> getVendorList()
	{
		logger.debug("getVendorMap()");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String, String>	vendorMap = VendorModelVersionDAO.getVendorMap();
		Set<String> set = vendorMap.keySet();
		Iterator<String> it = set.iterator();
		while(it.hasNext())
		{
			String city_id = it.next(); 
			Map<String,String> temp = new HashMap<String,String>();
			temp.put("vendor_id", city_id);
			temp.put("vendor_name", vendorMap.get(city_id));
			list.add(temp);
		}
		return list;
	}
	
	/**
	 * 查询需求挖掘
	 * @param cityId
	 * @param industry
	 * @param servTypeId
	 * @param deviceType
	 * @param adsl_hl
	 * @param flowMin
	 * @param flowMax
	 * @param startOpenDate
	 * @param endOpenDate
	 * @param onlinedateMin
	 * @param onlinedateMax
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryWarnRequireReport(String cityId, String industry, String servTypeId, String deviceType, String adsl_hl,
			String flowMin, String flowMax, String startOpenDate, String endOpenDate, String onlinedateMin, String onlinedateMax, 
			int curPage_splitPage, int num_splitPage) 
	{
		logger.debug("queryWarnRequireReport({})", new Object[]{cityId, industry, servTypeId, deviceType, adsl_hl, flowMin, flowMax, startOpenDate, endOpenDate, onlinedateMin, onlinedateMax, curPage_splitPage, num_splitPage});
		
		List<Map> result = warnReportDao.queryWarnRequireReport(cityId, industry, servTypeId, deviceType, adsl_hl, flowMin, flowMax, startOpenDate, endOpenDate, onlinedateMin, onlinedateMax, curPage_splitPage, num_splitPage);
		maxPage_splitPage = warnReportDao.countWarnRequireReport(cityId, industry, servTypeId, deviceType, adsl_hl, flowMin, flowMax, startOpenDate, endOpenDate, onlinedateMin, onlinedateMax, curPage_splitPage, num_splitPage);
		return  result;
	}
	
	/**
	 * @return the warnReportDao
	 */
	public WarnReportDAO getWarnReportDao() {
		return warnReportDao;
	}

	/**
	 * @param warnReportDao the warnReportDao to set
	 */
	public void setWarnReportDao(WarnReportDAO warnReportDao) {
		this.warnReportDao = warnReportDao;
	}

	/**
	 * 查询维挽预警
	 * @param cityId
	 * @param linkphone
	 * @param customerAddress
	 * @param productType
	 * @param vendorId
	 * @param adsl_hl
	 * @param industry
	 * @param deviceType
	 * @param warningReason
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param startWarningDate1
	 * @param endWarningDate1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryWarnReport(String cityId, String linkphone, String customerAddress,
			String productType, String vendorId, String adsl_hl, String industry, String deviceType, String warningReason,
			String startOpenDate1, String endOpenDate1, String startWarningDate1, String endWarningDate1, int curPage_splitPage, int num_splitPage) {
		logger.debug("queryWarnReport({})", new Object[]{cityId, linkphone, customerAddress, productType, vendorId, adsl_hl, industry, deviceType, warningReason, startOpenDate1, endOpenDate1, startWarningDate1, endWarningDate1, curPage_splitPage, num_splitPage});
		
		List<Map> result = warnReportDao.queryWarnReport(cityId, linkphone, customerAddress, productType, vendorId, adsl_hl, industry, deviceType, warningReason, startOpenDate1, endOpenDate1, startWarningDate1, endWarningDate1, curPage_splitPage, num_splitPage);
		maxPage_splitPage = warnReportDao.countWarnReport(cityId, linkphone, customerAddress, productType, vendorId, adsl_hl, industry, deviceType, warningReason, startOpenDate1, endOpenDate1, startWarningDate1, endWarningDate1, curPage_splitPage, num_splitPage);
		
		return result;
	}
}
