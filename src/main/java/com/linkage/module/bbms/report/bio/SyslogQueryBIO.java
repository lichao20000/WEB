
package com.linkage.module.bbms.report.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.bbms.report.dao.SyslogQueryDAO;

/**
 * 
 * @author ZhangCong
 *
 */
public class SyslogQueryBIO
{

	private static Logger logger = LoggerFactory.getLogger(SyslogQueryBIO.class);
	private SyslogQueryDAO sysLogQueryDao;
	
	/**
	 * 查询syslog日志
	 * @param logType 日志类型
	 * @param stat_time
	 * @param end_time
	 * @param device_id
	 * @param deviceId
	 * @return
	 */
	public List<Map> querySyslog(String logType, String stat_time,String end_time,
			String oui,String sn, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("querySyslog({},{},{},{})", new Object[]{logType,stat_time,end_time,oui,sn});

		DateTimeUtil start_dt = new DateTimeUtil(stat_time);
		DateTimeUtil end_dt = new DateTimeUtil(end_time);
		
		//开始结束时间
		String startTime = StringUtil.getStringValue(start_dt.getLongTime());
		String endTime = StringUtil.getStringValue(end_dt.getLongTime() + (24 * 60 * 60));
		
//		//判断需要查询的日志类型
//		if ("filter".equals(logType))
//		{
//			//告警日志 过滤
//			preName = "sgw_filter_original_";
//			tableFields = "deviceid,targetip,srcip,targetmac,srcmac,filterid,content,filtertimes,stime,etime";
//		}else if("conn".equals(logType))
//		{
//			//访问日志 连接
//			preName = "sgw_conn_original_";
//			tableFields = "deviceid,targetip,srcip,targetport,srcport,targetmac,srcmac,protocoltype,times,flux,stime,etime";
//		}else if("attack".equals(logType))
//		{
//			//安全审计日志  攻击
//			preName = "sgw_attack_original_";
//			tableFields = "deviceid,targetip,srcip,targetport,srcport,targetmac,srcmac,attacktype,mark,attacktimes,stime,etime";
//		}else
//		{
//			//应用审计日志 病毒
//			preName = "sgw_virus_original_";
//			tableFields = "deviceid,targetip,srcip,targetport,srcport,targetmac,srcmac,virustype,operation,remark,virustimes,stime,etime";
//		}

		//logger.info("=================tableNames:"+ tableNames);
		return sysLogQueryDao.querySysLogs( startTime, endTime, oui,sn,logType,curPage_splitPage,num_splitPage);
	}
	
	/**
	 * 查询syslog日志类型
	 * @return
	 */
	public List<Map> querySysLogTypes()
	{
		return sysLogQueryDao.querySysLogTypes();
	}
	
	/**
	 * 查询syslog日志
	 * @param logType 日志类型
	 * @param stat_time
	 * @param end_time
	 * @param device_id
	 * @param deviceId
	 * @return
	 */
	public int querySyslogCount(String logType, String stat_time,String end_time,
			String oui,String sn, int num_splitPage)
	{
		logger.debug("querySyslog({},{},{},{})", new Object[]{logType,stat_time,oui,sn});
		
		DateTimeUtil start_dt = new DateTimeUtil(stat_time);
		DateTimeUtil end_dt = new DateTimeUtil(end_time);
		
		//开始结束时间
		String startTime = StringUtil.getStringValue(start_dt.getLongTime());
		String endTime = StringUtil.getStringValue(end_dt.getLongTime() + (24 * 60 * 60));
		
		
		return sysLogQueryDao.querySysLogCount(startTime, endTime, oui,sn,logType,num_splitPage);
	}
	
//	/**
//	 * 获取表名集合(只支持月内跨天)
//	 * @param preName 表名前缀
//	 * @param startTime 开始时间
//	 * @param endTime 结束时间
//	 */
//	private List<String> getTableNamesByPreNameAndTime(final String preName,final DateTimeUtil start_dt,final DateTimeUtil end_dt)
//	{
//		List<String> talbeNames = new ArrayList<String>();
//		String year = "" + start_dt.getYear();
//		String month = "" + (start_dt.getMonth() < 10? "0" + start_dt.getMonth():start_dt.getMonth());
//		//只支持月内跨天
//		int startDay = start_dt.getDay();
//		int endDay = end_dt.getDay();
//		for(int i = startDay;i<= endDay ;i++)
//		{
//			String day = "" + (i < 10? "0" + i:i);
//			//"yyyy_mm_dd";
//			String tableName = preName + year + "_" + month + "_" + day;
//			talbeNames.add(tableName);
//		}
//		return talbeNames;
//	}

	public SyslogQueryDAO getSysLogQueryDao()
	{
		return sysLogQueryDao;
	}

	public void setSysLogQueryDao(SyslogQueryDAO sysLogQueryDao)
	{
		this.sysLogQueryDao = sysLogQueryDao;
	}
}
