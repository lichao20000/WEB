package com.linkage.module.ids.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;
import com.linkage.system.utils.StringUtils;

public class TestSpeedTaskDAO  extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(TestSpeedTaskDAO.class);
	
	private  HashMap<String,String> deviceTypeMap = null;
	
	private  HashMap<String,String> faultCodeMap = null;
	
	public int saveTestSpeedTask(long taskId, String taskName,
			long acc_oid, long addTime, String accountFilePath, String acc_cityId) 
	{
		List<String> sqllist = new ArrayList<String>();
		
		PrepareSQL sql1 = new PrepareSQL("insert into test_speed_task(");
		sql1.append("task_id,task_name,acc_oid,add_time,file_path,test_status,acc_cityId)");
		sql1.append(" values(?,?,?,?,?,0,?)");
		sql1.setLong(1, taskId);
		sql1.setString(2, taskName);
		sql1.setLong(3, acc_oid);
		sql1.setLong(4, addTime);
		sql1.setString(5, accountFilePath); 
		sql1.setString(6, acc_cityId); 
		sqllist.add(sql1.getSQL());

		int[] ier = null;
		try {
		    ier = doBatch(sqllist);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("任务定制：  失败");
			return 0;
		}
		
		if (ier != null && ier.length > 0) {
			logger.warn("任务定制：  成功");
			return 1;
		} else {
			logger.warn("任务定制：  失败");
			return 0;
		}
	}
	
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
	public List getOrderTaskList(int curPage_splitPage, int num_splitPage,
			String startTime,String endTime,String cityId,String taskName,long acc_oid,String accName) 
	{
//		String rolesql = "select role_id from tab_acc_role where acc_oid = " + acc_oid;

//		PrepareSQL psqls = new PrepareSQL(rolesql);
//		Map<String,String> roleMap = DBOperation.getRecord(psqls.getSQL());
//		String role_id = StringUtil.getStringValue(roleMap, "role_id");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_id,a.task_name,a.add_time,a.test_status,a.file_path,a.acc_oid,c.acc_loginname ");
		sql.append("from test_speed_task a,tab_accounts c where a.acc_oid=c.acc_oid ");
//		if(!role_id.equals("6")){
//			sql.append(" and a.acc_oid = " + acc_oid);
//		}else if(role_id.equals("6")){// && accName != null && !"".equals(accName)
//		}
		
		if(accName == null){
			accName = "";
		}
		sql.append(" and c.acc_loginname like '%" + accName + "%'");
		
		if (!StringUtil.IsEmpty(taskName)) {
			sql.append(" and a.task_name='").append(taskName).append("'");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			sql.append(" and a.add_time>=").append(getTime(startTime));
		}
		if (!StringUtil.IsEmpty(endTime)) {
			sql.append(" and a.add_time<=").append(getTime(endTime) + 86399);
		}
		// 属地
		if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId)) {
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.acc_cityid in (" + StringUtils.weave(list) + ")");
//			System.out.println("StringUtils.weave(list) ============= "+StringUtils.weave(list));
			list = null;
//			sql.append(" and a.acc_oid = " + acc_oid);
		}
		sql.append(" order by a.add_time desc");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		// vendorMap = GwVendorModelVersionDAO.getVendorMap();
		
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage+1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("acc_loginname", rs.getString("acc_loginname"));
				map.put("task_name",
						StringUtil.getStringValue(rs.getString("task_name")));
				try {
					long add_time = StringUtil.getLongValue(rs
							.getString("add_time"));
					DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
					map.put("add_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("add_time", "");
				} catch (Exception e) {
					map.put("add_time", "");
				}
				
				map.put("task_id", rs.getString("task_id"));
				map.put("test_status", rs.getString("test_status"));
				map.put("file_path", rs.getString("file_path"));
				return map;
			}
		});
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
//		String rolesql = "select role_id from tab_acc_role where acc_oid = " + acc_oid;
//		PrepareSQL psqls = new PrepareSQL(rolesql);
//		Map<String,String> roleMap = DBOperation.getRecord(psqls.getSQL());
//		String role_id = StringUtil.getStringValue(roleMap, "role_id");
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from test_speed_task a,tab_accounts c where a.acc_oid=c.acc_oid");
		
//		if(!role_id.equals("6")){
//			sql.append(" and a.acc_oid = " + acc_oid);
//		}else if(role_id.equals("6")){// && accName != null && !"".equals(accName)
//			
//		}		
		if(accName == null){
			accName = "";
		}
		sql.append(" and c.acc_loginname like '%" + accName + "%'");
		
		if (!StringUtil.IsEmpty(taskName)) {
			sql.append(" and a.task_name='").append(taskName).append("'");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			sql.append(" and a.add_time>=").append(getTime(startTime));
		}
		if (!StringUtil.IsEmpty(endTime)) {
			sql.append(" and a.add_time<=").append(getTime(endTime) + 86399);
		}
		if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId)) {

			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.acc_cityid in (" + StringUtils.weave(list) + ")");
			list = null;
			
//			sql.append(" and a.acc_oid = " + acc_oid);
		}
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
	 * @param taskId
	 * @return
	 */
	public List getTaskResult(String taskId,int curPage_splitPage,int num_splitPage) 
	{
		logger.debug("getTaskResult");
		deviceTypeMap =  this.getDeviceType();
		faultCodeMap = this.getFaultCode();
		//TODO wait
		String sql = "select a.task_name,b.device_id,b.device_serialnumber, b.create_time,b.update_time, " +
				" b.process_status,b.process_times,b.serv_name, c.city_id,c.vendor_id, c.devicetype_id,c.device_model_id " +
				" from test_speed_task a, test_speed_dev b, tab_gw_device c where a.task_id=b.task_id and a.task_id=? " +
				" and c.device_id=b.device_id order by b.update_time desc ";
		
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));
		List<Map> list = querySP(taskSql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage+1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("cityName",CityDAO.getCityName(String.valueOf(rs.getString("city_id"))));
				try {
					long update_time = StringUtil.getLongValue(rs
							.getString("update_time"));
					DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
					map.put("update_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("update_time", "");
				} catch (Exception e) {
					map.put("update_time", "");
				}
				map.put("vendorName", DeviceTypeUtil.vendorMap.get(StringUtil.getStringValue(rs.getString("vendor_id"))));
				map.put("deviceTypeName", deviceTypeMap.get(StringUtil.getStringValue(rs.getString("DEVICETYPE_ID"))));
				map.put("deviceModel", DeviceTypeUtil.deviceModelMap.get(StringUtil.getStringValue(rs.getString("device_model_id"))));
				map.put("deviceSerialNumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("servAccount", StringUtil.getStringValue(rs.getString("SERV_NAME")));
				String process_status = StringUtil.getStringValue(rs.getString("process_status"));
//				if("1".equals(process_status)){
//					map.put("result", "成功");
//				}else if("-1".equals(process_status)){
//					map.put("result", "失败");
//				}else if("0".equals(process_status)){
//					map.put("result", "未处理");
//				}else {
//					map.put("result", "未知情况");
//				}
				if("0".equals(process_status)){
					map.put("result", "未处理");
				}else if (faultCodeMap.get(process_status) == null) {
					map.put("result", "未知错误");
				} else {
					if("-3".equals(process_status)){
						map.put("result", "未获取到测速账号");
					}else{
						map.put("result", faultCodeMap.get(process_status));
					}
				}
				
				return map;
			}
		});
		
		return list;
	}
	
	/**
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param taskId
	 * @return
	 */
	public int countTaskResult(int curPage_splitPage, int num_splitPage,String taskId) 
	{
		logger.debug("countTaskResult");
		StringBuffer sql = new StringBuffer();
		//TODO wait
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from test_speed_task a,test_speed_dev b, tab_gw_device c " +
				" where a.TASK_ID=b.TASK_ID and b.device_id=c.device_id ");
		
		if (null != taskId && !"".equals(taskId)) {
			sql.append(" and a.task_id=").append(taskId).append("");
		}
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
	 * 删除任务
	 */	
	public String doDelete(String taskId) 
	{
		List<String> sqllist = new ArrayList<String>();
		PrepareSQL taskSql = new PrepareSQL("delete from test_speed_task where task_id=?");
		taskSql.setLong(1, Long.parseLong(taskId));
		sqllist.add(taskSql.getSQL());
		
		taskSql = new PrepareSQL("delete from test_speed_dev where task_id=?");
		taskSql.setLong(1, Long.parseLong(taskId));
		sqllist.add(taskSql.getSQL());
		
		int[] ier = doBatch(sqllist);
		if (ier != null && ier.length > 0) {
			logger.debug("任务定制：  成功");
			return "1";
		} else {
			logger.debug("任务定制：  失败");
			return "0";
		}
	}
	
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

	public  HashMap<String,String>  getFaultCode() 
	{
		logger.debug("getFaultCode()");
		faultCodeMap = new HashMap<String,String>();
		String sql = "select fault_code,fault_desc from tab_cpe_faultcode";

		PrepareSQL psql = new PrepareSQL(sql);
		List<HashMap<String,String>> faultCodeList = DBOperation.getRecords(psql.getSQL());
		for (HashMap<String,String> map : faultCodeList)
		{
			faultCodeMap.put(map.get("fault_code"), map.get("fault_desc"));
		}
		return  faultCodeMap;
	}
	
	private long getTime(String date) {
		DateTimeUtil dt = new DateTimeUtil(date);
		return dt.getLongTime();
	}
	
}
