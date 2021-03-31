package com.linkage.litms.paramConfig;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PvcObj {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(PvcObj.class);

	/** 设备编号 */
	private String device_id;
	/** WAN编号 */
	private String wan_id;
	/** WAN连接编号 */
	private String wan_conn_id;
	/** 采集时间 */
	private String gather_time;
	/** VLAN */
	private String vlan_id;
	/** WANIPConnectionNumberOfEntries */
	// WANDevice.1.WANConnectionDevice.1.WANIPConnectionNumberOfEntries
	private String ip_conn_num;
	/** VPI */
	private String vpi_id;
	/** VCI */
	private String vci_id;
	/** WANPPPConnectionNumberOfEntries */
	// WANDevice.1.WANConnectionDevice.1.WANPPPConnectionNumberOfEntries
	private String ppp_conn_num;

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
		if (gather_time == null || gather_time.length() == 0) {
			gather_time = "" + (new Date()).getTime() / 1000;
		}
		return gather_time;
	}

	public void setGather_time(String gather_time) {
		this.gather_time = gather_time;
	}

	public String getVlan_id() {
		if (vlan_id == null || vlan_id.length() == 0) {
			vlan_id = null;
		}
		return vlan_id;
	}

	public void setVlan_id(String vlan_id) {
		this.vlan_id = vlan_id;
	}

	public String getIp_conn_num() {
		if (ip_conn_num == null || ip_conn_num.length() == 0) {
			ip_conn_num = "0";
		}
		return ip_conn_num;
	}

	public void setIp_conn_num(String ip_conn_num) {
		this.ip_conn_num = ip_conn_num;
	}

	public String getVpi_id() {
		if (vpi_id == null || vpi_id.length() == 0) {
			vpi_id = null;
		}
		return vpi_id;
	}

	public void setVpi_id(String vpi_id) {
		this.vpi_id = vpi_id;
	}

	public String getVci_id() {
		if (vci_id == null || vci_id.length() == 0) {
			vci_id = null;
		}
		return vci_id;
	}

	public void setVci_id(String vci_id) {
		this.vci_id = vci_id;
	}

	public String getPpp_conn_num() {
		if (ppp_conn_num == null || ppp_conn_num.length() == 0) {
			ppp_conn_num = "0";
		}
		return ppp_conn_num;
	}

	public void setPpp_conn_num(String ppp_conn_num) {
		this.ppp_conn_num = ppp_conn_num;
	}

	/**
	 * return object.
	 */
	public String toString() {
		logger.debug("toString()");

		return device_id + "_" + wan_id + "_" + wan_conn_id;
	}

}
