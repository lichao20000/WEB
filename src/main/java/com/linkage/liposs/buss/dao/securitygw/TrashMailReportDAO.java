package com.linkage.liposs.buss.dao.securitygw;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.liposs.buss.util.CommonUtil;
import com.linkage.liposs.common.util.DbUtil;
import com.linkage.litms.common.util.DateTimeUtil;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-1
 * @category com.linkage.liposs.buss.dao.securitygw 版权：南京联创科技 网管科技部
 * 
 */
public class TrashMailReportDAO
{
	private static Logger log = LoggerFactory.getLogger(TrashMailReportDAO.class);
	private JdbcTemplate jt;
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
	private String startTime = null;
	private String endTime = null;
	
	/**
	 * 根据时间区域获取此时间段内所有SQL（union）
	 * @param starttime
	 * @param endtime
	 * @param tabNamePrex
	 * @param formate
	 * @return
	 */
	public String getAllSQLList(String deviceid, long starttime, long endtime,
	    int unitType, int stepSize, String prefix, String pattern, String sqlPattern) {

        	List<String> tableList = DbUtil.createTableNames(starttime*1000, endtime*1000,
        		unitType, stepSize, prefix, pattern);
        	
        	log.debug("所有表名：" + tableList);
        	tableList = CommonUtil.getExistTables(tableList);
        	
        	Iterator<String> it = tableList.iterator();
        	String tableName = null;
        	StringBuilder allSQL = new StringBuilder();
        	// 将所有的表名组装sql链表
        	while (it.hasNext()) {
        	    tableName = it.next();
        	    allSQL.append(sqlPattern.replaceAll("[?]", tableName));
        
        	    if (it.hasNext()) {
        		allSQL.append(" union ");
        	    }
        	}
        
        	sqlPattern = allSQL.toString();
        	return sqlPattern;
	}
	
	/**
	 * 查询小时表获取数据
	 * 
	 * @param deviceid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> getMailDayData(String deviceid, long starttime, long endtime)
	{
		DateTimeUtil dateTimeStart = new DateTimeUtil(starttime * 1000);
		DateTimeUtil dateTimeEnd = new DateTimeUtil(endtime * 1000);
		
		startTime = dateTimeStart.getLongDate();
		endTime = dateTimeEnd.getLongDate();
		
		String sqlTemp = "select sum(mailtimes) mailtimes,etime,stime from ? where deviceid='" + deviceid + "' and stime >=" + starttime
				+ " and etime<=" + endtime + " group by etime,stime";
		sqlTemp = getAllSQLList(deviceid, starttime, endtime,
		Calendar.DAY_OF_MONTH, 1, "sgw_mail_hour_", "yyyy_MM_dd",sqlTemp);
		/*String tablename = "sgw_mail_hour_" + dateTimeStart.getYear() + "_"
				+ getStringFromInt(dateTimeStart.getMonth()) + "_"
				+ getStringFromInt(dateTimeStart.getDay());
		String sql = "select sum(mailtimes) mailtimes,etime,stime from " + tablename + " where deviceid='" + deviceid + "' and stime >=" + starttime
			+ " and etime<=" + endtime + " group by etime,stime";*/
		
//		log.debug("邮件统计日报表：" + sqlTemp);
		List<Map> list = null;
		try{
			PrepareSQL psql = new PrepareSQL(sqlTemp);
			list = jt.queryForList(psql.getSQL());
		}catch(Exception e){
			log.error(e.getMessage());
		}finally{
			if(list == null)
				list = new ArrayList<Map>(0);
		}
		return list;
	}
	/**
	 * 垃圾邮件周报表
	 * @param deviceid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> getMailWeekData(String deviceid,long starttime,long endtime){
	    	return getMailMonthData(deviceid,starttime,endtime);
		/*DateTimeUtil dateTimeStart = new DateTimeUtil(starttime*1000);
		DateTimeUtil dateTimeEnd   = new DateTimeUtil(endtime*1000);
		startTime = dateTimeStart.getLongDate();
		endTime = dateTimeEnd.getLongDate();
		//表名前缀
		String tablenamePrex = "sgw_mail_day_";
		//表名
		String tablename = null;
		//数据SQL
		String sql = null;
		//数据源List 周
		List<Map> result = null;
		
		//如果是同年同月
		if(dateTimeStart.getYear() == dateTimeEnd.getYear() && dateTimeStart.getMonth() == dateTimeEnd.getMonth()){
			tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
			sql = "select sum(mailtimes) mailtimes,etime,stime from " + tablename + " where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by etime,stime";
		}else if(dateTimeStart.getYear() == dateTimeEnd.getYear()){//如果是同年不同月
			tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
			sql = "select sum(mailtimes) mailtimes,etime,stime from " + tablename + " where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by etime,stime";
			tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeEnd.getMonth());
			sql += " union select sum(mailtimes) mailtimes,etime,stime from " + tablename + " where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by etime,stime";
		}else{ //不同年不用月
			tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
			sql = "select sum(mailtimes) mailtimes,etime,stime from " + tablename + " where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by etime,stime";
			tablename = tablenamePrex + dateTimeEnd.getYear() + "_" + getStringFromInt(dateTimeEnd.getMonth());
			sql += " union select sum(mailtimes) mailtimes,etime,stime from from " + tablename + " where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by etime,stime";
		}
		
		log.debug("邮件统计周报表：" + sql);
		
		try{
			result = jt.queryForList(sql);
		}catch(Exception e){
			log.error(e);
		}finally{
			if(result == null)
				result = new ArrayList<Map>(0);
		}
		return result;*/
	}
	
	/**
	 * 垃圾邮件周报表
	 * @param deviceid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> getMailMonthData(String deviceid,long starttime,long endtime){
	    DateTimeUtil dateTimeStart = new DateTimeUtil(starttime * 1000);
		DateTimeUtil dateTimeEnd = new DateTimeUtil(endtime * 1000);
		
		startTime = dateTimeStart.getLongDate();
		endTime = dateTimeEnd.getLongDate();
		
		String sqlTemp = "select sum(mailtimes) mailtimes,etime,stime from ? where deviceid='" + deviceid + "' and stime >=" + starttime
				+ " and etime<=" + endtime + " group by etime,stime";
		sqlTemp = getAllSQLList(deviceid, starttime, endtime,
		Calendar.MONTH, 1, "sgw_mail_day_", "yyyy_MM",sqlTemp);
		/*String tablename = "sgw_mail_hour_" + dateTimeStart.getYear() + "_"
				+ getStringFromInt(dateTimeStart.getMonth()) + "_"
				+ getStringFromInt(dateTimeStart.getDay());
		String sql = "select sum(mailtimes) mailtimes,etime,stime from " + tablename + " where deviceid='" + deviceid + "' and stime >=" + starttime
			+ " and etime<=" + endtime + " group by etime,stime";*/
		
//		log.debug("邮件统计周、月报表：" + sqlTemp);
		List<Map> list = null;
		try{
			PrepareSQL psql = new PrepareSQL(sqlTemp);
			list = jt.queryForList(psql.getSQL());
		}catch(Exception e){
			log.error(e.getMessage());
		}finally{
			if(list == null)
				list = new ArrayList<Map>(0);
		}
		return list;
	}
    public String getStartTime() {
	return startTime;
    }

    public String getEndTime() {
	return endTime;
    }

    /**
     * 将日期中月份或者日数字转换成dd mm
     * 
     * @param value
     * @return
     */
    public String getStringFromInt(int value) {
	if (value < 10) {
	    return "0" + value;
	}
	return String.valueOf(value);
    }
}
