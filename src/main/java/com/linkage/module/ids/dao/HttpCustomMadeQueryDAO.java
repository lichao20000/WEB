
package com.linkage.module.ids.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.DecimalFormat;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.db.DBUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author yinlei3 (73167)
 * @version 1.0
 * @since 2015年6月12日
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class HttpCustomMadeQueryDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(HttpCustomMadeQueryDAO.class);

	/**
	 * 任务查询
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> queryTask(String name, String acc_loginname, String starttime,
			String endtime, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("HttpCustomMadeQueryDAO==>queryTask({},{},{},{})", new Object[] {
				name, acc_loginname, starttime, endtime, });
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,b.acc_loginname,a.add_time," +
				"a.task_id,a.url,a.level_report,a.file_name " +
				"from tab_ids_task a,tab_accounts b " +
				"where a.acc_oid=b.acc_oid and a.type_id=3 ");
		if (!StringUtil.IsEmpty(name)){
			sql.append(" and a.task_name like '%" + name + "%'");
		}
		if (!StringUtil.IsEmpty(acc_loginname)){
			sql.append(" and b.acc_loginname like '%" + acc_loginname + "%'");
		}
		if (!StringUtil.IsEmpty(starttime)){
			sql.append(" and a.add_time >").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)){
			sql.append(" and a.add_time <").append(endtime);
		}
		sql.append(" order by a.add_time desc");
		psql.setSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage);
		if (null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String addtime = String.valueOf(list.get(i).get("add_time"));
				list.get(i).put("add_time",
								DateUtil.transTime(Long.parseLong(addtime),
										"yyyy-MM-dd HH:mm:ss"));
			}
		}
		return list;
	}

	/**
	 * 任务查询(分页)
	 */
	public int queryTaskCount(String name, String acc_loginname, String starttime,
			String endtime, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("HttpCustomMadeQueryDAO==>queryTask({},{},{},{})", new Object[] {
				name, acc_loginname, starttime, endtime, });
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_ids_task a,tab_accounts b where a.acc_oid=b.acc_oid and a.type_id=3 ");
		if (!StringUtil.IsEmpty(name)){
			sql.append(" and a.task_name like '%" + name + "%'");
		}
		if (!StringUtil.IsEmpty(acc_loginname)){
			sql.append(" and b.acc_loginname like'%" + acc_loginname + "%'");
		}
		if (!StringUtil.IsEmpty(starttime)){
			sql.append(" and a.add_time >").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)){
			sql.append(" and a.add_time <").append(endtime);
		}
		psql.setSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 获得设备列表
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getTaskDevList(String taskId, int curPage_splitPage,int num_splitPage)
	{
		logger.debug("HttpCustomMadeQueryDAO==>getTaskDevList({})",
				new Object[] { taskId });
		PrepareSQL psql = new PrepareSQL();
		//TODO wait
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_id,a.city_name,a.device_serialnumber," +
				"a.loid,e.ip,d.add_time,e.bom_time,e.eom_time,e.total_bytes_rece ");
		sql.append("from tab_ids_task_dev a " +
				"left join tab_http_diag_result e on a.task_id=e.task_id " +
				"and a.device_serialnumber = e.device_serialnumber,tab_accounts c,tab_ids_task d ");
		sql.append("where a.task_id=d.task_id and d.acc_oid=c.acc_oid ");
		sql.append("and d.task_id=? order by d.add_time");
		psql.setSQL(sql.toString());
		psql.setString(1, taskId);
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 城市
				map.put("cityName", StringUtil.getStringValue(rs.getString("city_name")));
				// 设备序列号
				map.put("deviceSerialnumber",
						StringUtil.getStringValue(rs.getString("device_serialnumber")));
				// 宽带账号
				map.put("userName", StringUtil.getStringValue(rs.getString("loid")));
				// IP地址
				map.put("IpAddr", StringUtil.getStringValue(rs.getString("ip")));
				// 策略定制时间
				map.put("taskMadeTime",
						StringUtil.IsEmpty(rs.getString("add_time")) ? "-"
								: setTime(StringUtil.getLongValue(rs
										.getString("add_time"))));
				// 下载开始时间
				String bomTime = StringUtil.getStringValue(rs.getString("bom_time"));
				map.put("loadBeginTime",
						StringUtil.IsEmpty(bomTime) ? "-" : (bomTime.replace("T", " "))
								.substring(0, 19));
				// 下载结束时间
				String eomTime = StringUtil.getStringValue(rs.getString("eom_time"));
				map.put("loadEndTime",
						StringUtil.IsEmpty(eomTime) ? "-" : (eomTime.replace("T", " "))
								.substring(0, 19));
				// 接受字节数
				map.put("receiveBities",
						StringUtil.IsEmpty(rs.getString("total_bytes_rece")) ? "-"
								: StringUtil.getStringValue(rs
										.getString("total_bytes_rece")));
				// 测试结果
				map.put("testResult",
						getDownPert(bomTime, eomTime, StringUtil.getStringValue(rs
								.getString("total_bytes_rece"))));
				// 设备Id
				map.put("deviceId", StringUtil.getStringValue(rs.getString("device_id")));
				return map;
			}
		});
		return list;
	}

	/**
	 * 获得设备列表(分页)
	 */
	public int getTaskDevCount(String taskId, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("HttpCustomMadeQueryDAO==>getTaskDevCount({})",
				new Object[] { taskId });
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1)");
		}
		
		sql.append("from tab_ids_task_dev a " +
				"left join tab_http_diag_result e on a.task_id=e.task_id " +
				"and a.device_serialnumber=e.device_serialnumber,tab_accounts c,tab_ids_task d ");
		sql.append("where a.task_id=d.task_id and d.acc_oid=c.acc_oid and d.task_id=? ");
		psql.setSQL(sql.toString());
		psql.setString(1, taskId);
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 时间转换
	 * 
	 * @param time
	 *            long型字符串
	 * @return yyyy-mm-dd HH:mm:ss
	 */
	private String setTime(long time)
	{
		try
		{
			DateTimeUtil dt = new DateTimeUtil(time * 1000);
			return dt.getLongDate();
		}
		catch (Exception e)
		{
			return "";
		}
	}

	/**
	 * 速率计算公式
	 * 
	 * @param transportStartTime
	 *            开始时间
	 * @param transportEndTime
	 *            结束时间
	 * @param receiveByte
	 *            接受字节数
	 * @return 速率
	 */
	private String getDownPert(String transportStartTime, String transportEndTime,
			String receiveByte)
	{
		float ff = 0;
		String strtime = transportStartTime;
		String endtime = transportEndTime;
		if (!StringUtil.IsEmpty(strtime) && !StringUtil.IsEmpty(endtime))
		{
			BigDecimal strTime = new BigDecimal(strtime.split(":")[2].split("[.]")[0])
					.add(new BigDecimal(strtime.split(":")[2].split("[.]")[1]).divide(
							new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP));
			BigDecimal endTime = new BigDecimal(endtime.split(":")[2].split("[.]")[0])
					.add(new BigDecimal(endtime.split(":")[2].split("[.]")[1]).divide(
							new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP));
			BigDecimal receiveBytes = new BigDecimal(receiveByte).divide(new BigDecimal(
					"1024"), 6, BigDecimal.ROUND_HALF_UP);// k
			BigDecimal mintue = (new BigDecimal(endtime.split(":")[1])
					.subtract(new BigDecimal(strtime.split(":")[1])))
					.multiply(new BigDecimal("60"));
			BigDecimal period = endTime.subtract(strTime).add(mintue);
			// k/s
			ff = receiveBytes.divide(period, 6, BigDecimal.ROUND_HALF_UP).floatValue();
			DecimalFormat df = new DecimalFormat("#0.00");
			return StringUtil.getStringValue(df.format(ff)) + "/KB";
		}
		return "-";
	}
}
