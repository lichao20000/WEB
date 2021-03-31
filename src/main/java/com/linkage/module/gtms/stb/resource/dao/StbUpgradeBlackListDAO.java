
package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;
import com.linkage.system.utils.StringUtils;

public class StbUpgradeBlackListDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(StbUpgradeBlackListDAO.class);
	private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



	public long getStartTime() {
		try {
			String day = sdfDay.format(new Date());
			return sdfSec.parse(day + " 00:00:00").getTime() / 1000L;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}


	/**
	 * 查询设备信息
	 * @param sqlSpell
	 */
	public  ArrayList<HashMap<String, String>> getDevIdsHBLT(String sqlSpell){
		return DBOperation.getRecords(sqlSpell);
	}

	/**
	 * 增加任务信息
	 * @param name
	 * @param taskid
	 * @param accoid
	 * @param addtime
	 * @param starttime
	 * @param endtime
	 * @param enddate
	 * @return
	 */
	public int addTaskHBLT(String task_name,long taskid, long accoid, String param, int type,int status){

		PrepareSQL psql = new PrepareSQL(
		"insert into stb_tab_blacklist_task(task_id,task_name,acc_oid,add_time,param,type,status,update_time) values(?,?,?,?,?,?,?,?)");
		psql.setLong(1, taskid);
		psql.setString(2, task_name);
		psql.setLong(3, accoid);
		psql.setLong(4, taskid);
		psql.setString(5, param);
		psql.setInt(6, type);
		psql.setInt(7, status);
		psql.setLong(8, System.currentTimeMillis()/1000);

		return jt.update(psql.getSQL());

	}

	public int addBlackList(long taskid){

		PrepareSQL selectPsql = new PrepareSQL(
						"select a.device_id, a.device_serialnumber" +
						"from stb_tab_blacklist_dev a " +
						"where not exists (select task_id from stb_tab_blacklist b where a.device_id=b.device_id and a.task_id=?) and a.task_id=?");
		selectPsql.setLong(1, taskid);
		selectPsql.setLong(2, taskid);
		List<Map> list = jt.queryForList(selectPsql.getSQL());

        if(list == null || list.isEmpty()){
            return 0;
        }

        String[] sqlArr = new String[list.size()];
        PrepareSQL psql = new PrepareSQL();
        for (int i = 0; i < list.size(); i++) {
			psql.setSQL("insert into stb_tab_blacklist(device_id, device_serialnumber, add_time) values ('" +
					StringUtil.getStringValue(list.get(i), "device_id") + "','"+
					StringUtil.getStringValue(list.get(i), "device_serialnumber") + "','" +
					System.currentTimeMillis() / 1000 +
					"')");
			sqlArr[i] = psql.getSQL();
        }
        int[] resultArr = jt.batchUpdate(sqlArr);

        if(resultArr == null || resultArr.length == 0 || resultArr[0] == 0){
            return 0;
        }else{
            return 1;
        }
	}


	/**
	 * 把设备序列号插入临时表
	 * @param fileName
	 * @param dataList
	 */
	public void insertTmp(String fileName, List<String> dataList) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		for (int i = 0; i < dataList.size(); i++) {
			psql = new PrepareSQL();
			psql.append("insert into tab_temporary_device"
					+ "(filename,device_serialnumber)" + " values ('" + fileName + "','"
					+ dataList.get(i) + "')");
			sqlList.add(psql.getSQL());
		}
		int res = 0;
		if (LipossGlobals.inArea(Global.JSDX)) {
			res = DBOperation.executeUpdate(sqlList, "proxool.xml-report");
		} else {
			ArrayList<String> sqlListTmp = new ArrayList<String>();
			if(sqlList.size()>0){
				for(String sql : sqlList){
					sqlListTmp.add(sql);
					if(sqlListTmp.size()>=200){
						int resTmp = DBOperation.executeUpdate(sqlListTmp);
						if(resTmp>0){
							res += sqlListTmp.size();
						}
						sqlListTmp.clear();
					}
				}
			}
			if(sqlListTmp.size()>0){
				int resTmp = DBOperation.executeUpdate(sqlListTmp);
				if(resTmp>0){
					res += sqlListTmp.size();
				}
				sqlListTmp.clear();
			}
			logger.info("====成功插入tab_temporary_device表"+res+"条数据====");
		}
	}


	/**
	 * 获取设备信息
	 * @param serList
	 * @param filename
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getTaskDevListHBLT(String filename) {

		PrepareSQL psql = new PrepareSQL();

		psql.append(" select a.device_id, a.oui, a.device_serialnumber ");
		psql.append(" from stb_tab_gw_device a ,tab_temporary_device b where  ");
		psql.append(" a.device_id=b.device_serialnumber  ");
		psql.append(" and b.filename='"+filename+"'");


		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(psql.getSQL());
		if(null==list || list.size()==0){
			return null;
		}

		return list;
	}

	public ArrayList<String> sqlList(List<HashMap<String, String>> devList,long taskid){
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < devList.size(); i++) {
			String device_serialnumber = String.valueOf(devList.get(i).get("device_serialnumber"));
			String device_id = String.valueOf(devList.get(i).get("device_id"));
			String oui = String.valueOf(devList.get(i).get("oui"));

			PrepareSQL pSql = new PrepareSQL();
			pSql.append(" insert into stb_tab_blacklist_dev (task_id,device_id,device_serialnumber,oui,status,add_time)");
			pSql.append("  values(?,?,?,?,?,?)");

			pSql.setLong(1, taskid);
			pSql.setString(2, device_id);
			pSql.setString(3, device_serialnumber);
			pSql.setString(4, oui);
			pSql.setLong(5, 0);
			pSql.setLong(6, System.currentTimeMillis()/1000);

			sqlList.add(pSql.getSQL());
		}
		return sqlList;
	}


	/**
	 * 批量插入设备信息
	 * @param sqlList
	 */
	public int insertTaskDevHBLT(ArrayList<String> sqlList){
		return DBOperation.executeUpdate(sqlList);
	}

	private List<Map> queryAllVendor()
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select vendor_id, vendor_add from stb_tab_vendor");
		return querySP(pSql.getSQL(), 0, 1000);
	}

	private List<Map> queryAllModel()
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select device_model_id, device_model from stb_gw_device_model");
		return querySP(pSql.getSQL(), 0, 10000);
	}
	/**
	 * 查询所有设备类型
	 *
	 * @return
	 */
	private List<Map> queryAllTypeInfo()
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select devicetype_id, softwareversion from stb_tab_devicetype_info");
		return querySP(pSql.getSQL(), 0, 10000);
	}

	/**
	 * 转换软件版本
	 *
	 * @param typeInfoList
	 * @param devicetypeID
	 * @return
	 */
	public String getSoftVersionByTypeInfoID(List<Map> typeInfoList, String devicetypeID)
	{
		if (null == typeInfoList || StringUtil.IsEmpty(devicetypeID)
				|| "null".equals(devicetypeID.trim()))
		{
			return devicetypeID;
		}
		for (Map map : typeInfoList)
		{
			if (devicetypeID.equals(StringUtil.getStringValue(map, "devicetype_id")))
			{
				return StringUtil.getStringValue(map, "softwareversion");
			}
		}
		return devicetypeID;
	}
	/**
	 * 转换型号显示
	 *
	 * @param modelList
	 * @param modelID
	 * @return
	 */
	public String getModelNameByModelID(List<Map> modelList, String modelID)
	{
		if (null == modelList || StringUtil.IsEmpty(modelID)
				|| "null".equals(modelID.trim()))
		{
			return modelID;
		}
		for (Map map : modelList)
		{
			if (modelID.equals(StringUtil.getStringValue(map, "device_model_id")))
			{
				return StringUtil.getStringValue(map, "device_model");
			}
		}
		return modelID;
	}
	/**
	 * 转换厂商显示
	 *
	 * @param vendorList
	 * @param vendorID
	 * @return
	 */
	public String getVendorNameByVendorID(List<Map> vendorList, String vendorID)
	{
		if (null == vendorList || StringUtil.IsEmpty(vendorID)
				|| "null".equals(vendorID.trim()))
		{
			return vendorID;
		}
		for (Map map : vendorList)
		{
			if (vendorID.equals(StringUtil.getStringValue(map, "vendor_id")))
			{
				return StringUtil.getStringValue(map, "vendor_add");
			}
		}
		return vendorID;
	}

	public List<Map> getBlackDeviceList(int curPage_splitPage, int num_splitPage, String sn, String servAccount)
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select a.device_serialnumber, b.device_id, b.oui, b.serv_account, b.city_id, "
                    + " b.loopback_ip, b.devicetype_id, b.device_model_id, b.vendor_id, b.cpe_mac "
                    + " from stb_tab_blacklist a left join stb_tab_gw_device b"
					+ " on a.device_serialnumber=b.device_serialnumber "
					+ " where 1=1");
		if (!StringUtil.IsEmpty(sn) && !"-1".equals(sn))
		{
			pSql.append(" and a.device_serialnumber = '" + sn + "'");
		}
		if (!StringUtil.IsEmpty(servAccount))
		{
			pSql.append(" and b.serv_account = '" + servAccount + "'");
		}

		return querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage+1,num_splitPage, new RowMapper()
		{
			List<Map> vendorList = queryAllVendor();
			List<Map> modelList = queryAllModel();
			List<Map> typeInfoList = queryAllTypeInfo();

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs, vendorList, modelList,
						typeInfoList);
			}
		});

	}

	public int getBlacDeviceListCount(String servAccount,String deviceSerialnumber)
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select count(*) from stb_tab_blacklist a left join stb_tab_gw_device b"
					+ " on a.device_serialnumber=b.device_serialnumber "
					+ " where 1=1");
		if (!StringUtil.IsEmpty(deviceSerialnumber) && !"-1".equals(deviceSerialnumber))
		{
			pSql.append(" and a.device_serialnumber = '" + deviceSerialnumber + "'");
		}
		if (!StringUtil.IsEmpty(servAccount))
		{
			pSql.append(" and b.serv_account = '" + servAccount + "'");
		}
		return jt.queryForInt(pSql.getSQL());
	}

	private static final String transDate(Object seconds)
	{
		if (seconds != null)
		{
			try
			{
				DateTimeUtil dt = new DateTimeUtil(
						Long.parseLong(seconds.toString()) * 1000);
				return dt.getLongDate();
			}
			catch (NumberFormatException e)
			{
				logger.error(e.getMessage(), e);
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}

	public Map<String, String> resultSet2Map(Map<String, String> map, ResultSet rs,
			List<Map> vendorList, List<Map> modelList,
			List<Map> typeInfoList)
	{
		try
		{
			map.put("device_id", rs.getString("device_id"));
			map.put("oui", rs.getString("oui"));
			map.put("device_serialnumber", rs.getString("device_serialnumber"));
			map.put("serv_account", rs.getString("serv_account"));
			map.put("city_id", rs.getString("city_id"));
			map.put("city_name",CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
			map.put("loopback_ip", rs.getString("loopback_ip"));
			map.put("devicetype_id", rs.getString("devicetype_id"));
			map.put("softwareversion",getSoftVersionByTypeInfoID(typeInfoList,rs.getString("devicetype_id")));
			map.put("device_model_id", rs.getString("device_model_id"));
			map.put("device_model",getModelNameByModelID(modelList, rs.getString("device_model_id")));
			map.put("vendor_id", rs.getString("vendor_id"));
			map.put("vendor_add",getVendorNameByVendorID(vendorList, rs.getString("vendor_id")));
			map.put("cpe_mac", rs.getString("cpe_mac"));
		}
		catch (SQLException e)
		{
			logger.error(e.getMessage(), e);
		}
		return map;
	}

	public int deleteDevice(String device_serialnumber)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("delete from stb_tab_blacklist where device_serialnumber =?");
		pSQL.setString(1, device_serialnumber);
		return jt.update(pSQL.getSQL());
	}

	private long getTime(String date) {
		DateTimeUtil dt = new DateTimeUtil(date);
		return dt.getLongTime();
	}

	public List<Map> getOrderTaskList(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String taskName,long acc_oid,String accName) {
		StringBuffer sql = new StringBuffer("select a.task_id, a.task_name, a.add_time, a.status," +
                "b.acc_loginname from stb_tab_blacklist_task a left join tab_accounts b on a.acc_oid=b.acc_oid where ");

		if(acc_oid!=1){
			sql.append("b.acc_oid = "+ acc_oid);
		}
		else{
			sql.append("1 = 1");
		}

		if(null != accName && !"".equals(accName) ){
			sql.append(" and b.acc_loginname like '%" + accName + "%'");
		}

		if (null != taskName && !"".equals(taskName)) {
			sql.append(" and a.task_name like '%"+taskName+"%'");
		}
		if (null != startTime && !"".equals(startTime)) {
			sql.append(" and a.add_time>=").append(getTime(startTime));
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append(" and a.add_time<=").append(getTime(endTime) + 86399);
		}

		sql.append(" order by a.add_time desc ");
		PrepareSQL psql = new PrepareSQL(sql.toString());

		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("task_id", rs.getString("task_id"));
				map.put("acc_loginname", rs.getString("acc_loginname"));
				map.put("task_name", rs.getString("task_name"));
				map.put("add_time", transDate(rs.getString("add_time")));
				String status = rs.getString("status");
				if("0".equals(status)){
					status = "未做";
				}
				else if("1".equals(status)){
					status = "已做";
				}
				else if("-1".equals(status)){
					status = "失效";
				}
				else if("2".equals(status)){
					status = "正在执行中";
				}
				else{
					status = "异常";
				}
				map.put("status", status);
				return map;
			}
		});
	}

	public int countOrderTask(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String taskName,long acc_oid,String accName) {
		StringBuffer sql = new StringBuffer("select count(*) from stb_tab_blacklist_task a left join tab_accounts b on a.acc_oid=b.acc_oid where ");
		if(acc_oid!=1){
			sql.append("b.acc_oid = "+ acc_oid);
		}
		else{
			sql.append("1 = 1");
		}

		if(null != accName && !"".equals(accName) ){
			sql.append(" and b.acc_loginname like '%" + accName + "%'");
		}

		if (null != taskName && !"".equals(taskName)) {
			sql.append(" and a.task_name like '%"+taskName+"%'");
		}
		if (null != startTime && !"".equals(startTime)) {
			sql.append(" and a.add_time>=").append(getTime(startTime));
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append(" and a.add_time<=").append(getTime(endTime) + 86399);
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		return total;
	}

	public String doDisable(String taskId) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL pSQL1 = new PrepareSQL();
		pSQL1.setSQL("delete from stb_tab_blacklist a " +
                "where exists ( select b.task_id from stb_tab_blacklist_dev b Where a.device_id = b.device_id and b.task_id=?)");
		pSQL1.setLong(1, Long.parseLong(taskId));

		PrepareSQL pSQL2 = new PrepareSQL();
		pSQL2.setSQL("update stb_tab_blacklist_task set status =-1 where task_id=?");
		pSQL2.setLong(1, Long.parseLong(taskId));

		sqlList.add(pSQL1.getSQL());
		sqlList.add(pSQL2.getSQL());

		int res = DBOperation.executeUpdate(sqlList);

		logger.warn("失效操作返回结果res = "+res);
		if (res >0) {
			return "1";
		} else {
			return "0";
		}
	}

	public String doAble(String taskId) {
		ArrayList<String> sqlList = new ArrayList<String>();

        PrepareSQL selectPsql = new PrepareSQL();
        selectPsql.setSQL("select a.device_id, a.device_serialnumber" +
                "from stb_tab_blacklist_dev a " +
                "where not exists (select b.device_id from stb_tab_blacklist b where a.device_id=b.device_id and a.task_id=?) and a.task_id=?");
        selectPsql.setLong(1, Long.parseLong(taskId));
        selectPsql.setLong(2, Long.parseLong(taskId));
        ArrayList<HashMap<String, String>> list = DBOperation.getRecords(selectPsql.getSQL());

        PrepareSQL psql = new PrepareSQL();
        if(list != null && !list.isEmpty()){
            for (int i = 0; i < list.size(); i++) {
				psql.setSQL("insert into stb_tab_blacklist(device_id, device_serialnumber, add_time) values ('" +
						StringUtil.getStringValue(list.get(i), "device_id") + "','" +
						StringUtil.getStringValue(list.get(i), "device_serialnumber") + "','" +
						System.currentTimeMillis() / 1000 +
						"')");
                sqlList.add(psql.getSQL());
            }
        }

        PrepareSQL pSQL2 = new PrepareSQL();
		pSQL2.setSQL("update stb_tab_blacklist_task set status =1 where task_id=?");
		pSQL2.setLong(1, Long.parseLong(taskId));

		sqlList.add(pSQL2.getSQL());

		int res = DBOperation.executeUpdate(sqlList);

		logger.warn("生效操作返回结果res = "+res);
		if (res >0) {
			return "1";
		} else {
			return "0";
		}
	}

	public List getdetailList(String taskId, int curPage_splitPage,int num_splitPage) {
		PrepareSQL pSql = new PrepareSQL();

		pSql.setSQL("select a.device_serialnumber, b.device_id, b.oui, b.serv_account, b.city_id, "
                    + " b.loopback_ip, b.devicetype_id, b.device_model_id, b.vendor_id, b.cpe_mac "
                    + " from stb_tab_blacklist_dev a left join stb_tab_gw_device b"
					+ " on a.device_id=b.device_id "
					+ " where 1=1 and a.task_id=?");
		pSql.setLong(1, Long.parseLong(taskId));

		return querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage+1,num_splitPage, new RowMapper()
		{
			List<Map> vendorList = queryAllVendor();
			List<Map> modelList = queryAllModel();
			List<Map> typeInfoList = queryAllTypeInfo();

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs, vendorList, modelList,
						typeInfoList);
			}
		});
	}

	public int getdetailListCount(int curPage_splitPage, int num_splitPage,String taskId) {
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select count(*) from stb_tab_blacklist_dev a left join stb_tab_gw_device b"
					+ " on a.device_id=b.device_id "
					+ " where 1=1 and a.task_id=?");
		pSql.setLong(1, Long.parseLong(taskId));
		return jt.queryForInt(pSql.getSQL());
	}
}
