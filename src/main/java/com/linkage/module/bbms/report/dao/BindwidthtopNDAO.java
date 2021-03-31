
package com.linkage.module.bbms.report.dao;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.litms.LipossGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("unchecked")
public class BindwidthtopNDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BindwidthtopNDAO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	public List<Map> getTopNReport(String tableName, String cityId, String countDesc,
			String starttime, String endtime)
	{
		StringBuffer sql = new StringBuffer();

		if(DBUtil.GetDB() == Global.DB_MYSQL){
			sql.append("select ");
		}else{
			sql.append("select top 10 ");
		}
		sql
			.append(
					" a.device_id,c.city_id,c.device_serialnumber,c.loopback_ip,a.port_info," +
							"case when b.if_real_speed=0 then 0 else a.ifinoctetsbps/b.if_real_speed end as avg_count from ")
			.append(tableName)
			.append(" a,flux_interfacedeviceport b,tab_gw_device c ")
			.append(
					"where a.device_id=b.device_id and a.port_info=b.port_info and a.device_id=c.device_id ");// TODO wait (more table related)
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
		sql.append(" order by avg_count ").append(countDesc);

		if(DBUtil.GetDB() == Global.DB_MYSQL){
			sql.append("limit 10");
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		logger.debug("开始时间" + new Date());
		List<Map> list = jt.queryForList(sql.toString());
		List<Map> list1 = new ArrayList<Map>();
		if (list.size() > 0)
		{
			cityMap = CityDAO.getCityIdCityNameMap();
			List<String> deviceList = new ArrayList<String>();
			for (Map map : list)
			{
				deviceList.add(StringUtil.getStringValue(map.get("device_id")));
			}
			StringBuffer sql2 = new StringBuffer();
			sql2
					.append(
							"select e.device_id,d.customer_name,e.username from tab_customerinfo d,tab_egwcustomer e where d.customer_id=e.customer_id and e.user_state in ('1','2') and d.customer_state=1")
					.append(" and e.device_id in (")
					.append(StringUtils.weave(deviceList)).append(")");
			psql = new PrepareSQL(sql2.toString());
	    	psql.getSQL();
			List<Map> userlist = jt.queryForList(sql2.toString());
			Map<Object, Map> userMap = new HashMap<Object, Map>();
			for (Map map2 : userlist)
			{
				userMap.put(map2.get("device_id"), map2);
			}
			for (Map map3 : list)
			{
				Map rmap = new HashMap();
				String city_id = StringUtil.getStringValue(map3.get("city_id"));
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					rmap.put("city_name", city_name);
				}
				else
				{
					rmap.put("city_name", "");
				}
				rmap.put("device_serialnumber", map3.get("device_serialnumber"));
				rmap.put("loopback_ip", map3.get("loopback_ip"));
				rmap.put("port_info", map3.get("port_info"));
				double avg_count = StringUtil.getDoubleValue(map3, "avg_count");
				NumberFormat nf = NumberFormat.getPercentInstance();
				nf.setMinimumFractionDigits(2);
				String str = nf.format(avg_count);
				rmap.put("avg_count", str);
				if (userlist.size() > 0)
				{
					String username = null;
					String customer_name = null;
					if(null==userMap.get(map3.get("device_id"))){
						username = "";
						customer_name = "";
					}else{
						if(null==userMap.get(map3.get("device_id")).get("username")){
							username = "";
						}else{
							username = String.valueOf(userMap.get(map3.get("device_id")).get("username"));
						}
						if(null==userMap.get(map3.get("device_id")).get("customer_name")){
							customer_name = "";
						}else{
							customer_name = String.valueOf(userMap.get(map3.get("device_id")).get("customer_name"));
						}
					}
					rmap.put("username", username);
					rmap.put("customer_name", customer_name);
				}
				list1.add(rmap);
			}
		}
		logger.debug("结束时间" + new Date());

		cityMap = null;
		return list1;
	}

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

		PrepareSQL psql = new PrepareSQL(sql);
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
}
