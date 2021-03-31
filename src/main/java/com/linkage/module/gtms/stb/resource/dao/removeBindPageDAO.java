package com.linkage.module.gtms.stb.resource.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gtms.stb.resource.action.removeBindPageACT;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-2-2
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class removeBindPageDAO extends SuperDAO
{ 
	private static Logger logger = LoggerFactory.getLogger(removeBindPageACT.class);
	
	private static String writeLogSql = "insert into tab_oper_log("
			+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_name"
			+ ",operation_object,operation_content,operation_device"
			+ ",operation_result,result_id) values(?,?,?,?,?,?,?,?,?,?)";
	
	public List<Map> getcity(String mac,String servaccount,String cityId )
	{
		logger.debug("");
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id from stb_tab_customer where 1=1");
		if(!StringUtil.IsEmpty(servaccount))
		{
			sql.append(" and serv_account='" + servaccount + "'");
		}else if(!StringUtil.IsEmpty(mac)){
			sql.append(" and cpe_mac='" + mac + "'");
		}
		List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		sql.append(" and city_id in ("+StringUtils.weave(cityIdList)+")");
		
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		return jt.queryForList(pSQL.getSQL());
	}
	
	public List<Map> getMacList(String mac,String servaccount,String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,serv_account,cpe_mac,sn from stb_tab_customer where 1=1");
		if(!StringUtil.IsEmpty(servaccount))
		{
			sql.append(" and serv_account like'%" + servaccount + "%'");
		}
		else if(!StringUtil.IsEmpty(mac))
		{
			sql.append(" and cpe_mac like'%" + mac + "'");
		}
		List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		sql.append(" and city_id in ("+StringUtils.weave(cityIdList)+")");
		
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		return jt.queryForList(pSQL.getSQL());
	}
	
	public int queryCustomer(String mac,String servaccount)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select customer_id ");
		pSQL.append("from stb_tab_customer ");
		pSQL.append("where 1=1 ");
		if(!StringUtil.IsEmpty(servaccount) && !"null".equals(servaccount.toLowerCase())){
			pSQL.append("and serv_account='"+servaccount+"' ");
		}
		if(!StringUtil.IsEmpty(mac) && !"null".equals(mac.toLowerCase())){
			pSQL.append("and cpe_mac='"+mac.replaceAll(":","").toUpperCase()+"' ");
		}
		List<Map> list=jt.queryForList(pSQL.getSQL());
		return list==null||list.isEmpty()?0:1;
	}
	
	public void addLogUnBind(long user_id, String user_ip, String city_id,
			String mac, String servaccount) 
	{
		int i=queryCustomer(mac,servaccount);
			PrepareSQL psql = new PrepareSQL();
			psql.setSQL(writeLogSql);
			psql.setLong(1,user_id);
			psql.setString(2,user_ip);
			psql.setInt(3,1);
			psql.setLong(4,System.currentTimeMillis()/1000L);
			psql.setString(5,"5");
			psql.setString(6,"WEB");
			
			StringBuffer sb=new StringBuffer();
			sb.append("用户["+servaccount+"]与机顶盒["+mac.replace(":","").toUpperCase()+"]解绑");
			
			psql.setString(7,sb.toString());
			psql.setString(8,"Web");
			
			psql.setString(9,i>0?"失败":"成功");
			psql.setInt(10,1);
			DBOperation.executeUpdate(psql.getSQL());
	}
	
}
