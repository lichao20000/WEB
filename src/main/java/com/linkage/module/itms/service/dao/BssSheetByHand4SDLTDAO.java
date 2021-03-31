package com.linkage.module.itms.service.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.itms.service.obj.SheetObj;

public class BssSheetByHand4SDLTDAO extends SuperDAO{

	/**
	 * 查询终端规格
	 * @return
	 */
	public List<HashMap<String, String>> getSpecId()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  lan_num, wlan_num, spec_name, id from tab_bss_dev_port");

		return DBOperation.getRecords(psql.getSQL());
	}

	/**
	 * 根据loid查询用户信息
	 * @param loid
	 * @return
	 */
	public Map<String, String> getUserInfo(String loid,String cityId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_id, office_id, zone_id, access_style_id, linkman, linkphone, email, mobile, linkaddress, credno " +
				"from tab_hgwcustomer");
		psql.append(" where username=? ");
		psql.setString(1, loid);
		if(cityId!=null){
			psql.append(" and city_id in("+cityId+") ");
		}


		return DBOperation.getRecord(psql.getSQL());
	}

	/**
	 * 根据loid查询用户宽带业务信息
	 * @param loid
	 * @return
	 */
	public Map<String, String> getServInfo(String loid,String cityId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.username, b.passwd, b.vlanid, b.wan_type, b.ipaddress, b.gateway, b.adsl_ser " +
				"from tab_hgwcustomer a,hgwcust_serv_info b");
		psql.append(" where a.username =? and a.user_id=b.user_id and b.serv_type_id='10' ");
		psql.setString(1,loid);
		if(cityId!=null){
			psql.append(" and a.city_id in("+cityId+") ");
		}

		return DBOperation.getRecord(psql.getSQL());
	}

	/**
	 * 新增工单入库
	 */
	public void addHandSheetLog(SheetObj userInfoSheet,UserRes curUser,int servType,int operType,int resultId,String resultDesc)
	{
		User user = curUser.getUser();
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into tab_handsheet_log (id,username,city_id,serv_type_id,oper_type,result_id,result_desc,oper_time,occ_id,occ_ip) values(?,?,?,?,?,?,?,?,?,?) ");
		psql.setString(1, user.getId()+StringUtil.getStringValue(Math.round(Math.random() * 1000000000000L)));
		psql.setString(2, userInfoSheet.getLoid());
		psql.setString(3, userInfoSheet.getCityId());
		psql.setInt(4, servType);
		psql.setInt(5, operType);
		psql.setInt(6, resultId);
		psql.setString(7, resultDesc);
		psql.setLong(8, new Date().getTime()/1000);
		psql.setLong(9, user.getId());
		psql.setString(10, "");
		DBOperation.executeUpdate(psql.getSQL());
	}

}
