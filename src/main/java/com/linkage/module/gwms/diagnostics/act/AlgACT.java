/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.diagnostics.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.diagnostics.act.interf.I_AdvanceSearchACT;
import com.linkage.module.gwms.diagnostics.bio.AlgBIO;
import com.linkage.module.gwms.obj.gw.AlgOBJ;

/**
 * action:advance search.ALG,MWBAN,WAN,X_COM_Passwd.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 17, 2009
 * @see
 * @since 1.0
 */
public class AlgACT implements ServletRequestAware, I_AdvanceSearchACT {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(AlgACT.class);

	/** type of strategy:do now */
	private final int strategyType = 0;

	/** service_id */
	private final int serviceId = 105;

	/** request */
	private HttpServletRequest request;

	/** session */
	private HttpSession session;

	/** device_id */
	private String deviceId;

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

	/** BIO */
	private AlgBIO bio;

	/** ajax */
	private String ajax = "";

	/** result */
	private String result = "";
	
	private String gw_type = null;

	/**
	 * get data.
	 * 
	 * @return
	 */
	public String execute() {
		logger.debug("execute()");
		logger.warn("=======gw_type==="+gw_type+"==========");
		AlgOBJ algOBJ = bio.getData(deviceId);
		if (algOBJ != null) {
			logger.debug("[{}]:get data ok", this.deviceId);
			this.setH323Enab(algOBJ.getH323Enab());
			this.setIpsecEnab(algOBJ.getIpsecEnab());
			this.setL2tpEnab(algOBJ.getL2tpEnab());
			this.setRtspEnab(algOBJ.getRtspEnab());
			this.setSipEnab(algOBJ.getSipEnab());
		} else {
			logger.warn("[{}]:get data Error", this.deviceId);
		}

		this.result = bio.getResult();

		return "success";
	}
	
	/**
	 * ALG开关初始化，只有H323Enable，SIPEnable，RTSPEnable节点
	 *
	 * @author wangsenbo
	 * @date Dec 11, 2009
	 * @return String
	 */
	public String init() {
		logger.debug("execute()");

		AlgOBJ algOBJ = bio.getAlg(deviceId);
		if (algOBJ != null) {
			logger.debug("[{}]:get data ok", this.deviceId);
			this.setH323Enab(algOBJ.getH323Enab());
			this.setRtspEnab(algOBJ.getRtspEnab());
			this.setSipEnab(algOBJ.getSipEnab());
		} else {
			logger.warn("[{}]:get data Error", this.deviceId);
		}

		this.result = bio.getResult();

		return "alg";
	}
	/**
	 * 配置设备的ALG开关
	 *
	 * @author wangsenbo
	 * @date Dec 11, 2009
	 * @return String
	 */
	public String algConfig() {
		logger.debug("algConfig()");

		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();

		AlgOBJ algOBJ = new AlgOBJ();
		algOBJ.setDeviceId(deviceId);
		algOBJ.setH323Enab(h323Enab);
		algOBJ.setRtspEnab(rtspEnab);
		algOBJ.setSipEnab(sipEnab);

		if (bio.algConfig(algOBJ, accOId, this.strategyType, this.serviceId) == true) {
			ajax = "true";
		} else {
			ajax = "false";
		}

		this.result = bio.getResult();

		return "ajax";
	}


	/**
	 * config dev.
	 * 
	 * @return
	 */
	public String config() {
		logger.debug("config()");

		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();

		AlgOBJ algOBJ = new AlgOBJ();
		algOBJ.setDeviceId(deviceId);
		algOBJ.setH323Enab(h323Enab);
		algOBJ.setIpsecEnab(ipsecEnab);
		algOBJ.setL2tpEnab(l2tpEnab);
		algOBJ.setRtspEnab(rtspEnab);
		algOBJ.setSipEnab(sipEnab);

		ajax = bio.configDev(algOBJ, accOId, this.strategyType, this.serviceId, gw_type);

//		if ("1".equals(arrRet[0])) {
//			ajax = "true";
//		} else {
//			ajax = "false";
//		}

		this.result = bio.getResult();

		return "ajax";
	}

	/*
	 * set request
	 * 
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		logger.debug("setServletRequest(HttpServletRequest)");

		this.request = arg0;
	}

	/**
	 * set: BIO
	 * 
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(AlgBIO bio) {
		logger.debug("setBio(AdvanceSearchBIO)");

		this.bio = bio;
	}

	/**
	 * get:deviceId
	 * 
	 * @return the deviceId
	 */
	public String getDeviceId() {
		logger.debug("getDeviceId");

		return deviceId;
	}

	/**
	 * get:h323Enab
	 * 
	 * @return the h323Enab
	 */
	public int getH323Enab() {
		logger.debug("getDeviceId");

		return h323Enab;
	}

	/**
	 * get:sipEnab
	 * 
	 * @return the sipEnab
	 */
	public int getSipEnab() {
		logger.debug("getDeviceId");

		return sipEnab;
	}

	/**
	 * get:rtspEnab
	 * 
	 * @return the rtspEnab
	 */
	public int getRtspEnab() {
		logger.debug("getDeviceId");

		return rtspEnab;
	}

	/**
	 * get:l2tpEnab
	 * 
	 * @return the l2tpEnab
	 */
	public int getL2tpEnab() {
		logger.debug("getDeviceId");

		return l2tpEnab;
	}

	/**
	 * get:ipsecEnab
	 * 
	 * @return the ipsecEnab
	 */
	public int getIpsecEnab() {
		logger.debug("getDeviceId");

		return ipsecEnab;
	}

	/**
	 * set:deviceId
	 * 
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		logger.debug("setDeviceId({})", deviceId);

		this.deviceId = deviceId;
	}

	/**
	 * set:h323Enab
	 * 
	 * @param enab
	 *            the h323Enab to set
	 */
	public void setH323Enab(int enab) {
		logger.debug("setH323Enab({})", enab);

		h323Enab = enab;
	}

	/**
	 * set:sipEnab
	 * 
	 * @param sipEnab
	 *            the sipEnab to set
	 */
	public void setSipEnab(int sipEnab) {
		logger.debug("setSipEnab({})", sipEnab);

		this.sipEnab = sipEnab;
	}

	/**
	 * set:rtspEnab
	 * 
	 * @param rtspEnab
	 *            the rtspEnab to set
	 */
	public void setRtspEnab(int rtspEnab) {
		logger.debug("setRtspEnab({})", rtspEnab);

		this.rtspEnab = rtspEnab;
	}

	/**
	 * set:l2tpEnab
	 * 
	 * @param enab
	 *            the l2tpEnab to set
	 */
	public void setL2tpEnab(int enab) {
		logger.debug("setL2tpEnab({})", enab);

		l2tpEnab = enab;
	}

	/**
	 * set:ipsecEnab
	 * 
	 * @param ipsecEnab
	 *            the ipsecEnab to set
	 */
	public void setIpsecEnab(int ipsecEnab) {
		logger.debug("setIpsecEnab({})", ipsecEnab);

		this.ipsecEnab = ipsecEnab;
	}

	/**
	 * get:ajax
	 * 
	 * @return the ajax
	 */
	public String getAjax() {
		logger.debug("getAjax()");

		return ajax;
	}

	/**
	 * set:ajax
	 * 
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax) {
		logger.debug("setAjax({})", ajax);

		this.ajax = ajax;
	}

	/**
	 * get:
	 * @return the result
	 */
	public String getResult() {
		logger.debug("getResult()");

		return result;
	}

	/**
	 * set:
	 * @param result the result to set
	 */
	public void setResult(String result) {
		logger.debug("setResult({})", result);

		this.result = result;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

}
