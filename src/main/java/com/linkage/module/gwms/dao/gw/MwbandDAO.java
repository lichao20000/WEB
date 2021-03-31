/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.dao.gw;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.interf.I_DAO;
import com.linkage.module.gwms.interf.dao.I_StrategyDAO;

/**
 * DAO:advance search.ALG.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 17, 2009
 * @see
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class MwbandDAO extends SuperDAO implements I_DAO, I_StrategyDAO 
{
	private static Logger logger = LoggerFactory.getLogger(MwbandDAO.class);

//	/**
//	 * add strategy.
//	 */
//	public Boolean addStrategy(StrategyOBJ obj) {
//		logger.debug("addStrategy({})", obj);
//
//		if (obj == null) {
//			logger.debug("obj == null");
//
//			return false;
//		}
//
//		StringBuilder sql = new StringBuilder();
//		sql.append("insert into gw_serv_strategy (");
//		sql.append("id,acc_oid,time,type"
//				+ ",device_id,oui,device_serialnumber,username"
//				+ ",sheet_para,service_id,task_id,order_id,temp_id,is_last_one");
//		sql.append(") values (");
//		sql.append(obj.getId());
//		sql.append("," + obj.getAccOid());
//		sql.append("," + obj.getTime());
//		sql.append("," + obj.getType());
//		sql.append("," + StringUtil.getSQLString(obj.getDeviceId()));
//		sql.append("," + StringUtil.getSQLString(obj.getOui()));
//		sql.append("," + StringUtil.getSQLString(obj.getSn()));
//		sql.append("," + StringUtil.getSQLString(obj.getUsername()));
//		sql.append("," + StringUtil.getSQLString(obj.getSheetPara()));
//		sql.append("," + obj.getServiceId());
//		sql.append("," + StringUtil.getSQLString(obj.getTaskId()));
//		sql.append("," + obj.getOrderId());
//		sql.append("," + obj.getTempId());
//		sql.append("," + obj.getIsLastOne());
//		sql.append(")");
//
//		try {
//			jt.execute(sql.toString());
//
//			return true;
//		} catch (DataAccessException e) {
//			logger.error("DataAccessException:{}", e.getMessage());
//
//			return false;
//		}
//	}

	/**
	 * get MWBAND by device_id from db.
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map getData(String deviceId) 
	{
		logger.debug("getData({})", deviceId);
		if (deviceId == null) {
			logger.debug("deviceId == null");
			return null;
		}

		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select gather_time,m_mode,total_number,stb_enable,");
			psql.append("stb_number,camera_enable,camera_number,computer_enable,");
			psql.append("computer_number,phone_enable,phone_number ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_mwband where device_id=? ");
		psql.setString(1, deviceId);
		return queryForMap(psql.getSQL());
	}

}
