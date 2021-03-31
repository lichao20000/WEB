
package com.linkage.module.gwms.resource.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

public class SoftwareDAO extends SuperDAO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(SoftwareDAO.class);

	public boolean doSQLList(ArrayList<String> sqllist)
	{
		int iCode[] = DataSetBean.doBatch(sqllist);
		if (iCode != null && iCode.length > 0)
		{
			logger.debug("批量执行策略入库：  成功");
			return true;
		}
		else
		{
			logger.debug("批量执行策略入库：  失败");
			return false;
		}
	}

	/**
	 * 获取软件升级的目标型号，版本对应关系
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return Map
	 */
	public Map getSoftUp()
	{
		if(DBUtil.GetDB()==3)
		{
			PrepareSQL psql = new PrepareSQL();
			psql.append("select temp_id,devicetype_id_old,devicetype_id from gw_soft_upgrade_temp_map");
			Cursor Cursor=DataSetBean.getCursor(psql.getSQL());
			Map map=null;
			if(Cursor!=null){
				map=new HashMap();
				Map m=Cursor.getNext();
				while(m!=null){
					map.put(StringUtil.getStringValue(m,"temp_id")
							+"|"+StringUtil.getStringValue(m,"devicetype_id_old"),
							StringUtil.getStringValue(m,"devicetype_id"));
					m=Cursor.getNext();
				}
			}
			return map;
		}
		
		String strSQL = "select convert(varchar(8),temp_id)+'|'"
			+ "+ convert(varchar(4),devicetype_id_old),devicetype_id";
		
		//add by zhangcong@ 2011-06-21
		if(LipossGlobals.isOracle())
		{
			strSQL = "select to_char(temp_id)||'|'"
				+ "||to_char(devicetype_id_old),devicetype_id";
		}
		
		strSQL = strSQL + " from gw_soft_upgrade_temp_map";
		PrepareSQL psql = new PrepareSQL(strSQL);
		return DataSetBean.getMap(psql.getSQL());
	}

	/**
	 * 获取设备的型号
	 * 
	 * @param
	 * @author wangsenbo
	 * @date 2009-12-24
	 * @return Map
	 */
	public String getDevicetypeId(String deviceId)
	{
		String sql = "select devicetype_id from tab_gw_device where device_id = ?";
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(sql);
		psql.setString(1, deviceId);
		return StringUtil.getStringValue(queryForMap(psql.getSQL()).get("devicetype_id"));
	}

//	/**
//	 * 根据devicetype_id获取软件升级的工单参数
//	 * 
//	 * @param
//	 * @author Jason(3412)
//	 * @date 2008-12-17
//	 * @return Map
//	 */
//	public Map getSoftFileInfo()
//	{
//		String strSQL = "select devicetype_id,'|||'+outter_url+'/'+server_dir+'/'+softwarefile_name"
//				+ "+'|||||||||'+convert(varchar(8),softwarefile_size)+'|||'+softwarefile_name"
//				+ "+'|||0||||||' from tab_software_file a, tab_file_server b where a.dir_id=b.dir_id"
//				+ " and softwarefile_isexist=1";
//		PrepareSQL psql = new PrepareSQL(strSQL);
//		psql.getSQL();
//		return DataSetBean.getMap(strSQL);
//	}
	
	/**
	 * 根据devicetype_id获取软件升级的工单参数
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return Map
	 */
	public Map<String, Map> getSoftFileInfo()
	{
		String joinStr = "+";
		if (DBUtil.GetDB() == 1) {
			joinStr = "||";
		}
		//修改 by 王森博
		String strSQL = "select devicetype_id,outter_url"+joinStr+"'/'"+joinStr+"server_dir"+joinStr+"'/'"+joinStr+"softwarefile_name as file_url"
				+ ",softwarefile_size,softwarefile_name"
				+ " from tab_software_file a, tab_file_server b where a.dir_id=b.dir_id"
				+ " and softwarefile_isexist=1";
		PrepareSQL psql = new PrepareSQL(strSQL);
		
		List list = jt.queryForList(psql.getSQL());
		Map<String, Map> map = new HashMap<String, Map>();
		for (int i = 0; i < list.size(); i++)
		{
			Map tmap = (Map) list.get(i);
			map.put(StringUtil.getStringValue(tmap.get("devicetype_id")), tmap);
		}
		return map;
	}

	/**
	 * 获取预处理的IOR
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getPreProcessIOR()
	{
		String ior = null;
		String iorSQL = "select ior from tab_ior where object_name='PreProcess' and object_poa='PreProcess_Poa'";
		PrepareSQL psql = new PrepareSQL(iorSQL);
		Map<String, String> iorMap = DataSetBean.getRecord(psql.getSQL());
		return iorMap.get("ior");
	}

	/**
	 * 获得策略类型
	 * 
	 * @return
	 */
	public Map getStrategyType(String... typeIds)
	{
		StringBuffer strBuff = new StringBuffer();
		strBuff.append("select type_id,type_name from gw_strategy_type where ");
		for (int i = 0; i < typeIds.length; i++)
		{
			if (i == typeIds.length - 1)
			{
				strBuff.append(" type_id =").append(typeIds[i]);
			}
			else
			{
				strBuff.append(" type_id =").append(typeIds[i]).append(" or ");
			}
		}
		PrepareSQL psql = new PrepareSQL(strBuff.toString());
		return DataSetBean.getMap(psql.getSQL());
	}
	
	/**
	 * @author zhangshibei
	 * @param taskId   任务ID 当前定制时间的数字表现形式
	 * @param deviceId   设备ID
	 * @return      插入表tab_softtask_device 的sql组
	 */
	public List<String> createSoftUpTaskSQL(Long taskId, String deviceId) {
		logger.warn("strategySQL({})");
		List<String> sqlList = new ArrayList<String>();
		
		//生成入软件升级关联设备表的sql语句
		StringBuilder sql = new StringBuilder();
		sql.append("insert into tab_softtask_device (");
		sql.append("task_id,device_id,status) values (");
		sql.append(taskId);
		sql.append(",'" +deviceId);
		sql.append("'," + 0);    //状态 0：未执行，1：执行过
		sql.append(")");
		sqlList.add(sql.toString());
		logger.debug("入策略的sql语句-->{}",sql.toString());
		PrepareSQL psql =  new PrepareSQL(sql.toString());
        psql.getSQL();
		return sqlList;
	}
	/**
	 * @author zhangshibei
	 * @param taskId   任务ID 当前定制时间的数字表现形式
	 * @param starttime   定制轮询的起始时间
	 * @param endtime     定制轮询的结束时间
	 * @param deviceTypeId   目标版本ID 
	 * @return  插入tab_soft_task 表的sql
	 */
	public String getSoftUpTaskSQL(Long taskId, String starttime,
			String endtime, String deviceTypeId) {
		//生成入软件升级任务的sql语句
		StringBuilder sql = new StringBuilder();
		sql.append("insert into tab_soft_task(");
		sql.append("task_id,start_time,end_time,devicetype_id,status) values (");
		sql.append(taskId);
		sql.append(",'" + starttime);
		sql.append("','" + endtime);
		sql.append("'," + deviceTypeId);
		sql.append("," + 0);
		sql.append(")");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return psql.getSQL();
	}
	/** 
	 * @author zhangsibei  2013年6月7日 
	 * @param taskId   任务ID 当前定制时间的数字表现形式
	 * @param starttime   定制轮询的起始时间
	 * @param endtime     定制轮询的结束时间
	 * @param deviceTypeId   目标版本ID 
	 */
	public void addSoftUpTask(Long taskId, String starttime, String endtime,
			String deviceTypeId) {
		String sql = this.getSoftUpTaskSQL(taskId, starttime, endtime, deviceTypeId);
		jt.update(sql);
	}
	/**
	 * @auth   zhangsb 2013年6月7日 
	 * @param taskId 任务ID
	 * @param deviceSQL  前台传过来的sql
	 */
	public void addSoftUpRefDev(Long taskId, String deviceSQL) {
		logger.warn("addSoftUpRefDev="+deviceSQL);
		//截取从 from 开始 的条件语句
		String suffix = deviceSQL.substring(deviceSQL.indexOf("from"),deviceSQL.length());
		
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into tab_softtask_device(task_id,device_id,status) ");
		sql.append(" select distinct  ");
		sql.append(taskId);
		sql.append(", a.device_id, ");
		sql.append(0);
		sql.append("  "+suffix);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		jt.update(psql.getSQL());
	}
	/**
	 * 获取所有的任务信息
	 * @return
	 */
	public List getAllTaskList() 
	{
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		//task_id,start_time,end_time,devicetype_id,status,,,,
		// 无文件/gwms/resource/BatchSoftWareTaskList.jsp
		sql.append(" select * from tab_soft_task");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 获取设备型号ID对应的软件版本
	 * @return
	 */
	public Map<String,String> getSoftVersion() {
		HashMap<String,String> map = new HashMap<String,String>();
		
		StringBuffer sql = new StringBuffer();
		 sql.append(" select a.devicetype_id,a.softwarefile_name" );
		 sql.append("  from tab_software_file a,tab_devicetype_info b " );
		 sql.append("  where a.devicetype_id=b.devicetype_id  ");
		 Cursor cursor = DataSetBean.getCursor(sql.toString());
		 Map fields = cursor.getNext();
		 if (null != fields)
			{
				while (null != fields)
				{
					String devicetypeId = StringUtil.getStringValue(fields.get("devicetype_id"));
					String softName = StringUtil.getStringValue(fields.get("softwarefile_name"));
					map.put(devicetypeId, softName);
					fields = cursor.getNext();
				}
			}
		return map;
	}

	public void deleteTask(String taskId) {
		String sql ="delete from tab_soft_task where task_id ="+taskId;
		PrepareSQL psql = new PrepareSQL(sql);
		jt.update(psql.getSQL());
	}

	public void insertTask(long time, String task_id, String task_name, String mode, String id, String gw_type)
	{
		String sql ="insert into tab_softup_task(task_id,task_name,task_model,status,set_time,update_time,operator,gw_type) values (?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, task_id);
		psql.setString(2, task_name);
		psql.setInt(3, "1".equals(mode)?1:2);
		if (Global.CQDX.equals(Global.instAreaShortName)){
			psql.setInt(4, 2);
		}
		else{
			psql.setInt(4, 1);
		}
		psql.setLong(5, time);
		psql.setLong(6, time);
		psql.setString(7, id);
		psql.setLong(8, StringUtil.getIntegerValue(gw_type));
		jt.update(psql.getSQL());
	}
	
	public void insertTask(long time, String task_id, String task_name, String mode, String id, String gw_type,String softStrategy_type)
	{
		String sql ="insert into tab_softup_task(task_id,task_name,task_model,status,set_time,update_time,operator,gw_type,type) values (?,?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, task_id);
		psql.setString(2, task_name);
		psql.setInt(3, "1".equals(mode)?1:2);
		if (Global.CQDX.equals(Global.instAreaShortName)){
			psql.setInt(4, 2);
		}
		else{
			psql.setInt(4, 1);
		}
		psql.setLong(5, time);
		psql.setLong(6, time);
		psql.setString(7, id);
		psql.setLong(8, StringUtil.getIntegerValue(gw_type));
		psql.setInt(9, StringUtil.IsEmpty(softStrategy_type)?4:StringUtil.getIntegerValue(softStrategy_type));
		jt.update(psql.getSQL());
	}
	
	
	
	public void insertTaskNew(long time, String task_id, String task_name, String mode, String id, String gw_type,String softStrategy_type, String startTime, String endTime,
			String gwShare_queryType, String gwShare_queryField, String gwShare_queryParam, String gwShare_cityId, String gwShare_onlineStatus, 
			String gwShare_vendorId, String gwShare_deviceModelId, String gwShare_devicetypeId, String gwShare_bindType, String gwShare_deviceSerialnumber, String gwShare_fileName)
	{
		String sql ="insert into tab_softup_task(task_id,task_name,task_model,status,set_time,update_time,operator,gw_type,type,STARTTIME,ENDTIME,QUERYTYPE,QUERYFIELD,QUERYPARAM,CITY_ID,ONLINE_STATUS,VENDOR_ID,DEVICE_MODEL_ID,DEVICETYPE_ID,BINDTYPE,DEVICE_SERIALNUMBER,FILENAME) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, task_id);
		psql.setString(2, task_name);
		psql.setInt(3, "1".equals(mode)?1:2);
		if (Global.CQDX.equals(Global.instAreaShortName)){
			psql.setInt(4, 2);
		}
		else{
			psql.setInt(4, 1);
		}
		psql.setLong(5, time);
		psql.setLong(6, time);
		psql.setString(7, id);
		psql.setLong(8, StringUtil.getIntegerValue(gw_type));
		psql.setInt(9, StringUtil.IsEmpty(softStrategy_type)?4:StringUtil.getIntegerValue(softStrategy_type));
		
		psql.setLong(10, new DateTimeUtil(startTime).getLongTime());
		psql.setLong(11, new DateTimeUtil(endTime).getLongTime());
		psql.setString(12, gwShare_queryType);
		psql.setString(13, gwShare_queryField);
		psql.setString(14, gwShare_queryParam);
		psql.setString(15, gwShare_cityId);
		psql.setInt(16, StringUtil.getIntegerValue(gwShare_onlineStatus));
		psql.setString(17, gwShare_vendorId);
		psql.setString(18, gwShare_deviceModelId);
		psql.setLong(19, StringUtil.getLongValue(gwShare_devicetypeId));
		psql.setString(20, gwShare_bindType);
		psql.setString(21, gwShare_deviceSerialnumber);
		psql.setString(22, gwShare_fileName);
		
		jt.update(psql.getSQL());
	}
	
	public static void main(String[] args)
	{
		System.out.println(new DateTimeUtil().getLongTime());
	}

	/**
	 * 根据文件名查询 版本文件信息
	 * @param name
	 * @return
	 */
	public int queryFileByName(String name) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) from tab_software_file where softwarefile_name ='" + name + "'");
		}else{
			psql.append("select count(1) from tab_software_file where softwarefile_name ='" + name + "'");
		}
		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 根据厂商名获取型号
	 * @param vendor_name
	 * @return list
	 */
	public List<Map<String, String>> getModelListByVendorName(String vendor_name) 
	{
		String sql = "select device_model, device_model_id from gw_device_model a, tab_vendor b"
				+ " where a.vendor_id=b.vendor_id and b.vendor_name='" + vendor_name + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 获取设备类型Map
	 * @return
	 */
	public List<Map<String, String>> getDevTypeList() 
	{
		String sql = "select vendor_id, device_model_id, hardwareversion, softwareversion, devicetype_id "
				+ " from tab_devicetype_info";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 获取从版本文件信息表获取 设备类型id列表
	 * @return
	 */
	public List<Map<String, String>> getDevTypeIdBySoftwareFileIsexist() 
	{
		String sql = "select devicetype_id from tab_software_file where softwarefile_isexist = 1";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 根据厂商名获取厂商列表
	 * @param vendor_name
	 * @return
	 */
	public List<Map<String, String>> getVendorList(String vendor_name) 
	{
		String sql = "select vendor_id from tab_vendor where vendor_name='" + vendor_name + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	public Map queryTaskById(String taskId) 
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			sql.append("select vendor_id,device_model_id,devicetype_id,city_id,bindtype,");
			sql.append("starttime,endtime,querytype,queryfield,type,set_time,update_time ");
		}else{
			sql.append("select * ");
		}
		sql.append("from tab_softup_task where task_id=? ");
		
		sql.setString(1, taskId);
		List list = jt.queryForList(sql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if (list!=null &&list.size()>0 ) {
			 map=(Map) list.get(0);
		}
		
		String vendor_id = map.get("vendor_id");
		if(null!=DeviceTypeUtil.vendorMap && !StringUtil.IsEmpty(vendor_id)){
			String vendor = DeviceTypeUtil.vendorMap.get(vendor_id);
			map.put("vendor_id", vendor);
		}
		
		String device_model_id = map.get("device_model_id");
		if(null!=DeviceTypeUtil.deviceModelMap && !StringUtil.IsEmpty(device_model_id)){
			String model = DeviceTypeUtil.deviceModelMap.get(device_model_id);
			map.put("device_model_id", model);
		}
		
		String devicetype_id = StringUtil.getStringValue(map.get("devicetype_id"));
		if(null!=DeviceTypeUtil.softVersionMap && !StringUtil.IsEmpty(devicetype_id)){
			String version = DeviceTypeUtil.softVersionMap.get(devicetype_id);
			if(StringUtil.IsEmpty(version))
			{
				DeviceTypeUtil.init();  //更新下内存
				version = DeviceTypeUtil.softVersionMap.get(devicetype_id); //再取一次
			}
			map.put("version", version);
		}
		
		String city_id = map.get("city_id");
		
		if(null!=Global.G_CityId_CityName_Map && !StringUtil.IsEmpty(city_id)){
			map.put("city_name", Global.G_CityId_CityName_Map.get(city_id));
		}
		else{
			map.put("city_name","");
		}
		
		String bindtype = map.get("bindtype");
		if("1".equals(bindtype)){
			map.put("isbind", "是");
		}
		else{
			map.put("isbind", "否");
		}
		
		Object objects = map.get("starttime");
		Object objecte = map.get("endtime");
		if(!StringUtil.IsEmpty(StringUtil.getStringValue(objects))){
			long start = StringUtil.getLongValue(objects);
			//map.put("start_time", new DateTimeUtil(start*1000-3600000*8).getTime());
			map.put("start_time", new DateTimeUtil(start*1000).getLongDate().substring(11));
			map.put("start_date", new DateTimeUtil(start*1000).getLongDate().substring(0, 10));
		}
		if(!StringUtil.IsEmpty(StringUtil.getStringValue(objecte))){
			long end = StringUtil.getLongValue(objecte);
			//map.put("end_time", new DateTimeUtil(end*1000-3600000*8).getTime());
			map.put("end_time", new DateTimeUtil(end*1000).getLongDate().substring(11));
			map.put("end_date", new DateTimeUtil(end*1000).getLongDate().substring(0, 10));
		}
		
		//String filename = map.get("filename");
		String querytype = map.get("querytype");
		if("1".equals(querytype)){
			map.put("querytypeDesc", "简单查询");
		}
		else if("2".equals(querytype)){
			map.put("querytypeDesc", "高级查询");
		}
		else if("3".equals(querytype)){
			map.put("querytypeDesc", "文件导入");
		}
		
		String queryfield = map.get("queryfield");
		//String queryparam = map.get("queryparam");
		if("deviceSn".equals(queryfield)){
			map.put("queryfield", "设备序列号");
		}
		else if("deviceIp".equals(queryfield)){
			map.put("queryfield", "设备IP");
		}
		else if("username".equals(queryfield)){
			map.put("queryfield", "唯一标识");
		}
		else if("kdname".equals(queryfield)){
			map.put("queryfield", "宽带账号");
		}
		else if("voipPhoneNum".equals(queryfield)){
			map.put("queryfield", "VOIP电话号码");
		}
		else{
			map.put("queryfield", "其他");
		}
		
		Object object = map.get("type");
		String informNum  = StringUtil.getStringValue(object);
		StringBuffer type = new StringBuffer();
		for(int i=0;i<informNum.length();i++){
			if(!StringUtil.IsEmpty(type.toString()) && !",".equals(type.toString().substring(type.toString().length()-1))){
				type.append(",");
			}
			type.append(getInformDesc(informNum.substring(i, i+1)));
		}
		map.put("type", type.toString().replace(",", " "));
		
		map.put("set_time", new DateTimeUtil(StringUtil.getLongValue(map.get("set_time"))*1000).getLongDate());
		
		map.put("update_time", new DateTimeUtil(StringUtil.getLongValue(map.get("update_time"))*1000).getLongDate());
		
		return map;
	}

	public List<Map<String,String>> getImportDeviceList(String type,String gwType){
		StringBuffer sql = new StringBuffer();
		if(type.equals("username")){
			String tableName = "tab_hgwcustomer";
			if(!StringUtil.IsEmpty(gwType) && "2".equals(gwType)){
				tableName = "tab_egwcustomer";
			}
			sql.append("select a.device_id,b.devicetype_id from ");
			sql.append(tableName);
			sql.append(" a,tab_gw_device b,tab_softwareup_tmp c where a.device_id = b.device_id and a.user_state in ('1','2') and a.username = c.data and c.type = 1 ");
			if (null != gwType && !"".equals(gwType)&& !"null".equals(gwType) ) {
				sql.append(" and b.gw_type = " + gwType );
			}
		}else if(type.equals("device_serialnumber")){
			sql.append(" select a.device_id,a.devicetype_id from tab_gw_device a,tab_softwareup_tmp b ");
			sql.append(" where a.device_status =1 and a.device_serialnumber = b.data and b.type = 1");
			if (null != gwType && !"".equals(gwType)&& !"null".equals(gwType) ) {
				sql.append(" and a.gw_type = " + gwType );
			}
		}
		PrepareSQL prepareSQL = new PrepareSQL();
		prepareSQL.setSQL(sql.toString());
		return jt.queryForList(prepareSQL.getSQL());
	}

	public int[] insertSoftupTaskDev(String taskId,List<Map<String,String>> deviceList) {
		ArrayList<String> sqlList = new ArrayList<String>();
		String sql = "";
		for (Map<String,String> deviceMap : deviceList) {
			String deviceId = deviceMap.get("device_id");
			int deviceTypeId = deviceMap.get("devicetype_id") == null ? 0 : Integer.parseInt(deviceMap.get("devicetype_id"));
			sql = "insert into tab_softup_task_dev(task_id,device_id,device_type_id,status) values ('" + taskId + "','" + deviceId +  "'," + deviceTypeId + ",0)";
			sqlList.add(sql);
		}
		return DataSetBean.doBatch(sqlList);
	}
	
	
	String getInformDesc(String num){
		Map<String,String> map = new HashMap<String,String>();
		map.put("2", "周期上报");
		map.put("5", "重新启动");
		map.put("6", "参数改变");
		map.put("4", "下次连接到系统");
		if(null == map.get(num)) return "";
		return map.get(num);
	}
}
