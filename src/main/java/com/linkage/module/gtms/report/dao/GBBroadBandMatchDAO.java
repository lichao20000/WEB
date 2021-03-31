
package com.linkage.module.gtms.report.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
/**
 * JXDX-REQ-ITMS-20190227-WWF-001(ITMS+家庭网关页面匹配终端百兆千兆信息需求)-批注
 *
 */
public class GBBroadBandMatchDAO extends SuperDAO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(GBBroadBandMatchDAO.class);

	/** 根据oui和设备序列号查*/
	public List<Map> devInfo(String oui, String sn) {
		logger.debug("GBBroadBandMatchDAO.devInfo()");
		List<Map> list = new ArrayList<Map>();
		Map map = new HashMap();
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.setSQL("select b.vendor_name, c.device_model, d.hardwareversion, d.softwareversion, e.gbbroadband "
				+ "from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_device_version_attribute e "
				+ "where a.vendor_id = b.vendor_id and a.device_model_id = c.device_model_id and a.devicetype_id = d.devicetype_id "
				+ "and a.devicetype_id = e.devicetype_id and a.oui = '"+oui+"' and a.device_serialnumber ='"+sn+"'");
		list = jt.queryForList(psql.getSQL());
		if(list.size()<1) {
			map.put("vendor_name", "");
			map.put("device_model", "");
			map.put("hardwareversion", "");
			map.put("softwareversion", "");
			map.put("gbbroadband", "");
			list.add(map);
		}else {
			map = list.get(0);
			if(map.get("gbbroadband").toString().equals("1")) {
				map.put("gbbroadband", "千兆");
			}else {
				map.put("gbbroadband", "百兆");
			}
		}
		return list;
	}
}
