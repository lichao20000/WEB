package com.linkage.module.gtms.itv.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.system.utils.database.DBUtil;

public class FirstPageDAOImp extends SuperDAO implements FirstPageDAO
{
	private static final Logger LOG = Logger.getLogger(FirstPageDAOImp.class);

	@Override
	public List<Map<String,String>> getEPGReport(String interval,String cityID)
	{
		// TODO Auto-generated method stub
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		if(WEEKLY_INTERVAL.equals(interval))
		{
			List<String> dateList = getDateList(7);
			if(null != dateList && dateList.size()>0)
			{
				returnList = getDBEPGReport(dateList,cityID);
			}
		}
		return returnList;
	}

	@Override
	public List<Map<String,String>> getNFReport(String interval)
	{
		// TODO Auto-generated method stub
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		if(WEEKLY_INTERVAL.equals(interval))
		{
			List<String> dateList = getDateList(7);
			if(null != dateList && dateList.size()>0)
			{
				returnList = getDBNfReport(dateList);
			}
		}
		return returnList;
	}

	/**
	 * 根据时间list查询对应日期在数据库中是否有记录
	 * @param dateList
	 * @return
	 */
	private List<Map<String,String>> getDBNfReport(List<String> dateList)
	{
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		if(null != dateList && dateList.size() > 0)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("select reportdate,err_count from tab_akazam_nf_daily where reportdate in(");
			final int size = dateList.size();
			for(int i=0;i<size;i++)
			{
				if(i == size-1)
				{
					sb.append("'").append(dateList.get(i)).append("')");
				}
				else
				{
					sb.append("'").append(dateList.get(i)).append("',");
				}
			}
			LOG.info(sb.toString());
			returnList = jt.queryForList(sb.toString());
		}
		else
		{
			LOG.info("查询科升日报表的日期为空");
		}
		return returnList;
	}

	/**
	 * 根据时间list查询对应日期在数据库中是否有记录
	 * @param dateList
	 * @return
	 */
	private List<Map<String,String>> getDBEPGReport(List<String> dateList,String cityID)
	{
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		if(null != dateList && dateList.size() > 0)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("select e.reportdate,err_count,city from tab_akazam_epg_daily e where reportdate in(");
			final int size = dateList.size();
			for(int i=0;i<size;i++)
			{
				if(i == size-1)
				{
					sb.append("'").append(dateList.get(i)).append("')");
				}
				else
				{
					sb.append("'").append(dateList.get(i)).append("',");
				}
			}
			if(null != cityID && !"".equals(cityID) && !"00".equals(cityID))
			{
				PrepareSQL psql = new PrepareSQL("select c.city_name,c.parent_id,d.city_name as parent_name from tab_city c,tab_city d where c.parent_id = d.city_id and c.city_id ='" + cityID + "'");
				Map cityMap = jt.queryForMap(psql.getSQL());
				String parent_id = cityMap.get("parent_id").toString();
				if("00".equals(parent_id))
				{
					sb.append(" and city like '%").append(cityMap.get("city_name").toString()).append("%'");
				}
				else
				{
					sb.append(" and city like '%").append(cityMap.get("parent_name").toString()).append("%'");
				}
			}
			LOG.info(sb.toString());
			returnList = jt.queryForList(sb.toString());
		}
		else
		{
			LOG.info("查询科升日报表的日期为空");
		}
		return returnList;
	}

	/**
	 * 根据当前时间获取前多少天日期的list
	 * @param interval
	 * @return
	 */
	private  List<String> getDateList(int interval)
	{
		List<String> returnList = new ArrayList<String>();
		try
		{
			Calendar c = null;
			SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
			for(int i=1;i<=interval;i++)
			{
				c = Calendar.getInstance();
				c.set(Calendar.DAY_OF_YEAR,c.get(Calendar.DAY_OF_YEAR)-i);
				returnList.add(sdf.format(c.getTime()));
			}
		} catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			LOG.warn("分析当前时间出错", e);
		}
		return returnList;
	}


	@Override
	public List<Map<String, String>> getServiceUser(String interval,
			String cityID)
	{
		// TODO Auto-generated method stub
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		if(DAILY_INTERVAL.equals(interval))
		{
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_YEAR,c.get(Calendar.DAY_OF_YEAR)-1);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			long endTime = c.getTimeInMillis()/1000;
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			long startTime = c.getTimeInMillis()/1000;
			StringBuffer sql = new StringBuffer();
			if(null != cityID && !"".equals(cityID) && !"00".equals(cityID))
			{
				PrepareSQL psql = new PrepareSQL("select c.city_name,c.city_id,c.parent_id,d.city_name as parent_name from tab_city c,tab_city d where c.parent_id = d.city_id and c.city_id ='" + cityID + "'");
				Map cityMap = jt.queryForMap(psql.getSQL());
				String parent_id = cityMap.get("parent_id").toString();
				if("00".equals(parent_id))
				{
					sql.append(" where city_id in ('").append(cityMap.get("city_id").toString()).append("') ");
				}
				else
				{
					sql.append(" where city_id in ('").append(parent_id).append("') ");
				}
			}
			else
			{
				sql.append(" where city_id in ('00','0100','0200','0300','0400','0500','0600','0700','0800','0900','1000','1100','1200','1300') ");
			}
			sql.append(" and gather_time>=?").append(" and gather_time<=?").append(" order by city_id");
			sql.insert(0, "select  city_id,gather_time,onlinenum,vgoodnum,goodnum,nornum,badnum  from tab_city_quality ");
			PrepareSQL sql1= new PrepareSQL();
			sql1.setSQL(sql.toString());
			sql1.setLong(1, startTime);
			sql1.setLong(2, endTime);
			returnList = jt.queryForList(sql1.getSQL());
			if(null == returnList || returnList.size()==0)
			{
				c.set(Calendar.DAY_OF_YEAR,c.get(Calendar.DAY_OF_YEAR)-2);
				c.set(Calendar.HOUR_OF_DAY, 23);
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				long endTime2 = c.getTimeInMillis()/1000;
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				long startTime2 = c.getTimeInMillis()/1000;
				sql1.setSQL(sql.toString());
				sql1.setLong(1, startTime2);
				sql1.setLong(2, endTime2);
				returnList = jt.queryForList(sql1.getSQL());
			}
		}
		return returnList;
	}

	@Override
	public List<Map<String, String>> getWarnReport(String interval,
			String cityID)
	{
		// TODO Auto-generated method stub
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		if(WEEKLY_INTERVAL.equals(interval))
		{
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 23);
			long endTime = c.getTimeInMillis()/1000;
			c.set(Calendar.DAY_OF_YEAR,c.get(Calendar.DAY_OF_YEAR)-7);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			long startTime = c.getTimeInMillis()/1000;
			List<String> tableList = getExistStatWarnTables(startTime,endTime);
			StringBuffer sql = new StringBuffer();
			String queryPre = " select severity,serialno from ";
			StringBuffer queryEnd = new StringBuffer();
			queryEnd.append(" where creatorname='FSMP' and createtime >=").append(startTime).append(" and createtime<=").append(endTime);
			//queryEnd.append(" where creatorname='FSMP' ");
			for(int i=0;i<tableList.size();i++)
			{
				sql.append(queryPre).append(tableList.get(i)).append(queryEnd.toString());
				if(i != tableList.size()-1)
				{
					sql.append(" union ");
				}
			}
			sql.insert(0, "select severity,count(*) as countnum from (");
			sql.append(") a group by severity order by severity ");
			PrepareSQL psql = new PrepareSQL(sql.toString());
			returnList = jt.queryForList(psql.getSQL());
			//returnList = jt.queryForList("select severity,count(*) as countnum from ( select severity,serialno  from tab_hisstatwarn_2010_43 where creatorname='FSMP' ) a group by severity");
		}
		return returnList;
	}

	public Map getCityMap()
	{
		Map cityMap = new HashMap();
		PrepareSQL psql = new PrepareSQL("select city_id,city_name from tab_city ");
		List list = jt.queryForList(psql.getSQL());
		if(null != list && list.size()>0)
		{
			for(Object o: list)
			{
				Map m = (Map)o;
				cityMap.put(m.get("city_id").toString(), m.get("city_name").toString());
			}
		}
		return cityMap;
	}

	/**
	 * 获取tab_realstatwarn,tab_hisstatwarn_yyyy_w表 (列表中的顺序为表的创建时间升序)
	 *
	 * @param startTime
	 *            查询开始时间(秒)
	 * @param endTime
	 *            查询结束时间(秒)
	 * @return
	 */
	private List<String> getExistStatWarnTables(long startTime, long endTime)
	{
		List<String> resultList = new ArrayList<String>();
		// 分析查询范围内的表
		List<String> tableList = DBUtil.createTableNames(startTime * 1000L,
				endTime * 1000L, Calendar.WEEK_OF_YEAR, 1, "tab_hisstatwarn_", "yyyy_w");
		// 找到存在的表
		List<String> existTableList = DBUtil.getExistTables(tableList);
		Set<String> existTableSet = new HashSet<String>();
		resultList.add("tab_realstatwarn");
		for (String existTable : existTableList)
		{
			existTableSet.add(existTable.toLowerCase());
		}
		for (String table : tableList)
		{
			if (existTableSet.contains(table))
			{
				resultList.add(table);
			}
		}
		LOG.info("数据库中存在的告警表:" + resultList);
		return resultList;
	}

	private Map getThisWeek()
	{
		Date myDate = new Date();

		int hour = myDate.getHours();
		int minute = myDate.getMinutes();
		int second = myDate.getSeconds();
		int allsecond = hour * 60 * 60 + minute * 60 + second;// 当天已度过的秒数
		//int oneDay = 24 * 60 * 60;// 一天的秒数

		int thisweekday=myDate.getDay();//当前周几

		System.out.print("当前是周："+thisweekday);

		long dateLong = myDate.getTime() / 1000 - allsecond;
		long thisbegin=0;//本周开始
		long thisend=0;//本周结束
		long last7begin=0;//上周开始
		long last7end=0;//上周结束
		long last14begin=0;//上两周开始
		long last14end=0;//上两周结束
		long last21begin=0;//上三周开始
		long last21end=0;//上三周结束

	System.out.println(dateLong);

		switch(thisweekday)
		{
		case 1:last7begin=dateLong-86400*7;last7end=dateLong;last14begin=last7begin-86400*7;last14end=last7begin;last21begin=last14begin-86400*7;last21end=last14begin;break;
		case 2:thisbegin=dateLong-86400;thisend=dateLong;last7begin=thisbegin-86400*7;last7end=thisbegin;last14begin=last7begin-86400*7;last14end=last7begin;last21begin=last14begin-86400*7;last21end=last14begin;break;
		case 3:thisbegin=dateLong-86400*2;thisend=dateLong;last7begin=thisbegin-86400*7;last7end=thisbegin;last14begin=last7begin-86400*7;last14end=last7begin;last21begin=last14begin-86400*7;last21end=last14begin;break;
		case 4:thisbegin=dateLong-86400*3;thisend=dateLong;last7begin=thisbegin-86400*7;last7end=thisbegin;last14begin=last7begin-86400*7;last14end=last7begin;last21begin=last14begin-86400*7;last21end=last14begin;break;
		case 5:thisbegin=dateLong-86400*4;thisend=dateLong;last7begin=thisbegin-86400*7;last7end=thisbegin;last14begin=last7begin-86400*7;last14end=last7begin;last21begin=last14begin-86400*7;last21end=last14begin;break;
		case 6:thisbegin=dateLong-86400*5;thisend=dateLong;last7begin=thisbegin-86400*7;last7end=thisbegin;last14begin=last7begin-86400*7;last14end=last7begin;last21begin=last14begin-86400*7;last21end=last14begin;break;
		case 0:thisbegin=dateLong-86400*6;thisend=dateLong;last7begin=thisbegin-86400*7;last7end=thisbegin;last14begin=last7begin-86400*7;last14end=last7begin;last21begin=last14begin-86400*7;last21end=last14begin;break;
		}
		Map<String, Long> hm=new HashMap<String, Long>();
		hm.put("bz0", thisbegin*1000);
		hm.put("bz1", thisend*1000);
		hm.put("sz0", last7begin*1000);
		hm.put("sz1", last7end*1000);
		hm.put("slz0", last14begin*1000);
		hm.put("slz1", last14end*1000);
		hm.put("ssz0", last21begin*1000);
		hm.put("ssz1", last21end*1000);
		return hm;
	}

	@Override
	public List<Map<String, String>> getDatainputReport()
	{
		Map hm=this.getThisWeek();
		long startTime=Long.parseLong(hm.get("bz0").toString());
		long endTime=Long.parseLong(hm.get("bz1").toString());
		//List<String> tableNameList = DBUtil.createTableNames(startTime,endTime, Calendar.WEEK_OF_YEAR, 1, "tab_hisstatwarn_", "yyyy_w");
		List<String> tableNameList = DBUtil.getExistTableNames(startTime,endTime, Calendar.WEEK_OF_YEAR, 1, "tab_hisstatwarn_", "yyyy_w");
		if(tableNameList != null && tableNameList.size() != 0)
		{
			String tableName = tableNameList.get(0);// TODO wait (more table related)
			String sql = "select severity,count(*) from (select severity,serialno from tab_resourcetype_level rl,tab_deviceresource d," +
					"tab_realstatwarn w where resource_level =3 and rl.resource_type_id = d.resource_type_id and w.devicecoding=d.device_id " +
					"and  createtime >= "+ startTime +" and createtime<="+ endTime +" union select severity,serialno from tab_resourcetype_level rl,tab_deviceresource d," +
					""+ tableName +" w where resource_level =3 and rl.resource_type_id = d.resource_type_id and w.devicecoding=d.device_id " +
					"and  createtime >= "+ startTime +" and createtime<="+ endTime +" ) a group by severity order by severity";
			LOG.info("查询专用数据设备告警==sql"+sql);
			List<Map<String, String>> list = jt.queryForList(sql);
				return list;
		}else
		{// TODO wait (more table related)
			String sql = "select severity,count(*) from (select severity,serialno from tab_resourcetype_level rl,tab_deviceresource d," +
					"tab_realstatwarn w where resource_level =3 and rl.resource_type_id = d.resource_type_id and w.devicecoding=d.device_id " +
					"and  createtime >= "+ startTime +" and createtime<="+ endTime +" ) a group by severity order by severity";
			LOG.info("查询专用数据设备告警==sql"+sql);
			List<Map<String, String>> list = jt.queryForList(sql);
				return list;
		}
	}

	@Override
	public List<Map<String, String>> getTownReport()
	{
		Map hm=this.getThisWeek();
		long startTime=Long.parseLong(hm.get("bz0").toString());
		long endTime=Long.parseLong(hm.get("bz1").toString());
		//List<String> tableNameList = DBUtil.createTableNames(startTime,endTime, Calendar.WEEK_OF_YEAR, 1, "tab_hisstatwarn_", "yyyy_w");
		List<String> tableNameList = DBUtil.getExistTableNames(startTime,endTime, Calendar.WEEK_OF_YEAR, 1, "tab_hisstatwarn_", "yyyy_w");
		if(tableNameList != null && tableNameList.size() != 0)
		{
			String tableName = tableNameList.get(0);// TODO wait (more table related)
			String sql = "select severity,count(*) from (select severity,serialno from tab_resourcetype_level rl,tab_deviceresource d," +
					"tab_realstatwarn w where resource_level =2 and rl.resource_type_id = d.resource_type_id and w.devicecoding=d.device_id " +
					"and  createtime >= "+ startTime +" and createtime<="+ endTime +" union select severity,serialno from tab_resourcetype_level rl,tab_deviceresource d," +
					""+ tableName +" w where resource_level =2 and rl.resource_type_id = d.resource_type_id and w.devicecoding=d.device_id " +
					"and  createtime >= "+ startTime +" and createtime<="+ endTime +" ) a group by severity order by severity";
			LOG.info("查询城域网告警==sql"+sql);
			List<Map<String, String>> list = jt.queryForList(sql);
			return list;
		}else
		{// TODO wait (more table related)
			String sql = "select severity,count(*) from (select severity,serialno from tab_resourcetype_level rl,tab_deviceresource d," +
					"tab_realstatwarn w where resource_level =2 and rl.resource_type_id = d.resource_type_id and w.devicecoding=d.device_id " +
					"and  createtime >= "+ startTime +" and createtime<="+ endTime +" ) a group by severity order by severity";
			LOG.info("查询城域网告警==sql"+sql);
			List<Map<String, String>> list = jt.queryForList(sql);
			return list;
		}
	}
}
