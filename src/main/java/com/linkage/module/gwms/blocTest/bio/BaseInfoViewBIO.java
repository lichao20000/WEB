package com.linkage.module.gwms.blocTest.bio;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.blocTest.dao.BaseInfoViewDAO;

public class BaseInfoViewBIO {

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(BaseInfoViewBIO.class);
	
	BaseInfoViewDAO dao = null;
	
	/**
	 * 查询列表清单
	 * 
	 * @return
	 */
	public List getBaseInfoViewList(int curPage_splitPage, int num_splitPage,
			String customerName,String linkphone,
			String cityId,String username,String deviceId,String deviceSn,
			String bssSheetId,String result,String loopbackIp){
		logger.debug("getBaseInfoViewList()");
		
		return dao.getBaseInfoViewList(curPage_splitPage, num_splitPage, customerName, 
				linkphone, cityId, username, deviceId, deviceSn, bssSheetId, result, loopbackIp);
	}
	
	/**
	 * 查询合计条数
	 * 
	 * @return
	 */
	public int getBaseInfoViewCount(int curPage_splitPage, int num_splitPage,
			String customerName,String linkphone,
			String cityId,String username,String deviceId,String deviceSn,
			String bssSheetId,String result,String loopbackIp){
		return dao.getBaseInfoViewCount(num_splitPage, customerName, linkphone,
				cityId, username, deviceId, deviceSn, bssSheetId, result, loopbackIp);
	}
	
	public List getConfigFile(String deviceId){
		return dao.getConfigFile(deviceId);
	}

	public BaseInfoViewDAO getDao() {
		return dao;
	}

	public void setDao(BaseInfoViewDAO dao) {
		this.dao = dao;
	}
	
}
