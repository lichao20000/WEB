
package com.linkage.module.itms.config.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.config.dao.DigitDeviceDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-9-19 上午10:10:01
 * @category com.linkage.module.gwms.share.bio
 * @copyright 南京联创科技 网管科技部
 */
public class DigitDeviceBIO
{

	private static Logger logger = LoggerFactory.getLogger(DigitDeviceBIO.class);
	private DigitDeviceDAO digitDeviceDAO;
	private int maxPage_splitPage;

	
	public int getMaxPage_splitPage()
	{
		return maxPage_splitPage;
	}

	
	public void setMaxPage_splitPage(int maxPageSplitPage)
	{
		maxPage_splitPage = maxPageSplitPage;
	}

	public DigitDeviceDAO getDigitDeviceDAO()
	{
		return digitDeviceDAO;
	}

	public void setDigitDeviceDAO(DigitDeviceDAO digitDeviceDAO)
	{
		this.digitDeviceDAO = digitDeviceDAO;
	}

	public List<Map> query(String cityId, String vendorId, String deviceModelId,
			String deviceTypeId, String device_serialnumber, String loopback_ip,
			String device_id_ex, String task_name,int curPage_splitPage,int num_splitPage)
	{   maxPage_splitPage=digitDeviceDAO.getListCount(cityId, vendorId, deviceModelId, deviceTypeId,
			device_serialnumber, loopback_ip, device_id_ex, task_name,curPage_splitPage, num_splitPage);
		return digitDeviceDAO.query(cityId, vendorId, deviceModelId, deviceTypeId,
				device_serialnumber, loopback_ip, device_id_ex, task_name,curPage_splitPage, num_splitPage);
	}
}
