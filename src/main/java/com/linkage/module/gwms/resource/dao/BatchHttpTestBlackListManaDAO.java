package com.linkage.module.gwms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class BatchHttpTestBlackListManaDAO extends SuperDAO {

	private static Logger logger = LoggerFactory
			.getLogger(BatchHttpTestBlackListManaDAO.class);
	private  HashMap<String,String> deviceTypeMap = null;
	
	private  HashMap<String,String> faultCodeMap = null;


	/**
	 * 查看定制详细信息
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param cityId
	 * @param taskName
	 * @return
	 */
	public List<HashMap<String,String>> getOrderTaskList(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName) {
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select a.add_time,a.task_status,a.task_desc,a.task_name,a.task_id,b.acc_loginname ");
		}else{
			sql.append("select a.*,b.acc_loginname ");
		}
		
		sql.append("from tab_blacklist_task a left join tab_accounts b on a.acc_oid=b.acc_oid ");
		sql.append("where 1=1 ");
		if(acc_oid!=1){
			sql.append("b.acc_oid = "+ acc_oid);
		}
		
		if(!StringUtil.IsEmpty(accName) ){
			sql.append(" and b.acc_loginname like '%" + accName + "%'");
		}
		
		if (!StringUtil.IsEmpty(taskName)) {
			sql.append(" and a.task_name='").append(taskName).append("'");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			sql.append(" and a.add_time>=").append(getTime(startTime));
		}
		if (!StringUtil.IsEmpty(endTime)) {
			sql.append(" and a.add_time<=").append(getTime(endTime) + 86399);
		}
		
		sql.append(" order by a.add_time desc ");
		
		List<HashMap<String,String>> list;
		list = DBOperation.getRecords(thisQuerySP(sql.toString(),(curPage_splitPage - 1)
					* num_splitPage+1, num_splitPage));
		return list;
	}
	
	
	
	/**
	 * 获取定制的总页数
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param cityId
	 * @param taskName
	 * @return
	 */
	public int countOrderTask(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName) 
	{
		logger.debug("countOrderTask");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_blacklist_task a left join tab_accounts b on a.acc_oid=b.acc_oid ");
		sql.append("where 1=1");
		if(acc_oid!=1){
			sql.append("b.acc_oid = "+ acc_oid);
		}
		
		if(!StringUtil.IsEmpty(accName) ){
			sql.append(" and b.acc_loginname like '%" + accName + "%'");
		}
		
		if (!StringUtil.IsEmpty(taskName)) {
			sql.append(" and a.task_name='").append(taskName).append("'");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			sql.append(" and a.add_time>=").append(getTime(startTime));
		}
		if (!StringUtil.IsEmpty(endTime)) {
			sql.append(" and a.add_time<=").append(getTime(endTime) + 86399);
		}
		
//		sql.append(" order by a.add_time desc ");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	/**
	 * 获取任务结果
	 * @param taskId 任务id
	 * @param upResult 测速结果条件
	 * @return
	 */
	public List getTaskResult(String taskId, int curPage_splitPage,int num_splitPage) {
		String sql = "select device_serialnumber,pppoe_name,city_id,add_time from tab_blacklist_task_dev where task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));
		List<Map> list = querySP(taskSql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage+1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("cityName",CityDAO.getCityName(String.valueOf(rs.getString("city_id"))));
				try {
					long update_time = StringUtil.getLongValue(rs
							.getString("add_time"));
					DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
					map.put("add_time", "1970-01-01 08:00:00".equals(dt.getLongDate())?"- -":dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("add_time", "- -");
				} catch (Exception e) {
					map.put("add_time", "- -");
				}
				map.put("deviceSerialNumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("pppoe_name", rs.getString("pppoe_name"));
				return map;
			}
		});
		return list;
	}
	
	
	/**
	 * 分页方法
	 * @param sql 原始sql
	 * @param startRow 其实行
	 * @param rowsCount 查询行数
	 * @param rowMapper 结果处理回调对象
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map> thisQuerySP(String sql, int startRow, int rowsCount, RowMapper rowMapper)
			throws DataAccessException
	{
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		int inOfFrom = sql.indexOf(" from");
		sql = "select * from ("+sql.substring(0,inOfFrom)+",rownum as limit "+sql.substring(inOfFrom)+" ) where limit>"+(startRow-1)+" and limit<"+(startRow+rowsCount);
		logger.info("{}",sql);
		return (List) jt.query(sql,rowMapper);
	}
	
	/**
	 * 分页方法
	 * @param sql 原始sql
	 * @param startRow 其实行
	 * @param rowsCount 查询行数
	 * @return
	 */
	public String thisQuerySP(String sql, int startRow, int rowsCount)
	{
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		int inOfFrom = sql.indexOf(" from");
		sql = "select * from ("+sql.substring(0,inOfFrom)+",rownum as limit "+sql.substring(inOfFrom)+" ) where limit>"+(startRow-1)+" and limit<"+(startRow+rowsCount);
		logger.info("{}",sql);
		return sql;
	}
	
	
	/**
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param taskId
	 * @param upResult 
	 * @return
	 */
	public int countTaskResult(int curPage_splitPage, int num_splitPage,String taskId) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) from tab_blacklist_task_dev where task_id=? ");
		}else{
			psql.append("select count(1) from tab_blacklist_task_dev where task_id=? ");
		}
		psql.setLong(1, Long.parseLong(taskId));
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	

	private long getTime(String date) {
		DateTimeUtil dt = new DateTimeUtil(date);
		return dt.getLongTime();
	}

	
		
	/**
	 * 获取版本
	 * @return 版本map
	 */
	private  HashMap<String,String>  getDeviceType()
	{
		logger.debug("getDeviceType");
		 deviceTypeMap = new HashMap<String,String>();
		PrepareSQL pSQL = new PrepareSQL("select a.devicetype_id,a.softwareversion from tab_devicetype_info a  ");
		
		
		List<HashMap<String,String>> deviceTypelList = DBOperation.getRecords(pSQL.getSQL());
		for (HashMap<String,String> map:deviceTypelList)
		{
			deviceTypeMap.put(map.get("devicetype_id"), map.get("softwareversion"));

		}
		return  deviceTypeMap;
	}

	/**
	 * 获取错误码
	 * @return
	 */
	public  HashMap<String,String>  getFaultCode() {
		logger.debug("getFaultCode()");
		faultCodeMap = new HashMap<String,String>();
		String sql = "select fault_code,fault_desc  from tab_cpe_faultcode";

		PrepareSQL psql = new PrepareSQL(sql);
		List<HashMap<String,String>> faultCodeList = DBOperation.getRecords(psql.getSQL());
		for (HashMap<String,String> map : faultCodeList)
		{
			faultCodeMap.put(map.get("fault_code"), map.get("fault_desc"));
		}
		
		//业务错误码（cpe错误码表不包含的）
		faultCodeMap.put("-100", "桥接设备未匹配到带宽，无法测速");
		faultCodeMap.put("-101", "没有获取到WAN通道信息");
		faultCodeMap.put("-102", "设备没有上网通道");
		faultCodeMap.put("-103", "设备在测速黑名单中");
		return  faultCodeMap;
	}
	
	
	/**
	 * 失效操作
	 * @param taskId 任务id
	 * @return 结果 1成功 0失败
	 */
	public String doDisable(String taskId) {
		List<String> sqllist = new ArrayList<String>();
		PrepareSQL taskSql = new PrepareSQL("update tab_blacklist_task set task_status =? where task_id=?");
		taskSql.setInt(1, -1);
		taskSql.setLong(2,  StringUtil.getLongValue(taskId));
		
		int res = DBOperation.executeUpdate(taskSql.getSQL());
		logger.warn("失效操作返回结果res = "+res);
		if (res ==1) {
			logger.debug("失效操作返：  成功");
			return "1";
		} else {
			logger.debug("失效操作返：  失败");
			return "0";
		}
	}
	
	
	/**
	 * 更新描述 
	 * @param taskId 任务id
	 * @param desc 描述
	 * @return 结果 1成功 0失败
	 */
	public String commitDesc(String taskId,String desc) {
		List<String> sqllist = new ArrayList<String>();
		PrepareSQL taskSql = new PrepareSQL("update tab_blacklist_task set task_desc =? where task_id=?");
		taskSql.setString(1, desc);
		taskSql.setLong(2,  StringUtil.getLongValue(taskId));
		
		int res = DBOperation.executeUpdate(taskSql.getSQL());
		logger.warn("提交修改描述返回结果res = "+res);
		if (res ==1) {
			logger.debug("提交修改描述：  成功");
			return "1";
		} else {
			logger.debug("提交修改描述：  失败");
			return "0";
		}
	}
	
	
	/**
	 * 删除操作
	 * @param taskId 任务id
	 * @return 结果 1成功 0失败
	 */
	public String doDelete(String taskId) {
		//删除黑名单表
		PrepareSQL blackSql = new PrepareSQL("delete from tab_speed_blacklist where device_id in(select device_id from tab_blacklist_task_dev where task_id = ?)");
		blackSql.setLong(1,StringUtil.getLongValue(taskId));
		//删除黑名单任务表
		PrepareSQL taskSql = new PrepareSQL("delete from tab_blacklist_task where task_id=?");
		taskSql.setLong(1,  StringUtil.getLongValue(taskId));
		//删除黑名单任务详细表
		PrepareSQL taskdevSql = new PrepareSQL("delete from tab_blacklist_task_dev where task_id=?");
		taskdevSql.setLong(1,  StringUtil.getLongValue(taskId));
		
		ArrayList<String> sqlList = new ArrayList<String>();
		sqlList.add(blackSql.getSQL());
		sqlList.add(taskSql.getSQL());
		sqlList.add(taskdevSql.getSQL());
		int res = DBOperation.executeUpdate(sqlList);
		logger.warn("删除操作返回结果res = "+res);
		if (res ==1) {
			logger.debug("任务定制：  成功");
			return "1";
		} else {
			logger.debug("任务定制：  失败");
			return "0";
		}
	}


	/**
	 * 查询黑名单任务操作设备数 
	 * @param taskId
	 * @return
	 */
	public Map<String, String> getCountResult(String taskId) 
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("succ", "0");
		map.put("fail", "0");
		map.put("undo", "0");
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			sql.append("select b.status,count(*) as num ");
		}else{
			sql.append("select b.status,count(1) as num ");
		}
		sql.append("from tab_blacklist_task a,tab_blacklist_task_dev b, tab_gw_device c ");
		sql.append("where a.task_id=b.task_id and b.device_id=c.device_id and b.task_id=? ");
		sql.append("group by b.status ");
		sql.setLong(1, StringUtil.getLongValue(taskId));
		List list = jt.queryForList(sql.getSQL());
		if (false == list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map) list.get(i);
				String result = StringUtil.getStringValue(rmap.get("status"));
				if ("1".equals(result)) {
					map.put("succ",StringUtil.getStringValue(rmap.get("num")));
				}
				if ("0".equals(result)) {
					map.put("undo",StringUtil.getStringValue(rmap.get("num")));
				}
			}
		}
		logger.warn(map.toString());
		return map;
	}

	/**
	 * @param taskId
	 * @return
	 */
	public int countTaskResult(String taskId)
	{
		logger.debug("countTaskResult");
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_blacklist_task a,tab_blacklist_task_dev b,tab_gw_device c ");
		psql.append("where a.task_id=b.task_id and b.device_id=c.device_id ");
		if (null != taskId && !"".equals(taskId))
		{
			psql.append(" and a.task_id="+taskId+"");
		}
		
		int total = jt.queryForInt(psql.getSQL());
		return total;
	}
}


