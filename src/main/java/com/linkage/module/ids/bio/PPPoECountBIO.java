package com.linkage.module.ids.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.PPPoECountDAO;

public class PPPoECountBIO {
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(CityDroopBIO.class);
	private PPPoECountDAO dao;

	public PPPoECountDAO getDao() {
		return dao;
	}

	public void setDao(PPPoECountDAO dao) {
		this.dao = dao;
	}

	public static Logger getLogger() {
		return logger;
	}

	public List<Map<String, Object>> countPPPoE(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			int curPage_splitPage, int num_splitPage) {
		return dao.countPPPoE(indexName, indexType, starttime, endtime, cityId,
				curPage_splitPage, num_splitPage);
	}

	public List<Map<String, Object>> queryByPPPoEList(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			String operType, int curPage_splitPage, int num_splitPage) {
		return dao.queryByPPPoEList(indexName, indexType, starttime, endtime,
				cityId, operType, curPage_splitPage, num_splitPage);
	}

	public int queryByPPPoECount(String indexName, String indexType,
			String starttime, String endtime, String cityId, String operType,
			int curPage_splitPage, int num_splitPage) {
		return dao.queryByPPPoECount(indexName, indexType, starttime, endtime,
				cityId, operType, curPage_splitPage, num_splitPage);
	}

	public int queryByPPPoETotal(String indexName, String indexType,
			String starttime, String endtime, String cityId, String operType,
			int curPage_splitPage, int num_splitPage) {
		return dao.queryByPPPoETotal(indexName, indexType, starttime, endtime,
				cityId, operType, curPage_splitPage, num_splitPage);
	}

	public List<Map<String, Object>> queryByPPPoEExcel(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			String operType, int curPage_splitPage, int num_splitPage, int total) {
		return dao.queryByPPPoEExcel(indexName, indexType, starttime, endtime,
				cityId, operType, curPage_splitPage, num_splitPage, total);
	}
}
