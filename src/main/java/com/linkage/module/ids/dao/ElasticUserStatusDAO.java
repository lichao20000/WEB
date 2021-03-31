package com.linkage.module.ids.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.elasticsearch.bio.ElasticsearchInitBIO;
import com.linkage.commons.elasticsearch.util.ElasticDataUtil;
import com.linkage.commons.elasticsearch.util.MySearchOption;
import com.linkage.commons.elasticsearch.util.MySearchOption.OperType;
import com.linkage.commons.elasticsearch.util.MySearchOption.SearchLogic;
import com.linkage.commons.elasticsearch.util.MySearchOption.SearchType;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class ElasticUserStatusDAO  extends SuperDAO{
	 /**
	 * 用户网络状态评测查询 
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
	private static Logger logger = LoggerFactory
			.getLogger(ElasticUserStatusDAO.class);
	private ElasticDataUtil edu = null;

	public List<Map<String, Object>> queryPonstatusList(String indexName,
			String indexType, int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String quertTimeType,
			String deviceSerialnumber, String loid) {

		logger.debug("queryPonstatusList-->");

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
		String indicators = null; //指标
		String maxDp = null,maxTe = null,maxCt = null,maxVe = null; //光衰、温度、电流、电压最大值
		String minDp = null,minTe = null,minCt = null,minVe = null; //光衰、温度、电流、电压最小值
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sb = new StringBuffer();
		sb.append("select indicators,maximum,minimum from table_userstate_rating where 1=1 ");
		psql.setSQL(sb.toString());
		List<HashMap<String,String>> queryList = DBOperation.getRecords(psql.getSQL());
		if(null!= queryList && queryList.size()>0){
			for (int i = 0; i < queryList.size(); i++){
				indicators = (String) queryList.get(i).get("indicators");
				if("droop".equals(indicators)){
					maxDp = (String) queryList.get(i).get("maximum");
					minDp= (String) queryList.get(i).get("minimum");
				}else if("temperature".equals(indicators)){
					maxTe = (String) queryList.get(i).get("maximum");
					minTe = (String) queryList.get(i).get("minimum");
				}else if("current".equals(indicators)){
					maxCt = (String) queryList.get(i).get("maximum");
					minCt= (String) queryList.get(i).get("minimum");
				}else if("voltage".equals(indicators)){
					maxVe = (String) queryList.get(i).get("maximum");
					minVe= (String) queryList.get(i).get("minimum");
				}
			}
			
		}
		//查询页面展示
		if (null != list) {
			Map<String, Object> map = null;
			for (Map<String, Object> remap : list) {
				if (null != remap) {
					map = new HashMap<String, Object>();
					map.put("loid", remap.get("loid")); //LOID
					map.put("oui", remap.get("oui"));  //OUI
					map.put("device_serialnumber", //设备序列号
							remap.get("device_serialnumber"));
					String status = (String) remap.get("status"); //端口状态
					map.put("status", status);
					double droop = StringUtil.getDoubleValue(remap.get("rx_power")) -StringUtil.getDoubleValue(remap.get("tx_power"));
					DecimalFormat df = new DecimalFormat("#0.00");
					
					map.put("droop", StringUtil.getDoubleValue(df.format(droop))); //光衰
					double temperature =   StringUtil.getDoubleValue(remap.get("temperature"));
					map.put("temperature", temperature);
					double vottage = StringUtil.getDoubleValue(remap.get("vottage").toString()); //温度
					map.put("vottage", vottage);
					double bais_current = StringUtil.getDoubleValue( remap.get("bais_current")); //电流
					map.put("bais_current", bais_current); //电压
					long upload_time = StringUtil.getLongValue(remap
							.get("upload_time")); 
					DateTimeUtil dt = new DateTimeUtil(upload_time * 1000);
					map.put("upload_time", dt.getLongDate()); //上传时间
					long add_time = StringUtil.getLongValue(remap
							.get("add_time")); 
					DateTimeUtil dateTimeUtil = new DateTimeUtil(
							add_time * 1000);
					map.put("add_time", dateTimeUtil.getLongDate()); //入库时间
					//用户周期评各指标进行比较
					int i = 0;
					if(droop >= StringUtil.getLongValue(minDp) && droop <= StringUtil.getLongValue(maxDp)){
						i++;
					}
					if(temperature >= StringUtil.getLongValue(minTe) && temperature <= StringUtil.getLongValue(maxTe)){
						i++;
					}
					if(bais_current >= StringUtil.getLongValue(minCt) && bais_current <= StringUtil.getLongValue(maxCt)){
						i++;
					}
					if(vottage >= StringUtil.getLongValue(minVe) && vottage <= StringUtil.getLongValue(maxVe)){
						i++;
					}
					if(status.equals("Up")){
						i++;
					}
					//5项值都在正常值内评级为A，4项值在正常范围内评级为B，3项值在正常范围及以下评级为C
					if(i == 5){
						map.put("userLevel","A");
					}else if(i == 4){
						map.put("userLevel","B");
					}else{
						map.put("userLevel","C");
					}
					
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
	public int queryUserStatusCount(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime,
			String endTime, String quertTimeType, String deviceSerialnumber,
			String loid) {
		logger.debug("queryUserStatusCount-->");

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

}
