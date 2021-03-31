package com.linkage.module.gtms.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class SuperRoleDAOImpl extends SuperDAO implements SuperRoleDAO {
	private static Logger logger = LoggerFactory.getLogger(SuperRoleDAOImpl.class);
	/**
	 * 增加超级权限
	 * List treeList 选中的角色或是用户id
	 * lMaxId 当前超级权限id+1
	 * auth_name 超级权限名称
	 * auth_code 权限简码
	 * auth_desc 描述
	 * relation_type 关联类型 ，0：用户  1 ：角色
	 */
	@Override
	public boolean addSuperRole(List treeList, long lMaxId, String auth_name,
			String auth_code, String auth_desc, String relation_type) {
		logger.warn("addSuperRole({})",treeList.size());
		int len = treeList.size() ;
		String[] sqlArray = new String[len+1];

		PrepareSQL sql = new PrepareSQL();

		for(int i=0;i<treeList.size();i++){
			sql.setSQL(" insert into t_sys_authrelation values (?,?,?)");
			sql.setLong(1, lMaxId);
			sql.setInt(2, StringUtil.getIntegerValue(relation_type));
			sql.setLong(3, StringUtil.getLongValue(treeList.get(i)));
			sqlArray[i] = sql.getSQL();
		}
		sql.setSQL("insert into t_sys_auth values(?,?,?,?)");
		sql.setLong(1, lMaxId);
		sql.setString(2, auth_name);
		sql.setString(3, auth_code);
		sql.setString(4, auth_desc);
		sqlArray[len] = sql.getSQL();
		int [] result = jt.batchUpdate(sqlArray);
		return (result != null && result[0] > 0) ? true : false;
	}
	@SuppressWarnings({  "unchecked" })
	public List getTreeAll(String relation_type) {
		// TODO Auto-generated method stub
		logger.warn("getTreeAll({})",relation_type);
		String sql = "";
		List list = new ArrayList();
		if(relation_type.equals("1")){
			sql = "select role_id as tree_id from tab_role";
		}else{
			sql = "select acc_oid as tree_id from tab_accounts ";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		List lt = jt.queryForList(psql.getSQL());
		Map map = null;
		for (int i =0 ;i<lt.size();i++){
			map = (Map)lt.get(i);
			list.add(map.get("tree_id"));
		}
		return list;
	}
	/**
	 * 检验权限简码是否存在
	 */
	public boolean checkName(String auth_code) {
		PrepareSQL sql = new PrepareSQL("select count(*) from t_sys_auth where auth_code=?");
		sql.setString(1, auth_code);
		int result = jt.queryForInt(sql.getSQL());
		logger.warn("checkName()-->result="+result);
		return result>0?false:true;
	}
	/**
	 * 获取记录
	 */
	public List getAllRecord(String auth_name, String auth_code,
			String relation_type, String user_name,
			int curPageSplitPage, int numSplitPage) {
		String tab_name = " ";
		//默认是用户控制
		if("1".equals(relation_type)){// TODO wait (more table related)
			tab_name="b.role_name from  t_sys_auth a,  tab_role b ,t_sys_authrelation c ";
		}else{// TODO wait (more table related)
			tab_name="b.acc_loginname  from t_sys_auth a,  tab_accounts b, t_sys_authrelation c";
		}
		PrepareSQL sb = new PrepareSQL();
		sb.append(" select a.auth_id, a.auth_name,a.auth_code,c.relation_type,c.relation_id, "+tab_name);
		sb.append(" where a.auth_id=c.auth_id ");
		if("1".equals(relation_type)){
			sb.append(" and b.role_id = c.relation_id");
			sb.append(" and c.relation_type=1");
			if(!StringUtil.IsEmpty(user_name)){
				sb.append(" and b.role_name='"+user_name+"'");
			}
		}else if("0".equals(relation_type)){
			sb.append(" and b.acc_oid = c.relation_id");
			sb.append(" and c.relation_type=0");
			if(!StringUtil.IsEmpty(user_name)){
				sb.append(" and b.acc_loginname='"+user_name+"'");
			}
		}
		if(!StringUtil.IsEmpty(auth_name)){
			sb.append(" and a.auth_name ='"+auth_name+"'");
		}
		if(!StringUtil.IsEmpty(auth_code)){
			sb.append(" and a.auth_code ='"+auth_code+"'");
		}
		List<Map> list = querySP(sb.getSQL(), (curPageSplitPage - 1)
				* numSplitPage + 1, numSplitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				Map<String,String> resMap = new HashMap<String,String>();
				resMap.put("auth_id", StringUtil.getStringValue(rs.getString("auth_id")));
				resMap.put("auth_name", StringUtil.getStringValue(rs.getString("auth_name")));
				resMap.put("auth_code", StringUtil.getStringValue(rs.getString("auth_code")));
				resMap.put("relation_type", StringUtil.getStringValue(rs.getString("relation_type")));
				resMap.put("relation_id", StringUtil.getStringValue(rs.getString("relation_id")));
				String relation_type = StringUtil.getStringValue(rs.getString("relation_type"));
				if("1".equals(relation_type)){
					resMap.put("relation_name","角色控制");
					resMap.put("user_name", StringUtil.getStringValue(rs.getString("role_name")));
				}else{
					resMap.put("relation_name","用户控制");
					resMap.put("user_name", StringUtil.getStringValue(rs.getString("acc_loginname")));

				}
				return resMap;
			}
		});
		return list;
	}
	/**
	 * 最大页数
	 */
	public int getAllCount(String auth_name, String auth_code,
			String relation_type,  String user_name,
			int curPage_splitPage, int numSplitPage) {
		String tab_name = "tab_accounts";
		if("1".equals(relation_type)){
			tab_name=" tab_role";
		}else{
			tab_name=" tab_accounts";
		}
		PrepareSQL sb = new PrepareSQL();// TODO wait (more table related)
		sb.append(" select count(*) from t_sys_auth a , "+tab_name+" b ,t_sys_authrelation c");
		sb.append(" where a.auth_id=c.auth_id ");
		if("1".equals(relation_type)){
			sb.append(" and b.role_id = c.relation_id");
			sb.append(" and c.relation_type=1");
			if(!StringUtil.IsEmpty(user_name)){
				sb.append(" and b.role_name='"+user_name+"'");
			}
		}else if("0".equals(relation_type)){
			sb.append(" and b.acc_oid = c.relation_id");
			sb.append(" and c.relation_type=0");
			if(!StringUtil.IsEmpty(user_name)){
				sb.append(" and b.acc_loginname='"+user_name+"'");
			}
		}
		if(!StringUtil.IsEmpty(auth_name)){
			sb.append(" and a.auth_name ='"+auth_name+"'");
		}
		if(!StringUtil.IsEmpty(auth_code)){
			sb.append(" and a.auth_code ='"+auth_code+"'");
		}

		int total = jt.queryForInt(sb.getSQL());
		int maxPage = 1;
		if (total % numSplitPage == 0){
			maxPage = total / numSplitPage;
		}else{
			maxPage = total / numSplitPage + 1;
		}
		return maxPage;
	}
	/**
	 * 根据auth_id 删除权限
	 */
	public boolean deleteSuperRole(String auth_id,String relation_id) {
		String[] sqlArray = new String[2];
		PrepareSQL sql = new PrepareSQL("delete  from t_sys_authrelation  where auth_id=? and relation_id=?");
		sql.setInt(1, StringUtil.getIntegerValue(auth_id));
		sql.setInt(2, StringUtil.getIntegerValue(relation_id));
		sqlArray[0] = sql.getSQL();
		sql.setSQL("delete  from t_sys_auth where auth_id=? and auth_id not in (select auth_id from t_sys_authrelation)");
		sql.setInt(1, StringUtil.getIntegerValue(auth_id));
		sqlArray[1] = sql.getSQL();
		int[] result = jt.batchUpdate(sqlArray);
		return (result != null && result[0] > 0) ? true : false;
	}
	/**
	 * 根据auth_id获取记录
	 */
	@SuppressWarnings("unchecked")
	public Map preUpdateSuperRole(String auth_id) {
		logger.warn("SuperRoleDAOImpl-->preUpdateSuperRole({})",auth_id);
		Map reMap = new HashMap();
		PrepareSQL sb = new PrepareSQL();
		sb.append(" select a.auth_id, a.auth_name,a.auth_code,a.auth_desc, b.relation_type , b.relation_id ");
		sb.append(" from t_sys_auth a, t_sys_authrelation b where a.auth_id=b.auth_id ");
		sb.append(" and a.auth_id="+auth_id);
		List list = jt.queryForList(sb.getSQL());
		String tree_id_query = "";
		if(null!=list){
			Map tempMap = null;
			tempMap = (Map) list.get(0);
			reMap.put("auth_id", StringUtil.getStringValue(tempMap.get("auth_id")));
			reMap.put("auth_name", StringUtil.getStringValue(tempMap.get("auth_name")));
			reMap.put("auth_code", StringUtil.getStringValue(tempMap.get("auth_code")));
			reMap.put("relation_type", StringUtil.getStringValue(tempMap.get("relation_type")));
			reMap.put("auth_desc", StringUtil.getStringValue(tempMap.get("auth_desc")));
			for(int i=0;i<list.size();i++){
				tempMap = (Map) list.get(i);
				tree_id_query +=StringUtil.getStringValue(tempMap.get("relation_id"))+",";
			}
			if(tree_id_query.length()>1){
				tree_id_query = tree_id_query.substring(0, tree_id_query.length()-1);
			}
			reMap.put("tree_id_query", tree_id_query);
		}

		return reMap;
	}
	/**
	 * 更新
	 */
	@Override
	public boolean updateSuperRole(String auth_id, List<String> treeList,
			String auth_name, String auth_code, String auth_desc,
			String relation_type) {
		logger.warn("addSuperRole({})",treeList.size());
		int len = treeList.size() ;
		String[] sqlArray = new String[len+2];

		PrepareSQL sql = new PrepareSQL();;
		sql.setSQL(" delete from  t_sys_authrelation  where  auth_id=?");
		sql.setInt(1, StringUtil.getIntegerValue(auth_id));
		sqlArray[0] = sql.getSQL();
		for(int i=0;i<treeList.size();i++){
			sql.setSQL(" insert into t_sys_authrelation values (?,?,?)");
			sql.setLong(1,StringUtil.getIntegerValue(auth_id));
			sql.setInt(2, StringUtil.getIntegerValue(relation_type));
			sql.setLong(3, StringUtil.getLongValue(treeList.get(i)));
			sqlArray[i+1] = sql.getSQL();
		}
		sql.setSQL("update t_sys_auth set auth_name=?,auth_code=?,auth_desc=? where auth_id=?");
		sql.setString(1, auth_name);
		sql.setString(2, auth_code);
		sql.setString(3, auth_desc);
		sql.setInt(4, StringUtil.getIntegerValue(auth_id));
		sqlArray[len+1] = sql.getSQL();
		int [] result = jt.batchUpdate(sqlArray);
		return (result != null && result[0] > 0) ? true : false;
	}

}
