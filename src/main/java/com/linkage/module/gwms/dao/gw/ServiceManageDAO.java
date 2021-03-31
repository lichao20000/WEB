package com.linkage.module.gwms.dao.gw;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;


public class ServiceManageDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(ServiceManageDAO.class);

	public Map getFTP(String deviceId)
	{
		logger.debug("getFTP({})", deviceId);
		String sql = "select ftp_enable,ftp_username,ftp_passwd,ftp_port from gw_service_manage where device_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, deviceId);
		return queryForMap(psql.getSQL());
	}
	
	
}
