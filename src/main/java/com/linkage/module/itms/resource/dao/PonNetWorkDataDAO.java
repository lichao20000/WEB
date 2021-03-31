package com.linkage.module.itms.resource.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-8-22
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class PonNetWorkDataDAO extends SuperDAO
{
	
	public List<Map> ponInit(String start_time, String end_time)
	{
		String sql = "select AREA_NAME,SUBAREA_NAME,SITE_NAME,LOID,TX_POWER,RX_POWER,OLT_NAME,OLT_IP,PON_ID,ONT_ID,ACCESS_WAY,to_char(TIME,'yyyy-mm-dd') as time "
				+ " from NMS_ITMS.ODN_ALARMS_ONU_VIEW where TIME>=to_date(?,'yyyy-mm-dd') and TIME<=to_date(?,'yyyy-mm-dd')";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select AREA_NAME,SUBAREA_NAME,SITE_NAME,LOID,TX_POWER,RX_POWER,OLT_NAME,OLT_IP,PON_ID,ONT_ID,ACCESS_WAY,date_format(TIME,'%Y-%m-%d') as time "
					+ " from NMS_ITMS.ODN_ALARMS_ONU_VIEW where TIME>=str_to_date(?,'%Y-%m-%d') and TIME<=str_to_date(?,'%Y-%m-%d')";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, start_time);
		psql.setString(2, end_time);
		List<Map> list = jt.queryForList(psql.getSQL());
		return list;
	}

}
