package com.linkage.liposs.resource;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
/**
 * <li>此类用于获取网管系统中现有的厂商、设备型号数据，供前台页面展示厂商下拉框、设备型号下拉框
 * <li>避免出现有厂商、有设备型号但是选择后无设备情况。
 */
public class VendorHelper {
 
	//private static final String sqlVendor = "select vendor_id,vendor_name from tab_vendor where vendor_id in (select distinct oui from tab_gw_device where oui != '' and oui != null)";
	private static final String sqlVendor = "select vendor_id,vendor_name from tab_vendor";
	private static final String sqlVendorModel = "select distinct device_model_id as device_model from tab_gw_device where vendor_id=? order by device_model_id";
	
	private static final String sqlDeviceVenModel = "select a.* from tab_gw_device a where a.vendor_id=? and a.device_model_id=?";
	
	/**
	 * 从网管设备资源列表中获取现有的设备厂商数据
	 * @return 
	 */
	public static Cursor getResourceVendor(){
		PrepareSQL psql = new PrepareSQL(sqlVendor);
		return DataSetBean.getCursor(psql.getSQL());
	}
	/**
	 * 根据厂商编号从网管中设备资源列表中获取现有的设备型号数据
	 * @param vendor_id
	 * @return
	 */
	public static Cursor getResourceVendorModel(String vendor_id){
		PrepareSQL pSQL = new PrepareSQL(sqlVendorModel);
		pSQL.setStringExt(1, vendor_id,false);
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	/**
	 * 根据厂商id和设备型号从网管设备资源列表中获取设备
	 * @param vendor_id
	 * @param device_model
	 * @return
	 */
	public static Cursor getDeviceResource(String vendor_id,String device_model){
		PrepareSQL pSQL = new PrepareSQL(sqlDeviceVenModel);
		pSQL.setStringExt(1, vendor_id,false);
		pSQL.setString(2, device_model);
		
		return DataSetBean.getCursor(pSQL.getSQL());
	}
}
