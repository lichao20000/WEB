package com.linkage.module.gtms.blocTest.obj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * E8C VOIP业务工单对象
 * 
 * @author Bell(8835)
 * @date 2012年5月15日 17:06:35
 */
public class VoipSheetOBJ {

	public static Logger logger = LoggerFactory
			.getLogger(VoipSheetOBJ.class);

    //user_id
	private String userId;
	// voip认证帐号
	private String voipUsername;
	// voip认证密码
	private String voipPasswd;
	// SIP服务器地址
	private String sipIp;
	// SIP服务器端口
	private int sipPort;
	// SIP备用服务器地址
	private String standSipIp;
	// SIP备用服务器端口
	private int standSipPort;
	// 终端开通的线路端口：V1, V2
	private String linePort;
	// 业务电话号码
	private String voipTelepone;
	/**
	 * 江苏电信ims协议语音工单参数    mod by zhangsm 2011-10-12
	 */
	//注册地址
	private String registrarServer;
	//注册端口
	private int registrarServerPort;
	//备用注册地址
	private String standRegistrarServer;
	//备用注册端口
	private int standRegistrarServerPort;
	//外部绑定地址
	private String outboundProxy;
	//外部绑定端口
	private int outboundPort;
	//备用外部绑定地址
	private String standOutboundProxy;
	//备用外部绑定端口
	private int standOutboundPort;
	//协议类型
	private int protocol = 1;

	/** getter, setter methods */
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getVoipUsername() {
		return voipUsername;
	}

	public void setVoipUsername(String voipUsername) {
		this.voipUsername = voipUsername;
	}

	public String getVoipPasswd() {
		return voipPasswd;
	}

	public void setVoipPasswd(String voipPasswd) {
		this.voipPasswd = voipPasswd;
	}

	public String getSipIp() {
		return sipIp;
	}

	public void setSipIp(String sipIp) {
		this.sipIp = sipIp;
	}

	public String getStandSipIp() {
		return standSipIp;
	}

	public void setStandSipIp(String standSipIp) {
		this.standSipIp = standSipIp;
	}

	public String getLinePort() {
		return linePort;
	}

	public void setLinePort(String linePort) {
		this.linePort = linePort;
	}

	public int getSipPort() {
		return sipPort;
	}

	public void setSipPort(int sipPort) {
		this.sipPort = sipPort;
	}

	public int getStandSipPort() {
		return standSipPort;
	}

	public void setStandSipPort(int standSipPort) {
		this.standSipPort = standSipPort;
	}

	public String getVoipTelepone() {
		return voipTelepone;
	}

	public void setVoipTelepone(String voipTelepone) {
		this.voipTelepone = voipTelepone;
	}
	
	public String getRegistrarServer() {
		return registrarServer;
	}

	public void setRegistrarServer(String registrarServer) {
		this.registrarServer = registrarServer;
	}

	public int getRegistrarServerPort() {
		return registrarServerPort;
	}

	public void setRegistrarServerPort(int registrarServerPort) {
		this.registrarServerPort = registrarServerPort;
	}

	public String getStandRegistrarServer() {
		return standRegistrarServer;
	}

	public void setStandRegistrarServer(String standRegistrarServer) {
		this.standRegistrarServer = standRegistrarServer;
	}

	public int getStandRegistrarServerPort() {
		return standRegistrarServerPort;
	}

	public void setStandRegistrarServerPort(int standRegistrarServerPort) {
		this.standRegistrarServerPort = standRegistrarServerPort;
	}

	public int getOutboundPort()
	{
		return outboundPort;
	}

	
	public void setOutboundPort(int outboundPort)
	{
		this.outboundPort = outboundPort;
	}


	
	public String getOutboundProxy() {
		return outboundProxy;
	}

	public void setOutboundProxy(String outboundProxy) {
		this.outboundProxy = outboundProxy;
	}

	public String getStandOutboundProxy() {
		return standOutboundProxy;
	}

	public void setStandOutboundProxy(String standOutboundProxy) {
		this.standOutboundProxy = standOutboundProxy;
	}

	public int getStandOutboundPort()
	{
		return standOutboundPort;
	}

	
	public void setStandOutboundPort(int standOutboundPort)
	{
		this.standOutboundPort = standOutboundPort;
	}

	
	public int getProtocol()
	{
		return protocol;
	}

	
	public void setProtocol(int protocol)
	{
		this.protocol = protocol;
	}

	

}
