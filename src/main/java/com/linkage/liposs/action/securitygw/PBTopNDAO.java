package com.linkage.liposs.action.securitygw;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.liposs.buss.util.CommonUtil;
import com.linkage.liposs.buss.util.JdbcTemplateExtend;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;

/**
 * SDA的小时报表的统计功能
 *
 * @author Zhaof suixz(5253)
 * @version 1.0
 * @since 2007-09-25
 * @category SDA
 */
public class PBTopNDAO {
	private static Logger log= LoggerFactory.getLogger(PBTopNDAO.class);
	private JdbcTemplate jt;
	private JdbcTemplateExtend spjt;
	public DataSource dao;

	/**
	 * 设置数据源
	 *
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		this.dao = dao;
		jt = new JdbcTemplate(dao);
		spjt = new JdbcTemplateExtend(dao);
	}

	/**
	 * 通过条件获取网络连接情况原始表详细数据的查询SQL语句
	 *
	 * @param deviceid
	 *            设备id
	 * @param tip
	 *            目标用户IP
	 * @param sip
	 *            源用户IP
	 * @param tp
	 *            目标端口
	 * @param sp
	 *            源端口
	 * @param tm
	 *            目标Mac
	 * @param sm
	 *            源Mac
	 * @param type
	 *            协议类型
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return sql
	 */
	public String getConnOriginalDataSQL(String deviceid, String tip,
			String sip, String tp, String sp, String tm, String sm,
			String type, String start, String end) {

		String sql = "";
		long dayIntrval = 3600000 * 24;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date sd = df.parse(start, new ParsePosition(0));
		Date ed = df.parse(end, new ParsePosition(0));
		long startTime = sd.getTime();
		long endTime = ed.getTime();

		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(startTime);
		int startYear = cale.get(Calendar.YEAR);
		int startMonth = cale.get(Calendar.MONTH) + 1;
		int startDay = cale.get(Calendar.DATE);
		cale.setTimeInMillis(endTime);
		int endYear = cale.get(Calendar.YEAR);
		int endMonth = cale.get(Calendar.MONTH) + 1;
		int endDay = cale.get(Calendar.DATE);
		String smonth = (startMonth < 10) ? ("0" + startMonth)
				: (startMonth + "");
		String emonth = (endMonth < 10) ? ("0" + endMonth) : (endMonth + "");
		String sday = (startDay < 10) ? ("0" + startDay) : (startDay + "");
		String eday = (endDay < 10) ? ("0" + endDay) : (endDay + "");
		String tyear = "", tmonth = "", tday = "";
		String tempTableName = "";
		boolean isStart = true;
		Map tableNamemap = CommonUtil.getExistTables("sgw_conn_original_");
		if (tableNamemap == null) {
			return sql;
		}
		// 生成查询条件
		String sql_ = "";
		if (deviceid != null && !deviceid.equals("")) {
			sql_ += " and a.deviceid='" + deviceid + "'";
		}
		if (tip != null && !tip.equals("")) {
			sql_ += " and a.targetip='" + tip + "'";
		}
		if (sip != null && !sip.equals("")) {
			sql_ += " and a.srcip='" + sip + "'";
		}
		if (tp != null && !tp.equals("")) {
			sql_ += " and a.targetport=" + tp;
		}
		if (sp != null && !sp.equals("")) {
			sql_ += " and a.srcport=" + sp;
		}
		if (tm != null && !tm.equals("")) {
			sql_ += " and a.targetmac='" + tm + "'";
		}
		if (sm != null && !sm.equals("")) {
			sql_ += " and a.srcmac='" + sm + "'";
		}
		if (type != null && !type.equals("")) {
			sql_ += " and a.protocoltype=" + type;
		}

		// 所需查询的字段名称
		String selectName = "a.deviceid,"
			+ "(case when b.domainname is null then a.targetip else b.domainname end) as targetip,"
			+ "a.srcip,a.targetport,a.srcport,a.targetmac,a.srcmac,a.protocoltype,a.times,a.flux,a.stime,a.etime";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			selectName = "a.deviceid,"
					+ "(case when (b.domainname is null or b.domainname = '') then a.targetip else b.domainname end) as targetip,"
					+ "a.srcip,a.targetport,a.srcport,a.targetmac,a.srcmac,a.protocoltype,a.times,a.flux,a.stime,a.etime";
		}

		if (startYear == endYear && startMonth == endMonth
				&& startDay == endDay) {
			tempTableName = "sgw_conn_original_" + startYear + "_" + smonth
					+ "_" + sday;
			if (tableNamemap.containsKey(tempTableName)) {
				sql += "select " + selectName + " from " + tempTableName
					+ " a left join sgw_ip2domain b"
					+ " on a.targetip=b.ip";
				sql += " where a.stime>=" + startTime / 1000 + " and a.etime<="
						+ endTime / 1000;
				sql += sql_;
			}
		} else {
			for (long temp = startTime; temp <= endTime; temp += dayIntrval) {
				cale.setTimeInMillis(temp);
				int tempYear = cale.get(Calendar.YEAR);
				int tempMonth = cale.get(Calendar.MONTH) + 1;
				int tempDay = cale.get(Calendar.DATE);
				if (tempYear == startYear && tempMonth == startMonth
						&& tempDay == startDay) {
					tempTableName = "sgw_conn_original_" + startYear + "_"
							+ smonth + "_" + sday;
					if (tableNamemap.containsKey(tempTableName)) {
						sql += (isStart) ? "" : " union all ";
						sql += "select " + selectName + " from " + tempTableName
							+ " a left join sgw_ip2domain b"
							+ " on a.targetip=b.ip";
						sql += " where a.stime>=" + startTime / 1000;
						sql += sql_;
						isStart = false;
					}
				} else if (tempYear == endYear && tempMonth == endMonth
						&& tempDay == endDay) {
					tempTableName = "sgw_conn_original_" + endYear + "_"
							+ emonth + "_" + eday;
					if (tableNamemap.containsKey(tempTableName)) {
						sql += (isStart) ? "" : " union all ";
						sql += " select " + selectName + " from " + tempTableName
							+ " a left join sgw_ip2domain b"
							+ " on a.targetip=b.ip";
						sql += " where a.etime<=" + endTime / 1000;
						sql += sql_;
						isStart = false;
					}
				} else {
					tyear = (tempYear < 10) ? ("0" + tempYear)
							: (tempYear + "");
					tmonth = (tempMonth < 10) ? ("0" + tempMonth)
							: (tempMonth + "");
					tday = (tempDay < 10) ? ("0" + tempDay) : (tempDay + "");
					tempTableName = "sgw_conn_original_" + tyear + "_" + tmonth
							+ "_" + tday;
					if (tableNamemap.containsKey(tempTableName)) {
						sql += (isStart) ? "" : " union all ";
						sql += "  select " + selectName + " from " + tempTableName
							+ " a left join sgw_ip2domain b"
							+ " on a.targetip=b.ip";
						sql += " where 1=1";
						sql += sql_;
						isStart = false;
					}
				}
			}
		}
		PrepareSQL psql = new PrepareSQL(sql);
		return psql.getSQL();
	}

	public List<Map> getConnOriginalData(String sql, int curPage, int rowCount) {
		spjt.setMaxRows(10000); // 设置需要查询数据的总数
		spjt.setFetchSize(100);// 设置一次查询数据的数量
		return ("".equals(sql)) ? null : spjt.querySP(sql, (curPage - 1)
				* rowCount, rowCount, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map m = new HashMap();
				m.put("deviceid", rs.getString("deviceid"));
				m.put("tip", rs.getObject("targetip"));
				m.put("sip", rs.getString("srcip"));
				m.put("tp", rs.getString("targetport"));
				m.put("sp", rs.getString("srcport"));
				m.put("tm", rs.getString("targetmac"));
				m.put("sm", rs.getString("srcmac"));
				m.put("type", rs.getInt("protocoltype"));
				m.put("times", rs.getString("times"));
				m.put("flux", rs.getLong("flux"));
				m.put("stime", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", rs
						.getLong("stime")));
				m.put("etime", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", rs
						.getLong("etime")));
				return m;
			}
		});
	}

	/**
	 * 计算结果集总条数
	 *
	 * @param sql
	 * @return
	 */
	public int getCountForOriginalData(String sql) {
		if (sql == null || sql.equals("")) {
			return 0;
		}
		String sqlCount = "select count(*) as c from(" + sql + ") a";
		PrepareSQL psql = new PrepareSQL(sqlCount);
		Map count = jt.queryForMap(psql.getSQL());
		return Integer.parseInt(count.get("c").toString());
	}

	/**
	 * 根据时间获得查询表的sql信息
	 *
	 * @param list
	 * @param tableNamemap
	 * @param deviceId
	 * @return
	 */
	private String getTablesSql(List list, Map tableNamemap, String deviceId,
			String tableType) {
		String sql = "";
		boolean first = true;
		for (int i = 0; i < list.size(); i++) {
			String[] ss = (String[]) list.get(i);
			String tableName = tableType + ss[0];
			if (!tableNamemap.containsKey(tableName))
				continue;
			sql += first ? "" : " union all ";
			first = false;

			sql += "select srcip, sum(times) times from " + tableName
					+ " where";
			sql += " stime>=" + ss[1] + " and etime<=" + ss[2] + " and";
			sql += " deviceid='" + deviceId + "' group by srcip";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		return psql.getSQL();
	}

	/**
	 * 获取上网行为TopN日报表数据
	 *
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param topN
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public List<Map> getTopNDayStatic(String deviceId, String date, int topN,
			int type) {
		// 查询基础数据
		log.debug("PBTopNDAO getTopNDayStatic topN=" + topN + " and type="
				+ type);
		int totaltimes = 1;
		String sql = "", tableType = "sgw_conn_day_";
		// 查询topN数据
		String sql_ = " select  sum(times)  from (";
		if (type == 1) {
			tableType = "sgw_conn_hour_";
		}
		Map tableNamemap = CommonUtil.getExistTables(tableType);

		List<String[]> list = getTime(date, type);
		//log.debug(list);
		String tables = getTablesSql(list, tableNamemap, deviceId, tableType);

		if ("".equals(tables)) {
			return new ArrayList<Map>();
		}
		sql_ += tables;
		sql_ += ") t";
		log.debug("getTopN-DAYsql_gettimes=" + sql_);

		totaltimes = jt.queryForInt(sql_);
		sql += "select srcip, times, round(times/"
				+ totaltimes
				+ ",4)*100 precent from (select srcip,  sum(times) times  from (";
		sql += tables;
		sql += ") t  group by srcip ) s";
		sql += " group by srcip order by times desc";

		log.debug("getTopN-DAYsql=" + sql);

		List<Map> result = new ArrayList();
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			result = jt.queryForList(psql.getSQL());
		} catch (Exception ex) {
			log.debug(ex.getMessage());
//			log.debug(ex.getStackTrace());
//			log.debug(ex.getCause());
		}
		if(result == null||result.size() == 0){
			return null;
		}
		if(result.size() < topN){
			topN=result.size();
		}
		return result.subList(0, topN);
	}

	/**
	 * 获取上网行为TopN日报表数据
	 *
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param topN
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public List<Map> getTopNDay(String deviceId, String date, int topN) {
		// 查询基础数据
		String sql = "";
		// 查询topN数据
		String sql_ = "";
		Map tableNamemap = CommonUtil.getExistTables("sgw_conn_hour_");
		boolean first = true;
		List<String[]> list = getTime(date, 1);
		sql_ += "select srcip, sum(times) times from (";
		sql += "select srcip, protocoltype ,proname as yType, sum(times) times from (";
		for (int i = 0; i < list.size(); i++) {
			String[] ss = list.get(i);
			String tableName = "sgw_conn_hour_" + ss[0];
			if (!tableNamemap.containsKey(tableName))
				continue;
			sql += first ? "" : " union all ";
			first = false;

			sql += "select srcip, (case when protocoltype in(select protype from sgw_protocol_info where type=1) then protocoltype else -1 end) as protocoltype, sum(times) times from "
					+ tableName + " where";
			sql += " stime>=" + ss[1] + " and etime<=" + ss[2] + " and";
			sql += " deviceid='" + deviceId + "' group by srcip, protocoltype";
		}
		if (first) {
			return new ArrayList<Map>();
		}
		sql += ") t";
		sql += " left join sgw_protocol_info on t.protocoltype=protype  group by srcip, protocoltype,proname";
		sql_ += sql + ") tt group by srcip order by times desc";
		sql += " order by times";
		log.debug("======================getTopN-sql=" + sql);
		log.debug("===========***********getTopN_sql=" + sql_);
		// log.debug("[......]getTopN-sql_top=" + sql_);
		List<Map> virusHours = null;
		List<Map> rowTotal = null;
		List<Map> result = new ArrayList();
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			PrepareSQL psql2 = new PrepareSQL(sql_);
			virusHours = jt.queryForList(psql.getSQL());
			// 根据上网次数总和排序、根据srcip分组的结果
			rowTotal = jt.queryForList(psql2.getSQL());
			if (rowTotal != null) {
				if (rowTotal.size() > topN) {
					// 获取前topN的结果
					rowTotal = rowTotal.subList(0, topN);
				}
				for (Map m : rowTotal) {
					String ip = (String) m.get("srcip");
					int flag = 0;
					for (Map mm : virusHours) {
						if (((String) mm.get("srcip")).equals(ip)) {
							result.add(mm);
						}
					}
				}
			}
		} catch (Exception ex) {
			log.debug(ex.getMessage());
//			log.debug(ex.getStackTrace());
//			log.debug(ex.getCause());
		}
		return result;
	}

	/**
	 * 根据时间区间获取网络访问数据
	 *
	 * @param deviceId
	 * @param date
	 * @param topN
	 * @return
	 */
	public List<Map> getAdvNetBehaviorData(String deviceId, String stime,
			String etime, int topN) {
		// 查询基础数据
		String sql = "";
		// 查询topN数据
		String sql_ = "";
		String tableType = "sgw_conn_day_";
		if (stime.equals(etime)) {
			tableType = "sgw_conn_hour_";
		}
		Map tableNamemap = CommonUtil.getExistTables(tableType);
		boolean first = true;
		List<String[]> list = getSETimeList(stime, etime);
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql_ += "select srcip, count(*) total, sum(times) times from (";
		}
		else {
			sql_ += "select srcip, count(1) total, sum(times) times from (";
		}
		sql += "select srcip, protocoltype yType, sum(times) times from (";
		for (int i = 0; i < list.size(); i++) {
			String[] ss = list.get(i);
			String tableName = tableType + ss[0];
			if (!tableNamemap.containsKey(tableName))
				continue;
			sql += first ? "" : " union all ";
			first = false;

			sql += "select srcip, protocoltype, sum(times) times from "
					+ tableName + " where";
			sql += " stime>=" + ss[1] + " and etime<=" + ss[2] + " and";
			sql += " deviceid='" + deviceId + "' group by srcip, protocoltype";
		}
		if (first) {
			return new ArrayList<Map>();
		}
		sql += ") t";
		sql += " group by srcip, protocoltype";
		sql_ += sql + ") tt group by srcip order by times desc";
		sql += " order by srcip";
		log.debug("[......]getTopN-sql=" + sql);
		// log.debug("[......]getTopN-sql_top=" + sql_);
		List<Map> virusHours = null;
		List<Map> rowTotal = null;
		List<Map> result = new ArrayList();
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			PrepareSQL psql2 = new PrepareSQL(sql_);
			virusHours = jt.queryForList(psql.getSQL());
			// 根据上网次数总和排序、根据srcip分组的结果
			rowTotal = jt.queryForList(psql2.getSQL());
			if (rowTotal != null) {
				if (rowTotal.size() > topN) {
					// 获取前topN的结果
					rowTotal = rowTotal.subList(0, topN);
				}
				for (Map m : rowTotal) {
					String ip = (String) m.get("srcip");
					int flag = 0;
					for (Map mm : virusHours) {
						if (((String) mm.get("srcip")).equals(ip)) {
							flag = 1;
							result.add(mm);
						} else {
							if (flag == 1) {
								flag = 0;
								break;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			log.debug(ex.getMessage());
//			log.debug(ex.getStackTrace());
//			log.debug(ex.getCause());
		}
		return result;
	}

	/**
	 * 获取上网行为TopN周报表数据
	 *
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param topN
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public List<Map> getTopNWeek(String deviceId, String date, int topN) {
		String sql = "";
		String sql_ = "";
		Map tableNamemap = CommonUtil.getExistTables("sgw_conn_day_");
		boolean first = true;
		List<String[]> list = getTime(date, 7);
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql_ += "select srcip, count(*) total, sum(times) times from (";
		}
		else {
			sql_ += "select srcip, count(1) total, sum(times) times from (";
		}
		sql += "select srcip, protocoltype ,proname as yType, sum(times) times from (";
		for (int i = 0; i < list.size(); i++) {
			String[] ss = list.get(i);
			String tableName = "sgw_conn_day_" + ss[0];
			if (!tableNamemap.containsKey(tableName))
				continue;
			sql += first ? "" : " union all ";
			first = false;

			sql += "select srcip, (case when protocoltype in(select protype from sgw_protocol_info where type=1) then protocoltype else -1 end) as protocoltype, sum(times) times from "
					+ tableName + " where";
			sql += " stime>=" + ss[1] + " and etime<=" + ss[2] + " and";
			sql += " deviceid='" + deviceId + "' group by srcip, protocoltype";
		}
		if (first) {
			return new ArrayList<Map>();
		}
		sql += ") t";
		sql += " left join sgw_protocol_info on t.protocoltype=protype  group by srcip, protocoltype,proname";
		sql_ += sql + ") tt group by srcip order by times desc";
		sql += " order by times";
		log.debug("[......]getTopNWeek-sql=" + sql);
		// log.debug("[......]getTopNWeek-sql_total=" + sql_);
		List<Map> virusHours = null;
		List<Map> rowTotal = null;
		List<Map> result = new ArrayList();
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			PrepareSQL psql2 = new PrepareSQL(sql_);
			virusHours = jt.queryForList(psql.getSQL());
			// 根据上网次数总和排序、根据srcip分组的结果
			rowTotal = jt.queryForList(psql2.getSQL());
			if (rowTotal != null) {
				if (rowTotal.size() > topN) {
					// 获取前topN的结果
					rowTotal = rowTotal.subList(0, topN);
				}
				for (Map m : rowTotal) {
					String ip = (String) m.get("srcip");
					for (Map mm : virusHours) {
						if (((String) mm.get("srcip")).equals(ip)) {
							result.add(mm);
						}
					}
				}
			}
		} catch (Exception ex) {
			log.debug(ex.getMessage());
//			log.debug(ex.getStackTrace());
//			log.debug(ex.getCause());
		}
		return result;
	}

	/**
	 * 获取上网行为TopN月报表数据
	 *
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param topN
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public List<Map> getTopNMonth(String deviceId, String date, int topN) {
		String sql = "";
		String sql_ = "";
		Map tableNamemap = CommonUtil.getExistTables("sgw_conn_day_");
		boolean first = true;
		List<String[]> list = getTime(date, 30);
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql_ += "select srcip, count(*) total, sum(times) times from (";
		}
		else {
			sql_ += "select srcip, count(1) total, sum(times) times from (";
		}
		sql += "select srcip, (case when protocoltype in(select protype from sgw_protocol_info where type=1) then protocoltype else -1 end) as protocoltype, sum(times) times from (";
		for (int i = 0; i < list.size(); i++) {
			String[] ss = list.get(i);
			String tableName = "sgw_conn_day_" + ss[0];
			if (!tableNamemap.containsKey(tableName))
				continue;
			sql += first ? "" : " union all ";
			first = false;

			sql += "select srcip, protocoltype, sum(times) times from "
					+ tableName + " where";
			sql += " stime>=" + ss[1] + " and etime<=" + ss[2] + " and";
			sql += " deviceid='" + deviceId + "' group by srcip, protocoltype";
		}
		if (first) {
			return new ArrayList<Map>();
		}
		sql += ") t";
		sql += " left join sgw_protocol_info on t.protocoltype=protype  group by srcip, protocoltype,proname";
		sql_ += sql + ") tt group by srcip order by times desc";
		sql += " order by times";
		log.debug("[......]getTopNMonth-sql=" + sql);
		// log.debug("[......]getTopNMonth-sql_total=" + sql_);
		List<Map> virusHours = null;
		List<Map> rowTotal = null;
		List<Map> result = new ArrayList();
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			PrepareSQL psql2 = new PrepareSQL(sql_);
			virusHours = jt.queryForList(psql.getSQL());
			// 根据上网次数总和排序、根据srcip分组的结果
			rowTotal = jt.queryForList(psql2.getSQL());
			if (rowTotal != null) {
				if (rowTotal.size() > topN) {
					// 获取前topN的结果
					rowTotal = rowTotal.subList(0, topN);
				}
				for (Map m : rowTotal) {
					String ip = (String) m.get("srcip");
					for (Map mm : virusHours) {
						if (((String) mm.get("srcip")).equals(ip)) {
							result.add(mm);
						}
					}
				}
			}
		} catch (Exception ex) {
			log.debug(ex.getMessage());
//			log.debug(ex.getStackTrace());
//			log.debug(ex.getCause());
		}
		return result;
	}

	/**
	 * 获取WebTopN日报表数据
	 *
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param topN
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public List<Map> getWebTopNDay(String deviceId, String date, int topN) {
		String sql = "";
		Map tableNamemap = CommonUtil.getExistTables("sgw_conn_hour_");
		boolean first = true;
		List<String[]> list = getTime(date, 1);
		sql += "select srcip, sum(times) times, 'http' yType from (";
		for (int i = 0; i < list.size(); i++) {
			String[] ss = list.get(i);
			String tableName = "sgw_conn_hour_" + ss[0];
			if (!tableNamemap.containsKey(tableName))
				continue;
			sql += first ? "" : " union all ";
			first = false;

			sql += "select srcip, sum(times) times from " + tableName
					+ " where";
			sql += " stime>=" + ss[1] + " and etime<=" + ss[2] + " and";
			sql += " deviceid='" + deviceId
					+ "' and protocoltype=0 group by srcip";
		}
		if (first) {
			return new ArrayList<Map>();
		}
		sql += ") t";
		sql += " group by srcip order by times desc";
		log.debug("[......]getWebTopNDAY-sql=" + sql);
		List<Map> virusHours = null;
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			virusHours = jt.queryForList(psql.getSQL());
//			if (virusHours != null && virusHours.size() > topN) {
//				return virusHours.subList(0, topN);
//			}
		} catch (Exception ex) {
			log.debug(ex.getMessage());
		}
		return virusHours;
	}

	/**
	 * 获取WebTopN周报表数据
	 *
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param topN
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public List<Map> getWebTopNWeek(String deviceId, String date, int topN) {
		String sql = "";
		Map tableNamemap = CommonUtil.getExistTables("sgw_conn_day_");
		boolean first = true;
		List<String[]> list = getTime(date, 7);
		sql += "select srcip, sum(times) times, 'http' yType from (";
		for (int i = 0; i < list.size(); i++) {
			String[] ss = list.get(i);
			String tableName = "sgw_conn_day_" + ss[0];
			if (!tableNamemap.containsKey(tableName))
				continue;
			sql += first ? "" : " union all ";
			first = false;

			sql += "select srcip, sum(times) times from " + tableName
					+ " where";
			sql += " stime>=" + ss[1] + " and etime<=" + ss[2] + " and";
			sql += " deviceid='" + deviceId
					+ "' and protocoltype=0 group by srcip";
		}
		if (first) {
			return new ArrayList<Map>();
		}
		sql += ") t";
		sql += " group by srcip order by times desc";
		log.debug("[......]getWebTopNWeek-sql=" + sql);
		List<Map> virusHours = null;
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			virusHours = jt.queryForList(psql.getSQL());
//			if (virusHours != null && virusHours.size() > topN) {
//				return virusHours.subList(0, topN);
//			}
		} catch (Exception ex) {
			log.debug(ex.getMessage());
		}
		return virusHours;
	}

	/**
	 * 获取WebTopN月报表数据
	 *
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param topN
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public List<Map> getWebTopNMonth(String deviceId, String date, int topN) {
		String sql = "";
		Map tableNamemap = CommonUtil.getExistTables("sgw_conn_day_");
		boolean first = true;
		List<String[]> list = getTime(date, 30);
		sql += "select srcip, sum(times) times, 'http' yType from (";
		for (int i = 0; i < list.size(); i++) {
			String[] ss = list.get(i);
			String tableName = "sgw_conn_day_" + ss[0];
			if (!tableNamemap.containsKey(tableName))
				continue;
			sql += first ? "" : " union all ";
			first = false;

			sql += "select srcip, sum(times) times from " + tableName
					+ " where";
			sql += " stime>=" + ss[1] + " and etime<=" + ss[2] + " and";
			sql += " deviceid='" + deviceId
					+ "' and protocoltype=0 group by srcip";
		}
		if (first) {
			return new ArrayList<Map>();
		}
		sql += ") t";
		sql += " group by srcip order by times desc";
		log.debug("[......]getWebTopNMonth-sql=" + sql);
		List<Map> virusHours = null;
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			virusHours = jt.queryForList(psql.getSQL());
//			if (virusHours != null && virusHours.size() > topN) {
//				return virusHours.subList(0, topN);
//			}
		} catch (Exception ex) {
			log.debug(ex.getMessage());
		}
		return virusHours;
	}

	/**
	 * 获取MailTopN日报表数据
	 *
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param topN
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public List<Map> getMailTopNDay(String deviceId, String date, int topN) {
		String sql = "";
		Map tableNamemap = CommonUtil.getExistTables("sgw_conn_hour_");
		boolean first = true;
		List<String[]> list = getTime(date, 1);
		sql += "select srcip, sum(times) times, 'smtp,pop3' yType from (";
		for (int i = 0; i < list.size(); i++) {
			String[] ss = list.get(i);
			String tableName = "sgw_conn_hour_" + ss[0];
			if (!tableNamemap.containsKey(tableName))
				continue;
			sql += first ? "" : " union all ";
			first = false;

			sql += "select srcip, sum(times) times from " + tableName
					+ " where";
			sql += " stime>=" + ss[1] + " and etime<=" + ss[2] + " and";
			sql += " deviceid='" + deviceId
					+ "' and (protocoltype=2 or protocoltype=3) group by srcip";
		}
		if (first) {
			return new ArrayList<Map>();
		}
		sql += ") t";
		sql += " group by srcip order by times desc";
		log.debug("[......]getMailTopNDAY-sql=" + sql);
		List<Map> virusHours = null;
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			virusHours = jt.queryForList(psql.getSQL());
//			if (virusHours != null && virusHours.size() > topN) {
//				return virusHours.subList(0, topN);
//			}
		} catch (Exception ex) {
			log.debug(ex.getMessage());
		}
		return virusHours;
	}

	/**
	 * 获取MailTopN周报表数据
	 *
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param topN
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public List<Map> getMailTopNWeek(String deviceId, String date, int topN) {
		String sql = "";
		Map tableNamemap = CommonUtil.getExistTables("sgw_conn_day_");
		boolean first = true;
		List<String[]> list = getTime(date, 7);
		sql += "select srcip, sum(times) times, 'smtp,pop3' yType from (";
		for (int i = 0; i < list.size(); i++) {
			String[] ss = list.get(i);
			String tableName = "sgw_conn_day_" + ss[0];
			if (!tableNamemap.containsKey(tableName))
				continue;
			sql += first ? "" : " union all ";
			first = false;

			sql += "select srcip, sum(times) times from " + tableName
					+ " where";
			sql += " stime>=" + ss[1] + " and etime<=" + ss[2] + " and";
			sql += " deviceid='" + deviceId
					+ "' and (protocoltype=2 or protocoltype=3) group by srcip";
		}
		if (first) {
			return new ArrayList<Map>();
		}
		sql += ") t";
		sql += " group by srcip order by times desc";
		log.debug("[......]getMailTopNWeek-sql=" + sql);
		List<Map> virusHours = null;
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			virusHours = jt.queryForList(psql.getSQL());
//			if (virusHours != null && virusHours.size() > topN) {
//				return virusHours.subList(0, topN);
//			}
		} catch (Exception ex) {
			log.debug(ex.getMessage());
		}
		return virusHours;
	}

	/**
	 * 获取MailTopN月报表数据
	 *
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param topN
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public List<Map> getMailTopNMonth(String deviceId, String date, int topN) {
		String sql = "";
		Map tableNamemap = CommonUtil.getExistTables("sgw_conn_day_");
		boolean first = true;
		List<String[]> list = getTime(date, 30);
		sql += "select srcip, sum(times) times, 'smtp,pop3' yType from (";
		for (int i = 0; i < list.size(); i++) {
			String[] ss = list.get(i);
			String tableName = "sgw_conn_day_" + ss[0];
			if (!tableNamemap.containsKey(tableName))
				continue;
			sql += first ? "" : " union all ";
			first = false;

			sql += "select srcip, sum(times) times from " + tableName
					+ " where";
			sql += " stime>=" + ss[1] + " and etime<=" + ss[2] + " and";
			sql += " deviceid='" + deviceId
					+ "' and (protocoltype=2 or protocoltype=3) group by srcip";
		}
		if (first) {
			return new ArrayList<Map>();
		}
		sql += ") t";
		sql += " group by srcip order by times desc";
		log.debug("[......]getMailTopNMonth-sql=" + sql);
		List<Map> virusHours = null;
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			virusHours = jt.queryForList(psql.getSQL());
//			if (virusHours != null && virusHours.size() > topN) {
//				return virusHours.subList(0, topN);
//			}
		} catch (Exception ex) {
			log.debug(ex.getMessage());
		}
		return virusHours;
	}

	/**
	 * 获取协议类型表数据
	 *
	 * @return
	 */
	public List<Map> getProtTypes() {
		String sql = "select * from sgw_protocol_info where type=1";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select protype, proname from sgw_protocol_info where type=1";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		if (list == null) {
			list = new ArrayList();
		}
		log.debug("---------prot_types=" + list);
		return list;
	}

	/**
	 * 根据当前时间计算要查询的表名和起始时间
	 *
	 * @param start
	 *            毫秒数
	 * @param step
	 * @return
	 */
	public List<String[]> getTime(long start, int step) {
		return getTime(new SimpleDateFormat("yyyy_MM_dd")
				.format(new Date(start)), step);
	}

	/**
	 * 根据当前时间计算要查询的表名和起始时间
	 *
	 * @param start
	 * @param step
	 * @return
	 */
	public List<String[]> getTime(String start, int step) {
		List<String[]> list = new ArrayList();
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		String ds = start + " 00:00:00";
		Date date = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss").parse(ds,
				new ParsePosition(0));
		if (step == 1) {
			cale.setTimeInMillis(date.getTime());
			cale.add(Calendar.DATE, 1);
			Date dt = new Date(cale.getTimeInMillis());
			SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
			String[] s = new String[3];
			s[0] = new SimpleDateFormat("yyyy_MM_dd").format(date);
			s[1] = "" + (date.getTime() / 1000);// 开始时间（秒）
			s[2] = "" + (cale.getTimeInMillis() / 1000);// 结束时间（秒）
			list.add(s);
		} else if (step == 7) {
			cale.setTimeInMillis(date.getTime());
			int year = cale.get(Calendar.YEAR);
			int month = cale.get(Calendar.MONTH);
			String mid = "" + year + "_" + (month + 1) + "_01 00:00:00";
			Date dm = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss").parse(mid,
					new ParsePosition(0));
			Date dt = cale.getTime();
			SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
			String[] s = new String[3];
			s[0] = new SimpleDateFormat("yyyy_MM").format(date);
			s[2] = "" + (date.getTime() / 1000);
			cale.add(Calendar.WEEK_OF_MONTH, -1);
			s[1] = "" + (cale.getTimeInMillis() / 1000);
			if (cale.get(Calendar.MONTH) != month) {
				s[1] = "" + (dm.getTime() / 1000);
				String[] s_ = new String[3];
				s_[0] = new SimpleDateFormat("yyyy_MM").format(cale.getTime());
				s_[1] = "" + (cale.getTimeInMillis() / 1000);
				s_[2] = s[1];
				list.add(s_);
			}
			list.add(s);
		} else if (step == 30) {
			cale.setTimeInMillis(date.getTime());
			int year = cale.get(Calendar.YEAR);
			int month = cale.get(Calendar.MONTH);
			String mid = "" + year + "_" + (month + 1) + "_01 00:00:00";
			Date dm = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss").parse(mid,
					new ParsePosition(0));
			Date dt = cale.getTime();
			SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
			String[] s = new String[3];
			s[0] = new SimpleDateFormat("yyyy_MM").format(date);
			s[2] = "" + (date.getTime() / 1000);
			cale.add(Calendar.MONTH, -1);
			s[1] = "" + (cale.getTimeInMillis() / 1000);
			if (cale.get(Calendar.MONTH) != month) {
				s[1] = "" + (dm.getTime() / 1000);
				String[] s_ = new String[3];
				s_[0] = new SimpleDateFormat("yyyy_MM").format(cale.getTime());
				s_[1] = "" + (cale.getTimeInMillis() / 1000);
				s_[2] = s[1];
				list.add(s_);
			}
			list.add(s);
		}
		return list;
	}

	private List<String[]> getSETimeList(String stime, String etime) {
		List<String[]> list = new ArrayList();
		String start = stime + " 00:00:00";
		Date sdate;
		try {
			sdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);

			String end = etime + " 00:00:00";
			Date edate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);

			Calendar scale = Calendar.getInstance(Locale.CHINESE);
			Calendar ecale = Calendar.getInstance(Locale.CHINESE);
			Calendar tcale = Calendar.getInstance(Locale.CHINESE);
			// log.debug("getSETimeList stime=" + stime + ",etime=" + etime);
			// log.debug("sdate=" + sdate);
			scale.setTimeInMillis(sdate.getTime());
			ecale.setTimeInMillis(edate.getTime());
			tcale = scale;
			int syear = scale.get(Calendar.YEAR);
			int smonth = scale.get(Calendar.MONTH);
			int sday = scale.get(Calendar.DAY_OF_MONTH);

			int eyear = ecale.get(Calendar.YEAR);
			int emonth = ecale.get(Calendar.MONTH);
			int eday = ecale.get(Calendar.DAY_OF_MONTH);

			int tyear = syear;
			int tmonth = smonth;
			int tday = sday;

			String[] s = null;
			if (stime.equals(etime)) {
				s = new String[3];
				scale.add(Calendar.DATE, 1);
				s[0] = new SimpleDateFormat("yyyy_MM_dd").format(sdate);
				s[1] = "" + (sdate.getTime() / 1000);// 开始时间（秒）
				s[2] = "" + (scale.getTimeInMillis() / 1000);// 结束时间（秒）
				list.add(s);
			} else {
				if (tyear == eyear && tmonth == emonth) {
					s = new String[3];
					s[0] = new SimpleDateFormat("yyyy_MM").format(tcale
							.getTime());
					s[1] = "" + (sdate.getTime() / 1000);
					s[2] = "" + (edate.getTime() / 1000);
					log.debug("日期：" + s[0]);
					list.add(s);
				}
				while (tyear < eyear || tmonth <= emonth) {
					s = new String[3];
					s[0] = new SimpleDateFormat("yyyy_MM").format(tcale
							.getTime());
					s[1] = "" + (sdate.getTime() / 1000);
					s[2] = "" + (edate.getTime() / 1000);
					log.debug("日期：" + s[0]);
					list.add(s);
					tcale.add(Calendar.MONTH, 1);
					tyear = tcale.get(Calendar.YEAR);
					tmonth = tcale.get(Calendar.MONTH);
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();

			log.error("ParseException:" + e.getMessage());
		}
		log.debug("list.size=" + list.size());
		return list;
	}

	/**
	 * 根据起始时间平移
	 *
	 * @param start
	 *            毫秒
	 * @param step
	 * @return
	 */
	public static String getTimeStr(long start, int step) {
		return getTimeStr(new SimpleDateFormat("yyyy_MM_dd").format(new Date(
				start)), step);
	}

	/**
	 * 根据起始时间平移
	 *
	 * @param start
	 *            "yyyy_MM_dd"
	 * @param step
	 * @return
	 */
	public static String getTimeStr(String start, int step) {
		String s = "";
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		String ds = start + " 00:00:00";
		Date date = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss").parse(ds,
				new ParsePosition(0));
		cale.setTimeInMillis(date.getTime());
		if (step == 1) {
			cale.add(Calendar.DATE, 1);
		} else if (step == 7) {
			cale.add(Calendar.WEEK_OF_MONTH, -1);
		} else if (step == 30) {
			cale.add(Calendar.MONTH, -1);
		}
		Date dt = new Date(cale.getTimeInMillis());
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
		s = new SimpleDateFormat("yyyy_MM_dd").format(dt);
		return s;
	}

	/**
	 * 获取上网行为TopN日报表数据
	 *
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param topN
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public List<Map> getAdvTopNStatic(String deviceId, String stime,
			String etime, int topN) {
		// 查询基础数据
		log.debug("PBTopNDAO getTopNDayStatic topN=" + topN);
		int totaltimes = 1;
		String sql = "", tableType = "sgw_conn_day_";
		// 查询topN数据
		String sql_ = " select  sum(times)  from (";
		if (stime.equals(etime)) {
			tableType = "sgw_conn_hour_";
		}
		Map tableNamemap = CommonUtil.getExistTables(tableType);
		List<String[]> list = getSETimeList(stime, etime);
		// List<String[]> list = getTime(date, type);
//		log.debug(list);
		String tables = getTablesSql(list, tableNamemap, deviceId, tableType);

		sql_ += tables;
		sql_ += ") t";
		log.debug("getTopN-DAYsql_gettimes=" + sql_);

		totaltimes = jt.queryForInt(sql_);

		sql += "select srcip, times, round(times/"
				+ totaltimes
				+ ",4)*100 precent from (select srcip,  sum(times) times  from (";
		sql += tables;
		sql += ") t  group by srcip ) s";
		sql += " group by srcip order by times desc";

		log.debug("getTopN-DAYsql=" + sql);

		List<Map> result = new ArrayList();
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			result = jt.queryForList(psql.getSQL());
		} catch (Exception ex) {
			log.debug(ex.getMessage());
//			log.debug(ex.getStackTrace());
//			log.debug(ex.getCause());
		}
		if(result.size() < topN){
			topN = result.size();
		}
		return result.subList(0, topN);
	}

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	public JdbcTemplateExtend getSpjt() {
		return spjt;
	}

	public void setSpjt(JdbcTemplateExtend spjt) {
		this.spjt = spjt;
	}
}
