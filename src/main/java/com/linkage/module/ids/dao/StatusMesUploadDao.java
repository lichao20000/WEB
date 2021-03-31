package com.linkage.module.ids.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.ids.act.StatusMesUploadAct;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class StatusMesUploadDao extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(StatusMesUploadAct.class);

	
	public int importReportTask(String acc_oid,long currTime,String enable,String timelist,String serverurl,String paralist,String tftp_port)
	{
		String sql = "insert into tab_status_report_task (task_id,acc_oid,add_time,enable,timelist,serverurl,paralist,tftp_port) values(?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setLong(1, currTime);
		psql.setLong(2, StringUtil.getLongValue((acc_oid)));
		psql.setLong(3, currTime);
		psql.setLong(4, StringUtil.getLongValue((enable)));
		psql.setLong(5, StringUtil.getLongValue((timelist)));
		psql.setString(6, serverurl);
		psql.setString(7, paralist);
		psql.setLong(8, StringUtil.getLongValue((tftp_port)));
		return jt.update(psql.getSQL());
	}
	
	public List getQueryStatusList(long taskId) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select a.add_time,a.timelist,a.serverurl,a.tftp_port,");
			psql.append("a.paralist,a.task_id,b.acc_loginname ");
		}else{
			psql.append("select a.*,b.acc_loginname ");
		}
		psql.append("from tab_status_report_task a,tab_accounts b ");
		psql.append("where a.acc_oid=b.acc_oid and a.task_id=? ");
		psql.setLong(1,taskId);
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		List<Map<String,String>> templist = new ArrayList<Map<String,String>>();
		for (Map map : list)
		{
			PrepareSQL psql1 = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psql1.append("select count(*) ");
			}else{
				psql1.append("select count(1) ");
			}
			psql1.append("from tab_status_report_task_dev where task_id="+taskId);
			
			int count = jt.queryForInt(psql1.getSQL());
			map.put("devCount", count);
			templist.add(map);
		}
		return templist;
	}
	
	public void insertTaskDev(long taskId,List<String> deviceSN)
	{
		ArrayList<String> tempList = new ArrayList<String>();
		for (String dev : deviceSN)
		{
			String sql = "insert into tab_status_report_task_dev(task_id, device_serialnumber) values(?,?)";
			PrepareSQL psql = new PrepareSQL(sql.toString());
			psql.setLong(1,taskId);
			psql.setString(2,dev);
			tempList.add(psql.getSQL());
		}
		int[] ier = doBatch(tempList);
		if (ier != null && ier.length > 0) {
			logger.warn("批量入库成功");
		} else {
			logger.warn("批量入库失败");
		}
	}
	
	public void updateTaskDev(String taskid,String deviceSn,String result)
	{
		String sql = "update tab_status_report_task_dev set status = ? where task_id = ? and device_serialnumber = ?";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setLong(1,StringUtil.getLongValue(result));
		psql.setLong(2,StringUtil.getLongValue(taskid));
		psql.setString(3,deviceSn);
		int i = jt.update(psql.getSQL());
		if (i > 0) {
			logger.warn("更新任务设备表成功！");
		} else {
			logger.warn("更新任务设备表失败！");
		}
	}
	
	public Map getOuiByDeviceSN(String devicsSn)
	{
		String sql = "select oui from tab_gw_device where device_serialnumber=" + devicsSn;
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return queryForMap(psql.getSQL());
	}
	
	public Map getServerUrl()
	{
		String sql = "select dir_id,outter_url from tab_file_server where serv_name like 'ids%' and file_type=2";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForMap(psql.getSQL());
	}
	
	/**
	 * XJ查询设备列表(关联设备状态表查询)
	 */
	public int queryDeviceByLikeStatusCount(String gw_type,String cityId,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber,String onlineStatus)
	{
		PrepareSQL pSQL = new PrepareSQL();
		//TODO wait
		if(DBUtil.GetDB()==3){
			pSQL.append("select count(*) ");
		}else{
			pSQL.append("select count(1) ");
		}
		pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e " +
				" where a.device_serialnumber not in (select f.device_serialnumber from tab_gw_device_open f) and " +
				" a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
				" and d.is_check=1 and a.devicetype_id=d.devicetype_id ");
		if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
			pSQL.appendAndNumber("e.online_status",PrepareSQL.EQUEAL, onlineStatus);
		}
		pSQL.append(getSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,gw_type));

		return jt.queryForInt(pSQL.toString());
	}
	
	/**
	 * XJ查询设备列表(条数)
	 */
	public int queryDeviceCount(String gw_type,String cityId,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,String deviceSerialnumber)
	{
		PrepareSQL pSQL = new PrepareSQL();
		//TODO wait
		if(DBUtil.GetDB()==3){
			pSQL.append("select count(*) ");
		}else{
			pSQL.append("select count(1) ");
		}
		pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
				"where a.device_serialnumber not in (select f.device_serialnumber from tab_gw_device_open f) " +
				"and a.device_status =1 and d.is_check=1 and a.vendor_id=b.vendor_id " +
				"and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		pSQL.append(getSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,gw_type));
		
		return jt.queryForInt(pSQL.toString());
	}
	
	/**
	 * XJ查询设备列表
	 */
	public ArrayList<HashMap<String,String>> queryDeviceByLikeStatus(String gw_type,String cityId,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,String deviceSerialnumber,String onlineStatus)
	{
		PrepareSQL pSQL = new PrepareSQL();
		//TODO wait
		pSQL.setSQL("select a.device_serialnumber from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e " +
				" where a.device_serialnumber not in (select f.device_serialnumber from tab_gw_device_open f) and " +
				" a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
				" and d.is_check=1 and a.devicetype_id=d.devicetype_id ");
		if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
			pSQL.appendAndNumber("e.online_status",PrepareSQL.EQUEAL, onlineStatus);
		}
		pSQL.append(getSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,gw_type));

		return DBOperation.getRecords(pSQL.getSQL());
	}
	
	/**
	 * XJ查询设备列表
	 */
	public ArrayList<HashMap<String,String>> queryDevice(String gw_type,String cityId,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,String deviceSerialnumber)
	{
		PrepareSQL pSQL = new PrepareSQL();
		//TODO wait
		pSQL.setSQL("select a.device_serialnumber from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where " +
				" a.device_serialnumber not in (select f.device_serialnumber from tab_gw_device_open f) and " + " a.device_status =1 " +
				" and d.is_check=1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		pSQL.append(getSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,gw_type));
		return DBOperation.getRecords(pSQL.getSQL());
	}
	
	/**
	 *  更新 tab_gw_device_open(已开启状态信息上报功能设备表)
	 * 
	 * @param deviceList 设备序列号
	 * @param enable 是否开启：1,开启 ； 0,关闭
	 * @return
	 */
	public int updateOpenedDevices(List<String> deviceList, String enable)
	{
		ArrayList<String> sqlInsertList = new ArrayList<String>();
		ArrayList<String> sqlDeleteList = new ArrayList<String>();
		int result = -1;
		
		if("1".equals(enable)){
			for(String devSn : deviceList){
				String sqlCheck = "select device_serialnumber from tab_gw_device_open where device_serialnumber='"+devSn+"'";
				logger.info(sqlCheck);
				Map<String,String> map = DBOperation.getRecord(sqlCheck);
				if(null==map||map.size()==0){
					String sqlInsert = "insert into tab_gw_device_open(device_serialnumber) values('"+devSn+"')";
					logger.info(sqlInsert);
					sqlInsertList.add(sqlInsert);
				}
			}
			if(sqlInsertList.size()==0){
				result = 1;
			}else{
				result = DBOperation.executeUpdate(sqlInsertList);
			}
		}else if("0".equals(enable)){
			for(String devSn : deviceList){
				String sqlDelete = "delete from tab_gw_device_open where device_serialnumber='"+devSn+"'";
				logger.info(sqlDelete);
				sqlDeleteList.add(sqlDelete);
			}
			if(sqlDeleteList.size()==0){
				result = 1;
			}else{
				result = DBOperation.executeUpdate(sqlDeleteList);
			}
		}
		
		return result;
	}
	
	/**
	 * 拼接sql
	 */
	private String getSql(String cityId,String vendorId,String deviceModelId,String devicetypeId,
			String bindType,String sn,String gw_type)
	{
		StringBuffer sb=new StringBuffer();
		if(!"null".equals(cityId) && !StringUtil.IsEmpty(cityId) 
				&& !"-1".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		if(!"null".equals(vendorId) && !StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sb.append(" and a.vendor_id='"+vendorId+"'");
		}
		if(!"null".equals(deviceModelId) && !StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sb.append(" and a.device_model_id='"+deviceModelId+"'");
		}
		if(!StringUtil.IsEmpty(devicetypeId) && !"null".equals(devicetypeId) && !"-1".equals(devicetypeId)){
			sb.append(" and a.devicetype_id="+devicetypeId);
		}
		if(!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType)){
			sb.append(" and a.cpe_allocatedstatus="+bindType);
		}
		if(!StringUtil.IsEmpty(sn) && !"-1".equals(sn)){
			if(sn.length()>5){
				sb.append(" and a.dev_sub_sn ='"+sn.substring(sn.length()-6, sn.length())+"' ");
			}
			sb.append(" and a.device_serialnumber like '"+sn+"'");
		}
		
		if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type) ) {
			sb.append(" and a.gw_type=" + gw_type );
		}
		
		return sb.toString();
	}
	
}
