package com.linkage.module.ids.dao;

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
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2014-2-13
 * @category com.linkage.module.ids.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class PPPoEFailReasonCountDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(PPPoEFailReasonCountDAO.class);
	private Map<String, String> cityMap = null;
	
	public Map<String,Long> getPppoeFailReasonCountList(String start_time, String end_time,String city_id){
		logger.debug("PPPoEFailReasonCountDAO=>getPppoeFailReasonCountList({})");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,a.reason,count(1) num  from tab_netparam_info a, tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.status<>'Connected' ");
		
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.upload_time<=").append(end_time);
		}
		
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" group by b.city_id,a.reason");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		
		Map<String,Long> resultMap = new HashMap<String, Long>();  
		
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++)
			{
				resultMap.put(StringUtil.getStringValue(list.get(i).get("city_id"))+StringUtil.getStringValue(list.get(i).get("reason")),StringUtil.getLongValue(list.get(i).get("num")));
			}
		}
		
		return resultMap;
	}
	
	public List<Map> getPppoeFailReasonInfo(String start_time, String end_time,String city_id, String failCode,int curPage_splitPage, int num_splitPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,b.device_serialnumber,b.device_type,a.reason,a.status,a.upload_time,a.loid  from tab_netparam_info a, tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber   ");
		if(!StringUtil.IsEmpty(failCode)){
			sql.append(" and a.reason='").append(failCode).append("' ");
		}
		
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.upload_time<=").append(end_time);
		}
		
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
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
				String cityid = rs.getString("city_id");
				map.put("cityid", cityid);
				map.put("city_name", StringUtil.getStringValue(cityMap.get(cityid)));
				map.put("loid", rs.getString("loid"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device_type", rs.getString("device_type"));
				map.put("reason", StringUtil.getStringValue(rs.getString("reason")));
				map.put("status", rs.getString("status"));
				long lms = StringUtil.getLongValue(rs.getString("upload_time"));
				map.put("upload_time", StringUtil.formatDate("yyyy-MM-dd HH:mm:ss", lms));
				return map;
			}
		});
		return list;
	}
	
	public int countGetPppoeFailReasonInfo(String start_time, String end_time,String city_id, String failCode,int curPage_splitPage, int num_splitPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1)  from tab_netparam_info a, tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.status<>'Connected' ");
		if(!StringUtil.IsEmpty(failCode)){
			sql.append(" and a.reason='").append(failCode).append("' ");
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.upload_time<=").append(end_time);
		}
		
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
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
	

	
	public List<Map> getPppoeFailReasonInfoExcel(String start_time, String end_time,String city_id, String failCode){
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,b.device_serialnumber,b.device_type,a.reason,a.status,a.upload_time,a.loid  from tab_netparam_info a, tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber   ");
		if(!StringUtil.IsEmpty(failCode)){
			sql.append(" and a.reason='").append(failCode).append("' ");
		}
		
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.upload_time<=").append(end_time);
		}
		
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String cityId = StringUtil.getStringValue(list.get(i).get("city_id"));
				list.get(i).put("city_id",cityId);
				list.get(i).put("city_name",StringUtil.getStringValue(cityMap.get(cityId)));
				list.get(i).put("loid",StringUtil.getStringValue(list.get(i).get("loid")));
				list.get(i).put("device_serialnumber",StringUtil.getStringValue(list.get(i).get("device_serialnumber")));
				list.get(i).put("device_type",StringUtil.getStringValue(list.get(i).get("device_type")));
				list.get(i).put("reason",StringUtil.getStringValue(list.get(i).get("reason")));
				list.get(i).put("status",StringUtil.getStringValue(list.get(i).get("status")));
				list.get(i).put("upload_time",StringUtil.formatDate("yyyy-MM-dd HH:mm:ss", StringUtil.getLongValue((list.get(i).get("upload_time")))));
			}
		}
		return list;
	}
	
}
