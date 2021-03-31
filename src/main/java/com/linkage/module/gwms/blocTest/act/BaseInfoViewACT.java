package com.linkage.module.gwms.blocTest.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.blocTest.bio.BaseInfoViewBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 针对集团测试，作用不大
 * 查询基本信息，主要是针对设备信息
 * 顺便带上工单信息，如果该设备绑定了
 * 
 * @author Administrator
 *
 */
public class BaseInfoViewACT extends splitPageAction implements ServletRequestAware {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(BaseInfoViewACT.class);
	
	BaseInfoViewBIO bio = null;
	
	private String customerName = null;
	private String linkphone = null;
	private String cityId = null;
	private String username = null;
	private String deviceId = null;
	private String deviceSn = null;
	private String bssSheetId = null;
	private String result = null;
	private String loopbackIp = null;
	
	//request取登陆帐号使用
	private HttpServletRequest request;
	
	//属地列表
	private List<Map<String,String>> cityIdList = null;
	
	//数据集
	private List rsList = null;
	
	//配置文件查询
	private List configList = null;
	
	public String execute() throws Exception {
		
		logger.debug("execute()");
		
		if(null==this.cityId){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.cityId = curUser.getCityId();
		}
		this.cityIdList = CityDAO.getNextCityListByCityPid(this.cityId);
		
		return "success";
	}
	
	/**
	 * 查询文件
	 * @return
	 */
	public String getConfigFile(){
		
		configList = bio.getConfigFile(deviceId);
		
		return "configFile";
	}
	
	/**
	 * 开始查询
	 * @return
	 */
	public String startQuery(){
		this.dataOption();
		if(null==this.cityId || "-1".equals(this.cityId)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.cityId = curUser.getCityId();
		}
		
		this.rsList = bio.getBaseInfoViewList(curPage_splitPage, num_splitPage,
				customerName, linkphone, cityId, username, deviceId, 
				deviceSn, bssSheetId, result, loopbackIp);
		
		maxPage_splitPage = bio.getBaseInfoViewCount(curPage_splitPage, num_splitPage,
				customerName, linkphone, cityId, username, deviceId, deviceSn,
				bssSheetId, result, loopbackIp);
		
		return "list";
	}
	
	/**
	 * 翻页
	 * @return
	 */
	public String goPage(){
		this.dataOption();
		if(null==this.cityId || "-1".equals(this.cityId)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.cityId = curUser.getCityId();
		}
		this.rsList = bio.getBaseInfoViewList(curPage_splitPage, num_splitPage,
				customerName, linkphone, cityId, username, deviceId, 
				deviceSn, bssSheetId, result, loopbackIp);
		
		maxPage_splitPage = bio.getBaseInfoViewCount(curPage_splitPage, num_splitPage,
				customerName, linkphone, cityId, username, deviceId, deviceSn,
				bssSheetId, result, loopbackIp);
		
		return "list";
	}
	
	
	public void dataOption(){
		if(null==this.customerName || "".equals(this.customerName.trim())){
			this.customerName = null;
		}
		if(null==this.linkphone || "".equals(this.linkphone.trim())){
			this.linkphone = null;
		}
		if(null==this.cityId || "".equals(this.cityId.trim())){
			this.cityId = null;
		}
		if(null==this.username || "".equals(this.username.trim())){
			this.username = null;
		}
		if(null==this.deviceId || "".equals(this.deviceId.trim())){
			this.deviceId = null;
		}
		if(null==this.deviceSn || "".equals(this.deviceSn.trim())){
			this.deviceSn = null;
		}
		if(null==this.bssSheetId || "".equals(this.bssSheetId.trim())){
			this.bssSheetId = null;
		}
		if(null==this.result || "".equals(this.result.trim())){
			this.result = null;
		}
		if(null==this.loopbackIp || "".equals(this.loopbackIp.trim())){
			this.loopbackIp = null;
		}
	}
	
	
	public BaseInfoViewBIO getBio() {
		return bio;
	}
	public void setBio(BaseInfoViewBIO bio) {
		this.bio = bio;
	}
	public String getBssSheetId() {
		return bssSheetId;
	}
	public void setBssSheetId(String bssSheetId) {
		this.bssSheetId = bssSheetId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	public String getLinkphone() {
		return linkphone;
	}
	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}
	public String getLoopbackIp() {
		return loopbackIp;
	}
	public void setLoopbackIp(String loopbackIp) {
		this.loopbackIp = loopbackIp;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public List<Map<String, String>> getCityIdList() {
		return cityIdList;
	}

	public void setCityIdList(List<Map<String, String>> cityIdList) {
		this.cityIdList = cityIdList;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public List getRsList() {
		return rsList;
	}

	public void setRsList(List rsList) {
		this.rsList = rsList;
	}

	public List getConfigList() {
		return configList;
	}

	public void setConfigList(List configList) {
		this.configList = configList;
	}
	
	
	
}
