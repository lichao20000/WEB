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
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;

public class ElasticDeviceMonitorDAO {
	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ElasticDeviceMonitorDAO.class);
	private ElasticDataUtil edu = null;

	public List<Map<String, Object>> queryPonstatusList(String indexName,
			String indexType, int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String quertTimeType,
			String deviceSerialnumber, String loid) {

		logger.debug("queryPonstatusList1-->");

		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);

		int from = (curPage_splitPage - 1) * num_splitPage;

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		int timeType = StringUtil.getIntegerValue(quertTimeType);
		String time = "";
		switch (timeType) {
		case 0:
			time = "";
			break;
		case 1:
			time = "upload_time";
			break;
		case 2:
			time = "add_time";
			break;
		default:
			break;
		}
		List<Map<String, Object>> list = edu.simpleSearch(indexType,
				searchOptionList, from, num_splitPage, time, "desc");

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

					if (Global.JXDX.equals(Global.instAreaShortName)) {
						map.put("temperature", dateFormate(StringUtil
								.getDoubleValue(remap.get("temperature"))));
						map.put("vottage",
								dateFormate(StringUtil.getDoubleValue(remap
										.get("vottage")) * 100 / 1000 / 1000));
						map.put("bais_current",
								dateFormate(StringUtil.getDoubleValue(remap
										.get("bais_current")) * 2 / 1000));
					} else {
						map.put("temperature", remap.get("temperature"));
						map.put("vottage", remap.get("vottage"));
						map.put("bais_current", remap.get("bais_current"));
					}
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
	public int queryPonstatusCount(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {
		logger.debug("queryPonstatusCount1-->");

		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);

		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);

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
	public List<Map<String, Object>> queryNetparamList(String indexName,
			String indexType, int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String quertTimeType,
			String deviceSerialnumber, String loid) {

		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);

		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);

		int from = (curPage_splitPage - 1) * num_splitPage;

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		int timeType = StringUtil.getIntegerValue(quertTimeType);
		String time = "";
		switch (timeType) {
		case 0:
			time = "";
			break;
		case 1:
			time = "upload_time";
			break;
		case 2:
			time = "add_time";
			break;
		default:
			break;
		}
		List<Map<String, Object>> list = edu.simpleSearch(indexType,
				searchOptionList, from, num_splitPage, time, "desc");

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
					if ("null".equals(StringUtil.getStringValue(remap, "reason", "")))
					{
						map.put("reason", "");
					}
					else
					{
						map.put("reason", remap.get("reason"));
					}
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
	public int queryNetparamCount(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {

		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);

		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);

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
	public List<Map<String, Object>> queryVoicestatusList(String indexName,
			String indexType, int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String quertTimeType,
			String deviceSerialnumber, String loid) {

		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);

		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);

		int from = (curPage_splitPage - 1) * num_splitPage;

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		int timeType = StringUtil.getIntegerValue(quertTimeType);
		String time = "";
		switch (timeType) {
		case 0:
			time = "";
			break;
		case 1:
			time = "upload_time";
			break;
		case 2:
			time = "add_time";
			break;
		default:
			break;
		}
		List<Map<String, Object>> list = edu.simpleSearch(indexType,
				searchOptionList, from, num_splitPage, time, "desc");

		if (null != list) {
			Map<String, Object> map = null;
			for (Map<String, Object> remap : list) {

				if (null != remap) {
					map = new HashMap<String, Object>();
					map.put("loid", remap.get("loid"));
					map.put("oui", remap.get("oui"));
					map.put("device_serialnumber",
							remap.get("device_serialnumber"));
					map.put("voip_id", remap.get("voip_id"));
					map.put("voip_prof_id", remap.get("voip_prof_id"));
					map.put("line_id", remap.get("line_id"));
					map.put("status", remap.get("status"));
					String enabled = "";
					if ("Enabled".equals(remap.get("enabled"))) {
						enabled = "启用";
					} else {
						enabled = "未启用";
					}
					map.put("enabled", enabled);
					String reason = "";
					int reType = StringUtil
							.getIntegerValue(remap.get("reason"));
					switch (reType) {
					case 0:
						reason = "成功";
						break;
					case 1:
						reason = "IAD模块错误";
						break;
					case 2:
						reason = "访问路由不通";
						break;
					case 3:
						reason = "访问服务器无响应";
						break;
					case 4:
						reason = "帐号、密码错误";
						break;
					case 5:
						reason = "未知错误";
						break;
					default:
						break;
					}
					map.put("reason", reason);
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
	public int queryVoicestatusCount(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {

		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);

		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);

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

	public List<Map<String, Object>> queryLANList(String indexName,
			String indexType, int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String quertTimeType,
			String deviceSerialnumber, String loid) {
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
		int from = (curPage_splitPage - 1) * num_splitPage;

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		int timeType = StringUtil.getIntegerValue(quertTimeType);
		String time = "";
		switch (timeType) {
		case 0:
			time = "";
			break;
		case 1:
			time = "upload_time";
			break;
		case 2:
			time = "add_time";
			break;
		default:
			break;
		}
		List<Map<String, Object>> list = edu.simpleSearch(indexType,
				searchOptionList, from, num_splitPage, time, "desc");
		if (null != list) {
			Map<String, Object> map = null;
			for (Map<String, Object> remap : list) {

				if (null != remap) {
					map = new HashMap<String, Object>();
					map.put("loid", remap.get("loid"));
					map.put("lan_interface_config_id",
							remap.get("lan_interface_config_id"));
					map.put("device_serialnumber",
							remap.get("device_serialnumber"));
					map.put("status", remap.get("status"));
					map.put("packets_sent", remap.get("packets_sent"));
					map.put("packets_received", remap.get("packets_received"));
					map.put("bytes_sent", remap.get("bytes_sent"));
					map.put("bytes_received", remap.get("bytes_received"));
					String reason = "";
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

	public int queryLANCount(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {

		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);

		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);

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

	public List<Map<String, Object>> queryPONList(String indexName,
			String indexType, int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String quertTimeType,
			String deviceSerialnumber, String loid) {
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);
		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
		int from = (curPage_splitPage - 1) * num_splitPage;

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		int timeType = StringUtil.getIntegerValue(quertTimeType);
		String time = "";
		switch (timeType) {
		case 0:
			time = "";
			break;
		case 1:
			time = "upload_time";
			break;
		case 2:
			time = "add_time";
			break;
		default:
			break;
		}
		List<Map<String, Object>> list = edu.simpleSearch(indexType,
				searchOptionList, from, num_splitPage, time, "desc");
		if (null != list) {
			Map<String, Object> map = null;
			for (Map<String, Object> remap : list) {

				if (null != remap) {
					map = new HashMap<String, Object>();
					map.put("loid", remap.get("loid"));
					map.put("device_serialnumber",
							remap.get("device_serialnumber"));
					// map.put("status", remap.get("status"));
					map.put("packets_sent", remap.get("packets_sent"));
					map.put("packets_received", remap.get("packets_received"));
					map.put("bytes_sent", remap.get("bytes_sent"));
					map.put("bytes_received", remap.get("bytes_received"));
					
					//add start by xuzhicheng 2015-06-16 JXDX-ITMS-REQ-20150601-WUWF-001(PON口信息数据采集追加需求)
					map.put("drop_packets", remap.get("drop_packets"));
					map.put("fecerror", remap.get("fecerror"));
					map.put("hecerror", remap.get("hecerror"));
					//add end by xuzhicheng
					
					String reason = "";
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

	public int queryPONCount(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {

		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
				.get(indexName);

		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);

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
	public ArrayList<MySearchOption> getQueryParam(int curPage_splitPage,
			int num_splitPage, String startTime, String endTime,
			String quertTimeType, String deviceSerialnumber, String loid) {

		ArrayList<MySearchOption> searchOptionList = new ArrayList<MySearchOption>();

		MySearchOption mySearchOption = new MySearchOption(
				"device_serialnumber", SearchType.term, SearchLogic.must,
				deviceSerialnumber);
		MySearchOption mySearchOption1 = new MySearchOption("loid",
				SearchType.term, SearchLogic.must, loid);

		int timeType = StringUtil.getIntegerValue(quertTimeType);
		String time = "";
		switch (timeType) {
		case 0:
			time = "";
			break;
		case 1:
			time = "upload_time";
			break;
		case 2:
			time = "add_time";
			break;
		default:
			break;
		}
		searchOptionList.add(mySearchOption);
		searchOptionList.add(mySearchOption1);
		if (time == "") {

		} else {

			MySearchOption mySearchOption2 = null;
			MySearchOption mySearchOption3 = null;

			if (!StringUtil.IsEmpty(startTime)) {
				mySearchOption2 = new MySearchOption(time, SearchType.range,
						SearchLogic.must, OperType.gt,
						String.valueOf(new DateTimeUtil(startTime)
								.getLongTime()));
			}
			if (!StringUtil.IsEmpty(endTime)) {
				mySearchOption3 = new MySearchOption(time, SearchType.range,
						SearchLogic.must, OperType.lt,
						String.valueOf(new DateTimeUtil(endTime).getLongTime()));
			}
			searchOptionList.add(mySearchOption2);
			searchOptionList.add(mySearchOption3);
		}
		return searchOptionList;
	}

	/**
	 * 保留小数点后两位方法
	 * 
	 * @param str
	 * @return STR
	 */
	public String dateFormate(double str_double) {
		String str = StringUtil.getStringValue(str_double);
		if ("".equals(str) || str == null) {
			return null;
		} else {
			if (str.indexOf(".") != -1) {
				// 获取小数点的位置
				String dianAfter = str.substring(0, str.indexOf(".") + 1);
				String afterData = str.replace(dianAfter, "");
				// 获取小数点后面的数字 是否有两位 不足两位补足两位
				if (afterData.length() < 2) {
					afterData = afterData + "0";
				}
				str = str.substring(0, str.indexOf(".")) + "."
						+ afterData.substring(0, 2);
			}
			return str;
		}
	}
}
