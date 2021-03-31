/**
 * 
 */
package com.linkage.module.gwms.report.dao.interf;

import java.util.List;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-26
 * @category com.linkage.module.gwms.report.dao.interf
 * 
 */
public interface I_OnlineDevStatDAO {

	/**
	 * @category
	 * 
	 * @param city_id
	 * @param startTime
	 * @param startEnd
	 * @param chartType
	 * @return
	 */
	public List getDataOnLineDev(String city_id, long startTime,
			long startEnd, String chartType);
	
	/**
	 * @category 查询概属地下所有的数据
	 * 
	 * @param city_id
	 * @param startTime
	 * @param startEnd
	 * @param r_timepoint
	 * @return
	 */
	public List getMonthOnLineDevAll(String city_id, long startTime,
			long startEnd, String r_timepoint, String chartType) ;

	/**
	 * 
	 * @param city_id
	 * @return
	 */
	public List getChildCity(String city_id);
	
	/**
	 * 
	 * @param city_id
	 * @return
	 */
	public List getCityBySelf(String city_id) ;
}
