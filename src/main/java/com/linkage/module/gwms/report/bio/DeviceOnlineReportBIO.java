/**
 * 
 */
package com.linkage.module.gwms.report.bio;

import java.util.List;

import com.linkage.module.gwms.report.bio.interf.I_DeviceOnlineReportBIO;
import com.linkage.module.gwms.report.dao.interf.I_DeviceOnlineReportDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-12
 * @category com.linkage.module.gwms.report.bio
 * 
 */
public class DeviceOnlineReportBIO implements I_DeviceOnlineReportBIO{
	
	I_DeviceOnlineReportDAO deviceOnlineReportDAO;
	
	/**
	 * @category 查询所有的属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity(String cityId) {
		return deviceOnlineReportDAO.getAllCity(cityId);
	}
	
	/**
	 * @查询报表数据
	 * 
	 * @param city_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getReportData(int curPage_splitPage, int num_splitPage,String cityId, long startDate, long endDate) {
		return deviceOnlineReportDAO.getReportData(curPage_splitPage,num_splitPage,cityId,startDate,endDate);
	}
	
	/**
	 * @查询报表数据
	 * 
	 * @param city_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getReportData(String cityId, long startDate, long endDate) {
		return deviceOnlineReportDAO.getReportData(cityId,startDate,endDate);
	}
	
	/**
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getServStrategyCount(int curPage_splitPage, int num_splitPage,String cityId, long startDate, long endDate) {
		return deviceOnlineReportDAO.getServStrategyCount(curPage_splitPage,num_splitPage,cityId,startDate,endDate);
	}

	/**
	 * @return the deviceOnlineReportDAO
	 */
	public I_DeviceOnlineReportDAO getDeviceOnlineReportDAO() {
		return deviceOnlineReportDAO;
	}

	/**
	 * @param deviceOnlineReportDAO the deviceOnlineReportDAO to set
	 */
	public void setDeviceOnlineReportDAO(
			I_DeviceOnlineReportDAO deviceOnlineReportDAO) {
		this.deviceOnlineReportDAO = deviceOnlineReportDAO;
	}

}
