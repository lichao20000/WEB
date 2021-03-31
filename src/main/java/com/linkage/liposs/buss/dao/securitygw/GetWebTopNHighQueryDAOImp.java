package com.linkage.liposs.buss.dao.securitygw;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;

/**
 * 获取员工web浏览topN报表信息
 * 
 * @author suixz(5253) 2008-5-6
 * @version 1.0
 * @category securitygw
 */
public class GetWebTopNHighQueryDAOImp implements GetWebTopNHighQueryDAO
{
	private JdbcTemplate jt;
	/**
	 * 获取
	 * 
	 * @param deviceId
	 *            设备id
	 * @param startTime
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 * @param topN
	 *            前n名
	 * @return List
	 * @throws ParseException
	 */
	public List<Map> getWebTopNHighQuery(String deviceId, String startTime,
			String endTime, int topN) throws ParseException
	{
		List<Map> list = null;
		if (startTime.equals(endTime))
			{// 查询时间在同一天内
				String tableName = "sgw_conn_hour_";
				SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
				tableName += format.format(DateFormat.getDateInstance().parse(startTime));
				String sql = "select srcip, sum(times) times, 'http' yType from "
						+ tableName + " where deviceid='" + deviceId
						+ "' and protocoltype=0 group by srcip order by times desc";
				PrepareSQL psql = new PrepareSQL(sql);
				list = jt.queryForList(psql.getSQL());
//				if (list.size() > topN)
//					{
//						return list.subList(0, topN);
//					}
			}
		else
			{// 查询时间不在同一天内
				String sql = "";
				List<String> tableName = new ArrayList<String>();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-01");
				String stime = format.format(DateFormat.getDateInstance()
						.parse(startTime));
				String etime = format.format(DateFormat.getDateInstance().parse(endTime));
				Calendar sc = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));// 开始时间
				sc.setTime(DateFormat.getDateInstance().parse(stime));
				Calendar ec = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));// 结束时间
				ec.setTime(DateFormat.getDateInstance().parse(etime));
				long sTime = DateFormat.getDateInstance().parse(startTime).getTime() / 1000L;
				long eTime = DateFormat.getDateInstance().parse(endTime).getTime() / 1000L;
				if (sc.get(Calendar.MONTH) != ec.get(Calendar.MONTH))
					{// 查询时间不在同一月内
						// format.applyLocalizedPattern("yyyy_MM");
						while (sc.getTime().getTime() <= ec.getTime().getTime())
							{
								tableName.add("sgw_conn_day_"
										+ (sc.get(Calendar.YEAR))
										+ "_"
										+ ((sc.get(Calendar.MONTH) + 1) < 10 ? ("0" + (sc
												.get(Calendar.MONTH) + 1)) : (sc
												.get(Calendar.MONTH) + 1)));
								sc.add(Calendar.MONTH, 1);
							}
						sql = "select srcip, sum(times) times, 'http' yType from(";
						boolean flag = true;
						for (String tab : tableName)
							{
								if (flag == true)
									{
										sql += "select srcip, sum(times) times from "
												+ tab + " where deviceid='" + deviceId
												+ "' and protocoltype=0 and stime>="
												+ sTime + " and etime<=" + eTime
												+ " group by srcip";
										flag = false;
									}
								else
									{
										sql += " union all select srcip, sum(times) times from "
												+ tab
												+ " where deviceid='"
												+ deviceId
												+ "' and protocoltype=0 and stime>="
												+ sTime
												+ " and etime<="
												+ eTime
												+ " group by srcip";
									}
							}
						sql += " ) t  group by srcip order by times desc ";
						PrepareSQL psql = new PrepareSQL(sql);
						list = jt.queryForList(psql.getSQL());
//						if (list.size() > topN)
//							{
//								return list.subList(0, topN);
//							}
					}
				else
					{// 查询时间在同一月内,但不在同一天
						String tab = "sgw_conn_day_"
								+ (sc.get(Calendar.YEAR))
								+ "_"
								+ (sc.get(Calendar.MONTH) + 1 < 10 ? ("0" + (sc
										.get(Calendar.MONTH) + 1)) : (sc
										.get(Calendar.MONTH) + 1));
						sql += "select srcip, sum(times) times,'http' yType from " + tab
								+ " where deviceid='" + deviceId
								+ "' and protocoltype=0 and stime>=" + sTime
								+ " and etime<=" + eTime
								+ " group by srcip order by times desc";
						PrepareSQL psql = new PrepareSQL(sql);
						list = jt.queryForList(psql.getSQL());
//						if (list.size() > topN)
//							{
//								return list.subList(0, topN);
//							}
					}
			}
		return list;
	}
	/**
	 * 注入数据源
	 * 
	 * @param dao
	 *            数据源
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
}
