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
public class FilterReportDAO
{
	private static Logger log = LoggerFactory.getLogger(FilterReportDAO.class);
	private String startTime = null;
	private String endTime = null;
	private JdbcTemplate jt;
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
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
	 * 攻击事件日、周、月报表
	 * @param deviceid
	 * @param starttime
	 * @param endtime
	 * @param type  0：日报表  1：周报表 2：日报表
	 * @return
	 */
	public List<Map> getFilterReportData(String deviceid, long starttime, long endtime, int type)
	{
		DateTimeUtil dateTimeStart = new DateTimeUtil(starttime * 1000);
		DateTimeUtil dateTimeEnd = new DateTimeUtil(endtime * 1000);
		startTime = dateTimeStart.getLongDate();
		endTime = dateTimeEnd.getLongDate();
		
		// 表名
		//String tablename = null;
		// 数据SQL
		String sqlTemp = "select sum(filtertimes) filtertimes,stime,etime from ? where deviceid='"
		+ deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by stime,etime";
		sqlTemp = getAllSQLList(deviceid, starttime, endtime,type == 0 ? Calendar.DAY_OF_MONTH: Calendar.MONTH, 1, type == 0 ? "sgw_filter_hour_" : "sgw_filter_day_", type == 0 ? "yyyy_MM_dd" : "yyyy_MM",sqlTemp);
	
		// 数据源List 周
		List<Map> result = null;
		
		
		/*// 如果是同年同月
		if (dateTimeStart.getYear() == dateTimeEnd.getYear()
				&& dateTimeStart.getMonth() == dateTimeEnd.getMonth())
			{
				tablename = getTableName(dateTimeStart.getYear(),dateTimeStart.getMonth(),dateTimeStart.getDay(),type);//tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
				sql = "select sum(filtertimes) filtertimes,stime,etime from " + tablename + "  where deviceid='"
						+ deviceid + "' and stime >=" + starttime + " and etime<=" + endtime + " group by stime,etime";
			}
		else
			if (dateTimeStart.getYear() == dateTimeEnd.getYear())
				{// 如果是同年不同月
					tablename = getTableName(dateTimeStart.getYear(),dateTimeStart.getMonth(),dateTimeStart.getDay(),type);
					//tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
					sql = "select sum(filtertimes) filtertimes,stime,etime from " + tablename
							+ " where deviceid='" + deviceid + "' and stime >="
							+ starttime + " and etime<=" + endtime + " group by stime,etime";
					tablename = getTableName(dateTimeStart.getYear(),dateTimeEnd.getMonth(),dateTimeStart.getDay(),type);
					//tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeEnd.getMonth());
					sql += " union select sum(filtertimes) filtertimes,stime,etime from " + tablename
							+ " where deviceid='" + deviceid + "' and stime >="
							+ starttime + " and etime<=" + endtime + " group by stime,etime";
				}
			else
				{ // 不同年不用月
					tablename = getTableName(dateTimeStart.getYear(),dateTimeStart.getMonth(),dateTimeStart.getDay(),type);
					//tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
					sql = "select sum(filtertimes) filtertimes,stime,etime from " + tablename
							+ " where deviceid='" + deviceid + "' and stime >="
							+ starttime + " and etime<=" + endtime + " group by stime,etime";
					tablename = getTableName(dateTimeEnd.getYear(),dateTimeEnd.getMonth(),dateTimeEnd.getDay(),type);
					//tablename = tablenamePrex + dateTimeEnd.getYear() + "_" + getStringFromInt(dateTimeEnd.getMonth());
					sql += " union select sum(filtertimes) filtertimes,stime,etime from from " + tablename
							+ " where deviceid='" + deviceid + "' and stime >="
							+ starttime + " and etime<=" + endtime + " group by stime,etime";
				}*/
		log.debug("过滤统计报表：" + sqlTemp);
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
	 * 获取表名
	 * @param year
	 * @param month
	 * @param day
	 * @param type 0：日报表  1：周报表 2：日报表
	 * @return
	 */
	public String getTableName(int year,int month,int day,int type){
		String tablenamePrex = "sgw_filter_hour_";
		if(type == 1 || type == 2){
			tablenamePrex = "sgw_filter_day_";
		}
		
		String tablename = null;
		if(type == 0){
			tablename = tablenamePrex + year + "_" + getStringFromInt(month) + "_" + getStringFromInt(day);
		}else{
			tablename = tablenamePrex + year + "_" + getStringFromInt(month);
		}
		
		return tablename;
	}
	/**
	 * 将日期中月份或者日数字转换成dd mm
	 * 
	 * @param value
	 * @return
	 */
	public String getStringFromInt(int value)
	{
		if (value < 10)
			{
				return "0" + value;
			}
		return String.valueOf(value);
	}
	public String getStartTime()
	{
		return startTime;
	}
	public String getEndTime()
	{
		return endTime;
	}
}
