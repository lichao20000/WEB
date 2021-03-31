
package com.linkage.module.itms.service.obj;

import java.io.Serializable;

/**
 * 
 * @author guankai (Ailk No.300401)
 * @version 1.0
 * @since 2019年12月18日
 * @category com.huawei.base.model
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class Order implements Serializable
{


	private static final long serialVersionUID = 4299862182812154878L;
	
	public String getAd_account()
	{
		return ad_account;
	}
	
	public void setAd_account(String ad_account)
	{
		this.ad_account = ad_account;
	}
	
	public String getAd_userid()
	{
		return ad_userid;
	}
	
	public void setAd_userid(String ad_userid)
	{
		this.ad_userid = ad_userid;
	}
	
	public String getArea_code()
	{
		return area_code;
	}
	
	public void setArea_code(String area_code)
	{
		this.area_code = area_code;
	}
	
	public String getDeviceType()
	{
		return deviceType;
	}
	
	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}
	
	public String getOrder_LSH()
	{
		return order_LSH;
	}
	
	public void setOrder_LSH(String order_LSH)
	{
		this.order_LSH = order_LSH;
	}
	
	public String getOrder_No()
	{
		return order_No;
	}
	
	public void setOrder_No(String order_No)
	{
		this.order_No = order_No;
	}
	
	public String getOrder_Type()
	{
		return order_Type;
	}
	
	public void setOrder_Type(String order_Type)
	{
		this.order_Type = order_Type;
	}
	
	public String getService_code()
	{
		return service_code;
	}
	
	public void setService_code(String service_code)
	{
		this.service_code = service_code;
	}
	
	public String getUser_Type()
	{
		return user_Type;
	}
	
	public void setUser_Type(String user_Type)
	{
		this.user_Type = user_Type;
	}
	
	public String getUser_name()
	{
		return user_name;
	}
	
	public void setUser_name(String user_name)
	{
		this.user_name = user_name;
	}
	
	public String getVector_argues()
	{
		return vector_argues;
	}
	
	public void setVector_argues(String vector_argues)
	{
		this.vector_argues = vector_argues;
	}
	
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	private String ad_account = "";
	
	@Override
	public String toString()
	{
		return "Order [ad_account=" + ad_account + ", ad_userid=" + ad_userid + ", area_code=" + area_code + ", deviceType="
				+ deviceType + ", order_LSH=" + order_LSH + ", order_No=" + order_No + ", order_Type=" + order_Type
				+ ", service_code=" + service_code + ", user_Type=" + user_Type + ", user_name=" + user_name + ", vector_argues="
				+ vector_argues + "]";
	}

	private String ad_userid = "";
	private String area_code = "";
	private String deviceType = "";
	private String order_LSH = "";
	private String order_No = "";
	private String order_Type = "";
	private String service_code = "";
	private String user_Type = "";
	private String user_name = "";
	private String vector_argues = "";
}
