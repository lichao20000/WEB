package com.linkage.module.gwms.dao.tabquery;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-12-26
 * @category com.linkage.module.gwms.dao.tabquery
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class UserTypeDAO
{
	//日志记录
	private static Logger m_logger = LoggerFactory.getLogger(UserTypeDAO.class);
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getUserTypeIdNameMapCore(){
		
		m_logger.debug("getAreaIdPidMapCore()");
		
		String strSQL = "select user_type_id,type_name from user_type";
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		Map map = DataSetBean.getMap(strSQL);
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}
}
