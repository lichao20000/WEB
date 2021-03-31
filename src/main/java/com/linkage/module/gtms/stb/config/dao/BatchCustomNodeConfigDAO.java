
package com.linkage.module.gtms.stb.config.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;

public class BatchCustomNodeConfigDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory
			.getLogger(BatchCustomNodeConfigDAO.class);

	public int batchNodeConfig(long taskId, String cityId, String vendorId,
			String ipCheck, String macCheck, String taskName, long acc_oid, long addTime,
			String ipSG, String macSG,String filePath, String deviceModelIds, String deviceTypeIds,
			String paramNodePath, String paramValue, String paramType, String type)
	{
		if(!LipossGlobals.inArea(Global.JXDX))
		{
			List<String> sqllist = new ArrayList<String>();
			PrepareSQL sql1 = new PrepareSQL(
					"insert into  stb_batch_con_task(task_id,city_id,vendor_id,check_ip,status,check_mac,task_name,acc_oid,add_time) values(?,?,?,?,?,?,?,?,?)");
			sql1.setLong(1, taskId);
			sql1.setString(2, cityId);
			sql1.setString(3, vendorId);
			sql1.setInt(4, StringUtil.getIntegerValue(ipCheck));
			sql1.setInt(5, 0); // status:1生效 , 0失效
			sql1.setInt(6, StringUtil.getIntegerValue(macCheck));
			sql1.setString(7, taskName);
			sql1.setLong(8, acc_oid);
			sql1.setLong(9, addTime);
			sqllist.add(sql1.getSQL());
			// 版本
			String[] temp = deviceModelIds.split(",");
			String[] temp2 = deviceTypeIds.split(",");
			List lt = null;
			Map map = null;
			for (String devicemodelId : temp)
			{
				lt = this.getVersionList(devicemodelId);
				if (null != lt)
				{
					String softId = "";
					for (int i = 0; i < lt.size(); i++)
					{
						map = (Map) lt.get(i);
						softId = StringUtil.getStringValue(map.get("devicetype_id"));
						for (String devicetypeId : temp2)
						{
							if (devicetypeId.equals(softId))
							{
								PrepareSQL sql2 = new PrepareSQL(
										"insert into stb_batch_con_version values(?,?,?,?)");
								sql2.setLong(1, taskId);
								sql2.setString(2, vendorId);
								sql2.setString(3, devicemodelId);
								sql2.setLong(4, StringUtil.getLongValue(devicetypeId));
								sqllist.add(sql2.getSQL());
							}
						}
					}
				}
			}
			// 如果启动校验IP地址段,入库
			if ("1".equals(ipCheck.trim()))
			{
				String[] ipDuan = ipSG.trim().split(";");
				String[] ipStr;
				for (String s : ipDuan)
				{
					PrepareSQL sql3 = new PrepareSQL(
							"insert into stb_batch_con_ipcheck values(?,?,?)");
					sql3.setLong(1, taskId);
					ipStr = s.split(",");
					sql3.setString(2, getFillIP(ipStr[0]));
					sql3.setString(3, getFillIP(ipStr[1]));
					sqllist.add(sql3.getSQL());
				}
			}
			// 如果启动校验MAC地址段,入库
			if ("1".equals(macCheck.trim()))
			{
				String[] macDuan = macSG.trim().split(";");
				String[] macStr;
				for (String s : macDuan)
				{
					PrepareSQL sql3 = new PrepareSQL(
							"insert into stb_batch_con_maccheck  values(?,?,?)");
					sql3.setLong(1, taskId);
					macStr = s.split(",");
					sql3.setString(2, macStr[0]);
					sql3.setString(3, macStr[1]);
					sqllist.add(sql3.getSQL());
				}
			}
			// 设置参数
			String[] paramNodePaths = paramNodePath.split(",");
			String[] paramValues = paramValue.split(",");
			String[] paramTypes = paramType.split(",");
			int len = paramNodePaths.length;
			for (int i = 0; i < len - 1; i++)
			{
				PrepareSQL sql4 = new PrepareSQL(
						"insert into stb_batch_con_para_value values(?,?,?,?)");
				sql4.setLong(1, taskId);
				sql4.setString(2, paramNodePaths[i + 1]);
				sql4.setLong(3, StringUtil.getLongValue(paramTypes[i + 1]));
				sql4.setString(4, paramValues[i + 1]);
				sqllist.add(sql4.getSQL());
			}
			int[] ier = doBatch(sqllist);
			if (ier != null && ier.length > 0)
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
		else
		{
			List<String> sqllist = new ArrayList<String>();

			PrepareSQL sql1 = null;
			if("1".equals(type)){
				sql1 = new PrepareSQL(
						"insert into  stb_batch_con_task(task_id,city_id,vendor_id,check_ip,status,check_mac,task_name,acc_oid,add_time,file_path,update_time,type) " +
						" values(?,?,?,?,?,?,?,?,?,?,?,'1')");
			}else{
				sql1 = new PrepareSQL(
						"insert into  stb_batch_con_task(task_id,city_id,vendor_id,check_ip,status,check_mac,task_name,acc_oid,add_time,file_path,update_time) " +
						" values(?,?,?,?,?,?,?,?,?,?,?)");
			}

			sql1.setLong(1, taskId);
			sql1.setString(2, cityId);
			if(vendorId != null && vendorId.indexOf("#") != -1){
				sql1.setString(3, "-1");
			}else{
				sql1.setString(3, vendorId);
			}
			sql1.setInt(4, StringUtil.getIntegerValue(ipCheck));

//			if(null!=filePath && filePath.trim().length()!=0){
//				sql1.setInt(5, 2); // status:1生效 , 0失效 , 2导入账号方式待激活
//			}else{
//				sql1.setInt(5, 0); // status:1生效 , 0失效 , 2导入账号方式待激活
//			}
			sql1.setInt(5, 0);
			sql1.setInt(6, StringUtil.getIntegerValue(macCheck));
			sql1.setString(7, taskName);
			sql1.setLong(8, acc_oid);
			sql1.setLong(9, addTime);
			sql1.setString(10, filePath);
			sql1.setLong(11, addTime);
			sqllist.add(sql1.getSQL());

			// 版本 chenxj ↓
			if (vendorId != null && vendorId.indexOf("#") != -1
					&& deviceModelIds != null && deviceModelIds.indexOf("#") != -1
					&& deviceTypeIds != null && deviceTypeIds.indexOf("#") != -1) {
				String[] vendorId_data = vendorId.split("#");
				String[] deviceModelIds_data = deviceModelIds.split("#");
				String[] deviceTypeIds_data = deviceTypeIds.split("#");

				for (int count = 0; count < vendorId_data.length; count++) {
					String[] temp = deviceModelIds_data[count].split(",");
					String[] temp2 = deviceTypeIds_data[count].split(",");
					List lt = null;
					Map map = null;
					for (String devicemodelId : temp) {
						lt = this.getVersionList(devicemodelId);
						if (null != lt) {
							String softId = "";
							for (int i = 0; i < lt.size(); i++) {
								map = (Map) lt.get(i);
								softId = StringUtil.getStringValue(map
										.get("devicetype_id"));
								for (String devicetypeId : temp2) {
									if (devicetypeId.equals(softId)) {
										PrepareSQL sql2 = new PrepareSQL(
												"insert into  stb_batch_con_version  values(?,?,?,?)");
										sql2.setLong(1, taskId);
										sql2.setString(2, vendorId_data[count]);
										sql2.setString(3, devicemodelId);
										sql2.setLong(4, StringUtil
												.getLongValue(devicetypeId));
										sqllist.add(sql2.getSQL());
									}
								}
							}
						}
					}
				}
			} else {
				String[] temp = deviceModelIds.split(",");
				String[] temp2 = deviceTypeIds.split(",");
				List lt = null;
				Map map = null;
				for (String devicemodelId : temp) {
					lt = this.getVersionList(devicemodelId);
					if (null != lt) {
						String softId = "";
						for (int i = 0; i < lt.size(); i++) {
							map = (Map) lt.get(i);
							softId = StringUtil.getStringValue(map
									.get("devicetype_id"));
							for (String devicetypeId : temp2) {
								if (devicetypeId.equals(softId)) {
									PrepareSQL sql2 = new PrepareSQL(
											"insert into  stb_batch_con_version  values(?,?,?,?)");
									sql2.setLong(1, taskId);
									sql2.setString(2, vendorId);
									sql2.setString(3, devicemodelId);
									sql2.setLong(4,
											StringUtil.getLongValue(devicetypeId));
									sqllist.add(sql2.getSQL());
								}
							}
						}
					}
				}
			}
			// chenxj↑
			// 如果启动校验IP地址段,入库
			if ("1".equals(ipCheck.trim()))
			{
				String[] ipDuan = ipSG.trim().split(";");
				String[] ipStr;
				for (String s : ipDuan)
				{
					PrepareSQL sql3 = new PrepareSQL(
							"insert into stb_batch_con_ipcheck values(?,?,?)");
					sql3.setLong(1, taskId);
					ipStr = s.split(",");
					sql3.setString(2, getFillIP(ipStr[0]));
					sql3.setString(3, getFillIP(ipStr[1]));
					sqllist.add(sql3.getSQL());
				}
			}
			// 如果启动校验MAC地址段,入库
			if ("1".equals(macCheck.trim()))
			{
				String[] macDuan = macSG.trim().split(";");
				String[] macStr;
				for (String s : macDuan)
				{
					PrepareSQL sql3 = new PrepareSQL(
							"insert into stb_batch_con_maccheck  values(?,?,?)");
					sql3.setLong(1, taskId);
					macStr = s.split(",");
					sql3.setString(2, macStr[0]);
					sql3.setString(3, macStr[1]);
					sqllist.add(sql3.getSQL());
				}
			}
			// 设置参数
			String[] paramNodePaths = paramNodePath.split(",");
			String[] paramValues = paramValue.split(",");
			String[] paramTypes = paramType.split(",");
			int len = paramNodePaths.length;
			for (int i = 0; i < len - 1; i++)
			{
				PrepareSQL sql4 = new PrepareSQL(
						"insert into stb_batch_con_para_value values(?,?,?,?)");
				sql4.setLong(1, taskId);
				sql4.setString(2, paramNodePaths[i + 1]);
				sql4.setLong(3, StringUtil.getLongValue(paramTypes[i + 1]));
				sql4.setString(4, paramValues[i + 1]);
				sqllist.add(sql4.getSQL());
			}
			int[] ier = doBatch(sqllist);
			if (ier != null && ier.length > 0)
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

	/**
	 * 填充IP
	 */
	private String getFillIP(String ip)
	{
		String fillIP = ip;
		String[] ipArray = new String[4];
		ipArray = ip.split("\\.");
		for (int i = 0; i < 4; i++)
		{
			if (ipArray[i].length() == 1)
			{
				ipArray[i] = "00" + ipArray[i];
			}
			else if (ipArray[i].length() == 2)
			{
				ipArray[i] = "0" + ipArray[i];
			}
		}
		fillIP = ipArray[0] + "." + ipArray[1] + "." + ipArray[2] + "." + ipArray[3];
		return fillIP;
	}

	/**
	 * @category getVendor 获取所有的厂商
	 */
	public List getVendor()
	{
		logger.debug("getVendor()");
		PrepareSQL pSQL = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from stb_tab_vendor");
		return jt.queryForList(pSQL.toString());
	}

	// 根据厂商获取型号
	public List getDeviceModel(String vendorId)
	{
		logger.debug("getDeviceModel(vendorId:{})", vendorId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select a.device_model_id,a.device_model from stb_gw_device_model a where 1=1 ");
		if (null != vendorId && !"".equals(vendorId) && !"-1".equals(vendorId))
		{
			pSQL.append(" and a.vendor_id='");
			pSQL.append(vendorId);
			pSQL.append("'");
		}
		return jt.queryForList(pSQL.toString());
	}

	/**
	 * @category getVersionList 获取所有的设备版本
	 * @param vendor_id
	 * @param deviceModelId
	 * @return List
	 */
	public List getVersionList(String deviceModelIds)
	{
		logger.debug("getVersionList(deviceModelId:{})", deviceModelIds);
		String sql = "(";
		String[] tempId = deviceModelIds.split(",");
		if (tempId.length > 0)
		{
			for (int i = 0; i < tempId.length; i++)
			{
				sql += "'" + tempId[i] + "',";
			}
			sql = sql.substring(0, sql.length() - 1) + ")";
		}
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select a.devicetype_id,a.softwareversion from stb_tab_devicetype_info a where 1=1 ");
		if (!StringUtil.IsEmpty(sql))
		{
			pSQL.append(" and a.device_model_id in");
			pSQL.append(sql);
			pSQL.append("");
		}
		return jt.queryForList(pSQL.toString());
	}

	@SuppressWarnings("rawtypes")
	public int batchNodeConfigByITV(long taskId, String cityId, String vendorId,
			String taskName, long acc_oid, long addTime, String deviceModelIds,
			String deviceTypeIds, String paramNodePath, String paramValue,
			String paramType)
	{
		List<String> sqllist = new ArrayList<String>();
		PrepareSQL sql1 = new PrepareSQL(
				"insert into  stb_batch_con_task(task_id,city_id,vendor_id,check_ip,status,check_mac,task_name,acc_oid,add_time) values(?,?,?,?,?,?,?,?,?)");
		sql1.setLong(1, taskId);
		sql1.setString(2, cityId);
		sql1.setString(3, vendorId);
		sql1.setInt(4, 0);
		sql1.setInt(5, 1); // status:1生效 , 0失效
		sql1.setInt(6, 0);
		sql1.setString(7, taskName);
		sql1.setLong(8, acc_oid);
		sql1.setLong(9, addTime);
		sqllist.add(sql1.getSQL());
		// 版本
		String[] temp = deviceModelIds.split(",");
		String[] temp2 = deviceTypeIds.split(",");
		List lt = null;
		Map map = null;
		for (String devicemodelId : temp)
		{
			lt = this.getVersionList(devicemodelId);
			if (null != lt)
			{
				String softId = "";
				for (int i = 0; i < lt.size(); i++)
				{
					map = (Map) lt.get(i);
					softId = StringUtil.getStringValue(map.get("devicetype_id"));
					for (String devicetypeId : temp2)
					{
						if (devicetypeId.equals(softId))
						{
							PrepareSQL sql2 = new PrepareSQL(
									"insert into stb_batch_con_version values(?,?,?,?)");
							sql2.setLong(1, taskId);
							sql2.setString(2, vendorId);
							sql2.setString(3, devicemodelId);
							sql2.setLong(4, StringUtil.getLongValue(devicetypeId));
							sqllist.add(sql2.getSQL());
						}
					}
				}
			}
		}
		// 设置参数
		String[] paramNodePaths = paramNodePath.split(",");
		String[] paramValues = paramValue.split(",");
		String[] paramTypes = paramType.split(",");
		int len = paramNodePaths.length;
		for (int i = 0; i < len - 1; i++)
		{
			PrepareSQL sql3 = new PrepareSQL(
					"insert into stb_batch_con_para_value values(?,?,?,?)");
			sql3.setLong(1, taskId);
			sql3.setString(2, paramNodePaths[i + 1]);
			sql3.setLong(3, StringUtil.getLongValue(paramTypes[i + 1]));
			sql3.setString(4, paramValues[i + 1]);
			sqllist.add(sql3.getSQL());
		}
		int[] ier = doBatch(sqllist);
		if (ier != null && ier.length > 0)
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

	public int queryUndoNum()
	{
		logger.debug("queryUndoNum()");
		PrepareSQL psql = new PrepareSQL(
				"select count(*) from gw_serv_strategy where status=0 and type>0 and exec_count<=3 and service_id!=5");
		int num = jt.queryForInt(psql.getSQL());
		return num;
	}

//	@SuppressWarnings("unchecked")
//	public List<Map<String, String>> getDeviceInfo(String deviceId)
//	{
//		PrepareSQL psql = new PrepareSQL(
//				"select a.oui,a.device_serialnumber,b.username from  tab_gw_device a left join tab_hgwcustomer b on a.device_id  = b.device_id where a.device_id = ?");
//		psql.setString(1, deviceId);
//		return jt.queryForList(psql.getSQL());
//	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDeviceInfo(String deviceId)
	{
		PrepareSQL psql = new PrepareSQL(
				"select a.oui,a.device_serialnumber,b.serv_account username from  stb_tab_gw_device a left join stb_tab_customer b on a.customer_id  = b.customer_id where a.device_id = ?");
		psql.setString(1, deviceId);
		return jt.queryForList(psql.getSQL());
	}

	public int deleteBatchServAccount(String taskId)
	{
		logger.debug("deleteBatchServAccount({})", taskId);
		PrepareSQL psql = new PrepareSQL(
				"delete from gw_pic_task_batch where task_id = ? ");
		psql.setString(1, taskId);
		return DBOperation.executeUpdate(psql.getSQL());
	}

	public int updateTaskStatus(String taskId, int loadStatus)
	{
		logger.debug("updateTaskStatus({},{})", taskId, loadStatus);
		PrepareSQL psql = new PrepareSQL(
				"update gw_pic_task set load_status=? where task_id=?");
		psql.setInt(1, loadStatus);
		psql.setString(2, taskId);
		return DBOperation.executeUpdate(psql.getSQL());
	}

	/**
	 * 生成入策略的sql语句
	 *
	 * @author wangsenbo
	 * @date Jun 11, 2010
	 * @param
	 * @return List<String>
	 */
	public List<String> stbStrategySQL(StrategyOBJ obj)
	{
		logger.debug("strategySQL({})", obj);
		if(obj==null){
			return null;
		}
		List<String> sqlList = new ArrayList<String>();
		StringBuilder tempSql = new StringBuilder();
		tempSql.append("delete from stb_gw_serv_strategy where device_id='").append(obj.getDeviceId()).append("' and temp_id=").append(obj.getTempId());
		//生成入策略的sql语句
		StringBuilder sql = new StringBuilder();
		sql.append("insert into stb_gw_serv_strategy (");
		sql.append("id,acc_oid,time,type,gather_id,device_id,oui,device_serialnumber,username"
				+ ",sheet_para,service_id,task_id,order_id,sheet_type,temp_id,is_last_one");
		sql.append(") values (");
		sql.append(obj.getId());
		sql.append("," + obj.getAccOid());
		sql.append("," + obj.getTime());
		sql.append("," + obj.getType());
		sql.append("," + StringUtil.getSQLString(obj.getGatherId()));
		sql.append("," + StringUtil.getSQLString(obj.getDeviceId()));
		sql.append("," + StringUtil.getSQLString(obj.getOui()));
		sql.append("," + StringUtil.getSQLString(obj.getSn()));
		sql.append("," + StringUtil.getSQLString(obj.getUsername()));
		sql.append("," + StringUtil.getSQLString(obj.getSheetPara()));
		sql.append("," + obj.getServiceId());
		sql.append("," + StringUtil.getSQLString(obj.getTaskId()));
		sql.append("," + obj.getOrderId());
		sql.append("," + obj.getSheetType());
		sql.append("," + obj.getTempId());
		sql.append("," + obj.getIsLastOne());
		sql.append(")");
		//生成入策略日志的sql语句
		StringBuilder logsql = new StringBuilder();
		logsql.append("insert into stb_gw_serv_strategy_log (");
		logsql.append("id,acc_oid,time,type,gather_id,device_id,oui,device_serialnumber,username"
				+ ",sheet_para,service_id,task_id,order_id,sheet_type,temp_id,is_last_one");
		logsql.append(") values (");
		logsql.append(obj.getId());
		logsql.append("," + obj.getAccOid());
		logsql.append("," + obj.getTime());
		logsql.append("," + obj.getType());
		logsql.append("," + StringUtil.getSQLString(obj.getGatherId()));
		logsql.append("," + StringUtil.getSQLString(obj.getDeviceId()));
		logsql.append("," + StringUtil.getSQLString(obj.getOui()));
		logsql.append("," + StringUtil.getSQLString(obj.getSn()));
		logsql.append("," + StringUtil.getSQLString(obj.getUsername()));
		logsql.append("," + StringUtil.getSQLString(obj.getSheetPara()));
		logsql.append("," + obj.getServiceId());
		logsql.append("," + StringUtil.getSQLString(obj.getTaskId()));
		logsql.append("," + obj.getOrderId());
		logsql.append("," + obj.getSheetType());
		logsql.append("," + obj.getTempId());
		logsql.append("," + obj.getIsLastOne());
		logsql.append(")");
		sqlList.add(tempSql.toString());
		sqlList.add(sql.toString());
		sqlList.add(logsql.toString());
		logger.debug("入策略的sql语句-->{}",tempSql.toString()+";"+sql.toString()+";"+logsql.toString());
		PrepareSQL psql = new PrepareSQL(tempSql.toString());
        psql.getSQL();
        psql = new PrepareSQL(sql.toString());
        psql.getSQL();
        psql = new PrepareSQL(logsql.toString());
        psql.getSQL();
		return sqlList;
	}

	/**
	 * 查询上传服务器，下载服务器的用户名，密码，以及URL等
	 *
	 * @return
	 */
	public Map<String, String> getServerParameter() {

		logger.debug("getServerParameter({})", new Object[] {});
		PrepareSQL psql = new PrepareSQL();
		psql.append("select server_id, server_name, server_url, access_user, access_passwd, file_type " +
				"from stb_tab_picture_file_server  where 1=1 and file_type = 1 and server_name = '图片服务器' ");

		return queryForMap(psql.getSQL());
	}
}
