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
public interface I_OnlineDevStatConfigDAO {

	/**
	 * 取属地
	 * 
	 * @return
	 */
	public List getCity();

	/**
	 * @category 取时间
	 * 
	 * @return
	 */
	public List getTimePoint();

	/**
	 * 配置入库
	 * 
	 * @param time_point
	 * @param city_id
	 * @return
	 */
	public int batchConfig(String time_point[],String city_id[]);
}
