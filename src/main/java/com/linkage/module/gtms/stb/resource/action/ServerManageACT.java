package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gtms.stb.resource.serv.ServerManageBIO;


public class ServerManageACT extends splitPageAction
{
	
	private static Logger logger = LoggerFactory.getLogger(ServerManageACT.class);
	
	private String serverId = null;
	
	// 服务器名称
	private String serverName = null;
	private String serverNameAdd = null;
	
	// 服务器URL
	private String serverUrl = null;
	private String serverUrlAdd = null;
	
	// 用户名
	private String accessUser = null;
	private String accessUserAdd = null;
	
	// 密码
	private String accessPasswd = null;
	private String accessPasswdAdd = null;
	
	// 服务器类型
	private String fileType = null;
	private String fileTypeAdd = null;
	
	private String ajax = null;
	
	private List<Map> serverList = null;
	
	
	private ServerManageBIO serverManageBIO;
	
	
	/**
	 * 初始化查询页面
	 * @return
	 */
	public String init() {
		
		logger.debug("ServerManageACT==>init()");
		return "queryForm";
	}
	
	

	/**
	 * 查询机顶盒运营画面服务器
	 * 
	 * @return
	 */
	public String serverQueryList() {
		
		logger.debug("ServerManageACT==>serverQueryList()");
		logger.warn("===>机顶盒运营画面服务器管理--查询");
		serverList = serverManageBIO.queryServer(curPage_splitPage, num_splitPage, serverName, serverUrl);
		maxPage_splitPage = serverManageBIO.countServerNum(curPage_splitPage, num_splitPage, serverName, serverUrl);
		
		return "queryList";
	}
	
	
	/**
	 * 新增 机顶盒运营画面服务器
	 * 
	 * @return
	 */
	public String addServer() {
		
		logger.debug("ServerManageACT==>addServer()");
		logger.warn("===>机顶盒运营画面服务器管理--新增");
		ajax = serverManageBIO.addServer(serverNameAdd, accessUserAdd, accessPasswdAdd, serverUrlAdd, fileTypeAdd);
		return "ajax";
	}
	
	/**
	 * 修改
	 * @return
	 */
	public String editServer() {
		logger.debug("ServerManageACT==>editServer()");
		logger.warn("===>机顶盒运营画面服务器管理--编辑");
		ajax = serverManageBIO.editServer(serverId, serverNameAdd, accessUserAdd, accessPasswdAdd, serverUrlAdd, fileTypeAdd);
		return "ajax";
	}


	/**
	 * 删除
	 * 
	 * @return
	 */
	public String deleServer() {
		logger.debug("ServerManageACT==>deleServer()");
		logger.warn("===>机顶盒运营画面服务器管理--删除");
		ajax = serverManageBIO.deleServer(serverId);
		return "ajax";
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getAccessUser() {
		return accessUser;
	}

	public void setAccessUser(String accessUser) {
		this.accessUser = accessUser;
	}

	public String getAccessPasswd() {
		return accessPasswd;
	}

	public void setAccessPasswd(String accessPasswd) {
		this.accessPasswd = accessPasswd;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public ServerManageBIO getServerManageBIO() {
		return serverManageBIO;
	}

	public void setServerManageBIO(ServerManageBIO serverManageBIO) {
		this.serverManageBIO = serverManageBIO;
	}

	public List getServerList() {
		return serverList;
	}
	
	public void setServerList(List<Map> serverList) {
		this.serverList = serverList;
	}

	public String getServerNameAdd() {
		return serverNameAdd;
	}

	public void setServerNameAdd(String serverNameAdd) {
		try{
			this.serverNameAdd = java.net.URLDecoder.decode(serverNameAdd, "UTF-8");
		} catch (Exception e){
			this.serverNameAdd = serverNameAdd;
		}
	}

	public String getServerUrlAdd() {
		return serverUrlAdd;
	}

	public void setServerUrlAdd(String serverUrlAdd) {
		this.serverUrlAdd = serverUrlAdd;
	}

	public String getAccessUserAdd() {
		return accessUserAdd;
	}

	public void setAccessUserAdd(String accessUserAdd) {
		try{
			this.accessUserAdd = java.net.URLDecoder.decode(accessUserAdd, "UTF-8");
		} catch (Exception e){
			this.accessUserAdd = accessUserAdd;
		}
	}

	public String getAccessPasswdAdd() {
		return accessPasswdAdd;
	}

	public void setAccessPasswdAdd(String accessPasswdAdd) {
		this.accessPasswdAdd = accessPasswdAdd;
	}

	public String getFileTypeAdd() {
		return fileTypeAdd;
	}

	public void setFileTypeAdd(String fileTypeAdd) {
		this.fileTypeAdd = fileTypeAdd;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
}
