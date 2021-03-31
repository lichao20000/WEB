package com.linkage.liposs.buss.bio.securitygw;

import java.awt.Font;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.buss.dao.securitygw.EntSecStatDAO;
import com.linkage.liposs.buss.util.ChartUtil;

public class EntSecStatBIO {
	private static Logger log = LoggerFactory.getLogger(EntSecStatBIO.class);

	public EntSecStatDAO entSecStat;
	public ChartUtil cu;// jfreechart的封装类
	private Font font = new Font("宋体", Font.PLAIN, 12);// 字体

	public void setCu(ChartUtil cu) {
		this.cu = cu;
		cu.setFont(font);
	}


	public void setEntSecStatDAO(EntSecStatDAO entSecStat) {
		this.entSecStat = entSecStat;
	}
	
	/**
	 * 获取当天的病毒统计信息图
	 * @param deviceId:设备ID号
	 * @return：JFreeChart：chart对象
	 */
	public JFreeChart getVirusStatTodayByHour(String deviceId){
		Date now = new Date();
		long endTime = now.getTime()-3600 * 24*1000;
		long yesterday = endTime-3600 * 24*1000;
		 
		
		
		List<Map> virusStat = entSecStat.getEntSecurityByHourAndType(deviceId, yesterday, endTime, EntSecStatDAO.VIRUSEVENT);
		
		JFreeChart jfc = cu.createXYStackedBarP("病毒事件统计", "时间", "次数", virusStat, "virustimes", 
				                                 "stime", "virustype", 2, true);
		return jfc;
	}
	
	
	/**
	 * 获取当天的垃圾邮件统计信息图
	 * @param deviceId:设备ID号
	 * @return：JFreeChart：chart对象
	 */
	public JFreeChart getSpamStatTodayByHour(String deviceId){
		Date now = new Date();
		long today = now.getTime()/1000-3600 * 24;
		long yesterday = today-3600 * 24;
		
		
		List<Map> spamStat = entSecStat.getEntSecurityByHourAndType(deviceId, yesterday, today, EntSecStatDAO.SPAMMAIL);
		
		
		JFreeChart jfc = cu.createXYCategoryBarP("垃圾邮件统计", "时间", "次数", "垃圾邮件", spamStat, "stime", 
				                                 "mailtimes", 2, false);

		
		return jfc;
	}
	
	
	/**
	 * 获取当天的过滤统计信息图
	 * @param deviceId:设备ID号
	 * @return：JFreeChart：chart对象
	 */
	public JFreeChart getFilterStatTodayByHour(String deviceId){
		Date now = new Date();
		long today = now.getTime()/1000-3600 * 24;
		long yesterday = today-3600 * 24;
		
		
		List<Map> filterStat = entSecStat.getEntSecurityByHourAndType(deviceId, yesterday, today, EntSecStatDAO.FILTER);
		
		JFreeChart jfc = cu.createXYCategoryBarP("过滤事件统计", "时间", "次数", "过滤事件", filterStat, "stime", 
				                                 "filtertimes", 2, false);
		
		return jfc;
	}
	
	
	
	/**
	 * 获取当天的攻击统计信息图
	 * @param deviceId:设备ID号
	 * @return：JFreeChart：chart对象
	 */
	public JFreeChart getAttackStatTodayByHour(String deviceId){
		Date now = new Date();
		long today = now.getTime()/1000-3600 * 24;
		long yesterday = today-3600 * 24;
		
		
		List<Map> attackStat = entSecStat.getEntSecurityByHourAndType(deviceId, yesterday, today, EntSecStatDAO.ATTACK);
		
		JFreeChart jfc = cu.createXYStackedBarP("攻击事件统计", "时间", "次数", attackStat, "attacktimes", 
				                                 "stime", "attacktype", 2, false);
		
		return jfc;
	}
	
	
	
	/**
	 * 获取当天的流量统计信息图
	 * @param deviceId:设备ID号
	 * @return：JFreeChart：chart对象
	 */
	public JFreeChart getFluxStatTodayByHour(String deviceId){
		Date now = new Date();
		long today = now.getTime()/1000-3600 * 24;
		long yesterday = today-3600 * 24;
		
		
		List<Map> fluxStat = entSecStat.getEntSecurityByHourAndType(deviceId, yesterday, today, EntSecStatDAO.CONNECT);
		
		JFreeChart jfc = cu.createXYStackedBarP("网络流量统计", "时间", "次数", fluxStat, "times", 
                                                  "stime", "protocoltype", 2, true);
		
		return jfc;
	}
	
	
	
	/**
	 * 获取用户上网TOPN信息图
	 * @param deviceId:设备ID号
	 * @return：JFreeChart：chart对象
	 */
	public JFreeChart getTerminalConnStatTodayByHour(String deviceId){
		Date now = new Date();
		long today = now.getTime()/1000-3600 * 24;
		long yesterday = today-3600 * 24;
		
		
		List<Map> connStat = entSecStat.getTerminalDevTimesByHour(deviceId, yesterday, today);
		log.debug("size="+connStat.size());
		for(Map m:connStat)
		{
			log.debug(m.get("srcip")+"/"+m.get("deviceid"));
		}
		JFreeChart jfc = cu.createCategoryBar3DP("用户上网TOP10", "用户", "次数", connStat, "maxtimes", 
                                 "srcip", "deviceid", false);
		
		return jfc;		
	}
	
	
	/**
	 * 获取网络访问TOPN信息图
	 * @param deviceId:设备ID号
	 * @return：JFreeChart：chart对象
	 */
	public JFreeChart getWebVistTimesByHour(String deviceId){
		Date now = new Date();
		long today = now.getTime()/1000-3600 * 24;
		long yesterday = today-3600 * 24;
		
		
		List<Map> webVisitStat = entSecStat.getWebVistTimesByHour(deviceId, yesterday, today);
		for(Map m:webVisitStat)
		{
			log.debug(m.get("targetip")+"/"+m.get("deviceid"));
		}
		for(int i=0;i<webVisitStat.size();i++){
			if(i>=10){
				webVisitStat.remove(i);
			}else{
				Map data = (Map)webVisitStat.get(i);
				data.put("protocoltype", "http");
			}
			
		}
		
		JFreeChart jfc = cu.createCategoryBar3DP("网站访问TOP10", "网站", "次数", webVisitStat, "maxtimes", 
                                 "targetip", "deviceid", false);
		
		return jfc;
		
	}


}
