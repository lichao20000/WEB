package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.system.utils.StringUtils;
/**
 *
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-2-26
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class OpenDeviceShowPictureNewDAO extends  SuperDAO
{
	private static Logger logger = LoggerFactory
			.getLogger(OpenDeviceShowPictureNewDAO.class);
	private  HashMap<String,String> deviceTypeMap = null;
	private  HashMap<String,String> faultCodeMap = null;
	public List getrolename(List list)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.acc_oid from tab_accounts a,tab_persons b where a.acc_oid=b.per_acc_oid ");
		sql.append(" and b.per_city in( "+StringUtils.weave(list)+")");
		PrepareSQL psqls = new PrepareSQL(sql.toString());
		return jt.queryForList(psqls.getSQL());
	}
	public List getOrderTaskList(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,String acc_loginname) {
		StringBuffer sql1 = new StringBuffer();
		Map<String,String> roleMap=new HashMap<String, String>();
		String acc_oid="";
		if(Global.CQDX.equals(Global.instAreaShortName)
				|| Global.SXLT.equals(Global.instAreaShortName))
		{
			UserRes curUser = WebUtil.getCurrentUser();
			acc_oid = String.valueOf(curUser.getUser().getId());
		}else{
		if(!StringUtil.IsEmpty(acc_loginname)&&!acc_loginname.equals("null"))
		{
			sql1.append("select acc_oid from tab_accounts where acc_loginname='"+acc_loginname+"'");
			PrepareSQL psqls = new PrepareSQL(sql1.toString());
			roleMap=DBOperation.getRecord(psqls.getSQL());
			acc_oid=StringUtil.getStringValue(roleMap, "acc_oid");
		}
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_id,a.task_name,a.acc_oid,a.add_time,a.update_time,b.acc_loginname,a.status ");
		sql.append("from stb_tab_pic_task a,tab_accounts b ");
		sql.append("where a.acc_oid=b.acc_oid ");
		if(Global.JLDX.equals(Global.instAreaShortName)){
			sql.append("and a.status in(1,2,3) ");
		}else{
			sql.append("and a.status in(1,2) ");
		}

		if(!StringUtil.IsEmpty(cityId)&& !"-1".equals(cityId)&& !"00".equals(cityId))
		{
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (" + StringUtils.weave(list) + ")");
			list = null;
		}
		if (null != startTime && !"".equals(startTime)) {
			sql.append(" and  a.add_time>=").append(getTime(startTime));
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append(" and  a.add_time<=").append(getTime(endTime));
		}
		if (null != taskName && !"".equals(taskName)) {
			sql.append(" and  a.task_name='").append(taskName).append("'");
		}
		if(!StringUtil.IsEmpty(acc_oid))
		{
			sql.append(" and  a.acc_oid="+acc_oid+"");
		}
		sql.append(" order by  a.add_time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage+1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("acc_loginname", rs.getString("acc_loginname"));
				map.put("task_name",
						StringUtil.getStringValue(rs.getString("task_name")));
				if(StringUtil.IsEmpty(rs.getString("add_time")))
				{
					map.put("add_time", "");
				}else
				{
					long add_time = StringUtil.getLongValue(rs
							.getString("add_time"));
					DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
					map.put("add_time", dt.getLongDate());
				}
				if(StringUtil.IsEmpty(rs.getString("update_time")))
				{
					map.put("update_time", "");
				}else
				{
					long update_time = StringUtil.getLongValue(rs
							.getString("update_time"));
					DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
					map.put("update_time", dt.getLongDate());
				}
				map.put("task_id", rs.getString("task_id"));
				map.put("status", rs.getString("status"));
				return map;
			}
		});
		return list;
	}
	public List getOrderTaskList1(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,String acc_loginname,List accoidList) {
		StringBuffer sql1 = new StringBuffer();
		Map<String,String> roleMap=new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_id,a.task_name,a.acc_oid,a.add_time,a.update_time,b.acc_loginname,a.status from stb_tab_pic_task a,tab_accounts b where a.acc_oid=b.acc_oid and a.status in(1,2) ");
		if(!StringUtil.IsEmpty(cityId)&& !"-1".equals(cityId)&& !"00".equals(cityId))
		{
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (" + StringUtils.weave(list) + ")");
			list = null;
		}
		if (null != startTime && !"".equals(startTime)) {
			sql.append(" and  a.add_time>=").append(getTime(startTime));
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append(" and  a.add_time<=").append(getTime(endTime));
		}
		if (null != taskName && !"".equals(taskName)) {
			sql.append(" and  a.task_name='").append(taskName).append("'");
		}
		sql.append(" and  a.acc_oid in("+StringUtils.weave(accoidList)+")");
		sql.append(" order by  a.add_time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage+1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("acc_loginname", rs.getString("acc_loginname"));
				map.put("task_name",
						StringUtil.getStringValue(rs.getString("task_name")));
				if(StringUtil.IsEmpty(rs.getString("add_time")))
				{
					map.put("add_time", "");
				}else
				{
					long add_time = StringUtil.getLongValue(rs
							.getString("add_time"));
					DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
					map.put("add_time", dt.getLongDate());
				}
				if(StringUtil.IsEmpty(rs.getString("update_time")))
				{
					map.put("update_time", "");
				}else
				{
					long update_time = StringUtil.getLongValue(rs
							.getString("update_time"));
					DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
					map.put("update_time", dt.getLongDate());
				}
				map.put("task_id", rs.getString("task_id"));
				map.put("status", rs.getString("status"));
				return map;
			}
		});
		return list;
	}
	public int countOrderTask(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,String acc_loginname)
	{
		StringBuffer sql1 = new StringBuffer();
		Map<String,String> roleMap=new HashMap<String, String>();
		String acc_oid="";
		if(Global.CQDX.equals(Global.instAreaShortName)
				|| Global.SXLT.equals(Global.instAreaShortName))
		{
			UserRes curUser = WebUtil.getCurrentUser();
			acc_oid = String.valueOf(curUser.getUser().getId());
		}else{
			if(!StringUtil.IsEmpty(acc_loginname) && !acc_loginname.equals("null"))
			{
				sql1.append("select acc_oid from tab_accounts where acc_loginname='"+acc_loginname+"'");
				PrepareSQL psqls = new PrepareSQL(sql1.toString());
				roleMap=DBOperation.getRecord(psqls.getSQL());
				acc_oid=StringUtil.getStringValue(roleMap, "acc_oid");
			}
		}

		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_tab_pic_task a,tab_accounts b ");
		sql.append("where a.acc_oid=b.acc_oid ");
		if(Global.JLDX.equals(Global.instAreaShortName)){
			sql.append("and a.status in(1,2,3) ");
		}else{
			sql.append("and a.status in(1,2) ");
		}

		if(!StringUtil.IsEmpty(cityId)&& !"-1".equals(cityId)&& !"00".equals(cityId))
		{
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (" + StringUtils.weave(list) + ")");
			list = null;
		}
		if (null != startTime && !"".equals(startTime)) {
			sql.append(" and  a.add_time>=").append(getTime(startTime));
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append(" and  a.add_time<=").append(getTime(endTime));
		}
		if (null != taskName && !"".equals(taskName)) {
			sql.append(" and  a.task_name='").append(taskName).append("'");
		}
		if(!StringUtil.IsEmpty(acc_oid))
		{
			sql.append(" and  a.acc_oid="+acc_oid+"");
		}
//		sql.append(" order by  a.add_time desc");
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
	public int countOrderTask1(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,String acc_loginname,List accoidList)
	{
		StringBuffer sql1 = new StringBuffer();
		Map<String,String> roleMap=new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_tab_pic_task a,tab_accounts b where a.acc_oid=b.acc_oid and a.status in(1,2) ");
		if(!StringUtil.IsEmpty(cityId)&& !"-1".equals(cityId)&& !"00".equals(cityId))
		{
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (" + StringUtils.weave(list) + ")");
			list = null;
		}
		if (null != startTime && !"".equals(startTime)) {
			sql.append(" and  a.add_time>=").append(getTime(startTime));
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append(" and  a.add_time<=").append(getTime(endTime));
		}
		if (null != taskName && !"".equals(taskName)) {
			sql.append(" and  a.task_name='").append(taskName).append("'");
		}
			sql.append(" and  a.acc_oid in("+StringUtils.weave(accoidList)+")");
		sql.append(" order by  a.add_time desc");
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
	 * 查看详细
	 * @param taskId
	 * @return
	 */
	public List<Map> getTaskDetail1(String taskId) {
		logger.debug("getTaskResult");
		String sql = "select a.file_path,a.task_name,a.task_id,a.vendor_id,a.device_model_id,a.devicetype_id,a.add_time,a.acc_oid,a.city_id,a.sd_qd_pic_url,a.sd_kj_pic_url,a.sd_rz_pic_url,group_id,Invalid_time,priority from stb_tab_pic_task a where a.task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));
		return jt.queryForList(taskSql.getSQL());
	}
	/**
	 * 删除
	 */
	public String doDelete(String taskId) {
		List<String> sqllist = new ArrayList<String>();
		PrepareSQL taskSql = new PrepareSQL("delete  from stb_tab_pic_account where task_id=?");
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
	/**
	 * 修改任务状态
	 * @param status
	 * @param task_id
	 * @return
	 */
	public int updatestatus1(String task_id)
	{
		StringBuffer st=new StringBuffer();
		long currTime = new Date().getTime() / 1000L;
		st.append("update stb_tab_pic_task set status=-1,update_time=? where task_id=?");
		PrepareSQL sql = new PrepareSQL(st.toString());
		sql.setLong(1, currTime);
		sql.setInt(2, Integer.valueOf(task_id));
		int total = jt.update(sql.getSQL());
		return total;
	}
	/**
	 * 查询file_path
	 */
	public List<Map> getFilepath(String taskId)
	{
		PrepareSQL taskSql = new PrepareSQL("select  file_path from stb_tab_pic_task where task_id=?");
		taskSql.setLong(1, Long.parseLong(taskId));
		return jt.queryForList(taskSql.getSQL());
	}
	/**
	 * 查询定制人
	 */
	public List<Map> getaccounts(String acc_oid)
	{
		String sql="select acc_loginname from tab_accounts where acc_oid="+acc_oid;
		PrepareSQL taskSql = new PrepareSQL(sql);
		return jt.queryForList(taskSql.getSQL());
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
		sql.append("select count(*) from stb_logo_record  a, stb_tab_gw_device b where  a.device_id=b.device_id ");
		if (null != taskId && !"".equals(taskId)) {
			sql.append(" and task_id=").append(taskId).append("");
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
		deviceTypeMap =  this.getDeviceType();
		faultCodeMap = this.getFaultCode();
		String sql = "select a.result_id,a.start_time,a.end_time,b.city_id,b.vendor_id,b.device_model_id,b.devicetype_id," +
				" b.device_serialnumber,b.loopback_ip,b.cpe_mac,b.serv_account from stb_logo_record  a, stb_tab_gw_device b where  a.device_id=b.device_id and a.task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));

		List<Map> list = querySP(taskSql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage+1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("cityName",CityDAO.getCityName(String.valueOf(rs.getString("city_id"))));
				if(StringUtil.IsEmpty(rs.getString("end_time")))
				{
					map.put("update_time", "");
				}else
				{
					long update_time = StringUtil.getLongValue(rs
							.getString("end_time"));
					DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
					map.put("update_time", dt.getLongDate());
				}
				map.put("vendorName",DeviceTypeUtil.getVendorName(StringUtil.getStringValue(rs.getString("vendor_id"))));
				map.put("deviceTypeName", deviceTypeMap.get(StringUtil.getStringValue(rs.getString("devicetype_id"))));
				map.put("deviceModel", DeviceTypeUtil.getDeviceModel(StringUtil.getStringValue(rs.getString("device_model_id"))));
				map.put("deviceSerialNumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("servAccount", StringUtil.getStringValue(rs.getString("serv_account")));
				map.put("loopback_ip", StringUtil.getStringValue(rs.getString("loopback_ip")));
				map.put("cpe_mac", StringUtil.getStringValue(rs.getString("cpe_mac")));
				if(StringUtil.getStringValue(rs.getString("result_id")).equals("0"))
				{
					map.put("result","未操作");
				}else
				{
					map.put("result", faultCodeMap.get(StringUtil.getStringValue(rs.getString("result_id"))));
				}
				return map;
			}
		});

		return list;
	}
	/**
	 * 厂商
	 * @param vendor_id
	 * @return
	 */
	public  List<HashMap<String, String>> getVendorMap(String vendor_id)
	{
		PrepareSQL pSQL = new PrepareSQL(
				"select a.vendor_name,b.loopback_ip,b.cpe_mac,b.device_serialnumber,b.serv_account,b.city_id from stb_tab_vendor a,stb_tab_gw_device b where a.vendor_id=b.vendor_id and b.vendor_id ='"
						+ vendor_id + "'");
		return DBOperation.getRecords(pSQL.getSQL());
	}
	/**
	 * 型号
	 */
	public  List<HashMap<String, String>> queryDeviceModel(String device_model_id)
	{

		PrepareSQL pSQL = new PrepareSQL(
				"select a.device_model_id,a.device_model from stb_gw_device_model a where a.device_model_id='"
						+ device_model_id + "'");// 融合stb_gw_device_model
		return DBOperation.getRecords(pSQL.getSQL());
	}
	/**
	 * 版本
	 */
	public  List<HashMap<String, String>> getDeviceType(String devicetype)
	{

		PrepareSQL pSQL = new PrepareSQL();// 融合stb_gw_device_model
		pSQL.append("select a.devicetype_id,a.softwareversion ");
		pSQL.append("from stb_tab_devicetype_info a,stb_gw_device_model b ");
		pSQL.append("where a.vendor_id=b.vendor_id and a.devicetype_id=? ");
		pSQL.setInt(1,StringUtil.getIntegerValue(devicetype));

		return DBOperation.getRecords(pSQL.getSQL());
	}

	public void updatePicAccountStatus(long taskId, int status)
	  {
	    String sql = "update stb_tab_pic_account set status=?,update_time=? where task_id=? ";
	    PrepareSQL psql = new PrepareSQL(sql);
	    psql.setInt(1, status);
	    psql.setLong(2, System.currentTimeMillis() / 1000L);
	    psql.setLong(3, taskId);
	    if (Global.CQDX.equals(Global.instAreaShortName)
	    		|| Global.SXLT.equals(Global.instAreaShortName))
	    {
	      DBOperation.executeUpdate(psql.getSQL(), "xml-test");
	    }
	    else
	      DBOperation.executeUpdate(psql.getSQL(), "xml-picture");
	  }

	public List<HashMap<String, String>> getPicAcccounts(String taskid)
	  {
	    PrepareSQL pSQL = new PrepareSQL(
	      " select a.serv_account,a.customer_id,a.device_id from stb_tab_gw_device a,stb_tab_pic_account b  where a.serv_account = b.serv_account and task_id=" +
	      taskid + " ");
	    return DBOperation.getRecords(pSQL.getSQL());
	  }

	/**
	 * 修改任务状态
	 * @param status
	 * @param task_id
	 * @return
	 */
	public int updatestatus(String status,String task_id)
	{
		StringBuffer st=new StringBuffer();
		long currTime = new Date().getTime() / 1000L;
		if(status.equals("1")
				|| (status.equals("3") && Global.JLDX.equals(Global.instAreaShortName)))
		{
			st.append("update stb_tab_pic_task set status=2,update_time=? where task_id=?");
		}else if(status.equals("2"))
		{
			st.append("update stb_tab_pic_task set status=1,update_time=? where task_id=?");
		}
		PrepareSQL sql = new PrepareSQL(st.toString());
		sql.setLong(1, currTime);
		sql.setInt(2, Integer.valueOf(task_id));
		int total = jt.update(sql.getSQL());
		return total;
	}
	/**
	 * 查看详细信息
	 * @param taskId
	 * @return
	 */
	public Map<String, String> getTaskDetail(String taskId)
	{
		Map<String,String> taskMap = null;
		String sql = "select file_path, sd_qd_pic_url, sd_kj_pic_url, sd_rz_pic_url from stb_tab_pic_task where task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));
		taskMap = jt.queryForMap(taskSql.getSQL());
		return taskMap;
	}
	private  HashMap<String,String>  getDeviceType()
	{
		logger.debug("getDeviceType");
		 deviceTypeMap = new HashMap<String,String>();
		PrepareSQL pSQL = new PrepareSQL("select a.devicetype_id,a.softwareversion from stb_tab_devicetype_info a  ");


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
	public List getGroupId(String groupid)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select group_id,group_name from stb_tab_usergroup where 1=1");
		if(!StringUtil.IsEmpty(groupid))
		{
			pSQL.append(" and group_id="+groupid);
		}
		return DBOperation.getRecords(pSQL.getSQL(),"xml-test");
	}
	private long getTime(String date) {
		DateTimeUtil dt = new DateTimeUtil(date);
		return dt.getLongTime();
	}

	public void recordOperLog(String userAccount, String logip, String hostname,
			long accOid, String itemId, String oprCont){
		// 系统操作日志SQL
		final String saveOperLog = "insert into tr_web_oper_log (log_time, acc_loginname, log_ip, host_name, acc_oid, item_id, oper_cont, oper_type_id) values (?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(saveOperLog);
		psql.setLong(1, System.currentTimeMillis() / 1000);
		psql.setString(2, userAccount);
		psql.setString(3, logip);
		psql.setString(4, hostname);
		psql.setLong(5, accOid);
		psql.setString(6, itemId);
		psql.setString(7, oprCont);
		psql.setInt(8, 1);
		DBOperation.executeUpdate(psql.getSQL());
	}

	public List<Map<String,String>> getServerParameter()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select server_url, access_user, access_passwd from stb_tab_picture_file_server  where 1=1 and file_type = 1 and server_name = '图片服务器' ");
		return jt.queryForList(psql.getSQL());
	}

	public HashMap<String, String> getDeviceTypeMap()
	{
		return deviceTypeMap;
	}

	public void setDeviceTypeMap(HashMap<String, String> deviceTypeMap)
	{
		this.deviceTypeMap = deviceTypeMap;
	}

	public HashMap<String, String> getFaultCodeMap()
	{
		return faultCodeMap;
	}

	public void setFaultCodeMap(HashMap<String, String> faultCodeMap)
	{
		this.faultCodeMap = faultCodeMap;
	}
	public int OpenDeviceShowPicConfig(long taskId,long add_time)
	{
		logger.warn("stbBootAdvertisementBIO(taskId={},add_time={})",
	    		new Object[]{taskId,add_time});
		ArrayList<String> sqllist = new ArrayList<String>();
		PrepareSQL sql1 = new PrepareSQL("update stb_tab_pic_task set UPDATE_TIME=? where task_id=?");
		sql1.setLong(1, add_time);
		sql1.setLong(2, taskId);

		sqllist.add(sql1.getSQL());
		int ier=0;
		if(Global.CQDX.equals(Global.instAreaShortName)
				|| Global.SXLT.equals(Global.instAreaShortName))
		{
			ier= DBOperation.executeUpdate(sqllist, "xml-test");
		}else
		{
			ier = DBOperation.executeUpdate(sqllist, "xml-picture");
		}
		if (ier> 0) {
			logger.warn("任务定制：  成功");
			return 1;
		} else {
			logger.warn("任务定制：  失败");
			return 0;
		}
	}


}
