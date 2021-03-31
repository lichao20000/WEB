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

public class TerminalUserBindingDAO extends SuperDAO {
	private static Logger logger = LoggerFactory
			.getLogger(TerminalUserBindingDAO.class);

	private Map<String, String> cityMap = new HashMap<String, String>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> queryTerminalUserBinding(String city_id,
			String start_time, String end_time, String specName,
			String operation, String hiddenVal, String querytype) {
		logger.debug("TerminalUserBindingDAO=>queryTerminalUserBinding()");
		StringBuffer sql = new StringBuffer();
		Date date = new Date(Long.valueOf(start_time) * 1000);
		if ("1".equals(querytype)) {
			long StartTime = yesterdayStartTime(date);
			long endTime = yesterdayEndTime(date);
			sql.append("select a.city_id, a.num from tab_terminaluserbinding a where a.type=1 and a.module='1' ");
			if ("1".equals(hiddenVal)) {
				sql.append(" and a.id=1  ");
				sql.append(" and a.time>=").append(StartTime);
				sql.append(" and a.time<=").append(endTime);
			}
			if ("2".equals(hiddenVal)) {
				sql.append(" and a.id=2 ");
				sql.append(" and a.time>=").append(StartTime);
				sql.append(" and a.time<=").append(endTime);
			}
		}
		if ("2".equals(querytype)) {
			long StartTime = lastWeekStartTime(date);
			long endTime = lastWeekEndTime(date);
			sql.append("select a.city_id, a.num from tab_terminaluserbinding a where a.type=2 and a.module='1' ");
			if ("1".equals(hiddenVal)) {
				sql.append(" and a.id=1  ");
				sql.append(" and a.time>=").append(StartTime);
				sql.append(" and a.time<=").append(endTime);
			}
			if ("2".equals(hiddenVal)) {
				sql.append(" and a.id=2 ");
				sql.append(" and a.time>=").append(StartTime);
				sql.append(" and a.time<=").append(endTime);
			}
		}
		if ("3".equals(querytype)) {
			long StartTime = lastMonthStartTime(date);
			long endTime = lastMonthEndTime(date);
			sql.append("select a.city_id, a.num from tab_terminaluserbinding a where a.type=3 and a.module='1' ");
			if ("1".equals(hiddenVal)) {
				sql.append(" and a.id=1  ");
				sql.append(" and a.time>=").append(StartTime);
				sql.append(" and a.time<=").append(endTime);
			}
			if ("2".equals(hiddenVal)) {
				sql.append(" and a.id=2 ");
				sql.append(" and a.time>=").append(StartTime);
				sql.append(" and a.time<=").append(endTime);
			}
		}
		if ("4".equals(querytype)) {
			sql.append("select a.city_id, count(b.username) num from tab_gw_device a,tab_hgwcustomer b,bind_log c " +
				"where a.device_id = b.device_id and b.device_id = c.device_id ");
			if ("1".equals(hiddenVal)) {
				if (!StringUtil.IsEmpty(start_time)) {
					sql.append(" and b.dealdate>=").append(start_time);
				}
				if (!StringUtil.IsEmpty(end_time)) {
					sql.append(" and b.dealdate<=").append(end_time);
				}
			}
			if ("2".equals(hiddenVal)) {
				sql.append("select a.city_id, count(a.device_id) num from  tab_gw_device a,bind_log c where a.device_id = c.device_id ");
				if (!StringUtil.IsEmpty(start_time)) {
					sql.append(" and a.complete_time>=").append(start_time);
				}
				if (!StringUtil.IsEmpty(end_time)) {
					sql.append(" and a.complete_time<=").append(end_time);
				}
			}
			if (!StringUtil.IsEmpty(operation)) {
				sql.append(" and c.oper_type=").append(operation);
			}
//			if (!StringUtil.IsEmpty(specName)) {
//				sql.append(" and d.gw_type='" + specName + "'");
//			}
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if ("4".equals(querytype)) {
			sql.append(" group by a.city_id ");
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
	public List<Map> modelQuery(String city_id, String start_time,
			String end_time, String specName, String operation,
			int curPage_splitPage, int num_splitPage) {
		logger.debug("TerminalUserBindingDAO==>queryTerminalUserBindingList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,c.username,a.device_serialnumber,a.complete_time,c.oper_type,c.binddate,c.dealstaff " +
				"from tab_gw_device a,bind_log c where a.device_id = c.device_id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and a.complete_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and a.complete_time<=").append(end_time);
		}
//		if (!StringUtil.IsEmpty(specName)) {
//			sql.append(" and d.gw_type='" + specName + "'");
//		}
		if (!StringUtil.IsEmpty(operation)) {
			sql.append(" and c.oper_type=").append(operation);
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
				map.put("username",
						StringUtil.getStringValue(rs.getString("username")));
				map.put("device_serialnumber", StringUtil.getStringValue(rs
						.getString("device_serialnumber")));
				try {
					long complete_time = StringUtil.getLongValue(rs
							.getString("complete_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(complete_time);
					map.put("complete_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("complete_time", "");
				} catch (Exception e) {
					map.put("complete_time", "");
				}
//				String gw_type = StringUtil.getStringValue(rs
//						.getString("gw_type"));
//				if ("1".equals(gw_type)) {
//					map.put("gw_type", "家庭网关");
//				}
//				if ("2".equals(gw_type)) {
//					map.put("gw_type", "政企网关");
//				}
				String oper_type = StringUtil.getStringValue(rs
						.getString("oper_type"));
				if ("1".equals(oper_type)) {
					map.put("oper_type", "有绑定操作");
				}
				if ("2".equals(oper_type)) {
					map.put("oper_type", "有解绑操作");
				}
				try {
					long binddate = StringUtil.getLongValue(rs
							.getString("binddate")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(binddate);
					map.put("binddate", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("binddate", "");
				} catch (Exception e) {
					map.put("binddate", "");
				}
				map.put("dealstaff",
						StringUtil.getStringValue(rs.getString("dealstaff")));
				return map;
			}
		});
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> userQuery(String city_id, String start_time,
			String end_time, String specName, String operation,
			int curPage_splitPage, int num_splitPage) {
		logger.debug("TerminalUserBindingDAO==>queryTerminalUserBindingList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,b.username,a.device_serialnumber,b.dealdate,c.oper_type,c.binddate,c.dealstaff " +
				"from tab_gw_device a,tab_hgwcustomer b,bind_log c where a.device_id = b.device_id and b.device_id = c.device_id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and b.dealdate>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and b.dealdate<=").append(end_time);
		}
//		if (!StringUtil.IsEmpty(specName)) {
//			sql.append(" and d.gw_type='" + specName + "'");
//		}
		if (!StringUtil.IsEmpty(operation)) {
			sql.append(" and c.oper_type=").append(operation);
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
				map.put("username",
						StringUtil.getStringValue(rs.getString("username")));
				map.put("device_serialnumber", StringUtil.getStringValue(rs
						.getString("device_serialnumber")));
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
//				String gw_type = StringUtil.getStringValue(rs
//						.getString("gw_type"));
//				if ("1".equals(gw_type)) {
//					map.put("gw_type", "家庭网关");
//				}
//				if ("2".equals(gw_type)) {
//					map.put("gw_type", "政企网关");
//				}
				String oper_type = StringUtil.getStringValue(rs
						.getString("oper_type"));
				if ("1".equals(oper_type)) {
					map.put("oper_type", "有绑定操作");
				}
				if ("2".equals(oper_type)) {
					map.put("oper_type", "有解绑操作");
				}
				try {
					long binddate = StringUtil.getLongValue(rs
							.getString("binddate")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(binddate);
					map.put("binddate", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("binddate", "");
				} catch (Exception e) {
					map.put("binddate", "");
				}
				map.put("dealstaff",
						StringUtil.getStringValue(rs.getString("dealstaff")));
				return map;
			}
		});
		return list;
	}

	@SuppressWarnings("rawtypes")
	public int countQueryTerminalUserBindingList(String city_id,
			String start_time, String end_time, String specName,
			String operation, String hiddenVal, int curPage_splitPage,
			int num_splitPage) {
		logger.debug("TerminalUserBindingDAO==>countQueryTerminalUserBindingList()");
		StringBuffer sql = new StringBuffer();
		if ("1".equals(hiddenVal)) {
			sql.append("select count(1) from tab_gw_device a,tab_hgwcustomer b,bind_log c where a.device_id = b.device_id and b.device_id = c.device_id ");
		}
		if ("2".equals(hiddenVal)) {
			sql.append("select count(1) from tab_gw_device a,bind_log c where a.device_id = c.device_id ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if ("1".equals(hiddenVal)) {
			if (!StringUtil.IsEmpty(start_time)) {
				sql.append(" and b.dealdate>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time)) {
				sql.append(" and b.dealdate<=").append(end_time);
			}
		}
		if ("2".equals(hiddenVal)) {
			if (!StringUtil.IsEmpty(start_time)) {
				sql.append(" and a.complete_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time)) {
				sql.append(" and a.complete_time<=").append(end_time);
			}
		}
//		if (!StringUtil.IsEmpty(specName)) {
//			sql.append(" and d.gw_type='" + specName + "'");
//		}
		if (!StringUtil.IsEmpty(operation)) {
			sql.append(" and c.oper_type=").append(operation);
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
	public List<Map> excelModelQuery(String city_id, String start_time,
			String end_time, String specName, String operation) {
		logger.debug("TerminalUserBindingDAO==>excelQueryTerminalUserBindingList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,c.username,a.device_serialnumber,a.complete_time,c.oper_type,c.binddate,c.dealstaff " +
				"from tab_gw_device a,bind_log c where a.device_id = c.device_id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and a.complete_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and a.complete_time<=").append(end_time);
		}
//		if (!StringUtil.IsEmpty(specName)) {
//			sql.append(" and d.gw_type='" + specName + "'");
//		}
		if (!StringUtil.IsEmpty(operation)) {
			sql.append(" and c.oper_type=").append(operation);
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
				list.get(i).put(
						"device_serialnumber",
						StringUtil.getStringValue(list.get(i).get(
								"device_serialnumber")));
				list.get(i).put("username",
						StringUtil.getStringValue(list.get(i).get("username")));
				try {
					long complete_time = StringUtil.getLongValue(list.get(i)
							.get("complete_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(complete_time);
					list.get(i).put("complete_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					list.get(i).put("complete_time", "");
				} catch (Exception e) {
					list.get(i).put("complete_time", "");
				}
//				String gw_type = StringUtil.getStringValue(list.get(i).get(
//						"gw_type"));
//				if ("1".equals(gw_type)) {
//					list.get(i).put("gw_type", "家庭网关");
//				}
//				if ("2".equals(gw_type)) {
//					list.get(i).put("gw_type", "政企网关");
//				}
				String oper_type = StringUtil.getStringValue(list.get(i).get(
						"oper_type"));
				if ("1".equals(oper_type)) {
					list.get(i).put("oper_type", "有绑定操作");
				}
				if ("2".equals(oper_type)) {
					list.get(i).put("oper_type", "有解绑操作");
				}
				try {
					long binddate = StringUtil.getLongValue(list.get(i).get(
							"binddate")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(binddate);
					list.get(i).put("binddate", dt.getLongDate());
				} catch (NumberFormatException e) {
					list.get(i).put("binddate", "");
				} catch (Exception e) {
					list.get(i).put("binddate", "");
				}
				list.get(i)
						.put("dealstaff",
								StringUtil.getStringValue(list.get(i).get(
										"dealstaff")));
			}
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> excelUserQuery(String city_id, String start_time,
			String end_time, String specName, String operation) {
		logger.debug("TerminalUserBindingDAO==>excelQueryTerminalUserBindingList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,b.username,a.device_serialnumber,b.dealdate,c.oper_type,c.binddate,c.dealstaff from tab_gw_device a," +
				"tab_hgwcustomer b,bind_log c where a.device_id = b.device_id and b.device_id = c.device_id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and b.dealdate>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and b.dealdate<=").append(end_time);
		}
//		if (!StringUtil.IsEmpty(specName)) {
//			sql.append(" and d.gw_type='" + specName + "'");
//		}
		if (!StringUtil.IsEmpty(operation)) {
			sql.append(" and c.oper_type=").append(operation);
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
				list.get(i).put(
						"device_serialnumber",
						StringUtil.getStringValue(list.get(i).get(
								"device_serialnumber")));
				list.get(i).put("username",
						StringUtil.getStringValue(list.get(i).get("username")));
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
//				String gw_type = StringUtil.getStringValue(list.get(i).get(
//						"gw_type"));
//				if ("1".equals(gw_type)) {
//					list.get(i).put("gw_type", "家庭网关");
//				}
//				if ("2".equals(gw_type)) {
//					list.get(i).put("gw_type", "政企网关");
//				}
				String oper_type = StringUtil.getStringValue(list.get(i).get(
						"oper_type"));
				if ("1".equals(oper_type)) {
					list.get(i).put("oper_type", "有绑定操作");
				}
				if ("2".equals(oper_type)) {
					list.get(i).put("oper_type", "有解绑操作");
				}
				try {
					long binddate = StringUtil.getLongValue(list.get(i).get(
							"binddate")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(binddate);
					list.get(i).put("binddate", dt.getLongDate());
				} catch (NumberFormatException e) {
					list.get(i).put("binddate", "");
				} catch (Exception e) {
					list.get(i).put("binddate", "");
				}
				list.get(i)
						.put("dealstaff",
								StringUtil.getStringValue(list.get(i).get(
										"dealstaff")));
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
