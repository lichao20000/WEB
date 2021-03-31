
package com.linkage.module.itms.resource.act;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.linkage.module.itms.resource.bio.QueryVlanConfigBIO;

public class QueryVlanConfigACT extends splitPageAction implements SessionAware,
		ServletRequestAware
{

	private static final long serialVersionUID = -7787526862261263533L;
	private static Logger logger = LoggerFactory
			.getLogger(QueryVlanConfigACT.class);
	private QueryVlanConfigBIO bio;
	private HttpServletRequest request;
	@SuppressWarnings({ "rawtypes" })
	private Map session;
	@SuppressWarnings("rawtypes")
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	@SuppressWarnings("rawtypes")
	private List<Map> vlanConfigList = null;
	// 属地ID
	private String cityId = "";
	// 逻辑ID
	private String username = "";
	// 设备序列号
	private String deviceSerialnumber = "";
	// 设备序列号
	private String isErrPort = "";
	// 设备序列号
	private String selectType = "";
	// 属地列表
	private List<Map<String, String>> cityList;
	private String ajax = "";
	// 采集类型
	private List<Map<String, Object>> list = null;
	/** 查询总数 */
	private int queryCount;

	/**
	 * 家庭网关Vlan配置查询页面初始化
	 * 
	 * @author 岩
	 * @date 2016-11-15
	 * @return
	 */
	public String init()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		List<Map<String, String>> city_list = CityDAO.getNextCityListByCityPid(curUser
				.getCityId());
		cityList = new ArrayList<Map<String, String>>();
		for (Map<String, String> map : city_list)
		{
			Map<String, String> m = new HashMap<String, String>();
			String city_id = (String) map.get("city_id");
			String city_name = (String) map.get("city_name");
			if ("00".equals(city_id))
			{
				city_name = "全区";
			}
			m.put("city_id", city_id);
			m.put("city_name", city_name);
			cityList.add(m);
			m = null;
			map = null;
		}
		return "init";
	}

	/**
	 * 获取vlanConfigList
	 * @author 岩 
	 * @date 2016-11-15
	 * @return
	 */
	public String queryVlanConfigList()
	{
		logger.warn("QueryVlanConfigACT——》queryVlanConfigList");
		vlanConfigList = bio.queryVlanConfigList(selectType, username, deviceSerialnumber, cityId, isErrPort, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countVlanConfigList(selectType, username, deviceSerialnumber, cityId, isErrPort, curPage_splitPage, num_splitPage);
		queryCount = bio.getQueryCount();
		return "list";
	}

	/**
	 * 
	 * @return
	 */
	public String exportVlanConfigList() throws Exception {
		fileName = "家庭网关配置查询列表";
		title = new String[] { "序号","地市","区县", "逻辑ID","宽带账号", "厂家", "设备序列号","LAN 1","LAN 2","LAN 3", "LAN 4", "是否有异常端口",
				"采集时间"};
		column = new String[] { "index","parentName","cityName", "username","netAccount", "vendorName",
				"deviceSn","lan1","lan2","lan3", "lan4", "isErrPort", "gatherTime"};
		data = bio.exportVlanConfigList(selectType, username, deviceSerialnumber, cityId, isErrPort);
		return "excel";
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getData()
	{
		return data;
	}

	@SuppressWarnings("rawtypes")
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public String[] getColumn()
	{
		return column;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
	}

	public String[] getTitle()
	{
		return title;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public QueryVlanConfigBIO getBio()
	{
		return bio;
	}

	public void setBio(QueryVlanConfigBIO bio)
	{
		this.bio = bio;
	}

	@SuppressWarnings("rawtypes")
	public Map getSession()
	{
		return session;
	}

	@SuppressWarnings("rawtypes")
	public void setSession(Map session)
	{
		this.session = session;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getVlanConfigList()
	{
		return vlanConfigList;
	}

	@SuppressWarnings("rawtypes")
	public void setVlanConfigList(List<Map> vlanConfigList)
	{
		this.vlanConfigList = vlanConfigList;
	}


	public String getDeviceSerialnumber()
	{
		return deviceSerialnumber;
	}

	public void setDeviceSerialnumber(String deviceSerialnumber)
	{
		this.deviceSerialnumber = deviceSerialnumber;
	}

	public String getIsErrPort()
	{
		return isErrPort;
	}

	public void setIsErrPort(String isErrPort)
	{
		this.isErrPort = isErrPort;
	}

	public List<Map<String, Object>> getList()
	{
		return list;
	}

	public void setList(List<Map<String, Object>> list)
	{
		this.list = list;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}
}
