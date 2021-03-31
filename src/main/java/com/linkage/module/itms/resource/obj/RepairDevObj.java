
package com.linkage.module.itms.resource.obj;

import java.util.List;

public class RepairDevObj
{

	/**
	 * 厂商名称
	 */
	private String vendor_name = null;
	/**
	 * 型号子节点
	 */
	@SuppressWarnings("rawtypes")
	List<List> childList = null;

	public String getVendor_name()
	{
		return vendor_name;
	}

	@SuppressWarnings("rawtypes")
	public List<List> getChildList()
	{
		return childList;
	}

	public void setVendor_name(String vendor_name)
	{
		this.vendor_name = vendor_name;
	}

	@SuppressWarnings("rawtypes")
	public void setChildList(List<List> childList)
	{
		this.childList = childList;
	}
}
