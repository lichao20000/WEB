package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.dao.StbCallHistoryDAO;

/**
 * 机顶盒apk上报信息查询BIO
 */
public class StbCallHistoryBIO
{
	private static Logger logger = LoggerFactory.getLogger(StbCallHistoryBIO.class);
	private StbCallHistoryDAO dao;

	
	/**
	 * 查询列表
	 */
	public List<Map<String,String>> query(int curPage_splitPage, int num_splitPage,
			String login_ip, String mac, String request_username,
			String result_username, String startTime, String endTime,String table) 
	{
		return dao.query(curPage_splitPage,num_splitPage,login_ip,
							mac,request_username,result_username,startTime,endTime,table);
	}

	/**
	 * 分页
	 */
	public int count(int curPage_splitPage, int num_splitPage, String login_ip,
			String mac, String request_username, String result_username,
			String startTime, String endTime,String table) 
	{
		return dao.count(curPage_splitPage,num_splitPage,login_ip,
							mac,request_username,result_username,startTime,endTime,table);
	}

	/**
	 * 获取总数
	 */
	public int getQueryCount() 
	{
		return dao.getQueryCount();
	}
	
	/**
	 * 查看详细
	 */
	public List<Map<String, String>> detailDevice(String mac,String login_time,String table) 
	{
		List<Map<String, String>> list=dao.detailDevice(mac,StringUtil.getLongValue(login_time),table);
		for(Map<String,String> map:list){
			map.put("request_time", transDate(map.get("login_time")));
		}
		
		return list;
	}

	/**
	 * 日期格式转换
	 */
	private static final String transDate(Object seconds)
	{
		if (seconds != null)
		{
			try{
				DateTimeUtil dt = new DateTimeUtil(Long.parseLong(seconds.toString()) * 1000);
				return dt.getLongDate();
			}catch (Exception e){
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}
	
	
	
	public StbCallHistoryDAO getDao() {
		return dao;
	}

	public void setDao(StbCallHistoryDAO dao) {
		this.dao = dao;
	}

}
