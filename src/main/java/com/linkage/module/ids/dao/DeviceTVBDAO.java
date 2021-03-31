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

public class DeviceTVBDAO extends SuperDAO {

	private Logger logger = LoggerFactory.getLogger(DeviceTVBDAO.class);
	private ElasticDataUtil edu = null;

	/**
	 * 获取数据
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param indexName
	 * @param indexType
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @param temperature
	 * @param bais_current
	 * @param vottage
	 * @return
	 */
	public Map queryByTVB(int curPage_splitPage, int num_splitPage,
			String indexName, String indexType, String starttime,
			String endtime, String cityId, String temperature,
			String bais_current, String vottage) {
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		ArrayList<MySearchOption> searchOptionList = null;
		List cityList = null;
		Map<String, String> map = new HashMap<String, String>();
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)
				&& !"-1".equals(cityId)) {
			searchOptionList = getQueryParam(starttime, endtime, cityId,
					temperature, bais_current, vottage, curPage_splitPage,
					num_splitPage);
			long total = StringUtil.getIntegerValue(edu.getCount(indexType,
					searchOptionList));
			map.put(cityId, String.valueOf(total));
		} else {
			cityList = CityDAO.getNextCityIdsByCityPid(cityId);
			for (int i = 0; i < cityList.size(); i++) {
				searchOptionList = getQueryParam(starttime, endtime,
						String.valueOf(cityList.get(i)), temperature,
						bais_current, vottage, curPage_splitPage, num_splitPage);
				long total = StringUtil.getIntegerValue(edu.getCount(indexType,
						searchOptionList));
				map.put(String.valueOf(cityList.get(i)), String.valueOf(total));
			}
		}
		// logger.warn("DeviceTVBDAO=>queryByTVB.map={}",map);
		return map;
	}

	/**
	 * 终端详细
	 * @param indexName
	 * @param indexType
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @param temperature
	 * @param bais_current
	 * @param vottage
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map<String, Object>> queryByTVBList(String indexName,
			String indexType, String starttime, String endtime, String cityId,
			String temperature, String bais_current, String vottage,
			int curPage_splitPage, int num_splitPage) {
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		//分页（开始）
		int from = (curPage_splitPage - 1) * num_splitPage;
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		ArrayList<MySearchOption> searchOptionList = getQueryParam(starttime,
				endtime, cityId, temperature, bais_current, vottage,
				curPage_splitPage, num_splitPage);
		List<Map<String, Object>> list = edu.simpleSearch(indexType,
				searchOptionList, from, num_splitPage, "upload_time", "desc");
		if (null != list) {
			Map<String, Object> map = null;
			for (Map<String, Object> remap : list) {

				if (null != remap) {
					logger.warn("ElasticDeviceMonitorDAO:remap", remap);
					map = new HashMap<String, Object>();
					map.put("loid", remap.get("loid"));
					map.put("oui", remap.get("oui"));
					map.put("device_serialnumber",
							remap.get("device_serialnumber"));
					map.put("status", remap.get("status"));
					map.put("tx_power", remap.get("tx_power"));
					map.put("rx_power", remap.get("rx_power"));
					map.put("temperature", remap.get("temperature"));
					map.put("vottage", remap.get("vottage"));
					map.put("bais_current", remap.get("bais_current"));
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

	public List<Map> queryByTVBExcel(String indexName, String indexType,
			String starttime, String endtime, String cityId,
			String temperature, String bais_current, String vottage,
			int curPage_splitPage, int num_splitPage, int total) {
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		List<Map> returnList = new ArrayList<Map>();
		ArrayList<MySearchOption> searchOptionList = getQueryParam(starttime,
				endtime, cityId, temperature, bais_current, vottage,
				curPage_splitPage, num_splitPage);
		List<Map<String, Object>> list = edu.simpleSearch(indexType,
				searchOptionList, 0, total, "upload_time", "desc");
		if (null != list) {
			Map<String, Object> map = null;
			for (Map<String, Object> remap : list) {

				if (null != remap) {
					map = new HashMap<String, Object>();
					map.put("loid", remap.get("loid"));
					map.put("oui", remap.get("oui"));
					map.put("device_serialnumber",
							remap.get("device_serialnumber"));
					map.put("status", remap.get("status"));
					map.put("tx_power", remap.get("tx_power"));
					map.put("rx_power", remap.get("rx_power"));
					map.put("temperature", remap.get("temperature"));
					map.put("vottage", remap.get("vottage"));
					map.put("bais_current", remap.get("bais_current"));
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
	 * 查询总记录数
	 * @param indexName
	 * @param indexType
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @param temperature
	 * @param bais_current
	 * @param vottage
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryByTVBTotal(String indexName, String indexType,
			String starttime, String endtime, String cityId,
			String temperature, String bais_current, String vottage,
			int curPage_splitPage, int num_splitPage) {
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		ArrayList<MySearchOption> searchOptionList = getQueryParam(starttime,
				endtime, cityId, temperature, bais_current, vottage,
				curPage_splitPage, num_splitPage);
		return StringUtil.getIntegerValue(edu.getCount(indexType,
				searchOptionList));
	}

	

	/**
	 * 查询总记录数（分页）
	 * 
	 * @param indexName
	 * @param indexType
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @param temperature
	 * @param bais_current
	 * @param vottage
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryByTVBCount(String indexName, String indexType,
			String starttime, String endtime, String cityId,
			String temperature, String bais_current, String vottage,
			int curPage_splitPage, int num_splitPage) {
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);

		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				starttime, endtime, cityId, temperature, bais_current, vottage,
				curPage_splitPage, num_splitPage);
		int total = StringUtil.getIntegerValue(edu.getCount(indexType,
				searchOptionList));
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
			String endtime, String cityId, String temperature,
			String bais_current, String vottage, int curPage_splitPage,
			int num_splitPage) {

		ArrayList<MySearchOption> searchOptionList = new ArrayList<MySearchOption>();
		MySearchOption mySearchOption = null;
		MySearchOption mySearchOption1 = null;
		MySearchOption mySearchOption2 = null;
		MySearchOption mySearchOption3 = null;
		MySearchOption mySearchOption4 = null;
		MySearchOption mySearchOption5 = null;
		if (!StringUtil.IsEmpty(starttime)) {
			mySearchOption = new MySearchOption("upload_time",
					SearchType.range, SearchLogic.must, OperType.gt,
					String.valueOf(new DateTimeUtil(starttime).getLongTime()));
			searchOptionList.add(mySearchOption);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			mySearchOption1 = new MySearchOption("upload_time",
					SearchType.range, SearchLogic.must, OperType.lt,
					String.valueOf(new DateTimeUtil(endtime).getLongTime()));
			searchOptionList.add(mySearchOption1);
		}
		if (!StringUtil.IsEmpty(temperature)) {
			mySearchOption2 = new MySearchOption("temperature",
					SearchType.range, SearchLogic.must, OperType.gt,
					temperature);
			searchOptionList.add(mySearchOption2);
		}
		if (!StringUtil.IsEmpty(bais_current)) {
			mySearchOption3 = new MySearchOption("bais_current",
					SearchType.range, SearchLogic.must, OperType.gt,
					bais_current);
			searchOptionList.add(mySearchOption3);
		}
		if (!StringUtil.IsEmpty(vottage)) {
			mySearchOption4 = new MySearchOption("vottage", SearchType.range,
					SearchLogic.must, OperType.gt, vottage);
			searchOptionList.add(mySearchOption4);
		}
		if (!StringUtil.IsEmpty(cityId)) {
			mySearchOption5 = new MySearchOption("city_id", SearchType.term,
					SearchLogic.must, cityId);
			searchOptionList.add(mySearchOption5);
		}
		return searchOptionList;

	}

}
