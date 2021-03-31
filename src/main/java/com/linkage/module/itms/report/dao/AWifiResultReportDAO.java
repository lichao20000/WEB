
package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 考核所有的用户
 * 
 * @author 王森博
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AWifiResultReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(AWifiResultReportDAO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;
	/**
	 * HashMap<vendor_id,vendor_add>
	 */
	private HashMap<String, String> vendorMap = null;
	/**
	 * HashMap<device_model_id,device_model>
	 */
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> devicetypeMap = null;

	/**
	 * <pre>
	 * 所有配置的
	 * 由于数据库数据量较大，两次查询数据库很耗时，故采用countResult方法来计算出总数
	 * deprecated
	 * </pre>
	 * @author wangsenbo
	 * @date May 19, 2010
	 * @param
	 * @return Map
	 * @see #countResult(String, String, String, String, String)
	 */
	public Map countAll(String starttime1, String endtime1, String cityId,
			String gw_type,String isSoftUp, Integer serviceId)
	{
		logger.debug("countAll({},{},{})", new Object[] { starttime1, endtime1, cityId });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		String tableName = "gw_serv_strategy_batch";
		if("1".equals(isSoftUp)){
			tableName = "gw_serv_strategy_soft";
		}
		if(DBUtil.GetDB()==3){
			sql.append("select b.city_id,count(*) as total ");
		}else{
			sql.append("select b.city_id,count(1) as total ");
		}
		sql.append("from " + tableName + " a,tab_gw_device b ");
		sql.append(" where a.device_id=b.device_id and a.service_id=2001 and a.is_last_one=1 ");
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.time<=").append(endtime1);
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and b.gw_type=").append(gw_type);
		}
		sql.append(" group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty()){
			for (int i = 0; i < list.size(); i++){
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), StringUtil
						.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}

//	public Map<String, Map<String,String>> countSucc(String starttime1, String endtime1, String cityId){
//		logger.debug("countSucc({},{},{})", new Object[] { starttime1, endtime1, cityId });
//		
//		StringBuffer sql = new StringBuffer();
//		sql.append("select b.city_id,c.is_mgr,count(1) as total 
//	from gw_serv_strategy a,tab_gw_device b,cpe_mgr c where a.device_id=b.device_id 
//	and a.device_id=c.device_id and a.service_id=5 and a.is_last_one=1 and a.status=100 and a.result_id=1 ");
//		if (false == StringUtil.IsEmpty(cityId)||"00".equals(cityId)){
//			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
//			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
//					.append(")");
//			cityIdList = null;
//		}
//		if (false == StringUtil.IsEmpty(starttime1)){
//			sql.append(" and a.time>=").append(starttime1);
//		}
//		if (false == StringUtil.IsEmpty(endtime1)){
//			sql.append(" and a.time<=").append(endtime1);
//		}
//		sql.append(" group by b.city_id,c.is_mgr");
//		List list = jt.queryForList(sql.toString());
//		Map<String, Map<String,String>> map = new HashMap<String, Map<String,String>>();
//		if (false == list.isEmpty()){
//			for (int i = 0; i < list.size(); i++){
//				Map rmap = (Map) list.get(i);
//				String city_id = StringUtil.getStringValue(rmap.get("city_id"));
//				Map tmap = map.get(city_id);
//				if(tmap==null){
//					tmap = new HashMap<String, String>();				
//				}
//				tmap.put(StringUtil.getStringValue(rmap.get("is_mgr")), StringUtil
//						.getStringValue(rmap.get("total")));
//				map.put(city_id, tmap);
//			}
//		}
//		return map;
//	}

	public List countResult(String tableName, String starttime1, String endtime1, 
			String cityId,String vendorId, String deviceModelId, String gw_type,
			String awifi_type, String isSoftUp)
	{
		logger.debug("countResult({},{},{})",new Object[] { starttime1, endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		// String tableName = "gw_serv_strategy_batch";
		
		if("1".equals(isSoftUp)){
			tableName = "gw_serv_strategy_soft";
		}
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select b.city_id,a.status,a.result_id,count(*) as total ");
		}else{
			sql.append("select b.city_id,a.status,a.result_id,count(1) as total ");
		}
		sql.append("from " + tableName + " a,tab_gw_device b ");
		if(LipossGlobals.inArea(Global.JSDX)){
			sql.append(",tab_wirelesst_task c ");
		}
		sql.append("where a.device_id=b.device_id and a.is_last_one=1 and a.service_id=2001 ");
		if(LipossGlobals.inArea(Global.JSDX)){
			sql.append(" and a.ids_task_id=c.task_id and c.wireless_type="+awifi_type+" ");
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.time<=").append(endtime1);
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and b.gw_type=").append(gw_type);
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append(" and b.vendor_id='").append(vendorId).append("'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append(" and b.device_model_id='").append(deviceModelId).append("'");
		}
		
		sql.append(" group by b.city_id,a.status,a.result_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	public List<Map> getDevList(String tableName, String gw_type,String awifi_type, 
			String starttime1, String endtime1, String cityId,
			String status, String resultId, String isMgr, int curPage_splitPage,
			int num_splitPage, String isSoftUp,String vendorId, String deviceModelId)
	{
		if("1".equals(isSoftUp)){
			tableName = "gw_serv_strategy_soft";
		}
		
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,b.device_model_id,");
		sql.append("b.oui,b.device_serialnumber,b.loopback_ip,c.fault_desc");
		if(LipossGlobals.inArea(Global.JSDX)){
			sql.append(",d.wireless_type as awifi_type");
		}
		sql.append(" from " + tableName + " a,tab_gw_device b,tab_cpe_faultcode c ");
		if(LipossGlobals.inArea(Global.JSDX)){
			sql.append(",tab_wirelesst_task d ");
		}
		sql.append(" where a.result_id=c.fault_code and a.device_id=b.device_id ");
		sql.append("and a.service_id=2001 and a.is_last_one=1 ");
		if(LipossGlobals.inArea(Global.JSDX)){
			sql.append(" and a.ids_task_id=d.task_id and d.wireless_type="+awifi_type+" ");
		}
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.time<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		// 未做的直接status=0就可以了 result_id不参与判断
		if (false == StringUtil.IsEmpty(status)){
			sql.append(" and a.status=").append(status);
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and b.gw_type=").append(gw_type);
		}
		if (false == StringUtil.IsEmpty(resultId)){
			if("100".equals(status)){
				// 统计失败
				if ("not1".equals(resultId)){
					sql.append(" and a.result_id not in (0,1,2)");
				}else{
					// 成功
					sql.append(" and a.result_id =").append(resultId);
				}
			}
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append(" and b.vendor_id='").append(vendorId).append("'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append(" and b.device_model_id='").append(deviceModelId).append("'");
		}
//		if (false == StringUtil.IsEmpty(isMgr)){
//			sql.append(" and c.is_mgr=").append(isMgr);
//		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper(){
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}else{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model)){
					map.put("device_model", device_model);
				}else{
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
				if (false == StringUtil.IsEmpty(softwareversion)){
					map.put("softwareversion", softwareversion);
				}else{
					map.put("softwareversion", "");
				}
				if(LipossGlobals.inArea(Global.JSDX)){
					map.put("awifi_type", rs.getString("awifi_type"));
				}
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device", rs.getString("oui") + "-"+ rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("fault_desc", rs.getString("fault_desc"));
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}

	public int getDevCount(String tableName, String gw_type,String awifi_type, String starttime1, String endtime1, String cityId,
			String status, String resultId, String isMgr, int curPage_splitPage,
			int num_splitPage, String isSoftUp,String vendorId, String deviceModelId ){
		logger.debug("getDevList({},{},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, status, resultId, isMgr, curPage_splitPage,num_splitPage });
		StringBuffer sql = new StringBuffer();
		// String tableName = "gw_serv_strategy_batch";
		
//		if(LipossGlobals.inArea("js_dx")){
//			tableName = "gw_serv_strategy";
//		}else if(LipossGlobals.inArea("ah_dx")){
//			tableName = "gw_serv_strategy_soft";
//		}
		
		if("1".equals(isSoftUp)){
			tableName = "gw_serv_strategy_soft";
		}
		
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from " + tableName + " a,tab_gw_device b");
		if(LipossGlobals.inArea(Global.JSDX)){
			sql.append(",tab_wirelesst_task c ");
		}
		sql.append(" where a.device_id=b.device_id and a.service_id=2001 and a.is_last_one=1 ");
		if(LipossGlobals.inArea(Global.JSDX)){
			sql.append(" and a.ids_task_id=c.task_id and c.wireless_type = "+awifi_type+" ");
		}
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.time<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(status)){
			sql.append(" and a.status=").append(status);
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and b.gw_type=").append(gw_type);
		}
		if (false == StringUtil.IsEmpty(resultId)){
			if("100".equals(status)){
				if ("not1".equals(resultId)){
					// 失败
					sql.append(" and a.result_id not in (0,1,2)");
				}else{
					// 成功
					sql.append(" and a.result_id =").append(resultId);
				}
			}
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append(" and b.vendor_id='").append(vendorId).append("'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append(" and b.device_model_id='").append(deviceModelId).append("'");
		}
//		if (false == StringUtil.IsEmpty(isMgr)){
//			sql.append(" and c.is_mgr=").append(isMgr);
//		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List<Map> getDevExcel(String tableName, String gw_type,String awifi_type,
			String starttime1, String endtime1, String cityId,
			String status, String resultId, String isMgr, String isSoftUp, 
			String vendorId, String deviceModelId)
	{
		logger.debug("getDevExcel({},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, status, resultId, isMgr });
		StringBuffer sql = new StringBuffer();
		// String tableName = "gw_serv_strategy_batch";
		
//		if(LipossGlobals.inArea("js_dx")){
//			tableName = "gw_serv_strategy";
//		}else if(LipossGlobals.inArea("ah_dx")){
//			tableName = "gw_serv_strategy_soft";
//		}
		
		if("1".equals(isSoftUp)){
			tableName = "gw_serv_strategy_soft";
		}
		
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,b.device_model_id,");
		sql.append("b.oui,b.device_serialnumber,b.loopback_ip,c.fault_desc ");
		sql.append(" from " + tableName + " a,tab_gw_device b,tab_cpe_faultcode c ");
		if(LipossGlobals.inArea(Global.JSDX)){
			sql.append(",tab_wirelesst_task d ");
		}
		sql.append(" where a.result_id = c.fault_code and a.device_id=b.device_id ");
		sql.append("and a.service_id=2001 and a.is_last_one=1 ");
		if(LipossGlobals.inArea(Global.JSDX)){
			sql.append(" and a.ids_task_id=d.task_id and d.wireless_type="+awifi_type+" ");
		}
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.time<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(status)){
			sql.append(" and a.status=").append(status);
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and b.gw_type=").append(gw_type);
		}
		if (false == StringUtil.IsEmpty(resultId)){
			if("100".equals(status)){
				if ("not1".equals(resultId)){
					// 失败
					sql.append(" and a.result_id not in (0,1,2)");
				}else{
					// 成功
					sql.append(" and a.result_id=").append(resultId);
				}
			}
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append(" and b.vendor_id='").append(vendorId).append("'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append(" and b.device_model_id='").append(deviceModelId).append("'");
		}
//		if (false == StringUtil.IsEmpty(isMgr))
//		{
//			sql.append(" and c.is_mgr=").append(isMgr);
//		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper(){
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}else{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model)){
					map.put("device_model", device_model);
				}else{
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
				if (false == StringUtil.IsEmpty(softwareversion)){
					map.put("softwareversion", softwareversion);
				}else{
					map.put("softwareversion", "");
				}
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device", rs.getString("oui") + "-"+ rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("fault_desc", rs.getString("fault_desc"));
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}
	
	/**
	 * @category getVendor 获取所有的厂商
	 * @param city_id
	 * @return List
	 */
	public List getVendor() {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select vendor_id,vendor_name,vendor_add from tab_vendor order by vendor_name asc");
		return jt.queryForList(pSQL.getSQL());
	}
	
	/**
	 * @category getDevicetype 获取所有的设备型号
	 * @param vendorId
	 * @return List
	 */
	public List getDeviceModel(String vendorId) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select device_model_id,device_model from gw_device_model ");
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append("where vendor_id='"+vendorId+"'");
		}
		pSQL.append(" order by device_model asc");
		return jt.queryForList(pSQL.getSQL());
	}
}
