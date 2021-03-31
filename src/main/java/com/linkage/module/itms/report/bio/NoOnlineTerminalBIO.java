package com.linkage.module.itms.report.bio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.NoOnlineTerminalDAO;
import java.util.Collections;


public class NoOnlineTerminalBIO {
	
	private Logger logger = LoggerFactory.getLogger(NoOnlineTerminalBIO.class);
	
	private NoOnlineTerminalDAO noOnlineTerminalDAO;

	public List<Map> getNoOnlineTerminalInfo(String city_id, String endOpenDate){
		logger.debug("getNoOnlineTerminalInfo()");
		List<Map> list = new ArrayList<Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		
		String starttime3 = this.getStartDate(endOpenDate,3);
		//大于三个月以上不在线设备终端
		Map noactivemap3 = noOnlineTerminalDAO.getNoOnlineTerminalInfo(city_id, starttime3);
		//大于三个月以上不在线且绑定工单的终端数
		Map noactivemap3Bd = noOnlineTerminalDAO.getNoOnlineTerminalByBdInfo(city_id, starttime3);
		
		String starttime4 = getStartDate(endOpenDate,4);
		//大于四个月以上不在线设备终端
		Map noactivemap4 = noOnlineTerminalDAO.getNoOnlineTerminalInfo(city_id, starttime4);
		//大于三个月以上不在线且绑定工单的终端数
		Map noactivemap4Bd = noOnlineTerminalDAO.getNoOnlineTerminalByBdInfo(city_id, starttime4);
		
		String starttime5 = getStartDate(endOpenDate,5);
		//大于五个月以上不在线设备终端
		Map noactivemap5 = noOnlineTerminalDAO.getNoOnlineTerminalInfo(city_id, starttime5);
		//大于五个月以上不在线且绑定工单的终端数
		Map noactivemap5Bd = noOnlineTerminalDAO.getNoOnlineTerminalByBdInfo(city_id, starttime5);
		
		String starttime6 = getStartDate(endOpenDate,6);
		//大于六个月以上不在线设备终端
		Map noactivemap6 = noOnlineTerminalDAO.getNoOnlineTerminalInfo(city_id, starttime6);
		//大于六个月以上不在线且绑定工单的终端数
		Map noactivemap6Bd = noOnlineTerminalDAO.getNoOnlineTerminalByBdInfo(city_id, starttime6);
		
		//大于三个月以上不在线设备终端
		long total3 = 0;
		//大于三个月以上不在线且绑定工单的终端数
		long total3Bd = 0;
		
		//大于四个月以上不在线设备终端
		long total4 = 0;
		//大于四个月以上不在线且绑定工单的终端数
		long total4Bd = 0;
		
		//大于五个月以上不在线设备终端
		long total5 = 0;
		//大于五个月以上不在线且绑定工单的终端数
		long total5Bd = 0;	
		
		//大于六个月以上不在线设备终端
		long total6 = 0;
		//大于六个月以上不在线且绑定工单的终端数
		long total6Bd = 0;
		
		ArrayList<String> tlist = null;
		for(int i=0; i<cityList.size(); i++){
			Map<String, Object> tmap = new HashMap<String, Object>();
			total3 = 0;
			total3Bd = 0;
			total4 = 0;
			total4Bd = 0;
			total5 = 0;
			total5Bd = 0;
			total6 = 0;
			total6Bd = 0;
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			tmap.put("city_id", cityId);
			tmap.put("city_name", cityMap.get(cityId));
			for (int j = 0; j < tlist.size(); j++)
			{
				total3 = total3 + StringUtil.getLongValue(noactivemap3.get(tlist.get(j)));
				total3Bd = total3Bd + StringUtil.getLongValue(noactivemap3Bd.get(tlist.get(j)));
				
				total4 = total4 + StringUtil.getLongValue(noactivemap4.get(tlist.get(j)));
				total4Bd = total4Bd + StringUtil.getLongValue(noactivemap4Bd.get(tlist.get(j)));
				
				total5 = total5 + StringUtil.getLongValue(noactivemap5.get(tlist.get(j)));
				total5Bd = total5Bd + StringUtil.getLongValue(noactivemap5Bd.get(tlist.get(j)));
				
				total6 = total6 + StringUtil.getLongValue(noactivemap6.get(tlist.get(j)));
				total6Bd = total6Bd + StringUtil.getLongValue(noactivemap6Bd.get(tlist.get(j)));
			}
			tmap.put("total3", total3);
			tmap.put("total3Bd", total3Bd);
			
			tmap.put("total4", total4);
			tmap.put("total4Bd", total4Bd);
			
			tmap.put("total5", total5);
			tmap.put("total5Bd", total5Bd);
			
			tmap.put("total6", total6);
			tmap.put("total6Bd", total6Bd);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}
	
	public List<Map> getDeviceList(String city_id, String starttime, int curPage_splitPage,int num_splitPage)
	{
		return noOnlineTerminalDAO.getDeviceList(city_id, starttime, curPage_splitPage, num_splitPage);
	}

	public int getDevCount(String city_id, String starttime, int curPage_splitPage,int num_splitPage )
	{
		return noOnlineTerminalDAO.getDevCount(city_id, starttime, curPage_splitPage, num_splitPage);
	}

	public List<Map> getDevExcel(String city_id, String starttime)
	{
		return noOnlineTerminalDAO.getDevExcel(city_id, starttime );
	}

	public NoOnlineTerminalDAO getNoOnlineTerminalDAO() {
		return noOnlineTerminalDAO;
	}

	public void setNoOnlineTerminalDAO(NoOnlineTerminalDAO noOnlineTerminalDAO) {
		this.noOnlineTerminalDAO = noOnlineTerminalDAO;
	}
	
	private String getStartDate(String endOpenDate,int num){
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
		String time = "";
		String timeStr = "";
		try {
			Date start = fmtrq.parse(endOpenDate);
			start.setMonth(start.getMonth()-num);
			start.setHours(0);
			start.setMinutes(0);
			start.setSeconds(0);
			timeStr = fmtrq.format(start.getTime());
			DateTimeUtil dt = new DateTimeUtil(timeStr);
			time = String.valueOf(dt.getLongTime());
		} catch (ParseException e) {
			logger.error("NoOnlineTerminalBIO时间类型装换失败："+endOpenDate);
		}
		return time;
	}
	
}
