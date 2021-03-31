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
public class DevicetypeChildOBJ {

	/**
	 * 型号ID
	 */
	private String device_model = null;
	
	/**
	 * 具有版本个数
	 */
	long childInt = 0;
	
	/**
	 * 统计各本地网的终端数目
	 */
	List<List> num = null;
	
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
	 * @return the num
	 */
	public List<List> getNum() {
		return num;
	}

	/**
	 * @param num the num to set
	 */
	public void setNum(List<List> num) {
		this.num = num;
	}

	/**
	 * @return the device_model
	 */
	public String getDevice_model() {
		return device_model;
	}

	/**
	 * @param device_model the device_model to set
	 */
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}
	
}
