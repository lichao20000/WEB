
package com.linkage.module.gwms.resource.act;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.resource.bio.FamilyNetTopnBIO;
import com.linkage.module.gwms.resource.model.NetTopologicalInfoModel;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 家庭网络拓扑
 * 
 * @author yages (Ailk No.78987)
 * @version 1.0
 * @since 2019-11-4
 * @category com.linkage.module.gwms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings("serial")
public class FamilyNetTopnACT extends ActionSupport implements SessionAware
{

	private static Logger logger = LoggerFactory.getLogger(FamilyNetTopnACT.class);
	private FamilyNetTopnBIO bio;
	// 获取数据方式 1：ItmsService接口 2：集团接口 3:直接查询数据库
	private String query_type;
	// 未获取到数据处理：1：查数据库上次记录 2：调用其他接口
	private String next_handle;
	// 设备id
	private String device_id;
	private NetTopologicalInfoModel data;
	private String ajax;

	public String query()
	{
		logger.warn("FamilyNetTopN方法入口,device_Id[{}],query_type[{}],next_handle[{}]",
				device_id, query_type, next_handle);
		if (StringUtil.IsEmpty(device_id) || StringUtil.IsEmpty(query_type)
				|| StringUtil.IsEmpty(next_handle))
		{
			ajax = "";
			logger.warn("请传入设备id等信息");
			return "ajax";
		}
		ajax = JSON.toJSONString(bio.getTopnInfo(device_id, query_type, next_handle));
		logger.warn("FamilyNetTopN方法结束,返回参数为[{}],device_Id[{}]",ajax, device_id);
		return "ajax";
	}

	/**
	 * @return the bio
	 */
	public FamilyNetTopnBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(FamilyNetTopnBIO bio)
	{
		this.bio = bio;
	}

	/**
	 * @return the query_type
	 */
	public String getQuery_type()
	{
		return query_type;
	}

	/**
	 * @param query_type
	 *            the query_type to set
	 */
	public void setQuery_type(String query_type)
	{
		this.query_type = query_type;
	}

	/**
	 * @return the next_handle
	 */
	public String getNext_handle()
	{
		return next_handle;
	}

	/**
	 * @param next_handle
	 *            the next_handle to set
	 */
	public void setNext_handle(String next_handle)
	{
		this.next_handle = next_handle;
	}

	/**
	 * @return the device_id
	 */
	public String getDevice_id()
	{
		return device_id;
	}

	/**
	 * @param device_id
	 *            the device_id to set
	 */
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	/**
	 * @return the data
	 */
	public NetTopologicalInfoModel getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(NetTopologicalInfoModel data)
	{
		this.data = data;
	}

	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
	 */
	@Override
	public void setSession(Map<String, Object> session)
	{
		// TODO Auto-generated method stub
		
	}
}
