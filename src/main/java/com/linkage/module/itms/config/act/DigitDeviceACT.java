
package com.linkage.module.itms.config.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.init.obj.CpeFaultcodeOBJ;
import com.linkage.module.itms.config.bio.DigitDeviceBIO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-9-19 上午10:03:56
 * @category com.linkage.module.gwms.share.act
 * @copyright 南京联创科技 网管科技部
 */
public class DigitDeviceACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DigitDeviceACT.class);
	private Map session;
	public DigitDeviceBIO digitDeviceBIO;
	private String cityId;
	private String vendorId;
	private String deviceModelId;
	private String deviceTypeId;
	private String device_serialnumber;
	private String loopback_ip;
	private String device_id_ex;
	private String task_name;
	private String rsMsg;
	private List<Map> list;
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;

	public String execute() throws Exception
	{
		logger.debug("queryAll()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		return "success";
	}

	public String query() throws Exception
	{
		logger.debug("queryList()");
		list = digitDeviceBIO.query(cityId, vendorId, deviceModelId, deviceTypeId,
				device_serialnumber, loopback_ip, device_id_ex, task_name,curPage_splitPage, num_splitPage);
		
		logger.warn("list size" + list.size());
		for (int i = 0; i < list.size(); i++)
		{
			String enable = StringUtil.getStringValue(( list.get(i)).get("enable"));
			if (null != enable && !"".equals(enable))
			{
				int en = Integer.parseInt(enable);
				if (en == 1)
				{
					( list.get(i)).put("enable", "启用");
				}
				else
				{
					( list.get(i)).put("enable", "失效");
				}
			}
			// 时间格式转变
			String starttime = StringUtil.getStringValue(( list.get(i))
					.get("starttime"));
			if (null != starttime && !"".equals(starttime))
			{
				long st = Long.parseLong(starttime) * 1000;
				if (st > 0)
				{
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(st);
					String date = new SimpleDateFormat("yyyy-MM-dd")
							.format(cal.getTime());
					( list.get(i)).put("starttime", date);
				}
			}
			String endtime = StringUtil
					.getStringValue(((Map) list.get(i)).get("endtime"));
			if (null != endtime && !"".equals(endtime))
			{
				long et = Long.parseLong(endtime) * 1000;
				if (et > 0)
				{
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(et);
					String date = new SimpleDateFormat("yyyy-MM-dd")
							.format(cal.getTime());
					 list.get(i).put("endtime", date);
				}
			}
			// 下发结果转变
			String rs = StringUtil.getStringValue(( list.get(i)).get("result_id"));
			if (null != rs && !"".equals(rs))
			{
				int rsint = Integer.parseInt(rs);
				if (rsint != 1)
				{
					logger.warn("getData sg fail");
					CpeFaultcodeOBJ obj = Global.G_Fault_Map.get(rsint);
					if (null == obj)
					{
						logger.warn("can not find fault reason. rsint:" + rsint);
						this.rsMsg = Global.G_Fault_Map.get(100000).getFaultDesc();
						logger.warn("rsMsg if" + rsMsg);
						( list.get(i)).put("result_id", rsMsg);
					}
					else
					{
						this.rsMsg = obj.getFaultDesc();
						logger.warn("rsMsg else" + rsMsg);
						( list.get(i)).put("result_id", rsMsg);
						if (null == this.rsMsg)
						{
							this.rsMsg = Global.G_Fault_Map.get(100000).getFaultDesc();
							logger.warn("rsMsg else if" + rsMsg);
							( list.get(i)).put("result_id", rsMsg);
						}
					}
				}
				else
				{
					( list.get(i)).put("result_id", "成功");
					logger.warn("success");
				}
			}
		}
		
		maxPage_splitPage = digitDeviceBIO.getMaxPage_splitPage();
		return "list";
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getDeviceModelId()
	{
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId)
	{
		this.deviceModelId = deviceModelId;
	}

	public String getDeviceTypeId()
	{
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId)
	{
		this.deviceTypeId = deviceTypeId;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public Map getSession()
	{
		return this.session;
	}

	public DigitDeviceBIO getDigitDeviceBIO()
	{
		return digitDeviceBIO;
	}

	public void setDigitDeviceBIO(DigitDeviceBIO digitDeviceBIO)
	{
		this.digitDeviceBIO = digitDeviceBIO;
	}

	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String deviceSerialnumber)
	{
		device_serialnumber = deviceSerialnumber;
	}

	public String getLoopback_ip()
	{
		return loopback_ip;
	}

	public void setLoopback_ip(String loopbackIp)
	{
		loopback_ip = loopbackIp;
	}

	public String getDevice_id_ex()
	{
		return device_id_ex;
	}

	public void setDevice_id_ex(String deviceIdEx)
	{
		device_id_ex = deviceIdEx;
	}

	public String getTask_name()
	{
		return task_name;
	}

	public void setTask_name(String taskName)
	{
		task_name = taskName;
	}

	
	
	public List<Map> getList()
	{
		return list;
	}

	
	public void setList(List<Map> list)
	{
		this.list = list;
	}

	public String getRsMsg()
	{
		return rsMsg;
	}

	public void setRsMsg(String rsMsg)
	{
		this.rsMsg = rsMsg;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}
}
