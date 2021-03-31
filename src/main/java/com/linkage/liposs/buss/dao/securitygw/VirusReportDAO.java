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

public class VirusReportDAO {
	private static Logger log = LoggerFactory.getLogger(VirusReportDAO.class);
	
	private JdbcTemplate jt;
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
	private String startTime = null;
	private String endTime = null;
	/**
	 * 查询小时表获取数据
	 * @param deviceid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> getVirusDayData(String deviceid,long starttime,long endtime){
	    	DateTimeUtil dateTimeStart = new DateTimeUtil(starttime*1000);
		DateTimeUtil dateTimeEnd   = new DateTimeUtil(endtime*1000);
		
		startTime = dateTimeStart.getLongDate();
		endTime = dateTimeEnd.getLongDate();
		
		String sqlTemp = "select " + com.linkage.litms.common.util.DbUtil.getNullFunction("b.virusname", "'未知病毒'") + " virusname,sum(a.virustimes) virustimes,a.etime,a.stime from ? a left join sgw_virus_info b on a.virustype=b.virustype where deviceid='"
            		+ deviceid
            		+ "' and stime >="
            		+ starttime
            		+ " and etime <="
            		+ endtime + " group by virusname,stime,etime";
//		log.debug("starttime:" + starttime + " endtime:" + endtime);
		sqlTemp = getAllSQLList(deviceid, starttime, endtime,
			Calendar.MONTH, 1, "sgw_virus_hour_", "yyyy_MM_dd",sqlTemp);
		//数据源List 周
		List<Map> result = null;
//		log.debug("病毒统计日报表：" + sqlTemp);
		try{
			PrepareSQL psql = new PrepareSQL(sqlTemp);
			result = jt.queryForList(psql.getSQL());
		}catch(Exception e){
			log.error(e.getMessage());
		}finally{
			if(result == null)
			    result = new ArrayList<Map>(0);
		}
		return result;
	}
	/**
	 * 病毒周报表
	 * @param deviceid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> getVirusWeekData(String deviceid,long starttime,long endtime){
	    	return getVirusMonthData(deviceid,starttime,endtime);
		/*DateTimeUtil dateTimeStart = new DateTimeUtil(starttime*1000);
		DateTimeUtil dateTimeEnd   = new DateTimeUtil(endtime*1000);
		startTime = dateTimeStart.getLongDate();
		endTime = dateTimeEnd.getLongDate();
		//表名前缀
		String tablenamePrex = "sgw_virus_day_";
		//表名
		String tablename = null;
		//数据SQL
		String sql = null;
		//数据源List 周
		List<Map> result = null;
		
		//如果是同年同月
		if(dateTimeStart.getYear() == dateTimeEnd.getYear() && dateTimeStart.getMonth() == dateTimeEnd.getMonth()){
			tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
			sql = "select nvl(b.virusname,'未知病毒') virusname,sum(a.virustimes) virustimes,a.etime,a.stime from " + tablename + " a left join sgw_virus_info b on a.virustype=b.virustype where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by virusname,stime,etime";
		}else if(dateTimeStart.getYear() == dateTimeEnd.getYear()){//如果是同年不同月
			tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
			sql = "select nvl(b.virusname,'未知病毒') virusname,sum(a.virustimes) virustimes,a.etime,a.stime from " + tablename + " a left join sgw_virus_info b on a.virustype=b.virustype where  deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by virusname,stime,etime";
			tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeEnd.getMonth());
			sql += " union select nvl(b.virusname,'未知病毒') virusname,sum(a.virustimes) virustimes,a.etime,a.stime from " + tablename + " a left join sgw_virus_info b on a.virustype=b.virustype where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by virusname,stime,etime";
		}else{ //不同年不用月
			tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
			sql = "select nvl(b.virusname,'未知病毒') virusname,sum(a.virustimes) virustimes,a.etime,a.stime from " + tablename + " a left join sgw_virus_info b on a.virustype=b.virustype where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by virusname,stime,etime";
			tablename = tablenamePrex + dateTimeEnd.getYear() + "_" + getStringFromInt(dateTimeEnd.getMonth());
			sql += " union select nvl(b.virusname,'未知病毒') virusname,sum(a.virustimes) virustimes,a.etime,a.stime from " + tablename + " a left join sgw_virus_info b on a.virustype=b.virustype where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by virusname,stime,etime";
		}
		
		log.debug("病毒统计周报表：" + sql);
		
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
	 * 病毒月报表
	 * @param deviceid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> getVirusMonthData(String deviceid,long starttime,long endtime){
		DateTimeUtil dateTimeStart = new DateTimeUtil(starttime*1000);
		DateTimeUtil dateTimeEnd   = new DateTimeUtil(endtime*1000);
		
		startTime = dateTimeStart.getLongDate();
		endTime = dateTimeEnd.getLongDate();
		
		String sqlTemp = "select " + com.linkage.litms.common.util.DbUtil.getNullFunction("b.virusname", "'未知病毒'") +" virusname,sum(a.virustimes) virustimes,a.etime,a.stime from ? a left join sgw_virus_info b on a.virustype=b.virustype where deviceid='"
                		+ deviceid
                		+ "' and stime >="
                		+ starttime
                		+ " and etime<="
                		+ endtime + " group by virusname,stime,etime";
		sqlTemp = getAllSQLList(deviceid, starttime, endtime,
        		Calendar.MONTH, 1, "sgw_virus_day_", "yyyy_MM",sqlTemp);
		//数据源List 周
		List<Map> result = null;
		/*
		//表名前缀
		String tablenamePrex = "sgw_virus_day_";
		//表名
		String tablename = null;
		//数据SQL
		String sql = null;
		
		//如果是同年同月
		if(dateTimeStart.getYear() == dateTimeEnd.getYear() && dateTimeStart.getMonth() == dateTimeEnd.getMonth()){
			tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
			sql = "select nvl(b.virusname,'未知病毒') virusname,sum(a.virustimes) virustimes,a.etime,a.stime from " + tablename + " a left join sgw_virus_info b on a.virustype=b.virustype where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by virusname,stime,etime";
		}else if(dateTimeStart.getYear() == dateTimeEnd.getYear()){//如果是同年不同月
			tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
			sql = "select nvl(b.virusname,'未知病毒') virusname,sum(a.virustimes) virustimes,a.etime,a.stime from " + tablename + " a left join sgw_virus_info b on a.virustype=b.virustype where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by virusname,stime,etime";
			tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeEnd.getMonth());
			sql += " union select nvl(b.virusname,'未知病毒') virusname,sum(a.virustimes) virustimes,a.etime,a.stime from " + tablename + " a left join sgw_virus_info b on a.virustype=b.virustype where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by virusname,stime,etime";
		}else{ //不同年不用月
			tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
			sql = "select nvl(b.virusname,'未知病毒') virusname,sum(a.virustimes) virustimes,a.etime,a.stime from " + tablename + " a left join sgw_virus_info b on a.virustype=b.virustype where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by virusname,stime,etime";
			tablename = tablenamePrex + dateTimeEnd.getYear() + "_" + getStringFromInt(dateTimeEnd.getMonth());
			sql += " union select nvl(b.virusname,'未知病毒') virusname,sum(a.virustimes) virustimes,a.etime,a.stime from " + tablename + " a left join sgw_virus_info b on a.virustype=b.virustype where deviceid='" + deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by virusname,stime,etime";
		}
		*/
//		log.debug("病毒统计周、月报表：" + sqlTemp);
		
		try{
			PrepareSQL psql = new PrepareSQL(sqlTemp);
			result = jt.queryForList(psql.getSQL());
		}catch(Exception e){
			log.error(e.getMessage());
		}finally{
			if(result == null)
				result = new ArrayList<Map>(0);
		}
		return result;
	}
	/**
	 * 将日期中月份或者日数字转换成dd mm 
	 * @param value
	 * @return
	 */
	public String getStringFromInt(int value){
		if(value < 10){
			return "0" + value;
		}
		return String.valueOf(value);
	}
	public String getStartTime() {
		return startTime;
	}
	public String getEndTime() {
		return endTime;
	}
}
