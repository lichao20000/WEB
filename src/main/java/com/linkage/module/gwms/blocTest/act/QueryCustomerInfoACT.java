
package com.linkage.module.gwms.blocTest.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.module.gwms.blocTest.bio.QueryCustomerInfoBIO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-8-4 上午09:01:10
 * @category com.linkage.module.gwms.blocTest.act
 * @copyright 南京联创科技 网管科技部
 */
public class QueryCustomerInfoACT implements SessionAware
{

	private Map session;
	// 客户用户相关信息
	private List customerList;
	private QueryCustomerInfoBIO bio;
	//设备id
	private String device_id;
	//用户id
	private int user_id;
	//客户id
	private String customer_id;

	
	public int getUser_id()
	{
		return user_id;
	}
	
	public void setUser_id(int userId)
	{
		user_id = userId;
	}
	
	public String getCustomer_id()
	{
		return customer_id;
	}
	
	public void setCustomer_id(String customerId)
	{
		customer_id = customerId;
	}
	public String query()

	{	
		customerList = bio.QueryCustomerInfo(device_id);
		 return "query";
	}
	public List getCustomerList()
	{
		return customerList;
	}

	public void setCustomerList(List customerList)
	{
		this.customerList = customerList;
	}

	public QueryCustomerInfoBIO getBio()
	{
		return bio;
	}

	public void setBio(QueryCustomerInfoBIO bio)
	{
		this.bio = bio;
	}

	public String getDevice_id()
	{
		return device_id;
	}

	public void setDevice_id(String deviceId)
	{
		device_id = deviceId;
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}
}
