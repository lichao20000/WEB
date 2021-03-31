package com.linkage.module.gwms.config.dao;

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
 * 
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-12-27
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class CountMulticastBatchDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(CountMulticastBatchDAO.class);

	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> devicetypeMap = null;
	private ArrayList<String> citys=CityDAO.getAllCityIdList();
	
	/**
	 * 查询列表
	 * @param
	 * @param current_user
	 * @param map_name
	 * @param cityId
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cpe_allocatedstatus 
	 * @return
	 */
	public List<Map> queryMulticastBatchList(UserRes current_user,String device_serialnumber,String cityId,
			String vendorId,String deviceModelId,String deviceTypeId,
			int curPage_splitPage,int num_splitPage, String cpe_allocatedstatus) 
	{
		logger.debug("queryMulticastBatchList({},{},{},{},{},{})", 
				new Object[]{current_user,device_serialnumber,cityId,vendorId,deviceModelId,deviceTypeId});
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select c.acc_oid,c.city_id,c.user_id,c.device_id,");
			sql.append("c.device_serialnumber,c.settime,c.status,");
		}else{
			sql.append("select c.*,");
		}
		sql.append("b.city_name,a.vendor_id,a.device_model_id,a.devicetype_id,d.username ");
		sql.append("from tab_gw_device a,tab_city b,tab_setmulticast_dev c,tab_hgwcustomer d ");
		sql.append("where a.device_id = c.device_id and a.city_id=b.city_id and c.user_id=d.user_id " );

		if(!current_user.getCityId().equals("00")){
			sql.append(" and a.city_id in"+getSameCities(current_user.getCityId()));
		}
		
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)){
			sql.append(" and a.city_id='"+cityId+"'");
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append(" and a.vendor_id='"+vendorId+"'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append(" and a.device_model_id='"+deviceModelId+"'");
		}
		if(!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){
			sql.append(" and a.devicetype_id="+deviceTypeId);
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			sql.append(" and c.device_serialnumber like '%"+device_serialnumber+"'");
		}
		// 是否绑定
		if(!"-1".equals(cpe_allocatedstatus) && !StringUtil.IsEmpty(cpe_allocatedstatus)){
			sql.append(" and a.cpe_allocatedstatus="+cpe_allocatedstatus);
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException 
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("acc_oid", String.valueOf(rs.getInt("acc_oid")));
				map.put("city_id", rs.getString("city_id"));
				map.put("city_name", rs.getString("city_name"));
				map.put("loid", rs.getString("username"));
				map.put("user_id", rs.getString("user_id"));
				map.put("device_id", rs.getString("device_id"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				
				// 操作时间
				try{
					long settime = StringUtil.getLongValue(rs.getString("settime"));
					DateTimeUtil dt = new DateTimeUtil(settime * 1000);
					map.put("setTime", dt.getLongDate());
				}catch (Exception e){
					map.put("setTime", "");
				}
				int status = StringUtil.getIntegerValue(rs.getString("status"));
				if (1 == status){
					map.put("status", "成功");
				}else if (0 == status){
					map.put("status", "未做");
				}else{
					map.put("status", "失败");
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
				return map;
			}
		});
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		
		return list;
	}

	/**
	 * 统计数量
	 * @param
	 * @param current_user
	 * @param devSn
	 * @param cityId
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @param cpe_allocatedstatus 
	 * @return
	 */
	public int countMulticastBatchList(UserRes current_user,String device_serialnumber,String cityId,
			String vendorId, String deviceModelId,String deviceTypeId, String cpe_allocatedstatus) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_gw_device a,tab_city b,tab_setmulticast_dev c,tab_hgwcustomer d ");
		sql.append("where a.device_id=c.device_id and a.city_id=b.city_id and c.user_id=d.user_id ");
		
		if(!current_user.getCityId().equals("00")){
		 // 当前登陆用户属地权限下的所有模板和所有递归父属地(最终到省中心)权限下的模板
			sql.append(" and a.city_id in" + getSameCities(current_user.getCityId())); 
		}
		
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)){
			sql.append(" and a.city_id='"+cityId+"'");
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append(" and a.vendor_id='"+vendorId+"'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append(" and a.device_model_id='"+deviceModelId+"'");
		}
		if(!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){
			sql.append(" and a.devicetype_id="+deviceTypeId);
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			sql.append(" and c.device_serialnumber  like '%"+device_serialnumber+"'");
		}
		// 是否绑定
		if(!"-1".equals(cpe_allocatedstatus) && !StringUtil.IsEmpty(cpe_allocatedstatus)){
			sql.append(" and a.cpe_allocatedstatus  = "+cpe_allocatedstatus);
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForInt(psql.getSQL());
	}
	
	private String getSameCities(String city_id)
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
		sb.append(" (");
		for(int i=0; i<list.size(); i++)
		{
			sb.append("'" + list.get(i) + "',");
		}
		String str = sb.substring(0, sb.length() - 1)+") ";
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
	private List<String> getListFromMap(List<Map> listMap)
	{
		List<String> list = new ArrayList<String>();
		for(Map map:listMap)
		{
			list.add(StringUtil.getStringValue(map,"city_id",""));
		}
		return list;
	}
	
	public List<Map> getMulticastBatchExcel(UserRes current_user,String device_serialnumber,String cityId,
			String vendorId, String deviceModelId,String deviceTypeId, String cpe_allocatedstatus) 
	{
		logger.debug("queryMulticastBatchList({},{},{},{},{},{})", 
				new Object[]{current_user,device_serialnumber,cityId,vendorId,deviceModelId,deviceTypeId});
		
		String city_id = current_user.getCityId();
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select c.acc_oid,c.user_id,c.device_serialnumber,c.settime,c.status,");
			sql.append("a.city_id,a.vendor_id,a.device_model_id,a.devicetype_id,d.username ");
			sql.append("from tab_gw_device a,tab_setmulticast_dev c,tab_hgwcustomer d ");
			sql.append("where a.device_id=c.device_id and c.user_id=d.user_id ");
		}else{
			sql.append("select c.*,b.city_name,a.vendor_id,a.device_model_id,a.devicetype_id,d.username ");
			sql.append("from tab_gw_device a,tab_city b,tab_setmulticast_dev c,tab_hgwcustomer d ");
			sql.append("where a.device_id=c.device_id and a.city_id=b.city_id and c.user_id=d.user_id ");
		}
		
		if(!current_user.getCityId().equals("00")){
			sql.append(" and a.city_id in" + getSameCities(city_id));
		}
		
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)){
			sql.append("  and a.city_id = '"+cityId+"' ");
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append("  and a.vendor_id = '"+vendorId+"' ");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append("  and a.device_model_id = '"+deviceModelId+"'");
		}
		if(!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){
			sql.append("  and a.devicetype_id  = "+deviceTypeId);
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			sql.append("  and c.device_serialnumber  like '%"+device_serialnumber+"'");
		}
		// 是否绑定
		if(!"-1".equals(cpe_allocatedstatus) && !StringUtil.IsEmpty(cpe_allocatedstatus)){
			sql.append("  and a.cpe_allocatedstatus  = "+cpe_allocatedstatus);
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		
		List<Map> list;
		if(DBUtil.GetDB()==3)
		{
			List<Map> l=jt.queryForList(psql.getSQL());
			list=new ArrayList<Map>();
			if(l!=null && !l.isEmpty()){
				for(Map m:l)
				{
					String cityid=StringUtil.getStringValue(m, "city_id");
					if(!citys.contains(cityid)){
						continue;
					}
					
					Map<String, String> map = new HashMap<String, String>();
					map.put("acc_oid", StringUtil.getStringValue(m, "acc_oid"));
					map.put("city_id", cityid);
					map.put("city_name", CityDAO.getCityName(cityid));
					map.put("loid", StringUtil.getStringValue(m, "username"));
					map.put("user_id", StringUtil.getStringValue(m, "user_id"));
					map.put("device_serialnumber", StringUtil.getStringValue(m, "device_serialnumber"));
					
					// 操作时间
					try{
						long settime = StringUtil.getLongValue(m, "settime");
						DateTimeUtil dt = new DateTimeUtil(settime * 1000);
						map.put("setTime", dt.getLongDate());
					}catch (Exception e){
						map.put("setTime", "");
					}
					int status = StringUtil.getIntValue(m, "status");
					if (1 == status){
						map.put("status", "成功");
					}else if (0 == status){
						map.put("status", "未做");
					}else{
						map.put("status", "失败");
					}
					
					String vendor_id = StringUtil.getStringValue(m, "vendor_id");
					map.put("vendor_id", vendor_id);
					String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
					if (false == StringUtil.IsEmpty(vendor_add)){
						map.put("vendor_add", vendor_add);
					}else{
						map.put("vendor_add", "");
					}
					
					String device_model_id = StringUtil.getStringValue(m, "device_model_id");
					map.put("device_model_id", device_model_id);
					String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
					if (false == StringUtil.IsEmpty(device_model)){
						map.put("device_model", device_model);
					}else{
						map.put("device_model", "");
					}
					
					String devicetype_id = StringUtil.getStringValue(m, "devicetype_id");
					map.put("devicetype_id", devicetype_id);
					String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
					if (false == StringUtil.IsEmpty(softwareversion)){
						map.put("softwareversion", softwareversion);
					}else{
						map.put("softwareversion", "");
					}
					list.add(map);
				}
				l.clear();
				l=null;
			}
		}
		else
		{
			list = jt.query(psql.getSQL(), new RowMapper(){
				public Object mapRow(ResultSet rs, int arg1) throws SQLException 
				{
					Map<String, String> map = new HashMap<String, String>();
					map.put("acc_oid", String.valueOf(rs.getInt("acc_oid")));
					map.put("city_id", rs.getString("city_id"));
					map.put("city_name", rs.getString("city_name"));
					map.put("loid", rs.getString("username"));
					map.put("user_id", rs.getString("user_id"));
					map.put("device_serialnumber", rs.getString("device_serialnumber"));
					
					// 操作时间
					try{
						long settime = StringUtil.getLongValue(rs.getString("settime"));
						DateTimeUtil dt = new DateTimeUtil(settime * 1000);
						map.put("setTime", dt.getLongDate());
					}catch (Exception e){
						map.put("setTime", "");
					}
					int status = StringUtil.getIntegerValue(rs.getString("status"));
					if (1 == status){
						map.put("status", "成功");
					}else if (0 == status){
						map.put("status", "未做");
					}else{
						map.put("status", "失败");
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
					return map;
				}
			});
		}
		
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		
		return list;
	}
	
	public List<Map> queryMulticastDownwardsList( UserRes curUser ,String cityId,
			String vendorId,String deviceModelId)
	{
        logger.debug("queryMulticastDownwardsList({},{},{},{},{})", 
        		new Object[]{curUser, cityId, vendorId, deviceModelId});
		
        List<String> cityIdList = null;
        String userCity = curUser.getCityId();
		logger.debug("userCity:"+userCity);
		//获取子属地
		if(StringUtil.IsEmpty(cityId) || "-1".equals(cityId)){
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(userCity);
		}else{
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		}
		
		if(null == cityIdList || cityIdList.isEmpty() || cityIdList.size() < 1){
			logger.error("cityIdLlist is null");
			return null;
		}
		
		String sql = "";
        StringBuffer sb = null;
        List<Map> mapList = new ArrayList();
		
		//按属地统计
		
		if(DBUtil.GetDB()==3){
			sql = "select a.city_id,c.status,c.snooping_enable,c.multicast_vlan "
					+"from tab_gw_device a,tab_city b,tab_result_multicast_vlan c "
					+"where a.device_id=c.device_id ";
		}else{
			sql = "select b.city_name,c.status,c.snooping_enable,c.multicast_vlan "
					+"from tab_gw_device a,tab_city b,tab_result_multicast_vlan c "
					+"where a.device_id=c.device_id and a.city_id=b.city_id ";
		}
		
		int gatherTotalCounts = 0;
		int openTotailCounts = 0;
		int closeTotalCounts = 0;
		int gatherFailTotalCounts = 0;
		Map mapRes = null;
		boolean flag = false;
		for(String tmpCityId : cityIdList)
		{
			if(null == tmpCityId || "-1".equals(tmpCityId) || "null".equals(tmpCityId)){
				continue;
			}
			
			if(DBUtil.GetDB()==3 && !citys.contains(tmpCityId)){
				continue;
			}
			
			sb = new StringBuffer();
			sb.append(sql);
			sb.append(" and a.city_id='"+tmpCityId+"'");
			if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
				sb.append(" and a.vendor_id='"+vendorId+"'");
			}
			
			if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
				sb.append(" and a.device_model_id='"+deviceModelId+"'");
			}

			PrepareSQL psql = new PrepareSQL(sb.toString());
			List<Map> list = jt.queryForList(psql.getSQL());
			if(null == list || list.isEmpty()){
				continue;
			}
			
			//结果处理
			flag = true;
			int openCounts = 0 ; //开启数量
			int closeCounts = 0 ;//未开启数量
			int gatherFailCounts = 0; //采集失败数量
			int gatherSuccCounts = 0; //采集成功数量
			int total = list.size() ;
			String city_name = null;
			mapRes = new HashMap<String,String>();
			for(Map maps : list)
			{
				String status = String.valueOf(maps.get("status"));
				String snoopingEnb = String.valueOf(maps.get("snooping_enable"));
				String multiVlan = String.valueOf(maps.get("multicast_vlan"));

				if(!"1".equals(status) && !"0".equals(status)){
					gatherFailCounts = gatherFailCounts + 1;
				}else{
					gatherSuccCounts = gatherSuccCounts + 1;
					if("1".equals(snoopingEnb) && "4070".equals(multiVlan)){
						openCounts = openCounts +1;
					}else{
						closeCounts = closeCounts +1;
					}
				}
				
				if(StringUtil.IsEmpty(city_name)){
					if(DBUtil.GetDB()==3){
						city_name = CityDAO.getCityName(tmpCityId);
					}else{
						city_name = String.valueOf(maps.get("city_name"));
					}
				}
			}
			mapRes.put("cityId", tmpCityId);
			mapRes.put("cityName", city_name);
			mapRes.put("total", Integer.toString(total));
			mapRes.put("openCounts", Integer.toString(openCounts));
			mapRes.put("closeCounts", Integer.toString(closeCounts));
			mapRes.put("failCounts", Integer.toString(gatherFailCounts));
			mapRes.put("succCounts", Integer.toString(gatherSuccCounts));
			if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
				mapRes.put("vendorId", vendorId);
			}
			
			if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
				mapRes.put("deviceModelId", deviceModelId);
			}
			mapList.add(mapRes);
			
			gatherTotalCounts = gatherTotalCounts + total;
			openTotailCounts = openTotailCounts + openCounts;
			closeTotalCounts = closeTotalCounts + closeCounts;
			gatherFailTotalCounts = gatherFailTotalCounts + gatherFailCounts;
		}
		
		if(flag){
			mapRes = new HashMap<String,String>();
			mapRes.put("colName", "小计");
			mapRes.put("gatherTotalCounts", gatherTotalCounts);
			mapRes.put("openTotailCounts", openTotailCounts);
			mapRes.put("closeTotalCounts", closeTotalCounts);
			mapRes.put("gatherFailTotalCounts", gatherFailTotalCounts);
			if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
				mapRes.put("vendorId", vendorId);
			}
			
			if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
				mapRes.put("deviceModelId", deviceModelId);
			}
			if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)){
				mapRes.put("cityId", cityId);
			}
			mapList.add(mapRes);
		}
		return mapList; 
 	}
	
	/**
	 * 组播下移导出
	 * @param curUser
	 * @param cityId
	 * @param vendorId
	 * @param deviceModelId
	 * @return
	 */
	public List<Map> queryMulticastDownwardsListExcel( UserRes curUser,String cityId,
			String vendorId,String deviceModelId)
	{
        logger.debug("queryMulticastDownwardsList({},{},{},{},{})", 
        		new Object[]{curUser, cityId, vendorId, deviceModelId});
        
        List<String> cityIdList = null;
        String userCity = curUser.getCityId();
		if(StringUtil.IsEmpty(cityId) || "-1".equals(cityId)){
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(userCity);
		}else{
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		}
		
		if(null == cityIdList || cityIdList.isEmpty()){
			logger.error("cityIdLlist is null");
			return null;
		}
		logger.debug("cityList.size:"+cityIdList.size());
		
		String sql = "";
        StringBuffer sb = null;
        List<Map> mapList = new ArrayList();
		//按属地统计
		if(DBUtil.GetDB()==3){
			sql = "select a.city_id,c.status,c.snooping_enable,c.multicast_vlan "
					+"from tab_gw_device a,tab_result_multicast_vlan c "
					+"where a.device_id=c.device_id ";
		}else{
			sql = "select b.city_name,c.status,c.snooping_enable,c.multicast_vlan "
					+"from tab_gw_device a,tab_city b,tab_result_multicast_vlan c "
					+"where a.device_id=c.device_id and a.city_id=b.city_id ";
		}
		int gatherTotalCounts = 0;
		int openTotailCounts = 0;
		int closeTotalCounts = 0;
		int gatherFailTotalCounts = 0;
		Map mapRes = null;
		boolean flag = false;
		for(String tmpCityId : cityIdList)
		{
			if(null == tmpCityId || "-1".equals(tmpCityId) || "null".equals(tmpCityId)){
				continue;
			}
			if(DBUtil.GetDB()==3 && !citys.contains(tmpCityId)){
				continue;
			}
			mapRes = new HashMap<String,String>();
			sb = new StringBuffer();
			sb.append(sql);
			sb.append(" and a.city_id='"+tmpCityId+"'");
			if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
				sb.append(" and a.vendor_id='"+vendorId+"'");
			}
			
			if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
				sb.append(" and a.device_model_id='"+deviceModelId+"'");
			}

			//查询
			PrepareSQL psql = new PrepareSQL(sb.toString());
			List<Map> list = jt.queryForList(psql.getSQL());
			if(null==list || list.isEmpty()){
				continue;
			}
			
			//结果处理
			flag = true;
			int openCounts = 0 ; //开启数量
			int closeCounts = 0 ;//未开启数量
			int gatherFailCounts = 0; //采集失败数量
			int gatherSuccCounts = 0; //采集成功数量
			int total = list.size() ;
			String city_name = null;
			for(Map maps : list)
			{
				String status = String.valueOf(maps.get("status"));
				String snoopingEnb = String.valueOf(maps.get("snooping_enable"));
				String multiVlan = String.valueOf(maps.get("multicast_vlan"));
				logger.debug("status:"+status);
				if(!"1".equals(status) && !"0".equals(status)){
					gatherFailCounts = gatherFailCounts + 1;
				}else{
					gatherSuccCounts = gatherSuccCounts + 1;
					if("1".equals(snoopingEnb) && "4070".equals(multiVlan)){
						openCounts = openCounts +1;
					}else{
						closeCounts = closeCounts +1;
					}
				}
				
				if(StringUtil.IsEmpty(city_name)){
					if(DBUtil.GetDB()==3){
						city_name = CityDAO.getCityName(tmpCityId);
					}else{
						city_name = String.valueOf(maps.get("city_name"));
					}
				}
			}
			
			mapRes.put("cityName", city_name);
			mapRes.put("total", Integer.toString(total));
			mapRes.put("openCounts", Integer.toString(openCounts));
			mapRes.put("closeCounts", Integer.toString(closeCounts));
			mapRes.put("failCounts", Integer.toString(gatherFailCounts));
			mapRes.put("succCounts", Integer.toString(gatherSuccCounts));
			mapList.add(mapRes);
			
			gatherTotalCounts = gatherTotalCounts + total;
			openTotailCounts = openTotailCounts + openCounts;
			closeTotalCounts = closeTotalCounts + closeCounts;
			gatherFailTotalCounts = gatherFailTotalCounts + gatherFailCounts;
		}
		
		if(flag){
			mapRes = new HashMap<String,String>();
			mapRes.put("cityName", "小计");
			mapRes.put("total", gatherTotalCounts);
			mapRes.put("openCounts", openTotailCounts);
			mapRes.put("closeCounts", closeTotalCounts);
			mapRes.put("failCounts", gatherFailTotalCounts);
			mapList.add(mapRes);
		}
		return mapList; 
 	}
	
	
	public List<Map> multicastDownwardsDetail(UserRes curUser,String type, String cityId,
			String vendorId,String deviceModelId, int curPage_splitPage, int num_splitPage)
	{
		if(StringUtil.IsEmpty(cityId) || StringUtil.IsEmpty(type)){
			logger.error("multicastDownwardsDetail cityId or type is null");
			return null;
		}
		
		PrepareSQL sbf = new PrepareSQL();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		} */
		sbf.append("select a.vendor_id,a.device_model_id,a.devicetype_id,c.city_name,");
		sbf.append("a.device_serialnumber,b.gathertime,b.status,b.snooping_enable,b.multicast_vlan ");
		sbf.append("from tab_gw_device a,tab_result_multicast_vlan b,tab_city c ");
		sbf.append("where a.device_id=b.device_id and a.city_id=c.city_id ");
		
		List<String> cityIdList = null;
		if("all".equals(cityId)){
			cityId = "";
			String userCity = curUser.getCityId();
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(userCity);
		}else{
			cityIdList = new ArrayList<String>();
			cityIdList.add(cityId);
		}
		if(null != cityIdList && !cityIdList.isEmpty()){
			sbf.append(" and a.city_id in("+StringUtils.weave(cityIdList)+")");
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sbf.append(" and a.vendor_id='"+vendorId+"'");
		}
		
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sbf.append(" and a.device_model_id='"+deviceModelId+"'");
		}
		
		if("gatherCounts".equals(type)){
			//
		}else if("openCounts".equals(type)){
			sbf.append(" and (b.status=1 or b.status=0)");
			sbf.append(" and b.snooping_enable=1 and b.multicast_vlan=4070");
		}else if("closeCounts".equals(type)){
			sbf.append(" and (b.status=1 or b.status=0)");
			sbf.append(" and (b.snooping_enable!=1 or b.multicast_vlan!=4070)");
		}else if("failCounts".equals(type)){
			sbf.append(" and b.status!=1 and b.status!=0");
		}else{
			logger.error("type 不存在");
		}
		sbf.append(" order by a.city_id ");
		
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		
		List<Map> list = querySP(sbf.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException 
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("vendor_name", vendorMap.get(String.valueOf(rs.getInt("vendor_id"))));
				
				String device_model_id = rs.getString("device_model_id");
				map.put("device_model", StringUtil.getStringValue(deviceModelMap.get(device_model_id)));
				
				String devicetype_id = rs.getString("devicetype_id");
				map.put("software", StringUtil.getStringValue(devicetypeMap.get(devicetype_id)));
				
				map.put("city_name", rs.getString("city_name"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				
				try
				{
					long gathertime = StringUtil.getLongValue(rs.getString("gathertime"));
					DateTimeUtil dt = new DateTimeUtil(gathertime * 1000);
					map.put("gathertime", dt.getLongDate());
				}
				catch (Exception e)
				{
					map.put("gathertime", "");
				}
				
				String status = rs.getString("status");
				if(!"1".equals(status) && !"0".equals(status)){
					map.put("gatherRes", "采集失败");
				}else{
					int snoopingEnb = rs.getInt("snooping_enable");
					int multicast_vlan = rs.getInt("multicast_vlan");
					if(snoopingEnb == 1 && multicast_vlan == 4070){
						map.put("gatherRes", "开启");
					}else{
						map.put("gatherRes", "关闭");
					}
				}
				
				return map;
			}
		});
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		return list;
	}
	
	public int multicastDownwardsDetailCount(UserRes curUser,String type, 
			String vendorId,String deviceModelId,String cityId)
	{
		int num=0;
		if(StringUtil.IsEmpty(cityId) || StringUtil.IsEmpty(type)){
			logger.error("multicastDownwardsDetail cityId or type is null");
			return num;
		}
		
		PrepareSQL sbf = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			sbf.append("select a.city_id,count(*) cou ");
			sbf.append("from tab_gw_device a,tab_result_multicast_vlan b ");
			sbf.append("where a.device_id=b.device_id ");
		}else{
			sbf.append("select count(1) ");
			sbf.append("from tab_gw_device a,tab_result_multicast_vlan b,tab_city c ");
			sbf.append("where a.device_id=b.device_id and a.city_id=c.city_id ");
		}
		
		List<String> cityIdList = null;
		if("all".equals(cityId)){
			cityId = "";
			String userCity = curUser.getCityId();
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(userCity);
		}else{
			cityIdList = new ArrayList<String>();
			cityIdList.add(cityId);
		}
		
		if(null!=cityIdList && !cityIdList.isEmpty()){
			sbf.append(" and a.city_id in("+StringUtils.weave(cityIdList)+")");
		}
		
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sbf.append(" and a.vendor_id='"+vendorId+"'");
		}
		
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sbf.append(" and a.device_model_id='"+deviceModelId+"'");
		}
		
		if("gatherCounts".equals(type)){
			//
		}else if("openCounts".equals(type)){
			sbf.append(" and (b.status=1 or b.status=0)");
			sbf.append(" and b.snooping_enable=1 and b.multicast_vlan=4070");
		}else if("closeCounts".equals(type)){
			sbf.append(" and (b.status=1 or b.status=0)");
			sbf.append(" and (b.snooping_enable!=1 or b.multicast_vlan!=4070)");
		}else if("failCounts".equals(type)){
			sbf.append(" and b.status!=1 and b.status!=0");
		}else{
			logger.error("type 不存在");
		}
		
		if(DBUtil.GetDB()==3)
		{
			sbf.append(" group by a.city_id ");
			List list=jt.queryForList(sbf.getSQL());
			if(list!=null && !list.isEmpty()){
				for(int i=0;i<list.size();i++)
				{
					String city=StringUtil.getStringValue((Map)list.get(i),"city_id");
					int count=StringUtil.getIntValue((Map)list.get(i),"cou");
					
					if(citys.contains(city)){
						num+=count;
					}
					city=null;
					count=0;
				}
				citys=null;
			}
		}
		else
		{
			sbf.append(" order by a.city_id ");
			num=jt.queryForInt(sbf.getSQL());
		}
		return num;
	}
   
	public List<Map> multicastDownwardsDetailExcel(UserRes curUser,String type,
			String cityId,String vendorId,String deviceModelId)
	{
	   StringBuffer sbf = new StringBuffer();
		if(StringUtil.IsEmpty(cityId) || StringUtil.IsEmpty(type)){
			logger.error("multicastDownwardsDetailExcel cityId or type is null");
			return null;
		}
		
		if(DBUtil.GetDB()==3){
			sbf.append("select a.vendor_id,a.device_model_id,a.devicetype_id,a.city_id,a.device_serialnumber,");
			sbf.append("b.gathertime,b.status,b.snooping_enable,b.multicast_vlan ");
			sbf.append("from tab_gw_device a,tab_result_multicast_vlan b ");
			sbf.append("where a.device_id=b.device_id ");
		}else{
			sbf.append("select a.vendor_id,a.device_model_id,a.devicetype_id,c.city_name,");
			sbf.append("a.device_serialnumber,b.gathertime,b.status,b.snooping_enable,b.multicast_vlan ");
			sbf.append("from tab_gw_device a,tab_result_multicast_vlan b,tab_city c ");
			sbf.append("where a.device_id=b.device_id and a.city_id=c.city_id ");
		}
		
		List<String> cityIdList = null;
		if("all".equals(cityId)){
			cityId = "";
			String userCity = curUser.getCityId();
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(userCity);
		}else{
			cityIdList = new ArrayList<String>();
			cityIdList.add(cityId);
		}
		if(null != cityIdList && !cityIdList.isEmpty() && cityIdList.size() > 0){
			sbf.append("and a.city_id in("+StringUtils.weave(cityIdList)+") ");
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sbf.append("  and a.vendor_id = '"+vendorId+"' ");
		}
		
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sbf.append("  and a.device_model_id = '"+deviceModelId+"'");
		}
		
		if("gatherCounts".equals(type)){
			//
		}else
			if("openCounts".equals(type)){
				sbf.append(" and (b.status=1 or b.status=0) and b.snooping_enable=1 and b.multicast_vlan=4070 ");
			}else
				if("closeCounts".equals(type)){
					sbf.append(" and (b.status=1 or b.status=0) and (b.snooping_enable !=1 or b.multicast_vlan != 4070) ");
				}else
					if("failCounts".equals(type)){
						sbf.append(" and b.status != 1 and b.status != 0 ");
					}else{
						logger.error("type 不存在");
					}
		sbf.append(" order by a.city_id ");
		PrepareSQL psql = new PrepareSQL(sbf.toString());
		
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		
		List<Map> list=null;
		List<Map> returnList=new ArrayList<Map>();
		if(DBUtil.GetDB()==3)
		{
			list=jt.queryForList(psql.getSQL());
			
			if(list!=null && !list.isEmpty()){
				for(Map m:list)
				{
					Map<String, String> map = new HashMap<String, String>();
					String city_name=CityDAO.getCityName(StringUtil.getStringValue(m,"city_id"));
					if(StringUtil.IsEmpty(city_name)){
						continue;
					}
					map.put("city_name",city_name);
					
					map.put("vendor_name", vendorMap.get(StringUtil.getStringValue(m,"vendor_id")));
					
					String device_model_id = StringUtil.getStringValue(m,"device_model_id");
					map.put("device_model", StringUtil.getStringValue(deviceModelMap.get(device_model_id)));
					
					String devicetype_id = StringUtil.getStringValue(m,"devicetype_id");
					map.put("software", StringUtil.getStringValue(devicetypeMap.get(devicetype_id)));
					
					map.put("device_serialnumber", StringUtil.getStringValue(m,"device_serialnumber"));
					
					try
					{
						long gathertime = StringUtil.getLongValue(m,"gathertime");
						DateTimeUtil dt = new DateTimeUtil(gathertime * 1000);
						map.put("gathertime", dt.getLongDate());
					}
					catch (Exception e)
					{
						map.put("gathertime", "");
					}
					
					String status = StringUtil.getStringValue(m,"status");
					if(!"1".equals(status) && !"0".equals(status)){
						map.put("gatherRes", "采集失败");
					}else{
						int snoopingEnb = StringUtil.getIntValue(m,"snooping_enable");
						int multicast_vlan = StringUtil.getIntValue(m,"multicast_vlan");
						if(snoopingEnb == 1 && multicast_vlan == 4070){
							map.put("gatherRes", "开启");
						}else{
							map.put("gatherRes", "关闭");
						}
					}
					
					returnList.add(map);
				}
				
				list.clear();
				list=null;
				list=returnList;
			}
		}
		else
		{
			list = jt.query(psql.getSQL(), new RowMapper(){
				public Object mapRow(ResultSet rs, int arg1) throws SQLException 
				{
					Map<String, String> map = new HashMap<String, String>();
					map.put("city_name", rs.getString("city_name"));
					map.put("vendor_name", vendorMap.get(String.valueOf(rs.getInt("vendor_id"))));
					
					String device_model_id = rs.getString("device_model_id");
					map.put("device_model", StringUtil.getStringValue(deviceModelMap.get(device_model_id)));
					
					String devicetype_id = rs.getString("devicetype_id");
					map.put("software", StringUtil.getStringValue(devicetypeMap.get(devicetype_id)));
					
					map.put("device_serialnumber", rs.getString("device_serialnumber"));
					
					try
					{
						long gathertime = StringUtil.getLongValue(rs.getString("gathertime"));
						DateTimeUtil dt = new DateTimeUtil(gathertime * 1000);
						map.put("gathertime", dt.getLongDate());
					}
					catch (Exception e)
					{
						map.put("gathertime", "");
					}
					
					String status = rs.getString("status");
					if(!"1".equals(status) && !"0".equals(status)){
						map.put("gatherRes", "采集失败");
					}else{
						int snoopingEnb = rs.getInt("snooping_enable");
						int multicast_vlan = rs.getInt("multicast_vlan");
						if(snoopingEnb == 1 && multicast_vlan == 4070){
							map.put("gatherRes", "开启");
						}else{
							map.put("gatherRes", "关闭");
						}
					}
					
					return map;
				}
			});
		}
		
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		return list;
	}
}
