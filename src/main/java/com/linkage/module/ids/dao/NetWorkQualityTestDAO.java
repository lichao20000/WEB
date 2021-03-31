
package com.linkage.module.ids.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-11-1
 * @category com.linkage.module.ids.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings("rawtypes")
public class NetWorkQualityTestDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(NetWorkQualityTestDAO.class);

	public List<Map> netWorkQualityInfo(String start_time, String end_time,String avg_delay,
			String appear_count, String loss_pp, int curPage_splitPage,int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select distinct c.username,c.address, a.device_serialnumber,d.ag ,d.appear_count,a.loss_pp from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c ,");
			sql.append(" (select t.device_serialnumber,avg(t.avg_delay) ag, count(1) appear_count from tab_network_quality t  where 1=1 ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and t.update_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and t.update_time<=").append(end_time);
			}
			sql.append("  group by t.device_serialnumber) d");
			sql.append("  where a.device_serialnumber=b.device_serialnumber  and  a.device_serialnumber=d.device_serialnumber   and  TO_NUMBER(nvl(b.customer_id,0)) =c.user_id ");
		}
		else if(DBUtil.GetDB()==3)
		{
			//TODO wait
			sql.append("select distinct c.username,c.address,a.device_serialnumber,d.ag,d.appear_count,a.loss_pp ");
			sql.append("from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c,");
			sql.append("(select t.device_serialnumber,avg(t.avg_delay) ag,count(*) appear_count from tab_network_quality t where 1=1  ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and t.update_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and t.update_time<=").append(end_time);
			}
			sql.append(" group by t.device_serialnumber) d ");
			sql.append("where a.device_serialnumber=b.device_serialnumber and a.device_serialnumber=d.device_serialnumber ");
			sql.append("and cast(ifnull(b.customer_id,'0') as signed)=c.user_id ");
		}
		else
		{
			sql.append("select distinct c.username,c.address, a.device_serialnumber,d.ag ,d.appear_count,a.loss_pp from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c, ");
			sql.append(" (select t.device_serialnumber,avg(t.avg_delay) ag, count(1) appear_count from tab_network_quality t where 1=1  ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and t.update_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and t.update_time<=").append(end_time);
			}
			sql.append(" group by t.device_serialnumber) d ");
			sql.append(" where a.device_serialnumber=b.device_serialnumber and a.device_serialnumber=d.device_serialnumber  and   convert(numeric, isnull(b.customer_id,'0'))=c.user_id ");
		}
		if (!StringUtil.IsEmpty(loss_pp))
		{
			sql.append(" and a.loss_pp>=").append(loss_pp);
		}
		if (!StringUtil.IsEmpty(avg_delay))
		{
			sql.append(" and d.ag>").append(avg_delay);
		}
		if (!StringUtil.IsEmpty(appear_count))
		{
			sql.append(" and d.appear_count >=").append(appear_count);
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.update_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.update_time<=").append(end_time);
		}
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		List<Map> list = querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_serialnumber",
						StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("loid", StringUtil.getStringValue(rs.getString("username")));
				map.put("address", StringUtil.getStringValue(rs.getString("address")));
				double d = StringUtil.getDoubleValue(rs.getString("ag"));
				map.put("avg_delay", StringUtil.getStringValue(doubleToLongWhithRound(d)));
				map.put("appear_count",
						StringUtil.getStringValue(rs.getString("appear_count")));
				map.put("loss_pp", StringUtil.getStringValue(rs.getString("loss_pp")));
				return map;
			}
		});
		return list;
	}

	public int countnetWorkQualityInfo(String start_time, String end_time,
			String avg_delay, String appear_count, String loss_pp, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("NetWorkQualityTestDAO=>countNetWorkQualityTestInfo()");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) from (");
		}else{
			sql.append("select count(1) from (");
		}
		
		if (LipossGlobals.isOracle())
		{
			sql.append("select distinct c.username,c.address, a.device_serialnumber,d.ag ,d.appear_count,a.loss_pp from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c ,");
			sql.append(" (select t.device_serialnumber,avg(t.avg_delay) ag, count(1) appear_count from tab_network_quality t where 1=1  ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and t.update_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and t.update_time<=").append(end_time);
			}
			sql.append(" group by t.device_serialnumber) d ");
			sql.append("  where a.device_serialnumber=b.device_serialnumber  and  a.device_serialnumber=d.device_serialnumber   and  TO_NUMBER(nvl(b.customer_id,0)) =c.user_id ");
		}
		else if(DBUtil.GetDB()==3)
		{
			//TODO wait
			sql.append("select distinct c.username,c.address,a.device_serialnumber,d.ag,d.appear_count,a.loss_pp ");
			sql.append("from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c,");
			sql.append("(select t.device_serialnumber,avg(t.avg_delay) ag, count(*) appear_count ");
			sql.append("from tab_network_quality t  where 1=1 ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and t.update_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and t.update_time<=").append(end_time);
			}
			sql.append(" group by t.device_serialnumber) d ");
			sql.append("where a.device_serialnumber=b.device_serialnumber and a.device_serialnumber=d.device_serialnumber ");
			sql.append("and cast(isfnull(b.customer_id,'0') as signed)=c.user_id ");
		}
		else
		{
			sql.append("select distinct c.username,c.address, a.device_serialnumber,d.ag ,d.appear_count,a.loss_pp from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c, ");
			sql.append(" (select t.device_serialnumber,avg(t.avg_delay) ag, count(1) appear_count from tab_network_quality t  where 1=1 ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and t.update_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and t.update_time<=").append(end_time);
			}
			sql.append(" group by t.device_serialnumber) d ");
			sql.append(" where a.device_serialnumber=b.device_serialnumber and a.device_serialnumber=d.device_serialnumber  and   convert(numeric, isnull(b.customer_id,'0'))=c.user_id ");
		}
		if (!StringUtil.IsEmpty(loss_pp))
		{
			sql.append(" and a.loss_pp>=").append(loss_pp);
		}
		if (!StringUtil.IsEmpty(avg_delay))
		{
			sql.append(" and d.ag>").append(avg_delay);
		}
		if (!StringUtil.IsEmpty(appear_count))
		{
			sql.append(" and d.appear_count >=").append(appear_count);
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.update_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.update_time<=").append(end_time);
		}
		sql.append(" ) m");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	@SuppressWarnings("unchecked")
	public List<Map> netWorkQualityInfoExcel(String start_time, String end_time,
			String avg_delay, String appear_count, String loss_pp)
	{
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select distinct c.username,c.address, a.device_serialnumber,d.ag ,d.appear_count,a.loss_pp from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c ,");
			sql.append(" (select t.device_serialnumber,avg(t.avg_delay) ag, count(1) appear_count from tab_network_quality t where 1=1 ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and t.update_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and t.update_time<=").append(end_time);
			}
			sql.append(" group by t.device_serialnumber) d ");
			sql.append("  where a.device_serialnumber=b.device_serialnumber  and  a.device_serialnumber=d.device_serialnumber   and  TO_NUMBER(nvl(b.customer_id,0)) =c.user_id ");
		}
		else if(DBUtil.GetDB()==3)
		{
			//TODO wait
			sql.append("select distinct c.username,c.address,a.device_serialnumber,d.ag,d.appear_count,a.loss_pp ");
			sql.append("from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c,");
			sql.append("(select t.device_serialnumber,avg(t.avg_delay) ag,count(*) appear_count ");
			sql.append("from tab_network_quality t  where 1=1 ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and t.update_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and t.update_time<=").append(end_time);
			}
			sql.append(" group by t.device_serialnumber) d ");
			sql.append("where a.device_serialnumber=b.device_serialnumber and a.device_serialnumber=d.device_serialnumber ");
			sql.append("and cast(ifnull(b.customer_id,'0') as signed)=c.user_id ");
		}
		else
		{
			sql.append("select distinct c.username,c.address, a.device_serialnumber,d.ag ,d.appear_count,a.loss_pp from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c, ");
			sql.append(" (select t.device_serialnumber,avg(t.avg_delay) ag, count(1) appear_count from tab_network_quality t  where 1=1  ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and t.update_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and t.update_time<=").append(end_time);
			}
			sql.append(" group by t.device_serialnumber) d ");
			sql.append(" where a.device_serialnumber=b.device_serialnumber and a.device_serialnumber=d.device_serialnumber  and   convert(numeric, isnull(b.customer_id,'0'))=c.user_id ");
		}
		if (!StringUtil.IsEmpty(loss_pp))
		{
			sql.append(" and a.loss_pp>=").append(loss_pp);
		}
		if (!StringUtil.IsEmpty(avg_delay))
		{
			sql.append(" and d.ag>").append(avg_delay);
		}
		if (!StringUtil.IsEmpty(appear_count))
		{
			sql.append(" and d.appear_count >=").append(appear_count);
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.update_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.update_time<=").append(end_time);
		}
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSQL.getSQL());
		if (null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				list.get(i).put("device_serialnumber",
								StringUtil.getStringValue(list.get(i).get(
										"device_serialnumber")));
				list.get(i).put("loid",
						StringUtil.getStringValue(list.get(i).get("username")));
				list.get(i).put("address",
						StringUtil.getStringValue(list.get(i).get("address")));
				double d = StringUtil.getDoubleValue(list.get(i).get("ag"));
				list.get(i).put("avg_delay",
						StringUtil.getStringValue(doubleToLongWhithRound(d)));
				list.get(i).put("appear_count",
						StringUtil.getStringValue(list.get(i).get("appear_count")));
				list.get(i).put("loss_pp",
						StringUtil.getStringValue(list.get(i).get("loss_pp")));
			}
		}
		return list;
	}

	/**
	 * 查询网络质量检测分析情况
	 * 
	 * @param start_time
	 *            开始时间
	 * @param end_time
	 *            结束时间
	 * @param avg_delay
	 *            延迟
	 * @param appear_count
	 *            出现次数
	 * @param curPage_splitPage
	 *            当前页
	 * @param num_splitPage
	 *            当前页个数
	 * @return
	 */
	public List<Map> netWorkQualityTestInfo(String start_time, String end_time,
			String device_sn, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("NetWorkQualityTestDAO=>netWorkQualityTestInfo()");
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select  c.username,c.address, a.device_serialnumber,a.avg_delay ,a.loss_pp, a.update_time from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c ");
			sql.append(" where a.device_serialnumber=b.device_serialnumber and  TO_NUMBER(nvl(b.customer_id,0)) =c.user_id  ");
		}
		else if(DBUtil.GetDB()==3)
		{
			//TODO wait
			sql.append("select c.username,c.address,a.device_serialnumber,a.avg_delay,a.loss_pp,a.update_time ");
			sql.append("from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c ");
			sql.append("where a.device_serialnumber=b.device_serialnumber ");
			sql.append("and cast(ifnull(b.customer_id,'0') as signed)=c.user_id ");
		}
		else
		{
			sql.append("select  c.username,c.address, a.device_serialnumber,a.avg_delay ,a.loss_pp, a.update_time from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c ");
			sql.append(" where a.device_serialnumber=b.device_serialnumber and  convert(numeric, isnull(b.customer_id,'0'))=c.user_id  ");
		}
		if (!StringUtil.IsEmpty(device_sn))
		{
			sql.append(" and a.device_serialnumber='").append(device_sn).append("' ");
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.update_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.update_time<=").append(end_time);
		}
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		List<Map> list = querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_serialnumber",
						StringUtil.getStringValue(rs.getObject("device_serialnumber")));
				map.put("loid", StringUtil.getStringValue(rs.getObject("username")));
				map.put("address", StringUtil.getStringValue(rs.getObject("address")));
				map.put("avg_delay", StringUtil.getStringValue(rs.getObject("avg_delay")));
				map.put("loss_pp", StringUtil.getStringValue(rs.getObject("loss_pp")));
				long update_time = StringUtil.getLongValue(rs.getString("update_time"))* 1000L;
				DateTimeUtil dt = new DateTimeUtil(update_time);
				map.put("update_time", dt.getLongDate());
				return map;
			}
		});
		return list;
	}

	/**
	 * 统计网络质量检测分析最大记录数
	 * 
	 * @param start_time
	 * @param end_time
	 * @param avg_delay
	 * @param appear_count
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countNetWorkQualityTestInfo(String start_time, String end_time,
			String device_sn, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("NetWorkQualityTestDAO=>countNetWorkQualityTestInfo()");
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select count(1) from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c ");
			sql.append(" where a.device_serialnumber=b.device_serialnumber and  TO_NUMBER(nvl(b.customer_id,0)) =c.user_id  ");
		}
		else if(DBUtil.GetDB()==3)
		{
			//TODO wait
			sql.append("select count(*) from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c ");
			sql.append("where a.device_serialnumber=b.device_serialnumber ");
			sql.append("and cast(ifnull(b.customer_id,'0') as signed)=c.user_id ");
		}
		else
		{
			sql.append("select count(1) from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c ");
			sql.append(" where a.device_serialnumber=b.device_serialnumber and  convert(numeric, isnull(b.customer_id,'0'))=c.user_id  ");
		}
		if (!StringUtil.IsEmpty(device_sn))
		{
			sql.append(" and a.device_serialnumber='").append(device_sn).append("' ");
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.update_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.update_time<=").append(end_time);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	@SuppressWarnings("unchecked")
	public List<Map> netWorkQualityTestExcel(String start_time, String end_time,
			String device_sn)
	{
		logger.debug("NetWorkQualityTestDAO=>netWorkQualityTestInfo()");
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select  c.username,c.address, a.device_serialnumber,a.avg_delay ,a.loss_pp, a.update_time from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c ");
			sql.append(" where a.device_serialnumber=b.device_serialnumber and  TO_NUMBER(nvl(b.customer_id,0)) =c.user_id  ");
		}
		else if(DBUtil.GetDB()==3)
		{
			//TODO wait
			sql.append("select c.username,c.address,a.device_serialnumber,a.avg_delay,a.loss_pp,a.update_time ");
			sql.append("from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c ");
			sql.append("where a.device_serialnumber=b.device_serialnumber ");
			sql.append("and cast(ifnull(b.customer_id,'0') as signed)=c.user_id ");
		}
		else
		{
			sql.append("select  c.username,c.address, a.device_serialnumber,a.avg_delay ,a.loss_pp, a.update_time from tab_network_quality a,tab_gw_device b,tab_hgwcustomer c ");
			sql.append(" where a.device_serialnumber=b.device_serialnumber and  convert(numeric, isnull(b.customer_id,'0'))=c.user_id  ");
		}
		if (!StringUtil.IsEmpty(device_sn))
		{
			sql.append(" and a.device_serialnumber='").append(device_sn).append("' ");
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.update_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.update_time<=").append(end_time);
		}
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSQL.getSQL());
		if (null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				list.get(i).put("device_serialnumber",
								StringUtil.getStringValue(list.get(i).get(
										"device_serialnumber")));
				list.get(i).put("loid",
						StringUtil.getStringValue(list.get(i).get("username")));
				list.get(i).put("address",
						StringUtil.getStringValue(list.get(i).get("address")));
				list.get(i).put("avg_delay",
						StringUtil.getStringValue(list.get(i).get("avg_delay")));
				list.get(i).put("loss_pp",
						StringUtil.getStringValue(list.get(i).get("loss_pp")));
				
				long update_time = StringUtil.getLongValue(list.get(i).get("update_time"))* 1000L;
				DateTimeUtil dt = new DateTimeUtil(update_time);
				list.get(i).put("update_time", dt.getLongDate());
			}
		}
		return list;
	}

	/**
	 * double转long(四舍五入)
	 * 
	 * @param d
	 * @return 只截取前面的整数 (四舍五入)
	 */
	public long doubleToLongWhithRound(double d)
	{
		if (StringUtil.IsEmpty(StringUtil.getStringValue(d))){
			return 0;
		}
		
		long l = 0;
		try{
			l = Math.round(d);
		}catch (Exception e){
			l = 0;
			return l;
		}
		return l;
	}
}
