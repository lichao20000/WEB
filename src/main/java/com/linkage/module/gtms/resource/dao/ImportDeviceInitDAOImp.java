package com.linkage.module.gtms.resource.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.dao.SuperDAO;


public class ImportDeviceInitDAOImp extends SuperDAO implements ImportDeviceInitDAO {

	private static Logger logger = LoggerFactory.getLogger(ImportDeviceInitDAOImp.class);

	/**
	 * 属地ID、属地名Map<city_name, city_id>
	 *
	 * 初始化city_id与city_name对因映射关系
	 * @return
	 */
	public Map<String, String> getCityIdCityNameMap(){

		logger.debug("ImportDeviceInitServImp==>getCityIdCityNameMap()");

		String strSQL = "select city_name, city_id from tab_city order by city_id";
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		Map map = DataSetBean.getMap(strSQL);
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 检测该设备是否已经导入
	 * @param oui
	 * @param deviceSerialnumber
	 * @return
	 */
	public int checkDeviceSerialnumber(String oui, String deviceSerialnumber) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from tab_gw_device_init where oui = '"+oui+"' and device_serialnumber = '"+deviceSerialnumber+"'");
		return jt.queryForInt(psql.getSQL());
	}

}
