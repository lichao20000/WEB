package com.linkage.module.gwms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.litms.common.util.JdbcTemplateExtend;

public class SuperGatherLanTaskDAO extends SuperDAO{
	
	//日志记录
	private static Logger logger = LoggerFactory.getLogger(SuperGatherLanTaskDAO.class);
	
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	/**
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * 查询设备ID
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> queryDeviceIdList(long areaId,String cityId,String vendorId,String deviceModelId,String devicetypeId,String deviceSerialnumber,String deviceIp){
		logger.debug("SuperGatherLanTaskDAO=>queryDeviceIdList()");
		PrepareSQL pSQL = new PrepareSQL();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		pSQL.setSQL("select a.device_id from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		
		//这个是重庆软件升级的分支，因为bio方法有很多地方调用，所以没有直接传参当标识符，用以,结尾的判断
		if(",".equals(cityId.substring(cityId.length()-1))){
			pSQL.append(" and a.city_id in (" + cityId.substring(0,cityId.length()-1) + ")");
		}
		else{
			if(null!=cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !"00".equals(cityId)){
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
				cityArray = null;
			}
		}
		if(null!=vendorId && !"null".equals(vendorId) && !"".equals(vendorId) && !"-1".equals(vendorId)){
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if(null!=deviceModelId && !"null".equals(deviceModelId) && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)){
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		if(null!=devicetypeId && !"null".equals(devicetypeId) && !"".equals(devicetypeId) && !"-1".equals(devicetypeId)){
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
		}
		if(null!=deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)){
			if(deviceSerialnumber.length()>5){
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length()-6, deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}
		if(null!=deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)){
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
		}
		return jt.queryForList(pSQL.toString());
	}
	
	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  List<Map> queryDeviceIdList(long areaId,String cityId,String username){
		
		logger.debug("SuperGatherLanTaskDAO=>queryDeviceIdList()");
		PrepareSQL pSQL = new PrepareSQL();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		pSQL.setSQL("select a.device_id from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,");
		pSQL.append("tab_hgwcustomer");
		pSQL.append(" e where a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if(null!=cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		if(null!=username && !"".equals(username)){
			if("1".equalsIgnoreCase(LipossGlobals.getLipossProperty("isLikeQuery"))){
				pSQL.append(" and e.username like '%");
				pSQL.append(username);
				pSQL.append("' ");
				String user_sub_name = username;
				if(username.length() > 6){
					user_sub_name = username.substring(username.length()-6);
				}
				pSQL.append(" and e.user_sub_name ='");
				pSQL.append(user_sub_name);
				pSQL.append("'");
			}else{
				pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
			}
		}

		return jt.queryForList(pSQL.toString());
	}
	
	/**
	 * 根据VOIP电话号码以及属地统计有多少设备
	 * 
	 * @param city_id
	 * @param voipPhoneNum
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> queryDeviceIdList(String city_id,String voipPhoneNum){

		logger.debug("queryDeviceList({},{})", new Object[]{city_id, voipPhoneNum});

		PrepareSQL psql = new PrepareSQL();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		
		String table_customer = "tab_hgwcustomer";
		String table_voip = "tab_voip_serv_param";
		psql.append("select c.device_id ");
		psql.append("  from " + table_customer + " a, " + table_voip + " b, tab_gw_device c, tab_vendor d, gw_device_model e, tab_devicetype_info f ");
		psql.append(" where 1 = 1 ");
		psql.append("   and a.user_id = b.user_id");
		psql.append("   and a.device_id = c.device_id");
		psql.append("   and c.vendor_id = d.vendor_id");
		psql.append("   and c.device_model_id = e.device_model_id ");
		psql.append("   and c.devicetype_id = f.devicetype_id");
		psql.append("   and c.device_status = 1"); // 设备已确认

		// 根据VOIP电话号码
		if (null != voipPhoneNum && !"".equals(voipPhoneNum)) {
			psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
		}

		if(null!=city_id && !"null".equals(city_id) && !"".equals(city_id) && !"-1".equals(city_id) && !"00".equals(city_id)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
			psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}

		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 查询设备页面增加按照宽带账号统计总数
	 * @param gw_type
	 * @param cityId
	 * @param kdname
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> queryDeviceIdListByKdname(String cityId, String kdname)
	{
		logger.debug("queryDeviceByKdname({},{})", new Object[]{cityId, kdname});
		PrepareSQL psql = new PrepareSQL();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		String table_customer = "tab_hgwcustomer";
		String table_serv_info = "hgwcust_serv_info";
		psql.append("select c.device_id");
		psql.append(" from " + table_customer + " a, " + table_serv_info + " b, tab_gw_device c, tab_vendor d, ");
		psql.append(" gw_device_model e, tab_devicetype_info f");
		psql.append(" where a.device_id=c.device_id and a.user_id = b.user_id ");
		psql.append("   and c.vendor_id = d.vendor_id");
		psql.append("   and c.device_model_id = e.device_model_id ");
		psql.append("   and c.devicetype_id = f.devicetype_id");
		psql.append("   and c.device_status = 1"); // 设备已确认

		if(null!=cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}

		// 宽带账号
		if(!StringUtil.IsEmpty(kdname))
		{
			psql.append(" and b.serv_type_id = 10 and b.username='" + kdname + "'");
		}

		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 查询设备信息
	 * 
	 * @param deviceId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> queryDeviceMap(String deviceId){
		
		logger.debug("queryDeviceMap({})", new Object[]{deviceId});
		
		cityMap = CityDAO.getCityIdCityNameMap();
		
		PrepareSQL psql = new PrepareSQL();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		psql.append("select a.city_id,a.device_serialnumber,e.vendor_add,f.device_model,g.hardwareversion,g.softwareversion,d.username,h.gigabit_port");
		psql.append(" from tab_gw_device a left join (select b.device_id,c.username from tab_hgwcustomer b,hgwcust_serv_info c where b.user_id = c.user_id and c.serv_type_id = 10) d on a.device_id = d.device_id,");
		psql.append(" tab_vendor e,gw_device_model f,tab_devicetype_info g left join tab_device_version_attribute h on g.devicetype_id = h.devicetype_id");
		psql.append(" where a.vendor_id = e.vendor_id ");
		psql.append(" and a.device_model_id = f.device_model_id");
		psql.append(" and a.devicetype_id = g.devicetype_id ");
		psql.append(" and e.vendor_id = g.vendor_id");
		psql.append(" and a.device_id = " + deviceId);
		
		return jt.query(psql.getSQL(),new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				
				
				Map<String, String> map = new HashMap<String, String>();
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				
				
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("username", rs.getString("username"));
				
				if("1".equals(rs.getString("gigabit_port")))
				{
					map.put("gigabit_port", "是");
				}
				else {
					map.put("gigabit_port", "否");
				}
				
				return map;
			}
		});
	}
	
	/**
	 * 定制批量采集任务
	 * 
	 * @param taskMap
	 * @return
	 */
	public int createTask(Map<String,Object> taskMap){
		
		logger.debug("createTask({})", new Object[]{taskMap});
		PrepareSQL psql = new PrepareSQL();
		
		psql.append("insert into tab_gather_lan1_task (task_id,task_type,status,execute_count,file_name,city_id,vendor_id," +
				"device_model_id,devicetype_id,cpe_currentstatus,create_time,create_operator) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?)");
		
		String fileName = StringUtil.getStringValue(taskMap.get("file_name"));
		if(!StringUtil.IsEmpty(fileName)){
			fileName = getFilePath() + fileName;
		}
		
		psql.setLong(1, StringUtil.getLongValue(taskMap.get("task_id")));
		psql.setLong(2, StringUtil.getLongValue(taskMap.get("task_type")));
		psql.setLong(3, StringUtil.getLongValue(taskMap.get("status")));
		psql.setLong(4, StringUtil.getLongValue(taskMap.get("execute_count")));
		psql.setString(5, fileName);
		psql.setString(6, StringUtil.getStringValue(taskMap.get("city_id")));
		psql.setString(7, StringUtil.getStringValue(taskMap.get("vendor_id")));
		psql.setString(8, StringUtil.getStringValue(taskMap.get("device_model_id")));
		psql.setLong(9, StringUtil.getLongValue(taskMap.get("devicetype_id")));
		psql.setLong(10, StringUtil.getLongValue(taskMap.get("cpe_currentstatus")));
		psql.setLong(11, StringUtil.getLongValue(taskMap.get("create_time")));
		psql.setString(12, StringUtil.getStringValue(taskMap.get("create_operator")));
		return jt.update(psql.getSQL());
	}
	
	/**
	 * 查询任务列表
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings({"rawtypes" })
	public List<Map> getTaskList(String startTime, String endTime, int curPage_splitPage, int num_splitPage){
		logger.debug("getTaskList({})", new Object[]{startTime,endTime});
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("select a.task_id,a.task_type,a.status,a.city_id,a.file_name,a.execute_count,a.create_operator,a.create_time,");
		}else{
			psql.append("select a.*,");
		}
		
		psql.append("b.vendor_add,c.device_model,d.softwareversion " +
				"from tab_gather_lan1_task a " +
				"left join tab_vendor b on a.vendor_id = b.vendor_id " +
				"left join gw_device_model c on a.device_model_id = c.device_model_id " +
				"left join tab_devicetype_info d on a.devicetype_id = d.devicetype_id " +
				"where 1= 1 ");
		
		if(!StringUtil.IsEmpty(startTime)){
			psql.append(" and a.create_time >=" + startTime);
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			psql.append(" and a.create_time <=" + endTime);
		}
		
		psql.append(" order by a.create_time desc");
		
		cityMap = CityDAO.getCityIdCityNameMap();
		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage, new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("taskId", rs.getString("task_id"));
				
				String taskType = rs.getString("task_type");
				if ("1".equals(taskType)) {
					map.put("taskType", "高级定制");
				} else if ("2".equals(taskType)) {
					map.put("taskType", "导入定制");
				} else {
					map.put("taskType", "");
				}
				
				String status = rs.getString("status");
				map.put("status", status);
				if ("1".equals(status)) {
					map.put("statusName", "正在执行");
				} else if ("2".equals(status)) {
					map.put("statusName", "执行完成");
				} else {
					map.put("statusName", "未执行");
				}
				
				String taskDesc = "";
				if("1".equals(taskType)){
					
					String vendorAdd = StringUtil.IsEmpty(rs.getString("vendor_add")) ? "" : rs.getString("vendor_add");
					String deviceModel = StringUtil.IsEmpty(rs.getString("device_model")) ? "" : rs.getString("device_model");
					String softwareversion = StringUtil.IsEmpty(rs.getString("softwareversion")) ? "" : rs.getString("softwareversion");
					
					taskDesc = "属地：【" + cityMap.get(rs.getString("city_id")) + "】厂商：【" + vendorAdd +
							"】型号：【" + deviceModel + "】版本：【" + softwareversion + "】";
				}else if("2".equals(taskType)){
					taskDesc = rs.getString("file_name");
				}
				map.put("taskDesc", taskDesc);
				
				map.put("executeCount", rs.getString("execute_count"));
				map.put("createOperator", rs.getString("create_operator"));
				
				long time = Long.parseLong(rs.getString("create_time"));
				DateTimeUtil dateTimeUtil = new DateTimeUtil(time * 1000);
				map.put("createTime", dateTimeUtil.getYYYY_MM_DD_HH_mm_ss());
				return map;
			}
			
		});
	}
	
	/**
	 * 获取页数
	 * 
	 * @param startTime
	 * @param endTime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getMaxPage(String startTime, String endTime, int curPage_splitPage, int num_splitPage){
		logger.debug("getMaxPage({})", new Object[]{startTime,endTime});
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_gather_lan1_task a " +
				"left join tab_vendor b on a.vendor_id = b.vendor_id " +
				"left join gw_device_model c on a.device_model_id = c.device_model_id " +
				"left join tab_devicetype_info d on a.devicetype_id = d.devicetype_id " +
				"where 1=1 ");
		
		if(!StringUtil.IsEmpty(startTime)){
			psql.append(" and create_time >=" + startTime);
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			psql.append(" and create_time <=" + endTime);
		}
		int total = jt.queryForInt(psql.getSQL());
		
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * 查询任务详情列表
	 * 
	 * @param taskId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	@SuppressWarnings({"rawtypes" })
	public List<Map> getDetailList(long taskId, int curPage_splitPage, int num_splitPage){
		logger.debug("getDetailList({})", new Object[]{taskId});
		
		PrepareSQL psql = new PrepareSQL();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		psql.append("select a.city_id,a.device_serialnumber,e.vendor_add,f.device_model,g.hardwareversion,g.softwareversion,d.username,h.gigabit_port,t.status,t.lan1,t.update_time" +
				" from tab_gather_lan1_detail t,tab_gw_device a " +
				" left join (select b.device_id,c.username from tab_hgwcustomer b,hgwcust_serv_info c where b.user_id = c.user_id and c.serv_type_id = 10) d" +
				" on a.device_id = d.device_id," +
				" tab_vendor e,gw_device_model f,tab_devicetype_info g" +
				" left join tab_device_version_attribute h on g.devicetype_id = h.devicetype_id" +
				" where e.vendor_id = f.vendor_id and a.device_model_id = f.device_model_id and a.devicetype_id = g.devicetype_id" +
				" and e.vendor_id = g.vendor_id and a.device_id = t.device_id and t.task_id =  " + taskId);
		
		psql.append(" order by t.update_time desc");
		cityMap = CityDAO.getCityIdCityNameMap();
		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage, new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city_name",cityMap.get(rs.getString("city_id")));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("username", rs.getString("username"));
				
				map.put("lan1", rs.getString("lan1"));
				
				String gigabitPort = rs.getString("gigabit_port");
				if("1".equals(gigabitPort))
				{
					map.put("gigabit_port", "是");	
				}
				else {
					map.put("gigabit_port", "否");	
				}
				
				String status = rs.getString("status");
				if ("1".equals(status)) {
					map.put("status", "成功");
				} else if ("2".equals(status)) {
					map.put("status", "失败");
				} else {
					map.put("status", "未开始");
				}
				
				long time = Long.parseLong(rs.getString("update_time"));
				DateTimeUtil dateTimeUtil = new DateTimeUtil(time * 1000);
				map.put("update_time", dateTimeUtil.getYYYY_MM_DD_HH_mm_ss());
				
				return map;
			}
			
		});
	}
	
	/**
	 * 获取任务详情页数
	 * 
	 * @param startTime
	 * @param endTime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getDeatilMaxPage(long taskId, int curPage_splitPage, int num_splitPage){
		logger.debug("getDeatilMaxPage({})", new Object[]{taskId});
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_gather_lan1_detail t,tab_gw_device a " +
				" left join (select b.device_id,c.username from tab_hgwcustomer b,hgwcust_serv_info c where b.user_id = c.user_id and c.serv_type_id = 10) d" +
				" on a.device_id = d.device_id," +
				" tab_vendor e,gw_device_model f,tab_devicetype_info g" +
				" left join tab_device_version_attribute h on g.devicetype_id = h.devicetype_id" +
				" where e.vendor_id = f.vendor_id and a.device_model_id = f.device_model_id and a.devicetype_id = g.devicetype_id" +
				" and e.vendor_id = g.vendor_id and a.device_id = t.device_id and t.task_id =  " + taskId);
		int total = jt.queryForInt(psql.getSQL());
		
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * 删除任务
	 * 
	 * @param taskId
	 * @return
	 */
	public int delTask(long taskId){
		logger.debug("delTask({})", new Object[]{taskId});
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("delete from tab_gather_lan1_task where task_id = " + taskId);		
		return jt.update(psql.getSQL());
	}
	
	/**
	 * 查询任务详情
	 * 
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public List<Map> getDetailList(long taskId){
		logger.debug("getDetailList({})", new Object[]{taskId});
		
		PrepareSQL psql = new PrepareSQL();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		psql.append("select a.city_id,a.device_serialnumber,e.vendor_add,f.device_model,g.hardwareversion,g.softwareversion,d.username,h.gigabit_port,t.status,t.lan1,t.update_time" +
				" from tab_gather_lan1_detail t,tab_gw_device a " +
				" left join (select b.device_id,c.username from tab_hgwcustomer b,hgwcust_serv_info c where b.user_id = c.user_id and c.serv_type_id = 10) d" +
				" on a.device_id = d.device_id," +
				" tab_vendor e,gw_device_model f,tab_devicetype_info g" +
				" left join tab_device_version_attribute h on g.devicetype_id = h.devicetype_id" +
				" where e.vendor_id = f.vendor_id and a.device_model_id = f.device_model_id and a.devicetype_id = g.devicetype_id" +
				" and e.vendor_id = g.vendor_id and a.device_id = t.device_id and t.task_id =  " + taskId);
		
		psql.append(" order by t.update_time desc");
		cityMap = CityDAO.getCityIdCityNameMap();
		
		return jt.query(psql.getSQL(), new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("city_name",cityMap.get(rs.getString("city_id")));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("username", rs.getString("username"));
				
				map.put("lan1", rs.getString("lan1"));
				
				String gigabitPort = rs.getString("gigabit_port");
				if("1".equals(gigabitPort))
				{
					map.put("gigabit_port", "是");	
				}
				else {
					map.put("gigabit_port", "否");	
				}
				
				String status = rs.getString("status");
				if ("1".equals(status)) {
					map.put("status", "成功");
				} else if ("2".equals(status)) {
					map.put("status", "失败");
				} else {
					map.put("status", "未开始");
				}
				
				long time = Long.parseLong(rs.getString("update_time"));
				DateTimeUtil dateTimeUtil = new DateTimeUtil(time * 1000);
				map.put("update_time", dateTimeUtil.getYYYY_MM_DD_HH_mm_ss());
				
				return map;
			}
			
		});
	}
	
	public String getFilePath() {
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径",lipossHome);
		return lipossHome + "/temp/";
	}
}
