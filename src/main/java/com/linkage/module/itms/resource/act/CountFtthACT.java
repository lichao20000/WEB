
package com.linkage.module.itms.resource.act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.User;
import com.linkage.litms.system.dbimpl.DbUserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.CountFtthBIO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-11-18 下午03:18:32
 * @category com.linkage.module.itms.resource.act
 * @copyright 南京联创科技 网管科技部
 */
public class CountFtthACT extends splitPageAction implements SessionAware,
		ServletRequestAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(CountFtthACT.class);
	private static final long serialVersionUID = -8288612851382310132L;
	private Map session;
	// request取登陆帐号使用
	private HttpServletRequest request;
	private CountFtthBIO bio;
	/** DbUserRes */
	private DbUserRes dbUserRes;
	/** user */
	private User user;
	/** 属地列表 */
	private List<Map<String, String>> CityList = null;
	/** "0" 未绑定 "1" 未上报loid */
	private String type;
	private String device_id;
	private String startTime;
	private String endTime;
	private String cityId;
	private List deviceList;
	// ********Export All Data To Excel****************
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

	public String execute()
	{
		logger.warn("type:" + type);
		dbUserRes = (DbUserRes) session.get("curUser");
		user = dbUserRes.getUser();
		// 取得属地列表
		CityList = CityDAO.getAllNextCityListByCityPid(user.getCityId());
		if (type.equals("0"))
		{
			return "init1";
		}
		else
		{
			return "init2";
		}
	}

	public String queryUnbind()
	{
		if (!"".equals(startTime) && !"".equals(endTime))
		{
			startTime = dealTime(startTime);
			endTime = dealTime(endTime);
		}
		deviceList = bio.queryUnbind(startTime, endTime, cityId, curPage_splitPage,
				num_splitPage);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "unbind";
	}

	public String toExcel()
	{
		if (!"".equals(startTime) && !"".equals(endTime))
		{
			startTime = dealTime(startTime);
			endTime = dealTime(endTime);
		}
		deviceList = bio.queryUnbind(startTime, endTime, cityId);
		String excelCol = "city_name#vendor_add#device_model#device_serialnumber#device_id_ex#loopback_ip#last_time";
		String excelTitle = "属地名称#设备厂商#设备型号#设备序列号#逻辑SN#域名或者IP#上报时间";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "checkWork";
		data = deviceList;
		return "excel";
	}

	public String dealTime(String time)
	{
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date str = new Date();
		try
		{
			str = date.parse(time);
		}
		catch (ParseException e)
		{
			logger.warn("选择开始或者结束的时间格式不对:" + time);
		}
		return str.getTime() / 1000 + "";
	}

	public String queryFTTHDetail()
	{
		deviceList = bio.queryFTTHDetail(device_id);
		return "detail";
	}

	public String queryUnloid()
	{
		if (!"".equals(startTime) && !"".equals(endTime))
		{
			startTime = dealTime(startTime);
			endTime = dealTime(endTime);
		}
		deviceList = bio.queryUnloid(startTime, endTime, cityId, curPage_splitPage,
				num_splitPage);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "unloid";
	}

	public String toExcelUnloid()
	{
		if (!"".equals(startTime) && !"".equals(endTime))
		{
			startTime = dealTime(startTime);
			endTime = dealTime(endTime);
		}
		deviceList = bio.queryUnloid(startTime, endTime, cityId);
		String excelCol = "city_name#vendor_add#device_model#device_serialnumber#device_id_ex#loopback_ip#last_time";
		String excelTitle = "属地名称#设备厂商#设备型号#设备序列号#逻辑SN#域名或者IP#上报时间";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "checkWork";
		data = deviceList;
		return "excel";
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public CountFtthBIO getBio()
	{
		return bio;
	}

	public void setBio(CountFtthBIO bio)
	{
		this.bio = bio;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public Map getSession()
	{
		return session;
	}

	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public List<Map<String, String>> getCityList()
	{
		return CityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		CityList = cityList;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public List getDeviceList()
	{
		return deviceList;
	}

	public void setDeviceList(List deviceList)
	{
		this.deviceList = deviceList;
	}

	public String getDevice_id()
	{
		return device_id;
	}

	public void setDevice_id(String deviceId)
	{
		device_id = deviceId;
	}

	public List<Map> getData()
	{
		return data;
	}

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
}
