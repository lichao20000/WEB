package com.linkage.module.gtms.config.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.WirelessConfigServ;
import com.linkage.module.ids.util.WSClientUtil;

public class WirelessConfigActionImpl  extends splitPageAction implements WirelessConfigAction,
		SessionAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(WirelessConfigActionImpl.class);
	// Session
	private Map<String, Object> session;
	private String deviceId = "";
	/** 无线路由的状态 */
	private String specId = "";
	/** 帐号 **/
	private String username;
	/** LOID **/
	private String loidParam;
	private WirelessConfigServ bio;
	private String ajax = "";
	private String gw_type = "";
	/** 上网个数 */
	private String netNum = "";
	// 属地
	private String cityId = "00";
	// 关闭还是开通 flag 1:开通，0:关闭
	private String flag = "1";
	// 宽带帐号
	private String usenmames;
	// 查询设备
	private List<Map<String, String>> deviceList = new ArrayList<Map<String, String>>();
	// request
	private HttpServletRequest request = null;
	// 文件名
	private String fileName = null;
	// 返回信息
	private String msg = null;
	// 查询结果的合计条数
	private int total = 0;
	// 校验后宽带帐号
	private List<Map<String, String>> list = null;

	/**
	 * 根据deviceId查询ssid2Status
	 */
	public String getUserInfo() {
		logger.debug("getUserInfo({})", deviceId);
		specId = bio.getUserInfo(deviceId);
		return "queryRes";
	}

	/**
	 * 将ssid2Status置为2
	 */
	public String doConfig() {
		logger.debug("doConfig({})", deviceId);
		ajax = bio.doConfig(deviceId, gw_type);
		return "ajax";
	}

	/**
	 * 根据宽带帐号查询用户是否存在 该帐号为业务路宽带
	 * 
	 * @return
	 */
	public String getServUserInfo() {
		logger.debug("getServUserInfo({})", username);
		if (null == gw_type || "".equals(gw_type)) {
			gw_type = "1";
		}
		ajax = bio.getServUserInfo(username, gw_type);
		return "ajax";
	}
	/**
	 * 开通/关闭路由
	 * 
	 * @return
	 */
	public String doExecute() {
		logger.debug("doExecute({})", username);

		if (null == gw_type || "".equals(gw_type)) {
			gw_type = "1";
		}
		if ("1".equals(flag)) {
			ajax = bio.sendSSIDSheet(username, cityId, gw_type, netNum, loidParam);
		} else {
			String xmlStr = bio.sendCloseSSIDXML(username, loidParam);
			String url = LipossGlobals.getLipossProperty("jsdxVersionRelease");
			String resultString = WSClientUtil.callItmsService(url,
					xmlStr, "RoutToBridge");
			StringBuffer ajaxParam = new StringBuffer();
			ajaxParam.append("0|||0|||");
			String message = bio.getResultMeg(resultString);
			ajaxParam.append(message);
			ajax = ajaxParam.toString();
		}
		return "ajax";
	}

	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryDeviceList() {
		logger.warn("WirelessConfigActionImpl=>getDeviceList()");
		list = bio.getDeviceList(gw_type, fileName);
		if (null == this.list || this.list.size() < 1) {
			msg = bio.getMsg();
			return "deviceList";
		}
		String username = null;
		String netnum = null;
		Map<String, String> map = null;
		List<String> usernameList = new ArrayList<String>();
		List<String> netNumList = new ArrayList<String>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				username = StringUtil.getStringValue(list.get(i)).split(",")[0];
				String flag = bio.getServUserInfo(username, gw_type);
				if (!"1".equals(flag)) {
					continue;
				} else {
					netnum = StringUtil.getStringValue(list.get(i)).split(",")[1];
					usernameList.add(username);
					netNumList.add(netnum);
					deviceList.clear();
					for (int j = 0; j < usernameList.size(); j++) {
						map = new HashMap<String, String>();
						map.put("username", usernameList.get(j));
						map.put("netNum", netNumList.get(j));
						deviceList.add(map);
					}
				}
			}
		}
		total = deviceList == null ? 0 : this.deviceList.size();
		return "deviceList";
	}

	/**
	 * 批量开通路由
	 * 
	 * @return String
	 */
	public String doConfigAll() {
		logger.warn("WirelessConfigActionImpl->doConfigAll()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		List<String> usernamesList = new ArrayList<String>();
		List<String> netNumsList = new ArrayList<String>();
		if (usenmames.contains(",")) { // 多个设备
			String[] usenmameStr = usenmames.split(",");
			String[] netNumStr = netNum.split(",");
			for (int i = 0; i < usenmameStr.length; i++) {
				String usenmame = usenmameStr[i];
				String netNum = netNumStr[i];
				usernamesList.add(usenmame);
				netNumsList.add(netNum);
			}
		} else { // 单个设备
			usernamesList.add(usenmames);
			netNumsList.add(netNum);
		}
		ajax = bio.SendBathOpenSheet(usernamesList, netNumsList, cityId,
				gw_type);
		return "ajax";
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

	public WirelessConfigServ getBio() {
		return bio;
	}

	public void setBio(WirelessConfigServ bio) {
		this.bio = bio;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getNetNum() {
		return netNum;
	}

	public void setNetNum(String netNum) {
		this.netNum = netNum;
	}

	public String getUsenmames() {
		return usenmames;
	}

	public void setUsenmames(String usenmames) {
		this.usenmames = usenmames;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public static Logger getLogger() {
		return logger;
	}

	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	public List<Map<String, String>> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Map<String, String>> deviceList) {
		this.deviceList = deviceList;
	}

	
	public String getLoidParam()
	{
		return loidParam;
	}

	
	public void setLoidParam(String loidParam)
	{
		this.loidParam = loidParam;
	}

	
	

}
