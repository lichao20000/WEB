/**
 * 
 */
package com.linkage.module.gwms.report.bio.interf;

import java.util.List;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-6
 * @category com.linkage.module.gwms.report.bio.interf
 * 
 */
public interface I_ZeroConfigStatisticalBIO {

	/**
	 * 查询所有子属地
	 * 
	 * @param city_id
	 * @return
	 */
	public List getChildCityList(String city_id);
	
	/**
	 * 查询数据
	 * 
	 * @param cityList
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String[][] getData(List cityList,String startTime,String endTime);
	
	/**
	 * 查询成功数据
	 * 
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List getBindData(String cityId,String startTime,String endTime);
	
	/**
	 * 查询失败数据
	 * 
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List getNoBindData(String cityId,String startTime,String endTime);
}
