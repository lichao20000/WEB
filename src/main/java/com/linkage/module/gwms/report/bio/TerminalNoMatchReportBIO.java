package com.linkage.module.gwms.report.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.report.dao.TerminalNoMatchReportDAO;

public class TerminalNoMatchReportBIO {
	Logger logger = LoggerFactory.getLogger(TerminalNoMatchReportBIO.class);
	// 持久层
	TerminalNoMatchReportDAO dao;
	
	public List<Map> queryCityId(String area_id){
		return dao.queryCityId(area_id);
	}
	
	/**
	 * 废弃接口
	 * @param cityId
	 * @return
	 */
	public List<Map> getData(String cityId){
		List<Map> data = dao.getTyCount(cityId);
		return data;
	}
	
	/**
	 * 不匹配终端报表
	 * @param cityId
	 * @return
	 */
	public List<Map> getDevMisMtch(String cityId){
		return dao.getDevMisMtch(cityId);
	}
	
	/**
	 * 老旧终端报表
	 * @param cityId
	 * @return
	 */
	public List<Map> getOldDev(String cityId){
		return dao.getOldDev(cityId);
	}

	/**
	 * 第一批终端不匹配导出
	 * @param cityId
	 * @return
	 */
	public List<Map> queryNoMatchDetailListExcel(String cityId){
		return dao.queryNoMatchDetailListExcel(cityId);
	}
	/**
	 * 终端不匹配日完成量详情导出
	 * @param cityId
	 * @return
	 */
	public List<Map> getNoMatchDayCompltExcel(String cityId){
		return dao.getNoMatchDayCompltExcel(cityId);
	}
	
	/**
	 * 第一批新增终端详情
	 * @return
	 */
	public List<Map> queryDetailList(String cityId,int curPage_splitPage, int num_splitPage){
		return dao.queryDetailList(cityId,curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 第一批新增终端详情数量
	 * @return
	 */
	public int queryCountDetailList(String city_id,int curPage_splitPage, int num_splitPage)
	{
		return dao.queryCountDetailList(city_id,curPage_splitPage, num_splitPage);
	}
	
	public List<Map> queryDetailListExcel(String cityId){
		return dao.queryDetailListExcel(cityId);
	}
	
	/**
	 * 第一批终端不匹配详情
	 * @return
	 */
	public List<Map> queryNoMatchDetailList(String cityId,int curPage_splitPage, int num_splitPage){
		return dao.queryNoMatchDetailList(cityId,curPage_splitPage, num_splitPage);
	}
	/**
	 * 第一批终端不匹配详情
	 * @return
	 */
	public List<Map> getNoMatchDayDetailList(String cityId,int curPage_splitPage, int num_splitPage){
		return dao.getNoMatchDayDetailList(cityId,curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 第一批终端不匹配详情数量
	 * @return
	 */
	public int queryNoMatchCountDetailList(String city_id,int curPage_splitPage, int num_splitPage)
	{
		return dao.queryNoMatchCountDetailList(city_id,curPage_splitPage, num_splitPage);
	}
	
	
	/**
	 * 终端不匹配日完成量count
	 * @return
	 */
	public int queryNoMatchDayDetailCount(String city_id,int curPage_splitPage, int num_splitPage)
	{
		return dao.queryNoMatchDayDetailCount(city_id,curPage_splitPage, num_splitPage);
	}
	
	public List<Map> querySrcapDevDetailList(String cityId,int curPage_splitPage, int num_splitPage){
		return dao.querySrcapDevDetailList(cityId,curPage_splitPage, num_splitPage);
	}
	
	public int querySrcapDevCountDetailList(String city_id,int curPage_splitPage, int num_splitPage)
	{
		return dao.querySrcapDevCountDetailList(city_id,curPage_splitPage, num_splitPage);
	}
	
	public List<Map> querySrcapDetailListExcel(String cityId){
		return dao.querySrcapDetailListExcel(cityId);
	}

	/**
	 * 获取老旧终端在用设备的详情信息
	 * @param cityId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public Map<String,Object> queryOldDevDetailList(String cityId, int curPage_splitPage, int num_splitPage ){
		Map<String,Object> result = new HashMap<String, Object>();
		//1、获取老旧终端未完成的设备总数
		int total = dao.getOldUserDevCount(cityId);
		result.put("total",total);
		if(total == 0){
			result.put("list",null);
			logger.warn("this cityId has no oldDevice online,cityId:{}",cityId);
			return result;
		}
		List<Map> deviceList = dao.getOldUseDevByCityId(cityId,curPage_splitPage,num_splitPage );
		result.put("list", deviceList );
		return result;
	}
	
	/**
	 * pt921g未完成量统计
	 * @param cityId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public Map<String,Object> queryPt921gDetail(String cityId, int curPage_splitPage, int num_splitPage ){
		Map<String,Object> result = new HashMap<String, Object>();
		int total = dao.queryPt921gCount(cityId);
		result.put("total",total);
		if(total == 0){
			result.put("list",null);
			return result;
		}
		
		List<Map> deviceList = dao.queryPt921gByCityId(cityId,curPage_splitPage,num_splitPage);
		result.put("list", deviceList );
		return result;
	}
	
	/**
	 * 
	 * @param cityId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public Map<String,Object> queryThrdDetail(String cityId, int curPage_splitPage, int num_splitPage ){
		Map<String,Object> result = new HashMap<String, Object>();
		int total = dao.queryThrdCount(cityId);
		result.put("total",total);
		if(total == 0){
			result.put("list",null);
			return result;
		}
		
		List<Map> deviceList = dao.queryThrdByCityId(cityId,curPage_splitPage,num_splitPage);
		result.put("list", deviceList );
		return result;
	}
	
	/**
	 * 老旧终端第一批完成量总量详情
	 * @param cityId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public Map<String,Object> queryOldCompleteDevDetailList(String cityId, int curPage_splitPage, int num_splitPage ){
		Map<String,Object> result = new HashMap<String, Object>();
		int total = dao.getOldCompleteDevCount(cityId);
		result.put("total",total);
		if(total == 0){
			result.put("list",null);
			logger.warn("this cityId has no oldDevice online,cityId:{}",cityId);
			return result;
		}
		
		List<Map> deviceList = dao.getOldCompleteDevByCityId(cityId,curPage_splitPage,num_splitPage);
		result.put("list", deviceList );
		return result;
	}
	
	/**
	 * 老旧终端第一批日完成量详情
	 * @param cityId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public Map<String,Object> queryOldDayCompleteDevDetailList(String cityId, int curPage_splitPage, int num_splitPage ){
		Map<String,Object> result = new HashMap<String, Object>();
		int total = dao.getOldDayCompleteDevCount(cityId);
		result.put("total",total);
		if(total == 0){
			result.put("list",null);
			logger.warn("this cityId has no oldDevice online,cityId:{}",cityId);
			return result;
		}
		
		List<Map> deviceList = dao.getOldDayCompleteDevByCityId(cityId,curPage_splitPage,num_splitPage);
		result.put("list", deviceList );
		return result;
	}
	
	
	/**
	 * 获取老旧终端在用设备的详情信息导出
	 * @param cityId
	 * @return
	 */
	public List<Map> queryOldDevDetailExcel(String cityId ){
		return dao.queryOldDevDetailExcel(cityId);
	}
	
	public List<Map> queryOldCompleteDevDetailExcel(String cityId ){
		return dao.queryOldCompleteDevDetailExcel(cityId);
	}
	
	public List<Map> getOldDayCompleteDevDetailExcel(String cityId ){
		return dao.queryOldDayCompleteDevDetailExcel(cityId);
	}
	
	/**
	 * pt921g 终端未完成量详情导出
	 * @return
	 */
	public List<Map> getPt921gDetailExcel(String cityId){
		return dao.getPt921gDetailExcel(cityId);
	}
	
	/**
	 * 第三批 终端未完成量详情导出
	 * @return
	 */
	public List<Map> getThrdDetailExcel(String cityId){
		return dao.getThrdDetailExcel(cityId);
	}

	public TerminalNoMatchReportDAO getDao() {
		return dao;
	}

	public void setDao(TerminalNoMatchReportDAO dao) {
		this.dao = dao;
	}
	
}
