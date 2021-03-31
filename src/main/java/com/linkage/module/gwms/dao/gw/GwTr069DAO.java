/**
 * 
 */
package com.linkage.module.gwms.dao.gw;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.gw.GwTr069OBJ;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-7-20
 * @category com.linkage.module.gwms.dao.gw
 * 
 */
public class GwTr069DAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(GwTr069DAO.class);
	/**
	 * 获取指定device_id的VOIP节点，不包含线路信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public GwTr069OBJ getTr069Info(String deviceId){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select device_id,time,url,peri_inform_enable,");
		sql.append(" peri_inform_interval,peri_inform_time from " + Global.getTabName(deviceId,"gw_tr069") + " ");
		sql.append(" where device_id ='");
		sql.append(deviceId);
		sql.append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		List list = jt.queryForList(sql.toString());
		
		GwTr069OBJ rs = null;
		if(list.size()>0){
			
			rs = new GwTr069OBJ();
			Map one = (Map) list.get(0);
			rs.setDeviceId(String.valueOf(one.get("device_id")).toString());
			rs.setTime(String.valueOf(one.get("time")).toString());
			rs.setUrl(String.valueOf(one.get("url")).toString());
			rs.setPeriInformEnable(String.valueOf(one.get("peri_inform_enable")).toString());
			rs.setPeriInformInterval(String.valueOf(one.get("peri_inform_interval")).toString());
			rs.setPeriInformTime(String.valueOf(one.get("peri_inform_time")).toString());
			
		}
		
		return rs;
	}

	/**
	 * 获取TR069节点
	 *
	 * @author wangsenbo
	 * @date Jan 27, 2010
	 * @param 
	 * @return Map
	 */
	public Map getTR069(String deviceId)
	{
		logger.debug("getTR069({})",deviceId);
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select url,username,passwd,conn_req_username,");
			psql.append("conn_req_passwd,peri_inform_enable,peri_inform_interval ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_tr069 where device_id=? ");
		psql.setString(1, deviceId);
		return queryForMap(psql.getSQL());
	}

}
