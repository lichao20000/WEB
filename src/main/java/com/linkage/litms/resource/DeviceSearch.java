package com.linkage.litms.resource;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author Jason(3412)
 * @date 2008-8-28
 */
public class DeviceSearch {

	/** 日志记录 */
	private static Logger m_logger = LoggerFactory
			.getLogger(DeviceSearch.class);

	/**
	 * 根据序列号查询家庭网关设备信息
	 * 
	 * @param devSno
	 *            设备序列号； curCity_ID 属地ID
	 * @author Jason(3412)
	 * @date 2008-8-28
	 * @return Cursor
	 */
	public static Cursor queryHGWDevice(String devSno, String curCity_ID) {
		m_logger.debug("queryHGWDevice({},{})", devSno, curCity_ID);

		// 写查询sql
		String sql = "select * from tab_gw_device where gw_type=1 and cpe_allocatedstatus=0";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select device_id, city_id, vender_id, oui, device_serialnumber, cpe_allocatedstatus " +
					"from tab_gw_device where gw_type=1 and cpe_allocatedstatus=0";
		}

		if (devSno != null && !"".equals(devSno)) {
			if(devSno.length()>5){
				sql += " and dev_sub_sn ='" + devSno.substring(devSno.length()-6, devSno.length()) + "'";
			}
			sql += " and device_serialnumber like '%" + devSno + "'";
		}

		Map<String, String> cityMap = CityDAO.getCityIdPidMap();
		String pcityId = cityMap.get(curCity_ID);
		cityMap = null;
		if (null != pcityId && !pcityId.equals("-1")) {
			sql += " and city_id in('" + pcityId + "','" + curCity_ID
					+ "','00') ";
		}
		PrepareSQL psql = new PrepareSQL(sql);

		// 执行查询语句
		Cursor sor = DataSetBean.getCursor(psql.getSQL());

		// 返回结果处理
		return sor;
	}

	/**
	 * 根据序列号查询企业网关设备信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-9-2
	 * @return Cursor
	 */
	public static Cursor queryEGWDevice(String devSno, String curCity_ID) {
		m_logger.debug("queryEGWDevice({},{})", devSno, curCity_ID);

		// 写查询sql
		String sql = "select * from tab_gw_device where gw_type=2 and cpe_allocatedstatus=0";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select device_id, city_id, oui, device_serialnumber, cpe_allocatedstatus " +
					"from tab_gw_device where gw_type=2 and cpe_allocatedstatus=0";
		}
		if (devSno != null && !"".equals(devSno)) {
			if(devSno.length()>5){
				sql += " and dev_sub_sn ='" + devSno.substring(devSno.length()-6, devSno.length()) + "'";
			}
			sql += " and device_serialnumber like '%" + devSno + "'";
		}

		Map<String, String> cityMap = CityDAO.getCityIdPidMap();
		String pcityId = cityMap.get(curCity_ID);
		cityMap = null;
		if (null != pcityId && !pcityId.equals("-1")) {
			sql += " and city_id in('" + pcityId + "','" + curCity_ID
					+ "','00') ";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		// 执行查询语句
		Cursor sor = DataSetBean.getCursor(sql);

		// 返回结果处理
		return sor;
	}
}
