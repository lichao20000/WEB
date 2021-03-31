package com.linkage.module.ids.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.commons.db.DBUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

public class AlarmQueryDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(AlarmQueryDAO.class);
	private Map<String, String> alarmMap = new HashMap<String, String>();

	public AlarmQueryDAO() {
		alarmMap.put("10", "紧急");
		alarmMap.put("20", "重要");
		alarmMap.put("30", "次要");
		alarmMap.put("11", "语音注册失败");
		alarmMap.put("22", "光路劣化");
		alarmMap.put("33", "PPPOE拨号失败");
		alarmMap.put("44", "终端环境");
		alarmMap.put("55", "网络质量");
		alarmMap.put("66", "网络综合质量");
		alarmMap.put("77", "光路劣化关联");
		alarmMap.put("1", "客户类");
		alarmMap.put("2", "网络类");
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getIdsarmInfoList(String alarmname, String alarmcode,
			String alarmlevel, String alarmobject, String hour, String count,
			String temperature, String timedelay, String lightpower,
			String packetloss, int curPage_splitPage, int num_splitPage) 
	{
		logger.debug("IdsAlarmInfoDAO=>getIdsarmInfoList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select id,alarm_name,alarm_code,alarm_level,alarm_period,alarm_count,"
				+ "rx_power,temperature,delay,loss_pp,send_sheet_obj from tab_ids_alarm_rule where 1=1 ");
		SQLAppend(alarmname, alarmcode, alarmlevel, alarmobject, hour, count,
				temperature, timedelay, lightpower, packetloss, sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("alarm_name", rs.getString("alarm_name"));
				map.put("alarm_code", rs.getString("alarm_code"));
				map.put("alarm_level", StringUtil.getStringValue(alarmMap
						.get(rs.getString("alarm_level"))));
				if (!StringUtil.IsEmpty(rs.getString("alarm_period"))) {
					map.put("alarm_period", String.valueOf(Integer.valueOf(rs
							.getString("alarm_period")) / 3600));
				}
				map.put("alarm_count", rs.getString("alarm_count"));
				map.put("rx_power", rs.getString("rx_power"));
				map.put("temperature", rs.getString("temperature"));
				map.put("delay", rs.getString("delay"));
				map.put("loss_pp", rs.getString("loss_pp"));
				map.put("send_sheet_obj", StringUtil.getStringValue(alarmMap
						.get(rs.getString("send_sheet_obj"))));
				return map;
			}
		});
		return list;
	}

	public int countIdsarmInfoList(String alarmname, String alarmcode,
			String alarmlevel, String alarmobject, String hour, String count,
			String temperature, String timedelay, String lightpower,
			String packetloss, int curPage_splitPage, int num_splitPage) 
	{
		logger.debug("IdsAlarmInfoDAO=>getIdsarmInfoList()");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select count(*) from tab_ids_alarm_rule where 1=1 ");
		}else{
			sql.append("select count(1) from tab_ids_alarm_rule where 1=1 ");
		}
		
		SQLAppend(alarmname, alarmcode, alarmlevel, alarmobject, hour, count,
				temperature, timedelay, lightpower, packetloss, sql);
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
	public List<Map> getIdsarmInfoListExcel(String alarmname, String alarmcode,
			String alarmlevel, String alarmobject, String hour, String count,
			String temperature, String timedelay, String lightpower,
			String packetloss) 
	{
		logger.debug("IdsAlarmInfoDAO=>getIdsarmInfoListExcel()");
		StringBuffer sql = new StringBuffer();
		sql.append("select alarm_name,alarm_code,alarm_level,alarm_period,alarm_count,rx_power,"
				+ "temperature,delay,loss_pp,send_sheet_obj from tab_ids_alarm_rule where 1=1 ");
		SQLAppend(alarmname, alarmcode, alarmlevel, alarmobject, hour, count,
				temperature, timedelay, lightpower, packetloss, sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put(
						"alarm_name",
						StringUtil
								.getStringValue(list.get(i).get("alarm_name")));
				list.get(i).put(
						"alarm_code",
						StringUtil
								.getStringValue(list.get(i).get("alarm_code")));
				list.get(i)
						.put("alarm_level",
								StringUtil.getStringValue(alarmMap
										.get(StringUtil.getStringValue(list
												.get(i).get("alarm_level")))));
				if (!StringUtil.IsEmpty(StringUtil.getStringValue(list.get(i)
						.get("alarm_period")))) {
					list.get(i).put(
							"alarm_period",
							String.valueOf(Integer.valueOf(StringUtil
									.getStringValue(list.get(i).get(
											"alarm_period"))) / 3600));
				}
				list.get(i).put(
						"alarm_count",
						StringUtil.getStringValue(list.get(i)
								.get("alarm_count")));
				list.get(i).put("rx_power",
						StringUtil.getStringValue(list.get(i).get("rx_power")));
				list.get(i).put(
						"temperature",
						StringUtil.getStringValue(list.get(i)
								.get("temperature")));
				list.get(i).put("delay",
						StringUtil.getStringValue(list.get(i).get("delay")));
				list.get(i).put("loss_pp",
						StringUtil.getStringValue(list.get(i).get("loss_pp")));
				list.get(i).put(
						"send_sheet_obj",
						StringUtil.getStringValue(alarmMap.get(StringUtil
								.getStringValue(list.get(i).get(
										"send_sheet_obj")))));
			}
		}
		return list;
	}

	private void SQLAppend(String alarmname, String alarmcode,
			String alarmlevel, String alarmobject, String hour, String count,
			String temperature, String timedelay, String lightpower,
			String packetloss, StringBuffer sql) 
	{
		if (!StringUtil.IsEmpty(alarmname)) {
			alarmname = alarmMap.get(alarmname);
			sql.append(" and alarm_name=" + "'").append(alarmname).append("'");
		}
		if (!StringUtil.IsEmpty(alarmcode)) {
			sql.append(" and alarm_code=" + "'").append(alarmcode).append("'");
		}
		if (!StringUtil.IsEmpty(alarmlevel)) {
			sql.append(" and alarm_level=").append(alarmlevel);
		}
		if (!StringUtil.IsEmpty(alarmobject)) {
			sql.append(" and send_sheet_obj=" + "'").append(alarmobject)
					.append("'");
		}
		if (!StringUtil.IsEmpty(hour)) {
			sql.append(" and alarm_period=").append(
					String.valueOf(Integer.valueOf(hour) * 3600));
		}
		if (!StringUtil.IsEmpty(count)) {
			sql.append(" and alarm_count=").append(count);
		}
		if (!StringUtil.IsEmpty(temperature)) {
			sql.append(" and temperature>=").append(temperature);
		}
		if (!StringUtil.IsEmpty(timedelay)) {
			sql.append(" and delay>=").append(timedelay);
		}
		if (!StringUtil.IsEmpty(lightpower)) {
			sql.append(" and rx_power<=").append(lightpower);
		}
		if (!StringUtil.IsEmpty(packetloss)) {
			sql.append(" and loss_pp>=").append(packetloss);
		}
	}

	public void insert(String alarmname, String alarmcode, String alarmlevel,
			String alarmobject, String hour, String count, String temperature,
			String timedelay, String lightpower, String packetloss) 
	{
		PrepareSQL psql = new PrepareSQL("select max(id) from tab_ids_alarm_rule ");
		int id = jt.queryForInt(psql.getSQL());
		
		String sql = "insert into tab_ids_alarm_rule "
				+ "(id,alarm_name, alarm_code, alarm_level, alarm_period, " +
				"alarm_count, rx_power, temperature, delay, loss_pp, send_sheet_obj) "
				+ "values " + "(?,?,?,?,?,?,?,?,?,?,?)";
		PrepareSQL pSQL = new PrepareSQL(sql);
		int index = 0;
		pSQL.setInt(++index, (id + 1));
		pSQL.setString(++index, alarmMap.get(alarmname));
		pSQL.setString(++index, alarmcode);
		pSQL.setInt(++index, Integer.parseInt(alarmlevel));
		if (StringUtil.IsEmpty(hour)) {
			pSQL.setString(++index, null);
		} else {
			pSQL.setInt(++index, Integer.parseInt(hour) * 3600);
		}
		if (StringUtil.IsEmpty(count)) {
			pSQL.setString(++index, null);
		} else {
			pSQL.setInt(++index, Integer.parseInt(count));
		}
		if (StringUtil.IsEmpty(lightpower)) {
			pSQL.setString(++index, null);
		} else {
			pSQL.setInt(++index, Integer.parseInt(lightpower));
		}
		if (StringUtil.IsEmpty(temperature)) {
			pSQL.setString(++index, null);
		} else {
			pSQL.setInt(++index, Integer.parseInt(temperature));
		}
		if (StringUtil.IsEmpty(timedelay)) {
			pSQL.setString(++index, null);
		} else {
			pSQL.setInt(++index, Integer.parseInt(timedelay));
		}
		if (StringUtil.IsEmpty(packetloss)) {
			pSQL.setString(++index, null);
		} else {
			pSQL.setInt(++index, Integer.parseInt(packetloss));
		}
		pSQL.setString(++index, alarmobject);
		jt.execute(pSQL.getSQL());
	}

	public void delete(String id) 
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("delete from tab_ids_alarm_rule where id=" + id);
		jt.execute(sql.getSQL());
	}

	public void update(String id, String alarmname, String alarmcode,
			String alarmlevel, String alarmobject, String hour, String count,
			String temperature, String timedelay, String lightpower,
			String packetloss) 
	{
		String sql = "update tab_ids_alarm_rule set alarm_name=?,alarm_code=?," +
				"alarm_level=?,alarm_period=?,alarm_count=?,rx_power=?"
				+ ",temperature=?,delay=?,loss_pp=?,send_sheet_obj=? where id="+ id;
		PrepareSQL pSQL = new PrepareSQL(sql);
		int index = 0;
		pSQL.setString(++index, alarmMap.get(alarmname));
		pSQL.setString(++index, alarmcode);
		pSQL.setInt(++index, Integer.parseInt(alarmlevel));
		if (StringUtil.IsEmpty(hour)) {
			pSQL.setString(++index, null);
		} else {
			pSQL.setInt(++index, Integer.parseInt(hour) * 3600);
		}
		if (StringUtil.IsEmpty(count)) {
			pSQL.setString(++index, null);
		} else {
			pSQL.setInt(++index, Integer.parseInt(count));
		}
		if (StringUtil.IsEmpty(lightpower)) {
			pSQL.setString(++index, null);
		} else {
			pSQL.setInt(++index, Integer.parseInt(lightpower));
		}
		if (StringUtil.IsEmpty(temperature)) {
			pSQL.setString(++index, null);
		} else {
			pSQL.setInt(++index, Integer.parseInt(temperature));
		}
		if (StringUtil.IsEmpty(timedelay)) {
			pSQL.setString(++index, null);
		} else {
			pSQL.setInt(++index, Integer.parseInt(timedelay));
		}
		if (StringUtil.IsEmpty(packetloss)) {
			pSQL.setString(++index, null);
		} else {
			pSQL.setInt(++index, Integer.parseInt(packetloss));
		}
		pSQL.setString(++index, alarmobject);
		jt.execute(pSQL.getSQL());
	}
}
