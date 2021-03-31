
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
 * 考核所有的用户
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class PVCReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(PVCReportDAO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

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

	/**
	 * 部署的
	 * 
	 * @author wangsenbo
	 * @param isNew
	 * @date Feb 25, 2010
	 * @param
	 * @return Map
	 */
	public Map countDeployedPVC(String starttime1, String endtime1, String isNew,String prodSpecId, String isItv)
	{
		logger.debug("countDeployedPVC({},{})", new Object[] { starttime1, endtime1 });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select city_id,count(*) as num from itv_customer_info where 1=1 ");
		}else{
			sql.append("select city_id,count(1) as num from itv_customer_info where 1=1 ");
		}
		
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and completedate<=").append(endtime1);
		}
		sql.append(" and radius_onlinedate>").append(getTime());
		if ("0".equals(isNew))
		{
			sql.append(" and assess_flag=1");
		}
		if("-1".equals(prodSpecId))
		{
			sql.append(" and prod_spec_id in ('9','15')");
		}
		else
		{
			sql.append(" and prod_spec_id='").append(prodSpecId).append("'");
		}
		
		if(!StringUtil.IsEmpty(isItv) && !"-1".equals(isItv)){
			sql.append(" and forbid_net=").append(isItv);
		}
		
		sql.append(" and  reform_flag=1 and serv_status=1  ").append(
				" group by city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("num")));
			}
		}
		return map;
	}

	/**
	 * 未部署的
	 * 
	 * @author wangsenbo
	 * @param isNew
	 * @date Feb 25, 2010
	 * @param
	 * @return Map
	 */
	public Map countNonDeployPVC(String starttime1, String endtime1, String isNew,String prodSpecId,String isItv)
	{
		logger.debug("countDeployedPVC({},{})", new Object[] { starttime1, endtime1 });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select city_id,count(*) as num from itv_customer_info where 1=1 ");
		}else{
			sql.append("select city_id,count(1) as num from itv_customer_info where 1=1 ");
		}
		
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and completedate<=").append(endtime1);
		}
		sql.append(" and radius_onlinedate>").append(getTime());
		if ("0".equals(isNew))
		{
			sql.append(" and assess_flag=1");
		}
		if("-1".equals(prodSpecId))
		{
			sql.append(" and prod_spec_id in ('9','15')");
		}
		else
		{
			sql.append(" and prod_spec_id='").append(prodSpecId).append("'");
		}
		
		if(!StringUtil.IsEmpty(isItv) && !"-1".equals(isItv))
		{
			sql.append(" and forbid_net=").append(isItv);
		}
		
		sql.append(" and  reform_flag=0 and serv_status=1 ").append(
				" group by city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("num")));
			}
		}
		return map;
	}

	/**
	 * 查询用户
	 * 
	 * @author wangsenbo
	 * @param isNew
	 * @date Feb 25, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getHgwList(String starttime1, String endtime1, String cityId,
			String reform_flag, int curPage_splitPage, int num_splitPage, String isAll,
			String isNew, String prodSpecId, String isItv)
	{
		logger.debug("getHgwList({},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, reform_flag, curPage_splitPage, num_splitPage, isAll });
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,a.username,a.customer_id,a.prod_spec_id,a.customer_name,");
		sql.append("a.completedate,a.bas_ip,a.vlanid,b.prod_spec_name ");
		sql.append("from itv_customer_info a,itv_prod_spec b ");
		sql.append("where b.prod_spec_id=a.prod_spec_id");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.completedate<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		sql.append(" and a.radius_onlinedate>").append(getTime());
		if ("0".equals(isNew))
		{
			sql.append(" and a.assess_flag=1");
		}
		if("-1".equals(prodSpecId))
		{
			sql.append(" and a.prod_spec_id in ('9','15')");
		}
		else
		{
			sql.append(" and a.prod_spec_id='").append(prodSpecId).append("'");
		}
		sql.append(" and  a.reform_flag=").append(reform_flag).append(
				" and a.serv_status=1 ");
		if(!StringUtil.IsEmpty(isItv) && !"-1".equals(isItv))
		{
			sql.append(" and forbid_net=").append(isItv);
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
				map.put("customer_id", rs.getString("customer_id"));
				map.put("customer_name", rs.getString("customer_name"));
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
				map.put("bas_ip", rs.getString("bas_ip"));
				map.put("vlanid", rs.getString("vlanid"));
				map.put("prod_spec_id", rs.getString("prod_spec_id"));
				map.put("prod_spec_name", rs.getString("prod_spec_name"));
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	/**
	 * @author wangsenbo
	 * @param isNew
	 * @date Feb 25, 2010
	 * @param
	 * @return int
	 */
	public int getHgwCount(String starttime1, String endtime1, String cityId,
			String reform_flag, int curPage_splitPage, int num_splitPage, String isAll,
			String isNew, String prodSpecId,String isItv)
	{
		logger.debug("getHgwCount({},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, reform_flag, curPage_splitPage, num_splitPage, isAll });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) from itv_customer_info a where 1=1 ");
		}else{
			sql.append("select count(1) from itv_customer_info a where 1=1 ");
		}
		
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.completedate<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		sql.append(" and a.radius_onlinedate>").append(getTime());
		if ("0".equals(isNew))
		{
			sql.append(" and a.assess_flag=1");
		}
		if("-1".equals(prodSpecId))
		{
			sql.append(" and a.prod_spec_id in ('9','15')");
		}
		else
		{
			sql.append(" and a.prod_spec_id='").append(prodSpecId).append("'");
		}
		
		sql.append(" and  a.reform_flag=").append(reform_flag).append(" and a.serv_status=1 ");
		if(!StringUtil.IsEmpty(isItv) && !"-1".equals(isItv))
		{
			sql.append(" and forbid_net=").append(isItv);
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

	/**
	 * @author wangsenbo
	 * @param isNew
	 * @date Feb 25, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getHgwExcel(String starttime1, String endtime1, String cityId,
			String reform_flag, String isNew, String prodSpecId,String isItv)
	{
		logger.debug("getHgwExcel({},{},{},{})", new Object[] { starttime1, endtime1,
				cityId, reform_flag });
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,username,customer_id,customer_name,completedate,bas_ip,vlanid ");
		sql.append("from itv_customer_info where 1=1 ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and completedate<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		sql.append(" and radius_onlinedate>").append(getTime());
		if ("0".equals(isNew))
		{
			sql.append(" and assess_flag=1");
		}
		if("-1".equals(prodSpecId))
		{
			sql.append(" and prod_spec_id in ('9','15')");
		}
		else
		{
			sql.append(" and prod_spec_id='").append(prodSpecId).append("'");
		}
		sql.append(" and  reform_flag=").append(reform_flag).append(
				" and serv_status=1 ");
		
		if(!StringUtil.IsEmpty(isItv) && !"-1".equals(isItv))
		{
			sql.append(" and forbid_net=").append(isItv);
		}
		
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
				map.put("username", rs.getString("username"));
				map.put("customer_id", rs.getString("customer_id"));
				map.put("customer_name", rs.getString("customer_name"));
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
				map.put("bas_ip", rs.getString("bas_ip"));
				map.put("vlanid", rs.getString("vlanid"));
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	/**
	 * @return the cityMap
	 */
	public Map<String, String> getCityMap()
	{
		return cityMap;
	}

	/**
	 * @param cityMap
	 *            the cityMap to set
	 */
	public void setCityMap(Map<String, String> cityMap)
	{
		this.cityMap = cityMap;
	}

	public String getCompletedate()
	{
		String completedate;
		String sql = "select min(completedate) as completedate from itv_customer_info";
		PrepareSQL psql = new PrepareSQL(sql);
		Map map = queryForMap(psql.getSQL());
		try
		{
			long time = StringUtil.getLongValue(map.get("completedate"));
			DateTimeUtil dt = new DateTimeUtil(time * 1000);
			completedate = dt.getLongDate();
		}
		catch (NumberFormatException e)
		{
			completedate = "";
		}
		catch (Exception e)
		{
			completedate = "";
		}
		return completedate;
	}
}
