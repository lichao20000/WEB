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
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

@SuppressWarnings("rawtypes")
public class BatchHttpTestManaDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(BatchHttpTestManaDAO.class);
	private  HashMap<String,String> deviceTypeMap = null;
	private  HashMap<String,String> faultCodeMap = null;


	/**
	 * 查看定制详细信息
	 */
	public List<HashMap<String,String>> getOrderTaskList(int curPage_splitPage, 
			int num_splitPage,String startTime, String endTime, String cityId, 
			String taskName,long acc_oid,String accName,String type) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select a.add_time,a.task_status,a.task_desc,a.task_name,a.task_desc_short,a.task_id,b.acc_loginname ");
		}else{
			sql.append("select a.*,b.acc_loginname ");
		}
		
		sql.append("from tab_batchhttp_task a left join tab_accounts b on a.acc_oid=b.acc_oid ");
		// 排除掉逻辑删除的任务
		sql.append("where task_status != 9 ");
		
		if(acc_oid!=1){
			sql.append(" and b.acc_oid="+ acc_oid);
		}
		if(!StringUtil.IsEmpty(accName) ){
			sql.append(" and b.acc_loginname like '%" + accName + "%'");
		}
		if(!StringUtil.IsEmpty(type)  && !"undefined".equalsIgnoreCase(type)){
			sql.append(" and a.type='"+type+"'");
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
		
		List<HashMap<String,String>> list = DBOperation.getRecords(thisQuerySP(sql.toString(),
											(curPage_splitPage - 1) * num_splitPage+1, num_splitPage));
		return list;
	}
	
	/**
	 * 获取定制的总页数
	 */
	public int countOrderTask(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, 
			String cityId, String taskName,long acc_oid,String accName,String type)
	{
		logger.debug("countOrderTask");
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_batchhttp_task a left join tab_accounts b on a.acc_oid=b.acc_oid ");
		// 排除掉逻辑删除的任务
		sql.append("where task_status != 9 ");
		
		if(acc_oid!=1){
			sql.append(" and b.acc_oid="+ acc_oid);
		}
		if(!StringUtil.IsEmpty(accName) ){
			sql.append(" and b.acc_loginname like '%" + accName + "%'");
		}
		if(!StringUtil.IsEmpty(type)  && !"undefined".equalsIgnoreCase(type)){
			sql.append(" and a.type='"+type+"'");
		}
		if (!StringUtil.IsEmpty(taskName)) {
			sql.append(" and a.task_name='"+taskName+"'");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			sql.append(" and a.add_time>="+getTime(startTime));
		}
		if (!StringUtil.IsEmpty(endTime)) {
			sql.append(" and a.add_time<="+getTime(endTime) + 86399);
		}
		
		int total = jt.queryForInt(sql.getSQL());
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
	 */
	public List getTaskResult(String taskId,final String upResult,
			int curPage_splitPage,int num_splitPage) 
	{
		logger.debug("getTaskResult");
		deviceTypeMap =  this.getDeviceType();
		faultCodeMap = this.getFaultCode();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,b.pppoe_name,b.device_id,b.device_serialnumber,b.add_time,");
		sql.append("b.update_time,b.status,c.city_id,c.vendor_id,c.devicetype_id,c.device_model_id ");
		
		if(LipossGlobals.inArea(Global.HBLT))
		{
		/**	if(DBUtil.GetDB()==3){
				//TODO wait
			}else{
				
			} */
			sql.append(",d.rate ");
			sql.append("from tab_batchhttp_task a,tab_batchhttp_task_dev b,tab_gw_device c,tab_speed_dev_rate d ");
			sql.append("where (d.pppoe_name=b.pppoe_name or d.device_serialnumber=b.device_serialnumber) ");
			sql.append("and a.task_id=b.task_id and a.task_id=? and c.device_id=b.device_id ");
			
			if("0".equals(upResult) || "1".equals(upResult)){
				sql.append("and status="+upResult+" ");
			}else if("2".equals(upResult)){
				sql.append("and status!=0 and status!=1 ");	
			}
			sql.append("order by b.update_time desc ");
		}
		else if(LipossGlobals.inArea(Global.JLLT))
		{
			if("3".equals(upResult))
			{
			/**	if(DBUtil.GetDB()==3){
					//TODO wait
				}else{
					
				} */
				sql=null;
				sql=new StringBuffer();
				sql.append("select b.pppoe_name,c.vendor_id,c.devicetype_id,c.device_model_id,c.device_serialnumber,");
				sql.append("d.city_id,d.speed_dev,d.average_speed,d.is_sure,d.start_time,d.end_time,e.rate ");
				sql.append("from tab_gw_device c,tab_batch_http_speed d,tab_batchhttp_task_dev b,tab_hgwcustomer f ");
				sql.append("left join tab_speed_dev_rate e on e.user_id = f.user_id ");
				sql.append("where b.task_id=? and b.device_id=c.device_id and b.pppoe_name=d.pppoe_name  and c.device_id = f.device_id ");
				sql.append("and d.start_time>b.add_time and d.start_time<b.add_time+86400 ");
			}else{
			/**	if(DBUtil.GetDB()==3){
					//TODO wait
				}else{
					
				} */
				sql.append(",d.rate from tab_batchhttp_task a,tab_gw_device c,tab_batchhttp_task_dev b,tab_hgwcustomer e ");
				sql.append("left join tab_speed_dev_rate d on  e.user_id = d.user_id ");
				sql.append("where a.task_id=b.task_id and a.task_id=? and c.device_id=b.device_id  and c.device_id = e.device_id ");
				
				if("0".equals(upResult) || "1".equals(upResult)){
					sql.append("and status="+upResult+" ");
				}else if("2".equals(upResult)){
					sql.append("and status!=0 and status!=1 "); 
				}
			}
			sql.append("order by b.update_time desc ");
		}
		else
		{
		/**	if(DBUtil.GetDB()==3){
				//TODO wait
			}else{
				
			} */
			sql.append("from tab_batchhttp_task a,tab_batchhttp_task_dev b,tab_gw_device c ");
			sql.append("where a.task_id=b.task_id and a.task_id=? and c.device_id=b.device_id ");
			if("0".equals(upResult) || "1".equals(upResult)){
				sql.append("and status="+upResult+" ");
			}else if("2".equals(upResult)){
				sql.append("and status!=0 and status!=1 "); 
			}
			sql.append("order by b.update_time desc ");
		}
		
		logger.warn("upResult="+upResult);
		PrepareSQL taskSql = new PrepareSQL(sql.toString());
		taskSql.setLong(1, Long.parseLong(taskId));
		List<Map> list = querySP(taskSql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage+1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException 
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("cityName",CityDAO.getCityName(String.valueOf(rs.getString("city_id"))));
				map.put("vendorName", DeviceTypeUtil.vendorMap.get(StringUtil.getStringValue(rs.getString("vendor_id"))));
				map.put("deviceTypeName", deviceTypeMap.get(StringUtil.getStringValue(rs.getString("DEVICETYPE_ID"))));
				map.put("deviceModel", DeviceTypeUtil.deviceModelMap.get(StringUtil.getStringValue(rs.getString("device_model_id"))));
				map.put("deviceSerialNumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
				String pppoe_name = "null".equals(rs.getString("pppoe_name"))?"":rs.getString("pppoe_name");
				map.put("pppoe_name", pppoe_name);
				
				if(LipossGlobals.inArea(Global.JLLT))
				{
					long rate=StringUtil.getLongValue(rs.getString("rate"))/1024;
					map.put("rate",rate==0?"-":rate+"M");
					
					if("3".equals(upResult))
					{
						map.put("is_sure","1".equals(rs.getString("is_sure"))?"是":"否");
						map.put("speed_dev",rs.getString("speed_dev"));
						map.put("average_speed",rs.getString("average_speed"));
						map.put("start_time",getTime(rs.getString("start_time")));
						map.put("end_time",getTime(rs.getString("end_time")));
					}
					else
					{
						map.put("update_time",getTime(rs.getString("update_time")));
						map.put("result", getResult(StringUtil.getStringValue(rs.getString("status"))));
					}
				}
				else
				{
					if(LipossGlobals.inArea(Global.HBLT)){
						map.put("rate",StringUtil.getStringValue(rs.getString("rate")));
					}
					map.put("update_time",getTime(rs.getString("update_time")));
					map.put("result", getResult(StringUtil.getStringValue(rs.getString("status"))));
				}
				
				return map;
			}
			
			private String getResult(String process_status)
			{
				String res="";
				if("0".equals(process_status)){
					res="未处理";
				}else if("1".equals(process_status)){
					res="成功";
				}else if (faultCodeMap.get(process_status) == null ||"2".equals(process_status)) {
					res="失败";
				}else if(null != faultCodeMap.get(process_status)){
					res=faultCodeMap.get(process_status);
				}
				return res;
			}
			
			private String getTime(String time)
			{
				String str="- -";
				try {
					long update_time = StringUtil.getLongValue(time);
					DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
					str="1970-01-01 08:00:00".equals(dt.getLongDate())?"- -":dt.getLongDate();
				} catch (Exception e) {
					return str;
				}
				
				return str;
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
	 */
	@SuppressWarnings("unchecked")
	public List<Map> thisQuerySP(String sql, int startRow, int rowsCount, RowMapper rowMapper)
			throws DataAccessException
	{
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		int inOfFrom = sql.indexOf(" from");
		sql = "select * from ("+sql.substring(0,inOfFrom)+",rownum as limit "+sql.substring(inOfFrom)
				+" ) where limit>"+(startRow-1)+" and limit<"+(startRow+rowsCount);
		logger.info("{}",sql);
		return (List) jt.query(sql,rowMapper);
	}
	
	/**
	 * 分页方法
	 * @param sql 原始sql
	 * @param startRow 其实行
	 * @param rowsCount 查询行数
	 */
	public String thisQuerySP(String sql, int startRow, int rowsCount)
	{
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sql = "select * from (select a.* ,rownum as limit from ("+sql+" ) a ) where limit>"
				+(startRow-1)+" and limit<"+(startRow+rowsCount);
		logger.info("{}",sql);
		return sql;
	}
	
	public int countTaskResult(int curPage_splitPage, int num_splitPage,String taskId,String upResult)
	{
		logger.debug("countTaskResult");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		if(LipossGlobals.inArea(Global.HBLT))
		{
		/**	if(DBUtil.GetDB()==3){
				//TODO wait
			}else{
				
			} */
			sql.append("from tab_batchhttp_task a,tab_batchhttp_task_dev b,tab_gw_device c,tab_speed_dev_rate d ");
			sql.append("where (d.pppoe_name=b.pppoe_name or d.device_serialnumber=b.device_serialnumber) ");
			sql.append("and a.task_id=b.task_id and a.task_id=? and c.device_id=b.device_id ");
			
			if("0".equals(upResult) || "1".equals(upResult)){
				sql.append("and status="+upResult);
			}else if("2".equals(upResult)){
				sql.append("and status!=0 and status!=1 ");
			}
		}
		else if(LipossGlobals.inArea(Global.JLLT))
		{
		/**	if(DBUtil.GetDB()==3){
				//TODO wait
			}else{
				
			} */
			if("3".equals(upResult))
			{
				sql.append("from tab_batchhttp_task_dev b,tab_gw_device c,tab_batch_http_speed d ");
				sql.append("where b.task_id=? and b.device_id=c.device_id and b.pppoe_name=d.pppoe_name ");
				sql.append("and d.start_time>b.add_time and d.start_time<b.add_time+86400 ");
			}else{
				sql.append("from tab_batchhttp_task a,tab_gw_device c,tab_batchhttp_task_dev b ");
				sql.append("where a.task_id=b.task_id and a.task_id=? and c.device_id=b.device_id ");
				
				if("0".equals(upResult) || "1".equals(upResult)){
					sql.append("and status="+upResult+" ");
				}else if("2".equals(upResult)){
					sql.append("and status!=0 and status!=1 "); 
				}
			}
		}
		else
		{
		/**	if(DBUtil.GetDB()==3){
				//TODO wait
			}else{
				
			} */
			sql.append("from tab_batchhttp_task a,tab_batchhttp_task_dev b,tab_gw_device c ");
			sql.append("where a.task_id=b.task_id and b.device_id=c.device_id ");
			if("0".equals(upResult) || "1".equals(upResult)){
				sql.append(" and b.status=").append(upResult);
			}else if("2".equals(upResult)){
				sql.append(" and b.status!=0 and b.status!=1");
			}
			
			if (!StringUtil.IsEmpty(taskId)) {
				sql.append(" and a.task_id=").append(taskId);
			}
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		if(LipossGlobals.inArea(Global.JLLT) || LipossGlobals.inArea(Global.HBLT)) {
			psql.setLong(1,StringUtil.getLongValue(taskId));
		}
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	private long getTime(String date) 
	{
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
		PrepareSQL pSQL = new PrepareSQL("select devicetype_id,softwareversion from tab_devicetype_info ");
		
		List<HashMap<String,String>> deviceTypelList = DBOperation.getRecords(pSQL.getSQL());
		for (HashMap<String,String> map:deviceTypelList){
			deviceTypeMap.put(map.get("devicetype_id"), map.get("softwareversion"));
		}
		return  deviceTypeMap;
	}

	/**
	 * 获取错误码
	 */
	public  HashMap<String,String>  getFaultCode() 
	{
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
		faultCodeMap.put("-103", " 设备在测速黑名单");
		return  faultCodeMap;
	}
	
	
	/**
	 * 失效操作
	 * @return 结果 1成功 0失败
	 */
	public String doDisable(String taskId) 
	{
		PrepareSQL taskSql = new PrepareSQL("update tab_batchhttp_task set task_status =? where task_id=?");
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
	 * @return 结果 1成功 0失败
	 */
	public String commitDesc(String taskId,String desc) 
	{
		PrepareSQL taskSql = new PrepareSQL("update tab_batchhttp_task set task_desc =? where task_id=?");
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
	 * @return 结果 1成功 0失败
	 */
	public String doDelete(String taskId) 
	{
		// 逻辑删除，状态置为 9
		PrepareSQL taskSql = new PrepareSQL("update tab_batchhttp_task set task_status =? where task_id=?");
		taskSql.setInt(1, 9);
		taskSql.setLong(2,  StringUtil.getLongValue(taskId));

		int res = DBOperation.executeUpdate(taskSql.getSQL());
		logger.warn("删除操作返回结果res = " + res);
		if (res == 1) {
			logger.debug("删除操作返：  成功");
			return "1";
		}
		else {
			logger.debug("删除操作返：  失败");
			return "0";
		}
			
	/*	
		PrepareSQL taskSql = new PrepareSQL("delete from tab_batchhttp_task where task_id=?");
		taskSql.setLong(1,  StringUtil.getLongValue(taskId));
		
		PrepareSQL taskdevSql = new PrepareSQL("delete from tab_batchhttp_task_dev where task_id=?");
		taskdevSql.setLong(1,  StringUtil.getLongValue(taskId));
		
		ArrayList<String> sqlList = new ArrayList<String>();
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
	*/	
	}

	public Map<String, String> getCountResult(String taskId) 
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("succ", "0");// 更新服务器地址修改成功
		map.put("fail", "0");
		map.put("undo", "0");
		
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			sql.append("select b.status,count(*) as num ");
		}else{
			sql.append("select b.status,count(1) as num ");
		}
		
		if(LipossGlobals.inArea(Global.HBLT)){
			sql.append("from tab_batchhttp_task a,tab_batchhttp_task_dev b,tab_gw_device c,tab_speed_dev_rate d ");
			sql.append("where (d.pppoe_name=b.pppoe_name or d.device_serialnumber=b.device_serialnumber) ");
			sql.append("and a.task_id=b.task_id and b.device_id=c.device_id and b.task_id=? group by b.status");
		}else if(LipossGlobals.inArea(Global.JLLT)){
			sql.append("from tab_batchhttp_task a,tab_gw_device c,tab_batchhttp_task_dev b,tab_hgwcustomer e  ");
			sql.append("left join tab_speed_dev_rate d   on e.user_id = d.user_id ");
			sql.append("where a.task_id=b.task_id and b.device_id=c.device_id  and c.device_id = e.device_id and b.task_id=? group by b.status");
		}else{
			sql.append("from tab_batchhttp_task a,tab_batchhttp_task_dev b,tab_gw_device c ");
			sql.append("where a.task_id=b.task_id and b.device_id=c.device_id and b.task_id=? group by b.status");
		}
		
		sql.setLong(1, StringUtil.getLongValue(taskId));
		List list = jt.queryForList(sql.getSQL());
		if (!list.isEmpty()) {
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
		
		if(LipossGlobals.inArea(Global.JLLT))
		{
		/**	if(DBUtil.GetDB()==3){
				//TODO wait
			}else{
				
			} */
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.append("select count(distinct b.device_id) as num ");
			pSQL.append("from tab_batchhttp_task_dev b,tab_gw_device c,tab_batch_http_speed d,tab_hgwcustomer e  ");
			pSQL.append("where b.task_id=? and b.device_id=c.device_id and b.pppoe_name=d.pppoe_name and c.device_id = e.device_id ");
			pSQL.append("and d.start_time>b.add_time and d.start_time<b.add_time+86400 ");
			pSQL.setLong(1, StringUtil.getLongValue(taskId));
			
			map.put("dosucc",StringUtil.getStringValue(jt.queryForMap(pSQL.getSQL()),"num","0"));
		}
		return map;
	}

	/**
	 * 统计总数
	 */
	public int countTaskResult(String taskId) 
	{
		logger.debug("countTaskResult");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_batchhttp_task a,tab_batchhttp_task_dev b, tab_gw_device c " +
				" where a.TASK_ID=b.TASK_ID and b.device_id=c.device_id ");
		
		if (!StringUtil.IsEmpty(taskId)) {
			sql.append(" and a.task_id=").append(taskId).append("");
		}
		
		String sql1 = null;
		if(LipossGlobals.inArea(Global.HBLT)){
			sql1 = "from tab_batchhttp_task a, tab_batchhttp_task_dev b, tab_gw_device c, tab_speed_dev_rate d " +
						" where (d.pppoe_name=b.pppoe_name or d.device_serialnumber=b.device_serialnumber) and " +
						" a.task_id=b.task_id and a.task_id=? and c.device_id=b.device_id ";
		}else if(LipossGlobals.inArea(Global.JLLT)){
			sql1 ="from tab_batchhttp_task a, tab_gw_device c,tab_batchhttp_task_dev b ,tab_hgwcustomer e left join tab_speed_dev_rate d "
					+ "on e.user_id = d.user_id where a.task_id=b.task_id and a.task_id=? and c.device_id=b.device_id  and c.device_id = e.device_id " ;
		}else{
			sql1 = sql.toString();
		}
		
		PrepareSQL psql = new PrepareSQL(sql1);
		if(LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.JLLT)){
			psql.setLong(1, Long.parseLong(taskId));
		}
		
		return jt.queryForInt(psql.getSQL());
	}
}


