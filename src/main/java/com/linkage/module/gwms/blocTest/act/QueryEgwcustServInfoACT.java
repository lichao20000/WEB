package com.linkage.module.gwms.blocTest.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;


public class QueryEgwcustServInfoACT 
{
	public static Logger logger = LoggerFactory.getLogger(QueryEgwcustServInfoACT.class);
	
	/**
	 * 根据user_id 获取 资源_企业网关业务用户表 
	 * 
	 * @param user_id
	 * @return
	 */
	public Cursor getEgwcustServInfo(String user_id) 
	{
		logger.debug("getEgwcustServInfo({})", new Object[]{user_id});

		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select username,vpiid,vciid,vlanid ");
		}else{
			psql.append("select * ");
		}
		psql.append("from egwcust_serv_info where user_id="+ user_id);
		return DataSetBean.getCursor(psql.getSQL());
	}
	
	/**
	 * 获取设备上行方式
	 * 
	 * @param device_id
	 * @return
	 */
	public Cursor getAccessType(String device_id) 
	{
		logger.debug("getAccessType({})", new Object[]{device_id});
		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.access_type from tab_gw_device a,tab_devicetype_config_info b ");
		psql.append("where a.devicetype_id=b.devicetype_id and a.device_id='"+device_id+"' ");
		return DataSetBean.getCursor(psql.getSQL());
	}
}
