package com.linkage.module.itms.resource.bio;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.report.bio.PVCReportBIO;
import com.linkage.module.itms.resource.dao.LogRestartQueryDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogRestartQueryBIO
{
  private static Logger logger = LoggerFactory.getLogger(PVCReportBIO.class);
  private LogRestartQueryDAO restartDAO;
  private int maxPage_splitPage;

  public LogRestartQueryDAO getRestartDAO()
  {
    return this.restartDAO;
  }

  public void setRestartDAO(LogRestartQueryDAO restartDAO)
  {
    this.restartDAO = restartDAO;
  }

  public List<Map> countITMS(String cityId, String starttime, String endtime, int curPage_splitPage, int num_splitPage,long acc_oid)
  {
    logger.debug("countITMS({},{},{})", new Object[] { cityId, starttime, endtime });
    List list = this.restartDAO.getrestart(cityId, starttime, endtime, 
      curPage_splitPage, num_splitPage,acc_oid);
   
    List listShow = new ArrayList();
    Map testMap = new HashMap();
    testMap.put("test", "test");
    listShow.add(testMap);
    for (int i = 0; i < list.size(); i++)
    {
    	 int failNum = 0;
    	    int doneNum = 0;
    	    int unDoneNum = 0;
    	    int totalNum = 0;
      Map map = (Map)list.get(i);
      if ("-1".equals(map.get("status")))
      {
        failNum = Integer.valueOf((String)map.get("num")).intValue();
      }
      if ("1".equals(map.get("status")))
      {
        doneNum = Integer.valueOf((String)map.get("num")).intValue();
      }
      if ("3".equals(map.get("status")))
      {
        unDoneNum = Integer.valueOf((String)map.get("num")).intValue();
      }
      String temp = (String)map.get("add_time") + (String)map.get("task_name");
      int j = 0; if (j < listShow.size())
      {
        Map map_a = (Map)listShow.get(j);
        if ((!map_a.containsKey("temp")) || (!map_a.get("temp").equals(temp)))
        {
          Map mapshow = new HashMap();
          mapshow.put("temp", temp);
          mapshow.put("add_time", map.get("add_time"));
          mapshow.put("task_name", map.get("task_name"));
          mapshow.put("failNum", String.valueOf(failNum));
          mapshow.put("doneNum", String.valueOf(doneNum));
          mapshow.put("unDoneNum", String.valueOf(unDoneNum));
          totalNum = failNum + doneNum + unDoneNum;
          mapshow.put("totalNum", String.valueOf(totalNum));
          listShow.add(mapshow);
        }
        else
        {
          map_a.put("add_time", map.get("add_time"));
          map_a.put("task_name", map.get("task_name"));
          map_a.put(
            "failNum", 
            String.valueOf(String.valueOf(Integer.valueOf(
            (String)map_a
            .get("failNum")).intValue() + 
            failNum)));
          map_a.put(
            "doneNum", 
            String.valueOf(String.valueOf(Integer.valueOf(
            (String)map_a
            .get("doneNum")).intValue() + 
            doneNum)));
          map_a.put(
            "unDoneNum", 
            String.valueOf(String.valueOf(Integer.valueOf(
            (String)map_a
            .get("unDoneNum")).intValue() + 
            unDoneNum)));
          totalNum = failNum + doneNum + unDoneNum;
          map_a.put(
            "totalNum", 
            String.valueOf(String.valueOf(Integer.valueOf(
            (String)map_a
            .get("totalNum")).intValue() + 
            totalNum)));
        }
      }
    }

    this.maxPage_splitPage = this.restartDAO.getquerypaging(cityId, starttime, endtime, 
      curPage_splitPage, num_splitPage,acc_oid);
    return listShow;
  }

  public List<Map> countITMSExcel(String cityId, String starttime, String endtime,long acc_oid)
  {
    logger.debug("countITMS({},{},{})", new Object[] { cityId, starttime, endtime });
    List list = this.restartDAO.QueryDerive(cityId, starttime, endtime,acc_oid);
   
    List listShow = new ArrayList();
    Map testMap = new HashMap();
    testMap.put("test", "test");
    listShow.add(testMap);
    for (int i = 0; i < list.size(); i++)
    {
    	 int failNum = 0;
    	    int doneNum = 0;
    	    int unDoneNum = 0;
    	    int totalNum = 0;
      Map map = (Map)list.get(i);
      if ("-1".equals(map.get("status")))
      {
        failNum = Integer.valueOf((String)map.get("num")).intValue();
      }
      if ("1".equals(map.get("status")))
      {
        doneNum = Integer.valueOf((String)map.get("num")).intValue();
      }
      if ("3".equals(map.get("status")))
      {
        unDoneNum = Integer.valueOf((String)map.get("num")).intValue();
      }

      String temp = String.valueOf(map.get("add_time")) + 
        String.valueOf(map.get("task_name"));

      int j = 0; if (j < listShow.size())
      {
        Map map_a = (Map)listShow.get(j);
        if ((!map_a.containsKey("temp")) || (!map_a.get("temp").equals(temp)))
        {
          Map mapshow = new HashMap();
          mapshow.put("temp", temp);
          mapshow.put("add_time", map.get("add_time"));
          mapshow.put("task_name", map.get("task_name"));
          mapshow.put("failNum", String.valueOf(failNum));
          mapshow.put("doneNum", String.valueOf(doneNum));
          mapshow.put("unDoneNum", String.valueOf(unDoneNum));
          totalNum = failNum + doneNum + unDoneNum;
          mapshow.put("totalNum", String.valueOf(totalNum));
          listShow.add(mapshow);
        }
        else
        {
          map_a.put("add_time", map.get("add_time"));
          map_a.put("task_name", map.get("task_name"));
          map_a.put(
            "failNum", 
            String.valueOf(String.valueOf(Integer.valueOf(
            (String)map_a
            .get("failNum")).intValue() + 
            failNum)));
          map_a.put(
            "doneNum", 
            String.valueOf(String.valueOf(Integer.valueOf(
            (String)map_a
            .get("doneNum")).intValue() + 
            doneNum)));
          map_a.put(
            "unDoneNum", 
            String.valueOf(String.valueOf(Integer.valueOf(
            (String)map_a
            .get("unDoneNum")).intValue() + 
            unDoneNum)));
          totalNum = failNum + doneNum + unDoneNum;
          map_a.put(
            "totalNum", 
            String.valueOf(String.valueOf(Integer.valueOf(
            (String)map_a
            .get("totalNum")).intValue() + 
            totalNum)));
        }
      }
    }

    return listShow;
  }

  @SuppressWarnings("unchecked")
public List<Map> sunQuery(String add_time, String task_name, int curPage_splitPage, int num_splitPage,long acc_oid)
  {
	  List<Map> list = restartDAO.sunQuery(add_time, task_name, curPage_splitPage,
				num_splitPage,acc_oid);
		List<Map> listShow = new ArrayList<Map>();
		
		for (int i = 0; i < list.size(); i++)
		{	
			int failNum = 0;
			int doneNum = 0;
			int unDoneNum = 0;
			int totalNum = 0;
			Map mapshow = new HashMap();
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
		maxPage_splitPage = restartDAO.sunQuerypaging(add_time, task_name,
				curPage_splitPage, num_splitPage,acc_oid);
		return listShow;
  }

  @SuppressWarnings("unchecked")
public List<Map> sunQueryDrive(String add_time, String task_name,long acc_oid)
  {

		List<Map> list = restartDAO.sunQueryDrive(add_time, task_name,acc_oid);
		
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
			mapshow.put("city_name", map.get("city_name"));
			mapshow.put("city_id", map.get("city_id"));
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

  public List<Map> SuccessQuery(String add_time, String task_name, String doneNum, String type, int curPage_splitPage, int num_splitPage,long acc_oid)
  {
    logger.debug("SuccessQuery({},{},{})" + 
      new Object[] { add_time, task_name, doneNum });
    this.maxPage_splitPage = this.restartDAO.communalPaging(type, add_time, curPage_splitPage, num_splitPage, acc_oid, task_name);
    return this.restartDAO.communalquery(type, add_time, curPage_splitPage, num_splitPage, acc_oid, task_name);
    		
  }

  public List<Map> successQueryDerive(String add_time, String task_name, String doneNum, String type,long acc_oid)
  {
    return this.restartDAO.communalDerive(type, add_time, acc_oid, task_name);
  }

  public List<Map> FailQuery(String failNum, String add_time, String task_name, String type, int curPage_splitPage, int num_splitPage,long acc_oid)
  {
    this.maxPage_splitPage = this.restartDAO.communalPaging(type, add_time, curPage_splitPage, num_splitPage, acc_oid, task_name);
    return this.restartDAO.communalquery(type, add_time, curPage_splitPage, num_splitPage, acc_oid, task_name);
    		
  }

  public List<Map> FailQuertDerive(String failNum, String add_time, String task_name, String type,long acc_oid)
  {
    return this.restartDAO.communalDerive(type, add_time, acc_oid, task_name);
  }

  public List<Map> unexecute(String unDoneNum, String add_time, String task_name, String type, int curPage_splitPage, int num_splitPage,long acc_oid)
  {
    this.maxPage_splitPage = this.restartDAO.communalPaging(type, add_time, curPage_splitPage, num_splitPage, acc_oid, task_name);
    return this.restartDAO.communalquery(type, add_time, curPage_splitPage, num_splitPage, acc_oid, task_name);
    		
  }

  public List<Map> unexecutederive(String unDoneNum, String add_time, String task_name, String type,long acc_oid)
  {
    return this.restartDAO.communalDerive(type, add_time, acc_oid, task_name);
  }

  public int getMaxPage_splitPage()
  {
    return this.maxPage_splitPage;
  }

  public void setMaxPage_splitPage(int maxPage_splitPage)
  {
    this.maxPage_splitPage = maxPage_splitPage;
  }
}