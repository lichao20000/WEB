package com.linkage.module.gtms.stb.resource.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.util.StringUtil;
import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-2-1
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class bindPageDAO extends SuperDAO
{
	private static String writeLogSql = "insert into tab_oper_log("
			+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_name"
			+ ",operation_object,operation_content,operation_device"
			+ ",operation_result,result_id) values(?,?,?,?,?,?,?,?,?,?)";
	
	
	public List<Map> getcity(String mac,String servaccount,String cityId )
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id from stb_tab_customer where 1=1");
		if(!StringUtil.IsEmpty(servaccount))
		{
			sql.append(" and serv_account like'%" + servaccount + "%'");
		}else if(!StringUtil.IsEmpty(mac))
		{
			sql.append(" and cpe_mac like'%" + mac + "%'");
		}
		List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		sql.append(" and city_id in (").append(StringUtils.weave(cityIdList))
		.append(")");
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		return jt.queryForList(pSQL.getSQL());
	}
	
	public List<Map> getMacList(String mac,String servaccount,String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,serv_account,cpe_mac,sn from stb_tab_customer where 1=1");
		if(!StringUtil.IsEmpty(servaccount))
		{
			sql.append(" and serv_account like'%" + servaccount + "'");
		}
		List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		sql.append(" and city_id in (").append(StringUtils.weave(cityIdList))
		.append(")");
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		return jt.queryForList(pSQL.getSQL());
	}
	
	/**
	 * 查询用户
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> queryCustomer(String servaccount,String mac) {
		PrepareSQL psql = new PrepareSQL();
		if(!StringUtil.IsEmpty(servaccount)){
			psql.append("select city_id,cpe_mac,serv_account from stb_tab_customer ");
			psql.append("where serv_account=? ");
			psql.setString(1, servaccount);
		}else if(!StringUtil.IsEmpty(mac)){
			psql.append("select city_id,cpe_mac,serv_account from stb_tab_customer ");
			psql.append("where cpe_mac=? ");
			psql.setString(1,mac);
		}else{
			return null;
		}
		
		return jt.queryForMap(psql.getSQL());
	}
	
	/**
	 * 记录绑定日志
	 */
	public void addLogBind(long user_id, String user_ip, String city_id,
			String mac, String servaccount, String ajax,Map<String,String> map) 
	{
		StringBuffer sb=new StringBuffer();
		if(map==null || map.isEmpty() || 
				(!StringUtil.IsEmpty(map.get("cpe_mac")) && !StringUtil.IsEmpty(map.get("serv_account"))))
		{
			sb.append("用户["+servaccount+"]与机顶盒["+mac.replaceAll(":","").toUpperCase()+"]绑定信息：[]");
		}else{
			Map<String,String> map1=queryCustomer(servaccount,mac);
			
			if(map1!=null && !map1.isEmpty()){
				sb.append("用户["+servaccount+"]与机顶盒["+mac.replaceAll(":","").toUpperCase()+"]绑定信息：[");
				sb=combinStr(sb,"属地","00".equals(map.get("city_id"))?"0010000":map.get("city_id"),
									  "00".equals(map1.get("city_id"))?"0010000":map1.get("city_id"));
				sb=combinStr(sb,"cpe_mac",map.get("cpe_mac"),map1.get("cpe_mac"));
				sb.append("]");
			}
		}
		
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(writeLogSql);
		psql.setLong(1,user_id);
		psql.setString(2,user_ip);
		psql.setInt(3,1);
		psql.setLong(4,System.currentTimeMillis()/1000L);
		psql.setString(5,"5");
		psql.setString(6,"WEB");
		psql.setString(7,sb.toString());
		psql.setString(8,"Web");
		psql.setString(9,"成功".equals(ajax)?"成功":"失败");
		psql.setInt(10,1);
		DBOperation.executeUpdate(psql.getSQL());
	}

	/**
	 * 组装参数
	 */
	public StringBuffer combinStr(StringBuffer sb,String str_name,String old_data,String new_data){
		if(sb.toString().indexOf("->")!=-1){
			sb.append("，");
		}
		
		if(!StringUtil.IsEmpty(new_data)){
			if(StringUtil.IsEmpty(old_data)){
				sb.append(str_name+"： ->"+new_data);
			}else if(!new_data.equals(old_data)){
				sb.append(str_name+"："+old_data+"->"+new_data);
			}else{
				sb.deleteCharAt(sb.toString().length()-1);
			}
		}else{
			if(!StringUtil.IsEmpty(old_data)){
				sb.append(str_name+"："+old_data+"->  ");
			}else{
				sb.deleteCharAt(sb.toString().length()-1);
			}
		}
		
		return sb;
	}
}
