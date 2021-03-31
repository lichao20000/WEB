package com.linkage.module.gwms.sysConfig.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.sysConfig.bio.ResourceBindMemConfigBIO;
import com.linkage.module.gwms.sysConfig.obj.User4HInfoOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;

public class ResourceBindMemConfigACT {
	
	//日志记录
	private static Logger logger = LoggerFactory.getLogger(ResourceBindMemConfigACT.class);
	/**
	 * 用户名类型 "1" 逻辑SN, "2" 宽带号码, "3" IPTV号码, "4" VoIP认证号码, "5" VoIP电话号码
	 */
	private String usernameType;
	/** 用户名 */
	private String username = null;
	private String type = null;
	private String operate = null;
	private List<Map> userList = null;
	
	private User4HInfoOBJ userDbMap = null;
	
	private User4HInfoOBJ userMemMap = null;
	
	private String ajax = null;
	
	/** 终端类型 */
	private String gw_type = null;
	
	ResourceBindMemConfigBIO bio = null;
	
	public String getUserInfo(){

		if(null==username || "".equals(username) || 
				null==usernameType || "".equals(usernameType)){
			return "userInfo";
		}
		if(1==LipossGlobals.SystemType()||0==LipossGlobals.SystemType()){
			this.userList = bio.getUserInfo4H(usernameType, username);
		}else{
			//TODO
			logger.warn("针对此系统的还没开发");
		}
		logger.warn("userList="+userList);
		return "userInfo";
	}
	
	public String getUserDetail(){
		logger.warn("gw_type="+gw_type);
		if(1==LipossGlobals.SystemType()||0==LipossGlobals.SystemType()){
			this.userDbMap = bio.getUser4HDbDetail(username);
			this.userMemMap = bio.getUser4HMemDetail(username, gw_type);
			if(null!=this.userDbMap){
				this.username = this.userDbMap.getUsername();
			}else if(null!=this.userMemMap){
				this.username = this.userMemMap.getUsername();
			}else{
				this.username = null;
			}
		}else{
			//TODO
			logger.warn("针对此系统的还没开发");
		}
		return "userDetail";
	}
	
	public String getDeviceDetail() {
		if(1==LipossGlobals.SystemType()||0==LipossGlobals.SystemType()){
			String dbInfo = bio.getUserBind4HDbDetail(username);
			String memInfo = bio.getDevice4HMemDetail(username, gw_type);
			StringBuffer sb = new StringBuffer();
			if(null==dbInfo){
				sb.append(username);
				sb.append("数据库中未绑定用户！");
			}else{
				sb.append(username);
				sb.append("数据库中绑定用户");
				sb.append(dbInfo);
				sb.append("!");
			}
			if(null==memInfo){
				sb.append(username);
				sb.append("缓存中未绑定用户！");
			}else{
				sb.append(username);
				sb.append("缓存中绑定用户");
				sb.append(memInfo);
				sb.append("!");
			}
			this.ajax = sb.toString();
		}else{
			//TODO
			logger.warn("针对此系统的还没开发");
		}
		return "ajax";
	}
	
	public String configMem(){
		if(null==type || null==operate || null==username){
			this.ajax = "参数错误";
			return "ajax";
		}
		if(1==LipossGlobals.SystemType()||0==LipossGlobals.SystemType()){
			//bio.test(type, operate, username, gw_type);
			CreateObjectFactory.createResourceBind(gw_type).doTest(type, operate, username);
		}else{
			//TODO
			logger.warn("针对此系统的还没开发");
		}
		this.ajax = "成功";
		return "ajax";
	}
	
	public ResourceBindMemConfigBIO getBio() {
		return bio;
	}

	public void setBio(ResourceBindMemConfigBIO bio) {
		this.bio = bio;
	}

	public List<Map> getUserList() {
		return userList;
	}

	public void setUserList(List<Map> userList) {
		this.userList = userList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsernameType() {
		return usernameType;
	}

	public void setUsernameType(String usernameType) {
		this.usernameType = usernameType;
	}

	public User4HInfoOBJ getUserDbMap() {
		return userDbMap;
	}

	public void setUserDbMap(User4HInfoOBJ userDbMap) {
		this.userDbMap = userDbMap;
	}

	public User4HInfoOBJ getUserMemMap() {
		return userMemMap;
	}
	
	public void setUserMemMap(User4HInfoOBJ userMemMap) {
		this.userMemMap = userMemMap;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	
}
