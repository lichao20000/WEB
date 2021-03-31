/**
 * 
 */
package com.linkage.module.itms.resource.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.itms.resource.bio.CardInfoBIO;
import com.linkage.module.itms.resource.obj.CardStatusOBJ;

/**
 * @author chenjie(67371)
 * @date 2011-5-7
 * 
 * 机卡分离功能，卡信息管理
 */
public class CardInfoACT extends splitPageAction implements SessionAware{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(CardInfoACT.class);
	
	private Map session;
	
	private CardInfoBIO bio;
	
	public void setSession(Map session) 
	{
		this.session = session;
	}
	
	private String card_id;
	
	private String card_serialnumber;
	
	private String author_code;
	
	private String user_id;
	
	private String username;
	
	private String online_status;
	
	private String device_id;
	
	private List<Map> card_list;
	
	private String ajax;
	
	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String queryCard()
	{
		logger.debug("queryCard()");
		card_list = bio.queryCard(username, card_serialnumber, "-1", curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "query";
	}
	
	/**
	 * 获取卡的状态
	 * @return
	 */
	public String getCardStatus()
	{
		logger.debug("getCardStatus()");
		// 未绑定设备，不予采集
		/*
		if(device_id.equals(""))
		{
			ajax = "-1";
			return "ajax";
		}
		*/
		CardStatusOBJ cardObj = getCardStatusOBJ(device_id);
		// 采集失败
		if(cardObj.getResultCode() != 1 && cardObj.getResultCode() != -1)
		{
			ajax = "0" + "|" + cardObj.getResultStr();
		}
		// 采集成功,或者不在线
		else
		{
			ajax = "1" + "|" + cardObj.getCardStatus(); 
		}
		return "ajax";
	}
	
	public CardStatusOBJ getCardStatusOBJ(String device_id)
	{
		return bio.getCardStauts(device_id);
	}

	public String getAuthor_code() {
		return author_code;
	}

	public void setAuthor_code(String author_code) {
		this.author_code = author_code;
	}

	public CardInfoBIO getBio() {
		return bio;
	}

	public void setBio(CardInfoBIO bio) {
		this.bio = bio;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public List<Map> getCard_list() {
		return card_list;
	}

	public void setCard_list(List<Map> card_list) {
		this.card_list = card_list;
	}

	public String getCard_serialnumber() {
		return card_serialnumber;
	}

	public void setCard_serialnumber(String card_serialnumber) {
		this.card_serialnumber = card_serialnumber;
	}

	public String getOnline_status() {
		return online_status;
	}

	public void setOnline_status(String online_status) {
		this.online_status = online_status;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Map getSession() {
		return session;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
}
