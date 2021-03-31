package com.linkage.module.ids.bio;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.IdsQueryDAO;

public class IdsQueryBIO {
	private static Logger logger = LoggerFactory.getLogger(IdsQueryBIO.class);

	private IdsQueryDAO dao = null;
	
	

	
	public IdsQueryDAO getDao()
	{
		return dao;
	}

	
	public void setDao(IdsQueryDAO dao)
	{
		this.dao = dao;
	}

	public List getQueryStatusListByLoid(String indexName,String indexType ,int curPage_splitPage, int num_splitPage,
			String starttime, String endtime,String quertTimeType,String loid) {
		logger.debug("getQueryList()");
		return dao.getQueryStatusListByLoid(indexName,indexType,curPage_splitPage,num_splitPage,starttime, endtime,quertTimeType,loid);
	}

	public List getQueryStatusListByFile(String indexName,String indexType ,int curPage_splitPage, int num_splitPage,
			String starttime, String endtime,String quertTimeType,String file_path) {
		logger.debug("getQueryList()");
		return dao.getQueryStatusListByFile(indexName,indexType,curPage_splitPage,num_splitPage,starttime, endtime,quertTimeType,file_path);
	}
	
	public List getQueryStatusListByFileToExcel(String indexName,String indexType ,int curPage_splitPage, int num_splitPage,
			String starttime, String endtime,String quertTimeType,String file_path) {
		logger.debug("getQueryList()");
		return dao.getQueryStatusListByFileToExcel(indexName,indexType,curPage_splitPage,num_splitPage,starttime, endtime,quertTimeType,file_path);
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
			String endTime, String quertTimeType, 
			String loid) {
		logger.debug("queryPonstatusCount1-->");

		return dao.queryPonstatusCount(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType,loid);
	}
	
	public int queryPonstatusCount2(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, 
			String file_path) {
		logger.debug("queryPonstatusCount1-->");

		return dao.queryPonstatusCount2(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType,file_path);
	}

	




	
}
