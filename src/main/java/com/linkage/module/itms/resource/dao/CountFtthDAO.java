
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-11-18 下午03:28:13
 * @category com.linkage.module.itms.resource.dao
 * @copyright 南京联创科技 网管科技部
 */
public class CountFtthDAO extends SuperDAO
{


	public List queryUnbind(String startTime, String endTime, String cityId,
			int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sb = new StringBuffer();
		String sql = " select d.device_id,c.city_name,v.vendor_name,v.vendor_add,m.device_model,d.device_serialnumber,d.device_id_ex,d.loopback_ip,s.last_time from tab_gw_device d,"
				+ " tab_city c,tab_vendor v,gw_device_model m,gw_devicestatus s,tab_devicetype_info t where  d.city_id=c.city_id and d.vendor_id=v.vendor_id "
				+ " and d.device_model_id=m.device_model_id and d.device_id=s.device_id and d.devicetype_id=t.devicetype_id ";
		sb.append(sql);
		List<String> cityList = new ArrayList<String>();
		String cityIdStr = "";
		if (!cityId.equals("-1"))
		{
			cityIdStr = "(";
			cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++)
			{
				if (i + 1 == cityList.size())
				{
					cityIdStr = cityIdStr + "'"
							+ StringUtil.getStringValue(cityList.get(i)) + "')";
				}
				else
				{
					cityIdStr = cityIdStr + "'"
							+ StringUtil.getStringValue(cityList.get(i)) + "',";
				}
			}
		}
		if (startTime != null && !"".equals(startTime))
		{
			sb.append(" and s.last_time >= " + startTime);
		}
		if (endTime != null && !"".equals(endTime))
		{
			sb.append(" and s.last_time <= " + endTime);
		}
		if(Global.NXDX.equals(Global.instAreaShortName)){
			sb.append(" and t.rela_dev_type_id = 2 ");
		}else{
			sb
				.append(" and d.cpe_allocatedstatus=1 and ( t.access_style_relay_id=3 or t.access_style_relay_id= 4 ) ");
		
		}
		if (cityList.size() > 0)
		{
			sb.append(" and d.city_id in " + cityIdStr + "");
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				map.put("city_name", rs.getString("city_name"));
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device_id_ex", rs.getString("device_id_ex"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(Long.parseLong(StringUtil.getStringValue(rs
						.getString("last_time"))
						+ "000"));
				String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal
						.getTime());
				map.put("last_time", date);
				return map;
			}
		});
		return list;
	}

	public List queryUnbind(String startTime, String endTime, String cityId)
	{
		StringBuffer sb = new StringBuffer();
		String sql = " select d.device_id,c.city_name,v.vendor_name,v.vendor_add,m.device_model,d.device_serialnumber,d.device_id_ex,d.loopback_ip,s.last_time from tab_gw_device d,"
				+ " tab_city c,tab_vendor v,gw_device_model m,gw_devicestatus s,tab_devicetype_info t where  d.city_id=c.city_id and d.vendor_id=v.vendor_id "
				+ " and d.device_model_id=m.device_model_id and d.device_id=s.device_id and d.devicetype_id=t.devicetype_id ";
		sb.append(sql);
		List<String> cityList = new ArrayList<String>();
		String cityIdStr = "";
		if (!cityId.equals("-1"))
		{
			cityIdStr = "(";
			cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++)
			{
				if (i + 1 == cityList.size())
				{
					cityIdStr = cityIdStr + "'"
							+ StringUtil.getStringValue(cityList.get(i)) + "')";
				}
				else
				{
					cityIdStr = cityIdStr + "'"
							+ StringUtil.getStringValue(cityList.get(i)) + "',";
				}
			}
		}
		if (startTime != null && !"".equals(startTime))
		{
			sb.append(" and s.last_time >= " + startTime);
		}
		if (endTime != null && !"".equals(endTime))
		{
			sb.append(" and s.last_time <= " + endTime);
		}
		if(Global.NXDX.equals(Global.instAreaShortName)){
			sb.append(" and t.rela_dev_type_id = 2 ");
		}else{
			sb
				.append(" and d.cpe_allocatedstatus=1 and ( t.access_style_relay_id=3 or t.access_style_relay_id= 4 ) ");
		
		}
		if (cityList.size() > 0)
		{
			sb.append(" and d.city_id in " + cityIdStr + "");
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < list.size(); i++)
		{
			cal.setTimeInMillis(Long.parseLong(StringUtil.getStringValue(list.get(i).get(
					"last_time"))
					+ "000"));
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal
					.getTime());
			list.get(i).put("last_time", date);
		}
		return list;
	}

	public int getCount(String startTime, String endTime, String cityId,
			int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sb = new StringBuffer();
		String sql = " select count(*) from tab_gw_device d,"
				+ " tab_city c,tab_vendor v,gw_device_model m,gw_devicestatus s,tab_devicetype_info t where  d.city_id=c.city_id and d.vendor_id=v.vendor_id "
				+ " and d.device_model_id=m.device_model_id and d.device_id=s.device_id and d.devicetype_id=t.devicetype_id ";
		sb.append(sql);
		List<String> cityList = new ArrayList<String>();
		String cityIdStr = "";
		if (!cityId.equals("-1"))
		{
			cityIdStr = "(";
			cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++)
			{
				if (i + 1 == cityList.size())
				{
					cityIdStr = cityIdStr + "'"
							+ StringUtil.getStringValue(cityList.get(i)) + "')";
				}
				else
				{
					cityIdStr = cityIdStr + "'"
							+ StringUtil.getStringValue(cityList.get(i)) + "',";
				}
			}
		}
		if (startTime != null && !"".equals(startTime))
		{
			sb.append(" and s.last_time >= " + startTime + "");
		}
		if (endTime != null && !"".equals(endTime))
		{
			sb.append(" and s.last_time <= " + endTime + "");
		}
		if(Global.NXDX.equals(Global.instAreaShortName)){
			sb.append(" and t.rela_dev_type_id = 2 ");
		}else{
			sb
				.append(" and d.cpe_allocatedstatus=1 and ( t.access_style_relay_id=3 or t.access_style_relay_id= 4 ) ");
		
		}if (cityList.size() > 0)
		{
			sb.append(" and d.city_id in " + cityIdStr + "");
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
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

	public List queryFTTHDetail(String device_id)
	{
		String sql = "select d.device_id,c.city_name,v.vendor_name,v.vendor_add,m.device_model,d.device_serialnumber,d.device_id_ex,d.loopback_ip,"
				+ " s.last_time from tab_gw_device d, tab_city c,tab_vendor v,gw_device_model m,gw_devicestatus s,tab_devicetype_info t "
				+ " where  d.city_id=c.city_id and d.vendor_id=v.vendor_id  and d.device_model_id=m.device_model_id and d.device_id=s.device_id "
				+ " and d.devicetype_id=t.devicetype_id and d.device_id= '";
		sql = sql + device_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		String last_time = StringUtil
				.getStringValue(list.get(0).get("last_time") + "000");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong(last_time));
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		list.get(0).put("last_time", date);
		return list;
	}

	public List queryUnloid(String startTime, String endTime, String cityId,
			int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sb = new StringBuffer();
		String sql = " select d.device_id,c.city_name,v.vendor_name,v.vendor_add,m.device_model,d.device_serialnumber,d.device_id_ex,d.loopback_ip,s.last_time from tab_gw_device d,"
				+ " tab_city c,tab_vendor v,gw_device_model m,gw_devicestatus s,tab_devicetype_info t where  d.city_id=c.city_id and d.vendor_id=v.vendor_id "
				+ " and d.device_model_id=m.device_model_id and d.device_id=s.device_id and d.devicetype_id=t.devicetype_id ";
		sb.append(sql);
		List<String> cityList = new ArrayList<String>();
		String cityIdStr = "";
		if (!cityId.equals("-1"))
		{
			cityIdStr = "(";
			cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++)
			{
				if (i + 1 == cityList.size())
				{
					cityIdStr = cityIdStr + "'"
							+ StringUtil.getStringValue(cityList.get(i)) + "')";
				}
				else
				{
					cityIdStr = cityIdStr + "'"
							+ StringUtil.getStringValue(cityList.get(i)) + "',";
				}
			}
		}
		if (startTime != null && !"".equals(startTime))
		{
			sb.append(" and s.last_time >= " + startTime);
		}
		if (endTime != null && !"".equals(endTime))
		{
			sb.append(" and s.last_time <= " + endTime);
		}
		if(Global.NXDX.equals(Global.instAreaShortName)){
			sb.append(" and t.rela_dev_type_id = 2 ");
		}else{
			sb.append("  and ( t.access_style_relay_id=3 or t.access_style_relay_id= 4 ) ");
		}
		sb.append(" and (d.device_id_ex ='' or d.device_id_ex is null) ");
		if (cityList.size() > 0)
		{
			sb.append(" and d.city_id in " + cityIdStr + "");
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				map.put("city_name", rs.getString("city_name"));
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device_id_ex", rs.getString("device_id_ex"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(Long.parseLong(StringUtil.getStringValue(rs
						.getString("last_time"))
						+ "000"));
				String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal
						.getTime());
				map.put("last_time", date);
				return map;
			}
		});
		return list;
	}

	public List queryUnloid(String startTime, String endTime, String cityId)
	{
		StringBuffer sb = new StringBuffer();
		String sql = " select d.device_id,c.city_name,v.vendor_name,v.vendor_add,m.device_model,d.device_serialnumber,d.device_id_ex,d.loopback_ip,s.last_time from tab_gw_device d,"
				+ " tab_city c,tab_vendor v,gw_device_model m,gw_devicestatus s,tab_devicetype_info t where  d.city_id=c.city_id and d.vendor_id=v.vendor_id "
				+ " and d.device_model_id=m.device_model_id and d.device_id=s.device_id and d.devicetype_id=t.devicetype_id ";
		sb.append(sql);
		List<String> cityList = new ArrayList<String>();
		String cityIdStr = "";
		if (!cityId.equals("-1"))
		{
			cityIdStr = "(";
			cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++)
			{
				if (i + 1 == cityList.size())
				{
					cityIdStr = cityIdStr + "'"
							+ StringUtil.getStringValue(cityList.get(i)) + "')";
				}
				else
				{
					cityIdStr = cityIdStr + "'"
							+ StringUtil.getStringValue(cityList.get(i)) + "',";
				}
			}
		}
		if (startTime != null && !"".equals(startTime))
		{
			sb.append(" and s.last_time >= " + startTime);
		}
		if (endTime != null && !"".equals(endTime))
		{
			sb.append(" and s.last_time <= " + endTime);
		}
		if(Global.NXDX.equals(Global.instAreaShortName)){
			sb.append(" and t.rela_dev_type_id = 2 ");
		}else{
			sb.append("  and ( t.access_style_relay_id=3 or t.access_style_relay_id= 4 ) ");
		}
		sb.append(" and (d.device_id_ex ='' or d.device_id_ex is null ) ");
		if (cityList.size() > 0)
		{
			sb.append(" and d.city_id in " + cityIdStr + ""); 
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < list.size(); i++)
		{
			cal.setTimeInMillis(Long.parseLong(StringUtil.getStringValue(list.get(i).get(
					"last_time"))
					+ "000"));
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal
					.getTime());
			list.get(i).put("last_time", date);
		}
		return list;
	}

	public int getUnloidCount(String startTime, String endTime, String cityId,
			int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sb = new StringBuffer();
		String sql = " select count(*) from tab_gw_device d,"
				+ " tab_city c,tab_vendor v,gw_device_model m,gw_devicestatus s,tab_devicetype_info t where  d.city_id=c.city_id and d.vendor_id=v.vendor_id "
				+ " and d.device_model_id=m.device_model_id and d.device_id=s.device_id and d.devicetype_id=t.devicetype_id ";
		sb.append(sql);
		List<String> cityList = new ArrayList<String>();
		String cityIdStr = "";
		if (!cityId.equals("-1"))
		{
			cityIdStr = "(";
			cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++)
			{
				if (i + 1 == cityList.size())
				{
					cityIdStr = cityIdStr + "'"
							+ StringUtil.getStringValue(cityList.get(i)) + "')";
				}
				else
				{
					cityIdStr = cityIdStr + "'"
							+ StringUtil.getStringValue(cityList.get(i)) + "',";
				}
			}
		}
		if (startTime != null && !"".equals(startTime))
		{
			sb.append(" and s.last_time >= " + startTime + "");
		}
		if (endTime != null && !"".equals(endTime))
		{
			sb.append(" and s.last_time <= " + endTime + "");
		}
		if(Global.NXDX.equals(Global.instAreaShortName)){
			sb.append(" and t.rela_dev_type_id = 2 ");
		}else{
			sb.append("  and ( t.access_style_relay_id=3 or t.access_style_relay_id= 4 ) ");
		}
		sb.append(" and (d.device_id_ex ='' or d.device_id_ex is null ) ");
		if (cityList.size() > 0)
		{
			sb.append(" and d.city_id in " + cityIdStr + "");
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
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
}
