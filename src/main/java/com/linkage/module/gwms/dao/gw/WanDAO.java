package com.linkage.module.gwms.dao.gw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.gw.WanObj;

@SuppressWarnings("rawtypes")
public class WanDAO 
{
	private static Logger logger = LoggerFactory.getLogger(WlanDAO.class);

	/** JDBC */
	private JdbcTemplate dao;
	
	/**
	 * 获取WAN
	 * 
	 * @param device_id
	 * @return
	 */
	public List getData(String deviceId) {
		return null;
	}
	
	/**
	 * 根据deviceId和wanId获得一个WanObj
	 * @author gongsj
	 * @date 2009-7-16
	 * @param deviceId
	 * @param wanId
	 * @return
	 */
	public WanObj getWan(String deviceId, String wanId) 
	{
		WanObj wanObj = null;
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select wan_conn_num,access_type,gather_time from ");
		}else{
			psql.append("select * from ");
		}
		psql.append(getTabName(deviceId) + " where device_id=? and wan_id=? ");
		psql.setString(1, deviceId);
		psql.setStringExt(2, wanId, false);
		
		Map rMap = DataSetBean.getRecord(psql.getSQL());
		if (null != rMap && rMap.isEmpty() == false) {
			wanObj = new WanObj();
			wanObj.setDevId(deviceId);
			wanObj.setWanId(wanId);
			wanObj.setWanConnNum(String.valueOf(rMap.get("wan_conn_num")));
			wanObj.setAccessType(String.valueOf(rMap.get("access_type")));
			wanObj.setGatherTime(String.valueOf(rMap.get("gather_time")));
			
			//从配置文件读取参数，如果是2，accessType从versiontypeinfo表关联获取
			if (2 == LipossGlobals.accessTypeFrom())
			{
				String accessTypeString = getAccessTypeFromVersion(deviceId);
				if (!StringUtil.IsEmpty(accessTypeString))
				{
					wanObj.setAccessType(accessTypeString);
				}
			}
		}
		return wanObj;
		
	}
	
	/**
	 * 根据deviceId和wanId获得一个WanObj
	 * @author gongsj
	 * @date 2009-7-16
	 * @param deviceId
	 * @param wanId
	 * @return
	 */
	public String getAccessTypeFromVersion(String deviceId) 
	{
		//logger.error("从version表中查询");
		Map<String, String> acctype = new HashMap<String, String>();
		acctype.put("ADSL", "DSL");
		acctype.put("LAN", "Ethernet");
		acctype.put("EPON光纤", "EPON");
		acctype.put("GPON光纤", "GPON");
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select a.devicetype_id,a.access_style_relay_id,a.add_time ");
			psql.append("from tab_devicetype_info a,tab_gw_device c ");
			psql.append("where c.devicetype_id=a.devicetype_id and c.device_id=? ");
			psql.setString(1, deviceId);
			
			Map rMap = DataSetBean.getRecord(psql.getSQL());
			if(rMap==null || rMap.isEmpty()){
				return null;
			}
			psql = new PrepareSQL();
			psql.append("select type_name from gw_access_type ");
			psql.append("where type_id="+StringUtil.getStringValue(rMap,"access_style_relay_id"));
			Map m = DataSetBean.getRecord(psql.getSQL());
			if(rMap==null || rMap.isEmpty()){
				return null;
			}
			
			return acctype.get(String.valueOf(m.get("type_name")));
		}else{
			psql.append("select a.devicetype_id,a.access_style_relay_id,a.add_time,b.type_name ");
			psql.append("from tab_devicetype_info a,gw_access_type b,tab_gw_device c ");
			psql.append("where c.devicetype_id=a.devicetype_id ");
			psql.append("and a.access_style_relay_id=b.type_id and c.device_id=? ");
			psql.setString(1, deviceId);
		}
		
		
		Map rMap = DataSetBean.getRecord(psql.getSQL());
		if (null != rMap && rMap.isEmpty() == false) {
			return acctype.get(String.valueOf(rMap.get("type_name")));
		}
		return null;
		
	}
	
	
    /**
	 * 获取设备的上行方式，是否是ADSL(用于判断用PVC还是VLAN)
	 * 
	 * @param 设备ID
	 * @author Jason(3412)
	 * @date 2009-10-29
	 * @return boolean
	 */
	public boolean isADSL(String deviceId) {
		logger.debug("isADSL({})", deviceId);
		if (1 == getAccessType(deviceId)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取设备的上行方式，
	 * 
	 * @param 设备ID
	 * @author Jason(3412)
	 * @date 2009-10-29
	 * @return int 1:ADSL 2:Ethernet 3:PON(EPON) 4:POTS -1:未知
	 */
	public int getAccessType(String deviceId) {
		logger.debug("getAccessType({})", deviceId);
		//否则查询WAN连接信息表
		WanObj wanObj = null;
		
		wanObj = getWan(deviceId, "1");
		
		if (null != wanObj) {
			if ("DSL".equals(wanObj.getAccessType())) {
				return 1;
			} else if ("Ethernet".equals(wanObj.getAccessType())) {
				return 2;
			} else if ("EPON".equalsIgnoreCase(wanObj.getAccessType()) 
					|| "PON".equalsIgnoreCase(wanObj.getAccessType())
					|| "X_CU_PON".equalsIgnoreCase(wanObj.getAccessType())
					|| "X_CU_EPON".equalsIgnoreCase(wanObj.getAccessType())) {
				return 3;
			} else if ("GPON".equalsIgnoreCase(wanObj.getAccessType())
					|| "X_CU_GPON".equalsIgnoreCase(wanObj.getAccessType())) {
				return 4;
			} else {
				return -1;
			}
		} else {
			logger.warn("从数据库中未获取到设备的WAN结点信息");
			return -1;
		}
	}
    
	/**
	 * 提供是否是取VLANID的静态方法
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2010-1-26
	 * @return boolean
	 */
	public static boolean isLAN(String deviceId){
		logger.debug("isLAN({})", deviceId);
		WanDAO wanDao = new WanDAO();
		if(wanDao.isADSL(deviceId)){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * set jdbc
	 */
	public void setDao(DataSource dao) {
		logger.debug("setDao(DataSource)");

		this.dao = new JdbcTemplate(dao);
	}
	
	/**
	 * add by chenjie 2012-10-26 
	 * 采集表gw_wan区分ITMS/BBMS
	 * @param deviceId
	 * @return
	 */
	public String getTabName(String deviceId)
	{
		String tabName = "gw_wan";
		return Global.getTabName(deviceId, tabName);
	}
	
	public String getNetUsername(String deviceId) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select b.user_id ");
			psql.append("from tab_hgwcustomer b,tab_gw_device c ");
			psql.append("where b.device_id=c.device_id and c.device_id='"+deviceId+"'");
			
			String user_id=StringUtil.getStringValue(DBOperation.getRecord(psql.getSQL()),"user_id");
			if(StringUtil.IsEmpty(user_id)){
				return "";
			}
			psql = new PrepareSQL();
			psql.append("select username from hgwcust_serv_info ");
			psql.append("where serv_type_id=10 and user_id="+user_id);
			
			return StringUtil.getStringValue(DBOperation.getRecord(psql.getSQL()),"username","");
		}else{
			psql.append("select a.username ");
			psql.append("from hgwcust_serv_info a,tab_hgwcustomer b,tab_gw_device c ");
			psql.append("where a.user_id=b.user_id and a.serv_type_id=10 ");
			psql.append("and b.device_id=c.device_id and c.device_id='"+deviceId+"' order by a.opendate desc ");
			return StringUtil.getStringValue(DBOperation.getRecord(psql.getSQL()),"username","");
		}
	}
}
