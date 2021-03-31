
package com.linkage.module.itms.report.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author guankai (Ailk No.300401)
 * @version 1.0
 * @since 2020年4月20日
 * @category com.linkage.module.itms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class GroupManageDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(GroupManageDAO.class);


	public List<Map> getGroupManageList(String status, String cityId, int curPage_splitPage, int num_splitPage) {

		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select t.oui,t.device_serialnumber,");
		if(DBUtil.GetDB()==3){
			//TODO wait
			sqlStr.append("concat(t.oui,'-',t.device_serialnumber) as osn,");
		}else{
			sqlStr.append("t.oui || '-' || t.device_serialnumber as osn,");
		}
		
		sqlStr.append("t6.username,t2.vendor_name,t3.device_model,");
		sqlStr.append("t5.LINKMAN,t5.LINKADDRESS,t5.MOBILE,");
		sqlStr.append("t5.remark userremark,t4.hardwareversion,");
		sqlStr.append("t4.softwareversion,t.loopback_ip,t1.city_name,t.remark, ");
		if(DBUtil.GetDB()==3){
			sqlStr.append("CASE WHEN cast(t6.open_status as char) = '1' THEN '注册' ELSE '绑定' END openstatus, ");
		}else{
			sqlStr.append("CASE WHEN to_char(t6.open_status) = '1' THEN '注册' ELSE '绑定' END openstatus, ");
		}
		
		sqlStr.append("t5.username loid ");
		sqlStr.append("from tab_gw_device t ");
		sqlStr.append("left join tab_city t1 on t.city_id = t1.city_id ");
		sqlStr.append("left join tab_vendor t2 on t2.vendor_id = t.VENDOR_ID ");
		sqlStr.append("left join gw_device_model t3 on t3.device_model_id = t.device_model_id ");
		sqlStr.append("left join tab_devicetype_info t4 on t4.devicetype_id = t.devicetype_id ");
		sqlStr.append("left join tab_hgwcustomer t5 on t.customer_id = t5.user_id ");
		sqlStr.append("left join hgwcust_serv_info t6 on (t5.user_id = t6.user_id ) ");
		sqlStr.append("where (t6.serv_type_id = 10 or t6.serv_type_id is null) ");
		if(!"00".equals(cityId)){
			sqlStr.append(" and t.city_id = ? ");
		}
		if("-1".equals(status)) {
			sqlStr.append(" and  (t6.open_status <> 1 or t6.serv_type_id is null)");
		}else if("1".equals(status)){
			sqlStr.append(" and  t6.open_status = 1 ");
		}
		PrepareSQL pSQL = new PrepareSQL(sqlStr.toString());
		pSQL.setString(1,cityId);
		List<Map> list = querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage);

		return list;
	}

	public int getGroupManageCount(String status, String cityId) 
	{
		StringBuffer sqlStr = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sqlStr.append("select  count(*) ");
		}else{
			sqlStr.append("select  count(1) ");
		}
		
		sqlStr.append("  from tab_gw_device t ");
		sqlStr.append("  left join tab_city t1 on t.city_id = t1.city_id ");
		sqlStr.append("  left join tab_vendor t2 on t2.vendor_id = t.VENDOR_ID ");
		sqlStr.append("  left join gw_device_model t3  on t3.device_model_id = t.device_model_id ");
		sqlStr.append("  left join tab_devicetype_info t4 on t4.devicetype_id = t.devicetype_id ");
		sqlStr.append("  left join tab_hgwcustomer t5 on t.customer_id = t5.user_id ");
		sqlStr.append("  left join hgwcust_serv_info t6 on (t5.user_id = t6.user_id ) ");
		sqlStr.append(" where 1=1 and (t6.serv_type_id = 10 or t6.serv_type_id is null) ");
		if(!"00".equals(cityId)){
			sqlStr.append(" and t.city_id = ? ");
		}
		if("-1".equals(status)) {
			sqlStr.append(" and  (t6.open_status <> 1 or t6.serv_type_id is null)");
		}else if("1".equals(status)){
			sqlStr.append(" and  t6.open_status = 1 ");
		}
		PrepareSQL pSQL = new PrepareSQL(sqlStr.toString());
		if(!"00".equals(cityId)){
			pSQL.setString(1,cityId);
		}
		return jt.queryForInt(pSQL.getSQL());
	}

	public List<Map> getDetailExcel(String cityId, String status) {
		logger.warn("cityId:"+cityId);
		logger.warn("status:"+status);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select t.oui,t.device_serialnumber, ");
		if(DBUtil.GetDB()==3){
			//TODO wait
			sqlStr.append("       concat(t.oui,'-',t.device_serialnumber) as osn, ");
		}else{
			sqlStr.append("       t.oui || '-' || t.device_serialnumber as osn, ");
		}
		
		sqlStr.append("t6.username,t2.vendor_name,t3.device_model,t5.LINKMAN,");
		sqlStr.append("t5.LINKADDRESS,t5.MOBILE,t5.remark userremark,t4.hardwareversion,");
		sqlStr.append("t4.softwareversion,t.loopback_ip,t1.city_name,t.remark,");
		if(DBUtil.GetDB()==3){
			sqlStr.append("CASE WHEN cast(t6.open_status as char) = '1' THEN '注册' ELSE '绑定' END openstatus , ");
		}else{
			sqlStr.append("CASE WHEN to_char(t6.open_status) = '1' THEN '注册' ELSE '绑定' END openstatus , ");
		}
		
		sqlStr.append("t5.username loid ");
		sqlStr.append("  from tab_gw_device t ");
		sqlStr.append("  left join tab_city t1 on t.city_id = t1.city_id ");
		sqlStr.append("  left join tab_vendor t2 on t2.vendor_id = t.VENDOR_ID ");
		sqlStr.append("  left join gw_device_model t3 on t3.device_model_id = t.device_model_id ");
		sqlStr.append("  left join tab_devicetype_info t4 on t4.devicetype_id = t.devicetype_id ");
		sqlStr.append("  left join tab_hgwcustomer t5 on t.customer_id = t5.user_id ");
		sqlStr.append("  left join hgwcust_serv_info t6 on (t5.user_id = t6.user_id  ) ");
		sqlStr.append(" where 1=1 and (t6.serv_type_id = 10 or t6.serv_type_id is null) ");
		if(!"00".equals(cityId)){
			sqlStr.append(" and t.city_id = ? ");
		}
		if("-1".equals(status)) {
			sqlStr.append(" and  (t6.open_status <> 1 or t6.serv_type_id is null)");
		}else if("1".equals(status)){
			sqlStr.append(" and  t6.open_status = 1 ");
		}
		PrepareSQL pSQL = new PrepareSQL(sqlStr.toString());
		if(!"00".equals(cityId)){
			pSQL.setString(1,cityId);
		}
		List<Map> list = jt.queryForList(pSQL.getSQL());
		return list;
	}
}
