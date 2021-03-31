package com.linkage.module.bbms.service.bio;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.bbms.service.dao.EVDOBindLogDAO;

/**
 * EVDO模块：EVDO数据卡绑定日志
 * @author 漆学启
 * @date 2009-10-29
 */
public class EVDOBindLogBIO {
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(EVDOBindLogBIO.class);
	
	EVDOBindLogDAO dao = null;

	/**
	 * 查询记录
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @param deviceNo
	 * @return
	 */
	public List getEVDOBindLog(int curPage_splitPage, int num_splitPage,
			String cityId, long startDate, long endDate, String deviceNo) {
		logger.debug("EVDOBindLogBIO=>getEVDOBindLog()");
		return dao.getEVDOBindLog(curPage_splitPage, num_splitPage, cityId, startDate, endDate, deviceNo);
	}
	
	/**
	 * 查询记录显示页总数
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @param deviceNo
	 * @return
	 */
	public int getEVDOBindLogCount(int curPage_splitPage, int num_splitPage,
			String cityId, long startDate, long endDate, String deviceNo) {
		logger.debug("EVDOBindLogBIO=>getEVDOBindLogCount() start");
		return dao.getEVDOBindLogCount(curPage_splitPage, num_splitPage, cityId, startDate, endDate, deviceNo);
	}
	
	/**
	 * @return the dao
	 */
	public EVDOBindLogDAO getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(EVDOBindLogDAO dao) {
		this.dao = dao;
	}
	
	
}
