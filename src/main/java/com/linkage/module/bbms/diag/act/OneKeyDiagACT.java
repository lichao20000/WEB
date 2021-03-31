package com.linkage.module.bbms.diag.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.bbms.diag.bio.OneKeyDiagBIO;

/**
 * @author chenjie
 * @date 2011-8-5
 */
public class OneKeyDiagACT {

	private static Logger logger = LoggerFactory.getLogger(OneKeyDiagACT.class);

	private OneKeyDiagBIO bio;
	
	/**
	 * 设备ID
	 */
	private String deviceId;
	
	private String gw_type;
	
	private String ajax;
	
	private String dataBlockSize;
	
	private String host;
	
	private String numberOfRepetitions;
	
	private String timeout;
	
	private String url;
	
	private String httpVersion;
	
	private String traceRoutType;
	
	private String dnsServerIP;
	
	private String domainName;
	
	private String maxHopCount;
	
	private String dslWan;
	
	/**
	 * DSL
	 * @return
	 */
	public String dslDiag()
	{
		logger.debug("dslDiag()");
		ajax = bio.DSLList(deviceId, dslWan, 2);
		return "ajax";
	}
	
	/**
	 * PING
	 * @return
	 */
	public String pingDiag()
	{
		String Interface = bio.getIntetface(deviceId);
		ajax = bio.allPingResult(deviceId, Interface, dataBlockSize, host, numberOfRepetitions, timeout);
		return "ajax";
	} 
	
	/**
	 * HTTPGET
	 * @return
	 */
	public String httpgetDiag()
	{
		String Interface = bio.getIntetface(deviceId);
		ajax = bio.allHttpGetResult(deviceId, Interface, httpVersion, url, numberOfRepetitions, timeout);
		return "ajax";
	}
	
	/**
	 * TRACEROUT
	 * @return
	 */
	public String traceroutDiag()
	{
		String Interface = bio.getIntetface(deviceId);
		ajax = bio.traceRoute(deviceId, Interface, host, maxHopCount, timeout);
		return "ajax";
	}
	
	/**
	 * DNSQUERY
	 * @return
	 */
	public String dnsqueryDiag()
	{
		String Interface = bio.getIntetface(deviceId);
		ajax = bio.allDNSQueryResult(deviceId, Interface, dnsServerIP, domainName, numberOfRepetitions, timeout);
		return "ajax";
	}
	
	public String getDSLWans()
	{
		ajax = bio.getDslWAN(deviceId);
		return "ajax";
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public OneKeyDiagBIO getBio() {
		return bio;
	}

	public void setBio(OneKeyDiagBIO bio) {
		this.bio = bio;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getDataBlockSize() {
		return dataBlockSize;
	}

	public void setDataBlockSize(String dataBlockSize) {
		this.dataBlockSize = dataBlockSize;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getNumberOfRepetitions() {
		return numberOfRepetitions;
	}

	public void setNumberOfRepetitions(String numberOfRepetitions) {
		this.numberOfRepetitions = numberOfRepetitions;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	
	public String getMaxHopCount() {
		return maxHopCount;
	}

	public void setMaxHopCount(String maxHopCount) {
		this.maxHopCount = maxHopCount;
	}

	public String getDnsServerIP() {
		return dnsServerIP;
	}

	public void setDnsServerIP(String dnsServerIP) {
		this.dnsServerIP = dnsServerIP;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getTraceRoutType() {
		return traceRoutType;
	}

	public void setTraceRoutType(String traceRoutType) {
		this.traceRoutType = traceRoutType;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDslWan() {
		return dslWan;
	}

	public void setDslWan(String dslWan) {
		this.dslWan = dslWan;
	}
}