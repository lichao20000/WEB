package com.linkage.module.itms.service.dao;

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
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class ZeroconfigurationDAO_XJ extends SuperDAO {
	private static Logger logger = LoggerFactory.getLogger(ZeroconfigurationDAO_XJ.class);
	public List<Map<String,String>> queryServiceType(){
		PrepareSQL psql=new PrepareSQL();
		psql.append("select service_type from tab_zeroconfig_report group by service_type ");
		return jt.queryForList(psql.getSQL());
	}

	public List<Map<String,String>> queryOperateType(){
		PrepareSQL psql=new PrepareSQL();
		psql.append("select operate_type from tab_zeroconfig_report group by operate_type ");
		return jt.queryForList(psql.getSQL());
	}

	public List<Map> queryList(int curPage_splitPage, int num_splitPage,String user_info,String device_sn,
			String service_type,String operate_type,String cityId,String starttime,String endtime, String devsn){
		PrepareSQL psql = new PrepareSQL();
		if("4".equals(service_type))
		{
			if(!StringUtil.IsEmpty(user_info))
			{
				psql.append("select c.device_id,c.device_serialnumber,");
				if(DBUtil.GetDB()==3){
					//TODO wait
					psql.append("t1.city_id,t1.user_info,t1.device_sn,t1.operate_type,t1.update_time,");
					psql.append("t1.bind_time,t1.inft_result,t1.bind_result,t1.serv_account ");
				}else{
					psql.append("t1.* ");
				}
				psql.append("from stb_tab_gw_device c right join ");
				psql.append("(select a.city_id,a.user_info,a.device_sn,a.operate_type,a.update_time,");
				psql.append("a.bind_time,a.inft_result,a.bind_result,b.serv_account ");
				psql.append("from tab_zeroconfig_report a,stb_tab_customer b ");
				psql.append("where a.user_info=b.serv_account ");
				psql.append("and a.user_info='"+user_info+"'");
				if(!"-1".equals(service_type)){
					psql.append(" and a.service_type="+service_type);
				}
				if(!"-1".equals(operate_type) && !StringUtil.IsEmpty(operate_type)){
					psql.append(" and a.operate_type="+operate_type);
				}
				if((!"-1".equals(cityId) || "00".equals(cityId)) && !StringUtil.IsEmpty(cityId)){
					ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
					String cityStr = StringUtils.weave(cityArray,"','");
					psql.append(" and a.city_id in ('" + cityStr + "')");
				}

				if(!StringUtil.IsEmpty(starttime)){
					psql.append(" and a.update_time > "+new DateTimeUtil(starttime).getLongTime());
				}
				if(!StringUtil.IsEmpty(endtime)){
					psql.append(" and a.update_time <"+new DateTimeUtil(endtime).getLongTime());
				}
				if(!StringUtil.IsEmpty(devsn)){
					psql.append(" and a.device_sn='" + devsn + "' ");
				}

				psql.append(") t1 on c.cpe_mac=t1.device_sn");
			}
			else if(!StringUtil.IsEmpty(device_sn))
			{
				psql.append("select a.city_id,a.user_info,a.device_sn,c.device_serialnumber,a.operate_type, a.update_time, a.bind_time,a.inft_result," +
						"a.bind_result,c.device_id,c.serv_account from tab_zeroconfig_report a left join (select  device_id,device_serialnumber,cpe_mac,serv_account " +
						"from stb_tab_gw_device b where b.device_serialnumber ='"+device_sn+"') c on a.device_sn=c.device_serialnumber  ");

				if((!"-1".equals(cityId) || "00".equals(cityId)) && !StringUtil.IsEmpty(cityId)){
					ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
					String cityStr = StringUtils.weave(cityArray,"','");
					psql.append(" and a.city_id in ('" + cityStr + "')");
				}
				if(!StringUtil.IsEmpty(starttime)){
					psql.append(" and a.update_time > "+new DateTimeUtil(starttime).getLongTime());
				}
				if(!StringUtil.IsEmpty(endtime)){
					psql.append(" and a.update_time <"+new DateTimeUtil(endtime).getLongTime());
				}
				if(!StringUtil.IsEmpty(devsn)){
					psql.append(" and a.device_sn='" + devsn + "' ");
				}
			}
			else
			{
				psql.append("select c.device_id,c.device_serialnumber,");
				if(DBUtil.GetDB()==3){
					//TODO wait
					psql.append("t1.city_id,t1.user_info,t1.device_sn,t1.operate_type,t1.update_time,");
					psql.append("t1.bind_time,t1.inft_result,t1.bind_result,t1.serv_account ");
				}else{
					psql.append("t1.* ");
				}
				psql.append("from stb_tab_gw_device c right join ");
				psql.append("(select a.city_id,a.user_info,a.device_sn,a.operate_type,a.update_time,");
				psql.append("a.bind_time,a.inft_result,a.bind_result,b.serv_account ");
				psql.append("from tab_zeroconfig_report a,stb_tab_customer b ");
				psql.append("where a.user_info=b.serv_account ");
				if(!"-1".equals(service_type)){
					psql.append(" and a.service_type="+service_type);
				}
				if(!"-1".equals(operate_type) && !StringUtil.IsEmpty(operate_type)){
					psql.append(" and a.operate_type="+operate_type);
				}
				if((!"-1".equals(cityId) || "00".equals(cityId)) && !StringUtil.IsEmpty(cityId)){
					ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
					String cityStr = StringUtils.weave(cityArray,"','");
					psql.append(" and a.city_id in ('" + cityStr + "')");
				}

				if(!StringUtil.IsEmpty(starttime)){
					psql.append(" and a.update_time > "+new DateTimeUtil(starttime).getLongTime());
				}
				if(!StringUtil.IsEmpty(endtime)){
					psql.append(" and a.update_time <"+new DateTimeUtil(endtime).getLongTime());
				}
				if(!StringUtil.IsEmpty(devsn)){
					psql.append(" and a.device_sn='" + devsn + "' ");
				}
				psql.append(") t1 on c.cpe_mac=t1.device_sn");
			}
		}
		else
		{
			psql.append("select a.city_id,a.user_info,a.device_sn,a.service_type,a.operate_type, ");
			psql.append("a.update_time, a.bind_time,a.inft_result,a.bind_result, ");

			//	家庭网关用户表：tab_hgwcustomer  企业网关用户表：tab_egwcustomer 机顶盒用户表 stb_tab_customer
			//	家庭网关、企业网关 设备表：tab_gw_device  机顶盒设备表：stb_tab_gw_device
			if("3".equals(service_type)){
				psql.append("b.user_id customer_id,c.device_id ");
				psql.append("from tab_zeroconfig_report a,tab_egwcustomer b,tab_gw_device c ");
				psql.append("where a.user_info=b.username and a.device_sn=c.device_serialnumber ");
			}else{
				psql.append("b.user_id customer_id,c.device_id ");
				psql.append("from tab_zeroconfig_report a,tab_hgwcustomer b,tab_gw_device c ");
				psql.append("where a.user_info=b.username and a.device_sn=c.device_serialnumber ");
			}

			if(!StringUtil.IsEmpty(user_info)){
				psql.append(" and a.user_info='"+user_info+"'");
			}
			if(!StringUtil.IsEmpty(device_sn)){
				psql.append(" and a.device_sn='"+device_sn+"'");
			}
			if(!"-1".equals(service_type)){
				psql.append(" and a.service_type="+service_type);
			}
			if(!"-1".equals(operate_type) && !StringUtil.IsEmpty(operate_type)){
				psql.append(" and a.operate_type="+operate_type);
			}
			if((!"-1".equals(cityId) || "00".equals(cityId)) && !StringUtil.IsEmpty(cityId)){
				ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				String cityStr = StringUtils.weave(cityArray,"','");
				psql.append(" and a.city_id in ('" + cityStr + "')");
			}
			if(!StringUtil.IsEmpty(starttime)){
				psql.append(" and a.update_time > "+new DateTimeUtil(starttime).getLongTime());
			}
			if(!StringUtil.IsEmpty(endtime)){
				psql.append(" and a.update_time <"+new DateTimeUtil(endtime).getLongTime());
			}
			if(!StringUtil.IsEmpty(devsn)){
				psql.append(" and a.device_sn='" + devsn + "' ");
			}
		}

		return  querySP(psql.getSQL(),(curPage_splitPage-1) * num_splitPage+1,num_splitPage,new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String city_id = "";
				String user_info = "";
				String device_sn = "";
				String operate_type = "";
				String update_time = "";
				String inft_result = "";
				String bind_result = "";
				String serv_account = "";
				String device_id = "";
				String device_serialnumber = "";
				String bind_time = "";
				try
				{
					city_id = rs.getString("city_id");
					user_info = rs.getString("user_info");
					device_sn = rs.getString("device_sn");
					operate_type = rs.getString("operate_type");
					update_time = rs.getString("update_time");
					bind_time = rs.getString("bind_time");
					inft_result = rs.getString("inft_result");
					bind_result = rs.getString("bind_result");
					serv_account = rs.getString("serv_account");
					device_id = rs.getString("device_id");
					device_serialnumber = rs.getString("device_serialnumber");
				}
				catch (Exception e)
				{
					logger.debug(e.getMessage());
					e.printStackTrace();
				}

				String operate_type_name="";
				switch(Integer.parseInt(operate_type)){
					case 1:
						operate_type_name = "绑定";
						break;
					case 2:
						operate_type_name = "解绑";
						break;
				}

				switch(Integer.parseInt(inft_result)){
					case -1:
						inft_result = "失败";
						break;
					case 1:
						inft_result = "成功";
						break;
					case 2:
						inft_result = "用户不存在";
						break;
					case 3:
						inft_result = "设备不存在";
						break;
					default:
						inft_result = "";
						break;
				}

				switch(Integer.parseInt(bind_result)){
					case -1000:
						bind_result = "未做";
						break;
					case 1:
						bind_result = "成功";
						break;
					default:
						bind_result = "失败";
						break;
				}
				
				map.put("city_name", CityDAO.getCityName(city_id));
				map.put("user_info", user_info);
				map.put("device_sn", device_sn);
				map.put("operate_type", operate_type_name);
				map.put("update_time",new DateTimeUtil(Long.valueOf(update_time+"000")).getLongDate());
				if(bind_time!=null){
					map.put("bind_time",new DateTimeUtil(Long.valueOf(bind_time+"000")).getLongDate());
				}else{
					map.put("bind_time","");
				}
				map.put("inft_result", inft_result);
				map.put("bind_result", bind_result);
				map.put("serv_account", serv_account);
				map.put("device_id", device_id);
				map.put("device_serialnumber", device_serialnumber);
				return map;
			}
		});
	}
	
	
	public String getFailMsg(String user_info,String device_sn,
			String service_type,String operate_type,String devsn)
	{
		PrepareSQL psql=new PrepareSQL();
		PrepareSQL sqlp=new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		
		String sql = "";
		//  1:该账号不存在  2:请零配绑定设备  3:设备没有上报
		if(!StringUtil.IsEmpty(user_info)){
			if(DBUtil.GetDB()==3){
				sql = "select count(*) from stb_tab_customer where serv_account='"+user_info+"'";
			}else{
				sql = "select count(1) from stb_tab_customer where serv_account='"+user_info+"'";
			}
			
			sqlp.setSQL(sql);
			if(jt.queryForInt(sqlp.getSQL()) == 0){
				return "1";
			}
			
			psql.append(" from  stb_tab_gw_device b right join  (select a.device_sn from tab_zeroconfig_report a,");
			psql.append(" stb_tab_customer b where a.user_info=b.serv_account ");
			psql.append(" and a.user_info='"+user_info+"'");
			if(!StringUtil.IsEmpty(devsn)){
				psql.append(" and a.device_sn='" + devsn + "' ");
			}
			psql.append(") temptable on b.cpe_mac=temptable.device_sn");
			
			if(jt.queryForInt(psql.getSQL()) == 0){
				return "2";
			}
		}else {
			if(DBUtil.GetDB()==3){
				sql = "select count(*) from stb_tab_gw_device where 1=1 ";
			}else{
				sql = "select count(1) from stb_tab_gw_device where 1=1 ";
			}
			
			if(!StringUtil.IsEmpty(device_sn)){
				sql += " and device_serialnumber='"+device_sn+"' ";
			}
			if(!StringUtil.IsEmpty(devsn)){
				sql += " and cpe_mac='"+devsn+"' ";
			}
			sqlp.setSQL(sql);
			if(jt.queryForInt(sqlp.getSQL()) == 0){
				return "3";
			}
			
			if(!StringUtil.IsEmpty(device_sn)){
				psql.append(" from tab_zeroconfig_report a right join  "
						+ "(select cpe_mac from stb_tab_gw_device b where "
						+ "b.device_serialnumber='"+device_sn+"' "); 
				if(!StringUtil.IsEmpty(devsn)){
					psql.append(" and b.cpe_mac='" + devsn + "' ");
				}
				psql.append(") c on  a.device_sn=c.cpe_mac ");
				if(jt.queryForInt(psql.getSQL()) == 0){
					return "2";
				}
			}else{
				psql.append("from tab_zeroconfig_report a ");
				psql.append("where 1=1 ");
				if(!StringUtil.IsEmpty(devsn)){
					psql.append(" and a.device_sn='" + devsn + "' ");
				}
				if(jt.queryForInt(psql.getSQL()) == 0){
					return "2";
				}
			}
			
		}
		
		return "";
	}
	
	
	public int getCount(int num_splitPage,String user_info,String device_sn,
			String service_type,String operate_type,
			String cityId,String starttime,String endtime,String devsn)
	{
		PrepareSQL psql=new PrepareSQL();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}

		//	家庭网关用户表：tab_hgwcustomer  企业网关用户表：tab_egwcustomer 机顶盒用户表 stb_tab_customer
		//	家庭网关、企业网关 设备表：tab_gw_device  机顶盒设备表：stb_tab_gw_device
		if("4".equals(service_type)){
			if(!StringUtil.IsEmpty(user_info)){
				psql.append(" from  stb_tab_gw_device b right join  (select a.device_sn from tab_zeroconfig_report a,");
				psql.append("stb_tab_customer b where a.user_info=b.serv_account ");
				psql.append(" and a.user_info='"+user_info+"'");
				if(!"-1".equals(service_type)){
					psql.append(" and a.service_type="+service_type);
				}
				if(!"-1".equals(operate_type) && StringUtil.IsEmpty(operate_type)){
					psql.append(" and a.operate_type="+operate_type);
				}
				if((!"-1".equals(cityId) || "00".equals(cityId)) && StringUtil.IsEmpty(cityId)){
					ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
					String cityStr = StringUtils.weave(cityArray,"','");
					psql.append(" and a.city_id in ('" + cityStr + "')");
				}
				if(!StringUtil.IsEmpty(starttime)){
					psql.append(" and a.update_time > "+new DateTimeUtil(starttime).getLongTime());
				}
				if(!StringUtil.IsEmpty(endtime)){
					psql.append(" and a.update_time <"+new DateTimeUtil(endtime).getLongTime());
				}
				if(!StringUtil.IsEmpty(endtime)){
					psql.append(" and a.update_time <"+new DateTimeUtil(endtime).getLongTime());
				}
				if(!StringUtil.IsEmpty(devsn)){
					psql.append(" and a.device_sn='" + devsn + "' ");
				}
				psql.append(") temptable on b.cpe_mac=temptable.device_sn");
			}else if(!StringUtil.IsEmpty(device_sn)){
				psql.append(" from tab_zeroconfig_report a right join  (select cpe_mac from stb_tab_gw_device b where b.device_serialnumber='"+device_sn+"') c on  a.device_sn=c.cpe_mac "); 
				if(!"-1".equals(service_type)){
					psql.append(" and a.service_type="+service_type);
				}
				if(!"-1".equals(operate_type) && StringUtil.IsEmpty(operate_type)){
					psql.append(" and a.operate_type="+operate_type);
				}
				if((!"-1".equals(cityId) || "00".equals(cityId)) && StringUtil.IsEmpty(cityId)){
					ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
					String cityStr = StringUtils.weave(cityArray,"','");
					psql.append(" and a.city_id in ('" + cityStr + "')");
				}
				if(!StringUtil.IsEmpty(starttime)){
					psql.append(" and a.update_time > "+new DateTimeUtil(starttime).getLongTime());
				}
				if(!StringUtil.IsEmpty(endtime)){
					psql.append(" and a.update_time <"+new DateTimeUtil(endtime).getLongTime());
				}
				if(!StringUtil.IsEmpty(devsn)){
					psql.append(" and a.device_sn='" + devsn + "' ");
				}
			}else{
				psql.append(" from  stb_tab_gw_device b right join  (select a.device_sn from tab_zeroconfig_report a,");
				psql.append("stb_tab_customer b where a.user_info=b.serv_account ");
				if(!"-1".equals(service_type)){
					psql.append(" and a.service_type="+service_type);
				}
				if(!"-1".equals(operate_type) && StringUtil.IsEmpty(operate_type)){
					psql.append(" and a.operate_type="+operate_type);
				}
				if((!"-1".equals(cityId) || "00".equals(cityId)) && StringUtil.IsEmpty(cityId)){
					ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
					String cityStr = StringUtils.weave(cityArray,"','");
					psql.append(" and a.city_id in ('" + cityStr + "')");
				}
				if(!StringUtil.IsEmpty(starttime)){
					psql.append(" and a.update_time > "+new DateTimeUtil(starttime).getLongTime());
				}
				if(!StringUtil.IsEmpty(endtime)){
					psql.append(" and a.update_time <"+new DateTimeUtil(endtime).getLongTime());
				}
				if(!StringUtil.IsEmpty(endtime)){
					psql.append(" and a.update_time <"+new DateTimeUtil(endtime).getLongTime());
				}
				if(!StringUtil.IsEmpty(devsn)){
					psql.append(" and a.device_sn='" + devsn + "' ");
				}
				psql.append(") temptable on b.cpe_mac=temptable.device_sn");
			}
		}else{
			if("3".equals(service_type)){
				psql.append("from tab_zeroconfig_report a,tab_egwcustomer b,tab_gw_device c ");
				psql.append("where a.user_info=b.username and a.device_sn=c.device_serialnumber ");
			}else{
				psql.append("from tab_zeroconfig_report a,tab_hgwcustomer b,tab_gw_device c ");
				psql.append("where a.user_info=b.username and a.device_sn=c.device_serialnumber ");
			}
			if(!StringUtil.IsEmpty(user_info)){
				psql.append(" and a.user_info='"+user_info+"'");
			}
			if(!StringUtil.IsEmpty(device_sn)){
				psql.append(" and a.device_sn='"+device_sn+"'");
			}
			if(!"-1".equals(service_type)){
				psql.append(" and a.service_type="+service_type);
			}
			if(!"-1".equals(operate_type) && StringUtil.IsEmpty(operate_type)){
				psql.append(" and a.operate_type="+operate_type);
			}
			if((!"-1".equals(cityId) || "00".equals(cityId)) && StringUtil.IsEmpty(cityId)){
				ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				String cityStr = StringUtils.weave(cityArray,"','");
				psql.append(" and a.city_id in ('" + cityStr + "')");
			}
			if(!StringUtil.IsEmpty(starttime)){
				psql.append(" and a.update_time > "+new DateTimeUtil(starttime).getLongTime());
			}
			if(!StringUtil.IsEmpty(endtime)){
				psql.append(" and a.update_time <"+new DateTimeUtil(endtime).getLongTime());
			}
			if(!StringUtil.IsEmpty(devsn)){
				psql.append(" and a.device_sn='" + devsn + "' ");
			}
		}



		int max=jt.queryForInt(psql.getSQL());
		if(max==0){
			max=1;
		}
		if(max%num_splitPage==0){
			max=max/num_splitPage;
		}else{
			max=max/num_splitPage+1;
		}
		return max;
	}
}
