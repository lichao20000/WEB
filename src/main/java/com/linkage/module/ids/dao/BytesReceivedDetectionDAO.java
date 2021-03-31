
package com.linkage.module.ids.dao;

import java.text.DecimalFormat;
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
import com.linkage.module.gwms.util.StringUtil;

public class BytesReceivedDetectionDAO
{

	private Logger logger = LoggerFactory.getLogger(BytesReceivedDetectionDAO.class);
	private ElasticDataUtil edu = null;

	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public List<Map<String, Object>> queryLanAndPonData(String indexName,
			String indexType, String starttime, String endtime, String quertTimeType,
			String deviceSerialnumber, String loid, int curPage_splitPage,
			int num_splitPage)
	{
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap().get(indexName);
		int from = 0;
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		int timeType = StringUtil.getIntegerValue(quertTimeType);
		String time = "";
		DecimalFormat    df   = new DecimalFormat("######0.00");   
		switch (timeType)
		{
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
		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				curPage_splitPage, num_splitPage, starttime, endtime, quertTimeType,
				deviceSerialnumber, loid);
		int total = StringUtil.getIntegerValue(edu.getCount("laninfo", searchOptionList));
		List<Map<String, Object>> list = edu.simpleSearch("laninfo", searchOptionList,
				from, total, time, "desc");
		// logger.warn("BytesReceivedDetectionDAO->list.size={}", list.size());
		List<Map<String, Object>> p_list = edu.simpleSearch("poninfo", searchOptionList,
				from, total, time, "desc");
		if (null != list)
		{
			Map<String, Object> map = null;
			long temptime = 0;
			double tempbytes = 0;
			double bytespert = 0;
			List<Map> list1 = new ArrayList<Map>();
			List<Map> list2 = new ArrayList();
			List<Map> list3 = new ArrayList();
			List<Map> list4 = new ArrayList();
			for (int i = 0; i < list.size(); i++)
			{
				Map<String, Object> remap = list.get(i);
				if (remap.get("lan_interface_config_id").toString().equals("1"))
				{
					Map tmap = new HashMap<String, String>();
					tmap.put("bytes_received", remap.get("bytes_received"));
					tmap.put(time, remap.get(time));
					tmap.put("upload_time", remap.get("upload_time"));
					tmap.put("add_time", remap.get("add_time"));
					tmap.put("loid", remap.get("loid"));
					tmap.put("device_serialnumber", remap.get("device_serialnumber"));
					list1.add(tmap);
				}
				if (remap.get("lan_interface_config_id").toString().equals("2"))
				{
					Map tmap = new HashMap<String, String>();
					tmap.put("bytes_received", remap.get("bytes_received"));
					tmap.put(time, remap.get(time));
					tmap.put("upload_time", remap.get("upload_time"));
					tmap.put("add_time", remap.get("add_time"));
					list2.add(tmap);
				}
				if (remap.get("lan_interface_config_id").toString().equals("3"))
				{
					Map tmap = new HashMap<String, String>();
					tmap.put("bytes_received", remap.get("bytes_received"));
					tmap.put(time, remap.get(time));
					tmap.put("upload_time", remap.get("upload_time"));
					tmap.put("add_time", remap.get("add_time"));
					list3.add(tmap);
				}
				if (remap.get("lan_interface_config_id").toString().equals("4"))
				{
					Map tmap = new HashMap<String, String>();
					tmap.put("bytes_received", remap.get("bytes_received"));
					tmap.put(time, remap.get(time));
					tmap.put("upload_time", remap.get("upload_time"));
					tmap.put("add_time", remap.get("add_time"));
					list4.add(tmap);
				}
			}
			// logger.warn("BytesReceivedDetectionDAO->list1.size={}",
			// list1.size());
			for (int i = 0; i < list1.size(); i++)
			{
				Map _map = new HashMap<String, String>();
				if (i + 1 < list1.size())
				{
					// 时间跨度
					temptime = StringUtil.getLongValue(list1.get(i).get(time))
							- StringUtil.getLongValue(list1.get(i + 1).get(time));
					// logger.warn("BytesReceivedDetectionDAO->temptime={}",
					// temptime);
					// 流量
					tempbytes = StringUtil.getLongValue(list1.get(i)
							.get("bytes_received"))
							- StringUtil.getLongValue(list1.get(i + 1).get(
									"bytes_received"));
					// 速率
					if (temptime != 0)
					{
						bytespert = tempbytes /1024/ temptime;
					}
					_map.put("bytes1", df.format(tempbytes/1024/1024));
					_map.put("bytespert1", df.format(bytespert));
					if (i + 1 < list2.size())
					{
						// 时间跨度
						temptime = StringUtil.getLongValue(list2.get(i).get(time))
								- StringUtil.getLongValue(list2.get(i + 1).get(time));
						// 流量
						tempbytes = StringUtil.getLongValue(list2.get(i).get(
								"bytes_received"))
								- StringUtil.getLongValue(list2.get(i + 1).get(
										"bytes_received"));
						// 速率
						if (temptime != 0)
						{
							bytespert = tempbytes/1024 / temptime;
						}
						_map.put("bytes2", df.format(tempbytes/1024/1024));
						_map.put("bytespert2", df.format(bytespert));
					}
					if (i + 1 < list3.size())
					{
						// 时间跨度
						temptime = StringUtil.getLongValue(list3.get(i).get(time))
								- StringUtil.getLongValue(list3.get(i + 1).get(time));
						// 流量
						tempbytes = StringUtil.getLongValue(list3.get(i).get(
								"bytes_received"))
								- StringUtil.getLongValue(list3.get(i + 1).get(
										"bytes_received"));
						// 速率
						if (temptime != 0)
						{
							bytespert = tempbytes/1024 / temptime;
						}
						_map.put("bytes3", df.format(tempbytes/1024/1024));
						_map.put("bytespert3", df.format(bytespert));
					}
					if (i + 1 < list4.size())
					{
						// 时间跨度
						temptime = StringUtil.getLongValue(list4.get(i).get(time))
								- StringUtil.getLongValue(list4.get(i + 1).get(time));
						// 流量
						tempbytes = StringUtil.getLongValue(list4.get(i).get(
								"bytes_received"))
								- StringUtil.getLongValue(list4.get(i + 1).get(
										"bytes_received"));
						// 速率
						if (temptime != 0)
						{
							bytespert = tempbytes/1024 / temptime;
						}
						_map.put("bytes4", df.format(tempbytes/1024/1024));
						_map.put("bytespert4", df.format(bytespert));
					}
					_map.put("device_serialnumber",
							String.valueOf(list1.get(i).get("device_serialnumber")));
					_map.put("loid", String.valueOf(list1.get(i).get("loid")));
					long upload_time = StringUtil.getLongValue(list1.get(i).get(
							"upload_time"));
					DateTimeUtil dt = new DateTimeUtil(upload_time * 1000);
					_map.put("upload_time", dt.getLongDate());
					long add_time = StringUtil.getLongValue(list1.get(i).get("add_time"));
					dt = new DateTimeUtil(add_time * 1000);
					_map.put("add_time", dt.getLongDate());
					if (i + 1 < p_list.size())
					{
						// 流量
						long ponbytes = StringUtil.getLongValue(p_list.get(i).get(
								"bytes_received"))
								- StringUtil.getLongValue(p_list.get(i + 1).get(
										"bytes_received"));
						// //时间跨度
						// long pontime =
						// StringUtil.getLongValue(p_list.get(i).get("time"))-StringUtil.getLongValue(p_list.get(i+1).get("time"));
						// //速率
						// long ponpert = ponbytes/pontime;
						_map.put("ponbytes", df.format(ponbytes/1024/1024));
					}
					// map.put("ponpert", ponpert);
					returnList.add(_map);
				}
			}
		}
		return returnList;
	}

	// @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	// public List queryLanAndPonDataExcel(String indexName, String indexType,
	// String starttime, String endtime, String quertTimeType,
	// String deviceSerialnumber, String loid, int curPage_splitPage,
	// int num_splitPage) {
	// edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
	// .get(indexName);
	// int from = 0;
	// List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	// int timeType = StringUtil.getIntegerValue(quertTimeType);
	// String time = "";
	// switch (timeType) {
	// case 0:
	// time = "";
	// break;
	// case 1:
	// time = "upload_time";
	// break;
	// case 2:
	// time = "add_time";
	// break;
	// default:
	// break;
	// }
	// ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
	// curPage_splitPage, num_splitPage, starttime, endtime,
	// quertTimeType, deviceSerialnumber, loid);
	// int total = StringUtil.getIntegerValue(edu.getCount("laninfo",
	// searchOptionList));
	// logger.warn("BytesReceivedDetectionDAO->total={}", total);
	// List<Map<String, Object>> list = edu.simpleSearch("laninfo",
	// searchOptionList, from, num_splitPage, time, "desc");
	// logger.warn("BytesReceivedDetectionDAO->list.size={}", list.size());
	// List<Map<String, Object>> p_list = edu.simpleSearch("poninfo",
	// searchOptionList, from, num_splitPage, time, "desc");
	// if (null != list) {
	// Map<String, Object> map = null;
	// long temptime = 0;
	// long tempbytes = 0;
	// long bytespert;
	// List<Map> list1 = new ArrayList<Map>();
	// List<Map> list2 = new ArrayList();
	// List<Map> list3 = new ArrayList();
	// List<Map> list4 = new ArrayList();
	// for (int i = 0; i < list.size(); i++) {
	// Map<String, Object> remap = list.get(i);
	// if (remap.get("lan_interface_config_id").toString().equals("1")) {
	// Map tmap = new HashMap<String, String>();
	// tmap.put("bytes_received", remap.get("bytes_received"));
	// tmap.put(time, remap.get(time));
	// tmap.put("upload_time", remap.get("upload_time"));
	// tmap.put("add_time", remap.get("add_time"));
	// tmap.put("loid", remap.get("loid"));
	// tmap.put("device_serialnumber",
	// remap.get("device_serialnumber"));
	// list1.add(tmap);
	// }
	// if (remap.get("lan_interface_config_id").toString().equals("2")) {
	// Map tmap = new HashMap<String, String>();
	// tmap.put("bytes_received", remap.get("bytes_received"));
	// tmap.put(time, remap.get(time));
	// tmap.put("upload_time", remap.get("upload_time"));
	// tmap.put("add_time", remap.get("add_time"));
	// list2.add(tmap);
	// }
	// if (remap.get("lan_interface_config_id").toString().equals("3")) {
	// Map tmap = new HashMap<String, String>();
	// tmap.put("bytes_received", remap.get("bytes_received"));
	// tmap.put(time, remap.get(time));
	// tmap.put("upload_time", remap.get("upload_time"));
	// tmap.put("add_time", remap.get("add_time"));
	// list3.add(tmap);
	// }
	// if (remap.get("lan_interface_config_id").toString().equals("4")) {
	// Map tmap = new HashMap<String, String>();
	// tmap.put("bytes_received", remap.get("bytes_received"));
	// tmap.put(time, remap.get(time));
	// tmap.put("upload_time", remap.get("upload_time"));
	// tmap.put("add_time", remap.get("add_time"));
	// list4.add(tmap);
	// }
	// }
	// logger.warn("BytesReceivedDetectionDAO->list1.size={}",
	// list1.size());
	// for (int i = 0; i < list1.size(); i++) {
	// Map _map = new HashMap<String, String>();
	// if (i + 1 < list1.size()) {
	// // 时间跨度
	// temptime = StringUtil.getLongValue(list1.get(i).get(time))
	// - StringUtil.getLongValue(list1.get(i + 1)
	// .get(time));
	// logger.warn("BytesReceivedDetectionDAO->temptime={}",
	// temptime);
	// // 流量
	// tempbytes = StringUtil.getLongValue(list1.get(i).get(
	// "bytes_received"))
	// - StringUtil.getLongValue(list1.get(i + 1).get(
	// "bytes_received"));
	// // 速率
	// bytespert = tempbytes / temptime;
	// _map.put("bytes1", tempbytes);
	// _map.put("bytespert1", bytespert);
	//
	// // 时间跨度
	// temptime = StringUtil.getLongValue(list2.get(i).get(time))
	// - StringUtil.getLongValue(list2.get(i + 1)
	// .get(time));
	// // 流量
	// tempbytes = StringUtil.getLongValue(list2.get(i).get(
	// "bytes_received"))
	// - StringUtil.getLongValue(list2.get(i + 1).get(
	// "bytes_received"));
	// // 速率
	// bytespert = tempbytes / temptime;
	// _map.put("bytes2", tempbytes);
	// _map.put("bytespert2", bytespert);
	//
	// if (i + 1 < list3.size()) {
	// // 时间跨度
	// temptime = StringUtil.getLongValue(list3.get(i).get(
	// time))
	// - StringUtil.getLongValue(list3.get(i + 1).get(
	// time));
	// // 流量
	// tempbytes = StringUtil.getLongValue(list3.get(i).get(
	// "bytes_received"))
	// - StringUtil.getLongValue(list3.get(i + 1).get(
	// "bytes_received"));
	// // 速率
	// bytespert = tempbytes / temptime;
	// _map.put("bytes3", tempbytes);
	// _map.put("bytespert3", bytespert);
	//
	// // 时间跨度
	// temptime = StringUtil.getLongValue(list4.get(i).get(
	// time))
	// - StringUtil.getLongValue(list4.get(i + 1).get(
	// time));
	// // 流量
	// tempbytes = StringUtil.getLongValue(list4.get(i).get(
	// "bytes_received"))
	// - StringUtil.getLongValue(list4.get(i + 1).get(
	// "bytes_received"));
	// // 速率
	// bytespert = tempbytes / temptime;
	// _map.put("bytes4", tempbytes);
	// _map.put("bytespert4", bytespert);
	// }
	// _map.put(
	// "device_serialnumber",
	// String.valueOf(list1.get(i).get(
	// "device_serialnumber")));
	// _map.put("loid", String.valueOf(list1.get(i).get("loid")));
	// long upload_time = StringUtil.getLongValue(list1.get(i)
	// .get("upload_time"));
	// DateTimeUtil dt = new DateTimeUtil(upload_time * 1000);
	// _map.put("upload_time", dt.getLongDate());
	//
	// long add_time = StringUtil.getLongValue(list1.get(i).get(
	// "add_time"));
	// dt = new DateTimeUtil(add_time * 1000);
	// _map.put("add_time", dt.getLongDate());
	//
	// // 流量
	// long ponbytes = StringUtil.getLongValue(p_list.get(i).get(
	// "bytes_received"))
	// - StringUtil.getLongValue(p_list.get(i + 1).get(
	// "bytes_received"));
	// // //时间跨度
	// // long pontime =
	// //
	// StringUtil.getLongValue(p_list.get(i).get("time"))-StringUtil.getLongValue(p_list.get(i+1).get("time"));
	// // //速率
	// // long ponpert = ponbytes/pontime;
	//
	// _map.put("ponbytes", ponbytes);
	// // map.put("ponpert", ponpert);
	// returnList.add(_map);
	// }
	// }
	//
	// }
	//
	// return returnList;
	// }
	//
	// @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	// public int queryLanAndPonDataCount(String indexName, String indexType,
	// String starttime, String endtime, String quertTimeType,
	// String deviceSerialnumber, String loid, int curPage_splitPage,
	// int num_splitPage) {
	// edu = ElasticsearchInitBIO.getInstance().getElasticDataMap()
	// .get(indexName);
	// int from = 0;
	// List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	// int timeType = StringUtil.getIntegerValue(quertTimeType);
	// String time = "";
	// switch (timeType) {
	// case 0:
	// time = "";
	// break;
	// case 1:
	// time = "upload_time";
	// break;
	// case 2:
	// time = "add_time";
	// break;
	// default:
	// break;
	// }
	// ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
	// curPage_splitPage, num_splitPage, starttime, endtime,
	// quertTimeType, deviceSerialnumber, loid);
	// int total = StringUtil.getIntegerValue(edu.getCount("laninfo",
	// searchOptionList));
	// logger.warn("BytesReceivedDetectionDAO->total={}", total);
	// List<Map<String, Object>> list = edu.simpleSearch("laninfo",
	// searchOptionList, 0, total, time, "desc");
	// logger.warn("BytesReceivedDetectionDAO->list.size={}", list.size());
	// if (null != list) {
	// Map<String, Object> map = null;
	// long temptime = 0;
	// long tempbytes = 0;
	// long bytespert;
	// List<Map> list1 = new ArrayList<Map>();
	// List<Map> list2 = new ArrayList();
	// List<Map> list3 = new ArrayList();
	// List<Map> list4 = new ArrayList();
	// for (int i = 0; i < list.size(); i++) {
	// Map<String, Object> remap = list.get(i);
	// if (remap.get("lan_interface_config_id").toString().equals("1")) {
	// Map tmap = new HashMap<String, String>();
	// tmap.put("device_serialnumber",
	// remap.get("device_serialnumber"));
	// list1.add(tmap);
	// }
	// }
	// total = 0;
	// for (int i = 0; i < list1.size(); i++) {
	// Map _map = new HashMap<String, String>();
	// if (i + 1 < list1.size()) {
	// total++;
	// }
	// }
	// }
	// int maxPage = 1;
	// if (total % num_splitPage == 0) {
	// maxPage = total / num_splitPage;
	// } else {
	// maxPage = total / num_splitPage + 1;
	// }
	// return maxPage;
	// }
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
			int num_splitPage, String startTime, String endTime, String quertTimeType,
			String deviceSerialnumber, String loid)
	{
		ArrayList<MySearchOption> searchOptionList = new ArrayList<MySearchOption>();
		MySearchOption mySearchOption = new MySearchOption("device_serialnumber",
				SearchType.term, SearchLogic.must, deviceSerialnumber);
		MySearchOption mySearchOption1 = new MySearchOption("loid", SearchType.term,
				SearchLogic.must, loid);
		int timeType = StringUtil.getIntegerValue(quertTimeType);
		String time = "";
		switch (timeType)
		{
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
		if (time == "")
		{
		}
		else
		{
			MySearchOption mySearchOption2 = null;
			MySearchOption mySearchOption3 = null;
			if (!StringUtil.IsEmpty(startTime))
			{
				mySearchOption2 = new MySearchOption(time, SearchType.range,
						SearchLogic.must, OperType.gt, String.valueOf(new DateTimeUtil(
								startTime).getLongTime()));
			}
			if (!StringUtil.IsEmpty(endTime))
			{
				mySearchOption3 = new MySearchOption(time, SearchType.range,
						SearchLogic.must, OperType.lt, String.valueOf(new DateTimeUtil(
								endTime).getLongTime()));
			}
			searchOptionList.add(mySearchOption2);
			searchOptionList.add(mySearchOption3);
		}
		return searchOptionList;
	}
}
