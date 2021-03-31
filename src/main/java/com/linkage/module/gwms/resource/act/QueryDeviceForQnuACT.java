
package com.linkage.module.gwms.resource.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.resource.bio.QueryDeviceForQnuBIO;

/**
 * @author yinlei3 (73167)
 * @version 1.0
 * @since 2016年1月5日
 * @category com.linkage.module.gwms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class QueryDeviceForQnuACT extends splitPageAction
{

	/** 序列化 */
	private static final long serialVersionUID = 1L;
	/** 日志记录 */
	private static Logger logger = LoggerFactory
			.getLogger(QueryDeviceForQnuACT.class);
	/** 设备序列号 */
	private String device_serialnumber = "";
	// 导出数据
	@SuppressWarnings("rawtypes")
	private List<Map> data;
	private QueryDeviceForQnuBIO bio;

	/**
	 * 查询社会化管控设备
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String queryDevice() throws Exception
	{
		logger.debug("queryDevice()");
		if (!StringUtil.IsEmpty(device_serialnumber))
		{
			device_serialnumber = device_serialnumber.trim();
		}
		data = bio.getImpDeviceList(curPage_splitPage, num_splitPage, device_serialnumber);
		maxPage_splitPage = bio.getImpDeviceCount(curPage_splitPage, num_splitPage,
				device_serialnumber);
		return "queryDeviceForQnu";
	}

	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getData()
	{
		return data;
	}

	@SuppressWarnings("rawtypes")
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public void setBio(QueryDeviceForQnuBIO bio)
	{
		this.bio = bio;
	}
}
