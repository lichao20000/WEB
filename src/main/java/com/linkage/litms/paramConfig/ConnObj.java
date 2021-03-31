package com.linkage.litms.paramConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnObj {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ConnObj.class);

	private String device_id;

	private String wan_id;

	private String wan_conn_id;

	private String wan_conn_sess_id;

	private String gather_time;

	private String sess_type;

	private NodeObj[] nodeObj;

	/** 节点类型 */
	public static String[] NodeList = new String[] { "enable", "name",
			"conn_type", "serv_list", "bind_port", "username", "password",
			"ip_type", "ip", "mask", "gateway", "dns_enab", "dns" };

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getWan_id() {
		return wan_id;
	}

	public void setWan_id(String wan_id) {
		this.wan_id = wan_id;
	}

	public String getWan_conn_id() {
		return wan_conn_id;
	}

	public void setWan_conn_id(String wan_conn_id) {
		this.wan_conn_id = wan_conn_id;
	}

	public String getGather_time() {
		return gather_time;
	}

	public void setGather_time(String gather_time) {
		this.gather_time = gather_time;
	}

	public String getWan_conn_sess_id() {
		return wan_conn_sess_id;
	}

	public void setWan_conn_sess_id(String wan_conn_sess_id) {
		this.wan_conn_sess_id = wan_conn_sess_id;
	}

	public String getSess_type() {
		return sess_type;
	}

	public void setSess_type(String sess_type) {
		this.sess_type = sess_type;
	}

	public NodeObj[] getNodeObj() {
		return nodeObj;
	}

	public void setNodeObj(NodeObj[] nodeObj) {
		this.nodeObj = nodeObj;
	}

	/**
	 * return object.
	 */
	public String toString() {
		logger.debug("toString()");

		return device_id + "_" + wan_id + "_" + wan_conn_id + "_"
				+ wan_conn_sess_id;
	}
}
