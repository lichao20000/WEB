
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
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 根据设备序列号或LOID或宽带账号（下拉列表）、开通时间、结束时间查询无线业务策略
 * @author wanghong5 2015-02-13
 */
@SuppressWarnings("unchecked")
public class StrategyQueryDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(StrategyQueryDAO.class);
	private List<Map<String,String>> countList;

	public List<Map> getDevice_id(String con,String condition)
	{
		logger.warn("StrategyQueryDAO.getDevice_id()");
		List<Map> list=new ArrayList<Map>();;
		StringBuffer sql = new StringBuffer();
		sql.append("select device_id ");
		if("1".equals(con)){
			String sub=condition.substring(condition.length()-6);
			sql.append("from tab_gw_device where device_serialnumber ");
			sql.append("like '%"+condition+"' and dev_sub_sn = '"+sub+"'");
		}else if("0".equals(con)){
			sql.append("from tab_hgwcustomer where username = '"+condition+"' ");
		}else{
			sql.append("from tab_hgwcustomer a，hgwcust_serv_info b where a.user_id = b.user_id " +
					"and b.serv_type_id = 10 and b.username = '"+condition+"' ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		list= jt.queryForList(psql.getSQL());
		return list;
	}

	public List<Map> getDetailsForPage(String device_id,long starttimeCount,long endtimeCount,String openState,
			String type,int curPage_splitPage, int num_splitPage) 
	{
		logger.warn("StrategyQueryDAO.getDetailsForPage()");
		List<Map> list=new ArrayList<Map>();
		   
		StringBuffer sql = new StringBuffer();
		sql.append("select a.service_id,a.time,a.start_time,a.status,a.result_id,a.result_desc,b.wireless_type ");
		sql.append("from gw_serv_strategy_batch_log a,tab_wirelesst_task b ");
		sql.append(" where a.ids_task_id = b.task_id ");
		sql.append(" and a.device_id = "+device_id+" ");
		sql.append(" and a.time < "+endtimeCount+" and a.time > "+starttimeCount);
		sql.append(" and b.wireless_type = " + type+" ");
		if(!openState.equals("2")){
			if(openState.equals("1")){
				sql.append(" and a.status = 100 and a.result_id = 1");
			}else if (openState.equals("0")){
				sql.append(" and a.status = 100 and a.result_id <> 1");
			}else {
				sql.append(" and a.status <> 100");
			}
		}
		sql.append(" order by time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		list=querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				 		num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1)throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				//业务名称
				String service_id = rs.getString("service_id");
				String wireless_type =rs.getString("wireless_type");
				if("2001".equals(service_id)){
					if("1".equals(wireless_type)){
						map.put("service_id","wifi业务开通");
					}else if("2".equals(wireless_type)){
						map.put("service_id","校园网无线业务开通");
					}else if("3".equals(wireless_type)){
						map.put("service_id","无线专线业务开通");
					}
				}else{
					if("1".equals(wireless_type)){
						map.put("service_id","awifi业务关闭");
					}else if("2".equals(wireless_type)){
						map.put("service_id","校园网无线业务关闭");
					}else{
						map.put("service_id","无线专线业务关闭");
					}
				}
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
			String openState,String type,int num_splitPage) 
	{
		logger.warn("StrategyQueryDAO.getDetailsCount()");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		sql.append("from gw_serv_strategy_batch_log a,tab_wirelesst_task b ");
		sql.append("where a.ids_task_id = b.task_id ");
		sql.append("and a.device_id = "+device_id+" ");
		sql.append("and b.wireless_type = " + type+" ");
		sql.append("and a.time < "+endtimeCount+" and a.time > "+starttimeCount);
		if(!openState.equals("2")){
			if(openState.equals("1")){
				sql.append(" and a.status = 100 and a.result_id = 1");
			}else if (openState.equals("0")){
				sql.append(" and a.status = 100 and a.result_id <> 1");
			}else {
				sql.append(" and a.status <> 100");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		int maxPage = 1;
		int total = jt.queryForInt(psql.getSQL());
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List<Map<String, String>> getCountList() {
		return countList;
	}

	public void setCountList(List<Map<String, String>> countList) {
		this.countList = countList;
	}

	public StrategyQueryDAO(){
		
	}
	
}
