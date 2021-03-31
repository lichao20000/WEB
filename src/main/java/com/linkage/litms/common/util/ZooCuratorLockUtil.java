package com.linkage.litms.common.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.DbUtils;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.Global;

/**
 * zookeeper分布式锁
 * 单例
 * 
 * @author jiafh
 *
 */
public class ZooCuratorLockUtil {
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ZooCuratorLockUtil.class);
	
	private static ZooCuratorLockUtil zooCuratorLockUtil = new ZooCuratorLockUtil();
	
	private final String modelTypeKey = "/modelTypeKey/lock";
	
	private final String modelKey = "/modelKey/lock";
	
	// 厂商表
	private String tabVendor = "tab_vendor";
	
	// 厂商-OUI对应表
	private String tabVendorOui = "tab_vendor_oui";
	
	// 型号表
	private String gwDeviceModel = "gw_device_model";
	
	// 版本表
	private String tabDevicetypeInfo = "tab_devicetype_info";
	
	
	private ZooCuratorLockUtil() {
	}
	
	public static ZooCuratorLockUtil getInstance() {
		return zooCuratorLockUtil;
	}
	
	/**
	 * 获取设备型号ID
	 * 
	 * @param obj
	 * @param systemType
	 * @param key
	 */
	public String getTgwModelId(String oui, String deviceModel, String gwType){
		
		if("4".equals(gwType)) {
			gwDeviceModel = "stb_gw_device_model";
		}
		
		InterProcessLock lock = new InterProcessMutex(Global.curatorFramework, modelKey);
		try {
			// 获取锁
			lock.acquire();
			
			// 获取当前时间
			long currTime = new DateTimeUtil().getLongTime();
			long deviceModelId = 0L;
			
			// 判断型号是否存在
			PrepareSQL queryDevModelSQL = new PrepareSQL("select device_model_id from " + gwDeviceModel + " where vendor_id = ? and device_model = ?");
			queryDevModelSQL.setString(1, StringUtil.getStringValue(oui));
			queryDevModelSQL.setString(2, deviceModel);
			ArrayList<HashMap<String, String>> devModelList = DBOperation.getRecords(queryDevModelSQL.getSQL());
			if(null == devModelList || devModelList.isEmpty()) {
				//deviceModelId = DBOperation.getMaxId("device_model_id", gwDeviceModel) + 1;
				deviceModelId = DbUtils.getUnusedID("sql_" + gwDeviceModel, 1);
				
				// 新增型号
				PrepareSQL addDevModelSQL = new PrepareSQL("insert into " + gwDeviceModel + "(device_model_id,vendor_id,device_model,add_time) values (?,?,?,?)");
				addDevModelSQL.setString(1, StringUtil.getStringValue(deviceModelId));
				addDevModelSQL.setString(2, StringUtil.getStringValue(oui));
				addDevModelSQL.setString(3, deviceModel);
				addDevModelSQL.setLong(4, currTime);
				DBOperation.executeUpdate(addDevModelSQL.getSQL());
			}else {
				deviceModelId = StringUtil.getLongValue(devModelList.get(0).get("device_model_id"));
			}
			
			return StringUtil.getStringValue(deviceModelId);
			
		} catch (Exception e) {
			logger.error("ZooCuratorLockUtil==>getTgwModelTypeId error {}", e.getMessage());
			e.printStackTrace();
			return "-1";
		} finally {
			try {
				// 释放锁
				lock.release();
			} catch (Exception e) {
				logger.error("ZooCuratorLockUtil==>getTgwModelTypeId release lock error {}", e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取设备版本ID
	 * 
	 * @param obj
	 * @param systemType
	 * @param key
	 */
	public String getTgwModelTypeId(String vendorName, String oui, String deviceModel, String softwareversion, String hardwareversion, String specversion){
		
		InterProcessLock lock = new InterProcessMutex(Global.curatorFramework, modelTypeKey);
		try {
			// 获取锁
			lock.acquire();
			
			// 获取当前时间
			long currTime = new DateTimeUtil().getLongTime();
			
			long vendorId = 0L;
			long deviceModelId = 0L;
			long devicetypeId = 0L;
			
			// 根据厂商名称判断厂商是否存在
			PrepareSQL queryVendorSQL = new PrepareSQL("select vendor_id from " + tabVendor + " where vendor_name = ?");
			queryVendorSQL.setString(1,vendorName);
			ArrayList<HashMap<String, String>> vendorList = DBOperation.getRecords(queryVendorSQL.getSQL());
			if(null == vendorList || vendorList.isEmpty()) {
				//vendorId = DBOperation.getMaxId("vendor_id", tabVendor) + 1;
				vendorId = DbUtils.getUnusedID("sql_" + tabVendor, 1);
				
				// 新增厂商
				PrepareSQL addVendorSQL = new PrepareSQL("insert into " + tabVendor + "(vendor_id,vendor_name,vendor_add,add_time) values (?,?,?,?)");
				addVendorSQL.setString(1, StringUtil.getStringValue(vendorId));
				addVendorSQL.setString(2,vendorName);
				addVendorSQL.setString(3,vendorName);
				addVendorSQL.setLong(4, currTime);
				DBOperation.executeUpdate(addVendorSQL.getSQL());
			}else {
				vendorId = StringUtil.getLongValue(vendorList.get(0).get("vendor_id"));
			}
			
			// 判断厂商-OUI对应关系是否存在
			PrepareSQL queryVendorOUISQL = new PrepareSQL("select 1 from " + tabVendorOui + " where vendor_id = ? and oui = ?");
			queryVendorOUISQL.setString(1, StringUtil.getStringValue(vendorId));
			queryVendorOUISQL.setString(2, oui);
			ArrayList<HashMap<String, String>> vendorOUIList = DBOperation.getRecords(queryVendorOUISQL.getSQL());
			if(null == vendorOUIList || vendorOUIList.isEmpty()) {
				PrepareSQL addVendorOUISQL = new PrepareSQL("insert into " + tabVendorOui + "(vendor_id,oui) values (?,?)");
				addVendorOUISQL.setString(1, StringUtil.getStringValue(vendorId));
				addVendorOUISQL.setString(2, oui);
				DBOperation.executeUpdate(addVendorOUISQL.getSQL());
			}
			
			// 判断型号是否存在
			PrepareSQL queryDevModelSQL = new PrepareSQL("select device_model_id from " + gwDeviceModel + " where vendor_id = ? and device_model = ?");
			queryDevModelSQL.setString(1, StringUtil.getStringValue(vendorId));
			queryDevModelSQL.setString(2, deviceModel);
			ArrayList<HashMap<String, String>> devModelList = DBOperation.getRecords(queryDevModelSQL.getSQL());
			if(null == devModelList || devModelList.isEmpty()) {
				//deviceModelId = DBOperation.getMaxId("device_model_id", gwDeviceModel) + 1;
				deviceModelId = DbUtils.getUnusedID("sql_" + gwDeviceModel, 1);
				
				// 新增型号
				PrepareSQL addDevModelSQL = new PrepareSQL("insert into " + gwDeviceModel + "(device_model_id,vendor_id,device_model,add_time) values (?,?,?,?)");
				addDevModelSQL.setString(1, StringUtil.getStringValue(deviceModelId));
				addDevModelSQL.setString(2, StringUtil.getStringValue(vendorId));
				addDevModelSQL.setString(3, deviceModel);
				addDevModelSQL.setLong(4, currTime);
				DBOperation.executeUpdate(addDevModelSQL.getSQL());
			}else {
				deviceModelId = StringUtil.getLongValue(devModelList.get(0).get("device_model_id"));
			}
			
			// 判断版本是否存在
			PrepareSQL queryDevTypeInfoSQL = new PrepareSQL("select devicetype_id from " + tabDevicetypeInfo + " where vendor_id = ? and device_model_id = ? and softwareversion = ? and hardwareversion = ?");
			queryDevTypeInfoSQL.setString(1, StringUtil.getStringValue(vendorId));
			queryDevTypeInfoSQL.setString(2, StringUtil.getStringValue(deviceModelId));
			queryDevTypeInfoSQL.setString(3, softwareversion);
			queryDevTypeInfoSQL.setString(4, hardwareversion);
			ArrayList<HashMap<String, String>> devTypeInfolList = DBOperation.getRecords(queryDevTypeInfoSQL.getSQL());
			if(null == devTypeInfolList || devTypeInfolList.isEmpty()) {
				
				//devicetypeId = DBOperation.getMaxId("devicetype_id", tabDevicetypeInfo) + 1;
				devicetypeId = DbUtils.getUnusedID("sql_" + tabDevicetypeInfo, 1);
				
				// 新增版本
				PrepareSQL addDevTypeInfoSQL = new PrepareSQL("insert into " + tabDevicetypeInfo + "(devicetype_id,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,add_time) values (?,?,?,?,?,?,?)");
				addDevTypeInfoSQL.setLong(1, devicetypeId);
				addDevTypeInfoSQL.setString(2, StringUtil.getStringValue(vendorId));
				addDevTypeInfoSQL.setString(3, StringUtil.getStringValue(deviceModelId));
				addDevTypeInfoSQL.setString(4, specversion);
				addDevTypeInfoSQL.setString(5, hardwareversion);
				addDevTypeInfoSQL.setString(6, softwareversion);
				addDevTypeInfoSQL.setLong(7, currTime);
				DBOperation.executeUpdate(addDevTypeInfoSQL.getSQL());
			}else {
				devicetypeId = StringUtil.getLongValue(devTypeInfolList.get(0).get("devicetype_id"));
			}
			
			return StringUtil.getStringValue(devicetypeId);
			
		} catch (Exception e) {
			logger.error("ZooCuratorLockUtil==>getTgwModelTypeId error {}", e.getMessage());
			e.printStackTrace();
			return "-1";
		} finally {
			try {
				// 释放锁
				lock.release();
			} catch (Exception e) {
				logger.error("ZooCuratorLockUtil==>getTgwModelTypeId release lock error {}", e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
