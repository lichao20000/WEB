package com.linkage.module.gtms.itv.bio;

import java.util.List;
import java.util.Map;

public interface FirstPageBIO
{
	
	List<Map<String,String>> getEPGReport(String interval,String cityID);
	
	List<Map<String,String>> getNFReport(String interval);
	
	List<Map<String,String>> getServiceUser(String interval,String cityID);
	
	List<Map<String,String>> getWarnReport(String interval,String cityID);
	
	
	
	List<Map<String,String>> getDatainputReport();
	
	List<Map<String,String>> getTownReport();
	
	Map getCityMap();
}
