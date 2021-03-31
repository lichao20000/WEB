package com.linkage.module.gwms.dao.tabquery;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;

/**
 * @author Jason(3412)
 * @date 2009-11-17
 */
public class CustTypeDAO {
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(CustTypeDAO.class);
	/**
	 * 
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-11-17
	 * @return List
	 */
	public static List getAllCustType(){
		logger.debug("getAllCustType()");
		String strSQL = "select cust_type_id, cust_type_name from tab_customer_type";
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		List custTypeList = DataSetBean.executeQuery(strSQL, null);
		if(null == custTypeList || custTypeList.isEmpty()){
			logger.error("table tab_customer_type is empty");
			return null;
		}
		return custTypeList;
	}
}
