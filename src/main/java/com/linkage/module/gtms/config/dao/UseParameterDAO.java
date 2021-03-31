package com.linkage.module.gtms.config.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class UseParameterDAO  extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(UseParameterDAO.class);

	/**
	 * 获取账号信息
	 */
	public ArrayList<HashMap<String, String>> queryDeviceByUsername(String userName)
	{
		logger.debug("UseParameterDAO queryDeviceByUsername({})",userName);
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.append("select a.username loid,a.user_id,a.city_id,b.device_id,b.device_serialnumber,b.oui,d.online_status ");
		pSQL.append("from hgwcust_serv_info c left join tab_hgwcustomer a ");
		pSQL.append("on (a.user_id=c.user_id and c.serv_type_id=10 and c.serv_status=1) ");
		pSQL.append("left join tab_gw_device b ");
		pSQL.append("on (a.device_id=b.device_id and a.user_state in ('1','2')) ");
		pSQL.append("left join gw_devicestatus d on b.device_id=d.device_id ");
		pSQL.append("where c.username=? ");
		pSQL.setString(1, userName);

		return DBOperation.getRecords(pSQL.getSQL());
	}

	/**
	 * 修改上网方式
	 */
	public int doConfig (long userId,String netUsername,String pwd)
	{
		ArrayList<String> sqlList = new ArrayList<String>();

		PrepareSQL psql = new PrepareSQL();
		psql.append("update tab_net_serv_param set ip_type=3,untreated_ip_type=1 ");
		psql.append("where user_id=? and serv_type_id=10 and username=? ");
		psql.setLong(1,StringUtil.getLongValue(userId));
		psql.setString(2,netUsername);
		sqlList.add(psql.getSQL());

		psql = new PrepareSQL();
		psql.append("update hgwcust_serv_info set ip_type=3,passwd=?,wan_type=2,open_status=0,updatetime=? ");
		psql.append("where user_id=? and serv_type_id=10 and serv_status=1 and username=? ");
		psql.setString(1,pwd);
		psql.setLong(2,System.currentTimeMillis()/1000L);
		psql.setLong(3,StringUtil.getLongValue(userId));
		psql.setString(4,netUsername);
		sqlList.add(psql.getSQL());

		return DBOperation.executeUpdate(sqlList);
	}
}
