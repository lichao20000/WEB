package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class MoreBroadbandBusinessDAO extends SuperDAO {

	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;
	private static Map<String, String> _USERTYPEMAP = new HashMap<String, String>();
	private Map<String, String> userMap = new HashMap<String, String>();

	public MoreBroadbandBusinessDAO() {
		userMap.put("0", "政企客户");
		userMap.put("2", "家庭客户");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getUserType() {
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL pSQL = new PrepareSQL(
				"select id,spec_name from tab_bss_dev_port where 1=1");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSQL.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map.put(StringUtil.getStringValue(list.get(i).get("id")),
						StringUtil.getStringValue(list.get(i).get("spec_name")));
				_USERTYPEMAP.put(StringUtil.getStringValue(list.get(i).get("id")),
								StringUtil.getStringValue(list.get(i).get("spec_name")));
			}
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> countMoreBroadbandBusiness(String starttime,
			String endtime, String city_id, String userType, String user) 
	{
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sql.append("select a.city_id, count(distinct a.username) num ");
		sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,user_attribute c ");
		sql.append("where b.serv_type_id=10 and a.user_id = b.user_id ");
		sql.append("and b.user_id = c.user_id and c.is_unique_net=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(userType)) {
			sql.append(" and a.spec_id=").append(userType);
		}
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.opendate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.opendate<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(user)) {
			sql.append(" and a.cust_type_id=").append(user);
		}

		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDeviceListForWBdTerminal(String city_id,
			String starttime, String endtime, String userType, String user,
			int curPage_splitPage, int num_splitPage) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct a.city_id,a.username,a.spec_id,a.opendate,a.cust_type_id ");
		sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,user_attribute c ");
		sql.append("where b.serv_type_id=10 and a.user_id = b.user_id ");
		sql.append("and b.user_id = c.user_id and c.is_unique_net=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(userType)) {
			sql.append(" and a.spec_id=").append(userType);
		}
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.opendate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.opendate<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(user)) {
			sql.append(" and a.cust_type_id=").append(user);
		}
		sql.append(" order by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				map.put("username", rs.getString("username"));
				map.put("spec_id", _USERTYPEMAP.get(rs.getString("spec_id")));
				try {
					long opendate = StringUtil.getLongValue(rs
							.getString("opendate"));
					DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
					map.put("opendate", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("opendate", "");
				} catch (Exception e) {
					map.put("opendate", "");
				}
				map.put("cust_type_id",
						userMap.get(rs.getString("cust_type_id")));
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	@SuppressWarnings("rawtypes")
	public int getDeviceListForWBdTerminalCount(String city_id,
			String starttime, String endtime, String userType, String user,
			int curPage_splitPage, int num_splitPage) 
	{
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql = new StringBuffer();
		sql.append("select count(distinct a.username) ");
		sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,user_attribute c ");
		sql.append("where b.serv_type_id=10 and a.user_id = b.user_id ");
		sql.append("and b.user_id = c.user_id and c.is_unique_net=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(userType)) {
			sql.append(" and a.spec_id=").append(userType);
		}
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.opendate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.opendate<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(user)) {
			sql.append(" and a.cust_type_id=").append(user);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getDevExcel(String city_id, String starttime,
			String endtime, String userType, String user) 
	{
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct a.city_id,a.username,a.spec_id,a.opendate,a.cust_type_id ");
		sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,user_attribute c ");
		sql.append("where b.serv_type_id=10 and a.user_id = b.user_id ");
		sql.append("and b.user_id = c.user_id and c.is_unique_net=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(userType)) {
			sql.append(" and a.spec_id=").append(userType);
		}
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.opendate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.opendate<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(user)) {
			sql.append(" and a.cust_type_id=").append(user);
		}
		sql.append(" order by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				map.put("username", rs.getString("username"));
				map.put("spec_id", _USERTYPEMAP.get(rs.getString("spec_id")));
				try {
					long opendate = StringUtil.getLongValue(rs
							.getString("opendate"));
					DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
					map.put("opendate", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("opendate", "");
				} catch (Exception e) {
					map.put("opendate", "");
				}
				map.put("cust_type_id",
						userMap.get(rs.getString("cust_type_id")));
				return map;
			}
		});
		cityMap = null;
		return list;
	}
}
