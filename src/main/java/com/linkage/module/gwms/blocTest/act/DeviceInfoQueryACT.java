
package com.linkage.module.gwms.blocTest.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gwms.blocTest.bio.DeviceInfoQueryBIO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-7-21 下午04:10:14
 * @category resource.action
 * @copyright 南京联创科技 网管科技部
 */
public class DeviceInfoQueryACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(DeviceInfoQueryACT.class);
	/** 查询的所有数据列表 */
	private List<Map> deviceList;
	/** 家庭网关用户信息列表 */
	private List hgwcustServInfoList;
	
     /** 查询的所有资源配置列表 */
	private List<Map> deviceTypeConfigList;
	/** 上行方式 */
	private int access_type;
	/** 设备类型 */
	private int device_type;
	/** 是否机卡分离型 */
	private int is_card_apart;
	/** wan口名称 */
	private String wan_name;
	/** wan口数量 */
	private int wan_num;
	/** wan口处理能力 */
	private String wan_can;
	/** lan口名称 */
	private String lan_name;
	/** lan口数量 */
	private int lan_num;
	/** lan口处理能力 */
	private String lan_can;
	/** wlan口名称 */
	private String wlan_name;
	/** wlan口数量 */
	private int wlan_num;
	/** wlan口处理能力 */
	private String wlan_can;
	/** 语音口名称 */
	private String voip_name;
	/** 语音口数量 */
	private int voip_num;
	/** 语音口处理能力 */
	private String voip_can;
	/** 语音协议 */
	private int voip_protocol;
	/** 天线类型 */
	private int wireless_type;
	/** 天线根数 */
	private int wireless_num;
	/** 天线尺寸 */
	private int wireless_size;
	private DeviceInfoQueryBIO bio;
	/** 逻辑ID */
	private String username;
	/** 宽带帐号 */
	private String user;
	/** 用户电话号码 */
	private String telephone;
	private Map session;
	private List<Map> list;
	/** 厂家 */
	private int vendor = -1;
	/** 设备型号 */
	private int device_model = -1;
	/** 硬件版本 */
	private String hard_version;
	/** 软件型号 */
	private String soft_version;
	/** 是否审核 1是经过审核,0未审核 */
	private int is_check = -1;
	/** 设备类型 1是e8-b,2是e8-c */
	private int rela_dev_type = -1;
	/** 设备版本ID */
	private String devicetype_id;
	private Map configMap;
	private String ajax;

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public Map getConfigMap()
	{
		return configMap;
	}

	public void setConfigMap(Map configMap)
	{
		this.configMap = configMap;
	}

	public String getDevicetype_id()
	{
		return devicetype_id;
	}

	public void setDevicetype_id(String devicetypeId)
	{
		devicetype_id = devicetypeId;
	}

	public List<Map> getList()
	{
		return list;
	}

	public void setList(List<Map> list)
	{
		this.list = list;
	}

	public int getVendor()
	{
		return vendor;
	}

	public void setVendor(int vendor)
	{
		this.vendor = vendor;
	}

	public int getDevice_model()
	{
		return device_model;
	}

	public void setDevice_model(int deviceModel)
	{
		device_model = deviceModel;
	}

	public String getHard_version()
	{
		return hard_version;
	}

	public void setHard_version(String hardVersion)
	{
		hard_version = hardVersion;
	}

	public String getSoft_version()
	{
		return soft_version;
	}

	public void setSoft_version(String softVersion)
	{
		soft_version = softVersion;
	}

	public int getIs_check()
	{
		return is_check;
	}

	public void setIs_check(int isCheck)
	{
		is_check = isCheck;
	}

	public int getRela_dev_type()
	{
		return rela_dev_type;
	}

	public void setRela_dev_type(int relaDevType)
	{
		rela_dev_type = relaDevType;
	}

	public String execute()
	{
		return "success";
	}

	public String queryList()
	{
		deviceList = bio.queryDeviceList(username, user, telephone, curPage_splitPage,
				num_splitPage);
		
		maxPage_splitPage = bio.getMaxPage_splitPage();
		hgwcustServInfoList=  bio.getHgwcustServInfo(username, user, telephone);
		
		return "queryList";
	}

	// 查询设备版本配置信息,先查询设备
	public String queryDeviceList()
	{
		logger.debug("into queryDeviceList()");
		return "query";
	}

	public String query()
	{
		logger.debug("into queryList()");
		list = bio.queryList(vendor, device_model, hard_version, soft_version, is_check,
				rela_dev_type, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "list";
	}

	// 查询设备版本配置
	public String queryConfig()
	{
		configMap = bio.queryConfig(devicetype_id);
		logger.warn("into queryConfig()");
		return "config";
	}

	// public String queryDeviceTypeConfig()
	// {
	// deviceTypeConfigList = bio.queryDeviceTypeConfigList(curPage_splitPage,
	// num_splitPage);
	// maxPage_splitPage = bio.getMaxPage_splitPage();
	// return "list";
	// }
	// 添加设备版本配置信息
	public String add()
	{
		int result = bio.addDeviceTypeConfig(devicetype_id, getAccess_type(),
				device_type, is_card_apart, wan_name, wan_num, wan_can, lan_name,
				lan_num, lan_can, wlan_name, wlan_num, wlan_can, voip_name, voip_num,
				voip_can, voip_protocol, wireless_type, wireless_num, wireless_size);
		if (result > 0)
		{
			ajax = "1";
		}
		else
		{
			ajax = "0";
		}
		return "ajax";
	}

	public int getAccess_type()
	{
		return access_type;
	}

	public void setAccess_type(int accessType)
	{
		access_type = accessType;
	}

	public int getDevice_type()
	{
		return device_type;
	}

	public void setDevice_type(int deviceType)
	{
		device_type = deviceType;
	}

	public int getIs_card_apart()
	{
		return is_card_apart;
	}

	public void setIs_card_apart(int isCardApart)
	{
		is_card_apart = isCardApart;
	}

	public int getVoip_protocol()
	{
		return voip_protocol;
	}

	public void setVoip_protocol(int voipProtocol)
	{
		voip_protocol = voipProtocol;
	}

	public int getWireless_type()
	{
		return wireless_type;
	}

	public void setWireless_type(int wirelessType)
	{
		wireless_type = wirelessType;
	}

	public String deviceTypeConfig()
	{
		return "query";
	}

	public List<Map> getDeviceTypeConfigList()
	{
		return deviceTypeConfigList;
	}

	public void setDeviceTypeConfigList(List<Map> deviceTypeConfigList)
	{
		this.deviceTypeConfigList = deviceTypeConfigList;
	}

	public List<Map> getDeviceList()
	{
		return deviceList;
	}

	public void setDeviceList(List<Map> deviceList)
	{
		this.deviceList = deviceList;
	}

	public List getHgwcustServInfoList()
	{
		return hgwcustServInfoList;
	}

	
	public void setHgwcustServInfoList(List hgwcustServInfoList)
	{
		this.hgwcustServInfoList = hgwcustServInfoList;
	}
	
	
	
	
	public DeviceInfoQueryBIO getBio()
	{
		return bio;
	}

	public void setBio(DeviceInfoQueryBIO bio)
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

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public String getTelephone()
	{
		return telephone;
	}

	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public String getWan_name()
	{
		return wan_name;
	}

	public void setWan_name(String wanName)
	{
		wan_name = wanName;
	}

	public int getWan_num()
	{
		return wan_num;
	}

	public void setWan_num(int wanNum)
	{
		wan_num = wanNum;
	}

	public String getWan_can(String wan_can)
	{
		return wan_can;
	}

	public void setWan_can(String wanCan)
	{
		try
		{
			this.wan_can = java.net.URLDecoder.decode(wanCan, "UTF-8");
		}
		catch (Exception e)
		{
			this.wan_can = wan_can;
		}
	}

	public String getLan_name()
	{
		return lan_name;
	}

	public void setLan_name(String lanName)
	{
		lan_name = lanName;
	}

	public int getLan_num()
	{
		return lan_num;
	}

	public void setLan_num(int lanNum)
	{
		lan_num = lanNum;
	}

	public String getLan_can()
	{
		return lan_can;
	}

	public void setLan_can(String lanCan)
	{
		try
		{
			this.lan_can = java.net.URLDecoder.decode(lanCan, "UTF-8");
		}
		catch (Exception e)
		{
			this.lan_can = lan_can;
		}
	}

	public String getWlan_name()
	{
		return wlan_name;
	}

	public void setWlan_name(String wlanName)
	{
		wlan_name = wlanName;
	}

	public int getWlan_num()
	{
		return wlan_num;
	}

	public void setWlan_num(int wlanNum)
	{
		wlan_num = wlanNum;
	}

	public String getWlan_can()
	{
		return wlan_can;
	}

	public void setWlan_can(String wlanCan)
	{
		try
		{
			this.wlan_can = java.net.URLDecoder.decode(wlanCan, "UTF-8");
		}
		catch (Exception e)
		{
			this.wlan_can = wlan_can;
		}
	}

	public String getVoip_name()
	{
		return voip_name;
	}

	public void setVoip_name(String voipName)
	{
		voip_name = voipName;
	}

	public int getVoip_num()
	{
		return voip_num;
	}

	public void setVoip_num(int voipNum)
	{
		voip_num = voipNum;
	}

	public String getVoip_can()
	{
		return voip_can;
	}

	public void setVoip_can(String voipCan)
	{
		try
		{
			this.voip_can = java.net.URLDecoder.decode(voipCan, "UTF-8");
		}
		catch (Exception e)
		{
			this.voip_can = voip_can;
		}
	}

	public int getWireless_num()
	{
		return wireless_num;
	}

	public void setWireless_num(int wirelessNum)
	{
		wireless_num = wirelessNum;
	}

	public int getWireless_size()
	{
		return wireless_size;
	}

	public void setWireless_size(int wirelessSize)
	{
		wireless_size = wirelessSize;
	}
}
