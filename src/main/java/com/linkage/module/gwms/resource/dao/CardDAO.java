
package com.linkage.module.gwms.resource.dao;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

//TODO wait
//无引用的类
public class CardDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(CardDAO.class);

	/**
	 * 通过网关ID（deviceId）获得uim卡详细信息的map
	 * 
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-12
	 * @return Map 没有数据返回null
	 */
	public Map getUimCardInfoMap(String deviceId)
	{
		logger.debug("getOfficeIdNameMap()");
		Map rmap = null;
		if (null == deviceId || "".equals(deviceId) || deviceId.isEmpty())
		{
			String sql = "select * from uim_card where device_id = ?";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.setString(1, deviceId);
			rmap = queryForMap(sql);
		}
		return rmap;
	}

	/**
	 * 通过网关ID（deviceId）获得数据卡详细信息的map
	 * 
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-12
	 * @return Map 没有数据返回null
	 */
	public Map getDataCardInfoMap(String deviceId)
	{
		logger.debug("getDataCardInfoMap()");
		Map rmap = null;
		if (null == deviceId || "".equals(deviceId) || deviceId.isEmpty())
		{
			String sql = "select * from data_card where device_id = ?";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.setString(1, deviceId);
			rmap = queryForMap(sql);
		}
		return rmap;
	}
}
