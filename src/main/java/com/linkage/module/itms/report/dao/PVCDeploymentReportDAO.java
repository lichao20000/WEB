
package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 只考核e8-b的
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class PVCDeploymentReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(PVCDeploymentReportDAO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	private static final String CENTER_CITY = "00";

	/**
	 * 查询所有属地的PVC已部署用户的数量
	 * 
	 * @author wangsenbo
	 * @date Jan 29, 2010
	 * @param
	 * @return Map
	 */
	public Map getDeployedPVC(String starttime1, String endtime1)
	{
		logger.debug("getDeployedPVC({},{})", new Object[] { starttime1, endtime1 });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,count(*) as total ");
		}else{
			sql.append("select a.city_id,count(1) as total ");
		}
		sql.append("from tab_hgwcustomer a,tab_iptv_user b where a.user_id=b.user_id ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and b.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and b.completedate<=").append(endtime1);
		}
		sql.append(" and b.assess_flag=1 and b.reform_flag=1 and a.access_style_id=1 ").append(
				" and b.radius_onlinedate>").append(getTime()).append(
				" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}

	/**
	 * 查询所有属地的PVC未部署用户数量
	 * 
	 * @author wangsenbo
	 * @date Jan 29, 2010
	 * @param
	 * @return Map
	 */
	public Map getNonDeployPVC(String starttime1, String endtime1)
	{
		logger.debug("getNonDeployPVC({},{})", new Object[] { starttime1, endtime1 });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,count(*) as total ");
		}else{
			sql.append("select a.city_id,count(1) as total ");
		}
		sql.append("from tab_hgwcustomer a,tab_iptv_user b where a.user_id=b.user_id ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and b.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and b.completedate<=").append(endtime1);
		}
		sql.append(" and b.assess_flag=1 and b.reform_flag=0 and a.access_style_id=1 ").append(
				" and b.radius_onlinedate>").append(getTime()).append(
				" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}

	/**
	 * 查询用户
	 * 
	 * @author wangsenbo
	 * @date Jan 29, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getHgwList(String starttime1, String endtime1, String cityId,
			String reform_flag, int curPage_splitPage, int num_splitPage, String isAll)
	{
		logger.debug("getHgwList({},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, reform_flag, curPage_splitPage, num_splitPage, isAll });
		StringBuffer sql = new StringBuffer();
		sql.append("select a.user_id,a.city_id,b.username,b.bas_ip,");
		sql.append("a.device_serialnumber,b.vlanid,b.completedate ");
		sql.append("from tab_hgwcustomer a,tab_iptv_user b where a.user_id=b.user_id ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and b.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and b.completedate<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!CENTER_CITY.equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" and b.assess_flag=1 and b.reform_flag=").append(reform_flag).append(
				" and a.access_style_id=1 and b.radius_onlinedate>").append(getTime());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
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
				map.put("username", rs.getString("username"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("bas_ip", rs.getString("bas_ip"));
				map.put("vlanid", rs.getString("vlanid"));
				// 将completedate转换成时间
				try
				{
					long completedate = StringUtil.getLongValue(rs
							.getString("completedate"));
					DateTimeUtil dt = new DateTimeUtil(completedate * 1000);
					map.put("completedate", dt.getDate());
				}
				catch (NumberFormatException e)
				{
					map.put("completedate", "");
				}
				catch (Exception e)
				{
					map.put("completedate", "");
				}
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	/**
	 * @author wangsenbo
	 * @date Jan 29, 2010
	 * @param
	 * @return int
	 */
	public int getHgwCount(String starttime1, String endtime1, String cityId,
			String reform_flag, int curPage_splitPage, int num_splitPage, String isAll)
	{
		logger.debug("getHgwCount({},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, reform_flag, curPage_splitPage, num_splitPage, isAll });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_hgwcustomer a,tab_iptv_user b where a.user_id=b.user_id ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and b.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and b.completedate<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!CENTER_CITY.equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" and b.assess_flag=1 and b.reform_flag=").append(reform_flag).append(
				" and a.access_style_id=1 and b.radius_onlinedate>").append(getTime());
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

	/**
	 * 查询所有属地的尊享套餐用户部署PVC的数量
	 * 
	 * @author wangsenbo
	 * @date Jan 29, 2010
	 * @param
	 * @return Map
	 */
	public Map getDeployedPVCPackage(String starttime1, String endtime1)
	{
		logger.debug("getDeployedPVCPackage({},{})",
				new Object[] { starttime1, endtime1 });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select a.city_id,count(*) as total ");
		}else{
			sql.append("select a.city_id,count(1) as total ");
		}
		sql.append("from tab_hgwcustomer a,tab_iptv_user b,gw_cust_user_package c ");
		sql.append("where a.user_id=b.user_id and b.user_id=c.user_id ");
		sql.append("and c.serv_package_id in ('90000002','90000007') ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and b.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and b.completedate<=").append(endtime1);
		}
		sql.append(" and b.assess_flag=1 and b.reform_flag=1 and a.access_style_id=1 ").append(
				" and b.radius_onlinedate>").append(getTime()).append(
				" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}

	/**
	 * 查询所有属地的尊享套餐用户未部署PVC的数量
	 * 
	 * @author wangsenbo
	 * @date Jan 29, 2010
	 * @param
	 * @return Map
	 */
	public Map getNonDeployPVCPackage(String starttime1, String endtime1)
	{
		logger.debug("getNonDeployPVCPackage({},{})",
				new Object[] { starttime1, endtime1 });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select a.city_id,count(*) as total ");
		}else{
			sql.append("select a.city_id,count(1) as total ");
		}
		sql.append("from tab_hgwcustomer a,tab_iptv_user b,gw_cust_user_package c ");
		sql.append("where a.user_id=b.user_id and b.user_id=c.user_id ");
		sql.append("and c.serv_package_id in ('90000002','90000007') ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and b.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and b.completedate<=").append(endtime1);
		}
		sql.append(" and b.assess_flag=1 and b.reform_flag=0 and a.access_style_id=1 ").append(
				" and b.radius_onlinedate>").append(getTime()).append(
				" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}

	/**
	 * 尊享套餐
	 * 
	 * @author wangsenbo
	 * @date Jan 29, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getPackageHgwList(String starttime1, String endtime1, String cityId,
			String reform_flag, int curPage_splitPage, int num_splitPage, String isAll)
	{
		logger.debug("getPackageHgwList({},{},{},{},{},{},{})", new Object[] {
				starttime1, endtime1, cityId, reform_flag, curPage_splitPage,
				num_splitPage, isAll });
		StringBuffer sql = new StringBuffer();
		sql.append("select a.user_id,a.city_id,b.username,b.bas_ip,a.device_serialnumber,b.vlanid,b.completedate ");
		sql.append("from tab_hgwcustomer a,tab_iptv_user b,gw_cust_user_package c ");
		sql.append("where a.user_id=b.user_id and b.user_id=c.user_id ");
		sql.append("and c.serv_package_id in ('90000002','90000007') ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and b.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and b.completedate<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!CENTER_CITY.equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" and b.assess_flag=1 and b.reform_flag=").append(reform_flag).append(
				" and a.access_style_id=1 and b.radius_onlinedate>").append(getTime());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
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
				map.put("username", rs.getString("username"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("bas_ip", rs.getString("bas_ip"));
				map.put("vlanid", rs.getString("vlanid"));
				// 将completedate转换成时间
				try
				{
					long completedate = StringUtil.getLongValue(rs
							.getString("completedate"));
					DateTimeUtil dt = new DateTimeUtil(completedate * 1000);
					map.put("completedate", dt.getDate());
				}
				catch (NumberFormatException e)
				{
					map.put("completedate", "");
				}
				catch (Exception e)
				{
					map.put("completedate", "");
				}
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	/**
	 * 尊享套餐
	 * 
	 * @author wangsenbo
	 * @date Jan 29, 2010
	 * @param
	 * @return int
	 */
	public int getPackageHgwCount(String starttime1, String endtime1, String cityId,
			String reform_flag, int curPage_splitPage, int num_splitPage, String isAll)
	{
		logger.debug("getHgwCount({},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, reform_flag, curPage_splitPage, num_splitPage, isAll });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_hgwcustomer a,tab_iptv_user b,gw_cust_user_package c ");
		sql.append("where a.user_id=b.user_id and b.user_id=c.user_id ");
		sql.append("and c.serv_package_id in ('90000002','90000007') ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and b.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and b.completedate<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!CENTER_CITY.equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" and b.assess_flag=1 and b.reform_flag=").append(reform_flag).append(
				" and a.access_style_id=1 and b.radius_onlinedate>").append(getTime());
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

	/**
	 * 取得现在时间一个月前的时间（0时0分0秒）
	 * 
	 * @author wangsenbo
	 * @date Jan 29, 2010
	 * @param
	 * @return long
	 */
	public long getTime()
	{
		DateTimeUtil dt = new DateTimeUtil();
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
		calendar.set(dt.getYear(), dt.getMonth() - 2, dt.getDay(), 0, 0, 0);
		long radiusOnlinedate = calendar.getTimeInMillis() / 1000;
		return radiusOnlinedate;
	}

	public List<Map> getHgwExcel(String starttime1, String endtime1, String cityId,
			String reform_flag)
	{
		logger.debug("getHgwExcel({},{},{},{})", new Object[] { starttime1, endtime1,
				cityId, reform_flag });
		StringBuffer sql = new StringBuffer();
		sql.append("select a.user_id,a.city_id,b.username,b.bas_ip,a.device_serialnumber,b.vlanid,b.completedate ");
		sql.append("from tab_hgwcustomer a,tab_iptv_user b where a.user_id=b.user_id ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and b.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and b.completedate<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!CENTER_CITY.equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" and b.assess_flag=1 and b.reform_flag=").append(reform_flag).append(
				" and a.access_style_id=1 and b.radius_onlinedate>").append(getTime());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
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
				map.put("username", rs.getString("username"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("bas_ip", rs.getString("bas_ip"));
				map.put("vlanid", rs.getString("vlanid"));
				// 将completedate转换成时间
				try
				{
					long completedate = StringUtil.getLongValue(rs
							.getString("completedate"));
					DateTimeUtil dt = new DateTimeUtil(completedate * 1000);
					map.put("completedate", dt.getDate());
				}
				catch (NumberFormatException e)
				{
					map.put("completedate", "");
				}
				catch (Exception e)
				{
					map.put("completedate", "");
				}
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	public List<Map> getPackageHgwExcel(String starttime1, String endtime1,
			String cityId, String reform_flag)
	{
		logger.debug("getPackageHgwExcel({},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, reform_flag });
		StringBuffer sql = new StringBuffer();
		sql.append("select a.user_id,a.city_id,b.username,b.bas_ip,a.device_serialnumber,b.vlanid,b.completedate ");
		sql.append("from tab_hgwcustomer a,tab_iptv_user b,gw_cust_user_package c ");
		sql.append("where a.user_id=b.user_id and b.user_id=c.user_id ");
		sql.append("and c.serv_package_id in ('90000002','90000007') ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and b.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and b.completedate<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!CENTER_CITY.equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" and b.assess_flag=1 and b.reform_flag=").append(reform_flag).append(
				" and a.access_style_id=1 and b.radius_onlinedate>").append(getTime());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
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
				map.put("username", rs.getString("username"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("bas_ip", rs.getString("bas_ip"));
				map.put("vlanid", rs.getString("vlanid"));
				// 将completedate转换成时间
				try
				{
					long completedate = StringUtil.getLongValue(rs.getString("completedate"));
					DateTimeUtil dt = new DateTimeUtil(completedate * 1000);
					map.put("completedate", dt.getDate());
				}
				catch (NumberFormatException e)
				{
					map.put("completedate", "");
				}
				catch (Exception e)
				{
					map.put("completedate", "");
				}
				return map;
			}
		});
		cityMap = null;
		return list;
	}
}
