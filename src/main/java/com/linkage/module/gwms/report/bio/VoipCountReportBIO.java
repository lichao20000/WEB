package com.linkage.module.gwms.report.bio;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.module.gwms.report.dao.VoipCountReportDAO;

public class VoipCountReportBIO {
	Logger logger = LoggerFactory.getLogger(VoipCountReportBIO.class);
	// 持久层
	VoipCountReportDAO dao;
	
	public List<Map<String,String>> getCityList(String cityId) {
		 List<Map<String,String>> rsList = dao.getCityList(cityId);
		return rsList;
	}
	
	public List<Map<String,String>> queryDataList(String cityId,String gw_type) {
		List<Map<String,String>> rsList = dao.queryDataList(cityId,gw_type);
		return rsList;
	}
	
	public List<Map>  querydetailList(String gw_type,String city_id,String protocol,String dataCityId,int curPage_splitPage, int num_splitPage){
		List<Map>  list = dao.querydetailList(gw_type, city_id, protocol,dataCityId, curPage_splitPage,  num_splitPage);
		return list;
	}
	
	public List<Map>  queryDetailForExcel(String gw_type,String city_id,String protocol,String dataCityId){
		List<Map>  list = dao.queryDetailForExcel(gw_type, city_id, protocol,dataCityId);
		return list;
	}
	
	public int querydetailListCount(String gw_type,String city_id,String protocol,String dataCityId,
			int curPage_splitPage, int num_splitPage) {

		return dao.querydetailListCount(gw_type,city_id, protocol,dataCityId,curPage_splitPage,
				num_splitPage);
	}

	public VoipCountReportDAO getDao() {
		return dao;
	}

	public void setDao(VoipCountReportDAO dao) {
		this.dao = dao;
	}
	
	
}
