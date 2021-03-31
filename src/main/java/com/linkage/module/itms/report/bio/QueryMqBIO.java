package com.linkage.module.itms.report.bio;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.report.dao.QueryMqDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

public class QueryMqBIO {
	private QueryMqDAO dao;

	/**
	 * MQ折线图查询
	 * @author wangyan10
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @return
	 * @throws Exception
	 * @since 2017-1-3 下午4:06:27
	 */
	@SuppressWarnings("rawtypes")
	public String getMqEchartsData(String mqId, String starttime,
			String endtime, String topicName) throws Exception {
		List<Map> clientIdList = null;
		List<Map> echartsMqList = null;
		List<Map> echartsClientList = null;
		// 定义一个map去存放clientId，count_time 作为区分数据
		Map<String, String> newMqMap = new HashMap<String, String>();
		// 定义横坐标数组
		ArrayList<String> xAxisList = new ArrayList<String>();
		// 定义json
		JSONObject jo = new JSONObject();
		JSONObject data = new JSONObject();
		// 获取订阅者数量
		clientIdList = dao.getClientIdByTopicName(mqId, starttime, endtime,
				topicName);
		// 获取mq数据
		echartsMqList = dao.getMqEchartsData(mqId, starttime, endtime,
				topicName);
		// 定义并存储mq数据
		ArrayList<String> mqSaveList = new ArrayList<String>();
		for (int i = 0; i < echartsMqList.size(); i++) {
			mqSaveList.add(echartsMqList.get(i).get("count_num").toString());
		}
		data.put(topicName, mqSaveList);
		// 设置x轴
		for (int i = 0; i < echartsMqList.size(); i++) {
			xAxisList.add(echartsMqList.get(i).get("count_time").toString());
		}
		// 获取当前mq主题下，所有clientId数据
		echartsClientList = dao.getClientEchartsData(mqId, starttime, endtime,
				topicName);
		// 将数据放入newMqMap 
		for (Map map : echartsClientList) {
			newMqMap.put(map.get("count_time").toString()
					+ map.get("client_id").toString(), map.get("count_num")
					.toString());
		}
		// 获取订阅者数量，循环获取订阅者的统计值
		for (int i = 0; i < clientIdList.size(); i++) {

			String clientId = clientIdList.get(i).get("client_id").toString();
			String[] clientIdArray = clientId.split("-");
			ArrayList<String> saveList = new ArrayList<String>();
			// 将当前clientId，时间下  所有数据与newMqMap进行对比，并存储到数组
			for (int j = 0; j < xAxisList.size(); j++) {
				// 循环的将订阅者的统计值放入list里面
				String key = xAxisList.get(j) + clientId;
				if (newMqMap.containsKey(key)) {
					saveList.add(newMqMap.get(key));
				} else {
					saveList.add("0");
				}
			}
			// 将clientId作为name传入
			data.put(clientIdArray[0].substring(3) + "-" + clientIdArray[1],
					saveList);
		}
		// 清空xAxisList 并重新设置时间轴
		xAxisList.clear();
		for (int i = 0; i < echartsMqList.size(); i++) {
			DateTimeUtil dt = new DateTimeUtil(
					StringUtil.getLongValue(echartsMqList.get(i)
							.get("count_time").toString()) * 1000l);
			xAxisList.add(dt.getLongDate());
		}
		jo.put("data", data);
		jo.put("xcontent", xAxisList);
		return jo.toString();
	}

	/**
	 * 获取mq列表
	 * @author wangyan10
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 * @since 2017-1-3 下午4:14:02
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getMqListByMq(String mqId, String starttime,
			String endtime, String topicName, int curPage_splitPage,
			int num_splitPage) {
		return this.dao.getMqListByMq(mqId, starttime, endtime, topicName,
				curPage_splitPage, num_splitPage);
	}

	/**
	 * 统计mq数量
	 * @author wangyan10
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 * @since 2017-1-3 下午4:14:08
	 */
	public int countMqListByMq(String mqId, String starttime, String endtime,
			String topicName, int curPage_splitPage, int num_splitPage) {
		return this.dao.countMqListByMq(mqId, starttime, endtime, topicName,
				curPage_splitPage, num_splitPage);
	}

	/**
	 * 获取mq详情
	 * @author wangyan10
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param gathertime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 * @since 2017-1-3 下午4:14:16
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getMqDetail(String mqId, String starttime, String endtime,
			String gathertime, String topicName, int curPage_splitPage,
			int num_splitPage) {
		return this.dao.getMqDetail(mqId, starttime, endtime, gathertime,
				topicName, curPage_splitPage, num_splitPage);
	}

	/**
	 * 统计mq详情列表数量
	 * @author wangyan10
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param gathertime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 * @since 2017-1-3 下午4:14:21
	 */
	public int countMqDetail(String mqId, String starttime, String endtime,
			String gathertime, String topicName, int curPage_splitPage,
			int num_splitPage) {
		return this.dao.countMqDetail(mqId, starttime, endtime, gathertime,
				topicName, curPage_splitPage, num_splitPage);
	}

	/**
	 * 获取数量
	 * @author wangyan10
	 * @return
	 * @since 2017-1-3 下午4:16:19
	 */
	public int getQueryCount() {
		return this.dao.getQueryCount();
	}

	/**
	 * 获取所有Mq
	 * @author wangyan10
	 * @return
	 * @since 2017-1-3 下午4:16:31
	 */
	public List<HashMap<String, String>> getMqIpPort() {
		return this.dao.getMqIpPort();
	}

	/**
	 * 折线图，获取当前mq所有主题
	 * @author wangyan10
	 * @param starttime
	 * @param endtime
	 * @return
	 * @since 2017-1-3 下午4:16:47
	 */
	public List<HashMap<String, String>> getTopicNameList(String starttime,
			String endtime) {
		return this.dao.getTopicNameList(starttime, endtime);
	}

	public QueryMqDAO getDao() {
		return this.dao;
	}

	public void setDao(QueryMqDAO dao) {
		this.dao = dao;
	}
}