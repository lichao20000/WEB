
package com.linkage.module.gwms.dao.gw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.gw.LanVlanOBJ;
import com.linkage.module.gwms.util.StringUtil;

public class LanVlanDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(LanVlanDAO.class);

	/**
	 * 通过device_id获取table:gw_lan_vlan_dhcp的数据(LanVlanOBJ的List)
	 * 
	 * @author wangsenbo
	 * @date Sep 28, 2009
	 * @return List 没有数据返回null
	 */
	public List<LanVlanOBJ> getLanVlanList(String deviceId)
	{
		logger.debug("getLanVlanList({})", deviceId);
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select ip_enable,ip_address,ip_mask,ip_address_type,dhcp_enable,");
			psql.append("dhcp_min_addr,dhcp_max_addr,dhcp_res_addr,dhcp_mask,dhcp_dns,");
			psql.append("dhcp_domain,dhcp_gateway,dhcp_lease_time ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_lan_vlan_dhcp where device_id=? ");
		psql.setString(1, deviceId);
		List list = jt.queryForList(psql.getSQL());
		List<LanVlanOBJ> lanVlanList = null;
		if (null != list && false == list.isEmpty())
		{
			lanVlanList = new ArrayList<LanVlanOBJ>();
			int size = list.size();
			for (int i = 0; i < size; i++)
			{
				LanVlanOBJ lanVlanObj = new LanVlanOBJ();
				Map tmap = (Map) list.get(i);
				lanVlanObj = getLanVlanOBJ(tmap);
				lanVlanList.add(lanVlanObj);
			}
		}
		return lanVlanList;
	}

	/**
	 * 把table:gw_lan_vlan_dhcp的数据由Map转换为LanVlanOBJ
	 * 
	 * @author wangsenbo
	 * @date Sep 28, 2009
	 * @return LanVlanOBJ
	 */
	public LanVlanOBJ getLanVlanOBJ(Map map)
	{
		logger.debug("getLanVlanOBJ({})", map);
		LanVlanOBJ lanVlanObj = null;
		if (map != null)
		{
			lanVlanObj = new LanVlanOBJ();
			lanVlanObj.setDeviceId(StringUtil.getStringValue(map.get("device_id")));
			lanVlanObj.setVlanI(StringUtil.getIntegerValue(map.get("vlan_i")));
			lanVlanObj.setVlanId(StringUtil.getIntegerValue(map.get("vlan_id")));
			lanVlanObj.setVlanName(StringUtil.getStringValue(map.get("vlan_name")));
			String portList = StringUtil.getStringValue(map.get("port_list"));
			lanVlanObj.setPortList(portList.replace("WLAN-Configuration",
					"WLANConfiguration").replace("LAN-EthernetInterfaceConfig",
					"LANEthernetInterfaceConfig"));
			if (false == StringUtil.IsEmpty(portList))
			{
				portList = getPort(portList);
			}
			lanVlanObj.setPort(portList);
			lanVlanObj.setIpEnable(StringUtil.getIntegerValue(map.get("ip_enable")));
			lanVlanObj.setIpAddress(StringUtil.getStringValue(map.get("ip_address")));
			lanVlanObj.setIpMask(StringUtil.getStringValue(map.get("ip_mask")));
			lanVlanObj.setIpAddressType(StringUtil.getStringValue(map.get("ip_address_type")));
			lanVlanObj.setDhcpEnable(StringUtil.getIntegerValue(map.get("dhcp_enable")));
			lanVlanObj.setDhcpMinAddr(StringUtil.getStringValue(map.get("dhcp_min_addr")));
			lanVlanObj.setDhcpMaxAddr(StringUtil.getStringValue(map.get("dhcp_max_addr")));
			lanVlanObj.setDhcpResAddr(StringUtil.getStringValue(map.get("dhcp_res_addr")));
			lanVlanObj.setDhcpMask(StringUtil.getStringValue(map.get("dhcp_mask")));
			lanVlanObj.setDhcpDns(StringUtil.getStringValue(map.get("dhcp_dns")));
			lanVlanObj.setDhcpDomain(StringUtil.getStringValue(map.get("dhcp_domain")));
			lanVlanObj.setDhcpGateway(StringUtil.getStringValue(map.get("dhcp_gateway")));
			long dlt = StringUtil.getLongValue(map.get("dhcp_lease_time"));
			int day = StringUtil.getIntegerValue(dlt / (24 * 60 * 60));
			int hour = StringUtil.getIntegerValue((dlt % (24 * 60 * 60)) / (60 * 60));
			int minute = StringUtil.getIntegerValue(((dlt % (24 * 60 * 60)) % (60 * 60)) / 60);
			lanVlanObj.setDhcpLeaseTime(day + "/" + hour + "/" + minute);
		}
		return lanVlanObj;
	}

	/**
	 * 获取Vlan数
	 * 
	 * @author wangsenbo
	 * @param deviceId
	 * @date Oct 28, 2009
	 * @return Map
	 */
	public Map getVlanNum(String deviceId)
	{
		logger.debug("getVlanNum({})", deviceId);
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select vlan_max_num,vlan_cur_num ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_lan_vlan_num where device_id=? ");
		psql.setString(1, deviceId);
		return queryForMap(psql.getSQL());
	}

	/**
	 * 把表里的节点数据转换为WLAN或LAN
	 * 
	 * @author wangsenbo
	 * @date Oct 29, 2009
	 * @return String
	 */
	public String getPort(String portList)
	{
		StringBuffer sb = new StringBuffer();
		String[] prot = portList.split(",");
		int length = prot.length;
		for (int i = 0; i < length; i++)
		{
			if (-1 != prot[i].indexOf("WLANConfiguration")
					|| -1 != prot[i].indexOf("WLAN-Configuration"))
			{
				sb.append("WLAN").append(prot[i].substring(prot[i].length() - 1)).append(
						",");
			}
			if (-1 != prot[i].indexOf("LANEthernetInterfaceConfig")
					|| -1 != prot[i].indexOf("LAN-EthernetInterfaceConfig"))
			{
				sb.append("LAN").append(prot[i].substring(prot[i].length() - 1)).append(
						",");
			}
		}
		portList = sb.toString();
		if (false == StringUtil.IsEmpty(portList))
		{
			portList = portList.substring(0, portList.length() - 1);
		}
		return portList;
	}
}
