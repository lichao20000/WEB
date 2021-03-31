/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.init.dao;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.system.utils.database.Cursor;
import com.linkage.system.utils.database.DataSetBean;

/**
 * DAO:getPreProcessIOR
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 19, 2009
 * @see
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class AppInitDAO 
{
	private static Logger logger = LoggerFactory.getLogger(AppInitDAO.class);

	/**
	 * 获得预读模块的IOR
	 * 
	 * @return
	 */
	public static Map getPreProcessIOR(String gw_type) 
	{
		logger.debug("getPreProcessIOR()");

		PrepareSQL psql = new PrepareSQL();
		psql.append("select ior from tab_ior where object_name=? and object_poa=? ");
		psql.setString(1,Global.getPrefixName(gw_type)+Global.SYSTEM_PREPROCESS);
		psql.setString(2,Global.getPrefixName(gw_type)+Global.SYSTEM_PREPROCESS_POA);
		
		if("4".equals(gw_type)){
			logger.warn("getPreProcessIOR({})", gw_type);
			return DBOperation.getRecord(psql.getSQL(), "xml-stb");
		}
		return DataSetBean.getRecord(psql.getSQL());
	}

	/**
	 * 获得SG模块的IOR
	 * 
	 * @return
	 */
	public static Map getSuperGatherIOR(String gw_type) 
	{
		logger.debug("getSuperGatherIOR()");

		PrepareSQL psql = new PrepareSQL();
		psql.append("select ior from tab_ior where object_name=? ");
		psql.setString(1,Global.getPrefixName(gw_type)+Global.SYSTEM_SUPER_GATHER);
		
		if("4".equals(gw_type)){
			return DBOperation.getRecord(psql.getSQL(), "xml-stb");
		}

		return DataSetBean.getRecord(psql.getSQL());
	}
	
	/**
	 * 获取资源绑定模块的IOR
	 * @return
	 */
	public static Map getResourceBindIOR(String gw_type)
	{
		logger.debug("getResourceBindIOR()");
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select ior from tab_ior where object_name=? ");
		psql.setString(1,Global.getPrefixName(gw_type)+Global.SYSTEM_BUSINESS_LOGIC);
		
		if("4".equals(gw_type)){
			return DBOperation.getRecord(psql.getSQL(), "xml-stb");
		}
		
		return DataSetBean.getRecord(psql.getSQL());
	}
	
	/**
	 * 获取资源绑定模块的IOR
	 * @return
	 */
	public static Map getSoftUpgradeIOR(String gw_type)
	{
		logger.debug("getSoftUpgradeIOR()");
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select ior from tab_ior where object_name='ITMS_SoftUp'");
		
		return DataSetBean.getRecord(psql.getSQL());
	}
	
	/**
	 * 获得监控的默认ACS的IOR
	 * 
	 * @return
	 */
	public static Map getACSIOR() 
	{
		logger.debug("getACSIOR()");

		PrepareSQL psql = new PrepareSQL();
		psql.append("select ior from tab_ior where object_name like 'ACS_0'");
		return DataSetBean.getRecord(psql.getSQL());
	}

	/**
	 * 获得ACS的IOR
	 * 
	 * @return
	 */
	public static Cursor getFaultCode() 
	{
		logger.debug("getFaultCode()");

		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select fault_code,fault_type,fault_name,");
			psql.append("fault_desc,fault_reason,solutions ");
			psql.append("from tab_cpe_faultcode");
		}else{
			psql.append("select * from tab_cpe_faultcode");
		}
		
		return DataSetBean.getCursor(psql.getSQL());
	}
	
	/**
	 * init static src
	 * 
	 * @return
	 */
	public static Cursor getStaticSrc()
	{
		logger.debug("getStaticSrc()");
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select src_type,src_code,src_key,src_value ");
		psql.append("from tab_static_src order by src_type,src_code ");
		return DataSetBean.getCursor(psql.getSQL());
	}
}
