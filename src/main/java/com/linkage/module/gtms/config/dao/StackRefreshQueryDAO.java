package com.linkage.module.gtms.config.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.system.utils.database.Cursor;
import com.linkage.system.utils.database.DataSetBean;


public class StackRefreshQueryDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(StackRefreshQueryDAO.class);

	public List getWanConnIds(String device_id)
	{
		StringBuffer sql = new StringBuffer();
		List<Map> list = new ArrayList<Map>();

		sql.append("select b.sess_type,b.conn_type,b.serv_list, a.vlan_id, a.vpi_id, a.vci_id, b.wan_conn_id,b.wan_conn_sess_id " +
				"from gw_wan_conn a,gw_wan_conn_session b where a.device_id=b.device_id and a.wan_conn_id=b.wan_conn_id  and a.device_id='");
		sql.append(device_id).append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql.toString());
		for (int i = 0; i < cursor.getRecordSize(); i++)
		{
			Map map = cursor.getRecord(i);
			String vpi_id = StringUtil.getStringValue(map, "vpi_id");
			String vci_id = StringUtil.getStringValue(map, "vci_id");
			map.put("pvc", vpi_id + "/" + vci_id);
			list.add(map);
		}
		return list;
	}
}
