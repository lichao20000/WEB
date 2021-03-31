/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */

package com.linkage.module.gwms.resource.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class RefDelDevDAO extends SuperDAO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(RefDelDevDAO.class);

	/**
	 * 设备删除前的解绑
	 *
	 * @author wangsenbo
	 * @date Apr 14, 2010
	 * @param 
	 * @return String
	 */
	public String release(String deviceId,String gw_type)
	{
		PrepareSQL ppSQL = null;
		if ("1".equals(gw_type))
		{
			ppSQL = new PrepareSQL(
					" update tab_hgwcustomer set oui=null,device_serialnumber=null,device_id=null, binddate=null, updatetime=? where device_id=? ");
			ppSQL.setLong(1, new DateTimeUtil().getLongTime());
			ppSQL.setString(2, deviceId);
		}
		else
		{
			ppSQL = new PrepareSQL(
					" update tab_egwcustomer set oui=null,device_serialnumber=null,device_id=null,updatetime=?,binddate=null where device_id=? ");
			ppSQL.setLong(1, new DateTimeUtil().getLongTime());
			ppSQL.setString(2, deviceId);
		}
		return ppSQL.getSQL();
	}

	public String delete(String deviceId)
	{
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append(" delete from tab_gw_res_area where res_id ='").append(deviceId).append("' and res_type = 1 ").append(";");
		sbSQL.append(" delete from gw_devicestatus where device_id ='").append(deviceId).append("' ").append(";");
		sbSQL.append(" delete from sgw_security where device_id ='").append(deviceId).append("' ").append(";");
		sbSQL.append(" delete from tab_gw_device where device_id ='").append(deviceId).append("' ");
		return sbSQL.toString();
	}
	
	/**
	 * 批量更新数据库
	 *
	 * @author wangsenbo
	 * @date Apr 14, 2010
	 * @param 
	 * @return int[]
	 */
	public int[] batchUpdate(String[] sql){
		logger.debug("batchUpdate(sql:{})",sql);
		
		return jt.batchUpdate(sql);
	}
}
