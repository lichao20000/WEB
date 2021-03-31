
package com.linkage.module.gtms.stb.config.bio;

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

import com.ailk.tr069.devrpc.dao.corba.AcsCorbaDAO;
import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.util.StringUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.module.gtms.stb.config.dao.BaseConfigDAO;
import com.linkage.module.gtms.stb.config.dao.StrategyDAO;
import com.linkage.module.gtms.stb.utils.StbStatics;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

public class BaseConfigBIO
{

	private static Logger logger = LoggerFactory.getLogger(BaseConfigBIO.class);
	private BaseConfigDAO dao;
	private StrategyDAO strategyDAO;
	private String errorMessage;

	public Map getLAN(String deviceId)
	{
		logger.debug("getLAN({})", deviceId);
		int iret = new SuperGatherCorba().getCpeParams(deviceId, StbStatics.GATHER_LAN);
		if (iret == 1)
		{
			Map map = dao.getLAN(deviceId);
			return map;
		}
		else
		{
			logger.error("采集LAN失败");
			errorMessage = Global.G_Fault_Map.get(iret).getFaultReason();
			return null;
		}
	}

	/**
	 * 获取最新记录表数据
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map getLANRecent(String deviceId)
	{
		logger.debug("getLANRecent({})", deviceId);
		return dao.getLANRecent(deviceId);
	}

	public Map getCmpstVideoAndAspRatio(String deviceId)
	{
		logger.debug("getCmpstVideoAndAspRatio({})", deviceId);
		int iret = new SuperGatherCorba().getCpeParams(deviceId,
				StbStatics.GATHER_Capabilities);
		if (iret == 1)
		{
			Map map = dao.getCmpstVideoAndAspRatio(deviceId);
			return map;
		}
		else
		{
			logger.error("采集视频输出制式和屏显配置信息失败");
			errorMessage = Global.G_Fault_Map.get(iret).getFaultReason();
			return null;
		}
	}

	public Map getCmpstVideoAndAspRatioRecent(String deviceId)
	{
		logger.debug("getCmpstVideoAndAspRatioRecent({})", deviceId);
		return dao.getCmpstVideoAndAspRatioRecent(deviceId);
	}

	public Map getX_CTC_IPTV_ServiceInfo(String deviceId)
	{
		logger.debug("getX_CTC_IPTV_ServiceInfo({})", deviceId);
		int iret = new SuperGatherCorba().getCpeParams(deviceId,
				StbStatics.GATHER_X_CTC_IPTV_ServiceInfo);
		if (iret == 1)
		{
			Map map = dao.getX_CTC_IPTV_ServiceInfo(deviceId);
			return map;
		}
		else
		{
			logger.error("采集X_CTC_IPTV.ServiceInfo失败");
			errorMessage = Global.G_Fault_Map.get(iret).getFaultReason();
			return null;
		}
	}

	/**
	 * 获取最新记录表数据
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map getX_CTC_IPTV_ServiceInfoRecent(String deviceId)
	{
		logger.debug("getX_CTC_IPTV_ServiceInfoRecent({})", deviceId);
		return dao.getX_CTC_IPTV_ServiceInfoRecent(deviceId);
	}

	public Map getUserInterface(String deviceId)
	{
		logger.debug("getUserInterface({})", deviceId);
		int iret = new SuperGatherCorba().getCpeParams(deviceId,
				StbStatics.GATHER_UserInterface);
		Map map = new HashMap<String, String>();
		if (iret == 1)
		{
			map = dao.getUserInterface(deviceId);
			return map;
		}
		else
		{
			logger.error("采集UserInterface失败");
			errorMessage = Global.G_Fault_Map.get(iret).getFaultReason();
			return null;
		}
	}

	/**
	 * 获取最新记录表数据
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map getUserInterfaceRecent(String deviceId)
	{
		logger.debug("getUserInterfaceRecent({})", deviceId);
		return dao.getUserInterfaceRecent(deviceId);
	}

	/**
	 * @return the dao
	 */
	public BaseConfigDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(BaseConfigDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String baseConfig(String PPPoEID, String PPPoEIDbk, String PPPoEPassword,
			String PPPoEPasswordbk, String DHCPID, String DHCPIDbk, String DHCPPassword,
			String DHCPPasswordbk, String IPAddress, String IPAddressbk,
			String subnetMask, String subnetMaskbk, String defaultGateway,
			String defaultGatewaybk, String DNSServers, String DNSServersbk,
			String userID, String userIDbk, String userPassword, String userPasswordbk,
			String authURL, String authURLbk, String autoUpdateServer,
			String autoUpdateServerbk, String addressingType, String addressingTypebk,
			long accoid, String deviceId, String gw_type)
	{
		// 获取配置参数(XML)字符串
		String strategyXmlParam = baseConfigXml(PPPoEID, PPPoEIDbk, PPPoEPassword,
				PPPoEPasswordbk, DHCPID, DHCPIDbk, DHCPPassword, DHCPPasswordbk,
				IPAddress, IPAddressbk, subnetMask, subnetMaskbk, defaultGateway,
				defaultGatewaybk, DNSServers, DNSServersbk, userID, userIDbk,
				userPassword, userPasswordbk, authURL, authURLbk, autoUpdateServer,
				autoUpdateServerbk, addressingType, addressingTypebk);
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		// 配置的service_id
		int serviceId = 5001;
		StrategyOBJ strategyObj = new StrategyOBJ();
		// 策略ID
		strategyObj.createId();
		logger.warn("策略ID:" + strategyObj.getId());
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
		if (strategyDAO.addStrategy(strategyObj))
		{
			// 调用预读
			if (true == CreateObjectFactory.createPreProcess(gw_type).processOOBatch(String
					.valueOf(strategyObj.getId())))
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

	/**
	 * 视频输出制式和屏显配置
	 * 
	 * @param aspectRatio
	 * @param compositeVideo
	 * @param accoid
	 * @param deviceId
	 * @return
	 */
	public String configCmpstVideoAndAspRatio(String aspectRatio, String compositeVideo,
			long accoid, String deviceId, String spModel, String spzsModel, String gw_type)
	{
		// 获取配置参数(XML)字符串
		String strategyXmlParam = cmpstVideoAndAspRatioXml(aspectRatio, compositeVideo,
				spModel, spzsModel);
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		// 配置的service_id
		int serviceId = 6001;
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
		if (strategyDAO.addStrategy(strategyObj))
		{
			// 调用预读
			if (true == CreateObjectFactory.createPreProcess(gw_type).processOOBatch(String
					.valueOf(strategyObj.getId())))
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

	/**
	 * @param aspectRatio
	 * @param compositeVideo
	 * @return
	 */
	public String cmpstVideoAndAspRatioXml(String aspectRatio, String compositeVideo,
			String spModel, String spzsModel)
	{
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("STB");
		Element CompositeVideoStandard = root.addElement("CompositeVideoStandard");
		CompositeVideoStandard.addAttribute("flag", spzsModel);
		CompositeVideoStandard.addText(compositeVideo);
		Element AspectRatio = root.addElement("AspectRatio");
		AspectRatio.addAttribute("flag", spModel);
		AspectRatio.addText(aspectRatio);
		strXml = doc.asXML();
		return strXml;
	}

	/**
	 * @author wangsenbo
	 * @date Nov 22, 2010
	 * @param
	 * @return String
	 */
	public String baseConfigXml(String PPPoEID, String PPPoEIDbk, String PPPoEPassword,
			String PPPoEPasswordbk, String DHCPID, String DHCPIDbk, String DHCPPassword,
			String DHCPPasswordbk, String IPAddress, String IPAddressbk,
			String subnetMask, String subnetMaskbk, String defaultGateway,
			String defaultGatewaybk, String DNSServers, String DNSServersbk,
			String userID, String userIDbk, String userPassword, String userPasswordbk,
			String authURL, String authURLbk, String autoUpdateServer,
			String autoUpdateServerbk, String addressingType, String addressingTypebk)
	{
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: STB
		Element root = doc.addElement("STB");
		Element LAN = root.addElement("LAN");
		if (isEquals(IPAddress, IPAddressbk) && isEquals(subnetMask, subnetMaskbk)
				&& isEquals(defaultGateway, defaultGatewaybk)
				&& isEquals(DNSServers, DNSServersbk)
				&& !isEquals("Static", addressingType)) // added 2012年6月21日 当接入方式不是Static
														// Ip 时置为0
		{
			LAN.addAttribute("flag", "0");
		}
		else
		{
			LAN.addAttribute("flag", "1");
		}
		LAN.addElement("IPAddress").addText(StringUtil.getStringValue(IPAddress));
		LAN.addElement("SubnetMask").addText(StringUtil.getStringValue(subnetMask));
		LAN.addElement("DefaultGateway").addText(
				StringUtil.getStringValue(defaultGateway));
		LAN.addElement("DNSServers").addText(StringUtil.getStringValue(DNSServers));
		Element AddressingType = root.addElement("AddressingType");
		// 无论修改与否，都要下发。2012年7月6日
		/**
		 * if (isEquals(addressingType, addressingTypebk)) {
		 * AddressingType.addAttribute("flag", "0"); } else {
		 **/
		AddressingType.addAttribute("flag", "1");
		// }
		AddressingType.addText(StringUtil.getStringValue(addressingType));
		Element ServiceInfo = root.addElement("ServiceInfo");
		if (isEquals(userID, userIDbk) && isEquals(userPassword, userPasswordbk)
				&& isEquals(authURL, authURLbk))
		{
			ServiceInfo.addAttribute("flag", "0");
		}
		else
		{
			ServiceInfo.addAttribute("flag", "1");
		}
		ServiceInfo.addElement("UserID").addText(StringUtil.getStringValue(userID));
		ServiceInfo.addElement("UserPassword").addText(
				StringUtil.getStringValue(userPassword));
		ServiceInfo.addElement("AuthURL").addText(StringUtil.getStringValue(authURL));
		Element ServiceInfoppp = root.addElement("ServiceInfo-ppp");
		/**
		 * if (isEquals(PPPoEID, PPPoEIDbk) && isEquals(PPPoEPassword, PPPoEPasswordbk)) {
		 * ServiceInfoppp.addAttribute("flag", "0"); } else {
		 **/
		ServiceInfoppp.addAttribute("flag", "1");
		// }
		ServiceInfoppp.addElement("PPPoEID").addText(StringUtil.getStringValue(PPPoEID));
		ServiceInfoppp.addElement("PPPoEPassword").addText(
				StringUtil.getStringValue(PPPoEPassword));
		Element ServiceInfodhcp = root.addElement("ServiceInfo-dhcp");
		/**
		 * if (isEquals(DHCPID, DHCPIDbk) && isEquals(DHCPPassword, DHCPPasswordbk)) {
		 * ServiceInfodhcp.addAttribute("flag", "0"); } else {
		 **/
		ServiceInfodhcp.addAttribute("flag", "1");
		// }
		ServiceInfodhcp.addElement("DHCPUsername").addText(
				StringUtil.getStringValue(DHCPID));
		ServiceInfodhcp.addElement("DHCPPassword").addText(
				StringUtil.getStringValue(DHCPPassword));
		Element UserInterface = root.addElement("UserInterface");
		if (isEquals(autoUpdateServer, autoUpdateServerbk))
		{
			UserInterface.addAttribute("flag", "0");
		}
		else
		{
			UserInterface.addAttribute("flag", "1");
		}
		UserInterface.addElement("AutoUpdateServer").addText(
				StringUtil.getStringValue(autoUpdateServer));
		strXml = doc.asXML();
		return strXml;
	}

	/**
	 * 比较两个String是否相等
	 * 
	 * @author wangsenbo
	 * @date Nov 22, 2010
	 * @param
	 * @return boolean
	 */
	private boolean isEquals(String string1, String string2)
	{
		return StringUtil.IsEmpty(string1) ? StringUtil.IsEmpty(string2) : string1
				.equals(string2);
	}

	public List<DevRpcCmdOBJ> resPass(String deviceId, String newPassword, String gw_type)
	{
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[1];
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0].setName("Device.X_CTC_IPTV.ServiceInfo.UserPassword");
		AnyObject anyObject = new AnyObject();
		anyObject.para_value = newPassword;
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("resPass");
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = deviceId;
		Rpc rpc1 = new Rpc();
		rpc1.rpcId = "1";
		rpc1.rpcName = "SetParameterValues";
		rpc1.rpcValue = setParameterValues.toRPC();
		devRPCArr[0].rpcArr = new Rpc[] { rpc1 };
		List<DevRpcCmdOBJ> list = new AcsCorbaDAO(Global.getPrefixName(gw_type)
				+ Global.SYSTEM_ACS).execRPC(LipossGlobals.getClientId(),
				Global.DiagCmd_Type, Global.Priority_Hig, devRPCArr);
		return list;
	}

	public String getStrategyById(String strategyId)
	{
		Map<String, String> map = strategyDAO.getStrategyById(strategyId);
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
			if ("等待执行".equals(resultmessage) || "正在执行".equals(resultmessage))
			{
				return "0;" + resultmessage;
			}
			else
			{
				return "-1" + ";配置失败：" + resultmessage;
			}
		}
	}

	public void setStrategyDAO(StrategyDAO strategyDAO)
	{
		this.strategyDAO = strategyDAO;
	}
}
