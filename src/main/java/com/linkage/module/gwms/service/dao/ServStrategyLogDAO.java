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
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.User;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.DataSourceContextHolder;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

import dao.util.JdbcTemplateExtend;

/**
 * @author OneLineSky E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2008-12-18
 * @category dao.confTaskView
 * 
 */
public class ServStrategyLogDAO extends SuperDAO {
	
	private static Logger logger = LoggerFactory.getLogger(ServStrategyLogDAO.class);

	private JdbcTemplateExtend jt;

	private Map<String, String> status_map = new HashMap<String, String>();
	
	private HashMap<String, String> execTypeMap = new HashMap<String, String>();
	
	public ServStrategyLogDAO() {
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

	/**
	 * 初始化FaultCode
	 * @author gongsj
	 * @date 2009-9-23
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> initFaultCode() {
		Map<String, String> faultCodeMap = new HashMap<String, String>();
		
		PrepareSQL pSQL = new PrepareSQL();
		
		pSQL.setSQL("select fault_code, fault_reason from tab_cpe_faultcode");
		List<Map<String, String>> faultCodeList = jt.queryForList(pSQL.getSQL());
		
		for(Map<String, String> fcMap : faultCodeList) {
			faultCodeMap.put(String.valueOf(fcMap.get("fault_code")), String.valueOf(fcMap.get("fault_reason")));
		}
		
		return faultCodeMap;
	}
	/**
	 * 查询当前所有的业务操作
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getAllService_idList() {
		PrepareSQL pSQL = new PrepareSQL("select serv_type_id,serv_type_name from tab_gw_serv_type");
		return jt.queryForList(pSQL.getSQL());
	}
	
	/**
	 * 查询当前所有的厂商
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getAllVendor_idList() {
		PrepareSQL pSQL = new PrepareSQL("select vendor_id,vendor_name from tab_vendor");
		return jt.queryForList(pSQL.getSQL());
	}
	
	// 如果前台传过来的数据源 == 报表
	private void setDataSourceType(String datasource) {
		DataSourceContextHolder.clearDBType();
		if ("report".equals(datasource)) {
			String type = null;
			type = DataSourceTypeCfgPropertiesManager.getInstance()
					.getConfigItem(ServStrategyLogDAO.class.getName() + "ChangeDB");
			if (!StringUtil.IsEmpty(type)) {
				logger.warn("类：" + this.getClass().getName() + "的数据源类型配置为："
						+ type);
				DataSourceContextHolder.setDBType(type);
			}
		}
	}
	
	private String getTableName(Integer strategyType){
		StringBuffer tableName = null;
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
		tableName = tableName.append("_log");
		return tableName.toString();
	}
	
	/**
	 * 分页查询策略记录
	 * 
	 * @param	curPage_splitPage	
	 * @param	num_splitPage		
	 * @param	area_id				域
	 * @param	time_start			定制开始时间
	 * @param	time_end			定制结束时间
	 * @param	operatesName		
	 * @param	status				状态
	 * @param	service_id			服务ID
	 * @param	type				执行策略
	 * @param	vendor_id			厂商
	 * @param	device_serialnumber	设备序列号
	 * 
	 * @return	List
	 */
	public List getServStrategList(Integer strategyType, int curPage_splitPage, int num_splitPage,User user, 
			String time_start, String time_end, String operatesName,String status,
			String service_id,String type,String vendor_id,String device_serialnumber,
			String username, String datasource) {
		// 设置数据源
		this.setDataSourceType(datasource);
		
		if(execTypeMap.size()<1){
			execTypeMap = queryExecTypeMap();
		}
		
		PrepareSQL pSQL = new PrepareSQL();
		String uString = null;
		// 业务下发
		if(strategyType == 1){
			uString = "a.username";
		}
		else{
			uString = "b.device_id_ex as username";
		}
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		String sql = "select distinct a.status,a.result_id,a.result_desc,a.time,a.type," +
				" a.start_time,a.end_time,"+uString+",a.task_id,a.oui," +
				" b.device_serialnumber," +
				" d.serv_type_name from "+getTableName(strategyType)+" a,tab_gw_device b,tab_service c," +
				" tab_gw_serv_type d where a.device_id=b.device_id and a.service_id=c.service_id " +
				" and c.serv_type_id=d.serv_type_id ";
		// 业务下发，只查询宽带、IPTV、VOIP
		if(strategyType == 1){
			sql +=" and d.serv_type_id in(10,11,14)";
		}
		// 简单软件升级
		else if(strategyType == 4){
			sql+=" and d.serv_type_id=5";
		}
		// 恢复出厂设置
		else if(strategyType == 5){
			sql+=" and d.serv_type_id=8";
		}
		pSQL.setSQL(sql);

//		if( 1!=area_id ) {
//			
//			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
//			pSQL.append(area_id+") ");
//			
//		}
		if(!CityDAO.isAdmin(user.getCityId())){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(user.getCityId());
			pSQL.append(" and b.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		
		if(!(null == time_start || "".equals(time_start))){
			pSQL.append(" and a.time > "+(new DateTimeUtil(time_start).getLongTime()));
		}
		
		if(!(null == time_end || "".equals(time_end))){
			pSQL.append(" and a.time < "+(new DateTimeUtil(time_end).getLongTime()));
		}
		
		if (!(null == operatesName || "".equals(operatesName))) {
			pSQL.append(" and b.username = '");
			pSQL.append(operatesName);
			pSQL.append("' ");
		}
		
		if (!(null == status || "".equals(status))) {
			pSQL.append(" and a.status = ");
			pSQL.append(status);
		}
		
		if (!(null == service_id || "".equals(service_id))) {
			pSQL.append(" and c.serv_type_id =");
			pSQL.append(service_id);
		}
		
		if (!(null == type || "".equals(type))) {
			pSQL.append(" and a.type = ");
			pSQL.append(type);
		}
		
		if (!(null == vendor_id || "".equals(vendor_id))) {
			pSQL.append(" and a.oui in (select oui from tab_vendor_oui where vendor_id='");
			pSQL.append(vendor_id);
			pSQL.append("') ");
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
			// 业务下发
			if(strategyType==1){
				pSQL.append(" and a.username = '");
			}else{
				pSQL.append(" and b.device_id_ex = '");
			}
			pSQL.append(username);
			pSQL.append("' ");
		}

		pSQL.append(" order by a.time desc, a.task_id ");
		
		return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						
						Map<String, String> map = new HashMap<String, String>();

						map.put("status", status_map.get(rs.getString("status")));

						int result_id_ = StringUtil.getIntegerValue(rs.getString("result_id"));
						if(Global.G_Fault_Map.get(result_id_)==null){
							map.put("result_id", "");
						}else{
							map.put("result_id", Global.G_Fault_Map.get(result_id_).getFaultReason());
						}

						map.put("result_desc", rs.getString("result_desc"));

						map.put("time", new DateTimeUtil(Long.valueOf(rs.getString("time")+"000")).getLongDate());

						if (null == rs.getString("start_time") || "null".equals(rs.getString("start_time"))) {
							map.put("start_time", "");
						} else {
							map.put("start_time", new DateTimeUtil(Long.valueOf(rs.getString("start_time")+"000")).getLongDate());
						}
						
						if (null == rs.getString("end_time") || "null".equals(rs.getString("end_time"))) {
							map.put("end_time", "");
						} else {
							map.put("end_time", new DateTimeUtil(Long.valueOf(rs.getString("end_time")+"000")).getLongDate());
						}
						
						map.put("oui", rs.getString("oui"));

						map.put("device_serialnumber", rs.getString("device_serialnumber"));

						map.put("username", rs.getString("username"));

//						map.put("service_name", rs.getString("service_name"));
						
						map.put("serv_type_name", rs.getString("serv_type_name"));
						
						map.put("type_name", execTypeMap.get(rs.getString("type")));
						
						return map;
					}
				});
	}

	/**
	 * 策略记录总记录数
	 * 
	 * @param	curPage_splitPage	
	 * @param	num_splitPage		
	 * @param	area_id				域
	 * @param	time_start			定制开始时间
	 * @param	time_end			定制结束时间
	 * @param	operatesName		
	 * @param	status				状态
	 * @param	service_id			服务ID
	 * @param	type				执行策略
	 * @param	vendor_id			厂商
	 * @param	device_serialnumber	设备序列号
	 * 
	 * @return	int
	 */
	public int getServStrategyCount(Integer strategyType, int curPage_splitPage, int num_splitPage,User user, 
			String time_start, String time_end, String operatesName,String status,
			String service_id,String type,String vendor_id,String device_serialnumber,
			String username, String datasource) {
		// 设置数据源
		this.setDataSourceType(datasource);
		
		PrepareSQL pSQL = new PrepareSQL();
		
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		String sql = "select count(distinct a.id) from "+getTableName(strategyType)+" a,tab_gw_device b,tab_service c," +
				" tab_gw_serv_type d where a.device_id=b.device_id and a.service_id=c.service_id " +
				" and c.serv_type_id=d.serv_type_id ";
		// 业务下发，只查询宽带、IPTV、VOIP
		if(strategyType == 1){
			sql +=" and d.serv_type_id in(10,11,14)";
		}
		// 简单软件升级
		else if(strategyType == 4){
			sql+=" and d.serv_type_id=5";
		}
		// 恢复出厂设置
		else if(strategyType == 5){
			sql+=" and d.serv_type_id=8";
		}
		pSQL.setSQL(sql);
		
//		if( 1!=area_id ) {
//			
//			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
//			pSQL.append(area_id+") ");
//			
//		}
		if(!CityDAO.isAdmin(user.getCityId())){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(user.getCityId());
			pSQL.append(" and b.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if(!(null == time_start || "".equals(time_start))){
			pSQL.append(" and a.time > "+(new DateTimeUtil(time_start).getLongTime()));
		}
		
		if(!(null == time_end || "".equals(time_end))){
			pSQL.append(" and a.time < "+(new DateTimeUtil(time_end).getLongTime()));
		}
		
		if (!(null == operatesName || "".equals(operatesName))) {
			pSQL.append(" and b.username = '");
			pSQL.append(operatesName);
			pSQL.append("' ");
		}
		
		if (!(null == status || "".equals(status))) {
			pSQL.append(" and a.status = ");
			pSQL.append(status);
		}
		
		if (!(null == service_id || "".equals(service_id))) {
			pSQL.append(" and c.serv_type_id =");
			pSQL.append(service_id);
		}
		
		if (!(null == type || "".equals(type))) {
			pSQL.append(" and a.type = ");
			pSQL.append(type);
		}
		
		if (!(null == vendor_id || "".equals(vendor_id))) {
			pSQL.append(" and a.oui in (select oui from tab_vendor_oui where vendor_id='");
			pSQL.append(vendor_id);
			pSQL.append("') ");
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
			// 业务下发
			if(strategyType==1){
				pSQL.append(" and a.username = '");
			}else{
				pSQL.append(" and b.device_id_ex = '");
			}
			pSQL.append(username);
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
	
	
	/**
	 * 获取策略执行方式的HashMap<type_id, type_name>
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2010-7-2
	 * @return HashMap<String,String>
	 */
	public HashMap<String, String> queryExecTypeMap(){
		String strSQL = "select type_id,type_name from gw_strategy_type";
		PrepareSQL psql = new PrepareSQL(strSQL);
		return DataSetBean.getMap(psql.getSQL());
	}
}
