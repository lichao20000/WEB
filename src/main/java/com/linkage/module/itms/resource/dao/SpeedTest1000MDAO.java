package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


@SuppressWarnings({"unchecked","rawtypes"})
public class SpeedTest1000MDAO extends SuperDAO 
{
	private Map<String, String> deviceModelMap=new HashMap<String,String>();
	private Map<String, String> vendorMap=new HashMap<String,String>();
	private Map<String, String> cityMap=new HashMap<String,String>();
	
	@SuppressWarnings("unchecked")
	public List<Map> queryCityId(String area_id){
		PrepareSQL sql = new PrepareSQL();
		sql.append("select a.city_id from tab_city_area a ");
		sql.append("inner join tab_city b on a.city_id=b.city_id ");
		
		if (!StringUtil.IsEmpty(area_id) && !"1".equals(area_id)) {
			sql.append("where a.area_id="+area_id+" and b.parent_id='00' ");
		}else {
			sql.append("where a.area_id="+area_id+" ");
		}
		return jt.queryForList(sql.getSQL());
	}
	
	public List<Map> query(String speedStatus,String bandwidth,String startTime,
			String endTime,String cityId,int curPage_splitPage, int num_splitPage) 
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select aa.*,bb.parent_id from (");
		sql.append("select b.city_id,c.username loid,d.username,b.vendor_id,b.device_model_id,a.rate,a.status,f.DOWN_BANDWIDTH ");
		sql.append("from tab_xj_speedtest_result a,tab_gw_device b ,tab_hgwcustomer c,hgwcust_serv_info d,tab_net_serv_param f ");
		sql.append("where a.device_id=b.device_id and b.device_id=c.device_id and c.user_id = d.user_id and d.serv_type_id=10 ");
		sql.append("and d.user_id=f.user_id ");
		
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append("and b.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		if(!StringUtil.IsEmpty(speedStatus) && !"0".equals(speedStatus)){
			sql.append("and a.status=" + StringUtil.getIntegerValue(speedStatus) + " ");
		}
		
		if(!StringUtil.IsEmpty(bandwidth)){
			sql.append("and f.DOWN_BANDWIDTH='" + StringUtil.getStringValue(bandwidth) + "' ");
		}
		if(!StringUtil.IsEmpty(startTime)){
			sql.append("and a.exce_time >= " + startTime + " ");
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			sql.append("and a.exce_time <= " + endTime + " ");
		}
		
		sql.append(")aa,tab_city bb where aa.city_id=bb.city_id order by bb.parent_id ,bb.city_id"); 

		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		
		List<Map> list = querySP(sql.getSQL(), 
				(curPage_splitPage - 1) * num_splitPage+1, num_splitPage, new RowMapper() 
		{
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException 
			{
				Map<String, String> map = new HashMap<String, String>();
				try
				{
					String parent_id = StringUtil.getStringValue(rs.getString("parent_id"));
					if(!StringUtil.IsEmpty(parent_id) && "-1".equals(parent_id)){
						parent_id = "00";
					}
					map.put("city_name",cityMap.get(parent_id));
					
					String city_id = StringUtil.getStringValue(rs.getString("city_id"));
					map.put("county_name",cityMap.get(city_id));
					map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
					map.put("username", StringUtil.getStringValue(rs.getString("username")));
					map.put("bandwidth", StringUtil.getStringValue(rs.getString("DOWN_BANDWIDTH".toLowerCase())));
					 
					String vendor_id = StringUtil.getStringValue(rs.getString("vendor_id"));
					map.put("vendor_name", vendorMap.get(vendor_id));
					
					String device_mode_id = StringUtil.getStringValue(rs.getString("device_model_id"));
					map.put("devicemodel", deviceModelMap.get(device_mode_id));
					map.put("speed_ret", StringUtil.getStringValue(rs.getString("rate")));
					
					int status = StringUtil.getIntegerValue(rs.getInt("status"),0);
					String result = "未知";
					if(status == 1){
						result = "合格";
					}else if(status == -1){
						result = "不合格";
					}
					map.put("result",result);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}
	public int queryCount(String speedStatus,String bandwidth,String startTime,String endTime,String cityId,int curPage_splitPage, int num_splitPage){
		PrepareSQL sql = new PrepareSQL();
		sql.append("select count(*) ");
		sql.append("from tab_xj_speedtest_result a,tab_gw_device b ,tab_hgwcustomer c,hgwcust_serv_info d,tab_net_serv_param f ");
		sql.append("where a.device_id=b.device_id and b.device_id=c.device_id and c.user_id = d.user_id and d.serv_type_id=10 ");
		sql.append("and d.user_id=f.user_id ");
		
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append("and b.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		if(!StringUtil.IsEmpty(speedStatus) && !"0".equals(speedStatus)){
			sql.append("and a.status=" + StringUtil.getIntegerValue(speedStatus) + " ");
		}
		
		if(!StringUtil.IsEmpty(bandwidth)){
			sql.append("and f.DOWN_BANDWIDTH='" + StringUtil.getStringValue(bandwidth) + "' ");
		}
		if(!StringUtil.IsEmpty(startTime)){
			sql.append("and a.exce_time >= " + startTime + " ");
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			sql.append("and a.exce_time <= " + endTime + " ");
		}
		

		PrepareSQL psql = new PrepareSQL(sql.toString());
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
	
	public List<Map> toExcel(String speedStatus,String bandwidth,String startTime,String endTime,String cityId){
		PrepareSQL sql = new PrepareSQL();
		sql.append("select aa.*,bb.parent_id from (");
		sql.append("select b.city_id,c.username loid,d.username,b.vendor_id,b.device_model_id,a.rate,a.status,f.DOWN_BANDWIDTH ");
		sql.append("from tab_xj_speedtest_result a,tab_gw_device b ,tab_hgwcustomer c,hgwcust_serv_info d,tab_net_serv_param f ");
		sql.append("where a.device_id=b.device_id and b.device_id=c.device_id and c.user_id = d.user_id and d.serv_type_id=10 ");
		sql.append("and d.user_id=f.user_id ");
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append("and b.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		if(!StringUtil.IsEmpty(speedStatus) && !"0".equals(speedStatus)){
			sql.append("and a.status=" + StringUtil.getIntegerValue(speedStatus) + " ");
		}
		
		if(!StringUtil.IsEmpty(bandwidth)){
			sql.append("and f.DOWN_BANDWIDTH='" + StringUtil.getStringValue(bandwidth) + "' ");
		}
		if(!StringUtil.IsEmpty(startTime)){
			sql.append("and a.exce_time >= " + startTime + " ");
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			sql.append("and a.exce_time <= " + endTime + " ");
		}
		
		sql.append(")aa,tab_city bb where aa.city_id=bb.city_id order by bb.parent_id ,bb.city_id"); 

		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		
		List<Map> list = jt.query(sql.getSQL(), new RowMapper()
		{
			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String parent_id = StringUtil.getStringValue(rs.getString("parent_id"));
				if(!StringUtil.IsEmpty(parent_id) && "-1".equals(parent_id)){
					parent_id = "00";
				}
				map.put("city_name",cityMap.get(parent_id));
				
				String city_id = StringUtil.getStringValue(rs.getString("city_id"));
				map.put("county_name",cityMap.get(city_id));
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				map.put("username", StringUtil.getStringValue(rs.getString("username")));
				map.put("bandwidth", StringUtil.getStringValue(rs.getString("DOWN_BANDWIDTH".toLowerCase())));
				 
				String vendor_id = StringUtil.getStringValue(rs.getString("vendor_id"));
				map.put("vendor_name", vendorMap.get(vendor_id));
				
				String device_mode_id = StringUtil.getStringValue(rs.getString("device_model_id"));
				map.put("devicemodel", deviceModelMap.get(device_mode_id));
				map.put("speed_ret", StringUtil.getStringValue(rs.getString("rate")));
				
				int status = StringUtil.getIntegerValue(rs.getInt("status"),0);
				String result = "未知";
				if(status == 1){
					result = "合格";
				}else if(status == -1){
					result = "不合格";
				}
				map.put("result",result);
				
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	} 
}
