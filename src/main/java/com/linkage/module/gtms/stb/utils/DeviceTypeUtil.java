
package com.linkage.module.gtms.stb.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;

public class DeviceTypeUtil
{

	private static Logger logger = LoggerFactory.getLogger(DeviceTypeUtil.class);
	public static Map<String, String> vendorMap = new HashMap<String, String>();
	public static Map<String, String> deviceModelMap = new HashMap<String, String>();
	public static Map<String, String> devTypeMap = new HashMap<String, String>();

	public static String getVendorName(String vendorId)
	{
		List<HashMap<String, String>> vendorlist = null;
		if(StringUtil.IsEmpty(vendorId)){
			return null;
		}
		
		String vendorName = "";
		String split = "";
		String[] vendorIds = vendorId.split(",");
		for(String id : vendorIds){
			vendorlist = getVendorMap(id);
			for (HashMap<String, String> map : vendorlist)
			{
				vendorMap.put(map.get("vendor_id"), map.get("vendor_name"));
			}
			vendorName += split + vendorMap.get(id);
			split = ",";
		}
	    
		logger.warn("vendorMap=" + vendorMap);
		return vendorName;
	}
	
	public static void init(){
		getVendorName();
		getDeviceModel();
		getDeviceSoftVersion();
	}
	
	public static void getVendorName()
	{
		PrepareSQL pSQL = new PrepareSQL("select vendor_id,vendor_name, vendor_add from stb_tab_vendor");
		List<HashMap<String,String>> vendorlist = DBOperation.getRecords(pSQL.getSQL());
		for (HashMap<String,String> map:vendorlist)
		{
			vendorMap.put(map.get("vendor_name"), map.get("vendor_id"));
			vendorMap.put(map.get("vendor_id"), map.get("vendor_name"));
		}
	}
	
	public static void getDeviceModel(){
		PrepareSQL pSQL = new PrepareSQL("select a.device_model_id,a.device_model from stb_gw_device_model a");
		List<HashMap<String,String>> deviceModelList = DBOperation.getRecords(pSQL.getSQL());
		for (HashMap<String,String> map:deviceModelList)
		{
			deviceModelMap.put(map.get("device_model"), map.get("device_model_id"));
			deviceModelMap.put(map.get("device_model_id"), map.get("device_model"));

		}
	}
	
	public static void getDeviceSoftVersion(){
		PrepareSQL pSQL = new PrepareSQL("select devicetype_id,softwareversion from stb_tab_devicetype_info");
		List<HashMap<String,String>> softVerList = DBOperation.getRecords(pSQL.getSQL());
		for (HashMap<String, String> map : softVerList)
		{
			devTypeMap.put(StringUtil.getStringValue(map, "devicetype_id"),
					map.get("softwareversion"));
		}
	}
	
	
	public static String getDeviceModel(String deviceModelId)
	{
		List<HashMap<String, String>> deviceModelList = queryDeviceModel(deviceModelId);
		for (HashMap<String, String> map : deviceModelList)
		{
			deviceModelMap.put(StringUtil.getStringValue(map.get("device_model_id")),
					map.get("device_model"));
		}
		logger.warn("deviceModelMap=" + deviceModelMap);
		return deviceModelMap.get(deviceModelId);
	}

	public static String getDeviceSoftVersion(String deviceTypeId)
	{
		List<HashMap<String, String>> softVerList = querySoftVerList(deviceTypeId);
		for (HashMap<String, String> map : softVerList)
		{
			devTypeMap.put(StringUtil.getStringValue(map.get("devicetype_id")),
					map.get("softwareversion"));
		}
		logger.warn("devTypeMap=" + devTypeMap);
		return devTypeMap.get(deviceTypeId);
	}

	private static List<HashMap<String, String>> getVendorMap(String vendor_id)
	{
		PrepareSQL pSQL = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from stb_tab_vendor where vendor_id ='"
						+ vendor_id + "'");
		return DBOperation.getRecords(pSQL.getSQL());
	}

	private static List<HashMap<String, String>> queryDeviceModel(String device_model_id)
	{
		PrepareSQL pSQL = new PrepareSQL(
				"select a.device_model_id,a.device_model from stb_gw_device_model a where a.device_model_id='"
						+ device_model_id + "'");// 融合stb_gw_device_model
		return DBOperation.getRecords(pSQL.getSQL());
	}

	private static List<HashMap<String, String>> querySoftVerList(String devicetype_id)
	{
		PrepareSQL pSQL = new PrepareSQL(
				"select devicetype_id,softwareversion from stb_tab_devicetype_info where devicetype_id ="
						+ devicetype_id);// 融合stb_tab_devicetype_info
		return DBOperation.getRecords(pSQL.getSQL());
	}
}
