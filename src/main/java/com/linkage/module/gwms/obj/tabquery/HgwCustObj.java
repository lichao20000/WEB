package com.linkage.module.gwms.obj.tabquery;

/**
 * @author Jason(3412)
 * @date 2009-6-26
 */
public class HgwCustObj {

	//用户ID
	private String userId;
	//用户账号
	private String username;
	//设备ID
	private String deviceId;
	//OUI
	private String oui;
	//设备序列号
	private String deviceSerial;
	//身份证号码
	private String credno;
	//用户姓名
	private String realname;
	//属地ID
	private String cityId;
	//属地名称
	private String cityName;
	//局向ID
	private String officeId;
	//局向名称
	private String officeName;
	
	//网关类型名字
	private String   typeName;
	
	
	public String getTypeName()
	{
		return typeName;
	}

	
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	/** 下面的信息慢慢转换为从业务用户表中获取*/
	//业务类型ID
	private String servTypeId;
	//PVC
	private String vpiid;
	private String vciid;
	//VlanID
	private String vlanid;

	private String passwd;
	private String telepone;
	private long upSpeed;
	private long downSpeed;
	private int maxUserNum;
	
	private String zoneId;
	private String linkman;
	private String linkphone;
	private String email;
	private String address;
	private String mobile;
	
	private long dealdate;
	private int wanType;
	
	private String ip;
	private String mask;
	private String gateway;
	private String dns;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public int getWanType() {
		return wanType;
	}

	public void setWanType(int wanType) {
		this.wanType = wanType;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getLinkphone() {
		return linkphone;
	}

	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public long getUpSpeed() {
		return upSpeed;
	}

	public void setUpSpeed(long upSpeed) {
		this.upSpeed = upSpeed;
	}

	public long getDownSpeed() {
		return downSpeed;
	}

	public void setDownSpeed(long downSpeed) {
		this.downSpeed = downSpeed;
	}

	public int getMaxUserNum() {
		return maxUserNum;
	}

	public void setMaxUserNum(int maxUserNum) {
		this.maxUserNum = maxUserNum;
	}

	public String getTelepone() {
		return telepone;
	}

	public void setTelepone(String telepone) {
		this.telepone = telepone;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/** *getter setter */
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getServTypeId() {
		return servTypeId;
	}

	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	public String getVpiid() {
		return vpiid;
	}

	public void setVpiid(String vpiid) {
		this.vpiid = vpiid;
	}

	public String getVciid() {
		return vciid;
	}

	public void setVciid(String vciid) {
		this.vciid = vciid;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public String getDeviceSerial() {
		return deviceSerial;
	}

	public void setDeviceSerial(String deviceSerial) {
		this.deviceSerial = deviceSerial;
	}

	public String getCredno() {
		return credno;
	}

	public void setCredno(String credno) {
		this.credno = credno;
	}

	public String getVlanid() {
		return vlanid;
	}

	public void setVlanid(String vlanid) {
		this.vlanid = vlanid;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public long getDealdate() {
		return dealdate;
	}

	public void setDealdate(long dealdate) {
		this.dealdate = dealdate;
	}

}
