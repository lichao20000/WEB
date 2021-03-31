package com.linkage.module.gwms.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DbUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.interf.dao.I_StrategyDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.StringUtil;

import dao.util.SplitPageResultSetExtractor;

/**
 * @author Jason(3412)
 * @date 2009-7-6
 */
public class SuperDAO implements I_StrategyDAO {

	private static Logger logger = LoggerFactory.getLogger(SuperDAO.class);
	// spring的jdbc模版类
	public JdbcTemplate jt;
	/**
	 * 如果没有查询到记录或者查询到的记录大于1条，返回null
	 * 	如果结果记录为1条返回jt.queryForMap(sql)
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-7
	 * @return Map
	 */
	public Map queryForMap(String sql){
		logger.debug("queryForMap({})", sql);
		Map rMap = null;
		try{
			rMap = jt.queryForMap(sql);
			return rMap;
		}catch(IncorrectResultSizeDataAccessException e){
			logger.error(e.getMessage());
			return rMap;
		}catch(DataAccessException e1){
			logger.error(e1.getMessage());
			return rMap;
		}
	}
	
	
	/**
	 * setDao 注入
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
		this.setDataSourceType(this.getClass().getName());
	}
	
	private void setDataSourceType(String key)
	{
		String type = null;
		type = DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(key);
		if(!StringUtil.IsEmpty(type))
		{
			logger.warn("类："+key+"的数据源类型配置为："+type);
			DataSourceContextHolder.setDBType(type);
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
		if(obj==null){
			return null;
		}
		List<String> sqlList = new ArrayList<String>();
		StringBuilder tempSql = new StringBuilder();
		tempSql.append("delete from gw_serv_strategy where device_id='").append(obj.getDeviceId()).append("' and temp_id=").append(obj.getTempId());
		//生成入策略的sql语句
		StringBuilder sql = new StringBuilder();
		sql.append("insert into gw_serv_strategy (");
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
		//生成入策略日志的sql语句
		StringBuilder logsql = new StringBuilder();
		logsql.append("insert into gw_serv_strategy_log (");
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
		logger.debug("入策略的sql语句-->{}",tempSql.toString()+";"+sql.toString()+";"+logsql.toString());
		PrepareSQL psql = new PrepareSQL(tempSql.toString());
        psql.getSQL();
        psql = new PrepareSQL(sql.toString());
        psql.getSQL();
        psql = new PrepareSQL(logsql.toString());
        psql.getSQL();
		return sqlList;
	}
	
	
	public List<String> strategySQL(StrategyOBJ obj,String tableName)
	{
		logger.debug("strategySQL({})", obj);
		if(obj==null){
			return null;
		}
		
		List<String> sqlList = new ArrayList<String>();
		StringBuilder tempSql = new StringBuilder();
		tempSql.append("delete from "+tableName+" where device_id='").append(obj.getDeviceId()).append("' and temp_id=").append(obj.getTempId());
		//生成入策略的sql语句
		StringBuilder sql = new StringBuilder();
		sql.append("insert into "+tableName+" (");
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
		//生成入策略日志的sql语句
		StringBuilder logsql = new StringBuilder();
		logsql.append("insert into "+tableName+"_log (");
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
		logger.debug("入策略的sql语句-->{}",tempSql.toString()+";"+sql.toString()+";"+logsql.toString());
		PrepareSQL psql = new PrepareSQL(tempSql.toString());
        psql.getSQL();
        psql = new PrepareSQL(sql.toString());
        psql.getSQL();
        psql = new PrepareSQL(logsql.toString());
        psql.getSQL();
		return sqlList;
	}
	
	/**
	 * 生成入策略的sql语句
	 *
	 * @author wangsenbo
	 * @date Jun 11, 2010
	 * @param 
	 * @return List<String>
	 */
	public List<String> stbStrategySQL(StrategyOBJ obj)
	{
		logger.debug("strategySQL({})", obj);
		if(obj==null){
			return null;
		}
		List<String> sqlList = new ArrayList<String>();
		StringBuilder tempSql = new StringBuilder();
		tempSql.append("delete from stb_gw_serv_strategy where device_id='").append(obj.getDeviceId()).append("' and temp_id=").append(obj.getTempId());
		//生成入策略的sql语句
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
		//生成入策略日志的sql语句
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
		logger.debug("入策略的sql语句-->{}",tempSql.toString()+";"+sql.toString()+";"+logsql.toString());
		PrepareSQL psql = new PrepareSQL(tempSql.toString());
        psql.getSQL();
        psql = new PrepareSQL(sql.toString());
        psql.getSQL();
        psql = new PrepareSQL(logsql.toString());
        psql.getSQL();
		return sqlList;
	}
	
	/**
	 * 增加策略
	 */
	public Boolean addStrategy(StrategyOBJ obj) {
		logger.debug("addStrategy({})", obj);
		if (obj == null) {
			logger.debug("obj == null");
			return false;
		}
		List<String> sqlList = strategySQL(obj);

		int[] result = doBatch(sqlList);
		if (result != null && result.length > 0) {
			logger.debug("策略入库：  成功");
			return true;		
		} else {
			logger.debug("策略入库：  失败");
			return false;
		}
	}
	
	/**
	 * 普通分页查询<br>
	 * <b>如果结果结合比较大应该调用setFetchsize() 和setMaxRow两个方法来控制一下，否则会内存溢出</b>
	 * @see #setFetchSize(int)
	 * @see #setMaxRows(int)
	 * @param sql
	 *            查询的sql语句
	 * @param startRow
	 *            起始行
	 * @param rowsCount
	 *            获取的行数
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Map> querySP(String sql, int startRow, int rowsCount)
			throws DataAccessException
	{
		return querySP(sql, startRow, rowsCount, new ColumnMapRowMapper());
	}
	/**
	 * 自定义行包装器查询<br>
	 * <b>如果结果结合比较大应该调用setFetchsize() 和setMaxRow两个方法来控制一下，否则会内存溢出</b>
	 * @see #setFetchSize(int)
	 * @see #setMaxRows(int)
	 * @param sql
	 *            查询的sql语句
	 * @param startRow
	 *            起始行
	 * @param rowsCount
	 *            获取的行数
	 * @param rowMapper
	 *            行包装器
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Map> querySP(String sql, int startRow, int rowsCount, RowMapper rowMapper)
			throws DataAccessException
	{
		// Sybase SQL use top 
		sql = DbUtil.sybaseSqlTop(sql, startRow + rowsCount);
		if(DBUtil.GetDB() == Global.DB_MYSQL) {
			if(startRow < 1) {
				startRow = 1;
			}
			StringBuilder sb = new StringBuilder(sql);
			sb.append(" limit ").append(startRow -1).append(",").append(rowsCount);
			sql = sb.toString();
			startRow = 1;
		}
		return (List) jt.query(sql, new SplitPageResultSetExtractor(rowMapper, startRow,
				rowsCount));
	}
	
	/**
	 * 执行批量SQL.
	 * 
	 * @param arrsql
	 *            SQL语句数组
	 * @return 返回操作的记录条数
	 */
	public int[] doBatch(List<String> sqlList) {
		String[] arrsql = new String[sqlList.size()];
		for (int i = 0; i < sqlList.size(); i++) {
			arrsql[i] = String.valueOf(sqlList.get(i));
		}
		int[] result = jt.batchUpdate(arrsql);
		arrsql = null;
		return result;
	}

}
