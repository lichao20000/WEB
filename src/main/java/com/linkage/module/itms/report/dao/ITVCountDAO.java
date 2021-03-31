
package com.linkage.module.itms.report.dao;

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
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 考核所有的用户
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class ITVCountDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ITVCountDAO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	/**
	 * 获取上行方式列表
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getWANAccessTypeList()
	{
		PrepareSQL psql = new PrepareSQL(
				"select prod_spec_id,prod_spec_name from itv_prod_spec");
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 获取终端类型列表
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getDeviceTypeList()
	{
		PrepareSQL psql = new PrepareSQL("select type_id,type_name from itv_bss_dev_type");
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 获取套餐列表
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getPackageList()
	{
		PrepareSQL psql = new PrepareSQL(
				"select serv_package_id,serv_package_name from itv_serv_package");
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * iTV业务统计
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return List<Map>
	 */
	public Map iTVCount(String starttime1, String endtime1, String cityId,
			String wanAccessTypeId, String forbidNet, String typeId)
	{
		logger.debug("iTVCount({},{},{},{},{},{})", new Object[] { starttime1, endtime1,
				cityId, wanAccessTypeId, forbidNet, typeId });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select city_id, count(*) total from itv_customer_info where 1=1 ");
		}else{
			sql.append("select city_id, count(1) total from itv_customer_info where 1=1 ");
		}
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and completedate<").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)&&!"00".equals(cityId))
		{
			List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in (").append(StringUtils.weave(cityList)).append(
					") ");
			cityList = null;
		}
		if (false == StringUtil.IsEmpty(wanAccessTypeId) && !"-1".equals(wanAccessTypeId))
		{
			sql.append(" and prod_spec_id='").append(wanAccessTypeId).append("' ");
		}
		if (false == StringUtil.IsEmpty(forbidNet) && !"-1".equals(forbidNet))
		{
			sql.append(" and forbid_net=").append(forbidNet);
		}
		if (false == StringUtil.IsEmpty(typeId) && !"-1".equals(typeId))
		{
			sql.append(" and type_id='").append(typeId).append("' ");
		}
		sql.append(" group by city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
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
	 * iTV业务统计用户列表
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getHgwList(String starttime1, String endtime1, String cityId,
			String wanAccessTypeId, String forbidNet, String typeId,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getHgwList({},{},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, wanAccessTypeId, forbidNet, typeId, curPage_splitPage,
				num_splitPage });
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,a.username,b.type_name,c.serv_package_name,d.prod_spec_name,a.forbid_net ");
		sql.append("from itv_customer_info a,itv_bss_dev_type b,itv_serv_package c,itv_prod_spec d ");
		sql.append("where a.type_id=b.type_id and a.serv_package_id=c.serv_package_id ");
		sql.append("and a.prod_spec_id=d.prod_spec_id ");
		
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.completedate<").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)&&!"00".equals(cityId))
		{
			List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityList)).append(
					") ");
			cityList = null;
		}
		if (false == StringUtil.IsEmpty(wanAccessTypeId) && !"-1".equals(wanAccessTypeId))
		{
			sql.append(" and a.prod_spec_id='").append(wanAccessTypeId).append("' ");
		}
		if (false == StringUtil.IsEmpty(forbidNet) && !"-1".equals(forbidNet))
		{
			sql.append(" and a.forbid_net=").append(forbidNet);
		}
		if (false == StringUtil.IsEmpty(typeId) && !"-1".equals(typeId))
		{
			sql.append(" and a.type_id='").append(typeId).append("' ");
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
				map.put("type_name", rs.getString("type_name"));
				map.put("serv_package_name", rs.getString("serv_package_name"));
				map.put("prod_spec_name", rs.getString("prod_spec_name"));
				String forbid_net = rs.getString("forbid_net");
				if ("1".equals(forbid_net))
				{
					map.put("forbid_net", "纯IPTV");
				}
				else
				{
					map.put("forbid_net", "非纯IPTV");
				}
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	/**
	 * iTV业务统计用户列表计数
	 * 
	 * @author wangsenbo
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @date Apr 22, 2010
	 * @param
	 * @return int
	 */
	public int getHgwCount(String starttime1, String endtime1, String cityId,
			String wanAccessTypeId, String forbidNet, String typeId,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getHgwList({},{},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, wanAccessTypeId, forbidNet, typeId, curPage_splitPage,
				num_splitPage });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) from itv_customer_info where 1=1 ");
		}else{
			sql.append("select count(1) from itv_customer_info where 1=1 ");
		}
		
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and completedate<").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)&&!"00".equals(cityId))
		{
			List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in (").append(StringUtils.weave(cityList)).append(
					") ");
			cityList = null;
		}
		if (false == StringUtil.IsEmpty(wanAccessTypeId) && !"-1".equals(wanAccessTypeId))
		{
			sql.append(" and prod_spec_id='").append(wanAccessTypeId).append("' ");
		}
		if (false == StringUtil.IsEmpty(forbidNet) && !"-1".equals(forbidNet))
		{
			sql.append(" and forbid_net=").append(forbidNet);
		}
		if (false == StringUtil.IsEmpty(typeId) && !"-1".equals(typeId))
		{
			sql.append(" and type_id='").append(typeId).append("' ");
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
			String wanAccessTypeId, String forbidNet, String typeId)
	{
		logger.debug("getHgwList({},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, wanAccessTypeId, forbidNet, typeId });
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,a.username,b.type_name,c.serv_package_name,d.prod_spec_name,a.forbid_net ");
		sql.append("from itv_customer_info a,itv_bss_dev_type b,itv_serv_package c,itv_prod_spec d ");
		sql.append("where a.type_id=b.type_id and a.serv_package_id=c.serv_package_id ");
		sql.append("and a.prod_spec_id=d.prod_spec_id ");
		
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.completedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.completedate<").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)&&!"00".equals(cityId))
		{
			List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityList)).append(
					") ");
			cityList = null;
		}
		if (false == StringUtil.IsEmpty(wanAccessTypeId) && !"-1".equals(wanAccessTypeId))
		{
			sql.append(" and a.prod_spec_id='").append(wanAccessTypeId).append("' ");
		}
		if (false == StringUtil.IsEmpty(forbidNet) && !"-1".equals(forbidNet))
		{
			sql.append(" and a.forbid_net=").append(forbidNet);
		}
		if (false == StringUtil.IsEmpty(typeId) && !"-1".equals(typeId))
		{
			sql.append(" and a.type_id='").append(typeId).append("' ");
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
				map.put("type_name", rs.getString("type_name"));
				map.put("serv_package_name", rs.getString("serv_package_name"));
				map.put("prod_spec_name", rs.getString("prod_spec_name"));
				String forbid_net = rs.getString("forbid_net");
				if ("1".equals(forbid_net))
				{
					map.put("forbid_net", "纯IPTV");
				}
				else
				{
					map.put("forbid_net", "非纯IPTV");
				}
				return map;
			}
		});
		cityMap = null;
		return list;
	}
}
