package com.linkage.liposs.manaconf;

import java.util.ArrayList;

//配置管理工具类
//作用：辅助HTML代码生成
public class ManaConfTools 
{	
	
	public static String getTimeTypeList(String selected)
	{
		String result = null;
		String tmp = null;
		result = "<select name=time_type onchange=changeBKTime()>";
		
		for (int i=1; i<3; i++)
		{
			if (i == 1)
			{
				tmp = "定时备份";
			}
			else
			{
				tmp = "周期备份";
			}
			
           result += "<option value=" + i;
			
		    if (selected != null)
		    {
		    	int w = Integer.parseInt(selected);
		    	if (w == i)
		    	{
		    		result +=" selected='selected'";
		    	}
		    }
		    result += ">" + tmp + "</option>";		
		}
		
		result += "</select>";
		return result;
	}
	
	public static String getLogonModList(String selected)
	{
		String result = null;
		String tmp = null;
		result = "<select name='tel_logonmod' onchange='changeLogonMod()'>";
		
		for (int i=0; i<5; i++)
		{
			switch(i)
			{
			  case 0:tmp = "用户密码、enable、enable口令";break;
			  case 1:tmp = "用户名、用户密码、enable、enable口令";break;
			  case 2:tmp = "用户密码";break;
			  case 3:tmp = "用户名、用户密码";break;
			  case 4:tmp = "用户名、用户密码、enable";break;
			}
			
			result += "<option value=" + i;
			
		    if ((selected != null) && !selected.equals(""))
		    {
		    	int w = Integer.parseInt(selected);
		    	if (w == i)
		    	{
		    		result +=" selected='selected'";
		    	}
		    }
		    result += ">" + tmp + "</option>";		
		}
		
		result += "</select>";
		return result;
	}
	
	public static String getBackupTypeList(String selected)
	{
		String result = null;
		result = "<select name=backup_type class=bk onchange=changeBKType()>";
		String tmp = null;
		
		for (int i=1; i<5; i++)
		{
			switch(i)
			{
			   case 1:tmp = "TELNET";break;
			   case 2:tmp = "FTP";break;
			   case 3:tmp = "SNMP";break;
			   case 4:tmp = "SHOW";break;
			   default:tmp ="";break;
			}
			
			result += "<option value=" +i;
			
		    if (selected != null)
		    {
		    	int w = Integer.parseInt(selected);
		    	if (w == i)
		    	{
		    		result +=" selected='selected'";
		    	}
		    }
		    result += ">" + tmp + "</option>";
		}
		
		result += "</select>";
		return result;
	}
	
	public static String getIfBackupSelect(String selected)
	{
		String result = null;
		result = "<select name=if_backup class=bk>";
		String tmp = null;
		
		for (int i=0; i<2; i++)
		{
			if (i == 0)
			{
				tmp = "否";
			}
			else
			{
				tmp = "是";
			}
			
			result += "<option value=" + i;
			
		    if (selected != null)
		    {
		    	int w = Integer.parseInt(selected);
		    	if (w == i)
		    	{
		    		result +=" selected='selected'";
		    	}
		    }
		    result += ">" + tmp + "</option>";
		}
		
		result += "</select>";
		return result;
	}
	public static ArrayList FormateBackupTime(String time)
	{
		ArrayList list = new ArrayList();
		String year = null;
		String month = null;
        String day = null;
        String hour = null;
        String min = null;
        String seconds = null;
        String time1 = null;
        String time2 = null;
        
        year = time.substring(0, 4);
        month = time.substring(4,6);
        day = time.substring(6,8);
        hour = time.substring(8,10);
        min = time.substring(10,12);
        seconds = "00";
        
        time1 = year + "-" + month + "-" + day;
        time2 = hour + ":" + min + ":" + seconds;
        
        list.add(time1);
        list.add(time2);
		return list;
	}
	
	public static String getWeekSelect(String selected)
	{
		String result = null;
		String tmp = null;
		result = "<select name=week id=week style='display:none'>";
			   
		for (int i=0; i<7; i++)
		{
		    switch(i)
			{
				case 0: tmp = "日";
					    break;
				case 1: tmp = "一";
			            break;
				case 2: tmp = "二";
			            break;
				case 3: tmp = "三";
			            break;
				case 4: tmp = "四";
			            break;
				case 5: tmp = "五";
			            break;
				case 6: tmp = "六";
			            break;
			    default : tmp = "";
			              break;
			}
		    
		    result += "<option value=" + i;
		    
		    if (selected != null)
		    {
		    	int w = Integer.parseInt(selected);
		    	if (w == i)
		    	{
		    		result +=" selected='selected'";
		    	}
		    }
		    
		    result += ">星期" + tmp + "</option>";
		}
        
		result += "</select>";
		return result;
	}
	
	public static String getMonthSelect(String selected)
	{
		String result = null;
		
		result = "<select name=month id=month style='display:none'>";
		
		for (int i=1; i<32; i++)
		{
			result += "<option value=" + i;
			
		    if (selected != null)
		    {
		    	int w = Integer.parseInt(selected);
		    	if (w == i)
		    	{
		    		result +=" selected='selected'";
		    	}
		    }
		    
		    
			result += ">" + i + "日</option>";
		}
		
		result += "</select>";
		
		return result;
	}
	
	public static String getHourList(String selected)
	{
		String result = null;
		result = "<select name='hour'>";
		
		for (int i=0; i<24; i++)
		{
			if (i<10)
			{
				result += "<option value=0" + i;
			}
			else
			{
				result += "<option value=" + i;
			}
			
			if (selected != null)
			{
		    	int w = Integer.parseInt(selected);
		    	if (w == i)
		    	{
		    		result +=" selected='selected'";
		    	}
			}
			
			result += ">" + i + "点</option>";
			
		}
		result += "</select>";
		return result;
	}
	
	public static String getBackupType(String type)
	{
	      String backup_type = null;
	      
		  if (type.equals("1"))
	      {
	          backup_type = "TELNET";
	      }
	      else if (type.equals("2"))
	      {
	          backup_type = "FTP";
	      }
	      else if (type.equals("3"))
	      {
	          backup_type = "SNMP";
	      }
	      else if (type.equals("4"))
	      {
	          backup_type = "SHOW";
	      }
	      else
	      {
	          backup_type = "";
	      }
	      
	      return backup_type;
	}
	
	public static String getIfBackup(String ifBackup)
	{
		String if_backup = null;
		
	      if (ifBackup.equals("1"))
	      {
	         if_backup = "是";
	      }
	      else if (ifBackup.equals("0"))
	      {
	         if_backup = "否";
	      }
	      
		return if_backup;
	}
	
	public static String getBackupTime(String time_type,String time_model,String backup_time)
	{
		String time = "";
		String tmp[] = null;
		
	    if (time_type.equals("1"))
	    {
	        time = "定时-(" + backup_time + ")";
	    }
	    else if (time_type.equals("2"))
	    {
	       time = "周期-";
	       if (time_model.equals("1"))
	       {
	          time +="每天" + backup_time + "点";
	       }
	       else if (time_model.equals("2"))
	       {
	          time +="每周";
	          tmp = splitTime(time_model,backup_time);
	          time += tmp[0] + tmp[1] + "点";
	       }
	       else if (time_model.equals("3"))
	       {
	    	   time +="每月";
		       tmp = splitTime(time_model,backup_time);
		       time += tmp[0] + "日" + tmp[1] + "点";   
	       }
	    }
	    
		return time;
	}
	
	public static String[] splitTime(String model,String backup_time)
	{
		String result[] = new String[2];
		result = backup_time.split(",");
		
		if (model.equals("2"))
		{
			result[0] = getWeek(result[0]);
		}
		
		return result;
	}
	
	public static String getWeek(String week)
	{
		String result = null;
		int w = Integer.parseInt(week);
		
		switch(w)
		{
		   case 0: result = "日";
		           break;
		   case 1: result = "一";
                   break;
		   case 2: result = "二";
                   break;
		   case 3: result = "三";
                   break;
		   case 4: result = "四";
                   break;
		   case 5: result = "五";
                   break;
		   case 6: result = "六";
                   break;
           default : result = "";
                   break;
		}
		
		return result;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
