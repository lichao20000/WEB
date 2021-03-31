
package com.linkage.module.gtms.stb.resource.dto;

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

public class BSSStatisticsDTO implements Serializable
{

	private static final long serialVersionUID = -6171329682675757955L;
	// 属地
	private String cityId;
	private String cityName;
	// 是否包含下级属地 1 是 2否
	private Integer subordinate;
	// 业务帐号
	private String servAccount;
	// 操作类型
	// 0：开户1：销户2：移机3：修正
	private String opera;
	private String operation;
	// 接入方式
	private String addressingType;
	// 产品ID
	private String prodId;
	// BSS工单时间
	private String bssDate;
	// 开始时间
	private String fromTime;
	private Long beginTime;
	// 结束时间
	private String toTime;
	private Long endTime;

	public Integer getSubordinate()
	{
		return subordinate;
	}

	public void setSubordinate(Integer subordinate)
	{
		this.subordinate = subordinate;
	}

	public String getServAccount()
	{
		return servAccount;
	}

	public void setServAccount(String servAccount)
	{
		this.servAccount = servAccount;
	}

	public String getProdId()
	{
		return prodId;
	}

	public void setProdId(String prodId)
	{
		this.prodId = prodId;
	}

	public String getBssDate()
	{
		return bssDate;
	}

	public void setBssDate(String bssDate)
	{
		this.bssDate = bssDate;
	}

	public String getFromTime()
	{
		return fromTime;
	}

	public void setFromTime(String fromTime)
	{
		this.fromTime = fromTime;
	}

	public String getToTime()
	{
		return toTime;
	}

	public void setToTime(String toTime)
	{
		this.toTime = toTime;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public void setOperation(String operation)
	{
		this.operation = operation;
	}

	public String getOperation()
	{
		return operation;
	}

	public void setBeginTime(Long beginTime)
	{
		this.beginTime = beginTime;
	}

	public Long getBeginTime()
	{
		return beginTime;
	}

	public void setEndTime(Long endTime)
	{
		this.endTime = endTime;
	}

	public Long getEndTime()
	{
		return endTime;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public String getCityName()
	{
		return cityName;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setOpera(String opera)
	{
		this.opera = opera;
	}

	public String getOpera()
	{
		return opera;
	}

	public String getAddressingType()
	{
		return addressingType;
	}

	public void setAddressingType(String addressingType)
	{
		this.addressingType = addressingType;
	}
}
