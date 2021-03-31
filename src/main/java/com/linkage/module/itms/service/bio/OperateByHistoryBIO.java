package com.linkage.module.itms.service.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.service.dao.OperateByHistoryDAO;


public class OperateByHistoryBIO {
	
	private static Logger logger = LoggerFactory
			.getLogger(OperateByHistoryBIO.class);
	
	private OperateByHistoryDAO dao;
	
	public List<Map> getOperateByHistoryInfo(String starttime, String endtime,
			String username, String city_id, String servType, String resultType,
			String resultId,String usernameType,String gw_type,String isGl, int curPage_splitPage, int num_splitPage) {
		logger.debug("OperateByHistoryServImp->getOperateByHistoryInfo");
		if("1".equals(isGl)){
			servType = "20";
		}
		return dao.getOperateByHistoryInfo(isGl, starttime, endtime, username, city_id, servType, resultType, resultId, usernameType, gw_type, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> getOperateByHistoryInfoStb(String starttime, String endtime,
			String username, String city_id, String servType, String resultType,
			String resultId,String usernameType,String gw_type,String isGl, int curPage_splitPage, int num_splitPage) {
		logger.debug("OperateByHistoryServImp->getOperateByHistoryInfo");
		if("1".equals(isGl)){
			servType = "20";
		}
		return dao.getOperateByHistoryInfoStb(isGl, starttime, endtime, username, city_id, servType, resultType, resultId, usernameType, gw_type, curPage_splitPage, num_splitPage);
	}

	public int countOperateByHistoryInfo(String starttime, String endtime,
			String username, String city_id, String servType, String resultType,
			String resultId, String usernameType,String gw_type,String isGl,int curPage_splitPage, int num_splitPage) {
		logger.debug("OperateByHistoryServImp->countOperateByHistoryInfo");
		if("1".equals(isGl)){
			servType = "20";
		}
		return dao.countOperateByHistoryInfo(isGl, starttime, endtime, username, city_id, servType, resultType, resultId, usernameType, gw_type, curPage_splitPage, num_splitPage);
	}
	
	public int countOperateByHistoryInfoStb(String starttime, String endtime,
			String username, String city_id, String servType, String resultType,
			String resultId, String usernameType,String gw_type,String isGl,int curPage_splitPage, int num_splitPage) {
		logger.debug("OperateByHistoryServImp->countOperateByHistoryInfo");
		if("1".equals(isGl)){
			servType = "20";
		}
		return dao.countOperateByHistoryInfoStb(isGl, starttime, endtime, username, city_id, servType, resultType, resultId, usernameType, gw_type, curPage_splitPage, num_splitPage);
	}
	
	public Map<String,String> getOperateMessage(String bss_sheet_id,String receive_date, String gw_type){
		logger.debug("OperateByHistoryServImp->getOperateMessage");
		return dao.getOperateMessage(bss_sheet_id,receive_date,gw_type);
	}

	public OperateByHistoryDAO getDao() {
		return dao;
	}

	public void setDao(OperateByHistoryDAO dao) {
		this.dao = dao;
	}

}
