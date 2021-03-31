/**
 * 
 */
package com.linkage.module.gwms.report.bio;

import java.util.List;

import com.linkage.module.gwms.report.bio.interf.I_OnlineDevStatConfigBIO;
import com.linkage.module.gwms.report.dao.interf.I_OnlineDevStatConfigDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-26
 * @category com.linkage.module.gwms.report.bio
 * 
 */
public class OnlineDevStatConfigBIO implements I_OnlineDevStatConfigBIO{

	/**
	 * 注入
	 */
	private I_OnlineDevStatConfigDAO onlineDevStatConfigDao;

	/**
	 * 配置入库
	 * 
	 * @param time_point
	 * @param city_id
	 * @return
	 */
	public int batchConfig(String[] time_point, String[] city_id) {
		
		return onlineDevStatConfigDao.batchConfig(time_point, city_id);
	}

	/**
	 * 取属地
	 * 
	 * @return
	 */
	public List getCity() {
		
		return onlineDevStatConfigDao.getCity();
	}

	/**
	 * @category 取时间
	 * 
	 * @return
	 */
	public List getTimePoint() {
		
		return onlineDevStatConfigDao.getTimePoint();
	}

	/**
	 * @return the onlineDevStatConfigDao
	 */
	public I_OnlineDevStatConfigDAO getOnlineDevStatConfigDao() {
		return onlineDevStatConfigDao;
	}

	/**
	 * @param onlineDevStatConfigDao the onlineDevStatConfigDao to set
	 */
	public void setOnlineDevStatConfigDao(
			I_OnlineDevStatConfigDAO onlineDevStatConfigDao) {
		this.onlineDevStatConfigDao = onlineDevStatConfigDao;
	}

}
