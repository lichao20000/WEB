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

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年8月9日
 * @category com.linkage.module.itms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class HttpTestReportDAO extends SuperDAO
{
	private Logger logger = LoggerFactory.getLogger(HttpTestReportDAO.class);
	
	public  Map<String, String> qryRouteList(String cityId,int statCaliber,String startTime,String endTime){
        logger.warn("HttpTestReportDAO ==> qryRouteList,dbcityId:{}",cityId);
		
		StringBuilder sb = new StringBuilder();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sb.append("select sum(case when (ifnull(a.rate,0)>=0.9*cast(d.downlink as signed)) then 1 else 0 end) reached,");
			sb.append("sum(case when (ifnull(a.rate,0)<0.9*cast(d.downlink as signed)) then 1 else 0 end) noReached ");
			sb.append("from (select round(ifnull(avgsampledtotalvalues,0)*8/1024,2) rate,loid ");
		}else{
			sb.append("select sum(case when (nvl(a.rate,0)>=0.9*TO_NUMBER(d.downlink)) then 1 else 0 end) reached,");
			sb.append("sum(case when (nvl(a.rate,0)<0.9*TO_NUMBER(d.downlink)) then 1 else 0 end) noReached ");
			sb.append("from (select round(nvl(avgsampledtotalvalues,0)*8/1024,2) rate,loid ");
		}
		
		sb.append("from (select row_number() over(partition by loid order by avgsampledtotalvalues desc ) rn,");
		sb.append(" b.avgsampledtotalvalues, b.loid  from tab_http_diag_result_intf b ");
		sb.append(" where b.wan_type='IP_Routed' ");
		if(statCaliber != -1 && statCaliber != 0){
			sb.append(" and b.clienttype="+statCaliber);
		}
		if(!StringUtil.IsEmpty(startTime)){
			sb.append(" and b.test_time>="+startTime);
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			sb.append(" and b.test_time<="+endTime);
		}
		sb.append(") where rn=1");
		sb.append(") a,");
		sb.append(" tab_hgwcustomer b,hgwcust_serv_info c ,tab_netacc_spead d ");
		sb.append(" where a.loid=b.username and b.user_id=c.user_id and c.username = d.username and c.serv_type_id=10");
		if(!"00".equals(cityId)){
			sb.append(" and b.city_id in ");
			sb.append(" (select city_id from tab_city where city_id='" + cityId + "' or parent_id='" + cityId + "')");
		}
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		
		try
		{
			return DBOperation.getRecord(pSQL.getSQL());
		}
		catch (Exception e)
		{
			logger.warn("HttpTestReportDAO ==> qryRouteList ,err:{}",e.getMessage()); 
			return null;
		}
	}
	
	public Map<String, String> qryBridgedList(String cityId,int statCaliber,String startTime,String endTime){
		logger.warn("HttpTestReportDAO ==> qryBridgedList,dbcityId:{}",cityId);
		
		StringBuilder sb = new StringBuilder();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sb.append("select sum(case when (ifnull(a.rate,0)>=0.9*cast(a.speed as signed)) then 1 else 0 end) reached,");
			sb.append("sum(case when (ifnull(a.rate,0)<0.9*cast(a.speed as signed)) then 1 else 0 end) noReached ");
			sb.append("from (select round(ifnull(AVGSAMPLEDTOTALVALUES,0)*8/1024,2) rate,loid,speed ");
		}else{
			sb.append("select sum(case when (nvl(a.rate,0)>=0.9*TO_NUMBER(a.speed)) then 1 else 0 end) reached,");
			sb.append("sum(case when (nvl(a.rate,0)<0.9*TO_NUMBER(a.speed)) then 1 else 0 end) noReached ");
			sb.append("from (select round(nvl(AVGSAMPLEDTOTALVALUES,0)*8/1024,2) rate,loid,speed ");
		}
		
		sb.append("from (select row_number() over(partition by loid order by AVGSAMPLEDTOTALVALUES desc ) rn,");
		sb.append("b.AVGSAMPLEDTOTALVALUES, b.loid ,b.speed ");
		sb.append("from tab_http_diag_result_intf b where b.wan_type='PPPoE_Bridged' ");
		if(statCaliber != -1 && statCaliber != 0){
			sb.append(" and b.clienttype="+statCaliber);
		}
		if(!StringUtil.IsEmpty(startTime)){
			sb.append(" and b.test_time>="+startTime);
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			sb.append(" and b.test_time<="+endTime);
		}
		sb.append(") where rn=1");
		sb.append(") a,");
		
		sb.append(" tab_hgwcustomer b");
		sb.append(" where a.loid=b.username");
		if(!"00".equals(cityId)){
			sb.append(" and b.city_id in ");
			sb.append(" (select city_id from tab_city where city_id='" + cityId + "' or parent_id='" + cityId + "')");
		}
		
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		
		try
		{
			return DBOperation.getRecord(pSQL.getSQL());
		}
		catch (Exception e)
		{
			logger.warn("HttpTestReportDAO ==> qryBridgedList ,err:{}",e.getMessage()); 
			return null;
		} 
	}
	
	public List qryDetail(String cityId,String type,int statCaliber,String startTime,String endTime,
			int curPage_splitPage,int num_splitPage,boolean isTrun){
		
		if(StringUtil.IsEmpty(cityId) || StringUtil.IsEmpty(type)){
			logger.warn("qryDetail => 入参错误，返回null");
			return null;
		}
		
		if(isTrun){
			//清空表
			truncateTable();
			//入临时表
			ArrayList<String> sqlList = new ArrayList<String>();
			List<String> routList = qryRouteTemp(cityId, statCaliber, startTime, endTime, type);
			if(null != routList && !routList.isEmpty() && routList.size() > 0){
				sqlList.addAll(routList);
			}
			
			List<String> bridgeList = qryBridgedTemp(cityId, statCaliber, startTime, endTime, type);
			if(null != bridgeList && !bridgeList.isEmpty() && bridgeList.size() > 0){
				sqlList.addAll(bridgeList);
			}
			logger.warn("sql[{}]",sqlList);
			logger.warn("executeUpdate.size:"+sqlList.size());
			int res = DBOperation.executeUpdate(sqlList);
			logger.warn("res:{}",res);
		}
		
		//查询结果
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			pSQL.append("select loid,vendor_add,device_model,device_model,softwareversion,hardwareversion,");
			pSQL.append("city_name,speed,test_rate,gbbroadband,wan_type,testusername,test_time");
			pSQL.append("from tab_http_diag_detail_result");
		}else{
			pSQL.append("select * from tab_http_diag_detail_result");
		}
		 if(-1 != curPage_splitPage){
		    	return querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
		    			num_splitPage,new RowMapper() {
					  public Object mapRow(ResultSet rs, int arg1) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						try {
							map.put("loid", rs.getString("loid"));
							map.put("vendorName", rs.getString("vendor_add"));
							map.put("deviceModel", rs.getString("device_model"));
							map.put("software", rs.getString("softwareversion"));
							map.put("hardware", rs.getString("hardwareversion"));
							map.put("cityName", rs.getString("city_name"));
							map.put("signingRate", rs.getString("speed"));
							map.put("measureRate", rs.getString("test_rate"));
							map.put("isGE", "1".equals(rs.getString("gbbroadband"))?"是":"否");
							map.put("wan_type", rs.getString("wan_type"));
							map.put("testusername", rs.getString("testusername"));
							/*map.put("measureTime",  new DateTimeUtil(rs.getLong("test_time") * 1000)
							.getYYYY_MM_DD_HH_mm_ss());*/
							map.put("measureTime", rs.getString("test_time"));
							 
						} catch (SQLException e) {
							logger.error(e.getMessage());
						}
						return map; 
					}
				});
		    }else{
		    	return jt.query(pSQL.getSQL(), new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						try {
							map.put("loid", rs.getString("loid"));
							map.put("vendorName", rs.getString("vendor_add"));
							map.put("deviceModel", rs.getString("device_model"));
							map.put("software", rs.getString("softwareversion"));
							map.put("hardware", rs.getString("hardwareversion"));
							map.put("cityName", rs.getString("city_name"));
							map.put("signingRate", rs.getString("speed"));
							map.put("measureRate", rs.getString("test_rate"));
							map.put("isGE", "1".equals(rs.getString("gbbroadband"))?"是":"否");
							map.put("wan_type", rs.getString("wan_type"));
							map.put("testusername", rs.getString("testusername"));
							/*map.put("measureTime",  new DateTimeUtil(rs.getLong("test_time") * 1000)
							.getYYYY_MM_DD_HH_mm_ss());*/
							map.put("measureTime", rs.getString("test_time"));
 
						} catch (SQLException e) {
							logger.error(e.getMessage());
						}
						return map;  
					}
				});
		    }
	}
	
	 public List qryDetailNext(String cityId,String type,int statCaliber,String startTime,String endTime,
			  int curPage_splitPage,int num_splitPage){
			 return qryDetail(cityId, type, statCaliber, startTime, endTime, curPage_splitPage, num_splitPage, false);
	}
	 
	 public List<HashMap<String,String>> qryDetailExcel()
	 {
			PrepareSQL pSQL = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				pSQL.append("select loid,vendor_add,city_name,device_model,hardwareversion,softwareversion,");
				pSQL.append("speed,test_rate,wan_type,testusername,gbbroadband,test_time ");
				pSQL.append("from tab_http_diag_detail_result ");
			}else{
				pSQL.append("select * from tab_http_diag_detail_result ");
			}
			
			try
			{
				return DBOperation.getRecords(pSQL.getSQL());
			}
			catch (Exception e)
			{
				logger.error("qryDetailExcel=> err:{}",e.getMessage());
				 return null;
			}
	 }
	 
	public int qryDetailCount()
	{
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			pSQL.append("select count(*) num from tab_http_diag_detail_result");
		}else{
			pSQL.append("select count(1) num from tab_http_diag_detail_result");
		}
		
		try
		{
			Map<String, String> map = DBOperation.getRecord(pSQL.getSQL());
			return StringUtil.getIntegerValue(map.get("num"));
		}
		catch (Exception e)
		{
			logger.error("qryDetailCount=> err:{}",e.getMessage());
			 return 0;
		}
	}
	
	public static int truncateTable() {
		String sql = "truncate table tab_http_diag_detail_result";
		PrepareSQL ps = new PrepareSQL(sql);
		int rs = DBOperation.executeUpdate(ps.getSQL());
		return rs;
	}
	
	/**
	 * 获取路由模式下的SQL
	 * @param cityId
	 * @param statCaliber
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public  List<String> qryRouteTemp(String cityId,int statCaliber,String startTime,String endTime,String type){
        logger.warn("HttpTestReportDAO ==> qryRouteTemp,dbcityId:{}",cityId);
		
		StringBuilder sb = new StringBuilder();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sb.append("select a.loid,a.test_time,d.downlink,a.AVGSAMPLEDTOTALVALUES,a.testusername,");
		sb.append("f.softwareversion,f.hardwareversion,g.device_model,h.vendor_add,m.city_name ,aa.gbbroadband ");
		sb.append("from (select ");
		if(DBUtil.GetDB()==3){
			//TODO wait
			sb.append("round(ifnull(AVGSAMPLEDTOTALVALUES,0)*8/1024,2) rate,");
		}else{
			sb.append("round(nvl(AVGSAMPLEDTOTALVALUES,0)*8/1024,2) rate,");
		}
		sb.append("device_id,loid,AVGSAMPLEDTOTALVALUES,testusername,test_time ");
		sb.append("from (select row_number() over(partition by loid order by AVGSAMPLEDTOTALVALUES desc ) rn, bb.AVGSAMPLEDTOTALVALUES, bb.loid,bb.device_id,bb.testusername,bb.test_time");
		sb.append(" from tab_http_diag_result_intf bb ");
		sb.append(" where bb.wan_type='IP_Routed'");
		if(statCaliber != -1 && statCaliber != 0){
			sb.append(" and bb.clienttype="+statCaliber);
		}
		if(!StringUtil.IsEmpty(startTime)){
			sb.append(" and bb.test_time>="+startTime);
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			sb.append(" and bb.test_time<="+endTime);
		}
		sb.append(") where rn=1");
		sb.append(") a,");
		
		sb.append(" tab_hgwcustomer b,hgwcust_serv_info c,tab_netacc_spead d,");
		sb.append(" tab_gw_device e,tab_devicetype_info f,gw_device_model g,tab_vendor h,tab_city m,tab_device_version_attribute aa");
		sb.append(" where a.loid=b.username and b.user_id=c.user_id and c.username=d.username and c.serv_type_id=10");
		sb.append(" and a.device_id=e.device_id and e.devicetype_id=f.devicetype_id and e.device_model_id=g.device_model_id");
		sb.append(" and e.vendor_id=h.vendor_id and m.city_id=b.city_id and aa.devicetype_id=e.devicetype_id");
		if(!"00".equals(cityId)){
			sb.append(" and b.city_id in ");
			sb.append(" (select city_id from tab_city where city_id='" + cityId + "' or parent_id='" + cityId + "')");
		}
		if("reached".equals(type)){
			if(DBUtil.GetDB()==3){
				sb.append(" and ifnull(a.rate,0)>=0.9*cast(d.downlink as signed)");
			}else{
				sb.append(" and nvl(a.rate,0)>=0.9*TO_NUMBER(d.downlink)");
			}
			
		}else if("noReached".equals(type)){
			if(DBUtil.GetDB()==3){
				sb.append(" and ifnull(a.rate,0)<0.9*cast(d.downlink as signed)");
			}else{
				sb.append(" and nvl(a.rate,0)<0.9*TO_NUMBER(d.downlink)");
			}
		}
		
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		try
		{
			List<HashMap<String,String>> maps = DBOperation.getRecords(pSQL.getSQL());
			List<String> sqlList = new ArrayList<String>();
			if(null != maps && !maps.isEmpty() && maps.size() > 0){
				for(Map<String,String> map : maps){
					StringBuffer sql = new StringBuffer();
					sql.append(" insert into tab_http_diag_detail_result(");
					sql.append(" loid,vendor_add,device_model,softwareversion,hardwareversion,city_name,test_time,speed,test_rate,");
					sql.append(" gbbroadband,wan_type,testusername) values(");
					sql.append(StringUtil.getSQLString(map.get("loid")));
					sql.append(","+StringUtil.getSQLString(map.get("vendor_add")));
					sql.append(","+StringUtil.getSQLString(map.get("device_model")));
					sql.append(","+StringUtil.getSQLString(map.get("softwareversion")));
					sql.append(","+StringUtil.getSQLString(map.get("hardwareversion")));
					sql.append(","+StringUtil.getSQLString(map.get("city_name")));
					
					String time = map.get("test_time");
					if(StringUtil.IsEmpty(time)){
						time = "0";
					}
					
					sql.append(","+StringUtil.getSQLString(new DateTimeUtil(StringUtil.getLongValue(time)*1000).getYYYY_MM_DD_HH_mm_ss()));
					sql.append(","+StringUtil.getSQLString(map.get("downlink")));
					sql.append(","+StringUtil.getSQLString(map.get("AVGSAMPLEDTOTALVALUES".toLowerCase())));
					String ge = map.get("gbbroadband");
					if(!StringUtil.IsEmpty(ge) && ge.equals("1")){
						ge = "是";
					}else{
						ge = "否";
					}
					sql.append(","+StringUtil.getSQLString(ge));
					sql.append(",'路由'");
					sql.append(","+StringUtil.getSQLString(map.get("testusername")));
					sql.append(")");
					sqlList.add(sql.toString());
				}
			}
			return sqlList;
		}
		catch (Exception e)
		{
			logger.warn("HttpTestReportDAO ==> qryRouteTemp ,err:{}",e.getMessage()); 
			return null;
		}
	}
	
	/**
	 * 获取桥接模式下的sql
	 * @param cityId
	 * @param statCaliber
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<String> qryBridgedTemp(String cityId,int statCaliber,String startTime,String endTime,String type){
		StringBuilder sb = new StringBuilder();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sb.append("select a.loid,a.test_time,a.speed,a.AVGSAMPLEDTOTALVALUES,a.testusername,");
		sb.append("f.softwareversion,f.hardwareversion,g.device_model,h.vendor_add,m.city_name ,aa.gbbroadband ");
		sb.append("from (select ");
		if(DBUtil.GetDB()==3){
			sb.append("round(ifnull(AVGSAMPLEDTOTALVALUES,0)*8/1024,2) rate,");
		}else{
			sb.append("round(nvl(AVGSAMPLEDTOTALVALUES,0)*8/1024,2) rate,");
		}
		sb.append("device_id,loid,speed,AVGSAMPLEDTOTALVALUES,testusername,test_time ");
		sb.append("from (select row_number() over(partition by loid order by AVGSAMPLEDTOTALVALUES desc) rn," +
				"bb.AVGSAMPLEDTOTALVALUES,bb.device_id,bb.loid,bb.speed,bb.testusername,bb.test_time ");
		sb.append("from tab_http_diag_result_intf bb ");
		sb.append("where bb.wan_type='PPPoE_Bridged' ");
		if(statCaliber != -1 && statCaliber != 0){
			sb.append(" and bb.clienttype="+statCaliber);
		}
		if(!StringUtil.IsEmpty(startTime)){
			sb.append(" and bb.test_time>="+startTime);
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			sb.append(" and bb.test_time<="+endTime);
		}
		sb.append(") where rn=1");
		sb.append(") a,");
		
		sb.append(" tab_hgwcustomer b, tab_gw_device e,");
		sb.append(" tab_devicetype_info f,gw_device_model g,tab_vendor h,tab_city m,tab_device_version_attribute aa");
		sb.append(" where a.loid=b.username ");
		sb.append(" and a.device_id=e.device_id and e.devicetype_id=f.devicetype_id and e.device_model_id=g.device_model_id");
		sb.append(" and e.vendor_id=h.vendor_id and m.city_id=b.city_id and aa.devicetype_id=e.devicetype_id");
		if(!"00".equals(cityId)){
			sb.append(" and b.city_id in ");
			sb.append(" (select city_id from tab_city where city_id='" + cityId + "' or parent_id='" + cityId + "')");
		}
		if("reached".equals(type)){
			if(DBUtil.GetDB()==3){
				sb.append(" and ifnull(a.rate,0)>=0.9*cast(a.speed as signed)");
			}else{
				sb.append(" and nvl(a.rate,0)>=0.9*TO_NUMBER(a.speed)");
			}
		}else if("noReached".equals(type)){
			if(DBUtil.GetDB()==3){
				sb.append(" and ifnull(a.rate,0)<0.9*cast(a.speed as signed)");
			}else{
				sb.append(" and nvl(a.rate,0)<0.9*TO_NUMBER(a.speed)");
			}
		}
		
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		try
		{
			List<HashMap<String,String>> maps = DBOperation.getRecords(pSQL.getSQL());
			
			List<String> sqlList = new ArrayList<String>();
			if(null != maps && !maps.isEmpty() && maps.size() > 0){
				for(Map<String,String> map : maps){
					StringBuffer sql = new StringBuffer();
					sql.append(" insert into tab_http_diag_detail_result(");
					sql.append(" loid,vendor_add,device_model,softwareversion,hardwareversion,city_name,test_time,speed,test_rate,");
					sql.append(" gbbroadband,wan_type,testusername) values(");
					sql.append(StringUtil.getSQLString(map.get("loid")));
					sql.append(","+StringUtil.getSQLString(map.get("vendor_add")));
					sql.append(","+StringUtil.getSQLString(map.get("device_model")));
					sql.append(","+StringUtil.getSQLString(map.get("softwareversion")));
					sql.append(","+StringUtil.getSQLString(map.get("hardwareversion")));
					sql.append(","+StringUtil.getSQLString(map.get("city_name")));
					String time = map.get("test_time");
					if(StringUtil.IsEmpty(time)){
						time = "0";
					}
					
					sql.append(","+StringUtil.getSQLString(new DateTimeUtil(StringUtil.getLongValue(time)*1000).getYYYY_MM_DD_HH_mm_ss()));
					sql.append(","+StringUtil.getSQLString(map.get("speed")));
					sql.append(","+StringUtil.getSQLString(map.get("AVGSAMPLEDTOTALVALUES".toLowerCase())));
					String ge = map.get("gbbroadband");
					if(!StringUtil.IsEmpty(ge) && ge.equals("1")){
						ge = "是";
					}else{
						ge = "否";
					}
					sql.append(","+StringUtil.getSQLString(ge));
					sql.append(",'桥接'");
					sql.append(","+StringUtil.getSQLString(map.get("testusername")));
					sql.append(")");
					sqlList.add(sql.toString());
				}
			}
			return sqlList;
		}
		catch (Exception e)
		{
			logger.warn("HttpTestReportDAO ==> qryBridgedTemp ,err:{}",e.getMessage()); 
			return null;
		}
	}
	
	public List<HashMap<String, String>> qryCurrCity(String cityId)
	{
		String sql = "select city_id , city_name from tab_city where city_id ='" + cityId + "' or parent_id ='"+ cityId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		try
		{
			return DBOperation.getRecords(psql.getSQL());
		}
		catch (Exception e)
		{
			 logger.error("qryCurrCity == > err : {}", e.getMessage());
			 return null;
		} 
	}
	
}
