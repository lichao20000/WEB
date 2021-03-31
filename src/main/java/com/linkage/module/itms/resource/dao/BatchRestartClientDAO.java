package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
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

public class BatchRestartClientDAO extends SuperDAO {
	private static Logger logger = LoggerFactory
			.getLogger(BatchRestartClientDAO.class);
	
	private  HashMap<String,String> deviceTypeMap = null;
	
	private  HashMap<String,String> faultCodeMap = null;
	
	public int saveBatchRestartClientTask(long taskId, String taskName,	long acc_oid, long addTime, String accountFilePath, 
			String acc_cityId, String startTime, String endTime, Integer batchType, Integer netServNum, Integer netWanType) {
		List<String> sqllist = new ArrayList<String>();
		
		PrepareSQL sql1 = new PrepareSQL("insert into tab_batch_task(");
		try {
			if(batchType == 1){
				sql1.append("task_id,task_name,acc_oid,add_time,update_time,file_path,batch_type,city_id,dostart_time,doend_time)");
				sql1.append(" values(?,?,?,?,?,?,?,?,?,?)");
				sql1.setLong(1, taskId);
				sql1.setString(2, taskName);
				sql1.setLong(3, acc_oid);
				sql1.setLong(4, addTime);
				sql1.setLong(5, addTime);
				sql1.setString(6, accountFilePath); 
				sql1.setInt(7, batchType); 
				sql1.setString(8, acc_cityId); 
				sql1.setString(9, startTime); 
				sql1.setString(10, endTime); 
			}else if(batchType == 2){
				sql1.append("task_id,task_name,acc_oid,add_time,update_time,file_path,batch_type,net_serv_num,city_id)");
				sql1.append(" values(?,?,?,?,?,?,?,?,?)");
				sql1.setLong(1, taskId);
				sql1.setString(2, taskName);
				sql1.setLong(3, acc_oid);
				sql1.setLong(4, addTime);
				sql1.setLong(5, addTime);
				sql1.setString(6, accountFilePath);
				sql1.setInt(7, batchType); 
				sql1.setInt(8, netServNum); 
				sql1.setString(9, acc_cityId); 
			}else if(batchType == 3){
				sql1.append("task_id,task_name,acc_oid,add_time,update_time,file_path,batch_type,net_wan_type,city_id,dostart_time,doend_time)");
				sql1.append(" values(?,?,?,?,?,?,?,?,?,?,?)");
				sql1.setLong(1, taskId);
				sql1.setString(2, taskName);
				sql1.setLong(3, acc_oid);
				sql1.setLong(4, addTime);
				sql1.setLong(5, addTime);
				sql1.setString(6, accountFilePath);
				sql1.setInt(7, batchType); 
				sql1.setInt(8, netWanType); 
				sql1.setString(9, acc_cityId); 
				sql1.setString(10, startTime); 
				sql1.setString(11, endTime); 
			}			
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
	 * 批量重启终端
	 * @param taskId
	 * @param acc_oid
	 * @param addTime
	 * @param acc_cityId
	 * @param startTime
	 * @param endTime
	 * @param batchType
	 * @param queryType
	 * @param deviceId
	 * @param onlinestatus
	 * @param vendor_id
	 * @param device_model_id
	 * @param cpe_allocatedstatus
	 * @param deviceSerialnumber
	 * @return
	 */
	public int BacthRestartTerminal(long taskId,long acc_oid, long addTime,
			String acc_cityId, String startTime, String endTime, Integer batchType, String queryType,String onlinestatus,String vendor_id,
			String device_model_id,String cpe_allocatedstatus,String deviceSerialnumber)
	{
		List<String> sqllist=new ArrayList<String>();
		PrepareSQL sql1 = new PrepareSQL("insert into tab_batch_task(");
		
			sql1.append("task_id,acc_oid,add_time,update_time,city_id,dostart_time,doend_time,batch_type,search_type,online_status,vendor_id,device_model_id,cpe_allocatedstatus,device_serialnumber");
			sql1.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			sql1.setLong(1, taskId);
			sql1.setLong(2, acc_oid);
		    sql1.setLong(3, addTime);
		    sql1.setLong(4, addTime);
		    sql1.setString(5, acc_cityId);
		    sql1.setString(6, startTime);
		    sql1.setString(7, endTime);
		    sql1.setInt(8, batchType);
		    sql1.setInt(9, StringUtil.getIntegerValue(queryType));
		    sql1.setInt(10, StringUtil.getIntegerValue(onlinestatus));
			sql1.setString(11, vendor_id);
			sql1.setString(12, device_model_id);
			sql1.setInt(13, StringUtil.getIntegerValue(cpe_allocatedstatus));
			sql1.setString(14, deviceSerialnumber);
			
		
		sqllist.add(sql1.getSQL());

		int[] ier = null;
		try {
		    ier = doBatch(sqllist);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("任务重启：  失败");
			return 0;
		}
		if (ier != null && ier.length > 0) {
			logger.warn("任务重启：  成功");
			return 1;
		} else {
			logger.warn("任务重启：  失败");
			return 0;
		}
	}
	/**
	 * 批量变更上网方式
	 * @param taskId
	 * @param acc_oid
	 * @param addTime
	 * @param acc_cityId
	 * @param startTime
	 * @param endTime
	 * @param batchType
	 * @param queryType
	 * @param deviceId
	 * @param onlinestatus
	 * @param vendor_id
	 * @param device_model_id
	 * @param cpe_allocatedstatus
	 * @param deviceSerialnumber
	 * @param netWanType
	 * @return
	 */
	public int BatchChangeNetPlayMode(long taskId,long acc_oid, long addTime,
			String acc_cityId, String startTime, String endTime, Integer batchType, String queryType,String onlinestatus,String vendor_id,
			String device_model_id,String cpe_allocatedstatus,String deviceSerialnumber,Integer netWanType)
	{List<String> sqllist=new ArrayList<String>();
	PrepareSQL sql1 = new PrepareSQL("insert into tab_batch_task(");
	
		sql1.append("task_id,acc_oid,add_time,update_time,city_id,dostart_time,doend_time,batch_type,search_type,online_status,vendor_id,device_model_id,cpe_allocatedstatus,device_serialnumber,net_wan_type");
		sql1.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		sql1.setLong(1, taskId);
		sql1.setLong(2, acc_oid);
	    sql1.setLong(3, addTime);
	    sql1.setLong(4, addTime);
	    sql1.setString(5, acc_cityId);
	    sql1.setString(6, startTime);
	    sql1.setString(7, endTime);
	    sql1.setInt(8, batchType);
	    sql1.setInt(9, StringUtil.getIntegerValue(queryType));
	    sql1.setInt(10, StringUtil.getIntegerValue(onlinestatus));
		sql1.setString(11, vendor_id);
		sql1.setString(12, device_model_id);
		sql1.setInt(13, StringUtil.getIntegerValue(cpe_allocatedstatus));
		sql1.setString(14, deviceSerialnumber);
		sql1.setInt(15, netWanType);

		sqllist.add(sql1.getSQL());
		int[] ier = null;
		try {
		    ier = doBatch(sqllist);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("任务重启：  失败");
			return 0;
		}
		if (ier != null && ier.length > 0) {
			logger.warn("任务重启：  成功");
			return 1;
		} else {
			logger.warn("任务重启：  失败");
			return 0;
		}
	}
	/**
	 * 批量修改
	 * @param taskId
	 * @param taskName
	 * @param acc_oid
	 * @param addTime
	 * @param acc_cityId
	 * @param batchType
	 * @param netServNum
	 * @param queryType
	 * @param deviceId
	 * @param onlinestatus
	 * @param vendor_id
	 * @param device_model_id
	 * @param cpe_allocatedstatus
	 * @param deviceSerialnumber
	 * @return
	 */
	public int saveBatchRestartClient(long taskId, String taskName,	long acc_oid, long addTime,String acc_cityId, 
			Integer batchType, Integer netServNum, String queryType,String onlinestatus,String vendor_id,String device_model_id,String cpe_allocatedstatus,String deviceSerialnumber)
	{
			List<String> sqllist = new ArrayList<String>();
		
		PrepareSQL sql1 = new PrepareSQL("insert into tab_batch_task(");
			int[] ier = null;
		logger.warn("queryType=======>"+queryType);
		
			logger.warn("高级查询-------------");
			logger.warn("taskId==========="+taskId);
			logger.warn("taskName==========="+taskName);
			logger.warn("acc_oid==========="+acc_oid);
			logger.warn("addTime==========="+addTime);
			logger.warn("batchType==========="+batchType);
			logger.warn("netServNum==========="+netServNum);
			logger.warn("acc_cityId==========="+acc_cityId);
			logger.warn("onlinestatus==========="+onlinestatus);
			logger.warn("vendor_id==========="+vendor_id);
			logger.warn("device_model_id==========="+device_model_id);
			logger.warn("cpe_allocatedstatus==========="+cpe_allocatedstatus);
			logger.warn("deviceSerialnumber==========="+deviceSerialnumber);
			sql1.append("task_id,task_name,acc_oid,add_time,update_time,batch_type,net_serv_num,city_id,search_type," +
					"online_status,vendor_id,device_model_id,cpe_allocatedstatus,device_serialnumber)");
			sql1.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			sql1.setLong(1, taskId);
			sql1.setString(2, taskName);
			sql1.setLong(3, acc_oid);
			sql1.setLong(4, addTime);
			sql1.setLong(5, addTime);
			sql1.setInt(6, batchType); 
			sql1.setInt(7, netServNum); 
			sql1.setString(8, acc_cityId); 
			//sql1.setInt(9,StringUtil.getIntegerValue(tmap.get("devicetype_id")) );
			sql1.setInt(9, StringUtil.getIntegerValue(queryType));
			sql1.setInt(10, StringUtil.getIntegerValue(onlinestatus));
			sql1.setString(11, vendor_id);
			sql1.setString(12, device_model_id);
			sql1.setInt(13, StringUtil.getIntegerValue(cpe_allocatedstatus));
			sql1.setString(14, deviceSerialnumber);
			sqllist.add(sql1.getSQL());
			try {
			    ier = doBatch(sqllist);
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("111111");
				logger.warn("任务定制：  失败");
				return 0;
			}
			logger.warn("ier==========="+ier);
			if (ier != null && ier.length > 0) {
				logger.warn("任务定制：  成功");
				return 1;
			} else {
				logger.warn("222222222");
				logger.warn("任务定制：  失败");
				return 0;
			}
	}
	/**
	 * deviceId
	 * 查询
	 */
	public String getDevicetypeId(String deviceId)
	{
		logger.warn("getDevicetypeId=======>");
		String sql = "select devicetype_id from tab_gw_device where device_id = ?";
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(sql);
		psql.setString(1, deviceId);
		return StringUtil.getStringValue(queryForMap(psql.getSQL()).get("devicetype_id"));
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
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName, Integer batchType) {
		String rolesql = "select role_id from tab_acc_role where acc_oid = " + acc_oid;

		PrepareSQL psqls = new PrepareSQL(rolesql);
		Map<String,String> roleMap = DBOperation.getRecord(psqls.getSQL());
		String role_id = StringUtil.getStringValue(roleMap, "role_id");
		StringBuffer sql = new StringBuffer();
		sql.append("select  a.task_id,a.task_name,a.add_time,a.file_path,a.acc_oid,c.acc_loginname from tab_batch_task  a,tab_accounts c where a.acc_oid=c.acc_oid");
		
//		if(!role_id.equals("6")){
//			sql.append(" and a.acc_oid = " + acc_oid);
//		}else if(role_id.equals("6")){// && accName != null && !"".equals(accName)
//		}
		
		if(accName == null)
		{
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
		if (null != batchType && !"".equals(batchType)) {
			sql.append(" and a.batch_type=").append(batchType);
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
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName, Integer batchType) {
		logger.debug("countOrderTask");
		String rolesql = "select role_id from tab_acc_role where acc_oid = " + acc_oid;

		PrepareSQL psqls = new PrepareSQL(rolesql);
		Map<String,String> roleMap = DBOperation.getRecord(psqls.getSQL());
		String role_id = StringUtil.getStringValue(roleMap, "role_id");
		
		StringBuffer sql = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql.append("select count(*) from tab_batch_task a,tab_accounts c where a.acc_oid=c.acc_oid");
		}
		else {
			sql.append("select count(1) from tab_batch_task a,tab_accounts c where a.acc_oid=c.acc_oid");
		}
		
//		if(!role_id.equals("6")){
//			sql.append(" and a.acc_oid = " + acc_oid);
//		}else if(role_id.equals("6")){// && accName != null && !"".equals(accName)
//			
//		}		
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
		if (null != batchType && !"".equals(batchType)) {
			sql.append(" and a.batch_type=").append(batchType);
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
	public List getTaskResult(String taskId,int curPage_splitPage,int num_splitPage) {
		logger.debug("getTaskResult");
		deviceTypeMap =  this.getDeviceType();
//		faultCodeMap = this.getFaultCode();
		
		String sql = "select a.TASK_NAME,b.device_id,b.device_serialnumber, b.create_time,b.update_time, " +
				" b.process_status,b.process_times,b.SERV_NAME, c.city_id,c.vendor_id, c.DEVICETYPE_ID,c.device_model_id " +
				" from test_speed_task a, test_speed_dev b, tab_gw_device c  where  a.TASK_ID=b.TASK_ID and a.task_id=? " +
				" and c.device_id=b.device_id order by b.UPDATE_TIME desc ";
		
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
				if("1".equals(process_status)){
					map.put("result", "成功");
				}else if("-1".equals(process_status)){
					map.put("result", "失败");
				}else if("0".equals(process_status)){
					map.put("result", "未处理");
				}else {
					map.put("result", "未知情况");
				}
				
				return map;
			}
		});
		
		return list;
	}
	/**
	 * 将device_id添加到临时表中
	 */
	public int InsertDeviceId(String[] deviceId_array,long taskId)
	{
		String sql="insert into tab_seniorquery_tmp(filename,devicesn) values(?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		ArrayList<String> sqlList = new ArrayList<String>();
		for(String deviceIdarray:deviceId_array)
		{
			psql.setLong(1, taskId);
			psql.setString(2, deviceIdarray);
			sqlList.add(psql.getSQL());
		}
		int[] ier= doBatch(sqlList);
		if (ier != null && ier.length > 0) {
			logger.warn("插入：  成功");
			return 1;
		} else {
			logger.warn("插入：  失败");
			return 0;
		}
	}
	/**
	 * 查询devicetype_id
	 */
	public List devicetypeid(long taskId)
	{
		String sql="select a.devicetype_id from tab_gw_device a,tab_seniorquery_tmp b where a.device_id=b.devicesn and filename=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, taskId);
		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 删除临时表中的数据
	 * @return
	 */
	public int delecttable(long taskId)
	{
		PrepareSQL sql1 = new PrepareSQL("delete  from tab_seniorquery_tmp where filename=?");
		sql1.setLong(1, taskId);
		return jt.queryForInt(sql1.getSQL());
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
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql.append("select count(*) from test_speed_task a,test_speed_dev b, stb_tab_gw_device c " +
					" where a.TASK_ID=b.TASK_ID and b.device_id=c.device_id ");
		}
		else {
			sql.append("select count(1) from test_speed_task a,test_speed_dev b, stb_tab_gw_device c " +
					" where a.TASK_ID=b.TASK_ID and b.device_id=c.device_id ");
		}
		
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
	 * 获取所有厂商
	 * @return
	 */
	public List getVendor() {
		logger.debug("getVendor()");
		PrepareSQL pSQL = new PrepareSQL("select vendor_id,vendor_name, vendor_add from tab_vendor order by vendor_name");
		return jt.queryForList(pSQL.getSQL());
	}
	/**
	 * 获取所有型号
	 * @param vendorId
	 * @return
	 */
	public List getDeviceModel(String vendorId) {
		logger.debug("getDeviceModel(vendorId:{})",vendorId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select a.device_model_id,a.device_model from gw_device_model a where 1=1 ");
		if (null != vendorId && !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='");
			pSQL.append(vendorId);
			pSQL.append("'");
		}
		pSQL.append(" order by device_model");
		return jt.queryForList(pSQL.getSQL());
	}
	/**
	 * 获取所有设备版本
	 * @param deviceModelId
	 * @param isBatch
	 * @return
	 */
	public List getVersionList(String deviceModelId,String isBatch) {
		logger.debug("getVersionList(deviceModelId:{})",deviceModelId);
		PrepareSQL pSQL = new PrepareSQL();
		if("1".equals(isBatch)){
			pSQL.append("select a.devicetype_id,a.hardwareversion,a.softwareversion from tab_devicetype_info a, gw_soft_upgrade_temp_map b   where a.devicetype_id=b.devicetype_id_old ");
			if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
				pSQL.append(" and a.device_model_id='");
				pSQL.append(deviceModelId);
				pSQL.append("'");
				pSQL.append(" order by a.softwareversion ");
			}
		}else if("is_check".equals(isBatch)){
			pSQL.append("select a.devicetype_id,a.softwareversion,a.hardwareversion from tab_devicetype_info a where 1=1 and a.is_check=1 ");
			if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
				pSQL.append(" and a.device_model_id='");
				pSQL.append(deviceModelId);
				pSQL.append("'");
			}
		}else{
			pSQL.append("select a.devicetype_id,a.softwareversion,a.hardwareversion from tab_devicetype_info a where 1=1 ");
			if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
				pSQL.append(" and a.device_model_id='");
				pSQL.append(deviceModelId);
				pSQL.append("'");
			}
		}
		
		return jt.queryForList(pSQL.getSQL());
	}
	/**
	 * 删除任务
	 */	
	public String doDelete(String taskId) {
		List<String> sqllist = new ArrayList<String>();
		PrepareSQL taskSql = new PrepareSQL("delete  from tab_batch_task where task_id=?");
		taskSql.setLong(1, Long.parseLong(taskId));
		
		sqllist.add(taskSql.getSQL());
		
//		taskSql = new PrepareSQL("delete  from tab_batch_account where task_id=?");
		
//		taskSql.setLong(1, Long.parseLong(taskId));
		
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
	
}
