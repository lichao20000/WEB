package com.linkage.module.ids.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.ElasticCityDroopDAO;

public class CityDroopBIO {
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(CityDroopBIO.class);
	private ElasticCityDroopDAO dao;

	public List<Map<String, Object>> countCityDroop(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			int curPage_splitPage, int num_splitPage) {
		return dao.countCityDroop(indexName, indexType, starttime, endtime,
				cityId, curPage_splitPage, num_splitPage);
	}

	public int queryByDroopCount(String indexName, String indexType,
			String starttime, String endtime, String cityId, String operType,
			int curPage_splitPage, int num_splitPage) {
		return dao.queryByDroopCount(indexName, indexType, starttime, endtime,
				cityId, operType, curPage_splitPage, num_splitPage);
	}

	public List<Map<String, Object>> queryByDroopList(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			String operType, int curPage_splitPage, int num_splitPage) {
		return dao.queryByDroopList(indexName, indexType, starttime, endtime,
				cityId, operType, curPage_splitPage, num_splitPage);
	}

	public int queryByDroopTotal(String indexName, String indexType,
			String starttime, String endtime, String cityId,
			int curPage_splitPage, int num_splitPage) {
		return dao.queryByDroopTotal(indexName, indexType, starttime, endtime,
				cityId, curPage_splitPage, num_splitPage);
	}

	public ElasticCityDroopDAO getDao() {
		return dao;
	}

	public void setDao(ElasticCityDroopDAO dao) {
		this.dao = dao;
	}

	public static Logger getLogger() {
		return logger;
	}

	public List<Map<String, Object>> queryByDroopExcel(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			String operType, int curPage_splitPage, int num_splitPage, int total) {
		return dao.queryByDroopExcel(indexName, indexType, starttime, endtime,
				cityId, operType, curPage_splitPage, num_splitPage, total);
	}

	public String getDetail(String query_type, String queryVal) {
		return dao.getDetail(query_type, queryVal);
	}

}
