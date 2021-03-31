package com.linkage.module.gwms.util.message;

/**
 * 向配置模块发送消息对应的bean
 * @author jiafh (Ailk NO.)
 * @version 1.0
 * @since 2016-11-3
 * @category com.linkage.module.gwms.util.message
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class PreProcessMessage {
	
	// 方法名
	private String methodName;
	
	// serviceId
	private String serviceId;
	
	// 用户信息
	private UserBean[] userBeanArr;
	
	// 设备ID数组
	private String[] deviceIdIdArr;
	
	// 参数数组
	private String[] paramArr;
	
	// 策略ID数组
	private String[] strategyIdArr;
	
	//xml格式参数字符串
	private String paramXMLStr;
	
	//任务id
	private String taskId;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public UserBean[] getUserBeanArr() {
		return userBeanArr;
	}

	public void setUserBeanArr(UserBean[] userBeanArr) {
		this.userBeanArr = userBeanArr;
	}

	public String[] getParamArr() {
		return paramArr;
	}

	public void setParamArr(String[] paramArr) {
		this.paramArr = paramArr;
	}
	
	public String[] getDeviceIdIdArr() {
		return deviceIdIdArr;
	}

	public void setDeviceIdIdArr(String[] deviceIdIdArr) {
		this.deviceIdIdArr = deviceIdIdArr;
	}

	public String[] getStrategyIdArr() {
		return strategyIdArr;
	}

	public void setStrategyIdArr(String[] strategyIdArr) {
		this.strategyIdArr = strategyIdArr;
	}

	public String getParamXMLStr() {
		return paramXMLStr;
	}

	public void setParamXMLStr(String paramXMLStr) {
		this.paramXMLStr = paramXMLStr;
	}

	
	public String getTaskId()
	{
		return taskId;
	}

	
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
}
