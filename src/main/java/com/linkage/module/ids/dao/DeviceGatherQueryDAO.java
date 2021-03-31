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
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class DeviceGatherQueryDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(DeviceGatherQueryDAO.class);
	
	public List getDeviceInfo(int curPage_splitPage,int num_splitPage,String starttime1, String endtime1,
			String routeInfo,String voiceRegistInfo) 
	{
	  PrepareSQL ps = new PrepareSQL();
	  ps.append(" select a.gather_time,a.device_id,a.last_conn_error,b.regist_result "); 
	  ps.append(" from gw_wan_conn_session_history a left join gw_voip_prof_iad_history b ");
	  ps.append(" on a.device_id = b.device_id       ");
	  ps.append(" where a.gather_time=b.gather_time  ");
	  ps.append(" and a.serv_list = 'INTERNET'  ");
	  if(!StringUtil.IsEmpty(routeInfo)&&routeInfo.equals("1")){
			ps.append(" and last_conn_error = 'ERROR_NONE' ");
	  }else if(!StringUtil.IsEmpty(routeInfo)&&routeInfo.equals("0")){
			ps.append(" and last_conn_error != 'ERROR_NONE' ");
	  }
	  if(!StringUtil.IsEmpty(voiceRegistInfo)){
			ps.append(" and regist_result = '"+voiceRegistInfo+"'");
		}
	  ps.append(" and a.gather_time >= "+starttime1);
	  ps.append(" and a.gather_time < "+endtime1);
	  List<Map> list = querySP(ps.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				String device_id = rs.getString("device_id");
				map.put("device_id", device_id);
				//下发时间转格式yyyy-mmdd hh:mm:ss
				try
				{
					long addtime = StringUtil.getLongValue(rs.getString("gather_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(addtime);
					map.put("gather_time", dt.getLongDate());
				}
				catch (Exception e)
				{
					map.put("gather_time", "");
				}
				String enable = rs.getString("regist_result");
				if(StringUtil.getIntegerValue(enable)==1){
					map.put("regist_result", "失败");
				}else{
					map.put("regist_result", "成功");
				}
				if(StringUtil.getStringValue(rs.getString("last_conn_error")).equals("ERROR_NONE")){
					map.put("last_conn_error", "成功");
				}else{
					map.put("last_conn_error", "失败");
				}
				return map;
			}
		});
	  return list;
	}
	
	//总数
	public int getDeviceGatherCount(int num_splitPage, String starttime1,
			String endtime1,String routeInfo,String voiceRegistInfo) 
	{
		PrepareSQL ps = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			 ps.append("select count(*) ");
		}else{
			 ps.append("select count(1) ");
		}
		  
		ps.append("from gw_wan_conn_session_history a ");
		ps.append("left join gw_voip_prof_iad_history b on a.device_id=b.device_id ");
		ps.append("where a.gather_time=b.gather_time and a.serv_list='INTERNET' ");
		if(!StringUtil.IsEmpty(routeInfo) && "1".equals(routeInfo)){
			ps.append(" and last_conn_error='ERROR_NONE'");
		}else if(!StringUtil.IsEmpty(routeInfo) && "0".equals(routeInfo)){
			ps.append(" and last_conn_error!='ERROR_NONE'");
		}
		if(!StringUtil.IsEmpty(voiceRegistInfo)){
			ps.append(" and regist_result='"+voiceRegistInfo+"'");
		}
		ps.append(" and a.gather_time>="+starttime1+" and a.gather_time<"+endtime1);
	  
		int total = jt.queryForInt(ps.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	
}
