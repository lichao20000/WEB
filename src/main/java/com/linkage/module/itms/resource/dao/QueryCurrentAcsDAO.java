
package com.linkage.module.itms.resource.dao;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class QueryCurrentAcsDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(QueryCurrentAcsDAO.class);

	/**
	 * 根据devSn获取deviceId
	 * @author wangyan10
	 * @param devSn
	 * @return
	 * @since 2017-1-6 下午3:08:33
	 */
	public String getDeviceId(String devSn) {
		logger.warn("QueryCurrentAcsDAO——》getDeviceId");
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  device_id from tab_gw_device a ");
		psql.append(" where 1=1  ");
		psql.append("   and a.dev_sub_sn='"
				+ devSn.substring(devSn.length() - 6)
				+ "' and a.device_serialnumber like '%" + devSn + "'") ;
		List<HashMap<String,String>> List = DBOperation.getRecords(psql.getSQL());
		if(List != null && !List.isEmpty()){
			return StringUtil.getStringValue(List.get(0),"device_id", "");
		}else{
			return "";
		}
	}
	
	/**
	 * 校验deviceId
	 * @author wangyan10
	 * @param deviceId
	 * @return
	 * @since 2017-1-6 下午3:08:48
	 */
	public List<HashMap<String, String>> checkDeviceId(String deviceId) {
		logger.warn("QueryCurrentAcsDAO——》checkDeviceId");
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  device_id from tab_gw_device a ");
		psql.append(" where 1=1  ");
		psql.append("   and a.device_id='"+deviceId+ "'") ;
		List<HashMap<String,String>> list = DBOperation.getRecords(psql.getSQL());
		return list;
	}
}
