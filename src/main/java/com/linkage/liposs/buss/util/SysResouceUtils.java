package com.linkage.liposs.buss.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.resource.DeviceAct;

/**
 * 系统的常用工具方法。
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-11-18
 * @category utils
 */
public class SysResouceUtils
{
	/**
	 * 根据当前用户获取和他关联的gatherid
	 * 
	 * @param areaid
	 *            用户的域id
	 * @param devA
	 *            属性资源类，应该通过spring注入给调用该方法的dao类，然后传入即可
	 * @return gatherid的字符串，类似于'1','2','3','4','5'
	 */
	public static String getGatherIdForSql(String areaid, DeviceAct devA)
	{
		String gathers = null;// 域的对应gatherid
		List m_Gathers = devA.getGathersWithAreaId(areaid);
		if (m_Gathers.size() > 0)
			{
				gathers = "'" + m_Gathers.get(0) + "'";
			}
		for (int k = 1; k < m_Gathers.size(); k++)
			{
				gathers += ",'" + m_Gathers.get(k) + "'";
			}
		return gathers;
	}
	/**
	 * 查询所有设备和帐号对应关系，返回map
	 * key为 oui-device_serialnumber；value为customer_name
	 * @return Map<String, String>
	 * 
	 */
	public static Map<String, String> getCustomerMap(){
		Map<String, String> customer = new HashMap<String, String>();
		// 查询所有用户名和设备对应关系
		String sql = "select a.customer_name,b.oui,b.device_serialnumber from tab_customerinfo a,tab_egwcustomer b where a.customer_id=b.customer_id";
		PrepareSQL psql = new PrepareSQL(sql);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		while (fields != null){
			customer.put(fields.get("oui") + "-" + fields.get("device_serialnumber"), (String)fields.get("customer_name"));
			fields = cursor.getNext();
		}
		return customer;
	}
}