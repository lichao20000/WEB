/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.config.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.config.obj.StrategyQosParaOBJ;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.WlanDAO;

/**
 * DAO: qos_strategy.
 * gw_strategy_qos,gw_strategy_param
 * 
 * @author Jason(3412),alex(yanhj@lianchuang.com)
 * @date 2009-7-24
 */
@SuppressWarnings("unchecked")
public class StrategyQosDAO extends SuperDAO {

	private static Logger logger = LoggerFactory.getLogger(WlanDAO.class);

	/**
	 * 根据设备ID获取自定义QOS策略信息
	 * 
	 * @param
	 * @date 2009-7-23
	 * @return List
	 */
	public List getQosTmplSelf() 
	{
		//TODO wait
		//无引用的方法
		logger.debug("getQosTmplSelf()");

		List rList = null;
		String strSQL = "select * from qos_tmpl where tmpl_id=1";
		PrepareSQL psql = new PrepareSQL(strSQL);

		rList = jt.queryForList(psql.getSQL());

		return rList;
	}

	/**
	 * 根据设备ID获取自定义QOS策略信息
	 * 
	 * @param
	 * @date 2009-7-23
	 * @return List
	 */
	public List getQosTmpl(int tmplId) 
	{
		//TODO wait
		//无引用的方法
		logger.debug("getQosTmpl({})", tmplId);

		List rList = null;
		String strSQL = "select * from qos_tmpl where tmpl_id=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setInt(1, tmplId);
		rList = jt.queryForList(psql.getSQL());

		return rList;
	}

	/**
	 * 根据设备ID获取自定义QOS策略信息
	 * 
	 * @param
	 * @date 2009-7-23
	 * @return List
	 */
	public List getQosStrategySelf(String deviceId) 
	{
		logger.debug("getQosStrategy({},{})", deviceId);

		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select a.id,a.status,a.result_id,a.result_desc,a.acc_oid,a.time,b.tmpl_id ");
			psql.append("from gw_serv_strategy a,gw_strategy_qos b ");
			psql.append("where a.id=b.id and a.device_id=? ");
			psql.setString(1, deviceId);
			
			List l=jt.queryForList(psql.getSQL());
			List<Map> list=new ArrayList<Map>();
			if(l!=null && !l.isEmpty()){
				for(int i=0;i<l.size();i++)
				{
					psql = new PrepareSQL();
					psql.append("select count(*) from qos_tmpl ");
					psql.append("where tmpl_id=1 and tmpl_id=? ");
					psql.setLong(1,StringUtil.getLongValue((Map)l.get(i),"tmpl_id"));
					if(jt.queryForInt(psql.getSQL())>0){
						list.add((Map)l.get(i));
					}
				}
			}
			return list;			
		}else{
			psql.append("select a.id,a.status,a.result_id,a.result_desc,a.acc_oid,a.time ");
			psql.append("from gw_serv_strategy a,gw_strategy_qos b,qos_tmpl c ");
			psql.append("where a.id=b.id and a.device_id=? ");
			psql.append("and c.tmpl_id=1 and b.tmpl_id=c.tmpl_id ");
			psql.setString(1, deviceId);
			
			return jt.queryForList(psql.getSQL());
		}
	}

	/**
	 * 根据Qos模板ID和设备ID获取策略信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-23
	 * @return List
	 */
	public List getQosStrategy(String deviceId, int tmplId) {
		logger.debug("getQosStrategy({},{})", deviceId, tmplId);

		List rList = null;
		String strSQL = "select a.id, a.status, a.result_id, a.result_desc"
				+ ", a.acc_oid, a.time"
				+ " from gw_serv_strategy a, gw_strategy_qos b where"
				+ " a.id=b.id and a.device_id=? and b.tmpl_id=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, deviceId);
		psql.setInt(2, tmplId);
		rList = jt.queryForList(psql.getSQL());

		return rList;
	}

	/**
	 * 根据Qos模板ID和策略ID获取Qos相关配置参数信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-23
	 * @return List
	 */
	public List getQosStrategyPara(long strategyId) 
	{
		logger.debug("getQosStrategyPara({})", strategyId);
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select para_value from gw_strategy_qos_param where id=? ");
		}else{
			psql.append("select * from gw_strategy_qos_param where id=? ");
		}
		psql.setLong(1, strategyId);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 入策略Qos模板表
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-23
	 * @return int
	 */
	public int saveQosStrategyTemp(long strategyId, int tempId) {
		logger.debug("getQosStrategyPara({},{})", strategyId, tempId);
		String strSQL = "insert into gw_strategy_qos (id,tmpl_id) values (?,?)";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, strategyId);
		psql.setInt(2, tempId);
		return jt.update(psql.getSQL());
	}

	/**
	 * 保持Qos策略配置参数
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-24
	 * @return int
	 */
	public int saveQosStrategyParam(StrategyQosParaOBJ qosParaObj) {
		logger.debug("saveQosStrategyParam({})", qosParaObj);
		String strSQL = "insert into gw_strategy_qos_param "
			+ "(id,sub_order,type_order,sub_id,type_id,type_name,type_max,type_min,type_prot,queue_id,para_value)"
			+ " values (?,?,?,?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, qosParaObj.getId());
		psql.setInt(2, qosParaObj.getSubOrder());
		psql.setInt(3, qosParaObj.getTypeOrder());
		psql.setInt(4, qosParaObj.getSubId());
		psql.setInt(5, qosParaObj.getTypeId());
		psql.setString(6, qosParaObj.getTypeName());
		psql.setString(7, qosParaObj.getTypeMax());
		psql.setString(8, qosParaObj.getTypeMin());
		psql.setString(9, qosParaObj.getTypeProt());
		psql.setInt(10, qosParaObj.getQueueId());
		psql.setString(11, qosParaObj.getParaValue());
		return jt.update(psql.getSQL());
	}
	
	/**
	 * 获取策略和Qos的相关信息,
	 * 
	 * @param deviceId:设备ID; tempId: Qos模板ID 如:SSID
	 * @author Jason(3412)
	 * @date 2009-7-24
	 * @return List
	 */
	public List queryStrategyQos(String deviceId, String tempId){
		logger.debug("queryStrategyQos({},{})", deviceId, tempId);
		List rList = null;
		String strSQL = "select a.id,a.result_id,a.status,a.result_desc,a.time"
			+ " from gw_serv_strategy a, gw_strategy_qos b"
			+ " where a.id=b.id and a.device_id=? and b.tmpl_id=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, deviceId);
		psql.setStringExt(2, tempId, false);
		rList = jt.queryForList(psql.getSQL());
		return rList;
	}
}
