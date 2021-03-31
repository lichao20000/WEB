
package com.linkage.module.gtms.stb.resource.dto;

/**
 * itv手动下发配置dto
 * 
 * @author zhumiao
 * @version 1.0
 * @since 2011-12-5 下午03:22:35
 * @category com.linkage.module.lims.itv.zeroconf.dto<br>
 * @copyright 南京联创科技 网管科技部
 */
public class ZeroconfManualDTO
{

	/** 设备Id **/
	private String device_id;
	/** 客户Id **/
	private String customer_id;
	/** 业务帐号 **/
	private String serv_account;
	/** 机顶盒oui **/
	private String oui;
	/** 机顶盒序列号 **/
	private String device_serialnumber;
	/** 查询内容 **/
	private String queryStr;
	/**
	 * 查询类型 1：业务帐号 2：设备序列号
	 */
	private String queryType;
	/** 产品id **/
	private String prod_id;
	/** 工单编号 **/
	private String work_id;
	/** 配置下发时间 **/
	private String conf_down_time;
	/** 属地 **/
	private String city_id;
	/** 接入类型 **/
	private String addressing_type;

	public String getAddressing_type()
	{
		return addressing_type;
	}

	public void setAddressing_type(String addressing_type)
	{
		this.addressing_type = addressing_type;
	}

	public String getCity_id()
	{
		return city_id;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	public String getWork_id()
	{
		return work_id;
	}

	public void setWork_id(String work_id)
	{
		this.work_id = work_id;
	}

	public String getConf_down_time()
	{
		return conf_down_time;
	}

	public void setConf_down_time(String conf_down_time)
	{
		this.conf_down_time = conf_down_time;
	}

	public String getProd_id()
	{
		return prod_id;
	}

	public void setProd_id(String prod_id)
	{
		this.prod_id = prod_id;
	}

	public String getQueryStr()
	{
		return queryStr;
	}

	public void setQueryStr(String queryStr)
	{
		this.queryStr = queryStr;
	}

	public String getQueryType()
	{
		return queryType;
	}

	public void setQueryType(String queryType)
	{
		this.queryType = queryType;
	}

	public String getOui()
	{
		return oui;
	}

	public void setOui(String oui)
	{
		this.oui = oui;
	}

	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}

	public String getServ_account()
	{
		return serv_account;
	}

	public void setServ_account(String serv_account)
	{
		this.serv_account = serv_account;
	}

	
	public String getDevice_id()
	{
		return device_id;
	}

	
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	
	public String getCustomer_id()
	{
		return customer_id;
	}

	
	public void setCustomer_id(String customer_id)
	{
		this.customer_id = customer_id;
	}
	
}
