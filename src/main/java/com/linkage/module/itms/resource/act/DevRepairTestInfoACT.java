
package com.linkage.module.itms.resource.act;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.DevRepairTestInfoBIO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

/**
 * 终端维修检测情况统计
 * 
 * @author yinlei3 (73167)
 * @version 1.0
 * @since 2015年10月21日
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DevRepairTestInfoACT extends splitPageAction implements SessionAware
{

	/** 序列化 */
	private static final long serialVersionUID = 1L;
	/** 日志 */
	private static Logger logger = LoggerFactory
			.getLogger(DevRepairTestInfoACT.class);
	/** 维修厂家 */
	private String repair_vendor = "";
	/** 维修厂家列表 */
	private List<Map<String, String>> repair_vendor_list = new ArrayList<Map<String, String>>();
	/** 终端厂家 */
	private String device_vendor = "";
	/** 终端型号 */
	private String device_model = "";
	/** 归属城市 */
	private String attribution_city = "";
	/** 归属城市列表 */
	private List<Map<String, String>> attribution_city_list = new ArrayList<Map<String, String>>();
	/** 发往城市 */
	private String send_city = "";
	/** 发往城市列表 */
	private List<Map<String, String>> send_city_list;
	/** 出厂时间 (起始) */
	private String manufacture_date_start = "";
	/** 出厂时间 (结束) */
	private String manufacture_date_end = "";
	/** 设备序列号 */
	private String device_serialnumber = "";
	/** session */
	@SuppressWarnings("rawtypes")
	private Map session;
	/** bio */
	private DevRepairTestInfoBIO bio;
	// 返修厂商集合
	private List<Map<String, String>> vendorList;
	// 厂商
	private long repair_vendor_id;
	// 返修开始时间
	private String starttime = "";
	// 返修结束时间
	private String endtime = "";
	// 属地
	private String cityId = "00";
	private long userId;
	// 批次
	private String batchNum;
	// 批次集合
	private List<Map<String, String>> batchNumList;
	/** 终端维修检测情况列表 */
	@SuppressWarnings("rawtypes")
	private List<Map> DevRepairTestInfoList = null;
	/** 详情界面用Map */
	@SuppressWarnings("rawtypes")
	private Map devDetailMap = null;
	/** 导出文件列标题 */
	private String[] title = null;
	/** 导出文件列 */
	private String[] column = null;
	/** 导出文件名 */
	private String fileName = null;
	/** 导出数据 */
	@SuppressWarnings("rawtypes")
	private List<Map> data = null;
	/** 节点路径 */
	private String ajax = "";
	/** cityMap */
	private static Map<String, String> cityMap = new HashMap<String, String>();
	/** 维修厂家map */
	private Map<String, String> repairDevMap = new HashMap<String, String>();
	/** 终端厂家map */
	private Map<String, String> DevVendorMap = new HashMap<String, String>();
	/** 查询总数 */
	private int queryCount;
	@SuppressWarnings("rawtypes")
	private List dataList = null;
	static
	{
		cityMap = DevRepairTestInfoBIO.getSencondCityIdCityNameMap();
		DeviceTypeUtil.init();
	}
	// 查询方式 1:厂商 2.属地
	private String searchType;
	// 属地列表集合
	private List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();
	// request取登陆帐号使用
	private HttpServletRequest request;
	/**
	 * 查询结果的标题
	 */
	private List<String> titleList = null;

	/**
	 * 初始化页面
	 * 
	 * @author yinlei3
	 * @date 2015年10月21日
	 * @param
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		if ("00".equals(cityId))
		{
			attribution_city_list = CityDAO.getNextCityListByCityPid(cityId);
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
					attribution_city_list.add(map);
					break;
				}
			}
		}
		repair_vendor_list = bio.getRepairVendor();
		return "init";
	}

	/*
	 * 返修终端使用率统计页面初始化
	 */
	public String useRateInit()
	{
		return "useRateInit";
	}

	/*
	 * 根据用户选择厂商和属地展示统计页面
	 */
	public String useRateType()
	{
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		vendorList = bio.getVendor();
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
		batchNumList = bio.getBatchNum();
		return "useRateType";
	}

	/*
	 * 使用率返回上一级页面
	 */
	public String useRateReload()
	{
		return "useRateReload";
	}

	/*
	 * 合格率返回上一级页面
	 */
	public String qualifiedReload()
	{
		return "qualifiedReload";
	}

	/*
	 * 返修终端使用率统计柱状图
	 */
	public String getUseRateData() throws Exception
	{
		if ("1".equals(searchType))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
			userId = curUser.getUser().getId();
			ajax = bio.getUseRateDataByVendor(repair_vendor_id, starttime, endtime,
					batchNum, cityId);
		}
		else
		{
			UserRes curUser = (UserRes) session.get("curUser");
			String userCityId = curUser.getCityId();
			if ("00".equals(userCityId))
			{
				if ("00".equals(cityId))
				{
					cityId = "-1";
				}
				ajax = bio.getUseRateDataByCity(cityId, starttime, endtime, batchNum);
			}
			else
			{
				ajax = bio.getUseRateDataByCity(userCityId, starttime, endtime, batchNum);
			}
		}
		return "ajax";
	}

	/*
	 * 返修终端使用率统计饼图
	 */
	public String getUseRatePieData() throws Exception
	{
		if ("1".equals(searchType))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
			ajax = bio.getUseRatePieCountByVendor(repair_vendor_id, starttime, endtime,
					batchNum, cityId);
		}
		else
		{
			UserRes curUser = (UserRes) session.get("curUser");
			String userCityId = curUser.getCityId();
			if ("00".equals(userCityId))
			{
				if ("00".equals(cityId))
				{
					cityId = "-1";
				}
				ajax = bio.getUseRatePieCountByCity(cityId, starttime, endtime, batchNum);
			}
			else if (!"00".equals(userCityId))
			{
				ajax = bio.getUseRatePieCountByCity(userCityId, starttime, endtime,
						batchNum);
			}
		}
		return "ajax";
	}

	/*
	 * 返修终端使用率统计页面初始化
	 */
	public String qualifiedRateInit()
	{
		return "qualifiedRateInit";
	}

	/*
	 * 根据用户选择厂商和属地展示统计页面
	 */
	public String qualifiedRateType()
	{
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		vendorList = bio.getVendor();
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
		batchNumList = bio.getBatchNum();
		return "qualifiedRateType";
	}

	/*
	 * 返修终端合格率统计柱状图
	 */
	public String getQualifiedRate() throws Exception
	{
		if ("1".equals(searchType))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
			userId = curUser.getUser().getId();
			ajax = bio.getQualifiedRateByVendor(repair_vendor_id, starttime, endtime,
					batchNum, cityId);
		}
		else
		{
			UserRes curUser = (UserRes) session.get("curUser");
			String userCityId = curUser.getCityId();
			if ("00".equals(userCityId))
			{
				if ("00".equals(cityId))
				{
					cityId = "-1";
				}
				ajax = bio.getQualifiedRateByCity(cityId, starttime, endtime, batchNum);
			}
			else
			{
				ajax = bio.getQualifiedRateByCity(userCityId, starttime, endtime,
						batchNum);
			}
		}
		return "ajax";
	}

	/*
	 * 返修终端合格率统计饼图
	 */
	public String getQualifiedRatePieData() throws Exception
	{
		if ("1".equals(searchType))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
			ajax = bio.getQualifiedRatePieDataByVendor(repair_vendor_id, starttime,
					endtime, batchNum, cityId);
		}
		else
		{
			UserRes curUser = (UserRes) session.get("curUser");
			String userCityId = curUser.getCityId();
			if ("00".equals(userCityId))
			{
				if ("00".equals(cityId))
				{
					cityId = "-1";
				}
				ajax = bio.getQualifiedRatePieDataByCity(cityId, starttime, endtime,
						batchNum);
			}
			else if (!"00".equals(userCityId))
			{
				ajax = bio.getQualifiedRatePieDataByCity(userCityId, starttime, endtime,
						batchNum);
			}
		}
		return "ajax";
	}

	/*
	 * 导出使用率统计报表
	 */
	public String useRateToExcel()
	{
		List<Map<String, String>> cityTempList = new ArrayList<Map<String, String>>();
		if (0 == repair_vendor_id)
		{
			repair_vendor_id = -1;
		}
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		if ("1".equals(searchType))
		{
			cityTempList = bio.getCityList(userCityId);
		}
		else
		{
			if ("00".equals(userCityId))
			{
				if ("-1".equals(cityId))
				{
					cityTempList = bio.getCityList("00");
				}
				else
				{
					cityTempList = bio.getCityList(cityId);
				}
			}
			else
			{
				cityTempList = bio.getCityList(userCityId);
			}
		}
		titleList = new ArrayList<String>();
		titleList.add("厂商");
		titleList.add("总计");
		for (int i = 0; i < cityTempList.size(); i++)
		{
			String city_id = StringUtil.getStringValue(cityTempList.get(i), "city_id");
			if (!("00".equals(city_id)))
			{
				titleList
						.add(StringUtil.getStringValue(cityTempList.get(i), "city_name"));
			}
		}
		if ("00".equals(userCityId))
		{
			if ("-1".equals(cityId) || "00".equals(cityId))
			{
				titleList.add("全省");
			}
			else
			{
				titleList.add("全" + Global.G_CityId_CityName_Map.get(cityId));
			}
		}
		else
		{
			titleList.add("全" + Global.G_CityId_CityName_Map.get(userCityId));
		}
		cityId = "-1";
		dataList = bio.getUseRateTotalList(cityTempList, repair_vendor_id, cityId,
				starttime, endtime, batchNum);
		cityTempList = null;
		return "excelByCount";
	}

	/*
	 * 导出合格率统计报表
	 */
	public String qualifiedRateToExcel()
	{
		List<Map<String, String>> cityTempList = new ArrayList<Map<String, String>>();
		if (0 == repair_vendor_id)
		{
			repair_vendor_id = -1;
		}
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		if ("1".equals(searchType))
		{
			cityTempList = bio.getCityList(userCityId);
		}
		else
		{
			if ("00".equals(userCityId))
			{
				if ("-1".equals(cityId))
				{
					cityTempList = bio.getCityList("00");
				}
				else
				{
					cityTempList = bio.getCityList(cityId);
				}
			}
			else
			{
				cityTempList = bio.getCityList(userCityId);
			}
		}
		titleList = new ArrayList<String>();
		titleList.add("厂商");
		titleList.add("总计");
		for (int i = 0; i < cityTempList.size(); i++)
		{
			String city_id = StringUtil.getStringValue(cityTempList.get(i), "city_id");
			if (!("00".equals(city_id)))
			{
				titleList
						.add(StringUtil.getStringValue(cityTempList.get(i), "city_name"));
			}
		}
		if ("00".equals(userCityId))
		{
			if ("-1".equals(cityId) || "00".equals(cityId))
			{
				titleList.add("全省");
			}
			else
			{
				titleList.add("全" + Global.G_CityId_CityName_Map.get(cityId));
			}
		}
		else
		{
			titleList.add("全" + Global.G_CityId_CityName_Map.get(userCityId));
		}
		cityId = "-1";
		dataList = bio.getQualifiedRateTotalList(cityTempList, repair_vendor_id, cityId,
				starttime, endtime, batchNum);
		cityTempList = null;
		return "excelByCount";
	}

	/*
	 * 导出返修厂商未使用清单
	 */
	public String noUseListToExcel() throws Exception
	{
		fileName = "unUsedList";
		title = new String[] { "属地", "维修厂商", "设备序列号", "状态" };
		column = new String[] { "city_name", "vendor_name", "device_serialnumber",
				"status" };
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		if ("1".equals(searchType))
		{
			if ("00".equals(userCityId))
			{
				data = bio.noUseListToExcelByVendor(repair_vendor_id, starttime, endtime,
						batchNum, cityId);
			}
			else
			{
				data = bio.noUseListToExcelByVendor(repair_vendor_id, starttime, endtime,
						batchNum, userCityId);
			}
		}
		else
		{
			if ("00".equals(userCityId))
			{
				data = bio.noUseListToExcelByCity(cityId, starttime, endtime, batchNum);
			}
			else
			{
				data = bio.noUseListToExcelByCity(userCityId, starttime, endtime,
						batchNum);
			}
		}
		return "excel";
	}

	/*
	 * 导出返修厂商未合格清单
	 */
	public String noQualifiedToExcel() throws Exception
	{
		fileName = "unQualifiedList";
		title = new String[] { "属地", "维修厂商", "设备序列号", "最新绑定时间", "最新解绑时间" };
		column = new String[] { "city_name", "vendor_name", "device_serialnumber",
				"recent_binddate", "recent_unbinddate" };
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		if ("1".equals(searchType))
		{
			if ("00".equals(userCityId))
			{
				data = bio.noQualifiedToExcelByVendor(repair_vendor_id, starttime,
						endtime, batchNum, cityId);
			}
			else
			{
				data = bio.noQualifiedToExcelByVendor(repair_vendor_id, starttime,
						endtime, batchNum, userCityId);
			}
		}
		else
		{
			if ("00".equals(userCityId))
			{
				data = bio.noQualifiedToExcelByCity(cityId, starttime, endtime, batchNum);
			}
			else
			{
				data = bio.noQualifiedToExcelByCity(userCityId, starttime, endtime,
						batchNum);
			}
		}
		return "excel";
	}

	/**
	 * 查询终端维修检测情况
	 * 
	 * @author yinlei3
	 * @date 2015年10月21日
	 * @param
	 * @return String
	 */
	public String queryRepairDev()
	{
		logger.debug("queryRepairDev()");
		manufacture_date_start = setTime(manufacture_date_start);
		manufacture_date_end = setTime(manufacture_date_end);
		repairDevMap = bio.getRepairDevMap();
		DevVendorMap = bio.getDevVendorMap();
		for (Map.Entry<String, String> entry : DeviceTypeUtil.deviceModelMap.entrySet())
		{
			logger.warn(entry.getKey() + "-----" + entry.getValue());
		}
		// 将维修厂家ID,设备终端id和型号id转化为其名称
		if (!StringUtil.IsEmpty(device_vendor) && !"-1".equals(device_vendor))
		{
			device_vendor = DevVendorMap.get(device_vendor);
			if (StringUtil.IsEmpty(device_vendor))
			{
				device_vendor = "unknown";
			}
		}
		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model))
		{
			device_model = DeviceTypeUtil.deviceModelMap.get(device_model);
			if (StringUtil.IsEmpty(device_model))
			{
				device_vendor = "unknown";
			}
		}
		if (!StringUtil.IsEmpty(repair_vendor) && !"-1".equals(repair_vendor))
		{
			repair_vendor = repairDevMap.get(repair_vendor);
			if (StringUtil.IsEmpty(repair_vendor))
			{
				device_vendor = "unknown";
			}
		}
		DevRepairTestInfoList = bio.queryRepairDev(device_serialnumber, repair_vendor,
				device_vendor, device_model, attribution_city, send_city,
				manufacture_date_start, manufacture_date_end, curPage_splitPage,
				num_splitPage);
		maxPage_splitPage = bio.countQueryRepairDev(device_serialnumber, repair_vendor,
				device_vendor, device_model, attribution_city, send_city,
				manufacture_date_start, manufacture_date_end, curPage_splitPage,
				num_splitPage);
		queryCount = bio.getQueryCount();
		return "list";
	}

	/**
	 * 查询终端独立档案
	 * 
	 * @author yinlei3
	 * @date 2015年10月21日
	 * @param
	 * @return String
	 */
	public String queryRepairDevDetail()
	{
		logger.debug("queryRepairDevDetail()");
		try
		{
			// 中文乱码解决
			attribution_city = java.net.URLDecoder.decode(attribution_city,"utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			logger.warn(e.getMessage());
		}
		devDetailMap = bio.queryRepairDevDetail(device_serialnumber, attribution_city);
		return "detail";
	}

	/**
	 * 获取发送城市列表
	 * 
	 * @author yinlei3
	 * @date 2015年10月21日
	 * @param
	 * @return String
	 */
	public String getSendCity()
	{
		logger.debug("queryRepairDevDetail()");
		ajax = bio.getSendCity(attribution_city);
		return "ajax";
	}

	/**
	 * 导出Excel
	 * 
	 * @author yinlei3
	 * @date 2015年10月21日
	 * @param
	 * @return String
	 */
	public String parseExcel()
	{
		logger.debug("parseExcel");
		manufacture_date_start = setTime(manufacture_date_start);
		manufacture_date_end = setTime(manufacture_date_end);
		repairDevMap = bio.getRepairDevMap();
		DevVendorMap = bio.getDevVendorMap();
		// 将维修厂家ID,设备终端id和型号id转化为其名称
		if (!StringUtil.IsEmpty(device_vendor) && !"-1".equals(device_vendor))
		{
			device_vendor = DevVendorMap.get(device_vendor);
			if (StringUtil.IsEmpty(device_vendor))
			{
				device_vendor = "unknown";
			}
		}
		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model))
		{
			device_model = DeviceTypeUtil.deviceModelMap.get(device_model);
			if (StringUtil.IsEmpty(device_model))
			{
				device_vendor = "unknown";
			}
		}
		if (!StringUtil.IsEmpty(repair_vendor) && !"-1".equals(repair_vendor))
		{
			repair_vendor = repairDevMap.get(repair_vendor);
			if (StringUtil.IsEmpty(repair_vendor))
			{
				device_vendor = "unknown";
			}
		}
		fileName = "终端维修检测情况统计";
		title = new String[15];
		column = new String[15];
		title[0] = "维修厂家";
		title[1] = "保内保外";
		title[2] = "设备序列号";
		title[3] = "终端厂家";
		title[4] = "设备型号";
		title[5] = "检测结果";
		title[6] = "归属城市";
		title[7] = "发往城市";
		title[8] = "返修出厂日期";
		title[9] = "现网使用合格情况";
		title[10] = "终端实际使用城市";
		title[11] = "最新绑定时间";
		title[12] = "最新解绑时间";
		title[13] = "绑定LOID";
		title[14] = "是否到货测试";
		column[0] = "repair_vendor";
		column[1] = "insurance_status";
		column[2] = "device_serialnumber";
		column[3] = "device_vendor";
		column[4] = "device_model";
		column[5] = "check_result";
		column[6] = "attribution_city";
		column[7] = "send_city";
		column[8] = "manufacture_date";
		column[9] = "qualified_status";
		column[10] = "city_area";
		column[11] = "recent_binddate";
		column[12] = "recent_unbinddate";
		column[13] = "bind_loid";
		column[14] = "is_test";
		data = bio.parseExcel(device_serialnumber, repair_vendor, device_vendor,
				device_model, attribution_city, send_city, manufacture_date_start,
				manufacture_date_end);
		return "excel";
	}

	/**
	 * 时间转化
	 */
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

	public String getRepair_vendor()
	{
		return repair_vendor;
	}

	public String getDevice_vendor()
	{
		return device_vendor;
	}

	public String getDevice_model()
	{
		return device_model;
	}

	public String getAttribution_city()
	{
		return attribution_city;
	}

	public String getSend_city()
	{
		return send_city;
	}

	public String getManufacture_date_start()
	{
		return manufacture_date_start;
	}

	public String getManufacture_date_end()
	{
		return manufacture_date_end;
	}

	public void setRepair_vendor(String repair_vendor)
	{
		this.repair_vendor = repair_vendor;
	}

	public void setDevice_vendor(String device_vendor)
	{
		this.device_vendor = device_vendor;
	}

	public void setDevice_model(String device_model)
	{
		this.device_model = device_model;
	}

	public void setAttribution_city(String attribution_city)
	{
		this.attribution_city = attribution_city;
	}

	public void setSend_city(String send_city)
	{
		this.send_city = send_city;
	}

	public void setManufacture_date_start(String manufacture_date_start)
	{
		this.manufacture_date_start = manufacture_date_start;
	}

	public void setManufacture_date_end(String manufacture_date_end)
	{
		this.manufacture_date_end = manufacture_date_end;
	}

	public List<Map<String, String>> getAttribution_city_list()
	{
		return attribution_city_list;
	}

	public List<Map<String, String>> getSend_city_list()
	{
		return send_city_list;
	}

	public void setAttribution_city_list(List<Map<String, String>> attribution_city_list)
	{
		this.attribution_city_list = attribution_city_list;
	}

	public void setSend_city_list(List<Map<String, String>> send_city_list)
	{
		this.send_city_list = send_city_list;
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

	public DevRepairTestInfoBIO getBio()
	{
		return bio;
	}

	public void setBio(DevRepairTestInfoBIO bio)
	{
		this.bio = bio;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDevRepairTestInfoList()
	{
		return DevRepairTestInfoList;
	}

	@SuppressWarnings("rawtypes")
	public void setDevRepairTestInfoList(List<Map> devRepairTestInfoList)
	{
		DevRepairTestInfoList = devRepairTestInfoList;
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

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public static Logger getLogger()
	{
		return logger;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public long getRepair_vendor_id()
	{
		return repair_vendor_id;
	}

	public void setRepair_vendor_id(long repair_vendor_id)
	{
		this.repair_vendor_id = repair_vendor_id;
	}

	public String getBatchNum()
	{
		return batchNum;
	}

	public void setBatchNum(String batchNum)
	{
		this.batchNum = batchNum;
	}

	public List<Map<String, String>> getVendorList()
	{
		return vendorList;
	}

	public void setVendorList(List<Map<String, String>> vendorList)
	{
		this.vendorList = vendorList;
	}

	public List<Map<String, String>> getBatchNumList()
	{
		return batchNumList;
	}

	public void setBatchNumList(List<Map<String, String>> batchNumList)
	{
		this.batchNumList = batchNumList;
	}

	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}

	@SuppressWarnings("rawtypes")
	public Map getDevDetailMap()
	{
		return devDetailMap;
	}

	@SuppressWarnings("rawtypes")
	public void setDevDetailMap(Map devDetailMap)
	{
		this.devDetailMap = devDetailMap;
	}

	public String[] getTitle()
	{
		return title;
	}

	public String[] getColumn()
	{
		return column;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
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

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public List<Map<String, String>> getRepair_vendor_list()
	{
		return repair_vendor_list;
	}

	public void setRepair_vendor_list(List<Map<String, String>> repair_vendor_list)
	{
		this.repair_vendor_list = repair_vendor_list;
	}

	public static Map<String, String> getCityMap()
	{
		return cityMap;
	}

	public static void setCityMap(Map<String, String> cityMap)
	{
		DevRepairTestInfoACT.cityMap = cityMap;
	}

	public String getSearchType()
	{
		return searchType;
	}

	public void setSearchType(String searchType)
	{
		this.searchType = searchType;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public int getQueryCount()
	{
		return queryCount;
	}

	public void setQueryCount(int queryCount)
	{
		this.queryCount = queryCount;
	}

	public List<String> getTitleList()
	{
		return titleList;
	}

	public void setTitleList(List<String> titleList)
	{
		this.titleList = titleList;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	@SuppressWarnings("rawtypes")
	public List getDataList()
	{
		return dataList;
	}

	@SuppressWarnings("rawtypes")
	public void setDataList(List dataList)
	{
		this.dataList = dataList;
	}
}
