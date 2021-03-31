/**
 * 
 */
package com.linkage.module.gwms.report.bio.interf;

import java.util.List;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-12
 * @category com.linkage.module.gwms.report.bio
 * 
 */
public interface I_DeviceExceptionReportBIO {

	/**
	 * @category 查询所有的属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity(String cityId);
	
	/**
	 * @查询报表数据
	 * 
	 * @param city_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getReportData(int curPage_splitPage, int num_splitPage,String cityId, long startDate, long endDate) ;
	
	/**
	 * @查询报表数据
	 * 
	 * @param city_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getReportData(String cityId, long startDate, long endDate) ;
	
	/**
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getServStrategyCount(int curPage_splitPage, int num_splitPage,String cityId, long startDate, long endDate);
}
