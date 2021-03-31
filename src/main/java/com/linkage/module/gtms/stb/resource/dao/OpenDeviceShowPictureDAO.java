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
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.system.utils.StringUtils;

public class OpenDeviceShowPictureDAO extends SuperDAO {

	private static Logger logger = LoggerFactory
			.getLogger(OpenDeviceShowPictureDAO.class);
	private  HashMap<String,String> deviceTypeMap = null;

	private  HashMap<String,String> faultCodeMap = null;

	public int OpenDeviceShowPicConfig(long taskId, String cityId,
			String vendorId, String ipCheck, String macCheck, String taskName,
			long acc_oid, long addTime, String ipSG, String macSG,
			String deviceModelIds, String deviceTypeIds, String custCheck,
			String custSG, String bootFilePath, String startFilePath,
			String authFilePath, String accountFilePath, String remoteAbsoluteIP, String isLocked) {
		List<String> sqllist = new ArrayList<String>();
		// 任务
		PrepareSQL sql1 = new PrepareSQL("insert into  stb_logo_task(");
		sql1.append("task_id,city_id,vendor_id,check_ip,status,check_mac,task_name,acc_oid,add_time,");
		sql1.append("update_time,sd_qd_pic_url,sd_kj_pic_url,sd_rz_pic_url,load_status,file_path,isLocked)");
		sql1.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		sql1.setLong(1, taskId);
		sql1.setString(2, cityId);
		sql1.setString(3, vendorId);
		sql1.setInt(4, StringUtil.getIntegerValue(ipCheck));
		sql1.setInt(5, 0); // status:1生效 , 0失效
		sql1.setInt(6, StringUtil.getIntegerValue(macCheck));
		sql1.setString(7, taskName);
		sql1.setLong(8, acc_oid);
		sql1.setLong(9, addTime);
		sql1.setLong(10, addTime);

		if(StringUtil.IsEmpty(bootFilePath)){
			sql1.setString(11, "");
		}else{
			sql1.setString(11, remoteAbsoluteIP + bootFilePath);
		}
		if(StringUtil.IsEmpty(startFilePath)){
			sql1.setString(12, "");
		}else{
			sql1.setString(12, remoteAbsoluteIP + startFilePath);
		}
		if(StringUtil.IsEmpty(authFilePath)){
			sql1.setString(13, "");
		}else{
			sql1.setString(13, remoteAbsoluteIP + authFilePath);
		}

		sql1.setInt(14, 0);
		sql1.setString(15, accountFilePath);

		if(!"1".equals(isLocked)){
			isLocked = "0";
		}

		sql1.setString(16, isLocked);
		sqllist.add(sql1.getSQL());


		// 版本 chenxj ↓

		if(vendorId != null && vendorId.indexOf("#") !=-1 && deviceModelIds != null && deviceModelIds.indexOf("#") !=-1 && deviceTypeIds != null && deviceTypeIds.indexOf("#") !=-1){
		String[] vendorId_data = vendorId.split("#");
		String[] deviceModelIds_data = deviceModelIds.split("#");
		String[] deviceTypeIds_data = deviceTypeIds.split("#");

		for(int count=0 ; count<vendorId_data.length ; count++){
			String[] temp = deviceModelIds_data[count].split(",");
			String[] temp2 = deviceTypeIds_data[count].split(",");
			List lt = null;
			Map map = null;
			for (String devicemodelId : temp) {
				lt = this.getVersionList(devicemodelId);
				if (null != lt) {
					String softId = "";
					for (int i = 0; i < lt.size(); i++) {
						map = (Map) lt.get(i);
						softId = StringUtil
								.getStringValue(map.get("devicetype_id"));
						for (String devicetypeId : temp2) {
							if (devicetypeId.equals(softId)) {
								PrepareSQL sql2 = new PrepareSQL(
										"insert into  stb_logo_version  values(?,?,?,?)");
								sql2.setLong(1, taskId);
								sql2.setString(2, vendorId_data[count]);
								sql2.setString(3, devicemodelId);
								sql2.setLong(4,
										StringUtil.getLongValue(devicetypeId));
								sqllist.add(sql2.getSQL());
							}
						}
					}
				}
			}
		}
	}else{
		String[] temp = deviceModelIds.split(",");
		String[] temp2 = deviceTypeIds.split(",");
		List lt = null;
		Map map = null;
		for (String devicemodelId : temp) {
			lt = this.getVersionList(devicemodelId);
			if (null != lt) {
				String softId = "";
				for (int i = 0; i < lt.size(); i++) {
					map = (Map) lt.get(i);
					softId = StringUtil
							.getStringValue(map.get("devicetype_id"));
					for (String devicetypeId : temp2) {
						if (devicetypeId.equals(softId)) {
							PrepareSQL sql2 = new PrepareSQL(
									"insert into  stb_logo_version  values(?,?,?,?)");
							sql2.setLong(1, taskId);
							sql2.setString(2, vendorId);
							sql2.setString(3, devicemodelId);
							sql2.setLong(4,
									StringUtil.getLongValue(devicetypeId));
							sqllist.add(sql2.getSQL());
						}
					}
				}
			}
		}
	}


		// chenxj↑


//		// 版本
//		String[] temp = deviceModelIds.split(",");
//		String[] temp2 = deviceTypeIds.split(",");
//		List lt = null;
//		Map map = null;
//		for (String devicemodelId : temp) {
//			lt = this.getVersionList(devicemodelId);
//			if (null != lt) {
//				String softId = "";
//				for (int i = 0; i < lt.size(); i++) {
//					map = (Map) lt.get(i);
//					softId = StringUtil
//							.getStringValue(map.get("devicetype_id"));
//					for (String devicetypeId : temp2) {
//						if (devicetypeId.equals(softId)) {
//							PrepareSQL sql2 = new PrepareSQL(
//									"insert into  stb_logo_version  values(?,?,?,?)");
//							sql2.setLong(1, taskId);
//							sql2.setString(2, vendorId);
//							sql2.setString(3, devicemodelId);
//							sql2.setLong(4,
//									StringUtil.getLongValue(devicetypeId));
//							sqllist.add(sql2.getSQL());
//						}
//					}
//				}
//			}
//		}
		// 如果启动校验IP地址段,入库
		if ("1".equals(ipCheck.trim())) {
			String[] ipDuan = ipSG.trim().split(";");
			String[] ipStr;
			for (String s : ipDuan) {
				PrepareSQL sql3 = new PrepareSQL(
						"insert into stb_logo_ipcheck values(?,?,?)");
				sql3.setLong(1, taskId);
				ipStr = s.split(",");
				sql3.setString(2, getFillIP(ipStr[0]));
				sql3.setString(3, getFillIP(ipStr[1]));
				sqllist.add(sql3.getSQL());
			}
		}
		// 如果启动校验MAC地址段,入库
		if ("1".equals(macCheck.trim())) {
			String[] macDuan = macSG.trim().split(";");
			String[] macStr;
			for (String s : macDuan) {
				PrepareSQL sql3 = new PrepareSQL(
						"insert into stb_logo_maccheck  values(?,?,?)");
				sql3.setLong(1, taskId);
				macStr = s.split(",");
				sql3.setString(2, macStr[0]);
				sql3.setString(3, macStr[1]);
				sqllist.add(sql3.getSQL());
			}
		}
		// 手工输入的帐号需要导入，帐号的后台来的导入。
		if ("1".equals(custCheck) && !StringUtil.IsEmpty(custSG)) {
			String[] custInfo = custSG.split(";");
			for (String s : custInfo) {
				PrepareSQL sql4 = new PrepareSQL(
						"insert into stb_logo_account  values(?,?)");
				sql4.setLong(1, taskId);
				sql4.setString(2, s);
				sqllist.add(sql4.getSQL());
			}
		} else {
			// 如果是导入文件，则由后台模块来入帐号
		}

		int[] ier = doBatch(sqllist);


		if (ier != null && ier.length > 0) {
			logger.warn("任务定制：  成功");
			return 1;
		} else {
			logger.warn("任务定制：  失败");
			return 0;
		}
	}


	/**
	 * 填充IP
	 */
	private String getFillIP(String ip) {
		String fillIP = ip;
		String[] ipArray = new String[4];
		ipArray = ip.split("\\.");
		for (int i = 0; i < 4; i++) {
			if (ipArray[i].length() == 1) {
				ipArray[i] = "00" + ipArray[i];
			} else if (ipArray[i].length() == 2) {
				ipArray[i] = "0" + ipArray[i];
			}
		}
		fillIP = ipArray[0] + "." + ipArray[1] + "." + ipArray[2] + "."
				+ ipArray[3];

		return fillIP;
	}

	/**
	 * @category getVendor 获取所有的厂商
	 *
	 */
	public List getVendor() {
		logger.debug("getVendor()");
		PrepareSQL pSQL = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from stb_tab_vendor");
		return jt.queryForList(pSQL.getSQL());
	}

	// 根据厂商获取型号
	public List getDeviceModel(String vendorId) {
		logger.debug("getDeviceModel(vendorId:{})", vendorId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select a.device_model_id,a.device_model from stb_gw_device_model a where 1=1 ");
		if (null != vendorId && !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='");
			pSQL.append(vendorId);
			pSQL.append("'");
		}
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * @category getVersionList 获取所有的设备版本
	 *
	 * @param vendor_id
	 * @param deviceModelId
	 *
	 * @return List
	 */
	public List getVersionList(String deviceModelIds) {
		logger.debug("getVersionList(deviceModelId:{})", deviceModelIds);
		String sql = "(";
		String[] tempId = deviceModelIds.split(",");
		if (tempId.length > 0) {
			for (int i = 0; i < tempId.length; i++) {
				sql += "'" + tempId[i] + "',";
			}
			sql = sql.substring(0, sql.length() - 1) + ")";
		}
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select a.devicetype_id,a.softwareversion from stb_tab_devicetype_info a where 1=1 ");
		if (!StringUtil.IsEmpty(sql)) {
			pSQL.append(" and a.device_model_id in");
			pSQL.append(sql);
			pSQL.append("");
		}
		return jt.queryForList(pSQL.toString());
	}

	/**
	 * 查询上传服务器，下载服务器的用户名，密码，以及URL等
	 *
	 * @return
	 */
	public Map<String, String> getServerParameter() {

		logger.debug("getServerParameter({})", new Object[] {});

		PrepareSQL psql = new PrepareSQL();
		psql.append("select server_url, access_user, access_passwd from stb_tab_picture_file_server  where 1=1 and file_type = 1 and server_name = '图片服务器' ");

		return queryForMap(psql.getSQL());
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
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName,String isLocked) {
		String rolesql = "select role_id from tab_acc_role where acc_oid = " + acc_oid;

		PrepareSQL psqls = new PrepareSQL(rolesql);
		Map<String,String> roleMap = DBOperation.getRecord(psqls.getSQL());
		String role_id = StringUtil.getStringValue(roleMap, "role_id");
		StringBuffer sql = new StringBuffer();
		sql.append("select  a.task_id,a.task_name,a.add_time,a.status,a.update_time,c.acc_loginname from stb_logo_task a,tab_accounts c where a.acc_oid=c.acc_oid");

		if(!role_id.equals("6")){
			sql.append(" and a.acc_oid = " + acc_oid);
		}else if(role_id.equals("6")){// && accName != null && !"".equals(accName)
			if(accName == null){
				accName = "";
			}
			sql.append(" and c.acc_loginname like '%" + accName + "%'");
		}

		if (null != taskName && !"".equals(taskName)) {
			sql.append(" and a.task_name='").append(taskName).append("'");
		}
		if (null != startTime && !"".equals(startTime)) {
			sql.append(" and a.add_time>=").append(getTime(startTime));
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append(" and a.add_time<=").append(getTime(endTime) + 86399);
		}

		if ("1".equals(isLocked)) {
			sql.append(" and a.isLocked='1'");
		}else if("0".equals(isLocked)){
			sql.append(" and (a.isLocked = '0' or a.isLocked is null)");
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
				map.put("status", rs.getString("status"));
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
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName,String isLocked) {
		logger.debug("countOrderTask");
		String rolesql = "select role_id from tab_acc_role where acc_oid = " + acc_oid;

		PrepareSQL psqls = new PrepareSQL(rolesql);
		Map<String,String> roleMap = DBOperation.getRecord(psqls.getSQL());
		String role_id = StringUtil.getStringValue(roleMap, "role_id");

		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_logo_task  a,tab_accounts c where a.acc_oid=c.acc_oid");

		if(!role_id.equals("6")){
			sql.append(" and a.acc_oid = " + acc_oid);
		}else if(role_id.equals("6")){// && accName != null && !"".equals(accName)
			if(accName == null){
				accName = "";
			}
			sql.append(" and c.acc_loginname like '%" + accName + "%'");
		}

		if (null != taskName && !"".equals(taskName)) {
			sql.append(" and a.task_name='").append(taskName).append("'");
		}
		if (null != startTime && !"".equals(startTime)) {
			sql.append(" and a.add_time>=").append(getTime(startTime));
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append(" and a.add_time<=").append(getTime(endTime) + 86399);
		}

		if ("1".equals(isLocked)) {
			sql.append(" and a.isLocked='1'");
		}else if("0".equals(isLocked)){
			sql.append(" and (a.isLocked = '0' or a.isLocked is null)");
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
				try {
					long update_time = StringUtil.getLongValue(rs
							.getString("end_time"));
					DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
					map.put("update_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("update_time", "");
				} catch (Exception e) {
					map.put("update_time", "");
				}
				map.put("vendorName",DeviceTypeUtil.getVendorName(StringUtil.getStringValue(rs.getString("vendor_id"))));
				map.put("deviceTypeName", deviceTypeMap.get(StringUtil.getStringValue(rs.getString("devicetype_id"))));
				map.put("deviceModel", DeviceTypeUtil.getDeviceModel(StringUtil.getStringValue(rs.getString("device_model_id"))));
				map.put("deviceSerialNumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("servAccount", StringUtil.getStringValue(rs.getString("serv_account")));
				map.put("loopback_ip", StringUtil.getStringValue(rs.getString("loopback_ip")));
				map.put("cpe_mac", StringUtil.getStringValue(rs.getString("cpe_mac")));
				map.put("result", faultCodeMap.get(StringUtil.getStringValue(rs.getString("result_id"))));
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
	 * @param taskId
	 * @return
	 */
	public Map<String, String> getTaskDetail(String taskId) {
		Map<String,String> taskMap = null;
		String sql = "select a.city_id, a.vendor_id, a.add_time, b.acc_loginname from stb_logo_task a,tab_accounts b where task_id=? and a.acc_oid=b.acc_oid";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));
		taskMap = jt.queryForMap(taskSql.getSQL());
		if (null != taskMap) {
			if (null != taskMap.get("city_id")) {
				if(String.valueOf(taskMap.get("city_id")).indexOf(",") > 0){
					String[] cityArr = String.valueOf(taskMap.get("city_id")).split(",");
					String cityName = " ";
					for(String cityid : cityArr){
						cityName += CityDAO.getCityName(cityid) + ",";
					}
					taskMap.put("cityName",cityName.substring(0, cityName.length() - 1).trim());
				}else{
					taskMap.put("cityName", CityDAO.getCityName(String
							.valueOf(taskMap.get("city_id"))));
				}
			} else {
				taskMap.put("cityName", "");
			}
			if (null != taskMap.get("vendor_id")) {
				taskMap.put("vendorName",
						DeviceTypeUtil.getVendorName(taskMap.get("vendor_id")));
			} else {
				taskMap.put("vendorName", "");
			}
			try {
				long add_time = StringUtil
						.getLongValue(taskMap.get("add_time"));
				DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
				taskMap.put("add_time", dt.getLongDate());
			} catch (NumberFormatException e) {
				taskMap.put("add_time", "");
			} catch (Exception e) {
				taskMap.put("add_time", "");
			}
		}
		return taskMap;
	}
	/**
	 * 获取任务中定制的ip地址
	 * @param taskId
	 * @return
	 */
	public List<Map> getIpList(String taskId) {
		String sql = "select start_ip, end_ip from stb_logo_ipcheck  where task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));
		List taskMap = null;
		taskMap = jt.queryForList(taskSql.getSQL());
		return taskMap;
	}
	/**
	 * 获取任务中定制的mac地址
	 * @param taskId
	 * @return
	 */
	public List<Map> getMacList(String taskId) {
		String sql = "select start_mac, end_mac from stb_logo_maccheck  where task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));
		return  jt.queryForList(taskSql.getSQL());
	}

	private long getTime(String date) {
		DateTimeUtil dt = new DateTimeUtil(date);
		return dt.getLongTime();
	}

	public List exportShowDeviceExcelList(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

		/**
		 * 获取软件版本
		 */
		public List getVerList(String taskId)
		{
			String sql = "select vendor_id, device_model_id, devicetype_id from stb_logo_version where task_id=?";
			PrepareSQL taskSql = new PrepareSQL(sql);
			taskSql.setLong(1, Long.parseLong(taskId));
			List taskMap = null;
			taskMap = jt.queryForList(taskSql.getSQL());
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
		public String doDelete(String taskId) {
			// TODO Auto-generated method stub
			List<String> sqllist = new ArrayList<String>();
			PrepareSQL taskSql = new PrepareSQL("delete  from stb_logo_task where task_id=?");
			taskSql.setLong(1, Long.parseLong(taskId));

			sqllist.add(taskSql.getSQL());

			taskSql = new PrepareSQL("delete  from stb_tab_gw_device_locked where task_id=?");

			taskSql.setLong(1, Long.parseLong(taskId));

			sqllist.add(taskSql.getSQL());

			taskSql = new PrepareSQL("delete  from stb_logo_version where task_id=?");

			taskSql.setLong(1, Long.parseLong(taskId));

			sqllist.add(taskSql.getSQL());

			taskSql = new PrepareSQL("delete  from stb_logo_ipcheck where task_id=?");

			taskSql.setLong(1, Long.parseLong(taskId));

			sqllist.add(taskSql.getSQL());
			taskSql = new PrepareSQL("delete  from stb_logo_maccheck where task_id=?");

			taskSql.setLong(1, Long.parseLong(taskId));

			sqllist.add(taskSql.getSQL());
			taskSql = new PrepareSQL("delete  from stb_logo_account where task_id=?");

			taskSql.setLong(1, Long.parseLong(taskId));

			sqllist.add(taskSql.getSQL());
			taskSql = new PrepareSQL("delete  from stb_logo_record where task_id=?");

			taskSql.setLong(1, Long.parseLong(taskId));

			sqllist.add(taskSql.getSQL());
			taskSql = new PrepareSQL("delete  from stb_logo_recent where task_id=?");

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

}


