
package com.linkage.module.itms.config.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;
import com.linkage.module.itms.config.bio.PreConfigDiagBIO;

public class PreConfigDiagACT extends splitPageAction implements SessionAware
{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(PreConfigDiagACT.class);
	// session
	private Map session;
	private PreConfigDiagBIO bio;
	private String deviceId;
	private Map baseInfoMap;
	/**
	 * 宽带上网
	 */
	private String wideNetMsg = null;
	/**
	 * IPTV
	 */
	private String iptvMsg = null;
	/**
	 * VOIP
	 */
	private String voipMsg = null;
	/**
	 * QOS
	 */
	private String qosMsg = null;
	private String cityId;
	private String accessType;
	private String igmp;
	private String proxy;
	private String snooping;
	private String deviceserialnumber;
	private String oui;
	private String userId;

	/**
	 * @return the igmp
	 */
	public String getIgmp()
	{
		return igmp;
	}

	/**
	 * @param igmp
	 *            the igmp to set
	 */
	public void setIgmp(String igmp)
	{
		this.igmp = igmp;
	}
	
	
	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getSnooping() {
		return snooping;
	}

	public void setSnooping(String snooping) {
		this.snooping = snooping;
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

	private Map qosmap;
	private List internetlist;
	private List iptvlist;
	private List voipdevicelist;
	private List voipPVClist;
    
    /**
     * VOIP协议类型0-IMS SIP,1-软交换 SIP,2-H.248
     */
    private String voipProtocalTypeStr;
    
	private String ajax;
	private String strategyId;
	
	/** 终端类型 */
	private String gw_type = null;


	/**
	 * 基础信息
	 * 
	 * @author wangsenbo
	 * @date Nov 23, 2010
	 * @param
	 * @return String
	 */
	public String baseInfo()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		baseInfoMap = bio.baseInfo(deviceId, curUser, gw_type);
		return "baseInfo";
	}

	/**
	 * 采集设备信息，判断预配置
	 * 
	 * @author wangsenbo
	 * @date Nov 23, 2010
	 * @param
	 * @return String
	 */
	public String businessInfo()
	{
		int rsint = new SuperGatherCorba(gw_type).getCpeParams(deviceId, 0, 3);
		//int rsint = 1;
		/**
		 * 取全部，暂注释
		 */
		if (rsint != 1)
		{
		    //调用采集失败
			logger.warn("getData sg fail, rsint:{}", rsint);
			if (null == Global.G_Fault_Map.get(rsint))
			{
				this.wideNetMsg = Global.G_Fault_Map.get(100000).getFaultReason();
				this.iptvMsg = Global.G_Fault_Map.get(100000).getFaultReason();
				this.voipMsg = Global.G_Fault_Map.get(100000).getFaultReason();
				this.qosMsg = Global.G_Fault_Map.get(100000).getFaultReason();
			}else{
				this.wideNetMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
				this.iptvMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
				this.voipMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
				this.qosMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			}
			
		}
		else
		{
		    //采集数据
			bio.preConfigDiag(deviceId, cityId, Global.instAreaShortName,gw_type);
			qosmap = bio.getQosmap();
			internetlist = bio.getInternetlist();
			iptvlist = bio.getIptvlist();
			voipdevicelist = bio.getVoipdevicelist();
			voipPVClist = bio.getVoipPVClist();
			//协议类型
			voipProtocalTypeStr = bio.getVoipProtocalTypeStr();
			wideNetMsg = bio.getWideNetMsg();
			iptvMsg = bio.getIptvMsg();
			voipMsg = bio.getVoipMsg();
			qosMsg = bio.getQosMsg();
			accessType = bio.getAccessType();
			igmp = bio.getIgmp();
			proxy = bio.getProxy();
			snooping = bio.getSnooping();
		}
		return "businessInfo";
	}

	/**
	 * 配置设备基本配置
	 * 
	 * @author wangsenbo
	 * @date Nov 22, 2010
	 * @param
	 * @return String
	 */
	public String wideNetconfig()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		ajax = bio.wideNetconfig(curUser.getUser().getId(), deviceId, accessType, cityId,
				Global.instAreaShortName, gw_type);
		return "ajax";
	}

	/**
	 * 配置设备基本配置
	 * 
	 * @author wangsenbo
	 * @date Nov 22, 2010
	 * @param
	 * @return String
	 */
	public String iptvconfig()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		ajax = bio
				.iptvconfig(curUser.getUser().getId(), deviceId, accessType, cityId,
						Global.instAreaShortName,
						deviceserialnumber, gw_type);
		return "ajax";
	}

	public String voipconfig()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		ajax = bio.voipconfig(curUser.getUser().getId(), deviceId, accessType, cityId,
				Global.instAreaShortName,
				deviceserialnumber, oui, gw_type);
		return "ajax";
	}

	public String qosconfig()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		ajax = bio.qosconfig(curUser.getUser().getId(), deviceId, gw_type);
		return "ajax";
	}

	public String getStrategyById()
	{
		logger.debug("getStrategyById()");
		if (StringUtil.IsEmpty(strategyId))
		{
			logger.error("策略ID为空！");
		}
		else
		{
			ajax = bio.getStrategyById(strategyId);
		}
		return "ajax";
	}

	public String getHgwcustServById()
	{
		logger.debug("getHgwcustServById()");
		if (StringUtil.IsEmpty(userId))
		{
			logger.error("用户ID为空！");
		}
		else
		{
			ajax = bio.getHgwcustServById(userId);
		}
		return "ajax";
	}

	/**
	 * @return the bio
	 */
	public PreConfigDiagBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(PreConfigDiagBIO bio)
	{
		this.bio = bio;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	/**
	 * @return the baseInfoMap
	 */
	public Map getBaseInfoMap()
	{
		return baseInfoMap;
	}

	/**
	 * @param baseInfoMap
	 *            the baseInfoMap to set
	 */
	public void setBaseInfoMap(Map baseInfoMap)
	{
		this.baseInfoMap = baseInfoMap;
	}

	/**
	 * @return the wideNetMsg
	 */
	public String getWideNetMsg()
	{
		return wideNetMsg;
	}

	/**
	 * @param wideNetMsg
	 *            the wideNetMsg to set
	 */
	public void setWideNetMsg(String wideNetMsg)
	{
		this.wideNetMsg = wideNetMsg;
	}

	/**
	 * @return the iptvMsg
	 */
	public String getIptvMsg()
	{
		return iptvMsg;
	}

	/**
	 * @param iptvMsg
	 *            the iptvMsg to set
	 */
	public void setIptvMsg(String iptvMsg)
	{
		this.iptvMsg = iptvMsg;
	}

	/**
	 * @return the voipMsg
	 */
	public String getVoipMsg()
	{
		return voipMsg;
	}

	/**
	 * @param voipMsg
	 *            the voipMsg to set
	 */
	public void setVoipMsg(String voipMsg)
	{
		this.voipMsg = voipMsg;
	}

	/**
	 * @return the qosMsg
	 */
	public String getQosMsg()
	{
		return qosMsg;
	}

	/**
	 * @param qosMsg
	 *            the qosMsg to set
	 */
	public void setQosMsg(String qosMsg)
	{
		this.qosMsg = qosMsg;
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
	 * @return the accessType
	 */
	public String getAccessType()
	{
		return accessType;
	}

	/**
	 * @param accessType
	 *            the accessType to set
	 */
	public void setAccessType(String accessType)
	{
		this.accessType = accessType;
	}

	/**
	 * @return the qosmap
	 */
	public Map getQosmap()
	{
		return qosmap;
	}

	/**
	 * @param qosmap
	 *            the qosmap to set
	 */
	public void setQosmap(Map qosmap)
	{
		this.qosmap = qosmap;
	}

	/**
	 * @return the internetlist
	 */
	public List getInternetlist()
	{
		return internetlist;
	}

	/**
	 * @param internetlist
	 *            the internetlist to set
	 */
	public void setInternetlist(List internetlist)
	{
		this.internetlist = internetlist;
	}

	/**
	 * @return the iptvlist
	 */
	public List getIptvlist()
	{
		return iptvlist;
	}

	/**
	 * @param iptvlist
	 *            the iptvlist to set
	 */
	public void setIptvlist(List iptvlist)
	{
		this.iptvlist = iptvlist;
	}

	/**
	 * @return the voipdevicelist
	 */
	public List getVoipdevicelist()
	{
		return voipdevicelist;
	}

	/**
	 * @param voipdevicelist
	 *            the voipdevicelist to set
	 */
	public void setVoipdevicelist(List voipdevicelist)
	{
		this.voipdevicelist = voipdevicelist;
	}

	/**
	 * @return the deviceserialnumber
	 */
	public String getDeviceserialnumber()
	{
		return deviceserialnumber;
	}

	/**
	 * @param deviceserialnumber
	 *            the deviceserialnumber to set
	 */
	public void setDeviceserialnumber(String deviceserialnumber)
	{
		this.deviceserialnumber = deviceserialnumber;
	}

	/**
	 * @return the oui
	 */
	public String getOui()
	{
		return oui;
	}

	/**
	 * @param oui
	 *            the oui to set
	 */
	public void setOui(String oui)
	{
		this.oui = oui;
	}

	/**
	 * @return the userId
	 */
	public String getUserId()
	{
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	/**
	 * @return the voipPVClist
	 */
	public List getVoipPVClist()
	{
		return voipPVClist;
	}

	/**
	 * @param voipPVClist
	 *            the voipPVClist to set
	 */
	public void setVoipPVClist(List voipPVClist)
	{
		this.voipPVClist = voipPVClist;
	}

	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	/**
	 * @return the strategyId
	 */
	public String getStrategyId()
	{
		return strategyId;
	}

	/**
	 * @param strategyId
	 *            the strategyId to set
	 */
	public void setStrategyId(String strategyId)
	{
		this.strategyId = strategyId;
	}

    /**
     * 获取voipProtocalTypeStr
     * @return String voipProtocalTypeStr
     */
    public String getVoipProtocalTypeStr()
    {
        return voipProtocalTypeStr;
    }

    /**
     * 设置voipProtocalTypeStr
     * @param String voipProtocalTypeStr
     */
    public void setVoipProtocalTypeStr(String voipProtocalTypeStr)
    {
        this.voipProtocalTypeStr = voipProtocalTypeStr;
    }

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
}