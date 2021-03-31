
package com.linkage.module.gwms.dao.gw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class StrategyDao extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(StrategyDao.class);
	private Map<String, String> status_map = new HashMap<String, String>();

	public StrategyDao()
	{
		status_map.put("0", "等待执行");
		status_map.put("1", "预读PVC");
		status_map.put("2", "预读绑定端口");
		status_map.put("3", "预读无线");
		status_map.put("4", "业务下发");
		status_map.put("100", "执行完成");
	}

	/**
	 * 通过策略ID获得策略数据
	 * 
	 * @author wangsenbo
	 * @date Nov 5, 2009
	 * @return Map 没有数据返回null
	 */
	public Map getStrategyById(String strategyId)
	{
		logger.debug("getStrategyById({})", strategyId);
		
		Map rmap = null;
		if (false == StringUtil.IsEmpty(strategyId))
		{
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psql.append("select id,status,result_id,start_time,end_time ");
			}else{
				psql.append("select * ");
			}
			psql.append("from gw_serv_strategy where id=? ");
			psql.setLong(1, StringUtil.getLongValue(strategyId));
			Map tmap = queryForMap(psql.getSQL());
			if (tmap != null)
			{
				rmap = new HashMap<String, String>();
				rmap.put("strategyId", StringUtil.getStringValue(tmap.get("id")));
				rmap.put("status", status_map.get(StringUtil.getStringValue(tmap.get("status"))));
				rmap.put("result", Global.G_Fault_Map.get(
						StringUtil.getIntegerValue(tmap.get("result_id"))).getFaultDesc());
				// 将startTime转换成时间
				String startTime = StringUtil.getStringValue(tmap.get("start_time"));
				if (false == StringUtil.IsEmpty(startTime))
				{
					DateTimeUtil dateTimeUtil = new DateTimeUtil(Long
							.parseLong(startTime) * 1000);
					startTime = dateTimeUtil.getLongDate();
					dateTimeUtil = null;
				}
				rmap.put("startTime", startTime);
				// 将endTime转换成时间
				String endTime = StringUtil.getStringValue(tmap.get("end_time"));
				if (false == StringUtil.IsEmpty(endTime))
				{
					DateTimeUtil dateTimeUtil = new DateTimeUtil(Long
							.parseLong(endTime) * 1000);
					endTime = dateTimeUtil.getLongDate();
					dateTimeUtil = null;
				}
				rmap.put("endTime", endTime);
			}
		}
		return rmap;
	}
	
	/**
	 * 通过设备SN获得策略数据
	 * 
	 * @author zzs
	 * @date 2018-11-13
	 * @return 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getStrategyBySN(String deviceSN,String strategyId,String gw_type,int curPage_splitPage,int num_splitPage)
	{
		String tableName = "";
		String device_tableName = "";
		if(gw_type.endsWith("1"))
		{
			tableName = "gw_serv_strategy";
			device_tableName = "tab_gw_device";
		}else if (gw_type.endsWith("4"))
		{
			tableName = "stb_gw_serv_strategy";
			device_tableName = "stb_tab_gw_device";
		}else
		{
			logger.warn("查询未获取到gw_type字段，无法判断设备类型，返回！");
			return null;
		}
		logger.warn("查询的表名为：" + tableName);
		String sql = "select a.id,a.status,a.result_id,a.start_time,a.end_time,b.device_serialnumber from " + tableName + " a,"+device_tableName+" b"+" where a.service_id=5";
		if (!StringUtil.IsEmpty(deviceSN))
		{
			sql = sql + " and b.device_serialnumber like '%" + deviceSN + "%'";
		}
		if(!StringUtil.IsEmpty(strategyId))
		{
			sql= sql+" and a.id = '" + strategyId +"'" ;
		}
		sql = sql + "and a.device_id=b.device_id";
		List<Map> list = querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage);
		if(null == list || list.isEmpty()){
			return new ArrayList();
		}
		for (Map map : list)
		{
			map.put("strategyId", StringUtil.getStringValue(map.get("id")));
			map.put("status",
					status_map.get(StringUtil.getStringValue(map.get("status"))));
			Integer resultid = StringUtil.getIntegerValue(map.get("result_id"));
			if (1 == resultid)
			{
				map.put("result", "成功");
			}
			else if (0 == resultid || 2 == resultid)
			{
				map.put("result", "执行中");
			}
			else
			{
				map.put("result", "失败");
			}
			// 将startTime转换成时间
			String startTime = StringUtil.getStringValue(map.get("start_time"));
			if (false == StringUtil.IsEmpty(startTime))
			{
				DateTimeUtil dateTimeUtil = new DateTimeUtil(
						Long.parseLong(startTime) * 1000);
				startTime = dateTimeUtil.getLongDate();
				dateTimeUtil = null;
			}
			map.put("startTime", startTime);
			// 将endTime转换成时间
			String endTime = StringUtil.getStringValue(map.get("end_time"));
			if (false == StringUtil.IsEmpty(endTime))
			{
				DateTimeUtil dateTimeUtil = new DateTimeUtil(
						Long.parseLong(endTime) * 1000);
				endTime = dateTimeUtil.getLongDate();
				dateTimeUtil = null;
			}
			map.put("endTime", endTime);
		}
		return list;
	}
	
	/**
	 * 通过设备SN获得策略数据
	 * 
	 * @author zzs
	 * @date 2018-11-13
	 * @return 
	 */
	public int getStrategyBySNCounts(String deviceSN, String strategyId,String gw_type,int num_splitPage)
	{
		String tableName = "";
		String device_tableName ="";
		if (gw_type.endsWith("1"))
		{
			tableName = "gw_serv_strategy";
			device_tableName = "tab_gw_device";
		}
		else if (gw_type.endsWith("4"))
		{
			tableName = "stb_gw_serv_strategy";
			device_tableName = "stb_tab_gw_device";
		}
		else
		{
			logger.warn("查询未获取到gw_type字段，无法判断设备类型");
			return 0;
		}
		logger.warn("统计数量查询的表名为：" + tableName);
		String sql = "select count(*) from " + tableName + " a,"+device_tableName+" b"+" where a.service_id=5";
		if (null != deviceSN && !deviceSN.isEmpty())
		{
			sql = sql + " and b.device_serialnumber like '%" + deviceSN + "%'";
		}
		if (!StringUtil.IsEmpty(strategyId))
		{
			sql = sql + " and a.id = '" + strategyId + "'";
		}
		sql = sql +"and a.device_id=b.device_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		int total = jt.queryForInt(sql);
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
}
