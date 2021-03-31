package com.linkage.module.gwms.obj.gw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jason(3412)
 * @date 2009-10-26
 */
public class TimeNtpOBJ {

	private static Logger logger = LoggerFactory
			.getLogger(TimeNtpOBJ.class);

	// 设备ID
	private String deviceId;
	// NTP服务器1
	private String ntpServer1;
	// NTP服务器2
	private String ntpServer2;
	// 时区偏移
	private String timezone;
	// 时区名称
	private String timezoneName;
	// 当前时间(设备上的采集值，字符串形式)
	private String currentTime;
	// CISCO设备特有的结点
	private String apply;
	// 使能(规范中)
	private int enable;
	// 采集时间
	private long gatherTime;

	/** setter, getter methods */

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getNtpServer1() {
		return ntpServer1;
	}

	public void setNtpServer1(String ntpServer1) {
		this.ntpServer1 = ntpServer1;
	}

	public String getNtpServer2() {
		return ntpServer2;
	}

	public void setNtpServer2(String ntpServer2) {
		this.ntpServer2 = ntpServer2;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getTimezoneName() {
		return timezoneName;
	}

	public void setTimezoneName(String timezoneName) {
		this.timezoneName = timezoneName;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getApply() {
		return apply;
	}

	public void setApply(String apply) {
		this.apply = apply;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public long getGatherTime() {
		return gatherTime;
	}

	public void setGatherTime(long gatherTime) {
		this.gatherTime = gatherTime;
	}

	@Override
	public String toString() {
		logger.debug("TimeNtpOBJ toSting()");
		return "deviceId:" + deviceId + ";ntpServer1:" + ntpServer1;
	}

}
