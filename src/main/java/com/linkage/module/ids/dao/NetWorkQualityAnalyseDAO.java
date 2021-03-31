package com.linkage.module.ids.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2014-2-27
 * @category com.linkage.module.ids.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class NetWorkQualityAnalyseDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(NetWorkQualityAnalyseDAO.class);
	
	public List<Map> netWorkQualityAnalyseInfo(String start_time, String end_time,String device_serialnumber,
			String loid,int curPage_splitPage,int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		//TODO wait
		sql.append("select a.area_name,a.subarea_name,a.loid,a.time,c.device_serialnumber,a.tx_power,");
		sql.append("a.rx_power,c.linkaddress,a.olt_name,a.olt_ip,a.pon_id,a.ont_id,");
		sql.append("a.count_num,b.avg_delay,b.loss_pp,b.update_time ");
		sql.append("from tab_pon_netWork a,tab_network_quality b,tab_hgwcustomer c,tab_gw_device d ");
		sql.append("where a.loid=c.username and b.device_serialnumber=d.device_serialnumber and c.device_id=d.device_id ");
		if (!StringUtil.IsEmpty(start_time))
		{
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append(" and a.time>=str_to_date('"+start_time+"','%Y-%m-%d %H:%i:%s') ");
			}else{
				sql.append(" and a.time>=to_date('"+start_time+"','yyyy-mm-dd hh24:mi:ss') ");
			}
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append(" and a.time<=str_to_date('"+end_time+"','%Y-%m-%d %H:%i:%s') ");
			}else{
				sql.append(" and a.time<=to_date('"+end_time+"','yyyy-mm-dd hh24:mi:ss') ");
			}
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			sql.append(" and d.device_serialnumber='").append(device_serialnumber.trim()).append("' ");
		}
		if(!StringUtil.IsEmpty(loid)){
			sql.append(" and c.username='").append(loid.trim()).append("'");
		}
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		List<Map> list = querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("area_name", rs.getString("area_name"));
				map.put("subarea_name", rs.getString("subarea_name"));
				map.put("loid", rs.getString("loid"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("tx_power", rs.getString("tx_power"));
				map.put("rx_power", rs.getString("rx_power"));
				map.put("linkaddress", rs.getString("linkaddress"));
				map.put("olt_name", rs.getString("olt_name"));
				map.put("olt_ip", rs.getString("olt_ip"));
				map.put("pon_id", rs.getString("pon_id"));
				map.put("ont_id", rs.getString("ont_id"));
				map.put("count_num", rs.getString("count_num"));
				map.put("avg_delay", StringUtil.getStringValue(rs.getObject("avg_delay")));
				map.put("loss_pp", StringUtil.getStringValue(rs.getObject("loss_pp")));
				map.put("time", rs.getString("time"));
				return map;
			}
		});
		return list;
	}
	
	public int countNetWorkQualityAnalyseInfo(String start_time, String end_time,String device_serialnumber,
			String loid, int curPage_splitPage,int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append(" select count(*) ");
		}else{
			sql.append(" select count(1) ");
		}
		//TODO wait
		sql.append("from tab_pon_netWork a,tab_network_quality b ,tab_hgwcustomer c,tab_gw_device d ");
		sql.append("where a.loid=c.username and b.device_serialnumber=d.device_serialnumber and c.device_id=d.device_id ");
		if (!StringUtil.IsEmpty(start_time))
		{
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append(" and a.time>=str_to_date('"+start_time+"','%Y-%m-%d %H:%i:%s') ");
			}else{
				sql.append(" and a.time>=to_date('"+start_time+"','yyyy-mm-dd hh24:mi:ss') ");
			}
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append(" and a.time<=str_to_date('"+end_time+"','%Y-%m-%d %H:%i:%s') ");
			}else{
				sql.append(" and a.time<=to_date('"+end_time+"','yyyy-mm-dd hh24:mi:ss') ");
			}
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			sql.append(" and d.device_serialnumber='").append(device_serialnumber).append("' ");
		}
		if(!StringUtil.IsEmpty(loid)){
			sql.append(" and c.username='").append(loid).append("'");
		}
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(pSQL.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	public List<Map> netWorkQualityAnalyseInfoExcel(String start_time, String end_time,
			String device_serialnumber, String loid)
	{
		StringBuffer sql = new StringBuffer();
		//TODO wait
		sql.append("select a.area_name,a.subarea_name,a.loid,a.time,c.device_serialnumber,");
		sql.append("a.tx_power,a.rx_power,c.linkaddress,a.olt_name,a.olt_ip,a.pon_id,");
		sql.append("a.ont_id,a.count_num,b.avg_delay,b.loss_pp,b.update_time ");
		sql.append("from tab_pon_netWork a,tab_network_quality b,tab_hgwcustomer c,tab_gw_device d ");
		sql.append("where a.loid=c.username and b.device_serialnumber=d.device_serialnumber and c.device_id=d.device_id ");
		if (!StringUtil.IsEmpty(start_time))
		{
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append(" and a.time>=str_to_date('"+start_time+"','%Y-%m-%d %H:%i:%s') ");
			}else{
				sql.append(" and a.time>=to_date('"+start_time+"','yyyy-mm-dd hh24:mi:ss') ");
			}
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append(" and a.time<=str_to_date('"+end_time+"','%Y-%m-%d %H:%i:%s') ");
			}else{
				sql.append(" and a.time<=to_date('"+end_time+"','yyyy-mm-dd hh24:mi:ss') ");
			}
		}
		if(!StringUtil.IsEmpty(loid)){
			sql.append(" and c.username='").append(loid).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.queryForList(psql.getSQL());
		if(null!=list && list.size()>0){
			for (int i = 0; i < list.size(); i++)
			{
				list.get(i).put("area_name", list.get(i).get("area_name"));
				list.get(i).put("subarea_name", list.get(i).get("subarea_name"));
				list.get(i).put("loid", list.get(i).get("loid"));
				list.get(i).put("device_serialnumber", list.get(i).get("device_serialnumber"));
				list.get(i).put("tx_power", list.get(i).get("tx_power"));
				list.get(i).put("rx_power", list.get(i).get("rx_power"));
				list.get(i).put("linkaddress", list.get(i).get("linkaddress"));
				list.get(i).put("olt_name", list.get(i).get("olt_name"));
				list.get(i).put("olt_ip", list.get(i).get("olt_ip"));
				list.get(i).put("pon_id", list.get(i).get("pon_id"));
				list.get(i).put("ont_id", list.get(i).get("ont_id"));
				list.get(i).put("count_num", list.get(i).get("count_num"));
				list.get(i).put("avg_delay", StringUtil.getStringValue(list.get(i).get("avg_delay")));
				list.get(i).put("loss_pp", StringUtil.getStringValue(list.get(i).get("loss_pp")));
				list.get(i).put("time", StringUtil.getStringValue(list.get(i).get("time")));
			}
		}
		return list;
	}
}
