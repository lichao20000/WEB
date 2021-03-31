package com.linkage.module.bbms.diag.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.bbms.diag.bio.DeviceDiagBIO;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.diagnostics.obj.DiagResult;
import com.linkage.module.gwms.obj.gw.PingObject;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-6-15
 */
public class DeviceDiagACT {

	private static Logger logger = LoggerFactory
			.getLogger(DeviceDiagACT.class);

	/**
	 * 设备ID
	 */
	private String deviceId;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 诊断类型:有线无法上网，无线无法上网，上网速度慢，异常掉线多，IPTV无法使用
	 */
	private String diagType;
	/**
	 * 订单类型
	 */
	private String orderType;

	// ping地址类型：dnsAddress, special Address, specialDomain
	private String pingAddrType;

	/** 域名的ping操作 */
	private String pingAddr;
	private String packageSize;
	private String pingTimes;
	private String timeOut;

	/** DNS地址的ping操作 */
	private String pingAddrDns;
	private String packageSizeDns;
	private String pingTimesDns;
	private String timeOutDns;

	/** IP地址的ping操作 */
	private String pingAddrSpecial;
	private String packageSizeSpecial;
	private String pingTimesSpecial;
	private String timeOutSpecial;

	/** IP地址的ping操作 */
	private String pingAddrLan;
	private String packageSizeLan;
	private String pingTimesLan;
	private String timeOutLan;
	private String pingInterLan;

	// 业务类型
	private String servTypeId;
	// 返回字符串值
	private String ajax;
	// 采集类型
	private String gatherType = "1";
	// //1:诊断通过,继续诊断; -1:诊断到异常; -2诊断失败
	// private String pass;
	// //故障描述
	// private String faultDesc;
	// //诊断建议
	// private String suggest;
	// //诊断失败描述
	// private String failture;
	// 返回结果
	private List<Map<String, String>> resultList;
	// action执行结果
	public static String AJAX = "ajax";
	// BIO
	private DeviceDiagBIO diagBio;
	// 1表示有线, 2表示无线
	private int wireType;

	private DiagResult diagResult;
	
	/** action method fields */

	/**
	 * 初始化页面
	 */
	public String execute() {
		logger.debug("execute()");
		userId = diagBio.getUserId(deviceId);
		// 初始化ping操作各个参数
		Map pingMap = diagBio.initPingParam();
		pingAddrDns = StringUtil.getStringValue(pingMap.get("dnsAddress"));
		pingAddrSpecial = StringUtil.getStringValue(pingMap
				.get("specialAddress"));
		pingAddr = StringUtil.getStringValue(pingMap.get("specialDomain"));

		packageSizeSpecial = StringUtil.getStringValue(pingMap
				.get("packgeSize"));
		timeOutSpecial = StringUtil.getStringValue(pingMap.get("timeOut"));
		pingTimesSpecial = StringUtil.getStringValue(pingMap.get("pingTimes"));

		packageSize = StringUtil.getStringValue(pingMap.get("packgeSize"));
		timeOut = StringUtil.getStringValue(pingMap.get("timeOut"));
		pingTimes = StringUtil.getStringValue(pingMap.get("pingTimes"));

		packageSizeDns = StringUtil.getStringValue(pingMap.get("packgeSize"));
		timeOutDns = StringUtil.getStringValue(pingMap.get("timeOut"));
		pingTimesDns = StringUtil.getStringValue(pingMap.get("pingTimes"));

		packageSizeLan = StringUtil.getStringValue(pingMap.get("packgeSize"));
		timeOutLan = StringUtil.getStringValue(pingMap.get("timeOut"));
		pingTimesLan = StringUtil.getStringValue(pingMap.get("pingTimes"));

		servTypeId = StringUtil.getStringValue(ConstantClass.BBMS_NET);
		// 默认有线无法上网
		String act = "wiredNetDiag";
		if ("wiredNetDiag".equals(diagType)) {
			// 有线无法上网
		} else if ("wirelessNetDiag".equals(diagType)) {
			// 无线无法上网
			act = "wirelessNetDiag";
		} else if ("netSlowDiag".equals(diagType)) {
			// 上网速度慢
			act = "netSlowDiag";
		} else if ("netOfflineDiag".equals(diagType)) {
			// 异常掉线多
			act = "netOfflineDiag";
		} else if ("diagTools".equals(diagType)) {
			// 诊断工具
			act = "diagTools";
		} else if ("iptvDiag".equals(diagType)) {
			// iptv无法上网
			act = "iptvDiag";
		}
		return act;
	}

	/**
	 * 链路信息检查
	 */
	public String wireInfoCheck() {
		diagResult = diagBio.getWireInfo(deviceId);
		return "wireInfo";
	}

	/**
	 * 业务信息检查
	 */
	public String servParamCheck() {
		diagResult = diagBio.servParamCheck(deviceId, userId, servTypeId);
		return "servInfo";
	}

	/**
	 * ping检查
	 */
	public String pingCheck() {
		if ("lanPing".equals(pingAddrType)) {
			// diagResult = diagBio.lanPing(deviceId);
			PingObject pingObj = new PingObject();
			pingObj.setPingAddress(pingAddrLan);
			pingObj.setNumOfRepetitions(StringUtil
					.getIntegerValue(pingTimesLan));
			pingObj.setPackageSize(StringUtil.getIntegerValue(packageSizeLan));
			pingObj.setTimeOut(StringUtil.getIntegerValue(timeOutLan));
			diagResult = diagBio.lanPing(deviceId, pingObj);
		} else {
			// diagBio.getWanDevice(deviceId);
			PingObject pingObj = new PingObject();
			if ("dnsAddress".equals(pingAddrType)) {
				pingObj.setPingAddress(pingAddrDns);
				pingObj.setNumOfRepetitions(StringUtil
						.getIntegerValue(pingTimesDns));
				pingObj.setPackageSize(StringUtil
						.getIntegerValue(packageSizeDns));
				pingObj.setTimeOut(StringUtil.getIntegerValue(timeOutDns));
				diagResult = diagBio.pingCheck(deviceId, userId, servTypeId,
						pingAddrType, pingObj);
			} else if ("specialDomain".equals(pingAddrType)) {
				pingObj.setPingAddress(pingAddr);
				pingObj.setNumOfRepetitions(StringUtil
						.getIntegerValue(pingTimes));
				pingObj.setPackageSize(StringUtil.getIntegerValue(packageSize));
				pingObj.setTimeOut(StringUtil.getIntegerValue(timeOut));
				diagResult = diagBio.pingCheck(deviceId, userId, servTypeId,
						pingAddrType, pingObj);
			} else {
				// "specialDomain".equals(pingAddrType)
				pingObj.setPingAddress(pingAddr);
				pingObj.setNumOfRepetitions(StringUtil
						.getIntegerValue(pingTimesSpecial));
				pingObj.setPackageSize(StringUtil
						.getIntegerValue(packageSizeSpecial));
				pingObj.setTimeOut(StringUtil.getIntegerValue(timeOutSpecial));
				diagResult = diagBio.pingCheck(deviceId, userId, servTypeId,
						pingAddrType, pingObj);
			}
		}
		return "pingInfo";
	}

	/**
	 * PC连接检查
	 */
	public String lanHostCheck() {
		if (2 == wireType) {
			diagResult = diagBio.wlanHostCheck(deviceId);
			return "wlanInfo";
		} else {
			diagResult = diagBio.lanHostCheck(deviceId);
			return "lanInfo";
		}
	}

	/**
	 * DHCP检查
	 */
	public String dhcpCheck() {
		diagResult = diagBio.dhcpCheck(deviceId, wireType);
		return "dhcpInfo";
	}
	
	/**
	 * WLAN故障诊断
	 * @return
	 */
	public String wlanDiagInfo()
	{
		diagResult = diagBio.wlanDiagInfo(deviceId);
		return "wlanDiagInfo";
	}	


	/**
	 * getter(), setter() method fields
	 */

	public String getDiagType() {
		return diagType;
	}

	public void setDiagType(String diagType) {
		this.diagType = diagType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setDiagBio(DeviceDiagBIO diagBio) {
		this.diagBio = diagBio;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getServTypeId() {
		return servTypeId;
	}

	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	public String getPingAddrType() {
		return pingAddrType;
	}

	public void setPingAddrType(String pingAddrType) {
		this.pingAddrType = pingAddrType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGatherType() {
		return gatherType;
	}

	public void setGatherType(String gatherType) {
		this.gatherType = gatherType;
	}

	public List<Map<String, String>> getResultList() {
		return resultList;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getPingAddr() {
		return pingAddr;
	}

	public void setPingAddr(String pingAddr) {
		this.pingAddr = pingAddr;
	}

	public String getPackageSize() {
		return packageSize;
	}

	public void setPackageSize(String packageSize) {
		this.packageSize = packageSize;
	}

	public String getPingTimes() {
		return pingTimes;
	}

	public void setPingTimes(String pingTimes) {
		this.pingTimes = pingTimes;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public String getPingAddrDns() {
		return pingAddrDns;
	}

	public void setPingAddrDns(String pingAddrDns) {
		this.pingAddrDns = pingAddrDns;
	}

	public String getPackageSizeDns() {
		return packageSizeDns;
	}

	public void setPackageSizeDns(String packageSizeDns) {
		this.packageSizeDns = packageSizeDns;
	}

	public String getPingTimesDns() {
		return pingTimesDns;
	}

	public void setPingTimesDns(String pingTimesDns) {
		this.pingTimesDns = pingTimesDns;
	}

	public String getTimeOutDns() {
		return timeOutDns;
	}

	public void setTimeOutDns(String timeOutDns) {
		this.timeOutDns = timeOutDns;
	}

	public String getPingAddrSpecial() {
		return pingAddrSpecial;
	}

	public void setPingAddrSpecial(String pingAddrSpecial) {
		this.pingAddrSpecial = pingAddrSpecial;
	}

	public String getPackageSizeSpecial() {
		return packageSizeSpecial;
	}

	public void setPackageSizeSpecial(String packageSizeSpecial) {
		this.packageSizeSpecial = packageSizeSpecial;
	}

	public String getPingTimesSpecial() {
		return pingTimesSpecial;
	}

	public void setPingTimesSpecial(String pingTimesSpecial) {
		this.pingTimesSpecial = pingTimesSpecial;
	}

	public String getTimeOutSpecial() {
		return timeOutSpecial;
	}

	public void setTimeOutSpecial(String timeOutSpecial) {
		this.timeOutSpecial = timeOutSpecial;
	}

	public void setWireType(String wireType) {
		this.wireType = Integer.valueOf(wireType);
	}

	public String getPingAddrLan() {
		return pingAddrLan;
	}

	public void setPingAddrLan(String pingAddrLan) {
		this.pingAddrLan = pingAddrLan;
	}

	public String getPackageSizeLan() {
		return packageSizeLan;
	}

	public void setPackageSizeLan(String packageSizeLan) {
		this.packageSizeLan = packageSizeLan;
	}

	public String getPingTimesLan() {
		return pingTimesLan;
	}

	public void setPingTimesLan(String pingTimesLan) {
		this.pingTimesLan = pingTimesLan;
	}

	public String getTimeOutLan() {
		return timeOutLan;
	}

	public void setTimeOutLan(String timeOutLan) {
		this.timeOutLan = timeOutLan;
	}

	public String getPingInterLan() {
		return pingInterLan;
	}

	public void setPingInterLan(String pingInterLan) {
		this.pingInterLan = pingInterLan;
	}

	public DiagResult getDiagResult() {
		return diagResult;
	}

}