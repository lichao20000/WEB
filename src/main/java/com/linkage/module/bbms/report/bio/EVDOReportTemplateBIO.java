/**
 * 
 */
package com.linkage.module.bbms.report.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.bbms.report.dao.EVDOReportTemplateDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-11-20
 * @category com.linkage.module.bbms.report.bio
 * 
 */
public class EVDOReportTemplateBIO {
	/** log */
	private Logger logger = LoggerFactory.getLogger(EVDOReportTemplateBIO.class);
	
	EVDOReportTemplateDAO evdoDAO = null;

	/**
	 * 统计在线时长
	 * 
	 * @param reportType
	 *            报表类型 ‘1’日报表 ‘2’周报表 ‘3’月报表
	 * @param countDate
	 *            统计日期 秒格式
	 * @param countType
	 *            统计类型 ‘1’表示按区域 ‘2’表示按行业
	 * @return
	 */
	public ArrayList<Map<String, String>> getTimeLengthData(String reportType, String countDate,
			String countType) {
		logger.debug(
				"getTimeLengthData(reportType:{},countDate:{},countType:{})",
				new Object[] { reportType, countDate, countType });
		
		DateTimeUtil dt = new DateTimeUtil(Long.parseLong(countDate)*1000);
		//根据不同的报表类型，调整不同的日期格式
		if ("1".equals(reportType)) {
			countDate = dt.getYYYYMMDD();
		}else if ("2".equals(reportType)) {
			countDate = dt.getYYYYWW();
		}else{
			countDate = dt.getYYYYMM();
		}
		List rsList = evdoDAO.getTimeLengthData(reportType, countDate, countType);
		//返回数据
		ArrayList<Map<String, String>> aList = new ArrayList<Map<String, String>>();
		long wireTime = 0;
		long wirelessTime = 0;
		long wireTimeTemp = 0;
		long wirelessTimeTemp = 0;
		//区别统计方式
		if("1".equals(countType)){
			for(int i=0;i<rsList.size();i++){
				Map one = (Map)rsList.get(i);
				Map<String, String> aMap = new HashMap<String, String>();
				Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
				aMap.put("count_desc",cityMap.get(String.valueOf(one.get("count_desc"))));
				cityMap = null;
				wireTimeTemp = Long.parseLong(String.valueOf(one.get("wire_time")));
				wirelessTimeTemp = Long.parseLong(String.valueOf(one.get("wireless_time")));
				wireTime += wireTimeTemp;
				wirelessTime += wirelessTimeTemp;
				aMap.put("wire_time", DateTimeUtil.parseDateStr(wireTimeTemp));
				aMap.put("wireless_time", DateTimeUtil.parseDateStr(wirelessTimeTemp));
				aMap.put("all_time", DateTimeUtil.parseDateStr(wireTimeTemp+wirelessTimeTemp));
				aList.add(aMap);
			}
		}else{
			Map<String, String> cMap = getAllCustomerType();
			for(int i=0;i<rsList.size();i++){
				Map one = (Map)rsList.get(i);
				Map<String, String> aMap = new HashMap<String, String>();
				aMap.put("count_desc", cMap.get(String.valueOf(one.get("count_desc"))));
				wireTimeTemp = Long.parseLong(String.valueOf(one.get("wire_time")));
				wirelessTimeTemp = Long.parseLong(String.valueOf(one.get("wireless_time")));
				wireTime += wireTimeTemp;
				wirelessTime += wirelessTimeTemp;
				aMap.put("wire_time", DateTimeUtil.parseDateStr(wireTimeTemp));
				aMap.put("wireless_time", DateTimeUtil.parseDateStr(wirelessTimeTemp));
				aMap.put("all_time", DateTimeUtil.parseDateStr(wireTimeTemp+wirelessTimeTemp));
				aList.add(aMap);
			}
		}
		//求合计
//		Map<String, String> sumMap = new HashMap<String, String>();
//		sumMap.put("count_desc", "总计");
//		sumMap.put("wire_time", DateTimeUtil.parseDateStr(wireTime));
//		sumMap.put("wireless_time", DateTimeUtil.parseDateStr(wirelessTime));
//		sumMap.put("all_time", DateTimeUtil.parseDateStr(wireTime+wirelessTime));
//		aList.add(sumMap);
		//求百分比
		Map<String, String> avgMap = new HashMap<String, String>();
		avgMap.put("count_desc", "占比");
		avgMap.put("wire_time", (wireTime+wirelessTime)==(long)0?"0%":String.valueOf((wireTime)*100/(wireTime+wirelessTime)+"%"));
		avgMap.put("wireless_time", (wireTime+wirelessTime)==(long)0?"0%":String.valueOf((wirelessTime)*100/(wireTime+wirelessTime)+"%"));
		avgMap.put("all_time", (wireTime+wirelessTime)==(long)0?"-":"100%");
		aList.add(avgMap);
		return aList;
	}
	
	/**
	 * 统计网关流量
	 * 
	 * @param reportType
	 *            报表类型 ‘1’日报表 ‘2’周报表 ‘3’月报表
	 * @param countDate
	 *            统计日期 秒格式
	 * @param countType
	 *            统计类型 ‘1’表示按区域 ‘2’表示按行业
	 * @return
	 */
	public ArrayList<Map<String, String>> getFluxData(String reportType, String countDate,
			String countType) {
		logger.debug(
				"getFluxData(reportType:{},countDate:{},countType:{})",
				new Object[] { reportType, countDate, countType });
		
		DateTimeUtil dt = new DateTimeUtil(Long.parseLong(countDate)*1000);
		//根据不同的报表类型，调整不同的日期格式
		if ("1".equals(reportType)) {
			countDate = dt.getYYYYMMDD();
		}else if ("2".equals(reportType)) {
			countDate = dt.getYYYYWW();
		}else{
			countDate = dt.getYYYYMM();
		}
		List rsList = evdoDAO.getFluxData(reportType, countDate, countType);
		//返回数据
		ArrayList<Map<String, String>> aList = new ArrayList<Map<String, String>>();
		long wireUpFlux = 0;
		long wireDownFlux = 0;
		long wirelessUpFlux = 0;
		long wirelessDownFlux = 0;
		long wireUpFluxTemp = 0;
		long wireDownFluxTemp = 0;
		long wirelessUpFluxTemp = 0;
		long wirelessDownFluxTemp = 0;
		//区别统计方式
		if("1".equals(countType)){
			for(int i=0;i<rsList.size();i++){
				Map one = (Map)rsList.get(i);
				Map<String, String> aMap = new HashMap<String, String>();
				Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
				aMap.put("count_desc",cityMap.get(String.valueOf(one.get("count_desc"))));
				cityMap = null;
				wireUpFluxTemp = Long.parseLong(String.valueOf(one.get("wire_up_flux")));
				wireDownFluxTemp = Long.parseLong(String.valueOf(one.get("wire_down_flux")));
				wirelessUpFluxTemp = Long.parseLong(String.valueOf(one.get("wireless_up_flux")));
				wirelessDownFluxTemp = Long.parseLong(String.valueOf(one.get("wireless_down_flux")));
				wireUpFlux += wireUpFlux;
				wireDownFlux += wireDownFlux;
				wirelessUpFlux += wirelessUpFlux;
				wirelessDownFlux += wirelessDownFlux;
				aMap.put("wire_up_flux", String.valueOf(wireUpFluxTemp));
				aMap.put("wire_down_flux", String.valueOf(wireDownFluxTemp));
				aMap.put("wireless_up_flux", String.valueOf(wirelessUpFluxTemp));
				aMap.put("wireless_down_flux", String.valueOf(wirelessDownFluxTemp));
				aMap.put("all_time", String.valueOf(wireUpFlux+wireDownFlux+wirelessUpFlux+wirelessDownFlux));
				aList.add(aMap);
			}
		}else{
			Map<String, String> cMap = getAllCustomerType();
			for(int i=0;i<rsList.size();i++){
				Map one = (Map)rsList.get(i);
				Map<String, String> aMap = new HashMap<String, String>();
				aMap.put("count_desc", cMap.get(String.valueOf(one.get("count_desc"))));
				wireUpFluxTemp = Long.parseLong(String.valueOf(one.get("wire_up_flux")));
				wireDownFluxTemp = Long.parseLong(String.valueOf(one.get("wire_down_flux")));
				wirelessUpFluxTemp = Long.parseLong(String.valueOf(one.get("wireless_up_flux")));
				wirelessDownFluxTemp = Long.parseLong(String.valueOf(one.get("wireless_down_flux")));
				wireUpFlux += wireUpFlux;
				wireDownFlux += wireDownFlux;
				wirelessUpFlux += wirelessUpFlux;
				wirelessDownFlux += wirelessDownFlux;
				aMap.put("wire_up_flux", String.valueOf(wireUpFluxTemp));
				aMap.put("wire_down_flux", String.valueOf(wireDownFluxTemp));
				aMap.put("wireless_up_flux", String.valueOf(wirelessUpFluxTemp));
				aMap.put("wireless_down_flux", String.valueOf(wirelessDownFluxTemp));
				aMap.put("all_time", String.valueOf(wireUpFluxTemp+wireDownFluxTemp+wirelessUpFluxTemp+wirelessDownFluxTemp));
				aList.add(aMap);
			}
		}
		long all = wireUpFlux+wireDownFlux+wirelessUpFlux+wirelessDownFlux;
		//求合计
//		Map<String, String> sumMap = new HashMap<String, String>();
//		sumMap.put("count_desc", "总计");
//		sumMap.put("wire_up_flux",String.valueOf(wireUpFlux));
//		sumMap.put("wire_down_flux", String.valueOf(wireDownFlux));
//		sumMap.put("wireless_up_flux", String.valueOf(wirelessUpFlux));
//		sumMap.put("wireless_down_flux", String.valueOf(wirelessDownFlux));
//		sumMap.put("all_time", String.valueOf(all));
//		aList.add(sumMap);
		//求百分比
		Map<String, String> avgMap = new HashMap<String, String>();
		avgMap.put("count_desc", "占比");
		avgMap.put("wire_up_flux", (all)==(long)0?"0%":String.valueOf((wireUpFlux)*100/(all)+"%"));
		avgMap.put("wire_down_flux", (all)==(long)0?"0%":String.valueOf((wireDownFlux)*100/(all)+"%"));
		avgMap.put("wireless_up_flux", (all)==(long)0?"0%":String.valueOf((wirelessUpFlux)*100/(all)+"%"));
		avgMap.put("wireless_down_flux", (all)==(long)0?"0%":String.valueOf((wirelessDownFlux)*100/(all)+"%"));
		avgMap.put("all_time", (all)==(long)0?"-":"100%");
		aList.add(avgMap);
		return aList;
	}
	
	/**
	 * 统计网关频次
	 * 
	 * @param reportType
	 *            报表类型 ‘1’日报表 ‘2’周报表 ‘3’月报表
	 * @param countDate
	 *            统计日期 秒格式
	 * @param countType
	 *            统计类型 ‘1’表示按区域 ‘2’表示按行业
	 * @return
	 */
	public ArrayList<Map<String, String>> getFrequencyData(String reportType, String countDate,
			String countType) {
		logger.debug(
				"getFrequencyData(reportType:{},countDate:{},countType:{})",
				new Object[] { reportType, countDate, countType });
		
		DateTimeUtil dt = new DateTimeUtil(Long.parseLong(countDate)*1000);
		//根据不同的报表类型，调整不同的日期格式
		if ("1".equals(reportType)) {
			countDate = dt.getYYYYMMDD();
		}else if ("2".equals(reportType)) {
			countDate = dt.getYYYYWW();
		}else{
			countDate = dt.getYYYYMM();
		}
		List rsList = evdoDAO.getFrequencyData(reportType, countDate, countType);
		//返回数据
		ArrayList<Map<String, String>> aList = new ArrayList<Map<String, String>>();
		long frequency = 0;
		long frequencyTemp = 0;
		//区别统计方式
		if("1".equals(countType)){
			for(int i=0;i<rsList.size();i++){
				Map one = (Map)rsList.get(i);
				Map<String, String> aMap = new HashMap<String, String>();
				Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
				aMap.put("count_desc",cityMap.get(String.valueOf(one.get("count_desc"))));
				cityMap = null;
				frequencyTemp = Long.parseLong(String.valueOf(one.get("frequency")));
				frequency += frequency;
				aMap.put("frequency", String.valueOf(frequencyTemp));
				aList.add(aMap);
			}
		}else{
			Map<String, String> cMap = getAllCustomerType();
			for(int i=0;i<rsList.size();i++){
				Map one = (Map)rsList.get(i);
				Map<String, String> aMap = new HashMap<String, String>();
				aMap.put("count_desc", cMap.get(String.valueOf(one.get("count_desc"))));
				frequencyTemp = Long.parseLong(String.valueOf(one.get("frequency")));
				frequency += frequency;
				aMap.put("frequency", String.valueOf(frequencyTemp));
				aList.add(aMap);
			}
		}
		//求合计
//		Map<String, String> sumMap = new HashMap<String, String>();
//		sumMap.put("count_desc", "总计");
//		sumMap.put("frequency",String.valueOf(frequency));
//		aList.add(sumMap);
		return aList;
	}
	
	/**
	 * 统计网关时段
	 * 
	 * @param reportType
	 *            报表类型 ‘1’日报表 ‘2’周报表 ‘3’月报表
	 * @param countDate
	 *            统计日期 秒格式
	 * @param countType
	 *            统计类型 ‘1’表示按区域 ‘2’表示按行业
	 * @return
	 */
	public ArrayList<Map<String, String>> getTmslotData(String reportType, String countDate,
			String countType) {
		logger.debug(
				"getTmslotData(reportType:{},countDate:{},countType:{})",
				new Object[] { reportType, countDate, countType });
		
		DateTimeUtil dt = new DateTimeUtil(Long.parseLong(countDate)*1000);
		//根据不同的报表类型，调整不同的日期格式
		if ("1".equals(reportType)) {
			countDate = dt.getYYYYMMDD();
		}else if ("2".equals(reportType)) {
			countDate = dt.getYYYYWW();
		}else{
			countDate = dt.getYYYYMM();
		}
		List rsList = evdoDAO.getTmslotData(reportType, countDate, countType);
		//返回数据
		ArrayList<Map<String, String>> aList = new ArrayList<Map<String, String>>();
		long tmslot[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		long tmslotTemp[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		long all = 0,all_one=0;
		//区别统计方式
		if("1".equals(countType)){
			for(int i=0;i<rsList.size();i++){
				all_one = 0;
				Map one = (Map)rsList.get(i);
				Map<String, String> aMap = new HashMap<String, String>();
				Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
				aMap.put("count_desc",cityMap.get(String.valueOf(one.get("count_desc"))));
				cityMap = null;
				for(int j=7;j<19;j++){
					tmslotTemp[j] = Long.parseLong(String.valueOf(one.get("tmslot_"+j)));
					tmslot[j] += tmslotTemp[j];
					aMap.put("tmslot_"+j,String.valueOf(tmslotTemp[j]));
					all_one += tmslotTemp[j];
					all += tmslotTemp[j];
				}
				aMap.put("all", String.valueOf(all_one));
				aList.add(aMap);
			}
		}else{
			Map<String, String> cMap = getAllCustomerType();
			for(int i=0;i<rsList.size();i++){
				all_one = 0;
				Map one = (Map)rsList.get(i);
				Map<String, String> aMap = new HashMap<String, String>();
				aMap.put("count_desc", cMap.get(String.valueOf(one.get("count_desc"))));
				for(int j=7;j<19;j++){
					tmslotTemp[j] = Long.parseLong(String.valueOf(one.get("tmslot_"+j)));
					tmslot[j] += tmslotTemp[j];
					aMap.put("tmslot_"+j,String.valueOf(tmslotTemp[j]));
					all_one = tmslotTemp[j];
					all += tmslotTemp[j];
				}
				aMap.put("all", String.valueOf(all_one));
				aList.add(aMap);
			}
		}
		//求合计
//		Map<String, String> sumMap = new HashMap<String, String>();
		//求百分比
		Map<String, String> avgMap = new HashMap<String, String>();
//		sumMap.put("count_desc", "总计");
		avgMap.put("count_desc", "占比");
		for(int j=7;j<19;j++){
//			sumMap.put("tmslot_"+j,String.valueOf(tmslot[j]));
			avgMap.put("tmslot_"+j,(all)==(long)0?"0%":String.valueOf(tmslot[j]*100/all));
		}
//		sumMap.put("all", String.valueOf(all));
		avgMap.put("all", (all)==(long)0?"-":"100%");
//		aList.add(sumMap);
		aList.add(avgMap);
		return aList;
	}
	
	/**
	 * 统计EVDO激活情况
	 * 
	 * @param reportType
	 *            报表类型 ‘1’日报表 ‘2’周报表 ‘3’月报表
	 * @param countDate
	 *            统计日期 秒格式
	 * @param countType
	 *            统计类型 ‘1’表示按区域 ‘2’表示按行业
	 * @return
	 */
	public ArrayList<Map<String, String>> getActiveData(String reportType, String countDate,
			String countType) {
		logger.debug(
				"getActiveData(reportType:{},countDate:{},countType:{})",
				new Object[] { reportType, countDate, countType });
		
		DateTimeUtil dt = new DateTimeUtil(Long.parseLong(countDate)*1000);
		//根据不同的报表类型，调整不同的日期格式
		if ("1".equals(reportType)) {
			countDate = dt.getYYYYMMDD();
		}else if ("2".equals(reportType)) {
			countDate = dt.getYYYYWW();
		}else{
			countDate = dt.getYYYYMM();
		}
		List rsList = evdoDAO.getActiveData(reportType, countDate, countType);
		//返回数据
		ArrayList<Map<String, String>> aList = new ArrayList<Map<String, String>>();
		long activeNum = 0;
		long activelessNum = 0;
		long activeNumTemp = 0;
		long activelessNumTemp = 0;
		//区别统计方式
		if("1".equals(countType)){
			for(int i=0;i<rsList.size();i++){
				Map one = (Map)rsList.get(i);
				Map<String, String> aMap = new HashMap<String, String>();
				Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
				aMap.put("count_desc",cityMap.get(String.valueOf(one.get("count_desc"))));
				cityMap = null;
				activeNumTemp = Long.parseLong(String.valueOf(one.get("active_num")));
				activelessNumTemp = Long.parseLong(String.valueOf(one.get("activeless_num")));
				activeNum += activeNumTemp;
				activelessNum += activelessNumTemp;
				aMap.put("active_num", String.valueOf(activeNumTemp));
				aMap.put("activeless_num", String.valueOf(activelessNumTemp));
				aMap.put("all_num", String.valueOf(activeNumTemp+activelessNumTemp));
				aList.add(aMap);
			}
		}else{
			Map<String, String> cMap = getAllCustomerType();
			for(int i=0;i<rsList.size();i++){
				Map one = (Map)rsList.get(i);
				Map<String, String> aMap = new HashMap<String, String>();
				aMap.put("count_desc", cMap.get(String.valueOf(one.get("count_desc"))));
				activeNumTemp = Long.parseLong(String.valueOf(one.get("active_num")));
				activelessNumTemp = Long.parseLong(String.valueOf(one.get("activeless_num")));
				activeNum += activeNumTemp;
				activelessNum += activelessNumTemp;
				aMap.put("active_num", String.valueOf(activeNumTemp));
				aMap.put("activeless_num", String.valueOf(activelessNumTemp));
				aMap.put("all_num", String.valueOf(activeNumTemp+activelessNumTemp));
				aList.add(aMap);
			}
		}
		//求合计
//		Map<String, String> sumMap = new HashMap<String, String>();
//		sumMap.put("count_desc", "总计");
//		sumMap.put("active_num", String.valueOf(activeNum));
//		sumMap.put("activeless_num", String.valueOf(activelessNum));
//		sumMap.put("all_num", String.valueOf(activeNum+activelessNum));
//		aList.add(sumMap);
		//求百分比
		Map<String, String> avgMap = new HashMap<String, String>();
		avgMap.put("count_desc", "占比");
		avgMap.put("active_num", (activeNum+activelessNum)==(long)0?"0%":String.valueOf((activeNum)*100/(activeNum+activelessNum)+"%"));
		avgMap.put("activeless_num", (activeNum+activelessNum)==(long)0?"0%":String.valueOf((activelessNum)*100/(activeNum+activelessNum)+"%"));
		avgMap.put("all_num", (activeNum+activelessNum)==(long)0?"-":"100%");
		aList.add(avgMap);
		return aList;
	}
	
	/**
	 * 统计EVDO主备情况
	 * 
	 * @param reportType
	 *            报表类型 ‘1’日报表 ‘2’周报表 ‘3’月报表
	 * @param countDate
	 *            统计日期 秒格式
	 * @param countType
	 *            统计类型 ‘1’表示按区域 ‘2’表示按行业
	 * @return
	 */
	public ArrayList<Map<String, String>> getEvdoTypeData(String reportType, String countDate,
			String countType) {
		logger.debug(
				"getEvdoType(reportType:{},countDate:{},countType:{})",
				new Object[] { reportType, countDate, countType });
		
		DateTimeUtil dt = new DateTimeUtil(Long.parseLong(countDate)*1000);
		//根据不同的报表类型，调整不同的日期格式
		//由于EVDO主备情况只有周报表，估不做判断
		countDate = dt.getYYYYWW();
		List rsList = evdoDAO.getEvdoTypeData(reportType, countDate, countType);
		//返回数据
		ArrayList<Map<String, String>> aList = new ArrayList<Map<String, String>>();
		long mainNum = 0;
		long standyNum = 0;
		long mainNumTemp = 0;
		long standyNumTemp = 0;
		//区别统计方式
		if("1".equals(countType)){
			for(int i=0;i<rsList.size();i++){
				Map one = (Map)rsList.get(i);
				Map<String, String> aMap = new HashMap<String, String>();
				Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
				aMap.put("count_desc",cityMap.get(String.valueOf(one.get("count_desc"))));
				cityMap = null;
				mainNumTemp = Long.parseLong(String.valueOf(one.get("main_num")));
				standyNumTemp = Long.parseLong(String.valueOf(one.get("standy_num")));
				mainNum += mainNumTemp;
				standyNum += standyNumTemp;
				aMap.put("main_num", String.valueOf(mainNumTemp));
				aMap.put("standy_num", String.valueOf(standyNumTemp));
				aMap.put("all_num", String.valueOf(mainNumTemp+standyNumTemp));
				aList.add(aMap);
			}
		}else{
			Map<String, String> cMap = getAllCustomerType();
			for(int i=0;i<rsList.size();i++){
				Map one = (Map)rsList.get(i);
				Map<String, String> aMap = new HashMap<String, String>();
				aMap.put("count_desc", cMap.get(String.valueOf(one.get("count_desc"))));
				Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
				aMap.put("count_desc",cityMap.get(String.valueOf(one.get("count_desc"))));
				cityMap = null;
				mainNumTemp = Long.parseLong(String.valueOf(one.get("main_num")));
				standyNumTemp = Long.parseLong(String.valueOf(one.get("standy_num")));
				mainNum += mainNumTemp;
				standyNum += standyNumTemp;
				aMap.put("main_num", String.valueOf(mainNumTemp));
				aMap.put("standy_num", String.valueOf(standyNumTemp));
				aMap.put("all_num", String.valueOf(mainNumTemp+standyNumTemp));
				aList.add(aMap);
			}
		}
		//求合计
//		Map<String, String> sumMap = new HashMap<String, String>();
//		sumMap.put("count_desc", "总计");
//		sumMap.put("main_num", String.valueOf(mainNum));
//		sumMap.put("standy_num", String.valueOf(standyNum));
//		sumMap.put("all_num", String.valueOf(mainNum+standyNum));
//		aList.add(sumMap);
		//求百分比
		Map<String, String> avgMap = new HashMap<String, String>();
		avgMap.put("count_desc", "占比");
		avgMap.put("main_num", (mainNum+standyNum)==(long)0?"0%":String.valueOf((mainNum)*100/(mainNum+standyNum)+"%"));
		avgMap.put("standy_num", (mainNum+standyNum)==(long)0?"0%":String.valueOf((standyNum)*100/(mainNum+standyNum)+"%"));
		avgMap.put("all_num", (mainNum+standyNum)==(long)0?"-":"100%");
		aList.add(avgMap);
		return aList;
	}
	
	/**
	 * 查询所有的行业类型
	 * @return
	 */
	public Map<String, String> getAllCustomerType(){
		logger.debug("getAllCustomerType()");
		List list = evdoDAO.getCustomerType();
		Map<String, String> rsMap = new HashMap<String, String>();
		for(int i=0;i<list.size();i++){
			Map one = (Map)list.get(i);
			rsMap.put(String.valueOf(one.get("cust_type_id")), String.valueOf(one.get("cust_type_name")));
		}
		return rsMap;
	}
	/**
	 * @return the evdoDAO
	 */
	public EVDOReportTemplateDAO getEvdoDAO() {
		return evdoDAO;
	}

	/**
	 * @param evdoDAO the evdoDAO to set
	 */
	public void setEvdoDAO(EVDOReportTemplateDAO evdoDAO) {
		this.evdoDAO = evdoDAO;
	}
	
}
