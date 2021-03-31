
package com.linkage.module.itms.resource.act;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.resource.bio.EPonSNQueryBIO;

public class EPonSNQueryACT implements ServletRequestAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(EPonSNQueryACT.class);
	
	private EPonSNQueryBIO ePonSNQueryBIO;
	HttpServletRequest request = null;
	private String username = null;
	private String voipUsername = null;
	private String voipPhone = null;
	
	private List rsList = null;
	
	private String gw_type;

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String execute()
	{
		logger.debug("execute()");
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String cityId = curUser.getCityId();
		
		this.rsList = ePonSNQueryBIO.getHgwInfo(cityId, username, voipUsername, voipPhone, gw_type);
		
		return "list";
	}

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	/**
	 * @return the ePonSNQueryBIO
	 */
	public EPonSNQueryBIO getEPonSNQueryBIO() {
		return ePonSNQueryBIO;
	}

	/**
	 * @param ponSNQueryBIO the ePonSNQueryBIO to set
	 */
	public void setBio(EPonSNQueryBIO bio) {
		this.ePonSNQueryBIO = bio;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the rsList
	 */
	public List getRsList() {
		return rsList;
	}

	/**
	 * @param rsList the rsList to set
	 */
	public void setRsList(List rsList) {
		this.rsList = rsList;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the voipPhone
	 */
	public String getVoipPhone() {
		return voipPhone;
	}

	/**
	 * @param voipPhone the voipPhone to set
	 */
	public void setVoipPhone(String voipPhone) {
		this.voipPhone = voipPhone;
	}

	/**
	 * @return the voipUsername
	 */
	public String getVoipUsername() {
		return voipUsername;
	}

	/**
	 * @param voipUsername the voipUsername to set
	 */
	public void setVoipUsername(String voipUsername) {
		this.voipUsername = voipUsername;
	}
	
}
