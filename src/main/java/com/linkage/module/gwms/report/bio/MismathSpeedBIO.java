package com.linkage.module.gwms.report.bio;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.report.dao.MismathSpeedDAO;

public class MismathSpeedBIO {
	Logger logger = LoggerFactory.getLogger(MismathSpeedBIO.class);
	// 持久层
	MismathSpeedDAO dao;
	
	/**不匹配终端**/
	public List<Map> queryCityId(String area_id){
		return dao.queryCityId(area_id);
	}
	
	public List<Map> getMismathSpeedCount(String cityId){
		 return dao.getMismathSpeedCount(cityId);
	}

	public List<Map> queryDetail(String cityId,int curPage_splitPage, int num_splitPage){
		return dao.queryDetail(cityId,curPage_splitPage, num_splitPage);
	}
	
	public int queryDetailCount(String city_id,int curPage_splitPage, int num_splitPage)
	{
		return dao.queryDetailCount(city_id,curPage_splitPage, num_splitPage);
	}
	
	public List<Map> toExcel(String cityId){
		return dao.toExcel(cityId);
	}
	
	
	/**不匹配终端修改**/
	public List<Map> getMismathChageCount(String cityId){
		 return dao.getMismathChageCount(cityId);
	}

	public List<Map> getMismathChageDetail(String cityId,int curPage_splitPage, int num_splitPage){
		return dao.getMismathChageDetail(cityId,curPage_splitPage, num_splitPage);
	}
	
	public int getMismathChageDetailCount(String city_id,int curPage_splitPage, int num_splitPage)
	{
		return dao.getMismathChageDetailCount(city_id,curPage_splitPage, num_splitPage);
	}
	
	public List<Map> toChageExcel(String cityId){
		return dao.toChageExcel(cityId);
	}
	
	
	/**不匹配终端新增**/
	public List<Map> getMismathAddCount(String cityId){
		 return dao.getMismathAddCount(cityId);
	}

	public List<Map> getMismathAddDetail(String cityId,int curPage_splitPage, int num_splitPage){
		return dao.getMismathAddDetail(cityId,curPage_splitPage, num_splitPage);
	}
	
	public int getMismathAddDetailCount(String city_id,int curPage_splitPage, int num_splitPage)
	{
		return dao.getMismathAddDetailCount(city_id,curPage_splitPage, num_splitPage);
	}
	
	public List<Map> toAddExcel(String cityId){
		return dao.toAddExcel(cityId);
	}
	

	public MismathSpeedDAO getDao()
	{
		return dao;
	}

	
	public void setDao(MismathSpeedDAO dao)
	{
		this.dao = dao;
	}
	
}
