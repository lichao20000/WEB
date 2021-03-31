
package com.linkage.module.gwms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 桥接、路由统计
 * 
 * @author 王森博
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class BridgeToRouteCountDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(BridgeToRouteCountDAO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	/**
	 * 桥接、路由方式统计
	 * 
	 * @author wangsenbo
	 * @date Jul 19, 2010
	 * @param
	 * @return Map
	 */
	public List count(String starttime1, String endtime1, String cityId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.parm_type_id,b.city_id,count(1) as total ");
		psql.append("from res_user_serv_parm_conn a,tab_hgwcustomer b ");
		psql.append("where a.user_id=b.user_id ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			psql.append(" and a.upd_time>="+starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			psql.append(" and a.upd_time<"+endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId))
		{
			List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and b.city_id in ("+StringUtils.weave(cityList)+") ");
			cityList = null;
		}
		psql.append(" group by b.city_id,a.parm_type_id");
		
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 桥接、路由方式统计2
	 * 
	 * @param starttime1
	 * @param endtime1
	 * @param cityId
	 * @return
	 */
	public List count2(String starttime1, String endtime1, String cityId)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select a.city_id,b.wan_type,count(*) as total ");
		}else{
			psql.append("select a.city_id,b.wan_type,count(1) as total ");
		}
		psql.append("from tab_hgwcustomer a,hgwcust_serv_info b ");
		psql.append("where a.user_id=b.user_id and b.serv_type_id=10 ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			psql.append(" and b.opendate>="+starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			psql.append(" and b.opendate<"+endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId))
		{
			List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and a.city_id in ("+StringUtils.weave(cityList)+") ");
			cityList = null;
		}
		psql.append(" group by a.city_id,b.wan_type");
		
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 导出excel
	 * 
	 * 
	 * @param starttime1
	 * @param endtime1
	 * @param cityId
	 * @return
	 */
	public List<Map> exportExcel2(String starttime1, String endtime1, String cityId, String sessionType)
	{
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		sql.append("select a.city_id,a.username as loid,b.username as net_account," +
				"g.device_serialnumber,g.vendor_add,g.device_model,g.hardwareversion,g.softwareversion " +
				"from tab_hgwcustomer a left join " +
				"(select c.device_id,c.device_serialnumber,d.vendor_add,e.device_model," +
				"f.hardwareversion,f.softwareversion " +
				"from tab_gw_device c,tab_vendor d,gw_device_model e,tab_devicetype_info f " +
				"where c.vendor_id = d.vendor_id  and c.device_model_id = e.device_model_id " +
				"and c.devicetype_id = f.devicetype_id and d.vendor_id = f.vendor_id) g " +
				"on a.device_id = g.device_id," +
				"hgwcust_serv_info b " +
				"where a.user_id=b.user_id and b.serv_type_id = 10 and b.wan_type = " + sessionType);
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and b.opendate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and b.opendate<").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId))
		{
			List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityList)).append(
					") ");
			cityList = null;
		}
		sql.append(" order by a.city_id");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				
				
				map.put("loid", rs.getString("loid"));
				map.put("net_account", rs.getString("net_account"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	public List<Map> getHgwList(String starttime1, String endtime1, String cityId,
			String sessionType, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getHgwList({},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, sessionType, curPage_splitPage, num_splitPage });
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,b.username,b.oui,b.device_serialnumber,a.parm_type_id ");
		sql.append("from res_user_serv_parm_conn a,tab_hgwcustomer b ");
		sql.append("where a.user_id=b.user_id ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.upd_time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.upd_time<").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId))
		{
			List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityList)).append(
					") ");
			cityList = null;
		}
		if (false == StringUtil.IsEmpty(sessionType))
		{
			sql.append(" and a.parm_type_id=").append(sessionType);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				map.put("device", rs.getString("oui") + "-"
						+ rs.getString("device_serialnumber"));
				String parm_type_id = rs.getString("parm_type_id");
				if ("1".equals(parm_type_id))
				{
					map.put("sessionType", "桥接");
				}
				else if ("2".equals(parm_type_id))
				{
					map.put("sessionType", "路由");
				}
				else
				{
					map.put("sessionType", "-");
				}
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	public int getHgwCount(String starttime1, String endtime1, String cityId,
			String sessionType, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getHgwCount({},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, sessionType, curPage_splitPage, num_splitPage });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from res_user_serv_parm_conn a,tab_hgwcustomer b where a.user_id=b.user_id ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.upd_time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.upd_time<").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId))
		{
			List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityList)).append(
					") ");
			cityList = null;
		}
		if (false == StringUtil.IsEmpty(sessionType))
		{
			sql.append(" and a.parm_type_id=").append(sessionType);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List<Map> getHgwExcel(String starttime1, String endtime1, String cityId,
			String sessionType)
	{
		logger.debug("getHgwExcel({},{},{},{})", new Object[] { starttime1, endtime1,
				cityId, sessionType });
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,b.username,b.oui,b.device_serialnumber,a.parm_type_id ");
		sql.append("from res_user_serv_parm_conn a,tab_hgwcustomer b ");
		sql.append("where a.user_id=b.user_id ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.upd_time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.upd_time<").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId))
		{
			List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityList)).append(
					") ");
			cityList = null;
		}
		if (false == StringUtil.IsEmpty(sessionType))
		{
			sql.append(" and a.parm_type_id=").append(sessionType);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				map.put("device", rs.getString("oui") + "-"
						+ rs.getString("device_serialnumber"));
				String parm_type_id = rs.getString("parm_type_id");
				if ("1".equals(parm_type_id))
				{
					map.put("sessionType", "桥接");
				}
				else if ("2".equals(parm_type_id))
				{
					map.put("sessionType", "路由");
				}
				else
				{
					map.put("sessionType", "-");
				}
				return map;
			}
		});
		cityMap = null;
		return list;
	}
}
