package com.linkage.module.bbms.dao;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;


public class UimCardDAO extends SuperDAO
{
	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(UimCardDAO.class);

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
		logger.debug("getOfficeIdNameMap({})",deviceId);
		Map rmap = null;
		if (null != deviceId || !"".equals(deviceId) || !deviceId.isEmpty())
		{
			String sql = "select uim_card_id, uim_card_imsi, device_id, uim_card_desc, bind_stat, complete_time, " +
					"bind_time, update_time, voltage, remark from uim_card where device_id = ?";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.setString(1, deviceId);
			rmap = queryForMap(psql.getSQL());
		}
		return rmap;
	}

}
