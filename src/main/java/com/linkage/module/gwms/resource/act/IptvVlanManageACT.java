
package com.linkage.module.gwms.resource.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.resource.bio.IptvVlanManageBIO;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("unchecked")
public class IptvVlanManageACT extends ActionSupport implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(IptvVlanManageACT.class);
	// session
	private Map session;
	private IptvVlanManageBIO iptvVlanManageBio;
	private List cityVlanList = null;
	private List cityList = null;
	// 处理类型 1为增加 2为修改
	private String type = null;
	private String cityId;
	private String minVlanid;
	private String maxVlanid;
	private String basIp;
	private String ajax;
	private String id;

	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		if ("00".equals(curUser.getCityId()))
		{
			cityList = CityDAO.getNextCityListByCityPidCore(curUser.getCityId());
			cityVlanList = iptvVlanManageBio.getCityVlanList();
		}
		else
		{
			cityList = new ArrayList<Map>();
			Map<String, String> map = new HashMap<String, String>();
			String cid = CityDAO.getLocationCityIdByCityId(curUser.getCityId());
			Map<String, String> cmap = CityDAO.getCityIdCityNameMap();
			map.put("city_id", cid);
			map.put("city_name", cmap.get(cid));
			cityList.add(map);
			cityVlanList = iptvVlanManageBio.getCityVlanList(cid);
			cmap = null;
		}
		return "list";
	}

	/**
	 * 修改VLAN值
	 *
	 * @author wangsenbo
	 * @date Jan 11, 2010
	 * @return String
	 */
	public String configVlan()
	{
		logger.debug("configVlan()");
		UserRes curUser = (UserRes) session.get("curUser");
		ajax = iptvVlanManageBio.configVlan(curUser.getUser().getId(), cityId, minVlanid,
				maxVlanid, basIp, type, id);
		return "ajax";
	}
	
	/**
	 * 删除VLAN值
	 *
	 * @author wangsenbo
	 * @date Jan 11, 2010
	 * @return String
	 */
	public String delVlan()
	{
		logger.debug("delVlan({})",id);
		ajax = iptvVlanManageBio.delVlan(id);
		return "ajax";
	}

	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * @return the iptvVlanManageBio
	 */
	public IptvVlanManageBIO getIptvVlanManageBio()
	{
		return iptvVlanManageBio;
	}

	/**
	 * @param iptvVlanManageBio
	 *            the iptvVlanManageBio to set
	 */
	public void setIptvVlanManageBio(IptvVlanManageBIO iptvVlanManageBio)
	{
		this.iptvVlanManageBio = iptvVlanManageBio;
	}

	/**
	 * @return the cityVlanList
	 */
	public List getCityVlanList()
	{
		return cityVlanList;
	}

	/**
	 * @param cityVlanList
	 *            the cityVlanList to set
	 */
	public void setCityVlanList(List cityVlanList)
	{
		this.cityVlanList = cityVlanList;
	}

	/**
	 * @return the cityList
	 */
	public List getCityList()
	{
		return cityList;
	}

	/**
	 * @param cityList
	 *            the cityList to set
	 */
	public void setCityList(List cityList)
	{
		this.cityList = cityList;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId()
	{
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	/**
	 * @return the minVlanid
	 */
	public String getMinVlanid()
	{
		return minVlanid;
	}

	/**
	 * @param minVlanid
	 *            the minVlanid to set
	 */
	public void setMinVlanid(String minVlanid)
	{
		this.minVlanid = minVlanid;
	}

	/**
	 * @return the maxVlanid
	 */
	public String getMaxVlanid()
	{
		return maxVlanid;
	}

	/**
	 * @param maxVlanid
	 *            the maxVlanid to set
	 */
	public void setMaxVlanid(String maxVlanid)
	{
		this.maxVlanid = maxVlanid;
	}

	/**
	 * @return the basIp
	 */
	public String getBasIp()
	{
		return basIp;
	}

	/**
	 * @param basIp
	 *            the basIp to set
	 */
	public void setBasIp(String basIp)
	{
		this.basIp = basIp;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
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

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}
}
