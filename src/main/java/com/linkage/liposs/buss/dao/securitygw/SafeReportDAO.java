package com.linkage.liposs.buss.dao.securitygw;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
public class SafeReportDAO
{
	private static Logger log = LoggerFactory.getLogger(SafeReportDAO.class);
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
	public List<Map> getSafeReportData(String deviceid, long starttime, long endtime, int type)
	{
		DateTimeUtil dateTimeStart = new DateTimeUtil(starttime * 1000);
		DateTimeUtil dateTimeEnd = new DateTimeUtil(endtime * 1000);
		startTime = dateTimeStart.getLongDate();
		endTime = dateTimeEnd.getLongDate();
		
		// 表名
		String tablename = null;
		// 数据SQL
		//String sql = null;
		// 数据源List 周
		List<Map> result = null;
		
		String sqlTemp = "select sum(virustimes) virustimes,sum(ashmailtimes) ashmailtimes,sum(attacktimes) attacktimes,sum(filtertimes) filtertimes,sum(health) health from ? where deviceid='"
				+ deviceid + "' and stime >=" + starttime + " and etime<=" + endtime;
		sqlTemp = getAllSQLList(deviceid, starttime, endtime,Calendar.MONTH, 1, "sgw_security_","yyyy_MM",sqlTemp);
	
		/*// 如果是同年同月
		
		String sqlA = null;//定义两个变量，记录sql
		String sqlB = null;
		
		if (dateTimeStart.getYear() == dateTimeEnd.getYear()
				&& dateTimeStart.getMonth() == dateTimeEnd.getMonth())
			{
				tablename = getTableName(dateTimeStart.getYear(),dateTimeStart.getMonth(),dateTimeStart.getDay(),type);//tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
				sqlA = "select sum(virustimes) virustimes,sum(ashmailtimes) ashmailtimes,sum(attacktimes) attacktimes,sum(filtertimes) filtertimes,sum(health) health from " + tablename + "  where deviceid='"
						+ deviceid + "' and stime >=" + starttime + " and etime<=" + endtime;
			}
		else{//需要跨表查询，			
			if (dateTimeStart.getYear() == dateTimeEnd.getYear()){// 如果是同年不同月
					tablename = getTableName(dateTimeStart.getYear(),dateTimeStart.getMonth(),dateTimeStart.getDay(),type);
					//tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
					sqlA = "select sum(virustimes) virustimes,sum(ashmailtimes) ashmailtimes,sum(attacktimes) attacktimes,sum(filtertimes) filtertimes,sum(health) health from " + tablename
							+ " where deviceid='" + deviceid + "' and stime >="
							+ starttime + " and etime<=" + endtime;
					tablename = getTableName(dateTimeStart.getYear(),dateTimeEnd.getMonth(),dateTimeStart.getDay(),type);
					
					sqlB = "select sum(virustimes) virustimes,sum(ashmailtimes) ashmailtimes,sum(attacktimes) attacktimes,sum(filtertimes) filtertimes,sum(health) health from " + tablename
							+ " where deviceid='" + deviceid + "' and stime >="
							+ starttime + " and etime<=" + endtime;
					
			}else{ // 不同年不用月
					tablename = getTableName(dateTimeStart.getYear(),dateTimeStart.getMonth(),dateTimeStart.getDay(),type);
					//tablename = tablenamePrex + dateTimeStart.getYear() + "_" + getStringFromInt(dateTimeStart.getMonth());
					sqlA = "select sum(virustimes) virustimes,sum(ashmailtimes) ashmailtimes,sum(attacktimes) attacktimes,sum(filtertimes) filtertimes,sum(health) health from " + tablename
							+ " where deviceid='" + deviceid + "' and stime >="
							+ starttime + " and etime<=" + endtime;
					tablename = getTableName(dateTimeEnd.getYear(),dateTimeEnd.getMonth(),dateTimeEnd.getDay(),type);
					//tablename = tablenamePrex + dateTimeEnd.getYear() + "_" + getStringFromInt(dateTimeEnd.getMonth());
					sqlB = "select sum(virustimes) virustimes,sum(ashmailtimes) ashmailtimes,sum(attacktimes) attacktimes,sum(filtertimes) filtertimes,sum(health) health from " + tablename
							+ " where deviceid='" + deviceid + "' and stime >="
							+ starttime + " and etime<=" + endtime;
			}
		}*/
		
		log.debug("安全统计报表:" + sqlTemp);
		
		List<Map> listA = null;
		
		try{
			PrepareSQL psql = new PrepareSQL(sqlTemp);
			listA = jt.queryForList(psql.getSQL());
		}catch(Exception e){
		    listA = new ArrayList<Map>(0);
		}
		int virustimes = 0;
		int ashmailtimes = 0;
		int attacktimes = 0;
		int filtertimes = 0;
		int health = 0;
		
		if(listA != null && listA.size() > 0){
			log.debug("listA:" + listA);
			Iterator<Map> it = listA.iterator();
			Map record = null;
			
			while(it.hasNext()){
				record = it.next();
				if(record.get("virustimes") != null)
					virustimes += Integer.parseInt(String.valueOf(record.get("virustimes")));
				if(record.get("ashmailtimes") != null)
					ashmailtimes += Integer.parseInt(String.valueOf(record.get("ashmailtimes")));
				if(record.get("attacktimes") != null)
					attacktimes += Integer.parseInt(String.valueOf(record.get("attacktimes")));
				if(record.get("filtertimes") != null)
					filtertimes += Integer.parseInt(String.valueOf(record.get("filtertimes")));
				if(record.get("health") != null)
					health += Integer.parseInt(String.valueOf(record.get("health")));
			}
		}
		
		/*if(listB != null && listB.size() > 0){
			log.debug("listB:" + listB);
			Iterator<Map> it = listB.iterator();
			Map record = null;
			while(it.hasNext()){
				record = it.next();
				if(record.get("virustimes") != null)
					virustimes += Integer.parseInt(String.valueOf(record.get("virustimes")));
				if(record.get("ashmailtimes") != null)
					ashmailtimes += Integer.parseInt(String.valueOf(record.get("ashmailtimes")));
				if(record.get("attacktimes") != null)
					attacktimes += Integer.parseInt(String.valueOf(record.get("attacktimes")));
				if(record.get("filtertimes") != null)
					filtertimes += Integer.parseInt(String.valueOf(record.get("filtertimes")));
				if(record.get("health") != null)
					health += Integer.parseInt(String.valueOf(record.get("health")));
			}
		}
		*/
		if(result == null){
			result = new ArrayList<Map>();
		}
		result.clear();
		
		Map resultMap = new HashMap<String, String>(2);
		
		resultMap.put("type", "病毒数");
		resultMap.put("value", virustimes);
		result.add(resultMap);
		
		resultMap = new HashMap<String, String>(2);
		resultMap.put("type", "垃圾邮件数");
		resultMap.put("value", ashmailtimes);
		result.add(resultMap);
		
		resultMap = new HashMap<String, String>(2);
		resultMap.put("type", "攻击次数");
		resultMap.put("value", attacktimes);
		result.add(resultMap);
		
		resultMap = new HashMap<String, String>(2);
		resultMap.put("type", "过滤次数");
		resultMap.put("value", filtertimes);
		result.add(resultMap);
		
		
		/*resultMap = new HashMap<String, String>(2);
		resultMap.put("type", "健康值");
		resultMap.put("value", health);
		result.add(resultMap);*/
		
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
		String tablenamePrex = "sgw_security_";
		String tablename = null;
		
		tablename = tablenamePrex + year + "_" + getStringFromInt(month);
		
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
