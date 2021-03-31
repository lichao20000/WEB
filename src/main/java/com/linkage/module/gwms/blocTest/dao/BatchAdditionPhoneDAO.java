package com.linkage.module.gwms.blocTest.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-12-21
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BatchAdditionPhoneDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(BatchAdditionPhoneDAO.class);
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
	
	public int gettabphoneinfo(ArrayList<Map> list) throws ParseException
	{
		ArrayList<String> sqlList = new ArrayList<String>();
		 
		for(int i=0;i<list.size();i++)
		{
			StringBuilder sql = new StringBuilder();
			sql.append("insert into tab_phone_info(");
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("net_account"))))
			{
				sql.append(" net_account ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("phone_number"))))
			{
				sql.append(" ,phone_number");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("device_model"))))
			{
				sql.append(" ,device_model ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("phone_operator"))))
			{
				sql.append(" ,phone_operator ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("province"))))
			{
				sql.append(" ,province ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("city_name"))))
			{
				sql.append(" ,city_name ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("card_type"))))
			{
				sql.append(" ,card_type ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("brand"))))
			{
				sql.append(" ,brand ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("zhishi"))))
			{
				sql.append(" ,zhishi ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("operator_type"))))
			{
				sql.append(" ,operator_type ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("phone_firsttime"))))
			{
				sql.append(" ,phone_firsttime ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("phone_lasttime"))))
			{
				sql.append(" ,phone_lasttime ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("active_days"))))
			{
				sql.append(" ,active_days ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("active_day2"))))
			{
				sql.append(" ,active_day2 ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("dev_firsttime"))))
			{
				sql.append(" ,dev_firsttime ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("dev_lasttime"))))
			{
				sql.append(" ,dev_lasttime ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("dev_active_days"))))
			{
				sql.append(" ,dev_active_days ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("dev_active_day2"))))
			{
				sql.append(" ,dev_active_day2 ");
			}
			sql.append(" )values(");
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("net_account"))))
			{
				sql.append("'"+list.get(i).get("net_account")+"', ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("phone_number"))))
			{
				sql.append(" '"+list.get(i).get("phone_number")+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("device_model"))))
			{
				sql.append(" ,'"+list.get(i).get("device_model")+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("phone_operator"))))
			{
				sql.append(" ,'"+list.get(i).get("phone_operator")+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("province"))))
			{
				sql.append(" ,'"+list.get(i).get("province")+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("city_name"))))
			{
				sql.append(" ,'"+list.get(i).get("city_name")+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("card_type"))))
			{
				sql.append(" ,'"+list.get(i).get("card_type")+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("brand"))))
			{
				sql.append(" ,'"+list.get(i).get("brand")+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("zhishi"))))
			{
				sql.append(" ,'"+list.get(i).get("zhishi")+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("operator_type"))))
			{
				sql.append(" ,'"+list.get(i).get("operator_type")+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("phone_firsttime"))))
			{
				long ss=format.parse(String.valueOf(list.get(i).get("phone_firsttime"))).getTime()/1000;
				String phone_firsttime=String.valueOf(ss);
				sql.append(" ,'"+phone_firsttime+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("phone_lasttime"))))
			{
				long ss=format.parse(String.valueOf(list.get(i).get("phone_lasttime"))).getTime()/1000;
				String phone_firsttime=String.valueOf(ss);
				sql.append(" ,'"+phone_firsttime+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("active_days"))))
			{
				int active_days=Integer.valueOf(String.valueOf(list.get(i).get("active_days")));
				sql.append(" ,'"+active_days+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("active_day2"))))
			{
				int active_day2=Integer.valueOf(String.valueOf(list.get(i).get("active_day2")));
				sql.append(" ,'"+active_day2+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("dev_firsttime"))))
			{
				long dev_firsttime=format.parse(String.valueOf(list.get(i).get("dev_firsttime"))).getTime()/1000;
				sql.append(" ,"+dev_firsttime+" ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("dev_lasttime"))))
			{
				long dev_lasttime=format.parse(String.valueOf(list.get(i).get("dev_lasttime"))).getTime()/1000;
				sql.append(" ,"+dev_lasttime+" ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("dev_active_days"))))
			{
				int dev_active_days=Integer.valueOf(String.valueOf(list.get(i).get("dev_active_days")));
				sql.append(" ,'"+dev_active_days+"' ");
			}
			if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("dev_active_day2"))))
			{
				int dev_active_day2=Integer.valueOf(String.valueOf(list.get(i).get("dev_active_day2")));
				sql.append(" ,'"+dev_active_day2+"' ");
			}
			sql.append(" )");
			sqlList.add(sql.toString());
		}
		return DBOperation.executeUpdate(sqlList.toArray(new String[0]));
	}
	
	public int gettabmaccompany (List<Map<String,String>> list)
	{
		PrepareSQL psql = new PrepareSQL();
		
		ArrayList<String> sqlList = new ArrayList<String>();
		for(int i=0;i<list.size();i++)
		{
			StringBuilder sql = new StringBuilder();
		sql.append("insert into tab_mac_company (");
		if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("mac"))))
		{
			sql.append(" mac");
		}
		if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("company_name"))))
		{
			sql.append(",company_name");
		}
		sql.append(" ) values(");
		if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("mac"))))
		{
			sql.append("'"+list.get(i).get("mac")+"', ");
		}
		if(!StringUtil.IsEmpty(String.valueOf(list.get(i).get("company_name"))))
		{
			sql.append("'"+String.valueOf(list.get(i).get("company_name"))+"' ");
		}
		sql.append(")");
		sqlList.add(sql.toString());
		}
		return DBOperation.executeUpdate(sqlList.toArray(new String[0]));
	}
	
	/**
	 * 先入临时表tab_mac_company_temp，处理后再入正式表tab_mac_company
	 * @param list
	 * @return
	 */
	public int insertTabmaccompany (List<Map<String,String>> list)
	{
		logger.warn("mac特征数据入库");
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL sql = new PrepareSQL();
		//1、清空临时表
		sql.append("truncate table tab_mac_company_temp ");
		DBOperation.executeUpdate(sql.getSQL());
		
		//2、入临时表
		for(int i=0;i<list.size();i++)
		{
			sql=null;
			sql = new PrepareSQL();
			sql.append("insert into tab_mac_company_temp(mac,company_name) values(");
			sql.append("'"+String.valueOf(list.get(i).get("mac")).toUpperCase()+"',");
			sql.append("'"+String.valueOf(list.get(i).get("company_name"))+"')");
			
			sqlList.add(sql.getSQL());
			
			if(sqlList.size()>0 && sqlList.size()%200==0)
			{
				if(DBOperation.executeUpdate(sqlList.toArray(new String[0]))==1){
					sqlList.clear();
					try{
						Thread.sleep(500);
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					return -1;
				}
			}
		}
		
		if(sqlList.size()>0){
			DBOperation.executeUpdate(sqlList.toArray(new String[0]));
			sqlList.clear();
		}
		
		sql=null;
		sql=new PrepareSQL();
		//3、删除重复数据
		sql.append("delete from tab_mac_company_temp a where exists ");
		sql.append("(select 1 from tab_mac_company b ");
		sql.append("where a.mac=b.mac and a.company_name=b.company_name) ");
		sqlList.add(sql.getSQL());
		
		sql=null;
		sql=new PrepareSQL();
		//4、删除mac重复数据，以最新数据为准
		sql.append("delete from tab_mac_company where mac in(select mac from tab_mac_company_temp) ");
		sqlList.add(sql.getSQL());
		
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			int i=DBOperation.executeUpdate(sqlList.toArray(new String[0]));
			sqlList.clear();
			
			if(i==1){
				sql=null;
				sql=new PrepareSQL("select mac,company_name from tab_mac_company_temp ");
				List<HashMap<String,String>> l=DBOperation.getRecords(sql.getSQL());
				if(l!=null && !l.isEmpty())
				{
					sql=new PrepareSQL("insert into tab_mac_company(mac,company_name) values(?,?) ");
					for(HashMap<String,String> m:l){
						sql.setString(1,StringUtil.getStringValue(m,"mac"));
						sql.setString(2,StringUtil.getStringValue(m,"company_name"));
						
						sqlList.add(sql.getSQL());
						if(sqlList.size()==200){
							i=DBOperation.executeUpdate(sqlList.toArray(new String[0]));
							sqlList.clear();
							if(i!=1){
								return i;
							}
						}
					}
				}
				
				if(sqlList.size()>0){
					i=DBOperation.executeUpdate(sqlList.toArray(new String[0]));
					sqlList.clear();
				}
			}
			sqlList=null;
			return i;
		}else{
			sql=null;
			sql=new PrepareSQL();
			//5、临时表数据入正式表
			sql.append("insert into tab_mac_company(mac,company_name) ");
			sql.append("select mac,company_name from tab_mac_company_temp ");
			sqlList.add(sql.getSQL());
			
			return DBOperation.executeUpdate(sqlList.toArray(new String[0]));
		}
	}
	
	public static SimpleDateFormat getFormat()
	{
		return format;
	}
	
}
