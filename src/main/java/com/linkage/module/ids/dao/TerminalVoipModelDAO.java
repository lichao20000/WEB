package com.linkage.module.ids.dao;

import java.util.Map;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-7-30
 * @category com.linkage.module.ids.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class TerminalVoipModelDAO extends SuperDAO 
{
	/**
	 * 根据设备device_id检索设备信息
	 * @param device_id
	 * @return
	 */
	public Map<String, String> queryDevInfoByDeviceId(String device_id)
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.append(" select device_id, oui, device_serialnumber, city_id, loopback_ip, device_status, devicetype_id, cpe_mac, device_model_id, vendor_id, dev_sub_sn ");
		pSql.append(" from tab_gw_device ");
		pSql.append(" where 1=1 and device_id = '" + device_id + "' ");
		return DBOperation.getRecord(pSql.getSQL());
	}
}
