
package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.resource.dao.BindLogViewDAO;


public class BindLogViewBIO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BindLogViewBIO.class);
	
	BindLogViewDAO bindLogViewDAO;
	
	public List getBindLogList(int curPage_splitPage, int num_splitPage,
			long bindStartTime, long bindEndTime, String username,String deviceSn,
			String cityId,String operType) {
		
		logger.debug("BindLogViewBIO=>getBindLogList({},{},{},{},{},{},{},{})",
				new Object[]{curPage_splitPage,num_splitPage,bindStartTime,
				bindEndTime,username,deviceSn,cityId,operType});
		
		return bindLogViewDAO.getBindLogList(curPage_splitPage, num_splitPage, 
				bindStartTime, bindEndTime, username, deviceSn, cityId,operType);
	}
	
	public int getBindLogCount(int num_splitPage,long bindStartTime, long bindEndTime, 
			String username,String deviceSn,String cityId,String operType) {
		
		logger.debug("BindLogViewBIO=>getBindLogList({},{},{},{},{},{},{})",
				new Object[]{num_splitPage,bindStartTime,	bindEndTime,username,deviceSn,cityId,operType});
		
		return bindLogViewDAO.getBindLogCount(num_splitPage, bindStartTime, 
				bindEndTime, username, deviceSn, cityId,operType);
		
	}

	/**
	 * @return the bindLogViewDAO
	 */
	public BindLogViewDAO getBindLogViewDAO() {
		return bindLogViewDAO;
	}

	/**
	 * @param bindLogViewDAO the bindLogViewDAO to set
	 */
	public void setBindLogViewDAO(BindLogViewDAO bindLogViewDAO) {
		this.bindLogViewDAO = bindLogViewDAO;
	}
		
	
}
