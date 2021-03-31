package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


public class E8CActiveReportDAO extends SuperDAO
{
	private Map<String, String> cityMap = new HashMap<String, String>();
	
	
	public Map getE8CNum(String startDealdate,String endDealdate,String cityId,String isActive){
		StringBuffer sql=new StringBuffer();
		Date date=new Date();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateTimeUtil dt = new DateTimeUtil(fmt.format(date));
		String nowTime = String.valueOf(dt.getLongTime());
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,count(*) num ");
		}else{
			sql.append("select a.city_id,count(1) num ");
		}
		sql.append("from tab_hgwcustomer_report a,gw_cust_user_dev_type b");
		sql.append(" where a.user_id=b.user_id");
		sql.append(" and a.opmode='1' and b.type_id='2' and a.is_active=1");
		if(!StringUtil.IsEmpty(startDealdate)){
			sql.append(" and a.dealdate>").append(startDealdate);
		}
		if(!StringUtil.IsEmpty(endDealdate)){
			sql.append(" and a.dealdate<").append(endDealdate);
		}
		List cityIdList  = null;
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
//		if(!StringUtil.IsEmpty(isActive)&&!"-1".equals(isActive)){
//			sql.append(" and a.is_active=").append(isActive);
//		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list=jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("num")));
			}
		}
		return map;
	}
	/**
	 * 绑定活跃终端的用户数
	 * @param startDealdate
	 * @param endDealdate
	 * @param cityId
	 * @param isActive
	 * @return
	 */
	public Map getBindNum(String startDealdate,String endDealdate,String cityId,String isActive){
		StringBuffer sql=new StringBuffer();
		Date date=new Date();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateTimeUtil dt = new DateTimeUtil(fmt.format(date));
		String nowTime = String.valueOf(dt.getLongTime());
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select a.city_id city_id,count(*) num ");
		}else{
			sql.append("select a.city_id city_id,count(1) num ");
		}
		sql.append("from tab_hgwcustomer_report a,tab_gw_device b,gw_devicestatus c,gw_cust_user_dev_type d ");
		sql.append("where a.device_id=b.device_id and a.user_id=d.user_id and b.device_id=c.device_id ");
		sql.append("and a.opmode='1' and a.is_active=1 and d.type_id='2' ");
		sql.append("and "+nowTime+"-c.last_time<"+2592000);
		if(!StringUtil.IsEmpty(startDealdate)){
			sql.append(" and a.dealdate>").append(startDealdate);
		}
		if(!StringUtil.IsEmpty(endDealdate)){
			sql.append(" and a.dealdate<").append(endDealdate);
		}
		List cityIdList  = null;
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
//		if(!StringUtil.IsEmpty(isActive)&&!"-1".equals(isActive)){
//			sql.append(" and a.is_active=").append(isActive);
//		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list=jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("num")));
			}
		}
		return map;
	}
	
	/**
	 * E8-C活跃竣工用户数中未绑定终端数
	 * @param startDealdate
	 * @param endDealdate
	 * @param cityId
	 * @param isActive
	 * @return
	 */
	public Map getE8cNoDevice(String startDealdate,String endDealdate,String cityId,String isActive)
	{
		
		Date date=new Date();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateTimeUtil dt = new DateTimeUtil(fmt.format(date));
		String nowTime = String.valueOf(dt.getLongTime());
		
		StringBuffer sql=new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,count(*) num ");
		}else{
			sql.append("select a.city_id,count(1) num ");
		}
		sql.append("from tab_hgwcustomer_report a,gw_cust_user_dev_type b");
		sql.append(" where a.user_id=b.user_id");
		sql.append(" and a.opmode='1' and b.type_id='2' and a.is_active=1 and a.device_id=null");
		if(!StringUtil.IsEmpty(startDealdate)){
			sql.append(" and a.dealdate>").append(startDealdate);
		}
		if(!StringUtil.IsEmpty(endDealdate)){
			sql.append(" and a.dealdate<").append(endDealdate);
		}
		List cityIdList  = null;
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
//		if(!StringUtil.IsEmpty(isActive)&&!"-1".equals(isActive)){
//			sql.append(" and a.is_active=").append(isActive);
//		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list=jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("num")));
			}
		}
		return map;
	}
	
	/**
	 * E8-C活跃竣工用户数中绑定不活跃终端的用户数
	 * @param startDealdate
	 * @param endDealdate
	 * @param cityId
	 * @param isActive
	 * @return
	 */
	public Map getE8cNotIsActive(String startDealdate,String endDealdate,String cityId,String isActive)
	{
		Date date=new Date();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateTimeUtil dt = new DateTimeUtil(fmt.format(date));
		String nowTime = String.valueOf(dt.getLongTime());
		
		StringBuffer sql=new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select a.city_id,count(*) num ");
		}else{
			sql.append("select a.city_id,count(1) num ");
		}
		sql.append("from tab_hgwcustomer_report a,gw_cust_user_dev_type b,tab_gw_device c,gw_devicestatus d");
		sql.append(" where a.user_id=b.user_id and a.device_id=c.device_id and c.device_id=d.device_id");
		sql.append(" and a.opmode='1' and b.type_id='2' and a.is_active=1");
		sql.append(" and "+nowTime+"-d.last_time>"+2592000);
		if(!StringUtil.IsEmpty(startDealdate)){
			sql.append(" and a.dealdate>").append(startDealdate);
		}
		if(!StringUtil.IsEmpty(endDealdate)){
			sql.append(" and a.dealdate<").append(endDealdate);
		}
		List cityIdList  = null;
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
//		if(!StringUtil.IsEmpty(isActive)&&!"-1".equals(isActive)){
//			sql.append(" and a.is_active=").append(isActive);
//		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list=jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("num")));
			}
		}
		return map;
		
	}
	
	
	public List<Map> getCustomerLists(String startDealdate,String endDealdate,
			String cityId,String isActive,String type,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql=new StringBuffer();
		
		StringBuffer sql2=new StringBuffer();
		
		Date date=new Date();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateTimeUtil dt = new DateTimeUtil(fmt.format(date));
		String nowTime = String.valueOf(dt.getLongTime());
		if(type.equals("1")){
//			sql.append("select a.username loid,a.city_id city_id,a.is_active,a.device_serialnumber serial,a.opendate opendate,a.device_id device_id,a.dealdate");
//			sql.append("  from tab_hgwcustomer a,gw_cust_user_dev_type b");
//			sql.append("   where a.user_id = b.user_id and a.opmode = '1' and a.is_active = 1 and b.type_id='2'");
			sql.append("select a.username loid,a.city_id city_id,a.is_active,a.device_serialnumber serial,a.opendate opendate,a.device_id device_id,a.dealdate from tab_hgwcustomer_report a,gw_cust_user_dev_type b");
			sql.append(" where a.user_id=b.user_id");
			sql.append(" and a.opmode='1' and b.type_id='2' and a.is_active=1");
			if(!StringUtil.IsEmpty(startDealdate)){
				sql.append(" and a.dealdate>").append(startDealdate);
			}
			if(!StringUtil.IsEmpty(endDealdate)){
				sql.append(" and a.dealdate<").append(endDealdate);
			}
			List cityIdList  = null;
			if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
				cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
						.append(")");
				cityIdList = null;
			}
//			if(!StringUtil.IsEmpty(isActive)&&!"-1".equals(isActive)){
//				sql.append(" and a.is_active=").append(isActive);
//			}
			cityMap = CityDAO.getCityIdCityNameMap();
			PrepareSQL psql = new PrepareSQL(sql.toString());
			List<Map> list=querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage, new RowMapper() {
				
				@Override
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("LOID", rs.getString("loid"));
					String city_id=rs.getString("city_id");
					map.put("city_id", city_id);
					String city_name = StringUtil.getStringValue(cityMap.get(city_id));
					if (false == StringUtil.IsEmpty(city_name))
					{
						map.put("city_name", city_name);
					}
					else
					{
						map.put("city_name", "");
					}
					int is_active=rs.getInt("is_active");
					map.put("is_active",String.valueOf(is_active));
					if(false == StringUtil.IsEmpty(String.valueOf(is_active))){
						if(is_active==0){
							map.put("active_name", "否");
						}else{
							map.put("active_name", "是");
						}
					}
					if(rs.getString("device_id")!=null){
						map.put("last_time", DateUtil.transTime(Long.parseLong(getLastTime(rs.getString("device_id"))), "yyyy-MM-dd HH:mm:ss"));
					}
					map.put("serial", rs.getString("serial"));
					map.put("opendate", DateUtil.transTime(rs.getLong("opendate"), "yyyy-MM-dd HH:mm:ss"));
					return map;
				}
			});
			cityMap=null;
			return list;
		}else{
			if(!StringUtil.IsEmpty(isActive)&&"0".equals(isActive)){
				sql.append("select a.username loid,a.city_id city_id,a.is_active is_active,a.device_serialnumber serial,a.opendate opendate,1 last_time " +
						"from tab_hgwcustomer_report a,gw_cust_user_dev_type b");
				sql.append(" where a.user_id=b.user_id");
				sql.append(" and a.opmode='1' and b.type_id='2' and a.is_active=1 and a.device_id=null");
				if(!StringUtil.IsEmpty(startDealdate)){
					sql.append(" and a.dealdate>").append(startDealdate);
				}
				if(!StringUtil.IsEmpty(endDealdate)){
					sql.append(" and a.dealdate<").append(endDealdate);
				}
				List cityIdList  =null;
				if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
					cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
					sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
							.append(")");
					cityIdList = null;
				}
				
				/**	if(DBUtil.GetDB()==3){
					//TODO wait
				}else{
					
				} */
				sql.append(" UNION ALL " +
						"select a.username loid,a.city_id city_id,a.is_active is_active,a.device_serialnumber serial,a.opendate opendate,d.last_time last_time " +
						"from tab_hgwcustomer_report a,gw_cust_user_dev_type b,tab_gw_device c,gw_devicestatus d");
				sql.append(" where a.user_id=b.user_id and a.device_id=c.device_id and c.device_id=d.device_id");
				sql.append(" and a.opmode='1' and b.type_id='2' and a.is_active=1");
				sql.append(" and "+nowTime+"-d.last_time>"+2592000);
				if(!StringUtil.IsEmpty(startDealdate)){
					sql.append(" and a.dealdate>").append(startDealdate);
				}
				if(!StringUtil.IsEmpty(endDealdate)){
					sql.append(" and a.dealdate<").append(endDealdate);
				}
				cityIdList  =null;
				if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
					cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
					sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
							.append(")");
					cityIdList = null;
				}
				cityMap = CityDAO.getCityIdCityNameMap();
				PrepareSQL psql = new PrepareSQL(sql.toString());
				List<Map> list=querySP(psql.getSQL(),(curPage_splitPage - 1) * num_splitPage + 1, num_splitPage, new RowMapper() {
					
					@Override
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("LOID", rs.getString("loid"));
						String city_id=rs.getString("city_id");
						map.put("city_id", city_id);
						String city_name = StringUtil.getStringValue(cityMap.get(city_id));
						if (false == StringUtil.IsEmpty(city_name))
						{
							map.put("city_name", city_name);
						}
						else
						{
							map.put("city_name", "");
						}
						int is_active=rs.getInt("is_active");
						map.put("is_active",String.valueOf(is_active));
						if(false == StringUtil.IsEmpty(String.valueOf(is_active))){
							if(is_active==0){
								map.put("active_name", "否");
							}else{
								map.put("active_name", "是");
							}
						}
						map.put("serial", rs.getString("serial"));
						if(rs.getLong("last_time")==1){
							map.put("last_time","");
						}else{
							map.put("last_time",DateUtil.transTime(rs.getLong("last_time"), "yyyy-MM-dd HH:mm:ss") );
						}
						map.put("opendate", DateUtil.transTime(rs.getLong("opendate"), "yyyy-MM-dd HH:mm:ss"));
						return map;
					}
				});
				return list;
				
			}else{
				/**	if(DBUtil.GetDB()==3){
					//TODO wait
				}else{
					
				}*/
				sql.append("select a.username loid,a.city_id city_id,a.is_active is_active,a.device_serialnumber serial,a.opendate opendate,c.last_time last_time " +
						"from tab_hgwcustomer_report a,tab_gw_device b,gw_devicestatus c,gw_cust_user_dev_type d");
				sql.append(" where a.device_id=b.device_id and a.user_id=d.user_id and b.device_id=c.device_id and a.opmode='1' and  a.is_active=1 and d.type_id='2'");
				sql.append(" and "+nowTime+"-c.last_time<"+2592000);
				if(!StringUtil.IsEmpty(startDealdate)){
					sql.append(" and a.dealdate>").append(startDealdate);
				}
				if(!StringUtil.IsEmpty(endDealdate)){
					sql.append(" and a.dealdate<").append(endDealdate);
				}
				List cityIdList  =null;
				if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
					cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
					sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
							.append(")");
					cityIdList = null;
				}
				cityMap = CityDAO.getCityIdCityNameMap();
				PrepareSQL psql = new PrepareSQL(sql.toString());
				List<Map> list=querySP(psql.getSQL(),(curPage_splitPage - 1) * num_splitPage + 1, num_splitPage, new RowMapper() {
					
					@Override
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("LOID", rs.getString("loid"));
						String city_id=rs.getString("city_id");
						map.put("city_id", city_id);
						String city_name = StringUtil.getStringValue(cityMap.get(city_id));
						if (false == StringUtil.IsEmpty(city_name))
						{
							map.put("city_name", city_name);
						}
						else
						{
							map.put("city_name", "");
						}
						int is_active=rs.getInt("is_active");
						map.put("is_active",String.valueOf(is_active));
						if(false == StringUtil.IsEmpty(String.valueOf(is_active))){
							if(is_active==0){
								map.put("active_name", "否");
							}else{
								map.put("active_name", "是");
							}
						}
						map.put("serial", rs.getString("serial"));
						map.put("last_time",DateUtil.transTime(rs.getLong("last_time"), "yyyy-MM-dd HH:mm:ss") );
						map.put("opendate", DateUtil.transTime(rs.getLong("opendate"), "yyyy-MM-dd HH:mm:ss"));
						return map;
					}
				});
				return list;
			}
			
		}
	}
	
	public int queryCusCount(String startDealdate,String endDealdate,String cityId,String isActive,String type,int curPage_splitPage, int num_splitPage){
		StringBuffer sql=new StringBuffer();
		
		StringBuffer sql2=new StringBuffer();
		
		Date date=new Date();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateTimeUtil dt = new DateTimeUtil(fmt.format(date));
		String nowTime = String.valueOf(dt.getLongTime());
		if(type.equals("1")){
			if(DBUtil.GetDB()==3){
				sql.append("select count(*) ");
			}else{
				sql.append("select count(1) ");
			}
			
			sql.append("from tab_hgwcustomer_report a,gw_cust_user_dev_type b ");
			sql.append("where a.user_id = b.user_id and a.opmode = '1' and a.is_active = 1 and b.type_id='2'");
			
			if(!StringUtil.IsEmpty(startDealdate)){
				sql.append(" and a.dealdate>").append(startDealdate);
			}
			if(!StringUtil.IsEmpty(endDealdate)){
				sql.append(" and a.dealdate<").append(endDealdate);
			}
			List cityIdList  = null;
			if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
				cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
						.append(")");
				cityIdList = null;
			}
//			if(!StringUtil.IsEmpty(isActive)&&!"-1".equals(isActive)){
//				sql.append(" and a.is_active=").append(isActive);
//			}
			PrepareSQL psql = new PrepareSQL();
			psql.setSQL(sql.toString());
			int total = jt.queryForInt(psql.getSQL());
			int maxPage = 1;
			if (total % num_splitPage == 0){
				maxPage = total / num_splitPage;
			}else{
				maxPage = total / num_splitPage + 1;
			}
			return maxPage;
		}else{
			if(!StringUtil.IsEmpty(isActive)&&"0".equals(isActive)){
				if(DBUtil.GetDB()==3){
					sql.append("select count(*) ");
				}else{
					sql.append("select count(1) ");
				}
				sql.append("from tab_hgwcustomer_report a,gw_cust_user_dev_type b");
				sql.append(" where a.user_id=b.user_id");
				sql.append(" and a.opmode='1' and b.type_id='2' and a.is_active=1 and a.device_id=null");
				
				if(DBUtil.GetDB()==3){
					//TODO wait
					sql2.append("select count(*) ");
				}else{
					sql2.append("select count(1) ");
				}
				sql2.append("from tab_hgwcustomer_report a,gw_cust_user_dev_type b,tab_gw_device c,gw_devicestatus d");
				sql2.append(" where a.user_id=b.user_id and a.device_id=c.device_id and c.device_id=d.device_id");
				sql2.append(" and a.opmode='1' and b.type_id='2' and a.is_active=1");
				sql2.append(" and "+nowTime+"-d.last_time>"+2592000);
				if(!StringUtil.IsEmpty(startDealdate)){
					sql.append(" and a.dealdate>").append(startDealdate);
					sql2.append(" and a.dealdate>").append(startDealdate);
				}
				if(!StringUtil.IsEmpty(endDealdate)){
					sql.append(" and a.dealdate<").append(endDealdate);
					sql2.append(" and a.dealdate<").append(endDealdate);
				}
				List cityIdList  =null;
				if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
					cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
					sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
							.append(")");
					sql2.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
					cityIdList = null;
				}
				cityMap = CityDAO.getCityIdCityNameMap();
				PrepareSQL psql = new PrepareSQL(sql.toString());
				int total1 = jt.queryForInt(psql.getSQL());
				PrepareSQL psql2 = new PrepareSQL(sql2.toString());
				int total2 = jt.queryForInt(psql2.getSQL());
				
				int total =total1+total2;
				int maxPage = 1;
				if (total % num_splitPage == 0){
					maxPage = total / num_splitPage;
				}else{
					maxPage = total / num_splitPage + 1;
				}
				return maxPage;
				
			}else{
				if(DBUtil.GetDB()==3){
					//TODO wait
					sql.append("select count(*) ");
				}else{
					sql.append("select count(1) ");
				}
				sql.append("from tab_hgwcustomer_report a,tab_gw_device b,gw_devicestatus c,gw_cust_user_dev_type d ");
				sql.append("where a.device_id=b.device_id and a.user_id=d.user_id and b.device_id=c.device_id " +
						"and a.opmode='1' and  a.is_active=1 and d.type_id='2' ");
				sql.append("and "+nowTime+"-c.last_time<"+2592000);
				if(!StringUtil.IsEmpty(startDealdate)){
					sql.append(" and a.dealdate>").append(startDealdate);
				}
				if(!StringUtil.IsEmpty(endDealdate)){
					sql.append(" and a.dealdate<").append(endDealdate);
				}
//				if(!StringUtil.IsEmpty(isActive)&&!"-1".equals(isActive)){
//					sql.append(" and a.is_active=").append(isActive);
//				}
				List cityIdList  =null;
				if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
					cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
					sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
							.append(")");
					cityIdList = null;
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
		}
	}
	
	
	public List<Map> getCustomerExcel(String startDealdate,String endDealdate,
			String cityId,String isActive,String type)
	{
		StringBuffer sql=new StringBuffer();
		Date date=new Date();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateTimeUtil dt = new DateTimeUtil(fmt.format(date));
		String nowTime = String.valueOf(dt.getLongTime());
		if(type.equals("1")){
			sql.append("select a.username loid,a.city_id city_id,a.is_active,a.device_serialnumber serial,a.opendate opendate,a.device_id device_id,a.dealdate from tab_hgwcustomer_report a,gw_cust_user_dev_type b");
			sql.append(" where a.user_id=b.user_id");
			sql.append(" and a.opmode='1' and b.type_id='2' and a.is_active=1");
			if(!StringUtil.IsEmpty(startDealdate)){
				sql.append(" and a.dealdate>").append(startDealdate);
			}
			if(!StringUtil.IsEmpty(endDealdate)){
				sql.append(" and a.dealdate<").append(endDealdate);
			}
			List cityIdList  = null;
			if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
				cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
						.append(")");
				cityIdList = null;
			}
//			if(!StringUtil.IsEmpty(isActive)&&!"-1".equals(isActive)){
//				sql.append(" and a.is_active=").append(isActive);
//			}
			cityMap = CityDAO.getCityIdCityNameMap();
			PrepareSQL psql = new PrepareSQL(sql.toString());
			@SuppressWarnings("unchecked")
			List<Map> list=jt.query(psql.getSQL(),new RowMapper() {
				
				@Override
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("LOID", rs.getString("loid"));
					String city_id=rs.getString("city_id");
					map.put("city_id", city_id);
					String city_name = StringUtil.getStringValue(cityMap.get(city_id));
					if (false == StringUtil.IsEmpty(city_name))
					{
						map.put("city_name", city_name);
					}
					else
					{
						map.put("city_name", "");
					}
					int is_active=rs.getInt("is_active");
					map.put("is_active",String.valueOf(is_active));
					if(false == StringUtil.IsEmpty(String.valueOf(is_active))){
						if(is_active==0){
							map.put("active_name", "否");
						}else{
							map.put("active_name", "是");
						}
					}
					if(rs.getString("device_id")!=null){
						map.put("last_time", DateUtil.transTime(Long.parseLong(getLastTime(rs.getString("device_id"))), "yyyy-MM-dd HH:mm:ss"));
					}
					map.put("serial", rs.getString("serial"));
					map.put("opendate", DateUtil.transTime(rs.getLong("opendate"), "yyyy-MM-dd HH:mm:ss"));
					return map;
				}
			});
			cityMap=null;
			return list;
			
		}else{
				if(!StringUtil.IsEmpty(isActive)&&"0".equals(isActive)){
					sql.append("select a.username loid,a.city_id city_id,a.is_active is_active,a.device_serialnumber serial,a.opendate opendate,1 last_time from tab_hgwcustomer_report a,gw_cust_user_dev_type b");
					sql.append(" where a.user_id=b.user_id");
					sql.append(" and a.opmode='1' and b.type_id='2' and a.is_active=1 and a.device_id=null");
					if(!StringUtil.IsEmpty(startDealdate)){
						sql.append(" and a.dealdate>").append(startDealdate);
					}
					if(!StringUtil.IsEmpty(endDealdate)){
						sql.append(" and a.dealdate<").append(endDealdate);
					}
					List cityIdList  =null;
					if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
						cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
						sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
								.append(")");
						cityIdList = null;
					}
					
					/**	if(DBUtil.GetDB()==3){
						//TODO wait
					}else{
						
					} */
					sql.append(" UNION ALL " +
							"select a.username loid,a.city_id city_id,a.is_active is_active,a.device_serialnumber serial,a.opendate opendate,d.last_time last_time " +
							"from tab_hgwcustomer_report a,gw_cust_user_dev_type b,tab_gw_device c,gw_devicestatus d");
					sql.append(" where a.user_id=b.user_id and a.device_id=c.device_id and c.device_id=d.device_id");
					sql.append(" and a.opmode='1' and b.type_id='2' and a.is_active=1");
					sql.append(" and "+nowTime+"-d.last_time>"+2592000);
					if(!StringUtil.IsEmpty(startDealdate)){
						sql.append(" and a.dealdate>").append(startDealdate);
					}
					if(!StringUtil.IsEmpty(endDealdate)){
						sql.append(" and a.dealdate<").append(endDealdate);
					}
					cityIdList  =null;
					if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
						cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
						sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
								.append(")");
						cityIdList = null;
					}
					cityMap = CityDAO.getCityIdCityNameMap();
					PrepareSQL psql = new PrepareSQL(sql.toString());
					@SuppressWarnings("unchecked")
					List<Map> list=jt.query(psql.getSQL(), new RowMapper() {
						
						@Override
						public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> map = new HashMap<String, String>();
							map.put("LOID", rs.getString("loid"));
							String city_id=rs.getString("city_id");
							map.put("city_id", city_id);
							String city_name = StringUtil.getStringValue(cityMap.get(city_id));
							if (false == StringUtil.IsEmpty(city_name))
							{
								map.put("city_name", city_name);
							}
							else
							{
								map.put("city_name", "");
							}
							int is_active=rs.getInt("is_active");
							map.put("is_active",String.valueOf(is_active));
							if(false == StringUtil.IsEmpty(String.valueOf(is_active))){
								if(is_active==0){
									map.put("active_name", "否");
								}else{
									map.put("active_name", "是");
								}
							}
							map.put("serial", rs.getString("serial"));
							if(rs.getLong("last_time")==1){
								map.put("last_time","");
							}else{
								map.put("last_time",DateUtil.transTime(rs.getLong("last_time"), "yyyy-MM-dd HH:mm:ss") );
							}
							map.put("opendate", DateUtil.transTime(rs.getLong("opendate"), "yyyy-MM-dd HH:mm:ss"));
							return map;
						}
					});
					return list;
					
				}else{
					/**	if(DBUtil.GetDB()==3){
						//TODO wait
					}else{
						
					} */
					sql.append("select  a.username loid,a.city_id city_id,a.is_active is_active,a.device_serialnumber serial,a.opendate opendate,c.last_time last_time " +
							"from tab_hgwcustomer_report a,tab_gw_device b,gw_devicestatus c,gw_cust_user_dev_type d");
					sql.append(" where a.device_id=b.device_id and a.user_id=d.user_id and b.device_id=c.device_id " +
							"and a.opmode='1' and  a.is_active=1 and d.type_id='2'");
					sql.append(" and "+nowTime+"-c.last_time<"+2592000);
					if(!StringUtil.IsEmpty(startDealdate)){
						sql.append(" and a.dealdate>").append(startDealdate);
					}
					if(!StringUtil.IsEmpty(endDealdate)){
						sql.append(" and a.dealdate<").append(endDealdate);
					}
					List cityIdList  =null;
					if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
						cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
						sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
								.append(")");
						cityIdList = null;
					}
					cityMap = CityDAO.getCityIdCityNameMap();
					PrepareSQL psql = new PrepareSQL(sql.toString());
					@SuppressWarnings("unchecked")
					List<Map> list=jt.query(psql.getSQL(), new RowMapper() {
						
						@Override
						public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> map = new HashMap<String, String>();
							map.put("LOID", rs.getString("loid"));
							String city_id=rs.getString("city_id");
							map.put("city_id", city_id);
							String city_name = StringUtil.getStringValue(cityMap.get(city_id));
							if (false == StringUtil.IsEmpty(city_name))
							{
								map.put("city_name", city_name);
							}
							else
							{
								map.put("city_name", "");
							}
							int is_active=rs.getInt("is_active");
							map.put("is_active",String.valueOf(is_active));
							if(false == StringUtil.IsEmpty(String.valueOf(is_active))){
								if(is_active==0){
									map.put("active_name", "否");
								}else{
									map.put("active_name", "是");
								}
							}
							map.put("serial", rs.getString("serial"));
							map.put("last_time",DateUtil.transTime(rs.getLong("last_time"), "yyyy-MM-dd HH:mm:ss") );
							map.put("opendate", DateUtil.transTime(rs.getLong("opendate"), "yyyy-MM-dd HH:mm:ss"));
							return map;
						}
					});
					return list;
				}
				
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Map> doQuery(String sql){
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.query(psql.getSQL(),new RowMapper() {
			
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("LOID", rs.getString("loid"));
				String city_id=rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				int is_active=rs.getInt("is_active");
				map.put("is_active",String.valueOf(is_active));
				if(false == StringUtil.IsEmpty(String.valueOf(is_active))){
					if(is_active==0){
						map.put("active_name", "否");
					}else{
						map.put("active_name", "是");
					}
				}
				map.put("serial", rs.getString("serial"));
				map.put("last_time",DateUtil.transTime(rs.getLong("last_time"), "yyyy-MM-dd HH:mm:ss") );
				map.put("opendate", DateUtil.transTime(rs.getLong("opendate"), "yyyy-MM-dd HH:mm:ss"));
				return map;
			}
		});
	}
	
	public String getLastTime(String deviceId){
		StringBuffer sql = new StringBuffer();
		sql.append("select last_time from gw_devicestatus where device_id=?");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1 ,deviceId);
		List<Map> list = jt.queryForList(psql.getSQL());
		return String.valueOf(list.get(0).get("last_time"));
	}

	public Map<String, String> getCityMap() {
		return cityMap;
	}

	public void setCityMap(Map<String, String> cityMap) {
		this.cityMap = cityMap;
	}
	
}
