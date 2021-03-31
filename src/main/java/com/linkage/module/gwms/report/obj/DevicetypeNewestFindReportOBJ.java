/**
 * 
 */
package com.linkage.module.gwms.report.obj;

import java.util.List;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-9-9
 * @category com.linkage.module.gwms.report.obj
 * 
 */
public class DevicetypeNewestFindReportOBJ {

	/**
	 * 厂商ID
	 */
	private String vendor_id = null;
	
	/**
	 * 厂商名称
	 */
	private String vendor_name = null;
	
	/**
	 * 具有版本个数
	 */
	long childInt = 0;
	
	/**
	 * 型号子节点
	 */
	List<DevicetypeChildOBJ> childList = null;

	/**
	 * @return the childInt
	 */
	public long getChildInt() {
		return childInt;
	}

	/**
	 * @param childInt the childInt to set
	 */
	public void setChildInt(long childInt) {
		this.childInt = childInt;
	}

	/**
	 * @return the childList
	 */
	public List<DevicetypeChildOBJ> getChildList() {
		return childList;
	}

	/**
	 * @param childList the childList to set
	 */
	public void setChildList(List<DevicetypeChildOBJ> childList) {
		this.childList = childList;
	}

	/**
	 * @return the vendor_id
	 */
	public String getVendor_id() {
		return vendor_id;
	}

	/**
	 * @param vendor_id the vendor_id to set
	 */
	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}

	/**
	 * @return the vendor_name
	 */
	public String getVendor_name() {
		return vendor_name;
	}

	/**
	 * @param vendor_name the vendor_name to set
	 */
	public void setVendor_name(String vendor_name) {
		this.vendor_name = vendor_name;
	}
	
	
}
