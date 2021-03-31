package com.linkage.liposs.buss.dao.securitygw;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.liposs.buss.util.CommonUtil;
import com.linkage.liposs.buss.util.JdbcTemplateExtend;
import com.linkage.litms.common.util.StringUtils;

/**
 * <p>
 * Title: Global View
 * </p>
 * 
 * <p>
 * Description: 企业安全统计
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: 联创科技
 * </p>
 * 
 * @author 史亮
 * @version 1.0
 */
public class EntSecStatDAO
{
	private static Logger log = LoggerFactory.getLogger(EntSecStatDAO.class);
	public static final int VIRUSEVENT = 1;
	public static final int SPAMMAIL = 2;
	public static final int FILTER = 3;
	public static final int ATTACK = 4;
	public static final int CONNECT = 5;
	private JdbcTemplate jt;
	public String stationid = "";
	public DataSource ds;
	private JdbcTemplateExtend spjt;
	private String sqlTemp = "";
	public void setDao(DataSource ds)
	{
		log.debug("ds");
		this.ds = ds;
		jt = new JdbcTemplate(ds);
		this.spjt = new JdbcTemplateExtend(ds);
	}
	/**
	 * 获取事件类型字符串
	 * 
	 * @param eventType
	 *            事件类型号
	 * @return String 事件类型字符串
	 */
	public String getStringByEventType(int eventType)
	{
		String str = "";
		log.debug("eventType=" + eventType);
		switch (eventType)
			{
			case VIRUSEVENT:
				str = "virus";
				break;
			case SPAMMAIL:
				str = "ashmail";
				break;
			case FILTER:
				str = "filter";
				break;
			case ATTACK:
				str = "attack";
				break;
			case CONNECT:
				str = "conn";
				break;
			default:
				str = "";
			}
		log.debug("str=" + str);
		return str;
	}
	/**
	 * 获取某种事件类型的原始表
	 * 
	 * @param deviceId
	 *            设备Id号
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param eventType
	 *            事件类型
	 * @return List<Map> 内容
	 */
	public List<Map> getOrigEntSecurity(String deviceId, long startTime, long endTime,
			int eventType)
	{
		String sql = "";
		String strEvent = getStringByEventType(eventType);
		long dayIntrval = 3600000 * 24;
		if (startTime >= endTime)
			return null;
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(startTime);
		int startYear = cale.get(Calendar.YEAR);
		int startMonth = cale.get(Calendar.MONTH) + 1;
		int startDay = cale.get(Calendar.DATE);
		cale.setTimeInMillis(endTime);
		int endYear = cale.get(Calendar.YEAR);
		int endMonth = cale.get(Calendar.MONTH) + 1;
		int endDay = cale.get(Calendar.DATE);
		if (startYear == endYear && startMonth == endMonth && startDay == endDay)
			{
				sql += "select * from " + strEvent + "_original_" + startYear + "_"
						+ startMonth + "_" + startDay;
				sql += " where stime>=" + startTime + " and etime<=" + endTime
						+ " and deviceid='" + deviceId + "'";
			}
		else
			{
				for (long temp = startTime; temp <= endTime; temp += dayIntrval)
					{
						cale.setTimeInMillis(temp);
						int tempYear = cale.get(Calendar.YEAR);
						int tempMonth = cale.get(Calendar.MONTH) + 1;
						int tempDay = cale.get(Calendar.DATE);
						if (tempYear == startYear && tempMonth == startMonth
								&& tempDay == startDay)
							{
								sql += "select * from " + strEvent + "_original_"
										+ tempYear + "_" + tempMonth + "_" + tempDay;
								sql += " where stime>=" + temp + " and deviceid='"
										+ deviceId + "'";
							}
						else
							if (tempYear == endYear && tempMonth == endMonth
									&& tempDay == endDay)
								{
									sql += " union all select * from " + strEvent
											+ "_original_" + tempYear + "_" + tempMonth
											+ "_" + tempDay;
									sql += " where etime<=" + temp + " and deviceid='"
											+ deviceId + "'";
								}
							else
								{
									sql += " union all select * from " + strEvent
											+ "_original_" + tempYear + "_" + tempMonth
											+ "_" + tempDay;
									sql += " where deviceid='" + deviceId + "'";
								}
					}
			}
//		log.debug("getOrigEntSecurity-sql=" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> virusHours = jt.queryForList(psql.getSQL());
		return virusHours;
	}
	/**
	 * 获取某种事件类型的小时表
	 * 
	 * @param deviceId
	 *            设备Id号
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param eventType
	 *            事件类型
	 * @return List<Map> 内容
	 */
	public List<Map> getEntSecurityByHourAndType(String deviceId, long startTime,
			long endTime, int eventType)
	{
		String sql = "";
		String strEvent = getStringByEventType(eventType);
		log.debug("strEvent=" + strEvent);
		long dayIntrval = 3600 * 24 * 1000;
		if (startTime >= endTime)
			return null;
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(startTime);
		int startYear = cale.get(Calendar.YEAR);
		int startMonth = cale.get(Calendar.MONTH) + 1;
		int startDay = cale.get(Calendar.DATE);
		cale.setTimeInMillis(endTime);
		int endYear = cale.get(Calendar.YEAR);
		int endMonth = cale.get(Calendar.MONTH) + 1;
		int endDay = cale.get(Calendar.DATE);
		if (startYear == endYear && startMonth == endMonth && startDay == endDay)
			{
				// sql += "select * from
				// "+strEvent+"_hour_"+startYear+"_"+startMonth+"_"+startDay;
				sql += "select * from " + strEvent + "_hour_yyyy_mm_dd";
				sql += " where stime>=" + startTime + " and etime<=" + endTime
						+ " and deviceid='" + deviceId + "'";
			}
		else
			{
				for (long temp = startTime; temp <= endTime; temp += dayIntrval)
					{
						cale.setTimeInMillis(temp);
						int tempYear = cale.get(Calendar.YEAR);
						int tempMonth = cale.get(Calendar.MONTH) + 1;
						int tempDay = cale.get(Calendar.DATE);
						if (tempYear == startYear && tempMonth == startMonth
								&& tempDay == startDay)
							{
								// sql += "select * from
								// "+strEvent+"_hour_"+tempYear+"_"+tempMonth+"_"+tempDay;
								sql += "select * from " + strEvent + "_hour_yyyy_mm_dd";
								sql += " where stime>=" + temp + " and deviceid='"
										+ deviceId + "'";
							}
						else
							if (tempYear == endYear && tempMonth == endMonth
									&& tempDay == endDay)
								{
									// sql += " union all select * from
									// "+strEvent+"_hour_"+tempYear+"_"+tempMonth+"_"+tempDay;
									sql += " union all select * from " + strEvent
											+ "_hour_yyyy_mm_dd";
									sql += " where etime<=" + endTime + " and deviceid='"
											+ deviceId + "'";
								}
							else
								{
									// sql += " union all select * from
									// "+strEvent+"_hour_"+tempYear+"_"+tempMonth+"_"+tempDay;
									sql += " union all select * from " + strEvent
											+ "_hour_yyyy_mm_dd";
									sql += " where deviceid='" + deviceId + "'";
								}
					}
			}
//		log.debug("getEntSecurityByHourAndType-sql=" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> virusHours = jt.queryForList(psql.getSQL());
		return virusHours;
	}
	/**
	 * 获取某种事件类型的天表
	 * 
	 * @param deviceId
	 *            设备Id号
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param eventType
	 *            事件类型
	 * @return List<Map> 内容
	 */
	public List<Map> getEntSecurityByDayAndType(String deviceId, long startTime,
			long endTime, int eventType)
	{
		String sql = "";
		String strEvent = getStringByEventType(eventType);
		if (startTime >= endTime)
			return null;
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(startTime);
		int startYear = cale.get(Calendar.YEAR);
		int startMonth = cale.get(Calendar.MONTH) + 1;
		cale.setTimeInMillis(endTime);
		int endYear = cale.get(Calendar.YEAR);
		int endMonth = cale.get(Calendar.MONTH) + 1;
		if (startYear == endYear && startMonth == endMonth)
			{
				sql += "select * from virus_day_" + startYear + "_" + startMonth;
				sql += " where stime>=" + startTime + " and etime<=" + endTime
						+ " and deviceid=" + deviceId;
			}
		else
			{
				for (int tempYear = startYear; tempYear <= endYear; tempYear++)
					{
						for (int tempMonth = startMonth; tempMonth <= 12; tempMonth++)
							{
								if (tempYear == startYear && tempMonth == startMonth)
									{
										sql += "select * from " + strEvent + "_day_"
												+ tempYear + "_" + tempMonth;
										sql += " where stime>=" + startTime
												+ " and deviceid=" + deviceId;
									}
								else
									if (tempYear == endYear && tempMonth == endMonth)
										{
											sql += " union all select * from " + strEvent
													+ "_day_" + tempYear + "_"
													+ tempMonth;
											sql += " where etime<=" + endTime
													+ " and deviceid=" + deviceId;
										}
									else
										if (tempYear == endYear && tempMonth > endMonth)
											{
												break;
											}
										else
											{
												sql += " union all select * from "
														+ strEvent + "_day_" + tempYear
														+ "_" + tempMonth;
												sql += " where deviceid=" + deviceId;
											}
							}
					}
			}
//		log.debug("getEntSecurityByDayAndType-sql=" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> virusHours = jt.queryForList(psql.getSQL());
		return virusHours;
	}
	/**
	 * 获取终端设备的流量信息
	 * 
	 * @param deviceId
	 *            设备Id号
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return List<Map> 内容
	 */
	public List<Map> getTerminalDevTimesByHour(String deviceId, long startTime,
			long endTime)
	{
		String sql = "";
		long dayIntrval = 3600 * 24;
		if (startTime >= endTime)
			return null;
		Calendar cale = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.CHINESE);
		cale.setTimeInMillis(startTime);
		int startYear = cale.get(Calendar.YEAR);
		int startMonth = cale.get(Calendar.MONTH) + 1;
		int startDay = cale.get(Calendar.DATE);
		cale.setTimeInMillis(endTime);
		int endYear = cale.get(Calendar.YEAR);
		int endMonth = cale.get(Calendar.MONTH) + 1;
		int endDay = cale.get(Calendar.DATE);
		sql += "select a.srcip,sum(a.times) as maxtimes,a.deviceid from(";
		if (startYear == endYear && startMonth == endMonth && startDay == endDay)
			{
				// sql += "select * from
				// conn_hour_"+startYear+"_"+startMonth+"_"+startDay;
				sql += "select * from conn_hour_yyyy_mm_dd";
				sql += " where stime>=" + startTime + " and etime<=" + endTime
						+ " and deviceid='" + deviceId + "'";
			}
		else
			{
				for (long temp = startTime; temp <= endTime; temp += dayIntrval)
					{
						cale.setTimeInMillis(temp);
						int tempYear = cale.get(Calendar.YEAR);
						int tempMonth = cale.get(Calendar.MONTH) + 1;
						int tempDay = cale.get(Calendar.DATE);
						if (tempYear == startYear && tempMonth == startMonth
								&& tempDay == startDay)
							{
								// sql += "select * from
								// conn_hour_"+tempYear+"_"+tempMonth+"_"+tempDay;
								sql += "select * from conn_hour_yyyy_mm_dd";
								sql += " where stime>=" + temp + " and deviceid='"
										+ deviceId + "'";
							}
						else
							if (tempYear == endYear && tempMonth == endMonth
									&& tempDay == endDay)
								{
									// sql += " union all select * from
									// conn_hour_"+tempYear+"_"+tempMonth+"_"+tempDay;
									sql += " union all select * from conn_hour_yyyy_mm_dd";
									sql += " where etime<=" + temp + " and deviceid='"
											+ deviceId + "'";
								}
							else
								{
									// sql += " union all select * from
									// conn_hour_"+tempYear+"_"+tempMonth+"_"+tempDay;
									sql += " union all select * from conn_hour_yyyy_mm_dd";
									sql += " where deviceid='" + deviceId + "'";
								}
					}
			}
		sql += ") a group by a.srcip,a.deviceid order by maxtimes desc ";
//		log.debug("getTerminalDevFluxByHour-sql=" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> devDimes = jt.queryForList(psql.getSQL());
		return devDimes;
	}
	/**
	 * 获取网络访问信息
	 * 
	 * @param deviceId
	 *            设备Id号
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return List<Map> 内容
	 */
	public List<Map> getWebVistTimesByHour(String deviceId, long startTime, long endTime)
	{
		String sql = "";
		long dayIntrval = 3600 * 24;
		if (startTime >= endTime)
			return null;
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(startTime);
		int startYear = cale.get(Calendar.YEAR);
		int startMonth = cale.get(Calendar.MONTH) + 1;
		int startDay = cale.get(Calendar.DATE);
		cale.setTimeInMillis(endTime);
		int endYear = cale.get(Calendar.YEAR);
		int endMonth = cale.get(Calendar.MONTH) + 1;
		int endDay = cale.get(Calendar.DATE);
		sql += "select a.targetip,sum(a.times) as maxtimes,a.deviceid from(";
		if (startYear == endYear && startMonth == endMonth && startDay == endDay)
			{
				// sql += "select * from
				// conn_hour_"+startYear+"_"+startMonth+"_"+startDay;
				sql += "select * from conn_hour_yyyy_mm_dd";
				sql += " where stime>=" + startTime + " and etime<=" + endTime
						+ " and deviceid='" + deviceId + "'";
			}
		else
			{
				for (long temp = startTime; temp <= endTime; temp += dayIntrval)
					{
						cale.setTimeInMillis(temp);
						int tempYear = cale.get(Calendar.YEAR);
						int tempMonth = cale.get(Calendar.MONTH) + 1;
						int tempDay = cale.get(Calendar.DATE);
						if (tempYear == startYear && tempMonth == startMonth
								&& tempDay == startDay)
							{
								// sql += "select * from
								// conn_hour_"+tempYear+"_"+tempMonth+"_"+tempDay;
								sql += "select * from conn_hour_yyyy_mm_dd";
								sql += " where stime>=" + temp + " and deviceid='"
										+ deviceId + "'";
							}
						else
							if (tempYear == endYear && tempMonth == endMonth
									&& tempDay == endDay)
								{
									// sql += " union all select * from
									// conn_hour_"+tempYear+"_"+tempMonth+"_"+tempDay;
									sql += " union all select * from conn_hour_yyyy_mm_dd";
									sql += " where etime<=" + temp + " and deviceid='"
											+ deviceId + "'";
								}
							else
								{
									// sql += " union all select * from
									// conn_hour_"+tempYear+"_"+tempMonth+"_"+tempDay;
									sql += " union all select * from conn_hour_yyyy_mm_dd";
									sql += " where deviceid='" + deviceId + "'";
								}
					}
			}
		sql += ") a group by a.targetip,a.deviceid order by maxtimes desc ";
//		log.debug("getWebVistTimesByHour-sql=" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> webDimes = jt.queryForList(psql.getSQL());
		return webDimes;
	}
	/**
	 * 通过查询条件获取病毒的原始表 提供病毒详细数据的查询
	 * 
	 * @param deviceId
	 *            设备Id号
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param virusType
	 *            病毒类型
	 * @param srcIp
	 *            源用户IP
	 * @param opType
	 *            处理方式
	 * @return List<Map> 数据
	 */
	public String getVirusOrigDataSQL(String deviceId, long startTime, long endTime,
			int virusType, String srcIp, int opType)
	{
		String sql = "";
		long dayIntrval = 3600000 * 24;
		if (startTime >= endTime)
			return null;
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(startTime);
		int startYear = cale.get(Calendar.YEAR);
		int startMonth = cale.get(Calendar.MONTH) + 1;
		int startDay = cale.get(Calendar.DATE);
		cale.setTimeInMillis(endTime);
		int endYear = cale.get(Calendar.YEAR);
		int endMonth = cale.get(Calendar.MONTH) + 1;
		int endDay = cale.get(Calendar.DATE);
		String smonth = (startMonth < 10) ? ("0" + startMonth) : (startMonth + "");
		String emonth = (endMonth < 10) ? ("0" + endMonth) : (endMonth + "");
		String sday = (startDay < 10) ? ("0" + startDay) : (startDay + "");
		String eday = (endDay < 10) ? ("0" + endDay) : (endDay + "");
		String tyear = "" , tmonth = "" , tday = "";
		String tempTableName = "";
		boolean isStart = true;
		Map tableNamemap = CommonUtil.getExistTables("sgw_virus_original");
		if (tableNamemap == null)
			{
				return sql;
			}
 
		if (startYear == endYear && startMonth == endMonth && startDay == endDay)
			{
				tempTableName = "sgw_virus_original_" + startYear + "_" + smonth + "_"
						+ sday;
//				logger.debug(tempTableName);
				if (tableNamemap.containsKey(tempTableName))
					{
						sql += "select * from " + tempTableName;
						sql += " as a left outer join sgw_virus_info as b on  a.virustype=b.virustype ";
						sql += " where stime>=" + startTime / 1000 + " and etime<="
								+ endTime / 1000 + " and deviceid='" + deviceId + "'";
					}
			}
		else
			{
				for (long temp = startTime; temp <= endTime; temp += dayIntrval)
					{
						cale.setTimeInMillis(temp);
						int tempYear = cale.get(Calendar.YEAR);
						int tempMonth = cale.get(Calendar.MONTH) + 1;
						int tempDay = cale.get(Calendar.DATE);
						if (tempYear == startYear && tempMonth == startMonth
								&& tempDay == startDay)
							{
								tempTableName = "sgw_virus_original_" + startYear + "_"
										+ smonth + "_" + sday;
								if (tableNamemap.containsKey(tempTableName))
									{
										sql += (isStart)?"":"union all ";
										sql += "select * from " + tempTableName;
										sql += "  as a left outer join sgw_virus_info as b on  a.virustype=b.virustype ";
										sql += " where stime>=" + startTime / 1000
												+ " and deviceid='" + deviceId + "'";
										isStart = false;
									}
							}
						else
							if (tempYear == endYear && tempMonth == endMonth
									&& tempDay == endDay)
								{
									tempTableName = "sgw_virus_original_" + endYear + "_"
											+ emonth + "_" + eday;
									if (tableNamemap.containsKey(tempTableName))
										{
											sql += (isStart)?"":"union all ";
											sql += "select * from "
													+ tempTableName;
											// sql += " union all select * from
											// sgw_virus_original_yyyy_mm_dd ";
											sql += " as a left outer join sgw_virus_info as b on  a.virustype=b.virustype ";
											sql += " where etime<=" + endTime / 1000
													+ " and deviceid='" + deviceId + "'";
											isStart = false;
										}
								}
							else
								{
									tyear = (tempYear < 10) ? ("0" + tempYear)
											: (tempYear + "");
									tmonth = (tempMonth < 10) ? ("0" + tempMonth)
											: (tempMonth + "");
									tday = (tempDay < 10) ? ("0" + tempDay)
											: (tempDay + "");
									tempTableName = "sgw_virus_original_" + tyear + "_"
											+ tmonth + "_" + tday;
									if (tableNamemap.containsKey(tempTableName))
										{
											sql += (isStart)?"":"union all ";
											sql += " select * from "
													+ tempTableName;
											// sql += " union all select * from
											// sgw_virus_original_yyyy_mm_dd ";
											sql += " as a left outer join sgw_virus_info as b on  a.virustype=b.virustype ";
											sql += " where deviceid='" + deviceId + "'";
											isStart = false;
										}
								}
					}
			}
//		sql += " order by stime desc"; 
//		log.debug("sql=" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		return psql.getSQL();
	}
	public int getCountForOrginData(String sql)
	{	
		if (null == sql || "".equals(sql)) {
			return 0;
		}
		String sqlCount = "select count(*) as c from(" + sql + ") a";
		PrepareSQL psql = new PrepareSQL(sqlCount);
		Map count = jt.queryForMap(psql.getSQL());
		return Integer.parseInt(count.get("c").toString());
	}
	public List<Map> getVirusOrigData(String sql, int curPage, int rowCount)
	{
		spjt.setMaxRows(10000); // 设置需要查询数据的总数
		spjt.setFetchSize(100);// 设置一次查询数据的数量
		List<Map> list = new ArrayList();
		try
			{
				list = ("".equals(sql)) ? null : spjt.querySP(sql, (curPage - 1)
						* rowCount, rowCount, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						Map m = new HashMap();
						m.put("deviceid", rs.getString("deviceid"));
						m.put("targetip", rs.getObject("targetip"));
						m.put("srcip", rs.getString("srcip"));
						m.put("targetmac", rs.getString("targetmac"));
						m.put("srcmac", rs.getString("srcmac"));
						m.put("virustype", rs.getInt("virustype"));
						m.put("operation", rs.getInt("operation"));
						m.put("virustimes", rs.getLong("virustimes"));
						m.put("virusremark", rs.getString("remark")); 
						m.put("stime", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", rs
								.getLong("stime")));
						m.put("etime", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", rs
								.getLong("etime")));
						m.put("virusname", rs.getString("virusname"));
						return m;
					}
				});
			}
		catch (Exception e)
			{
				log.error(e.toString());
			}
		return list;
	}
	/**
	 * 通过查询条件获取攻击的原始表 提供攻击详细数据的查询
	 * 
	 * @param deviceId
	 *            设备Id号
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param attackType
	 *            攻击类型
	 * @param srcIp
	 *            源用户IP
	 * @return List<Map> 数据
	 */
	public String getAttackOrigDataSQL(String deviceId, long startTime, long endTime,
			int attackType, String srcIp)
	{
		String sql = "";
		long dayIntrval = 3600000 * 24;
		if (startTime >= endTime)
			return null;
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(startTime);
		int startYear = cale.get(Calendar.YEAR);
		int startMonth = cale.get(Calendar.MONTH) + 1;
		int startDay = cale.get(Calendar.DATE);
		cale.setTimeInMillis(endTime);
		int endYear = cale.get(Calendar.YEAR);
		int endMonth = cale.get(Calendar.MONTH) + 1;
		int endDay = cale.get(Calendar.DATE);
		String smonth = (startMonth < 10) ? ("0" + startMonth) : (startMonth + "");
		String emonth = (endMonth < 10) ? ("0" + endMonth) : (endMonth + "");
		String sday = (startDay < 10) ? ("0" + startDay) : (startDay + "");
		String eday = (endDay < 10) ? ("0" + endDay) : (endDay + "");
		String tyear = "" , tmonth = "" , tday = "";
		String tempTableName = "";
		boolean isStart = true;
		Map tableNamemap = CommonUtil.getExistTables("sgw_attack_original_");
		if (tableNamemap == null)
			{
				return sql;
			}
		if (startYear == endYear && startMonth == endMonth && startDay == endDay)
			{
				tempTableName = "sgw_attack_original_" + startYear + "_" + smonth + "_"
						+ sday;
				if (tableNamemap.containsKey(tempTableName))
					{
						sql += "select * from " + tempTableName;
						// sql += "select * from
						// sgw_attack_original_yyyy_mm_dd";
						sql += " as a left outer join sgw_attack_info as b on  a.attacktype=b.attacktype ";
						sql += " where stime>=" + startTime / 1000 + " and etime<="
								+ endTime / 1000 + " and deviceid='" + deviceId + "'";
					}
			}
		else
			{
				for (long temp = startTime; temp <= endTime; temp += dayIntrval)
					{
						cale.setTimeInMillis(temp);
						int tempYear = cale.get(Calendar.YEAR);
						int tempMonth = cale.get(Calendar.MONTH) + 1;
						int tempDay = cale.get(Calendar.DATE);
						if (tempYear == startYear && tempMonth == startMonth
								&& tempDay == startDay)
							{
								tempTableName = "sgw_attack_original_" + startYear + "_"
										+ smonth + "_" + sday;
								if (tableNamemap.containsKey(tempTableName))
									{
										sql += (isStart)?"":"union all ";
										sql += "select * from " + tempTableName;
										// sql += "select * from
										// sgw_attack_original_yyyy_mm_dd ";
										sql += " as a left outer join sgw_attack_info as b on  a.attacktype=b.attacktype ";
										sql += " where stime>=" + startTime / 1000
												+ " and deviceid='" + deviceId + "'";
										isStart = false;
										
									}
							}
						else
							if (tempYear == endYear && tempMonth == endMonth
									&& tempDay == endDay)
								{
									tempTableName = "sgw_attack_original_" + endYear
											+ "_" + emonth + "_" + eday;
									if (tableNamemap.containsKey(tempTableName))
										{
											sql += (isStart)?"":"union all ";
											sql += " select * from "
													+ tempTableName;
											// sql += " union all select * from
											// sgw_attack_original_yyyy_mm_dd ";
											sql += " as a left outer join sgw_attack_info as b on  a.attacktype=b.attacktype ";
											sql += " where etime<=" + endTime / 1000
													+ " and deviceid='" + deviceId + "'";
											isStart = false;
										}
								}
							else
								{
									tyear = (tempYear < 10) ? ("0" + tempYear)
											: (tempYear + "");
									tmonth = (tempMonth < 10) ? ("0" + tempMonth)
											: (tempMonth + "");
									tday = (tempDay < 10) ? ("0" + tempDay)
											: (tempDay + "");
									tempTableName = "sgw_attack_original_" + tyear + "_"
											+ tmonth + "_" + tday;
									if (tableNamemap.containsKey(tempTableName))
										{
											sql += (isStart)?"":"union all ";
											sql += "  select * from "
													+ tempTableName;
											// sql += " union all select * from
											// sgw_attack_original_yyyy_mm_dd ";
											sql += " as a left outer join sgw_attack_info as b on  a.attacktype=b.attacktype ";
											sql += " where deviceid='" + deviceId + "'";
											isStart = false;
										}
								}
					}
			}
//		sql += " order by stime desc";

//		log.debug("sql=" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		return psql.getSQL();
	}
	public List<Map> getAttackOrigData(String sql, int curPage, int rowCount)
	{
		spjt.setMaxRows(10000); // 设置需要查询数据的总数
		spjt.setFetchSize(100);// 设置一次查询数据的数量
		return ("".equals(sql)) ? null : spjt.querySP(sql, (curPage - 1) * rowCount,
				rowCount, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						Map m = new HashMap();
						m.put("deviceid", rs.getString("deviceid"));
						m.put("targetip", rs.getObject("targetip"));
						m.put("srcip", rs.getString("srcip"));
						m.put("targetmac", rs.getString("targetmac"));
						m.put("srcmac", rs.getString("srcmac"));
						m.put("attacktype", rs.getInt("attacktype"));
						m.put("mark", rs.getString("mark"));
						m.put("attacktimes", rs.getLong("attacktimes"));
						m.put("stime", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", rs
								.getLong("stime")));
						m.put("etime", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", rs
								.getLong("etime")));
						m.put("attackname", rs.getString("attackname"));
						return m;
					}
				});
	}
	/**
	 * 通过查询条件获取垃圾邮件的原始表 提供攻击详细数据的查询
	 * 
	 * @param deviceId
	 *            设备Id号
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return List<Map> 数据
	 */
	public String getSpamMailOrigDataSQL(String deviceId, long startTime, long endTime)
	{
		String sql = "";
		long dayIntrval = 3600000 * 24;
		if (startTime >= endTime)
			return null;
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(startTime);
		int startYear = cale.get(Calendar.YEAR);
		int startMonth = cale.get(Calendar.MONTH) + 1;
		int startDay = cale.get(Calendar.DATE);
		cale.setTimeInMillis(endTime);
		int endYear = cale.get(Calendar.YEAR);
		int endMonth = cale.get(Calendar.MONTH) + 1;
		int endDay = cale.get(Calendar.DATE);
		String smonth = (startMonth < 10) ? ("0" + startMonth) : (startMonth + "");
		String emonth = (endMonth < 10) ? ("0" + endMonth) : (endMonth + "");
		String sday = (startDay < 10) ? ("0" + startDay) : (startDay + "");
		String eday = (endDay < 10) ? ("0" + endDay) : (endDay + "");
		String tyear = "" , tmonth = "" , tday = "";
		String tempTableName = "";
		boolean isStart = true;

		Map tableNamemap = CommonUtil.getExistTables("sgw_mail_original_");
		if (tableNamemap == null)
			{
				return sql;
			}
		if (startYear == endYear && startMonth == endMonth && startDay == endDay)
			{
				tempTableName = "sgw_mail_original_" + startYear + "_" + startMonth + "_"
						+ startDay;
				if (tableNamemap.containsKey(tempTableName))
					{
						sql += "select * from " + tempTableName;
						// sql += "select * from sgw_mail_original_yyyy_mm_dd";
						sql += " where stime>=" + startTime / 1000 + " and etime<="
								+ endTime / 1000 + " and deviceid='" + deviceId + "'";
					}
			}
		else
			{
				for (long temp = startTime; temp <= endTime; temp += dayIntrval)
					{
						cale.setTimeInMillis(temp);
						int tempYear = cale.get(Calendar.YEAR);
						int tempMonth = cale.get(Calendar.MONTH) + 1;
						int tempDay = cale.get(Calendar.DATE);
						if (tempYear == startYear && tempMonth == startMonth
								&& tempDay == startDay)
							{
							log.debug("1");
								tempTableName = "sgw_mail_original_" + startYear + "_"
										+ smonth + "_" + sday;
								if (tableNamemap.containsKey(tempTableName))
									{
										sql += (isStart)?"":"union all ";
										sql += "select * from " + tempTableName;
										// sql += "select * from
										// sgw_mail_original_yyyy_mm_dd";
										sql += " where stime>=" + startTime / 1000
												+ " and deviceid='" + deviceId + "'";
										isStart = false;
									} 
							}
						else
							if (tempYear == endYear && tempMonth == endMonth
									&& tempDay == endDay)
								{
								log.debug("2");
									tempTableName = "sgw_mail_original_" + startYear
											+ "_" + smonth + "_" + sday;
									if (tableNamemap.containsKey(tempTableName))
										{
											sql += (isStart)?"":"union all ";
											sql += "  select * from "
													+ tempTableName;
											// sql += " union all select * from
											// sgw_mail_original_yyyy_mm_dd";
											sql += " where etime<=" + endTime / 1000
													+ " and deviceid='" + deviceId + "'";
											isStart = false;
										}
								}
							else
								{
								log.debug("3");
									tyear = (tempYear < 10) ? ("0" + tempYear)
											: (tempYear + "");
									tmonth = (tempMonth < 10) ? ("0" + tempMonth)
											: (tempMonth + "");
									tday = (tempDay < 10) ? ("0" + tempDay)
											: (tempDay + "");
									tempTableName = "sgw_attack_original_" + tyear + "_"
											+ tmonth + "_" + tday;
									if (tableNamemap.containsKey(tempTableName))
										{
											sql += (isStart)?"":"union all ";
											sql += " select * from "
													+ tempTableName;
											sql += " where deviceid='" + deviceId + "'";
											isStart = false;
										}
								}
					}
			}
//		sql += " order by stime desc";

//		log.debug("sql=" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		return psql.getSQL();
	}
	public List<Map> getSpamMailOrigData(String sql, int curPage, int rowCount)
	{
		spjt.setMaxRows(10000); // 设置需要查询数据的总数
		spjt.setFetchSize(100);// 设置一次查询数据的数量
		return ("".equals(sql)) ? null : spjt.querySP(sql, (curPage - 1) * rowCount,
				rowCount, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						Map m = new HashMap();
						m.put("deviceid", rs.getString("deviceid"));
						m.put("targetip", rs.getObject("targetip"));
						m.put("srcip", rs.getString("srcip"));
						m.put("targetmac", rs.getString("targetmac"));
						m.put("srcmac", rs.getString("srcmac"));
						m.put("mailsenderaddress", rs.getString("mailsenderaddress"));
						m.put("mailtitle", rs.getString("mailtitle"));
						m.put("maildetail", rs.getString("maildetail"));
						m.put("mailtimes", rs.getLong("mailtimes"));
						m.put("stime", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", rs
								.getLong("stime")));
						m.put("etime", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", rs
								.getLong("etime")));
						return m;
					}
				});
	}
	/**
	 * 通过查询条件获取过滤事件的原始表 提供攻击详细数据的查询
	 * 
	 * @param deviceId
	 *            设备Id号
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return List<Map> 数据
	 */
	public String getOrigDataSQL(String deviceId, long startTime, long endTime,
			String tableName)
	{
		String sql = "";
		long dayIntrval = 3600000 * 24;
		if (startTime >= endTime)
			return null;
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(startTime);
		int startYear = cale.get(Calendar.YEAR);
		int startMonth = cale.get(Calendar.MONTH) + 1;
		int startDay = cale.get(Calendar.DATE);
		cale.setTimeInMillis(endTime);
		int endYear = cale.get(Calendar.YEAR);
		int endMonth = cale.get(Calendar.MONTH) + 1;
		int endDay = cale.get(Calendar.DATE);
		// logger.debug("startYear="+startYear+"startMonth="+startMonth+"startDay="+startDay);
		// logger.debug("endYear="+endYear+"endMonth="+endMonth+"endDay="+endDay);
		String smonth = (startMonth < 10) ? ("0" + startMonth) : (startMonth + "");
		String emonth = (endMonth < 10) ? ("0" + endMonth) : (endMonth + "");
		String sday = (startDay < 10) ? ("0" + startDay) : (startDay + "");
		String eday = (endDay < 10) ? ("0" + endDay) : (endDay + "");
		String tyear = "" , tmonth = "" , tday = "";
		String tempTableName = "";
		boolean isStart = true;
		Map tableNamemap = CommonUtil.getExistTables(tableName);
		if (tableNamemap == null)
			{
				return sql;
			}
		if (startYear == endYear && startMonth == endMonth && startDay == endDay)
			{
				tempTableName = tableName + "_" + startYear + "_" + smonth + "_" + sday;
				if (tableNamemap.containsKey(tempTableName))
					{
						sql += "select * from " + tempTableName;
						sql += " where stime>=" + startTime / 1000 + " and etime<="
								+ endTime / 1000 + " and deviceid='" + deviceId + "'";
 
					}
			}
		else
			{
				for (long temp = startTime; temp <= endTime; temp += dayIntrval)
					{
						cale.setTimeInMillis(temp);
						int tempYear = cale.get(Calendar.YEAR);
						int tempMonth = cale.get(Calendar.MONTH) + 1;
						int tempDay = cale.get(Calendar.DATE);
						log.debug("tempYear=" + tempYear + "tempYear="
								+ tempYear + "tempDay=" + tempDay);
						if (tempYear == startYear && tempMonth == startMonth
								&& tempDay == startDay)
							{
								tempTableName = tableName + "_" + startYear + "_"
										+ smonth + "_" + sday;
								if (tableNamemap.containsKey(tempTableName))
									{
										sql += "select * from " + tempTableName;
										// sql += "select * from
										// sgw_filter_original_yyyy_mm_dd";
										sql += " where stime>=" + startTime / 1000
												+ " and deviceid='" + deviceId + "'";
										isStart = false;
									}
							}
						else
							if (tempYear == endYear && tempMonth == endMonth
									&& tempDay == endDay)
								{
									tempTableName = tableName + "_" + endYear + "_"
											+ emonth + "_" + eday;
									if (tableNamemap.containsKey(tempTableName))
										{
											sql += (isStart)?"":"union all ";
											sql += "select * from "
													+ tempTableName;
											sql += " where etime<=" + endTime / 1000
													+ " and deviceid='" + deviceId + "'";
											isStart = false;
										}
								}
							else
								{
									tyear = (tempYear < 10) ? ("0" + tempYear)
											: (tempYear + "");
									tmonth = (tempMonth < 10) ? ("0" + tempMonth)
											: (tempMonth + "");
									tday = (tempDay < 10) ? ("0" + tempDay)
											: (tempDay + "");
									tempTableName = tableName + "_" + tyear + "_"
											+ tmonth + "_" + tday;
									if (tableNamemap.containsKey(tempTableName))
										{
											sql += (isStart)?"":"union all ";
											sql += "select * from "
													+ tempTableName;
											sql += " where deviceid='" + deviceId + "'";
											isStart = false;
										}
								}
					}
			}
//		log.debug("sql=" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		return psql.getSQL();
	}
	public List<Map> getFilterOrigData(String sql, int curPage, int rowCount)
	{
		spjt.setMaxRows(10000); // 设置需要查询数据的总数
		spjt.setFetchSize(100);// 设置一次查询数据的数量
		return ("".equals(sql)) ? null : spjt.querySP(sql, (curPage - 1) * rowCount,
				rowCount, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						Map m = new HashMap();
						m.put("deviceid", rs.getString("deviceid"));
						m.put("targetip", rs.getObject("targetip"));
						m.put("srcip", rs.getString("srcip"));
						m.put("targetmac", rs.getString("targetmac"));
						m.put("srcmac", rs.getString("srcmac"));
						m.put("filterid", rs.getString("filterid"));
						m.put("content", rs.getString("content"));
						m.put("filtertimes", rs.getLong("filtertimes"));
						m.put("stime", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", rs
								.getLong("stime")));
						m.put("etime", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", rs
								.getLong("etime")));
						return m;
					}
				});
	}
}
