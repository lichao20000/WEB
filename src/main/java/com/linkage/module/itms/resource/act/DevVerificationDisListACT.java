
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
import com.linkage.module.itms.resource.bio.DevVerificationDisListBIO;

/**
 * 
 * @author 岩 (Ailk No.)
 * @version 1.0
 * @since 2016-4-18
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class DevVerificationDisListACT extends splitPageAction implements SessionAware
{

	/** 序列化 */
	private static final long serialVersionUID = 1L;
	/** 日志 */
	private static Logger logger = LoggerFactory
			.getLogger(DevVerificationDisListACT.class);

	/** 属地列表集合 */
	private List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();
	/** 开始时间 */
	private String starttime = "";
	/** 结束时间 */
	private String endtime = "";
	/** 核销时间 */
	private String useTime = "";	
	/** 核销方式 */
	private String useType = "";
	/** 属地 */
	private String cityId = "";
	/** 核销是否一致 */
	private String isMatch = "";
	/** cityMap */
	private static Map<String, String> cityMap = new HashMap<String, String>();
	/** 核销设备列表 */
	@SuppressWarnings("rawtypes")
	private List<Map> DevVerificationList = null;
	/** 查询总数 */
	private int queryCount;
	/** bio */
	private DevVerificationDisListBIO bio;
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
		cityMap = DevVerificationDisListBIO.getSencondCityIdCityNameMap();
	}
	/** session */
	@SuppressWarnings("rawtypes")
	private Map session;

	/**
	 * 
	 * @author 岩 
	 * @date 2016-4-21
	 * @return
	 */
	public String init()
	{
		logger.debug("DevVerificationDisListACT ==> init");
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
	 * 查询列表
	 * @author 岩 
	 * @since 2016-4-21
	 * @return
	 */
	public String queryDevVerification()
	{
		logger.debug("DevVerificationDisListACT ==> queryDevVerification()");
		starttime = setTime(starttime);
		endtime = setTime(endtime);

		DevVerificationList = bio.queryDevVerification(cityId, starttime,
				endtime, isMatch, useType,  curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countQueryDevVerification(cityId, starttime, endtime,
				isMatch, useType, curPage_splitPage, num_splitPage);
		queryCount = bio.getQueryCount();
		return "list";
	}
	
	
	/**
	 * 导出Excel
	 * @author 岩 
	 * @date 2016-4-21
	 * @return
	 */
	public String parseExcel()
	{
		logger.debug("parseExcel");
		starttime = setTime(starttime);
		endtime = setTime(endtime);

		fileName = "终端核销不一致清单统计";
		title = new String[20];
		column = new String[20];
		title[0] = "属地";
		title[1] = "用户LOID";
		title[2] = "工单号";
		title[3] = "ITMS设备";
		title[4] = "ITMS设备规格";
		title[5] = "核销设备";
		title[6] = "核销设备规格";
		title[7] = "核销方式";
		title[8] = "核销是否一致";
		title[9] = "核销日期";
		title[10] = "本地网";
		title[11] = "区域";
		title[12] = "工程性质";
		title[13] = "用户地址";
		title[14] = "班组";
		title[15] = "装机人员";
		title[16] = "ITMS设备序列号（材料核销的第8天采集）";
		title[17] = "ITMS设备规格（材料核销的第8天采集）";
		title[18] = "ITMS设备序列号（材料核销的第31天采集）";
		title[19] = "ITMS设备规格（材料核销的第31天采集）";

		column[0] = "city_id";
		column[1] = "username";
		column[2] = "bill_no";
		column[3] = "itms_serial";
		column[4] = "spec_name_itms";
		column[5] = "term_serial";
		column[6] = "spec_name_term";
		column[7] = "use_type";
		column[8] = "is_match";
		column[9] = "use_time";
		column[10] = "orgname";
		column[11] = "area";
		column[12] = "project_nature";
		column[13] = "inst_addr";
		column[14] = "group_name";
		column[15] = "install_person";
		column[16] = "device_sn_8";
		column[17] = "device_spec_8";
		column[18] = "device_sn_31";
		column[19] = "device_spec_31";

		data = bio.parseExcel(cityId, starttime, endtime, isMatch, useType);
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

	
	public int getQueryCount()
	{
		return queryCount;
	}

	
	public void setQueryCount(int queryCount)
	{
		this.queryCount = queryCount;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDevVerificationList()
	{
		return DevVerificationList;
	}

	
	@SuppressWarnings("rawtypes")
	public void setDevVerificationList(List<Map> devVerificationList)
	{
		DevVerificationList = devVerificationList;
	}

	public String getUseTime()
	{
		return useTime;
	}

	
	public void setUseTime(String useTime)
	{
		this.useTime = useTime;
	}

	
	public String getUseType()
	{
		return useType;
	}

	
	public void setUseType(String useType)
	{
		this.useType = useType;
	}

	
	public String getIsMatch()
	{
		return isMatch;
	}

	
	public void setIsMatch(String isMatch)
	{
		this.isMatch = isMatch;
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
		DevVerificationDisListACT.cityMap = cityMap;
	}

	
	
	public DevVerificationDisListBIO getBio()
	{
		return bio;
	}

	
	public void setBio(DevVerificationDisListBIO bio)
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
