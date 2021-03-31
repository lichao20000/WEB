/**
 * 
 */
package com.linkage.module.gwms.obj.gw;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-7-20
 * @category com.linkage.module.gwms.obj.gw
 * 
 */
public class GwTr069OBJ {

	/**
	 * 设备ID
	 */
	private String deviceId = null;
	
	/**
	 * PVC配置
	 */
	private String pvc = null;
	
	/**
	 * 时间
	 */
	private String time = null;
	
	/**
	 * ACS地址
	 */
	private String url = null;
	
	/**
	 * 是否周期上报
	 */
	private String periInformEnable = null;
	
	/**
	 * 周期上报间隔
	 */
	private String periInformInterval = null;
	
	/**
	 * 周期上报时间点
	 */
	private String periInformTime = null;
	
	/**
	 * 设备连接acs的用户名
	 */
	private String cpeUsername = null;
	
	/**
	 * 设备连接acs的密码
	 */
	private String cpePasswd = null;

	/**
	 * acs连接设备的用户名
	 */
	private String acsUsername = null;
	
	/**
	 * acs连接设备的密码
	 */
	private String acsPasswd = null;
	
	/**
	 * DHCP地址
	 */
	private String loopbackIp = null;
	
	/**
	 * @return the pvc
	 */
	public String getPvc() {
		return pvc;
	}

	/**
	 * @param pvc the pvc to set
	 */
	public void setPvc(String pvc) {
		this.pvc = pvc;
	}

	/**
	 * @return the cpePasswd
	 */
	public String getCpePasswd() {
		return cpePasswd;
	}

	/**
	 * @param cpePasswd the cpePasswd to set
	 */
	public void setCpePasswd(String cpePasswd) {
		this.cpePasswd = cpePasswd;
	}

	/**
	 * @return the cpeUsername
	 */
	public String getCpeUsername() {
		return cpeUsername;
	}

	/**
	 * @param cpeUsername the cpeUsername to set
	 */
	public void setCpeUsername(String cpeUsername) {
		this.cpeUsername = cpeUsername;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the periInformEnable
	 */
	public String getPeriInformEnable() {
		return periInformEnable;
	}

	/**
	 * @param periInformEnable the periInformEnable to set
	 */
	public void setPeriInformEnable(String periInformEnable) {
		this.periInformEnable = periInformEnable;
	}

	/**
	 * @return the periInformInterval
	 */
	public String getPeriInformInterval() {
		return periInformInterval;
	}

	/**
	 * @param periInformInterval the periInformInterval to set
	 */
	public void setPeriInformInterval(String periInformInterval) {
		this.periInformInterval = periInformInterval;
	}

	/**
	 * @return the periInformTime
	 */
	public String getPeriInformTime() {
		return periInformTime;
	}

	/**
	 * @param periInformTime the periInformTime to set
	 */
	public void setPeriInformTime(String periInformTime) {
		this.periInformTime = periInformTime;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the acsPasswd
	 */
	public String getAcsPasswd() {
		return acsPasswd;
	}

	/**
	 * @param acsPasswd the acsPasswd to set
	 */
	public void setAcsPasswd(String acsPasswd) {
		this.acsPasswd = acsPasswd;
	}

	/**
	 * @return the acsUsername
	 */
	public String getAcsUsername() {
		return acsUsername;
	}

	/**
	 * @param acsUsername the acsUsername to set
	 */
	public void setAcsUsername(String acsUsername) {
		this.acsUsername = acsUsername;
	}

	/**
	 * @return the loopbackIp
	 */
	public String getLoopbackIp() {
		return loopbackIp;
	}

	/**
	 * @param loopbackIp the loopbackIp to set
	 */
	public void setLoopbackIp(String loopbackIp) {
		this.loopbackIp = loopbackIp;
	}
	
}
