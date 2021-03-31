package com.linkage.litms.webtopo.warn;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;



public class DeviceWarnStat {
  HttpServletRequest request = null;
  public DeviceWarnStat() {
  }

  public DeviceWarnStat(HttpServletRequest request) {
    this.request = request;
  }

  public boolean IsExitTable(String tableName)
  {
    boolean flag=false;
    String mysql="select count(1) as num from sysobjects where type='U' and name= '" +
              tableName + "'";
    if (DBUtil.GetDB() == Global.DB_ORACLE)
	{// oracle
    	mysql = "select count(1) from user_tables where table_name='"
				+ StringUtil.getUpperCase(tableName) + "'";
	}
	else if (DBUtil.GetDB() == Global.DB_SYBASE)
	{// sybase
		mysql = "select count(1) from sysobjects where type='U' and name='" + tableName
				+ "'";
	}
    PrepareSQL psql = new PrepareSQL(mysql);
	psql.getSQL();
    Map map=DataSetBean.getRecord(mysql);
    if(map!=null && Integer.parseInt((String)map.get("num"))>0)
    {
      flag=true;
    }
    return flag;
  }

  /**
   * 获取统计的信息
   * @return
   */
  public HashMap getStatResult() {
    HashMap retMap = new HashMap();
    HashMap devMap=new HashMap();
    String mysql = "";
    //报表的类型
    String report_type = request.getParameter("report_type");
    //查询的开始时间
    String gathertime = request.getParameter("gathertime");
    long begin = 0;
    long end = 0;
    //首先根据报表的类型生成表名和查询时间
    java.sql.Date day = java.sql.Date.valueOf(gathertime);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

    UserRes userRes=(UserRes)this.request.getSession().getAttribute("curUser");
    long areaid=userRes.getUser().getAreaId();
    //初始化设备名称和设备IP
    mysql="select device_id,loopback_ip,device_name from tab_gw_device where device_id"
        +" in(select res_id from tab_gw_res_area where area_id="+areaid+" and res_type=1)";
    PrepareSQL psql = new PrepareSQL(mysql);
	psql.getSQL();
    Cursor cursor=DataSetBean.getCursor(mysql);
    Map map=cursor.getNext();
    while(map!=null)
    {
      devMap.put(map.get("device_id"),map.get("device_name")+"/"+map.get("loopback_ip"));
      map=cursor.getNext();
    }


    long l_day=day.getTime()/1000;
    mysql="";
    switch (Integer.parseInt(report_type)) {
      //日报表
      case 1: {
        begin=l_day;
        end=l_day+86400;
        int week_start = StringUtils.getWeekOfYear((int)l_day)+1;
        mysql="select devicecoding,severity,count(1) as num from event_raw_"
            +sdf.format(day)+"_"+week_start+" where createtime>="+begin+" and createtime<"+end+""
            +" and devicecoding in (select res_id from tab_gw_res_area where area_id="+areaid+" and res_type=1) "
            +" group by devicecoding,severity ";

        break;
      }
      //周报表
      case 2: {
        int num=StringUtils.getDayOfWeek(l_day);
        begin=l_day-(num-1)*86400L;
        end=l_day+(8-num)*86400L;

        int week_start = StringUtils.getWeekOfYear((int)l_day)+1;
        mysql="select devicecoding,severity,count(1) as num from event_raw_"
           +sdf.format(day)+"_"+week_start+" where createtime>="+begin+" and createtime<"+end+""
           +" and devicecoding in (select res_id from tab_gw_res_area where area_id="+areaid+" and res_type=1) "
           +" group by devicecoding,severity ";

        break;
      }
      //月报表
      case 3: {
        //首先获取这个月的开始时间
        begin=StringUtils.getNowMonthDay((int)l_day);
        //其次获取这个月的结束时间
        end=StringUtils.getNextMonthDay((int)l_day);
        //接着生成该月的几个表
        long temp=begin;
        int j=0;
        int week_start =0;
        String tableName="";
        while(temp<end)
        {
          week_start= StringUtils.getWeekOfYear((int)temp)+1;;
          tableName="event_raw_"+sdf.format(day)+"_"+week_start;
          if(IsExitTable(tableName))
          {
            if (j == 0) {
              mysql = "select devicecoding,severity,count(1) as num from " +
                  tableName + " where createtime>=" + begin + " "
                  + "  and  devicecoding in(select res_id from tab_gw_res_area where area_id=" +
                  areaid + " and res_type=1 )"
                  + " group by devicecoding,severity ";
            }
            else {
              mysql +=
                  "union all select devicecoding,severity,count(1) as num from " +
                  tableName + " where createtime<" + end + " "
                  + "  and devicecoding in(select res_id from tab_gw_res_area where area_id=" +
                  areaid + " and res_type=1 )"
                  + " group by devicecoding,severity ";
            }
            j++;
          }
          temp +=86400*7;
        }
        //生成sql脚本

        break;
      }
    }
    if(mysql.trim().compareTo("")==0)
    {

      return retMap;
    }
    psql = new PrepareSQL(mysql);
    cursor=DataSetBean.getCursor(psql.getSQL());
    map=cursor.getNext();
    String device_id=null;
    int severity=0;
    int num=0;
    WarnResult wr=null;
    while(map!=null)
    {
      device_id=(String)map.get("devicecoding");
      num=Integer.parseInt((String)map.get("num"));
      severity=Integer.parseInt((String)map.get("severity"));
      if(retMap.containsKey(device_id))
      {
        wr=(WarnResult)retMap.get(device_id);
      }
      else
      {
        wr=new WarnResult();
        wr.setDevice_id(device_id);
        wr.setDevice_name((String)devMap.get(device_id));
      }

      switch(severity)
        {
          case 0:
          {
            wr.setW0(num);
            break;
          }
          case 1:
          {
            wr.setW1(num);
            break;
          }
          case 2:
          {
            wr.setW2(num);
            break;
          }
          case 3:
          {
            wr.setW3(num);
            break;
          }
          case 4:
          {
            wr.setW4(num);
            break;
          }
          case 5:
          {
            wr.setW5(num);
            break;
          }
        }
      retMap.put(device_id,wr);
      map=cursor.getNext();
    }


    devMap.clear();
    devMap=null;
    return retMap;

  }



}