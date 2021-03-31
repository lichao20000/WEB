package com.linkage.module.itms.service.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.service.dao.OrderQueryDAO;

public class OrderQueryBIO {
	
	private static Logger logger = LoggerFactory.getLogger(OrderQueryBIO.class);
	
	private OrderQueryDAO dao;
	
	public List<Map> getOrderInfo(String devicesn,String username,int curPage_splitPage, int num_splitPage){
		logger.debug("OrderQueryBIO->getOrderInfo");
		return dao.getOrderInfo(devicesn, username, curPage_splitPage, num_splitPage);
	}
	
	public int countOrderInfo(String devicesn,String username,int curPage_splitPage, int num_splitPage){
		logger.debug("OrderQueryBIO->countOrderInfo");
		return dao.countOrderInfo(devicesn, username, curPage_splitPage, num_splitPage);
	}

	public OrderQueryDAO getDao() {
		return dao;
	}

	public void setDao(OrderQueryDAO dao) {
		this.dao = dao;
	}
	
	
}
