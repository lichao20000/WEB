
package com.linkage.module.itms.resource.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.RecycleDevRateBIO;

/**
 * 
 * @author 岩 (Ailk No.)
 * @version 1.0
 * @since 2016-4-18
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class RecycleDevRateACT extends splitPageAction implements SessionAware
{

	/** 序列化 */
	private static final long serialVersionUID = 1L;
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(RecycleDevRateACT.class);

	/** 属地列表集合 */
	private List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();
	/** 开始时间 */
	private String starttime = "";
	/** 结束时间 */
	private String endtime = "";
	/** 属地 */
	private String cityId = "";
	/** cityMap */
	private static Map<String, String> cityMap = new HashMap<String, String>();

	/** bio */
	private RecycleDevRateBIO bio;
	/** 节点路径 */
	private String ajax = "";
	/** 导出文件列标题 */
	private String[] title = null;
	/** 导出文件列 */
	private String[] column = null;
	/** 导出文件名 */
	private String fileName = null;
	/** 导出数据 */
	@SuppressWarnings("rawtypes")
	private List<Map> data = null;
	static
	{
		cityMap = RecycleDevRateBIO.getSencondCityIdCityNameMap();
	}
	/** session */
	@SuppressWarnings("rawtypes")
	private Map session;

	/**
	 * 回收终端页面初始化
	 * @author 岩 
	 * @date 2016-4-26
	 * @return
	 */
	public String init()
	{
		logger.debug("RecycleDevRateACT ==> init");
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		if ("00".equals(cityId))
		{
			cityList = CityDAO.getNextCityListByCityPid(cityId);
		}
		else
		{
			// 获取用户所在属地对应的二级属地 (南京 宿迁之类的)
			Map<String, String> map = new HashMap<String, String>();
			for (Map.Entry<String, String> entry : cityMap.entrySet())
			{
				if (CityDAO.getCityName(cityId).contains(entry.getValue()))
				{
					map.put("city_id", entry.getKey());
					map.put("city_name", entry.getValue());
					cityList.add(map);
					break;
				}
			}
		}
		return "init";
	}

	/**
	 * 回收终端使用率率统计柱状图和饼图
	 */
	public String recycleDevRate()
	{
		logger.debug("RecycleDevRateACT ==> recycleDevRate");
		// 时间转化
		starttime = setTime(starttime);
		endtime = setTime(endtime);
		ajax = bio.recycleDevRate(cityId, starttime, endtime);
		return "ajax";
	}
	
	/**
	 * 导出Excel
	 * @author 岩 
	 * @date 2016-4-26
	 * @return
	 */
	public String parseExcel()
	{
		logger.debug("parseExcel");
		starttime = setTime(starttime);
		endtime = setTime(endtime);
		fileName = "回收终端使用率";
		title = new String[4];
		column = new String[4];
		
		title[0] = "属地";
		title[1] = "回收终端使用数";
		title[2] = "回收终端总数";
		title[3] = "回收使用率";

		column[0] = "city";
		column[1] = "usecount";
		column[2] = "totalcount";
		column[3] = "userate";

		data = bio.parseExcel(cityId, starttime, endtime);
		return "excel";
	}

	/**
	 * 导出清单
	 * @author 岩 
	 * @date 2016-4-26
	 * @return
	 */
	public String parseDetail()
	{
		logger.debug("parseExcel");
		// 时间转化
		starttime = setTime(starttime);
		endtime = setTime(endtime);
		fileName = "回收终端使用率清单";
		title = new String[15];
		column = new String[15];
		
		title[0] = "属地";
		title[1] = "用户LOID";
		title[2] = "本地网";
		title[3] = "区域";
		title[4] = "回收设备";
		title[5] = "回收设备规格";
		title[6] = "业务账号";
		title[7] = "宽带账号";
		title[8] = "工单号";
		title[9] = "用户地址";
		title[10] = "装维人员";
		title[11] = "班组";
		title[12] = "回收时间";
		title[13] = "是否使用";

		column[0] = "city";
		column[1] = "username";
		column[2] = "orgname";
		column[3] = "area";
		column[4] = "device_serialnumber_A";
		column[5] = "spec_name_A";
		column[6] = "serv_no";
		column[7] = "net_account";
		column[8] = "bill_no";
		column[9] = "inst_addr";
		column[10] = "install_person";
		column[11] = "group_name";
		column[12] = "revert_time";
		column[13] = "is_used";
		
		data = bio.parseDetail(cityId, starttime, endtime );
		return "excel";
	}
	
	
	/**
	 * 时间转化
	 */
	private String setTime(String time)
	{
		logger.debug("setTime()" + time);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (!StringUtil.IsEmpty(time))
		{
			dt = new DateTimeUtil(time);
			time = StringUtil.getStringValue(dt.getLongTime());
			return time;
		}
		else
		{
			return "";
		}
	}
	
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}
	
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public String getStarttime()
	{
		return starttime;
	}
	
	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	public String getEndtime()
	{
		return endtime;
	}
	
	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}
	
	public String getCityId()
	{
		return cityId;
	}
	
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}
	
	public static Map<String, String> getCityMap()
	{
		return cityMap;
	}
	
	public static void setCityMap(Map<String, String> cityMap)
	{
		RecycleDevRateACT.cityMap = cityMap;
	}

	public RecycleDevRateBIO getBio()
	{
		return bio;
	}
	
	public void setBio(RecycleDevRateBIO bio)
	{
		this.bio = bio;
	}

	public String getAjax()
	{
		return ajax;
	}
	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
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
	
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	public static Logger getLogger()
	{
		return logger;
	}

}
