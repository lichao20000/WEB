package com.linkage.module.bbms.dao;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2010-3-11
 */
public class EgwcutomerDAO extends SuperDAO {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(EgwcutomerDAO.class);
	
	
	/**
	 * 根据用户帐号获取(开户或暂停状态)用户对应的客户ID,即customer_id
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2010-3-11
	 * @return String 返回customer_id, 如果无该用户或者customer_id字符为空则返回null
	 */
	public static String getEgwCustIdByUsername(String username) {
		logger.debug("getEgwCustIdByUsername({})", username);
		String customerId = null;
		String strSQL = "select user_id, customer_id"
			+ " from tab_egwcustomer where (user_state='1' or user_state='2')"
			+ " and username=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, username);
		Map userMap = DataSetBean.getRecord(psql.getSQL());
		if(null != userMap && false == userMap.isEmpty()){
			customerId = StringUtil.getStringValue(userMap.get("customer_id"));
		}
		return customerId;
	}

	
	/**
	 * 根据联系电话获取客户ID,即customer_id
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2010-3-22
	 * @return String 返回customer_id, 如果无该用户或者customer_id字符为空则返回null
	 */
	public static String getEgwCustIdByTelepone(String telephone) {
		logger.debug("getEgwCustIdByTelepone({})", telephone);
		String customerId = null;
		String strSQL = "select customer_id"
			+ " from tab_customerinfo where customer_state=1"
			+ " and linkphone=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, telephone);
		Map userMap = DataSetBean.getRecord(psql.getSQL());
		if(null != userMap && false == userMap.isEmpty()){
			customerId = StringUtil.getStringValue(userMap.get("customer_id"));
		}
		return customerId;
	}
}
