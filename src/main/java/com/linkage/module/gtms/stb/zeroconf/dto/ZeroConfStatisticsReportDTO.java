
package com.linkage.module.gtms.stb.zeroconf.dto;

/**
 * 零配件BSS工单统计实体类
 * 
 * @author 田启明
 * @version 1.0
 * @since 2011-12-02
 * @category com.linkage.module.itv.zeroconf.dto<br>
 *           版权：南京联创科技 网管科技部
 */
import java.io.Serializable;

public class ZeroConfStatisticsReportDTO implements Serializable
{

	private static final long serialVersionUID = -6171329682675757955L;
	// 属地
	private String cityId;
	private String cityName;
	// 开通状态
	private String userStatus;
	// 开始时间
	private String fromTime;
	private Long beginTime;
	// 结束时间
	private String toTime;
	private Long endTime;
	private String custAccessType;

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getCityName()
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public String getUserStatus()
	{
		return userStatus;
	}

	public void setUserStatus(String userStatus)
	{
		this.userStatus = userStatus;
	}

	public String getFromTime()
	{
		return fromTime;
	}

	public void setFromTime(String fromTime)
	{
		this.fromTime = fromTime;
	}

	public Long getBeginTime()
	{
		return beginTime;
	}

	public void setBeginTime(Long beginTime)
	{
		this.beginTime = beginTime;
	}

	public String getToTime()
	{
		return toTime;
	}

	public void setToTime(String toTime)
	{
		this.toTime = toTime;
	}

	public Long getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Long endTime)
	{
		this.endTime = endTime;
	}

	public String getCustAccessType()
	{
		return custAccessType;
	}

	public void setCustAccessType(String custAccessType)
	{
		this.custAccessType = custAccessType;
	}
}
