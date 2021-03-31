/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.obj.gw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.obj.interf.I_DevConfOBJ;

/**
 * object:MWBAND.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 17, 2009
 * @see
 * @since 1.0
 */
public class MwbandOBJ implements I_DevConfOBJ {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(MwbandOBJ.class);

	/** device_id */
	private String deviceId = null;

	/** gather_time */
	private long gatherTime = 0;

	/** mode */
	private int mode = 0;

	/** total_number */
	private int totalNumber = 0;

	/** stb_enable */
	private int stbEnable = 0;

	/** stb_number */
	private int stbNumber = 0;

	/** camera_enable */
	private int cameraEnable = 0;

	/** camera_number */
	private int cameraNumber = 0;

	/** computer_enable */
	private int computerEnable = 0;

	/** computer_number */
	private int computerNumber = 0;

	/** phone_enable */
	private int phoneEnable = 0;

	/** phone_number */
	private int phoneNumber = 0;

	/**
	 * get:
	 * 
	 * @return the deviceId
	 */
	public String getDeviceId() {
		logger.debug("getDeviceId()");

		return deviceId;
	}

	/**
	 * get:
	 * 
	 * @return the gatherTime
	 */
	public long getGatherTime() {
		logger.debug("getGatherTime()");

		return gatherTime;
	}

	/**
	 * get:
	 * 
	 * @return the mode
	 */
	public int getMode() {
		logger.debug("getMode()");

		return mode;
	}

	/**
	 * get:
	 * 
	 * @return the totalNumber
	 */
	public int getTotalNumber() {
		logger.debug("getTotalNumber()");

		return totalNumber;
	}

	/**
	 * get:
	 * 
	 * @return the stbEnable
	 */
	public int getStbEnable() {
		logger.debug("getStbEnable()");

		return stbEnable;
	}

	/**
	 * get:
	 * 
	 * @return the stbNumber
	 */
	public int getStbNumber() {
		logger.debug("getStbNumber()");

		return stbNumber;
	}

	/**
	 * get:
	 * 
	 * @return the cameraEnable
	 */
	public int getCameraEnable() {
		logger.debug("getCameraEnable()");

		return cameraEnable;
	}

	/**
	 * get:
	 * 
	 * @return the cameraNumber
	 */
	public int getCameraNumber() {
		logger.debug("getCameraNumber()");

		return cameraNumber;
	}

	/**
	 * get:
	 * 
	 * @return the computerEnable
	 */
	public int getComputerEnable() {
		logger.debug("getComputerEnable()");

		return computerEnable;
	}

	/**
	 * get:
	 * 
	 * @return the computerNumber
	 */
	public int getComputerNumber() {
		logger.debug("getComputerNumber()");

		return computerNumber;
	}

	/**
	 * get:
	 * 
	 * @return the phoneEnable
	 */
	public int getPhoneEnable() {
		logger.debug("getPhoneEnable()");

		return phoneEnable;
	}

	/**
	 * get:
	 * 
	 * @return the phoneNumber
	 */
	public int getPhoneNumber() {
		logger.debug("getPhoneNumber()");

		return phoneNumber;
	}

	/**
	 * set:
	 * 
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		logger.debug("setDeviceId({})", deviceId);

		this.deviceId = deviceId;
	}

	/**
	 * set:
	 * 
	 * @param gatherTime
	 *            the gatherTime to set
	 */
	public void setGatherTime(long gatherTime) {
		logger.debug("setGatherTime({})", gatherTime);

		this.gatherTime = gatherTime;
	}

	/**
	 * set:
	 * 
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(int mode) {
		logger.debug("setMode({})", mode);

		this.mode = mode;
	}

	/**
	 * set:
	 * 
	 * @param totalNumber
	 *            the totalNumber to set
	 */
	public void setTotalNumber(int totalNumber) {
		logger.debug("setTotalNumber({})", totalNumber);

		this.totalNumber = totalNumber;
	}

	/**
	 * set:
	 * 
	 * @param stbEnable
	 *            the stbEnable to set
	 */
	public void setStbEnable(int stbEnable) {
		logger.debug("setStbEnable({})", stbEnable);

		this.stbEnable = stbEnable;
	}

	/**
	 * set:
	 * 
	 * @param stbNumber
	 *            the stbNumber to set
	 */
	public void setStbNumber(int stbNumber) {
		logger.debug("setStbNumber({})", stbNumber);

		this.stbNumber = stbNumber;
	}

	/**
	 * set:
	 * 
	 * @param cameraEnable
	 *            the cameraEnable to set
	 */
	public void setCameraEnable(int cameraEnable) {
		logger.debug("setCameraEnable({})", cameraEnable);

		this.cameraEnable = cameraEnable;
	}

	/**
	 * set:
	 * 
	 * @param cameraNumber
	 *            the cameraNumber to set
	 */
	public void setCameraNumber(int cameraNumber) {
		logger.debug("setCameraNumber({})", cameraNumber);

		this.cameraNumber = cameraNumber;
	}

	/**
	 * set:
	 * 
	 * @param computerEnable
	 *            the computerEnable to set
	 */
	public void setComputerEnable(int computerEnable) {
		logger.debug("setComputerEnable({})", computerEnable);

		this.computerEnable = computerEnable;
	}

	/**
	 * set:
	 * 
	 * @param computerNumber
	 *            the computerNumber to set
	 */
	public void setComputerNumber(int computerNumber) {
		logger.debug("setComputerNumber({})", computerNumber);

		this.computerNumber = computerNumber;
	}

	/**
	 * set:
	 * 
	 * @param phoneEnable
	 *            the phoneEnable to set
	 */
	public void setPhoneEnable(int phoneEnable) {
		logger.debug("setPhoneEnable({})", phoneEnable);

		this.phoneEnable = phoneEnable;
	}

	/**
	 * set:
	 * 
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(int phoneNumber) {
		logger.debug("setPhoneNumber({})", phoneNumber);

		this.phoneNumber = phoneNumber;
	}

	/**
	 * string to object.
	 */
	public String toString() {
		logger.debug("toString()");

		String tmp = "[" + deviceId + "] ";

		tmp += "mode=" + mode;

		tmp += "totalNumber=" + totalNumber;

		return tmp;
	}

}
