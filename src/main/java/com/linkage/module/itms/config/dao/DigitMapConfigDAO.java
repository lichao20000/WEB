package com.linkage.module.itms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author chenjie(67371)
 * @date   2011.4.7
 */

@SuppressWarnings("unchecked")
public class DigitMapConfigDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(DigitMapConfigDAO.class);

	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> devicetypeMap = null;
	
	/**
	 * modify by zhangchy 2011-09-19
	 * 
	 * 新增了查询条件
	 * 
	 * @param current_user
	 * @param map_name
	 * @param cityId
	 * @param vendorId
	 * @param deviceModelId
	 * @param devicetypeId
	 * @return
	 */
	public List<Map> queryAllDigitMap(UserRes current_user,String map_name, 
			String cityId, String vendorId, String deviceModelId, String deviceTypeId) 
	{
		logger.debug("queryAllDigitMap({},{},{},{},{},{})", 
				new Object[]{current_user, map_name,cityId,vendorId,deviceModelId, deviceTypeId});
		
		String city_id = current_user.getCityId();
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.map_id,a.map_name,a.map_content,a.acc_oid,a.city_id,");
			sql.append("a.upd_time,a.vendor_id,a.device_model_id,a.devicetype_id,a.is_default,");
		}else{
			sql.append("select a.*,");
		}
		
		sql.append("b.city_name from gw_voip_digit_map a, tab_city b ");
		sql.append("where a.city_id = b.city_id " );
		// 如果是省中心用户，查看所有的
		if(!current_user.getCityId().equals("00")){
			sql.append("and a.city_id in " + getSameCities(city_id));
		}
		
		// 模板名称
		if(!StringUtil.IsEmpty(map_name)){
			sql.append("  and a.map_name like '%"+map_name+"%' ");
		}
		// 设备属地
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)){
			sql.append("  and a.city_id = '"+cityId+"' ");
		}
		// 厂商ID
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append("  and a.vendor_id = '"+vendorId+"' ");
		}
		// 型号ID
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append("  and a.device_model_id = '"+deviceModelId+"'");
		}
		// 版本ID
		if(!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){
			sql.append("  and a.devicetype_id  = "+deviceTypeId);
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		
		List<Map> list = jt.query(psql.getSQL(), new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("map_id", String.valueOf(rs.getInt("map_id")));
				map.put("map_name", rs.getString("map_name"));
				map.put("map_content", rs.getString("map_content"));
				map.put("acc_oid", String.valueOf(rs.getInt("acc_oid")));
				map.put("city_id", rs.getString("city_id"));
				map.put("city_name", rs.getString("city_name"));
				
				// 操作时间
				try{
					long upd_time = StringUtil.getLongValue(rs.getString("upd_time"));
					DateTimeUtil dt = new DateTimeUtil(upd_time * 1000);
					map.put("upd_time", dt.getLongDate());
				}catch (NumberFormatException e){
					map.put("upd_time", "");
				}catch (Exception e){
					map.put("upd_time", "");
				}
				
				String vendor_id = rs.getString("vendor_id");
				map.put("vendor_id", vendor_id);
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}else{
					map.put("vendor_add", "");
				}
				
				String device_model_id = rs.getString("device_model_id");
				map.put("device_model_id", device_model_id);
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model)){
					map.put("device_model", device_model);
				}else{
					map.put("device_model", "");
				}
				
				String devicetype_id = rs.getString("devicetype_id");
				map.put("devicetype_id", devicetype_id);
				String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
				if (false == StringUtil.IsEmpty(softwareversion)){
					map.put("softwareversion", softwareversion);
				}else{
					map.put("softwareversion", "");
				}
				map.put("is_default", rs.getString("is_default"));

				return map;
			}
			
		});
		
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		
		return list;
	}
	
	public String getSameCities(String city_id)
	{
		logger.debug("getSameCities({})", new Object[]{city_id});
		
		List<String> list = new ArrayList<String>();
		// 当前属地
		list.add(city_id);
		// 上级属地
		list.addAll(getAllParentCity(city_id));
		// 下级属地
		list.addAll(getAllChildCity(city_id));
		
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for(int i=0; i<list.size(); i++)
		{
			sb.append("'" + list.get(i) + "'");
			sb.append(",");
		}
		String str = sb.substring(0, sb.length() - 1);
		str = str + ")";
		
		logger.debug("sameCities: " + str);
		return str;
	}
	
	/**
	 * 获取父级地市
	 * @param city_id
	 * @return
	 */
	public String getParentCity(String city_id)
	{
		String sql = "select parent_id from tab_city where city_id='" + city_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		Map map = jt.queryForMap(psql.getSQL());
		return (String)map.get("parent_id");
	}
	
	/**
	 * 获取全部的父级地市,包括上级的上级地市
	 * @param city_id
	 * @return
	 */
	public List<String> getAllParentCity(String city_id)
	{
		List<String> list = new ArrayList<String>();
		
		// 上一级
		String parent_id = getParentCity(city_id);
		
		// 如果直接是省中心
		if("-1".equals(parent_id))
		{
			return list;
		}
		
		list.add(parent_id);
		
		// 如果上一级直接是省中心，那么不需要递归向上查询
		if("00".equals(parent_id))
		{
			return list;
		}
		else
		{
			list.addAll(getAllParentCity(parent_id));
		}
		
		return list;
	}
	
	/**
	 * 获取子级地市
	 * @param city_id
	 * @return
	 */
	public List<String> getChildCity(String city_id)
	{
		String sql = "select city_id from tab_city where parent_id='" + city_id + "'";
		PrepareSQL psql = new PrepareSQL(sql); 
		List<Map> list = jt.queryForList(psql.getSQL());
		return getListFromMap(list);
	}
	
	/**
	 * 获取全部的子级地市,包括下级的下级地市
	 * @param city_id
	 * @return
	 */
	public List<String> getAllChildCity(String city_id)
	{
		List<String> list = new ArrayList<String>();
		
		// 第一级子地市
		List<String> childList = getChildCity(city_id);
		
		String temp = "";
		for(int i=0; i<childList.size(); i++)
		{
			temp = childList.get(i);
			list.add(temp);
			
			// 如果有再下一级地市
			if(getChildCity(temp).size() != 0)
			{
				list.addAll(getAllChildCity(temp));
			}
		}
		
		return list;
	}
	
	/**
	 * 工具方法转换
	 * @param map
	 * @return
	 */
	public List<String> getListFromMap(List<Map> listMap)
	{
		// 查询字段
		String field = "city_id";
		List<String> list = new ArrayList<String>();
		String temp = "";
		for(int i=0; i<listMap.size(); i++)
		{
			temp = (String)listMap.get(i).get(field);
			if(!StringUtil.IsEmpty(temp))
			{
				list.add(temp);
			}
			else
			{
				list.add("");
			}
		}
		return list;
	}
	
	public void addDigitMap(String map_name, String map_content, UserRes user,
			String cityId, String vendorId, String deviceModelId, 
			String deviceTypeId,String is_default) 
	{
		logger.debug("addDigitMap({},{},{},{},{},{},{})", 
				new Object[]{map_name,map_content,user,cityId, vendorId, 
				deviceModelId, deviceTypeId,is_default});
		String sql = "";
		PrepareSQL psql = null;
		
		if(!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId))
		{
			sql = "insert into gw_voip_digit_map(map_id,map_name,map_content,acc_oid,city_id, vendor_id, device_model_id, devicetype_id,is_default) values(?,?,?,?,?,?,?,?,?)";
			psql = new PrepareSQL(sql);
			psql.setInt(1, getMaxId());
			psql.setString(2, map_name);
			psql.setString(3, map_content);
			psql.setLong(4, user.getUser().getId());
			psql.setString(5, cityId);
			psql.setString(6,vendorId);
			psql.setString(7, deviceModelId);
			psql.setLong(8, new Long(deviceTypeId));
			psql.setLong(9, new Long(is_default));
		}else{
			sql = "insert into gw_voip_digit_map(map_id,map_name,map_content,acc_oid,city_id, vendor_id, device_model_id, devicetype_id,is_default) " +
				  "values("+getMaxId()+",'"+map_name+"','"+map_content+"',"+user.getUser().getId()+",'"+cityId+"','"+vendorId+"','"+deviceModelId+"', null,"+is_default+")";
			psql = new PrepareSQL(sql);
		}
		
		jt.update(psql.getSQL());
	}
	
	public int getMaxId()
	{
		logger.debug("getMaxId()");
		String sql = "select max(map_id) as maxId from gw_voip_digit_map";
		PrepareSQL psql = new PrepareSQL(sql); 
		int maxId = 0;
		maxId = jt.queryForInt(psql.getSQL());
		maxId++;
		return maxId <= 0 ? 1 : maxId;
	}

	public Map<String,String> getDigitMapById(String map_id) 
	{
		logger.debug("getDigitMapById({})", new Object[]{map_id});
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select a.map_name,a.city_id,a.map_content,b.city_name ");
		}else{
			psql.append("select a.*,b.city_name ");
		}
		psql.append("from gw_voip_digit_map a, tab_city b ");
		psql.append("where a.city_id = b.city_id and a.map_id=" + map_id);
		return jt.queryForMap(psql.getSQL());
	}

	public void deleteDigitMapById(String map_id) 
	{
		logger.debug("deleteDigitMapById({})", new Object[]{map_id});
		String sql = "delete from gw_voip_digit_map where map_id=" + map_id;
		PrepareSQL psql = new PrepareSQL(sql); 
		jt.update(psql.getSQL());
	}

	/**
	 * 更新数图模板
	 * 
	 * @param map_id
	 * @param map_name
	 * @param map_content
	 * @param cityId
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 */
	public void update(String map_id, String map_name, String map_content,
			String cityId, String vendorId, String deviceModelId, 
			String deviceTypeId,String is_default) 
	{
		String sql = "";
		PrepareSQL psql = null;
		
		if(!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){
			sql = "update gw_voip_digit_map set map_name=?, map_content=?,city_id=?, vendor_id=?, device_model_id=?, devicetype_id=?,is_default=? where map_id=" + map_id;
			psql = new PrepareSQL(sql);
			psql.setString(1, map_name);
			psql.setString(2, map_content);
			psql.setString(3, cityId);
			psql.setString(4, vendorId);
			psql.setString(5, deviceModelId);
			psql.setLong(6, new Long(deviceTypeId));
			psql.setLong(7, new Long(is_default));
		}else{
			sql = "update gw_voip_digit_map set map_name='"+map_name+"', map_content='"+map_content+"',city_id='"+cityId+"', vendor_id='"+vendorId+"', device_model_id='"+deviceModelId+"', devicetype_id=null,is_default ="+is_default+" where map_id=" + map_id;
			psql = new PrepareSQL(sql);
		}
		jt.update(psql.getSQL());
	}

	/**
	 * 根据选择的设备展示适合的数图模板来供配置
	 * @param device_id
	 * @return
	 */
	public List<Map> queryForConfigMap(String device_id, String city_id) {
		logger.debug("queryForConfigMap({})", device_id);
		String sql = "select devicetype_id from tab_gw_device where device_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, device_id);
		// 查询devicetype_id
		String devicetype_id = StringUtil.getStringValue(queryForMap(psql.getSQL()).get("devicetype_id"));
		
		String sql2=null;
		if(DBUtil.GetDB()==3){
			sql2="select a.vendor_id,a.device_model_id,a.devicetype_id,a.city_id,a.map_id,a.map_name ";
		}else{
			sql2="select a.* ";
		}
		// 根据设备的地市，厂家，型号查询模板
		sql2 += 	" from gw_voip_digit_map a, tab_gw_device b where a.vendor_id=b.vendor_id and a.device_model_id=b.device_model_id" +
				" and b.device_id=? and a.city_id in (" + StringUtils.weave(CityDAO.getAllPcityIdByCityId(city_id)) +")";
		PrepareSQL psql2 = new PrepareSQL(sql2);
		psql2.setString(1, device_id);
		List<Map> list = jt.queryForList(psql2.getSQL());
		List<Map> result = new ArrayList<Map>();
		List<Map> result2 = new ArrayList<Map>();
		if(list == null)
		{
			return result;
		}
		else if(list.isEmpty())
		{
			return result;
		}
		else	
		{
			Map map = null;
			String devicetype_id_temp = null;
			for(int i=0; i<list.size(); i++)
			{
				map = list.get(i);
				devicetype_id_temp = StringUtil.getStringValue(map.get("devicetype_id"));
				{
					// 符合devicetype_id，加入记录
					if(!StringUtil.IsEmpty(devicetype_id_temp) && devicetype_id_temp.equals(devicetype_id))
					{
						result.add(map);
					}
					// devicetype_id为null的记录
					if(StringUtil.IsEmpty(devicetype_id_temp))
					{
						result2.add(map);
					}
				}
			}
		}
		if(result.size() == 0)
		{
			return result2;
		}
		else
			return result;
	}
	
	
	/**
	 * 查询符合搜索条件的数图模板
	 * 
	 * @param cityId
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @return
	 */
	public List<Map> queryForConfigAllMap(String cityId, String vendorId, String deviceModelId, String deviceTypeId){
		
		logger.debug("queryForConfigAllMap({},{},{},{})", new Object[]{cityId, vendorId, deviceModelId, deviceTypeId});
		
		StringBuffer sBuffer = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sBuffer.append("select a.vendor_id,a.device_model_id,a.devicetype_id,a.city_id,a.map_id,a.map_name ");
		}else{
			sBuffer.append("select a.* ");
		}
		// 根据设备的地市，厂家，型号查询模板
		sBuffer.append("from gw_voip_digit_map a  where 1=1 ");
		
		// 设备属地ID
		if(!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) 
				&& !"-1".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sBuffer.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		
		// 设备厂商ID
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sBuffer.append(" and a.vendor_id = '"+vendorId+"' ");
		}
		
		// 设备型号ID
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sBuffer.append(" and a.device_model_id = '"+deviceModelId+"' ");
		}
		
		// 设备版本ID
		if(!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){
			sBuffer.append(" and a.devicetype_id = "+deviceTypeId+" ");
		}
		
		PrepareSQL psql2 = new PrepareSQL(sBuffer.toString());
		
		List<Map> list = jt.queryForList(psql2.getSQL());
		
		List<Map> result = new ArrayList<Map>();
		
		if(null == list || list.isEmpty()){
			return result;
		}else{
			Map map = null;
			for(int i=0; i<list.size(); i++){
				map = list.get(i);
				result.add(map);
			}
		}
		return result;
	}
	

	/**
	 * 入数图配置下发设备记录表
	 * @param device_id
	 * @parm map_id
	 */
	public void addDigitMapRecord(String device_id, String map_id) {
		logger.debug("addDigitMapRecord({})", map_id);
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) as num from gw_voip_digit_device where device_id=? ");
		}else{
			psql.append("select count(1) as num from gw_voip_digit_device where device_id=? ");
		}
		psql.setString(1, device_id);
		int num = jt.queryForInt(psql.getSQL());
		boolean hasRec = true;
		if(num <= 0)
			hasRec = false;
		
		// 如果之前有记录
		if(hasRec)
		{
			PrepareSQL psql2 = new PrepareSQL("update gw_voip_digit_device set enable= -1 where device_id=?");
			psql2.setString(1, device_id);
			jt.update(psql2.getSQL());
		}
		
		// 插入新记录
		PrepareSQL psql3 = new PrepareSQL("insert into gw_voip_digit_device(device_id, task_id,tasktime,starttime,endtime,map_id,enable,result_id)" +
				" values(?,-1,?,null,null,?,1,null)");
		
		psql3.setString(1, device_id);
		psql3.setLong(2, System.currentTimeMillis()/1000);
		psql3.setInt(3, Integer.parseInt(map_id));
		jt.execute(psql3.getSQL());
	}
	
	
	/**
	 * 批量设备下发数图配置时 入 数图任务表（gw_voip_digit_task）
	 */
	public Long addGwVoipDigitTask() {
		
		logger.debug("addGwVoipDigitTask()");
		
		long task_id = System.currentTimeMillis()/1000;
		
		try {
			PrepareSQL psql = new PrepareSQL("insert into gw_voip_digit_task(task_id, task_name, task_type) values(?,?,?)");
			
			psql.setLong(1, task_id);
			psql.setString(2, "批量选定范围数图配置下发");
			psql.setLong(3, 2);  // task_type = 2 表示：批量选定范围数图配置下发
			
			jt.execute(psql.getSQL());
			
			return task_id;
		} catch (Exception e) {
			logger.error("insert into gw_voip_digit_task failed!");
			task_id = -1;
			return task_id;
		}
	}
	
	/**
	 * 
	 * 批量设备下发数图配置时 入 数图任务表（gw_voip_digit_task）
	 * 数图配置下发设备记录表
	 * 
	 * @param task_id
	 * @param deviceIds
	 * @param map_id
	 */
	public List<String> addDigitTaskAndDigitDevice(String deviceIds, String map_id, String taskName) {
		
		logger.debug("addDigitTaskAndDigitDevice({},{})", new Object[]{deviceIds, map_id});
		
		List<String> sqlList = new ArrayList<String>();
		
		long task_id = System.currentTimeMillis()/1000;
		StringBuilder sql_1 = new StringBuilder();
		sql_1.append("insert into gw_voip_digit_task(task_id, task_name, task_type) ");
		sql_1.append(" values("+task_id+",'"+taskName+"',2)");    // task_type = 2 表示：批量选定范围数图配置下发
		PrepareSQL psql = new PrepareSQL(sql_1.toString());
		psql.getSQL();
		sqlList.add(sql_1.toString());
		
		String [] deviceIdArray = deviceIds.split("\\|");
		
		for(int i = 0; i < deviceIdArray.length; i++){
			StringBuilder sql_2 = new StringBuilder();
			sql_2.append("update gw_voip_digit_device set enable= -1 where device_id = ");
			sql_2.append("'"+deviceIdArray[i]+"'");
			psql = new PrepareSQL(sql_2.toString());
			psql.getSQL();
			sqlList.add(sql_2.toString());
			
			// 插入新记录
			StringBuilder sql_3 = new StringBuilder();
			sql_3.append("insert into gw_voip_digit_device(device_id, task_id,tasktime,map_id,enable) ");
			sql_3.append(" values('"+deviceIdArray[i]+"'," + task_id + ","+ System.currentTimeMillis()/1000 +","+ Integer.parseInt(map_id) +",1)");
			psql = new PrepareSQL(sql_3.toString());
			psql.getSQL();
			sqlList.add(sql_3.toString());
		}
		
		return sqlList;
	}
	
	/**
	 * 根据map_id查询map_content
	 * @param map_id
	 * @return
	 */
	public String getDigitMapContentById(String map_id) 
	{
		String sql = "select map_content from gw_voip_digit_map where map_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setInt(1, Integer.parseInt(map_id));
		Map map = queryForMap(psql.getSQL());
		return StringUtil.getStringValue(map.get("map_content"));
	}
	
	/**
	 * 查询设备列表(关联设备状态表查询)
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDeviceByLikeStatus(long areaId,String cityId,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber,String onlineStatus)
	{
		logger.debug("queryDeviceByLikeStatusCount({},{},{},{},{},{},{},{})",
				new Object[]{areaId,cityId,vendorId,deviceModelId,devicetypeId,
				bindType,deviceSerialnumber,onlineStatus});
		
		PrepareSQL pSQL = new PrepareSQL();
//		pSQL.setSQL("select a.*,b.vendor_add,c.device_model,d.softwareversion 
//		from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e 
//		where a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id 
//		and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		
		pSQL.setSQL("select a.device_id,a.device_serialnumber,a.vendor_id," +
				"a.device_model_id,a.devicetype_id,a.complete_time " +
				"from tab_gw_device a, gw_devicestatus e " +
				"where a.device_id = e.device_id and a.device_status =1");
		
		if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
			pSQL.appendAndNumber("e.online_status",PrepareSQL.EQUEAL, onlineStatus);
		}
		
		if(!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) 
				&& !"-1".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		
		if(!StringUtil.IsEmpty(vendorId) && !"null".equals(vendorId) 
				&& !"-1".equals(vendorId)){
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		
		if(!StringUtil.IsEmpty(deviceModelId) && !"null".equals(deviceModelId) 
				&& !"".equals(deviceModelId) && !"-1".equals(deviceModelId)){
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		
		if(!StringUtil.IsEmpty(devicetypeId) && !"null".equals(devicetypeId) 
				&& !"-1".equals(devicetypeId)){
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
		}
		
		if(!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType)){
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
		}
		
		if(!StringUtil.IsEmpty(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)){
			if(deviceSerialnumber.length()>5){
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length()-6, deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}

		pSQL.append(" order by a.complete_time");
		
		return jt.queryForList(pSQL.toString());
	}
	
	
	
	
	/**
	 * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDevice(long areaId,String cityId,String vendorId,
			String deviceModelId,String devicetypeId,String bindType, String deviceSerialnumber)
	{
		logger.debug("queryDevice({},{},{},{},{},{},{})", 
				new Object[]{areaId,cityId,vendorId,deviceModelId,devicetypeId,
				bindType,deviceSerialnumber});
		
		PrepareSQL pSQL = new PrepareSQL();
//		pSQL.setSQL("select a.*,b.vendor_add,c.device_model,d.softwareversion 
//		from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d 
//		where a.device_status =1 and  a.vendor_id=b.vendor_id 
//		and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		
		pSQL.setSQL("select a.device_id,a.device_serialnumber,a.vendor_id," +
				"a.device_model_id,a.devicetype_id,a.complete_time " +
				"from tab_gw_device a where 1=1 and a.device_status=1 ");
		
		if(!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) 
				&& !"-1".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		
		if(!StringUtil.IsEmpty(vendorId) && !"null".equals(vendorId) 
				&& !"-1".equals(vendorId)){
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		
		if(!StringUtil.IsEmpty(deviceModelId) && !"null".equals(deviceModelId) 
				&& !"-1".equals(deviceModelId)){
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		
		if(!StringUtil.IsEmpty(devicetypeId) && !"null".equals(devicetypeId) 
				&& !"-1".equals(devicetypeId)){
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
		}
		
		if(!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType)){
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
		}
		
		if(!StringUtil.IsEmpty(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)){
			if(deviceSerialnumber.length()>5){
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length()-6, deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}
		
		pSQL.append(" order by a.complete_time");

		return jt.queryForList(pSQL.toString());
	}
	
	
	public int queryIsDefault(String vendorId,String deviceModelId,String is_default)
	{
		PrepareSQL pSQL = new PrepareSQL();

		pSQL.setSQL("select count(*) from  gw_voip_digit_map a where 1=1 ");
		if(!StringUtil.IsEmpty(vendorId) && !"null".equals(vendorId) 
				&& !"-1".equals(vendorId)){
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"null".equals(deviceModelId) 
				&& !"-1".equals(deviceModelId)){
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		
		if(!StringUtil.IsEmpty(is_default) && !"null".equals(is_default) 
				&& !"-1".equals(is_default)){
			pSQL.appendAndNumber("a.is_default", PrepareSQL.EQUEAL, is_default);
		}
		return jt.queryForInt(pSQL.toString());
	}
	
}
