/**
 * 
 */
package com.linkage.module.gwms.report.bio;

import java.util.List;

import com.linkage.module.gwms.report.bio.interf.I_DeviceExceptionReportBIO;
import com.linkage.module.gwms.report.dao.interf.I_DeviceExceptionReportDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-12
 * @category com.linkage.module.gwms.report.bio
 * 
 */
public class DeviceExceptionReportBIO implements I_DeviceExceptionReportBIO{
	
	I_DeviceExceptionReportDAO deviceExceptionReportDAO;
	
	/**
	 * @category 查询所有的属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity(String cityId) {
		return deviceExceptionReportDAO.getAllCity(cityId);
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
		return deviceExceptionReportDAO.getReportData(curPage_splitPage,num_splitPage,cityId,startDate,endDate);
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
		return deviceExceptionReportDAO.getReportData(cityId,startDate,endDate);
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
		return deviceExceptionReportDAO.getServStrategyCount(curPage_splitPage,num_splitPage,cityId,startDate,endDate);
	}

	/**
	 * @return the deviceExceptionReportDAO
	 */
	public I_DeviceExceptionReportDAO getDeviceExceptionReportDAO() {
		return deviceExceptionReportDAO;
	}

	/**
	 * @param deviceExceptionReportDAO the deviceExceptionReportDAO to set
	 */
	public void setDeviceExceptionReportDAO(
			I_DeviceExceptionReportDAO deviceExceptionReportDAO) {
		this.deviceExceptionReportDAO = deviceExceptionReportDAO;
	}

}
