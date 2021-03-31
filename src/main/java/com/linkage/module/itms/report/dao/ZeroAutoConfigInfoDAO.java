package com.linkage.module.itms.report.dao;

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
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class ZeroAutoConfigInfoDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(ZeroAutoConfigInfoDAO.class);
	private Map<String, String> cityMap = null;
			
	private Map<String,String> accessMap = new HashMap<String, String>();
	public ZeroAutoConfigInfoDAO(){
		accessMap.put("1", "ADSL");
		accessMap.put("2", "LAN");
		accessMap.put("3", "EPON");
		accessMap.put("4", "GPON");
	}
	
	
	public List<Map> getZeroAutoConfigInfo(String starttime, String endtime,
			String city_id, String servTypeId,  String resultType,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getZeroAutoConfigInfo({},{},{},{},{},{},{},{})", new Object[] {
				starttime, endtime, city_id, servTypeId,  resultType,
				curPage_splitPage, num_splitPage });
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select a.user_id,a.username,a.city_id,a.device_serialnumber,a.oui,a.device_id, ");
		sql.append("b.serv_type_id,b.serv_status,b.dealdate,b.opendate,b.open_status,b.wan_type,");
		sql.append("b.username as serUsername, d.vendor_name, e.device_model, f.access_style_relay_id ");
		sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_device c,");
		sql.append("tab_vendor d,gw_device_model e,tab_devicetype_info f ");
		sql.append("where a.device_id=c.device_id and a.user_id=b.user_id ");
		sql.append("and c.devicetype_id=f.devicetype_id and f.vendor_id=d.vendor_id ");
		sql.append("and f.device_model_id =e.device_model_id ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
		if (!StringUtil.IsEmpty(starttime)){
			sql.append(" and b.dealdate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)){
			sql.append(" and b.dealdate<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(servTypeId) && !"-1".equals(servTypeId)){
			sql.append(" and b.serv_type_id=").append(servTypeId);
		}
		if (!StringUtil.IsEmpty(resultType) && !"2".equals(resultType)){
			sql.append(" and b.open_status=").append(resultType);
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_Name", rs.getString("username"));
				String cityName = null;
				cityName = (String) cityMap.get(rs.getString("city_id"));
				if (!StringUtil.IsEmpty(cityName))
				{
					map.put("city_id", cityName);
				}
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				
				String servType = getServType(rs.getString("serv_type_id"));
				if (!StringUtil.IsEmpty(servType))
				{
					map.put("serv_type", servType);
				}
				
				try
				{
					long opertime = StringUtil.getLongValue(rs.getString("dealdate")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(opertime);
					map.put("dealdate", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("dealdate", "");
				}
				catch (Exception e)
				{
					map.put("dealdate", "");
				}
				
				String resultId = getResultType(rs.getString("open_status"));
				if (!StringUtil.IsEmpty(resultId))
				{
					map.put("open_status", resultId);
				}
				
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("device_model", rs.getString("device_model"));
				map.put("access_style_relay_id", accessMap.get(rs.getString("access_style_relay_id")));
				map.put("zdType", "E8-C");
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	public int countZeroAutoConfigInfo(String starttime, String endtime, String city_id,
			String servTypeId,String resultType, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("countZeroAutoConfigInfo({},{},{},{},{},{},{},{})", new Object[] {
				starttime, endtime, city_id, servTypeId, resultType,
				curPage_splitPage, num_splitPage });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_device c,");
		sql.append("tab_vendor d,gw_device_model e,tab_devicetype_info f ");
		sql.append("where a.device_id=c.device_id and a.user_id=b.user_id ");
		sql.append("and c.devicetype_id=f.devicetype_id and f.vendor_id=d.vendor_id ");
		sql.append("and f.device_model_id=e.device_model_id ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
		if (!StringUtil.IsEmpty(starttime)){
			sql.append(" and b.dealdate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)){
			sql.append(" and b.dealdate<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(servTypeId) && !"-1".equals(servTypeId)){
			sql.append(" and b.serv_type_id=").append(servTypeId);
		}
		if (!StringUtil.IsEmpty(resultType) && !"2".equals(resultType)){
			sql.append(" and b.open_status=").append(resultType);
		}
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

	public int countZeroAutoConfigInfoExcel(String starttime, String endtime,
			String city_id, String servTypeId, String resultType)
	{
		logger.debug("countOperateByHandInfoExcel({},{},{},{},{},{},{},{})",
				new Object[] { starttime, endtime, city_id, servTypeId,
						resultType });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_device c,");
		sql.append("tab_vendor d,gw_device_model e,tab_devicetype_info f ");
		sql.append("where a.device_id=c.device_id and a.user_id=b.user_id ");
		sql.append("and c.devicetype_id=f.devicetype_id and f.vendor_id=d.vendor_id ");
		sql.append("and f.device_model_id=e.device_model_id ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
		if (!StringUtil.IsEmpty(starttime)){
			sql.append(" and .b.dealdate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)){
			sql.append(" and .b.dealdate<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(servTypeId) && !"-1".equals(servTypeId)){
			sql.append(" and b.serv_type_id=").append(servTypeId);
		}
		if (!StringUtil.IsEmpty(resultType) && !"2".equals(resultType)){
			sql.append(" and b.open_status=").append(resultType);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		return total;
	}

	@SuppressWarnings("unchecked")
	public List<Map> getZeroAutoConfigInfoExcel(String starttime, String endtime,
			String city_id, String servTypeId, String resultType)
	{
		logger.debug("ZeroAutoConfig({},{},{},{},{},{},{},{})", new Object[] {
				starttime, endtime, city_id, servTypeId,  resultType });
		StringBuffer sql = new StringBuffer();
		sql.append("select a.user_id,a.username,a.city_id,a.device_serialnumber,a.oui,a.device_id,a.linkaddress,");
		sql.append("b.serv_type_id,b.serv_status,b.dealdate,b.opendate,b.open_status,b.wan_type,");
		sql.append("b.username as serUsername,d.vendor_name,e.device_model,f.access_style_relay_id ");
		sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_device c,tab_vendor d,");
		sql.append("gw_device_model e,tab_devicetype_info f ");
		sql.append("where a.device_id=c.device_id and a.user_id=b.user_id and c.devicetype_id=f.devicetype_id ");
		sql.append("and f.vendor_id=d.vendor_id and f.device_model_id=e.device_model_id ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
		if (!StringUtil.IsEmpty(starttime)){
			sql.append(" and b.dealdate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)){
			sql.append(" and b.dealdate<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(servTypeId) && !"-1".equals(servTypeId)){
			sql.append(" and b.serv_type_id=").append(servTypeId);
		}
		if (!StringUtil.IsEmpty(resultType) && !"2".equals(resultType)){
			sql.append(" and b.open_status=").append(resultType);
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{	
			for (int i = 0; i < list.size(); i++)
			{
				String name = (String) list.get(i).get("username");
				list.get(i).put("username", name);
				String cid = (String) list.get(i).get("city_id");
				list.get(i).put("city_id", cid);
				String city_name = StringUtil.getStringValue(cityMap.get(cid));
				if (!StringUtil.IsEmpty(city_name))
				{
					list.get(i).put("city_name", city_name);
				}
				else
				{
					list.get(i).put("city_name", "");
				}
				String ddevice_serialnumber = StringUtil.getStringValue(list.get(i).get("device_serialnumber"));
				list.get(i).put("device_serialnumber", ddevice_serialnumber);
				
				String serType = StringUtil.getStringValue(list.get(i).get("serv_type_id"));
				list.get(i).put("serv_type", getServType(serType));
				list.get(i).put("linkaddress", StringUtil.getStringValue(list.get(i).get("linkaddress")));
				String result = StringUtil.getStringValue(list.get(i).get("open_status"));
				if (!StringUtil.IsEmpty(result))
				{
					list.get(i).put("open_status", getResultType(result));
				}
				try
				{
					long opertime = StringUtil.getLongValue(list.get(i).get("dealdate")
							+ "") * 1000L;
					DateTimeUtil dt = new DateTimeUtil(opertime);
					list.get(i).put("dealdate", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					list.get(i).put("dealdate", "");
				}
				catch (Exception e)
				{
					list.get(i).put("dealdate", "");
				}
				
				String vendor_name = StringUtil.getStringValue(list.get(i).get("vendor_name"));
				list.get(i).put("vendor_name", vendor_name);
				String device_model= StringUtil.getStringValue(list.get(i).get("device_model"));
				list.get(i).put("device_model", device_model);
				String access_style_relay_id = StringUtil.getStringValue(list.get(i).get("access_style_relay_id"));
				list.get(i).put("access_style_relay_id", accessMap.get(access_style_relay_id));
				list.get(i).put("zdType", "E8-C");
				
			}
			cityMap = null;
		}
		return list;
	}

	private String getServType(String servTypeId)
	{
		String str = null;
		if (!StringUtil.IsEmpty(servTypeId))
		{
			int key = Integer.parseInt(servTypeId);
			switch (key)
			{
				case 10:
					str = "宽带";
					break;
				case 11:
					str = "IPTV";
					break;
				case 14:
					str = "VOIP";
					break;
				default:
					break;
			}
		}
		return str;
	}
	
	private String getResultType(String resultId)
	{
		String str = "";
		if (!StringUtil.IsEmpty(resultId))
		{
			int key = Integer.parseInt(resultId);
			switch (key)
			{
				case 0:
					str = "未做";
					break;
				case 1:
					str = "成功";
					break;
				case -1:
					str = "失败";
					break;
				default:
					break;
			}
		}
		return str;
	}
}
