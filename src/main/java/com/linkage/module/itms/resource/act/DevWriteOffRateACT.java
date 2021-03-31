
package com.linkage.module.itms.resource.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.DevWriteOffRateBIO;

/**
 * @author yinlei3 (Ailk No.73167)
 * @version 1.0
 * @since 2016年4月11日
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DevWriteOffRateACT implements SessionAware
{

	/** 日志 */
	private static Logger logger = LoggerFactory
			.getLogger(DevWriteOffRateACT.class);
	/** 属地列表集合 */
	private List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();
	/** 用户受理开始时间 */
	private String starttime = "";
	/** 用户受理结束时间 */
	private String endtime = "";
	/** 核销方式 */
	private String writeOffType = "";
	/** 属地 */
	private String cityId = "";
	/** cityMap */
	private static Map<String, String> cityMap = new HashMap<String, String>();
	/** bio */
	private DevWriteOffRateBIO bio;
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
	/** 统计类型 */
	private String type = "";
	static
	{
		cityMap = DevWriteOffRateBIO.getSencondCityIdCityNameMap();
	}
	/** session */
	@SuppressWarnings("rawtypes")
	private Map session;

	/**
	 * 初始化界面
	 */
	public String initWriteOff()
	{
		logger.debug("DevWriteOffRateACT ==> initWriteOff");
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
		if ("1".equals(type))
		{
			// 规范率界面
			return "initChange";
		}
		else
		{
			// 核销率界面
			return "initWriteOff";
		}
	}

	/**
	 * 核销率统计柱状图和饼图
	 */
	public String writeOffRate()
	{
		logger.debug("DevWriteOffRateACT ==> writeOffRate");
		// 时间转化
		starttime = setTime(starttime);
		endtime = setTime(endtime);
		ajax = bio.writeOffRate(cityId, starttime, endtime);
		return "ajax";
	}

	/**
	 * 核销率导出报表
	 */
	public String parseExcel()
	{
		logger.debug("parseExcel");
		// 时间转化
		starttime = setTime(starttime);
		endtime = setTime(endtime);
		title = new String[4];
		column = new String[4];
		title[0] = "属地";
		title[1] = "核销工单数";
		column[0] = "city";
		column[1] = "totalcount";
		if ("1".equals(writeOffType))
		{
			fileName = "自动核销工单占比";
			title[2] = "自动核销工单数";
			title[3] = "自动核销率";
			column[2] = "autocount";
			column[3] = "autorate";
		}
		else
		{
			fileName = "人工核销工单占比";
			title[2] = "人工核销工单数";
			title[3] = "人工核销率";
			column[2] = "mancount";
			column[3] = "manrate";
		}
		data = bio.parseExcel(cityId, starttime, endtime);
		return "excel";
	}

	/**
	 * 核销率导出清单
	 */
	public String parseDetail()
	{
		logger.debug("parseExcel");
		// 时间转化
		starttime = setTime(starttime);
		endtime = setTime(endtime);
		fileName = "核销工单清单";
		title = new String[13];
		column = new String[13];
		title[0] = "属地";
		title[1] = "工单号";
		title[2] = "本地网";
		title[3] = "区域";
		title[4] = "班组";
		title[5] = "装维人员";
		title[6] = "用户LOID";
		title[7] = "用户地址";
		title[8] = "核销设备";
		title[9] = "核销设备规格";
		title[10] = "核销方式";
		title[11] = "核销日期";
		title[12] = "工程性质";
		column[0] = "city";
		column[1] = "bill_no";
		column[2] = "orgname";
		column[3] = "area";
		column[4] = "group_name";
		column[5] = "install_person";
		column[6] = "username";
		column[7] = "inst_addr";
		column[8] = "term_serial";
		column[9] = "spec_name_term";
		column[10] = "use_type";
		column[11] = "use_time";
		column[12] = "project_nature";
		data = bio.parseDetail(cityId, starttime, endtime, writeOffType);
		return "excel";
	}

	/**
	 * 更换率柱状图和饼图
	 */
	public String devChange()
	{
		logger.debug("DevWriteOffRateACT ==> writeChange");
		// 时间转化
		starttime = setTime(starttime);
		endtime = setTime(endtime);
		ajax = bio.devChange(cityId, starttime, endtime);
		return "ajax";
	}

	/**
	 * 更换率导出报表
	 */
	public String parseChangeExcel()
	{
		logger.debug("parseExcel");
		// 时间转化
		starttime = setTime(starttime);
		endtime = setTime(endtime);
		fileName = "终端更换规范率统计";
		title = new String[4];
		column = new String[4];
		title[0] = "属地";
		title[1] = "终端更换总数";
		title[2] = "规范更换设备数";
		title[3] = "终端更换规范率";
		column[0] = "city";
		column[1] = "totalcount";
		column[2] = "normalcount";
		column[3] = "normalrate";
		data = bio.parseChangeExcel(cityId, starttime, endtime);
		return "excel";
	}

	/**
	 * 更换率导出清单
	 */
	public String parseChangeDetail()
	{
		logger.debug("parseExcel");
		// 时间转化
		starttime = setTime(starttime);
		endtime = setTime(endtime);
		fileName = "更换不规范终端清单";
		title = new String[16];
		column = new String[16];
		title[0] = "属地";
		title[1] = "用户LOID";
		title[2] = "宽带账号";
		title[3] = "绑定设备";
		title[4] = "解绑设备";
		title[5] = "绑定时间";
		title[6] = "解绑时间";
		title[7] = "业务号码";
		title[8] = "工单号";
		title[9] = "地址";
		title[10] = "业务类型";
		title[11] = "处理人（装维个人）";
		title[12] = "处理组（班组）";
		title[13] = "本地网";
		title[14] = "区域";
		title[15] = "回单时间";

		column[0] = "city";
		column[1] = "username";
		column[2] = "net_account";
		column[3] = "dev_sn_after";
		column[4] = "dev_sn_before";
		column[5] = "bind_time";
		column[6] = "update_time";
		column[7] = "serv_no";
		column[8] = "bill_no";
		column[9] = "addr";
		column[10] = "buss_type";
		column[11] = "deal_person";
		column[12] = "deal_group";
		column[13] = "orgname";
		column[14] = "area";
		column[15] = "revert_time";
		data = bio.parseChangeDetail(cityId, starttime, endtime);
		return "excel";
	}

	public String parseChangeAllDetail()
	{
		logger.debug("parseExcel");
		// 时间转化
		starttime = setTime(starttime);
		endtime = setTime(endtime);
		fileName = "更换全量清单";
		title = new String[17];
		column = new String[17];
		title[0] = "属地";
		title[1] = "用户LOID";
		title[2] = "宽带账号";
		title[3] = "绑定设备";
		title[4] = "解绑设备";
		title[5] = "绑定时间";
		title[6] = "解绑时间";
		title[7] = "业务号码";
		title[8] = "工单号";
		title[9] = "地址";
		title[10] = "业务类型";
		title[11] = "处理人（装维个人）";
		title[12] = "处理组（班组）";
		title[13] = "本地网";
		title[14] = "区域";
		title[15] = "回单时间";
		title[16] = "是否规范";

		column[0] = "city";
		column[1] = "username";
		column[2] = "net_account";
		column[3] = "dev_sn_after";
		column[4] = "dev_sn_before";
		column[5] = "bind_time";
		column[6] = "update_time";
		column[7] = "serv_no";
		column[8] = "bill_no";
		column[9] = "addr";
		column[10] = "buss_type";
		column[11] = "deal_person";
		column[12] = "deal_group";
		column[13] = "orgname";
		column[14] = "area";
		column[15] = "revert_time";
		column[16] = "is_normal";
		data = bio.parseChangeAllDetail(cityId, starttime, endtime);
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

	@Override
	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	@SuppressWarnings("rawtypes")
	public Map getSession()
	{
		return session;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public String getStarttime()
	{
		return starttime;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public String getWriteOffType()
	{
		return writeOffType;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	public void setWriteOffType(String writeOffType)
	{
		this.writeOffType = writeOffType;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public void setBio(DevWriteOffRateBIO bio)
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

	public String[] getColumn()
	{
		return column;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
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

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
