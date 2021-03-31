package com.linkage.module.gwms.dao.tabquery;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.tabquery.ExpertOBJ;

/**
 * @author Jason(3412)
 * @date 2009-9-4
 */
public class ExpertDAO extends SuperDAO{

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ExpertDAO.class);
	
	
	/**
	 * 获取专家建议列表
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-4
	 * @return List
	 */
	public List queryExpertList()
	{
		logger.debug("queryExpertList()");
		List rlist = null;
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select id,ex_fault_desc,ex_suggest,ex_desc from gw_expert");
		}else{
			psql.append("select * from gw_expert");
		}
		rlist = jt.queryForList(psql.getSQL());
		if(null == rlist || rlist.size() <= 0){
			logger.warn("rlist if empty");
		}
		return rlist;
	}
	
	
	/**
	 * 更新专家建议记录
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-4
	 * @return int
	 */
	public int updateExpert(ExpertOBJ obj){
		logger.debug("updateExpert()");
		String strSQL = "update gw_expert set ex_fault_desc=? , ex_suggest=?"
				+ " where id=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, obj.getExFaultDesc());
		psql.setString(2, obj.getExSuggest());
		psql.setStringExt(3, obj.getId(), false);
		int i = (jt.update(psql.getSQL())>=0)? 1:-1;
		if(i < 0){
			logger.warn("update failture ...");
		}
		return i; 
	}
}
