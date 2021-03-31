
package com.linkage.module.gtms.stb.resource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;

/**
 * @author yinlei3 (73167.)
 * @version 1.0
 * @since 2016年3月8日
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class SoftupgradByImportDAO extends SuperDAO
{

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(SoftupgradByImportDAO.class);

	/**
	 * 获取设备信息
	 * @param param
	 * @param username
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getDeviceInfo(String param, boolean username)
	{
		Map<String, String> taskMap = null;
		String sql = "select device_id,serv_account from stb_tab_gw_device  where device_serialnumber = ?  ";
		if (username)
		{
			sql = "select device_id from stb_tab_gw_device  where  serv_account = ?  ";
		}
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setString(1, StringUtil.getStringValue(param));
		try
		{
			taskMap = jt.queryForMap(taskSql.getSQL());
		}
		catch (DataAccessException e)
		{
			taskMap = null;
		}
		return taskMap;
	}

	/**
	 * 生成策略
	 * @param list
	 * @param versionPathId
	 * @return
	 */
	public String addStrategy(ArrayList<Map<String, String>> list, String versionPathId)
	{
		Map<String, String> taskMap = getversionPathById(versionPathId);
		if (taskMap == null || taskMap.isEmpty())
		{
			return "目标版本不存在";
		}
		String versionPath = StringUtil.getStringValue(taskMap, "version_path");
		if (StringUtil.IsEmpty(versionPath))
		{
			return "目标版本不存在";
		}
		;
		ArrayList<String> sqllist = new ArrayList<String>();
		for (Map<String, String> devInfoMap : list)
		{
			sqllist.addAll(strategySQL(devInfoMap, versionPath));
			if (sqllist.size() > 200)
			{
				DBOperation.executeUpdate(sqllist);
				sqllist.clear();
			}
		}
		if (!sqllist.isEmpty())
		{
			DBOperation.executeUpdate(sqllist);
			sqllist.clear();
		}
		return "解析生成策略成功";
	}

	/**
	 * 生成入策略的sql语句
	 *
	 * @author wangsenbo
	 * @date Jun 11, 2010
	 * @param
	 * @return List<String>
	 */
	public List<String> strategySQL(Map<String, String> devInfoMap, String versionPath)
	{
		StringBuilder sql = new StringBuilder();
		StringBuilder sql1 = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();
		if (devInfoMap == null || devInfoMap.isEmpty())
		{
		}
		String device_id = StringUtil.getStringValue(devInfoMap, "device_id");
		String sheetPara = stbSoftObj2Xml(versionPath);
		long id = StrategyOBJ.createStrategyId();
		long currentTime = TimeUtil.getCurrentTime();
		ArrayList<String> list = new ArrayList<String>();
		sql.append("delete from stb_gw_serv_strategy where device_id='")
				.append(device_id).append("' and temp_id=").append("5");

		sql1.append("insert into stb_gw_serv_strategy (");
		sql1.append("redo,id,acc_oid,time,type,device_id,sheet_para,service_id,task_id,order_id,sheet_type, temp_id, is_last_one,priority");
		sql1.append(") values (0,");
		sql1.append(id);
		sql1.append("," + WebUtil.getCurrentUser().getUser().getId());
		sql1.append("," + currentTime);
		sql1.append(", 4");
		sql1.append(",'" + device_id);
		sql1.append("','" + sheetPara);
		sql1.append("'," + 5);
		sql1.append(",'" + StringUtil.getStringValue(currentTime));
		sql1.append("'," + 1);
		sql1.append("," + 2);
		sql1.append("," + 5);
		sql1.append("," + 1);
		sql1.append("," + 1);
		sql1.append(")");

		sql2.append("insert into stb_gw_serv_strategy_log (");
		sql2.append("redo,id,acc_oid,time,type,device_id,sheet_para,service_id,task_id,order_id,sheet_type, temp_id, is_last_one,priority");
		sql2.append(") values (0,");
		sql2.append(id);
		sql2.append("," + WebUtil.getCurrentUser().getUser().getId());
		sql2.append("," + currentTime);
		sql2.append("," + 4);
		sql2.append(",'" + device_id);
		sql2.append("','" + sheetPara);
		sql2.append("'," + 5);
		sql2.append(",'" + StringUtil.getStringValue(currentTime));
		sql2.append("'," + 1);
		sql2.append("," + 2);
		sql2.append("," + 5);
		sql2.append("," + 1);
		sql2.append("," + 1);
		sql2.append(")");

		PrepareSQL psql = new PrepareSQL();

		psql.setSQL(sql.toString());
		list.add(psql.getSQL());
		psql.setSQL(sql1.toString());
		list.add(psql.getSQL());
		psql.setSQL(sql2.toString());
		list.add(psql.getSQL());

		sql = null;
		sql1 = null;
		sql2 = null;
		return list;
	}

	/**
	 * STB软件升级XML
	 *
	 * @author gongsj
	 * @date 2010-11-8
	 * @param versionPath
	 * @return
	 */
	private String stbSoftObj2Xml(String versionPath)
	{
		logger.debug("进入stbSoftObj2Xml...");
		String strXml = null;
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("STBSOFT");
		root.addElement("VersionPath").addText(versionPath);
		strXml = doc.asXML();
		return strXml;
	}

	/**
	 * @category getversionPath
	 */
	@SuppressWarnings("rawtypes")
	public List getversionPath()
	{
		logger.debug("getversionPath()");
		PrepareSQL pSQL = new PrepareSQL(
				"select id, softwareversion, version_path from stb_gw_version_file_path where valid=1 order by vendor_id");
		List<Map> list = jt.queryForList(pSQL.getSQL());
		if(list == null || list.isEmpty()){
			return null;
		}
		for (Map map : list) {
			String softwareversion = StringUtil.getStringValue(map, "softwareversion");
			String version_path = StringUtil.getStringValue(map, "version_path");
			map.put("version_path", "[" + softwareversion + "]-" + version_path);
		}
		return list;
	}

	/**
	 * @category getversionPathById
	 */
	@SuppressWarnings({ "unchecked" })
	public Map<String, String> getversionPathById(String id)
	{
		logger.debug("getversionPathById()");
		PrepareSQL pSQL = new PrepareSQL(
				"select id,version_path from stb_gw_version_file_path where id =" + id);
		return jt.queryForMap(pSQL.getSQL());
	}
}
