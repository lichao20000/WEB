
package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.ITVCountBIO;

/**
 * iTV业务统计
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class ITVCountACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ITVCountACT.class);
	// session
	private Map session;
	/** 开始时间 */
	private String starttime = "";
	/** 开始时间 */
	private String starttime1 = "";
	/** 结束时间 */
	private String endtime = "";
	/** 结束时间 */
	private String endtime1 = "";
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 属地ID */
	private String cityId = "-1";
	/** 上行方式列表 */
	private List<Map<String, String>> wanAccessTypeList = null;
	/** 上行方式ID */
	private String wanAccessTypeId = "-1";
	/** IPTV类型 1纯IPTV，0非纯IPTV */
	private String forbidNet = "-1";
	/** 终端类型列表 */
	private List<Map<String, String>> deviceTypeList = null;
	/** 上行方式ID */
	private String typeId = "-1";
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private ITVCountBIO bio;
	private List<Map> hgwList = null;

	/**
	 * iTV业务统计初始化页面
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(cityId);
		wanAccessTypeList = bio.getWANAccessTypeList();
		deviceTypeList = bio.getDeviceTypeList();
		DateTimeUtil dt = new DateTimeUtil();
		starttime = dt.getDate();
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil(start_time * 1000);
		starttime = dt.getLongDate();
		dt = new DateTimeUtil((start_time+24*3600-1) * 1000);
		endtime = dt.getLongDate();
		return "init";
	}

	/**
	 * iTV业务统计
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return String
	 */
	public String iTVCount()
	{
		logger.debug("iTVCount()");
		this.setTime();
		data = bio.iTVCount(starttime1, endtime1, cityId, wanAccessTypeId, forbidNet,
				typeId);
		return "list";
	}

	/**
	 * iTV业务统计导出Excel
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return String
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
		fileName = "IPTV";
		title = new String[] { "属地", "用户数" };
		column = new String[] { "city_name", "total" };
		data = bio.iTVCount(starttime1, endtime1, cityId, wanAccessTypeId, forbidNet,
				typeId);
		return "excel";
	}

	/**
	 * iTV业务统计用户列表
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return String
	 */
	public String getHgw()
	{
		logger.debug("getHgw()");
		hgwList = bio.getHgwList(starttime1, endtime1, cityId, wanAccessTypeId,
				forbidNet, typeId, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getHgwCount(starttime1, endtime1, cityId,
				wanAccessTypeId, forbidNet, typeId, curPage_splitPage, num_splitPage);
		return "hgwlist";
	}

	/**
	 * iTV业务统计用户列表Excel
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return String
	 */
	public String getHgwExcel()
	{
		logger.debug("getHgwExcel()");
		fileName = "IPTV用户";
		title = new String[] { "属地", "宽带账号", "终端类型", "套餐类型", "接入方式", "是否非纯" };
		column = new String[] { "city_name", "username", "type_name",
				"serv_package_name", "prod_spec_name", "forbid_net" };
		data = bio.getHgwExcel(starttime1, endtime1, cityId, wanAccessTypeId, forbidNet,
				typeId);
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			starttime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endtime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
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
	 * @return the starttime
	 */
	public String getStarttime()
	{
		return starttime;
	}

	/**
	 * @param starttime
	 *            the starttime to set
	 */
	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	/**
	 * @return the starttime1
	 */
	public String getStarttime1()
	{
		return starttime1;
	}

	/**
	 * @param starttime1
	 *            the starttime1 to set
	 */
	public void setStarttime1(String starttime1)
	{
		this.starttime1 = starttime1;
	}

	/**
	 * @return the cityList
	 */
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	/**
	 * @param cityList
	 *            the cityList to set
	 */
	public void setCityList(List<Map<String, String>> cityList)
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
	 * @return the data
	 */
	public List<Map> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn()
	{
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the hgwList
	 */
	public List<Map> getHgwList()
	{
		return hgwList;
	}

	/**
	 * @param hgwList
	 *            the hgwList to set
	 */
	public void setHgwList(List<Map> hgwList)
	{
		this.hgwList = hgwList;
	}

	/**
	 * @return the endtime
	 */
	public String getEndtime()
	{
		return endtime;
	}

	/**
	 * @param endtime
	 *            the endtime to set
	 */
	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	/**
	 * @return the endtime1
	 */
	public String getEndtime1()
	{
		return endtime1;
	}

	/**
	 * @param endtime1
	 *            the endtime1 to set
	 */
	public void setEndtime1(String endtime1)
	{
		this.endtime1 = endtime1;
	}

	/**
	 * @return the wanAccessTypeList
	 */
	public List<Map<String, String>> getWanAccessTypeList()
	{
		return wanAccessTypeList;
	}

	/**
	 * @param wanAccessTypeList
	 *            the wanAccessTypeList to set
	 */
	public void setWanAccessTypeList(List<Map<String, String>> wanAccessTypeList)
	{
		this.wanAccessTypeList = wanAccessTypeList;
	}

	/**
	 * @return the wanAccessTypeId
	 */
	public String getWanAccessTypeId()
	{
		return wanAccessTypeId;
	}

	/**
	 * @param wanAccessTypeId
	 *            the wanAccessTypeId to set
	 */
	public void setWanAccessTypeId(String wanAccessTypeId)
	{
		this.wanAccessTypeId = wanAccessTypeId;
	}

	/**
	 * @return the forbidNet
	 */
	public String getForbidNet()
	{
		return forbidNet;
	}

	/**
	 * @param forbidNet
	 *            the forbidNet to set
	 */
	public void setForbidNet(String forbidNet)
	{
		this.forbidNet = forbidNet;
	}

	/**
	 * @return the deviceTypeList
	 */
	public List<Map<String, String>> getDeviceTypeList()
	{
		return deviceTypeList;
	}

	/**
	 * @param deviceTypeList
	 *            the deviceTypeList to set
	 */
	public void setDeviceTypeList(List<Map<String, String>> deviceTypeList)
	{
		this.deviceTypeList = deviceTypeList;
	}

	/**
	 * @return the typeId
	 */
	public String getTypeId()
	{
		return typeId;
	}

	/**
	 * @param typeId
	 *            the typeId to set
	 */
	public void setTypeId(String typeId)
	{
		this.typeId = typeId;
	}

	/**
	 * @return the bio
	 */
	public ITVCountBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(ITVCountBIO bio)
	{
		this.bio = bio;
	}
}
