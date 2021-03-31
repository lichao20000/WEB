/**
 * 
 */
package com.linkage.module.gwms.report.dao.interf;

import java.util.List;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-6
 * @category com.linkage.module.gwms.report.dao.interf
 * 
 */
public interface I_ZeroConfigStatisticalDAO {

	/**
	 * 查询零配置数据
	 * 
	 * @param cityList
	 * @param startTime
	 * @param endTime
	 * @param bindFlag
	 * @return
	 */
	public int getData(List cityList,String startTime,String endTime,String bindFlag);
	
	/**
	 * 查询成功数据
	 * 
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List getBindData(List cityList,String startTime,String endTime);
	
	/**
	 * 查询失败数据
	 * 
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List getNoBindData(List cityList,String startTime,String endTime);
}
