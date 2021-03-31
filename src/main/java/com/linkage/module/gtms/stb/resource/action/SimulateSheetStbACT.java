
package com.linkage.module.gtms.stb.resource.action;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.SimulateSheetStbBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.SimulateSheetNxNewBIO;

public class SimulateSheetStbACT implements SessionAware
{

	private static Logger logger = LoggerFactory.getLogger(SimulateSheetStbACT.class);

	// 业务类型
	private String servTypeId;
	// 操作类型
	private String operateType;
	//业务受理时间
	private String dealdate;
	//客户类型
	private String userType;
	//LOID
	private String loidMsg;
	//用户宽带账号
	private String netUsername;
	//属地
	private String cityId;
	//业务账号
	private String bussAccount;
	//老业务账号
	private String oldBussAccount;
	//新业务账号
	private String newBussAccount;
	
	//业务密码
	private String bussPwd;
	//上网方式
	private String wlantype;
	//接入账号
	private String connAccount;
	//接入密码
	private String connPwd;
	//Ip地址
	private String ipAddr;
	//掩码
	private String hideNode;
	//网关
	private String netCheck;
	//DNS
	private String dnsMsg;
	//机顶盒MAC
	private String stbMacMsg;
	//新密码
	private String newPwd;
	
	private SimulateSheetStbBIO bio;
	
	// session
	private Map<String, Object> session;

	// 地市
	private List<Map<String, String>> cityList = null;

	private String ajax;

	public String excute()
	{
		logger.debug("excute()");
		return "success";
	}
	private String getNowDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String time = fmtrq.format(now.getTime());
		return time;
	}
	/*
	 * 展示机顶盒工单列表
	 */
	public String showSTBSheet()
	{
		UserRes curUser = (UserRes) this.session.get("curUser");
		this.cityId = curUser.getCityId();
		this.cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		this.dealdate = getNowDate();
		if (25 == StringUtil.getIntegerValue(this.servTypeId))
		{
			if ("1".equals(this.operateType))
			{
				logger.debug("开户");
				return "openAccount";
			}
			if ("3".equals(this.operateType))
			{
				logger.debug("销户");
				return "closeAccount";
			}
			if ("6".equals(this.operateType))
			{
				logger.debug("更改业务帐号");
				return "changeBussAccount";
			}
			if ("5".equals(this.operateType))
			{
				logger.debug("更改业务帐号密码");
				return "changeBussPassword";
			}
			if ("7".equals(this.operateType))
			{
				logger.debug("更换机顶盒");
				return "changeStb";
			}
			this.ajax = "未知操作类型";
			return "ajax";
		}

		this.ajax = "未知业务类型";
		return "ajax";
	}
	/*
	 * 展示机顶盒工单列表
	 */
	public String sendSheet()
	{
		if (StringUtil.IsEmpty(this.userType))
		{
			this.userType = "1";
		}
		if (StringUtil.IsEmpty(this.connAccount))
		{
			this.connAccount = "";
		}
		if (StringUtil.IsEmpty(this.connPwd))
		{
			this.connPwd = "";
		}
		if (StringUtil.IsEmpty(this.ipAddr))
		{
			this.ipAddr = "";
		}
		if (StringUtil.IsEmpty(this.hideNode))
		{
			this.hideNode = "";
		}
		if (StringUtil.IsEmpty(this.netCheck))
		{
			this.netCheck = "";
		}
		if (StringUtil.IsEmpty(this.dnsMsg))
		{
			this.dnsMsg = "";
		}
		if (25 == StringUtil.getIntegerValue(this.servTypeId))
		{
			logger.warn("25 == StringUtil.getIntegerValue(this.servTypeId)");
			if ("1".equals(this.operateType))
			{
				this.ajax = this.bio.sendOpenSheet(this.servTypeId,this.operateType,this.dealdate,this.userType,
								this.loidMsg,this.netUsername,this.cityId,this.bussAccount,this.bussPwd,
								this.wlantype,this.connAccount,this.connPwd,this.ipAddr,this.hideNode,this.netCheck,
								this.dnsMsg,this.stbMacMsg);
			}else if("3".equals(this.operateType)){
				this.ajax = this.bio.sendStopSheet(this.servTypeId, this.operateType, this.dealdate, this.userType, 
						this.loidMsg, this.cityId, this.bussAccount);
			}else if("6".equals(this.operateType)){
				this.ajax = this.bio.sendChangeAccSheet(this.servTypeId, this.operateType, this.dealdate, this.userType, this.loidMsg,
								this.cityId, this.oldBussAccount, this.newBussAccount);
			}else if("5".equals(this.operateType)){
				this.ajax = this.bio.sendChangeAccPwdSheet(servTypeId, operateType, dealdate, userType, loidMsg, cityId, bussAccount, newPwd);
			}else if("7".equals(this.operateType)){
				this.ajax = this.bio.changeStb(servTypeId, operateType, dealdate, userType, loidMsg, cityId, bussAccount, stbMacMsg);
			}
		}
		return "sheetStbResult";
	}
	public Map<String, Object> getSession()
	{
		return session;
	}

	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	public String getServTypeId()
	{
		return servTypeId;
	}

	public void setServTypeId(String servTypeId)
	{
		this.servTypeId = servTypeId;
	}

	public String getOperateType()
	{
		return operateType;
	}

	public void setOperateType(String operateType)
	{
		this.operateType = operateType;
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

	public String getDealdate()
	{
		return dealdate;
	}

	public void setDealdate(String dealdate)
	{
		this.dealdate = dealdate;
	}
	
	
	public String getUserType()
	{
		return userType;
	}
	
	public void setUserType(String userType)
	{
		this.userType = userType;
	}
	
	public String getLoidMsg()
	{
		return loidMsg;
	}
	
	public void setLoidMsg(String loidMsg)
	{
		this.loidMsg = loidMsg;
	}
	
	public String getNetUsername()
	{
		return netUsername;
	}
	
	public void setNetUsername(String netUsername)
	{
		this.netUsername = netUsername;
	}
	
	public String getBussAccount()
	{
		return bussAccount;
	}
	
	public void setBussAccount(String bussAccount)
	{
		this.bussAccount = bussAccount;
	}
	
	public String getOldBussAccount()
	{
		return oldBussAccount;
	}
	
	public void setOldBussAccount(String oldBussAccount)
	{
		this.oldBussAccount = oldBussAccount;
	}
	
	public String getNewBussAccount()
	{
		return newBussAccount;
	}
	
	public void setNewBussAccount(String newBussAccount)
	{
		this.newBussAccount = newBussAccount;
	}
	public String getBussPwd()
	{
		return bussPwd;
	}
	
	public void setBussPwd(String bussPwd)
	{
		this.bussPwd = bussPwd;
	}
	
	public String getWlantype()
	{
		return wlantype;
	}
	
	public void setWlantype(String wlantype)
	{
		this.wlantype = wlantype;
	}
	
	public String getConnAccount()
	{
		return connAccount;
	}
	
	public void setConnAccount(String connAccount)
	{
		this.connAccount = connAccount;
	}
	public String getConnPwd()
	{
		return connPwd;
	}
	
	public void setConnPwd(String connPwd)
	{
		this.connPwd = connPwd;
	}
	
	public String getIpAddr()
	{
		return ipAddr;
	}
	
	public void setIpAddr(String ipAddr)
	{
		this.ipAddr = ipAddr;
	}
	
	public String getHideNode()
	{
		return hideNode;
	}
	
	public void setHideNode(String hideNode)
	{
		this.hideNode = hideNode;
	}
	
	public String getNetCheck()
	{
		return netCheck;
	}
	
	public void setNetCheck(String netCheck)
	{
		this.netCheck = netCheck;
	}
	
	public String getDnsMsg()
	{
		return dnsMsg;
	}
	
	public void setDnsMsg(String dnsMsg)
	{
		this.dnsMsg = dnsMsg;
	}
	
	public String getStbMacMsg()
	{
		return stbMacMsg;
	}
	
	public void setStbMacMsg(String stbMacMsg)
	{
		this.stbMacMsg = stbMacMsg;
	}
	
	
	public String getNewPwd()
	{
		return newPwd;
	}
	
	public void setNewPwd(String newPwd)
	{
		this.newPwd = newPwd;
	}
	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	public SimulateSheetStbBIO getBio()
	{
		return bio;
	}
	
	public void setBio(SimulateSheetStbBIO bio)
	{
		this.bio = bio;
	}
}
