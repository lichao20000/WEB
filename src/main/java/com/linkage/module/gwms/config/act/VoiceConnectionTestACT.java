package com.linkage.module.gwms.config.act;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.config.bio.VoiceConnectionTestBIO;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;

public class VoiceConnectionTestACT {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(VoiceConnectionTestACT.class);
	
	VoiceConnectionTestBIO bio = null;

	/**
	 * 获取值
	 */
	/**
	 * PhoneConnectivityTest/X_CT-COM_SimulateTest
	 */
	public String testSelector = null;
	/**
	 * 设备Id
	 */
	public String deviceId = null;
	/**
	 * 主叫仿真时的被叫号码
	 */
	public String calledNumber = null;
	/**
	 * 仿真测试类型
	 * "Caller" 主叫仿真
	 * "Called" 被叫仿真
	 * "None" 取消仿真
	 */
	public String testType = null;
	/**
	 * 1、0
	 */
	public String dailDTMFConfirmEnable = null;
	/**
	 * 根据文本款输入(只能是*#0-9的字符)
	 */
	public String dailDTMFConfirmNumber = null;
	/**
	 * 1、2（代表端口）
	 */
	public String voipLine = null;
	
	/**
	 * 定制成功/定制失败
	 * 连接/未连接
	 * Complete###Success###true#CallerFailReason
	 */
	public String ajax = null;
	
	public String gw_type = null;
	
	/**
	 * 设置诊断
	 * @return
	 */
	public String setValue(){
		
		logger.debug("setValue()");
		
		int _temp = 0;
		if("PhoneConnectivityTest".equals(testSelector)){
			_temp = bio.setPhoneConnectivityTest(deviceId, voipLine, gw_type);
		}else{
			_temp = bio.setX_CT_COM_SimulateTest(deviceId,voipLine,calledNumber,testType,dailDTMFConfirmEnable,dailDTMFConfirmNumber, gw_type);
		}
		if(1==_temp){
			this.ajax = "定制成功!";
		}else{
			this.ajax = "定制失败!";
		}
		return "ajax";
	}
	
	/**
	 * 获取诊断结果
	 * 
	 * @return
	 */
	public String getValue(){
		
		logger.debug("getValue()");
		ArrayList<ParameValueOBJ> objList = null;
		if("PhoneConnectivityTest".equals(testSelector)){
			objList = bio.getPhoneConnectivityTest(deviceId, voipLine, gw_type);
			if(null!=objList){
				this.ajax = objList.get(0).getValue();
			}else{
				this.ajax = "获取失败";
			}
		}else{
			objList = bio.getX_CT_COM_SimulateTest(deviceId,voipLine,gw_type);
			if(null!=objList){
				this.ajax = objList.get(0).getValue() + "###" +
							objList.get(1).getValue() + "###" +
							objList.get(2).getValue() + "###" +
							objList.get(3).getValue();
			}else{
				this.ajax = "获取失败###获取失败###获取失败###获取失败";
			}
		}
		return "ajax";
	}
	
	public VoiceConnectionTestBIO getBio() {
		return bio;
	}

	public void setBio(VoiceConnectionTestBIO bio) {
		this.bio = bio;
	}

	public String getCalledNumber() {
		return calledNumber;
	}

	public void setCalledNumber(String calledNumber) {
		this.calledNumber = calledNumber;
	}

	public String getDailDTMFConfirmEnable() {
		return dailDTMFConfirmEnable;
	}

	public void setDailDTMFConfirmEnable(String dailDTMFConfirmEnable) {
		this.dailDTMFConfirmEnable = dailDTMFConfirmEnable;
	}

	public String getDailDTMFConfirmNumber() {
		return dailDTMFConfirmNumber;
	}

	public void setDailDTMFConfirmNumber(String dailDTMFConfirmNumber) {
		this.dailDTMFConfirmNumber = dailDTMFConfirmNumber;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getVoipLine() {
		return voipLine;
	}

	public void setVoipLine(String voipLine) {
		this.voipLine = voipLine;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getTestSelector() {
		return testSelector;
	}

	public void setTestSelector(String testSelector) {
		this.testSelector = testSelector;
	}

	
	public String getGw_type() {
		return gw_type;
	}

	
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
}
