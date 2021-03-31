package com.linkage.module.gtms.itv.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.litms.common.database.PrepareSQL;
import org.apache.log4j.Logger;


import com.linkage.module.gtms.itv.bio.WeekTime;
import com.linkage.module.gwms.dao.SuperDAO;

public class ISAPicDAOImp extends SuperDAO implements ISAPicDAO{

	private static final Logger LOG = Logger.getLogger(ISAPicDAOImp.class);

	@Override
	public List<Map> getISATerminalReport(String cityName)
	{
		System.out.println("属地名称："+cityName);
		Map hm=WeekTime.getThisWeek();
		String thisbeginDate=hm.get("bz0").toString();
		String thisendDate=hm.get("bz1").toString();
		String last7beginDate=hm.get("sz0").toString();
		String last7endDate=hm.get("sz1").toString();
		String last14beginDate=hm.get("slz0").toString();
		String last14endDate=hm.get("slz1").toString();
		String last21beginDate=hm.get("ssz0").toString();
		String last21endDate=hm.get("ssz1").toString();

		String gatherId_sql="";
		if(!"省中心".equals(cityName))
		{
			gatherId_sql=" location='"+cityName+"' and ";
		}
		else
		{
			gatherId_sql=" location !='江苏省' and ";
		}

		String sql0 = "select sum(severityLevel6UserCount) as severityLevel6UserCount,sum(severityLevel5UserCount) " +
				"as severityLevel5UserCount,sum(severityLevel4UserCount) as severityLevel4UserCount,sum(severityLevel3UserCount)" +
				" as severityLevel3UserCount,reportType from RptStbOutageDistrictBo where 1=1 and "+gatherId_sql+" startTime>='"+thisbeginDate+"'" +
						" and startTime<'"+thisendDate+"' and reportType=3 group by reportType";

		String sql1 = "select sum(severityLevel6UserCount) as severityLevel6UserCount,sum(severityLevel5UserCount) " +
		"as severityLevel5UserCount,sum(severityLevel4UserCount) as severityLevel4UserCount,sum(severityLevel3UserCount)" +
		" as severityLevel3UserCount,reportType from RptStbOutageDistrictBo where 1=1 and "+gatherId_sql+"  startTime>='"+last7beginDate+"'" +
				" and startTime<'"+last7endDate+"' and reportType=3 group by reportType";

		String sql2 = "select sum(severityLevel6UserCount) as severityLevel6UserCount,sum(severityLevel5UserCount) " +
		"as severityLevel5UserCount,sum(severityLevel4UserCount) as severityLevel4UserCount,sum(severityLevel3UserCount)" +
		" as severityLevel3UserCount,reportType from RptStbOutageDistrictBo where 1=1 and "+gatherId_sql+"  startTime>='"+last14beginDate+"'" +
				" and startTime<'"+last14endDate+"' and reportType=3 group by reportType";

		String sql3 = "select sum(severityLevel6UserCount) as severityLevel6UserCount,sum(severityLevel5UserCount) " +
		"as severityLevel5UserCount,sum(severityLevel4UserCount) as severityLevel4UserCount,sum(severityLevel3UserCount)" +
		" as severityLevel3UserCount,reportType from RptStbOutageDistrictBo where 1=1 and "+gatherId_sql+"  startTime>='"+last21beginDate+"'" +
				" and startTime<'"+last21endDate+"' and reportType=3 group by reportType";

		PrepareSQL psql0 = new PrepareSQL(sql0);
		PrepareSQL psql1 = new PrepareSQL(sql1);
		PrepareSQL psql2 = new PrepareSQL(sql2);
		PrepareSQL psql3 = new PrepareSQL(sql3);

		List<Map> list0 = jt.queryForList(psql0.getSQL());
		List<Map> list1 = jt.queryForList(psql1.getSQL());
		List<Map> list2 = jt.queryForList(psql2.getSQL());
		List<Map> list3 = jt.queryForList(psql3.getSQL());


		List<Map> list = new ArrayList();
		Map mp;

		if(list0==null || list0.size()==0)
		{
			System.out.print("本周暂无数据0");
			    mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","三级");
				mp.put("data","0");
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","二级");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list0.size();i++)
		{
			    mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","非常严重");
				mp.put("data",list0.get(i).get("severityLevel6UserCount").toString());
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","严重");
				mp.put("data",list0.get(i).get("severityLevel5UserCount").toString());
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","三级");
				mp.put("data",list0.get(i).get("severityLevel4UserCount").toString());
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","二级");
				mp.put("data",list0.get(i).get("severityLevel3UserCount").toString());
				list.add(mp);

		}
		}

		if(list1==null ||list1.size()==0)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","三级");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","二级");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list1.size();i++)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","非常严重");
				mp.put("data",list1.get(i).get("severityLevel6UserCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","严重");
				mp.put("data",list1.get(i).get("severityLevel5UserCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","三级");
				mp.put("data",list1.get(i).get("severityLevel4UserCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","二级");
				mp.put("data",list1.get(i).get("severityLevel3UserCount").toString());
				list.add(mp);
		}
		}

		if(list2==null || list2.size()==0)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","三级");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","二级");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list2.size();i++)
		{

			    mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","非常严重");
				mp.put("data",list2.get(i).get("severityLevel6UserCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","严重");
				mp.put("data",list2.get(i).get("severityLevel5UserCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","三级");
				mp.put("data",list2.get(i).get("severityLevel4UserCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","二级");
				mp.put("data",list2.get(i).get("severityLevel3UserCount").toString());
				list.add(mp);
		}
		}

		if(list3==null || list3.size()==0)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","三级");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","二级");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list3.size();i++)
		{

			    mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","非常严重");
				mp.put("data",list3.get(i).get("severityLevel6UserCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","严重");
				mp.put("data",list3.get(i).get("severityLevel5UserCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","三级");
				mp.put("data",list3.get(i).get("severityLevel4UserCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","二级");
				mp.put("data",list3.get(i).get("severityLevel3UserCount").toString());
				list.add(mp);
		}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	//频道自己
	public List<Map> getISAEPGReport(String cityName)
	{
		System.out.println("属地名称："+cityName);
		Map hm=WeekTime.getThisWeek();
		String thisbeginDate=hm.get("bz0").toString();
		String thisendDate=hm.get("bz1").toString();
		String last7beginDate=hm.get("sz0").toString();
		String last7endDate=hm.get("sz1").toString();
		String last14beginDate=hm.get("slz0").toString();
		String last14endDate=hm.get("slz1").toString();
		String last21beginDate=hm.get("ssz0").toString();
		String last21endDate=hm.get("ssz1").toString();

		String gatherId_sql="";
		if(!"省中心".equals(cityName))
		{
			gatherId_sql=" location ='"+cityName+"' and ";
		}
		//else
		//{
		//	gatherId_sql=" location !='江苏省' and ";
		//}
		String sql0 = "select sum(severityLevel6Count) as severityLevel6Count,sum(severityLevel5Count) " +
				"as severityLevel5Count,sum(severityLevel4Count) as severityLevel4Count,sum(severityLevel3Count)" +
				" as severityLevel3Count,reportType from RptChnOutageBo where 1=1 and "+gatherId_sql+" startTime>='"+thisbeginDate+"'" +
						" and startTime<'"+thisendDate+"' and reportType=3 group by reportType";

		String sql1 = "select sum(severityLevel6Count) as severityLevel6Count,sum(severityLevel5Count) " +
		"as severityLevel5Count,sum(severityLevel4Count) as severityLevel4Count,sum(severityLevel3Count)" +
		" as severityLevel3Count,reportType from RptChnOutageBo where 1=1 and "+gatherId_sql+" startTime>='"+last7beginDate+"'" +
				" and startTime<'"+last7endDate+"' and reportType=3 group by reportType";

		String sql2 = "select sum(severityLevel6Count) as severityLevel6Count,sum(severityLevel5Count) " +
		"as severityLevel5Count,sum(severityLevel4Count) as severityLevel4Count,sum(severityLevel3Count)" +
		" as severityLevel3Count,reportType from RptChnOutageBo where 1=1 and "+gatherId_sql+" startTime>='"+last14beginDate+"'" +
				" and startTime<'"+last14endDate+"' and reportType=3 group by reportType";

		String sql3 = "select sum(severityLevel6Count) as severityLevel6Count,sum(severityLevel5Count) " +
		"as severityLevel5Count,sum(severityLevel4Count) as severityLevel4Count,sum(severityLevel3Count)" +
		" as severityLevel3Count,reportType from RptChnOutageBo where 1=1 and "+gatherId_sql+" startTime>='"+last21beginDate+"'" +
				" and startTime<'"+last21endDate+"' and reportType=3 group by reportType";

		PrepareSQL psql0 = new PrepareSQL(sql0);
		PrepareSQL psql1 = new PrepareSQL(sql1);
		PrepareSQL psql2 = new PrepareSQL(sql2);
		PrepareSQL psql3 = new PrepareSQL(sql3);

		List<Map> list0 = jt.queryForList(psql0.getSQL());
		List<Map> list1 = jt.queryForList(psql1.getSQL());
		List<Map> list2 = jt.queryForList(psql2.getSQL());
		List<Map> list3 = jt.queryForList(psql3.getSQL());

		//System.out.println("呵呵："+"你妈:"+list1.size()+":"+list2.size()+":"+list3.size());


		List<Map> list = new ArrayList();
		Map mp;

		if(list0==null || list0.size()==0)
		{
			System.out.print("本周暂无数据1");
			    mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","三级");
				mp.put("data","0");
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","二级");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list0.size();i++)
		{
			    mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","非常严重");
				mp.put("data",list0.get(i).get("severityLevel6Count").toString());
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","严重");
				mp.put("data",list0.get(i).get("severityLevel5Count").toString());
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","三级");
				mp.put("data",list0.get(i).get("severityLevel4Count").toString());
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","二级");
				mp.put("data",list0.get(i).get("severityLevel3Count").toString());
				list.add(mp);

		}
		}

		if(list1==null ||list1.size()==0)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","三级");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","二级");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list1.size();i++)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","非常严重");
				mp.put("data",list1.get(i).get("severityLevel6Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","严重");
				mp.put("data",list1.get(i).get("severityLevel5Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","三级");
				mp.put("data",list1.get(i).get("severityLevel4Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","二级");
				mp.put("data",list1.get(i).get("severityLevel3Count").toString());
				list.add(mp);
		}
		}

		if(list2==null || list2.size()==0)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","三级");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","二级");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list2.size();i++)
		{

			    mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","非常严重");
				mp.put("data",list2.get(i).get("severityLevel6Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","严重");
				mp.put("data",list2.get(i).get("severityLevel5Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","三级");
				mp.put("data",list2.get(i).get("severityLevel4Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","二级");
				mp.put("data",list2.get(i).get("severityLevel3Count").toString());
				list.add(mp);
		}
		}

		if(list3==null || list3.size()==0)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","三级");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","二级");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list3.size();i++)
		{

			    mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","非常严重");
				mp.put("data",list3.get(i).get("severityLevel6Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","严重");
				mp.put("data",list3.get(i).get("severityLevel5Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","三级");
				mp.put("data",list3.get(i).get("severityLevel4Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","二级");
				mp.put("data",list3.get(i).get("severityLevel3Count").toString());
				list.add(mp);
		}
		}
		return list;
	}

//频道省中心的
	@SuppressWarnings("unchecked")
	public List<Map> getISAEPGReportSZX()
	{

		Map hm=WeekTime.getThisWeek();
		String thisbeginDate=hm.get("bz0").toString();
		String thisendDate=hm.get("bz1").toString();
		String last7beginDate=hm.get("sz0").toString();
		String last7endDate=hm.get("sz1").toString();
		String last14beginDate=hm.get("slz0").toString();
		String last14endDate=hm.get("slz1").toString();
		String last21beginDate=hm.get("ssz0").toString();
		String last21endDate=hm.get("ssz1").toString();



		String sql0 = "select sum(severityLevel6Count) as severityLevel6Count,sum(severityLevel5Count) " +
				"as severityLevel5Count,sum(severityLevel4Count) as severityLevel4Count,sum(severityLevel3Count)" +
				" as severityLevel3Count,reportType from RptChnOutageBo where 1=1 and location='省中心' and startTime>='"+thisbeginDate+"'" +
						" and startTime<'"+thisendDate+"' and reportType=3 group by reportType";

		String sql1 = "select sum(severityLevel6Count) as severityLevel6Count,sum(severityLevel5Count) " +
		"as severityLevel5Count,sum(severityLevel4Count) as severityLevel4Count,sum(severityLevel3Count)" +
		" as severityLevel3Count,reportType from RptChnOutageBo where 1=1 and location='省中心' and startTime>='"+last7beginDate+"'" +
				" and startTime<'"+last7endDate+"' and reportType=3 group by reportType";

		String sql2 = "select sum(severityLevel6Count) as severityLevel6Count,sum(severityLevel5Count) " +
		"as severityLevel5Count,sum(severityLevel4Count) as severityLevel4Count,sum(severityLevel3Count)" +
		" as severityLevel3Count,reportType from RptChnOutageBo where 1=1 and location='省中心' and startTime>='"+last14beginDate+"'" +
				" and startTime<'"+last14endDate+"' and reportType=3 group by reportType";

		String sql3 = "select sum(severityLevel6Count) as severityLevel6Count,sum(severityLevel5Count) " +
		"as severityLevel5Count,sum(severityLevel4Count) as severityLevel4Count,sum(severityLevel3Count)" +
		" as severityLevel3Count,reportType from RptChnOutageBo where 1=1 and location='省中心' and startTime>='"+last21beginDate+"'" +
				" and startTime<'"+last21endDate+"' and reportType=3 group by reportType";

		PrepareSQL psql0 = new PrepareSQL(sql0);
		PrepareSQL psql1 = new PrepareSQL(sql1);
		PrepareSQL psql2 = new PrepareSQL(sql2);
		PrepareSQL psql3 = new PrepareSQL(sql3);

		List<Map> list0 = jt.queryForList(psql0.getSQL());
		List<Map> list1 = jt.queryForList(psql1.getSQL());
		List<Map> list2 = jt.queryForList(psql2.getSQL());
		List<Map> list3 = jt.queryForList(psql3.getSQL());

		//System.out.println("呵呵："+"你妈:"+list1.size()+":"+list2.size()+":"+list3.size());


		List<Map> list = new ArrayList();
		Map mp;

		if(list0==null || list0.size()==0)
		{
			System.out.print("本周暂无数据1");
			    mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","三级");
				mp.put("data","0");
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","二级");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list0.size();i++)
		{
			    mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","非常严重");
				mp.put("data",list0.get(i).get("severityLevel6Count").toString());
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","严重");
				mp.put("data",list0.get(i).get("severityLevel5Count").toString());
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","三级");
				mp.put("data",list0.get(i).get("severityLevel4Count").toString());
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","二级");
				mp.put("data",list0.get(i).get("severityLevel3Count").toString());
				list.add(mp);

		}
		}

		if(list1==null ||list1.size()==0)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","三级");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","二级");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list1.size();i++)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","非常严重");
				mp.put("data",list1.get(i).get("severityLevel6Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","严重");
				mp.put("data",list1.get(i).get("severityLevel5Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","三级");
				mp.put("data",list1.get(i).get("severityLevel4Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","二级");
				mp.put("data",list1.get(i).get("severityLevel3Count").toString());
				list.add(mp);
		}
		}

		if(list2==null || list2.size()==0)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","三级");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","二级");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list2.size();i++)
		{

			    mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","非常严重");
				mp.put("data",list2.get(i).get("severityLevel6Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","严重");
				mp.put("data",list2.get(i).get("severityLevel5Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","三级");
				mp.put("data",list2.get(i).get("severityLevel4Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","二级");
				mp.put("data",list2.get(i).get("severityLevel3Count").toString());
				list.add(mp);
		}
		}

		if(list3==null || list3.size()==0)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","三级");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","二级");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list3.size();i++)
		{

			    mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","非常严重");
				mp.put("data",list3.get(i).get("severityLevel6Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","严重");
				mp.put("data",list3.get(i).get("severityLevel5Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","三级");
				mp.put("data",list3.get(i).get("severityLevel4Count").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","二级");
				mp.put("data",list3.get(i).get("severityLevel3Count").toString());
				list.add(mp);
		}
		}
		return list;
	}

	@Override
	public List<Map> getEPG()
	{
		Map hm=WeekTime.getThisWeek();
		String thisbeginDate=hm.get("bz0").toString();
		String thisendDate=hm.get("bz1").toString();
		String last7beginDate=hm.get("sz0").toString();
		String last7endDate=hm.get("sz1").toString();
		String last14beginDate=hm.get("slz0").toString();
		String last14endDate=hm.get("slz1").toString();
		String last21beginDate=hm.get("ssz0").toString();
		String last21endDate=hm.get("ssz1").toString();

		String sql0 = "select sum(faultCount4) as faultCount from RptEpgOutageBo where 1=1 and location='省中心' and startTime>='"+thisbeginDate+"'" +
						" and startTime<'"+thisendDate+"' and reportType=3 group by reportType";
		LOG.info("EPG(ISA)数据查询=============sql0:"+sql0);
		String sql1 = "select sum(faultCount3) as faultCount from RptEpgOutageBo where 1=1 and location='省中心' and startTime>='"+last7beginDate+"'" +
				" and startTime<'"+last7endDate+"' and reportType=3 group by reportType";
		LOG.info("EPG(ISA)数据查询=============sql1:"+sql1);
		String sql2 = "select sum(faultCount2) as faultCount from RptEpgOutageBo where 1=1 and location='省中心' and startTime>='"+last14beginDate+"'" +
				" and startTime<'"+last14endDate+"' and reportType=3 group by reportType";
		LOG.info("EPG(ISA)数据查询=============sql2:"+sql2);
		String sql3 = "select sum(faultCount1) as faultCount from RptEpgOutageBo where 1=1 and location='省中心' and startTime>='"+last21beginDate+"'" +
				" and startTime<'"+last21endDate+"' and reportType=3 group by reportType";
		LOG.info("EPG(ISA)数据查询=============sql3:"+sql3);
		List<Map> list0 = jt.queryForList(sql0);
		List<Map> list1 = jt.queryForList(sql1);
		List<Map> list2 = jt.queryForList(sql2);
		List<Map> list3 = jt.queryForList(sql3);

		List<Map> list = new ArrayList();
		Map mp;

		if(list0==null || list0.size()==0)
		{
			System.out.print("本周暂无数据1");
			    mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","警告");
				mp.put("data","0");
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","普通");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list0.size();i++)
		{
			    mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","非常严重");
				mp.put("data",list0.get(i).get("faultCount").toString());
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","严重");
				mp.put("data",list0.get(i).get("faultCount").toString());
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","警告");
				mp.put("data",list0.get(i).get("faultCount").toString());
				list.add(mp);

				mp = new HashMap();
				mp.put("weekScope", "本周");
				mp.put("type","普通");
				mp.put("data",list0.get(i).get("faultCount").toString());
				list.add(mp);

		}
		}

		if(list1==null ||list1.size()==0)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","警告");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","普通");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list1.size();i++)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","非常严重");
				mp.put("data",list1.get(i).get("faultCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","严重");
				mp.put("data",list1.get(i).get("faultCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","警告");
				mp.put("data",list1.get(i).get("faultCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前一周");
				mp.put("type","普通");
				mp.put("data",list1.get(i).get("faultCount").toString());
				list.add(mp);
		}
		}

		if(list2==null || list2.size()==0)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","警告");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","普通");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
		for(int i=0;i<list2.size();i++)
		{

			    mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","非常严重");
				mp.put("data",list2.get(i).get("faultCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","严重");
				mp.put("data",list2.get(i).get("faultCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","警告");
				mp.put("data",list2.get(i).get("faultCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前两周");
				mp.put("type","普通");
				mp.put("data",list2.get(i).get("faultCount").toString());
				list.add(mp);
		}
		}

		if(list3==null || list3.size()==0)
		{
			    mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","非常严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","严重");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","警告");
				mp.put("data","0");
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","普通");
				mp.put("data","0");
				list.add(mp);
		}
		else
		{
			for(int i=0;i<list3.size();i++)
			{
			    mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","非常严重");
				mp.put("data",list3.get(i).get("faultCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","严重");
				mp.put("data",list3.get(i).get("faultCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","警告");
				mp.put("data",list3.get(i).get("faultCount").toString());
				list.add(mp);
				mp = new HashMap();
				mp.put("weekScope", "前三周");
				mp.put("type","普通");
				mp.put("data",list3.get(i).get("faultCount").toString());
				list.add(mp);
			}
		}
		return list;
	}

}
