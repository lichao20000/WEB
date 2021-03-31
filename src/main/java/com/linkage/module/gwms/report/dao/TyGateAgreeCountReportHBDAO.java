package com.linkage.module.gwms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
/**
 * 江西电信天翼网关版本一致率
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2019-4-15
 */
public class TyGateAgreeCountReportHBDAO extends SuperDAO{

	Logger logger = LoggerFactory.getLogger(TyGateAgreeCountReportHBDAO.class);
	
	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> softwareversionMap = null;
	private HashMap<String, String> hardwareversionMap = null;
	/**
	 * 根据属地查询该属地所有满足条件的用户
	 * @param
	 * @param cityId
	 * @param startTime1
	 * @param endTime1
	 * @param is_highversion
	 * @return
	 */
	public int getTyCount(String cityId,String startTime1,String endTime1, String is_highversion
			,String gwType,String isExcludeUpgrade,String recent_start_Time1, String recent_end_Time1)
	{
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.setSQL("select count(distinct a.device_id) ");
		ppSQL.append("from tab_gw_device a,tab_hgwcustomer b,hgwcust_serv_info c,");
		ppSQL.append("tab_devicetype_info d,tab_device_version_attribute e");
		if (!StringUtil.IsEmpty(recent_start_Time1)||!StringUtil.IsEmpty(recent_end_Time1) ) {
			ppSQL.append(",gw_devicestatus f");
		}
		ppSQL.append(" where a.device_id=b.device_id and b.user_id=c.user_id ");
		ppSQL.append("and a.devicetype_id=d.devicetype_id and a.devicetype_id=e.devicetype_id ");
		if (!StringUtil.IsEmpty(recent_start_Time1)||!StringUtil.IsEmpty(recent_end_Time1) ) {
			ppSQL.append("  and a.device_id = f.device_id ");
		}
		ppSQL.append(" and a.gw_type ="+gwType+" and e.is_tygate=1 and d.is_normal=1 ");
		if(!"00".equals(cityId)){
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			ppSQL.append(PrepareSQL.AND,"a.city_id",list);
			list = null;
		}else{
			ppSQL.append(" and a.city_id <> '00'");
		}
		if (!"".equals(is_highversion) && !"-1".equals(is_highversion) && null != is_highversion){
			ppSQL.append(" and d.is_highversion ="+is_highversion);
		}
		if (!"".endsWith(startTime1) && null != startTime1){
			ppSQL.append(" and b.binddate>"+startTime1);
		}
		if (!"".endsWith(endTime1) && null != endTime1){
			ppSQL.append(" and b.binddate<"+endTime1);
		}
		if (!StringUtil.IsEmpty(recent_start_Time1)) {
			ppSQL.append(" and f.last_time >="+recent_start_Time1+"  ");
		}
		if (!StringUtil.IsEmpty(recent_end_Time1)) {
			ppSQL.append(" and f.last_time <"+recent_end_Time1+"  ");
		}
		if (!StringUtil.IsEmpty(isExcludeUpgrade)) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -3);// 0表示当前月，-3就是当前月-3
			long time=calendar.getTimeInMillis()/1000;
			ppSQL.append(" and not exists (select 1 from gw_serv_strategy_soft abc  where abc.device_id = a.device_id and abc.start_time >="+time+")  ");
		}
		
		return jt.queryForInt(ppSQL.toString());
	}
	
	
	/**
	 * 根据属地，开始时间结束时间来获取对应的厂商型号版本的数量
	 * @param
	 * @param cityId
	 * @param startTime1
	 * @param endTime1
	 * @param is_highversion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getTyDetailCountList(String cityId,String startTime1,String endTime1, String is_highversion
				,String gwType,String isExcludeUpgrade,String recent_start_Time1, String recent_end_Time1)
	{
		logger.debug("getTyCount(cityId:{},startTime:{},endTime:{})",cityId,startTime1,endTime1);
		PrepareSQL ppSQL = new PrepareSQL();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		ppSQL.setSQL("select count(distinct a.device_id) as num,a.vendor_id,a.device_model_id,a.devicetype_id  ");
		ppSQL.append("from tab_gw_device 				a");
		ppSQL.append(" ,tab_hgwcustomer 				b");
		ppSQL.append(" ,hgwcust_serv_info 				c");
		ppSQL.append(" ,tab_devicetype_info 			d");
		ppSQL.append(" ,tab_device_version_attribute 	e");
		if (!StringUtil.IsEmpty(recent_start_Time1)||!StringUtil.IsEmpty(recent_end_Time1) ) {
			ppSQL.append(" ,gw_devicestatus              f");
		}
		ppSQL.append(" where a.device_id = b.device_id ");
		ppSQL.append(" and b.user_id = c.user_id   ");
		ppSQL.append(" and a.devicetype_id = d.devicetype_id");
		ppSQL.append(" and a.devicetype_id=e.devicetype_id ");
		if (!StringUtil.IsEmpty(recent_start_Time1)||!StringUtil.IsEmpty(recent_end_Time1) ) {
			ppSQL.append("  and a.device_id = f.device_id  ");
		}
		ppSQL.append(" and a.gw_type ="+gwType+" and e.is_tygate=1 and d.is_normal=1 ");
		if(!"00".equals(cityId)){
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			ppSQL.append(PrepareSQL.AND,"a.city_id",list);
			list = null;
		}else{
			ppSQL.append(" and a.city_id <> '00'");
		}		
		if (!"".equals(is_highversion)&& !"-1".equals(is_highversion) && null != is_highversion){
			ppSQL.append(" and d.is_highversion ="+is_highversion);
		}
		if (!"".endsWith(startTime1) && null != startTime1){
			ppSQL.append(" and b.binddate>"+startTime1);
		}
		if (!"".endsWith(endTime1) && null != endTime1){
			ppSQL.append(" and b.binddate<"+endTime1);
		}
		if (!StringUtil.IsEmpty(recent_start_Time1)) {
			ppSQL.append(" and f.last_time >="+recent_start_Time1+"  ");
		}
		if (!StringUtil.IsEmpty(recent_end_Time1)) {
			ppSQL.append(" and f.last_time <"+recent_end_Time1+"  ");
		}
		if (!StringUtil.IsEmpty(isExcludeUpgrade)) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -3);// 0表示当前月，-3就是当前月-3
			long time=calendar.getTimeInMillis()/1000;
			ppSQL.append(" and not exists (select 1 from gw_serv_strategy_soft abc  where abc.device_id = a.device_id and abc.start_time >="+time+")  ");
		}
		ppSQL.append(" group by a.vendor_id,a.device_model_id,a.devicetype_id");
		
    	vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		softwareversionMap = VendorModelVersionDAO.getDevicetypeMap();
		hardwareversionMap = VendorModelVersionDAO.getDeviceTypeInfoMap();
		List<Map<String,String>> list = jt.query(ppSQL.toString(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("vendor_add", StringUtil.getStringValue(vendorMap.get(rs.getString("vendor_id"))));
				map.put("device_model", StringUtil.getStringValue(deviceModelMap
						.get(rs.getString("device_model_id"))));
				map.put("softwareversion", softwareversionMap.get(rs.getString("devicetype_id")));
				map.put("hardwareversion", hardwareversionMap.get(rs.getString("devicetype_id")));
				map.put("countNum", rs.getString("num"));
				return map;
			}
		});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String,String>>  queryDataListJXDX(String gw_type,String vendorId,String deviceModelId,String isExcludeUpgrade,
			String startTime1,String endTime1,String recent_start_Time1,String recent_end_Time1)
	{
		StringBuffer sql=new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select a.vendor_id,a.device_model_id,a.hardwareversion,a.sum1,a.sum2,a.total,");
			sql.append("case when a.total=0 then '0%' else  concat(cast(round((a.sum1 / a.total * 100), 2) as char),'%')  end  total_percent from ");
		}else{
			sql.append("select a.*,");
			sql.append("case when a.total=0 then '0%' else  round((a.sum1 / a.total * 100), 2) ||'%'  end  total_percent from ");
		}
		
		sql.append("(select b.vendor_id,c.device_model_id, d.hardwareversion,");
		sql.append(" sum(case when d.is_highversion = 1 then 1 else 0 end) sum1,");
		sql.append(" sum(case when d.is_highversion != 1 then 1  else 0 end) sum2,");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append(" count(*) total  from tab_gw_device  a");
		}else{
			sql.append(" count(1) total  from tab_gw_device  a");
		}
		
		sql.append(",tab_vendor b,gw_device_model c,tab_devicetype_info d,");
		sql.append(",tab_device_version_attribute e,tab_hgwcustomer f ");
		if (!StringUtil.IsEmpty(recent_start_Time1)||!StringUtil.IsEmpty(recent_end_Time1) ) {
			sql.append(",gw_devicestatus g");
		}
		sql.append(" where a.vendor_id=b.vendor_id and a.device_model_id = c.device_model_id  and a.devicetype_id = d.devicetype_id ");
		sql.append(" and d.devicetype_id=e.devicetype_id and a.device_id = f.device_id ");
		if (!StringUtil.IsEmpty(recent_start_Time1)||!StringUtil.IsEmpty(recent_end_Time1) ) {
			sql.append(" and a.device_id = g.device_id ");
		}
		sql.append(" and a.gw_type ="+gw_type+" and d.is_normal=1 and e.is_tygate = 1 ");
		sql.append(" and exists (select 1 from hgwcust_serv_info fc where fc.user_id = f.user_id) ");
		if (!StringUtil.IsEmpty(startTime1)) {
			sql.append(" and f.binddate >="+startTime1);
		}
		if (!StringUtil.IsEmpty(endTime1)) {
			sql.append(" and f.binddate <"+endTime1);
		}
		if (!StringUtil.IsEmpty(recent_start_Time1)) {
			sql.append(" and g.last_time >="+recent_start_Time1);
		}
		if (!StringUtil.IsEmpty(recent_end_Time1)) {
			sql.append(" and g.last_time <"+recent_end_Time1+" ");
		}
		if (!StringUtil.IsEmpty(isExcludeUpgrade)) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -3);// 0表示当前月，-3就是当前月-3
			long time=calendar.getTimeInMillis()/1000;
			sql.append(" and not exists (select 1 from gw_serv_strategy_soft abc  where abc.device_id = a.device_id and abc.start_time >="+time+") ");
		}
		if (!StringUtil.IsEmpty(vendorId) && !vendorId.equals("-1")) {
			sql.append(" and a.vendor_id="+vendorId+" ");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !deviceModelId.equals("-1")) {
			sql.append(" and a.device_model_id="+deviceModelId+" ");
		}
		
		sql.append(" group by b.vendor_id, c.device_model_id, d.hardwareversion ");
		sql.append(" order by b.vendor_id, c.device_model_id, d.hardwareversion) a ");
		
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(sql.toString());
		List<Map<String,String>> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("vendor_id",rs.getString("vendor_id"));
				map.put("vendor_add", StringUtil.getStringValue(vendorMap.get(rs.getString("vendor_id"))));
				map.put("device_model_id",rs.getString("device_model_id"));
				map.put("device_model", StringUtil.getStringValue(deviceModelMap.get(rs.getString("device_model_id"))));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("sum1", rs.getString("sum1"));
				map.put("sum2", rs.getString("sum2"));
				map.put("total", rs.getString("total"));
				map.put("total_percent", rs.getString("total_percent"));
				return map;
			}
		});
		return list;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>>  querydetailListJXDX(String gw_type,String vendorId,String deviceModelId,String isExcludeUpgrade,
			String startTime1,String endTime1,String recent_start_Time1,String recent_end_Time1,String is_highversion,String hardwareversion)
	{
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql=new StringBuffer();
		sql.append(" select b.vendor_id,c.device_model_id, d.hardwareversion,d.softwareversion ");
		if (is_highversion.equals("1")) {
			sql.append(" ,sum(case when d.is_highversion = 1 then 1 else 0 end) num ");
		}else if(is_highversion.equals("0")){
			sql.append(" ,sum(case when d.is_highversion != 1 then 1  else 0 end) num ");
		}else if(is_highversion.equals("-1")){
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append(" ,count(*) num ");
			}else{
				sql.append(" ,count(1) num ");
			}
		}
		sql.append("from tab_gw_device a,tab_vendor b,gw_device_model c,");
		sql.append("tab_devicetype_info d,");
		sql.append("tab_device_version_attribute e,");
		sql.append("tab_hgwcustomer f ");
		if (!StringUtil.IsEmpty(recent_start_Time1)||!StringUtil.IsEmpty(recent_end_Time1) ) {
			sql.append(" ,gw_devicestatus g ");
		}
		sql.append(" where a.vendor_id=b.vendor_id and a.device_model_id = c.device_model_id  and a.devicetype_id = d.devicetype_id ");
		sql.append(" and d.devicetype_id=e.devicetype_id and a.device_id = f.device_id ");
		if (!StringUtil.IsEmpty(recent_start_Time1)||!StringUtil.IsEmpty(recent_end_Time1) ) {
			sql.append(" and a.device_id = g.device_id ");
		}
		sql.append(" and a.gw_type ="+gw_type+" and d.is_normal=1 and e.is_tygate = 1 ");
		sql.append(" and exists (select 1 from hgwcust_serv_info fc where fc.user_id = f.user_id) ");
		if (!StringUtil.IsEmpty(startTime1)) {
			sql.append(" and f.binddate >="+startTime1);
		}
		if (!StringUtil.IsEmpty(endTime1)) {
			sql.append(" and f.binddate <"+endTime1+" ");
		}
		if (!StringUtil.IsEmpty(recent_start_Time1)) {
			sql.append(" and g.last_time >="+recent_start_Time1+" ");
		}
		if (!StringUtil.IsEmpty(recent_end_Time1)) {
			sql.append(" and g.last_time <"+recent_end_Time1+" ");
		}
		if (!StringUtil.IsEmpty(isExcludeUpgrade)) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -3);// 0表示当前月，-3就是当前月-3
			long time=calendar.getTimeInMillis()/1000;
			sql.append(" and not exists (select 1 from gw_serv_strategy_soft abc  where abc.device_id = a.device_id and abc.start_time >="+time+") ");
		}
		if (!StringUtil.IsEmpty(vendorId) && !vendorId.equals("-1")) {
			sql.append(" and a.vendor_id='"+vendorId+"' ");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !deviceModelId.equals("-1")) {
			sql.append(" and a.device_model_id='"+deviceModelId+"' ");
		}
		if (!StringUtil.IsEmpty(hardwareversion)) {
			sql.append(" and d.hardwareversion='"+hardwareversion+"' ");
		}
		
		sql.append(" group by b.vendor_id, c.device_model_id, d.hardwareversion,d.softwareversion ");
		sql.append(" order by b.vendor_id, c.device_model_id, d.hardwareversion,d.softwareversion ");
		
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(sql.toString());
		List<Map<String,String>> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("vendor_id",rs.getString("vendor_id"));
				map.put("vendor_add", StringUtil.getStringValue(vendorMap.get(rs.getString("vendor_id"))));
				map.put("device_model_id",rs.getString("device_model_id"));
				map.put("device_model", StringUtil.getStringValue(deviceModelMap.get(rs.getString("device_model_id"))));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("countNum", rs.getString("num"));
				return map;
			}
		});
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>>  queryDataListSDDX(String querydata,String gw_type,String vendorId,String deviceModelId,String isExcludeUpgrade,
			String startTime1,String endTime1,String recent_start_Time1,String recent_end_Time1)
	{
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql=new StringBuffer();
		sql.append(" insert into tab_unificationreport_temp select "+querydata+" querydata,b.vendor_id,c.device_model_id, d.hardwareversion,d.softwareversion,");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append(" count(*) total  from tab_gw_device a,");
		}else{
			sql.append(" count(1) total  from tab_gw_device  a,");
		}
		
		sql.append("tab_vendor b,gw_device_model c,");
		sql.append("tab_devicetype_info d,tab_hgwcustomer f ");
		if (!StringUtil.IsEmpty(recent_start_Time1)||!StringUtil.IsEmpty(recent_end_Time1) ) {
			sql.append(" ,gw_devicestatus g ");
		}
		sql.append(" where a.vendor_id=b.vendor_id and a.device_model_id = c.device_model_id  and a.devicetype_id = d.devicetype_id ");
		sql.append(" and a.device_id = f.device_id ");
		if (!StringUtil.IsEmpty(recent_start_Time1)||!StringUtil.IsEmpty(recent_end_Time1) ) {
			sql.append(" and a.device_id = g.device_id ");
		}
		sql.append(" and a.gw_type ="+gw_type+" ");
		sql.append(" and exists (select 1 from hgwcust_serv_info fc where fc.user_id = f.user_id) ");
		if (!StringUtil.IsEmpty(startTime1)) {
			sql.append(" and f.binddate >="+startTime1);
		}
		if (!StringUtil.IsEmpty(endTime1)) {
			sql.append(" and f.binddate <"+endTime1+" ");
		}
		if (!StringUtil.IsEmpty(recent_start_Time1)) {
			sql.append(" and g.last_time >="+recent_start_Time1+" ");
		}
		if (!StringUtil.IsEmpty(recent_end_Time1)) {
			sql.append(" and g.last_time <"+recent_end_Time1+" ");
		}
		if (!StringUtil.IsEmpty(isExcludeUpgrade)) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -3);// 0表示当前月，-3就是当前月-3
			long time=calendar.getTimeInMillis()/1000;
			sql.append(" and a.device_id not in (select abc.device_id from gw_serv_strategy_soft abc  where  abc.start_time >="+time+") ");
		}
		if (!StringUtil.IsEmpty(vendorId) && !vendorId.equals("-1")) {
			sql.append(" and a.vendor_id='"+vendorId+"' ");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !deviceModelId.equals("-1")) {
			sql.append(" and a.device_model_id='"+deviceModelId+"' ");
		}
		sql.append(" group by b.vendor_id, c.device_model_id, d.hardwareversion,d.softwareversion ");
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(sql.toString());
		int flag =0;
		try {
			flag=jt.update(psql.getSQL());
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		if (flag>0) {
			String sql1;
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				//TODO wait
				sql1="select a.vendor_id,a.device_model_id,a.hardwareversion,a.softwareversion,a.num,v.totalnum," +
						"cast(round((a.num / v.totalnum * 100), 2) as decimal(8,2)) total_percent ";
			}else{
				sql1="select a.*,v.totalnum ,convert(decimal(10,2), round((a.num / v.totalnum * 100), 2)) total_percent ";
			}
			sql1+="from tab_unificationreport_temp a"
						+" left join (select b.vendor_id,b.device_model_id,b.hardwareversion,sum(b.num) totalnum "
						+" from tab_unificationreport_temp b where querydata="+querydata+" group by b.vendor_id, b.device_model_id, b.hardwareversion) v"
						+" on a.vendor_id = v.vendor_id and a.device_model_id = v.device_model_id and a.hardwareversion = v.hardwareversion "
						+" where a.num <> 0 and querydata="+querydata+" order by a.vendor_id ,a.device_model_id,a.hardwareversion desc";
			vendorMap = VendorModelVersionDAO.getVendorMap();
			deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
			PrepareSQL psql1 = new PrepareSQL();
			psql1.setSQL(sql1);
			list = jt.query(psql1.getSQL(), new RowMapper()
			{
				public Object mapRow(ResultSet rs, int arg1) throws SQLException
				{
					Map<String, String> map = new HashMap<String, String>();
					map.put("vendor_id",rs.getString("vendor_id"));
					map.put("vendor_add", StringUtil.getStringValue(vendorMap.get(rs.getString("vendor_id"))));
					map.put("device_model_id",rs.getString("device_model_id"));
					map.put("device_model", StringUtil.getStringValue(deviceModelMap.get(rs.getString("device_model_id"))));
					map.put("hardwareversion", rs.getString("hardwareversion"));
					map.put("softwareversion", rs.getString("softwareversion"));
					map.put("num", rs.getString("num"));
					map.put("total_percent", rs.getString("total_percent"));
					return map;
				}
			});
		}else {
			logger.warn("版本一致率统计报表没有数据");
		}
		
		
		return list;
		
	}
}
