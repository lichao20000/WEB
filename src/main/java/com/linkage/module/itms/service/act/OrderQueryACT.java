package com.linkage.module.itms.service.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.itms.service.bio.OrderQueryBIO;

public class OrderQueryACT extends splitPageAction implements
		ServletRequestAware, SessionAware {
	
	private static Logger logger = LoggerFactory.getLogger(OrderQueryACT.class);
	private OrderQueryBIO bio;
	private Map session;
	private HttpServletRequest request;
	
	//设备序列号 
	private String devicesn;
	//LOID
	private String username;
	//工单结果集
	private List<Map> orderList;
	
	public String execute(){
		return "success";
	}
	
	public String getOrderInfo(){
		logger.debug("OrderQueryACT->getOrderInfo{}");
		orderList = bio.getOrderInfo(devicesn, username, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countOrderInfo(devicesn, username, curPage_splitPage, num_splitPage);
		return "list";
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getDevicesn() {
		return devicesn;
	}

	public void setDevicesn(String devicesn) {
		this.devicesn = devicesn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Map> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Map> orderList) {
		this.orderList = orderList;
	}

	public OrderQueryBIO getBio() {
		return bio;
	}

	public void setBio(OrderQueryBIO bio) {
		this.bio = bio;
	}

}
