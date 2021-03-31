/**
 * 
 */
package com.linkage.module.gwms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.service.act.ServStrategyConfigACT;

import dao.util.JdbcTemplateExtend;

/**
 * @author OneLineSky E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2010-6-10
 * @category dao.confTaskView
 * 
 */
public class ServStrategyConfigDAO {

	private static Logger logger = LoggerFactory.getLogger(ServStrategyConfigDAO.class);

	private JdbcTemplateExtend jt;

	private Map<String, String> status_map = new HashMap<String, String>();
	private Map<String, String> faultCodeMap = new HashMap<String, String>();
	private Map<String, String> serviceCodeMap = new HashMap<String, String>();
	
	public ServStrategyConfigDAO() {
		status_map.put("0", "等待执行");
		status_map.put("1", "预读PVC");
		status_map.put("2", "预读绑定端");
		status_map.put("3", "预读无线");
		status_map.put("4", "业务下发");
		status_map.put("100", "执行完成");
	}

	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}
	
	public List getServStrategy(int curPage_splitPage, int num_splitPage,
			String cityId, String time_start, String time_end,
			String device_serialnumber, String username,String taskId,int type) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(" select distinct a.device_id,a.username,b.oui,b.device_serialnumber,a.task_id from "+getTableName(type)+" a,tab_gw_device b where a.device_id=b.device_id ");
		if (!CityDAO.isAdmin(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and b.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (!(null == time_start || "".equals(time_start))) {
			pSQL.append(" and a.time > ");
			pSQL.append(String.valueOf(new DateTimeUtil(time_start)
					.getLongTime()));
		}

		if (!(null == time_end || "".equals(time_end))) {
			pSQL.append(" and a.time < ");
			pSQL.append(String
					.valueOf(new DateTimeUtil(time_end).getLongTime()));
		}

		if (!(null == device_serialnumber || "".equals(device_serialnumber))) {
			if(device_serialnumber.length()>5){
				pSQL.append(" and b.dev_sub_sn='");
				pSQL.append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(" and b.device_serialnumber like '%");
			pSQL.append(device_serialnumber);
			pSQL.append("' ");
		}

		if (!(null == username || "".equals(username))) {
			pSQL.append(" and a.username = '");
			pSQL.append(username);
			pSQL.append("' ");
		}
		
		if (!(null == taskId || "".equals(taskId))) {
			pSQL.append(" and a.task_id = '");
			pSQL.append(taskId);
			pSQL.append("' ");
		}

		return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("device_id", rs.getString("device_id"));
				map.put("task_id", rs.getString("task_id"));
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("username", rs.getString("username"));
				
				return map;
			}
		});
	}
	/**
	 *        策略分表
			* @param strategyType
			* @return
	 */
	private String getTableName(int strategyType){
		StringBuffer tableName = null;
		logger.warn("param-strategyType is:"+strategyType);
		switch(strategyType){
			// 业务下发查询
			case 1:
				tableName = new StringBuffer(LipossGlobals.getLipossProperty("strategy_tabname.serv.tabname"));
				break;
			// 批量软件
			case 2:
				tableName = new StringBuffer(LipossGlobals.getLipossProperty("strategy_tabname.soft.tabname"));
				break;
			// 批量参数配置
			case 3:
				tableName = new StringBuffer(LipossGlobals.getLipossProperty("strategy_tabname.batch.tabname"));
				break;
			// 简单软件升级
			case 4:
				tableName = new StringBuffer(LipossGlobals.getLipossProperty("strategy_tabname.strategy.tabname"));
				break;
			// 恢复出厂设置
			case 5:
				tableName = new StringBuffer(LipossGlobals.getLipossProperty("strategy_tabname.serv.tabname"));
				break;
			default:
				tableName = new StringBuffer(LipossGlobals.getLipossProperty("strategy_tabname.strategy.tabname"));
				break;
		}
		logger.warn("final table is:"+tableName);
		return tableName.toString();
	}
	public int getServStrategyCount(int curPage_splitPage, int num_splitPage,
			String cityId, String time_start, String time_end,
			String device_serialnumber, String username,String taskId,int strategyType) {
		
		PrepareSQL pSQL = new PrepareSQL();
		
		// 数据库类型为Oracle
		if (DBUtil.GetDB() == 1) {
			pSQL.setSQL(" select count(distinct a.device_id||a.username||a.task_id)  from ");
		}else if(DBUtil.GetDB()==3){
			pSQL.setSQL(" select count(distinct a.device_id,a.username,a.task_id)  from ");
		}else {
			pSQL.setSQL(" select count(distinct a.device_id+a.username+a.task_id)  from ");
		}
		pSQL.append(getTableName(strategyType)+" a,tab_gw_device b where a.device_id=b.device_id ");
		if (!CityDAO.isAdmin(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and b.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (!(null == time_start || "".equals(time_start))) {
			pSQL.append(" and a.time > ");
			pSQL.append(String.valueOf(new DateTimeUtil(time_start)
					.getLongTime()));
		}

		if (!(null == time_end || "".equals(time_end))) {
			pSQL.append(" and a.time < ");
			pSQL.append(String
					.valueOf(new DateTimeUtil(time_end).getLongTime()));
		}

		if (!(null == device_serialnumber || "".equals(device_serialnumber))) {
			if(device_serialnumber.length()>5){
				pSQL.append(" and b.dev_sub_sn='");
				pSQL.append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length()));
				pSQL.append("' ");
			}			
			pSQL.append(" and b.device_serialnumber like '%");
			pSQL.append(device_serialnumber);
			pSQL.append("' ");
		}

		if (!(null == username || "".equals(username))) {
			pSQL.append(" and a.username = '");
			pSQL.append(username);
			pSQL.append("' ");
		}
		
		if (!(null == taskId || "".equals(taskId))) {
			pSQL.append(" and a.task_id = '");
			pSQL.append(taskId);
			pSQL.append("' ");
		}
		
		int total = jt.queryForInt(pSQL.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}

		return maxPage;
	}
	
	@SuppressWarnings("unchecked")
	public List getServStrategyInfo(List<String> deviceList,int type){
		
		PrepareSQL pSQL = new PrepareSQL();
//		pSQL.setSQL("select fault_code, fault_desc,fault_reason,solutions from tab_cpe_faultcode");
//		List<Map> faultCodeList = jt.queryForList(pSQL.getSQL());
//		Map<String, Map<String, String>> faultMap = new HashMap<String, Map<String,String>>();
//		Map<String, String> faultInfoMap = null;
//		for (Map map : faultCodeList)
//		{
//			faultInfoMap = new HashMap<String, String>();
//			faultInfoMap.put("fault_desc", StringUtil.getStringValue(map.get("fault_desc")));
//			faultInfoMap.put("fault_reason", StringUtil.getStringValue(map.get("fault_reason")));
//			faultInfoMap.put("solutions", StringUtil.getStringValue(map.get("solutions")));
//			faultMap.put(StringUtil.getStringValue(map.get("fault_code")),faultInfoMap);
//		}
		pSQL.setSQL("select distinct service_id,service_name from tab_service ");
		List serviceList = jt.queryForList(pSQL.getSQL());
		for(int i=0;i<serviceList.size();i++){
			Map fcMap = (Map) serviceList.get(i);
			serviceCodeMap.put(String.valueOf(fcMap.get("service_id")), 
							 String.valueOf(fcMap.get("service_name")));
		}
		
		pSQL.setSQL(" select a.id,a.device_id,a.username,a.task_id,a.status,a.result_id,a.result_desc," +
				" a.time,a.start_time,a.end_time,a.service_id from "+getTableName(type)+
				" a where 1=1 ");
		if (null!=deviceList) {
			pSQL.append(" and a.device_id in ('"
					+ StringUtils.weave(deviceList, "','") + "')");
		}
		pSQL.append(" order by a.device_id,a.service_id ");
		
		List list_ = jt.query(pSQL.getSQL(),  new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				map.put("id", rs.getString("id"));
				map.put("task_id", rs.getString("task_id"));	
				map.put("username", rs.getString("username"));			
				map.put("result_id", rs.getString("result_id"));
				map.put("status", rs.getString("status"));
				map.put("result_desc", rs.getString("result_desc"));
				map.put("time", new DateTimeUtil(rs.getLong("time")*1000).getLongDate());
				if(rs.getLong("start_time")==0){
					map.put("start_time", "");
				}else{
					map.put("start_time", new DateTimeUtil(rs.getLong("start_time")*1000).getLongDate());
				}
				map.put("end_time", new DateTimeUtil(rs.getLong("end_time")*1000).getLongDate());
				map.put("service_id", rs.getString("service_id"));
				return map;
			}
		});
		for(int i=0;i<list_.size();i++){
			Map map = (Map)list_.get(i);
			if(Global.G_Fault_Map.get(StringUtil.getIntegerValue(map.get("result_id"))) == null)
			{
				map.put("result_id", "");
			}
			else
			{
				map.put("result_id", Global.G_Fault_Map.get(StringUtil.getIntegerValue(map.get("result_id"))).getFaultDesc());
			}
			map.put("service_name", serviceCodeMap.get(map.get("service_id")));
			map.put("status", status_map.get(map.get("status")));
		}
		
		return list_;
	}
	
	public int cancelData(String deviceId,String taskId,String id,int type){
		PrepareSQL pSQL = new PrepareSQL();
		if(null==taskId || "".equals(taskId.trim())){
			pSQL.setSQL("delete from "+getTableName(type)+" where device_id=? and id=? ");
			pSQL.setString(1, deviceId);
			pSQL.setLong(2, Long.parseLong(id));
		}else{
			pSQL.setSQL("delete from "+getTableName(type)+" where device_id=? and task_id=? ");
			pSQL.setString(1, deviceId);
			pSQL.setString(2, taskId);
		}
		return jt.update(pSQL.toString());
	}

	public int resetData(String deviceId,String taskId,String id,int type){
		PrepareSQL pSQL = new PrepareSQL();
		if(null==taskId || "".equals(taskId.trim())){
			pSQL.setSQL(" update "+getTableName(type)+" set status=0, result_id=0, result_desc=null,type=4 where device_id=? and id=? ");
			pSQL.setString(1, deviceId);
			pSQL.setLong(2, Long.parseLong(id));
		}else{
			pSQL.setSQL(" update "+getTableName(type)+" set status=0, result_id=0, result_desc=null,type=4 where device_id=? and task_id=? ");
			pSQL.setString(1, deviceId);
			pSQL.setString(2, taskId);
		}
		return jt.update(pSQL.toString());
	}
	
}
