
package com.linkage.module.gtms.stb.resource.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.resource.serv.ConfigInfoBIO;

import action.splitpage.splitPageAction;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2012-2-28 上午09:48:01
 * @category com.linkage.module.lims.stb.resource.act
 * @copyright 南京联创科技 网管科技部
 */
public class ConfigInfoACT extends splitPageAction
{

	private ConfigInfoBIO bio;
	private String servAccount;
	private String serialnumber;
	private String startTime;
	private String endTime;
	private String queryTime;
	private List<Map> deviceIdsList = new ArrayList();
	private List<Map> configInfoList = new ArrayList();
	private String device_id;
	/*
	 * 历史配置信息详情
	 */
	private Map dataDetail;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ConfigInfoACT.class);

	public String execute()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(calendar.getTimeInMillis() - 24 * 3600 * 1000);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		queryTime = "" + year + "-" + month + "-" + day + " 00:00:00";
		return "success";
	}

	public String query()
	{
		deviceIdsList = bio.deviceIds(servAccount, serialnumber);
		if (null == deviceIdsList || deviceIdsList.size() == 0)
		{
			return "list";
		}
		configInfoList = bio.query(servAccount, serialnumber, deviceIdsList,
				formatTime(startTime), formatTime(endTime), curPage_splitPage,
				num_splitPage);
		maxPage_splitPage = bio.getCount(servAccount, serialnumber, deviceIdsList,
				formatTime(startTime), formatTime(endTime), curPage_splitPage,
				num_splitPage);
		return "list";
	}

	public long formatTime(String time)
	{
		Date date = new Date();
		if (time == null && "".equals(time))
		{
			return 0;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			date = format.parse(time);
		}
		catch (ParseException e)
		{
			return System.currentTimeMillis() / 1000;
		}
		return date.getTime() / 1000;
	}

	public String queryDetail()
	{
		logger.debug("queryDetail");
		dataDetail = bio.queryDetail(device_id, formatTime(startTime),
				formatTime(endTime));
		return "detail";
	}

	public ConfigInfoBIO getBio()
	{
		return bio;
	}

	public void setBio(ConfigInfoBIO bio)
	{
		this.bio = bio;
	}

	public String getServAccount()
	{
		return servAccount;
	}

	public void setServAccount(String servAccount)
	{
		this.servAccount = servAccount;
	}

	public String getSerialnumber()
	{
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber)
	{
		this.serialnumber = serialnumber;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public List<Map> getConfigInfoList()
	{
		return configInfoList;
	}

	public void setConfigInfoList(List<Map> configInfoList)
	{
		this.configInfoList = configInfoList;
	}

	public List<Map> getDeviceIdsList()
	{
		return deviceIdsList;
	}

	public void setDeviceIdsList(List<Map> deviceIdsList)
	{
		this.deviceIdsList = deviceIdsList;
	}

	public String getQueryTime()
	{
		return queryTime;
	}

	public void setQueryTime(String queryTime)
	{
		this.queryTime = queryTime;
	}

	public String getDevice_id()
	{
		return device_id;
	}

	public void setDevice_id(String deviceId)
	{
		device_id = deviceId;
	}

	public Map getDataDetail()
	{
		return dataDetail;
	}

	public void setDataDetail(Map dataDetail)
	{
		this.dataDetail = dataDetail;
	}
}
