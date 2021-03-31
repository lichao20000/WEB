
package com.linkage.module.gwms.dao.gw;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.jta.JtaAfterCompletionSynchronization;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author 王森博 2009-11-17
 */
public class VendorModelVersionDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(VendorModelVersionDAO.class);

	/**
	 * 取得所有vendorId和厂商名对应的MAP
	 * @author 王森博 
	 * 2009-11-17
	 */
	public static HashMap<String, String> getVendorMap()
	{
		logger.debug("getVendorMap()");
		HashMap<String, String> vendorMap = new HashMap<String, String>();
		vendorMap.clear();
		PrepareSQL psql = new PrepareSQL("select vendor_id,vendor_name,vendor_add from tab_vendor");
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		if (fields == null){
			logger.debug("厂商资源表没数据");
		}else{
			while (fields != null)
			{
				String vendor_add = (String) fields.get("vendor_add");
				if (false==StringUtil.IsEmpty(vendor_add))
				{
					vendorMap.put((String) fields.get("vendor_id"), vendor_add);
				}
				else
				{
					vendorMap.put((String) fields.get("vendor_id"), (String) fields
							.get("vendor_name"));
				}
				fields = cursor.getNext();
			}
		}
		return vendorMap;
	}
	
	/**
	 * 取得devicetype_id和软件版本对应的MAP
	 *
	 * @author wangsenbo
	 * @date Nov 17, 2009
	 * @return HashMap<String,String>
	 */
	public static HashMap<String, String> getDevicetypeMap()
	{
		logger.debug("getDevicetypeMap()");
		HashMap<String, String> devicetypeMap = new HashMap<String, String>();
		devicetypeMap.clear();
		PrepareSQL psql = new PrepareSQL("select devicetype_id,softwareversion from tab_devicetype_info");
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		if (fields == null){
			logger.debug("设备型号版本表没数据");
		}else{
			while (fields != null)
			{	
				devicetypeMap.put((String) fields.get("devicetype_id"), (String) fields.get("softwareversion"));
				fields = cursor.getNext();
			}
		}
		return devicetypeMap;
	}
	/**
	 * 取得devicetype_id和硬件版本版本对应的MAP
	 *
	 * @author wangsenbo
	 * @date Nov 17, 2009
	 * @return HashMap<String,String>
	 */
	public static HashMap<String, String> getDeviceTypeInfoMap()
	{
		logger.debug("getDevicetypeMap()");
		HashMap<String, String> mapObj = new HashMap<String, String>();
		mapObj.clear();
		PrepareSQL psql = new PrepareSQL("select devicetype_id,hardwareversion from tab_devicetype_info");
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		if (fields == null){
			logger.debug("设备型号版本表没数据");
		}else{
			while (fields != null)
			{	
				mapObj.put((String) fields.get("devicetype_id"), (String) fields.get("hardwareversion"));
				fields = cursor.getNext();
			}
		}
		return mapObj;
	}
	
	/**
	 * 取得device_model_id和设备型号对应的MAP
	 *
	 * @author wangsenbo
	 * @date Nov 17, 2009
	 * @return HashMap<String,String>
	 */
	public static HashMap<String, String> getDeviceModelMap()
	{
		logger.debug("getDeviceModelMap()");
		HashMap<String, String> deviceModelMap = new HashMap<String, String>();
		deviceModelMap.clear();
		PrepareSQL psql = new PrepareSQL("select device_model_id,device_model from gw_device_model");
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		if (fields == null){
			logger.debug("设备型号表没数据");
		}else{
			while (fields != null)
			{	
				deviceModelMap.put((String) fields.get("device_model_id"), (String) fields.get("device_model"));
				fields = cursor.getNext();
			}
		}
		return deviceModelMap;
	}
	
	/**
	 * 取得devicetype_id和部分字段对应的MAP
	 *
	 */
	public static HashMap<String, Map<String,String>> getDeviceTypeSomeMap()
	{
		logger.debug("getDeviceTypeSomeMap()");
		HashMap<String, Map<String,String>> mapObj = new HashMap<String, Map<String,String>>();
		PrepareSQL psql = new PrepareSQL("select devicetype_id,softwareversion,hardwareversion,access_style_relay_id from tab_devicetype_info");
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		if (fields == null){
			logger.debug("设备型号版本表没数据");
		}else{
			while (fields != null)
			{
				Map<String,String> temp = new HashMap<String, String>();
				temp.put("softwareversion",(String) fields.get("softwareversion"));
				temp.put("hardwareversion",(String) fields.get("hardwareversion"));
				temp.put("accessId",String.valueOf(fields.get("access_style_relay_id")));
				mapObj.put((String) fields.get("devicetype_id"), temp);
				fields = cursor.getNext();
			}
		}
		return mapObj;
	}
}
