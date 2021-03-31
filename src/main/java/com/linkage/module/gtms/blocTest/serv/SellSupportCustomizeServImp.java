package com.linkage.module.gtms.blocTest.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.blocTest.dao.SellSupportCustomizeDao;

public class SellSupportCustomizeServImp implements SellSupportCustomizeServ{
	
	private static Logger logger = LoggerFactory.getLogger(SellSupportCustomizeServImp.class);
	
	private SellSupportCustomizeDao dao ;
	
	
	public List queryReport(long custManagerId)
	{
		return dao.queryReport(custManagerId);
	}
	/**
	 * 查询客户信息列表
	 * 
	 * @return
	 */
	public List queryCustomer() {
		logger.debug("queryCustomer()");
		return dao.queryCustomer();
	}
	@Override
	public void addSellSupportCustomize(String custManagerId, String flowMax,
			String flowMin, String timeMax, String timeMin, String customerId)
	{
		if(!StringUtil.IsEmpty(customerId))
		{
			String[] custIds =customerId.split(",");
			for(int i = 0 ; i < custIds.length ; i++)
			{
				dao.addSellSupportCustomize(custManagerId, flowMax, flowMin, timeMax, timeMin, custIds[i]);		
			}
		}
		else
		{
			logger.warn("customer id is null");
		}
		
	}
	public Map<String, String> getMailTmpt(String custManagerId){
		return dao.getMailTmpt(custManagerId);
	}
	@Override
	public List<Map<String, String>> getAllCustManager()
	{
		// TODO Auto-generated method stub
		return dao.getAllCustManager();
	}
	
	public SellSupportCustomizeDao getDao()
	{
		return dao;
	}
	
	public void setDao(SellSupportCustomizeDao dao)
	{
		this.dao = dao;
	}




	
}
