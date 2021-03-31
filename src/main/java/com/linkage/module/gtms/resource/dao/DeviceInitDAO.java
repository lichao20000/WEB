package com.linkage.module.gtms.resource.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 设备数据是否录入初始表tab_gw_device_init功能查询
 * @author wanghong
 *
 */
public class DeviceInitDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(DeviceInitDAO.class);
	
	/**
	 * 获取设备序列号
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> query(String sn)
	{
		logger.debug("DeviceInitDAO.query({})",sn);
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_serialnumber from tab_gw_device_init ");
		psql.append("where device_serialnumber like '%"+sn+"' ");
		psql.append("order by device_id");
		
		return jt.queryForList(psql.getSQL());
	}
}
