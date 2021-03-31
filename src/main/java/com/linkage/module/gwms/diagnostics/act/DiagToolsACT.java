package com.linkage.module.gwms.diagnostics.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.diagnostics.bio.DiagToolsBIO;
import com.linkage.module.gwms.obj.gw.PingObject;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.ACSCorba;

/**
 * @author Jason(3412)
 * @date 2009-7-13
 */
public class DiagToolsACT implements SessionAware{
	private static Logger logger = LoggerFactory
	.getLogger(DiagToolsACT.class);
	//设备ID
	private String deviceId;
	//ping目的地址
	private String pingAddr;
	//包大小
	private String packageSize;
	//ping 次数
	private String times;
	//码流数据
	private List streamList;
	//返回
	private String ajax;
	
	/** 终端类型 */
	private String gw_type = null;
	
	//BIO
	private DiagToolsBIO diagToolsBio;
	
	private Map session;

	
	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	
	/**
	 * @param session the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * 页面初始化
	 */
	public String execute(){
		return "diagTools";
	}
	
	/**
	 * 重启
	 */
	public String reboot(){
		logger.debug("reboot...");
		int flg = diagToolsBio.devReboot(deviceId, gw_type);
		if(1 == flg){
			ajax = "重启成功";
		}else{
			String fail = Global.G_Fault_Map.get(flg).getFaultReason();
			ajax = "<font color=red>重启失败：" + fail + "</font>";
		}
		return "ajax";
	}
	
	/**
	 * 恢复出厂设置
	 */
	public String factoryReset(){
		logger.debug("factoryReset...");
		int flg = diagToolsBio.devReset(deviceId, gw_type);
		if(1 == flg){
			ajax = "恢复出厂设置成功";
		}else{
			String fail = Global.G_Fault_Map.get(flg).getFaultReason();
			ajax = "<font color=red>恢复出厂设置失败：" + fail + "</font>";
		}
		return "ajax";
	}
	
	/**
	 * ping操作
	 */
	public String ping(){
		logger.debug("ping...");
		logger.debug("pingAddr" + pingAddr);
		PingObject obj = diagToolsBio.devPing(deviceId, pingAddr, 
				StringUtil.getIntegerValue(packageSize), StringUtil.getIntegerValue(times));
		if(null == obj){
			ajax = "<font color=red>ping检查失败：未获取到上网WAN口信息或者未连接</font>";
		}else{
			if(obj.isSuccess()){
				ajax = " 成功数：" + obj.getSuccessCount() + " 失败数：" + obj.getFailureCount()
				+ "  平均时延：" + obj.getAverageResponseTime() + "ms  最大时延：" + obj.getMaximumResponseTime()
				+ "ms  最小时延：" + obj.getMinimumResponseTime() + "ms";
			}else{
				String fail = Global.G_Fault_Map.get(obj.getFaultCode()).getFaultReason();
				ajax = "<font color=red>ping检查失败：" + fail + "</font>";
			}
		}
		return "ajax";
	}
	
	/**
	 * 上传日志文件
	 */
	public String upLogFile(){
		logger.debug("upLogFile...");
		UserRes user = (UserRes) session.get("curUser");
		int ir = diagToolsBio.uploadLogFile(deviceId,user.getUser().getId(), gw_type);
		ajax = String.valueOf(ir);
		return "ajax";
	}
	
	/**
	 * 开启码流分析工具
	 */
	public String openStream(){
		logger.debug("openStream()");
		//通知ACS，开启码流记录
		int r = new ACSCorba(gw_type).chgInfo(1, 1,deviceId);
		if(1 == r){
			ajax = "开启成功";
		}else{
			ajax = "开启失败";
		}
		return "ajax";
	}
	
	/**
	 * 关闭码流分析工具
	 */
	public String closeStream(){
		logger.debug("closeStream()");
		//通知ACS，关闭码流记录
		int r = new ACSCorba(gw_type).chgInfo(0, 1,deviceId);
		if(1 == r){
			ajax = "关闭成功";
		}else{
			ajax = "关闭失败";
		}
		return "ajax";
	}
	
	/**
	 * 清除ACS与设备交互记录
	 */
	public String clearStream(){
		logger.debug("clearStream()");
		//清除码流记录
		int r = diagToolsBio.clearAcsStream(deviceId);
		if(r >= 0){
			ajax = "清除成功";
		}else{
			ajax = "清除失败";
		}
		return "ajax";
	}
	
	/**
	 * 查看ACS与设备交互内容
	 */
	public String showInterStream(){
		logger.debug("showInterStream()");
		//查询gw_acs_stream表，展示交互内容
		streamList = diagToolsBio.getAcsStream(deviceId);
		return "showStream";
	}
	
	
	/** getter,setter method fields**/
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getPingAddr() {
		return pingAddr;
	}

	public void setPingAddr(String pingAddr) {
		this.pingAddr = pingAddr;
	}

	
	public String getPackageSize() {
		return packageSize;
	}

	public void setPackageSize(String packageSize) {
		this.packageSize = packageSize;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public List getStreamList() {
		return streamList;
	}

	public void setDiagToolsBio(DiagToolsBIO diagToolsBio) {
		this.diagToolsBio = diagToolsBio;
	}

	public String getAjax() {
		return ajax;
	}


	public String getGw_type() {
		return gw_type;
	}


	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

}
