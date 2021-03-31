package com.linkage.module.gwms.dao.tabquery;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;


public class SgwSecurityDAO extends SuperDAO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(SgwSecurityDAO.class);

	/**
	 * 获得SNMP信息的map
	 * 
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-23
	 * @return Map 没有数据返回null
	 */
	public Map getSgwSecurity(String deviceId)
	{
		logger.debug("getSgwSecurity({})",deviceId);
		Map rmap = null;
		if (false == StringUtil.IsEmpty(deviceId))
		{
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psql.append("select is_enable,snmp_version,");
				psql.append("security_username,security_model,engine_id,");
				psql.append("context_name,security_level,auth_protocol,auth_passwd,");
				psql.append("privacy_protocol,privacy_passwd,snmp_r_passwd,snmp_w_passwd ");
			}else{
				psql.append("select * ");
			}
			psql.append("from sgw_security where device_id=? ");
			psql.setString(1, deviceId);
			rmap = queryForMap(psql.getSQL());
		}
		return rmap;
	}

	
}
