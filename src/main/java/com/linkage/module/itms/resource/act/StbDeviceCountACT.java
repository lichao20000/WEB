
package com.linkage.module.itms.resource.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.bio.StbDeviceCountBIO;

public class StbDeviceCountACT extends splitPageAction implements SessionAware,
		ServletRequestAware
{

	// 序列化
	private static final long serialVersionUID = 1L;
	// 日志
	private static Logger logger = LoggerFactory.getLogger(StbDeviceCountACT.class);
	// request取登陆帐号使用
	private HttpServletRequest request;
	// SESSION
	private Map<String, Object> session;
	// 返回类型
	private String returnType;
	// 业务逻辑处理
	private StbDeviceCountBIO bio;
	// 属地列表
	private List<Map<String, String>> cityList = null;
	// 查询结果的标题
	private List<String> titleList = null;
	// 导出数据
	private List<Map<String, String>> data = null;
	// 厂商
	private String vendorId;
	// 属地
	private String cityId;
	@SuppressWarnings("rawtypes")
	private List<Map> stbDevList = null;

	/*
	 * 设备数量统计
	 */
	public String deviceCountList()
	{
		logger.debug("StbDeviceCountACT=>deviceCountList()");
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(userCityId);
		titleList = new ArrayList<String>();
		titleList.add("厂商");
		for (int i = 0; i < cityList.size(); i++)
		{
			titleList.add(StringUtil.getStringValue(cityList.get(i), "city_name"));
		}
		titleList.add("小计");
		data = bio.getCountData(userCityId);
		if ("excel".equals(returnType))
		{
			return "excel";
		}
		else
		{
			return "list";
		}
	}

	/*
	 * 详细页面展示
	 */
	public String getDetailInfo()
	{
		logger.debug("StbDeviceCountACT=>getDetailInfo()");
		stbDevList = bio
				.getDetailInfo(vendorId, cityId, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countQueryDetail(vendorId, cityId, curPage_splitPage,
				num_splitPage);
		return "detail";
	}

	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public Map<String, Object> getSession()
	{
		return session;
	}

	public String getReturnType()
	{
		return returnType;
	}

	public void setReturnType(String returnType)
	{
		this.returnType = returnType;
	}

	public void setBio(StbDeviceCountBIO bio)
	{
		this.bio = bio;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public List<String> getTitleList()
	{
		return titleList;
	}

	public void setTitleList(List<String> titleList)
	{
		this.titleList = titleList;
	}

	public List<Map<String, String>> getData()
	{
		return data;
	}

	public void setData(List<Map<String, String>> data)
	{
		this.data = data;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getStbDevList()
	{
		return stbDevList;
	}

	@SuppressWarnings("rawtypes")
	public void setStbDevList(List<Map> stbDevList)
	{
		this.stbDevList = stbDevList;
	}
}
