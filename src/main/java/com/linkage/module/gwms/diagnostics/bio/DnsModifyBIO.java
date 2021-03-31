package com.linkage.module.gwms.diagnostics.bio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.HgwServUserDAO;
import com.linkage.module.gwms.diagnostics.dao.HealthInfoDAO;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

public class DnsModifyBIO {
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(DnsModifyBIO.class);
	
	/** 数据库操作dao*/
	private HgwServUserDAO hgwServUserDao;
	private HealthInfoDAO healthInfoDao;
	
	/**
	 * 获取用户信息
	 * @param userId
	 * @param servTypeId
	 * @return
	 */
	public HgwServUserObj getUserInfo(String userId){
		return hgwServUserDao.getUserInfo(userId, "10");
	}	
	
	/**
	 * 根据设备ID获取用户ID
	 * @param deviceId
	 * @return
	 */
	public String getUserId(String deviceId){
		return healthInfoDao.getUserId(deviceId);
	}
	
	/**
	 * 更新数据库中的dns
	 * @param cellMap
	 * @return
	 */
	public boolean updateDnsDB(Map<String,String> cellMap,String userId,String servType){
		return hgwServUserDao.editPartBussInfo(cellMap,userId,servType);
	}
	
	public String setDnsConfig(String device_id, String gw_type,String dns){
				
		String dnsDevice = getDsnDevice(device_id, gw_type);
		if(StringUtil.IsEmpty(dnsDevice)){
			logger.warn("getDsnDevice(){},{}获取dns节点为空",device_id,gw_type);
			return "获取设置DNS节点失败";
		}
		
		String result = "";
		ACSCorba acsCorba = new ACSCorba(gw_type);
		
		// 配置dns
		int saveResult = acsCorba.setValue(device_id, new ParameValueOBJ(dnsDevice,dns,"1"));
		
		switch (saveResult) {
		case 0 | 1:
			result = "成功";
			break;
		case -1:
			result = "设备连接失败";
			break;
		case -6:
			result = "设备正被操作";
			break;
		case -7:
			result = "系统参数错误";
			break;
		case -9:
			result = "系统内部错误";
			break;
		default:
			result = "其它:TR069错误";
			break;
		}
		return result;
	}
	
	/**
	 * 获取dns的下发节点
	 * @param device_id
	 * @param gw_type
	 * @return
	 */
	public String getDsnDevice(String device_id, String gw_type){
		SuperGatherCorba sgCorba = new SuperGatherCorba(gw_type);
		String dnsDevice = "";

		// 1、调用采集,采集InternetGatewayDevice.WANDevice下节点
		int irt = sgCorba.getCpeParams(device_id, ConstantClass.GATHER_WAN, 1);		
		logger.warn("调用采集结果=====" + irt);
		if (irt != 1)
		{
			logger.warn("调用采集失败!");
			return "";
		}
		
		// 2、从数据库获取wan_conn_id/wan_conn_sess_id
		List<Map> wanConnIds = getWanConnIds(device_id,gw_type);
		if (wanConnIds.isEmpty())
		{
			logger.warn("没有获取到Wan接口");
			return "";
		}
		
		for (Map map : wanConnIds)
		{
			String wan_conn_id = StringUtil
					.getStringValue(map.get("wan_conn_id"));
			String wan_conn_sess_id = StringUtil.getStringValue(map
					.get("wan_conn_sess_id"));
			String sessType = StringUtil.getStringValue(map.get("sess_type"));

			// 当前策略取第一个查询到的dns
			if (sessType.equals("1"))
			{
				dnsDevice = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice." + wan_conn_id + ".WANPPPConnection."
						+ wan_conn_sess_id + ".DNSServers";
				break;
			}
			else
			{
				dnsDevice = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice." + wan_conn_id + ".WANIPConnection."
						+ wan_conn_sess_id + ".DNSServers";
				break;
			}
		}
		return dnsDevice;
	}
	
	private List getWanConnIds(String device_id,String gw_type)
	{
		StringBuffer sql = new StringBuffer();
		List<Map> list = new ArrayList<Map>();
		
		sql.append("select b.sess_type,b.wan_conn_id,b.wan_conn_sess_id ");
		if(Global.GW_TYPE_ITMS.equals(gw_type)){
			sql.append("from gw_wan_conn a,gw_wan_conn_session b where a.device_id=b.device_id and a.wan_conn_id=b.wan_conn_id and upper(b.serv_list) like '%INTERNET%' and a.device_id='");
		}
		else{
			sql.append("from gw_wan_conn_bbms a,gw_wan_conn_session_bbms b where a.device_id=b.device_id and a.wan_id=b.wan_id and a.wan_conn_id=b.wan_conn_id and upper(b.serv_list) like '%INTERNET%' and a.device_id='");
		}		
		sql.append(device_id).append("'");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql.toString());
		for (int i = 0; i < cursor.getRecordSize(); i++)
		{
			list.add(cursor.getRecord(i));
		}
		return list;
	}
	
	public HgwServUserDAO getHgwServUserDao() {
		return hgwServUserDao;
	}
	public void setHgwServUserDao(HgwServUserDAO hgwServUserDao) {
		this.hgwServUserDao = hgwServUserDao;
	}

	public HealthInfoDAO getHealthInfoDao() {
		return healthInfoDao;
	}

	public void setHealthInfoDao(HealthInfoDAO healthInfoDao) {
		this.healthInfoDao = healthInfoDao;
	}
}
