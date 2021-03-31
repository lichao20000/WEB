package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class QueryMqDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(QueryMqDAO.class);
	private int queryCount;

	/**
	 * 获取当前mq主题数据
	 * @author wangyan10
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @return
	 * @since 2017-1-3 下午4:18:09
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getMqEchartsData(String mqId, String starttime, String endtime, String topicName)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.count_num,a.count_time,a.topic_name ");
		psql.append(" from  tab_mqmonitor_count a ");
		psql.append(" where a.type =1 ");
		if (!"-1".equals(mqId)) {
			psql.append(" and  a.mq_id=" + mqId);
		}
		if (!StringUtil.IsEmpty(starttime)) {
			psql.append("   and a.count_time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			psql.append("   and a.count_time <= ");
			psql.append(endtime);
		}
		if (!StringUtil.IsEmpty(topicName)) {
			psql.append("   and a.topic_name like '%");
			psql.append(topicName + "%'");
		}
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 获取当前mq所有clientid数据
	 * @author wangyan10
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @return
	 * @since 2017-1-3 下午4:18:40
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getClientEchartsData(String mqId, String starttime, String endtime, String topicName)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.count_num,a.count_time,a.client_id ");
		psql.append(" from  tab_mqmonitor_count a ");
		psql.append(" where a.type =2 ");
		if (!"-1".equals(mqId)) {
			psql.append(" and  a.mq_id=" + mqId);
		}
		if (!StringUtil.IsEmpty(starttime)) {
			psql.append("   and a.count_time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			psql.append("   and a.count_time <= ");
			psql.append(endtime);
		}
		if (!StringUtil.IsEmpty(topicName)) {
			psql.append("   and a.topic_name like '%");
			psql.append(topicName + "%'");
		}
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 获取订阅者数量
	 * @author wangyan10
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @return
	 * @since 2017-1-3 下午4:19:36
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getClientIdByTopicName(String mqId, String starttime, String endtime, String topicName)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select distinct(client_id) ");
		psql.append(" from  tab_mqmonitor_count a ");
		psql.append(" where a.type = 2  ");
		if (!"-1".equals(mqId)) {
			psql.append(" and  a.mq_id=" + mqId);
		}
		if (!StringUtil.IsEmpty(starttime)) {
			psql.append("   and a.count_time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			psql.append("   and a.count_time <= ");
			psql.append(endtime);
		}
		if (!StringUtil.IsEmpty(topicName)) {
			psql.append("   and a.topic_name like '%");
			psql.append(topicName + "%'");
		}
		return jt.queryForList(psql.getSQL());
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
	 * @since 2017-1-3 下午4:19:43
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getMqListByMq(String mqId, String starttime,
			String endtime, String topicName, int curPage_splitPage,
			int num_splitPage) 
	{
		logger.debug("getMqListByMq()");
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.topic_name,a.consumer_num,a.message_enqueued,a.message_dequeued,a.time,a.mq_id ");
		psql.append(" from  tab_mqmonitor_topics a ");
		psql.append(" where 1=1 ");
		if (!"-1".equals(mqId)) {
			psql.append(" and  a.mq_id=" + mqId);
		}
		if (!StringUtil.IsEmpty(topicName)) {
			psql.append("   and a.topic_name like '%");
			psql.append(topicName + "%'");
		}
		if (!StringUtil.IsEmpty(starttime)) {
			psql.append("   and a.time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			psql.append("   and a.time <= ");
			psql.append(endtime);
		}
		psql.append(" order by a.time desc");

		List list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				// 主题
				map.put("topicName",
						StringUtil.getStringValue(rs.getString("topic_name")));

				// mqId
				map.put("mqId",
						StringUtil.getStringValue(rs.getString("mq_id")));

				// 消费者数量
				map.put("consumer_num",
						StringUtil.getStringValue(rs.getString("consumer_num")));

				// 消息入队
				map.put("message_enqueued", StringUtil.getStringValue(rs
						.getString("message_enqueued")));

				// 消息出队
				map.put("message_dequeued", StringUtil.getStringValue(rs
						.getString("message_dequeued")));

				// 采集时间
				map.put("gathertime", StringUtil.getStringValue(rs
						.getString("time")));

				 // 核销日期
				 DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(rs
				 .getString("time")) * 1000l);
				 map.put("showtime", dt.getLongDate());

				return map;
			}
		});

		return list;
	}

	/**
	 * 获取mq列表数量
	 * @author wangyan10
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 * @since 2017-1-3 下午4:19:48
	 */
	public int countMqListByMq(String mqId, String starttime, String endtime,
			String topicName, int curPage_splitPage, int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_mqmonitor_topics a where 1=1 ");
		if (!"-1".equals(mqId)) {
			psql.append(" and  a.mq_id=" + mqId);
		}
		if (!StringUtil.IsEmpty(topicName)) {
			psql.append("   and a.topic_name like '%");
			psql.append(topicName + "%'");
		}
		if (!StringUtil.IsEmpty(starttime)) {
			psql.append("   and a.time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			psql.append("   and a.time <= ");
			psql.append(endtime);
		}

		queryCount = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (queryCount % num_splitPage == 0) {
			maxPage = queryCount / num_splitPage;
		} else {
			maxPage = queryCount / num_splitPage + 1;
		}
		return maxPage;
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
	 * @since 2017-1-3 下午4:19:54
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getMqDetail(String mqId, String starttime, String endtime,
			String gathertime, String topicName, int curPage_splitPage,
			int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.client_id,a.subscripion_name,a.destination_topic,");
		psql.append("a.pending_queue_size,a.dispatched_queue_size,");
		psql.append("a.dispatched_counter,a.enqueue_counter,a.dequeue_counter,a.time ");
		psql.append(" from  tab_mqmonitor_subscribers a ");
		psql.append(" where 1=1 ");
		if ((!"-1".equals(mqId)) && (!StringUtil.IsEmpty(mqId))) {
			psql.append(" and  a.mq_id=" + mqId);
		}
		if (!StringUtil.IsEmpty(topicName)) {
			psql.append("   and a.destination_topic like '%");
			psql.append(topicName + "%'");
		}

		if (!StringUtil.IsEmpty(gathertime)) {
			psql.append("   and a.time = ");
			psql.append(gathertime + "");
		} else {
			if (!StringUtil.IsEmpty(starttime)) {
				psql.append("   and a.time >= ");
				psql.append(starttime);
			}
			if (!StringUtil.IsEmpty(endtime)) {
				psql.append("   and a.time <= ");
				psql.append(endtime);
			}
		}
		psql.append(" order by a.time desc");

		List list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();

				// 用户LOID
				map.put("client_id",
						StringUtil.getStringValue(rs.getString("client_id")));

				// 工单号
				map.put("subscripion_name", StringUtil.getStringValue(rs
						.getString("subscripion_name")));

				// ITMS设备
				map.put("destination_topic", StringUtil.getStringValue(rs
						.getString("destination_topic")));

				// ITMS设备规格
				map.put("pending_queue_size", StringUtil.getStringValue(rs
						.getString("pending_queue_size")));

				// 核销设备
				map.put("dispatched_queue_size", StringUtil.getStringValue(rs
						.getString("dispatched_queue_size")));

				// 核销设备规格
				map.put("dispatched_counter", StringUtil.getStringValue(rs
						.getString("dispatched_counter")));
				map.put("enqueue_counter", StringUtil.getStringValue(rs
						.getString("enqueue_counter")));
				map.put("dequeue_counter", StringUtil.getStringValue(rs
						.getString("dequeue_counter")));
				DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(rs
						.getString("time")) * 1000l);
				map.put("gathertime", dt.getLongDate());

				return map;
			}
		});

		return list;
	}

	/**
	 * 统计mq详情数量
	 * @author wangyan10
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param gathertime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 * @since 2017-1-3 下午4:20:00
	 */
	public int countMqDetail(String mqId, String starttime, String endtime,
			String gathertime, String topicName, int curPage_splitPage,
			int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		
		psql.append("from tab_mqmonitor_subscribers a where 1=1 ");
		if ((!"-1".equals(mqId)) && (!StringUtil.IsEmpty(mqId))) {
			psql.append(" and  a.mq_id=" + mqId);
		}
		if (!StringUtil.IsEmpty(topicName)) {
			psql.append("   and a.destination_topic like '%");
			psql.append(topicName + "%'");
		}

		if (!StringUtil.IsEmpty(gathertime)) {
			psql.append("   and a.time = ");
			psql.append(gathertime + "");
		} else {
			if (!StringUtil.IsEmpty(starttime)) {
				psql.append("   and a.time >= ");
				psql.append(starttime);
			}
			if (!StringUtil.IsEmpty(endtime)) {
				psql.append("   and a.time <= ");
				psql.append(endtime);
			}
		}

		this.queryCount = this.jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (this.queryCount % num_splitPage == 0) {
			maxPage = this.queryCount / num_splitPage;
		} else {
			maxPage = this.queryCount / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 获取所有mq
	 * @author wangyan10
	 * @return
	 * @since 2017-1-3 下午4:20:05
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<HashMap<String, String>> getMqIpPort() {
		String strSQL = "select id, mq_ipaddress, mq_port from tab_mqmonitor_list  ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		List list = this.jt.queryForList(psql.getSQL());
		if (list == null) {
			list = new ArrayList();
		}
		return list;
	}

	/**
	 * 折线图，获取当前mq所有主题名
	 * @author wangyan10
	 * @param starttime
	 * @param endtime
	 * @return
	 * @since 2017-1-3 下午4:20:10
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<HashMap<String, String>>  getTopicNameList(String starttime, String endtime) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select distinct(topic_name) id,topic_name ");
		psql.append("from tab_mqmonitor_count a where type=1 ");
		if (!StringUtil.IsEmpty(starttime)) {
			psql.append("   and a.count_time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			psql.append("   and a.count_time <= ");
			psql.append(endtime);
		}
		
		List list = this.jt.queryForList(psql.getSQL());
		if (list == null) {
			list = new ArrayList();
		}
		return list;
	}
	
	public int getQueryCount() {
		return this.queryCount;
	}
}