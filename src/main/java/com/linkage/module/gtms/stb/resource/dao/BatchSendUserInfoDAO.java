package com.linkage.module.gtms.stb.resource.dao;

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
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;
import com.linkage.system.utils.StringUtils;

public class BatchSendUserInfoDAO extends SuperDAO  {
	private static Logger logger = LoggerFactory
			.getLogger(BatchSendUserInfoDAO.class);

	private  HashMap<String,String> deviceTypeMap = null;

	private  HashMap<String,String> faultCodeMap = null;

	public int saveBatchSendUserInfoTask(long taskId, String taskName,	long acc_oid, long addTime, String accountFilePath,
			String acc_cityId, String configType) {
		List<String> sqllist = new ArrayList<String>();

		PrepareSQL sql1 = new PrepareSQL("insert into stb_tab_batch_task(");
		try {
				sql1.append("task_id,task_name,acc_oid,add_time,update_time,file_path,city_id,config_type)");
				sql1.append(" values(?,?,?,?,?,?,?,?)");
				sql1.setLong(1, taskId);
				sql1.setString(2, taskName);
				sql1.setLong(3, acc_oid);
				sql1.setLong(4, addTime);
				sql1.setLong(5, addTime);
				sql1.setString(6, accountFilePath);
				sql1.setString(7, acc_cityId);
				sql1.setInt(8, Integer.parseInt(configType));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName,String isActive) {
		String rolesql = "select role_id from tab_acc_role where acc_oid = " + acc_oid;

		PrepareSQL psqls = new PrepareSQL(rolesql);
		Map<String,String> roleMap = DBOperation.getRecord(psqls.getSQL());
		String role_id = StringUtil.getStringValue(roleMap, "role_id");
		StringBuffer sql = new StringBuffer();

		sql.append("select a.config_type,a.is_active,a.task_id,a.city_id,a.task_name,a.vendor_id,a.add_time,a.load_status,a.update_time,a.file_path,c.acc_loginname from stb_tab_batch_task a,tab_accounts c where a.acc_oid=c.acc_oid ");

		if(!role_id.equals("6")){
			sql.append(" and a.acc_oid = " + acc_oid);
		}else if(role_id.equals("6")){// && accName != null && !"".equals(accName)

		}
		if(accName == null){
			accName = "";
		}
		sql.append(" and c.acc_loginname like '%" + accName + "%'");

		if (null != taskName && !"".equals(taskName)) {
			sql.append(" and a.task_name='").append(taskName).append("'");
		}
		if (null != startTime && !"".equals(startTime)) {
			sql.append(" and a.add_time>=").append(getTime(startTime));
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append(" and a.add_time<=").append(getTime(endTime) + 86399);
		}

		if ("1".equals(isActive)) {
			sql.append(" and a.is_active=1 ");
		}else if("0".equals(isActive)){
			sql.append(" and a.is_active=0 ");
		}

		// 属地
		if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId)) {
//			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
//			sql.append(" and  a.city_id in (" + StringUtils.weave(list) + ")");
//			list = null;
			sql.append(" and a.acc_oid = " + acc_oid);
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
				map.put("task_id", rs.getString("task_id"));
				map.put("file_path", rs.getString("file_path"));
				if(rs.getInt("is_active")==1){
					map.put("is_active", "有效");
				}else{
					map.put("is_active", "失效");
				}

				if(rs.getInt("config_type")==1){
					map.put("config_type", "接入账号及密码、业务账号及密码");
				}else if(rs.getInt("config_type")==0){
					map.put("config_type", "接入账号及密码");
				}else{
					map.put("config_type", "");
				}

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
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName,String isActive) {
		logger.debug("countOrderTask");
		String rolesql = "select role_id from tab_acc_role where acc_oid = " + acc_oid;

		PrepareSQL psqls = new PrepareSQL(rolesql);
		Map<String,String> roleMap = DBOperation.getRecord(psqls.getSQL());
		String role_id = StringUtil.getStringValue(roleMap, "role_id");

		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_tab_batch_task  a,tab_accounts c where a.acc_oid=c.acc_oid ");

		if(!role_id.equals("6")){
			sql.append(" and a.acc_oid = " + acc_oid);
		}else if(role_id.equals("6")){// && accName != null && !"".equals(accName)

		}
		if(accName == null){
			accName = "";
		}
		sql.append(" and c.acc_loginname like '%" + accName + "%'");

		if (null != taskName && !"".equals(taskName)) {
			sql.append(" and a.task_name='").append(taskName).append("'");
		}
		if (null != startTime && !"".equals(startTime)) {
			sql.append(" and a.add_time>=").append(getTime(startTime));
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append(" and a.add_time<=").append(getTime(endTime) + 86399);
		}

		if ("1".equals(isActive)) {
			sql.append(" and a.is_active=1 ");
		}else if("0".equals(isActive)){
			sql.append(" and a.is_active=0 ");
		}

		if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId)) {

//			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
//			sql.append(" and  a.city_id in (" + StringUtils.weave(list) + ")");
//			list = null;

			sql.append(" and a.acc_oid = " + acc_oid);
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
	public List getTaskResult(String taskId,int curPage_splitPage,int num_splitPage) {
		logger.debug("getTaskResult");
//		String sql = "select a.status,a.serv_account,a.checkRes,b.city_id,b.vendor_id,b.device_model_id,b.devicetype_id," +
//				" b.device_serialnumber,b.loopback_ip,b.cpe_mac,b.serv_account from stb_tab_batch_account  a left join " +
//				" stb_tab_gw_device b on a.serv_account=b.serv_account where and a.task_id=?";

		String sql = "select a.status,a.serv_account,a.checkRes from stb_tab_batch_account a where a.task_id=?";

		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setString(1, taskId);

		List<Map> list = querySP(taskSql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage+1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				int status = rs.getInt("status");

				if (status == 1) {
					map.put("status", "下发成功");
					map.put("checkRes", "");
				} else if (status == -1) {
					map.put("status", "下发失败");
					map.put("checkRes", StringUtil.getStringValue(rs.getString("checkRes")));
				} else if (status == -2) {
					map.put("status", "数据有误,不进行下发");
					map.put("checkRes", StringUtil.getStringValue(rs.getString("checkRes")));
				} else {
					map.put("status", "尚未进行下发");
					map.put("checkRes", "");
				}

				map.put("serv_account", StringUtil.getStringValue(rs.getString("serv_account")));

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
	public int countTaskResult(int curPage_splitPage, int num_splitPage,String taskId) {
		logger.debug("countTaskResult");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_tab_batch_account a where a.task_id='"+taskId+"'");
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
	public String doDelete(String taskId) {
		List<String> sqllist = new ArrayList<String>();
		PrepareSQL taskSql = new PrepareSQL("delete from stb_tab_batch_task where task_id=?");
		taskSql.setLong(1, Long.parseLong(taskId));

		sqllist.add(taskSql.getSQL());

		taskSql = new PrepareSQL("delete from stb_tab_batch_account where task_id=?");

		taskSql.setString(1, taskId);

		sqllist.add(taskSql.getSQL());
		int[] ier = doBatch(sqllist);
		if (ier != null && ier.length > 0) {
			logger.debug("删除任务：  成功");
			return "1";
		} else {
			logger.debug("删除任务：  失败");
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
		return  faultCodeMap;
	}

	private long getTime(String date) {
		DateTimeUtil dt = new DateTimeUtil(date);
		return dt.getLongTime();

	}

	/**
	 * 设置任务失效
	 */
	public String beNotActived(String taskId) {
		List<String> sqllist = new ArrayList<String>();
		PrepareSQL taskSql = new PrepareSQL("update stb_tab_batch_task set is_active=0 where task_id=?");
		taskSql.setLong(1, Long.parseLong(taskId));
		sqllist.add(taskSql.getSQL());

		int[] ier = doBatch(sqllist);
		if (ier != null && ier.length > 0) {
			logger.debug("设置任务失效：  成功");
			return "1";
		} else {
			logger.debug("设置任务失效：  失败");
			return "0";
		}
	}

	/**
	 * @param taskId
	 * @return
	 */
	public Map<String, String> getTaskDetail(String taskId) {
		String sql = " select a.add_time, a.update_time, a.is_active, a.config_type, b.acc_loginname from stb_tab_batch_task a,tab_accounts b where task_id=? and a.acc_oid=b.acc_oid ";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, Long.parseLong(taskId));
		HashMap<String,String> resultMap = (HashMap<String, String>) DBOperation.getRecord(psql.getSQL());
		if(null==resultMap || resultMap.size()==0){
			return null;
		}

		try {
			long add_time = StringUtil.getLongValue(resultMap.get("add_time"));
			DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
			resultMap.put("add_time", dt.getLongDate());
		} catch (NumberFormatException e) {
			resultMap.put("add_time", "");
		} catch (Exception e) {
			resultMap.put("add_time", "");
		}

		try {
			long update_time = StringUtil.getLongValue(resultMap.get("update_time"));
			DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
			resultMap.put("update_time", dt.getLongDate());
		} catch (NumberFormatException e) {
			resultMap.put("update_time", "");
		} catch (Exception e) {
			resultMap.put("update_time", "");
		}

		if("1".equals(resultMap.get("is_active"))){
			resultMap.put("is_active", "有效");
		}else{
			resultMap.put("is_active", "失效");
		}

		if("1".equals(resultMap.get("config_type"))){
			resultMap.put("config_type", "接入账号及密码、业务账号及密码");
		}else if("0".equals(resultMap.get("config_type"))){
			resultMap.put("config_type", "接入账号及密码");
		}else{
			resultMap.put("config_type", "");
		}
		return resultMap;
	}
}
