package com.linkage.module.inmp.dao;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class HomePageDataDao extends SuperDAO {

	private static Logger logger = LoggerFactory.getLogger(HomePageDataDao.class);
	
	/**
	 * 
	 * @param report_type 1:终端，2：用户，3：版本，4：管控终端
	 * @return
	 */
	public List<Map<String,String>> getPieData(String report_type){
		logger.warn("HomePageDataDao ==> getPieData({})",report_type);
		int flag = 1;
		flag = Integer.parseInt(report_type);
		StringBuffer sql = new StringBuffer();
		sql.append("select count_type,total_count,no_active_count,active_count from tab_user_device_report ");
		sql.append("where report_type = ? and count_time > ? and count_time < ? order by count_type");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setInt(1, flag);
		psql.setLong(2, timesBeforeDaysInSecond(1));
		psql.setLong(3, timesBeforeDaysInSecond(0));
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		return list;
	}
	
	public List<Map<String,String>> getAutoBindBarData(String cityId,String year,String month,String sort){
		logger.warn("HomePageDataDao ==> getAutoBindBarData({},{},{})",new Object[]{cityId,year,month});
		StringBuffer sql = new StringBuffer();
		
		if(month != null && !"".equals(month)){
			sql.append("select city_id,total_count,bind_count from tab_auto_bind_report_new ");
			sql.append(" where p_city_id = ? ");
			sql.append(" and count_year = ? ");
			sql.append(" and count_month = ? ");
			if(sort != null && !"".equals(sort)){
				sql.append(" order by count_rate desc ");
			}
		}else{
			sql.append("select city_id,sum(total_count) total_count ,sum(bind_count) bind_count ,count_year, sum(bind_count)/sum(total_count) count_rate from tab_auto_bind_report_new ");
			sql.append(" where p_city_id = ? ");
			sql.append(" and count_year = ? ");
			sql.append(" group by city_id,count_year ");
			if(sort != null && !"".equals(sort)){
				sql.append(" order by count_rate desc ");
			}
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, cityId);
		psql.setInt(2, StringUtil.getIntegerValue(year));
		if(month != null && !"".equals(month)){
			psql.setInt(3, StringUtil.getIntegerValue(month));
		}
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		return list;
	}
	
	public List<Map<String,String>> getAutoBindGaugeDataForYear(String cityId,String year){
		logger.warn("HomePageDataDao ==> getAutoBindGaugeDataForYear({},{})",new Object[]{cityId,year});
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,total_count,bind_count,count_month from tab_auto_bind_report_new ");
//		sql.append("where count_time > ? and count_time < ? ");
		sql.append(" where p_city_id = ? ");
		sql.append(" and count_year = ? ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
//		psql.setLong(1, timesBeforeDaysInSecond(1));
//		psql.setLong(2, timesBeforeDaysInSecond(0));
		psql.setString(1, cityId);
		psql.setInt(2, StringUtil.getIntegerValue(year));
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		return list;
	}
	
	public List<Map<String,String>> getBussBarData(String cityId,String year,String month,String sort){
		logger.warn("HomePageDataDao ==> getBussBarData({},{},{})",new Object[]{cityId,year,month});
		StringBuffer sql = new StringBuffer();
		
		if(month != null && !"".equals(month)){
			sql.append("select city_id,net_rate,itv_rate,voip_rate,total_rate from tab_buss_report_new ");
			sql.append(" where p_city_id = ? ");
			sql.append(" and count_year = ? ");
			sql.append(" and count_month = ? ");
			if(sort != null && !"".equals(sort)){
				sql.append(" order by total_rate desc ");
			}
		}else{
			sql.append("select city_id,round(avg(net_rate),2) net_rate ,round(avg(itv_rate),2) itv_rate ,round(avg(voip_rate),2) voip_rate ,round(avg(total_rate),2) total_rate,count_year from tab_buss_report_new ");
			sql.append(" where p_city_id = ? ");
			sql.append(" and count_year = ? ");
			sql.append(" group by city_id,count_year ");
			if(sort != null && !"".equals(sort)){
				sql.append(" order by total_rate desc ");
			}
		}
		
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, cityId);
		psql.setInt(2, StringUtil.getIntegerValue(year));
		if(month != null && !"".equals(month)){
			psql.setInt(3, StringUtil.getIntegerValue(month));
		}
		
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		return list; 
	}
	
	
	public List<Map<String,String>> getBussGaugeDataForYear(String cityId,String year){
		logger.warn("HomePageDataDao ==> getBussGaugeDataForYear({},{})",new Object[]{cityId,year});
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,net_rate,itv_rate,voip_rate,total_rate,count_month from tab_buss_report_new ");
//		sql.append("where count_time > ? and count_time < ? ");
		sql.append(" where p_city_id = ? ");
		sql.append(" and count_year = ? ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
//		psql.setLong(1, timesBeforeDaysInSecond(1));
//		psql.setLong(2, timesBeforeDaysInSecond(0));
		psql.setString(1, cityId);
		psql.setInt(2, StringUtil.getIntegerValue(year));
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		return list;
	}
	
	public List<Map<String,String>> getPvcGaugeDataForYear(String cityId,String year){
		logger.warn("HomePageDataDao ==> getPvcGaugeDataForYear({},{})",new Object[]{cityId,year});
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,deploy_count,nodeploy_count,count_month from tab_pvc_report_new ");
//		sql.append("where count_time > ? and count_time < ? ");
		sql.append(" where p_city_id = ? ");
		sql.append(" and count_year = ? ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
//		psql.setLong(1, timesBeforeDaysInSecond(1));
//		psql.setLong(2, timesBeforeDaysInSecond(0));
		psql.setString(1, cityId);
		psql.setInt(2, StringUtil.getIntegerValue(year));
		
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		return list;
	} 
	
	public List<Map<String,String>> getPvcBarData(String cityId,String year,String month,String sort){
		logger.warn("HomePageDataDao ==> getPvcBarData()",new Object[]{cityId,year,month});
		StringBuffer sql = new StringBuffer();
		
		if(month != null && !"".equals(month)){
			sql.append("select city_id,deploy_count,nodeploy_count,count_rate from tab_pvc_report_new ");
			sql.append(" where p_city_id = ? ");
			sql.append(" and count_year = ? ");
			sql.append(" and count_month = ? ");
			if(sort != null && !"".equals(sort)){
				sql.append(" order by count_rate desc ");
			}
		}else{
			sql.append("select city_id,sum(deploy_count) deploy_count ,sum(nodeploy_count) nodeploy_count ,count_year, sum(deploy_count)/(sum(deploy_count)+sum(nodeploy_count)) count_rate from tab_pvc_report_new ");
			sql.append(" where p_city_id = ? ");
			sql.append(" and count_year = ? ");
			sql.append(" group by city_id,count_year ");
			if(sort != null && !"".equals(sort)){
				sql.append(" order by count_rate desc ");
			}
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, cityId);
		psql.setInt(2, StringUtil.getIntegerValue(year));
		if(month != null && !"".equals(month)){
			psql.setInt(3, StringUtil.getIntegerValue(month));
		}
		
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		return list;
	} 
	
	
	public List<Map<String,String>> getVersionDevGaugeDataForYear(String cityId,String year){
		logger.warn("HomePageDataDao ==> getVersionDevData()",new Object[]{cityId,year});
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,nostand_count,stand_count,total_count,count_month from tab_version_dev_report_new ");
		sql.append(" where p_city_id = ? ");
		sql.append(" and count_year = ? ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, cityId);
		psql.setInt(2, StringUtil.getIntegerValue(year));
		
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		return list;
	}
	
	public List<Map<String,String>> getVersionDevData(String cityId,String year,String month,String sort){
		logger.warn("HomePageDataDao ==> getVersionDevData()",new Object[]{cityId,year,month});
		StringBuffer sql = new StringBuffer();
		
		if(month != null && !"".equals(month)){
			sql.append("select city_id,total_count,stand_count from tab_version_dev_report_new ");
			sql.append(" where p_city_id = ? ");
			sql.append(" and count_year = ? ");
			sql.append(" and count_month = ? ");
			if(sort != null && !"".equals(sort)){
				sql.append(" order by count_rate desc ");
			}
		}else{
			sql.append("select city_id,sum(nostand_count) nostand_count ,sum(stand_count) stand_count ,sum(total_count) total_count ,count_year, sum(stand_count)/sum(total_count) count_rate from tab_version_dev_report_new ");
			sql.append(" where p_city_id = ? ");
			sql.append(" and count_year = ? ");
			sql.append(" group by city_id,count_year ");
			if(sort != null && !"".equals(sort)){
				sql.append(" order by count_rate desc ");
			}
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, cityId);
		psql.setInt(2, StringUtil.getIntegerValue(year));
		if(month != null && !"".equals(month)){
			psql.setInt(3, StringUtil.getIntegerValue(month));
		}
		
		
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		return list;
	}
	
	public static long timesBeforeDaysInSecond(int days)
	{
		Calendar time = Calendar.getInstance();
		time.add(Calendar.DAY_OF_MONTH, -days);
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		return time.getTimeInMillis() / 1000;
	}
	
	public static void main(String args[]){
		Calendar time = Calendar.getInstance();
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		System.out.println(time.getTimeInMillis()/1000);
		System.out.println(timesBeforeDaysInSecond(1));
	}
}
