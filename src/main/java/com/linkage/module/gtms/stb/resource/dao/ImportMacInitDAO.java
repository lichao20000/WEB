
package com.linkage.module.gtms.stb.resource.dao;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-6-5
 * @category com.linkage.module.lims.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ImportMacInitDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(ImportMacInitDAO.class);

	/**
	 * 属地ID、属地名Map<city_name, city_id> 初始化city_id与city_name对因映射关系
	 *
	 * @return
	 */
	public Map<String, String> getCityIdCityNameMap()
	{
		PrepareSQL psql = new PrepareSQL(
				"select city_name, city_id from tab_city order by city_id");
		Map<String, String> map = DataSetBean.getMap(psql.getSQL());
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

//	public int[] batchSql(ArrayList list)
//	{
//		if (null != list && !list.isEmpty())
//		{
//			return DataSetBean.doBatch(list);
//		}
//		else
//		{
//			return new int[0];
//		}
//	}

	public int batchSql(ArrayList<String> list)
	{
		if(null == list || list.isEmpty())
		{
			logger.warn("ImportMacInitDAO>batchSql():sql list is null,or list.size =0");
			return 0;
		}
		return DBOperation.executeUpdate(list);
	}


	public String getDeviceId()
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB() == 1)
		{// oracle
			psql.append("select max(to_number(t.device_id)) device_id from stb_tab_devmac_init t");
		}
		else if(DBUtil.GetDB() == 3)
		{// mysql
			psql.append("select max(cast(t.device_id as signed)) device_id from stb_tab_devmac_init t");
		}
		else
		{
			psql.append("select max(convert(numeric, t.device_id)) device_id from stb_tab_devmac_init t");
		}
		Map map = DataSetBean.getRecord(psql.getSQL());
		return (String) map.get("device_id");
	}

	/**
	 * 根据设备Mac检查Mac唯一性
	 *
	 * @param deviceMac
	 *            设备Mac
	 * @param device_sn
	 *            设备序列号，该参数不被使用
	 * @return 返回mac存在个数，0表示不存在 >0表示存在
	 */
	public int checkDeviceMacAndSn(String deviceMac, String device_sn)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_tab_devmac_init where 1=1 ");
		if (!StringUtil.IsEmpty(deviceMac))
		{
			sql.append(" and cpe_mac='").append(deviceMac).append("'");
		}
		// 江西电信 Mac作为唯一性校验，去掉设备序列号
		// if(!StringUtil.IsEmpty(device_sn)){
		// sql.append(" and device_sn='").append(device_sn).append("'");
		// }
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForInt(psql.getSQL());
	}
}
