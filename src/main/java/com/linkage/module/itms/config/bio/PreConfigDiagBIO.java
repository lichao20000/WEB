
package com.linkage.module.itms.config.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.util.StringUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;
import com.linkage.module.itms.config.dao.PreConfigDiagDAO;

@SuppressWarnings("unchecked")
public class PreConfigDiagBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(PreConfigDiagBIO.class);
	private PreConfigDiagDAO dao;
	private String wideNetMsg = "1";
	private String iptvMsg = "1";
	private String voipMsg = "1";
	private String qosMsg = "1";
	private Map qosmap;
	private List internetlist;
	private List iptvlist;
	private List voipdevicelist;
	private List voipPVClist;
	
    /**
     * VOIP协议类型0-IMS SIP,1-软交换 SIP,2-H.248
     */
    private String voipProtocalTypeStr;
    
    /**
     * ACS Corba
     */
    private ACSCorba acsCorba = new ACSCorba();
    
	private String accessType;
	private String igmp;
	private String proxy;
	private String snooping;
	//private Map<String, String> bindPortMap = new HashMap<String, String>();
	/*
	private String LAN1 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1";
	private String LAN2 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2";
	private String LAN3 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3";
	private String LAN4 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4";
	private String WLAN1 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.1";
	private String WLAN2 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.2";
	private String WLAN3 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.3";
	private String WLAN4 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.4";
	*/

	public PreConfigDiagBIO()
	{
		/* remove by chenjie 2012-3-28
		 * bindPortMap统一使用Global类里面的
		 * 
		bindPortMap.put("LAN1", LAN1);
		bindPortMap.put("LAN2", LAN2);
		bindPortMap.put("LAN3", LAN3);
		bindPortMap.put("LAN4", LAN4);
		bindPortMap.put("WLAN1", WLAN1);
		bindPortMap.put("WLAN2", WLAN2);
		bindPortMap.put("WLAN3", WLAN3);
		bindPortMap.put("WLAN4", WLAN4);
		bindPortMap.put("SSID2", WLAN2);
		bindPortMap.put(LAN1, "LAN1");
		bindPortMap.put(LAN2, "LAN2");
		bindPortMap.put(LAN3, "LAN3");
		bindPortMap.put(LAN4, "LAN4");
		bindPortMap.put(WLAN1, "WLAN1");
		bindPortMap.put(WLAN2, "WLAN2");
		bindPortMap.put(WLAN3, "WLAN3");
		bindPortMap.put(WLAN4, "WLAN4");
		*/
	}

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
	 * 基础信息
	 * 
	 * @author wangsenbo
	 * @date Nov 23, 2010
	 * @param
	 * @return Map
	 */
	public Map baseInfo(String deviceId, UserRes curUser, String gw_type)
	{
		Map devicemap = null;
		
		if(Global.GW_TYPE_BBMS.equals(gw_type)){
			devicemap = dao.getDeviceInfoByEgw(deviceId);
		}else{
			devicemap = dao.getDeviceInfo(deviceId);
		}

		Map map = new HashMap();
		String onlinestatus = testConnection(deviceId, curUser);
		map.put("onlinestatus", onlinestatus);
		map.put("username", devicemap.get("username"));
		map.put("deviceSN", devicemap.get("device_serialnumber"));
		String userline = String.valueOf(devicemap.get("userline"));
		if ("5".equals(userline)||"6".equals(userline)||"7".equals(userline))
		{
			map.put("userline", "是");
		}
		else
		{
			map.put("userline", "不是");
		}
		map.put("last_time", devicemap.get("last_time"));
		long last_time = StringUtil.getLongValue(map.get("last_time"));
		DateTimeUtil dt = new DateTimeUtil();
		long nowtime = dt.getLongTime();
		if (last_time >= (nowtime - 30 * 24 * 3600))
		{
			map.put("huoyue", "活跃");
		}
		else
		{
			map.put("huoyue", "不活跃");
		}
		return map;
	}

	/**
	 * 测试设备在线情况
	 * 
	 * @author wangsenbo
	 * @date Nov 23, 2010
	 * @param
	 * @return String
	 */
	public String testConnection(String deviceId, UserRes curUser)
	{
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "";
		rpcArr[0].rpcValue = "";
		devRPCArr[0].rpcArr = rpcArr;
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(deviceId);

		// corba
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.RpcTest_Type);
		int flag = 0;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", deviceId);
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", deviceId);
		}
		else
		{
			flag = devRPCRep.get(0).getStat();
		}
		logger.warn("+++++++" + flag);
		if (flag == 1)
		{
			//return "在线";
			return "设备与平台能正常交互";
		}
		else
		{
			//return "不在线";
			return  "设备与平台不能正常交互";
		}
	}

	/**
	 * @return the dao
	 */
	public PreConfigDiagDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(PreConfigDiagDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * 诊断预配置
	 * 
	 * modify by zhangchy 2011-10-31 增加了备用服务器地址，备用服务器端口校验
	 * 
	 * @author wangsenbo
	 * @param string
	 * @date Nov 23, 2010
	 * @param
	 * @return void
	 */
	public void preConfigDiag(String deviceId, String cityId, String shortName, String gw_type)
	{
		String parecityid = CityDAO.getLocationCityIdByCityId(cityId);
		accessType = dao.getAccessType(deviceId, 1);
		if (StringUtil.IsEmpty(accessType))
		{
			logger.error("预配置诊断:设备没有上行方式！！");
		}
		Map<String, List<Map>> preconfmap = dao.getPreConfInfo(parecityid, shortName,
				accessType);
		internetlist = dao.getInternet(deviceId);
		iptvlist = dao.getIPTV(deviceId);
		logger.warn("qixueqi:" + iptvlist.size());
		
		Map<String, String> map = dao.getIGMP(deviceId);
		igmp = map.get("igmp_enab");
		proxy = map.get("proxy_enable");
		snooping = map.get("snooping_enable");
		
		//VOIP支持IMS
		String voipProtocalType = null;
		voipdevicelist = null;
		voipPVClist = null;
		//获取设备的VOIP协议类型
        //InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.X_CT-COM_ServerType
		acsCorba = new ACSCorba(gw_type);
        ArrayList<ParameValueOBJ> result = acsCorba.getValue(deviceId, "InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.X_CT-COM_ServerType");
        if(null == result)
        {
            //获取设备绑定用户的VOIP的开通协议类型(暂时不处理H.248的情况)
            String voipProtocal = getBssVoipSheetProtocalByDeviceId(deviceId);
            if(null != voipProtocal && !"".equals(voipProtocal.trim()))
            {
                voipProtocalType = voipProtocal;
            }
        }else
        {
            voipProtocalType = result.get(0).getValue();
        }
        
      //无法获取，则显示无法获取VOIP信息
        boolean needToGatherVoip = false;
        if(null == voipProtocalType)
        {
            this.voipMsg = "设备没有绑定用户或者绑定的用户没有开通VOIP业务";
        }else
        {
            // 转换显示信息
            switch (Integer.parseInt(voipProtocalType))
            {
                case 0:
                {
                    voipProtocalTypeStr = "IMS SIP";
                    break;
                }
                case 1:
                {
                    voipProtocalTypeStr = "软交换 SIP";
                    break;
                }
                case 2:
                {
                    voipProtocalTypeStr = "H.248";
                    break;
                }
            }
            

            voipdevicelist = dao.getDeviceVOIP(deviceId);
            voipPVClist = dao.getPVCVOIP(deviceId);
            needToGatherVoip = true;
        }
		
		
		String userId = dao.getUserByDevice(deviceId);
		if ("DSL".equals(accessType))
		{
			// 宽带上网预配置
			logger.warn("DSL宽带上网预配置");
			List<Map> intPreList = preconfmap.get("INTERNET");
			for (int i = 0; i < intPreList.size(); i++)
			{
				boolean falg = false;
				Map tmap = intPreList.get(i);
				if (internetlist.isEmpty())
				{
					wideNetMsg = "0";
				}
				else
				{
					for (int j = 0; j < internetlist.size(); j++)
					{
						Map rmap = (Map) internetlist.get(j);
						if (StringUtil.getStringValue(tmap.get("vpi_id")).equals(
								StringUtil.getStringValue(rmap.get("vpi_id")))
								&& StringUtil.getStringValue(tmap.get("vci_id")).equals(
										StringUtil.getStringValue(rmap.get("vci_id"))))
						{
							falg = true;
							break;
						}
					}
					if (!falg)
					{
						wideNetMsg = "0";
						break;
					}
				}
			}
			// IPTV预配置
			logger.warn("DSLIPTV预配置");
			List<Map> iptvPreList = preconfmap.get("IPTV");
			for (int i = 0; i < iptvPreList.size(); i++)
			{
				boolean falg = false;
				Map tmap = iptvPreList.get(i);
				String[] iptvprebindport = StringUtil.getStringValue(
						tmap.get("bind_port")).split(",");
				if (iptvlist.isEmpty())
				{
					iptvMsg = "0";
				}
				else
				{
					for (int j = 0; j < iptvlist.size(); j++)
					{
						Map rmap = (Map) iptvlist.get(j);
						if (StringUtil.getStringValue(tmap.get("vpi_id")).equals(
								StringUtil.getStringValue(rmap.get("vpi_id")))
								&& StringUtil.getStringValue(tmap.get("vci_id")).equals(
										StringUtil.getStringValue(rmap.get("vci_id"))))
						{
							// String[] iptvbindport =
							// StringUtil.getStringValue(rmap.get("bind_port")).split(",");
							String iptvbindportStr = StringUtil.getStringValue(rmap
									.get("bind_port"));
							boolean falg2 = false;
							if (null == iptvbindportStr)
							{
								falg2 = false;
							}
							else
							{
								for (int k = 0; k < iptvprebindport.length; k++)
								{
									if (iptvbindportStr.indexOf(Global.bindPortMap
											.get(iptvprebindport[k])) > -1)
									{
										iptvbindportStr = iptvbindportStr.replace(
												Global.bindPortMap.get(iptvprebindport[k]),
												iptvprebindport[k]);
										rmap.put("bind_port", iptvbindportStr);
										falg2 = true;
									}
									else
									{
										falg2 = false;
										break;
									}
								}
							}
							if (!falg2)
							{
								iptvMsg = "0";
							}
							// for (int k = 0; k < iptvprebindport.length; k++)
							// {
							// boolean falg2 = false;
							// for (int k2 = 0; k2 < iptvbindport.length; k2++)
							// {
							// logger.warn("iptvprebindport"+iptvprebindport[k]);
							// logger.warn("iptvprebindport12"+);
							// if (bindPortMap.get(iptvprebindport[k]).equals(
							// iptvbindport[k2]))
							// {
							// falg2 = true;
							// }
							// }
							// if (falg2 == false)
							// {
							// iptvMsg = "0";
							// }
							// }
							falg = true;
							if ("0".equals(iptvMsg))
							{
								falg = false;
							}
							break;
						}
					}
					if (!falg)
					{
						iptvMsg = "0";
						break;
					}
				}
			}
//			if ("0".equals(igmp) && "1".equals(proxy) && "1".equals(snooping)){
			if ("1".equals(snooping)){
//				iptvMsg = "0";
//				iptvMsg = "1";
			}else {
				iptvMsg = "0";
			}

            // VOIP预配置
			if(needToGatherVoip)
            {
                logger.warn("DSLVOIP预配置");
                if (voipdevicelist.isEmpty())
                {
                    voipMsg = "设备没有VOIP。";
                }
                else
                {
                    if (!StringUtil.IsEmpty(userId))
                    {
                        List voipuserlist = dao.getUserVOIP(userId);
                        for (int i = 0; i < voipuserlist.size(); i++)
                        {
                            boolean falg = false;
                            Map tmap = (Map) voipuserlist.get(i);
                            for (int j = 0; j < voipdevicelist.size(); j++)
                            {
                                Map rmap = (Map) voipdevicelist.get(i);
                                if (StringUtil.getStringValue(
                                        tmap.get("voip_username")).equals(
                                        StringUtil.getStringValue(rmap
                                                .get("username")))
                                        && StringUtil.getStringValue(
                                                tmap.get("line_id")).equals(
                                                StringUtil.getStringValue(rmap
                                                        .get("line_id")))
                                        && StringUtil.getStringValue(
                                                tmap.get("prox_serv")).equals(
                                                StringUtil.getStringValue(rmap
                                                        .get("prox_serv")))
                                        && StringUtil.getStringValue(
                                                tmap.get("prox_port")).equals(
                                                StringUtil.getStringValue(rmap
                                                        .get("prox_port")))
                                        && StringUtil
                                                .getStringValue(
                                                        tmap.get("stand_prox_serv"))
                                                .equals(StringUtil.getStringValue(rmap
                                                        .get("prox_serv_2")))
                                        && StringUtil
                                                .getStringValue(
                                                        tmap.get("stand_prox_port"))
                                                .equals(StringUtil.getStringValue(rmap
                                                        .get("prox_port_2"))))
                                {
                                    falg = true;
                                    break;
                                }
                            }
                            if (!falg)
                            {
                                voipMsg = "0";
                                break;
                            }
                        }
                        List<Map> voipPreList = preconfmap.get("VOIP");
                        Map emap = voipPreList.get(0);
                        boolean falg1 = false;
                        for (int i = 0; i < voipPVClist.size(); i++)
                        {
                            Map vmap = (Map) voipPVClist.get(i);
                            if (StringUtil.getStringValue(emap.get("vpi_id"))
                                    .equals(StringUtil.getStringValue(vmap
                                            .get("vpi_id")))
                                    && StringUtil.getStringValue(
                                            emap.get("vci_id")).equals(
                                            StringUtil.getStringValue(vmap
                                                    .get("vci_id"))))
                            {
                                falg1 = true;
                            }
                        }
                        if (!falg1)
                        {
                            voipMsg = "0";
                        }
                    }
                    else
                    {
                        voipMsg = "未绑定用户。";
                    }
                }
			}
		}
		else
		{
			if (!StringUtil.IsEmpty(userId))
			{
				// PON上网业务预配置
				if ("PON".equals(accessType) || "EPON".equals(accessType)
						|| "GPON".equals(accessType))
				{
					HgwServUserObj internetServobj = dao.queryHgwcustServUserByDevId(
							StringUtil.getLongValue(userId), 10);
					if (internetServobj != null && internetServobj.getVlanid() != null)
					{
						boolean falg = false;
						if (internetlist.isEmpty())
						{
							wideNetMsg = "0";
						}
						else
						{
							for (int j = 0; j < internetlist.size(); j++)
							{
								Map rmap = (Map) internetlist.get(j);
								if (StringUtil.getStringValue(rmap.get("vlan_id"))
										.equals(internetServobj.getVlanid()))
								{
									falg = true;
									break;
								}
							}
							if (!falg)
							{
								wideNetMsg = "0";
							}
						}
					}
				}
			}
			// IPTV预配置
			logger.warn("IPTV预配置");
			List<Map> iptvPreList = preconfmap.get("IPTV");
			for (int i = 0; i < iptvPreList.size(); i++)
			{
				logger.warn("IPTV预配置1");
				boolean falg = false;
				Map tmap = iptvPreList.get(i);
				String[] iptvprebindport = StringUtil.getStringValue(
						tmap.get("bind_port")).split(",");
				for (int f = 0; f < iptvprebindport.length; f++)
				{
					iptvprebindport[f] = Global.bindPortMap.get(iptvprebindport[f]);
				}
				if (!StringUtil.IsEmpty(userId))
				{
					HgwServUserObj iptvServobj = dao.queryHgwcustServUserByDevId(
							StringUtil.getLongValue(userId), 11);
					if (iptvServobj != null && iptvServobj.getBindPort() != null
							&& !"".equals(iptvServobj.getBindPort()))
					{
						iptvprebindport = iptvServobj.getBindPort().split(",");
					}
				}
				if (iptvlist.isEmpty())
				{
					logger.warn("IPTV预配置2");
					iptvMsg = "0";
				}
				else
				{
					for (int j = 0; j < iptvlist.size(); j++)
					{
						logger.warn("IPTV预配置3");
						Map rmap = (Map) iptvlist.get(j);
						if (StringUtil.getStringValue(tmap.get("vlan_id")).equals(
								StringUtil.getStringValue(rmap.get("vlan_id"))))
						{
							// String[] iptvbindport =
							// StringUtil.getStringValue(rmap.get("bind_port")).split(",");
							logger.warn("IPTV预配置4");
							String iptvbindportStr = StringUtil.getStringValue(rmap
									.get("bind_port"));
							logger.warn("iptvbindportStr:" + iptvbindportStr);
							boolean falg2 = false;
							if (null == iptvbindportStr)
							{
								falg2 = false;
							}
							else
							{
								for (int k = 0; k < iptvprebindport.length; k++)
								{
									if (iptvbindportStr.indexOf(iptvprebindport[k]) > -1)
									{
										iptvbindportStr = iptvbindportStr.replace(
												iptvprebindport[k],
												Global.bindPortMap.get(iptvprebindport[k]));
										rmap.put("bind_port", iptvbindportStr);
										falg2 = true;
									}
									else
									{
										falg2 = false;
										break;
									}
								}
							}
							if (!falg2)
							{
								logger.warn("IPTV预配置5");
								iptvMsg = "0";
							}
							logger.warn("IPTV预配置8");
							// for (int k = 0; k < iptvprebindport.length; k++)
							// {
							// logger.warn("IPTV预配置9");
							// boolean falg2 = false;
							// for (int k2 = 0; k2 < iptvbindport.length; k2++)
							// {
							// if (bindPortMap.get(iptvprebindport[k]).equals(
							// iptvbindport[k2]))
							// {
							// logger.warn("IPTV预配置10");
							// falg2 = true;
							// }
							// }
							// if (falg2 == false)
							// {
							// iptvMsg = "0";
							// }
							// }
							falg = true;
							if ("0".equals(iptvMsg))
							{
								falg = false;
							}
							break;
						}
					}
					if (!falg)
					{
						logger.warn("IPTV预配置9");
						iptvMsg = "0";
						break;
					}
				}
			}
//			if ("0".equals(igmp) && "1".equals(proxy) && "1".equals(snooping)){
			if ("1".equals(snooping)){
////				iptvMsg = "0";
//				iptvMsg = "1";
			}else {
				logger.warn("IPTV预配置10");
				iptvMsg = "0";
			}
		}
		

        // VOIP预配置
        if (needToGatherVoip)
        {
            logger.debug("VOIP预配置1");
            if (voipdevicelist.isEmpty())
            {
                logger.debug("VOIP预配置2");
                voipMsg = "设备没有VOIP。";
            }
            else
            {
                logger.debug("VOIP预配置7");
                if (!StringUtil.IsEmpty(userId))
                {
                    logger.debug("VOIP预配置8");
                    List voipuserlist = dao.getUserVOIP(userId);
                    if (!voipuserlist.isEmpty())
                    {
                        logger.debug("VOIP预配置9");
                        for (int i = 0; i < voipuserlist.size(); i++)
                        {
                            boolean falg = false;
                            Map tmap = (Map) voipuserlist.get(i);
                            logger.debug("VOIP预配置10");
                            for (int j = 0; j < voipdevicelist.size(); j++)
                            {
                                Map rmap = (Map) voipdevicelist.get(i);
                                logger.debug("VOIP预配置11");
                                if (StringUtil.getStringValue(
                                        tmap.get("voip_username")).equals(
                                        StringUtil.getStringValue(rmap
                                                .get("username")))
                                        && StringUtil.getStringValue(
                                                tmap.get("line_id")).equals(
                                                StringUtil.getStringValue(rmap
                                                        .get("line_id")))
                                        && StringUtil.getStringValue(
                                                tmap.get("prox_serv")).equals(
                                                StringUtil.getStringValue(rmap
                                                        .get("prox_serv")))
                                        && StringUtil.getStringValue(
                                                tmap.get("prox_port")).equals(
                                                StringUtil.getStringValue(rmap
                                                        .get("prox_port")))
                                        && StringUtil
                                                .getStringValue(
                                                        tmap.get("stand_prox_serv"))
                                                .equals(StringUtil.getStringValue(rmap
                                                        .get("prox_serv_2")))
                                        && StringUtil
                                                .getStringValue(
                                                        tmap.get("stand_prox_port"))
                                                .equals(StringUtil.getStringValue(rmap
                                                        .get("prox_port_2"))))
                                {
                                    falg = true;
                                    break;
                                }
                            }
                            if (!falg)
                            {
                                voipMsg = "0";
                                break;
                            }
                        }
                        List<Map> voipPreList = preconfmap.get("VOIP");
                        logger.debug("VOIP预配置3");
                        Map emap = voipPreList.get(0);
                        logger.debug("VOIP预配置4" + emap.get("vlan_id"));
                        boolean falg1 = false;
                        for (int i = 0; i < voipPVClist.size(); i++)
                        {
                            Map vmap = (Map) voipPVClist.get(i);
                            if (StringUtil.getStringValue(emap.get("vlan_id"))
                                    .equals(StringUtil.getStringValue(vmap
                                            .get("vlan_id"))))
                            {
                                falg1 = true;
                            }
                        }
                        if (!falg1)
                        {
                            voipMsg = "0";
                        }
                    }
                    else
                    {
                        voipMsg = "用户没有VOIP业务。";
                    }
                }
                else
                {
                    voipMsg = "未绑定用户。";
                }
            }
        }
		// QOS预配置
		qosmap = dao.getQOS(deviceId);
		logger.warn("QOS预配置");
		if(qosmap == null)
		{
			qosMsg = "0";
		}
		else
		{
			if (null == voipdevicelist || voipdevicelist.isEmpty())
			{
				if (("INTERNET,TR069,IPTV".equals(StringUtil.getStringValue(
						qosmap.get("qos_mode")).trim()) || "INTERNET,TR069,IPTV,VOIP"
						.equals(StringUtil.getStringValue(qosmap.get("qos_mode")).trim()))
						&& "1".equals(StringUtil.getStringValue(qosmap.get("enable"))))
				{
				}
				else
				{
					qosMsg = "0";
				}
			}
			else
			{
				if (("INTERNET,TR069,IPTV,VOIP".equals(StringUtil.getStringValue(
						qosmap.get("qos_mode")).trim())||"INTERNET,TR069,VOIP,IPTV".equals(StringUtil.getStringValue(
								qosmap.get("qos_mode")).trim()))
						&& "1".equals(StringUtil.getStringValue(qosmap.get("enable"))))
				{
				}
				else
				{
					qosMsg = "0";
				}
			}
		}
		
	}
    
    /**
     * <p>
     * [获取设备绑定用户的VOIP的开通协议类型]
     * </p>
     * @param deviceId
     * @return
     */
    public String getBssVoipSheetProtocalByDeviceId(String deviceId)
    {
        String user_id = dao.getUserByDevice(deviceId);

        if (null != user_id && !"".equals(user_id.trim()))
        {
            List userInfo = dao.getBssVoipSheet(user_id);
            if (userInfo.size() > 0)
            {
                Map oneUserInfo = (Map) userInfo.get(0);
                return StringUtil.getStringValue(oneUserInfo.get("protocol"));
            }
        }

        return null;
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

	public String wideNetconfig(long accoid, String deviceId, String accessType2,
			String cityId, String shortName, String gw_type)
	{
		String parecityid = CityDAO.getLocationCityIdByCityId(cityId);
		List wideNetlist = dao.getPreConfInfo(parecityid, shortName, accessType2,
				"INTERNET");
		internetlist = dao.getInternet(deviceId);
		List addlist = new ArrayList();
		for (int i = 0; i < wideNetlist.size(); i++)
		{
			boolean falg = false;
			Map tmap = (Map) wideNetlist.get(i);
			if (internetlist.size() == 0)
			{
				addlist.addAll(wideNetlist);
			}
			else
			{
				for (int j = 0; j < internetlist.size(); j++)
				{
					Map rmap = (Map) internetlist.get(j);
					if (StringUtil.getStringValue(tmap.get("vpi_id")).equals(
							StringUtil.getStringValue(rmap.get("vpi_id")))
							&& StringUtil.getStringValue(tmap.get("vci_id")).equals(
									StringUtil.getStringValue(rmap.get("vci_id"))))
					{
						falg = true;
						break;
					}
				}
				if (falg == false)
				{
					addlist.add(tmap);
					break;
				}
			}
		}
		for (int i = 0; i < addlist.size(); i++)
		{
			Map dmap = (Map) addlist.get(i);
			String strategyXmlParam = wideNetconfigXml(dmap, accessType2);
			/** 入策略表，调预读 */
			// 立即执行
			int strategyType = 0;
			// 配置的service_id
			int serviceId = 1001;
			StrategyOBJ strategyObj = new StrategyOBJ();
			// 策略ID
			strategyObj.createId();
			// 策略配置时间
			strategyObj.setTime(TimeUtil.getCurrentTime());
			// 用户id
			strategyObj.setAccOid(accoid);
			// 立即执行
			strategyObj.setType(strategyType);
			// 设备ID
			strategyObj.setDeviceId(deviceId);
			// QOS serviceId
			strategyObj.setServiceId(serviceId);
			// 顺序,默认1
			strategyObj.setOrderId(1);
			// 工单类型: 新工单,工单参数为xml串的工单
			strategyObj.setSheetType(2);
			// 参数
			strategyObj.setSheetPara(strategyXmlParam);
			strategyObj.setTempId(serviceId);
			strategyObj.setIsLastOne(1);
			// 入策略表
			if (dao.addStrategy(strategyObj))
			{
				// 调用预读
				if (true == CreateObjectFactory.createPreProcess(gw_type).processOOBatch(String
						.valueOf(strategyObj.getId())))
				{
					logger.debug("成功");
				}
				else
				{
					logger.warn("调用预读失败");
					return -1 + ";调用后台失败";
				}
			}
			else
			{
				logger.warn("策略入库失败");
				return -1 + ";策略入库失败";
			}
		}
		return "1;配置成功";
	}

	private String wideNetconfigXml(Map dmap, String accessType2)
	{
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: STB
		Element root = doc.addElement("NET");
		Element WAN = root.addElement("WAN");
		if ("DSL".equals(accessType2))
		{
			WAN.addElement("AccessType").addText("1");
		}
		else if ("Ethernet".equals(accessType2))
		{
			WAN.addElement("AccessType").addText("2");
		}
		else if ("EPON".equals(accessType2))
		{
			WAN.addElement("AccessType").addText("3");
		}
		else
		{
			WAN.addElement("AccessType").addText("");
		}
		WAN.addElement("WanType").addText("1");
		WAN.addElement("Pvc").addText(
				"PVC:" + StringUtil.getStringValue(dmap.get("vpi_id")) + "/"
						+ StringUtil.getStringValue(dmap.get("vci_id")));
		WAN.addElement("BindPort").addText("");
		WAN.addElement("Username").addText("");
		WAN.addElement("Password").addText("");
		WAN.addElement("Mode").addText("");
		WAN.addElement("VlanId").addText(StringUtil.getStringValue(dmap.get("vlan_id")));
		WAN.addElement("Ip").addText("");
		WAN.addElement("Mask").addText("");
		WAN.addElement("Gateway").addText("");
		WAN.addElement("Dns").addText("");
		strXml = doc.asXML();
		return strXml;
	}

	public String iptvconfig(long accoid, String deviceId, String accessType2,
			String cityId, String shortName, String deviceserialnumber, String gw_type)
	{
		String parecityid = CityDAO.getLocationCityIdByCityId(cityId);
		List iptvlist = dao.getPreConfInfo(parecityid, shortName, accessType2, "IPTV");
		String strategyXmlParam = iptvconfigXml(iptvlist, deviceserialnumber);
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		// 配置的service_id
		int serviceId = 1101;
		StrategyOBJ strategyObj = new StrategyOBJ();
		// 策略ID
		strategyObj.createId();
		// 策略配置时间
		strategyObj.setTime(TimeUtil.getCurrentTime());
		// 用户id
		strategyObj.setAccOid(accoid);
		// 立即执行
		strategyObj.setType(strategyType);
		// 设备ID
		strategyObj.setDeviceId(deviceId);
		// QOS serviceId
		strategyObj.setServiceId(serviceId);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(strategyXmlParam);
		strategyObj.setTempId(serviceId);
		strategyObj.setIsLastOne(1);
		// 入策略表
		if (dao.addStrategy(strategyObj))
		{
			// 调用预读
			if (true == CreateObjectFactory.createPreProcess(gw_type).processOOBatch(String.valueOf(strategyObj
					.getId())))
			{
				logger.debug("成功");
				return 1 + ";调用后台成功;" + strategyObj.getId();
			}
			else
			{
				logger.warn("调用预读失败");
				return -1 + ";调用后台失败";
			}
		}
		else
		{
			logger.warn("策略入库失败");
			return -1 + ";策略入库失败";
		}
	}

	public String getStrategyById(String strategyId)
	{
		Map map = dao.getStrategyById(strategyId);
		String status = StringUtil.getStringValue(map.get("status"));
		String resultmessage = StringUtil.getStringValue(map.get("result"));
		if ("执行完成".equals(status))
		{
			if ("成功".equals(resultmessage))
			{
				return "1;配置成功";
			}
			else
			{
				return "-1;配置失败：" + resultmessage;
			}
		}
		else
		{
			return "0;" + resultmessage;
		}
	}

	private String iptvconfigXml(List iptvlist2, String deviceserialnumber)
	{
		Map map = (Map) iptvlist2.get(0);
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: STB
		
		//modify by zhangcong(67706) 依照漆学启要求，修改iTV的XML文件 2011-06-03
		Element root = doc.addElement("iTVs");
		Element Lan = root.addElement("Lan");
		Lan.addAttribute("flag", "1");
		//添加3个节点，内容为空
		Lan.addElement("i").addText("");
		Lan.addElement("j").addText("");
		Lan.addElement("k").addText("");
		
		Lan.addElement("WanType").addText("1");
		Lan.addElement("Username").addText("");
		Lan.addElement("Password").addText("");
		Lan.addElement("Pvc").addText(
				"PVC:" + StringUtil.getStringValue(map.get("vpi_id")) + "/"
						+ StringUtil.getStringValue(map.get("vci_id")));
		Lan.addElement("VlanId").addText(StringUtil.getStringValue(map.get("vlan_id")));
		Lan.addElement("Ip").addText("");
		Lan.addElement("Mask").addText("");
		Lan.addElement("Gateway").addText("");
		Lan.addElement("Dns").addText("");
		Element Wlan = root.addElement("Wlan");
		Wlan.addAttribute("flag", "1");
		Wlan.addElement("Ssid").addText(
				"iTV" + deviceserialnumber.substring(deviceserialnumber.length() - 5));
		Wlan.addElement("PreSharedKey").addText(
				deviceserialnumber.substring(deviceserialnumber.length() - 8));
		Element BindPort = root.addElement("BindPort");
		BindPort.addAttribute("flag", "1");
		logger.warn("map.get(bind_port)" + map.get("bind_port"));
		String[] bindport = StringUtil.getStringValue(map.get("bind_port")).split(",");
		String bind_port = "";
		for (int i = 0; i < bindport.length; i++)
		{
			bind_port = bind_port + Global.bindPortMap.get(bindport[i]) + ",";
		}
		BindPort.addText(bind_port.substring(0, bind_port.length() - 1));
		
		// add by zhangchy 2012-01-04 应现场需求添加以下三个节点(IGMPEnable, ProxyEnable, SnoopingEnable)
		Element IGMP = root.addElement("IGMP");
		IGMP.addAttribute("flag", "1");
		IGMP.addElement("IGMPEnable").addText("0");
		IGMP.addElement("ProxyEnable").addText("1");
		IGMP.addElement("SnoopingEnable").addText("1");
		
//		Element IGMPEnable = root.addElement("IGMPEnable");  // 注释 by zhangchy 2012-01-04
//		IGMPEnable.addAttribute("flag", "1");
//		IGMPEnable.addText("0");
		strXml = doc.asXML();
		logger.warn(strXml);
		return strXml;
	}

	public String qosconfig(long accoid, String deviceId, String gw_type)
	{
		String mode = null;
		if (dao.ishavevoip(deviceId))
		{
			mode = "INTERNET,TR069,IPTV,VOIP";
		}
		else
		{
			mode = "INTERNET,TR069,IPTV";
		}
		String strategyXmlParam = qosconfigXml(mode, "1");
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		// 配置的service_id
		int serviceId = 1301;
		StrategyOBJ strategyObj = new StrategyOBJ();
		// 策略ID
		strategyObj.createId();
		// 策略配置时间
		strategyObj.setTime(TimeUtil.getCurrentTime());
		// 用户id
		strategyObj.setAccOid(accoid);
		// 立即执行
		strategyObj.setType(strategyType);
		// 设备ID
		strategyObj.setDeviceId(deviceId);
		// QOS serviceId
		strategyObj.setServiceId(serviceId);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(strategyXmlParam);
		strategyObj.setTempId(serviceId);
		strategyObj.setIsLastOne(1);
		// 入策略表
		if (dao.addStrategy(strategyObj))
		{
			// 调用预读
			if (true == CreateObjectFactory.createPreProcess(gw_type).processOOBatch(String.valueOf(strategyObj
					.getId())))
			{
				logger.debug("成功");
				return 1 + ";调用后台成功;" + strategyObj.getId();
			}
			else
			{
				logger.warn("调用预读失败");
				return -1 + ";调用后台失败";
			}
		}
		else
		{
			logger.warn("策略入库失败");
			return -1 + ";策略入库失败";
		}
	}

	private String qosconfigXml(String mode, String enable)
	{
		String strXml = null;
		Document doc = DocumentHelper.createDocument();
		// root node: X_CT-COM_UplinkQoS
		Element root = doc.addElement("QoS");
		root.addAttribute("type", "0");
		root.addElement("Mode").addText(mode);
		root.addElement("Enable").addText(enable);
		strXml = doc.asXML();
		return strXml;
	}

	public String voipconfig(long accoid, String deviceId, String accessType2,
			String cityId, String shortName, String deviceserialnumber, String oui, String gw_type)
	{
		long userId = StringUtil.getLongValue(dao.getUserByDevice(deviceId));
		// 用户的业务信息
		HgwServUserObj hgwServUserObj = dao.queryHgwcustServUserByDevId(userId, 14);
		// 更新业务用户表的业务开通状态
		dao.updateServOpenStatus(userId, 14);
		// 预读调用对象
		PreServInfoOBJ preInfoObj = new PreServInfoOBJ(StringUtil
				.getStringValue(hgwServUserObj.getUserId()), deviceId, oui,
				deviceserialnumber, "14", "1");
		if (1 == CreateObjectFactory.createPreProcess(gw_type).processServiceInterface(CreateObjectFactory.createPreProcess()
				.GetPPBindUserList(preInfoObj)))
		{
			logger.debug("调用后台预读模块成功");
			return 1 + ";调用后台成功;" + StringUtil.getStringValue(hgwServUserObj.getUserId());
		}
		else
		{
			logger.warn("调用后台预读模块失败");
			return -1 + ";调用后台失败";
		}
	}

	public String getHgwcustServById(String userId)
	{
		HgwServUserObj hgwServUserObj = dao.queryHgwcustServUserByDevId(StringUtil
				.getLongValue(userId), 14);
		if ("1".equals(hgwServUserObj.getOpenStatus()))
		{
			return "1;配置成功";
		}
		else if ("-1".equals(hgwServUserObj.getOpenStatus()))
		{
			return "1;配置失败";
		}
		else
		{
			return "0";
		}
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

    public static void main(String[] args)
	{
		DateTimeUtil dt = new DateTimeUtil();
		long nowtime = dt.getLongTime();
		System.out.println(nowtime);
		if (1290737912 >= (nowtime - 30 * 24 * 3600))
		{
			System.out.println("活跃");
		}
		else
		{
			System.out.println("no活跃");
		}
	}

}