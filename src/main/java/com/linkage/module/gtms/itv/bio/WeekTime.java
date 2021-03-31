package com.linkage.module.gtms.itv.bio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WeekTime {

	/**
	 * 本周开始结束时间
	 */
	public static Map<String, String> getThisWeek()
	{
		Map<String, String> hm=new HashMap<String, String>();
		
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
		//System.out.print("时间哈哈："+thisbegin);
		 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		Date thisbeginDate = new Date(thisbegin*1000);
		Date thisendDate = new Date(thisend*1000);
		Date last7beginDate = new Date(last7begin*1000);
		Date last7endDate = new Date(last7end*1000);
		Date last14beginDate = new Date(last14begin*1000);
		Date last14endDate = new Date(last14end*1000);
		Date last21beginDate = new Date(last21begin*1000);
		Date last21endDate = new Date(last21end*1000);
		
		 
		System.out.println(sf.format(thisbeginDate));
		  
		System.out.println("本周开始时间"+sf.format(thisbeginDate)+"  本周结束时间"+sf.format(thisendDate));
		System.out.println("上周开始时间"+sf.format(last7beginDate)+"  上周结束时间"+sf.format(last7endDate));
		System.out.println("上两周开始时间"+sf.format(last14beginDate)+"  上两周结束时间"+sf.format(last14endDate));
		System.out.println("上三周开始时间"+sf.format(last21beginDate)+"  上三周结束时间"+sf.format(last21endDate));
		
		hm.put("bz0", sf.format(thisbeginDate));
		hm.put("bz1", sf.format(thisendDate));
		hm.put("sz0", sf.format(last7beginDate));
		hm.put("sz1", sf.format(last7endDate));
		hm.put("slz0", sf.format(last14beginDate));
		hm.put("slz1", sf.format(last14endDate));
		hm.put("ssz0", sf.format(last21beginDate));
		hm.put("ssz1", sf.format(last21endDate));
		
		return hm;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WeekTime obj=new WeekTime();
		obj.getThisWeek();
	}

}
