
package com.linkage.module.gtms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-27
 * @category com.linkage.module.gtms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ConfigFailInfoDaoImpl extends SuperDAO implements ConfigFailInfoDao
{

	private static Logger logger = LoggerFactory.getLogger(ConfigFailInfoDaoImpl.class);


	private Map<String, String> cityMap = null;

	@Override
	public List<Map> queryfailinfo(String start_time, String end_time,
			String city_id,int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryfailinfo({},{},{},{},{})", new Object[] {
				start_time, end_time, city_id, curPage_splitPage,  num_splitPage});

		logger.warn("queryfailinfo({},{},{},{},{})", new Object[] {
				start_time, end_time, city_id, curPage_splitPage,  num_splitPage});

		StringBuffer sql = this.getSql(1, start_time, end_time);
		//sql.append("select city_id,device_serialnumber,device_id_ex,device_id,times,result_id,fault_desc,fault_reason from temp_fault_city  ");

//		if(!StringUtil.IsEmpty(start_time)){
//			sql.append(" and t.times>=").append(start_time);
//		}
//
//		if(!StringUtil.IsEmpty(end_time)){
//			sql.append(" and times<=").append(end_time);
//		}


		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append("  and t.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}

		PrepareSQL psqlQuery = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psqlQuery.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String cityName = (String) cityMap.get(rs.getString("city_id"));

						if(!StringUtil.IsEmpty(cityName)){
							map.put("city_name", cityName);
						}

						//设备类型
						map.put("device_model", rs.getString("device_model"));
						map.put("device_serialnumber", rs.getString("device_serialnumber"));
						map.put("device_id_ex", rs.getString("device_id_ex"));


						//下发时间转格式yyyy-mmdd hh:mm:ss
						try
						{
							long opertime = StringUtil.getLongValue(rs.getString("times")) * 1000L;
							DateTimeUtil dt = new DateTimeUtil(opertime);
							map.put("times", dt.getLongDate());
						}
						catch (NumberFormatException e)
						{
							map.put("times", "");
						}
						catch (Exception e)
						{
							map.put("times", "");
						}

						map.put("fault_desc", rs.getString("fault_desc"));
						map.put("fault_reason", rs.getString("fault_reason"));
						return map;
					}
				});

		logger.warn("获取列表大小："+list.size());
		cityMap = null;

		return list;
	}


	@Override
	public int countQueryfailinfo(String start_time, String end_time, String city_id,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("countQueryfailinfo({},{},{},{},{})", new Object[] {
				start_time, end_time, city_id, curPage_splitPage,  num_splitPage});
		StringBuffer sql = this.getSql(2, start_time, end_time);

//		sql.append("select count(*) from temp_fault_city where 1=1 ");
//
//		if(!StringUtil.IsEmpty(start_time)){
//			sql.append(" and times>=").append(start_time);
//		}
//
//		if(!StringUtil.IsEmpty(end_time)){
//			sql.append(" and times<=").append(end_time);
//		}

		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append("  and t.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}

		logger.warn("最大值："+total);

		return maxPage;
	}





	@Override
	public int countQueryfailinfoExcel(String start_time, String end_time, String city_id)
	{
		StringBuffer sql = this.getSql(2, start_time, end_time);
//		sql.append("select count(*) from temp_fault_city where 1=1 ");
//
//		if(!StringUtil.IsEmpty(start_time)){
//			sql.append(" and times>=").append(start_time);
//		}
//
//		if(!StringUtil.IsEmpty(end_time)){
//			sql.append(" and times<=").append(end_time);
//		}


		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append("  and t.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());


		return total;
	}





	@SuppressWarnings("unchecked")
	public List<Map> getQueryfailinfoExcel(String start_time, String end_time,
			String city_id)
	{
		StringBuffer sql = this.getSql(1, start_time, end_time);

//		sql.append("select city_id,device_serialnumber,device_id_ex,device_id,times,result_id,fault_desc,fault_reason from temp_fault_city where 1=1 ");
//
//		if(!StringUtil.IsEmpty(start_time)){
//			sql.append(" and times>=").append(start_time);
//		}
//
//		if(!StringUtil.IsEmpty(end_time)){
//			sql.append(" and times<=").append(end_time);
//		}


		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append("  and t.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}

		PrepareSQL psqlQuery = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();

		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psqlQuery.getSQL());

		if(list.size()>0){
			for(int i=0; i<list.size(); i++){
				String city_name = cityMap.get(list.get(i).get("city_id"));
				list.get(i).put("city_name", city_name);

				//设备型号
				String device_model= (String)list.get(i).get("device_model");
				list.get(i).put("device_model", device_model);

				String device_serialnumber = (String)list.get(i).get("device_serialnumber");
				list.get(i).put("device_serialnumber", device_serialnumber);

				String device_id_ex = (String)list.get(i).get("device_id_ex");
				list.get(i).put("device_id_ex", device_id_ex);


				//下发时间转格式yyyy-mmdd hh:mm:ss
				try
				{
					long opertime = StringUtil.getLongValue(list.get(i).get("times")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(opertime);
					list.get(i).put("times", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					list.get(i).put("times", "");
				}
				catch (Exception e)
				{
					list.get(i).put("times", "");
				}

				String fault_desc = (String)list.get(i).get("fault_desc");
				list.get(i).put("fault_desc", fault_desc);

				String fault_reason = (String)list.get(i).get("fault_reason");
				list.get(i).put("fault_reason", fault_reason);
			}
		}
		return list;
	}





	private StringBuffer getSql(int type,String start_time, String end_time){

		StringBuffer sql = new StringBuffer();

		if(type==1){// TODO wait (more table related)

			sql.append("select t.city_id,t.device_model,t.device_serialnumber,t.device_id_ex,t.device_id,t.times,t.result_id,t.fault_desc,t.fault_reason from ");
			sql.append(" (select b.city_id,e.device_model,b.device_serialnumber,b.device_id_ex,a.*,c.result_id,d.fault_desc,d.fault_reason from ");
			sql.append(" (select a.device_id,b.times from gw_serv_strategy_log  a, ");
			sql.append(" (select device_id,min(time) times from gw_serv_strategy_log ");
			sql.append(" where device_serialnumber!=null ");
			sql.append(" and time>=").append(start_time);
			sql.append(" and time<=").append(end_time);
			sql.append(" and result_id not in (0,-1) and service_id=1401 group by device_id) b");
			sql.append(" where a.device_id=b.device_id and a.time=b.times and a.result_id!=1 and a.service_id=1401 and a.device_serialnumber!=null");
			sql.append(" and time>=").append(start_time);
			sql.append(" and time<=").append(end_time);
			sql.append(" and a.result_id not in (0,-1)) a, ");
			sql.append(" tab_gw_device b,gw_serv_strategy_log c,tab_cpe_faultcode d,gw_device_model e");
			sql.append(" where a.device_id=b.device_id and b.device_type!='e8-b' and a.device_id=c.device_id and a.times=c.time and c.result_id=d.fault_code and b.device_model_id=e.device_model_id) t where 1=1");


		}else if(type==2){// TODO wait (more table related)
			sql.append("select count(*) from ");
			sql.append(" (select b.city_id,b.device_serialnumber,b.device_id_ex,a.*,c.result_id,d.fault_desc,d.fault_reason from ");
			sql.append(" (select a.device_id,b.times from gw_serv_strategy_log  a, ");
			sql.append(" (select device_id,min(time) times from gw_serv_strategy_log ");
			sql.append(" where device_serialnumber!=null ");
			sql.append(" and time>=").append(start_time);
			sql.append(" and time<=").append(end_time);
			sql.append(" and result_id not in (0,-1) and service_id=1401 group by device_id) b");
			sql.append(" where a.device_id=b.device_id and a.time=b.times and a.result_id!=1 and a.service_id=1401 and a.device_serialnumber!=null");
			sql.append(" and time>=").append(start_time);
			sql.append(" and time<=").append(end_time);
			sql.append(" and a.result_id not in (0,-1)) a, ");
			sql.append(" tab_gw_device b,gw_serv_strategy_log c,tab_cpe_faultcode d");
			sql.append(" where a.device_id=b.device_id and b.device_type!='e8-b' and a.device_id=c.device_id and a.times=c.time and c.result_id=d.fault_code) t where 1=1");
		}

		return sql;
	}




	/**
	 * 算法定义
	 */
	private void collectDataFail(String start_time, String end_time){

		String sqlFlag1 = "select TABLE_NAME  from user_tables where TABLE_NAME='temp_hujg_str3'";
		String sqlFlag2 = "select TABLE_NAME  from user_tables where TABLE_NAME='temp_hujg_str4'";
		String sqlFlag3 = "select TABLE_NAME  from user_tables where TABLE_NAME='temp_fault_city'";


		PrepareSQL ps1 = new PrepareSQL(sqlFlag1);
		int num1 = jt.queryForInt(ps1.getSQL());

		PrepareSQL ps2 = new PrepareSQL(sqlFlag2);
		int num2 = jt.queryForInt(ps2.getSQL());

		PrepareSQL ps3 = new PrepareSQL(sqlFlag3);
		int num3 = jt.queryForInt(ps3.getSQL());

		if(num1==1 && num2==1 && num3==1 ){
			String sql1 = "drop  table temp_hujg_str3 ";
			String sql2 = "drop  table temp_hujg_str4 ";
			String sql3 = "drop  table temp_fault_city ";

			PrepareSQL psql1 = new PrepareSQL(sql1);
			PrepareSQL psql2 = new PrepareSQL(sql2);
			PrepareSQL psql3 = new PrepareSQL(sql3);

			jt.execute(psql1.getSQL());
			jt.execute(psql2.getSQL());
			jt.execute(psql3.getSQL());
		}

		String sqlCount = "";
		if(DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sqlCount = "create table temp_hujg_str3 select device_id,min(time) times ";
		}else{
			sqlCount = "select device_id,min(time) times into temp_hujg_str3 ";
		}
		sqlCount += " from gw_serv_strategy_log"
				+"  where device_serialnumber!=null"
				+"  and time>="+start_time
				+"  and time<="+end_time
				+"  and result_id not in (0,-1) "
				+"  and service_id=1401"
				+"  group by device_id";
		PrepareSQL psql = new PrepareSQL(sqlCount);
		jt.queryForInt(psql.getSQL());

		String sqlDevice = "";
		if(DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sqlDevice = "create table temp_hujg_str4 select a.device_id,b.times ";
		}else{
			sqlDevice = "select a.device_id,b.times into temp_hujg_str4 ";
		}
		sqlDevice += " from gw_serv_strategy_log a,temp_hujg_str3 b"
							+"  where a.device_id=b.device_id"
							+"  and a.time=b.times"
							+"  and a.result_id!=1"
							+"  and a.service_id=1401"
							+"  and a.device_serialnumber!=null"
							+"  and time>="+start_time
							+"  and time<="+end_time
							+"  and a.result_id not in (0,-1)";
		PrepareSQL psql2 = new PrepareSQL(sqlDevice);
		jt.queryForInt(psql2.getSQL());

		String sqlInfo = "";
		if(DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sqlInfo = "create table temp_fault_city select b.city_id,b.device_serialnumber,b.device_id_ex,a.*,c.result_id,d.fault_desc,d.fault_reason ";
		}else{
			sqlInfo = "select b.city_id,b.device_serialnumber,b.device_id_ex,a.*,c.result_id,d.fault_desc,d.fault_reason into temp_fault_city ";
		}// TODO wait (more table related)
		sqlInfo += " from temp_hujg_str4 a,tab_gw_device b,gw_serv_strategy_log c,tab_cpe_faultcode d"
					+"  where a.device_id=b.device_id"
					+"  and b.device_type!='e8-b'"
					+"  and a.device_id=c.device_id"
					+"  and a.times=c.time"
					+"  and c.result_id=d.fault_code";

		PrepareSQL psql3 = new PrepareSQL(sqlInfo);
		jt.queryForInt(psql3.getSQL());
	}





}
