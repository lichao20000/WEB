package com.linkage.module.itms.resource.bio;

import java.util.HashMap;
import java.util.List;

import com.linkage.module.itms.resource.dao.QueryCurrentAcsDAO;

public class QueryCurrentAcsBIO
{
	private QueryCurrentAcsDAO dao;
	
	/**
	 * 根据devSn获取deviceId
	 * @author wangyan10
	 * @param devSn
	 * @return
	 * @since 2017-1-6 下午3:07:12
	 */
	public String getDeviceId(String devSn) {
		return dao.getDeviceId(devSn);
	}
	
	/**
	 * 校验deviceId
	 * @author wangyan10
	 * @param deviceId
	 * @return
	 * @since 2017-1-6 下午3:07:29
	 */
	public List<HashMap<String, String>> checkDeviceId(String deviceId) {
		return dao.checkDeviceId(deviceId);
	}
	
	public QueryCurrentAcsDAO getDao() {
		return dao;
	}
	public void setDao(QueryCurrentAcsDAO dao) {
		this.dao = dao;
	}
	
}
