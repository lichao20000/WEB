package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class QueryVlanConfigDAO extends SuperDAO {
	private static Logger logger = LoggerFactory
			.getLogger(QueryVlanConfigDAO.class);
	private int queryCount;

	/**
	 * @author 岩
	 * @date 2016-10-13
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryVlanConfigList(String selectType, String username,
			String deviceSerialnumber, String cityId, String isErrorPort,
			int curPage_splitPage, int num_splitPage) {
		logger.debug("queryVlanConfigList()");
		PrepareSQL psql = new PrepareSQL();
		// teledb
		if (DBUtil.GetDB() == 3) {
			psql.append("select city_id, loid, net_account, vendor_id, device_serialnumber, lan1, lan2, lan3, lan4, is_errport, end_time ");
		}
		else {
			psql.append("select * ");
		}
		psql.append(" from  tab_power_result a ");
		psql.append(" where 1=1 ");
		// username若为空，则不加该判断条件，若不为空则根据selectType进行判断
		if ((!StringUtil.IsEmpty(username))) {
			if ("0".equals(selectType)) {
				psql.append(" and  a.loid ='" + username + "'");
			} else {
				psql.append(" and  a.net_account ='" + username + "'");
			}
		}
		// deviceSerialnumber若为空，则不加该判断条件，若不为空则增加筛选条件
		if ((!StringUtil.IsEmpty(deviceSerialnumber))) {
			psql.append(" and a.device_serialnumber like '%");
			psql.append(deviceSerialnumber+"' ");
		}
		// cityId若为-1，则不加该判断条件，若不为空则增加筛选条件
		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId)) && (!"-1".equals(cityId)))
		{
			psql.append("   and city_id in (select city_id from tab_city where city_id='");
			psql.append(cityId);
			psql.append("' or parent_id='");
			psql.append(cityId);
			psql.append("') ");
		}
		// isErrorPort若为0，则不加该判断条件，若不为空则增加筛选条件
		if ((!StringUtil.IsEmpty(isErrorPort)) && (!"0".equals(isErrorPort))) {
			psql.append("   and a.is_errport =");
			psql.append(isErrorPort);
		}

		final Map<String, String> vendorMap = getVendor();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				Map<String, String> map = new HashMap<String, String>();
				String parentName = StringUtil.getStringValue(rs.getString("city_id"));
				String cityName = "";
				if(parentName != null && !"".equals(parentName)){
					if ("00".equals(rs.getString("city_id"))
							|| "00".equals(CityDAO.getCityIdPidMap().get(
									rs.getString("city_id")))) {
						parentName = CityDAO.getCityIdCityNameMap().get(
								rs.getString("city_id"));
						
					} else {
						parentName = CityDAO.getCityIdCityNameMap().get(
								CityDAO.getCityIdPidMap().get(
										rs.getString("city_id")));
					}
					cityName = CityDAO.getCityIdCityNameMap().get(StringUtil.getStringValue(rs.getString("city_id")));
				}else{
					parentName = CityDAO.getCityIdCityNameMap().get("00");
					cityName = CityDAO.getCityIdCityNameMap().get("00");
				}
				
				map.put("parentName", parentName);
				map.put("cityName",cityName);
				map.put("username", StringUtil.getStringValue(rs.getString("loid")));
				map.put("netAccount", StringUtil.getStringValue(rs.getString("net_account")));
				map.put("vendorName", StringUtil.getStringValue(vendorMap.get(rs.getString("vendor_id"))));
				map.put("deviceSn",StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("lan1", StringUtil.getStringValue(rs.getString("lan1")));
				map.put("lan2",StringUtil.getStringValue(rs.getString("lan2")));
				map.put("lan3", StringUtil.getStringValue(rs.getString("lan3")));
				map.put("lan4",StringUtil.getStringValue(rs.getString("lan4")));
				if ("-1".equals(StringUtil.getStringValue(rs.getString("is_errport")))){
					map.put("isErrPort","否" );
				}else{
					map.put("isErrPort","是" );
				}
				
				long gatherTime = StringUtil.getLongValue(rs
						.getString("end_time")) * 1000L;
				DateTimeUtil dt = new DateTimeUtil(gatherTime);
				map.put("gatherTime", dt.getLongDate());
			
				return map;
			}
		});
		return list;
	}

	/**
	 * @author 岩
	 * @date 2016-10-13
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countVlanConfigList(String selectType, String username,
			String deviceSerialnumber, String cityId, String isErrorPort,
			int curPage_splitPage, int num_splitPage) {
		logger.debug("countVlanConfigList()");
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(1) ");
		psql.append(" from  tab_power_result a ");
		psql.append(" where 1=1 ");
		// username若为空，则不加该判断条件，若不为空则根据selectType进行判断
		if ((!StringUtil.IsEmpty(username))) {
			if ("0".equals(selectType)) {
				psql.append(" and  a.loid ='" + username + "'");
			} else {
				psql.append(" and  a.net_account ='" + username + "'");
			}
		}
		// deviceSerialnumber若为空，则不加该判断条件，若不为空则增加筛选条件
		if ((!StringUtil.IsEmpty(deviceSerialnumber))) {
			psql.append(" and a.device_serialnumber like '%");
			psql.append(deviceSerialnumber+"' ");
		}
		// cityId若为-1，则不加该判断条件，若不为空则增加筛选条件
		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId)) && (!"-1".equals(cityId)))
		{
			psql.append("   and city_id in (select city_id from tab_city where city_id='");
			psql.append(cityId);
			psql.append("' or parent_id='");
			psql.append(cityId);
			psql.append("') ");
		}
		// isErrorPort若为0，则不加该判断条件，若不为空则增加筛选条件
		if ((!StringUtil.IsEmpty(isErrorPort)) && (!"0".equals(isErrorPort))) {
			psql.append("   and a.is_errport =");
			psql.append(isErrorPort);
		}

		queryCount = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (queryCount % num_splitPage == 0) {
			maxPage = queryCount / num_splitPage;
		} else {
			maxPage = queryCount / num_splitPage + 1;
		}
		return maxPage;
	}

	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> exportVlanConfigList(String selectType, String username,
			String deviceSerialnumber, String cityId, String isErrorPort) {
		PrepareSQL psql = new PrepareSQL();
		// teledb
		if (DBUtil.GetDB() == 3) {
			psql.append("select city_id, loid, net_account, vendor_id, device_serialnumber, lan1, lan2, lan3, lan4, is_errport, end_time ");
		}
		else {
			psql.append("select * ");
		}
		psql.append(" from  tab_power_result a ");
		psql.append(" where 1=1 ");
		// username若为空，则不加该判断条件，若不为空则根据selectType进行判断
		if ((!StringUtil.IsEmpty(username))) {
			if ("0".equals(selectType)) {
				psql.append(" and  a.loid ='" + username + "'");
			} else {
				psql.append(" and  a.net_account ='" + username + "'");
			}
		}
		// deviceSerialnumber若为空，则不加该判断条件，若不为空则增加筛选条件
		if ((!StringUtil.IsEmpty(deviceSerialnumber))) {
			psql.append(" and a.device_serialnumber like '%");
			psql.append(deviceSerialnumber+"' ");
		}
		// cityId若为-1，则不加该判断条件，若不为空则增加筛选条件
		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId)) && (!"-1".equals(cityId)))
		{
			psql.append("   and city_id in (select city_id from tab_city where city_id='");
			psql.append(cityId);
			psql.append("' or parent_id='");
			psql.append(cityId);
			psql.append("') ");
		}
		// isErrorPort若为0，则不加该判断条件，若不为空则增加筛选条件
		if ((!StringUtil.IsEmpty(isErrorPort)) && (!"0".equals(isErrorPort))) {
			psql.append("   and a.is_errport =");
			psql.append(isErrorPort);
		}
		
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		List<Map> list = new ArrayList<Map>();
		Map<String, Object> map = null;
		Map<String, String> vendorMap = getVendor();
		if (queryList != null && queryList.size() > 0) {
			for (int i = 0; i < queryList.size(); i++) {
				map = new HashMap<String, Object>();
				Map<String, String> queryMap = queryList.get(i);
				String parentName = StringUtil.getStringValue(queryMap, "city_id");
				String cityName = "";
				if(parentName != null && !"".equals(parentName)){
					if ("00".equals(StringUtil.getStringValue(queryMap, "city_id"))
							|| "00".equals(CityDAO.getCityIdPidMap().get(StringUtil.getStringValue(queryMap, "city_id")))) {
						parentName = CityDAO.getCityIdCityNameMap().get(
								StringUtil.getStringValue(queryMap, "city_id"));
						
					} else {
						parentName = CityDAO.getCityIdCityNameMap().get(
								CityDAO.getCityIdPidMap().get(
										StringUtil.getStringValue(queryMap, "city_id")));
					}
					cityName = CityDAO.getCityIdCityNameMap().get(StringUtil.getStringValue(StringUtil.getStringValue(queryMap, "city_id")));
				}else{
					parentName = CityDAO.getCityIdCityNameMap().get("00");
					cityName = CityDAO.getCityIdCityNameMap().get("00");
				}
				
				map.put("index",i+1);
				map.put("parentName", parentName);
				map.put("cityName",cityName);
				map.put("username", StringUtil.getStringValue(queryMap,"loid"));
				map.put("netAccount", StringUtil.getStringValue(queryMap,"net_account"));
				map.put("vendorName", StringUtil.getStringValue(vendorMap.get(queryMap.get("vendor_id"))));
				map.put("deviceSn",StringUtil.getStringValue(queryMap,"device_serialnumber"));
				map.put("lan1", StringUtil.getStringValue(queryMap,"lan1"));
				map.put("lan2",StringUtil.getStringValue(queryMap,"lan2"));
				map.put("lan3", StringUtil.getStringValue(queryMap,"lan3"));
				map.put("lan4",StringUtil.getStringValue(queryMap,"lan4"));
				if ("-1".equals(StringUtil.getStringValue(queryMap,"is_errport"))){
					map.put("isErrPort","否" );
				}else{
					map.put("isErrPort","是" );
				}
				
				long gatherTime = StringUtil.getLongValue(queryMap,"end_time") * 1000L;
				DateTimeUtil dt = new DateTimeUtil(gatherTime);
				map.put("gatherTime", dt.getLongDate());
				list.add(map);
			}
		}
		return list;
	}
	
	public int getQueryCount() {
		return queryCount;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static HashMap<String, String> getVendor() {
		Cursor cursor = DataSetBean
				.getCursor("select vendor_id,vendor_name, vendor_add from tab_vendor where vendor_name is not null");
		HashMap<String, String> vendorMap = new HashMap<String, String>();
		Map fields = cursor.getNext();
		while (fields != null) {
			String vendor_add = (String) fields.get("vendor_add");
			if (false == StringUtil.IsEmpty(vendor_add)) {
				vendorMap.put((String) fields.get("vendor_id"), vendor_add);
			} else {
				vendorMap.put((String) fields.get("vendor_id"),
						(String) fields.get("vendor_name"));
			}
			fields = cursor.getNext();
		}
		return vendorMap;
	}

}
