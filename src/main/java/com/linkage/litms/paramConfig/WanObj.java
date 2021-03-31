/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.litms.paramConfig;

import java.util.Date;

/**
 * Object for WAN.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Dec 23, 2008
 * @see
 * @since 1.0
 */
public class WanObj {

	/** 设备编号 */
	private String devId = null;
	/** WAN编号 */
	private String wanId = null;
	/** 采集时间 */
	private String gatherTime = null;
	/** 上行方式 */
	private String accessType = null;
	/** 连接数量 */
	private String wanConnNum = null;

	/**
	 * get:
	 * 
	 * @return the devId
	 */
	public String getDevId() {
		return devId;
	}

	/**
	 * set:
	 * 
	 * @param devId
	 *            the devId to set
	 */
	public void setDevId(String devId) {
		this.devId = devId;
	}

	/**
	 * get:
	 * 
	 * @return the wanId
	 */
	public String getWanId() {
		return wanId;
	}

	/**
	 * set:
	 * 
	 * @param wanId
	 *            the wanId to set
	 */
	public void setWanId(String wanId) {
		this.wanId = wanId;
	}

	/**
	 * get:
	 * 
	 * @return the gatherTime
	 */
	public String getGatherTime() {
		if (gatherTime == null || gatherTime.length() == 0) {
			gatherTime = "" + (new Date()).getTime() / 1000;
		}

		return gatherTime;
	}

	/**
	 * set:
	 * 
	 * @param gatherTime
	 *            the gatherTime to set
	 */
	public void setGatherTime(String gatherTime) {
		this.gatherTime = gatherTime;
	}

	/**
	 * get:
	 * 
	 * @return the accessType
	 */
	public String getAccessType() {
		if (accessType == null || gatherTime.length() == 0) {
			accessType = "DSL";
		}
		return accessType;
	}

	/**
	 * set:
	 * 
	 * @param accessType
	 *            the accessType to set
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	/**
	 * get:
	 * 
	 * @return the wanConnNum
	 */
	public String getWanConnNum() {
		if (wanConnNum == null || wanConnNum.length() == 0) {
			gatherTime = "1";
		}

		return wanConnNum;
	}

	/**
	 * set:
	 * 
	 * @param wanConnNum
	 *            the wanConnNum to set
	 */
	public void setWanConnNum(String wanConnNum) {
		this.wanConnNum = wanConnNum;
	}
}
