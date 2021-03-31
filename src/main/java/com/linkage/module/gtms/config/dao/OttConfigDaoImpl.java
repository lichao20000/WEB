package com.linkage.module.gtms.config.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.litms.common.database.DataSetBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;


/**
 *
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2014-4-1
 * @category com.linkage.module.gtms.config.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class OttConfigDaoImpl extends SuperDAO implements OttConfigDAO
{

	private static Logger logger = LoggerFactory.getLogger(OttConfigDaoImpl.class);

	/**
	 * 1：根据loid查
	 * 2：根据宽带账号查
	 * 3：根据电话号码查
	 * 4：根据认证账号查
	 */
	@Override
	public List<HashMap<String,String>> isExists(String searchType, String username, String gwType)
	{
		logger.debug("OttConfigDaoImpl.isExists()");
		int type = StringUtil.getIntegerValue(searchType, 1);
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.user_id,a.username,b.serv_type_id,a.spec_id ");

		switch (type) {
			case 1:
				psql.append("	from tab_hgwcustomer a, hgwcust_serv_info b");
				psql.append("	where a.user_id=b.user_id ");
				psql.append("	and a.username='" + username + "'");
				break;
			case 2:
				psql.append("	from tab_hgwcustomer a, hgwcust_serv_info b");
				psql.append("	where a.user_id=b.user_id ");
				psql.append("	and a.user_id in(select user_id from hgwcust_serv_info b where b.username='" + username + "')");
				break;
			case 3:
				psql.append(" from tab_hgwcustomer a,hgwcust_serv_info b");
				psql.append("	where a.user_id=b.user_id ");
				psql.append(" and a.user_id in(select user_id from tab_voip_serv_param c where c.voip_phone='" + username + "')");
				break;
			case 4:
				psql.append(" from tab_hgwcustomer a,hgwcust_serv_info b");
				psql.append("	where a.user_id=b.user_id ");
				psql.append(" and a.user_id in(select user_id from tab_voip_serv_param c where c.voip_username='" + username + "')");
				break;
			default:
				psql.append(" from tab_hgwcustomer a where a.user_state = '1'");
				psql.append(" and a.username = '" + username + "'");
			}

		return DBOperation.getRecords(psql.getSQL());
	}

	@Override
	public int openOttServ(long user_id, Map<String,String> userInfo, String bind_port)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into hgwcust_serv_info (user_id,serv_type_id,username,passwd,serv_status,");
		psql.append(" bind_port,open_status, dealdate,opendate,updatetime) values (?,?,?,?,?, ?,?,?,?,?)");
		psql.setLong(1, user_id);
		psql.setInt(2, 18);//ott业务
		psql.setString(3, userInfo.get("username"));
		psql.setString(4, "");
		// 业务状态，开户
		psql.setInt(5, 1);
		psql.setString(6, bind_port);

			// 开通状态置为未开通
		psql.setInt(7, 1);

		psql.setLong(8, new Date().getTime()/1000);
		// 开户时间
		psql.setLong(9, new Date().getTime()/1000);
		psql.setLong(10, new Date().getTime()/1000);

		return DBOperation.executeUpdate(psql.getSQL());
	}

	/**
	 * 开通：true
	 *
	 */
	@Override
	public boolean isOpenOtt(String user_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) num from hgwcust_serv_info where serv_type_id = 18 and user_id ="+user_id);

		Map<String, String> map = DBOperation.getRecord(psql.getSQL());
		if (map != null && StringUtil.getIntValue(map, "num") > 0) {
			return true;
		}
		return false;
	}

	@Override
	public int deleteOttServ(String user_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("delete from hgwcust_serv_info where serv_type_id = 18 and user_id ="+user_id);

		return DBOperation.executeUpdate(psql.getSQL());
	}

	@Override
	public int updateNetBindPort(String user_id, String bind_port)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("update hgwcust_serv_info set bind_port = ?,open_status = 0 where serv_type_id = 10 and user_id ="+user_id);
		psql.setString(1, bind_port);

		return DBOperation.executeUpdate(psql.getSQL());
	}

	@Override
	public Map<String, String> getUserInfo(String userId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.user_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.spec_id, b.bind_port from tab_hgwcustomer a,hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 10 and a.user_id = "+userId);
		return DBOperation.getRecord(psql.getSQL());
	}
}
