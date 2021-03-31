/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.litms.common.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

/**
 * util for vendor.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jul 12, 2008
 * @see
 * @since 1.0
 */
public class VendorUtil {

    /** log */
    private static Logger logger = LoggerFactory
	    .getLogger(VendorUtil.class);

    /**
     * 获取厂商名字
     * 
     * @param device_id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String getManufacturerByDeviceId(String device_id) {
	logger.debug("getManufacturerByDeviceId({})", device_id);
//	String sql = "select vendor_name from tab_gw_device a, tab_vendor b"
//		+ " where device_id='" + device_id + "' and a.oui=b.vendor_id";
	String sql = "select b.vendor_name from tab_gw_device a, tab_vendor b"
		+ " where a.device_id='" + device_id + "' and a.vendor_id=b.vendor_id";
	PrepareSQL psql = new PrepareSQL(sql);
	psql.getSQL();
	Cursor cursor = DataSetBean.getCursor(sql);
	Map fields = cursor.getNext();
	if (fields != null) {
	    logger
		    .debug("vendor_name:()", fields.get("vendor_name")
			    .toString());
	    return fields.get("vendor_name").toString();
	} else {
	    logger.debug("no data in db");
	    return "";
	}
    }

    /**
     * 是否思科设备
     * 
     * @param manufacturer
     * @return
     */
    public static boolean IsCisco(String manufacturer) {
	logger.debug("IsCisco({})", manufacturer);
	if (manufacturer == null) {
	    return false;
	}

	if ("CISCO".equalsIgnoreCase(manufacturer)) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * 根据device_id判断是否思科设备
     * 
     * @param device_id
     * @return
     */
    public static boolean IsCiscoByDeviceId(String device_id) {
	//return IsCisco(getManufacturerByDeviceId(device_id));
	return isRight("CISCO", device_id);
    }

    /**
     * 根据device_id判断是否华三设备
     * 
     * @param device_id
     * @return
     */
    public static boolean IsH3CByDeviceId(String device_id) {
	return isRight("H3C", device_id);
    }
    /**
     * 根据device_id判断是否阿朗设备
     * 
     * @param device_id
     * @return
     */
    public static boolean IsAlcatelByDeviceId(String device_id) {
	return isRight("Alcatel", device_id);
    }
    /**
     * 根据device_id判断是否大亚设备
     * 
     * @param device_id
     * @return
     */
    public static boolean IsDAREByDeviceId(String device_id) {
	return isRight("DARE", device_id);
    }
    /**
     * 根据device_id判断是否大唐设备
     * 
     * @param device_id
     * @return
     */
    public static boolean IsDatangByDeviceId(String device_id) {
	return isRight("Datang", device_id);
    }

    /**
     * 根据device_id判断是否中兴设备
     * 
     * @param device_id
     * @return
     */
    public static boolean IsZTEByDeviceId(String device_id) {
	return isRight("ZTE", device_id);
    }
    /**
     * 根据device_id判断是否DLink设备
     * 
     * @param device_id
     * @return
     */
    public static boolean IsDLinkByDeviceId(String device_id) {
	return isRight("DLink", device_id);
    }
    /**
     * 根据device_id判断是否华为设备
     * 
     * @param device_id
     * @return
     */
    public static boolean IsHuaweiByDeviceId(String device_id) {
	return isRight("Huawei", device_id);
    }
    /**
     * 根据device_id判断是否贝尔设备
     * 
     * @param device_id
     * @return
     */
    public static boolean IsBroadcomByDeviceId(String device_id) {
	return isRight("Broadcom", device_id);
    }
    public static boolean isRight(String vendor_name, String device_id) {
	String manufacturer = getManufacturerByDeviceId(device_id);
	logger.debug("Is" + vendor_name + "({})", manufacturer);
	return null != manufacturer && -1 != manufacturer.toLowerCase().indexOf(vendor_name.toLowerCase());
    }
}
