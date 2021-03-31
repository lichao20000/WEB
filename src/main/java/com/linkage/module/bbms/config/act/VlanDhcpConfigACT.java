
package com.linkage.module.bbms.config.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.config.bio.VlanDhcpConfigBIO;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.gw.LanVlanOBJ;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;
import com.opensymphony.xwork2.ActionSupport;

public class VlanDhcpConfigACT extends ActionSupport implements SessionAware
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(VlanDhcpConfigACT.class);
	// ajax
	private String ajax;
	private String deviceId;
	private List<LanVlanOBJ> lanVlanList;
	private String massage;
	// 设备支持VLAN总数
	private String vlanMaxNum;
	// 设备当前VLAN数
	private String vlanCurNum;
	// 设备上的WLAN端口
	private List wlanList;
	// 设备上LAN端口
	private List lanList;
	private int vlanI;
	private int dhcpEnable;
	private String dhcpMinAddr;
	private String dhcpMaxAddr;
	private String dhcpDomain;
	private String dhcpDns;
	private String dhcpGateway;
	private String dhcpLeaseTime;
	private String vlanName;
	private String portList;
	private int ipEnable;
	private String ipAddress;
	private String ipMask;
	private String ipAddressType;
	private int vlanId;
	// VLAN配置类型，“1”为编辑 “0”为新增
	private String type;
	// BIO
	private VlanDhcpConfigBIO vlanDhcpConfigBio;
	// session
	private Map session;

	/**
	 * 获取设备DHCP配置信息
	 * 
	 * @author wangsenbo
	 * @date 2009-10-27
	 * @return String
	 */
	public String dhcpInit()
	{
		logger.debug("dhcpInit()");
		if (false == StringUtil.IsEmpty(deviceId))
		{
			int iret = new SuperGatherCorba(LipossGlobals.getGw_Type(deviceId)).getCpeParams(deviceId,
					ConstantClass.GATHER_VLAN);
			// int iret = 1;
			if (iret == 1)
			{
				lanVlanList = vlanDhcpConfigBio.getDHCPConfigStatus(deviceId);
				if (lanVlanList == null)
				{
					massage = "该设备没有此信息";
				}
			}
			else
			{
				logger.warn(Global.G_Fault_Map.get(iret).getFaultReason());
				massage = Global.G_Fault_Map.get(iret).getFaultReason();
			}
		}
		else
		{
			massage = "未选定设备";
		}
		return "dhcp";
	}

	/**
	 * 配置设备DHCP信息
	 * 
	 * @author wangsenbo
	 * @date 2009-10-27
	 * @return String
	 */
	public String configDHCP()
	{
		logger.debug("configDHCP()");
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		LanVlanOBJ lanVlanObj = new LanVlanOBJ();
		lanVlanObj.setDeviceId(deviceId);
		lanVlanObj.setDhcpEnable(dhcpEnable);
		lanVlanObj.setDhcpGateway(dhcpGateway);
		lanVlanObj.setDhcpLeaseTime(dhcpLeaseTime);
		lanVlanObj.setDhcpMaxAddr(dhcpMaxAddr);
		lanVlanObj.setDhcpMinAddr(dhcpMinAddr);
		lanVlanObj.setDhcpDomain(dhcpDomain);
		lanVlanObj.setDhcpDns(dhcpDns);
		lanVlanObj.setVlanI(vlanI);
		ajax = vlanDhcpConfigBio.configDHCP(accoid, lanVlanObj);
		return "ajax";
	}

	/**
	 * 获取设备Vlan配置信息
	 * 
	 * @author wangsenbo
	 * @date Oct 28, 2009
	 * @return String
	 */
	public String vlanInit()
	{
		logger.debug("vlanInit()");
		if (false == StringUtil.IsEmpty(deviceId))
		{
			int iret = new SuperGatherCorba(LipossGlobals.getGw_Type(deviceId)).getCpeParams(deviceId,
					ConstantClass.GATHER_VLAN);
			// int iret = 1;
			if (iret == 1)
			{
				lanVlanList = vlanDhcpConfigBio.getVlanConfigStatus(deviceId);
				lanList = vlanDhcpConfigBio.getLanEth(deviceId);
				wlanList = vlanDhcpConfigBio.getWlan(deviceId);
				Map rmap = vlanDhcpConfigBio.getVlanNum(deviceId);
				if (rmap != null)
				{
					vlanMaxNum = StringUtil.getStringValue(rmap.get("vlan_max_num"));
					vlanCurNum = StringUtil.getStringValue(rmap.get("vlan_cur_num"));
				}
				else
				{
					vlanMaxNum = "";
					vlanCurNum = "";
				}
				if (lanVlanList == null)
				{
					massage = "该设备没有此信息";
				}
			}
			else
			{
				logger.warn(Global.G_Fault_Map.get(iret).getFaultReason());
				massage = Global.G_Fault_Map.get(iret).getFaultReason();
			}
		}
		else
		{
			massage = "未选定设备";
		}
		return "vlan";
	}

	/**
	 * 配置设备VLAN信息
	 * 
	 * @author wangsenbo
	 * @date 2009-10-27
	 * @return String
	 */
	public String configVlan()
	{
		logger.debug("configVlan()");
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		LanVlanOBJ lanVlanObj = new LanVlanOBJ();
		lanVlanObj.setDeviceId(deviceId);
		lanVlanObj.setVlanId(vlanId);
		lanVlanObj.setVlanName(vlanName);
		lanVlanObj.setPortList(portList);
		lanVlanObj.setIpEnable(ipEnable);
		lanVlanObj.setIpAddress(ipAddress);
		lanVlanObj.setIpMask(ipMask);
		lanVlanObj.setIpAddressType(ipAddressType);
		lanVlanObj.setVlanI(vlanI);
		ajax = vlanDhcpConfigBio.configVlan(accoid, lanVlanObj, type);
		return "ajax";
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
	 * @return the massage
	 */
	public String getMassage()
	{
		return massage;
	}

	/**
	 * @param massage
	 *            the massage to set
	 */
	public void setMassage(String massage)
	{
		this.massage = massage;
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
	 * @return the lanVlanList
	 */
	public List getLanVlanList()
	{
		return lanVlanList;
	}

	/**
	 * @param lanVlanList
	 *            the lanVlanList to set
	 */
	public void setLanVlanList(List lanVlanList)
	{
		this.lanVlanList = lanVlanList;
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
	 * @return the vlanDhcpConfigBio
	 */
	public VlanDhcpConfigBIO getVlanDhcpConfigBio()
	{
		return vlanDhcpConfigBio;
	}

	/**
	 * @param vlanDhcpConfigBio
	 *            the vlanDhcpConfigBio to set
	 */
	public void setVlanDhcpConfigBio(VlanDhcpConfigBIO vlanDhcpConfigBio)
	{
		this.vlanDhcpConfigBio = vlanDhcpConfigBio;
	}

	/**
	 * @return the vlanMaxNum
	 */
	public String getVlanMaxNum()
	{
		return vlanMaxNum;
	}

	/**
	 * @param vlanMaxNum
	 *            the vlanMaxNum to set
	 */
	public void setVlanMaxNum(String vlanMaxNum)
	{
		this.vlanMaxNum = vlanMaxNum;
	}

	/**
	 * @return the vlanCurNum
	 */
	public String getVlanCurNum()
	{
		return vlanCurNum;
	}

	/**
	 * @param vlanCurNum
	 *            the vlanCurNum to set
	 */
	public void setVlanCurNum(String vlanCurNum)
	{
		this.vlanCurNum = vlanCurNum;
	}

	/**
	 * @return the wlanList
	 */
	public List getWlanList()
	{
		return wlanList;
	}

	/**
	 * @param wlanList
	 *            the wlanList to set
	 */
	public void setWlanList(List wlanList)
	{
		this.wlanList = wlanList;
	}

	/**
	 * @return the lanList
	 */
	public List getLanList()
	{
		return lanList;
	}

	/**
	 * @param lanList
	 *            the lanList to set
	 */
	public void setLanList(List lanList)
	{
		this.lanList = lanList;
	}

	/**
	 * @return the vlanI
	 */
	public int getVlanI()
	{
		return vlanI;
	}

	/**
	 * @param vlanI
	 *            the vlanI to set
	 */
	public void setVlanI(int vlanI)
	{
		this.vlanI = vlanI;
	}

	/**
	 * @return the dhcpEnable
	 */
	public int getDhcpEnable()
	{
		return dhcpEnable;
	}

	/**
	 * @param dhcpEnable
	 *            the dhcpEnable to set
	 */
	public void setDhcpEnable(int dhcpEnable)
	{
		this.dhcpEnable = dhcpEnable;
	}

	/**
	 * @return the dhcpMinAddr
	 */
	public String getDhcpMinAddr()
	{
		return dhcpMinAddr;
	}

	/**
	 * @param dhcpMinAddr
	 *            the dhcpMinAddr to set
	 */
	public void setDhcpMinAddr(String dhcpMinAddr)
	{
		this.dhcpMinAddr = dhcpMinAddr;
	}

	/**
	 * @return the dhcpMaxAddr
	 */
	public String getDhcpMaxAddr()
	{
		return dhcpMaxAddr;
	}

	/**
	 * @param dhcpMaxAddr
	 *            the dhcpMaxAddr to set
	 */
	public void setDhcpMaxAddr(String dhcpMaxAddr)
	{
		this.dhcpMaxAddr = dhcpMaxAddr;
	}

	/**
	 * @return the dhcpDomain
	 */
	public String getDhcpDomain()
	{
		return dhcpDomain;
	}

	/**
	 * @param dhcpDomain
	 *            the dhcpDomain to set
	 */
	public void setDhcpDomain(String dhcpDomain)
	{
		this.dhcpDomain = dhcpDomain;
	}

	/**
	 * @return the dhcpDns
	 */
	public String getDhcpDns()
	{
		return dhcpDns;
	}

	/**
	 * @param dhcpDns
	 *            the dhcpDns to set
	 */
	public void setDhcpDns(String dhcpDns)
	{
		this.dhcpDns = dhcpDns;
	}

	/**
	 * @return the dhcpGateway
	 */
	public String getDhcpGateway()
	{
		return dhcpGateway;
	}

	/**
	 * @param dhcpGateway
	 *            the dhcpGateway to set
	 */
	public void setDhcpGateway(String dhcpGateway)
	{
		this.dhcpGateway = dhcpGateway;
	}

	/**
	 * @return the dhcpLeaseTime
	 */
	public String getDhcpLeaseTime()
	{
		return dhcpLeaseTime;
	}

	/**
	 * @param dhcpLeaseTime
	 *            the dhcpLeaseTime to set
	 */
	public void setDhcpLeaseTime(String dhcpLeaseTime)
	{
		this.dhcpLeaseTime = dhcpLeaseTime;
	}

	/**
	 * @return the vlanName
	 */
	public String getVlanName()
	{
		return vlanName;
	}

	/**
	 * @param vlanName
	 *            the vlanName to set
	 */
	public void setVlanName(String vlanName)
	{
		this.vlanName = vlanName;
	}

	/**
	 * @return the portList
	 */
	public String getPortList()
	{
		return portList;
	}

	/**
	 * @param portList
	 *            the portList to set
	 */
	public void setPortList(String portList)
	{
		this.portList = portList;
	}

	/**
	 * @return the ipEnable
	 */
	public int getIpEnable()
	{
		return ipEnable;
	}

	/**
	 * @param ipEnable
	 *            the ipEnable to set
	 */
	public void setIpEnable(int ipEnable)
	{
		this.ipEnable = ipEnable;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress()
	{
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 *            the ipAddress to set
	 */
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the ipMask
	 */
	public String getIpMask()
	{
		return ipMask;
	}

	/**
	 * @param ipMask
	 *            the ipMask to set
	 */
	public void setIpMask(String ipMask)
	{
		this.ipMask = ipMask;
	}

	/**
	 * @return the ipAddressType
	 */
	public String getIpAddressType()
	{
		return ipAddressType;
	}

	/**
	 * @param ipAddressType
	 *            the ipAddressType to set
	 */
	public void setIpAddressType(String ipAddressType)
	{
		this.ipAddressType = ipAddressType;
	}

	/**
	 * @return the vlanId
	 */
	public int getVlanId()
	{
		return vlanId;
	}

	/**
	 * @param vlanId
	 *            the vlanId to set
	 */
	public void setVlanId(int vlanId)
	{
		this.vlanId = vlanId;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the sessions
	 */
	public Map getSession()
	{
		return session;
	}
}