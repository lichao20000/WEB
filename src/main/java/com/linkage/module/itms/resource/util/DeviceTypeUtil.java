package com.linkage.module.itms.resource.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;

public class DeviceTypeUtil {

	private static Logger logger = LoggerFactory.getLogger(DeviceTypeUtil.class);

	public static Map<String,String> vendorMap = new HashMap<String,String>();
	
	public static Map<String,String> deviceModelMap = new HashMap<String,String>();
	
	public static Map<String,String> checkMap = new HashMap<String,String>();

	public static Map<String,String> devTypeMap = new HashMap<String,String>();
	
	public static Map<String,String> accessMap = new HashMap<String,String>();

	public static Map<String,String> specMap = new HashMap<String,String>();
	
	public static Map<String, String> softVersionMap = new HashMap<String, String>();

	static
	{
		init();
	}

	public static void init()
	{
		logger.warn("init deviceMap");
		checkMap.put("审核", "1");
		checkMap.put("未审核","-1");
		checkMap.put("1", "审核");
		checkMap.put("-1", "未审核");

		List<HashMap<String,String>> vendorlist = getVendorMap();
		for (HashMap<String,String> map:vendorlist)
		{
			vendorMap.put(map.get("vendor_name"), map.get("vendor_id"));
			vendorMap.put(map.get("vendor_id"), map.get("vendor_name"));
		}

		List<HashMap<String,String>> deviceModelList = getDeviceModel();
		for (HashMap<String,String> map:deviceModelList)
		{
			deviceModelMap.put(map.get("device_model"), map.get("device_model_id"));
			deviceModelMap.put(map.get("device_model_id"), map.get("device_model"));

		}
		devTypeMap.put("e8-b","1");
		devTypeMap.put("e8-c","2");
		devTypeMap.put("1","e8-b");
		devTypeMap.put("2","e8-c");

		accessMap.put("ADSL","1");
		accessMap.put("LAN","2");
		accessMap.put("EPON光纤","3");		
		accessMap.put("GPON光纤","4");

		accessMap.put("1","ADSL");
		accessMap.put("2","LAN");
		accessMap.put("3","EPON光纤");		
		accessMap.put("4","GPON光纤");

		List<HashMap<String,String>> specList = new ArrayList<HashMap<String,String>>();
		specList = querySpecList();
		for (Map<String,String> map:specList)
		{
			specMap.put(map.get("spec_name"),map.get("id"));
			specMap.put(map.get("id"),map.get("spec_name"));
		}

		List<HashMap<String, String>> softVerList = querySoftVerList();
		for (HashMap<String, String> map : softVerList)
		{
			softVersionMap.put(StringUtil.getStringValue(map, "devicetype_id"),
					map.get("softwareversion"));
		}

	}

	private static List<HashMap<String,String>> getVendorMap()
	{
		PrepareSQL pSQL = new PrepareSQL("select vendor_id,vendor_name, vendor_add from tab_vendor");
		return DBOperation.getRecords(pSQL.getSQL());
	}
	
	private static List<HashMap<String,String>> getDeviceModel()
	{
		PrepareSQL pSQL = new PrepareSQL("select a.device_model_id,a.device_model from gw_device_model a");
		return DBOperation.getRecords(pSQL.getSQL());
	}
	
	private static List<HashMap<String,String>> querySpecList()
	{
		PrepareSQL pSQL = new PrepareSQL("select id,spec_name from tab_bss_dev_port");
		return DBOperation.getRecords(pSQL.getSQL());
	}

	private static List<HashMap<String, String>> querySoftVerList()
	{
		PrepareSQL pSQL = new PrepareSQL(
				"select devicetype_id,softwareversion from tab_devicetype_info");// 融合stb_tab_devicetype_info
		return DBOperation.getRecords(pSQL.getSQL());
	}
}
