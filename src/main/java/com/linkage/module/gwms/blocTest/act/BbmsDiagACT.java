
package com.linkage.module.gwms.blocTest.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gwms.blocTest.bio.BbmsDiagBIO;

/**
 * @author 王森博
 */
public class BbmsDiagACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BbmsDiagACT.class);
	// session
	private Map session;
	/** 设备上上网业务信息 * */
	private List<Map<String, String>> deviceMapList = null;
	/** 业务用户上网业务信息 * */
	private Map<String, String> userMap = null;
	
	private String gw_type = null;
	
	
	/**
	 * 处理结果
	 */
	private Map<String,String> comparaResult = null;
	private String deviceId = null;
	private BbmsDiagBIO bio;

	public String wanServDiag()
	{
		boolean hasCompare = false;
		deviceMapList = bio.getDeviceWan(deviceId, gw_type);
		String access_type = bio.getAccessType(deviceId);
		userMap = bio.getUserServ(deviceId);
		logger.debug("access_type......." + access_type);
		
		boolean hasErrorMsg = true;
		
		if(null != deviceMapList && 1 == deviceMapList.size())
		{
			
			logger.debug("deviceMapList......." + deviceMapList.size());
			
			String errorMsg =  deviceMapList.get(0).get("corbaMsg");
			
			logger.debug("errorMsg......." + errorMsg);
			
			if(null == errorMsg || "".equals(errorMsg))
			{
				hasErrorMsg = false;
			}
		}
	
		
		//如果没有结果或者结果里面包含错误信息或者获取不到上行信息则不进行比对
		if(null != deviceMapList && (deviceMapList.size() != 0) && !hasErrorMsg)
		{
			if(null != access_type && !"".equals(access_type) && null != userMap)
			{
				//用户业务配置值
				String vpiid = trimString(String.valueOf(userMap.get("vpiid")));
				String vciid = trimString(String.valueOf(userMap.get("vciid")));
				String vlanid = trimString(String.valueOf(userMap.get("vlanid")));

				//尝试获取PVC或者VLANID
				for(Map<String,String> map:deviceMapList)
				{
					//DSL
					if("1".equals(access_type))
					{
						String vpi_id = trimString(String.valueOf(map.get("vpi_id")));
						String vci_id = trimString(String.valueOf(map.get("vci_id")));
						if(null != vpiid && null != vciid && vpiid.equals(vpi_id) && vciid.equals(vci_id))
						{
							//开始比较
							compare(map, userMap);
							hasCompare = true;
							break;
						}
					}else
					{
						//PON
						String vlan_id = trimString(String.valueOf(map.get("vlan_id")));
						if(null != vlanid && null != vlan_id && vlanid.equals(vlan_id))
						{
							//开始比较
							compare(map, userMap);
							hasCompare = true;
							break;
						}
					}
				}
				
			}
		}

		//没有匹配、则置异常
		if(!hasCompare)
		{
			logger.debug("hasCompare......." + hasCompare);
			comparaResult = new HashMap<String,String>();
			comparaResult.put("access_tag_compare", "0");
			comparaResult.put("username_compare", "0");
			comparaResult.put("ip_compare", "0");
			comparaResult.put("mask_compare", "0");
			comparaResult.put("gateway_compare", "0");
			comparaResult.put("dns_compare", "0");
		}
		
		return "wanServDiag";
	}
	
	private void compare(Map<String,String> cpeWanInfo,Map<String,String> userWanInfo)
	{
		
		comparaResult = new HashMap<String,String>();
		comparaResult.put("access_tag_compare", "1");
		
		//设备信息
		String cpeUserName = trimString(String.valueOf(cpeWanInfo.get("username")));
		String cpeIP = trimString(String.valueOf(cpeWanInfo.get("ip")));
		String cpeIPMask = trimString(String.valueOf(cpeWanInfo.get("mask")));
		String cpeGateWay = trimString(String.valueOf(cpeWanInfo.get("gateway")));
		String cpeDNS = trimString(String.valueOf(cpeWanInfo.get("dns")));

		//业务信息
		String userUserName = trimString(String.valueOf(userWanInfo.get("username")));
		String userIP = trimString(String.valueOf(userWanInfo.get("ip")));
		String userIPMask = trimString(String.valueOf(userWanInfo.get("mask")));
		String userGateWay = trimString(String.valueOf(userWanInfo.get("gateway")));
		String userDNS = trimString(String.valueOf(userWanInfo.get("dns")));
		
		String wan_type = trimString(String.valueOf(userWanInfo.get("wan_type")));
		boolean isStatic = "3".equals(wan_type)? true:false;
		
		//比较(如果是静态类型则直接ok，如果用户帐号为空则直接ok)
		if((isStatic) || (null == userUserName) || (null != cpeUserName && null != userUserName && cpeUserName.equals(userUserName)))
		{
			comparaResult.put("username_compare", "1");
		}else
		{
			comparaResult.put("username_compare", "0");
		}
		
		if((null == userIP) || (null != cpeIP && null != userIP && cpeIP.equals(userIP)))
		{
			comparaResult.put("ip_compare", "1");
		}else
		{
			comparaResult.put("ip_compare", "0");
		}
		
		if((null == userIPMask) || (null != cpeIPMask && null != userIPMask && cpeIPMask.equals(userIPMask)))
		{
			comparaResult.put("mask_compare", "1");
		}else
		{
			comparaResult.put("mask_compare", "0");
		}
		
		if((null == userGateWay) || (null != cpeGateWay && null != userGateWay && cpeGateWay.equals(userGateWay)))
		{
			comparaResult.put("gateway_compare", "1");
		}else
		{
			comparaResult.put("gateway_compare", "0");
		}
		
		if((null == userDNS) || (null != cpeDNS && cpeDNS.indexOf(userDNS) != -1))
		{
			comparaResult.put("dns_compare", "1");
		}else
		{
			comparaResult.put("dns_compare", "0");
		}
	}
	
	private String trimString(String str)
	{
		if(str == null || str.trim().equals("null") || str.trim().equals(""))
		{
			return null;
		}else
		{
			return str.trim();
		}
		
	}

	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}
	
	public Map<String, String> getComparaResult()
	{
		return comparaResult;
	}

	public void setComparaResult(Map<String, String> comparaResult)
	{
		this.comparaResult = comparaResult;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	public List<Map<String, String>> getDeviceMapList()
	{
		return deviceMapList;
	}

	public void setDeviceMapList(List<Map<String, String>> deviceMapList)
	{
		this.deviceMapList = deviceMapList;
	}

	/**
	 * @return the userMap
	 */
	public Map<String, String> getUserMap()
	{
		return userMap;
	}

	/**
	 * @param userMap
	 *            the userMap to set
	 */
	public void setUserMap(Map<String, String> userMap)
	{
		this.userMap = userMap;
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
	 * @return the bio
	 */
	public BbmsDiagBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(BbmsDiagBIO bio)
	{
		this.bio = bio;
	}

	
	public String getGw_type() {
		return gw_type;
	}

	
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
}
