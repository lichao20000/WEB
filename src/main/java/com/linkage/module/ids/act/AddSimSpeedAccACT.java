package com.linkage.module.ids.act;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.ids.bio.AddSimSpeedAccBIO;
import com.linkage.system.extend.struts.result.AJAXResult;

public class AddSimSpeedAccACT implements Serializable,SessionAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4826857867976046589L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(AddSimSpeedAccACT.class);

	// session
	private Map session;
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 带宽速率列表 */
	private List<Map<String, String>> netRateList = null;
	/** 账号*/
	private String account;
	/** 密码*/
	private String password;
	/** 属地*/
	private String cityId;
	/** 带宽速率*/
	private String netRate;
	private AddSimSpeedAccBIO bio;
	private String ajax;
	/** 添加类型 1:添加0:更新*/
	private String type;
	/** 设备序列号*/
	private String devSn;
	/**
	 * 页面初始化
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String
	 */
	public String execute()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		netRateList = bio.getNetRate();
		return "init";
	}
	public String queryAcc()
	{
		logger.debug("queryAcc()");
		ajax = bio.queryAcc(cityId, netRate);
		return "ajax";
	}
	public String addAcc()
	{
		logger.debug("queryAcc()");
		ajax = bio.addAcc(cityId, netRate, account, password, type);
		return "ajax";
	}
	public String searchAcc()
	{
		logger.debug("searchAcc()");
		ajax = bio.searchAcc(devSn);
		return "ajax";
	}
	public String updateRate()
	{
		logger.debug("updateAcc()");
		if(bio.updateRate(devSn, netRate) > 0){
			ajax = "1";
		}else{
			ajax = "-1";
		}
		return "ajax";
	}
	public String updateInit()
	{
		logger.debug("updateInit()");
		netRateList = bio.getNetRate();
		return "updateInit";
	}
	public Map getSession() {
		return session;
	}
	public void setSession(Map session) {
		this.session = session;
	}
	public List<Map<String, String>> getCityList() {
		return cityList;
	}
	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getNetRate() {
		return netRate;
	}
	public void setNetRate(String netRate) {
		this.netRate = netRate;
	}
	public AddSimSpeedAccBIO getBio() {
		return bio;
	}
	public void setBio(AddSimSpeedAccBIO bio) {
		this.bio = bio;
	}
	public String getAjax() {
		return ajax;
	}
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	public List<Map<String, String>> getNetRateList() {
		return netRateList;
	}
	public void setNetRateList(List<Map<String, String>> netRateList) {
		this.netRateList = netRateList;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDevSn() {
		return devSn;
	}
	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}


}
