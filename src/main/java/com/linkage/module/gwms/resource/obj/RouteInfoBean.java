/**
 * 
 */
package com.linkage.module.gwms.resource.obj;

/**
 * MQ对象类
 * @author chenjie
 * @version 1.0
 * @since 2013-6-6
 */
public class RouteInfoBean {
	//查询类型1:单条件查询2:多条件查询
	private String query_type;
	//单条件查询类型:1设备序列号2宽带账号3逻辑ID4电话号码
	private String queryType;
	private String single_condition;
	private String gwShare_cityId;
	private String gwShare_vendorId;
	private String gwShare_deviceModelId;
	private String gwShare_devicetypeId;
	private String gwShare_netType;
	private String start_time;
	private String end_time;
	private String gw_type;
	
	public String getQuery_type()
	{
		return query_type;
	}
	
	public void setQuery_type(String query_type)
	{
		this.query_type = query_type;
	}
	
	public String getQueryType()
	{
		return queryType;
	}
	
	public void setQueryType(String queryType)
	{
		this.queryType = queryType;
	}
	
	public String getSingle_condition()
	{
		return single_condition;
	}
	
	public void setSingle_condition(String single_condition)
	{
		this.single_condition = single_condition;
	}
	
	public String getGwShare_cityId()
	{
		return gwShare_cityId;
	}
	
	public void setGwShare_cityId(String gwShare_cityId)
	{
		this.gwShare_cityId = gwShare_cityId;
	}
	
	public String getGwShare_vendorId()
	{
		return gwShare_vendorId;
	}
	
	public void setGwShare_vendorId(String gwShare_vendorId)
	{
		this.gwShare_vendorId = gwShare_vendorId;
	}
	
	public String getGwShare_deviceModelId()
	{
		return gwShare_deviceModelId;
	}
	
	public void setGwShare_deviceModelId(String gwShare_deviceModelId)
	{
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}
	
	public String getGwShare_devicetypeId()
	{
		return gwShare_devicetypeId;
	}
	
	public void setGwShare_devicetypeId(String gwShare_devicetypeId)
	{
		this.gwShare_devicetypeId = gwShare_devicetypeId;
	}
	
	public String getGwShare_netType()
	{
		return gwShare_netType;
	}
	
	public void setGwShare_netType(String gwShare_netType)
	{
		this.gwShare_netType = gwShare_netType;
	}
	
	public String getStart_time()
	{
		return start_time;
	}
	
	public void setStart_time(String start_time)
	{
		this.start_time = start_time;
	}
	
	public String getEnd_time()
	{
		return end_time;
	}
	
	public void setEnd_time(String end_time)
	{
		this.end_time = end_time;
	}

	
	public String getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}
	
}
