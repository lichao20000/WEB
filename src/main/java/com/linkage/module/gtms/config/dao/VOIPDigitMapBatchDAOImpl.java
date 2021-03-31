package com.linkage.module.gtms.config.dao;

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
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.StrategyQueryDAO;

public class VOIPDigitMapBatchDAOImpl extends SuperDAO implements VOIPDigitMapBatchDAO {
	private static Logger logger = LoggerFactory.getLogger(VOIPDigitMapBatchDAOImpl.class);
	/**
	 * 查询所有的语音数图模版
	 */
	public List<Map> queryAllDigitMap(String flag) {
		String sql="";
		if(!"2".equals(flag)){
			sql = " select map_id, map_name from gw_voip_digit_map" ;
		}else if("2".equals(flag)){
			sql = " select digit_map_code map_id,remark map_name from tab_digit_map" ;
		}
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	public String queryDigitMapById(String mapId,String flag) {
		 String mapContent = "";
		if(!"2".equals(flag)){
			String sql = "select map_content from gw_voip_digit_map where map_id="+mapId;
			PrepareSQL psql = new PrepareSQL(sql);
		    Map map  =  jt.queryForMap(psql.getSQL());
		    if(null!= map  && !map.isEmpty()){
		    	mapContent = StringUtil.getStringValue(map.get("map_content"));
		    }
		}else{
			String sql = "select digit_map_value from tab_digit_map where digit_map_code='"+mapId+"'";
			PrepareSQL psql = new PrepareSQL(sql);
		    Map map  =  jt.queryForMap(psql.getSQL());
		    if(null!= map  && !map.isEmpty()){
		    	mapContent = StringUtil.getStringValue(map.get("digit_map_value"));
		    }
		}

		 return  mapContent ;
	}
	public List<Map> getDevice_id(String con,String condition){
		logger.warn("VOIPDigitMapBatchDAOImpl.getDevice_id()");
		List<Map> list=new ArrayList<Map>();;
		StringBuffer sql = new StringBuffer();
		sql.append("select device_id ");
		if("1".equals(con)){//dn
			String sub=condition.substring(condition.length()-6);
			sql.append("from tab_gw_device a where a.device_serialnumber like '%"+condition+"' and dev_sub_sn = '"+sub+"'");
		}else if("0".equals(con)){//loid
			sql.append("from tab_hgwcustomer where username = '"+condition+"' ");
		}else if("-1".equals(con)){//kdName
			sql.append("from tab_hgwcustomer a，hgwcust_serv_info b where a.user_id = b.user_id " +
					"and b.serv_type_id = 10 and b.username = '"+condition+"' ");
		}else if("2".equals(con)){//voiceNum
			sql.append("from tab_hgwcustomer a，hgwcust_serv_info b where a.user_id = b.user_id " +
					"and b.serv_type_id = 14 and b.username = '"+condition+"' ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		list= jt.queryForList(sql.toString());
		return list;
	}
	public List<Map> getDetailsForPage(String device_id,long starttimeCount,long endtimeCount,String openState,
			int curPage_splitPage, int num_splitPage) {
		logger.warn("VOIPDigitMapBatchDAOImpl.getDetailsForPage()");
		List<Map> list=new ArrayList<Map>();

		StringBuffer sql = new StringBuffer();
		sql.append("select service_id,time,start_time,status,result_id,result_desc ");
		sql.append("from gw_serv_strategy_batch_log ");
		sql.append("where service_id = 7 and device_id = "+device_id);

		sql.append(" and time < "+endtimeCount+" and time > "+starttimeCount);
		if(!openState.equals("2")){
			if(openState.equals("1")){
				sql.append(" and status = 100 and result_id = 1");
			}else if (openState.equals("0")){
				sql.append(" and status = 100 and result_id <> 1");
			}else {
				sql.append(" and status <> 100");
			}
		}
		sql.append(" order by time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();

		list=querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				 		num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1)throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				//业务名称
				String service_id = rs.getString("service_id");
				map.put("service_id", "虚拟网语音数图配置策略");
				//策略状态
				String status = rs.getString("status");
				String result_id = rs.getString("result_id");
				if("100".equals(status)){
					map.put("status","执行完成");
					if("1".equals(result_id)){
						map.put("result_id","成功");
					}else{
						map.put("result_id","失败");
					}
				}else{
					map.put("status","等待执行");
					map.put("result_id","");
				}
				// 将毫秒转换成时间
				try{
					//定制时间
					long time = StringUtil.getLongValue(rs.getString("time"));
					//执行时间
					long start_time = StringUtil.getLongValue(rs.getString("start_time"));
					map.put("time", new DateTimeUtil(time * 1000).getLongDate());
					if (!"等待执行".equals(map.get("status"))) {
						map.put("start_time", new DateTimeUtil(
								start_time * 1000).getLongDate());
					} else {
						map.put("start_time", "");
					}
				}catch (Exception e){
					map.put("time", "");
					map.put("start_time", "");
				}
				String result_desc = rs.getString("result_desc");
				if (Global.G_Fault_Map.get(StringUtil.getIntegerValue(rs.getString("result_id"))) == null) {
					map.put("result_desc",result_desc);
				} else {
					map.put("result_desc",Global.G_Fault_Map.get(StringUtil.getIntegerValue(rs.getString("result_id"))).getFaultReason());
				}
				return map;
			}
		});

		return list;
	}

	public int getDetailsCount(String device_id,long starttimeCount,long endtimeCount,
			String openState,int num_splitPage) {
		logger.warn("VOIPDigitMapBatchDAOImpl.getDetailsCount()");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) ");
		sql.append("from gw_serv_strategy_batch_log ");
		sql.append(" where service_id = 7 and device_id = "+device_id+" ");
		sql.append(" and time < "+endtimeCount+" and time > "+starttimeCount);
		if(!openState.equals("2")){
			if(openState.equals("1")){
				sql.append(" and status = 100 and result_id = 1");
			}else if (openState.equals("0")){
				sql.append(" and status = 100 and result_id <> 1");
			}else {
				sql.append(" and status <> 100");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();

		int maxPage = 1;
		int total = jt.queryForInt(sql.toString());
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
}
