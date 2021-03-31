package com.linkage.module.gwms.report.act;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.report.bio.AutoBindReportBIO;

/**
 * @author Jason(3412)
 * @date 2009-8-6
 */
public class AutoBindReportACT {

	private static Logger logger = LoggerFactory
		.getLogger(AutoBindReportACT.class);
	
	//用户账号
	private String username;
	//设备序列号
	private String devSn;
	//返回页面的list结果
	private List reportList;
	//导入文件,根据此文件解析用户账号
	private File userfile;
	
	AutoBindReportBIO autoBindReportBio;
	/**
	 * 初始化
	 */
	public String execute(){
		logger.debug("execute()");
		return "success";
	}
	
	/**
	 * 查询用户绑定，以及回综调的情况
	 */
	public String queryUser(){
		logger.debug("queryUser()");
		reportList = autoBindReportBio.queryBindUser(username);
		return "dataList";
	}
	
	/**
	 * 查询文件中所有用户绑定，以及回综调的情况
	 */
	public String queryUserList(){
		logger.debug("queryUser()");
		reportList = autoBindReportBio.queryBindUser(userfile);
		return "dataList";
	}
	
	/** getter,setter methods**/
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDevSn() {
		return devSn;
	}

	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}

	public List getReportList() {
		return reportList;
	}
	

	public void setUserfile(File userfile) {
		this.userfile = userfile;
	}

	public void setAutoBindReportBio(AutoBindReportBIO autoBindReportBio) {
		this.autoBindReportBio = autoBindReportBio;
	}
	
}
