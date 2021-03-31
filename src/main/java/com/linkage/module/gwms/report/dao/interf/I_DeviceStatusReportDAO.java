/**
 * 
 */
package com.linkage.module.gwms.report.dao.interf;

import java.util.List;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-4
 * @category com.linkage.module.gwms.report.dao.interf
 * 
 */
public interface I_DeviceStatusReportDAO {
	
	/**
	 * @category 查询所有的子属地，返回
	 * 
	 * @param cityId
	 * @return
	 */
	public List getChildCity(String cityId);
	
	/**
	 * @category 查询所有属地
	 * 
	 * @return
	 */
	public List getAllCity();
	
	/**
	 * @category 根据属地，时间查询套餐对应的工单
	 * 
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getDevNum(List cityList,long startDate,long endDate,int cpe_allocatedstatus);
}
