package com.linkage.module.gwms.report.dao;

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
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class MismathSpeedDAO extends SuperDAO{

	Logger logger = LoggerFactory.getLogger(MismathSpeedDAO.class);
	private Map<String, String> cityMap = null;
	
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
		return jt.queryForList(sql.toString());
	}
	
	public List<Map> getMismathSpeedCount(String cityId){
		
		HashMap<String,String> retMap = new HashMap<String,String>();
		HashMap<String,String> totalMap = new HashMap<String,String>();
		
		List<Map> retList1 = null;
		try
		{
			//针对二级属地例如794
			List<Map> retList2 = getMismathSpeedCount2(cityId);
			if(null != retList2 && !retList2.isEmpty() && retList2.size() > 0){
				for(Map maps : retList2){
					if(null == maps || maps.isEmpty()){
						continue;
					}
					retMap.put(StringUtil.getStringValue(maps, "city_id"), 
							StringUtil.getStringValue(maps, "total"));
				}
			}
			logger.warn("针对二级属地:{}",retMap);
			//针对三级属地 79401
			String dbCityId = "";
			int total = 0;
			int dbTotal = 0;
			cityMap = CityDAO.getCityIdCityNameMap();
			retList1 = getMismathSpeedCount1(cityId);
			logger.warn("针对三级属地:{}",retList1);
			
			for(Map maps : retList1){
				dbCityId = StringUtil.getStringValue(maps, "parent_id");
				dbTotal = StringUtil.getIntValue(maps, "total");
				
				if("00".equals(dbCityId) || "-1".equals(dbCityId)){
					maps.remove(dbCityId);
					continue;
				}
				if(!retMap.isEmpty() && retMap.containsKey(dbCityId)){
					dbTotal = dbTotal + StringUtil.getIntegerValue(retMap.get(dbCityId),0);
				}
				
				String cityName = StringUtil.getStringValue(cityMap.get(dbCityId));
				maps.put("city_name", cityName);
				maps.put("total", dbTotal);
				
				total = total + dbTotal;
			}
			totalMap.put("city_name","合计");
			totalMap.put("total", StringUtil.getStringValue(total));
			retList1.add(totalMap);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		cityMap = null;
		return retList1;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map> getMismathSpeedCount1(String cityId)
	{
		logger.warn("getMismathSpeedCount cityId -> {}",cityId);
		PrepareSQL sql = new PrepareSQL();
		sql.append("select ");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("sum(case when(cast(d.downlink as signed) >= 200 and f.gbbroadband=2) then 1 else 0 end) total,");
		}else{
			sql.append("sum(case when(to_number(d.downlink) >= 200 and f.gbbroadband=2) then 1 else 0 end) total,");
		}
		
		sql.append("t.parent_id from ");
		sql.append("tab_gw_device a,tab_hgwcustomer b,hgwcust_serv_info c,tab_netacc_spead d,tab_device_version_attribute f,tab_city t ");
		sql.append("where ");
		sql.append("a.device_id=b.device_id and b.user_id=c.user_id and c.serv_type_id=10 ");
		sql.append("and not exists (select 1 from tab_gw_ht_megabytes tt where a.device_name=tt.device_sn) ");
		sql.append("and c.username=d.username and a.devicetype_id=f.devicetype_id and a.city_id=t.city_id ");
		sql.append("group by t.parent_id order by t.parent_id ");
		
		return jt.queryForList(sql.toString());
	}
	
	public List<Map> getMismathSpeedCount2(String cityId)
	{
		logger.warn("getMismathSpeedCountForMore cityId -> {}",cityId);
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select aa.total,aa.city_id from (select ");
		}else{
			sql.append("select aa.* from (select ");
		}
		
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("sum(case when(cast(d.downlink as signed) >= 200 and f.gbbroadband=2) then 1 else 0 end) total,t.city_id ");
		}else{
			sql.append("sum(case when(TO_NUMBER(d.downlink) >= 200 and f.GBBROADBAND=2) then 1 else 0 end) total,t.city_id ");
		}
		
		sql.append("from ");
		sql.append("tab_gw_device a,tab_hgwcustomer b,hgwcust_serv_info c,tab_netacc_spead d,tab_device_version_attribute f,tab_city t ");
		sql.append("where ");
		sql.append("a.device_id=b.device_id and b.user_id=c.user_id and c.serv_type_id=10 ");
		sql.append("and c.username=d.username and a.devicetype_id=f.devicetype_id and a.city_id=t.city_id and t.parent_id='00'");
		sql.append("and NOT EXISTS (SELECT 1 FROM tab_gw_ht_megabytes tt WHERE a.device_name=tt.device_sn) ");
		sql.append("group by t.city_id order by t.city_id");
		sql.append(") aa where aa.total !=0");
		return jt.queryForList(sql.toString());
	}
	
	public List<Map> queryDetail(String cityId,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		//TODO wait
		sql.append("select ");
		sql.append("t.city_id,t.city_name,d.downlink,b.username loid,c.username,h.device_model,g.HARDWAREVERSION,g.SOFTWAREVERSION ");
		sql.append("from ");
		sql.append("tab_gw_device a,tab_hgwcustomer b,hgwcust_serv_info c,tab_netacc_spead d,tab_device_version_attribute f,");
		sql.append("tab_devicetype_info g,gw_device_model h,tab_city t ");
		sql.append("where ");
		sql.append("a.device_id=b.device_id and b.user_id=c.user_id and c.serv_type_id=10 ");
		sql.append("and c.username=d.username and a.devicetype_id=f.devicetype_id and a.city_id=t.city_id ");
		sql.append("and a.devicetype_id=g.devicetype_id and a.device_model_id = h.device_model_id ");
		
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("and cast(d.downlink as signed) >= 200 and f.gbbroadband=2 ");
		}else{
			sql.append("and to_number(d.downlink) >= 200 and f.gbbroadband=2 ");
		}
		sql.append("and not exists (select 1 from tab_gw_ht_megabytes tt where a.device_name=tt.device_sn) ");
		sql.append("and t.city_id in(select city_id from tab_city where city_id='" + cityId + "' or parent_id='" + cityId + "') ");
		sql.append("order by t.city_id"); 
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		final String paramCityId = cityId;
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city",StringUtil.getStringValue(cityMap.get(paramCityId)));
				
				map.put("city_id", StringUtil.getStringValue(rs.getString("city_id")));
				map.put("city_name", StringUtil.getStringValue(rs.getString("city_name")));
				map.put("downlink", StringUtil.getStringValue(rs.getString("downlink")));
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				map.put("net_user", StringUtil.getStringValue(rs.getString("username")));
				map.put("device_model", StringUtil.getStringValue(rs.getString("device_model")));
				map.put("hardwareversion", StringUtil.getStringValue(rs.getString("HARDWAREVERSION".toLowerCase())));
				map.put("softwareversion", StringUtil.getStringValue(rs.getString("SOFTWAREVERSION".toLowerCase())));
				 
				return map;
			}
		});
		cityMap = null;
		return list;
	}
	
	public int queryDetailCount(String cityId,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select count(*) from ");
		}else{
			sql.append("select count(1) from ");
		}
		
		sql.append("tab_gw_device a,tab_hgwcustomer b,hgwcust_serv_info c,tab_netacc_spead d,tab_device_version_attribute f,");
		sql.append("tab_devicetype_info g,gw_device_model h,tab_city t ");
		sql.append("where ");
		sql.append("a.device_id=b.device_id and b.user_id=c.user_id and c.serv_type_id=10 ");
		sql.append("and c.username=d.username and a.devicetype_id=f.devicetype_id and a.city_id=t.city_id ");
		sql.append("and a.devicetype_id=g.devicetype_id and a.device_model_id = h.device_model_id ");
		
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("and cast(d.downlink as signed) >= 200 and f.GBBROADBAND=2 ");
		}else{
			sql.append("and TO_NUMBER(d.downlink) >= 200 and f.GBBROADBAND=2 ");
		}
		sql.append("and NOT EXISTS (SELECT 1 FROM tab_gw_ht_megabytes tt WHERE a.device_name=tt.device_sn) ");
		sql.append("and t.city_id in(select city_id from tab_city where city_id='" + cityId + "' or parent_id='" + cityId + "') ");
		sql.append("order by t.city_id"); 
		 
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
	
	public List<Map> toExcel(String cityId)
	{
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		} */

		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append("bb.city_name company_name, aa.city_name,aa.downlink,aa.loid,aa.username,aa.device_model,aa.HARDWAREVERSION,aa.SOFTWAREVERSION ");
		sql.append("from ( ");
		sql.append("select ");
		
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("substring(a.city_id,2,3) company_id,");
		}else{
			sql.append("substr(a.city_id,1,3) company_id,");
		}
		sql.append("t.city_id,t.city_name,d.downlink,b.username loid,c.username,h.device_model,g.HARDWAREVERSION,g.SOFTWAREVERSION ");
		sql.append("from ");
		sql.append("tab_gw_device a,tab_hgwcustomer b,hgwcust_serv_info c,tab_netacc_spead d,tab_device_version_attribute f,");
		sql.append("tab_devicetype_info g,gw_device_model h,tab_city t ");
		sql.append("where ");
		sql.append("a.device_id=b.device_id and b.user_id=c.user_id and c.serv_type_id=10 ");
		sql.append("and c.username=d.username and a.devicetype_id=f.devicetype_id and a.city_id=t.city_id ");
		sql.append("and a.devicetype_id=g.devicetype_id and a.device_model_id = h.device_model_id ");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("and cast(d.downlink as signed) >= 200 and f.GBBROADBAND=2 ");
		}else{
			sql.append("and TO_NUMBER(d.downlink) >= 200 and f.GBBROADBAND=2 ");
		}
		
		sql.append("and NOT EXISTS (SELECT 1 FROM tab_gw_ht_megabytes tt WHERE a.device_name=tt.device_sn) ");
		sql.append("and t.city_id in(select city_id from tab_city where city_id='" + cityId + "' or parent_id='" + cityId + "') ");
		sql.append("order by t.city_id "); 
		sql.append(") aa, tab_city bb "); 
		sql.append("where aa.company_id = bb.city_id"); 
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(sql.toString());
	}
	
	public List<Map> getMismathChageCount(String cityId)
	{
		List<Map> list=null;
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select ifnull(a.city_id, '-1') city_id,");
			sql.append("sum(a.changed_num) total ");
			sql.append("from tab_jx_mismatch_report a ");
			sql.append("group by a.city_id");
			
			List l=jt.queryForList(sql.getSQL());
			if(l!=null && !l.isEmpty()){
				list=new ArrayList<Map>();
				int total=0;
				for(int i=0;i<l.size();i++)
				{
					Map m=(Map)l.get(i);
					String city_id=StringUtil.getStringValue(m,"city_id");
					if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
					{
						if(!cityId.equals(city_id)) {
							continue;
						}
					}
					
					String city_name=CityDAO.getCityName(city_id);
					if(StringUtil.IsEmpty(city_name)){
						continue;
					}
					
					Map map=new HashMap();
					map.put("city_id",city_id);
					map.put("city_name", city_name);
					map.put("total",StringUtil.getStringValue(m,"total"));
					
					total+=StringUtil.getIntValue(m,"total");
					list.add(map);
				}
				
				Map map=new HashMap();
				map.put("city_id","-1");
				map.put("city_name","合计");
				map.put("total",total+"");
				list.add(map);
			}
		}else{
			sql.append("select b.*,nvl(c.city_name, '合计') city_name  from ");
			sql.append("(select nvl(a.city_id, '-1') city_id,sum(a.changed_num) total ");
			sql.append("from tab_jx_mismatch_report a ");
			sql.append("group by rollup(a.city_id)) b ");
			sql.append("left join tab_city c on b.city_id=c.city_id ");
			
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
				sql.append("where b.city_id='"+cityId+"' ");
			}
			list=jt.queryForList(sql.getSQL());
		}
		return list;
	}
	
	public List<Map> getMismathChageDetail(String cityId,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select parent_id,city_id,downlink,loid,netusername,");
			sql.append("device_model,hardwareversion,softwareversion,oui_sn ");
		}else{
			sql.append("select * ");
		}
		sql.append("from tab_mismatch_dev_speed where oper_type=? and status=0 ");
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)){
			sql.append("and parent_id='" + cityId + "' "); 
		}
		sql.append("order by open_date desc ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setInt(1, 1);
		
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city",StringUtil.getStringValue(cityMap.get(rs.getString("parent_id"))));
				
				map.put("city_id", StringUtil.getStringValue(rs.getString("city_id")));
				map.put("city_name", StringUtil.getStringValue(cityMap.get(rs.getString("city_id"))));
				map.put("downlink", StringUtil.getStringValue(rs.getString("downlink")));
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				map.put("net_user", StringUtil.getStringValue(rs.getString("netusername")));
				map.put("device_model", StringUtil.getStringValue(rs.getString("device_model")));
				map.put("hardwareversion", StringUtil.getStringValue(rs.getString("hardwareversion")));
				map.put("softwareversion", StringUtil.getStringValue(rs.getString("softwareversion")));
				map.put("oui_sn", StringUtil.getStringValue(rs.getString("oui_sn")));
				return map;
			}
		});
		cityMap = null;
		return list;
	}
	
	public int getMismathChageDetailCount(String cityId,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_mismatch_dev_speed where oper_type=? and status=0 ");
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)){
			sql.append("and parent_id='" + cityId + "' "); 
		}
		sql.append("order by open_date desc  ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setInt(1, 1);
		 
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	public List<Map> toChageExcel(String cityId)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select g.city_name company_name,t.city_name,");
			psql.append("t.downlink,t.loid,t.netusername,t.device_model,");
			psql.append("t.hardwareversion,t.softwareversion,t.oui_sn ");
			psql.append("from (select a.company_name,a.downlink,a.loid,a.netusername,a.oui_sn,"); 
			psql.append("a.device_model,a.hardwareversion,a.softwareversion,b.city_name ");
		}else{
			psql.append("select g.city_name company_name,t.* "); 
			psql.append("from (select a.*,b.city_name "); 
		}
		psql.append("from tab_mismatch_dev_speed a,tab_city b ");
		psql.append("where a.city_id=b.city_id and a.oper_type=1 and status=0 "); 
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)){
			psql.append(" and a.parent_id='" + cityId + "'"); 
		}
		psql.append(") t,"); 
		psql.append("tab_city g where t.parent_id=g.city_id "); 
		
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 不匹配终端新增量
	 * @param cityId
	 * @return
	 */
	public List<Map> getMismathAddCount(String cityId)
	{
		List<Map> list=null;
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select ifnull(a.city_id, '-1') city_id,");
			sql.append("sum(a.increased_num) total ");
			sql.append("from tab_jx_mismatch_report a ");
			sql.append("group by a.city_id");
			
			List l=jt.queryForList(sql.getSQL());
			if(l!=null && !l.isEmpty()){
				list=new ArrayList<Map>();
				int total=0;
				for(int i=0;i<l.size();i++)
				{
					Map m=(Map)l.get(i);
					String city_id=StringUtil.getStringValue(m,"city_id");
					if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
					{
						if(!cityId.equals(city_id)) {
							continue;
						}
					}
					
					String city_name=CityDAO.getCityName(city_id);
					if(StringUtil.IsEmpty(city_name)){
						continue;
					}
					
					Map map=new HashMap();
					map.put("city_id",city_id);
					map.put("city_name", city_name);
					map.put("total",StringUtil.getStringValue(m,"total"));
					
					total+=StringUtil.getIntValue(m,"total");
					list.add(map);
				}
				
				Map map=new HashMap();
				map.put("city_id","-1");
				map.put("city_name","合计");
				map.put("total",total+"");
				list.add(map);
			}
		}else{
			sql.append("select b.*,nvl(c.city_name, '合计') city_name  from ");
			sql.append("(select nvl(a.city_id, '-1') city_id,sum(a.increased_num) total ");
			sql.append("from tab_jx_mismatch_report a ");
			sql.append("group by rollup(a.city_id)) b ");
			sql.append("left join tab_city c on b.city_id=c.city_id ");
			
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
				sql.append("where b.city_id='"+cityId+"' ");
			}
			list=jt.queryForList(sql.getSQL());
		}
		return list;
	}
	
	public List<Map> getMismathAddDetail(String cityId,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select parent_id,city_id,downlink,loid,netusername,oui_sn,");
			sql.append("device_model,hardwareversion,softwareversion,open_date,is_month ");
		}else{
			sql.append("select * ");
		}
		sql.append("from tab_mismatch_dev_speed where oper_type=? ");
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)){
			sql.append("and parent_id='" + cityId + "' "); 
		}
		sql.append("order by open_date desc ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setInt(1, 2);
		
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city",StringUtil.getStringValue(cityMap.get(rs.getString("parent_id"))));
				
				map.put("city_id", StringUtil.getStringValue(rs.getString("city_id")));
				map.put("city_name", StringUtil.getStringValue(cityMap.get(rs.getString("city_id"))));
				map.put("downlink", StringUtil.getStringValue(rs.getString("downlink")));
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				map.put("net_user", StringUtil.getStringValue(rs.getString("netusername")));
				map.put("device_model", StringUtil.getStringValue(rs.getString("device_model")));
				map.put("hardwareversion", StringUtil.getStringValue(rs.getString("hardwareversion")));
				map.put("softwareversion", StringUtil.getStringValue(rs.getString("softwareversion")));
				map.put("oui_sn", StringUtil.getStringValue(rs.getString("oui_sn")));
				map.put("opendate", StringUtil.getStringValue(rs.getString("open_date")));
				map.put("isMonth", "1".equals(StringUtil.getStringValue(rs.getString("is_month")))?"是":"否");
				return map;
			}
		});
		cityMap = null;
		return list;
	}
	
	public int getMismathAddDetailCount(String cityId,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_mismatch_dev_speed where oper_type=? ");
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)){
			sql.append("and parent_id='" + cityId + "' "); 
		}
		sql.append("order by open_date desc  ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setInt(1, 2);
		 
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	public List<Map> toAddExcel(String cityId)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select g.city_name company_name,t.city_name,t.downlink,t.loid,t.netusername,"); 
			sql.append("t.device_model,t.hardwareversion,t.softwareversion,t.oui_sn,t.open_date,");
			sql.append("(case when(t.is_month=1) then '是' else '否' end) currMonth from ");
			sql.append("(select a.downlink,a.loid,a.netusername,a.device_model,a.oui_sn,");
			sql.append("a.hardwareversion,a.softwareversion,a.open_date,a.is_month,b.city_name ");
		}else{
			sql.append("select g.city_name company_name,t.*,"); 
			sql.append("(case when(t.is_month=1) then '是' else '否' end) currMonth from ");
			sql.append("(select a.*,b.city_name "); 
		}
		sql.append("from tab_mismatch_dev_speed a,tab_city b ");
		sql.append("where a.city_id=b.city_id and a.oper_type=2"); 
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)){
			sql.append(" and a.parent_id='" + cityId + "'"); 
		}
		sql.append(") t, "); 
		sql.append("tab_city g where t.parent_id=g.city_id "); 
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		return jt.queryForList(psql.getSQL());
	}
}
