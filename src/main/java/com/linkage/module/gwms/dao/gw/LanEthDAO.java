
package com.linkage.module.gwms.dao.gw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.gw.LanEthObj;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-7-10
 */
@SuppressWarnings("rawtypes")
public class LanEthDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(LanEthDAO.class);

	/**
	 * 返回设备的第一个状态为'Up'的结点信息
	 */
	public LanEthObj getUpLanEthObj(String deviceId)
	{
		logger.debug("getUpLanEthObj(deviceId):" + deviceId);
		LanEthObj[] lanEthArr = getLanEthObj(deviceId, "Up");
		if (null == lanEthArr || lanEthArr.length < 1){
			return null;
		}
	
		return lanEthArr[0];
	}

	/**
	 * 获取设备LAN侧的信息,LanEthObj对象数组
	 */
	public LanEthObj[] getLanEthObjArr(String deviceId)
	{
		logger.debug("getLanEthObjArr(deviceId):" + deviceId);
		return getLanEthObj(deviceId, null);
	}

	/**
	 * 返回设备的所有状态为state的结点信息
	 */
	public LanEthObj[] getLanEthObj(String deviceId, String state)
	{
		logger.debug("getLanEthObj(deviceId):" + deviceId);
		LanEthObj[] lanEthArr = null;

		if (StringUtil.IsEmpty(deviceId))
		{
			logger.warn("getLanEthObj(deviceId): is null or ''");
		}
		else
		{
			String strSQL= "select lan_id,lan_eth_id,enable,status,mac_address,gather_time from "
						+ Global.getTabName(deviceId,"gw_lan_eth") +" where device_id=? ";
//			String gw_type = LipossGlobals.getGw_Type(deviceId);
//			if(gw_type.equals(Global.GW_TYPE_ITMS)){
//				strSQL = "select * from gw_lan_eth where device_id=? ";
//			}else{
//				strSQL = "select * from gw_lan_eth" + Global.getSuffixName(gw_type) +  " where device_id=? ";
//			}
			
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.setString(1, deviceId);
			if (!StringUtil.IsEmpty(state)){
				psql.append(" and status='" + state + "' ");
			}
			psql.append(" order by lan_eth_id ");
			List rList = jt.queryForList(psql.getSQL());
			if (null == rList || rList.size() < 1)
			{
				logger.warn("[{}] getLanEthObj(deviceId): 没有该deviceId对应的lan结点记录",deviceId);
			}
			else
			{
				int size = rList.size();
				lanEthArr = new LanEthObj[size];
				for (int i = 0; i < size; i++)
				{
					Map tMap = (Map) rList.get(i);
					lanEthArr[i] = new LanEthObj();
					lanEthArr[i].setLanid(StringUtil.getIntegerValue(tMap.get("lan_id")));
					lanEthArr[i].setLanEthid(StringUtil.getIntegerValue(tMap.get("lan_eth_id")));
					lanEthArr[i].setEnable(StringUtil.getStringValue(tMap.get("enable")));
					lanEthArr[i].setStatus(StringUtil.getStringValue(tMap.get("status")));
					lanEthArr[i].setMac(StringUtil.getStringValue(tMap.get("mac_address")));
					lanEthArr[i].setGatherTime(StringUtil.getLongValue(tMap,"gather_time"));
				}
			}
		}
		return lanEthArr;
	}

	/**
	 * 获取lan口的
	 */
	public String getLanEthStatus(String deviceId, String inter)
	{
		logger.debug("getLanEthStatus({},{})", deviceId, inter);
		String status = null;
		String gw_type = LipossGlobals.getGw_Type(deviceId);

		String strSQL;
		if(gw_type.equals(Global.GW_TYPE_ITMS))
		{
			strSQL = "select status from gw_lan_eth where device_id=?"
					+ " and lan_id=1 and lan_eth_id=?";
		}
		else
		{
			strSQL = "select status from gw_lan_eth" + Global.getSuffixName(gw_type) 
					+ " where device_id=? and lan_id=1 and lan_eth_id=?";
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, deviceId);
		psql.setStringExt(2, inter, false);
		List rList = jt.queryForList(psql.getSQL());
		if (null == rList || rList.size() < 1)
		{
			logger.warn("getLanEthStatus(deviceId): 没有该deviceId对应的lan结点记录");
		}
		else
		{
			// 只有一条
			Map tMap = (Map) rList.get(0);
			status = StringUtil.getStringValue(tMap.get("status"));
		}
		return status;
	}

	/**
	 * 获取Lan口的列表
	 */
	@SuppressWarnings("unchecked")
	public List getLanEth(String deviceId)
	{
		logger.debug("getLanEth({}):" + deviceId);
		List lanList = null;
		
		if (StringUtil.IsEmpty(deviceId))
		{
			logger.warn("getLanEthObj(deviceId): is null or ''");
		}
		else
		{
			String gw_type = LipossGlobals.getGw_Type(deviceId);
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psql.append("select lan_id,lan_eth_id ");
			}else{
				psql.append("select * ");
			}
			if(gw_type.equals(Global.GW_TYPE_ITMS)){
				psql.append("from gw_lan_eth ");
			}else{
				psql.append("from gw_lan_eth " + Global.getSuffixName(gw_type));
			}
			psql.append("where device_id=? order by lan_eth_id");
			psql.setString(1, deviceId);
			
			List rList = jt.queryForList(psql.getSQL());
			if (null == rList || rList.size() < 1)
			{
				logger.warn("getLanEth(deviceId): 没有该deviceId对应的lan结点记录");
			}
			else
			{
				int size = rList.size();	
				lanList = new ArrayList();
				for (int i = 0; i < size; i++)
				{
					Map tMap = (Map) rList.get(i);
					StringBuffer key = new StringBuffer();
					Map rmap = new HashMap<String, String>();
					key.append("InternetGatewayDevice.LANDevice.")
					   .append(StringUtil.getIntegerValue(tMap.get("lan_id")))
					   .append(".LANEthernetInterfaceConfig.")
					   .append(StringUtil.getIntegerValue(tMap.get("lan_eth_id")));
					StringBuffer value = new StringBuffer();
					value.append("LAN")
						 .append(StringUtil.getIntegerValue(tMap.get("lan_eth_id")));
					rmap.put(key, value);
					lanList.add(rmap);
				}
			}
		}
		return lanList;
	}
}
