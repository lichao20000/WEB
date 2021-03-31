package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


public class TerminalEnvironmentStatementDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(TerminalEnvironmentStatementDAO.class);
	
	private Map<String,String> cityMap = new HashMap<String, String>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,String> queryTerminalEnvironmentStatement(String city_id, String start_time, String end_time,String temperature,String bais_current, String voltage){
		logger.debug("TerminalEnvironmentStatementDAO=>queryTerminalEnvironmentStatement()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(distinct b.device_serialnumber) num from  tab_gw_device a,  tab_ponstatus_info b where a.oui = b.oui and a.device_serialnumber=b.device_serialnumber ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and b.upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and b.upload_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(temperature))
		{
			sql.append(" and b.temperature>").append(temperature);
		}
		
		if(!StringUtil.IsEmpty(bais_current)){
			sql.append(" and to_number(b.bais_current)>").append(bais_current);
		}
		
		if(!StringUtil.IsEmpty(voltage)){
			sql.append(" and to_number(b.vottage)>").append(voltage);
		}
		
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" group by a.city_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> queryTerminalEnvironmentStatementList(String city_id, String start_time, String end_time,String temperature,String bais_current,String voltage, int curPage_splitPage, int num_splitPage){
		logger.debug("TerminalEnvironmentStatementDAO==>queryTerminalEnvironmentStatementList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,a.device_serialnumber,a.device_type,b.loid,b.temperature,b.bais_current,b.vottage,b.upload_time from tab_ponstatus_info b,tab_gw_device a where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and b.upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and b.upload_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(temperature))
		{
			sql.append(" and b.temperature>").append(temperature);
		}
		
		if(!StringUtil.IsEmpty(bais_current)){
			sql.append(" and to_number(b.bais_current)>").append(bais_current);
		}
		if(!StringUtil.IsEmpty(voltage)){
			sql.append(" and to_number(b.vottage)>").append(voltage);
		}
		
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)  && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" order by a.city_id ");
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL pSql = new PrepareSQL(sql.toString());
		
		List<Map> list = new ArrayList<Map>();
		list = querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					@Override
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String cityId = StringUtil.getStringValue(rs.getString("city_id"));
						map.put("city_id",cityId);
						map.put("city_name",cityMap.get(cityId));
						map.put("device_serialnumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
						map.put("device_type", StringUtil.getStringValue(rs.getString("device_type")));
						map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
						map.put("temperature", StringUtil.getStringValue(rs.getString("temperature")));
						map.put("bais_current", StringUtil.getStringValue(rs.getString("bais_current")));
						map.put("vottage", StringUtil.getStringValue(rs.getString("vottage")));
						try
						{
							long opertime = StringUtil.getLongValue(rs.getString("upload_time")) * 1000L;
							DateTimeUtil dt = new DateTimeUtil(opertime);
							map.put("upload_time", dt.getLongDate());
						}
						catch (NumberFormatException e)
						{
							map.put("upload_time", "");
						}
						catch (Exception e)
						{
							map.put("upload_time", "");
						}
						return map;
					}
				});
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public int countQueryTerminalEnvironmentStatementList(String city_id, String start_time, String end_time,String temperature,String bais_current,String voltage, int curPage_splitPage, int num_splitPage){
		logger.debug("TerminalEnvironmentStatementDAO==>countQueryTerminalEnvironmentStatementList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from  tab_gw_device a ,  tab_ponstatus_info b ");
		sql.append("where a.oui = b.oui and a.device_serialnumber=b.device_serialnumber  ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and b.upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and b.upload_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(temperature))
		{
			sql.append(" and b.temperature>").append(temperature);
		}
		
		if(!StringUtil.IsEmpty(bais_current)){
			sql.append(" and to_number(b.bais_current)>").append(bais_current);
		}
		if(!StringUtil.IsEmpty(voltage)){
			sql.append(" and to_number(b.vottage)>").append(voltage);
		}
		
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)  && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> excelQueryTerminalEnvironmentStatementList(String city_id, String start_time, String end_time,String temperature,String bais_current,String voltage ){
		logger.debug("TerminalEnvironmentStatementDAO==>excelQueryTerminalEnvironmentStatementList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,a.device_serialnumber,a.device_type,b.loid,b.temperature,b.bais_current,b.vottage,b.upload_time from tab_ponstatus_info b,tab_gw_device a where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and b.upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and b.upload_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(temperature))
		{
			sql.append(" and b.temperature>").append(temperature);
		}
		
		
		if(!StringUtil.IsEmpty(bais_current)){
			sql.append(" and to_number(b.bais_current)>").append(bais_current);
		}
		if(!StringUtil.IsEmpty(voltage)){
			sql.append(" and to_number(b.vottage)>").append(voltage);
		}
		
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)  && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" order by a.city_id ");
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL pSql = new PrepareSQL(sql.toString());
		
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSql.getSQL());
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++)
			{
				String cityId = StringUtil.getStringValue(list.get(i).get("city_id"));
				list.get(i).put("city_id", cityId);
				list.get(i).put("city_name", cityMap.get(cityId));
				list.get(i).put("device_serialnumber",StringUtil.getStringValue(list.get(i).get("device_serialnumber")));
				list.get(i).put("device_type",StringUtil.getStringValue(list.get(i).get("device_type")));
				list.get(i).put("loid",StringUtil.getStringValue(list.get(i).get("loid")));
				list.get(i).put("temperature",StringUtil.getStringValue(list.get(i).get("temperature")));
				list.get(i).put("bais_current",StringUtil.getStringValue(list.get(i).get("bais_current")));
				list.get(i).put("vottage",StringUtil.getStringValue(list.get(i).get("vottage")));
				try
				{
					long opertime = StringUtil.getLongValue(list.get(i).get("upload_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(opertime);
					list.get(i).put("upload_time", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					list.get(i).put("upload_time", "");
				}
				catch (Exception e)
				{
					list.get(i).put("upload_time", "");
				}
			}
		}
		return list;
	}
}
