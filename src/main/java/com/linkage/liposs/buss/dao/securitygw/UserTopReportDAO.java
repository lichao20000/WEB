package com.linkage.liposs.buss.dao.securitygw;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.liposs.buss.util.ChartUtil;
import com.linkage.liposs.buss.util.ChartYType;
import com.linkage.liposs.common.util.DbUtil;
import com.linkage.litms.common.util.StringUtils;

/**
 * @author 王志猛(5194) tel：13701409234
 * @version 1.0
 * @since 2008-4-3
 * @category com.linkage.liposs.buss.dao.securitygw 版权：南京联创科技 网管科技部
 * 
 */
public class UserTopReportDAO
{
	/**
	 * 日报表常量
	 */
	final static public int DCHART = 0;
	/**
	 * 周报表常量
	 */
	final static public int WCHART = 1;
	/**
	 * 月报表常量
	 */
	final static public int MCHART = 2;
	/**
	 * 高级查询
	 */
	final static public int AD = 3;
	private JdbcTemplate jt;
	static private String getFunInfoSql = "select a.loopback_ip,d.customer_name as customname,a.device_model_id as device_model,b.virustimes ,b.ashmailtimes ,b.attacktimes ,b.filtertimes ,c.severity"
			+ " from tab_gw_device a "
			+ "left join sgw_security_static b on a.device_id=b.deviceid "
			+ "left join tab_customerinfo d on a.customer_id=d.customer_id "
			+ "left join tab_taskplan_data c on a.device_id=c.device_id where a.device_id=?";
	static private String getVSql = "select srcip,sum(virustimes) as virustimes,virusname from ";
	static private String getVConsSql = " a left join sgw_virus_info b on a.virustype=b.virustype where deviceid=? and a.virustype=? and etime<? and etime>=? group by a.virustype,srcip,virusname order by virustimes";
	static private String unionGetVConsSql = " a left join sgw_virus_info b on a.virustype=b.virustype where deviceid=? and a.virustype=? and etime<? and etime>=? group by a.virustype,srcip,virusname";
	static private String getMSql = "select srcip,sum(mailtimes) as mailtimes, '垃圾邮件次数' colName from ";
	static private String getMconsSql = " where deviceid=? and etime<? and etime>? group by srcip order by mailtimes";
	static private String unionGetMconsSql = " where deviceid=? and etime<? and etime>? group by srcip";
	static private String getFSql = "select srcip,sum(filtertimes) as filtertimes,'过滤用户次数' colName from ";
	static private String getFconsSql = " where deviceid=? and etime<? and etime>? group by srcip order by filtertimes";
	static private String unionGetFconsSql = " where deviceid=? and etime<? and etime>? group by srcip";
	static private String getASql = "select srcip,sum(attacktimes) as attacktimes,attackname from ";
	static private String getAConsSql = " a left join sgw_attack_info b on a.attacktype=b.attacktype where deviceid=? and a.attacktype=? and etime<? and etime>=? group by a.attacktype,srcip,attackname order by attacktimes";
	static private String unionGetAConsSql = " a left join sgw_attack_info b on a.attacktype=b.attacktype where deviceid=? and a.attacktype=? and etime<? and etime>=? group by a.attacktype,srcip,attackname";
	private PrepareSQL ppSql;// 用来组装sql语句的
	private ChartUtil chartUtil;//
	private static Logger log = LoggerFactory.getLogger(UserTopReportDAO.class);
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
	/**
	 * 提供报表信息的左上角的设备信息
	 * 
	 * @param device_id
	 *            需要查询的设备信息
	 * @return Map 内部字段:<br>
	 *         a.loopback_ip,d.customname,a.device_model,b.virustimes
	 *         ,b.ashmailtimes ,b.attacktimes ,b.filtertimes ,c.severity
	 */
	public Map<String, String> getFunInfo(String device_id)
	{
		ppSql.setSQL(getFunInfoSql);
		ppSql.setString(1, device_id);
		List<Map> res = jt.queryForList(ppSql.getSQL());
		return res.size() == 0 ? null : res.get(0);
	}
	/**
	 * 获取病毒的用户前十名报表图
	 * 
	 * @param type
	 *            报表的类型，日、周、月 请使用该dao类的三个常量指定
	 * @param stime
	 *            结束时间
	 * @param etime
	 *            结束时间
	 * @param vType
	 *            病毒类型
	 * @param device_id
	 *            设备id
	 * @return JFreechart
	 */
	public JFreeChart getVChart(int type, long stime, long etime, int vType,
			String device_id)
	{
		chartUtil.setCLablePosition(ChartYType.DOWN_30);
		JFreeChart chart = null;
		if (type == this.DCHART)
			{
				String tn = "";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date ds = c.getTime();
				c.add(Calendar.DAY_OF_MONTH, 1);
				Date dt = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn = "sgw_virus_hour_" + y + "_" + (m < 10 ? "0" + m : m) + "_"
						+ (d < 10 ? "0" + d : d);
				ppSql.setSQL(getVSql + tn + getVConsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, vType);
				ppSql.setInt(3, t);
				ppSql.setInt(4, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
				chart = chartUtil.createCategoryBar3DP("病毒用户排名(" + f.format(ds) + "到"
						+ f.format(dt) + ")", "用户IP", "病毒数量", rs, "virustimes", "srcip",
						"virusname", true);
			}
		else if (type == this.WCHART)
			{
				String tn = "";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.WEEK_OF_YEAR, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				tn = "sgw_virus_day_" + y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getVSql + tn + getVConsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, vType);
				ppSql.setInt(3, t);
				ppSql.setInt(4, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				chart = chartUtil.createCategoryBar3DP("病毒用户排名(" + f.format(ds) + "到"
						+ f.format(dt) + ")", "用户IP", "病毒数量", rs, "virustimes", "srcip",
						"virusname", true);
			}
		else if (type == this.MCHART)
			{
				String tn = "";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.MONTH, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				tn = "sgw_virus_day_" + y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getVSql + tn + getVConsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, vType);
				ppSql.setInt(3, t);
				ppSql.setInt(4, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				chart = chartUtil.createCategoryBar3DP("病毒用户排名(" + f.format(ds) + "到"
						+ f.format(dt) + ")", "用户IP", "病毒数量", rs, "virustimes", "srcip",
						"virusname", true);
			}
		else
			{
				Date sd = new Date(stime);
				Date ed = new Date(etime);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				if (f.format(sd).equals(f.format(ed)))
					{
						List<String> tns = DbUtil
								.createTableNames(stime, etime, Calendar.DAY_OF_MONTH, 1,
										"sgw_virus_hour_", "yyyy_MM_dd");
						String tn = tns.get(0);
						ppSql.setSQL(getVSql + tn + getVConsSql);
						ppSql.setString(1, device_id);
						ppSql.setInt(2, vType);
						ppSql.setInt(4, (int) (stime / 1000));
						ppSql.setInt(3, (int) (etime / 1000 + 3600 * 24));
						log.debug("获取高级病毒查询的sql" + ppSql.getSQL());
						List<Map> rs = jt.queryForList(ppSql.getSQL());
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						f = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
						chart = chartUtil.createCategoryBar3DP("病毒用户排名(" + f.format(sd)
								+ "到" + f.format(new Date(stime + 24 * 3600000)) + ")",
								"用户IP", "病毒数量", rs, "virustimes", "srcip", "virusname",
								true);
					}
				else
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.MONTH, 1, "sgw_virus_day_", "yyyy_MM");
						String sql = getVSql + tns.get(0) + unionGetVConsSql;
						ppSql.setSQL(sql);
						ppSql.setString(1, device_id);
						ppSql.setInt(2, vType);
						ppSql.setInt(4, (int) (stime / 1000));
						ppSql.setInt(3, (int) (etime / 1000 + 3600 * 24));
						sql = ppSql.getSQL();
						int lens = tns.size();
						if (lens > 1)
							{
								for (int i = 1; i < lens; i++)
									sql += " union all " + getVSql + tns.get(i)
											+ unionGetVConsSql;
								ppSql.setSQL(sql);
								ppSql.setString(1, device_id);
								ppSql.setInt(2, vType);
								ppSql.setInt(4, (int) (stime / 1000));
								ppSql.setInt(3, (int) (etime / 1000 + 3600 * 24));
								sql = ppSql.getSQL();
							}
						sql = "select srcip,virustimes,virusname from ( " + sql
								+ ") t order by virustimes";
						PrepareSQL psql = new PrepareSQL(sql);
						psql.getSQL();
						List<Map> rs = jt.queryForList(sql);
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						f = new SimpleDateFormat("yyyy_MM_dd");
						chart = chartUtil.createCategoryBar3DP("病毒用户排名(" + f.format(sd)
								+ "到" + f.format(new Date(etime)) + ")", "用户IP", "病毒数量",
								rs, "virustimes", "srcip", "virusname", true);
					}
			}
		return chart;
	}
	/**
	 * 获取病毒分类的topN排名表
	 * 
	 * @param type
	 *            报表类型，使用该dao的常量设置
	 * @param stime
	 *            表报的时间点
	 * @param etime
	 *            报表的结束时间
	 * @param vType
	 *            病毒类型
	 * @param device_id
	 *            设备id
	 * @return 拼装好的table字符串
	 */
	public String getVTable(int type, long stime, long etime, int vType, String device_id)
	{
		StringBuilder tb = new StringBuilder(
				"<table border='1' cellpadding='0' cellspacing='0' style='width:670px;' class='table'>");
		if (type == this.DCHART)
			{
				String tn = "";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date ds = c.getTime();
				c.add(Calendar.DAY_OF_MONTH, 1);
				Date dt = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn = "sgw_virus_hour_" + y + "_" + (m < 10 ? "0" + m : m) + "_"
						+ (d < 10 ? "0" + d : d);
				ppSql.setSQL(getVSql + tn + getVConsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, vType);
				ppSql.setInt(3, t);
				ppSql.setInt(4, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				tb.append("<tr class='tr_yellow'><td>用户</td><td>病毒数量</td></tr>");
				if (rs.size() == 0)
					{
						tb.append("<tr><td colspan=2>没有病毒用户排名数据</td></tr>");
					}
				else
					{
						int p = 0;
						for (Map r : rs)
							{
								tb.append("<tr class='"
										+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
										+ "'><td>" + r.get("srcip").toString()
										+ "</td><td>" + r.get("virustimes").toString()
										+ "</td></tr>");
								p++;
							}
					}
			}
		else if (type == this.WCHART)
			{
				String tn = "";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.WEEK_OF_YEAR, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				tn = "sgw_virus_day_" + y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getVSql + tn + getVConsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, vType);
				ppSql.setInt(3, t);
				ppSql.setInt(4, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				tb.append("<tr class='tr_yellow'><td>用户</td><td>病毒数量</td></tr>");
				if (rs.size() == 0)
					{
						tb.append("<tr><td colspan=2>没有病毒用户排名数据</td></tr>");
					}
				else
					{
						int p = 0;
						for (Map r : rs)
							{
								tb.append("<tr class='"
										+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
										+ "'><td>" + r.get("srcip").toString()
										+ "</td><td>" + r.get("virustimes").toString()
										+ "</td></tr>");
								p++;
							}
					}
			}
		else if (type == this.MCHART)
			{
				String tn = "";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.MONTH, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				tn = "sgw_virus_day_" + y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getVSql + tn + getVConsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, vType);
				ppSql.setInt(3, t);
				ppSql.setInt(4, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				tb.append("<tr class='tr_yellow'><td>用户</td><td>病毒数量</td></tr>");
				if (rs.size() == 0)
					{
						tb.append("<tr><td colspan=2>没有病毒用户排名数据</td></tr>");
					}
				else
					{
						int p = 0;
						for (Map r : rs)
							{
								tb.append("<tr class='"
										+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
										+ "'><td>" + r.get("srcip").toString()
										+ "</td><td>" + r.get("virustimes").toString()
										+ "</td></tr>");
								p++;
							}
					}
			}
		else
			{
				Date sd = new Date(stime);
				Date ed = new Date(etime);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				if (f.format(sd).equals(f.format(ed)))
					{
						List<String> tns = DbUtil
								.createTableNames(stime, etime, Calendar.DAY_OF_MONTH, 1,
										"sgw_virus_hour_", "yyyy_MM_dd");
						String tn = tns.get(0);
						ppSql.setSQL(getVSql + tn + getVConsSql);
						ppSql.setString(1, device_id);
						ppSql.setInt(2, vType);
						ppSql.setInt(4, (int) (stime / 1000));
						ppSql.setInt(3, (int) (etime / 1000 + 3600 * 24));
						log.debug("获取高级病毒查询的sql" + ppSql.getSQL());
						List<Map> rs = jt.queryForList(ppSql.getSQL());
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						tb.append("<tr class='tr_yellow'><td>用户</td><td>病毒数量</td></tr>");
						if (rs.size() == 0)
							{
								tb.append("<tr><td colspan=2>没有病毒用户排名数据</td></tr>");
							}
						else
							{
								int p = 0;
								for (Map r : rs)
									{
										tb
												.append("<tr class='"
														+ ((p % 2 == 0) ? "tr_white"
																: "tr_glory") + "'><td>"
														+ r.get("srcip").toString()
														+ "</td><td>"
														+ r.get("virustimes").toString()
														+ "</td></tr>");
										p++;
									}
							}
					}
				else
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.MONTH, 1, "sgw_virus_day_", "yyyy_MM");
						String sql = getVSql + tns.get(0) + unionGetVConsSql;
						ppSql.setSQL(sql);
						ppSql.setString(1, device_id);
						ppSql.setInt(2, vType);
						ppSql.setInt(4, (int) (stime / 1000));
						ppSql.setInt(3, (int) (etime / 1000 + 3600 * 24));
						sql = ppSql.getSQL();
						int lens = tns.size();
						if (lens > 1)
							{
								for (int i = 1; i < lens; i++)
									sql += " union all " + getVSql + tns.get(i)
											+ unionGetVConsSql;
								ppSql.setSQL(sql);
								ppSql.setString(1, device_id);
								ppSql.setInt(2, vType);
								ppSql.setInt(4, (int) (stime / 1000));
								ppSql.setInt(3, (int) (etime / 1000 + 3600 * 24));
								sql = ppSql.getSQL();
							}
						sql = "select srcip,virustimes,virusname from ( " + sql
								+ ") t order by virustimes";
						PrepareSQL psql = new PrepareSQL(sql);
						psql.getSQL();
						List<Map> rs = jt.queryForList(sql);
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						tb.append("<tr class='tr_yellow'><td>用户</td><td>病毒数量</td></tr>");
						if (rs.size() == 0)
							{
								tb.append("<tr><td colspan=2>没有病毒用户排名数据</td></tr>");
							}
						else
							{
								int p = 0;
								for (Map r : rs)
									{
										tb
												.append("<tr class='"
														+ ((p % 2 == 0) ? "tr_white"
																: "tr_glory") + "'><td>"
														+ r.get("srcip").toString()
														+ "</td><td>"
														+ r.get("virustimes").toString()
														+ "</td></tr>");
										p++;
									}
							}
					}
			}
		tb.append("</table>");
		return tb.toString();
	}
	/**
	 * 获取过滤时间的前十名报表图
	 * 
	 * @param type
	 *            报表类型，使用该dao常量设置
	 * @param stime
	 *            时间点
	 * @param etime
	 *            结束时间
	 * @param device_id
	 *            设备id
	 * @return
	 */
	public JFreeChart getFChart(int type, long stime, long etime, String device_id)
	{
		chartUtil.setCLablePosition(ChartYType.DOWN_30);
		JFreeChart chart = null;
		if (type == this.DCHART)
			{
				String tn = "sgw_filter_hour_";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date ds = c.getTime();
				c.add(Calendar.DAY_OF_MONTH, 1);
				Date dt = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn += y + "_" + (m < 10 ? "0" + m : m) + "_" + (d < 10 ? "0" + d : d);
				ppSql.setSQL(getFSql + tn + getFconsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, t);
				ppSql.setInt(3, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
				chart = chartUtil.createCategoryBar3DP("过滤用户排名(" + f.format(ds) + "到"
						+ f.format(dt) + ")", "用户IP", "过滤用户数量", rs, "filtertimes",
						"srcip", "colName", true);
			}
		else if (type == this.WCHART)
			{
				String tn = "sgw_filter_day_";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.WEEK_OF_YEAR, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn += y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getFSql + tn + getFconsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, t);
				ppSql.setInt(3, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				chart = chartUtil.createCategoryBar3DP("过滤用户排名(" + f.format(ds) + "到"
						+ f.format(dt) + ")", "用户IP", "过滤用户数量", rs, "filtertimes",
						"srcip", "colName", true);
			}
		else if (type == this.MCHART)
			{
				String tn = "sgw_filter_day_";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.MONTH, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn += y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getFSql + tn + getFconsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, t);
				ppSql.setInt(3, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				chart = chartUtil.createCategoryBar3DP("过滤用户排名(" + f.format(ds) + "到"
						+ f.format(dt) + ")", "用户IP", "过滤用户数量", rs, "filtertimes",
						"srcip", "colName", true);
			}
		else
			{
				Date sd = new Date(stime);
				Date ed = new Date(etime);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				if (f.format(sd).equals(f.format(ed)))
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.DAY_OF_MONTH, 1, "sgw_filter_hour_",
								"yyyy_MM_dd");
						String tn = tns.get(0);
						ppSql.setSQL(getFSql + tn + getFconsSql);
						ppSql.setString(1, device_id);
						ppSql.setInt(3, (int) (stime / 1000));
						ppSql.setInt(2, (int) (etime / 1000 + 3600 * 24));
						log.debug("获取过滤用户查询的sql" + ppSql.getSQL());
						List<Map> rs = jt.queryForList(ppSql.getSQL());
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						f = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
						chart = chartUtil.createCategoryBar3DP("过滤用户排名(" + f.format(sd)
								+ "到" + f.format(new Date(stime + 24 * 3600000)) + ")",
								"用户IP", "过滤用户数量", rs, "filtertimes", "srcip", "colName",
								true);
					}
				else
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.MONTH, 1, "sgw_filter_day_", "yyyy_MM");
						String sql = getFSql + tns.get(0) + unionGetFconsSql;
						ppSql.setSQL(sql);
						ppSql.setString(1, device_id);
						ppSql.setInt(3, (int) (stime / 1000));
						ppSql.setInt(2, (int) (etime / 1000 + 3600 * 24));
						sql = ppSql.getSQL();
						int lens = tns.size();
						if (lens > 1)
							{
								for (int i = 1; i < lens; i++)
									sql += " union all " + getFSql + tns.get(i)
											+ unionGetFconsSql;
								ppSql.setSQL(sql);
								ppSql.setString(1, device_id);
								ppSql.setInt(3, (int) (stime / 1000));
								ppSql.setInt(2, (int) (etime / 1000 + 3600 * 24));
								sql = ppSql.getSQL();
							}
						sql = "select srcip,filtertimes,colName from ( " + sql
								+ ") t order by filtertimes";
						PrepareSQL psql = new PrepareSQL(sql);
						psql.getSQL();
						List<Map> rs = jt.queryForList(sql);
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						f = new SimpleDateFormat("yyyy_MM_dd");
						chart = chartUtil.createCategoryBar3DP("过滤用户排名(" + f.format(sd)
								+ "到" + f.format(new Date(etime)) + ")", "用户IP",
								"过滤用户数量", rs, "filtertimes", "srcip", "colName", true);
					}
			}
		return chart;
	}
	/**
	 * 获取过滤时间的前十名报表表格
	 * 
	 * @param type
	 *            报表类型，使用该dao的常量进行设置
	 * @param stime
	 *            时间点
	 * @param etime
	 *            结束时间
	 * @param device_id
	 *            设备id
	 * @return
	 */
	public String getFTable(int type, long stime, long etime, String device_id)
	{
		StringBuilder tb = new StringBuilder(
				"<table border='1' cellpadding='0' cellspacing='0' style='width:670px;' class='table'>");
		if (type == this.DCHART)
			{
				String tn = "sgw_filter_hour_";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date ds = c.getTime();
				c.add(Calendar.DAY_OF_MONTH, 1);
				Date dt = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn += y + "_" + (m < 10 ? "0" + m : m) + "_" + (d < 10 ? "0" + d : d);
				ppSql.setSQL(getFSql + tn + getFconsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, t);
				ppSql.setInt(3, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				tb.append("<tr class='tr_yellow'><td>用户IP</td><td>过滤用户数量</td></tr>");
				if (rs.size() == 0)
					{
						tb.append("<tr><td colspan=2>没有过滤用户排名数据</td></tr>");
					}
				else
					{
						int p = 0;
						for (Map r : rs)
							{
								tb.append("<tr class='"
										+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
										+ "'><td>" + r.get("srcip").toString()
										+ "</td><td>" + r.get("filtertimes").toString()
										+ "</td></tr>");
								p++;
							}
					}
			}
		else if (type == this.WCHART)
			{
				String tn = "sgw_filter_day_";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.WEEK_OF_YEAR, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn += y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getFSql + tn + getFconsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, t);
				ppSql.setInt(3, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				tb.append("<tr class='tr_yellow'><td>用户IP</td><td>过滤用户数量</td></tr>");
				if (rs.size() == 0)
					{
						tb.append("<tr><td colspan=2>没有过滤用户排名数据</td></tr>");
					}
				else
					{
						int p = 0;
						for (Map r : rs)
							{
								tb.append("<tr class='"
										+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
										+ "'><td>" + r.get("srcip").toString()
										+ "</td><td>" + r.get("filtertimes").toString()
										+ "</td></tr>");
								p++;
							}
					}
			}
		else if (type == this.MCHART)
			{
				String tn = "sgw_filter_day_";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.MONTH, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn += y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getFSql + tn + getFconsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, t);
				ppSql.setInt(3, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				tb.append("<tr class='tr_yellow'><td>用户IP</td><td>过滤用户数量</td></tr>");
				if (rs.size() == 0)
					{
						tb.append("<tr><td colspan=2>没有用户过滤排名数据</td></tr>");
					}
				else
					{
						int p = 0;
						for (Map r : rs)
							{
								tb.append("<tr class='"
										+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
										+ "'><td>" + r.get("srcip").toString()
										+ "</td><td>" + r.get("filtertimes").toString()
										+ "</td></tr>");
								p++;
							}
					}
			}
		else
			{
				Date sd = new Date(stime);
				Date ed = new Date(etime);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				if (f.format(sd).equals(f.format(ed)))
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.DAY_OF_MONTH, 1, "sgw_filter_hour_",
								"yyyy_MM_dd");
						String tn = tns.get(0);
						ppSql.setSQL(getFSql + tn + getFconsSql);
						ppSql.setString(1, device_id);
						ppSql.setInt(3, (int) (stime / 1000));
						ppSql.setInt(2, (int) (etime / 1000 + 3600 * 24));
						log.debug("获取过滤用户查询的sql" + ppSql.getSQL());
						List<Map> rs = jt.queryForList(ppSql.getSQL());
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						tb
								.append("<tr class='tr_yellow'><td>用户IP</td><td>过滤用户数量</td></tr>");
						if (rs.size() == 0)
							{
								tb.append("<tr><td colspan=2>没有用户过滤排名数据</td></tr>");
							}
						else
							{
								int p = 0;
								for (Map r : rs)
									{
										tb
												.append("<tr class='"
														+ ((p % 2 == 0) ? "tr_white"
																: "tr_glory") + "'><td>"
														+ r.get("srcip").toString()
														+ "</td><td>"
														+ r.get("filtertimes").toString()
														+ "</td></tr>");
										p++;
									}
							}
					}
				else
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.MONTH, 1, "sgw_filter_day_", "yyyy_MM");
						String sql = getFSql + tns.get(0) + unionGetFconsSql;
						ppSql.setSQL(sql);
						ppSql.setString(1, device_id);
						ppSql.setInt(3, (int) (stime / 1000));
						ppSql.setInt(2, (int) (etime / 1000 + 3600 * 24));
						sql = ppSql.getSQL();
						int lens = tns.size();
						if (lens > 1)
							{
								for (int i = 1; i < lens; i++)
									sql += " union all " + getFSql + tns.get(i)
											+ unionGetFconsSql;
								ppSql.setSQL(sql);
								ppSql.setString(1, device_id);
								ppSql.setInt(3, (int) (stime / 1000));
								ppSql.setInt(2, (int) (etime / 1000 + 3600 * 24));
								sql = ppSql.getSQL();
							}
						sql = "select srcip,filtertimes,colName from ( " + sql
								+ ") t order by filtertimes";
						PrepareSQL psql = new PrepareSQL(sql);
						psql.getSQL();
						List<Map> rs = jt.queryForList(sql);
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						tb
								.append("<tr class='tr_yellow'><td>用户IP</td><td>过滤用户数量</td></tr>");
						if (rs.size() == 0)
							{
								tb.append("<tr><td colspan=2>没有用户过滤排名数据</td></tr>");
							}
						else
							{
								int p = 0;
								for (Map r : rs)
									{
										tb
												.append("<tr class='"
														+ ((p % 2 == 0) ? "tr_white"
																: "tr_glory") + "'><td>"
														+ r.get("srcip").toString()
														+ "</td><td>"
														+ r.get("filtertimes").toString()
														+ "</td></tr>");
										p++;
									}
							}
					}
			}
		tb.append("</table>");
		return tb.toString();
	}
	/**
	 * 获取垃圾邮件的前十名报表图
	 * 
	 * @param type
	 *            报表类型，使用该dao的常量进行设置
	 * @param stime
	 *            时间点
	 * @param etime
	 *            结束时间点
	 * @param device_id
	 *            设备id
	 * @return
	 */
	public JFreeChart getAMChart(int type, long stime, long etime, String device_id)
	{
		chartUtil.setCLablePosition(ChartYType.DOWN_30);
		JFreeChart chart = null;
		if (type == this.DCHART)
			{
				String tn = "sgw_mail_hour_";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date ds = c.getTime();
				c.add(Calendar.DAY_OF_MONTH, 1);
				Date dt = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn += y + "_" + (m < 10 ? "0" + m : m) + "_" + (d < 10 ? "0" + d : d);
				ppSql.setSQL(getMSql + tn + getMconsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, t);
				ppSql.setInt(3, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
				chart = chartUtil.createCategoryBar3DP("垃圾邮件排名(" + f.format(ds) + "到"
						+ f.format(dt) + ")", "用户IP", "垃圾邮件数量", rs, "mailtimes", "srcip",
						"colName", true);
			}
		else if (type == this.WCHART)
			{
				String tn = "sgw_mail_day_";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.WEEK_OF_YEAR, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn += y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getMSql + tn + getMconsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, t);
				ppSql.setInt(3, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				chart = chartUtil.createCategoryBar3DP("垃圾邮件排名(" + f.format(ds) + "到"
						+ f.format(dt) + ")", "用户IP", "垃圾邮件数量", rs, "mailtimes", "srcip",
						"colName", true);
			}
		else if (type == this.MCHART)
			{
				String tn = "sgw_mail_day_";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.MONTH, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn += y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getMSql + tn + getMconsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, t);
				ppSql.setInt(3, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				chart = chartUtil.createCategoryBar3DP("垃圾邮件排名(" + f.format(ds) + "到"
						+ f.format(dt) + ")", "用户IP", "垃圾邮件数量", rs, "mailtimes", "srcip",
						"colName", true);
			}
		else
			{
				Date sd = new Date(stime);
				Date ed = new Date(etime);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				if (f.format(sd).equals(f.format(ed)))
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.DAY_OF_MONTH, 1, "sgw_mail_hour_", "yyyy_MM_dd");
						String tn = tns.get(0);
						ppSql.setSQL(getMSql + tn + getMconsSql);
						ppSql.setString(1, device_id);
						ppSql.setInt(3, (int) (stime / 1000));
						ppSql.setInt(2, (int) (etime / 1000 + 3600 * 24));
						log.debug("获取垃圾邮件查询的sql" + ppSql.getSQL());
						List<Map> rs = jt.queryForList(ppSql.getSQL());
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						f = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
						chart = chartUtil.createCategoryBar3DP("垃圾邮件用户排名(" + f.format(sd)
								+ "到" + f.format(new Date(stime + 24 * 3600000)) + ")",
								"用户IP", "垃圾邮件数量", rs, "mailtimes", "srcip", "colName",
								true);
					}
				else
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.MONTH, 1, "sgw_mail_day_", "yyyy_MM");
						String sql = getMSql + tns.get(0) + unionGetMconsSql;
						ppSql.setSQL(sql);
						ppSql.setString(1, device_id);
						ppSql.setInt(3, (int) (stime / 1000));
						ppSql.setInt(2, (int) (etime / 1000 + 3600 * 24));
						sql = ppSql.getSQL();
						int lens = tns.size();
						if (lens > 1)
							{
								for (int i = 1; i < lens; i++)
									sql += " union all " + getMSql + tns.get(i)
											+ unionGetMconsSql;
								ppSql.setSQL(sql);
								ppSql.setString(1, device_id);
								ppSql.setInt(3, (int) (stime / 1000));
								ppSql.setInt(2, (int) (etime / 1000 + 3600 * 24));
								sql = ppSql.getSQL();
							}
						sql = "select srcip,mailtimes,colName from ( " + sql
								+ ") t order by mailtimes";
						PrepareSQL psql = new PrepareSQL(sql);
						psql.getSQL();
						List<Map> rs = jt.queryForList(sql);
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						f = new SimpleDateFormat("yyyy_MM_dd");
						chart = chartUtil.createCategoryBar3DP("垃圾邮件用户排名(" + f.format(sd)
								+ "到" + f.format(new Date(etime)) + ")", "用户IP",
								"垃圾邮件数量", rs, "mailtimes", "srcip", "colName", true);
					}
			}
		return chart;
	}
	/**
	 * 获取垃圾邮件的前十名报表表格
	 * 
	 * @param type
	 *            报表类型，使用该dao的常量进行设置
	 * @param stime
	 *            时间点
	 * @param etime
	 *            结束时间点
	 * @param device_id
	 *            设备id
	 * @return
	 */
	public String getAMTable(int type, long stime, long etime, String device_id)
	{
		StringBuilder tb = new StringBuilder(
				"<table border='1' cellpadding='0' cellspacing='0' style='width:670px;' class='table'>");
		if (type == this.DCHART)
			{
				String tn = "sgw_mail_hour_";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date ds = c.getTime();
				c.add(Calendar.DAY_OF_MONTH, 1);
				Date dt = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn += y + "_" + (m < 10 ? "0" + m : m) + "_" + (d < 10 ? "0" + d : d);
				ppSql.setSQL(getMSql + tn + getMconsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, t);
				ppSql.setInt(3, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				tb.append("<tr class='tr_yellow'><td>用户IP</td><td>垃圾邮件数量</td></tr>");
				if (rs.size() == 0)
					{
						tb.append("<tr><td colspan=2>没有垃圾邮件用户排名数据</td></tr>");
					}
				else
					{
						int p = 0;
						for (Map r : rs)
							{
								tb.append("<tr class='"
										+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
										+ "'><td>" + r.get("srcip").toString()
										+ "</td><td>" + r.get("mailtimes").toString()
										+ "</td></tr>");
								p++;
							}
					}
			}
		else if (type == this.WCHART)
			{
				String tn = "sgw_mail_day_";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.WEEK_OF_YEAR, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn += y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getMSql + tn + getMconsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, t);
				ppSql.setInt(3, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				tb.append("<tr class='tr_yellow'><td>用户IP</td><td>垃圾邮件数量</td></tr>");
				if (rs.size() == 0)
					{
						tb.append("<tr><td colspan=2>没有垃圾邮件用户排名数据</td></tr>");
					}
				else
					{
						int p = 0;
						for (Map r : rs)
							{
								tb.append("<tr class='"
										+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
										+ "'><td>" + r.get("srcip").toString()
										+ "</td><td>" + r.get("mailtimes").toString()
										+ "</td></tr>");
								p++;
							}
					}
			}
		else if (type == this.MCHART)
			{
				String tn = "sgw_mail_day_";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.MONTH, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn += y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getMSql + tn + getMconsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, t);
				ppSql.setInt(3, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				tb.append("<tr class='tr_yellow'><td>用户IP</td><td>垃圾邮件数量</td></tr>");
				if (rs.size() == 0)
					{
						tb.append("<tr><td colspan=2>没有垃圾邮件用户排名数据</td></tr>");
					}
				else
					{
						int p = 0;
						for (Map r : rs)
							{
								tb.append("<tr class='"
										+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
										+ "'><td>" + r.get("srcip").toString()
										+ "</td><td>" + r.get("mailtimes").toString()
										+ "</td></tr>");
								p++;
							}
					}
			}
		else
			{
				Date sd = new Date(stime);
				Date ed = new Date(etime);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				if (f.format(sd).equals(f.format(ed)))
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.DAY_OF_MONTH, 1, "sgw_mail_hour_", "yyyy_MM_dd");
						String tn = tns.get(0);
						ppSql.setSQL(getMSql + tn + getMconsSql);
						ppSql.setString(1, device_id);
						ppSql.setInt(3, (int) (stime / 1000));
						ppSql.setInt(2, (int) (etime / 1000 + 3600 * 24));
//						log.debug("获取垃圾邮件查询的sql" + ppSql.getSQL());
						List<Map> rs = jt.queryForList(ppSql.getSQL());
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						tb
								.append("<tr class='tr_yellow'><td>用户IP</td><td>垃圾邮件数量</td></tr>");
						if (rs.size() == 0)
							{
								tb.append("<tr><td colspan=2>没有垃圾邮件用户排名数据</td></tr>");
							}
						else
							{
								int p = 0;
								for (Map r : rs)
									{
										tb
												.append("<tr class='"
														+ ((p % 2 == 0) ? "tr_white"
																: "tr_glory") + "'><td>"
														+ r.get("srcip").toString()
														+ "</td><td>"
														+ r.get("mailtimes").toString()
														+ "</td></tr>");
										p++;
									}
							}
					}
				else
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.MONTH, 1, "sgw_mail_day_", "yyyy_MM");
						String sql = getMSql + tns.get(0) + unionGetMconsSql;
						ppSql.setSQL(sql);
						ppSql.setString(1, device_id);
						ppSql.setInt(3, (int) (stime / 1000));
						ppSql.setInt(2, (int) (etime / 1000 + 3600 * 24));
						sql = ppSql.getSQL();
						int lens = tns.size();
						if (lens > 1)
							{
								for (int i = 1; i < lens; i++)
									sql += " union all " + getMSql + tns.get(i)
											+ unionGetMconsSql;
								ppSql.setSQL(sql);
								ppSql.setString(1, device_id);
								ppSql.setInt(3, (int) (stime / 1000));
								ppSql.setInt(2, (int) (etime / 1000 + 3600 * 24));
								sql = ppSql.getSQL();
							}
						sql = "select srcip,mailtimes,colName from ( " + sql
								+ ") t order by mailtimes";
						PrepareSQL psql = new PrepareSQL(sql);
						psql.getSQL();
						List<Map> rs = jt.queryForList(sql);
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						tb
								.append("<tr class='tr_yellow'><td>用户IP</td><td>垃圾邮件数量</td></tr>");
						if (rs.size() == 0)
							{
								tb.append("<tr><td colspan=2>没有垃圾邮件用户排名数据</td></tr>");
							}
						else
							{
								int p = 0;
								for (Map r : rs)
									{
										tb
												.append("<tr class='"
														+ ((p % 2 == 0) ? "tr_white"
																: "tr_glory") + "'><td>"
														+ r.get("srcip").toString()
														+ "</td><td>"
														+ r.get("mailtimes").toString()
														+ "</td></tr>");
										p++;
									}
							}
					}
			}
		tb.append("</table>");
		return tb.toString();
	}
	/**
	 * 获取攻击事件排名报表图
	 * 
	 * @param type
	 *            报表类型，使用该dao的常量进行设置
	 * @param stime
	 *            时间
	 * @param etime
	 *            结束时间
	 * @param aType
	 *            攻击类型
	 * @param device_id
	 *            设备id
	 * @return
	 */
	public JFreeChart getATChart(int type, long stime, long etime, int aType,
			String device_id)
	{
		chartUtil.setCLablePosition(ChartYType.DOWN_30);
		JFreeChart chart = null;
		if (type == this.DCHART)
			{
				String tn = "";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date ds = c.getTime();
				c.add(Calendar.DAY_OF_MONTH, 1);
				Date dt = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn = "sgw_attack_hour_" + y + "_" + (m < 10 ? "0" + m : m) + "_"
						+ (d < 10 ? "0" + d : d);
				ppSql.setSQL(getASql + tn + getAConsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, aType);
				ppSql.setInt(3, t);
				ppSql.setInt(4, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
				chart = chartUtil.createCategoryBar3DP("攻击用户排名(" + f.format(ds) + "到"
						+ f.format(dt) + ")", "用户IP", "攻击次数", rs, "attacktimes", "srcip",
						"attackname", true);
			}
		else if (type == this.WCHART)
			{
				String tn = "";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.WEEK_OF_YEAR, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				tn = "sgw_attack_day_" + y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getASql + tn + getAConsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, aType);
				ppSql.setInt(3, t);
				ppSql.setInt(4, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				chart = chartUtil.createCategoryBar3DP("攻击用户排名(" + f.format(ds) + "到"
						+ f.format(dt) + ")", "用户IP", "攻击次数", rs, "attacktimes", "srcip",
						"attackname", true);
			}
		else if (type == this.MCHART)
			{
				String tn = "";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.MONTH, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				tn = "sgw_attack_day_" + y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getASql + tn + getAConsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, aType);
				ppSql.setInt(3, t);
				ppSql.setInt(4, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				chart = chartUtil.createCategoryBar3DP("攻击用户排名(" + f.format(ds) + "到"
						+ f.format(dt) + ")", "用户IP", "攻击次数", rs, "attacktimes", "srcip",
						"attackname", true);
			}
		else
			{
				Date sd = new Date(stime);
				Date ed = new Date(etime);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				if (f.format(sd).equals(f.format(ed)))
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.DAY_OF_MONTH, 1, "sgw_attack_hour_",
								"yyyy_MM_dd");
						String tn = tns.get(0);
						ppSql.setSQL(getASql + tn + getAConsSql);
						ppSql.setString(1, device_id);
						ppSql.setInt(2, aType);
						ppSql.setInt(4, (int) (stime / 1000));
						ppSql.setInt(3, (int) (etime / 1000 + 3600 * 24));
//						log.debug("获取攻击用户查询的sql" + ppSql.getSQL());
						List<Map> rs = jt.queryForList(ppSql.getSQL());
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						f = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
						chart = chartUtil.createCategoryBar3DP("攻击用户排名(" + f.format(sd)
								+ "到" + f.format(new Date(stime + 24 * 3600000)) + ")",
								"用户IP", "攻击次数", rs, "攻击次数", "srcip", "attackname",
								true);
					}
				else
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.MONTH, 1, "sgw_attack_day_", "yyyy_MM");
						String sql = getASql + tns.get(0) + unionGetAConsSql;
						ppSql.setSQL(sql);
						ppSql.setString(1, device_id);
						ppSql.setInt(2, aType);
						ppSql.setInt(4, (int) (stime / 1000));
						ppSql.setInt(3, (int) (etime / 1000 + 3600 * 24));
						sql = ppSql.getSQL();
						int lens = tns.size();
						if (lens > 1)
							{
								for (int i = 1; i < lens; i++)
									sql += " union all " + getASql + tns.get(i)
											+ unionGetAConsSql;
								ppSql.setSQL(sql);
								ppSql.setString(1, device_id);
								ppSql.setInt(2, aType);
								ppSql.setInt(4, (int) (stime / 1000));
								ppSql.setInt(3, (int) (etime / 1000 + 3600 * 24));
								sql = ppSql.getSQL();
							}
						sql = "select srcip,attacktimes,attackname from ( " + sql
								+ ") t order by attacktimes";
						PrepareSQL psql = new PrepareSQL(sql);
						psql.getSQL();
						List<Map> rs = jt.queryForList(sql);
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						f = new SimpleDateFormat("yyyy_MM_dd");
						chart = chartUtil.createCategoryBar3DP("攻击排名(" + f.format(sd)
								+ "到" + f.format(new Date(etime)) + ")", "用户IP",
								"攻击次数", rs, "attacktimes", "srcip", "attackname", true);
					}
			}
		return chart;
	}
	/**
	 * 获取攻击事件的前十名报表表格
	 * 
	 * @param type
	 *            报表类型，使用该dao的常量进行设置
	 * @param stime
	 *            时间点
	 * @param etime
	 *            结束时间
	 * @param aType
	 *            攻击类型
	 * @param device_id
	 *            设备id
	 * @return
	 */
	public String getATTable(int type, long stime, long etime, int aType, String device_id)
	{
		StringBuilder tb = new StringBuilder(
				"<table border='1' cellpadding='0' cellspacing='0' style='width:670px;' class='table'>");
		if (type == this.DCHART)
			{
				String tn = "";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date ds = c.getTime();
				c.add(Calendar.DAY_OF_MONTH, 1);
				Date dt = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				int d = StringUtils.getDate(t);
				tn = "sgw_attack_hour_" + y + "_" + (m < 10 ? "0" + m : m) + "_"
						+ (d < 10 ? "0" + d : d);
				ppSql.setSQL(getASql + tn + getAConsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, aType);
				ppSql.setInt(3, t);
				ppSql.setInt(4, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				tb.append("<tr class='tr_yellow'><td>用户</td><td>攻击次数</td></tr>");
				if (rs.size() == 0)
					{
						tb.append("<tr><td colspan=2>没有攻击用户排名数据</td></tr>");
					}
				else
					{
						int p = 0;
						for (Map r : rs)
							{
								tb.append("<tr class='"
										+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
										+ "'><td>" + r.get("srcip").toString()
										+ "</td><td>" + r.get("attacktimes").toString()
										+ "</td></tr>");
								p++;
							}
					}
			}
		else if (type == this.WCHART)
			{
				String tn = "";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.WEEK_OF_YEAR, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				tn = "sgw_attack_day_" + y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getASql + tn + getAConsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, aType);
				ppSql.setInt(3, t);
				ppSql.setInt(4, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				tb.append("<tr class='tr_yellow'><td>用户</td><td>攻击次数</td></tr>");
				if (rs.size() == 0)
					{
						tb.append("<tr><td colspan=2>没有攻击用户排名数据</td></tr>");
					}
				else
					{
						int p = 0;
						for (Map r : rs)
							{
								tb.append("<tr class='"
										+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
										+ "'><td>" + r.get("srcip").toString()
										+ "</td><td>" + r.get("attacktimes").toString()
										+ "</td></tr>");
								p++;
							}
					}
			}
		else if (type == this.MCHART)
			{
				String tn = "";
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(stime);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dt = c.getTime();
				c.add(Calendar.MONTH, -1);
				Date ds = c.getTime();
				int s = (int) (ds.getTime() / 1000);
				int t = (int) (dt.getTime() / 1000);
				int y = StringUtils.getYear(t);
				int m = StringUtils.getMonth(t);
				tn = "sgw_attack_day_" + y + "_" + (m < 10 ? "0" + m : m);
				ppSql.setSQL(getASql + tn + getAConsSql);
				ppSql.setString(1, device_id);
				ppSql.setInt(2, aType);
				ppSql.setInt(3, t);
				ppSql.setInt(4, s);
				List<Map> rs = jt.queryForList(ppSql.getSQL());
				int len = rs.size();
				rs = rs.subList(0, len > 9 ? 9 : len);
				tb.append("<tr class='tr_yellow'><td>用户</td><td>攻击次数</td></tr>");
				if (rs.size() == 0)
					{
						tb.append("<tr><td colspan=2>没有攻击用户排名数据</td></tr>");
					}
				else
					{
						int p = 0;
						for (Map r : rs)
							{
								tb.append("<tr class='"
										+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
										+ "'><td>" + r.get("srcip").toString()
										+ "</td><td>" + r.get("attacktimes").toString()
										+ "</td></tr>");
								p++;
							}
					}
			}
		else
			{
				Date sd = new Date(stime);
				Date ed = new Date(etime);
				SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd");
				if (f.format(sd).equals(f.format(ed)))
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.DAY_OF_MONTH, 1, "sgw_attack_hour_",
								"yyyy_MM_dd");
						String tn = tns.get(0);
						ppSql.setSQL(getASql + tn + getAConsSql);
						ppSql.setString(1, device_id);
						ppSql.setInt(2, aType);
						ppSql.setInt(4, (int) (stime / 1000));
						ppSql.setInt(3, (int) (etime / 1000 + 3600 * 24));
//						log.debug("获取攻击用户查询的sql" + ppSql.getSQL());
						List<Map> rs = jt.queryForList(ppSql.getSQL());
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						tb.append("<tr class='tr_yellow'><td>用户</td><td>攻击次数</td></tr>");
						if (rs.size() == 0)
							{
								tb.append("<tr><td colspan=2>没有攻击用户排名数据</td></tr>");
							}
						else
							{
								int p = 0;
								for (Map r : rs)
									{
										tb.append("<tr class='"
												+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
												+ "'><td>" + r.get("srcip").toString()
												+ "</td><td>" + r.get("attacktimes").toString()
												+ "</td></tr>");
										p++;
									}
							}
					}
				else
					{
						List<String> tns = DbUtil.createTableNames(stime, etime,
								Calendar.MONTH, 1, "sgw_attack_day_", "yyyy_MM");
						String sql = getASql + tns.get(0) + unionGetAConsSql;
						ppSql.setSQL(sql);
						ppSql.setString(1, device_id);
						ppSql.setInt(2, aType);
						ppSql.setInt(4, (int) (stime / 1000));
						ppSql.setInt(3, (int) (etime / 1000 + 3600 * 24));
						sql = ppSql.getSQL();
						int lens = tns.size();
						if (lens > 1)
							{
								for (int i = 1; i < lens; i++)
									sql += " union all " + getASql + tns.get(i)
											+ unionGetAConsSql;
								ppSql.setSQL(sql);
								ppSql.setString(1, device_id);
								ppSql.setInt(2, aType);
								ppSql.setInt(4, (int) (stime / 1000));
								ppSql.setInt(3, (int) (etime / 1000 + 3600 * 24));
								sql = ppSql.getSQL();
							}
						sql = "select srcip,attacktimes,attackname from ( " + sql
								+ ") t order by attacktimes";
						PrepareSQL psql = new PrepareSQL(sql);
						psql.getSQL();
						List<Map> rs = jt.queryForList(sql);
						int len = rs.size();
						rs = rs.subList(0, len > 9 ? 9 : len);
						tb.append("<tr class='tr_yellow'><td>用户</td><td>攻击次数</td></tr>");
						if (rs.size() == 0)
							{
								tb.append("<tr><td colspan=2>没有攻击用户排名数据</td></tr>");
							}
						else
							{
								int p = 0;
								for (Map r : rs)
									{
										tb.append("<tr class='"
												+ ((p % 2 == 0) ? "tr_white" : "tr_glory")
												+ "'><td>" + r.get("srcip").toString()
												+ "</td><td>" + r.get("attacktimes").toString()
												+ "</td></tr>");
										p++;
									}
							}
					}
			}
		tb.append("</table>");
		return tb.toString();
	}
	/**
	 * 获取病毒类型
	 * 
	 * @return
	 */
	public List<Map> getVT()
	{
		return jt.queryForList("select virustype,virusname from sgw_virus_info");
	}
	/**
	 * 获取攻击类型
	 * 
	 * @return
	 */
	public List<Map> getAT()
	{
		return jt.queryForList("select attacktype,attackname from sgw_attack_info");
	}
	public void setPpSql(PrepareSQL ppSql)
	{
		this.ppSql = ppSql;
	}
	public void setChartUtil(ChartUtil chartUtil)
	{
		this.chartUtil = chartUtil;
	}
}
