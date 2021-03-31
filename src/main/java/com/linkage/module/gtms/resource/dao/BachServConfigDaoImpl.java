
package com.linkage.module.gtms.resource.dao;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gtms.resource.obj.BachServObj;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2014-4-11
 * @category com.linkage.module.gtms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BachServConfigDaoImpl extends SuperDAO implements BachServConfigDao
{

	private static Logger logger = LoggerFactory.getLogger(BachServConfigDaoImpl.class);

	@Override
	public int updateNetServInfo(BachServObj obj, String gw_type)
	{
		logger.debug("updateServUserSql()");
		String tabName = "hgwcust_serv_info";
		if ("2".equals(gw_type))
		{
			tabName = "egwcust_serv_info";
		}
		PrepareSQL psql = new PrepareSQL();
		psql.append("update " + tabName + " set ");
		if (!StringUtil.IsEmpty(obj.getNewNetUserName()))
		{
			psql.append("username='" + obj.getNewNetUserName() + "',");
		}
		if (!StringUtil.IsEmpty(obj.getNewNetPwd()))
		{
			psql.append("passwd='" + obj.getNewNetPwd() + "',");
		}
		if (!StringUtil.IsEmpty(obj.getNetWanType()))
		{
			psql.append("wan_type=" + obj.getNetWanType() + ",");
		}
		if (!StringUtil.IsEmpty(obj.getNetVlanId()))
		{
			psql.append("vlanid='" + obj.getNetVlanId() + "',");
		}
		psql.append(" open_status=?,updatetime=?  where user_id=? and serv_type_id=? and serv_status=1 ");
		if (!StringUtil.IsEmpty(obj.getOldNetUserName()))
		{
			psql.append(" and username = '" + obj.getOldNetUserName() + "'");
		}
		// 置为未开通
		psql.setInt(1, 0);
		psql.setLong(2, new Date().getTime() / 1000);
		//
		psql.setLong(3, obj.getUserId());
		psql.setInt(4, 10);
		return DBOperation.executeUpdate(psql.getSQL());
	}

	public int updateNetServParam(BachServObj obj)
	{
		logger.debug("updateNetServParam()");
		PrepareSQL psql = new PrepareSQL();
		psql.append("update tab_net_serv_param set ");
		if (!StringUtil.IsEmpty(obj.getNewNetUserName()))
		{
			psql.append("username='" + obj.getNewNetUserName() + "'");
		}
		if (!StringUtil.IsEmpty(obj.getOldNetUserName()))
		{
			psql.append(" where username = '" + obj.getOldNetUserName() + "' ");
		}
		return DBOperation.executeUpdate(psql.getSQL());
	}

	private String getBindPort(String bindPort)
	{
		String result = "";
		if (!StringUtil.IsEmpty(bindPort))
		{
			result = bindPort.replaceAll("L",
					"InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.")
					.replace("SSID",
							"InternetGatewayDevice.LANDevice.1.WLANConfiguration.");
		}
		return result;
	}

	@Override
	public int updateItvServInfo(BachServObj obj, String gw_type)
	{
		logger.debug("updateServUserSql()");
		String tabName = "hgwcust_serv_info";
		if ("2".equals(gw_type))
		{
			tabName = "egwcust_serv_info";
		}
		PrepareSQL psql = new PrepareSQL();
		psql.append("update " + tabName + " set ");
		if (!StringUtil.IsEmpty(obj.getNewItvUserName()))
		{
			psql.append("username='" + obj.getNewItvUserName() + "',");
		}
		if (!StringUtil.IsEmpty(obj.getItvBindPort()))
		{
			psql.append("bind_port='" + getBindPort(obj.getItvBindPort()) + "',");
		}
		if (!StringUtil.IsEmpty(obj.getItvVlanId()))
		{
			psql.append("vlanid='" + obj.getItvVlanId() + "',");
		}
		psql.append(" open_status=?,updatetime=?  where user_id=? and serv_type_id=? and serv_status=1");
		if (!StringUtil.IsEmpty(obj.getOldItvUserName()))
		{
			psql.append(" and username = '" + obj.getOldItvUserName() + "'");
		}
		// 置为未开通
		psql.setInt(1, 0);
		psql.setLong(2, new Date().getTime() / 1000);
		//
		psql.setLong(3, obj.getUserId());
		psql.setInt(4, 11);
		return DBOperation.executeUpdate(psql.getSQL());
	}

	@Override
	public Map<String, String> queryUserId(String username, String gw_type)
	{
		PrepareSQL psql = new PrepareSQL();
		String tabName = "tab_hgwcustomer";
		if ("2".equals(gw_type))
		{
			tabName = "tab_egwcustomer";
		}
		psql.append("select a.user_id,a.username,a.device_id,a.oui,a.device_serialnumber from "
				+ tabName + " a where  a.username = '" + username + "'");
		return DBOperation.getRecord(psql.getSQL());
	}

	@Override
	public int getSipId(BachServObj obj)
	{
		logger.debug("getSipId({})", new Object[] { obj });
		PrepareSQL psql = new PrepareSQL();
		psql.append("select sip_id from tab_sip_info where 1=1 ");
		if (!StringUtil.IsEmpty(obj.getMgcIp()))
		{
			psql.append(" and prox_serv='" + obj.getMgcIp() + "' ");
		}
		if (!StringUtil.IsEmpty(obj.getMgcPort()))
		{
			psql.append(" and prox_port=" + obj.getMgcPort());
		}
		if (!StringUtil.IsEmpty(obj.getStandMgcIp()))
		{
			psql.append(" and stand_prox_serv='" + obj.getStandMgcIp() + "' ");
		}
		if (!StringUtil.IsEmpty(obj.getStandMgcPort()))
		{
			psql.append(" and stand_prox_port=" + obj.getStandMgcPort());
		}
		if (!StringUtil.IsEmpty(obj.getMgcIp()))
		{
			psql.append(" and out_bound_proxy='" + obj.getMgcIp() + "' ");
		}
		if (!StringUtil.IsEmpty(obj.getMgcPort()))
		{
			psql.append(" and out_bound_port=" + obj.getMgcPort());
		}
		if (!StringUtil.IsEmpty(obj.getStandMgcIp()))
		{
			psql.append(" and stand_out_bound_proxy='" + obj.getStandMgcIp() + "' ");
		}
		if (!StringUtil.IsEmpty(obj.getStandMgcPort()))
		{
			psql.append(" and stand_out_bound_port=" + obj.getStandMgcPort());
		}
		if (!StringUtil.IsEmpty(obj.getMgcIp()))
		{
			psql.append(" and regi_serv='" + obj.getMgcIp() + "' ");
		}
		if (!StringUtil.IsEmpty(obj.getMgcPort()))
		{
			psql.append(" and regi_port=" + obj.getMgcPort());
		}
		if (!StringUtil.IsEmpty(obj.getStandMgcIp()))
		{
			psql.append(" and stand_regi_serv='" + obj.getStandMgcIp() + "' ");
		}
		if (!StringUtil.IsEmpty(obj.getStandMgcPort()))
		{
			psql.append(" and stand_regi_port=" + obj.getStandMgcPort());
		}
		Map<String, String> resMap = DBOperation.getRecord(psql.getSQL());
		if (null == resMap || resMap.isEmpty())
		{
			long maxSipId = DBOperation.getMaxId("sip_id", "tab_sip_info");
			psql = new PrepareSQL();
			++maxSipId;
			psql.append("insert into tab_sip_info (sip_id, prox_serv, prox_port, stand_prox_serv, stand_prox_port, ");
			psql.append(" regi_serv, regi_port, stand_regi_serv, stand_regi_port, ");
			psql.append(" out_bound_proxy, out_bound_port, stand_out_bound_proxy, stand_out_bound_port, remark) ");
			psql.append(" values (?,?,?,?,?,   ?,?,?,?,   ?,?,?,?,?)");
			//
			psql.setLong(1, maxSipId);
			psql.setString(2, obj.getMgcIp());
			psql.setInt(3, StringUtil.getIntegerValue(obj.getMgcPort()));
			psql.setString(4, obj.getStandMgcIp());
			psql.setInt(5, StringUtil.getIntegerValue(obj.getStandMgcPort()));
			psql.setString(6, obj.getMgcIp());
			psql.setInt(7, StringUtil.getIntegerValue(obj.getMgcPort()));
			psql.setString(8, obj.getStandMgcIp());
			psql.setInt(9, StringUtil.getIntegerValue(obj.getStandMgcPort()));
			psql.setString(10, obj.getMgcIp());
			psql.setInt(11, StringUtil.getIntegerValue(obj.getMgcPort()));
			psql.setString(12, obj.getStandMgcIp());
			psql.setInt(13, StringUtil.getIntegerValue(obj.getStandMgcPort()));
			//
			psql.setString(14, "EServer");
			DBOperation.executeUpdate(psql.getSQL());
			// 库中不存在，添加返回
			return StringUtil.getIntegerValue(maxSipId);
		}
		else
		{
			// 库中已经存在，直接返回
			return StringUtil.getIntegerValue(resMap.get("sip_id"));
		}
	}

	@Override
	public int updateVoipServInfo(BachServObj obj, String gw_type)
	{
		logger.debug("updateServUserSql()");
		String tabName = "hgwcust_serv_info";
		if ("2".equals(gw_type))
		{
			tabName = "egwcust_serv_info";
		}
		PrepareSQL psql = new PrepareSQL();
		psql.append("update " + tabName + " set ");
		if (!StringUtil.IsEmpty(obj.getVoiceVlanId()))
		{
			psql.append("vlanid='" + obj.getVoiceVlanId() + "',");
		}
		psql.append(" open_status=?,updatetime=?  where user_id=? and serv_type_id=? and serv_status=1");
		// 置为未开通
		psql.setInt(1, 0);
		psql.setLong(2, new Date().getTime() / 1000);
		//
		psql.setLong(3, obj.getUserId());
		psql.setInt(4, 14);
		return DBOperation.executeUpdate(psql.getSQL());
	}

	@Override
	public int updateVoipParam(BachServObj voipObj, String gw_type, int sipId)
	{
		// logger.debug("getUpdateVoipServParam({}, {})", voipObj, _userId);
		String tabName = "tab_voip_serv_param";
		if ("2".equals(gw_type))
		{
			tabName = "tab_egw_voip_serv_param";
		}
		PrepareSQL psql = new PrepareSQL();
		psql.append("update " + tabName + " set ");
		if (!StringUtil.IsEmpty(voipObj.getNewTelphone()))
		{
			psql.append("voip_phone='" + voipObj.getNewTelphone() + "',");
		}
		if (!StringUtil.IsEmpty(voipObj.getRegId()))
		{
			psql.append("REG_ID='" + voipObj.getRegId() + "',");
		}
		if (!StringUtil.IsEmpty(voipObj.getVoipPort()))
		{
			psql.append("VOIP_PORT='" + voipObj.getVoipPort() + "',");
		}
		if (sipId != 0)
		{
			psql.append(" sip_id= " + sipId + ",");
		}
		//
		psql.append(" updatetime=? where user_id=? ");
		if (!StringUtil.IsEmpty(voipObj.getOldTelphone()))
		{
			psql.append(" and voip_phone='" + voipObj.getOldTelphone() + "'");
		}
		//
		psql.setLong(1, new Date().getTime() / 1000);
		psql.setLong(2, voipObj.getUserId());
		return DBOperation.executeUpdate(psql.getSQL());
	}
}
