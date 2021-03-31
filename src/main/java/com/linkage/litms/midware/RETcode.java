package com.linkage.litms.midware;


/**
 * 
 * @author Zhaof
 * 
 */

public enum RETcode {
	SUCCESS(0), CONF_FILE_ERROR(-1), SERVER_CONNECTION_ERROR(1), SPECIAL(2), UNKNOW_ERROR(
			3), AAA_ERROR(10000), DATA_FORMAT_ERROR(10001), DEVICE_EXIST(10002), DEVICE_NOT_EXIST(
			10003), BUSSINESS_OPEN(10101), BUSSINESS_NOT_OPEN(10102), PARAM_ERROR(
			10201), GATEWAY_CONNECTION_ERROR(20001), ADD_MODULE_ERROR(20002), REMOVE_MODULE_ERROR(
			20003), UPDATE_MODULE_ERROR(20004), SET_MODULE_PARAM_ERROR(20005);

	private int value;

	RETcode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static String getDescription(int value) {
		RETcode[] retCodes = RETcode.values();
		for (int i = 0; i < retCodes.length; i++) {
			int value_ = retCodes[i].getValue();
			if (value_ == value) {
				return retCodes[i].getDescription();
			}
		}
		return "未定义错误类型";
	}

	public String getDescription() {
		switch (this) {
			case SUCCESS :
				return "成功";
			case CONF_FILE_ERROR :
				return "配置文件错误";
			case SERVER_CONNECTION_ERROR :
				return "服务器连接失败";
			case SPECIAL :
				return "()";
			case UNKNOW_ERROR :
				return "未知错误";
			case AAA_ERROR :
				return "授权校验失败";
			case DATA_FORMAT_ERROR :
				return "数据格式错误";
			case DEVICE_EXIST :
				return "设备已存在";
			case DEVICE_NOT_EXIST :
				return "设备不存在";
			case BUSSINESS_OPEN :
				return "业务已开通";
			case BUSSINESS_NOT_OPEN :
				return "业务未开通";
			case PARAM_ERROR :
				return "参数错误";
			case GATEWAY_CONNECTION_ERROR :
				return "网关连接失败";
			case ADD_MODULE_ERROR :
				return "模块新增失败";
			case REMOVE_MODULE_ERROR :
				return "模块卸载失败";
			case UPDATE_MODULE_ERROR :
				return "模块升级失败";
			case SET_MODULE_PARAM_ERROR :
				return "模块参数设置失败";
			default :
				return "未定义错误类型";
		}
	}

	private static void pl(Object s) {
	}

	public static void main(String[] args) {
		// samples
	}
}
