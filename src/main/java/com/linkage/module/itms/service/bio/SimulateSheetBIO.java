package com.linkage.module.itms.service.bio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.service.dao.SimulateSheetDAO;
import com.linkage.module.itms.service.obj.SheetObj4CQ;

/**
 * 
 * @author zhangshimin(工号)
 * @version 1.0
 * @since 2011-5-21 下午02:42:44
 * @category com.linkage.module.itms.service.bio
 * @copyright 南京联创科技 网管科技部
 * 
 */
public class SimulateSheetBIO {
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(SimulateSheetBIO.class);
	private SimulateSheetDAO dao;

	public String checkUsername(String username, String gw_type) {
		List<Map<String, String>> userList = dao.getUserInfo(username, gw_type);
		int size = userList.size();
		if (size == 1) {
			logger.warn("1#" + userList.get(0).get("orderType"));
			return "1#" + userList.get(0).get("orderType");
		} else if (size > 1) {
			return "0#请输入完整的逻辑SN";
		} else {
			return "-1#逻辑SN不存在，请先走建设流程";
		}
	}

	/**
	 * 生成资料接口开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param devType
	 * @param username
	 * @param cityId
	 * @param officeId
	 * @param zoneId
	 * @param orderType
	 * @param linkman
	 * @param linkphone
	 * @param linkEmail
	 * @param linkMobile
	 * @param homeAddr
	 * @param credNo
	 * @return
	 */
	public String sendOpenSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username, String cityId,
			String officeId, String zoneId, int orderType, String linkman,
			String linkphone, String linkEmail, String linkMobile,
			String homeAddr, String credNo, String customerId,
			String customerAccount, String customerPwd, String instAreaName) {
		logger.debug("sendOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(officeId).append("|||");
		sbuffer.append(zoneId).append("|||");
		sbuffer.append(orderType).append("|||");
		sbuffer.append(linkman).append("|||");
		sbuffer.append(linkphone).append("|||");
		sbuffer.append(linkEmail).append("|||");
		sbuffer.append(linkMobile).append("|||");
		sbuffer.append(homeAddr).append("|||");
		sbuffer.append(credNo).append("|||");
		if (Global.NMGDX.equals(instAreaName)) {
			sbuffer.append(transferDateFormate(customerId)).append("|||");
		} else {
			sbuffer.append(customerId).append("|||");
		}
		sbuffer.append(customerAccount).append("|||");

		if (Global.NMGDX.equals(instAreaName)) {
			sbuffer.append(customerPwd);
			sbuffer.append("LINKAGE");
		} else {
			sbuffer.append(customerPwd).append("|||");
			sbuffer.append("e8cp42").append("FROMWEB");
		}
		logger.warn("开户工单：" + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * 全业务销户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 *            LOID
	 * @param userAccount
	 *            业务帐号
	 * @param cityId
	 * @return
	 */
	public String sendStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String netUsername) {
		logger.debug("sendStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(netUsername).append("LINKAGE");
		logger.warn(sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * 建设流程销户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 *            LOID
	 * @param userAccount
	 *            业务帐号
	 * @param cityId
	 * @return
	 */
	public String sendStopSheetNew(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String userType,
			String instAreaName) {
		logger.debug("sendStopSheet()");
		// TODO 需要调整工单
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		if (Global.NMGDX.equals(instAreaName)) {
			sbuffer.append(cityId).append("LINKAGE");
		} else {
			sbuffer.append(cityId).append("FROMWEB");
		}
		logger.warn("sendStopSheetNew: " + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * iptv业务销户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param userType
	 * @param iptvUsername
	 * @param iptvLanPort
	 * @return
	 */
	public String sendIptvStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String iptvUsername) {
		logger.debug("sendStopSheet()");
		// TODO 需要调整工单
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(iptvUsername).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * 上网业务开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param netUsername
	 * @param netPassword
	 * @param vlanId
	 * @return
	 */
	public String sendNetOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String netUsername, String vlanId, String userType, String netway,
			String dns, String code, String ipaddress, String useriptype,
			String wlantype) {
		logger.warn("sendNetOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append("e8c").append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(vlanId).append("|||");
		sbuffer.append(useriptype).append("|||");
		sbuffer.append(wlantype).append("|||");
		sbuffer.append(ipaddress).append("|||");
		sbuffer.append(code).append("|||");
		sbuffer.append(netway).append("|||");
		sbuffer.append(dns).append("|||");
		sbuffer.append(userType).append("LINKAGE");
		logger.warn("上网:" + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	
	/**
	 * 上网业务开户工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendNetOpenSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendNetOpenSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("Z");
		root.addElement("Service_code").addText("wband");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Old_User_id").addText(obj.getOlduserid());
		info.addElement("Device_ID").addText(obj.getDeviceid());
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText(obj.getContactperson());
		info.addElement("Phonenumber").addText(obj.getPhonenumber());
		info.addElement("Migration").addText(obj.getMigration());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		
		String argues = "rg_mode="+obj.getRgmode()+"^ad_account="+obj.getAdaccount()+"^ad_password="+obj.getAdpassword()+"^rg_port_id="+obj.getRgportid()
				+"^pvc_vpi="+obj.getPvcvpi()+"^pvc_vci="+obj.getPvcvci()+"^vlan_id="+obj.getVlanid()+"^multi_device_mode="+obj.getMultidevicemode()
				+"^pc_numbers="+obj.getPcnumbers()+"^keep_user_info="+obj.getKeepuserinfo()+"^IPV6_Enable="+obj.getIPV6Enable()
				+"^AFTR_Address="+obj.getAFTRAddress()+"^dhcp_enable="+obj.getDhcpenable()+"^awifi_port_id="+obj.getAwifiportid();
		info.addElement("Vector_argues").addText(argues);
		logger.warn("即将发送宽带开户工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	/**
	 * 上网业务销户工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendNetStopSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendNetStopSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("C");
		root.addElement("Service_code").addText("wband");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Old_User_id").addText(obj.getOlduserid());
		info.addElement("Device_ID").addText(obj.getDeviceid());
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText(obj.getContactperson());
		info.addElement("Phonenumber").addText(obj.getPhonenumber());
		info.addElement("Migration").addText(obj.getMigration());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		
		String argues = "rg_port_id="+obj.getRgportid()+"^awifi_port_id="+obj.getAwifiportid();
		info.addElement("Vector_argues").addText(argues);
		logger.warn("即将发送宽带销户工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	
	/**
	 * 上网业务销户工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendNetAlterAccountSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendNetAlterAccountSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("service_type").addText("21");
		// 结果描述
		root.addElement("service_opt").addText("7");
		Element info = root.addElement("itms_97_info");
		info.addElement("work_asgn_id").addText(date);
		info.addElement("account_name").addText(obj.getAdaccount());
		info.addElement("account_name_new").addText(obj.getNewadaccount());
		info.addElement("areacode").addText(obj.getCityId());
		info.addElement("subarea_code").addText(obj.getSubareacode());
		info.addElement("logic_id").addText(obj.getLoid());
		info.addElement("customer_id").addText(obj.getCustomerid());
		
		logger.warn("即将发送宽带改账号工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	/**
	 * 宽带信息修改工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendNetMsgAlterSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendNetMsgAlterSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("V");
		root.addElement("Service_code").addText("wband");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Old_User_id").addText(obj.getOlduserid());
		info.addElement("Device_ID").addText("");//不填
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText("");//不填
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText("");//不填
		info.addElement("Phonenumber").addText("");//不填
		info.addElement("Migration").addText(obj.getMigration());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		
		String argues = "vlan_id="+obj.getVlanid();
		info.addElement("Vector_argues").addText(argues);
		
		logger.warn("即将发送宽带信息修改工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	/**
	 * 宽带信息修改工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendNetAlterCustomerdSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendNetAlterCustomerdSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("G");
		root.addElement("Service_code").addText("wband");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Device_ID").addText(obj.getDeviceid());//不填
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());//不填
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		info.addElement("Contact_person").addText(obj.getContactperson());//不填
		info.addElement("Phonenumber").addText(obj.getPhonenumber());//不填
		
		
		
		String argues = "rg_mode=" + StringUtil.getStringValue(obj.getRgmode()) + "^dhcp_enable="
				+ StringUtil.getStringValue(obj.getDhcpenable()) + "^ad_account="
				+ StringUtil.getStringValue(obj.getAdaccount()) + "^ad_password="
				+ StringUtil.getStringValue(obj.getAdpassword());
		info.addElement("Vector_argues").addText(argues);
		
		logger.warn("发送宽带客户信息修改工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	
	/**
	 * 宽带信息修改工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendNetAlterPcNumSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendNetAlterPcNumSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("X");
		root.addElement("Service_code").addText("wband");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Device_ID").addText(obj.getDeviceid());//不填
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());//不填
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		info.addElement("Contact_person").addText(obj.getContactperson());//不填
		info.addElement("Phonenumber").addText(obj.getPhonenumber());//不填
		
		
		
		String argues = "multi_device_mode=" + obj.getMultidevicemode() + "^pc_numbers="
				+ obj.getPcnumbers();
		info.addElement("Vector_argues").addText(argues);
		
		logger.warn("发送修改宽带上网终端连接数工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	
	/**
	 * 宽带信息解绑工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendNetUnbindSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendNetMsgAlterSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("service_type").addText("21");
		// 结果描述
		root.addElement("service_opt").addText("20");
		Element info = root.addElement("itms_97_info");
		info.addElement("work_asgn_id").addText(date);
		info.addElement("account_name").addText(obj.getAdaccount());
		info.addElement("logic_id").addText(obj.getLoid());
		info.addElement("customer_id").addText(obj.getCustomerid());
		info.addElement("device_id").addText(obj.getDeviceid());
		info.addElement("keep_user_info").addText(obj.getKeepuserinfo());
		
		logger.warn("即将发送解除绑定工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	
	/**
	 * 宽带修改设备id工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendNetAlterDeviceidSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendNetAlterDeviceidSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("service_type").addText("21");
		// 结果描述
		root.addElement("service_opt").addText("6");
		Element info = root.addElement("itms_97_info");
		info.addElement("work_asgn_id").addText(date);
		info.addElement("account_name").addText(obj.getAdaccount());
		info.addElement("logic_id").addText(obj.getLoid());
		info.addElement("customer_id").addText(obj.getCustomerid());
		info.addElement("device_id").addText(obj.getDeviceid());
		
		logger.warn("即将发送更改设备id工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	
	/**
	 * 宽带模式工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendNetAlterModeSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendNetAlterModeSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		// 结果代码
		root.addElement("service_type").addText("21");
		// 结果描述
		root.addElement("service_opt").addText("9");
		Element info = root.addElement("itms_97_info");
		info.addElement("work_asgn_id").addText(date);
		info.addElement("logic_id").addText(obj.getLoid());
		info.addElement("customer_id").addText(obj.getCustomerid());
		info.addElement("account_name").addText(obj.getAdaccount());
		info.addElement("passwd").addText(obj.getAdpassword());
		info.addElement("rg_mode").addText(obj.getRgmode());
		info.addElement("vlan_id").addText(obj.getVlanid());
		
		logger.warn("即将发送更改模式工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	/**
	 * 宽带修改密码工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendNetAlterPasswdSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendNetAlterPasswdSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		// 结果代码
		root.addElement("service_type").addText("21");
		// 结果描述
		root.addElement("service_opt").addText("10");
		Element info = root.addElement("itms_97_info");
		info.addElement("work_asgn_id").addText(date);
		info.addElement("logic_id").addText(obj.getLoid());
		info.addElement("account_name").addText(obj.getAdaccount());
		info.addElement("passwd").addText(obj.getAdpassword());
		
		logger.warn("即将发送宽带更改密码工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	
	/**
	 * IPTV业务开户工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendIptvOpenSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendIptvOpenSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("Z");
		root.addElement("Service_code").addText("iptv");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Old_User_id").addText(obj.getOlduserid());
		info.addElement("Device_ID").addText(obj.getDeviceid());
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText(obj.getContactperson());
		info.addElement("Phonenumber").addText(obj.getPhonenumber());
		info.addElement("Migration").addText(obj.getMigration());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		
		String argues = "rg_mode="+obj.getRgmode()+"^iptv_rg_port_id="+obj.getIptvrgportid()+"^vlan_id="+obj.getVlanid()
				+"^multi_vlan_mode="+obj.getMultivlanmode()+"^snoopingEnable="+obj.getSnoopingEnable()+"^pvc_vpi="+obj.getPvcvpi()
				+"^pvc_vci="+obj.getPvcvci();
		info.addElement("Vector_argues").addText(argues);
		logger.warn("即将发送Iptv开户工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	
	/**
	 * IPTV信息修改工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendIptvMsgAlterSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendIptvMsgAlterSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("V");
		root.addElement("Service_code").addText("iptv");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Device_ID").addText("");
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText("");
		info.addElement("User_address").addText("");
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText("");
		info.addElement("Phonenumber").addText("");
		info.addElement("Area_code").addText("");
		info.addElement("SubArea_code").addText("");
		
		String argues = "vlan_id="+obj.getVlanid()+"^";
		info.addElement("Vector_argues").addText(argues);
		logger.warn("即将发送Iptv信息修改工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	
	/**
	 * 调整IPTV组播参数工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendIptvAlterMultiSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendIptvAlterMultiSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("M");
		root.addElement("Service_code").addText("iptv");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Device_ID").addText("");
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText("");
		info.addElement("User_address").addText("");
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText("");
		info.addElement("Phonenumber").addText("");
		info.addElement("Area_code").addText("");
		info.addElement("SubArea_code").addText("");
		
		String argues = "vlan_id="+obj.getVlanid()+"^multi_vlan_mode="+obj.getMultivlanmode()+"^snoopingEnable="+obj.getSnoopingEnable();
		info.addElement("Vector_argues").addText(argues);
		logger.warn("即将发送调整Iptv组播参数工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	
	
	/**
	 * iptv业务销户工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendIptvStopSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendIptvStopSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("C");
		root.addElement("Service_code").addText("iptv");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Old_User_id").addText(obj.getOlduserid());
		info.addElement("Device_ID").addText(obj.getDeviceid());
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText(obj.getContactperson());
		info.addElement("Phonenumber").addText(obj.getPhonenumber());
		info.addElement("Migration").addText(obj.getMigration());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		
		String argues = "iptv_rg_port_id="+obj.getIptvrgportid()+"^";
		info.addElement("Vector_argues").addText(argues);
		logger.warn("即将发送iptv销户工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	/**
	 * VOIP imssip业务开户工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendVoipOpenSheet4CQ_imssip(SheetObj4CQ obj) {
		logger.warn("sendVoipOpenSheet4CQ_imssip()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("Z");
		root.addElement("Service_code").addText("voip");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Old_User_id").addText(obj.getOlduserid());
		info.addElement("Device_ID").addText(obj.getDeviceid());
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText(obj.getContactperson());
		info.addElement("Phonenumber").addText(obj.getPhonenumber());
		info.addElement("Migration").addText(obj.getMigration());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		
		String argues = "protocol_type="+obj.getProtocoltype()+"^vlan_id="+obj.getVlanid()+"^ims_dns_ip="+obj.getImsdnsip()
				+"^ims_dns_bak_ip="+obj.getImsdnsbakip()+"^mg_ip="+obj.getMgip()+"^mg_mask="+obj.getMgmask()
				+"^mg_gw="+obj.getMggw()+"^proxyserver_ip="+obj.getProxyserverip()+"^proxyserver_port="+obj.getProxyserverport()
				+"^proxyserver_bak_ip="+obj.getProxyserverbakip()+"^proxyserver_bak_port="+obj.getProxyserverbakport()+"^registrarserver_ip="+obj.getRegistrarserverip()
				+"^registrarserver_port="+obj.getRegistrarserverport()+"^standby_registrarserver_ip="+obj.getStandbyregistrarserverip()
				+"^standby_registrarserver_port="+obj.getStandbyregistrarserverport()+"^sip_uri="+obj.getSipuri()+"^user_tid="+obj.getUsertid()
				+"^auth_username="+obj.getAuthusername()+"^auth_password="+obj.getAuthpassword();
		info.addElement("Vector_argues").addText(argues);
		logger.warn("即将发送VOIP imssip 开户工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	/**
	 * VOIP h248 业务开户工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendVoipOpenSheet4CQ_h248(SheetObj4CQ obj) {
		logger.warn("sendVoipOpenSheet4CQ_h248()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("Z");
		root.addElement("Service_code").addText("voip");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Old_User_id").addText(obj.getOlduserid());
		info.addElement("Device_ID").addText(obj.getDeviceid());
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText(obj.getContactperson());
		info.addElement("Phonenumber").addText(obj.getPhonenumber());
		info.addElement("Migration").addText(obj.getMigration());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		
		String argues = "protocol_type="+obj.getProtocoltype()+"^vlan_id="+obj.getVlanid()+"^mg_ip="+obj.getMgip()+"^mg_mask="+obj.getMgmask()
				+"^mg_gw="+obj.getMggw()+"^mg_domain="+obj.getMgDomain()+"^ss_ip="+obj.getSsip()+"^ss_port="+obj.getSsport()
				+"^ss_bak_ip="+obj.getSsbakip()+"^ss_bak_port="+obj.getSsbakport()+"^phonenumber="+obj.getPhonenumberSip()+"^user_tid="+obj.getUsertid();
		info.addElement("Vector_argues").addText(argues);
		logger.warn("即将发送VOIP h248开户工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	
	/**
	 * VOIP imssip信息修改工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendVoipAlterMsgSheet4CQ_imssip(SheetObj4CQ obj) {
		logger.warn("sendVoipAlterMsgSheet4CQ_imssip()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("V");
		root.addElement("Service_code").addText("voip");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Device_ID").addText("");
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText("");
		info.addElement("User_address").addText("");
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText("");
		info.addElement("Phonenumber").addText("");
		info.addElement("Area_code").addText("");
		info.addElement("SubArea_code").addText("");
		
		String argues = "protocol_type="+obj.getProtocoltype()+"^vlan_id="+obj.getVlanid()+"^ims_dns_ip="+obj.getImsdnsip()
				+"^ims_dns_bak_ip="+obj.getImsdnsbakip()+"^mg_ip="+obj.getMgip()+"^mg_mask="+obj.getMgmask()
				+"^mg_gw="+obj.getMggw()+"^proxyserver_ip="+obj.getProxyserverip()+"^proxyserver_port="+obj.getProxyserverport()
				+"^proxyserver_bak_ip="+obj.getProxyserverbakip()+"^proxyserver_bak_port="+obj.getProxyserverbakport()+"^registrarserver_ip="+obj.getRegistrarserverip()
				+"^registrarserver_port="+obj.getRegistrarserverport()+"^standby_registrarserver_ip="+obj.getStandbyregistrarserverip()
				+"^standby_registrarserver_port="+obj.getStandbyregistrarserverport()+"^sip_uri="+obj.getSipuri()+"^user_tid="+obj.getUsertid()
				+"^auth_username="+obj.getAuthusername()+"^auth_password="+obj.getAuthpassword();
		info.addElement("Vector_argues").addText(argues);
		logger.warn("即将发送VOIP imssip 信息修改工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	/**
	 * 语音IMS/SS SIP 修改用户鉴权密码工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendVoipAlterAuthpassSheet4CQ_sip(SheetObj4CQ obj) {
		logger.warn("sendVoipAlterAuthpassSheet4CQ_sip()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("service_type").addText("23");
		// 结果描述
		root.addElement("service_opt").addText("12");
		Element info = root.addElement("itms_97_info");
		info.addElement("work_asgn_id").addText(date);
		info.addElement("account_name").addText(obj.getAdaccount());
		info.addElement("logic_id").addText(obj.getLoid());
		info.addElement("customer_id").addText(obj.getCustomerid());
		info.addElement("device_id").addText(obj.getDeviceid());
		info.addElement("user_tid").addText(obj.getUsertid());
		Element voiceservice = info.addElement("voiceservice");
		Element sip = voiceservice.addElement("sip");
		sip.addElement("auth_username").addText(obj.getAuthusername());
		sip.addElement("auth_password").addText(obj.getAuthpassword());
		sip.addElement("sip_uri").addText(obj.getSipuri());
		logger.warn("即将语音IMS/SS SIP 修改用户鉴权密码工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}	
	
	/**
	 * VOIP H248信息修改工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendVoipAlterMsgSheet4CQ_H248(SheetObj4CQ obj) {
		logger.warn("sendVoipAlterMsgSheet4CQ_H248()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("V");
		root.addElement("Service_code").addText("voip");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Device_ID").addText("");
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText("");
		info.addElement("User_address").addText("");
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText("");
		info.addElement("Phonenumber").addText("");
		info.addElement("Area_code").addText("");
		info.addElement("SubArea_code").addText("");
		
		String argues = "protocol_type="+obj.getProtocoltype()+"^vlan_id="+obj.getVlanid()+"^mg_ip="+obj.getMgip()+"^mg_mask="+obj.getMgmask()
				+"^mg_gw="+obj.getMggw()+"^mg_domain="+obj.getMgDomain()+"^ss_ip="+obj.getSsip()+"^ss_port="+obj.getSsport()+"^ss_bak_ip="+obj.getSsbakip()
				+"^ss_bak_port="+obj.getSsbakport()+"^phonenumber="+obj.getPhonenumberSip()+"^user_tid="+obj.getUsertid();
		info.addElement("Vector_argues").addText(argues);
		logger.warn("即将发送VOIP H248 信息修改工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	/**
	 * VOIP业务销户工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendVoipStopSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendVoipStopSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText(obj.getDevicewan());
		root.addElement("Order_Type").addText("C");
		root.addElement("Service_code").addText("voip");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText(obj.getUsertype());
		info.addElement("Old_User_id").addText(obj.getOlduserid());
		info.addElement("Device_ID").addText(obj.getDeviceid());
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText(obj.getContactperson());
		info.addElement("Phonenumber").addText(obj.getPhonenumber());
		String sipuri = obj.getSipuri();
		String voipPort = dao.qryVoipPort(sipuri,obj.getLoid());
		logger.warn("voipPort="+voipPort);
		if(StringUtil.IsEmpty(voipPort)){
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><itms_97_interface><service_type>23</service_type><service_opt>2</service_opt><result><work_asgn_id></work_asgn_id><value>2</value><errcomment>用户电话不存在</errcomment></result></itms_97_interface>";
		}
		
		info.addElement("Migration").addText(obj.getMigration());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		
		String argues = "user_tid="+voipPort+"^";
		info.addElement("Vector_argues").addText(argues);
		logger.warn("即将发送VOIP销户工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	/**
	 * stbiptv 业务开户工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendStbiptvOpenSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendStbiptvOpenSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText("2");
		root.addElement("Order_Type").addText("Z");
		root.addElement("Service_code").addText("stb_iptv");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText("3");
		info.addElement("Device_ID").addText(obj.getDeviceid());
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText(obj.getContactperson());
		info.addElement("Phonenumber").addText(obj.getPhonenumber());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		
		String argues = "rg_mode="+obj.getRgmode()+"^Pppoe_Id="+obj.getPppoeid()+"^Pppoe_Password="+obj.getPppoepassword()
				+"^Dhcp_Id="+obj.getDhcpid()+"^Dhcp_Password="+obj.getDhcppassword()+"^Iptv_User_Id="+obj.getIptvuserid()
				+"^Iptv_Password="+obj.getIptvpassword()+"^Auth_Url="+obj.getAuthurl()+"^Auth_Url_Backup="+obj.getAuthurl_Backup();
		info.addElement("Vector_argues").addText(argues);
		logger.warn("即将发送stb iptv 开户工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	/**
	 * stbims 业务开户工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendStbimsOpenSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendStbimsOpenSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText("2");
		root.addElement("Order_Type").addText("Z");
		root.addElement("Service_code").addText("stb_ims");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText("3");
		info.addElement("Device_ID").addText(obj.getDeviceid());
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText(obj.getContactperson());
		info.addElement("Phonenumber").addText(obj.getPhonenumber());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		
		String argues = "Ims_id="+obj.getImsid()+"^Ims_password="+obj.getImspassword()+"^Bac_domain="+obj.getBacdomain()
				+"^Bac_address1="+obj.getBacaddress1()+"^Bac_address2="+obj.getBacaddress2();
		logger.warn("即将发送stb ims 开户工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	
	/**
	 * stbiptv 业务销户工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendStbiptvStopSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendStbiptvStopSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText("2");
		root.addElement("Order_Type").addText("C");
		root.addElement("Service_code").addText("stb_iptv");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText("3");
		info.addElement("Device_ID").addText(obj.getDeviceid());
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText(obj.getContactperson());
		info.addElement("Phonenumber").addText(obj.getPhonenumber());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		info.addElement("Vector_argues").addText("");
		logger.warn("即将发送stb iptv 销户工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	/**
	 * stbims 业务销户工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendStbimsStopSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendStbimsStopSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText("2");
		root.addElement("Order_Type").addText("C");
		root.addElement("Service_code").addText("stb_ims");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText("3");
		info.addElement("Device_ID").addText(obj.getDeviceid());
		info.addElement("Ad_account").addText(obj.getAdaccount());
		info.addElement("User_name").addText(obj.getUsername());
		info.addElement("User_address").addText(obj.getUseraddress());
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText(obj.getContactperson());
		info.addElement("Phonenumber").addText(obj.getPhonenumber());
		info.addElement("Area_code").addText(obj.getCityId());
		info.addElement("SubArea_code").addText(obj.getSubareacode());
		info.addElement("Vector_argues").addText("");
		logger.warn("即将发送stb ims 销户工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	/**
	 * stb 更改设备id工单 重庆
	 * 
	 * @param obj 业务参数对象 SheetObj4CQ
	 * @return 工单模块返回消息
	 */
	public String sendStbAlterDeviceidSheet4CQ(SheetObj4CQ obj) {
		logger.warn("sendStbAlterDeviceidSheet4CQ()");
		DateTimeUtil dateU = new DateTimeUtil();
		String date = "web" + dateU.getLongTime() + (int)(Math.random()*10000);
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("itms_97_interface");
		
		
		// 结果代码
		root.addElement("Order_Remark").addText("SG");
		// 结果描述
		root.addElement("Device_WAN").addText("2");
		root.addElement("Order_Type").addText("G");
		root.addElement("Service_code").addText("stb_ims");
		Element info = root.addElement("itms_97_info");
		info.addElement("Order_No").addText(date);
		info.addElement("Order_LSH").addText(date);
		info.addElement("Order_Time").addText(dateU.getYYYYMMDDHHMMSS());
		info.addElement("Order_Self").addText(obj.getOrderself());
		info.addElement("User_Type").addText("3");
		info.addElement("Device_ID").addText(obj.getDeviceid());
		info.addElement("Ad_account").addText("");
		info.addElement("User_name").addText("");
		info.addElement("User_address").addText("");
		info.addElement("User_id").addText(obj.getLoid());
		info.addElement("Contact_person").addText("");
		info.addElement("Phonenumber").addText("");
		info.addElement("Area_code").addText("");
		info.addElement("SubArea_code").addText("");
		info.addElement("Vector_argues").addText("");
		logger.warn("即将发送stb 更改deviceid工单:" + document.asXML());
		return this.sendSheetCQ(document.asXML());
	}
	
	
	
	/**
	 * VOIP业务开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipUsername
	 * @param voipPasswd
	 * @param sipIp
	 * @param sipPort
	 * @param standSipIp
	 * @param standSipPort
	 * @param linePort
	 * @param voipTelepone
	 * @return
	 */
	public String sendVoipOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String voipUsername, String voipPasswd, String voipTelepone,
			String linePort) {
		logger.debug("sendNetOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append("e8c").append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(voipTelepone).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(voipUsername).append("|||");
		sbuffer.append(voipPasswd).append("|||");
		sbuffer.append(linePort).append("LINKAGE");
		logger.warn("VOIP业务(SIP):" + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * VOIP销户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipTelepone
	 * @return
	 */
	public String sendVoipStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String voipTelepone) {
		logger.debug("sendVoipStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(voipTelepone).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * VOIP销户工单2
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipTelepone
	 * @return
	 */
	public String sendVoipStopSheet2(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String voipTelepone, String userType) {
		logger.debug("sendStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(voipTelepone).append("FROMWEB");
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * 新VOIP销户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipTelepone
	 * @return
	 */
	public String sendVoipStopSheetNew(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String voipTelepone, String userType) {
		logger.debug("sendStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(voipTelepone).append("LINKAGE");
		logger.warn("sendVoipStopSheetNew: " + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * H248 VOIP 开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipUsername
	 * @param voipPasswd
	 * @param sipIp
	 * @param sipPort
	 * @param standSipIp
	 * @param standSipPort
	 * @param linePort
	 * @param voipTelepone
	 * @param devType
	 * @return
	 */
	public String sendH248VoipOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String sipIp,
			int sipPort, String standSipIp, int standSipPort, String linePort,
			String voipTelepone, String userType, String regId) {
		logger.debug("sendNetOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(voipTelepone).append("|||");
		sbuffer.append(cityId).append("|||");

		sbuffer.append(regId).append("|||");
		sbuffer.append(1).append("|||");
		sbuffer.append(sipIp).append("|||");
		sbuffer.append(sipPort).append("|||");
		sbuffer.append(standSipIp).append("|||");
		sbuffer.append(standSipPort).append("|||");
		sbuffer.append(linePort).append("|||");
		sbuffer.append("45").append("FROMWEB");
		logger.warn("h248工单:" + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * H248 VOIP 新开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipUsername
	 * @param voipPasswd
	 * @param sipIp
	 * @param sipPort
	 * @param standSipIp
	 * @param standSipPort
	 * @param linePort
	 * @param voipTelepone
	 * @param devType
	 * @param netType
	 * @param ipAddress
	 * @param mask
	 * @param gateway
	 * @param dsn
	 * @return
	 */
	public String sendH248VoipOpenSheetNew(String servTypeId,
			String operateType, String dealdate, String userType,
			String username, String voipTelepone, String cityId, String regId,
			String regIdType, String sipIp, int sipPort, String standSipIp,
			int standSipPort, String linePort, String netType,
			String ipAddress, String mask, String gateway, String dsn) {
		logger.debug("sendH248VoipOpenSheetNew()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(voipTelepone).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(regId).append("|||");
		sbuffer.append(regIdType).append("|||");
		sbuffer.append(sipIp).append("|||");
		sbuffer.append(sipPort).append("|||");
		sbuffer.append(standSipIp).append("|||");
		sbuffer.append(standSipPort).append("|||");
		sbuffer.append(linePort).append("|||");
		sbuffer.append(netType).append("|||");
		sbuffer.append(ipAddress).append("|||");
		sbuffer.append(mask).append("|||");
		sbuffer.append(gateway).append("|||");
		sbuffer.append(dsn).append("LINKAGE");
		logger.warn("sendH248VoipOpenSheetNew: " + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * IPTV开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipUsername
	 * @param iptvUsername
	 * @param servNum
	 * @param iptvLanPort
	 * @return
	 */
	public String sendIptvOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String iptvUsername, int servNum) {
		logger.debug("sendIptvOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append("E8c").append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(iptvUsername).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(servNum).append("|||");
		sbuffer.append("").append("LINKAGE");
		logger.warn("iptv工单:" + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	public String sendE8bNetOpenSheet(String servTypeId, String operateType,
			String dealdate, String oui, String deviceSn, String devType,
			String netUsername, String netPassword, String adslPhone,
			String maxDownSpeed, String maxUpSpeed, String maxNetNum,
			String vlanid, String vpi, String vci, String cityId,
			String officeId, String zoneId, String linkman, String linkphone,
			String linkEmail, String linkMobile, String homeAddr,
			int orderType, String credNo) {
		logger.debug("sendE8bNetOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(oui).append("|||");
		sbuffer.append(deviceSn).append("|||");
		sbuffer.append(devType).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(netPassword).append("|||");

		sbuffer.append(adslPhone).append("|||");
		sbuffer.append(maxDownSpeed).append("|||");
		sbuffer.append(maxUpSpeed).append("|||");
		sbuffer.append(maxNetNum).append("|||");
		sbuffer.append(vlanid).append("|||");
		sbuffer.append(vpi).append("|||");
		sbuffer.append(vci).append("|||");

		sbuffer.append("").append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append("").append("|||");

		sbuffer.append(cityId).append("|||");
		sbuffer.append(officeId).append("|||");
		sbuffer.append(zoneId).append("|||");
		sbuffer.append(linkman).append("|||");
		sbuffer.append(linkphone).append("|||");

		sbuffer.append(linkEmail).append("|||");
		sbuffer.append(linkMobile).append("|||");
		sbuffer.append(homeAddr).append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append(orderType).append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append(credNo).append("FROMWEB");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendE8bNetStopSheet(String servTypeId, String operateType,
			String dealdate, String oui, String deviceSn, String netUsername,
			String cityId) {
		logger.debug("sendE8bNetStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(oui).append("|||");
		sbuffer.append(deviceSn).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(cityId).append("FROMWEB");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendE8bIptvOpenSheet(String servTypeId, String operateType,
			String dealdate, String oui, String deviceSn, String devType,
			String netUsername, String netPassword, String adslPhone,
			String maxDownSpeed, String maxUpSpeed, String vlanid, String vpi,
			String vci, String cityId, String officeId, String zoneId,
			String linkman, String linkphone, String linkEmail,
			String linkMobile, String homeAddr, int orderType, String credNo,
			String iptvUsername, int iptvNum) {
		logger.debug("sendE8bIptvOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(oui).append("|||");
		sbuffer.append(deviceSn).append("|||");
		sbuffer.append(devType).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(netPassword).append("|||");

		sbuffer.append(adslPhone).append("|||");
		sbuffer.append(maxDownSpeed).append("|||");
		sbuffer.append(maxUpSpeed).append("|||");
		sbuffer.append(vlanid).append("|||");
		sbuffer.append(vpi).append("|||");
		sbuffer.append(vci).append("|||");

		sbuffer.append(cityId).append("|||");
		sbuffer.append(officeId).append("|||");
		sbuffer.append(zoneId).append("|||");
		sbuffer.append(linkman).append("|||");
		sbuffer.append(linkphone).append("|||");

		sbuffer.append(linkEmail).append("|||");
		sbuffer.append(linkMobile).append("|||");
		sbuffer.append(homeAddr).append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append(orderType).append("|||");
		sbuffer.append("").append("|||");

		sbuffer.append(credNo).append("|||");
		sbuffer.append(iptvNum).append("|||");
		sbuffer.append("").append("FROMWEB");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendE8bIptvStopSheet(String servTypeId, String operateType,
			String dealdate, String oui, String deviceSn, String netUsername,
			String iptvUsername, String cityId) {
		logger.debug("sendE8bIptvStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(oui).append("|||");
		sbuffer.append(deviceSn).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(iptvUsername).append("|||");
		sbuffer.append(cityId).append("FROMWEB");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendtianyiOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String netUsername, String vlanId) {
		logger.debug("sendtianyiOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append("e8c").append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(vlanId).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendRouterOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String iptvUsername, int servNum) {
		logger.debug("sendRouterOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append("50").append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(iptvUsername).append("|||");
		sbuffer.append(servNum).append("|||");
		sbuffer.append(username).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendtianyiStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String netUsername) {
		logger.debug("sendtianyiStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(netUsername).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendRouterStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String netUsername) {
		logger.debug("sendRouterStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append("50").append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(netUsername).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * 时间格式转换
	 * 
	 * @param dealdate
	 *            yyy-MM-dd HH:mm:ss
	 * @return yyyyMMddHHmmss
	 */
	private String transferDateFormate(String dealdate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(dealdate);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.warn("工单受理时间转换异常");
		}
		dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date);
	}

	
	/**
	 * 向工单接口发送模拟的BSS工单。正常返回工单接口的回单结果，如果过程中出现问题返回null
	 * 
	 * @param 模拟工单数据
	 * @author zhangsm
	 * @date 2011-05-21
	 * @return String 回单信息
	 */
	public String sendSheet(String bssSheet) {
		logger.debug("sendSheet({})", bssSheet);
		if (StringUtil.IsEmpty(bssSheet)) {
			logger.warn("sendSheet is null");
			return null;
		}
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		String retResult = SocketUtil
				.sendStrMesg(server, port, bssSheet + "\n");
		return retResult;
	}
	
	/**
	 * 向工单接口发送模拟的BSS工单。正常返回工单接口的回单结果，如果过程中出现问题返回null
	 * 
	 * @param 模拟工单数据
	 * @author zhangsm
	 * @date 2011-05-21
	 * @return String 回单信息
	 */
	public String sendSheetCQ(String bssSheet) {
		logger.debug("sendSheet({})", bssSheet);
		if (StringUtil.IsEmpty(bssSheet)) {
			logger.warn("sendSheet is null");
			return null;
		}
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		String retResult = SocketUtil
				.sendStrMesgCQ(server, port, bssSheet + "\n");
		return retResult;
	}

	/**
	 * 根据传参获取loid
	 * @param sn 设备序列号
	 * @param username 宽带账号
	 * @return
	 */
	public String getLoidFP(String sn,String username){
		logger.warn("getLoidFP begin......");
		Map<String, String> map = new HashMap<String,String>();
		if(!StringUtil.IsEmpty(sn)){
			map = dao.queryUserInfo(2,sn);
			logger.warn("map=......"+map);
			return StringUtil.getStringValue(map, "username");
		}
		else if(!StringUtil.IsEmpty(username)){
			map = dao.queryUserInfo(1,username);
			logger.warn("map=......"+map);
			return StringUtil.getStringValue(map, "username");
		}
		else{
			return "ERROR";
		}
	}
	public void insertSheet(String id, String username, String serv_account,
			String city_id, int serv_type_id, int oper_type, int result_id,
			String result_desc, long oper_time, long occ_id,String occ_ip) {
		if(Global.CQDX.equals(Global.instAreaShortName)){
			dao.insertSheet4CQ(id, username, city_id, serv_type_id,
					oper_type, result_id, result_desc, oper_time, occ_id,occ_ip);
		}
		else{
			dao.insertSheet(id, username, serv_account, city_id, serv_type_id,
				oper_type, result_id, result_desc, oper_time, occ_id,occ_ip);
		}
	}
	
	public String qryLoid4CQ(String loid) {
		Map<String, String> resMap = dao.qryLoid4CQ(loid);
		String objectStr = "0";
		if(null == resMap){
			return objectStr;
		}
		String access = StringUtil.getStringValue(resMap, "access_style_id", "4");
		String type = resMap.get("type_id");
		String userType = "";
		if("6".equals(type)){
			userType = "1";
		}
		else{
			userType = "0";
		}
		objectStr=access+"|"+StringUtil.getStringValue(resMap, "city_id", "4")+"|"+userType;
		logger.warn(objectStr);
		return objectStr;
	}
	

	public int qyeryLoid(String loid) {
		return dao.qyeryLoid(loid);
	}

	public int checkIptvUser(String iptvUserName, String loid) {
		return dao.checkIptvUser(iptvUserName, loid);
	}

	public SimulateSheetDAO getDao() {
		return dao;
	}

	public void setDao(SimulateSheetDAO dao) {
		this.dao = dao;
	}

	public String sendVoipOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String voipUsername,
			String voipPasswd, String sipIp, int sipPort, String standSipIp,
			int standSipPort, String linePort, String voipTelepone, String devType,
			String registrarServer, int registrarServerPort, String standRegistrarServer,
			int standRegistrarServerPort, String outboundProxy, int outboundProxyPort,
			String standOutboundProxy, int standOutboundProxyPort, int protocol)
	{
		logger.debug("sendNetOpenSheet()");
	    StringBuffer sbuffer = new StringBuffer();
	    sbuffer.append(servTypeId).append("|||");
	    sbuffer.append(operateType).append("|||");
	    sbuffer.append(transferDateFormate(dealdate)).append("|||");
	    sbuffer.append(devType).append("|||");
	    sbuffer.append(username).append("|||");
	    sbuffer.append(voipTelepone).append("|||");
	    sbuffer.append(cityId).append("|||");

	    sbuffer.append(voipUsername).append("|||");
	    sbuffer.append(voipPasswd).append("|||");
	    sbuffer.append(sipIp).append("|||");
	    sbuffer.append(sipPort).append("|||");
	    sbuffer.append(standSipIp).append("|||");
	    sbuffer.append(standSipPort).append("|||");
	    sbuffer.append(linePort).append("|||");

	    sbuffer.append(registrarServer).append("|||");
	    sbuffer.append(registrarServerPort).append("|||");
	    sbuffer.append(standRegistrarServer).append("|||");
	    sbuffer.append(standRegistrarServerPort).append("|||");

	    sbuffer.append(outboundProxy).append("|||");
	    sbuffer.append(outboundProxyPort).append("|||");
	    sbuffer.append(standOutboundProxy).append("|||");
	    sbuffer.append(standOutboundProxyPort).append("|||");

	    sbuffer.append(protocol).append("FROMWEB");
	    return sendSheet(sbuffer.toString());
	}

}
