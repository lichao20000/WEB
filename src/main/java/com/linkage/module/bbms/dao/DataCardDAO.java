
package com.linkage.module.bbms.dao;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class DataCardDAO extends SuperDAO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(DataCardDAO.class);

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
		logger.debug("getDataCardInfoMap({})", deviceId);
		Map rmap = null;
		if (false == StringUtil.IsEmpty(deviceId))
		{
			String sql = "select data_card_id, data_card_esn, device_id, data_card_desc, bind_stat, complete_time, " +
					"bind_time, update_time, remark, work_mode, vendor_id, model_id, hard_id, firm_id, plug_stat " +
					"from data_card where device_id =?";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.setString(1, deviceId);
			rmap = queryForMap(psql.getSQL());
		}
		return rmap;
	}

	/**
	 * 通过网关ID（deviceId）获得session中的数据卡工作状态和工作模式
	 *
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-12
	 * @return Map 失败返回null
	 */
	public Map getEvdoDataCardBySession(String deviceId)
	{
		logger.debug("getEvdoDataCardBySession({})", deviceId);
		Map rmap = null;
		if (false == StringUtil.IsEmpty(deviceId))
		{
			logger.debug("获取卡表状态");
			String sql = "select a.work_mode, a.conn_status "
					+ "from gw_wan_conn_session a, gw_wan b "
					+ "where a.wan_id=b.wan_id and a.device_id=b.device_id and a.device_id=? and b.access_type='Cellular'";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.setString(1, deviceId);
			rmap = queryForMap(psql.getSQL());
			if (null != rmap)
			{
				String connStatus = StringUtil.getStringValue(rmap.get("conn_status"));
				String workMode = StringUtil.getStringValue(rmap.get("work_mode"));
				int useStat;
				rmap = new HashMap();
				if ("Connected".equals(connStatus))
				{
					rmap.put("useStat", "卡正在使用");
					useStat = 1;
					rmap.put("workMode", workMode);
				}
				else
				{
					rmap.put("useStat", "卡未使用");
					useStat = 0;
					rmap.put("workMode", workMode);
				}
				logger.debug("修改data_card表");
				String usql = "update data_card set work_mode=?, use_stat=? where device_id=?";
				PrepareSQL pusql = new PrepareSQL(usql);
				pusql.setString(1, workMode);
				pusql.setInt(2, useStat);
				pusql.setString(3, deviceId);
				jt.update(pusql.getSQL());
			}
			else
			{
				rmap = new HashMap();
				rmap.put("useStat", "卡未使用");
				rmap.put("workMode", "未知");
			}
		}
		return rmap;
	}
}
