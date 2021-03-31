/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.obj.gw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.obj.interf.I_DevConfOBJ;

/**
 * object:alg.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 17, 2009
 * @see
 * @since 1.0
 */
public class AlgOBJ implements I_DevConfOBJ {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(AlgOBJ.class);

	/** device_id */
	private String deviceId = null;

	/** gather_time */
	private long gatherTime = 0;

	/** H323 */
	private int h323Enab = 0;

	/** SIP */
	private int sipEnab = 0;

	/** RTSP */
	private int rtspEnab = 0;

	/** L2TP */
	private int l2tpEnab = 0;

	/** IPSEC */
	private int ipsecEnab = 0;

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
	 * get:
	 * 
	 * @return the gatherTime
	 */
	public long getGatherTime() {
		logger.debug("getGatherTime()");

		return gatherTime;
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
	 * get:
	 * 
	 * @return the h323Enab
	 */
	public int getH323Enab() {
		logger.debug("getH323Enab()");

		return h323Enab;
	}

	/**
	 * set:
	 * 
	 * @param enab
	 *            the h323Enab to set
	 */
	public void setH323Enab(int h323Enab) {
		logger.debug("setH323Enab({})", h323Enab);

		this.h323Enab = h323Enab;
	}

	/**
	 * get:
	 * 
	 * @return the sipEnab
	 */
	public int getSipEnab() {
		logger.debug("getSipEnab()");

		return sipEnab;
	}

	/**
	 * set:
	 * 
	 * @param sipEnab
	 *            the sipEnab to set
	 */
	public void setSipEnab(int sipEnab) {
		logger.debug("setSipEnab({})", sipEnab);

		this.sipEnab = sipEnab;
	}

	/**
	 * get:
	 * 
	 * @return the rtspEnab
	 */
	public int getRtspEnab() {
		logger.debug("getRtspEnab()");

		return rtspEnab;
	}

	/**
	 * set:
	 * 
	 * @param rtspEnab
	 *            the rtspEnab to set
	 */
	public void setRtspEnab(int rtspEnab) {
		logger.debug("setRtspEnab({})", rtspEnab);

		this.rtspEnab = rtspEnab;
	}

	/**
	 * get:
	 * 
	 * @return the l2tpEnab
	 */
	public int getL2tpEnab() {
		logger.debug("getL2tpEnab()");

		return l2tpEnab;
	}

	/**
	 * set:
	 * 
	 * @param enab
	 *            the l2tpEnab to set
	 */
	public void setL2tpEnab(int l2tpEnab) {
		logger.debug("setL2tpEnab({})", l2tpEnab);

		this.l2tpEnab = l2tpEnab;
	}

	/**
	 * get:
	 * 
	 * @return the ipsecEnab
	 */
	public int getIpsecEnab() {
		logger.debug("getIpsecEnab()");

		return ipsecEnab;
	}

	/**
	 * set:
	 * 
	 * @param ipsecEnab
	 *            the ipsecEnab to set
	 */
	public void setIpsecEnab(int ipsecEnab) {
		logger.debug("setIpsecEnab({})", ipsecEnab);

		this.ipsecEnab = ipsecEnab;
	}

	/** string to object */
	public String toString() {
		logger.debug("toString()");

		return "[" + deviceId + "]";
	}

}
