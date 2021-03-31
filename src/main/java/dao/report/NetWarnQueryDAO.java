package dao.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.CommonUtil;
import com.linkage.litms.common.util.JdbcTemplateExtend;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.common.util.SysResouceUtils;
import com.linkage.litms.resource.DeviceAct;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 历史查询的dao类，供网络历史告警查询使用
 * 
 * @author 王志猛(5194), suixz(5253) tel(13512508857)
 * @since 2008-1-15
 * @category report
 */
public class NetWarnQueryDAO
{
	private static Logger log = LoggerFactory.getLogger(NetWarnQueryDAO.class);
	
	private DeviceAct devA;
	private JdbcTemplateExtend spjt;

	/**
	 * 查询告警等级及其对应的告警数量
	 * 
	 * @param severity
	 *            告警等级
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param deviceIp
	 *            设备IP
	 * @param deviceName
	 *            设备名称
	 * @param actstatus
	 *            确认状态
	 * @param clearstatus
	 *            清除状态
	 * @param creatortype
	 *            告警类型
	 * @param city_id
	 *            属地
	 * @param areaId
	 *            域id
	 * @param isAdmin
	 *            amdin域标志 true:是,false:否
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getSeverityAndWarnNum(String resourceType,
			String gatherId, String severity, long startTime, long endTime,
			String deviceIp, String deviceName, String actstatus,
			String clearstatus, String creatortype, String city_id,
			long areaId, boolean isAdmin) throws ParseException, SQLException
	{
		String sql = "";
		String gatherIds = getGatherIdWithAreaId(String.valueOf(areaId));
		int startWeek = CommonUtil.getWeekOfYear(startTime * 1000L);
		int endWeek = CommonUtil.getWeekOfYear(endTime * 1000L);
		int startYear = CommonUtil.getCurYearWithTime(startTime * 1000L);
		int endYear = CommonUtil.getCurYearWithTime(endTime * 1000L);
		String shareSql = "select severity,count(1) as num from ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			shareSql = "select severity,count(*) as num from ";
		}
		if (startYear == endYear)
		{ // 查询同一年内的历史告警
			sql = getSqlOfNumInTheSameYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, gatherIds, sql, shareSql, 0);
		} else if (startYear < endYear)
		{// 跨年度查询历史告警
			sql = getSqlInDiffrentYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, endYear, gatherIds, sql,
					shareSql, 0);
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return ("".equals(sql)) ? null : spjt.queryForList(sql);
	}

	/**
	 * 查询历史告警信息
	 * 
	 * @param severity
	 *            告警等级
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param deviceIp
	 *            设备IP
	 * @param deviceName
	 *            设备名称
	 * @param areaId
	 *            域id
	 * @param actstatus
	 *            确认状态
	 * @param clearstatus
	 *            清除状态
	 * @param creatortype
	 *            告警类型
	 * @param isAdmin
	 *            当前用户是否是admin用户
	 * @param curPage
	 *            要查询的页码
	 * @param rowCount
	 *            每页显示的条数
	 * @return List<Map>
	 * @throws ParseException
	 */
	public List<Map> getHisWarnInfo(String resourceType, String gatherId,
			String severity, long startTime, long endTime, String deviceIp,
			String deviceName, String actstatus, String clearstatus,
			String creatortype, String city_id, long areaId, boolean isAdmin,
			int curPage, int rowCount, final Map TitleMap)
			throws ParseException, SQLException
	{
		String sql = "";
		String gatherIds = getGatherIdWithAreaId(String.valueOf(areaId));
		int startWeek = CommonUtil.getWeekOfYear(startTime * 1000L);
		int endWeek = CommonUtil.getWeekOfYear(endTime * 1000L);
		int startYear = CommonUtil.getCurYearWithTime(startTime * 1000L);
		int endYear = CommonUtil.getCurYearWithTime(endTime * 1000L);
		String shareSql = "select a.gather_id,a.serialno, a.createtime,a.subserialno,a.creatorname,a.sourceip,a.sourcename,a.displaystring,a.severity,a.city,a.devicecoding from ";
		if (startYear == endYear)
		{ // 查询同一年内的历史告警
			sql = getSqlOfNumInTheSameYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, gatherIds, sql, shareSql, 1);
		} else if (startYear < endYear)
		{// 跨年度查询历史告警,支持跨一年查询
			sql = getSqlInDiffrentYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, endYear, gatherIds, sql,
					shareSql, 1);
		}
		sql += "".equals(sql) ? "" : " order by a.createtime desc";
		spjt.setMaxRows(10000); // 设置需要查询数据的总数
		spjt.setFetchSize(100);// 设置一次查询数据的数量
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return ("".equals(sql)) ? null : spjt.querySP(sql, (curPage - 1)
				* rowCount, rowCount, new RowMapper()
		{
			@SuppressWarnings("unchecked")
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Map m = new HashMap();
				m.put("createtime", StringUtils.formatDate(
						"yyyy-MM-dd HH:mm:ss", rs.getLong("createtime")));
				m.put("gather_id", rs.getString("gather_id"));
				m.put("year", StringUtils.getYear(Integer.parseInt(rs
						.getBigDecimal("createtime").toString())));
				m.put("week", CommonUtil.getWeekOfYear(Long.parseLong(rs
						.getBigDecimal("createtime").toString()) * 1000L));
				m.put("serialno", rs.getString("serialno"));
				m
						.put("subserialno", rs.getBigDecimal("subserialno")
								.toString());
				m.put("creatorname", rs.getString("creatorname"));
				m.put("sourceip", rs.getString("sourceip"));
				m.put("sourcename", rs.getString("sourcename"));
				m.put("displaystring", rs.getString("displaystring"));
				// m.put("severity",
				// rs.getBigDecimal("severity").toString());
				m.put("severity", TitleMap.get(rs.getBigDecimal("severity")
						.toString().trim()));
				m.put("ctime", rs.getLong("createtime"));
				m.put("city_name", rs.getString("city"));
				return m;
			}
		});
	}

	
	public List<Map> getHisWarnInfoDetail(String resourceType, String gatherId,
			String severity, long startTime, long endTime, String deviceIp,
			String deviceName, String actstatus, String clearstatus,
			String creatortype, String city_id, long areaId, boolean isAdmin,
			int curPage, int rowCount, final Map TitleMap)
			throws ParseException, SQLException
	{
		String sql = "";
		String gatherIds = getGatherIdWithAreaId(String.valueOf(areaId));
		int startWeek = CommonUtil.getWeekOfYear(startTime * 1000L);
		int endWeek = CommonUtil.getWeekOfYear(endTime * 1000L);
		int startYear = CommonUtil.getCurYearWithTime(startTime * 1000L);
		int endYear = CommonUtil.getCurYearWithTime(endTime * 1000L);
		String shareSql = "select a.gather_id,a.serialno, a.createtime,a.subserialno,a.creatorname,a.sourceip,a.sourcename,a.displaystring,a.severity,a.city,a.devicecoding from ";
		if (startYear == endYear)
		{ // 查询同一年内的历史告警
			sql = getSqlOfNumInTheSameYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, gatherIds, sql, shareSql, 1);
		} else if (startYear < endYear)
		{// 跨年度查询历史告警,支持跨一年查询
			sql = getSqlInDiffrentYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, endYear, gatherIds, sql,
					shareSql, 1);
		}
		sql += "".equals(sql) ? "" : " order by a.createtime desc";
		spjt.setMaxRows(10000); // 设置需要查询数据的总数
		spjt.setFetchSize(100);// 设置一次查询数据的数量
		
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		
		
		return ("".equals(sql)) ? null : spjt.querySP(sql, (curPage - 1)
				* rowCount, rowCount, new RowMapper()
		{
			@SuppressWarnings("unchecked")
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Map m = new HashMap();
				m.put("createtime", StringUtils.formatDate(
						"yyyy-MM-dd HH:mm:ss", rs.getLong("createtime")));
				m.put("gather_id", rs.getString("gather_id"));
				m.put("year", StringUtils.getYear(Integer.parseInt(rs
						.getBigDecimal("createtime").toString())));
				m.put("week", CommonUtil.getWeekOfYear(Long.parseLong(rs
						.getBigDecimal("createtime").toString()) * 1000L));
				m.put("serialno", rs.getString("serialno"));
				m
						.put("subserialno", rs.getBigDecimal("subserialno")
								.toString());
				m.put("creatorname", rs.getString("creatorname"));
				m.put("sourceip", rs.getString("sourceip"));
				m.put("sourcename", rs.getString("sourcename"));
				m.put("displaystring", rs.getString("displaystring"));
				// m.put("severity",
				// rs.getBigDecimal("severity").toString());
				m.put("severity", TitleMap.get(rs.getBigDecimal("severity")
						.toString().trim()));
				m.put("ctime", rs.getLong("createtime"));
				m.put("city_name", rs.getString("city"));
				return m;
			}
		});
	}
	
	/**
	 * 获取符合条件的所有告警(导出Excel用)
	 * 
	 * @param resourceType
	 *            设备层次
	 * @param gatherId
	 *            采集点ID
	 * @param severity
	 *            告警等级
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param deviceIp
	 *            设备IP地址
	 * @param deviceName
	 *            设备名称
	 * @param actstatus
	 *            确认状态
	 * @param clearstatus
	 *            清除状态
	 * @param creatortype
	 *            告警类型
	 * @param areaId
	 *            用户所在域ID
	 * @param isAdmin
	 *            是否是Admin用户
	 * @param TitleMap
	 *            告警等级对应的中英文说明(实现中英文切换)
	 * @return List 符合条件的所有数据
	 * @throws ParseException
	 * @since 2008-1-23 benyp(5260)
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getAllHisWarnData(String resourceType, String gatherId,
			String severity, long startTime, long endTime, String deviceIp,
			String deviceName, String actstatus, String clearstatus,
			String creatortype, String city_id, long areaId, boolean isAdmin,
			final Map TitleMap) throws ParseException, SQLException
	{
		String sql = "";
		String gatherIds = getGatherIdWithAreaId(String.valueOf(areaId));
		int startWeek = CommonUtil.getWeekOfYear(startTime * 1000L);
		int endWeek = CommonUtil.getWeekOfYear(endTime * 1000L);
		int startYear = CommonUtil.getCurYearWithTime(startTime * 1000L);
		int endYear = CommonUtil.getCurYearWithTime(endTime * 1000L);
		String shareSql = "select a.serialno, a.createtime,a.subserialno,a.creatorname,a.sourceip,a.sourcename,a.displaystring,a.severity,a.city,a.devicecoding from ";
		if (startYear == endYear)
		{ // 查询同一年内的历史告警
			sql = getSqlOfNumInTheSameYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, gatherIds, sql, shareSql, 1);
		} else if (startYear < endYear)
		{// 跨年度查询历史告警,支持跨一年查询
			sql = getSqlInDiffrentYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, endYear, gatherIds, sql,
					shareSql, 1);
		}
		if ("".equals(sql))
		{
			return null;
		} else
		{
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			List<Map> list = spjt.query(sql, new RowMapper()
			{
				public Object mapRow(ResultSet rs, int rowNum)
						throws SQLException
				{
					Map m = new HashMap();
					m.put("serialno", rs.getString("serialno"));
					m.put("creatorname", rs.getString("creatorname"));
					m.put("sourceip", rs.getString("sourceip"));
					m.put("createtime", StringUtils.formatDate(
							"yyyy-MM-dd HH:mm:ss", rs.getLong("createtime")));
					m.put("severity", TitleMap.get(rs.getBigDecimal("severity")
							.toString().trim()));
					m.put("sourcename", rs.getString("sourcename"));
					m.put("displaystring", rs.getString("displaystring"));
					m.put("city_name", rs.getString("city"));
					return m;
				}
			});
			return list;
		}
	}

	/**
	 * 查询该属地的所有子属地(包括自己)
	 * 
	 * @param city_id
	 * @return
	 */
	public List<Map<String, String>> getCityList(String city_id)
	{
		return CityDAO.getAllNextCityListByCityPid(city_id);

	}

	/**
	 * 根据域id查询采集点信息
	 * 
	 * @param areaId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getGatherList(long areaId)
	{
		String sql = "select gather_id,descr from tab_process_desc where gather_id in( select res_id from tab_gw_res_area where res_type=2 and area_id="
				+ areaId + ")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return spjt.queryForList(sql);
	}

	public String getGatherListStr(long areaId)
	{
		String sql = "select gather_id,descr from tab_process_desc where gather_id in( select res_id from tab_gw_res_area where res_type=2 and area_id="
				+ areaId + ")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql); 
		String gatherid = "";
		Map field = cursor.getNext();
		while(field != null) {
			gatherid = (String)field.get("gather_id");
			field = cursor.getNext();
		}
		
		return gatherid;
	}
	
	/**
	 * 查询设备层次列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getResourceTypeList()
	{
		String sql = "select resource_type_id,resource_name from tab_resourcetype order by resource_type_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return spjt.queryForList(sql);
	}

	/**
	 * 获取查询同一年内告警等级与数量的sql语句
	 * 
	 * @param resourceType
	 *            设备层次
	 * @param gatherId
	 *            采集点
	 * @param severity
	 *            告警等级
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param deviceIp
	 *            设备IP
	 * @param deviceName
	 *            设备名称
	 * @param actstatus
	 *            确认状态
	 * @param clearstatus
	 *            清除状态
	 * @param creatortype
	 *            告警类型
	 * @param areaId
	 *            域id
	 * @param isAdmin
	 *            是否为admin
	 * @param startWeek
	 *            起始周
	 * @param endWeek
	 *            结束周
	 * @param startYear
	 *            年份
	 * @param gatherIds
	 *            采集点,类似于'1','2','3','4','5'
	 * @return
	 */
	private String getSqlOfNumInTheSameYear(String resourceType,
			String gatherId, String severity, long startTime, long endTime,
			String deviceIp, String deviceName, String actstatus,
			String clearstatus, String creatortype, String city_id,
			long areaId, boolean isAdmin, int startWeek, int endWeek,
			int startYear, String gatherIds, String sql, String shareSql,
			int flag)
	{
		for (int i = startWeek; i <= endWeek; i++)
		{
			String tableName = getTableName(startYear, i);
			System.out.println("45678"+tableName);
			int tableIsExist = CommonUtil.tableIsExist(tableName, spjt); // 判断表是否存在
			if (tableIsExist == 0)
			{ // 表不存在,跳过
				continue;
			}
			sql = getSqlResourceType(gatherId, severity, startTime, endTime,
					deviceIp, deviceName, actstatus, clearstatus, creatortype,
					city_id, areaId, isAdmin, gatherIds, tableName, sql,
					shareSql, flag);
		}
		return sql;
	}

	/**
	 * 获取统计数据的sql语句
	 * 
	 * @param gatherId
	 *            采集点
	 * @param severity
	 *            告警级别
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param deviceIp
	 *            设备IP
	 * @param deviceName
	 *            设备名称
	 * @param areaId
	 *            域id
	 * @param actstatus
	 *            确认状态
	 * @param clearstatus
	 *            清除状态
	 * @param creatortype
	 *            告警类型
	 * @param isAdmin
	 *            是否为admin
	 * @param gatherIds
	 *            采集点,类似于'1','2','3'
	 * @param tableName
	 * @return
	 */
	private String getSqlResourceType(String gatherId, String severity,
			long startTime, long endTime, String deviceIp, String deviceName,
			String actstatus, String clearstatus, String creatortype,
			String city_id, long areaId, boolean isAdmin, String gatherIds,
			String tableName, String sql, String shareSql, int flag)
	{
		tableName += " a";
		if ("".equals(sql))
		{
			if ((gatherId != null) && (!"".equals(gatherId)))// 采集点为空
			{
				sql = shareSql
						+ tableName
						+ ((city_id != null && !city_id.equals("")) ? ",tab_gw_device b "
								: "") + " where a.createtime>=" + startTime
						+ " and a.createtime<=" + endTime
						+ " and a.gather_id='" + gatherId + "'";
			} else
			{
				sql = shareSql
						+ tableName
						+ ((city_id != null && !city_id.equals("")) ? ",tab_gw_device b "
								: "") + " where a.createtime>=" + startTime
						+ " and a.createtime<=" + endTime
						+ " and a.gather_id in(" + gatherIds + ")";
			}
		} else
		{
			if ((gatherId != null) && (!"".equals(gatherId)))
			{
				sql += " union all "
						+ shareSql
						+ tableName
						+ ((city_id != null && !city_id.equals("")) ? ",tab_gw_device b "
								: "") + " where a.createtime>=" + startTime
						+ " and a.createtime<=" + endTime
						+ " and a.gather_id='" + gatherId + "'";
			} else
			{
				sql += " union all "
						+ shareSql
						+ tableName
						+ ((city_id != null && !city_id.equals("")) ? ",tab_gw_device b "
								: "") + " where a.createtime>=" + startTime
						+ " and a.createtime<=" + endTime
						+ " and a.gather_id in(" + gatherIds + ")";
			}
		}
		// city_id
		List<String> clist = CityDAO.getAllNextCityIdsByCityPid(city_id);
		if (city_id != null && !city_id.equals(""))
		{
			sql += " and a.devicecoding=b.device_id and b.city_id in ("
					+ StringUtils.weave(clist) + ")";
		}
		clist = null;
		// 清除状态
		if (clearstatus != null && !clearstatus.equals(""))
		{
			sql += " and a.clearstatus !=1";
		}
		// 确认状态
		if (actstatus != null && !actstatus.equals(""))
		{
			sql += " and a.activestatus !=1";
		}
		// 告警类型
		if (creatortype != null && !creatortype.equals(""))
		{
			sql += " and a.creatortype in(" + creatortype + ")";
		}
		if ((severity != null) && (!"".equals(severity)))
		{
			sql += " and a.severity in(" + severity + ")";
		}
		if ((deviceIp != null) && (!"".equals(deviceIp)))
		{
			sql += " and a.sourceip='" + deviceIp + "'";
		}
		if ((deviceName != null) && (!"".equals(deviceName)))
		{
			sql += " and a.sourcename='" + deviceName + "'";
		}
		if (isAdmin == false)
		{
			sql += " and  a.devicecoding in(select res_id from tab_gw_res_area where area_id="
					+ areaId + " and res_type=1 )";
		}
		if (flag == 0)
		{
			sql += " group by a.severity";
		} else if (flag == 2)
		{
			sql += " group by a.sourcename";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return sql;
	}

	/**
	 * 跨年查询的sql
	 * 
	 * @param resourceType
	 *            设备层次
	 * @param gatherId
	 *            采集点
	 * @param severity
	 *            告警级别
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param deviceIp
	 *            设备IP
	 * @param deviceName
	 *            设备名称
	 * @param actstatus
	 *            确认状态
	 * @param clearstatus
	 *            清除状态
	 * @param creatortype
	 *            告警类型
	 * @param areaId
	 *            域id
	 * @param isAdmin
	 *            是否为admin
	 * @param gatherIds
	 *            采集点,类似于'1','2','3'
	 * @param
	 * @param sql
	 * @param shareSql
	 * @throws ParseException
	 */
	private String getSqlInDiffrentYear(String resourceType, String gatherId,
			String severity, long startTime, long endTime, String deviceIp,
			String deviceName, String actstatus, String clearstatus,
			String creatortype, String city_id, long areaId, boolean isAdmin,
			int startWeek, int endWeek, int startYear, int endYear,
			String gatherIds, String sql, String shareSql, int flag)
			throws ParseException
	{
		String endDayOfYear = startYear + "-12-31";
		long time = DateFormat.getDateInstance().parse(endDayOfYear).getTime();
		int endWeekOfYear = CommonUtil.getWeekOfYear(time);
		for (int i = startWeek; i <= endWeekOfYear; i++)
		{// 前一年的查询
			String tableName = getTableName(startYear, i);
			int tableIsExist = CommonUtil.tableIsExist(tableName, spjt); // 判断表是否存在
			if (tableIsExist == 0)
			{ // 表不存在,跳过
				continue;
			}
			sql = getSqlResourceType(gatherId, severity, startTime, endTime,
					deviceIp, deviceName, actstatus, clearstatus, creatortype,
					city_id, areaId, isAdmin, gatherIds, tableName, sql,
					shareSql, flag);
		}
		for (int i = 1; i <= endWeek; i++)
		{// 第二年的查询
			String tableName = getTableName(endYear, i);
			int tableIsExist = CommonUtil.tableIsExist(tableName, spjt); // 判断表是否存在
			if (tableIsExist == 0)
			{ // 表不存在,跳过
				continue;
			}
			sql = getSqlResourceType(gatherId, severity, startTime, endTime,
					deviceIp, deviceName, actstatus, clearstatus, creatortype,
					city_id, areaId, isAdmin, gatherIds, tableName, sql,
					shareSql, flag);
		}
		return sql;
	}

	/**
	 * 根据域id获取和当前用户关联的采集点
	 * 
	 * @param areaId
	 *            域id
	 * @return gathers:采集点字符串,类似于'1','2','3','4','5'
	 */
	private String getGatherIdWithAreaId(String areaId)
	{
		String gathers = SysResouceUtils.getGatherIdForSql(areaId, devA);
		return gathers;
	}

	/**
	 * 根据传入的参数获取所用的表名,类似于event_raw_2008_01
	 * 
	 * @param year
	 *            年份
	 * @param weekOfYear
	 *            周
	 * @return tableName:表名
	 */
	private String getTableName(int year, int weekOfYear)
	{
		return "event_raw_" + year + "_" + weekOfYear;
	}

	public void getDevReport(String resourceType, String gatherId,
			String severity, long startTime, long endTime, String deviceIp,
			String deviceName, String actstatus, String clearstatus,
			String creatortype, String city_id, long areaId, boolean isAdmin)
			throws ParseException, SQLException
	{
		String sql = "";
		String gatherIds = getGatherIdWithAreaId(String.valueOf(areaId));
		int startWeek = CommonUtil.getWeekOfYear(startTime * 1000L);
		int endWeek = CommonUtil.getWeekOfYear(endTime * 1000L);
		int startYear = CommonUtil.getCurYearWithTime(startTime * 1000L);
		int endYear = CommonUtil.getCurYearWithTime(endTime * 1000L);
		String shareSql = "select count(*) as num from ";
		// String shareSql = "select * as num from ";
		if (startYear == endYear)
		{ // 查询同一年内的历史告警
			sql = getSqlOfNumInTheSameYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, gatherIds, sql, shareSql, 1);
		} else if (startYear < endYear)
		{// 跨年度查询历史告警
			sql = getSqlInDiffrentYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, endYear, gatherIds, sql,
					shareSql, 1);
		}
		sql = "select severity, devicecoding, count(1) from (" + sql
				+ ") group by severity, devicecoding";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select severity, devicecoding, count(*) from (" + sql
					+ ") group by severity, devicecoding";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
	}

	// ==============================================按设备统计告警条数（北京测试用）2008-6-23====================
	/**
	 * 查询每台设备的告警条数
	 * 
	 * @param resourceType
	 *            设备层次
	 * @param gatherId
	 *            采集点
	 * @param severity
	 *            告警等级
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param deviceIp
	 *            设备ip
	 * @param deviceName
	 *            设备名称
	 * @param actstatus
	 *            确认状态
	 * @param clearstatus
	 *            清楚状态
	 * @param creatortype
	 *            创建者
	 * @param city_id
	 *            属地id
	 * @param areaId
	 *            域id
	 * @param isAdmin
	 *            是否是admin用户
	 * @param curPage
	 *            当前页码
	 * @param rowCount
	 *            每页行数
	 * @param TitleMap
	 * @return
	 * @throws ParseException
	 */

	public List<Map> queryWarnInfoByDevice(String resourceType,
			String gatherId, String severity, long startTime, long endTime,
			String deviceIp, String deviceName, String actstatus,
			String clearstatus, String creatortype, String city_id,
			long areaId, boolean isAdmin, int curPage, int rowCount,
			final Map TitleMap) throws ParseException
	{
		String sql = "";
		String gatherIds = getGatherIdWithAreaId(String.valueOf(areaId));
		int startWeek = CommonUtil.getWeekOfYear(startTime * 1000L);
		int endWeek = CommonUtil.getWeekOfYear(endTime * 1000L);
		int startYear = CommonUtil.getCurYearWithTime(startTime * 1000L);
		int endYear = CommonUtil.getCurYearWithTime(endTime * 1000L);
		String shareSql = " select a.sourcename,count(1) as num," +
				"sum(case when a.severity=5 then 1 else 0 end) as level5," +
				"sum(case when a.severity=4 then 1 else 0 end) as level4," +
				"sum(case when a.severity=3 then 1 else 0 end) as level3," +
				"sum(case when a.severity=2 then 1 else 0 end) as level2," +
				"sum(case when a.severity=1 then 1 else 0 end) as level1," +
				"sum(case when a.severity=0 then 1 else 0 end) as level0," +
				// 对已处理的告警进行统计
				"sum(case when a.severity=5 and (a.activestatus !=1 or a.clearstatus !=1) then 1 else 0 end) as level5deal," +
				"sum(case when a.severity=4 and (a.activestatus !=1 or a.clearstatus !=1) then 1 else 0 end) as level4deal," +
				"sum(case when a.severity=3 and (a.activestatus !=1 or a.clearstatus !=1) then 1 else 0 end) as level3deal," +
				"sum(case when a.severity=2 and (a.activestatus !=1 or a.clearstatus !=1) then 1 else 0 end) as level2deal," +
				"sum(case when a.severity=1 and (a.activestatus !=1 or a.clearstatus !=1) then 1 else 0 end) as level1deal," +
				"sum(case when a.severity=0 and (a.activestatus !=1 or a.clearstatus !=1) then 1 else 0 end) as level0deal" +
				" from ";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			shareSql = " select a.sourcename,count(*) as num," +
					"sum(case when a.severity=5 then 1 else 0 end) as level5," +
					"sum(case when a.severity=4 then 1 else 0 end) as level4," +
					"sum(case when a.severity=3 then 1 else 0 end) as level3," +
					"sum(case when a.severity=2 then 1 else 0 end) as level2," +
					"sum(case when a.severity=1 then 1 else 0 end) as level1," +
					"sum(case when a.severity=0 then 1 else 0 end) as level0," +
					// 对已处理的告警进行统计
					"sum(case when a.severity=5 and (a.activestatus !=1 or a.clearstatus !=1) then 1 else 0 end) as level5deal," +
					"sum(case when a.severity=4 and (a.activestatus !=1 or a.clearstatus !=1) then 1 else 0 end) as level4deal," +
					"sum(case when a.severity=3 and (a.activestatus !=1 or a.clearstatus !=1) then 1 else 0 end) as level3deal," +
					"sum(case when a.severity=2 and (a.activestatus !=1 or a.clearstatus !=1) then 1 else 0 end) as level2deal," +
					"sum(case when a.severity=1 and (a.activestatus !=1 or a.clearstatus !=1) then 1 else 0 end) as level1deal," +
					"sum(case when a.severity=0 and (a.activestatus !=1 or a.clearstatus !=1) then 1 else 0 end) as level0deal" +
					" from ";
		}
		if (startYear == endYear)
		{ // 查询同一年内的历史告警
			sql = getSqlOfNumInTheSameYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, gatherIds, sql, shareSql, 2);
		} else if (startYear < endYear)
		{// 跨年度查询历史告警,支持跨一年查询
			sql = getSqlInDiffrentYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, endYear, gatherIds, sql,
					shareSql, 2);
		}
		if (StringUtil.IsEmpty(sql)) {
			return null;
		}
		sql = "select sourcename,sum(num) as num," +
				"sum(level5) as level5,sum(level4) as level4," +
				"sum(level3) as level3,sum(level2) as level2," +
				"sum(level1) as level1,sum(level0) as level0," +
				
				"sum(level5deal) as level5deal,sum(level4deal) as level4deal," +
				"sum(level3deal) as level3deal,sum(level2deal) as level2deal," +
				"sum(level1deal) as level1deal,sum(level0deal) as level0deal" +
				" from ("
				+ sql
				+ ("".equals(sql) ? ""
						: " ) t group by sourcename order by sourcename");
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		System.out.println("66666"+psql.getSQL());
		spjt.setMaxRows(10000); // 设置需要查询数据的总数
		spjt.setFetchSize(100);// 设置一次查询数据的数量

		return ("".equals(sql)) ? null : spjt.querySP(sql, (curPage - 1)
				* rowCount, rowCount);
	}

	/**
	 * 查询有告警存在设备的总数
	 * 
	 * @param resourceType
	 *            设备层次
	 * @param gatherId
	 *            采集点
	 * @param severity
	 *            告警级别
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param deviceIp
	 *            设备ip
	 * @param deviceName
	 *            设备名称
	 * @param actstatus
	 *            确认状态
	 * @param clearstatus
	 *            清楚状态
	 * @param creatortype
	 *            创建者
	 * @param city_id
	 *            属地id
	 * @param areaId
	 *            域id
	 * @param isAdmin
	 *            是否是admin用户
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public List<Map> queryWarnNumByDevice(String resourceType, String gatherId,
			String severity, long startTime, long endTime, String deviceIp,
			String deviceName, String actstatus, String clearstatus,
			String creatortype, String city_id, long areaId, boolean isAdmin)
			throws ParseException
	{
		String sql = "";
		String gatherIds = getGatherIdWithAreaId(String.valueOf(areaId));
		int startWeek = CommonUtil.getWeekOfYear(startTime * 1000L);
		int endWeek = CommonUtil.getWeekOfYear(endTime * 1000L);
		int startYear = CommonUtil.getCurYearWithTime(startTime * 1000L);
		int endYear = CommonUtil.getCurYearWithTime(endTime * 1000L);
		
		String shareSql = " select a.sourcename,count(1) as num from ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			shareSql = " select a.sourcename,count(*) as num from ";
		}
		if (startYear == endYear)
		{ // 查询同一年内的历史告警
			sql = getSqlOfNumInTheSameYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, gatherIds, sql, shareSql, 2);
		} else if (startYear < endYear)
		{// 跨年度查询历史告警,支持跨一年查询
			sql = getSqlInDiffrentYear(resourceType, gatherId, severity,
					startTime, endTime, deviceIp, deviceName, actstatus,
					clearstatus, creatortype, city_id, areaId, isAdmin,
					startWeek, endWeek, startYear, endYear, gatherIds, sql,
					shareSql, 2);
		}
		if (StringUtil.IsEmpty(sql)) {
			return null;
		}
		log.warn("===sql="+sql+"====");
		log.warn("===startYear=="+startYear+"====");
		log.warn("===endYear=="+endYear+"====");
		sql = "select sourcename,sum(num) as num from (" + sql
				+ ("".equals(sql) ? "" : " ) t  group by sourcename");
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return spjt.queryForList(sql);
	}

	public void setDevA(DeviceAct devA)
	{
		this.devA = devA;
	}

	public void setDao(DataSource dao)
	{
		this.spjt = new JdbcTemplateExtend(dao);
	}
}
