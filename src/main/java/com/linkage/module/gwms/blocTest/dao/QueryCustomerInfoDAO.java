
package com.linkage.module.gwms.blocTest.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-8-4 上午09:13:16
 * @category com.linkage.module.gwms.blocTest.dao
 * @copyright 南京联创科技 网管科技部
 */
public class QueryCustomerInfoDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(QueryCustomerInfoDAO.class);

	public List queryCustomerInfo(String device_id){
		
		logger.debug("QueryCustomerInfoDAO==>queryCustomerInfo()", device_id);
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select c.customer_id, ec.user_id,ec.username,ec.device_serialnumber, c.customer_name,c.linkphone,c.customer_address,c.zone_id,c.office_id,ec.bandwidth " );
		psql.append("  from tab_customerinfo c,tab_egwcustomer ec ");
		psql.append("  where c.customer_id=ec.customer_id ");
		psql.append(" and ec.device_id='" + device_id + "'");
		
		
		return jt.queryForList(psql.getSQL());
	}
}
