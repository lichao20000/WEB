/**
 *
 */
package com.linkage.module.gtms.stb.report.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 工单报表
 * fanjm
 * @author HP (AILK No.)
 * @version 1.0
 * @since 2018-4-20
 * @category com.linkage.module.gtms.stb.report.dao
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class BatchImpQryStbInfoDAO extends SuperDAO{



	public void truncate()
	{
		PrepareSQL psql = new PrepareSQL("truncate table tab_temp_import");
		jt.update(psql.getSQL());
	}


	/**
	 * 批量插入导入的数据
	 * @param dataList
	 */
	public void insertDate(List<String> dataList)
	{
		List<String> sqlL = new ArrayList<String>();
		PrepareSQL psql = new PrepareSQL();
		String[] sqlS;
		//五百一次执行批量插入
		for(int i=0,index=1;i<dataList.size();i++,index++){
			psql.setSQL("insert into tab_temp_import values('"+dataList.get(i)+"')");
			sqlL.add(psql.getSQL());
			if(index>=500){
				sqlS = new String[sqlL.size()];
				sqlL.toArray(sqlS);
				jt.batchUpdate(sqlS);
				sqlS = null;
				sqlL.clear();
			}
		}
		if(null!=sqlL && sqlL.size()>0){
			sqlS = new String[sqlL.size()];
			sqlL.toArray(sqlS);
			jt.batchUpdate(sqlS);
			sqlS = null;
			sqlL.clear();
		}

	}





	public List<Map> getResultPage(int curPage_splitPage, int num_splitPage,
			String importQueryField)
	{// TODO wait (more table related)
		StringBuffer plsql = new StringBuffer("select a.serv_account, d.device_serialnumber,d.city_id,v.vendor_name,m.device_model,t.hardwareversion,t.softwareversion," +
				"d.cpe_currentupdatetime from stb_tab_gw_device d left join stb_tab_customer a on a.customer_id=d.customer_id " +
				"left join stb_tab_vendor v on v.vendor_id=d.vendor_id left join stb_gw_device_model m on d.device_model_id=m.device_model_id left join stb_tab_devicetype_info t on t.devicetype_id = d.devicetype_id " +
				"where exists (select 1 from tab_temp_import i where i.keyvalue=");
		if("device_serialnumber".equals(importQueryField)){
			plsql.append("d.device_serialnumber)");
		}
		else{
			plsql.append("a.serv_account)");
		}
		PrepareSQL psql = new PrepareSQL(plsql.toString());
		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage);
	}

	public List<Map> getResult( String importQueryField)
	{// TODO wait (more table related)
		StringBuffer plsql = new StringBuffer("select a.serv_account, d.device_serialnumber,d.city_id,v.vendor_name,m.device_model,t.hardwareversion,t.softwareversion," +
				"d.cpe_currentupdatetime from stb_tab_gw_device d left join stb_tab_customer a on a.customer_id=d.customer_id " +
				"left join stb_tab_vendor v on v.vendor_id=d.vendor_id left join stb_gw_device_model m on d.device_model_id=m.device_model_id left join stb_tab_devicetype_info t on t.devicetype_id = d.devicetype_id " +
				"where exists (select 1 from tab_temp_import i where i.keyvalue=");
		if("device_serialnumber".equals(importQueryField)){
			plsql.append("d.device_serialnumber)");
		}
		else{
			plsql.append("a.serv_account)");
		}
		PrepareSQL psql = new PrepareSQL(plsql.toString());
		return jt.queryForList(psql.getSQL());
	}





	public int getCount(String importQueryField)
	{// TODO wait (more table related)
		StringBuffer plsql = new StringBuffer("select count(d.device_serialnumber) from stb_tab_gw_device d left join stb_tab_customer a on a.customer_id=d.customer_id " +
				"left join stb_tab_vendor v on v.vendor_id=d.vendor_id left join stb_gw_device_model m on d.device_model_id=m.device_model_id left join stb_tab_devicetype_info t on t.devicetype_id = d.devicetype_id " +
				"where exists (select 1 from tab_temp_import i where i.keyvalue=");
		if("device_serialnumber".equals(importQueryField)){
			plsql.append("d.device_serialnumber)");
		}
		else{
			plsql.append("a.serv_account)");
		}
		PrepareSQL psql = new PrepareSQL(plsql.toString());
		return jt.queryForInt(psql.getSQL());
	}



	public List<Map> getResultPageItms(int curPage_splitPage, int num_splitPage,
			String importQueryField)
	{// TODO wait (more table related)
		StringBuffer plsql = new StringBuffer();
		if("username".equals(importQueryField)){
			plsql.append("select a.username loid, b.username broadbandname, d.device_serialnumber,d.city_id,v.vendor_name,m.device_model,d.cpe_currentupdatetime,t.hardwareversion,t.softwareversion " +
					"from tab_gw_device d " +
					"inner join tab_hgwcustomer a on a.device_id=d.device_id " +
					"inner join hgwcust_serv_info b on b.user_id=a.user_id and b.serv_type_id=10 " +
					"left join tab_vendor v on v.vendor_id=d.vendor_id " +
					"left join gw_device_model m on d.device_model_id=m.device_model_id left join tab_devicetype_info t on t.devicetype_id = d.devicetype_id " +
					"where exists (select 1 from tab_temp_import i where i.keyvalue=b.username)");
		}
		else{// TODO wait (more table related)
			plsql.append("select a.username loid, b.username broadbandname, d.device_serialnumber,d.city_id,v.vendor_name,m.device_model,t.hardwareversion,t.softwareversion," +
					"d.cpe_currentupdatetime from tab_gw_device d left join tab_hgwcustomer a on a.device_id=d.device_id left join hgwcust_serv_info b on b.user_id=a.user_id and b.serv_type_id=10 " +
					"left join tab_vendor v on v.vendor_id=d.vendor_id left join gw_device_model m on d.device_model_id=m.device_model_id left join tab_devicetype_info t on t.devicetype_id = d.devicetype_id " +
					"where exists (select 1 from tab_temp_import i where i.keyvalue=");
			if("device_serialnumber".equals(importQueryField)){
				plsql.append("d.device_serialnumber)");
			}
			else{
				plsql.append("a.username)");
			}
		}


		PrepareSQL psql = new PrepareSQL(plsql.toString());
		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage);
	}

	public List<Map> getResultItms( String importQueryField)
	{
		StringBuffer plsql = new StringBuffer();
		if("username".equals(importQueryField)){// TODO wait (more table related)
			plsql.append("select a.username loid,b.username broadbandname,d.device_serialnumber,d.city_id,v.vendor_name,m.device_model,d.cpe_currentupdatetime,t.hardwareversion,t.softwareversion " +
					"from tab_gw_device d " +
					"inner join tab_hgwcustomer a on a.device_id=d.device_id " +
					"inner join hgwcust_serv_info b on b.user_id=a.user_id and b.serv_type_id=10 " +
					"left join tab_vendor v on v.vendor_id=d.vendor_id " +
					"left join gw_device_model m on d.device_model_id=m.device_model_id left join tab_devicetype_info t on t.devicetype_id = d.devicetype_id " +
					"where exists (select 1 from tab_temp_import i where i.keyvalue=b.username)");
		}
		else{// TODO wait (more table related)
			plsql.append("select a.username loid, b.username broadbandname, d.device_serialnumber,d.city_id,v.vendor_name,m.device_model,t.hardwareversion,t.softwareversion," +
					"d.cpe_currentupdatetime from tab_gw_device d left join tab_hgwcustomer a on a.device_id=d.device_id left join hgwcust_serv_info b on b.user_id=a.user_id and b.serv_type_id=10 " +
					"left join tab_vendor v on v.vendor_id=d.vendor_id left join gw_device_model m on d.device_model_id=m.device_model_id left join tab_devicetype_info t on t.devicetype_id = d.devicetype_id " +
					"where exists (select 1 from tab_temp_import i where i.keyvalue=");
			if("device_serialnumber".equals(importQueryField)){
				plsql.append("d.device_serialnumber)");
			}
			else{
				plsql.append("a.username)");
			}
		}
		PrepareSQL psql = new PrepareSQL(plsql.toString());
		return jt.queryForList(psql.getSQL());
	}





	public int getCountItms(String importQueryField)
	{
		StringBuffer plsql = new StringBuffer();
		if("username".equals(importQueryField)){// TODO wait (more table related)
			plsql.append("select count(d.device_serialnumber) " +
					"from tab_gw_device d " +
					"inner join tab_hgwcustomer a on a.device_id=d.device_id " +
					"inner join hgwcust_serv_info b on b.user_id=a.user_id and b.serv_type_id=10 " +
					"left join tab_vendor v on v.vendor_id=d.vendor_id " +
					"left join gw_device_model m on d.device_model_id=m.device_model_id left join tab_devicetype_info t on t.devicetype_id = d.devicetype_id " +
					"where exists (select 1 from tab_temp_import i where i.keyvalue=b.username)");
		}
		else{// TODO wait (more table related)
			plsql.append("select count(d.device_serialnumber) from tab_gw_device d left join tab_hgwcustomer a on a.device_id=d.device_id " +
					"left join tab_vendor v on v.vendor_id=d.vendor_id left join gw_device_model m on d.device_model_id=m.device_model_id left join tab_devicetype_info t on t.devicetype_id = d.devicetype_id " +
					"where exists (select 1 from tab_temp_import i where i.keyvalue=");
			if("device_serialnumber".equals(importQueryField)){
				plsql.append("d.device_serialnumber)");
			}
			else{
				plsql.append("a.username)");
			}
		}
		PrepareSQL psql = new PrepareSQL(plsql.toString());
		return jt.queryForInt(psql.getSQL());
	}




}
