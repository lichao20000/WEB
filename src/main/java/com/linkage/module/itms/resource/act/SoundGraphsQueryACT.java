package com.linkage.module.itms.resource.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.bio.SoundGraphsQueryBIO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-2-15
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class SoundGraphsQueryACT extends splitPageAction implements SessionAware,
ServletRequestAware
{
	private static final long serialVersionUID = 2425363349057904543L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SoundGraphsQueryACT.class);
	//开通时间
	private String startOpenDate;
	
	//结束时间 
	private String endOpenDate;
	/**
	 * 用户名类型 "1" 逻辑SN, "2" 宽带号码, "3" IPTV号码, "4" VoIP认证号码, "5" VoIP电话号码
	 */
	private String usernameType;
	/**
	 * 开通状态"1"成功，"0"未做,"-1"失败
	 */
	private String openstatus;
	/** 业务信息列表*/
	private List<Map> Date;
	private List<Map> userid;
	private List<Map> DeviceId;
	
	private String username;
	
	private SoundGraphsQueryBIO bio;
	
	private int user_id;
	private String deviceid;
	private String digit_map;
	private String  gw_type;
	private String username1;
	/**
	 * 初始化页面
	 * @return
	 */
	public String init()
	{
		DateTimeUtil dt = new DateTimeUtil();
		endOpenDate = dt.getDate();
		startOpenDate = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endOpenDate);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endOpenDate = dt.getLongDate();
		dt = new DateTimeUtil(startOpenDate);
		startOpenDate = dt.getLongDate();
		return "init";
	}
	//时间转换
	public void setTime()
	{
		logger.debug("setTime()" + startOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate))
		{
			startOpenDate = null;
		}
		else
		{
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			endOpenDate = null;
		}
		else
		{
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate = String.valueOf(dt.getLongTime());
		}
	}
	
	/**
	 * 查询业务信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String query()
	{
		this.setTime();
		Map<String,String> map=new HashMap<String, String>();
		// 查询user_id
		userid = bio.userid(usernameType, username,gw_type);
		if(userid!=null&&!userid.isEmpty())
		{
			if(userid.size()==1)
			{
			 map = userid.get(0);
			 user_id = StringUtil.getIntegerValue(map.get("user_id"));
			 DeviceId=bio.deviceid(usernameType, username, user_id);

			 //查询deviceid,digit_map
			if(DeviceId!=null&&!DeviceId.isEmpty())
			{
				 map = DeviceId.get(0);
				 deviceid = StringUtil.getStringValue(map.get("device_id"));
				 digit_map = StringUtil.getStringValue(map.get("digit_map"));
				 username1=StringUtil.getStringValue(map.get("username"));
				 logger.warn("username1++++"+username1);
				 if(DeviceId.size()==1)
				 {
				 if(!StringUtil.IsEmpty(deviceid))
				 {
					 
				if(!StringUtil.IsEmpty(digit_map))
				{
					//查询策略表
					Date=bio.query(username1,startOpenDate, endOpenDate, usernameType, openstatus, username, user_id, deviceid, digit_map,curPage_splitPage,
							num_splitPage);
					maxPage_splitPage=bio.getMaxPage_splitPage(username1,startOpenDate, endOpenDate, usernameType, openstatus, username, user_id, deviceid, digit_map,curPage_splitPage,num_splitPage);
					return "success";
				}
				else
				{
					return "success";
				}
			}
			else
			{
				return "success";
			}
			}
			else
			{
				return "success";
			}
			}
			else
			{
				return "success";
			}
		}else
		{
			return "success";
		}
		} else{
			return  "success";
		}
	}
	
	
	public String number()
	{
		Date=bio.number(digit_map);
		return "number";
	}
	public String getStartOpenDate()
	{
		return startOpenDate;
	}
	
	public void setStartOpenDate(String startOpenDate)
	{
		this.startOpenDate = startOpenDate;
	}
	
	public String getEndOpenDate()
	{
		return endOpenDate;
	}
	
	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}
	
	public String getUsernameType()
	{
		return usernameType;
	}
	
	public void setUsernameType(String usernameType)
	{
		this.usernameType = usernameType;
	}
	
	public String getOpenstatus()
	{
		return openstatus;
	}
	
	public void setOpenstatus(String openstatus)
	{
		this.openstatus = openstatus;
	}

	public List<Map> getDate()
	{
		return Date;
	}
	
	public void setDate(List<Map> date)
	{
		Date = date;
	}
	
	public SoundGraphsQueryBIO getBio()
	{
		return bio;
	}
	
	public void setBio(SoundGraphsQueryBIO bio)
	{
		this.bio = bio;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	
	public int getUser_id()
	{
		return user_id;
	}
	
	public void setUser_id(int user_id)
	{
		this.user_id = user_id;
	}
	public String getDeviceid()
	{
		return deviceid;
	}
	
	public void setDeviceid(String deviceid)
	{
		this.deviceid = deviceid;
	}
	public String getDigit_map()
	{
		return digit_map;
	}
	
	public void setDigit_map(String digit_map)
	{
		this.digit_map = digit_map;
	}
	
	public List<Map> getUserid()
	{
		return userid;
	}
	
	public void setUserid(List<Map> userid)
	{
		this.userid = userid;
	}
	
	public List<Map> getDeviceId()
	{
		return DeviceId;
	}
	
	public void setDeviceId(List<Map> deviceId)
	{
		DeviceId = deviceId;
	}
	
	public String getGw_type()
	{
		return gw_type;
	}
	
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}
	
	public String getUsername1()
	{
		return username1;
	}
	
	public void setUsername1(String username1)
	{
		this.username1 = username1;
	}
	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setSession(Map<String, Object> session)
	{
		// TODO Auto-generated method stub
		
	}

	
	
}
