
package com.linkage.module.gwms.blocTest.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-8-10 下午02:27:31
 * @category com.linkage.module.gwms.blocTest.dao
 * @copyright 南京联创科技 网管科技部
 */
public class EgwExpertDAO extends SuperDAO
{ 

	public List queryAll(int id ,String name)
	{
		PrepareSQL pSql = new PrepareSQL();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			//无/gwms/blocTest/EgwExpert.jsp
		} */
		pSql.append("select * from gw_egw_expert where 1=1");
		if (!StringUtil.IsEmpty(name)){
			pSql.append(" and ex_name='"+name+"'");
		}
		if (id != 0){
			pSql.append(" and id="+id);
		}
		
		return jt.queryForList(pSql.getSQL());
	}
	
	public Map queryOne(int id)
	{
		PrepareSQL pSql = new PrepareSQL();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			///无gwms/blocTest/EgwExpertEdit.jsp
		} */
		pSql.append("select * from gw_egw_expert where id="+id);
		return	(Map)(jt.queryForList(pSql.getSQL()).get(0));
	}
	
	public int update(int id,String ex_regular,String ex_name,String ex_bias,
			String ex_succ_desc,String ex_fault_desc,String ex_suggest,String ex_desc)
	{
		if(ex_regular.equals("2")){
			ex_regular=">";	
		}
		if(ex_regular.equals("1")){
			ex_regular="<";	
		}
		if(ex_regular.equals("0")){
			ex_regular="=";	
		}
		
		String sql="update  gw_egw_expert set ex_regular='"+ex_regular+"',"
					+ "ex_name='"+ex_name+"',"
					+ "ex_bias='"+ex_bias+"',"
					+ "ex_succ_desc='"+ex_succ_desc+"',"
					+ "ex_fault_desc='"+ex_fault_desc+"',"
					+ "ex_suggest='"+ex_suggest+"',"
					+ "ex_desc='"+ex_desc+"' "
					+ "where id="+id;
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL(sql);
		return	jt.update(pSql.getSQL());
	}
	
	
}
