package com.linkage.module.ids.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
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

public class ElasticCityDroopDAO extends SuperDAO {
	/**
	 * 用户网络状态评测查询
	 * 
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @return
	 */
	private static Logger logger = LoggerFactory
			.getLogger(ElasticCityDroopDAO.class);
	private ElasticDataUtil edu = null;
	List<String> cityList = null;
	float maxDp = 0, minDp = 0;// 光衰最大值,光衰最小值
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
	public List<Map<String, Object>> countCityDroop(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			int curPage_splitPage, int num_splitPage) {
		ArrayList<MySearchOption> droopSearchOptionList, searchOptionList = null;
		long droopNum = 0, disDroopNum = 0, total = 0;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		Map<String, String> totalmap = new HashMap<String, String>();
		Map<String, String> droopmap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(
				"select indicators,maximum,minimum from table_userstate_rating where indicators = 'droop' ");
		List<HashMap<String, String>> queryList = DBOperation.getRecords(psql
				.getSQL());
		if (null != queryList && queryList.size() > 0) {
			maxDp = Float.parseFloat((queryList.get(0).get("maximum")));
			minDp = Float.parseFloat((queryList.get(0).get("minimum")));
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)
				&& !"-1".equals(cityId)) {
			searchOptionList = getQueryParam(starttime, endtime, cityId,
					curPage_splitPage, num_splitPage);
			total = StringUtil.getIntegerValue(edu.getCount(indexType,
					searchOptionList));
			totalmap.put(cityId, String.valueOf(total));
			droopSearchOptionList = getQueryParamDroop(starttime, endtime,
					cityId, maxDp, minDp, curPage_splitPage, num_splitPage);
			droopNum = StringUtil.getIntegerValue(edu.getCount(indexType,
					droopSearchOptionList));
			droopmap.put(cityId, String.valueOf(droopNum));
		} else {
			cityList = CityDAO.getNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++) {
				searchOptionList = getQueryParam(starttime, endtime,
						String.valueOf(cityList.get(i)), curPage_splitPage,
						num_splitPage);
				total = StringUtil.getIntegerValue(edu.getCount(indexType,
						searchOptionList));
				totalmap.put(String.valueOf(cityList.get(i)),
						String.valueOf(total));
			}
			for (int i = 0; i < cityList.size(); i++) {
				droopSearchOptionList = getQueryParamDroop(starttime, endtime,
						String.valueOf(cityList.get(i)), maxDp, minDp,
						curPage_splitPage, num_splitPage);
				droopNum = StringUtil.getIntegerValue(edu.getCount(indexType,
						droopSearchOptionList));
				droopmap.put(String.valueOf(cityList.get(i)),
						String.valueOf(droopNum));
			}
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)
				&& !"-1".equals(cityId)) {
			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("cityId", cityId);
			tmap.put("cityName", cityMap.get(cityId));
			tmap.put("droopNum", StringUtil.getLongValue(droopmap.get(cityId)));
			disDroopNum = StringUtil.getLongValue(totalmap.get(cityId))
					- StringUtil.getLongValue(droopmap.get(cityId));
			tmap.put("disDroopNum", disDroopNum);
			tmap.put("starttime", starttime);
			tmap.put("endtime", endtime);
			list.add(tmap);
		} else {
			cityList = CityDAO.getNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++) {
				Map<String, Object> tmap = new HashMap<String, Object>();
				tmap.put("cityId", cityList.get(i));
				tmap.put("cityName", cityMap.get(cityList.get(i)));
				tmap.put("droopNum",
						StringUtil.getLongValue(droopmap.get(cityList.get(i))));
				disDroopNum = StringUtil.getLongValue(totalmap.get(cityList
						.get(i)))
						- StringUtil
								.getLongValue(droopmap.get(cityList.get(i)));
				tmap.put("disDroopNum", disDroopNum);
				tmap.put("starttime", starttime);
				tmap.put("endtime", endtime);
				list.add(tmap);
			}
		}
		return list;
	}

	/**
	 * 用户网络状态评测详细列表
	 * 
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @return
	 */
	public List<Map<String, Object>> queryByDroopList(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			String operType, int curPage_splitPage, int num_splitPage) {
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		int from = (curPage_splitPage - 1) * num_splitPage;
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		ArrayList<MySearchOption> searchOptionList, searchOptionList1 = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		PrepareSQL psql = new PrepareSQL(
				"select indicators,maximum,minimum from table_userstate_rating where indicators = 'droop' ");
		List<HashMap<String, String>> queryList = DBOperation.getRecords(psql
				.getSQL());
		if (null != queryList && queryList.size() > 0) {
			maxDp = Float.parseFloat((queryList.get(0).get("maximum")));
			minDp = Float.parseFloat((queryList.get(0).get("minimum")));
		}
		if ("1".equals(operType)) {
			searchOptionList = getQueryParamDroop(starttime, endtime, cityId,
					maxDp, minDp, curPage_splitPage, num_splitPage);
			list = edu.simpleSearch(indexType, searchOptionList, from,
					num_splitPage, "upload_time", "desc");
		} else {
			searchOptionList = getQueryParamDisDroop1(starttime, endtime,
					cityId, maxDp, minDp, curPage_splitPage, num_splitPage);
			list = edu.simpleSearch(indexType, searchOptionList, from,
					num_splitPage, "upload_time", "desc");
			searchOptionList1 = getQueryParamDisDroop2(starttime, endtime,
					cityId, maxDp, minDp, curPage_splitPage, num_splitPage);
			list1 = edu.simpleSearch(indexType, searchOptionList1, from,
					num_splitPage, "upload_time", "desc");
			list.addAll(list1);
		}

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
					long upload_time = StringUtil.getLongValue(remap
							.get("upload_time"));
					DateTimeUtil dt = new DateTimeUtil(upload_time * 1000);
					map.put("upload_time", dt.getLongDate());
					long add_time = StringUtil.getLongValue(remap
							.get("add_time"));
					DateTimeUtil dateTimeUtil = new DateTimeUtil(
							add_time * 1000);
					map.put("add_time", dateTimeUtil.getLongDate());
					double rx_power = StringUtil.getDoubleValue(remap
							.get("rx_power"));
					DecimalFormat df = new DecimalFormat("#0.00");
					map.put("droop",
							StringUtil.getStringValue(df.format(rx_power)));// 光衰
				}
				returnList.add(map);
			}
		}
		return returnList;
	}

	public int queryByDroopTotal(String indexName, String indexType,
			String starttime, String endtime, String cityId,
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

	public List<Map<String, Object>> queryByDroopExcel(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			String operType, int curPage_splitPage, int num_splitPage, int total) {
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		ArrayList<MySearchOption> searchOptionList, searchOptionList1 = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		PrepareSQL psql = new PrepareSQL(
				"select indicators,maximum,minimum from table_userstate_rating where indicators = 'droop' ");
		List<HashMap<String, String>> queryList = DBOperation.getRecords(psql
				.getSQL());
		if (null != queryList && queryList.size() > 0) {
			maxDp = Float.parseFloat((queryList.get(0).get("maximum")));
			minDp = Float.parseFloat((queryList.get(0).get("minimum")));
		}
		if ("1".equals(operType)) {
			searchOptionList = getQueryParamDroop(starttime, endtime, cityId,
					maxDp, minDp, curPage_splitPage, num_splitPage);
			list = edu.simpleSearch(indexType, searchOptionList, 0, total,
					"upload_time", "desc");
		} else if ("2".equals(operType)) {
			searchOptionList = getQueryParamDisDroop1(starttime, endtime,
					cityId, maxDp, minDp, curPage_splitPage, num_splitPage);
			list = edu.simpleSearch(indexType, searchOptionList, 0, total,
					"upload_time", "desc");
			searchOptionList1 = getQueryParamDisDroop2(starttime, endtime,
					cityId, maxDp, minDp, curPage_splitPage, num_splitPage);
			list1 = edu.simpleSearch(indexType, searchOptionList1, 0, total,
					"upload_time", "desc");
			list.addAll(list1);
		}
		if (null != list) {
			Map<String, Object> map = null;
			for (Map<String, Object> remap : list) {

				if (null != remap) {
					map = new HashMap<String, Object>();
					String city_id = String.valueOf(remap.get("city_id"));
					map.put("username", remap.get("username"));
					Map<String, String> cityMap = CityDAO
							.getCityIdCityNameMap();
					String city_name = StringUtil.getStringValue(cityMap
							.get(city_id));
					if (false == StringUtil.IsEmpty(city_name)) {
						map.put("city_name", city_name);
					} else {
						map.put("city_name", "");
					}
					map.put("loid", remap.get("loid"));
					map.put("device_serialnumber",
							remap.get("device_serialnumber"));
					long upload_time = StringUtil.getLongValue(remap
							.get("upload_time"));
					DateTimeUtil dt = new DateTimeUtil(upload_time * 1000);
					map.put("upload_time", dt.getLongDate());
					long add_time = StringUtil.getLongValue(remap
							.get("add_time"));
					DateTimeUtil dateTimeUtil = new DateTimeUtil(
							add_time * 1000);
					map.put("add_time", dateTimeUtil.getLongDate());
					double rx_power = StringUtil.getDoubleValue(remap
							.get("rx_power"));
					DecimalFormat df = new DecimalFormat("#0.00");
					map.put("droop",
							StringUtil.getStringValue(df.format(rx_power)));// 光衰
				}
				returnList.add(map);
			}
		}
		return returnList;
	}

	/**
	 * 查询详细信息页面
	 * 
	 * @param query_type
	 * @param queryVal
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public String getDetail(String query_type, String queryVal) {
		PrepareSQL psql = null;
		List<Object> list = null;
		String sub = queryVal.substring(queryVal.length() - 6);
		if ("1".equals(query_type)) {
			psql = new PrepareSQL(
					"select a.user_id from tab_hgwcustomer a where a.username = '"
							+ queryVal + "'");
		} else if ("2".equals(query_type)) {
			psql = new PrepareSQL(
					"select a.device_id from tab_gw_device a where a.device_serialnumber like '%"
							+ queryVal + "' and a.dev_sub_sn = '" + sub + "'");
		}
		list = jt.queryForList(psql.getSQL());
		Map<String, Object> map = null;
		if (null != list && list.size() > 0) {
			map = (Map<String, Object>) list.get(0);
			if (map.size() > 0 && null != map) {
				if ("1".equals(query_type)) {
					String user_id = StringUtil.getStringValue(map
							.get("user_id"));
					// 判断user_id是否为空
					if ("".equals(user_id) || user_id == null) {
						return "0";
					} else {
						return user_id;
					}
				} else {
					String device_id = StringUtil.getStringValue(map
							.get("device_id"));
					// 判断device_id是否为空
					if ("".equals(device_id) || device_id == null) {
						return "0";
					} else {
						return device_id;
					}
				}
			}
		}
		return "0";
	}

	/**
	 * 查询总计路数
	 * 
	 * @param indexName
	 * @param indexType
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryByDroopCount(String indexName, String indexType,
			String starttime, String endtime, String cityId, String operType,
			int curPage_splitPage, int num_splitPage) {
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		PrepareSQL psql = new PrepareSQL(
				"select indicators,maximum,minimum from table_userstate_rating where indicators = 'droop' ");
		List<HashMap<String, String>> queryList = DBOperation.getRecords(psql
				.getSQL());
		if (null != queryList && queryList.size() > 0) {
			maxDp = Float.parseFloat((queryList.get(0).get("maximum")));
			minDp = Float.parseFloat((queryList.get(0).get("minimum")));
		}
		ArrayList<MySearchOption> searchOptionList, searchOptionList1 = null;
		int total = 0, total1, total2 = 0;
		if ("1".equals(operType)) {
			searchOptionList = this.getQueryParamDroop(starttime, endtime,
					cityId, maxDp, minDp, curPage_splitPage, num_splitPage);
			total = StringUtil.getIntegerValue(edu.getCount(indexType,
					searchOptionList));
		} else if ("2".equals(operType)) {
			searchOptionList = this.getQueryParamDisDroop1(starttime, endtime,
					cityId, maxDp, minDp, curPage_splitPage, num_splitPage);
			total1 = StringUtil.getIntegerValue(edu.getCount(indexType,
					searchOptionList));
			searchOptionList1 = this.getQueryParamDisDroop2(starttime, endtime,
					cityId, maxDp, minDp, curPage_splitPage, num_splitPage);
			total2 = StringUtil.getIntegerValue(edu.getCount(indexType,
					searchOptionList1));
			total = total1 + total2;
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
	 * 设置查询条件
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param quertTimeType
	 * @param deviceSerialnumber
	 * @param loid
	 * @return
	 */
	public ArrayList<MySearchOption> getQueryParam(String starttime,
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

	/**
	 * 设置查询条件
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param quertTimeType
	 * @param deviceSerialnumber
	 * @param loid
	 * @return
	 */
	public ArrayList<MySearchOption> getQueryParamDroop(String starttime,
			String endtime, String cityId, float maxDp, float minDp,
			int curPage_splitPage, int num_splitPage) {
		ArrayList<MySearchOption> droopSearchOptionList = new ArrayList<MySearchOption>();
		MySearchOption mySearchOption1 = null;
		MySearchOption mySearchOption2 = null;
		MySearchOption mySearchOption3 = null;
		MySearchOption mySearchOption4 = null;
		MySearchOption mySearchOption5 = null;
		if (!StringUtil.IsEmpty(starttime)) {
			mySearchOption1 = new MySearchOption("upload_time",
					SearchType.range, SearchLogic.must, OperType.gt,
					String.valueOf(new DateTimeUtil(starttime).getLongTime()));
			droopSearchOptionList.add(mySearchOption1);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			mySearchOption2 = new MySearchOption("upload_time",
					SearchType.range, SearchLogic.must, OperType.lt,
					String.valueOf(new DateTimeUtil(endtime).getLongTime()));
			droopSearchOptionList.add(mySearchOption2);
		}
		mySearchOption3 = new MySearchOption("rx_power", SearchType.range,
				SearchLogic.must, OperType.lt, maxDp);
		droopSearchOptionList.add(mySearchOption3);
		mySearchOption4 = new MySearchOption("rx_power", SearchType.range,
				SearchLogic.must, OperType.gt, minDp);
		droopSearchOptionList.add(mySearchOption4);
		if (!StringUtil.IsEmpty(cityId)) {
			mySearchOption5 = new MySearchOption("city_id", SearchType.term,
					SearchLogic.must, cityId);
			droopSearchOptionList.add(mySearchOption5);
		}
		return droopSearchOptionList;

	}

	/**
	 * 设置查询条件
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param quertTimeType
	 * @param deviceSerialnumber
	 * @param loid
	 * @return
	 */
	public ArrayList<MySearchOption> getQueryParamDisDroop1(String starttime,
			String endtime, String cityId, float maxDp, float minDp,
			int curPage_splitPage, int num_splitPage) {
		ArrayList<MySearchOption> disDroopSearchOptionList = new ArrayList<MySearchOption>();
		MySearchOption mySearchOption1 = null;
		MySearchOption mySearchOption2 = null;
		MySearchOption mySearchOption3 = null;
		MySearchOption mySearchOption4 = null;
		if (!StringUtil.IsEmpty(starttime)) {
			mySearchOption1 = new MySearchOption("upload_time",
					SearchType.range, SearchLogic.must, OperType.gt,
					String.valueOf(new DateTimeUtil(starttime).getLongTime()));
			disDroopSearchOptionList.add(mySearchOption1);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			mySearchOption2 = new MySearchOption("upload_time",
					SearchType.range, SearchLogic.must, OperType.lt,
					String.valueOf(new DateTimeUtil(endtime).getLongTime()));
			disDroopSearchOptionList.add(mySearchOption2);
		}
		mySearchOption3 = new MySearchOption("rx_power", SearchType.range,
				SearchLogic.must, OperType.gt, maxDp);
		disDroopSearchOptionList.add(mySearchOption3);
		if (!StringUtil.IsEmpty(cityId)) {
			mySearchOption4 = new MySearchOption("city_id", SearchType.term,
					SearchLogic.must, cityId);
			disDroopSearchOptionList.add(mySearchOption4);
		}
		return disDroopSearchOptionList;

	}

	/**
	 * 设置查询条件
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param quertTimeType
	 * @param deviceSerialnumber
	 * @param loid
	 * @return
	 */
	public ArrayList<MySearchOption> getQueryParamDisDroop2(String starttime,
			String endtime, String cityId, float maxDp, float minDp,
			int curPage_splitPage, int num_splitPage) {
		ArrayList<MySearchOption> disDroopSearchOptionList = new ArrayList<MySearchOption>();
		MySearchOption mySearchOption1 = null;
		MySearchOption mySearchOption2 = null;
		MySearchOption mySearchOption3 = null;
		MySearchOption mySearchOption4 = null;
		if (!StringUtil.IsEmpty(starttime)) {
			mySearchOption1 = new MySearchOption("upload_time",
					SearchType.range, SearchLogic.must, OperType.gt,
					String.valueOf(new DateTimeUtil(starttime).getLongTime()));
			disDroopSearchOptionList.add(mySearchOption1);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			mySearchOption2 = new MySearchOption("upload_time",
					SearchType.range, SearchLogic.must, OperType.lt,
					String.valueOf(new DateTimeUtil(endtime).getLongTime()));
			disDroopSearchOptionList.add(mySearchOption2);
		}
		mySearchOption3 = new MySearchOption("rx_power", SearchType.range,
				SearchLogic.must, OperType.lt, minDp);
		disDroopSearchOptionList.add(mySearchOption3);
		if (!StringUtil.IsEmpty(cityId)) {
			mySearchOption4 = new MySearchOption("city_id", SearchType.term,
					SearchLogic.must, cityId);
			disDroopSearchOptionList.add(mySearchOption4);
		}
		return disDroopSearchOptionList;
	}

	public static Logger getLogger() {
		return logger;
	}

	public ElasticDataUtil getEdu() {
		return edu;
	}

	public void setEdu(ElasticDataUtil edu) {
		this.edu = edu;
	}

}
