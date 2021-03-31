package com.linkage.module.itms.report.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.report.dao.ZeroAutoConfigInfoDAO;


public class ZeroAutoConfigInfoBIO {
	private static Logger logger = LoggerFactory
			.getLogger(ZeroAutoConfigInfoBIO.class);
	
	private ZeroAutoConfigInfoDAO zeroAutoConfigDAO;

	public List<Map> getZeroConfigInfo(String starttime, String endtime,
			String city_id, String servTypeId,  String resultType,
			int curPage_splitPage, int num_splitPage)
	{
		logger
		.debug("ZeroAutoConfigBio==>getZeroConfigInfo({},{},{},{},{},{},{},{})",
				new Object[] {  starttime,  endtime,
				 city_id,  servTypeId,  resultType,
				 curPage_splitPage,  num_splitPage});
		
		List<Map> list = zeroAutoConfigDAO.getZeroAutoConfigInfo(starttime, endtime, city_id, servTypeId, resultType, curPage_splitPage, num_splitPage);
		return list;
	}
	
	public int countZeroConfigInfo(String starttime, String endtime,
			String city_id, String servTypeId, String resultType,
			int curPage_splitPage, int num_splitPage){
		
		logger
		.debug("ZeroAutoConfigBio==>countZeroConfigInfo({},{},{},{},{},{},{})",
				new Object[] {  starttime,  endtime,
				 city_id,  servTypeId,   resultType,
				 curPage_splitPage,  num_splitPage});
		
		return zeroAutoConfigDAO.countZeroAutoConfigInfo(starttime, endtime, city_id, servTypeId, resultType, curPage_splitPage, num_splitPage);
	}
	
	public int countZeroConfigInfoExcel(String starttime, String endtime,
			String city_id, String servTypeId, String resultType)
	{
		logger
		.debug("ZeroAutoConfigBio==>countZeroConfigInfoExcel({},{},{},{},{},{},{})",
				new Object[] {  starttime,  endtime,
				 city_id,  servTypeId,  resultType});
		
		return zeroAutoConfigDAO.countZeroAutoConfigInfoExcel(starttime, endtime, city_id, servTypeId, resultType);
	}
	
	public List<Map> getZeroConfigInfoExcel(String starttime, String endtime,
			String city_id, String servTypeId,  String resultType)
	{	
		logger
		.debug("ZeroAutoConfigBio==>getZeroConfigInfoExcel({},{},{},{},{},{},{},{})",
				new Object[] {  starttime,  endtime,
				 city_id,  servTypeId,   resultType});
		return zeroAutoConfigDAO.getZeroAutoConfigInfoExcel(starttime, endtime, city_id, servTypeId, resultType);
	}

	public ZeroAutoConfigInfoDAO getZeroAutoConfigDAO() {
		return zeroAutoConfigDAO;
	}

	public void setZeroAutoConfigDAO(ZeroAutoConfigInfoDAO zeroAutoConfigDAO) {
		this.zeroAutoConfigDAO = zeroAutoConfigDAO;
	}

	
}
