
package com.linkage.module.gtms.stb.resource.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.resource.dao.SoftwareDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author zzs (Ailk No.78987)
 * @version 1.0
 * @since 2018-10-19
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class StbSoftWareDAO extends SuperDAO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(SoftwareDAO.class);

	public boolean doSQLList(ArrayList<String> sqllist)
	{
		int iCode[] = DataSetBean.doBatch(sqllist);
		if (iCode != null && iCode.length > 0)
		{
			logger.debug("批量执行策略入库：  成功");
			return true;
		}
		else
		{
			logger.debug("批量执行策略入库：  失败");
			return false;
		}
	}

	/**
	 * 获取软件升级的目标型号，版本对应关系
	 *
	 * @param
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return Map
	 */
	public Map getSoftUp()
	{
		PrepareSQL psql = new PrepareSQL("select temp_id, devicetype_id_old, devicetype_id from stb_gw_soft_upgrade_temp_map");
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());

		Map softUpMap = new HashMap();

		String temp_id = "";
		String devicetype_id_old = "";
		String temp_id_devicetype_id_old = "";
		String devicetype_id = "";
		for (Map<String, String> map : list) {
			temp_id = StringUtil.getStringValue(map, "temp_id");
			devicetype_id_old = StringUtil.getStringValue(map, "devicetype_id_old");
			devicetype_id = StringUtil.getStringValue(map, "devicetype_id");

			temp_id_devicetype_id_old =  temp_id  + "|" + devicetype_id_old;
			softUpMap.put(temp_id_devicetype_id_old, devicetype_id);
		}

		return softUpMap;
	}

	/**
	 * 根据devicetype_id获取软件升级的工单参数
	 *
	 * @param
	 * @author zzs(78987)
	 * @date 2018-10-19
	 * @return Map
	 */
	public Map<String, Map> getSoftFileInfo()
	{
		String strSQL = "select devicetype_id, outter_url, server_dir, softwarefile_name, softwarefile_size, softwarefile_name"
				+ " from stb_tab_software_file a, tab_file_server b where a.dir_id=b.dir_id"
				+ " and softwarefile_isexist=1";
		PrepareSQL psql = new PrepareSQL(strSQL);
		List list = jt.queryForList(psql.getSQL());
		Map<String, Map> map = new HashMap<String, Map>();

		for (int i = 0; i < list.size(); i++)
		{
			Map tmap = (Map) list.get(i);
			tmap.put("file_url",
					StringUtil.getStringValue(tmap, "outter_url") + "/" +
					StringUtil.getStringValue(tmap, "server_dir") + "/" +
					StringUtil.getStringValue(tmap, "softwarefile_name")
			);
			map.put(StringUtil.getStringValue(tmap.get("devicetype_id")), tmap);
		}
		return map;
	}

	/**
	 * 获取设备的型号
	 *
	 * @param
	 * @author zzs
	 * @date 2018-10-19
	 * @return Map
	 */
	public String getDevicetypeId(String deviceId)
	{
		String sql = "select devicetype_id from stb_tab_gw_device where device_id = ?";
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(sql);
		psql.setString(1, deviceId);
		return StringUtil.getStringValue(queryForMap(psql.getSQL()).get("devicetype_id"));
	}

	/**
	 * 批量软件升级，添加进任务表
	 */
	@SuppressWarnings("deprecation")
	public int addToTask(long taskId, String vendorId, long cityId, String pathId,
			String strategyType, long accoid, String devicetypeId, String ipCheck,
			String ipSG, String taskDesc, String param_sql)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.setSQL("insert into stb_gw_softup_task(task_id,vendor_id,city_id,version_path_id,record_time,update_time,acc_oid,valid,strategy_type,check_ip,task_desc,Match_sql,is_import_task) values(?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		long nowtime = new DateTimeUtil().getLongTime();
		sql.setLong(1, taskId);
		sql.setString(2, vendorId);
		sql.setString(3, String.valueOf(cityId));
		sql.setLong(4, StringUtil.getLongValue(pathId));
		sql.setLong(5, nowtime);
		sql.setLong(6, nowtime);
		sql.setLong(7, accoid);
		sql.setLong(8, 1);
		sql.setLong(9, StringUtil.getLongValue(strategyType));
		sql.setInt(10, Integer.parseInt(ipCheck));
		sql.setString(11, taskDesc);
		sql.setString(12, param_sql);
		sql.setLong(13, 0);
		int ier = DBOperation.executeUpdate(sql.getSQL());
		if (ier > 0)
		{
			logger.debug("任务定制：  成功");
			return 1;
		}
		else
		{
			logger.debug("任务定制：  失败");
			return 0;
		}
	}
}
