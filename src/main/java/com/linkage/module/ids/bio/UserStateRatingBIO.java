package com.linkage.module.ids.bio;

import com.linkage.module.ids.dao.ElasticUserStatusDAO;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserStateRatingBIO {
	private static Logger logger = LoggerFactory
			.getLogger(UserStateRatingBIO.class);


	private ElasticUserStatusDAO dao = null;
	/**
	 * 查询pon状态
	 * 
	 * @param indexName
	 * @param indexType
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param quertTimeType
	 * @param deviceSerialnumber
	 * @param loid
	 * @return
	 */
	public List<Map<String, Object>> queryPonstatusList(String indexName,
			String indexType, int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String quertTimeType,
			String deviceSerialnumber, String loid) {

		return dao.queryPonstatusList(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
	}

	/**
	 * 查询总记录数
	 * 
	 * @param indexName
	 * @param indexType
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param quertTimeType
	 * @param deviceSerialnumber
	 * @param loid
	 * @return
	 */
	public int queryUserStatusCount(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {
		logger.warn("queryPonstatusCount-->");

		return dao.queryUserStatusCount(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
	}

	public ElasticUserStatusDAO getDao() {
		return dao;
	}

	public void setDao(ElasticUserStatusDAO dao) {
		this.dao = dao;
	}

	public static Logger getLogger() {
		return logger;
	}
	
	
}