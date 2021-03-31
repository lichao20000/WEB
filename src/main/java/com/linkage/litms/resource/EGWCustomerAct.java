package com.linkage.litms.resource;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * 
 */

/**
 * @author johnson
 * @date 2008-7-22
 */
public class EGWCustomerAct {

	public static Map getInfoById(String customer_id) {

		if(null == customer_id){
			return null;
		}
		String querySQL = "select * from tab_customerinfo where "
				+ "customer_id = '" + customer_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			querySQL = "select customer_name, customer_type, customer_size, customer_address, linkman, linkphone, " +
					"email, city_id, office_id, zone_id " +
					"from tab_customerinfo where "
					+ "customer_id = '" + customer_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(querySQL);
		return DataSetBean.getRecord(psql.getSQL());
	}
}
