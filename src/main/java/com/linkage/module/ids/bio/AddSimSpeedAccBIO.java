package com.linkage.module.ids.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.ids.dao.AddSimSpeedAccDAO;

public class AddSimSpeedAccBIO {
	private static Logger logger = LoggerFactory
			.getLogger(AddSimSpeedAccBIO.class);
	private AddSimSpeedAccDAO dao;
	public String queryAcc(String cityId,String netRate)
	{
		logger.debug("queryAcc()");
		List<Map<String, String>> list= dao.queryAccount(cityId, Integer.valueOf(netRate)*1024);
//		if(0<list.size()){
//			return list.get(0).get("account")+","+list.get(0).get("password");
//		}
		if(null!=list && list.size()>0){
			String username = "";
			String password = "";
			for(Map<String, String> map : list){
				username += (StringUtil.getStringValue(map, "account", "")+",");
				password += (StringUtil.getStringValue(map, "password", "")+",");
			}
			if(username.endsWith(",")){
				username = username.substring(0,username.length()-1);
			}
			if(password.endsWith(",")){
				password = password.substring(0,password.length()-1);
			}
			return username+"###"+password;
		}
		return "";
	}
	
	public String addAcc(String cityId,String netRate,String account,String password,String type)
	{
		logger.debug("addAcc()");
		int num = 0;
//		if("0".equals(type)){
//			num = dao.updateAccount(cityId, Integer.valueOf(netRate)*1024, account, password);
//		}else{
//			num = dao.addAccount(cityId, Integer.valueOf(netRate)*1024, account, password);
//		}
		String[] accountArr = account.split("##");
		String[] passwordArr = password.split("##");
		
		ArrayList<String> sqlList = new ArrayList<String>();
		sqlList.add(dao.delAccount(cityId, Integer.valueOf(netRate)*1024));
		for(int i=0;i<accountArr.length;i++)
		{
			sqlList.add(dao.addAccount(cityId, Integer.valueOf(netRate)*1024, accountArr[i], passwordArr[i]));
		}
		return dao.updateAccountList(sqlList);
	}
	public List<Map<String, String>> getNetRate()
	{
		String netRates = LipossGlobals.getLipossProperty("SimNetRate");
		String[] netRateArr = netRates.split(",");
		Map<String, String> map = null;
		List<Map<String, String>> list= new ArrayList<Map<String, String>>();
		for(int i=0;i<netRateArr.length;i++){
			map = new HashMap<String, String>();
			map.put("rate", netRateArr[i]);
			list.add(map);
		}
		return list;
	}
	public String searchAcc(String devSn)
	{
		logger.debug("searchAcc()");
		return dao.searchAcc(devSn);
	}
	
	public int updateRate(String devSn, String netRate)
	{
		logger.debug("updateAcc()");
		return dao.updateRate(devSn, Long.valueOf(netRate)*1024);
	}
	public AddSimSpeedAccDAO getDao() {
		return dao;
	}
	public void setDao(AddSimSpeedAccDAO dao) {
		this.dao = dao;
	}
	
}
