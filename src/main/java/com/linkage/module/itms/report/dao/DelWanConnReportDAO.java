package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("rawtypes")
public class DelWanConnReportDAO extends SuperDAO
{
	/** Map<city_id,city_name> */
	private Map<String, String> cityMap = null;
	

	public List countDelWanConnResult(String starttime1, String endtime1,String cityId) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select b.city_id,a.status,count(*) as total ");
		}else{
			sql.append("select b.city_id,a.status,count(1) as total ");
		}
		sql.append("from tab_delwanconn_detail a,tab_gw_device b ");
		sql.append("where a.device_id=b.device_id ");
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ("+StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.add_time>=").append(starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.add_time<=").append(endtime1);
		}
		sql.append(" group by b.city_id,a.status");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	public List<Map> getDevList(String starttime1, String endtime1,
			String cityId, String status, int curPage_splitPage,int num_splitPage) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.loid,a.serv_type,a.vlanid,b.device_id," +
				"b.city_id,b.device_serialnumber,b.loopback_ip,a.res_desc ");
		sql.append("from tab_delwanconn_detail a,tab_gw_device b ");
		sql.append("where a.device_id=b.device_id ");
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.add_time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.add_time<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(status)){
			sql.append(" and a.status=").append(status);
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (!StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("res_desc", rs.getString("res_desc"));
				map.put("loid", rs.getString("loid"));
				map.put("serv_type", rs.getString("serv_type"));
				map.put("vlanid", rs.getString("vlanid"));
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	public int getDevCount(String starttime1, String endtime1, String cityId,
			String status, int curPage_splitPage, int num_splitPage) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) ");
		sql.append("from tab_delwanconn_detail a,tab_gw_device b ");
		sql.append("where a.device_id=b.device_id ");
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.add_time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.add_time<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(status)){
			sql.append(" and a.status=").append(status);
		}
		cityMap = CityDAO.getCityIdCityNameMap();
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

	@SuppressWarnings("unchecked")
	public List<Map> getDevExcel(String starttime1, String endtime1,
			String cityId, String status) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.loid,a.serv_type,a.vlanid,b.device_id," +
				"b.city_id,b.device_serialnumber,b.loopback_ip,a.res_desc ");
		sql.append("from tab_delwanconn_detail a,tab_gw_device b ");
		sql.append("where a.device_id=b.device_id ");
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.add_time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.add_time<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(status)){
			sql.append(" and a.status=").append(status);
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				map.put("city_name", StringUtil.getStringValue(cityMap,rs.getString("city_id"),""));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("res_desc", rs.getString("res_desc"));
				map.put("loid", rs.getString("loid"));
				map.put("serv_type", rs.getString("serv_type"));
				map.put("vlanid", rs.getString("vlanid"));
				return map;
			}
		});
		cityMap = null;
		return list;
	}

}
