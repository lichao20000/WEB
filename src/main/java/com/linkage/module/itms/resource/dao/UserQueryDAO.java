package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class UserQueryDAO extends SuperDAO {
	private static Logger logger = LoggerFactory
			.getLogger(UserQueryDAO.class);

	private Map<String, String> cityMap = new HashMap<String, String>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> queryUserQuery(String city_id,
			String start_time, String end_time, String deviceType,
			String isActive, String specName, String moreinter, String moreitv,
			String morevoip, String moretianyi, String querytype) {
		StringBuffer sql = new StringBuffer();
		Date date = new Date(Long.valueOf(start_time) * 1000);
		if ("1".equals(querytype)) {
			long StartTime = yesterdayStartTime(date);
			long endTime = yesterdayEndTime(date);
			sql.append("select b.city_id, b.num from tab_terminaluserbinding b where b.type=1 and b.module='2' ");
			sql.append(" and b.id=-1  ");
			sql.append(" and b.time>=").append(StartTime);
			sql.append(" and b.time<=").append(endTime);
		}
		if ("2".equals(querytype)) {
			long StartTime = lastWeekStartTime(date);
			long endTime = lastWeekEndTime(date);
			sql.append("select b.city_id, b.num from tab_terminaluserbinding b where b.type=2 and b.module='2' ");
			sql.append(" and b.id=-1  ");
			sql.append(" and b.time>=").append(StartTime);
			sql.append(" and b.time<=").append(endTime);
		}
		if ("3".equals(querytype)) {
			long StartTime = lastMonthStartTime(date);
			long endTime = lastMonthEndTime(date);
			sql.append("select b.city_id, b.num from tab_terminaluserbinding b where b.type=3 and b.module='2' ");
			sql.append(" and b.id=-1  ");
			sql.append(" and b.time>=").append(StartTime);
			sql.append(" and b.time<=").append(endTime);
		}
		if ("4".equals(querytype)) {
			sql.append("select b.city_id, count(1) num from tab_hgwcustomer b,gw_cust_user_dev_type c,tab_bss_dev_port d");
			if (!StringUtil.IsEmpty(moreinter)) {
				sql.append(",user_attribute e");
			}
			if (!StringUtil.IsEmpty(moreitv)) {
				sql.append(",(select b.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 11  group by b.user_id having count(1) > 1) f");
			}
			if (!StringUtil.IsEmpty(morevoip)) {
				sql.append(",(select b.user_id from tab_hgwcustomer a, tab_voip_serv_param b where a.user_id = b.user_id group by a.user_id having count(1) > 1) g");
			}
			if (!StringUtil.IsEmpty(moretianyi)) {
				sql.append(",(select a.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 25) j");
			}
			sql.append(" where b.spec_id = d.id and b.user_id=c.user_id ");
			if (!StringUtil.IsEmpty(deviceType)) {
				sql.append(" and c.type_id='" + deviceType + "'");
			}
			if (!StringUtil.IsEmpty(start_time)) {
				sql.append(" and b.dealdate>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time)) {
				sql.append(" and b.dealdate<=").append(end_time);
			}
			if (!StringUtil.IsEmpty(isActive)) {
				sql.append(" and b.is_active=").append(isActive);
			}
			if (!StringUtil.IsEmpty(specName)) {
				sql.append(" and d.gw_type='" + specName + "'");
			}
			if (!StringUtil.IsEmpty(moreinter)) {
				sql.append(" and b.user_id = e.user_id and e.is_unique_net=")
						.append(moreinter);
			}
			if (!StringUtil.IsEmpty(moreitv)) {
				sql.append(" and b.user_id = f.user_id");
			}
			if (!StringUtil.IsEmpty(morevoip)) {
				sql.append(" and b.user_id = g.user_id");
			}
			if (!StringUtil.IsEmpty(moretianyi)) {
				sql.append(" and b.user_id = j.user_id");
			}
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if ("4".equals(querytype)) {
			sql.append(" group by b.city_id ");
		}
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
	public List<Map> queryUserQueryList(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String moreinter, String moreitv, String morevoip,
			String moretianyi, int curPage_splitPage, int num_splitPage) {
		logger.debug("UserQueryDAO==>queryUserQueryList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,b.username loid,b.user_id,b.dealdate"
				+ " from tab_hgwcustomer b,tab_bss_dev_port d,gw_cust_user_dev_type c ");
		if (!StringUtil.IsEmpty(moreinter)) {
			sql.append(",user_attribute e");
		}
		if (!StringUtil.IsEmpty(moreitv)) {
			sql.append(",(select b.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 11  group by b.user_id having count(1) > 1) f");
		}
		if (!StringUtil.IsEmpty(morevoip)) {
			sql.append(",(select b.user_id from tab_hgwcustomer a, tab_voip_serv_param b where a.user_id = b.user_id group by a.user_id having count(1) > 1) g");
		}
		if (!StringUtil.IsEmpty(moretianyi)) {
			sql.append(",(select a.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 25) j");
		}
		sql.append(" where b.spec_id = d.id and b.user_id=c.user_id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and c.type_id='" + deviceType + "'");
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and b.dealdate>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and b.dealdate<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(isActive)) {
			sql.append(" and b.is_active=").append(isActive);
		}
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and d.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(moreinter)) {
			sql.append(" and b.user_id = e.user_id and e.is_unique_net=")
					.append(moreinter);
		}
		if (!StringUtil.IsEmpty(moreitv)) {
			sql.append(" and b.user_id = f.user_id");
		}
		if (!StringUtil.IsEmpty(morevoip)) {
			sql.append(" and b.user_id = g.user_id");
		}
		if (!StringUtil.IsEmpty(moretianyi)) {
			sql.append(" and b.user_id = j.user_id");
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL pSql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String cityId = StringUtil.getStringValue(rs
						.getString("city_id"));
				map.put("city_id", cityId);
				map.put("city_name", cityMap.get(cityId));
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				map.put("user_id", StringUtil.getStringValue(rs.getString("user_id")));
				try {
					long dealdate = StringUtil.getLongValue(rs
							.getString("dealdate")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(dealdate);
					map.put("dealdate", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("dealdate", "");
				} catch (Exception e) {
					map.put("dealdate", "");
				}
				return map;
			}
		});
		
		List<Map> returnList = new ArrayList<Map>();
		List<Map> tmpList = null;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				int user_id = StringUtil.getIntegerValue(list.get(i).get("user_id"));
				Map<String,String> tmp = (Map<String,String>)list.get(i);
				
				StringBuffer sqls = new StringBuffer();
				sqls.append("select h.username interusername,h.username itvusername,k.voip_username voipusername,h.serv_type_id from hgwcust_serv_info h left join tab_voip_serv_param k on( h.user_id=k.user_id and h.serv_type_id=14) ");
				sqls.append("where h.user_id = " + user_id);
				PrepareSQL psql = new PrepareSQL(sqls.toString());
				tmpList = new ArrayList<Map>();
				tmpList = jt.queryForList(psql.getSQL());
				String interusername = "";
				String itvusername = "";
				String voipusername = "";
				for(int j = 0; j < tmpList.size(); j++){
					String type = String.valueOf(tmpList.get(j).get("serv_type_id"));
					if("10".equals(type)){
						interusername = (String)tmpList.get(j).get("interusername");
					}
					if("11".equals(type)){
						itvusername = (String)tmpList.get(j).get("itvusername");
					}
					if("14".equals(type)){
						voipusername = (String)tmpList.get(j).get("voipusername");
					}
				}
				tmp.put("interusername", interusername);
				tmp.put("itvusername", itvusername);
				tmp.put("voipusername", voipusername);

				returnList.add(tmp);
				tmp = null;
				psql = null;
				tmpList = null;
			}
		}
		
		return returnList;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> queryUserQueryListNewBak(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String moreinter, String moreitv, String morevoip,
			String moretianyi, int curPage_splitPage, int num_splitPage) {
		logger.debug("UserQueryDAO==>queryUserQueryList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,b.username loid,h.username interusername,h.username itvusername,k.voip_username voipusername,b.dealdate,"
				+ "h.serv_type_id from tab_hgwcustomer b,tab_bss_dev_port d,gw_cust_user_dev_type c,hgwcust_serv_info h left join "
				+ "tab_voip_serv_param k on( h.user_id=k.user_id and h.serv_type_id=14) ");
		if (!StringUtil.IsEmpty(moreinter)) {
			sql.append(",user_attribute e");
		}
		if (!StringUtil.IsEmpty(moreitv)) {
			sql.append(",(select b.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 11  group by b.user_id having count(1) > 1) f");
		}
		if (!StringUtil.IsEmpty(morevoip)) {
			sql.append(",(select b.user_id from tab_hgwcustomer a, tab_voip_serv_param b where a.user_id = b.user_id group by a.user_id having count(1) > 1) g");
		}
		if (!StringUtil.IsEmpty(moretianyi)) {
			sql.append(",(select a.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 25) j");
		}
		sql.append(" where b.spec_id = d.id and b.user_id=c.user_id and b.user_id = h.user_id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and c.type_id='" + deviceType + "'");
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and b.dealdate>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and b.dealdate<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(isActive)) {
			sql.append(" and b.is_active=").append(isActive);
		}
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and d.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(moreinter)) {
			sql.append(" and b.user_id = e.user_id and e.is_unique_net=")
					.append(moreinter);
		}
		if (!StringUtil.IsEmpty(moreitv)) {
			sql.append(" and b.user_id = f.user_id");
		}
		if (!StringUtil.IsEmpty(morevoip)) {
			sql.append(" and b.user_id = g.user_id");
		}
		if (!StringUtil.IsEmpty(moretianyi)) {
			sql.append(" and b.user_id = j.user_id");
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL pSql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String cityId = StringUtil.getStringValue(rs
						.getString("city_id"));
				map.put("city_id", cityId);
				map.put("city_name", cityMap.get(cityId));
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				String serv_type_id = StringUtil.getStringValue(rs
						.getString("serv_type_id"));
				map.put("servType", serv_type_id);
				if ("10".equals(serv_type_id)) {
					map.put("interusername", StringUtil.getStringValue(rs
							.getString("interusername")));
					map.put("itvusername", "");
					map.put("voipusername", "");
				}
				if ("11".equals(serv_type_id)) {
					map.put("interusername", "");
					map.put("itvusername", StringUtil.getStringValue(rs
							.getString("itvusername")));
					map.put("voipusername", "");
				}
				if ("14".equals(serv_type_id)) {
					map.put("interusername", "");
					map.put("itvusername", "");
					map.put("voipusername", StringUtil.getStringValue(rs
							.getString("voipusername")));
				}
				try {
					long dealdate = StringUtil.getLongValue(rs
							.getString("dealdate")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(dealdate);
					map.put("dealdate", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("dealdate", "");
				} catch (Exception e) {
					map.put("dealdate", "");
				}
				return map;
			}
		});
		
		List<Map> returnList = new ArrayList<Map>();
		List<String> countList = new ArrayList<String>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String loid = StringUtil.getStringValue(list.get(i).get("loid"));
				if(countList.indexOf(loid) > -1) continue;
				Map<String,String> tmp = new HashMap<String,String>();
				String cityId = StringUtil.getStringValue(list.get(i).get("city_id"));
				tmp.put("city_id", cityId);
				tmp.put("city_name", cityMap.get(cityId));
				tmp.put("loid",StringUtil.getStringValue(list.get(i).get("loid")));
				String interusername = "";
				String itvusername = "";
				String voipusername = "";
				for(int j = 0; j < list.size(); j++){
					Map tmpMap = (Map)list.get(j);
					String tmpLoid = String.valueOf(list.get(j).get("loid"));
					if(tmpLoid.equals(loid)){
						String tmpValue = (String)tmpMap.get("servType");
						if("10".equals(tmpValue)){
							interusername = (String)list.get(j).get("interusername");
						}
						if("11".equals(tmpValue)){
							itvusername = (String)list.get(j).get("itvusername");
						}
						if("14".equals(tmpValue)){
							voipusername = (String)list.get(j).get("voipusername");
						}
					}
				}
				tmp.put("interusername", interusername);
				tmp.put("itvusername", itvusername);
				tmp.put("voipusername", voipusername);
				tmp.put("dealdate", StringUtil.getStringValue(list.get(i).get("dealdate")));

				returnList.add(tmp);
				countList.add(loid);
				tmp = null;
			}
		}
		
		return returnList;
	}


	public List<Map> queryUserQueryListBak(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String moreinter, String moreitv, String morevoip,
			String moretianyi, int curPage_splitPage, int num_splitPage) {
		logger.debug("UserQueryDAO==>queryUserQueryList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,b.username loid,h.username interusername,h.username itvusername,k.voip_username voipusername,b.dealdate,"
				+ "h.serv_type_id from tab_hgwcustomer b,tab_bss_dev_port d,gw_cust_user_dev_type c,hgwcust_serv_info h left join "
				+ "tab_voip_serv_param k on( h.user_id=k.user_id and h.serv_type_id=14) ");
		if (!StringUtil.IsEmpty(moreinter)) {
			sql.append(",user_attribute e");
		}
		if (!StringUtil.IsEmpty(moreitv)) {
			sql.append(",(select b.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 11  group by b.user_id having count(1) > 1) f");
		}
		if (!StringUtil.IsEmpty(morevoip)) {
			sql.append(",(select b.user_id from tab_hgwcustomer a, tab_voip_serv_param b where a.user_id = b.user_id group by a.user_id having count(1) > 1) g");
		}
		if (!StringUtil.IsEmpty(moretianyi)) {
			sql.append(",(select a.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 25) j");
		}
		sql.append(" where b.spec_id = d.id and b.user_id=c.user_id and b.user_id = h.user_id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and c.type_id='" + deviceType + "'");
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and b.dealdate>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and b.dealdate<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(isActive)) {
			sql.append(" and b.is_active=").append(isActive);
		}
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and d.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(moreinter)) {
			sql.append(" and b.user_id = e.user_id and e.is_unique_net=")
					.append(moreinter);
		}
		if (!StringUtil.IsEmpty(moreitv)) {
			sql.append(" and b.user_id = f.user_id");
		}
		if (!StringUtil.IsEmpty(morevoip)) {
			sql.append(" and b.user_id = g.user_id");
		}
		if (!StringUtil.IsEmpty(moretianyi)) {
			sql.append(" and b.user_id = j.user_id");
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL pSql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String cityId = StringUtil.getStringValue(rs
						.getString("city_id"));
				map.put("city_id", cityId);
				map.put("city_name", cityMap.get(cityId));
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				String serv_type_id = StringUtil.getStringValue(rs
						.getString("serv_type_id"));
				if ("10".equals(serv_type_id)) {
					map.put("interusername", StringUtil.getStringValue(rs
							.getString("interusername")));
					map.put("itvusername", "");
					map.put("voipusername", "");
				}
				if ("11".equals(serv_type_id)) {
					map.put("interusername", "");
					map.put("itvusername", StringUtil.getStringValue(rs
							.getString("itvusername")));
					map.put("voipusername", "");
				}
				if ("14".equals(serv_type_id)) {
					map.put("interusername", "");
					map.put("voipusername", "");
					map.put("voipusername", StringUtil.getStringValue(rs
							.getString("voipusername")));
				}
				try {
					long dealdate = StringUtil.getLongValue(rs
							.getString("dealdate")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(dealdate);
					map.put("dealdate", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("dealdate", "");
				} catch (Exception e) {
					map.put("dealdate", "");
				}
				return map;
			}
		});
		return list;
	}

	@SuppressWarnings("rawtypes")
	public int countQueryUserQueryList(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String moreinter, String moreitv, String morevoip,
			String moretianyi, int curPage_splitPage, int num_splitPage) {
		logger.debug("UserQueryDAO==>countQueryUserQueryList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from tab_hgwcustomer b,tab_bss_dev_port d,gw_cust_user_dev_type c ");
		if (!StringUtil.IsEmpty(moreinter)) {
			sql.append(",user_attribute e");
		}
		if (!StringUtil.IsEmpty(moreitv)) {
			sql.append(",(select b.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 11  group by b.user_id having count(1) > 1) f");
		}
		if (!StringUtil.IsEmpty(morevoip)) {
			sql.append(",(select b.user_id from tab_hgwcustomer a, tab_voip_serv_param b where a.user_id = b.user_id group by a.user_id having count(1) > 1) g");
		}
		if (!StringUtil.IsEmpty(moretianyi)) {
			sql.append(",(select a.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 25) j");
		}
		sql.append(" where b.spec_id = d.id and b.user_id=c.user_id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and c.type_id='" + deviceType + "'");
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and b.dealdate>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and b.dealdate<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(isActive)) {
			sql.append(" and b.is_active=").append(isActive);
		}
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and d.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(moreinter)) {
			sql.append(" and b.user_id = e.user_id and e.is_unique_net=")
					.append(moreinter);
		}
		if (!StringUtil.IsEmpty(moreitv)) {
			sql.append(" and b.user_id = f.user_id");
		}
		if (!StringUtil.IsEmpty(morevoip)) {
			sql.append(" and b.user_id = g.user_id");
		}
		if (!StringUtil.IsEmpty(moretianyi)) {
			sql.append(" and b.user_id = j.user_id");
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> excelQueryUserQueryList(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String moreinter, String moreitv, String morevoip,
			String moretianyi) {
		logger.debug("UserQueryDAO==>excelQueryUserQueryList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,b.username loid,h.username interusername,h.username itvusername,k.voip_username voipusername,b.dealdate,"
				+ "h.serv_type_id from tab_hgwcustomer b,tab_bss_dev_port d,gw_cust_user_dev_type c,hgwcust_serv_info h left join "
				+ "tab_voip_serv_param k on( h.user_id=k.user_id and h.serv_type_id=14) ");
		if (!StringUtil.IsEmpty(moreinter)) {
			sql.append(",user_attribute e");
		}
		if (!StringUtil.IsEmpty(moreitv)) {
			sql.append(",(select b.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 11  group by b.user_id having count(1) > 1) f");
		}
		if (!StringUtil.IsEmpty(morevoip)) {
			sql.append(",(select b.user_id from tab_hgwcustomer a, tab_voip_serv_param b where a.user_id = b.user_id group by a.user_id having count(1) > 1) g");
		}
		if (!StringUtil.IsEmpty(moretianyi)) {
			sql.append(",(select a.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 25) j");
		}
		sql.append(" where b.spec_id = d.id and b.user_id=c.user_id and b.user_id = h.user_id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and c.type_id='" + deviceType + "'");
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and b.dealdate>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and b.dealdate<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(isActive)) {
			sql.append(" and b.is_active=").append(isActive);
		}
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and d.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(moreinter)) {
			sql.append(" and b.user_id = e.user_id and e.is_unique_net=")
					.append(moreinter);
		}
		if (!StringUtil.IsEmpty(moreitv)) {
			sql.append(" and b.user_id = f.user_id");
		}
		if (!StringUtil.IsEmpty(morevoip)) {
			sql.append(" and b.user_id = g.user_id");
		}
		if (!StringUtil.IsEmpty(moretianyi)) {
			sql.append(" and b.user_id = j.user_id");
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL pSql = new PrepareSQL(sql.toString());

		List<Map> returnList = new ArrayList<Map>();
		List<String> countList = new ArrayList<String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSql.getSQL());
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String loid = StringUtil.getStringValue(list.get(i).get("loid"));
				if(countList.indexOf(loid) > -1) continue;
				Map tmp = new HashMap();
				String cityId = StringUtil.getStringValue(list.get(i).get(
						"city_id"));
				tmp.put("city_id", cityId);
				tmp.put("city_name", cityMap.get(cityId));
				tmp.put("loid",
						StringUtil.getStringValue(list.get(i).get("loid")));
				String interusername = "";
				String itvusername = "";
				String voipusername = "";
				for(int j = 0; j < list.size(); j++){
					String tmpLoid = String.valueOf(list.get(j).get("loid"));
					if(tmpLoid.equals(loid)){
						String tmpValue = String.valueOf(list.get(j).get("serv_type_id"));
						if("10".equals(tmpValue)){
							interusername = (String)list.get(j).get("interusername");
						}
						if("11".equals(tmpValue)){
							itvusername = (String)list.get(j).get("itvusername");
						}
						if("14".equals(tmpValue)){
							voipusername = (String)list.get(j).get("voipusername");
						}
					}
				}
				tmp.put("interusername", interusername);
				tmp.put("itvusername", itvusername);
				tmp.put("voipusername", voipusername);
				try {
					long dealdate = StringUtil.getLongValue(list.get(i).get(
							"dealdate")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(dealdate);
					tmp.put("dealdate", dt.getLongDate());
				} catch (NumberFormatException e) {
					tmp.put("dealdate", "");
				} catch (Exception e) {
					tmp.put("dealdate", "");
				}
				returnList.add(tmp);
				countList.add(loid);
				tmp = null;
			}
		}
		return returnList;
	}
	
	
	public List<Map> excelQueryUserQueryListBak(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String moreinter, String moreitv, String morevoip,
			String moretianyi) {
		logger.debug("UserQueryDAO==>excelQueryUserQueryList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,b.username loid,h.username interusername,h.username itvusername,k.voip_username voipusername,b.dealdate,"
				+ "h.serv_type_id from tab_hgwcustomer b,tab_bss_dev_port d,gw_cust_user_dev_type c,hgwcust_serv_info h left join "
				+ "tab_voip_serv_param k on( h.user_id=k.user_id and h.serv_type_id=14) ");
		if (!StringUtil.IsEmpty(moreinter)) {
			sql.append(",user_attribute e");
		}
		if (!StringUtil.IsEmpty(moreitv)) {
			sql.append(",(select b.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 11  group by b.user_id having count(1) > 1) f");
		}
		if (!StringUtil.IsEmpty(morevoip)) {
			sql.append(",(select b.user_id from tab_hgwcustomer a, tab_voip_serv_param b where a.user_id = b.user_id group by a.user_id having count(1) > 1) g");
		}
		if (!StringUtil.IsEmpty(moretianyi)) {
			sql.append(",(select a.user_id from tab_hgwcustomer a, hgwcust_serv_info b where a.user_id = b.user_id and b.serv_type_id = 25) j");
		}
		sql.append(" where b.spec_id = d.id and b.user_id=c.user_id and b.user_id = h.user_id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and c.type_id='" + deviceType + "'");
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and b.dealdate>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and b.dealdate<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(isActive)) {
			sql.append(" and b.is_active=").append(isActive);
		}
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and d.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(moreinter)) {
			sql.append(" and b.user_id = e.user_id and e.is_unique_net=")
					.append(moreinter);
		}
		if (!StringUtil.IsEmpty(moreitv)) {
			sql.append(" and b.user_id = f.user_id");
		}
		if (!StringUtil.IsEmpty(morevoip)) {
			sql.append(" and b.user_id = g.user_id");
		}
		if (!StringUtil.IsEmpty(moretianyi)) {
			sql.append(" and b.user_id = j.user_id");
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL pSql = new PrepareSQL(sql.toString());

		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSql.getSQL());
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String cityId = StringUtil.getStringValue(list.get(i).get(
						"city_id"));
				list.get(i).put("city_id", cityId);
				list.get(i).put("city_name", cityMap.get(cityId));
				list.get(i).put("loid",
						StringUtil.getStringValue(list.get(i).get("loid")));
				String serv_type_id = StringUtil.getStringValue(list.get(i)
						.get("serv_type_id"));
				if ("10".equals(serv_type_id)) {
					list.get(i).put(
							"interusername",
							StringUtil.getStringValue(list.get(i).get(
									"interusername")));
					list.get(i).put("itvusername", "");
					list.get(i).put("voipusername", "");
				}
				if ("11".equals(serv_type_id)) {
					list.get(i).put("interusername", "");
					list.get(i).put(
							"itvusername",
							StringUtil.getStringValue(list.get(i).get(
									"itvusername")));
					list.get(i).put("voipusername", "");
				}
				if ("14".equals(serv_type_id)) {
					list.get(i).put("interusername", "");
					list.get(i).put("voipusername", "");
					list.get(i).put(
							"voipusername",
							StringUtil.getStringValue(list.get(i).get(
									"voipusername")));
				}
				try {
					long dealdate = StringUtil.getLongValue(list.get(i).get(
							"dealdate")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(dealdate);
					list.get(i).put("dealdate", dt.getLongDate());
				} catch (NumberFormatException e) {
					list.get(i).put("dealdate", "");
				} catch (Exception e) {
					list.get(i).put("dealdate", "");
				}
			}
		}
		return list;
	}

	private long yesterdayStartTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis() / 1000;
	}

	private long yesterdayEndTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTimeInMillis() / 1000;
	}

	private long lastWeekStartTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTimeInMillis() / 1000;
	}

	private long lastWeekEndTime(Date date) {
		final long weekInSecond = 7 * 24 * 3600;
		return lastWeekStartTime(date) + weekInSecond - 1;
	}

	public long lastMonthStartTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis() / 1000;
	}

	private long lastMonthEndTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 0);
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTimeInMillis() / 1000;
	}
}
