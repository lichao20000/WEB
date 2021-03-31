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
public interface I_SheetFailureReasonDAO {

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
	 * 查询所有的错误码
	 * 
	 * @return
	 */
	public List getFaultCode();
	
	/**
	 * @category 根据属地，时间查询套餐对应的工单
	 * 
	 * @param cityList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getDevNum(List cityList,long startDate,long endDate);
	
}
