package com.linkage.module.gwms.obj.gw;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.util.StringUtil;

/**
 * gw_qos_queue:InternetGatewayDevice.X_CT-COM_UplinkQoS.PriorityQueue.i.
 * 
 * @author gongsj
 * @date 2009-6-17
 */
public class QoSPriorityQueueOBJ {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(QoSOBJ.class);

	/** device_id */
	private String deviceId;

	/** gather time */
	private long gatherTime;

	/** id */
	private String queueId;

	/** enable */
	private String enable;

	/** 优先级 */
	private String priority;

	/** 权重 */
	private String weight;

	/**
	 * constructor
	 */
	public QoSPriorityQueueOBJ() {
		super();
	}

	/**
	 * constructor
	 */
	@SuppressWarnings("unchecked")
	public QoSPriorityQueueOBJ(Map map) {
		if (map == null) {
			logger.warn("WlanOBJ is null");

			return;
		}

		this.deviceId = StringUtil.getStringValue(map, "device_id");
		this.gatherTime = StringUtil.getLongValue(map, "gather_time");
		this.queueId = StringUtil.getStringValue(map, "queue_id");
		this.enable = StringUtil.getStringValue(map, "enable");
		this.priority = StringUtil.getStringValue(map, "priority");
		this.weight = StringUtil.getStringValue(map, "weight");
	}

	/**
	 * get:
	 * 
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * get:
	 * 
	 * @return the gatherTime
	 */
	public long getGatherTime() {
		return gatherTime;
	}

	/**
	 * get:
	 * 
	 * @return the queueId
	 */
	public String getQueueId() {
		return queueId;
	}

	/**
	 * get:
	 * 
	 * @return the enable
	 */
	public String getEnable() {
		return enable;
	}

	/**
	 * get:
	 * 
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * get:
	 * 
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * set:
	 * 
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * set:
	 * 
	 * @param gatherTime
	 *            the gatherTime to set
	 */
	public void setGatherTime(long gatherTime) {
		this.gatherTime = gatherTime;
	}

	/**
	 * set:
	 * 
	 * @param queueId
	 *            the queueId to set
	 */
	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}

	/**
	 * set:
	 * 
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(String enable) {
		this.enable = enable;
	}

	/**
	 * set:
	 * 
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * set:
	 * 
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * string to object.
	 */
	public String toString() {
		logger.debug("toString()");

		return "[" + deviceId + "] queueId=" + this.queueId + "|enable="
				+ this.enable;
	}

}
