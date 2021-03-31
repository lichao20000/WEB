
package com.linkage.module.gtms.config.dao;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-22
 * @category com.linkage.module.gtms.config.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ConfigMaxTerminalDaoImp extends SuperDAO implements ConfigMaxTerminalDao
{

	private Logger logger = LoggerFactory.getLogger(ConfigMaxTerminalDaoImp.class);
	
	/**执行状态参数MAP */
	private Map<String, String> status_map = new HashMap<String, String>();

	public ConfigMaxTerminalDaoImp()
	{
		status_map.put("0", "等待执行");
		status_map.put("1", "预读PVC");
		status_map.put("2", "预读绑定端");
		status_map.put("3", "预读无线");
		status_map.put("4", "业务下发");
		status_map.put("100", "执行完成");
	}
 
	
	/**
	 * 查询最大上网数
	 * 
	 * @param device_id 设备id
	 * return map
	 */
	public Map<String, String> queryMaxTerminal(String device_id)
	{
		logger.debug("queryMaxTerminal()");
		String sql = "select g.device_id, g.m_mode, g.total_number from gw_mwband g where g.device_id='"
				+ device_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		DBOperation db = new DBOperation();
		Map<String, String> map = new HashMap<String, String>();
		map = db.getRecord(psql.getSQL());
		return map;
	}

	/**
	 * 
	 */
	public String updateMaxTerminal(String device_id, String mode, String total_number)
	{
		logger.debug("updateMaxTerminal()");
		StringBuffer sql = new StringBuffer();
		sql.append("update gw_mwband set ");
		if (!StringUtil.IsEmpty(mode))
		{
			sql.append(" m_mode=").append(mode);
		}
		if (!StringUtil.IsEmpty(total_number))
		{
			sql.append(", total_number=").append(total_number);
		}
		sql.append(" where device_id='").append(device_id).append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int num = jt.update(psql.getSQL());
		logger.info(num + "");
		return num + "";
	}
	
	/**
	 * 获取策略信息
	 * 
	 * @param id  策略表id
	 * @return map
	 */
	public Map<String, String> queryStrategy(String id)
	{
		logger.debug("queryStrategy()");
		String sql = "select s.id, s.device_id, s.start_time, s.end_time, s.status, s.result_id, t.service_name from gw_serv_strategy s left join  tab_service t on  s.service_id=t.service_id  where s.id="
				+ id;
		PrepareSQL psql = new PrepareSQL(sql);
		Map<String, String> map = new HashMap<String, String>();
		DBOperation db = new DBOperation();
		map = db.getRecord(psql.getSQL());
	
		return this.getMap(map);
	}
	
	/**
	 * 获取策略信息
	 * 
	 * @param device_id  设备id
	 * @return map
	 */
	public Map<String, String> queryStrategyByDeviceId(String device_id)
	{
		logger.debug("queryStrategyByDeviceId()");
		String tableName = LipossGlobals.getLipossProperty("strategy_tabname.batch");
		String sql = "select s.id, s.device_id, s.start_time, s.end_time, s.status, s.result_id, t.service_name from " + tableName + " s left join tab_service t on s.service_id=t.service_id where s.device_id='"
				+ device_id + "' and s.service_id=101";
		PrepareSQL psql = new PrepareSQL(sql);
		Map<String, String> map = new HashMap<String, String>();
		DBOperation db = new DBOperation();
		map = db.getRecord(psql.getSQL());
		
		return this.getMap(map);
	}
	
	//转换时间显示类型
	private String switchLongToDate(String time)
	{
		if (StringUtil.IsEmpty(time))
		{
			return "";
		}
		long fromtime = StringUtil.getLongValue(time) * 1000L;
		DateTimeUtil dt = new DateTimeUtil(fromtime);
		return dt.getLongDate();
	}
	
	//处理数据合并
	private Map<String,String> getMap(Map<String,String> fromMap){
		
		Map<String,String> map = fromMap;
		
		
		if (null != map && !map.isEmpty())
		{
			String start = map.get("start_time");
			String start_time = this.switchLongToDate(start);
			String end = map.get("end_time");
			String end_time = this.switchLongToDate(end);
			map.put("start_time", start_time);
			map.put("end_time", end_time);
			String status = map.get("status");
			map.put("status", status_map.get(status));
			map.put("fault_desc",
					Global.G_Fault_Map
							.get(StringUtil.getIntegerValue(map.get("result_id"))).getFaultDesc());
		}
		
		return map;
	}
}
