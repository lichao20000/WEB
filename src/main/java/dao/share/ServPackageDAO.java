/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package dao.share;

import java.util.HashMap;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.system.utils.database.Cursor;
import com.linkage.system.utils.database.DataSetBean;

/**
 * dao: gw_serv_package.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Apr 13, 2009
 * @see
 * @since 1.0
 */
public class ServPackageDAO {

	/** log */
	private static final Logger LOG = LoggerFactory
			.getLogger(ServPackageDAO.class);

	/**
	 * 获得套餐ID与套餐名字之间的对照
	 * 
	 * @return Map map<serv_package_id,serv_package_name>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getPackageIdNameMap() {
		LOG.debug("getPackageIdNameMap()");

		String sql = "select * from gw_serv_package";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select serv_package_id, serv_package_name from gw_serv_package";
		}

		// 查询所有属地
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map<String, String> map = new HashMap<String, String>();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map<String, String> ht = cursor.getNext();
		while (ht != null) {
			map.put(ht.get("serv_package_id"), ht.get("serv_package_name"));
			ht = cursor.getNext();
		}

		return map;
	}

}
