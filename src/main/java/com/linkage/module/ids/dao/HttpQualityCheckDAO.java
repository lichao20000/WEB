
package com.linkage.module.ids.dao;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author yinlei3 (Ailk No.73167)
 * @version 1.0
 * @since 2016年4月27日
 * @category com.linkage.module.ids.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class HttpQualityCheckDAO extends SuperDAO
{

	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory
			.getLogger(HttpQualityCheckDAO.class);

	/**
	 * 根据设备序列号，厂商OUI检索设备信息
	 * 
	 * @param DevSn
	 *            设备序列号
	 * @param oui
	 *            设备oui
	 * @return device_id
	 */
	public Map<String, String> queryDevInfo(String DevSn, String oui)
	{
		logger.debug("DeviceInfoDAO==>queryDevInfo({},{},{})",
				new Object[] { DevSn, oui });
		PrepareSQL pSql = new PrepareSQL();
		// 如果设备序列号不为空
		pSql.append("select device_id ");
		pSql.append("  from tab_gw_device ");
		pSql.append(" where device_serialnumber = '" + DevSn + "' ");
		pSql.append("   and oui = '" + oui + "' ");
		return DBOperation.getRecord(pSql.getSQL());
	}
}
