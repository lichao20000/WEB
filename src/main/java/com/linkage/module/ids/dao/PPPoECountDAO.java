package com.linkage.module.ids.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.elasticsearch.bio.ElasticsearchInitBIO;
import com.linkage.commons.elasticsearch.util.ElasticDataUtil;
import com.linkage.commons.elasticsearch.util.MySearchOption;
import com.linkage.commons.elasticsearch.util.MySearchOption.OperType;
import com.linkage.commons.elasticsearch.util.MySearchOption.SearchLogic;
import com.linkage.commons.elasticsearch.util.MySearchOption.SearchType;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class PPPoECountDAO extends SuperDAO {
	/**
	 * PPPoE失败原因统计
	 * 
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @return
	 */
	private static Logger logger = LoggerFactory
			.getLogger(PPPoECountDAO.class);
	private ElasticDataUtil edu = null;
	List<String> cityList = null;
	// 按本地网统计
	Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();

	/**
	 * 用户网络状态评测统计查询
	 * 
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @return
	 */
	public List<Map<String, Object>> countPPPoE(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			int curPage_splitPage, int num_splitPage) {
		ArrayList<MySearchOption> searchOptionList = null;
		Map<String, String> totalmap1 = new HashMap<String, String>();
		Map<String, String> totalmap2 = new HashMap<String, String>();
		Map<String, String> totalmap3 = new HashMap<String, String>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		long total1, total2, total3 = 0;
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		String reason = null;
		reason = "ERROR_ISP_TIME_OUT";
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)
				&& !"-1".equals(cityId)) {
			searchOptionList = getQueryParamByReason(starttime, endtime,
					cityId, reason, curPage_splitPage, num_splitPage);
			total1 = StringUtil.getIntegerValue(edu.getCount(indexType,
					searchOptionList));
			totalmap1.put(cityId, String.valueOf(total1));
		} else {
			cityList = CityDAO.getNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++) {
				searchOptionList = getQueryParamByReason(starttime, endtime,
						String.valueOf(cityList.get(i)), reason,
						curPage_splitPage, num_splitPage);
				total1 = StringUtil.getIntegerValue(edu.getCount(indexType,
						searchOptionList));
				totalmap1.put(String.valueOf(cityList.get(i)),
						String.valueOf(total1));
			}
		}
		reason = "ERROR_AUTHENTICATION_FAILURE";
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)
				&& !"-1".equals(cityId)) {
			searchOptionList = getQueryParamByReason(starttime, endtime,
					cityId, reason, curPage_splitPage, num_splitPage);
			total2 = StringUtil.getIntegerValue(edu.getCount(indexType,
					searchOptionList));
			totalmap2.put(cityId, String.valueOf(total2));
		} else {
			cityList = CityDAO.getNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++) {
				searchOptionList = getQueryParamByReason(starttime, endtime,
						String.valueOf(cityList.get(i)), reason,
						curPage_splitPage, num_splitPage);
				total2 = StringUtil.getIntegerValue(edu.getCount(indexType,
						searchOptionList));
				totalmap2.put(String.valueOf(cityList.get(i)),
						String.valueOf(total2));
			}
		}
		reason = "ERROR_ISP_DISCONNECT";
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)
				&& !"-1".equals(cityId)) {
			searchOptionList = getQueryParamByReason(starttime, endtime,
					cityId, reason, curPage_splitPage, num_splitPage);
			total3 = StringUtil.getIntegerValue(edu.getCount(indexType,
					searchOptionList));
			totalmap3.put(cityId, String.valueOf(total3));
		} else {
			cityList = CityDAO.getNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++) {
				searchOptionList = getQueryParamByReason(starttime, endtime,
						String.valueOf(cityList.get(i)), reason,
						curPage_splitPage, num_splitPage);
				total3 = StringUtil.getIntegerValue(edu.getCount(indexType,
						searchOptionList));
				totalmap3.put(String.valueOf(cityList.get(i)),
						String.valueOf(total3));
			}
		}

		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)
				&& !"-1".equals(cityId)) {
			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("cityId", cityId);
			tmap.put("cityName", cityMap.get(cityId));
			tmap.put("timeoutNum",
					StringUtil.getIntegerValue(totalmap1.get(cityId)));
			tmap.put("failureNum",
					StringUtil.getIntegerValue(totalmap2.get(cityId)));
			tmap.put("disConnectNum",
					StringUtil.getIntegerValue(totalmap3.get(cityId)));
			list.add(tmap);
		} else {
			cityList = CityDAO.getNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++) {
				Map<String, Object> tmap = new HashMap<String, Object>();
				tmap.put("cityId", cityList.get(i));
				tmap.put("cityName", cityMap.get(cityList.get(i)));
				tmap.put("timeoutNum", StringUtil.getIntegerValue(totalmap1
						.get(cityList.get(i))));
				tmap.put("failureNum", StringUtil.getIntegerValue(totalmap2
						.get(cityList.get(i))));
				tmap.put("disConnectNum", StringUtil.getIntegerValue(totalmap3
						.get(cityList.get(i))));
				list.add(tmap);
			}
		}
		return list;
	}

	/**
	 * 分页查询
	 * 
	 * @param indexName
	 * @param indexType
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryByPPPoECount(String indexName, String indexType,
			String starttime, String endtime, String cityId, String operType,
			int curPage_splitPage, int num_splitPage) {
		int total = 0;
		ArrayList<MySearchOption> searchOptionList = null;
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		String reason = null;
		if ("1".equals(operType)) {
			reason = "ERROR_ISP_TIME_OUT";
			searchOptionList = getQueryParamByReason(starttime, endtime,
					cityId, reason, curPage_splitPage, num_splitPage);
			total = StringUtil.getIntegerValue(edu.getCount(indexType,
					searchOptionList));
		} else if ("2".equals(operType)) {
			reason = "ERROR_AUTHENTICATION_FAILURE";
			searchOptionList = getQueryParamByReason(starttime, endtime,
					cityId, reason, curPage_splitPage, num_splitPage);
			total = StringUtil.getIntegerValue(edu.getCount(indexType,
					searchOptionList));
		} else {
			reason = "ERROR_ISP_DISCONNECT";
			searchOptionList = getQueryParamByReason(starttime, endtime,
					cityId, reason, curPage_splitPage, num_splitPage);
			total = StringUtil.getIntegerValue(edu.getCount(indexType,
					searchOptionList));
		}
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = (int) (total / num_splitPage);
		} else {
			maxPage = (int) (total / num_splitPage + 1);
		}
		return maxPage;
	}

	/**
	 * 查询总记录数
	 * 
	 * @param indexName
	 * @param indexType
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryByPPPoETotal(String indexName, String indexType,
			String starttime, String endtime, String cityId, String operType,
			int curPage_splitPage, int num_splitPage) {
		ArrayList<MySearchOption> searchOptionList = null;
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		searchOptionList = getQueryParam(starttime, endtime, cityId,
				curPage_splitPage, num_splitPage);
		int count = StringUtil.getIntegerValue(edu.getCount(indexType,
				searchOptionList));
		return count;
	}

	/**
	 * 导出统计页面
	 * 
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @return
	 */
	public List<Map<String, Object>> queryByPPPoEExcel(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			String operType, int curPage_splitPage, int num_splitPage, int total) {
		return countPPPoE(indexName, indexType, starttime, endtime, cityId,
				curPage_splitPage, num_splitPage);
	}

	/**
	 * 详细列表页面
	 * 
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @return
	 */
	public List<Map<String, Object>> queryByPPPoEList(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			String operType, int curPage_splitPage, int num_splitPage) {

		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		ArrayList<MySearchOption> searchOptionList = null;
		String reason = null;
		if ("1".equals(operType)) {
			reason = "ERROR_ISP_TIME_OUT";
			searchOptionList = getQueryParamByReason(starttime, endtime,
					cityId, reason, curPage_splitPage, num_splitPage);
		} else if ("2".equals(operType)) {
			reason = "ERROR_AUTHENTICATION_FAILURE";
			searchOptionList = getQueryParamByReason(starttime, endtime,
					cityId, reason, curPage_splitPage, num_splitPage);
		} else {
			reason = "ERROR_ISP_DISCONNECT";
			searchOptionList = getQueryParamByReason(starttime, endtime,
					cityId, reason, curPage_splitPage, num_splitPage);
		}
		int from = (curPage_splitPage - 1) * num_splitPage;
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = edu.simpleSearch(indexType,
				searchOptionList, from, num_splitPage, "upload_time", "desc");
		if (null != list) {
			Map<String, Object> map = null;
			for (Map<String, Object> remap : list) {
				if (null != remap) {
					map = new HashMap<String, Object>();
					String city_id = String.valueOf(remap.get("city_id"));
					String city_name = StringUtil.getStringValue(cityMap
							.get(city_id));
					map.put("city_name", city_name);
					map.put("loid", remap.get("loid"));
					map.put("oui", remap.get("oui"));
					map.put("device_serialnumber",
							remap.get("device_serialnumber"));
					map.put("status", remap.get("status"));
					map.put("reason", remap.get("reason"));
					long upload_time = StringUtil.getLongValue(remap
							.get("upload_time"));
					DateTimeUtil dt = new DateTimeUtil(upload_time * 1000);
					map.put("upload_time", dt.getLongDate());
					long add_time = StringUtil.getLongValue(remap
							.get("add_time"));
					DateTimeUtil dateTimeUtil = new DateTimeUtil(
							add_time * 1000);
					map.put("add_time", dateTimeUtil.getLongDate());
				}
				returnList.add(map);
			}
		}
		return returnList;

	}

	/**
	 * 设置查询条件
	 * 
	 * @param reason
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param reason
	 * @return
	 */
	private ArrayList<MySearchOption> getQueryParamByReason(String starttime,
			String endtime, String cityId, String reason,
			int curPage_splitPage, int num_splitPage) {
		ArrayList<MySearchOption> searchOptionList = new ArrayList<MySearchOption>();
		MySearchOption mySearchOption1 = null;
		MySearchOption mySearchOption2 = null;
		MySearchOption mySearchOption3 = null;
		MySearchOption mySearchOption4 = null;
		if (!StringUtil.IsEmpty(starttime)) {
			mySearchOption1 = new MySearchOption("upload_time",
					SearchType.range, SearchLogic.must, OperType.gt,
					String.valueOf(new DateTimeUtil(starttime).getLongTime()));
			searchOptionList.add(mySearchOption1);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			mySearchOption2 = new MySearchOption("upload_time",
					SearchType.range, SearchLogic.must, OperType.lt,
					String.valueOf(new DateTimeUtil(endtime).getLongTime()));
			searchOptionList.add(mySearchOption2);
		}
		if (!StringUtil.IsEmpty(cityId)) {
			mySearchOption3 = new MySearchOption("city_id", SearchType.term,
					SearchLogic.must, cityId);
			searchOptionList.add(mySearchOption3);
		}
		if (!StringUtil.IsEmpty(reason)) {
			mySearchOption4 = new MySearchOption("reason", SearchType.term,
					SearchLogic.must, reason);
			searchOptionList.add(mySearchOption4);
		}
		return searchOptionList;
	}

	/**
	 * 设置查询条件
	 * 
	 * @param reason
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param cityId
	 * @return
	 */
	private ArrayList<MySearchOption> getQueryParam(String starttime,
			String endtime, String cityId, int curPage_splitPage,
			int num_splitPage) {
		ArrayList<MySearchOption> searchOptionList = new ArrayList<MySearchOption>();
		MySearchOption mySearchOption1 = null;
		MySearchOption mySearchOption2 = null;
		MySearchOption mySearchOption3 = null;
		if (!StringUtil.IsEmpty(starttime)) {
			mySearchOption1 = new MySearchOption("upload_time",
					SearchType.range, SearchLogic.must, OperType.gt,
					String.valueOf(new DateTimeUtil(starttime).getLongTime()));
			searchOptionList.add(mySearchOption1);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			mySearchOption2 = new MySearchOption("upload_time",
					SearchType.range, SearchLogic.must, OperType.lt,
					String.valueOf(new DateTimeUtil(endtime).getLongTime()));
			searchOptionList.add(mySearchOption2);
		}
		if (!StringUtil.IsEmpty(cityId)) {
			mySearchOption3 = new MySearchOption("city_id", SearchType.term,
					SearchLogic.must, cityId);
			searchOptionList.add(mySearchOption3);
		}
		return searchOptionList;
	}

	public static Logger getLogger() {
		return logger;
	}
}
