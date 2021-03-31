
package com.linkage.module.bbms.report.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("unchecked")
public class PortFluxQueryDAO extends SuperDAO
{


	/**
	 * 判断报表是否存在
	 *
	 * @author wangsenbo
	 * @date Mar 18, 2010
	 * @param
	 * @return int
	 */
	public int isHaveTable(String tableName)
	{
		String sql = "select name from sysobjects where name ='" + tableName + "'";
		if (DBUtil.GetDB() == Global.DB_ORACLE)
		{// oracle
			sql = "select table_name as name from user_tables where table_name='"
					+ StringUtil.getUpperCase(tableName) + "'";
		}
		else if (DBUtil.GetDB() == Global.DB_SYBASE)
		{// sybase
			sql = "select name from sysobjects where name ='" + tableName + "'";
		}
		else if (DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sql = "select table_name as name from information_schema.tables where table_name ='" + tableName + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		Map map = queryForMap(sql);
		if (map == null)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}


	/**
	 * 查询设备流量报表
	 *
	 * @author wangsenbo
	 * @date Mar 19, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getDevice(String tableName, String cityId,
			String device_serialnumber, String loopbackIp, String starttime,
			String endtime, List<String> deviceIdList)
	{
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql
				.append(
						"select a.device_id,c.city_id,c.device_serialnumber,c.loopback_ip,a.port_info,case when 0=b.if_real_speed then 0 else a.ifinoctetsbps/b.if_real_speed end as avg_count,case when 0=b.if_real_speed then 0 else a.ifinoctetsbpsmax/b.if_real_speed end as max_count from ")
				.append(tableName)
				.append(" a,flux_interfacedeviceport b,tab_gw_device c ")
				.append(
						"where a.device_id=b.device_id and a.port_info=b.port_info and a.device_id=c.device_id ");
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and c.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(starttime))
		{
			sql.append(" and a.collecttime>=").append(starttime);
		}
		if (false == StringUtil.IsEmpty(endtime))
		{
			sql.append(" and a.collecttime<").append(endtime);
		}
		if (false == StringUtil.IsEmpty(device_serialnumber))
		{
			sql.append(" and c.device_serialnumber like '%").append(device_serialnumber)
					.append("'");
		}
		if (false == StringUtil.IsEmpty(loopbackIp))
		{
			sql.append(" and c.loopback_ip='").append(loopbackIp).append("'");
		}
		if (deviceIdList != null && deviceIdList.size() > 0)
		{
			sql.append(" and a.device_id in (").append(StringUtils.weave(deviceIdList))
					.append(")");
		}
		sql.append(" order by avg_count ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		List<Map> list = jt.queryForList(sql.toString());
		return list;
	}

	/**
	 * 查询用户和客户
	 *
	 * @author wangsenbo
	 * @date Mar 19, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getUser(String cityId, String userName, String customerName,
			String linkphone, List<String> deviceIdList)
	{
		StringBuffer sql = new StringBuffer();
		sql
				.append("select e.device_id,d.customer_name,e.username from tab_customerinfo d,tab_egwcustomer e where d.customer_id=e.customer_id and e.user_state in ('1','2') and d.customer_state=1");
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and d.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(customerName))
		{
			sql.append(" and d.customer_name like '%").append(customerName).append("%'");
		}
		if (false == StringUtil.IsEmpty(linkphone))
		{
			sql.append(" and d.linkphone='").append(linkphone).append("'");
		}
		if (false == StringUtil.IsEmpty(userName))
		{
			sql.append(" and e.username='").append(userName).append("'");
		}
		if (deviceIdList != null && deviceIdList.size() > 0)
		{
			sql.append(" and e.device_id in (").append(StringUtils.weave(deviceIdList))
					.append(")");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		List<Map> list = jt.queryForList(sql.toString());
		return list;
	}
}
