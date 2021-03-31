package com.linkage.module.gwms.obj.gw;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.config.obj.StrategyQosParaOBJ;
import com.linkage.module.gwms.util.StringUtil;

/**
 * gw_qos. InternetGatewayDevice.X_CT-COM_UplinkQoS.
 * 
 * @author gongsj
 * @date 2009-6-17
 */
public class QoSOBJ {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(QoSOBJ.class);

	/** device_id */
	private String deviceId;

	/** gather time */
	private long gatherTime;

	/** Enable */
	private String enable;

	/** 模式 */
	private String mode;

	/** Plan */
	private String plan;

	/** 带宽 */
	private String bandwidth;

	/** 强制带宽 */
	private String enabWidth;

	/** DSCP */
	private String enabDscp;

	/** 802-1_P */
	private String enab802p;
	
	//Qos配置class的结点对象
	private QoSClassificationOBJ[] qosCalss;
	//Qos配置App的结点对象
	private QoSAppOBJ[] qosApp;
	//Qos的配置参数对象
	private StrategyQosParaOBJ[] qosParam;

	//type, 0:老的模板  1:新的模板, 老的模板只需要配置Mode和Enable两个结点。
	private int type = 1;
	
	/**
	 * constructor
	 */
	public QoSOBJ() {
		super();
	}

	/**
	 * constructor
	 */
	@SuppressWarnings("unchecked")
	public QoSOBJ(Map map) {

		if (map == null) {
			logger.warn("WlanOBJ is null");

			return;
		}

		this.deviceId = StringUtil.getStringValue(map, "device_id");
		this.gatherTime = StringUtil.getLongValue(map, "gather_time");
		this.enable = StringUtil.getStringValue(map, "enable");
		this.mode = StringUtil.getStringValue(map, "qos_mode");
		this.plan = StringUtil.getStringValue(map, "qos_plan");
		this.bandwidth = StringUtil.getStringValue(map, "bandwidth");
		this.enabWidth = StringUtil.getStringValue(map, "enab_width");
		this.enabDscp = StringUtil.getStringValue(map, "enab_dscp");
		this.enab802p = StringUtil.getStringValue(map, "enab_802p");
	}

	/**
	 * deviceId
	 * 
	 * @return
	 */
	public String getDeviceId() {
		logger.debug("getDeviceId()");

		return deviceId;
	}

	/**
	 * set deviceId.
	 * 
	 * @param deviceId
	 */
	public void setDeviceId(String deviceId) {
		logger.debug("setDeviceId({})", deviceId);

		this.deviceId = deviceId;
	}

	/**
	 * 
	 * @return
	 */
	public long getGatherTime() {
		logger.debug("getGatherTime()");

		return gatherTime;
	}

	/**
	 * 
	 * @param gatherTime
	 */
	public void setGatherTime(long gatherTime) {
		logger.debug("setGatherTime({})", gatherTime);

		this.gatherTime = gatherTime;
	}

	/**
	 * 
	 * @return
	 */
	public String getEnable() {
		logger.debug("getEnable()");

		return enable;
	}

	/**
	 * 
	 * @param enable
	 */
	public void setEnable(String enable) {
		logger.debug("setEnable({})", enable);

		this.enable = enable;
	}

	/**
	 * 
	 * @return
	 */
	public String getMode() {
		logger.debug("getMode()");

		return mode;
	}

	/**
	 * 
	 * @param mode
	 */
	public void setMode(String mode) {
		logger.debug("setMode({})", mode);

		this.mode = mode;
	}

	/**
	 * 
	 * @return
	 */
	public String getPlan() {
		logger.debug("getPlan()");

		return plan;
	}

	/**
	 * 
	 * @param plan
	 */
	public void setPlan(String plan) {
		logger.debug("setPlan({})", plan);

		this.plan = plan;
	}

	/**
	 * 
	 * @return
	 */
	public String getBandwidth() {
		logger.debug("getBandwidth()");

		return bandwidth;
	}

	/**
	 * 
	 * @param bandwidth
	 */
	public void setBandwidth(String bandwidth) {
		logger.debug("setBandwidth({})", bandwidth);

		this.bandwidth = bandwidth;
	}

	/**
	 * 
	 * @return
	 */
	public String getEnabWidth() {
		logger.debug("getEnabWidth()");

		return enabWidth;
	}

	/**
	 * 
	 * @param enabWidth
	 */
	public void setEnabWidth(String enabWidth) {
		logger.debug("setEnabWidth({})", enabWidth);

		this.enabWidth = enabWidth;
	}

	/**
	 * 
	 * @return
	 */
	public String getEnabDscp() {
		logger.debug("getEnabDscp()");

		return enabDscp;
	}

	/**
	 * 
	 * @param enabDscp
	 */
	public void setEnabDscp(String enabDscp) {
		logger.debug("setEnabDscp({})", enabDscp);

		this.enabDscp = enabDscp;
	}

	/**
	 * 
	 * @return
	 */
	public String getEnab802p() {
		logger.debug("getEnab802p()");

		return enab802p;
	}

	/**
	 * 
	 * @param enab802p
	 */
	public void setEnab802p(String enab802p) {
		logger.debug("setEnab802p({})", enab802p);

		this.enab802p = enab802p;
	}

	
	public QoSClassificationOBJ[] getQosCalss() {
		return qosCalss;
	}

	public void setQosCalss(QoSClassificationOBJ[] qosCalss) {
		this.qosCalss = qosCalss;
	}

	public QoSAppOBJ[] getQosApp() {
		return qosApp;
	}

	public void setQosApp(QoSAppOBJ[] qosApp) {
		this.qosApp = qosApp;
	}

	
	public StrategyQosParaOBJ[] getQosParam() {
		return qosParam;
	}

	public void setQosParam(StrategyQosParaOBJ[] qosParam) {
		this.qosParam = qosParam;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * string to object.
	 */
	public String toString() {
		logger.debug("toString()");

		return "[" + deviceId + "] enable=" + this.enable + "|mode="
				+ this.mode + "|plan=" + this.plan;
	}
}
