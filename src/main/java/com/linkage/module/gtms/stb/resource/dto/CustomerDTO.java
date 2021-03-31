
package com.linkage.module.gtms.stb.resource.dto;

import java.io.Serializable;

/**
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-8-26
 * @category com.linkage.module.lims.itv.zeroconf.dto
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class CustomerDTO implements Serializable
{

	/**
	 * 包含下级属地
	 */
	public static final String SUBORDINATE_YES = "1";
	/**
	 * 不包含下级属地
	 */
	public static final String SUBORDINATE_NO = "2";
	private static final long serialVersionUID = -628281091691676790L;
	/**
	 * 属地ID
	 */
	private String cityId;
	/**
	 * 是否包含下级属地，1：是 2：否
	 */
	private String subordinate;
	/**
	 * 开始时间，格式yyyy-MM-dd
	 */
	private String startTime;
	/**
	 * 结束时间，格式yyyy-MM-dd
	 */
	private String endTime;
	/**
	 * 业务账号
	 */
	private String servAccount;
	/**
	 * 接入账号
	 */
	private String pppoeUser;
	/**
	 * 开通状态 -1：失败 0：未做 1：成功
	 */
	private String userStatus;
	/**
	 * 接入方式 1:FTTH 2:FTTB 3:LAN
	 */
	private String stbuptyle;
	/**
	 * 客户类型 1:家庭网关 5:政企网关
	 */
	private String custType;
	/**
	 * 吉林电信加loid
	 */
	private String loid;

	private String orderNo;

	/**
	 * 山西联通加 机顶盒序列号
	 */
	private String deviceSerialnumber;
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getSubordinate()
	{
		return subordinate;
	}

	public void setSubordinate(String subordinate)
	{
		this.subordinate = subordinate;
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

	public String getServAccount()
	{
		return servAccount;
	}

	public void setServAccount(String servAccount)
	{
		this.servAccount = servAccount;
	}

	public String getPppoeUser()
	{
		return pppoeUser;
	}

	public void setPppoeUser(String pppoeUser)
	{
		this.pppoeUser = pppoeUser;
	}

	public String getUserStatus()
	{
		return userStatus;
	}

	public void setUserStatus(String userStatus)
	{
		this.userStatus = userStatus;
	}

	public String getStbuptyle()
	{
		return stbuptyle;
	}

	public void setStbuptyle(String stbuptyle)
	{
		this.stbuptyle = stbuptyle;
	}

	
	public String getCustType()
	{
		return custType;
	}

	
	public void setCustType(String custType)
	{
		this.custType = custType;
	}

	public String getLoid() {
		return loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}
	
	public String getDeviceSerialnumber() {
		return deviceSerialnumber;
	}

	public void setDeviceSerialnumber(String deviceSerialnumber) {
		this.deviceSerialnumber = deviceSerialnumber;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerDTO [cityId=");
		builder.append(cityId);
		builder.append(", subordinate=");
		builder.append(subordinate);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", servAccount=");
		builder.append(servAccount);
		builder.append(", pppoeUser=");
		builder.append(pppoeUser);
		builder.append(", userStatus=");
		builder.append(userStatus);
		builder.append(", stbuptyle=");
		builder.append(stbuptyle);
		builder.append(", custType=");
		builder.append(custType);
		builder.append(", loid=");
		builder.append(loid);
		builder.append(", deviceSerialnumber=");
		builder.append(deviceSerialnumber);
		builder.append("]");
		return builder.toString();
	}
}
