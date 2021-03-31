package com.linkage.module.gwms.report.bio;

import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.report.dao.PingResultDAO;

public class PingResultBIO {

	PingResultDAO pingResultDao;


	public List<Map<String, String>> getPingResult(String deviceId) {

		return pingResultDao.getPingResult(deviceId);
	}

	public PingResultDAO getPingResultDao() {
		return pingResultDao;
	}

	public void setPingResultDao(PingResultDAO pingResultDao) {
		this.pingResultDao = pingResultDao;
	}

	
}
