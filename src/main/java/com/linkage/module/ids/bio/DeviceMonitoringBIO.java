package com.linkage.module.ids.bio;

import com.linkage.module.ids.dao.DeviceMonitoringDAO;
import com.linkage.module.ids.dao.ElasticDeviceMonitorDAO;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceMonitoringBIO {
	private static Logger logger = LoggerFactory
			.getLogger(DeviceMonitoringBIO.class);

	private DeviceMonitoringDAO dao = null;

	private ElasticDeviceMonitorDAO edmDAO = new ElasticDeviceMonitorDAO();

	public DeviceMonitoringDAO getDao() {
		return this.dao;
	}

	public void setDao(DeviceMonitoringDAO dao) {
		this.dao = dao;
	}

	public List<Map> queryPonstatusList(String startTime, String endTime,
			String quertTimeType, String deviceno, String loid,
			int curPage_splitPage, int num_splitPage) {
		List list = this.dao
				.queryPonstatusList(startTime, endTime, quertTimeType,
						deviceno, loid, curPage_splitPage, num_splitPage);
		return list;
	}

	public int queryPonstatusCount(String startTime, String endTime,
			String quertTimeType, String deviceno, String loid,
			int curPage_splitPage, int num_splitPage) {
		int count = this.dao
				.queryPonstatusCount(startTime, endTime, quertTimeType,
						deviceno, loid, curPage_splitPage, num_splitPage);
		return count;
	}

	public List<Map> queryNetparamList(String startTime, String endTime,
			String quertTimeType, String deviceno, String loid,
			int curPage_splitPage, int num_splitPage) {
		List list = this.dao
				.queryNetparamList(startTime, endTime, quertTimeType, deviceno,
						loid, curPage_splitPage, num_splitPage);
		return list;
	}

	public int queryNetparamCount(String startTime, String endTime,
			String quertTimeType, String deviceno, String loid,
			int curPage_splitPage, int num_splitPage) {
		int count = this.dao
				.queryNetparamCount(startTime, endTime, quertTimeType,
						deviceno, loid, curPage_splitPage, num_splitPage);
		return count;
	}

	public List<Map> queryVoicestatusList(String startTime, String endTime,
			String quertTimeType, String deviceno, String loid,
			int curPage_splitPage, int num_splitPage) {
		List list = this.dao
				.queryVoicestatusList(startTime, endTime, quertTimeType,
						deviceno, loid, curPage_splitPage, num_splitPage);
		return list;
	}

	public int queryVoicestatusCount(String startTime, String endTime,
			String quertTimeType, String deviceno, String loid,
			int curPage_splitPage, int num_splitPage) {
		int count = this.dao
				.queryVoicestatusCount(startTime, endTime, quertTimeType,
						deviceno, loid, curPage_splitPage, num_splitPage);
		return count;
	}

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
	public List<Map<String, Object>> queryPonstatusList1(String indexName,
			String indexType, int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String quertTimeType,
			String deviceSerialnumber, String loid) {

		return edmDAO.queryPonstatusList(indexName, indexType,
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
	public int queryPonstatusCount1(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {
		logger.debug("queryPonstatusCount1-->");

		return edmDAO.queryPonstatusCount(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
	}

	/**
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
	public List queryNetparamList1(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {
		logger.warn("into queryNetparamList1");
		return edmDAO.queryNetparamList(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
	}

	/**
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
	public int queryNetparamCount1(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {
		return edmDAO.queryNetparamCount(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
	}

	/**
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
	public List queryVoicestatusList(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {

		return edmDAO.queryVoicestatusList(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
	}

	/**
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
	public List queryLANList(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {

		return edmDAO.queryLANList(indexName, indexType, curPage_splitPage, num_splitPage, startTime, endTime, quertTimeType, deviceSerialnumber, loid);
	}
	
	public int queryLANCount(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {

		return edmDAO.queryLANCount(indexName, indexType, curPage_splitPage, num_splitPage, startTime, endTime, quertTimeType, deviceSerialnumber, loid);
	}
	/**
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
	public List queryPONList(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {

		return edmDAO.queryPONList(indexName, indexType, curPage_splitPage, num_splitPage, startTime, endTime, quertTimeType, deviceSerialnumber, loid);
	}
	
	/**
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
	public int queryPONCount(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {

		return edmDAO.queryPONCount(indexName, indexType, curPage_splitPage, num_splitPage, startTime, endTime, quertTimeType, deviceSerialnumber, loid);
	}
	/**
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
	public int queryVoicestatusCount1(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {

		return edmDAO.queryVoicestatusCount(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
	}

}