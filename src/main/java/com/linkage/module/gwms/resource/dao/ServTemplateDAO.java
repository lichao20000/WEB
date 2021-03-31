/**
 * 
 */

package com.linkage.module.gwms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.obj.TreeNode;

/**
 * @author chenjie
 */
public class ServTemplateDAO extends SuperDAO
{

	// 日志记录
	private static final Logger logger = LoggerFactory.getLogger(ServTemplateDAO.class);

	public List<Map> queryTemplateList(String name, String vlan, String serv, int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sb = new StringBuffer();
		String sql = null;
		sql = "select * from tab_serv_template where gw_type='4' ";
		sb.append(sql);
		/*if (!StringUtil.IsEmpty(vlan)){
			sb.append(" and vlanid='" + vlan + "'");
		}*/
		if (!StringUtil.IsEmpty(name)){
			sb.append(" and name like '%" + name + "%'");
		}
		/*if (!StringUtil.IsEmpty(serv)){
			sb.append(" and serv=" + serv + "");
		}*/
		
		sb.append(" order by UPDATE_TIME desc");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("serv", getServName(rs.getString("serv")));
				map.put("name", rs.getString("name"));
				map.put("describe", rs.getString("describe"));
				map.put("vlanid", rs.getString("vlanid"));
				map.put("vendor_id", rs.getString("vendor_id"));
				map.put("device_model_id", rs.getString("device_model_id"));
				map.put("devicetype_id", rs.getString("devicetype_id"));
				map.put("update_time", new DateTimeUtil(StringUtil.getLongValue(rs.getString("update_time"))*1000).getLongDate());
				return map;
			}
		});
		return list;
	}
	
	public List<HashMap<String, String>> queryTemplateList(String name, String vlan, String serv)
	{
		StringBuffer sb = new StringBuffer();
		String sql = null;
		sql = "select * from tab_serv_template where gw_type='4' ";
		sb.append(sql);
		if (!StringUtil.IsEmpty(vlan)){
			sb.append(" and vlanid='" + vlan + "'");
		}
		if (!StringUtil.IsEmpty(name)){
			sb.append(" and name like '%" + name + "%'");
		}
		if (!StringUtil.IsEmpty(serv)){
			sb.append(" and serv=" + serv + "");
		}
		List<HashMap<String, String>> list = DBOperation.getRecords(sb.toString());
		return list;
	}
	
	/**
	 * 查询模板
	 * @param id
	 * @return
	 */
	public Map<String, String> queryTemplate(int id)
	{
		PrepareSQL pSQL = new PrepareSQL("select * from tab_serv_template where id=?");
		pSQL.setInt(1, id);

		return DBOperation.getRecord(pSQL.getSQL());
	}
	
	/**
	 * 查询模板参数
	 * @param id
	 * @return
	 */
	public ArrayList<HashMap<String, String>> queryTemplateParam(int id)
	{
		PrepareSQL pSQL = new PrepareSQL("select * from tab_serv_template_param where template_id=?");
		pSQL.setInt(1, id);

		return DBOperation.getRecords(pSQL.getSQL());
	}
	
	
	public int queryTemplateListCount(String name, String vlan, String serv, int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sb = new StringBuffer();
		String sql = "select count(*) from tab_serv_template where gw_type='4' ";
		sb.append(sql);
		/*if (!StringUtil.IsEmpty(vlan)){
			sb.append(" and vlanid='" + vlan + "'");
		}*/
		if (!StringUtil.IsEmpty(name)){
			sb.append(" and name like '%" + name + "%'");
		}
		/*if (!StringUtil.IsEmpty(serv)){
			sb.append(" and serv=" + serv + "");
		}*/
		PrepareSQL psql = new PrepareSQL(sb.toString());
		int total = jt.queryForInt(psql.getSQL());
		logger.warn("total" + total);
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	private String getServName(String serv){
		if("10".equals(serv)){
			return "宽带";
		}
		else if("11".equals(serv)){
			return "IPTV";
		}
		else if("14".equals(serv)){
			return "语音";
		}
		else if("0".equals(serv)){
			return "全部";
		}
		else{
			return "其他";
		}
	} 

	public void deleteDevice(int id)
	{
		logger.debug("deleteDevice({})", new Object[] { id });
		String[] sql = new String[3];
		sql[0] = "delete from tab_serv_template where id=" + id;
		sql[1] = "delete from tab_serv_template_param where TEMPLATE_ID=" + id;
		sql[2] = "delete from tab_service where service_id=" + (id+4000);
		jt.batchUpdate(sql);
	}

	/**
	 * 更新模板表，模板参数表
	 * @param templateParams
	 * @param id
	 * @param name 
	 * @param describe 
	 * @param vlan 
	 * @param serv 
	 * @return
	 */
	public int saveTemplate(List<TreeNode> templateParams, int id, String serv, String vlan, String describe, String name){
		String[] sqls = new String[2+templateParams.size()];
		PrepareSQL sql1 = new PrepareSQL("update tab_serv_template set UPDATE_TIME=?,name=?,serv=?,vlanid=?,describe=? where id=?");
		sql1.setLong(1, new DateTimeUtil().getLongTime());
		sql1.setString(2, name);
		sql1.setInt(3, StringUtil.getIntegerValue(serv));
		sql1.setString(4, vlan);
		sql1.setString(5, describe);
		sql1.setInt(6, id);
		
		PrepareSQL sql2 = new PrepareSQL("delete from tab_serv_template_param where TEMPLATE_ID=?");
		sql2.setInt(1, id);
		sqls[0] = sql1.getSQL();
		sqls[1] = sql2.getSQL();
		
		for(int i=0;i<templateParams.size();i++){
			PrepareSQL sql = new PrepareSQL("insert into tab_serv_template_param(TEMPLATE_ID, PATH, VALUE, TYPE, PRIORITY) values (?,?,?,?,?)");
			sql.setLong(1, id);
			sql.setString(2, templateParams.get(i).getPath());
			sql.setString(3, templateParams.get(i).getValue());
			sql.setInt(4, StringUtil.getIntegerValue(templateParams.get(i).getType()));
			sql.setInt(5, StringUtil.getIntegerValue(templateParams.get(i).getPriority()));
			sqls[i+2] = sql.getSQL();
		}

		return DBOperation.executeUpdate(sqls);
	}
	
	
	/**
	 * 新增模板表，模板参数表(service_id=template_id+4000)
	 * @param templateParams
	 * @param id
	 * @param name 
	 * @param describe 
	 * @param vlan 
	 * @param serv 
	 * @param service_id 
	 * @param sport_del 
	 * @param sserv_svlan_del 
	 * @param sserv_del 
	 * @param nserv_svlan_del 
	 * @return
	 */
	public int addTemplate(List<TreeNode> templateParams, int id, String serv, String vlan, String describe, String name, String nserv_svlan_del, String sserv_del, String sserv_svlan_del, String sport_del, String service_id){
		String[] sqls = new String[2+templateParams.size()];
		if(!StringUtil.IsEmpty(service_id)){
			sqls = new String[1+templateParams.size()];
		}
		StringBuffer sf = new StringBuffer("insert into tab_serv_template(UPDATE_TIME,name,serv,vlanid,describe,id,gw_type");
		if(!StringUtil.IsEmpty(service_id)){
			sf.append(", nserv_svlan_del, sserv_del, sserv_svlan_del, sport_del, service_id) values (?,?,?,?,?,?,?,?,?,?,?,?)");
		}
		else{
			sf.append(") values (?,?,?,?,?,?,?)");
		}
		PrepareSQL sql1 = new PrepareSQL(sf.toString());
		sql1.setLong(1, new DateTimeUtil().getLongTime());
		sql1.setString(2, name);
		sql1.setInt(3, StringUtil.getIntegerValue(serv));
		sql1.setString(4, vlan);
		sql1.setString(5, describe);
		sql1.setInt(6, id);
		sql1.setString(7, "4");
		if(!StringUtil.IsEmpty(service_id)){
			sql1.setInt(8, StringUtil.getIntegerValue(nserv_svlan_del));
			sql1.setInt(9, StringUtil.getIntegerValue(sserv_del));
			sql1.setInt(10, StringUtil.getIntegerValue(sserv_svlan_del));
			sql1.setInt(11, StringUtil.getIntegerValue(sport_del));
			sql1.setInt(12, StringUtil.getIntegerValue(service_id));
		}
		
		PrepareSQL sql2 = new PrepareSQL("insert into tab_service(SERVICE_ID,SERVICE_DESC) values (?,'BatchSetTemplate')");
		sql2.setInt(1, id + 4000);
		
		sqls[0] = sql1.getSQL();
		
		if(!StringUtil.IsEmpty(service_id)){
			for(int i=0;i<templateParams.size();i++){
				PrepareSQL sql = new PrepareSQL("insert into tab_serv_template_param(TEMPLATE_ID, PATH, VALUE, TYPE, PRIORITY) values (?,?,?,?,?)");
				sql.setLong(1, id);
				sql.setString(2, templateParams.get(i).getPath());
				sql.setString(3, templateParams.get(i).getValue());
				sql.setInt(4, StringUtil.getIntegerValue(templateParams.get(i).getType()));
				sql.setInt(5, StringUtil.getIntegerValue(templateParams.get(i).getPriority()));
				sqls[i+1] = sql.getSQL();
			}
		}
		else{
			sqls[1] = sql2.getSQL();
			
			for(int i=0;i<templateParams.size();i++){
				PrepareSQL sql = new PrepareSQL("insert into tab_serv_template_param(TEMPLATE_ID, PATH, VALUE, TYPE, PRIORITY) values (?,?,?,?,?)");
				sql.setLong(1, id);
				sql.setString(2, templateParams.get(i).getPath());
				sql.setString(3, templateParams.get(i).getValue());
				sql.setInt(4, StringUtil.getIntegerValue(templateParams.get(i).getType()));
				sql.setInt(5, StringUtil.getIntegerValue(templateParams.get(i).getPriority()));
				sqls[i+2] = sql.getSQL();
			}
		}

		return DBOperation.executeUpdate(sqls);
	}
	
	
	
	/**
	 * 获取最大的模板id
	 * tab_serv_template模板表id=tab_service表service_id值减去4000
	 * @return
	 */
	public int getMaxId()
	{
		String sql = "select max(id) from tab_serv_template";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForInt(psql.getSQL());
	}

}
