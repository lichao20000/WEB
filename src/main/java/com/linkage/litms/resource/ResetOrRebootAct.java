
package com.linkage.litms.resource;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import PreProcess.UserInfo;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterfaceV3;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.PreProcessInterface;

public class ResetOrRebootAct
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ResetOrRebootAct.class);
	private FileSevice fileSevice = null;

	/**
	 * 获取V3所需要的认证信息
	 * 
	 * @param device_id
	 * @return
	 */
	public Performance.SNMPV3PARAM getAuthInfoForV3(String device_id)
	{
		String sql = "select * from sgw_security";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select auth_passwd, auth_protocol, privacy_passwd, privacy_protocol, security_level, security_model, " +
					"security_username, snmp_r_passwd, snmp_w_passwd " +
					"from sgw_security";
		}
		sql += " where device_id='" + device_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		HashMap map = DataSetBean.getRecord(sql);
		Performance.SNMPV3PARAM param = new Performance.SNMPV3PARAM();
		param.authPasswd = (String) map.get("auth_passwd");
		param.authProtocol = (String) map.get("auth_protocol");
		param.privPasswd = (String) map.get("privacy_passwd");
		param.privProtocol = (String) map.get("privacy_protocol");
		param.securityLevel = Integer.parseInt((String) map.get("security_level"));
		param.securityModel = Integer.parseInt((String) map.get("security_model"));
		param.securityName = (String) map.get("security_username");
		param.snmp_r_word = (String) map.get("snmp_r_passwd");
		param.snmp_w_word = (String) map.get("snmp_w_passwd");
		return param;
	}

	/**
	 * SetWayV1
	 * 
	 * @param request
	 * @return
	 */
	public Map SetWayV1(HttpServletRequest request)
	{
		Map resultMap = new HashMap();
		String loopback_ip = "";
		String result = "";
		String device_name = "";
		String device_id = request.getParameter("device_id");
		String oid_type = request.getParameter("oid_type");
		String type = request.getParameter("type");
		if (type == null)
		{// 不含方式选项
			fileSevice = new FileSevice();
			// snmp_ro_community读口令
			// snmp_rw_community写口令
			// String[] arrDevice_id = device_id.split(",");
			// 取得tab_deviceresource表中的值
			// 得到设备类型
			String gw_type = request.getParameter("gw_type");
			String StrSQL_1 = "select a.loopback_ip, a.snmp_udp, a.gw_type, b.snmp_r_passwd, b.snmp_w_passwd from tab_gw_device a, sgw_security b where a.device_id=b.device_id and "
					+ " a.device_id='" + device_id + "'";
			PrepareSQL psql = new PrepareSQL(StrSQL_1);
			psql.getSQL();
			HashMap record_1 = DataSetBean.getRecord(StrSQL_1);
			if (record_1 == null && "1".equals(oid_type))
			{
				// TR069重启
				resultMap = fileSevice.ChongqiList(request, gw_type);
			}
			else if (record_1 == null && "2".equals(oid_type))
			{
				// TR069恢复出厂设置
				resultMap = fileSevice.HuifuList(request, gw_type);
			}
			else if (record_1 != null)
			{
				// SNMP重启和恢复出厂设置
				// 取得tab_gw_model_oper_oid表中的值
				String StrSQL_2 = "select oid, oid_value from tab_gw_model_oper_oid "
						+ " where device_model = (select device_model_id from tab_gw_device where device_id='"
						+ device_id + "') and oid_type =" + oid_type + "";
				psql = new PrepareSQL(StrSQL_2);
				psql.getSQL();
				HashMap record_2 = DataSetBean.getRecord(StrSQL_2);
				Performance.setData setDate_value = new Performance.setData(
						(String) record_1.get("loopback_ip"),
						(String) record_1.get("snmp_udp"), (String) record_2.get("oid"),
						(String) record_2.get("oid_value"),
						(String) record_1.get("snmp_r_passwd"),
						(String) record_1.get("snmp_w_passwd"));
				Performance.setData[] setData_arr = new Performance.setData[1];
				setData_arr[0] = setDate_value;
				logger.debug("setData_arr[0].oid:" + setData_arr[0].oid);
				logger.debug("setData_arr[0].readComm :" + setData_arr[0].readComm);
				// 调用接口中方法
				Performance.setDataReturn[] setDataReturnArr = SnmpGatherInterface
						.GetInstance().SetWayV1(setData_arr, 1, device_id);
				if (null == setDataReturnArr || 0 == setDataReturnArr.length)
				{
					logger.warn("SetWayV1() 调用后台失败!");
				}
				else
				{
					loopback_ip = setDataReturnArr[0].loopback_ip;
					result = setDataReturnArr[0].result;
					// 获取值失败
					if ("".equals(device_name) && "".equals(result))
					{
						resultMap = null;
					}
					if ("true".equalsIgnoreCase(result))
					{
						result = "设置成功";
					}
					else
					{
						result = "设置失败";
					}
					resultMap.put(loopback_ip, result);
				}
			}
		}
		else
		{// 可选择tr069或snmp方式
			int protocol = LipossGlobals.getGwProtocol();
			String device_tab_name = null;
			if (protocol == 2)
			{
				device_tab_name = "tab_deviceresource";
			}
			else
			{
				device_tab_name = "tab_gw_device";
			}
			if ("1".equals(type))
			{// tr069
				logger.debug("get into tr069 reboot");
				// 得到设备类型
				String gw_type = LipossGlobals.getGw_Type(device_id);
				fileSevice = new FileSevice();
				if ("1".equals(oid_type))
				{
					// TR069重启
					resultMap = fileSevice.ChongqiList(request, gw_type);
				}
				else if ("2".equals(oid_type))
				{
					// TR069恢复出厂设置
					resultMap = fileSevice.HuifuList(request, gw_type);
				}
			}
			else
			{// snmp V3
				// 取得设备表中的值
				String sqlDevInfo = "select device_name, gather_id,loopback_ip,device_id from "
						+ device_tab_name + " where device_id='" + device_id + "'";
				PrepareSQL psql = new PrepareSQL(sqlDevInfo);
				psql.getSQL();
				HashMap devInfoMap = DataSetBean.getRecord(sqlDevInfo);
				// SNMP重启和恢复出厂设置
				// 取得tab_gw_model_oper_oid表中的值
				/*
				 * String sqlGetOidInfo = "select sequence, opt_type, oid, oid_value from
				 * tab_gw_model_oper_oid " + " where (device_model = (select device_model
				 * from gw_device_model where device_model_id=(select device_model_id from
				 * " + device_tab_name + " where device_id= '" + device_id + "')) or
				 * device_model='0') and oid_type =" + oid_type + " order by sequence";
				 * 
				 * logger.debug("sqlGetOidInfo:" + sqlGetOidInfo); Cursor cursor =
				 * DataSetBean.getCursor(sqlGetOidInfo); if (cursor != null) { Map
				 * oidInfoMap = cursor.getNext(); Performance.setDataV3 sd3 = new
				 * Performance.setDataV3(); sd3.loopback_ip = (String)
				 * devInfoMap.get("loopback_ip"); sd3.oid = (String)
				 * oidInfoMap.get("oid"); sd3.value = ""; sd3.port = "0"; sd3.readComm =
				 * ""; sd3.writeComm = ""; sd3.v3param = getAuthInfoForV3(device_id); }
				 * HashMap oidInfoMap = DataSetBean.getRecord(sqlGetOidInfo);
				 * 
				 * Performance.setDataV3[] setDataV3_arr = new Performance.setDataV3[1];
				 * //setDataV3_arr[0] = sd3;
				 */
				Performance.SNMPV3PARAM v3param = getAuthInfoForV3(device_id);
				String oid_1 = "1.3.6.1.4.1.25506.2.3.1.3.3.1.2";
				Performance.Data data_1 = SnmpGatherInterfaceV3.getInstance()
						.getNextRouteInfoV3Single(oid_1, v3param, devInfoMap);
				String idx = null;
				if (data_1 != null)
				{
					String index = data_1.index;
					if (index != null)
					{
						String[] arr = index.split("\\.");
						if (arr != null)
						{
							idx = arr.length > 0 ? arr[arr.length - 1] : arr[0];
						}
					}
				}
				else
				{
					logger.warn("return data is null !!!!!!");
				}
				if (idx == null)
					return resultMap;
				String oid_2 = "1.3.6.1.4.1.25506.2.3.1.3.1.0";
				String oid_3 = "1.3.6.1.4.1.25506.2.3.1.3.2.0";
				Performance.setDataV3 d1 = new Performance.setDataV3();
				d1.loopback_ip = (String) devInfoMap.get("loopback_ip");
				d1.port = "161";
				d1.oid = oid_2;
				d1.value = idx;
				d1.readComm = "";
				d1.writeComm = "";
				d1.v3param = v3param;
				Performance.setDataV3 d2 = new Performance.setDataV3();
				d2.loopback_ip = (String) devInfoMap.get("loopback_ip");
				d2.port = "161";
				d2.oid = oid_3;
				d2.value = "3";
				d2.readComm = "";
				d2.writeComm = "";
				d2.v3param = v3param;
				Performance.setDataV3[] setDataV3_arr = new Performance.setDataV3[2];
				setDataV3_arr[0] = d1;
				setDataV3_arr[1] = d2;
				// 调用接口中方法
				Performance.setDataReturn[] setDataReturnArr = SnmpGatherInterfaceV3
						.getInstance().setWayV3(setDataV3_arr, setDataV3_arr.length,
								devInfoMap);
				boolean ret = true;
				if (null == setDataReturnArr || 0 == setDataReturnArr.length)
				{
					logger.warn("SetWayV1() 调用后台失败!");
				}
				else
				{
					for (int i = 0; i < setDataReturnArr.length; i++)
					{
						String ip = setDataReturnArr[0].loopback_ip;
						String rst = setDataReturnArr[0].result;
						ret = ret && Boolean.parseBoolean(rst.toLowerCase());
					}
				}
				if (ret)
				{
					resultMap.put((String) devInfoMap.get("device_name"), "重启成功");
				}
				else
				{
					resultMap.put((String) devInfoMap.get("device_name"), "重启失败");
				}
			}
		}
		return resultMap;
	}

	/**
	 * reset
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, String> reset(HttpServletRequest request)
	{
		Map<String, String> resultMap = new HashMap<String, String>();
		String device_id = request.getParameter("device_id");
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select gw_type,customer_id,oui,device_serialnumber,device_name from tab_gw_device where device_id='");
		pSql.append(device_id);
		pSql.append("'");
		HashMap devInfoMap = DataSetBean.getRecord(pSql.getSQL());
		// 得到设备类型
		String gw_type = StringUtil.getStringValue(devInfoMap, "gw_type");
		// 恢复出厂设置成功，更新用户标识
		//湖北恢复出厂设置，先更新业务用户表的状态
		updateCustStatus(gw_type,StringUtil.getStringValue(devInfoMap, "customer_id"));
		// TR069恢复出厂设置,调用配置模块业务接口
		PreProcessInterface ppc = CreateObjectFactory.createPreProcess(gw_type);
		UserInfo[] userInfo = new UserInfo[1];
		userInfo[0] = new UserInfo();
		userInfo[0].deviceId = device_id;
		userInfo[0].oui = StringUtil.getStringValue(devInfoMap, "oui");
		userInfo[0].deviceSn = StringUtil.getStringValue(devInfoMap,
				"device_serialnumber");
		userInfo[0].gatherId = "factory_reset";
		userInfo[0].userId = StringUtil.getStringValue(devInfoMap, "customer_id");
		userInfo[0].servTypeId = "0";
		userInfo[0].operTypeId = "1";
		int ret = ppc.processServiceInterface(userInfo);
		logger.warn("ResetOrRebootAct.reset PreProcess({})", ret);
		if (1 == ret)
		{
			logger.info(
					"ResetOrRebootAct.factory_reset device_id[{}] success. update customer open_status",
					device_id);
			resultMap.put((String) devInfoMap.get("device_name"), "恢复出厂设置成功");
		}
		else
		{
			resultMap.put((String) devInfoMap.get("device_name"), "恢复出厂设置失败");
		}
		return resultMap;
	}

	/**
	 * 设备恢复出厂设置后更新用户状态
	 * 
	 * @param gwType
	 * @param userId
	 */
	private void updateCustStatus(String gwType, String userId)
	{
		String tableName = null;
		if (Global.GW_TYPE_BBMS.endsWith(gwType))
		{
			tableName = "egwcust_serv_info";
		}
		else
		{
			// 用户类型为家庭或者家庭政企融合时
			tableName = "hgwcust_serv_info";
		}
		if (StringUtil.IsEmpty(userId))
		{
			logger.info(
					"user_id is empty, no need to update table[{}] set open_status = 0",
					tableName);
			return;
		}
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("update ");
		pSql.append(tableName);
		pSql.append(" set open_status=0,updatetime=? where user_id=? and serv_status in (1,2) and open_status!=0");
		//宁夏联通iptv业务步下发
		if(LipossGlobals.inArea(Global.NXLT)){
			pSql.append(" and serv_type_id <> 11");
		}
		int index = 0;
		pSql.setLong(++index, new DateTimeUtil().getLongTime());
		pSql.setInt(++index, StringUtil.getIntegerValue(userId));
		int updateRows = DataSetBean.executeUpdate(pSql.getSQL());
		logger.info("update table[{}] rows[{}].", tableName, updateRows);
	}

	/**
	 * SetWayV2
	 * 
	 * @param request
	 * @return
	 */
	public Map SetWayV2(HttpServletRequest request)
	{
		Map resultMap = new HashMap();
		String device_id = request.getParameter("device_id");
		String oid_type = request.getParameter("oid_type");
		fileSevice = new FileSevice();
		// snmp_ro_community读口令
		// snmp_rw_community写口令
		// String[] arrDevice_id = device_id.split(",");
		// 获取设备类型
		String StrSQL_0 = "select gw_type from tab_gw_device where device_id='"
				+ device_id + "'";
		PrepareSQL psql0 = new PrepareSQL(StrSQL_0);
		psql0.getSQL();
		// 查询结果肯定不为null
		HashMap record_0 = DataSetBean.getRecord(StrSQL_0);
		// 得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		// 取得tab_deviceresource表中的值
		String StrSQL_1 = "select a.loopback_ip, a.snmp_udp, b.snmp_r_passwd, b.snmp_w_passwd from tab_gw_device a, sgw_security b where a.device_id=b.device_id and "
				+ " a.device_id='" + device_id + "'";
		PrepareSQL psql = new PrepareSQL(StrSQL_1);
		psql.getSQL();
		HashMap record_1 = DataSetBean.getRecord(StrSQL_1);
		if (record_1 == null && "1".equals(oid_type))
		{
			// TR069重启
			resultMap = fileSevice.ChongqiList(request, gw_type);
		}
		else if (record_1 == null && "2".equals(oid_type))
		{
			// TR069恢复出厂设置
			resultMap = fileSevice.HuifuList(request, gw_type);
		}
		else if (record_1 != null)
		{
			// SNMP重启和恢复出厂设置
			// 取得tab_gw_model_oper_oid表中的值
			String StrSQL_2 = "select oid, oid_value from tab_gw_model_oper_oid "
					+ " where device_model = (select device_model_id from tab_gw_device where device_id='"
					+ device_id + "') and oid_type =" + oid_type + "";
			psql = new PrepareSQL(StrSQL_2);
			psql.getSQL();
			HashMap record_2 = DataSetBean.getRecord(StrSQL_2);
			Performance.setData setDate_value = new Performance.setData(
					(String) record_1.get("loopback_ip"),
					(String) record_1.get("snmp_udp"), (String) record_2.get("oid"),
					(String) record_2.get("oid_value"),
					(String) record_1.get("snmp_r_passwd"),
					(String) record_1.get("snmp_w_passwd"));
			Performance.setData[] setData_arr = new Performance.setData[1];
			setData_arr[0] = setDate_value;
			// 调用接口中方法
			Performance.setDataReturn[] setDataReturnArr = SnmpGatherInterface
					.GetInstance().SetWayV2(setData_arr, 1, device_id);
			String loopback_ip = setDataReturnArr[0].loopback_ip;
			String result = setDataReturnArr[0].result;
			String StrSQL_3 = "select device_name from tab_gw_device  where loopback_ip="
					+ loopback_ip;
			psql = new PrepareSQL(StrSQL_3);
			psql.getSQL();
			HashMap record_3 = DataSetBean.getRecord(StrSQL_1);
			String device_name = (String) record_3.get("device_name");
			resultMap.put(device_name, result);
		}
		return resultMap;
	}
	// public Map SetWayV1(HttpServletRequest request) {
	// Map resultMap = new HashMap();
	// String loopback_ip = "";
	// String result = "";
	// String device_name = "";
	// //返回结构描述 －－add by lizhaojun 2007-12-20
	// String resultDesc = "";
	//
	// String device_id = request.getParameter("device_id");
	// String oid_type = request.getParameter("oid_type");
	// fileSevice = new FileSevice();
	// //snmp_ro_community读口令
	// //snmp_rw_community写口令
	// //String[] arrDevice_id = device_id.split(",");
	// //取得tab_deviceresource表中的值
	// String StrSQL_1 = "select loopback_ip, snmp_udp, snmp_ro_community,
	// snmp_rw_community from tab_deviceresource "
	// + " where device_id='" + device_id + "'";
	// logger.debug("StrSQL_1:" + StrSQL_1);
	// HashMap record_1 = DataSetBean.getRecord(StrSQL_1);
	//
	// if (record_1 == null && "1".equals(oid_type)) {
	// //TR069重启
	// resultMap = fileSevice.ChongqiList(request);
	// } else if (record_1 == null && "2".equals(oid_type)) {
	// //TR069恢复出厂设置
	// resultMap = fileSevice.HuifuList(request);
	// } else if (record_1 != null) {
	//
	//
	//
	// //SNMP重启和恢复出厂设置
	// //取得tab_gw_model_oper_oid表中的值
	// String StrSQL_2 = "select oid, oid_value from tab_gw_model_oper_oid "
	// + " where device_model = (select device_model from tab_deviceresource
	// where device_id= '"+ device_id +"') and oid_type =" + oid_type;
	// logger.debug("StrSQL_2:" + StrSQL_2);
	// HashMap record_2 = DataSetBean.getRecord(StrSQL_2);
	//
	// Performance.setData setDate_value = new
	// Performance.setData((String)record_1.get("loopback_ip"),(String)record_1.get("snmp_udp"),(String)record_2.get("oid"),(String)record_2.get("oid_value"),(String)record_1.get("snmp_ro_community"),(String)record_1.get("snmp_rw_community"));
	//
	// Performance.setData[] setData_arr = new Performance.setData[1];
	// setData_arr[0] = setDate_value;
	// //调用接口中方法
	// Performance.setDataReturn[] setDataReturnArr =
	// SnmpGatherInterface.GetInstance().SetWayV1(setData_arr, 1, device_id);
	//
	// if (null == setDataReturnArr || 0 == setDataReturnArr.length) {
	//
	// if(oid_type.equals("1")){
	// resultDesc = "设备重启失败，调用corba接口失败！";
	// } else {
	// resultDesc = "设备恢复出厂设置失败，调用corba接口失败！";
	// }
	//
	// } else {
	// loopback_ip = setDataReturnArr[0].loopback_ip;
	// result = setDataReturnArr[0].result;
	//
	// /**********************************************************************************************************************/
	// /**
	// /**
	// /** add by lizhaojun 设备重启或者恢复出厂设置后，如果返回的是true 再去get 看是否和原来的值相同，如果相同
	// /**
	// /** 设备重启或者恢复出厂设置成功，否则都是失败
	// /**
	// /**
	// /**********************************************************************************************************************/
	//
	// //set完后再去get
	// if(result.equals("true")){
	//
	// Performance.Data[] getDataResult =
	// SnmpGatherInterface.GetInstance().GetDataIMDPort(setData_arr, 1,
	// device_id);
	//
	// if(getDataResult == null || getDataResult.length == 0){
	// if(oid_type.equals("1")){
	// resultDesc = "设备重启失败，调用corba接口失败！";
	// } else {
	// resultDesc = "设备恢复出厂设置失败，调用corba接口失败！";
	// }
	// } else {
	//
	// String value = getDataResult[0].dataStr;
	// String oid_value = (String)record_2.get("oid_value");
	//
	// logger.warn("setValue :" + oid_value + " ----getValue :" + value);
	// if(oid_value.equals(value)){
	//
	// if(oid_type.equals("1")){
	// resultDesc = "设备重启成功！";
	// } else {
	// resultDesc = "设备恢复出厂设置成功！";
	// }
	//
	// } else {
	//
	// if(oid_type.equals("1")){
	// resultDesc = "设备重启失败，调用corba接口失败！";
	// } else {
	// resultDesc = "设备恢复出厂设置失败，调用corba接口失败！";
	// }
	// }
	//
	// }
	// } else {
	// if(oid_type.equals("1")){
	// resultDesc = "设备重启失败，调用corba接口失败！";
	// } else {
	// resultDesc = "设备恢复出厂设置失败，调用corba接口失败！";
	// }
	// }
	//
	//
	// }
	// }
	// resultMap.put(loopback_ip, resultDesc);
	// return resultMap;
	// }
}
