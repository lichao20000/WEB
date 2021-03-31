
package com.linkage.module.gtms.stb.resource.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gtms.stb.resource.serv.StbCategoryCountBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 湖南联通机顶盒类别统计
 */
@SuppressWarnings("rawtypes")
public class StbCategoryCountAction extends splitPageAction implements SessionAware
{

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(StbCategoryCountAction.class);
	private Map session;
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 属地列表（有序） */
	private List<HashMap<String, String>> cityListOrder = null;
	/** 属地ID */
	private String city_id = "-1";
	/** 属地in或=标志，0为=，1为in */
	private String cityType = "0";
	/** 厂商列表 */
	private ArrayList<HashMap<String, String>> vendorList = null;
	/** 厂商列表 （有序） */
	private List<Map<String, String>> vendorListOrder = null;
	/** 厂商ID */
	private String vendor_id = "-1";
	/** 统计类型，queryType：vendor，city */
	private String queryType = "";
	private String version = "";
	/** 存储统计后的数据文件，供导出时用，无需再次统计 */
	private String queryTime;
	// 导出数据
	private List<Map<String, String>> data;
	private int queryCount;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private StbCategoryCountBIO bio;
	private List<Map> devList = null;
	private String ajax;

	/**
	 * 初始化页面
	 */
	public String init()
	{
		if ("city".equals(queryType))
		{
			cityList = CityDAO.getNextCityListByCityPid("00");
		}
		else if("vendor".equals(queryType))
		{
			vendorList = bio.getVendorList("00");
		}
		return "init";
	}

	/**
	 * 按属地、版本统计
	 */
	public String execute()
	{
		if("city".equals(queryType))
		{
			logger.warn("设备类别按属地统计,city_id[{}],",city_id);
			if (StringUtil.IsEmpty(city_id) || "-1".equals(city_id))
			{
				city_id = "00";
			}
			cityList = CityDAO.getNextCityListByCityPid(city_id);
			data = bio.countResultByCity(city_id, cityList);
			cityListOrder = bio.getNextCityListOrderByCityPid(city_id);
			ajax = bio.toConversion("category", data, city_id, cityListOrder);
		}
		else if ("vendor".equals(queryType))
		{
			logger.warn("设备类别按厂商统计,vendor_id[{}],",vendor_id);
			if (StringUtil.IsEmpty(vendor_id) || "-1".equals(vendor_id))
			{
				vendor_id = "00";
			}
			vendorList = bio.getVendorList(vendor_id);
			data = bio.countResultByVendor(vendor_id, vendorList);
			ajax = bio.toConversion4Vendor("category", data, vendor_id, vendorList);
		}
		
		return "ajax";
	}

	/**
	 * 按属地、版本展示设备详细
	 */
	public String getStbDeviceList()
	{
		logger.warn("getStbDeviceList({},{},{},{})", new Object[] { city_id, version,
				curPage_splitPage, num_splitPage });
		if (StringUtil.IsEmpty(city_id) || "-1".equals(city_id))
		{
			city_id = "00";
		}
		data = bio.countResultByCity(queryType, city_id, cityType, version,
				curPage_splitPage, num_splitPage);
		queryCount = bio.getCountResultByCity(queryType, city_id, cityType,
				version);
		if (queryCount % num_splitPage == 0)
		{
			maxPage_splitPage = queryCount / num_splitPage;
		}
		else
		{
			maxPage_splitPage = queryCount / num_splitPage + 1;
		}
		return "stb_dev_list";
	}

	/**
	 * 导出统计数据
	 */
	public String getExcel()
	{
		logger.warn("getExcel({},{},{})", city_id, queryType, queryTime);
		if (StringUtil.IsEmpty(city_id) || "-1".equals(city_id))
		{
			city_id = "00";
		}
		if ("city".equals(queryType))
		{
			cityList = CityDAO.getNextCityListByCityPid(city_id);
			data = bio.countResultByCity(city_id, cityList);
			fileName = "机顶盒类别按属地统计";
			title = new String[cityList.size() + 2];
			title[0] = "类别";
			for (int i = 0; i < cityList.size(); i++)
			{
				title[i + 1] = StringUtil.getStringValue(cityList.get(i), "city_name");
			}
			title[cityList.size() + 1] = "小计";
			column = new String[cityList.size() + 2];
			column[0] = "category";
			for (int i = 0; i < cityList.size(); i++)
			{
				column[i + 1] = StringUtil.getStringValue(cityList.get(i), "city_id");
			}
			column[cityList.size() + 1] = "total_num";
		}
		else if ("vendor".equals(queryType))
		{
			vendor_id = city_id;
			fileName = "机顶盒类别按厂商统计";
			vendorList = bio.getVendorList(vendor_id);
			data = bio.countResultByVendor(vendor_id, vendorList);
			title = new String[vendorList.size() + 2];
			title[0] = "类别";
			for (int i = 0; i < vendorList.size(); i++)
			{
				title[i + 1] = StringUtil.getStringValue(vendorList.get(i), "vendor_add");
			}
			title[vendorList.size() + 1] = "小计";
			column = new String[vendorList.size() + 2];
			column[0] = "category";
			for (int i = 0; i < vendorList.size(); i++)
			{
				column[i + 1] = StringUtil.getStringValue(vendorList.get(i), "vendor_id");
			}
			column[vendorList.size() + 1] = "total_num";
		}
		return "excel";
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public List<Map<String, String>> getData()
	{
		return data;
	}

	public void setData(List<Map<String, String>> data)
	{
		this.data = data;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public int getQueryCount()
	{
		return queryCount;
	}

	public void setQueryCount(int queryCount)
	{
		this.queryCount = queryCount;
	}

	public StbCategoryCountBIO getBio()
	{
		return bio;
	}

	public void setBio(StbCategoryCountBIO bio)
	{
		this.bio = bio;
	}

	public String[] getTitle()
	{
		return title;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}

	public String[] getColumn()
	{
		return column;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public List<Map> getDevList()
	{
		return devList;
	}

	public void setDevList(List<Map> devList)
	{
		this.devList = devList;
	}

	public String getCity_id()
	{
		return city_id;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getQueryType()
	{
		return queryType;
	}

	public void setQueryType(String queryType)
	{
		this.queryType = queryType;
	}

	public String getCityType()
	{
		return cityType;
	}

	public void setCityType(String cityType)
	{
		this.cityType = cityType;
	}

	public String getQueryTime()
	{
		return queryTime;
	}

	public void setQueryTime(String queryTime)
	{
		this.queryTime = queryTime;
	}

	public List<HashMap<String, String>> getCityListOrder()
	{
		return cityListOrder;
	}

	public void setCityListOrder(List<HashMap<String, String>> cityListOrder)
	{
		this.cityListOrder = cityListOrder;
	}

	public List<Map<String, String>> getVendorListOrder()
	{
		return vendorListOrder;
	}

	public void setVendorListOrder(List<Map<String, String>> vendorListOrder)
	{
		this.vendorListOrder = vendorListOrder;
	}

	public ArrayList<HashMap<String, String>> getVendorList()
	{
		return vendorList;
	}

	public void setVendorList(ArrayList<HashMap<String, String>> vendorList)
	{
		this.vendorList = vendorList;
	}

	public String getVendor_id()
	{
		return vendor_id;
	}

	public void setVendor_id(String vendor_id)
	{
		this.vendor_id = vendor_id;
	}
}
