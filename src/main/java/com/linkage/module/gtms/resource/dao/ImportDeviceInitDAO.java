package com.linkage.module.gtms.resource.dao;

import java.util.Map;


public interface ImportDeviceInitDAO {
	
	public Map<String, String> getCityIdCityNameMap();
	
	public int checkDeviceSerialnumber(String oui, String deviceSerialnumber);
	
	
}
