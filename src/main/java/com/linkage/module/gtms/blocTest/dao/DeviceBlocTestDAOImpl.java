
package com.linkage.module.gtms.blocTest.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since May 11, 2012
 * @category com.linkage.module.gtms.blocTest.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DeviceBlocTestDAOImpl extends SuperDAO implements DeviceBlocTestDAO
{

	private static Logger logger = LoggerFactory.getLogger(DeviceBlocTestDAOImpl.class);

	/**
	 * 根据设备ID查询设备信息
	 * @param deviceId
	 * @return
	 */
	@Override
	public List<Map> queryDeviceInfoById(String deviceId)
	{
		logger.debug("DeviceBlocTestDAOImpl==>queryDeviceInfoById({})", deviceId);
		PrepareSQL psql = new PrepareSQL();
		psql
				.append("Select device_id,gather_time,list_i,is_online,attached_port,friendly_name,device_type,provisioning_code,software_version,software_desc,up_time,hardware_version,oui,product_class,serial_number ");
		psql.append(" from gw_ap_device_info ");
		psql.append(" where 1=1 ");
		if (null != deviceId && deviceId.length() > 0)
		{
			psql.append(" and device_id = '" + deviceId+"'");
		}
		List<Map> list = jt.queryForList(psql.getSQL());
		
		return list;
	}
}
