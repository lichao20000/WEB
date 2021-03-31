package com.linkage.module.itms.report.act;

import action.splitpage.splitPageAction;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.itms.report.bio.QueryMqBIO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQ监控
 * @author wangyan10
 * @since 2017-1-3 下午3:36:15
 */
public class QueryMqACT extends splitPageAction implements SessionAware {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(QueryMqACT.class);
	// 局点名称
		private String instAreaName;

	/**
	 * mq列表
	 */
	private List<HashMap<String, String>> mqList = null;
	/**
	 * mq折线图主题名列表
	 */
	private List<HashMap<String, String>> topicNameList = null;
	/**
	 * mqId
	 */
	private String mqId;
	/**
	 * 开始时间
	 */
	private String starttime;
	/**
	 * 结束时间
	 */
	private String endtime;
	/**
	 * 采集时间
	 */
	private String gathertime;
	/**
	 * mq主题名
	 */
	private String topicName;
	private String ajax = "";
	@SuppressWarnings("rawtypes")
	private List<Map> queryMqList = null;

	@SuppressWarnings("rawtypes")
	private List<Map> mqDetailList = null;
	@SuppressWarnings("rawtypes")
	private Map session;
	private QueryMqBIO bio;
	/**
	 * 统计数
	 */
	private int queryCount;
	/**
	 * 初始化类型
	 */
	private String type = "";

	/**
	 * MQ查询页面初始化
	 * type：1,2  1：查询页面；2：折线图
	 * @author wangyan10
	 * @return
	 * @since 2017-1-3 下午3:35:13
	 */
	public String init() {
		logger.debug("RecycleDevRateACT ==> init");
		DateTimeUtil dt = new DateTimeUtil();
		this.endtime = dt.getDate();
		this.starttime = dt.getDate();
		dt = new DateTimeUtil(this.endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 86400L - 1L) * 1000L);
		this.endtime = dt.getLongDate();
		dt = new DateTimeUtil(this.starttime);
		this.starttime = dt.getLongDate();

		mqList = bio.getMqIpPort();
		String newStartTime = setTime(this.starttime);
		String newEndTime = setTime(this.endtime);
		topicNameList = bio.getTopicNameList(newStartTime, newEndTime);
		// 局点名称
				instAreaName = Global.instAreaShortName;
				logger.warn("instAreaName="+instAreaName);
		if ("1".equals(this.type)) {
			return "init";
		}else{
			return "initEcharts";
		}
	}
	/**
	 * 1表示一周以内
	 * -1表示一周以外
	 * @return
	 */
	public String bijiao()
	{
		this.starttime = setTime(this.starttime);
		this.endtime = setTime(this.endtime);
		int A=Integer.valueOf(starttime);
		int B=Integer.valueOf(endtime);
		int C=B-A;
		logger.warn("比较"+C);
		if(C<=604800)
		{
			ajax="1";
		}
		else
		{
			ajax="-1";
		}
		return "ajax";
	}
	/**
	 * 根据mq，开始时间，结束时间主题名获取列表
	 * @author wangyan10
	 * @return
	 * @since 2017-1-3 下午3:36:45
	 */
	public String getMqListByMq() {
		logger.debug("DevVerificationDisListACT ==> queryDevVerification()");
		logger.warn(this.starttime + "-------starttime--------");
		this.starttime = setTime(this.starttime);
		this.endtime = setTime(this.endtime);
		this.queryMqList = this.bio.getMqListByMq(this.mqId, this.starttime,
				this.endtime, this.topicName, this.curPage_splitPage,
				this.num_splitPage);
		this.maxPage_splitPage = this.bio.countMqListByMq(this.mqId,
				this.starttime, this.endtime, this.topicName,
				this.curPage_splitPage, this.num_splitPage);
		this.queryCount = this.bio.getQueryCount();
		return "list";
	}

	/**
	 * 获取当前主题名的详情
	 * @author wangyan10
	 * @return
	 * @since 2017-1-3 下午3:51:48
	 */
	public String getMqDetail() {
		logger.debug("DevVerificationDisListACT ==> queryDevVerification()");
		if (this.starttime.length() > 10) {
			this.starttime = setTime(this.starttime);
			this.endtime = setTime(this.endtime);
		}

		long starttime1 = Long.parseLong(this.starttime);
		long endtime1 = Long.parseLong(this.endtime);
		this.mqDetailList = this.bio.getMqDetail(this.mqId, this.starttime,
				this.endtime, this.gathertime, this.topicName,
				this.curPage_splitPage, this.num_splitPage);
		this.maxPage_splitPage = this.bio.countMqDetail(this.mqId,
				this.starttime, this.endtime, this.gathertime, this.topicName,
				this.curPage_splitPage, this.num_splitPage);
		this.queryCount = this.bio.getQueryCount();
		DateTimeUtil dt = new DateTimeUtil();
		dt = new DateTimeUtil(endtime1 * 1000L);
		this.endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime1 * 1000L);
		this.starttime = dt.getLongDate();
		mqList = bio.getMqIpPort();
		return "detail";
	}

	/**
	 * 获取当前查询条件的详情数据
	 * @author wangyan10
	 * @return
	 * @since 2017-1-3 下午3:54:46
	 */
	public String queryMqDetail() {
		logger.debug("DevVerificationDisListACT ==> queryDevVerification()");
		this.starttime = setTime(this.starttime);
		this.endtime = setTime(this.endtime);

		long starttime1 = Long.parseLong(this.starttime);
		long endtime1 = Long.parseLong(this.endtime);
		this.mqDetailList = this.bio.getMqDetail(this.mqId, this.starttime,
				this.endtime, this.gathertime, this.topicName,
				this.curPage_splitPage, this.num_splitPage);
		this.maxPage_splitPage = this.bio.countMqDetail(this.mqId,
				this.starttime, this.endtime, this.gathertime, this.topicName,
				this.curPage_splitPage, this.num_splitPage);
		this.queryCount = this.bio.getQueryCount();
		DateTimeUtil dt = new DateTimeUtil();
		dt = new DateTimeUtil(endtime1 * 1000L);
		this.endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime1 * 1000L);
		this.starttime = dt.getLongDate();
		return "detailList";
	}

	/**
	 * 折线图图表
	 * @author wangyan10
	 * @return
	 * @throws Exception
	 * @since 2017-1-3 下午3:55:16
	 */
	public String getMqEchartsData() throws Exception{
		logger.warn("getMqEchartsData()方法人口");
		this.starttime = setTime(this.starttime);
		this.endtime = setTime(this.endtime);
		ajax =bio.getMqEchartsData(mqId, starttime, endtime,topicName);
		logger.warn("getMqEchartsData()方法出口");
		return "ajax";
	}
	
	/**
	 * 时间设置
	 * @author wangyan10
	 * @param time
	 * @return
	 * @since 2017-1-3 下午3:57:42
	 */
	private String setTime(String time) {
		logger.debug("setTime()" + time);
		DateTimeUtil dt = null;
		if (!StringUtil.IsEmpty(time)) {
			dt = new DateTimeUtil(time);
			time = StringUtil.getStringValue(Long.valueOf(dt.getLongTime()));
			return time;
		}

		return "";
	}

	public List<HashMap<String, String>> getMqList() {
		return mqList;
	}

	public void setMqList(List<HashMap<String, String>> mqList) {
		this.mqList = mqList;
	}


	public String getMqId() {
		return mqId;
	}

	public void setMqId(String mqId) {
		this.mqId = mqId;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getGathertime() {
		return gathertime;
	}

	public void setGathertime(String gathertime) {
		this.gathertime = gathertime;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getQueryMqList() {
		return queryMqList;
	}

	@SuppressWarnings("rawtypes")
	public void setQueryMqList(List<Map> queryMqList) {
		this.queryMqList = queryMqList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getMqDetailList() {
		return mqDetailList;
	}

	@SuppressWarnings("rawtypes")
	public void setMqDetailList(List<Map> mqDetailList) {
		this.mqDetailList = mqDetailList;
	}

	@SuppressWarnings("rawtypes")
	public Map getSession() {
		return session;
	}

	@SuppressWarnings("rawtypes")
	public void setSession(Map session) {
		this.session = session;
	}

	public QueryMqBIO getBio() {
		return bio;
	}

	public void setBio(QueryMqBIO bio) {
		this.bio = bio;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public List<HashMap<String, String>> getTopicNameList() {
		return topicNameList;
	}

	public void setTopicNameList(List<HashMap<String, String>> topicNameList) {
		this.topicNameList = topicNameList;
	}

	public String getInstAreaName() {
		return instAreaName;
	}

	public void setInstAreaName(String instAreaName) {
		this.instAreaName = instAreaName;
	}

}