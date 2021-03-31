
package com.linkage.module.gtms.stb.config.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.obj.StrategyOBJ;

/**
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-11-27
 * @category com.linkage.module.gtms.stb.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class StrategyDAO
{

	private static Logger logger = LoggerFactory.getLogger(StrategyDAO.class);
	private static Map<String, String> status_map = new HashMap<String, String>();
	private static Map<String, String> result_map = new HashMap<String, String>();
	static
	{
		status_map.put("0", "等待执行");
		status_map.put("1", "预读PVC");
		status_map.put("2", "预读绑定端口");
		status_map.put("3", "预读无线");
		status_map.put("4", "业务下发");
		status_map.put("100", "执行完成");
		result_map.put("-10", "策略执行过程中程序异常");
		result_map.put("-9", "系统内部错误");
		result_map.put("-8", "任务中前一策略失败导致");
		result_map.put("-7", "系统参数错误");
		result_map.put("-6", "设备正被操作");
		result_map.put("-5", "系统没有模板");
		result_map.put("-4", "系统没有设备");
		result_map.put("-3", "系统没有工单");
		result_map.put("-2", "设备没有响应");
		result_map.put("-1", "设备连接失败");
		result_map.put("0", "等待执行");
		result_map.put("1", "成功");
		result_map.put("2", "正在执行");
		result_map.put("3", "设备无法连接");
		result_map.put("9001", "请求拒绝");
		result_map.put("9002", "请求拒绝");
		result_map.put("9003", "参数不对");
		result_map.put("9004", "资源超支");
		result_map.put("9005", "节点不对");
		result_map.put("9006", "节点类型不对");
		result_map.put("9007", "节点值不对");
		result_map.put("9008", "节点不可更改");
		result_map.put("9009", "通知失败");
		result_map.put("9010", "下载失败");
	}
	private JdbcTemplate jt;

	public Map<String, String> getStrategyById(String strategyId)
	{
		if (StringUtil.IsEmpty(strategyId))
		{
			return null;
		}
		Map<String, String> rmap = null;
		String sql = "select id, status, result_id, start_time, end_time from stb_gw_serv_strategy where id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, StringUtil.getLongValue(strategyId));
		Map tmap = jt.queryForMap(psql.getSQL());
		if (tmap != null)
		{
			rmap = new HashMap<String, String>();
			rmap.put("strategyId", StringUtil.getStringValue(tmap.get("id")));
			rmap.put("status",
					status_map.get(StringUtil.getStringValue(tmap.get("status"))));
			rmap.put("result",
					result_map.get(StringUtil.getStringValue(tmap.get("result_id"))));
			// 将startTime转换成时间
			String startTime = StringUtil.getStringValue(tmap.get("start_time"));
			if (false == StringUtil.IsEmpty(startTime))
			{
				DateTimeUtil dateTimeUtil = new DateTimeUtil(
						Long.parseLong(startTime) * 1000);
				startTime = dateTimeUtil.getLongDate();
				dateTimeUtil = null;
			}
			rmap.put("startTime", startTime);
			// 将endTime转换成时间
			String endTime = StringUtil.getStringValue(tmap.get("end_time"));
			if (false == StringUtil.IsEmpty(endTime))
			{
				DateTimeUtil dateTimeUtil = new DateTimeUtil(
						Long.parseLong(endTime) * 1000);
				endTime = dateTimeUtil.getLongDate();
				dateTimeUtil = null;
			}
			rmap.put("endTime", endTime);
		}
		return rmap;
	}

	/**
	 * 增加策略
	 */
	public Boolean addStrategy(StrategyOBJ obj)
	{
		logger.debug("addStrategy({})", obj);
		if (obj == null)
		{
			logger.debug("obj == null");
			return false;
		}
		List<String> sqlList = strategySQL(obj);
		int[] result = doBatch(sqlList);
		if (result != null && result.length > 0)
		{
			logger.debug("策略入库：  成功");
			return true;
		}
		else
		{
			logger.debug("策略入库：  失败");
			return false;
		}
	}

	/**
	 * 生成入策略的sql语句
	 *
	 * @author wangsenbo
	 * @date Jun 11, 2010
	 * @param
	 * @return List<String>
	 */
	public List<String> strategySQL(StrategyOBJ obj)
	{
		logger.debug("strategySQL({})", obj);
		if (obj == null)
		{
			return null;
		}
		List<String> sqlList = new ArrayList<String>();
		StringBuilder tempSql = new StringBuilder();
		tempSql.append("delete from stb_gw_serv_strategy where device_id='")
				.append(obj.getDeviceId()).append("' and temp_id=")
				.append(obj.getTempId());
		// 生成入策略的sql语句
		StringBuilder sql = new StringBuilder();
		sql.append("insert into stb_gw_serv_strategy (");
		sql.append("id,acc_oid,time,type,gather_id,device_id,oui,device_serialnumber,username"
				+ ",sheet_para,service_id,task_id,order_id,sheet_type,temp_id,is_last_one");
		sql.append(") values (");
		sql.append(obj.getId());
		sql.append("," + obj.getAccOid());
		sql.append("," + obj.getTime());
		sql.append("," + obj.getType());
		sql.append("," + StringUtil.getSQLString(obj.getGatherId()));
		sql.append("," + StringUtil.getSQLString(obj.getDeviceId()));
		sql.append("," + StringUtil.getSQLString(obj.getOui()));
		sql.append("," + StringUtil.getSQLString(obj.getSn()));
		sql.append("," + StringUtil.getSQLString(obj.getUsername()));
		sql.append("," + StringUtil.getSQLString(obj.getSheetPara()));
		sql.append("," + obj.getServiceId());
		sql.append("," + StringUtil.getSQLString(obj.getTaskId()));
		sql.append("," + obj.getOrderId());
		sql.append("," + obj.getSheetType());
		sql.append("," + obj.getTempId());
		sql.append("," + obj.getIsLastOne());
		sql.append(")");
		// 生成入策略日志的sql语句
		StringBuilder logsql = new StringBuilder();
		logsql.append("insert into stb_gw_serv_strategy_log (");
		logsql.append("id,acc_oid,time,type,gather_id,device_id,oui,device_serialnumber,username"
				+ ",sheet_para,service_id,task_id,order_id,sheet_type,temp_id,is_last_one");
		logsql.append(") values (");
		logsql.append(obj.getId());
		logsql.append("," + obj.getAccOid());
		logsql.append("," + obj.getTime());
		logsql.append("," + obj.getType());
		logsql.append("," + StringUtil.getSQLString(obj.getGatherId()));
		logsql.append("," + StringUtil.getSQLString(obj.getDeviceId()));
		logsql.append("," + StringUtil.getSQLString(obj.getOui()));
		logsql.append("," + StringUtil.getSQLString(obj.getSn()));
		logsql.append("," + StringUtil.getSQLString(obj.getUsername()));
		logsql.append("," + StringUtil.getSQLString(obj.getSheetPara()));
		logsql.append("," + obj.getServiceId());
		logsql.append("," + StringUtil.getSQLString(obj.getTaskId()));
		logsql.append("," + obj.getOrderId());
		logsql.append("," + obj.getSheetType());
		logsql.append("," + obj.getTempId());
		logsql.append("," + obj.getIsLastOne());
		logsql.append(")");
		sqlList.add(tempSql.toString());
		sqlList.add(sql.toString());
		sqlList.add(logsql.toString());
		logger.debug("入策略的sql语句-->{}", tempSql.toString() + ";" + sql.toString() + ";"
				+ logsql.toString());
		PrepareSQL psql = new PrepareSQL(tempSql.toString());
		psql.getSQL();
		psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		psql = new PrepareSQL(logsql.toString());
		psql.getSQL();
		return sqlList;
	}

	/**
	 * 执行批量SQL.
	 *
	 * @param arrsql
	 *            SQL语句数组
	 * @return 返回操作的记录条数
	 */
	private int[] doBatch(List<String> sqlList)
	{
		String[] arrsql = new String[sqlList.size()];
		for (int i = 0; i < sqlList.size(); i++)
		{
			arrsql[i] = String.valueOf(sqlList.get(i));
		}
		int[] result = jt.batchUpdate(arrsql);
		arrsql = null;
		return result;
	}

	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
}
