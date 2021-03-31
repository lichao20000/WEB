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
public class QosDAO extends SuperDAO implements I_DAO, I_StrategyDAO {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(QosDAO.class);

//	/**
//	 * add strategy
//	 * 
//	 * @param obj
//	 *            StrategyOBJ
//	 * @return Boolean
//	 *         <li>true: success</li>
//	 *         <li>false: fail</li>
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
//				+ ",sheet_para,service_id,task_id,order_id");
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
	 * get alg by device_id from db.
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map getData(String deviceId) 
	{
		logger.debug("getData({})", deviceId);
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSQL.append("select enable,qos_mode from ");
		}else{
			pSQL.append("select * from ");
		}
		pSQL.append(Global.getTabName(deviceId,"gw_qos") + " where device_id=? ");
		pSQL.setString(1, deviceId);

		return queryForMap(pSQL.getSQL());
	}

}
