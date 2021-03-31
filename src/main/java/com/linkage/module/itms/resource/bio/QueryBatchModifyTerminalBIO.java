package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.resource.dao.QueryBatchModifyTerminalDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-3-30
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class QueryBatchModifyTerminalBIO
{
	private QueryBatchModifyTerminalDAO dao;
	private int maxPage_splitPage;
	@SuppressWarnings("unchecked")
	public List<Map> query(String starttime,String endtime,String taskName,int curPage_splitPage, int num_splitPage,long acc_oid)
	{
		List<Map> list=dao.Query(starttime, endtime, taskName, curPage_splitPage, num_splitPage,acc_oid);
		
		List<Map> listShow = new ArrayList<Map>();
		
		for(int i=0;i<list.size();i++)
		{
			int failNum = 0;
			int doneNum = 0;
			int unDoneNum = 0;
			int totalNum = 0;
			Map mapshow = new HashMap();
			Map<String, String> map = list.get(i);
			
			if("-1".equals(map.get("status")))
			{
			failNum = Integer.valueOf((map.get("num")));
			}
			if("0".equals(map.get("status")))
			{
				unDoneNum =Integer.valueOf(map.get("num"));
			}
			if("1".equals(map.get("status")))
			{
				doneNum =Integer.valueOf(map.get("num"));
			}
			String temp = map.get("add_time")+map.get("task_name");
					mapshow.put("temp", temp);
					mapshow.put("add_time",  map.get("add_time"));
					mapshow.put("task_name",  map.get("task_name"));
					mapshow.put("failNum", String.valueOf(failNum));
					mapshow.put("unDoneNum", String.valueOf(unDoneNum));
					mapshow.put("doneNum", String.valueOf(doneNum));
					totalNum = failNum + doneNum + unDoneNum;
					mapshow.put("totalNum", String.valueOf(totalNum));
					listShow.add(mapshow);
			}
			
		maxPage_splitPage=dao.getquerypaging(starttime, endtime, taskName, curPage_splitPage, num_splitPage,acc_oid);
		return listShow;
		}
	/**
	 *	下发成功数查询
	 * @return
	 */
	public List<Map> doneNumQuery(String type,String add_time,int curPage_splitPage, int num_splitPage,long acc_oid,String task_name)
	{
		maxPage_splitPage=dao.communalPaging( type, add_time, curPage_splitPage, num_splitPage,acc_oid,task_name);
		return dao.communalquery( type, add_time, curPage_splitPage, num_splitPage,acc_oid,task_name);
				
	}
	/**
	 *	下发成功数导出
	 * @return
	 */
	public List<Map> doneNumDerive(String type,String add_time,long acc_oid,String task_name)
	{
		return dao.communalDerive( type, add_time,acc_oid,task_name);
	}
	/**
	 *	未触发数查询
	 * @return
	 */
	public List<Map> unDoneNumQuery(String type,String add_time,int curPage_splitPage, int num_splitPage,long acc_oid,String task_name)
	{
		maxPage_splitPage=dao.communalPaging(type, add_time, curPage_splitPage, num_splitPage,acc_oid,task_name);
	
		return dao.communalquery( type, add_time, curPage_splitPage, num_splitPage,acc_oid,task_name);
				
	}
	
	/**
	 *	未触发数导出
	 * @return
	 */
	public List<Map> unDoneNumDerive(String type,String add_time,long acc_oid,String task_name)
	{
		return dao.communalDerive( type, add_time,acc_oid,task_name);
	}
	
	/**
	 *	未成功数查询
	 * @return
	 */
	public List<Map> failNumQuery(String type,String add_time,int curPage_splitPage, int num_splitPage,long acc_oid,String task_name)
	{
		maxPage_splitPage=dao.communalPaging(type, add_time, curPage_splitPage, num_splitPage,acc_oid,task_name);
		return dao.communalquery( type, add_time, curPage_splitPage, num_splitPage,acc_oid,task_name);
				
	}
	/**
	 *	未成功数导出
	 * @return
	 */
	public List<Map> failNumDerive(String type,String add_time,long acc_oid,String task_name)
	{
		return dao.communalDerive(type, add_time,acc_oid,task_name);
	}
	/**
	 * 总数导出
	 */
	@SuppressWarnings("unchecked")
	public List<Map> sunQueryDrive(String add_time, String task_name,long acc_oid)
	{
		List<Map> list = dao.sunQueryDrive(add_time, task_name,acc_oid);
		
		List<Map> listShow = new ArrayList<Map>();
		Map mapshow = new HashMap();
		for (int i = 0; i < list.size(); i++)
		{	int failNum = 0;
			int doneNum = 0;
			int unDoneNum = 0;
			int totalNum = 0;
			Map<String, String> map = list.get(i);
			if ("-1".equals(map.get("status")))
			{
				failNum = Integer.valueOf((map.get("num")));
			}
			if ("1".equals(map.get("status")))
			{
				doneNum = Integer.valueOf((map.get("num")));
			}
			if ("3".equals(map.get("status")))
			{
				unDoneNum = Integer.valueOf((map.get("num")));
			}
			totalNum = failNum + doneNum + unDoneNum;
			mapshow.put("update_time", map.get("update_time"));
			mapshow.put("task_name", map.get("task_name"));
			mapshow.put("failNum", String.valueOf(failNum));
			mapshow.put("doneNum", String.valueOf(doneNum));
			mapshow.put("unDoneNum", String.valueOf(unDoneNum));
			mapshow.put("totalNum", String.valueOf(totalNum));
			mapshow.put("city_id", map.get("city_id"));
			mapshow.put("city_name", map.get("city_name"));
			mapshow.put("username", map.get("username"));
			mapshow.put("device_serialnumber", map.get("device_serialnumber"));
			mapshow.put("specversion", map.get("specversion"));
			mapshow.put("hardwareversion", map.get("hardwareversion"));
			mapshow.put("softwareversion", map.get("softwareversion"));
			mapshow.put("vendor_name", map.get("vendor_name"));
			mapshow.put("fail_desc", map.get("fail_desc"));
			mapshow.put("device_model", map.get("device_model"));
			listShow.add(mapshow);
		}
		return listShow;
	}
	/**
	 * 总数查询
	 */
	@SuppressWarnings("unchecked")
	public List<Map> sunQuery(String add_time, String task_name, int curPage_splitPage,
			int num_splitPage,long acc_oid)
	{
		List<Map> list = dao.sunQuery(add_time, task_name, curPage_splitPage,
				num_splitPage,acc_oid);
		int failNum = 0;
		int doneNum = 0;
		int unDoneNum = 0;
		int totalNum = 0;
		List<Map> listShow = new ArrayList<Map>();
		
		for (int i = 0; i < list.size(); i++)
		{	Map mapshow = new HashMap();
			Map<String, String> map = list.get(i);
			if ("-1".equals(map.get("status")))
			{
				failNum = Integer.valueOf((map.get("num")));
			}
			if ("1".equals(map.get("status")))
			{
				doneNum = Integer.valueOf((map.get("num")));
			}
			if ("3".equals(map.get("status")))
			{
				unDoneNum = Integer.valueOf((map.get("num")));
			}
			totalNum = failNum + doneNum + unDoneNum;
			mapshow.put("update_time", map.get("update_time"));
			mapshow.put("failNum", String.valueOf(failNum));
			mapshow.put("doneNum", String.valueOf(doneNum));
			mapshow.put("unDoneNum", String.valueOf(unDoneNum));
			mapshow.put("totalNum", String.valueOf(totalNum));
			mapshow.put("city_id", map.get("city_id"));
			mapshow.put("city_name", map.get("city_name"));
			mapshow.put("username", map.get("username"));
			mapshow.put("device_serialnumber", map.get("device_serialnumber"));
			mapshow.put("specversion", map.get("specversion"));
			mapshow.put("hardwareversion", map.get("hardwareversion"));
			mapshow.put("softwareversion", map.get("softwareversion"));
			mapshow.put("vendor_name", map.get("vendor_name"));
			mapshow.put("fail_desc", map.get("fail_desc"));
			mapshow.put("device_model", map.get("device_model"));
			listShow.add(mapshow);
			
		}
		maxPage_splitPage = dao.sunQuerypaging(add_time, task_name,
				curPage_splitPage, num_splitPage,acc_oid);
		return listShow;
	}

	public QueryBatchModifyTerminalDAO getDao()
	{
		return dao;
	}
	
	public void setDao(QueryBatchModifyTerminalDAO dao)
	{
		this.dao = dao;
	}
	
	public int getMaxPage_splitPage()
	{
		return maxPage_splitPage;
	}
	
	public void setMaxPage_splitPage(int maxPage_splitPage)
	{
		this.maxPage_splitPage = maxPage_splitPage;
	}
	
}
