package com.linkage.module.itms.resource.dao;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

public class UsersCountDAO extends SuperDAO {
	private static Logger logger = LoggerFactory.getLogger(UsersCountDAO.class);

	@SuppressWarnings("unchecked")
	public JSONArray queryUsersCount(String city_id) {
		logger.warn("UsersCountDAO=>queryUsersCount({})",city_id);
		StringBuffer sql = new StringBuffer();
		sql.append("select city_name as city_name, date_time as date_time ,SUM(countall) as countall,SUM(netsucc) as netsucc,SUM(iptvsucc) as iptvsucc,SUM(voipsucc) as voipsucc from tab_zeroconfig_res_day where city_name in (select city_name from tab_city where city_id=");
		sql.append("'");
		sql.append(city_id);
		sql.append("' or  parent_id = '");
		sql.append(city_id);
		sql.append("') and date_time in ('" +getDateBefore(-1)+"','"+
				                               getDateBefore(-2)+"','"+
				                               getDateBefore(-3)+"','"+
				                               getDateBefore(-4)+"','"+
				                               getDateBefore(-5)+"','"+
				                               getDateBefore(-6)+"','"+
				                               getDateBefore(-7)+"','"+
				                               getDateBefore(-8)+"','"+
				                               getDateBefore(-9)+"','"+
				                               getDateBefore(-10)+"')"+
				                               
				" group by city_name ,date_time");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		logger.warn("psql({})",psql.getSQL());
		List<Map<String,Object>> list = jt.queryForList(psql.getSQL());
        JSONArray jsonArray=new JSONArray();
		if(list != null && !list.isEmpty()){
			for(int i=0; i<list.size(); i++ ){
				JSONObject jsonObject=new JSONObject();
				String countall=list.get(i).get("countall").toString() == null ?"":list.get(i).get("countall").toString();
				String netsucc=list.get(i).get("netsucc").toString() == null ?"":list.get(i).get("netsucc").toString();
				String iptvsucc=list.get(i).get("iptvsucc").toString() == null ?"":list.get(i).get("iptvsucc").toString();
				String voipsucc=list.get(i).get("voipsucc").toString() == null ?"":list.get(i).get("voipsucc").toString();
				String city_name=list.get(i).get("city_name").toString() == null ?"":list.get(i).get("city_name").toString();
				String date_time=list.get(i).get("date_time").toString() == null ?"":list.get(i).get("date_time").toString();
				try
				{
					jsonObject.accumulate("countall", countall);
					jsonObject.accumulate("netsucc", netsucc);
					jsonObject.accumulate("iptvsucc", iptvsucc);
					jsonObject.accumulate("voipsucc", voipsucc);
					jsonObject.accumulate("city_name", city_name);
					jsonObject.accumulate("date_time", date_time);
				}
				catch (JSONException e)
				{
					jsonObject=new JSONObject();
				}
				jsonArray.put(jsonObject);
			}
		}
		return  jsonArray;
	}
    
	@SuppressWarnings("unchecked")
	public JSONArray queryUsersCountPerMinutes(String city_id) {
		logger.warn("UsersCountDAO=>queryUsersCount({})",city_id);
		StringBuffer sql = new StringBuffer();
		sql.append("select city_name as city_name  ,sum(countall) as countall,sum(netsucc) as netsucc,sum(iptvsucc) as iptvsucc,sum(voipsucc) as voipsucc from  tab_zeroconfig_res_minute where city_name in (select city_name from tab_city where city_id=");
		sql.append("'");
		sql.append(city_id);
		sql.append("' or  parent_id = '");
		sql.append(city_id);
		sql.append("') and add_time >" +getCurrentS()+
				" group by city_name");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		logger.warn("psql({})",psql.getSQL());
		List<Map<String,Object>> list = jt.queryForList(psql.getSQL());
        JSONArray jsonArray=new JSONArray();
		if(list != null && !list.isEmpty()){
			for(int i=0; i<list.size(); i++ ){
				JSONObject jsonObject=new JSONObject();
				String countall=list.get(i).get("countall").toString() == null ?"":list.get(i).get("countall").toString();
				String netsucc=list.get(i).get("netsucc").toString() == null ?"":list.get(i).get("netsucc").toString();
				String iptvsucc=list.get(i).get("iptvsucc").toString() == null ?"":list.get(i).get("iptvsucc").toString();
				String voipsucc=list.get(i).get("voipsucc").toString() == null ?"":list.get(i).get("voipsucc").toString();
				String city_name=list.get(i).get("city_name").toString() == null ?"":list.get(i).get("city_name").toString();
			
				try
				{
					jsonObject.accumulate("countall", countall);
					jsonObject.accumulate("netsucc", netsucc);
					jsonObject.accumulate("iptvsucc", iptvsucc);
					jsonObject.accumulate("voipsucc", voipsucc);
					jsonObject.accumulate("city_name", city_name);
					
				}
				catch (JSONException e)
				{
					jsonObject=new JSONObject();
				}
				jsonArray.put(jsonObject);
			}
		}
		return  jsonArray;
	}
	
	public static  String getDateBefore(int i)
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");  
        Date date=new Date();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, i);  
        date = calendar.getTime();  
        System.out.println(sdf.format(date));
		return null == sdf.format(date) ? null : sdf.format(date);
	}

    public  String getCurrentS()
    {   long currentMs=System.currentTimeMillis();
        long tenMinsAgoS=currentMs/1000 -10*60;       
    	return String.valueOf(tenMinsAgoS);
    }
   
}
