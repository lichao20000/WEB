package com.linkage.module.itms.service.dao;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-4-26
 * @category com.linkage.module.itms.service.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class ItvHandWorkDAO extends SuperDAO 
{
	public static Logger logger = LoggerFactory.getLogger(ItvHandWorkDAO.class);
	
	/**
	 * city_id转换
	 * @param cityId
	 * @return
	 */
	public List<HashMap<String, String>> getCityIdExFromGwCityMap(String cityId) 
	{
		logger.debug("ItvHandWorkDAO==>getCityIdExFromGwCityMap()", cityId);
		PrepareSQL psql = new PrepareSQL();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
			//无引用的方法
		}*/
		
		psql.append("select * from gw_city_map ");
		psql.append(" where city_id = '" + cityId + "'");

		return DBOperation.getRecords(psql.getSQL());
	}

}
