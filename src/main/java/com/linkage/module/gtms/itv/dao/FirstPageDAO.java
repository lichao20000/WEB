package com.linkage.module.gtms.itv.dao;

import java.util.List;
import java.util.Map;

public interface FirstPageDAO
{
	String DAILY_INTERVAL="daily";
	
	String WEEKLY_INTERVAL="weekly";
	
	String MONTHLY_INTERVAL="monthly";
	
	List<Map<String,String>> getEPGReport(String interval,String cityID);
	
	List<Map<String,String>> getNFReport(String interval);
	
	List<Map<String,String>> getServiceUser(String interval,String cityID);
	
	List<Map<String,String>> getWarnReport(String interval,String cityID);
	
	
	List<Map<String,String>> getDatainputReport();
	
	List<Map<String,String>> getTownReport();
	
	Map getCityMap();
}
