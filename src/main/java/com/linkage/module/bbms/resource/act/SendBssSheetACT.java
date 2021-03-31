package com.linkage.module.bbms.resource.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.resource.bio.SendBssSheetBIO;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.dao.tabquery.CustTypeDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-10-13
 */
public class SendBssSheetACT implements SessionAware {
	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory
			.getLogger(SendBssSheetACT.class);

	// 业务类型
	private String servTypeId;
	// 操作类型
	private String operateType;
	// 用户账号
	private String username;
	// 用户密码
	private String passwd;
	// 订单类型
	private String orderType;
	// PVC
	private String vpi;
	private String vci;
	// Vlan
	private String vlanid;
	// 静态IP时的参数
	private String ip;
	private String mask;
	private String gateway;
	private String dns;
	// 属地，局向
	private String cityId;
	private String officeId;
	// 最大上下行速率
	private String maxUpSpeed;
	private String maxDownSpeed;
	// 最大上网数
	private String maxUserNum;
	// 联系人姓名，电话，手机，email
	private String linkman;
	private String linkphone;
	private String mobile;
	private String email;
	// 客户ID
	private String customerId;
	// 客户账号，客户密码，客户名称，客户地址
	private String customerAccount;
	private String customerPasswd;
	private String customerName;
	private String customerAddr;
	// 套餐
	private String serialCode;
	// 设备OUI，序列号，设备类型，设备型号
	private String oui;
	private String devSn;
	private String devModel;
	private String devType;
	// EVDO卡相关信息
	// 数据库ESN号码
	private String dataEsn;
	// 数据库型号
	private String dataModel;
	// UIM卡IMSI号码
	private String uimImsi;
	// UIM卡ICCID号码
	private String uimIccid;
	// UIM卡手机号码
	private String uimMobile;
	
	//客户行业类别
	private String custType;
	//工作(链路)模式
	private String workMode;
	
	//<select 标签的行业类别
	private List custTypeList;
	// 受理时间
	private String dealdate;
	// 属地List
	private List cityList;
	// 局向List
	private List officeList;
	// Session
	private Map session;
	// ajax
	private String ajax;
	// BIO
	private SendBssSheetBIO sendBssSheetBio;

	/**
	 * 初始化页面操作
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-10-14
	 * @return String
	 */
	public String execute() {
		logger.debug("execute()");

		// 初始化页面信息

		return "success";
	}

	/**
	 * 根据不同的业务类型，不同的操作类型显示需要的工单字段
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-10-14
	 * @return String
	 */
	public String showSheet() {
		logger.debug("showSheet({},{})", servTypeId, operateType);
		
		//属地列表初始化
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		//属地<select>标签列表
		cityList = CityDAO.getNextCityListByCityPid(userCityId);
		//客户行业类别<select>标签列表
		custTypeList = CustTypeDAO.getAllCustType();
		// 显示不同业务不同操作类型的页面
		if (ConstantClass.BBMS_EVDO == StringUtil.getIntegerValue(servTypeId)) {
			logger.debug("EVDO 业务");
			if ("1".equals(operateType)) {
				logger.debug("EVDO 开户业务");
				return "evdoOpen";
			} else if ("2".equals(operateType)) {
				logger.debug("EVDO 暂停业务");
				return "evdoPause";
			} else if ("3".equals(operateType)) {
				logger.debug("EVDO 销户业务");
				return "evdoStop";
			} else if ("4".equals(operateType)) {
				logger.debug("EVDO 复机业务");
				return "evdoRestart";
			} else {
				ajax = "未知操作类型";
				return "ajax";
			}
		}else if(ConstantClass.BBMS_NET == StringUtil.getIntegerValue(servTypeId)){
			logger.debug("NET 业务");
			if ("1".equals(operateType)) {
				logger.debug("NET 开户业务");
				return "netOpen";
			} else if ("2".equals(operateType)) {
				logger.debug("EVDO 暂停业务");
				return "netPause";
			} else if ("3".equals(operateType)) {
				logger.debug("EVDO 销户业务");
				return "netStop";
			} else if ("4".equals(operateType)) {
				logger.debug("EVDO 复机业务");
				return "netRestart";
			} else {
				ajax = "未知操作类型";
				return "ajax";
			}
		}else {
			ajax = "未知业务类型";
			return "ajax";
		}
	}

	/**
	 * 发送工单
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-10-14
	 * @return String
	 */
	public String sendSheet() {
		logger.debug("sendSheet()");
		// 生成工单
		String strSheet = createOpenBssSheet();
		if(StringUtil.IsEmpty(strSheet)){
			ajax = "生成工单异常";
		}else{
			// 向工单接口发送工单
			ajax = sendBssSheetBio.sendSheet(strSheet);
			if("0".equals(ajax.substring(0, 1))){
				ajax = "成功 " + ajax;
			}else{
				ajax = "失败 " + ajax;
			}
		}
		return "sheetResult";
	}

	/**
	 * 生成BSS业务工单字符串
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-10-13
	 * @return String
	 */
	private String createOpenBssSheet() {
		logger.debug("createBssSheet()");
		StringBuffer sbuffer = new StringBuffer();
		// 前6个字段
		sbuffer.append(servTypeId + "|||");
		sbuffer.append(operateType + "|||");
		sbuffer.append(orderType + "|||");
		sbuffer.append(dealdate + "|||");
		//有些页面没有OUI和序列号自动，需要把null转为''
		sbuffer.append(StringUtil.getStringValue(oui) + "|||");
		sbuffer.append(StringUtil.getStringValue(devSn) + "|||");
		if (ConstantClass.BBMS_EVDO == StringUtil.getIntegerValue(servTypeId)) {
			logger.debug("EVDO 业务");
			if ("1".equals(operateType)) {
				logger.debug("EVDO 开户业务");
				sbuffer.append(devType + "|||");
				sbuffer.append(devModel + "|||");
				sbuffer.append(username + "|||");
				sbuffer.append(passwd + "|||");
				sbuffer.append(username + "|||");
				// 专线地址数
				sbuffer.append(1 + "|||");
				sbuffer.append(ip + "|||");
				sbuffer.append(mask + "|||");
				sbuffer.append(gateway + "|||");
				sbuffer.append(dns + "|||");
				sbuffer.append(maxDownSpeed + "|||");
				sbuffer.append(maxUpSpeed + "|||");
				sbuffer.append(maxUserNum + "|||");
				// VLANID
				sbuffer.append(vlanid + "|||");
				sbuffer.append(vpi + "|||");
				sbuffer.append(vci + "|||");
				sbuffer.append(cityId + "|||");
				sbuffer.append(officeId + "|||");
				sbuffer.append(linkman + "|||");
				sbuffer.append(linkphone + "|||");
				sbuffer.append(email + "|||");
				sbuffer.append(mobile + "|||");
				sbuffer.append(customerAddr + "|||");
				// 客户ID
				sbuffer.append(customerId + "|||");
				sbuffer.append(customerAccount + "|||");
				sbuffer.append(customerPasswd + "|||");
				sbuffer.append(customerName + "|||");
				sbuffer.append(serialCode + "|||");
				sbuffer.append(dataEsn + "|||");
				sbuffer.append(dataModel + "|||");
				sbuffer.append(uimImsi + "|||");
				sbuffer.append(uimIccid + "|||");
				sbuffer.append(uimMobile + "|||");
				//行业类别
				sbuffer.append(custType + "|||");
				sbuffer.append(workMode);
				sbuffer.append("LINKAGE");
				
			} else if ("2".equals(operateType) || "3".equals(operateType)
					|| "4".equals(operateType)) {
				logger.debug("EVDO 暂停，复机，销户业务");
				sbuffer.append(username + "|||");
				sbuffer.append(username + "|||");
				sbuffer.append(cityId + "|||");
				sbuffer.append(customerId);
				sbuffer.append("LINKAGE");
			} else {
				logger.debug("EVDO 未知操作类型");
				return null;
			}
		} else if (ConstantClass.BBMS_NET == StringUtil.getIntegerValue(servTypeId)) {
			logger.debug("宽带上网业务");
			if("1".equals(operateType)){
				logger.debug("开户");
				sbuffer.append(devType + "|||");
				sbuffer.append(devModel + "|||");
				sbuffer.append(username + "|||");
				sbuffer.append(passwd + "|||");
				sbuffer.append(username + "|||");
				// 专线地址数
				sbuffer.append(1 + "|||");
				sbuffer.append(ip + "|||");
				sbuffer.append(mask + "|||");
				sbuffer.append(gateway + "|||");
				sbuffer.append(dns + "|||");
				sbuffer.append(maxDownSpeed + "|||");
				sbuffer.append(maxUpSpeed + "|||");
				sbuffer.append(maxUserNum + "|||");
				// VLANID
				sbuffer.append(vlanid + "|||");
				sbuffer.append(vpi + "|||");
				sbuffer.append(vci + "|||");
				sbuffer.append(cityId + "|||");
				sbuffer.append(officeId + "|||");
				sbuffer.append(linkman + "|||");
				sbuffer.append(linkphone + "|||");
				sbuffer.append(email + "|||");
				sbuffer.append(mobile + "|||");
				sbuffer.append(customerAddr + "|||");
				// 客户ID
				sbuffer.append(customerId + "|||");
				sbuffer.append(customerAccount + "|||");
				sbuffer.append(customerPasswd + "|||");
				sbuffer.append(customerName + "|||");
				sbuffer.append(serialCode + "|||");
				//行业类别
				sbuffer.append(custType);
				sbuffer.append("LINKAGE");
			}else if("2".equals(operateType) || "3".equals(operateType)
					|| "4".equals(operateType)){
				logger.debug("NET 暂停，复机，销户业务");
				sbuffer.append(username + "|||");
				sbuffer.append(username + "|||");
				sbuffer.append(cityId + "|||");
				sbuffer.append(customerId);
				sbuffer.append("LINKAGE");
			}
		} else {
			logger.debug("未知业务");
			return null;
		}
		return sbuffer.toString();
	}

	/** getter,setter methods */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getVpi() {
		return vpi;
	}

	public void setVpi(String vpi) {
		this.vpi = vpi;
	}

	public String getVci() {
		return vci;
	}

	public void setVci(String vci) {
		this.vci = vci;
	}

	public String getVlanid() {
		return vlanid;
	}

	public void setVlanid(String vlanid) {
		this.vlanid = vlanid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getMaxUpSpeed() {
		return maxUpSpeed;
	}

	public void setMaxUpSpeed(String maxUpSpeed) {
		this.maxUpSpeed = maxUpSpeed;
	}

	public String getMaxDownSpeed() {
		return maxDownSpeed;
	}

	public void setMaxDownSpeed(String maxDownSpeed) {
		this.maxDownSpeed = maxDownSpeed;
	}

	public String getMaxUserNum() {
		return maxUserNum;
	}

	public void setMaxUserNum(String maxUserNum) {
		this.maxUserNum = maxUserNum;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getLinkphone() {
		return linkphone;
	}

	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDealdate() {
		return dealdate;
	}

	public void setDealdate(String dealdate) {
		this.dealdate = dealdate;
	}

	public List getCityList() {
		return cityList;
	}

	public List getOfficeList() {
		return officeList;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getAjax() {
		return ajax;
	}

	public String getServTypeId() {
		return servTypeId;
	}

	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getCustomerPasswd() {
		return customerPasswd;
	}

	public void setCustomerPasswd(String customerPasswd) {
		this.customerPasswd = customerPasswd;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddr() {
		return customerAddr;
	}

	public void setCustomerAddr(String customerAddr) {
		this.customerAddr = customerAddr;
	}

	public String getSerialCode() {
		return serialCode;
	}

	public void setSerialCode(String serialCode) {
		this.serialCode = serialCode;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public String getDevSn() {
		return devSn;
	}

	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}

	public String getDevModel() {
		return devModel;
	}

	public void setDevModel(String devModel) {
		this.devModel = devModel;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public String getDataEsn() {
		return dataEsn;
	}

	public void setDataEsn(String dataEsn) {
		this.dataEsn = dataEsn;
	}

	public String getDataModel() {
		return dataModel;
	}

	public void setDataModel(String dataModel) {
		this.dataModel = dataModel;
	}

	public String getUimImsi() {
		return uimImsi;
	}

	public void setUimImsi(String uimImsi) {
		this.uimImsi = uimImsi;
	}

	public String getUimIccid() {
		return uimIccid;
	}

	public void setUimIccid(String uimIccid) {
		this.uimIccid = uimIccid;
	}

	public String getUimMobile() {
		return uimMobile;
	}

	public void setUimMobile(String uimMobile) {
		this.uimMobile = uimMobile;
	}

	public void setSendBssSheetBio(SendBssSheetBIO sendBssSheetBio) {
		this.sendBssSheetBio = sendBssSheetBio;
	}

	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getWorkMode() {
		return workMode;
	}

	public void setWorkMode(String workMode) {
		this.workMode = workMode;
	}

	public List getCustTypeList() {
		return custTypeList;
	}

}
