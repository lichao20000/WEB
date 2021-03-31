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
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author songxq
 * @version 1.0
 * @since 2019-8-6 上午10:33:01
 * @category 
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchRestartResultDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(BatchRestartResultDAO.class);
	
	private Map<String, String> cityMap = null;
	private Map<String, String> vendorMap = null;    // 厂商
	private Map<String, String> devicetypeMap = null;   // 版本 
	private Map<String, String> deviceModelMap = null; // 型号
	
	public Map<String, String> getAllRestart(String cityId, String starttime,
			String endtime)
	{
		// TODO Auto-generated method stub
		logger.debug("getAllRestart({},{},{})", new Object[]{cityId, starttime, endtime});
		
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select b.city_id,count(*) as total ");
		}else{
			sql.append("select b.city_id,count(1) as total ");
		}
		sql.append("from tab_dev_batch_restart a,tab_gw_device b ");
		sql.append("where a.device_id=b.device_id and a.restart_time<=? and a.restart_time>=? ");
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setLong(1, StringUtil.getLongValue(endtime));
		psql.setLong(2, StringUtil.getLongValue(starttime));
		
		List list = jt.queryForList(psql.getSQL());
		if(false == list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map)list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		
		return map;
	}

	public Map<String, String> getSuccRestart(String cityId, String starttime,
			String endtime)
	{
		logger.debug("getSuccRestart({},{},{})", new Object[]{cityId, starttime, endtime});
		
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select b.city_id,count(*) as total ");
		}else{
			sql.append("select b.city_id,count(1) as total ");
		}
		sql.append("from tab_dev_batch_restart a,tab_gw_device b ");
		sql.append("where a.device_id=b.device_id and a.restart_time<=? and a.restart_time>=? ");
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" and a.restart_status = 1");
		sql.append(" group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setLong(1, StringUtil.getLongValue(endtime));
		psql.setLong(2, StringUtil.getLongValue(starttime));
		
		List list = jt.queryForList(psql.getSQL());
		if(false == list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map)list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		
		return map;
	}

	public Map<String, String> getFailRestart(String cityId, String starttime,
			String endtime)
	{
		// TODO Auto-generated method stub
		logger.debug("getSuccRestart({},{},{})", new Object[]{cityId, starttime, endtime});
		
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select b.city_id,count(*) as total ");
		}else{
			sql.append("select b.city_id,count(1) as total ");
		}
		sql.append("from tab_dev_batch_restart a,tab_gw_device b ");
		sql.append("where a.device_id = b.device_id and a.restart_time <= ? and a.restart_time >= ? ");
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" and a.restart_status = -1");
		sql.append(" group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setLong(1, StringUtil.getLongValue(endtime));
		psql.setLong(2, StringUtil.getLongValue(starttime));
		
		List list = jt.queryForList(psql.getSQL());
		if(false == list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map)list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		
		return map;
	}
	
	
	public List<Map>  getDetail(String gw_type, String cityId,String restartStatus,
			int curPage_splitPage, int num_splitPage,String starttime,String endtime )
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select c.device_id,c.device_serialnumber,c.vendor_id,c.devicetype_id,");
			psql.append("c.city_id,c.device_model_id,c.restart_status,c.restart_time ");
		}else{
			psql.append("select c.*,");
		}
		psql.append("d.username from ");
		psql.append("(select a.device_id,device_serialnumber,vendor_id,devicetype_id,");
		psql.append("city_id,device_model_id,b.restart_status,b.restart_time ");
		psql.append("from tab_gw_device a,tab_dev_batch_restart b ");
		psql.append("where a.device_id=b.device_id and b.restart_time<=? ");
		psql.append("and b.restart_time>=?  and gw_type=" + gw_type);
		//100 由于js 不能传空 故传未被占用的100 传100时 表示查询总数
		if(!StringUtil.IsEmpty(restartStatus) && !"100".equals(restartStatus))
		{
			psql.append(" and b.restart_status = ? ");
		}
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and a.city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		psql.append(" ) c ");
		
		psql.append("left join (select device_id,username from tab_hgwcustomer where user_state in ('1','2') ) d ");
		
		psql.append(" on c.device_id=d.device_id ");
		psql.setLong(1, StringUtil.getLongValue(endtime));
		psql.setLong(2, StringUtil.getLongValue(starttime));
		if(null != restartStatus && !"".equals(restartStatus))
		{
			psql.setInt(3, StringUtil.getIntegerValue(restartStatus));
		}
		
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();           // 厂商
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();   // 版本 
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap(); // 型号
	
		//List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				
				Map<String, String> map = new HashMap<String, String>();
				// 属地
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap, city_id, "");
				map.put("city_name", city_name);
				map.put("city_id", city_id);
				
				// 厂商
				String vendorId = rs.getString("vendor_id");
				String vendorName = StringUtil.getStringValue(vendorMap, vendorId, "");
				map.put("vendor_name", vendorName);
				
				// 软件版本
				String devicetypeId = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap, devicetypeId, "");
				map.put("softwareversion", softwareversion);
				
				// 型号
				String deviceModelId = rs.getString("device_model_id");
				String deviceModel = StringUtil.getStringValue(deviceModelMap, deviceModelId, "");
				map.put("device_model", deviceModel);
				
				// 设备序列号
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				
				// 设备ID
				map.put("device_id", rs.getString("device_id"));
				
				//loid
				map.put("loid", rs.getString("username"));
				//重启时间
				map.put("restart_time", transDate(rs.getString("restart_time")));
				//重启结果
				String result = rs.getString("restart_status");
				if("1".equals(result))
				{
					map.put("restart_status", "重启成功");
				}
				else if("-1".equals(result)){
					map.put("restart_status", "重启失败");
				}
				else if("0".equals(result)){
					map.put("restart_status", "未重启");
				}
				
				return map;
			}
		});
		
		cityMap = null;
		vendorMap = null;
		devicetypeMap = null;
		deviceModelMap = null;
		
		return list;
	}
	
	public List<Map>  getDetailExcel(String gw_type, String cityId,
			String restartStatus,String starttime,String endtime )
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select c.device_id,c.device_serialnumber,c.vendor_id,c.devicetype_id,");
			psql.append("c.city_id,c.device_model_id,c.restart_status,c.restart_time ");
		}else{
			psql.append("select c.*,");
		}
		psql.append("d.username from ");
		psql.append("(select a.device_id,device_serialnumber,vendor_id,devicetype_id,");
		psql.append("city_id,device_model_id,b.restart_status,b.restart_time ");
		psql.append("from tab_gw_device a ,tab_dev_batch_restart b ");
		psql.append("where a.device_id = b.device_id and b.restart_time <= ? ");
		psql.append("and b.restart_time >= ?  and gw_type=" + gw_type);
		//100 由于js 不能传空 故传未被占用的100 传100时 表示查询总数
		if(!StringUtil.IsEmpty(restartStatus) && !"100".equals(restartStatus))
		{
			psql.append(" and b.restart_status = ? ");
		}
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and a.city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		psql.append("  ) c ");
		
		psql.append(" left join (select device_id,username from tab_hgwcustomer where user_state in ('1','2') ) d  ");
		
		psql.append(" on c.device_id=d.device_id   ");
		psql.setLong(1, StringUtil.getLongValue(endtime));
		psql.setLong(2, StringUtil.getLongValue(starttime));
		if(null != restartStatus && !"".equals(restartStatus))
		{
			psql.setInt(3, StringUtil.getIntegerValue(restartStatus));
		}
		
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();           // 厂商
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();   // 版本 
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap(); // 型号
	
		//List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		@SuppressWarnings("unchecked")
		List<Map> list = jt.query(psql.getSQL(),new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				
				Map<String, String> map = new HashMap<String, String>();
				// 属地
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap, city_id, "");
				map.put("city_name", city_name);
				map.put("city_id", city_id);
				
				// 厂商
				String vendorId = rs.getString("vendor_id");
				String vendorName = StringUtil.getStringValue(vendorMap, vendorId, "");
				map.put("vendor_name", vendorName);
				
				// 软件版本
				String devicetypeId = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap, devicetypeId, "");
				map.put("softwareversion", softwareversion);
				
				// 型号
				String deviceModelId = rs.getString("device_model_id");
				String deviceModel = StringUtil.getStringValue(deviceModelMap, deviceModelId, "");
				map.put("device_model", deviceModel);
				
				// 设备序列号
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				
				// 设备ID
				map.put("device_id", rs.getString("device_id"));
				
				//loid
				map.put("loid", rs.getString("username"));
				//重启时间
				map.put("restart_time", transDate(rs.getString("restart_time")));
				//重启结果
				String result = rs.getString("restart_status");
				if("1".equals(result))
				{
					map.put("restart_status", "重启成功");
				}
				else if("-1".equals(result)){
					map.put("restart_status", "重启失败");
				}
				else if("0".equals(result)){
					map.put("restart_status", "未重启");
				}
				
				return map;
			}
		});
		
		cityMap = null;
		vendorMap = null;
		devicetypeMap = null;
		deviceModelMap = null;
		
		return list;
	}
	
	
	private static final String transDate(Object seconds) {
		if (seconds != null) {
			try {
				DateTimeUtil dt = new DateTimeUtil(Long.parseLong(seconds
						.toString()) * 1000);
				return dt.getLongDate();
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}

	public int getCount(String gw_type, String cityId, String restartStatus,
			int curPage_splitPage, int num_splitPage, String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("select count(*) from ");
		}else{
			psql.append("select count(1) from ");
		}
		
		psql.append("(select a.device_id,device_serialnumber,vendor_id,devicetype_id,");
		psql.append("city_id,device_model_id,b.restart_status,b.restart_time ");
		psql.append("from tab_gw_device a,tab_dev_batch_restart b ");
		psql.append("where a.device_id = b.device_id and b.restart_time <= ? ");
		psql.append("and b.restart_time >= ?  and gw_type=" + gw_type);
		if(!StringUtil.IsEmpty(restartStatus)&& !"100".equals(restartStatus))
		{
			psql.append(" and b.restart_status = ? ");
		}
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and a.city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		psql.append("  ) c ");
		
		psql.append(" left join (select device_id,username from tab_hgwcustomer where user_state in ('1','2') ) d  ");
		
		psql.append(" on c.device_id=d.device_id   ");
		psql.setLong(1, StringUtil.getLongValue(endtime));
		psql.setLong(2, StringUtil.getLongValue(starttime));
		if(!StringUtil.IsEmpty(restartStatus) && !"100".equals(restartStatus))
		{
			psql.setInt(3, StringUtil.getIntegerValue(restartStatus));
		}
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	
	/**
	 * 获取该cityId的所有子属地表中数据(包含自身)
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getNextCityIdsByCityPid(String cityId)
	{
		String getNextCityIdsByCityPid = "select city_id from tab_city where (parent_id ='" +
				cityId + "' or city_id = '"+cityId+"' ) order by city_id";
		
		PrepareSQL psql = new PrepareSQL(getNextCityIdsByCityPid);
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1)
					throws java.sql.SQLException
			{
				Map m = new HashMap();
				m.put("city_id", rs.getString("city_id"));
				return m;
			}
		});
		return list;
	}
}

