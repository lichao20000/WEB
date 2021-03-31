package com.linkage.module.gwms.util.strategy;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.obj.gw.LanVlanOBJ;
import com.linkage.module.gwms.obj.gw.QoSAppOBJ;
import com.linkage.module.gwms.obj.gw.QoSClassificationOBJ;
import com.linkage.module.gwms.obj.gw.QoSClassificationTypeOBJ;
import com.linkage.module.gwms.obj.gw.QoSOBJ;
import com.linkage.module.gwms.obj.gw.TimeNtpOBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.gw.WlanOBJ;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.obj.tabquery.SgwSecurityOBJ;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.service.WanTypeUtil;

/**
 * @author Jason(3412)
 * @date 2009-7-22
 */
public class StrategyXml {

	private static Logger logger = LoggerFactory.getLogger(StrategyXml.class);
	
	
	
	/**
	 * Qos业务下发，入策略表的xml格式参数
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-7-22
	 * @return String
	 */
	public static String qosObj2Xml(QoSOBJ qosObj) {
		logger.debug("qosObj2Xml({})", qosObj);
		String strXml = null;
		if(null != qosObj){
			//new doc
			Document doc = DocumentHelper.createDocument();
			//root node: X_CT-COM_UplinkQoS
			Element root = doc.addElement("QoS");
			if(qosObj.getType() == 1){
				//使用老的Qos配置模板
				logger.debug("使用新的Qos配置方式");
				root.addAttribute("type", "1");
				root.addElement("Enable").addText(qosObj.getEnable());
				root.addElement("Plan").addText(qosObj.getPlan());
				root.addElement("EnableDSCPMark").addText(qosObj.getEnabDscp());
				root.addElement("Enable802-1_P").addText(qosObj.getEnab802p());
				//App
				QoSAppOBJ[] appObjArr = qosObj.getQosApp();
				if(null != appObjArr && appObjArr.length > 0){
					root.addElement("Mode").addText(qosObj.getMode());
					root.addElement("Bandwidth").addText(qosObj.getBandwidth());
					root.addElement("EnableForceWeight").addText(qosObj.getEnabWidth());
					int len = appObjArr.length;
					logger.debug("qosObj.AppArr length:" + len);
					for(int i = 0; i < len; i++){
						Element app = root.addElement("App");
						app.addAttribute("flag", "1");
						app.addElement("AppName").addText(appObjArr[i].getAppName());
						app.addElement("ClassQueue").addText(appObjArr[i].getQueueId());;
					}
				}else{
					logger.debug("qosObj.AppArr is empty");
					Element app = root.addElement("App");
					app.addAttribute("flag", "0");
				}
				//classification node
				QoSClassificationOBJ[] claObjArr = qosObj.getQosCalss();
				if(null != claObjArr && claObjArr.length > 0){
					int len = claObjArr.length;
					logger.debug("qosObj.ClassificationArr length:" + len);
					for(int i = 0; i < len; i++){
						Element clas = root.addElement("Classification");
						clas.addAttribute("flag", "1");
						clas.addElement("ClassQueue").addText(claObjArr[i].getQueueId());
						clas.addElement("DSCPMarkValue").addText(claObjArr[i].getValueDscp());
						clas.addElement("_802-1_P_Value").addText(claObjArr[i].getValue8021p());
						
						QoSClassificationTypeOBJ[] typeObjArr = claObjArr[i].getClassType();
						int typeLen = typeObjArr.length;
						logger.debug("qosObj.Classification.Type length:" + typeLen);
						for(int j = 0; j < typeLen; j++){
							//type node
							Element typ = clas.addElement("type");
							typ.addAttribute("flag", "1");
							typ.addElement("Type").addText(typeObjArr[j].getTypeName());
							typ.addElement("Max").addText(typeObjArr[j].getTypeMax());
							typ.addElement("Min").addText(typeObjArr[j].getTypeMin());
							typ.addElement("ProtocolList").addText(typeObjArr[j].getTypeProt());
						}
					}
				}else{
					logger.debug("qosObj.ClassificationArr is empty");
					Element clas = root.addElement("Classification");
					clas.addAttribute("flag", "0");
				}
			}else if (qosObj.getType() == 0){
				//使用老的Qos配置模板
				logger.debug("使用老的Qos配置模板");
				root.addAttribute("type", "0");
				root.addElement("Mode").addText(qosObj.getMode());
				root.addElement("Enable").addText(qosObj.getEnable());
			}
			strXml = doc.asXML();
		}else{
			logger.warn("qosObj2Xml(qosObj is null)");
		}
		logger.debug("qosObj2Xml return({})", strXml);
		return strXml;
	}
	
	
	
	/**
	 * VOIP对象生成XML
	 * isWan:是否下发wan连接
	 * 
	 * @date 2009-7-23
	 * @param HgwCustomerObj
	 *            hgwObj
	 * @return
	 */
	public static String voipObj2Xml(HgwServUserObj hgwServUserObj,
			VoiceServiceProfileObj voipProfObj) {
		logger.debug("voipObj2Xml(hgwServUserObj, voipProfObj)");
		return voipObj2Xml(hgwServUserObj, voipProfObj, 1);
	}
	
	
	
	/**
	 * VOIP对象生成XML
	 * isWan:是否下发wan连接
	 * 
	 * @date 2009-7-23
	 * @param HgwCustomerObj
	 *            hgwObj
	 * @return
	 */
	public static String voipObj2Xml(HgwServUserObj servUserObj,
			VoiceServiceProfileObj voipProfObj, int isWan) {
		logger.debug("voipObj2Xml({}, {}, {})", new Object[]{servUserObj, voipProfObj, isWan});
		
		if(null == servUserObj || null == voipProfObj){
			logger.warn("servUserObj or voipProfObj is null");
			return null;
		}
		
		String strXml = null;

		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: Voip
		Element root = doc.addElement("VOIP");
		Element wan = root.addElement("WAN");
		// WAN连接需要下发
		wan.addAttribute("flag", String.valueOf(isWan));
		// WANAccessType： DSL | Ethernet | POTS
		wan.addElement("AccessType")
				.addText(StringUtil.getStringValue(WanTypeUtil.getAccessType(StringUtil.getIntegerValue(servUserObj.getWanType()))));
		// 接入方式+上网方式
		wan.addElement("WanType").addText(String.valueOf(servUserObj.getWanType()));
		// PVC
		wan.addElement("Pvc").addText(
				"PVC:" + servUserObj.getVpiid() + "/" + servUserObj.getVciid());
		// Voip无绑定端口
		wan.addElement("BindPort").addText("");
		// 路由必须
		wan.addElement("Username").addText(
				null == servUserObj.getUsername() ? "" : servUserObj.getUsername());
		wan.addElement("Password").addText(
				null == servUserObj.getPasswd() ? "" : servUserObj.getPasswd());
		// LAN上行和EPON上行必须
		wan.addElement("VlanId").addText(
				null == servUserObj.getVlanid() ? "" : servUserObj.getVlanid());
		// 静态IP必须
		wan.addElement("Ip").addText(
				null == servUserObj.getIpAddress() ? "" : servUserObj.getIpAddress());
		wan.addElement("Mask").addText(
				null == servUserObj.getIpMask() ? "" : servUserObj.getIpMask());
		wan.addElement("Gateway").addText(
				null == servUserObj.getGateway() ? "" : servUserObj.getGateway());
		wan.addElement("Dns").addText(
				null == servUserObj.getDns() ? "" : servUserObj.getDns());

		Element services = root.addElement("Services");
		// Voip连接需要下发
		services.addAttribute("flag", "1");

		Element voiceService = services.addElement("VoiceService");
		Element voiceProfile = voiceService.addElement("VoiceProfile");
		// Voip相关的服务器地址和端口信息
		Element voiceSip = voiceProfile.addElement("SIP");
		voiceSip.addElement("ProxyServer").addText(
				null == voipProfObj.getProxServ() ? "" : voipProfObj
						.getProxServ());
		voiceSip.addElement("ProxyServerPort").addText(
				null == voipProfObj.getProxPort() ? "" : voipProfObj
						.getProxPort());
		voiceSip.addElement("X_CT-COM_Standby-ProxyServer").addText(
				null == voipProfObj.getProxServ2() ? "" : voipProfObj
						.getProxServ2());
		voiceSip.addElement("X_CT-COM_Standby-ProxyServerPort").addText(
				null == voipProfObj.getProxPort2() ? "" : voipProfObj
						.getProxPort2());

		voiceSip.addElement("RegistrarServer").addText(
				null == voipProfObj.getRegiServ() ? "" : voipProfObj
						.getRegiServ());
		voiceSip.addElement("RegistrarServerPort").addText(
				null == voipProfObj.getRegiPort() ? "" : voipProfObj
						.getRegiPort());
		voiceSip.addElement("X_CT-COM_Standby-RegistrarServer").addText(
				null == voipProfObj.getStandRegiServ() ? "" : voipProfObj
						.getStandRegiServ());
		voiceSip.addElement("X_CT-COM_Standby-RegistrarServerPort").addText(
				null == voipProfObj.getStandRegiPort() ? "" : voipProfObj
						.getStandRegiPort());

		voiceSip.addElement("OutboundProxy").addText(
				null == voipProfObj.getOutBoundProxy() ? "" : voipProfObj
						.getOutBoundProxy());
		voiceSip.addElement("OutboundProxyPort").addText(
				null == voipProfObj.getOutBoundPort() ? "" : voipProfObj
						.getOutBoundPort());
		voiceSip.addElement("X_CT-COM_Standby-OutboundProxy").addText(
				null == voipProfObj.getStandOutBoundProxy() ? "" : voipProfObj
						.getStandOutBoundProxy());
		voiceSip.addElement("X_CT-COM_Standby-OutboundProxyPort").addText(
				null == voipProfObj.getStandOutBoundPort() ? "" : voipProfObj
						.getStandOutBoundPort());

		Element line = voiceProfile.addElement("Line");
		// 使能
		line.addElement("Enable").addText("Enabled");
		// 认证Voip账号和Voip密码
		Element lineSip = line.addElement("SIP");
		lineSip.addElement("AuthUserName").addText(null == servUserObj.getUsername() ? "" : servUserObj.getUsername());
		lineSip.addElement("AuthPassword").addText(null == servUserObj.getPasswd() ? "" : servUserObj.getPasswd());

		strXml = doc.asXML();

		logger.debug("VOIP对象生成XML：{" + strXml + "}");
		return strXml;
	}
	
	
	
	/**
	 * 业务用户对象生成XML
	 * 
	 * @date 2009-7-23
	 * @param HgwCustomerObj
	 *            hgwObj
	 * @return
	 */
	public static String WanConnObj2Xml(WanConnObj wanConnOBJ, WanConnSessObj wanConnSessOBJ, int wanType) {
		logger.debug("WanConnObj2Xml({},{})", wanConnOBJ, wanConnSessOBJ);
		String strXml = null;

		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("NET");
		Element wan = root.addElement("WAN");
		// WANAccessType： DSL | Ethernet | POTS
		wan.addElement("AccessType")
				.addText(StringUtil.getStringValue(WanTypeUtil.getAccessType(StringUtil.getIntegerValue(wanType))));
		// 接入方式+上网方式
		wan.addElement("WanType").addText(String.valueOf(wanType));
		// PVC
		wan.addElement("Pvc").addText(
				"PVC:" + wanConnOBJ.getVpi_id() + "/" + wanConnOBJ.getVci_id());
		// 绑定端口
		wan.addElement("BindPort").addText(null == wanConnSessOBJ.getBindPort() ? "" : wanConnSessOBJ.getBindPort());
		// 路由必须
		wan.addElement("Username").addText(
				null == wanConnSessOBJ.getUsername() ? "" : wanConnSessOBJ.getUsername());
		wan.addElement("Password").addText(
				null == wanConnSessOBJ.getPassword() ? "" : wanConnSessOBJ.getPassword());
		// LAN上行和EPON上行必须
		wan.addElement("VlanId").addText(
				null == wanConnOBJ.getVlan_id() ? "" : wanConnOBJ.getVlan_id());
		// 静态IP必须
		wan.addElement("Ip").addText(
				null == wanConnSessOBJ.getIp() ? "" : wanConnSessOBJ.getIp());
		wan.addElement("Mask").addText(
				null == wanConnSessOBJ.getMask() ? "" : wanConnSessOBJ.getMask());
		wan.addElement("Gateway").addText(
				null == wanConnSessOBJ.getGateway() ? "" : wanConnSessOBJ.getGateway());
		wan.addElement("Dns").addText(
				null == wanConnSessOBJ.getDns() ? "" : wanConnSessOBJ.getDns());

		strXml = doc.asXML();

		logger.debug("WanConn,WanConnSess对象生成WAN连接XML：{" + strXml + "}");
		return strXml;
	}
	
	
	
	/**
	 * 业务用户对象生成XML
	 * 
	 * @date 2009-7-23
	 * @param HgwCustomerObj
	 *            hgwObj
	 * @return
	 */
	public static String ServUserObj2Xml(HgwServUserObj servUserObj) {
		logger.debug("ServUserObj2Xml(hgwServUserObj:{})", servUserObj);
		String strXml = null;

		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("NET");
		Element wan = root.addElement("WAN");
		// WANAccessType： DSL | Ethernet | POTS
		wan.addElement("AccessType")
				.addText(StringUtil.getStringValue(WanTypeUtil.getAccessType(StringUtil.getIntegerValue(servUserObj.getWanType()))));
		// 接入方式+上网方式
		wan.addElement("WanType").addText(String.valueOf(servUserObj.getWanType()));
		// PVC
		wan.addElement("Pvc").addText(
				"PVC:" + servUserObj.getVpiid() + "/" + servUserObj.getVciid());
		// 绑定端口
		wan.addElement("BindPort").addText(null == servUserObj.getBindPort() ? "" : servUserObj.getBindPort());
		// 路由必须
		wan.addElement("Username").addText(
				null == servUserObj.getUsername() ? "" : servUserObj.getUsername());
		wan.addElement("Password").addText(
				null == servUserObj.getPasswd() ? "" : servUserObj.getPasswd());
		// LAN上行和EPON上行必须
		wan.addElement("VlanId").addText(
				null == servUserObj.getVlanid() ? "" : servUserObj.getVlanid());
		// 静态IP必须
		wan.addElement("Ip").addText(
				null == servUserObj.getIpAddress() ? "" : servUserObj.getIpAddress());
		wan.addElement("Mask").addText(
				null == servUserObj.getIpMask() ? "" : servUserObj.getIpMask());
		wan.addElement("Gateway").addText(
				null == servUserObj.getGateway() ? "" : servUserObj.getGateway());
		wan.addElement("Dns").addText(
				null == servUserObj.getDns() ? "" : servUserObj.getDns());

		strXml = doc.asXML();

		logger.debug("业务用户对象生成WAN连接XML：{" + strXml + "}");
		return strXml;
	}
	
	/**
	 * SgwSecurityOBJ业务下发，入的xml格式参数
	 *
	 * @author wangsenbo
	 * @date Oct 29, 2009
	 * @return String
	 */
	public static String sgwSecurityOBJ2Xml(SgwSecurityOBJ sgwSecurityObj)
	{
		logger.debug("sgwSecurityOBJ2Xml(SgwSecurityOBJ:{})", sgwSecurityObj);
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("SNMP");
		root.addElement("Enable").addText(
				StringUtil.getStringValue(sgwSecurityObj.getIsEnable()));
		root.addElement("SNMPVersion").addText(
				null == sgwSecurityObj.getSnmpVersion() ? "" : sgwSecurityObj
						.getSnmpVersion());
		root.addElement("TrustedHost").addText("");
		root.addElement("ReadOnlyCommunity").addText(
				true == StringUtil.IsEmpty(sgwSecurityObj.getSnmprPasswd()) ? "" : sgwSecurityObj
						.getSnmprPasswd());
		root.addElement("ReadWriteCommunity").addText(
				true == StringUtil.IsEmpty(sgwSecurityObj.getSnmpwPasswd()) ? "" : sgwSecurityObj
						.getSnmpwPasswd());
		root.addElement("TrapCommunity").addText("");
		root.addElement("SecurityUserName").addText(
				null == sgwSecurityObj.getSecurityUsername() ? "" : sgwSecurityObj
						.getSecurityUsername());
		root.addElement("SecurityLevel").addText(
				StringUtil.getStringValue(sgwSecurityObj.getSecurityLevel()));
		root.addElement("AuthPasswd").addText(
				null == sgwSecurityObj.getAuthPasswd() ? "" : sgwSecurityObj
						.getAuthPasswd());
		root.addElement("PrivacyPasswd").addText(
				null == sgwSecurityObj.getPrivacyPasswd() ? "" : sgwSecurityObj
						.getPrivacyPasswd());
		root.addElement("Contact").addText("");
		root.addElement("DeviceName").addText("");
		root.addElement("Location").addText("");
		root.addElement("TrapVersion").addText("");
		root.addElement("TrapDestination").addText("");
		root.addElement("EncryptArith").addText(
				null == sgwSecurityObj.getPrivacyProtocol() ? "" : sgwSecurityObj
						.getPrivacyProtocol());
		root.addElement("ValidateArith").addText(
				null == sgwSecurityObj.getAuthProtocol() ? "" : sgwSecurityObj
						.getAuthProtocol());
		Element apply = root.addElement("Apply");
		apply.addAttribute("flag", "0");
		apply.addText("");
		strXml = doc.asXML();
		return strXml;
	}
	
	/**
	 * DHCP配置 xml格式参数
	 *
	 * @author wangsenbo
	 * @date Oct 29, 2009
	 * @return String
	 */
	public static String DHCP2Xml(LanVlanOBJ lanVlanObj)
	{
		logger.debug("DHCP2Xml(LanVlanOBJ:{})", lanVlanObj);
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("DHCP");
		root.addElement("Enable").addText(
				StringUtil.getStringValue(lanVlanObj.getDhcpEnable()));
		root.addElement("MinAddress").addText(
				null == lanVlanObj.getDhcpMinAddr() ? "" : lanVlanObj.getDhcpMinAddr());
		root.addElement("MaxAddress").addText(
				null == lanVlanObj.getDhcpMaxAddr() ? "" : lanVlanObj.getDhcpMaxAddr());
		root.addElement("ReservedAddresses").addText(
				null == lanVlanObj.getDhcpResAddr() ? "" : lanVlanObj.getDhcpResAddr());
		root.addElement("SubnetMask").addText(
				null == lanVlanObj.getDhcpMask() ? "" : lanVlanObj.getDhcpMask());
		root.addElement("DNSServers").addText(
				null == lanVlanObj.getDhcpDns() ? "" : lanVlanObj.getDhcpDns());
		root.addElement("DomainName").addText(
				null == lanVlanObj.getDhcpDomain() ? "" : lanVlanObj.getDhcpDomain());
		root.addElement("DefaultGateway").addText(
				null == lanVlanObj.getDhcpGateway() ? "" : lanVlanObj.getDhcpGateway());
		root.addElement("DHCPLeaseTime").addText(
				null == lanVlanObj.getDhcpLeaseTime() ? "" : lanVlanObj
						.getDhcpLeaseTime());
		Element apply = root.addElement("IPInterface_Apply");
		apply.addAttribute("flag", "0");
		apply.addText("");
		root.addElement("Vlan_i").addText(
				StringUtil.getStringValue(lanVlanObj.getVlanI()));
		strXml = doc.asXML();
		return strXml;
	}
	
	/**
	 * VLAN配置 xml格式参数
	 *
	 * @author wangsenbo
	 * @date Oct 29, 2009
	 * @return String
	 */
	public static String VLAN2Xml(String type,LanVlanOBJ lanVlanObj)
	{
		logger.debug("VLAN2Xml({},{})", type,lanVlanObj);
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("VLAN");
		root.addAttribute("flag", type);
		root.addElement("VlanId").addText(
				StringUtil.getStringValue(lanVlanObj.getVlanId()));
		root.addElement("VlanName").addText(
				null == lanVlanObj.getVlanName() ? "" : lanVlanObj.getVlanName());
		root.addElement("PortList").addText(
				null == lanVlanObj.getPortList() ? "" : lanVlanObj.getPortList());
		root.addElement("IPInterface_Enable").addText(StringUtil.getStringValue(lanVlanObj.getIpEnable()));
		root.addElement("IPInterface_IP").addText(
				null == lanVlanObj.getIpAddress() ? "" : lanVlanObj.getIpAddress());
		root.addElement("IPInterface_Mask").addText(
				null == lanVlanObj.getIpMask() ? "" : lanVlanObj.getIpMask());
		root.addElement("IPInterface_AddressType").addText(
				null == lanVlanObj.getIpAddressType() ? "" : lanVlanObj.getIpAddressType());
		Element apply = root.addElement("IPInterface_Apply");
		apply.addAttribute("flag", "0");
		apply.addText("");
		root.addElement("Vlan_i").addText(
				StringUtil.getStringValue(lanVlanObj.getVlanI()));
		strXml = doc.asXML();
		return strXml;
	}
	
	
	/**
	 * 根据时间NTP对象获取配置该项的XML字符串
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-11-3
	 * @return String
	 */
	public static String timeNtp2Xml(TimeNtpOBJ timeNtpObj){
		logger.debug("timeNtp2Xml({})", timeNtpObj);
		if(null == timeNtpObj){
			logger.warn("timeNtp2Xml(timeNtpObj) is null");
			return null;
		}
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: Time
		Element root = doc.addElement("Time");
		root.addElement("Enable").addText(StringUtil.getStringValue(timeNtpObj.getEnable()));
		root.addElement("NTPServer1").addText(StringUtil.getStringValue(timeNtpObj.getNtpServer1()));
		root.addElement("NTPServer2").addText(StringUtil.getStringValue(timeNtpObj.getNtpServer2()));
		root.addElement("LocalTimeZone").addText(StringUtil.getStringValue(timeNtpObj.getTimezone()));
		root.addElement("LocalTimeZoneName").addText(StringUtil.getStringValue(timeNtpObj.getTimezoneName()));
		root.addElement("Apply").addAttribute("flag", "0").addText("1");
		strXml = doc.asXML();
		return strXml;
	}
	
	
	
	/**
	 * Wlan配置功能；addOrEdit 0:新增 1:编辑
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-12-8
	 * @return String
	 */
	public static String wlan2Xml(WlanOBJ wlanOBJ, int addOrEdit){
		logger.debug("wlan2Xml({}, {})", wlanOBJ, addOrEdit);
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: Time
		Element root = doc.addElement("WLAN");
		//0:新增 1:编辑
		root.addAttribute("flag", StringUtil.getStringValue(addOrEdit));
		//系统类型ITMS OR BBMS
		root.addElement("SystemType").addText(StringUtil.getStringValue(LipossGlobals.SystemType()));
		//1:不加密 2:WEP加密 3:WPA加密 4:11i加密
		root.addElement("EncryptionType").addText(StringUtil.getStringValue(wlanOBJ.getEncryptionType()));
		root.addElement("Enable").addText(StringUtil.getStringValue(wlanOBJ.getEnable()));
		root.addElement("SSID").addText(StringUtil.getStringValue(wlanOBJ.getSsid()));
		
		/** itms 使用 */
		root.addElement("X_CT-COM_APModuleEnable").addText(StringUtil.getStringValue(wlanOBJ.getApEnable()));
		root.addElement("X_CT-COM_Powerlevel").addText(StringUtil.getStringValue(wlanOBJ.getPowerlevel()));
		root.addElement("X_CT-COM_SSIDHide").addText(StringUtil.getStringValue(wlanOBJ.getHide()));
		root.addElement("X_CT-COM_WPSKeyWord").addText(StringUtil.getStringValue(wlanOBJ.getWpsKeyWord()));
		root.addElement("Channel").addText(StringUtil.getStringValue(wlanOBJ.getChannel()));
		
		/** 以下两项暂时为BBMS所用 */
		root.addElement("RadioEnabled").addText(StringUtil.getStringValue(wlanOBJ.getRadioEnable()));
		root.addElement("WEPKeyIndex").addText("1");
		
		root.addElement("BeaconType").addText(StringUtil.getStringValue(wlanOBJ.getBeacontype()));
		
		root.addElement("BasicAuthenticationMode").addText(StringUtil.getStringValue(wlanOBJ.getBasicAuthMode()));
		root.addElement("WEPEncryptionLevel").addText(StringUtil.getStringValue(wlanOBJ.getWepEncrLevel()));
		//当设备支持KeyPassphrase时，此参数不需要
		root.addElement("WEPKey").addText(StringUtil.getStringValue(wlanOBJ.getWepKey()));
		
		root.addElement("IEEE11iAuthenticationMode").addText(StringUtil.getStringValue(wlanOBJ.getIeeeAuthMode()));
		root.addElement("IEEE11iEncryptionModes").addText(StringUtil.getStringValue(wlanOBJ.getIeeeEncrMode()));
		//当设备支持KeyPassphrase时，此参数不需要
		root.addElement("PreSharedKey").addText(StringUtil.getStringValue(wlanOBJ.getPreSharedKey()));

		root.addElement("KeyPassphrase").addText(StringUtil.getStringValue(wlanOBJ.getKeyPassphrase()));
		
		root.addElement("Lan_i").addText(StringUtil.getStringValue(wlanOBJ.getLanId()));
		root.addElement("Wlan_i").addText(StringUtil.getStringValue(wlanOBJ.getLanWlanId()));
		
		strXml = doc.asXML();
		
		return strXml;
	}
	
	
	/**
	 * 工单参数,入策略表调预读用
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2008-8-29
	 * @return String
	 */
	public static String getSheetParam(HgwServUserObj servUserObj){
		logger.debug("getSheetParam({})", servUserObj);
		if(null == servUserObj){
			logger.warn("servUserObj is null");
			return null;
		}
		int wan_type = StringUtil.getIntegerValue(servUserObj.getWanType());
		String sheet_para_acs = "";
		String pvc = "PVC:" + servUserObj.getVpiid() + "/" + servUserObj.getVciid();
		String bindPort = StringUtil.getStringValue(servUserObj.getBindPort());
		logger.debug("wan_type: {}", wan_type);
		switch(wan_type){
		case 1:
			sheet_para_acs += pvc;
			sheet_para_acs += "|||";
			sheet_para_acs += bindPort;
			break;
		case 2:
			sheet_para_acs += pvc;
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getUsername();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getPasswd();
			sheet_para_acs += "|||";
			sheet_para_acs += bindPort;
			break;
		case 3:
			//静态IP
			sheet_para_acs += pvc;
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getIpAddress();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getIpMask();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getGateway();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getDns();
			sheet_para_acs += "|||";
			sheet_para_acs += bindPort;
			break;
			
		case 4:
			//DHCP
			sheet_para_acs += pvc;
			sheet_para_acs += "|||";
			sheet_para_acs += bindPort;
			break;
		case 5:
			sheet_para_acs += servUserObj.getVlanid();
			sheet_para_acs += "|||";
			sheet_para_acs += bindPort;
			break;
		case 6:
			sheet_para_acs += servUserObj.getVlanid();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getUsername();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getPasswd();
			sheet_para_acs += "|||";
			sheet_para_acs += bindPort;
			break;
		case 7:
			sheet_para_acs += servUserObj.getVlanid();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getIpAddress();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getIpMask();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getGateway();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getDns();
			sheet_para_acs += "|||";
			sheet_para_acs += bindPort;
			break;
		case 8:
			//DHCP
			sheet_para_acs += servUserObj.getVlanid();
			sheet_para_acs += "|||";
			sheet_para_acs += bindPort;
			break;
		case 9:
			sheet_para_acs += servUserObj.getVlanid();
			sheet_para_acs += "|||";
			sheet_para_acs += bindPort;
			break;
		case 10:
			sheet_para_acs += servUserObj.getVlanid();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getUsername();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getPasswd();
			sheet_para_acs += "|||";
			sheet_para_acs += bindPort;
			break;
		case 11:
			//
			sheet_para_acs += servUserObj.getVlanid();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getIpAddress();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getIpMask();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getGateway();
			sheet_para_acs += "|||";
			sheet_para_acs += servUserObj.getDns();
			sheet_para_acs += "|||";
			sheet_para_acs += bindPort;
			break;
		case 12:
			//DHCP
			sheet_para_acs += servUserObj.getVlanid();
			sheet_para_acs += "|||";
			sheet_para_acs += bindPort;
			break;
		default:
			
		}
		return sheet_para_acs;
	}

}
