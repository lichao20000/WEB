package com.linkage.liposs.buss.dao.securitygw;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.liposs.common.util.DbUtil;

/**
 * 接口GetPBMailTopNHighDAO的实现类:获取员工浏览mail的topN信息
 * 
 * @author suixz(5253)
 * @version 1.0
 * @category securitygw
 */
public class GetPBMailTopNHighDAOImp implements GetPBMailTopNHighDAO
{
	private JdbcTemplate jt;
	/**
	 * 查询员工浏览mail的topN信息
	 * 
	 * @param deviceId
	 *            设备id
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @throws ParseException
	 * @return List
	 */
	public List<Map> getMailTopNData(String deviceId, String startTime, String endTime)
			throws ParseException
	{
		List<Map> list = null;
		if (startTime.equals(endTime))
			{// 查询时间在同一天内
				String tableName = "sgw_conn_hour_";
				SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
				tableName += format.format(DateFormat.getDateInstance().parse(startTime));
				String sql = "select srcip, sum(times) times, 'smtp,pop3' yType from "
						+ tableName
						+ " where deviceid='"
						+ deviceId
						+ "' and (protocoltype=2 or protocoltype=3) group by srcip order by times desc";
				PrepareSQL psql = new PrepareSQL(sql);
				list = jt.queryForList(psql.getSQL());
			}
		else
			{// 查询时间不在同一天内
				long sTime = DateFormat.getDateInstance().parse(startTime).getTime() / 1000L;
				long eTime = DateFormat.getDateInstance().parse(endTime).getTime() / 1000L;
				List<String> tableName = DbUtil.createTableNames(DateFormat
						.getDateInstance().parse(startTime), DateFormat.getDateInstance()
						.parse(endTime), Calendar.MONTH, 1, "sgw_conn_day_", "yyyy_MM");
				String sql = "select srcip, sum(times) times, 'smtp,pop3' yType from(";
				boolean flag = true;
				for (String tab : tableName)
					{
						if (flag == true)
							{
								sql += "select srcip, sum(times) times from "
										+ tab
										+ " where deviceid='"
										+ deviceId
										+ "' and (protocoltype=2 or protocoltype=3) and stime>="
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
										+ "' and (protocoltype=2 or protocoltype=3) and stime>="
										+ sTime + " and etime<=" + eTime
										+ " group by srcip";
							}
					}
				sql += " ) t  group by srcip order by times desc ";
				PrepareSQL psql = new PrepareSQL(sql);
				list = jt.queryForList(psql.getSQL());
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
