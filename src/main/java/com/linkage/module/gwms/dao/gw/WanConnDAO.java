package com.linkage.module.gwms.dao.gw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.gw.WanConnObj;

/**
 * @author Jason(3412)
 * @date 2009-7-6
 */
public class WanConnDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(WanConnDAO.class);
	
	/**
	 * 获取设备WanConn特定PVC值结点信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-24
	 * @return WanConnObj
	 */
	public WanConnObj queryDevWanConn(String deviceId, String vpiid, String vciid) 
	{
		logger.debug("queryDevWanConn({},{})", deviceId, vpiid);
		if(null == deviceId || null == vpiid || null == vciid){
			logger.warn("deviceId or pvc is null");
			return null;
		}
		WanConnObj wanConnObj = null;
		PrepareSQL psql = new PrepareSQL();
		 if(DBUtil.GetDB()==Global.DB_MYSQL){
			 psql.append("select wan_id,wan_conn_id,vpi_id,vci_id,gather_time ");
		 }else{
			 psql.append("select * ");
		 }
		psql.append("from "+getTabName(deviceId));
		psql.append(" where device_id=? and vpi_id=? and vci_id=? ");
		psql.setString(1, deviceId);
		psql.setString(2, vpiid);
		psql.setStringExt(3, vciid, false);
		Map rMap = DataSetBean.getRecord(psql.getSQL());
		if (null != rMap && rMap.isEmpty() == false) {
			wanConnObj = new WanConnObj();
			wanConnObj.setWan_id(String.valueOf(rMap.get("wan_id")));
			wanConnObj.setWan_conn_id(String.valueOf(rMap.get("wan_conn_id")));
			wanConnObj.setVpi_id(String.valueOf(rMap.get("vpi_id")));
			wanConnObj.setVci_id(String.valueOf(rMap.get("vci_id")));
			wanConnObj.setDevice_id(deviceId);
			wanConnObj.setGather_time(String.valueOf(rMap.get("gather_time")));
		}
		return wanConnObj;
	}
	
	
	public WanConnObj queryDevWanConnIPTVNEW(String deviceId,String serv_list) 
	{
		logger.debug("queryDevWanConn({},{})", deviceId);
		if(null == deviceId ){
			logger.warn("deviceId");
			return null;
		}
		WanConnObj wanConnObj = null;
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select wan_id,wan_conn_id,vpi_id,vci_id,vlan_id,gather_time ");
		}else{
			psql.append("select * ");
		}
		psql.append("from "+getTabName(deviceId));
		psql.append(" where device_id=? and wan_conn_id in ");
		psql.append("(select wan_conn_id from "+getTabName_sess(deviceId));
		psql.append(" where device_id=? and serv_list like '%"+serv_list+"%')");
		psql.setString(1, deviceId);
		psql.setString(2, deviceId);
		
		Map rMap = DataSetBean.getRecord(psql.getSQL());
		if (null != rMap && rMap.isEmpty() == false) {
			wanConnObj = new WanConnObj();
			wanConnObj.setWan_id(String.valueOf(rMap.get("wan_id")));
			wanConnObj.setWan_conn_id(String.valueOf(rMap.get("wan_conn_id")));
			wanConnObj.setVpi_id(String.valueOf(rMap.get("vpi_id")));
			wanConnObj.setVci_id(String.valueOf(rMap.get("vci_id")));
			wanConnObj.setVlan_id(String.valueOf(rMap.get("vlan_id")));
			wanConnObj.setDevice_id(deviceId);
			wanConnObj.setGather_time(String.valueOf(rMap.get("gather_time")));
		}
		return wanConnObj;
	}
	
	public List<WanConnObj> queryDevWanConnIPTVNEWs(String deviceId,String serv_list) 
	{
		WanConnObj wanConnObj = new WanConnObj();
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select wan_id,wan_conn_id,vpi_id,vci_id,vlan_id,gather_time ");
		}else{
			psql.append("select * ");
		}
		psql.append("from "+getTabName(deviceId));
		psql.append(" where device_id=? and wan_conn_id in ");
		psql.append("(select wan_conn_id from "+getTabName_sess(deviceId));
		psql.append(" where device_id=? and serv_list like '%"+serv_list+"%')");
		psql.setString(1, deviceId);
		psql.setString(2, deviceId);
		List<WanConnObj> resList = new ArrayList<WanConnObj>();
		List<HashMap<String,String>> mapsList = DBOperation.getRecords(psql.getSQL());
		for(int i=0;i<mapsList.size();i++){
			HashMap<String,String> rMap = mapsList.get(i);
			wanConnObj = new WanConnObj();
			wanConnObj.setWan_id(String.valueOf(rMap.get("wan_id")));
			wanConnObj.setWan_conn_id(String.valueOf(rMap.get("wan_conn_id")));
			wanConnObj.setVpi_id(String.valueOf(rMap.get("vpi_id")));
			wanConnObj.setVci_id(String.valueOf(rMap.get("vci_id")));
			wanConnObj.setVlan_id(String.valueOf(rMap.get("vlan_id")));
			wanConnObj.setDevice_id(deviceId);
			wanConnObj.setGather_time(String.valueOf(rMap.get("gather_time")));
			resList.add(wanConnObj);
		}
		
		return resList;
	}
	
	/**
	 * 获得设备相应vlanId的数据（From DB）
	 * @author gongsj
	 * @date 2009-7-16
	 * @param deviceId
	 * @param vlanId
	 * @return
	 */
	public WanConnObj queryDevWanConn(String deviceId, String vlanId) 
	{
		WanConnObj wanConnObj = new WanConnObj();
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select wan_id,wan_conn_id,gather_time ");
		}else{
			psql.append("select * ");
		}
		psql.append("from "+getTabName(deviceId));
		psql.append(" where device_id=? and vlan_id=? ");
		psql.setString(1, deviceId);
		psql.setString(2, vlanId);
		Map rMap = DataSetBean.getRecord(psql.getSQL());
		wanConnObj = new WanConnObj();
		if (null != rMap && rMap.isEmpty() == false) {
			wanConnObj.setWan_id(String.valueOf(rMap.get("wan_id")));
			wanConnObj.setWan_conn_id(String.valueOf(rMap.get("wan_conn_id")));
			wanConnObj.setVpi_id(String.valueOf(rMap.get("vlan_id")));
			wanConnObj.setDevice_id(deviceId);
			wanConnObj.setGather_time(String.valueOf(rMap.get("gather_time")));
		}
		return wanConnObj;
	}
	
	/**
	 * 获取设备WanConn所有结点信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-24
	 * @return WanConnObj[]
	 */
	public WanConnObj queryDevWanConn(Map map) 
	{
		// 查询设备WanConnectionDevice信息
		String device_id = StringUtil.getStringValue(map.get("device_id"));
		String wan_id =  StringUtil.getStringValue(map.get("wan_id"));
		String wan_conn_id =  StringUtil.getStringValue(map.get("wan_conn_id"));
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select device_id,wan_id,wan_conn_id,ip_conn_num,");
			psql.append("ppp_conn_num,vpi_id,vci_id,vlan_id,gather_time ");
		}else{
			psql.append("select * ");
		}
		psql.append("from "+getTabName(device_id));
		psql.append(" where device_id=? and wan_id=? and wan_conn_id=?");
		psql.setString(1, device_id);
		psql.setInt(2, Integer.parseInt(wan_id));
		psql.setInt(3, Integer.parseInt(wan_conn_id));
		
		// 执行查询
		List rList = jt.queryForList(psql.getSQL());
		// WanConnObj数组，存储结果
		WanConnObj[] wanConnObj = null;
		if (null != rList && rList.size() > 0) {
			int lSize = rList.size();
			wanConnObj = new WanConnObj[lSize];
			for (int i = 0; i < lSize; i++) {
				Map rMap = (Map) rList.get(i);
				wanConnObj[i] = new WanConnObj();
				wanConnObj[i].setDevice_id(String.valueOf(rMap.get("device_id")));
				wanConnObj[i].setWan_id(String.valueOf(rMap.get("wan_id")));
				wanConnObj[i].setWan_conn_id(String.valueOf(rMap.get("wan_conn_id")));
				wanConnObj[i].setIp_conn_num(String.valueOf(rMap.get("ip_conn_num")));
				wanConnObj[i].setPpp_conn_num(String.valueOf(rMap.get("ppp_conn_num")));
				wanConnObj[i].setVpi_id(String.valueOf(rMap.get("vpi_id")));
				wanConnObj[i].setVci_id(String.valueOf(rMap.get("vci_id")));
				// add by chenjie 2013-10-25 运营商信息
//				if(LipossGlobals.getLipossProperty("telecom").equals(Global.TELECOM_CUC))
//				{
//					// CUC的vlanid在wanConnSess中
//				}
//				else
//				{
					wanConnObj[i].setVlan_id(String.valueOf(rMap.get("vlan_id"))); 
//				}
				
				wanConnObj[i].setGather_time(String.valueOf(rMap.get("gather_time")));
			}
		}
		return wanConnObj[0];
	}

	
	/**
	 * 获取设备WanConn所有结点信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-24
	 * @return WanConnObj[]
	 */
	public WanConnObj[] queryDevWanConn(String deviceId) 
	{
		// 查询设备WanConnectionDevice信息
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select device_id,wan_id,wan_conn_id,ip_conn_num,ppp_conn_num,");
			psql.append("vpi_id,vci_id,vlan_id,gather_time ");
		}else{
			psql.append("select * ");
		}
		psql.append("from "+getTabName(deviceId) + " where device_id=? ");
		psql.setString(1, deviceId);
		// 执行查询
		List rList = jt.queryForList(psql.getSQL());
		// WanConnObj数组，存储结果
		WanConnObj[] wanConnObj = null;
		if (null != rList && rList.size() > 0) {
			int lSize = rList.size();
			wanConnObj = new WanConnObj[lSize];
			for (int i = 0; i < lSize; i++) {
				Map rMap = (Map) rList.get(i);
				wanConnObj[i] = new WanConnObj();
				wanConnObj[i].setDevice_id(String.valueOf(rMap.get("device_id")));
				wanConnObj[i].setWan_id(String.valueOf(rMap.get("wan_id")));
				wanConnObj[i].setWan_conn_id(String.valueOf(rMap.get("wan_conn_id")));
				wanConnObj[i].setIp_conn_num(String.valueOf(rMap.get("ip_conn_num")));
				wanConnObj[i].setPpp_conn_num(String.valueOf(rMap.get("ppp_conn_num")));
				wanConnObj[i].setVpi_id(String.valueOf(rMap.get("vpi_id")));
				wanConnObj[i].setVci_id(String.valueOf(rMap.get("vci_id")));
				wanConnObj[i].setVlan_id(String.valueOf(rMap.get("vlan_id")));
				wanConnObj[i].setGather_time(String.valueOf(rMap.get("gather_time")));
			}
		}
		return wanConnObj;
	}
	
	/**
	 * add by chenjie 2012-10-26 
	 * 采集表gw_wan_conn区分ITMS/BBMS
	 * @param deviceId
	 * @return
	 */
	public String getTabName(String deviceId)
	{
		String tabName = "gw_wan_conn";
		return Global.getTabName(deviceId, tabName);
	}
	
	public String getTabName_sess(String deviceId)
	{
		String tabName = "gw_wan_conn_session";
		return Global.getTabName(deviceId, tabName);
	}
}
