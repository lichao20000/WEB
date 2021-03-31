package com.linkage.module.ids.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.BytesReceivedDetectionDAO;

public class BytesReceivedDetectionBIO {
	
	private Logger logger = LoggerFactory.getLogger(BytesReceivedDetectionBIO.class);
	
	private BytesReceivedDetectionDAO dao = new BytesReceivedDetectionDAO();
	
	
	public List<Map<String, Object>> queryLanAndPonData(String indexName,
			String indexType,String starttime,String endtime,String quertTimeType,String deviceSerialnumber, String loid,int curPage_splitPage, int num_splitPage){
		return dao.queryLanAndPonData(indexName, indexType, starttime, endtime, quertTimeType, deviceSerialnumber, loid, curPage_splitPage, num_splitPage);
	}

//	public List queryLanAndPonDataExcel(String indexName,
//			String indexType,String starttime,String endtime,String quertTimeType,String deviceSerialnumber, String loid,int curPage_splitPage, int num_splitPage){
//		return dao.queryLanAndPonDataExcel(indexName, indexType, starttime, endtime, quertTimeType, deviceSerialnumber, loid, curPage_splitPage, num_splitPage);
//	}
//	
//	public int queryLanAndPonDataCount(String indexName, String indexType,
//			String starttime, String endtime, String quertTimeType,
//			String deviceSerialnumber, String loid, int curPage_splitPage,
//			int num_splitPage) {
//		return dao.queryLanAndPonDataCount(indexName, indexType, starttime, endtime, quertTimeType, deviceSerialnumber, loid, curPage_splitPage, num_splitPage);
//	}
}
