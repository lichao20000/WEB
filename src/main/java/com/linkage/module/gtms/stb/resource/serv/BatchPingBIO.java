package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.resource.dao.BatchPingDAO;

public class BatchPingBIO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BatchPingBIO.class);
	private BatchPingDAO bpDao = null;

	/**
	 * 批量ping数据查询
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cityId
	 * @param deviceIp
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDataList(int curPage_splitPage, int num_splitPage, 
			String cityId, String deviceIp, String result, String startTime, String endTime)
	{
		logger.debug("BatchPingBIO => getDataList");
		List<Map> list = bpDao.queryData(curPage_splitPage, num_splitPage, cityId, deviceIp, result, startTime, endTime);
		return list;
	}

	/**
	 * 查询记录数
	 * @param cityId
	 * @param deviceIp
	 * @return
	 */
	public int getDataCount(String cityId, String deviceIp, String result, String startTime, String endTime) {
		logger.debug("BatchPingBIO => getDataList");
		int count = bpDao.queryCount(cityId, deviceIp, result, startTime, endTime);
		return count;
	}

	public BatchPingDAO getBpDao() {
		return bpDao;
	}

	public void setBpDao(BatchPingDAO bpDao) {
		this.bpDao = bpDao;
	}
}
