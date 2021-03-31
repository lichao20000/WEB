package com.linkage.module.gtms.config.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class WifiPwdManageDAO  extends SuperDAO{

	public List isExists(String username) {
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select a.user_id,b.device_id,b.username,c.device_id ,c.device_serialnumber from hgwcust_serv_info a , tab_hgwcustomer  b ,tab_gw_device c " );
		sql.append("  where a.user_id = b.user_id  and a.username=? and a.serv_type_id=10 and b.device_id = c.device_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1,username);
		return  jt.queryForList(psql.getSQL());
	}

	public String getDeviceId(String device_serialnumber) {
		StringBuffer sql = new StringBuffer();
		sql.append("select device_id from tab_gw_device where device_serialnumber = ?" );
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1,device_serialnumber);
		Map queryForMap = jt.queryForMap(psql.getSQL());
		if(null != queryForMap && !queryForMap.isEmpty()){
			return StringUtil.getStringValue(queryForMap,"device_id");
		}
		return  null;
	}
}
