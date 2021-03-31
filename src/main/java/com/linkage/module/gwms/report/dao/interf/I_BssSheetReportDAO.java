/**
 * 
 */
package com.linkage.module.gwms.report.dao.interf;

import java.util.List;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-11
 * @category dao.report
 * 
 */
@SuppressWarnings("unchecked")
public interface I_BssSheetReportDAO {

	/**
	 * @category 查询所有的子属地，返回
	 * 
	 * @param cityId
	 * @return
	 */
	public List getChildCity(String cityId);
	
	/**
	 * @category 查询所有的属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity();
	
	/**
	 * @category 查询所有的操作类型
	 * 
	 * @return
	 */
	public List getGwOperType();
	
	/**
	 * @category 根据属地，时间查询北向发送过来的工单
	 * 
	 * @param cityList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getBssSheet(List cityList,long startDate,long endDate);
	
}
