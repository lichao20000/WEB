/**
 * 
 */
package com.linkage.module.gwms.report.bio.interf;

import java.util.List;
import java.util.Map;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-4
 * @category com.linkage.module.gwms.report.bio.interf
 * 
 */
public interface I_DeviceStatusReportBIO {
	/**
	 * @category 查询所有的属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity(String cityId) ;

	/**
	 * @查询报表数据
	 * 
	 * @param city_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String[][] getReportData(String cityId, long startDate, long endDate) ;

	/**
	 * 获取属地父子关系
	 * 
	 * @return
	 */
	public Map<String, String> getCityPC() ;

	/**
	 * @category 查询子节点，不包含自己
	 * 
	 * @param cityMap
	 * @param cityId
	 * @return
	 */
	public List<String> getAllChild(Map<String, String> cityMap, String cityId) ;
}
