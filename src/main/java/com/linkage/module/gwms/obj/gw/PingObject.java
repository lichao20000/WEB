package com.linkage.module.gwms.obj.gw;


/**
 * @author Jason(3412)
 * @date 2009-6-15
 */
public class PingObject {

	/** 设置值，设置为Requested，设备就会做相应的Ping操作 */
	private String DiagnosticsState = "Requested";
	/** 测试分组要使用的DiffServ codepoint,默认情况下CPE将该值设为0 */
	private String DSCP = "0";
	/** ping的目的地址,ip或者域名 */
	private String pingAddress;
	/** 测试执行的WAN或LAN的IP层接口 */
	private String devInterface;
	/** PING次数 */
	private int numOfRepetitions = 4;
	/** Ping测试超时的微秒数 */
	private long timeOut = 300;
	/** 每个Ping要发送的数据块大小(以字节计) */
	private long packageSize = 100;

	/** 成功次数 */
	private int successCount;
	/** 失败次数 */
	private int failureCount;
	/** AverageResponseTime平均响应时间 */
	private int averageResponseTime;
	/** 最小响应时间 */
	private int minimumResponseTime;
	/** 最大响应时间 */
	private int maximumResponseTime;

	/** ping检测结果 true成功 */
	private boolean success = false;

	private int faultCode = -9;
	/**
	 * 调用该构造方法，需要set相关ping参数
	 */
	public PingObject() {
		super();
	}

	/**
	 * 
	 * @param pingAddress
	 *            ping检测目的地址(域名或IP)
	 * @param devInterface
	 *            ping检测使用端口
	 * @param pingTimes
	 *            ping次数
	 * @param timeOut
	 *            超时时间(微妙)
	 * @param packageSize
	 *            包大小(字节)
	 */
	public PingObject(String pingAddress, String devInterface, int pingTimes,
			long timeOut, long packageSize) {
		this.pingAddress = pingAddress;
		// 去掉最后一个点
		if (devInterface.endsWith(".")) {
			this.devInterface = devInterface.substring(0,
					devInterface.length() - 1);
		} else {
			this.devInterface = devInterface;
		}
		this.numOfRepetitions = pingTimes;
		this.packageSize = packageSize;
		this.timeOut = timeOut;
	}

	/** getter, setter method* */
	public String getDiagnosticsState() {
		return DiagnosticsState;
	}

	public void setDiagnosticsState(String diagnosticsState) {
		DiagnosticsState = diagnosticsState;
	}

	public String getDSCP() {
		return DSCP;
	}

	public void setDSCP(String dscp) {
		DSCP = dscp;
	}

	public String getPingAddress() {
		return pingAddress;
	}

	public void setPingAddress(String pingAddress) {
		this.pingAddress = pingAddress;
	}

	public String getDevInterface() {
		return devInterface;
	}

	public void setDevInterface(String devInterface) {
		// 去掉最后一个点
		if (devInterface.endsWith(".")) {
			devInterface = devInterface.substring(0, devInterface.length() - 1);
		}
		this.devInterface = devInterface;
	}

	public int getNumOfRepetitions() {
		return numOfRepetitions;
	}

	public void setNumOfRepetitions(int numOfRepetitions) {
		this.numOfRepetitions = numOfRepetitions;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public long getPackageSize() {
		return packageSize;
	}

	public void setPackageSize(long packageSize) {
		this.packageSize = packageSize;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}

	public int getAverageResponseTime() {
		return averageResponseTime;
	}

	public void setAverageResponseTime(int averageResponseTime) {
		this.averageResponseTime = averageResponseTime;
	}

	public int getMinimumResponseTime() {
		return minimumResponseTime;
	}

	public void setMinimumResponseTime(int minimumResponseTime) {
		this.minimumResponseTime = minimumResponseTime;
	}

	public int getMaximumResponseTime() {
		return maximumResponseTime;
	}

	public void setMaximumResponseTime(int maximumResponseTime) {
		this.maximumResponseTime = maximumResponseTime;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	
	public int getFaultCode() {
		return faultCode;
	}

	public void setFaultCode(int faultCode) {
		this.faultCode = faultCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "端口：" + this.getDevInterface() + " 目的地址：" + this.pingAddress;
	}

}
