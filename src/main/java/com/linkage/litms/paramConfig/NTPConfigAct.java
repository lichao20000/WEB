/**
 * 
 */

package com.linkage.litms.paramConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;
import Performance.Data;
import Performance.SNMPV3PARAM;
import Performance.createDataV3;
import Performance.setDataReturn;
import Performance.setDataV3;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.service.RPCObject;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterfaceV3;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.common.util.VendorUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author zhaixf
 * @date 2008-8-5
 */
public class NTPConfigAct
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(NTPConfigAct.class);
	private String device_id;
	public static String device_name;
//	private String device_serialnumber;
	private String loopback_ip;
//	private String port;
//	private String path;
//	private String gather_id;
//	private String acs_username;
//	private String acs_passwd;
//	private String oui;
//	private String vendor_name;
//	private String ior;
	private SNMPV3PARAM v3param = null;
	private HashMap devInfoMap = null;
	public static String main_ntp_server = ""; // 主NTP服务器地址或域名
	public static String second_ntp_server = ""; // 备NTP服务器地址或域名
	public static String configType = ""; // 配置方式: tr069/snmp
	public static String status = ""; // 服务状态: 开始/关闭
	public static final String result = "result";
	private static final String oid_1 = "1.3.6.1.4.1.25506.8.22.2.1.1.1.36";

	private String gw_type;

	public NTPConfigAct(String pram_device_id)
	{
		device_id = pram_device_id;
	}

	/**
	 * NTP Server地址配置 设置NTP服务状态、NTP服务器地址(为批量配置准备的方法)
	 * 
	 * @param theDevice_id
	 *            设备ID
	 * @return
	 */
	public Map<String, String> NTPConfigure()
	{
		Map<String, String> resultMap = new HashMap<String, String>();
		// 取得设备表中的值
		devInfoMap = getDeviceInfo(device_id);
		// 如果找不到设备信息，就返回
		if (devInfoMap != null && !devInfoMap.isEmpty())
		{
			if ("1".equals(configType))
			{
				return tr069NTPConfig();
			}
			else if ("2".equals(configType))
			{
				return snmpNTPConfig(status);
			}
		}
		resultMap.put(result, "nok");
		resultMap.put(device_name, "没有找到该设备的相关信息");
		return resultMap;
	}

	/**
	 * 通过tr069协议，配置NTP
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-5
	 * @return Map
	 */
	private Map<String, String> tr069NTPConfig()
	{
		Map<String, String> resultMap = new HashMap<String, String>();
		// 最外层结构:SetParameterValues
		SetParameterValues setParameterValues = new SetParameterValues();
		String[] p1 = new String[] { "InternetGatewayDevice.Time.NTPServer1", "1",
				main_ntp_server };
		String[] p2 = new String[] { "InternetGatewayDevice.Time.NTPServer2", "1",
				second_ntp_server };
		String tmp = "InternetGatewayDevice.Time.X_CT-COM_Apply";
		boolean ciscoFlag = VendorUtil.IsCiscoByDeviceId(device_id);
		String tempStr = "";
		String[][] params = null;
		ParameterValueStruct[] parameterValueStructArr = null;
		DevRpc[] devRPCArr = null;
		if (main_ntp_server != null && !"".equals(main_ntp_server.trim()))
		{
			params = new String[][] { p1 };
			if (ciscoFlag)
			{
				parameterValueStructArr = new ParameterValueStruct[params.length + 1];
			}
			else
			{
				parameterValueStructArr = new ParameterValueStruct[params.length];
			}
			for (int i = 0; i < params.length; i++)
			{
				String[] p = params[i];
				AnyObject anyObject = new AnyObject();
				anyObject.para_value = p[2];
				anyObject.para_type_id = p[1];
				parameterValueStructArr[i] = new ParameterValueStruct();
				parameterValueStructArr[i].setName(p[0]);
				parameterValueStructArr[i].setValue(anyObject);
			}
			if (ciscoFlag)
			{
				AnyObject anyObject = new AnyObject();
				anyObject.para_value = "1";
				anyObject.para_type_id = "1";
				parameterValueStructArr[params.length] = new ParameterValueStruct();
				parameterValueStructArr[params.length].setName(tmp);
				parameterValueStructArr[params.length].setValue(anyObject);
			}
			setParameterValues.setParameterList(parameterValueStructArr);
			devRPCArr = getDevRPCArr(setParameterValues);
			List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type);
			// 一个设备返回的命令
			if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
			{
				return resultMap;
			}
			// 一个设备返回的命令
			if (devRPCRep.get(0).getStat() == 1)
			{
				tempStr += "主NTP服务器配置成功";
				resultMap.put(device_name, tempStr);
				resultMap.put(result, "ok");
			}
			else
			{
				tempStr += "主NTP服务器配置失败";
				resultMap.put(device_name, tempStr);
				resultMap.put(result, "nok");
			}
			// 释放资源
			params = null;
			parameterValueStructArr = null;
			setParameterValues = null;
		}
		// ------------------------
		if (second_ntp_server != null && !"".equals(second_ntp_server.trim()))
		{
			params = new String[][] { p2 };
			if (ciscoFlag)
			{
				parameterValueStructArr = new ParameterValueStruct[params.length + 1];
			}
			else
			{
				parameterValueStructArr = new ParameterValueStruct[params.length];
			}
			for (int i = 0; i < params.length; i++)
			{
				String[] p = params[i];
				AnyObject anyObject = new AnyObject();
				anyObject.para_value = p[2];
				anyObject.para_type_id = p[1];
				parameterValueStructArr[i] = new ParameterValueStruct();
				parameterValueStructArr[i].setName(p[0]);
				parameterValueStructArr[i].setValue(anyObject);
			}
			if (ciscoFlag)
			{
				AnyObject anyObject = new AnyObject();
				anyObject.para_value = "1";
				anyObject.para_type_id = "1";
				parameterValueStructArr[params.length] = new ParameterValueStruct();
				parameterValueStructArr[params.length].setName(tmp);
				parameterValueStructArr[params.length].setValue(anyObject);
			}
			setParameterValues = new SetParameterValues();
			setParameterValues.setParameterList(parameterValueStructArr);
			devRPCArr = getDevRPCArr(setParameterValues);
			List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type);
			// 一个设备返回的命令
			if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
			{
				return resultMap;
			}
			if (devRPCRep.get(0).getStat() == 1)
			{
				tempStr += " 备NTP服务器配置成功";
				resultMap.put(device_name, tempStr);
				resultMap.put(result, "ok");
			}
			else
			{
				tempStr += " 备NTP服务器配置失败";
				resultMap.put(device_name, tempStr);
				resultMap.put(result, "nok");
			}
			// 释放资源
			params = null;
			parameterValueStructArr = null;
			setParameterValues = null;
		}
		return resultMap;
	}

	/**
	 * snmp协议配置NTP
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-5
	 * @return Map
	 */
	private Map<String, String> snmpNTPConfig(String status)
	{
		Map<String, String> resultMap = new HashMap<String, String>();
		v3param = getAuthInfoForV3(device_id);
		Data[] dataList = SnmpGatherInterfaceV3.getInstance().getDataListIMDV3(oid_1,
				devInfoMap);
		if (dataList != null)
		{
			// 原来已有实例，删除先
			for (int i = 0; i < dataList.length; i++)
			{
				Data data = dataList[i];
				String ip = data.index;
				ip = oid_1 + "." + ip;
				logger.debug("NTP sub oid = " + ip);
				setDataV3 d1 = new setDataV3();
				d1.loopback_ip = loopback_ip;
				d1.port = "161";
				d1.oid = ip;
				d1.value = "6";
				d1.readComm = "";
				d1.writeComm = "";
				d1.v3param = v3param;
				setDataV3[] setDataV3_arr = new setDataV3[1];
				setDataV3_arr[0] = d1;
				// 调用接口中方法
				setDataReturn[] setDataReturnArr = SnmpGatherInterfaceV3.getInstance()
						.setWayV3(setDataV3_arr, setDataV3_arr.length, devInfoMap);
				boolean ret = true;
				if (null == setDataReturnArr || 0 == setDataReturnArr.length)
				{
					logger.warn("删除原有NTPServer实例失败!");
					resultMap.put(device_name, "删除原有NTPServer实例失败!");
					return resultMap;
				}
				else
				{
					for (int j = 0; j < setDataReturnArr.length; j++)
					{
						String rst = setDataReturnArr[0].result;
						ret = ret && Boolean.parseBoolean(rst.toLowerCase());
					}
				}
				if (!ret)
				{
					logger.warn("删除原有NTPServer实例失败!");
					resultMap.put(device_name, "删除原有NTPServer实例失败!");
					return resultMap;
				}
				else
				{
					logger.debug("删除NTPServer实例" + ip + "成功!");
				}
			}
		}
		if ("1".equals(status))
		{// 状态是启用，则创建新server
			createDataV3 cd = null;
			if (null != main_ntp_server && !"".equals(main_ntp_server))
			{
				cd = new createDataV3();
				cd.loopback_ip = loopback_ip;
				cd.oid = oid_1 + "." + main_ntp_server.trim() + ".3";
				cd.port = "161";
				cd.readComm = "";
				cd.type = 2;
				cd.v3param = v3param;
				cd.value = "4";
				cd.writeComm = "";
				logger.debug("cd.oid=" + cd.oid);
			}
			createDataV3 cd2 = null;
			if (null != second_ntp_server && !"".equals(second_ntp_server))
			{
				cd2 = new createDataV3();
				cd2.loopback_ip = loopback_ip;
				cd2.oid = oid_1 + "." + second_ntp_server.trim() + ".3";
				cd2.port = "161";
				cd2.readComm = "";
				cd2.type = 2;
				cd2.v3param = v3param;
				cd2.value = "4";
				cd2.writeComm = "";
				logger.debug("cd2.oid=" + cd2.oid);
			}
			createDataV3[] setDataV3_arr = null;
			if (cd != null && cd2 != null)
			{
				setDataV3_arr = new createDataV3[2];
				setDataV3_arr[0] = cd;
				setDataV3_arr[1] = cd2;
			}
			else if (cd != null)
			{
				setDataV3_arr = new createDataV3[1];
				setDataV3_arr[0] = cd;
			}
			else if (cd2 != null)
			{
				setDataV3_arr = new createDataV3[1];
				setDataV3_arr[0] = cd2;
			}
			else
			{
				return resultMap;
			}
			logger.debug("setDataV3_arr.length=" + setDataV3_arr.length);
			setDataReturn[] dataList2 = SnmpGatherInterfaceV3.getInstance()
					.createOidNodeV3(setDataV3_arr, setDataV3_arr.length, devInfoMap);
			if (null == dataList2 || 0 == dataList2.length)
			{
				resultMap.put(device_name, "创建新NTPServer实例失败!");
				logger.warn("创建新NTPServer实例失败!");
				return resultMap;
			}
			boolean ret2 = true;
			for (int i = 0; i < dataList2.length; i++)
			{
				setDataReturn data = dataList2[i];
				String rst = dataList2[0].result;
				ret2 = ret2 && Boolean.parseBoolean(rst.toLowerCase());
				System.out.println("[" + i + "]loopback_ip=" + data.loopback_ip);
				logger.debug("[" + i + "]result=" + data.result);
			}
			if (ret2)
			{
				resultMap.put(device_name, "启用成功");
				resultMap.put(result, "ok");
			}
			else
			{
				resultMap.put(device_name, "启用失败");
				resultMap.put(result, "nok");
			}
		}
		else
		{// 禁用
			resultMap.put(device_name, "禁用成功");
			resultMap.put(result, "ok");
		}
		return resultMap;
	}

	/**
	 * 获取设备信息
	 * 
	 * @param device_id
	 * @return
	 */
	private HashMap getDeviceInfo(String device_id)
	{
		String sqlDevInfo = "select device_name, gather_id, loopback_ip, cr_port"
				+ ",cr_path, acs_username, acs_passwd, device_id, device_serialnumber"
				+ ",oui,vendor_name from tab_gw_device"
				+ " a, tab_vendor b where device_id='" + device_id
				// + "' and a.oui=b.vendor_id";
				+ "' and a.vendor_id=b.vendor_id";
		PrepareSQL psql = new PrepareSQL(sqlDevInfo);
		psql.getSQL();
		Map devInfoMap = DataSetBean.getRecord(sqlDevInfo);
		if (devInfoMap != null)
		{
//			gather_id = (String) devInfoMap.get("gather_id");
//			oui = (String) devInfoMap.get("oui");
//			vendor_name = (String) devInfoMap.get("vendor_name");
//			device_serialnumber = (String) devInfoMap.get("device_serialnumber");
			loopback_ip = (String) devInfoMap.get("loopback_ip");
//			port = (String) devInfoMap.get("cr_port");
//			path = (String) devInfoMap.get("cr_path");
//			acs_username = (String) devInfoMap.get("acs_username");
//			acs_passwd = (String) devInfoMap.get("acs_passwd");
			device_name = (String) devInfoMap.get("device_name");
		}
//		if (gather_id != null)
//			ior = getIOR(gather_id);
		return (HashMap) devInfoMap;
	}

	/**
	 * 取得IOR
	 * 
	 * @param gather_id
	 * @return ior
	 */
	private String getIOR(String gather_id)
	{
		String ior = "";
		String getIorSQL = "select ior from tab_ior where object_name='ACS_" + gather_id
				+ "' and object_poa='ACS_Poa_" + gather_id + "'";
		PrepareSQL psql = new PrepareSQL(getIorSQL);
		psql.getSQL();
		Map map = DataSetBean.getRecord(getIorSQL);
		if (null != map)
		{
			ior = (String) map.get("ior");
		}
		return ior;
	}

	/**
	 * 根据device_id得到长度为1的DevRPC对象数组
	 * 
	 * @author wangsenbo
	 * @date Apr 19, 2011
	 * @param
	 * @return DevRpc[]
	 */
	public DevRpc[] getDevRPCArr(RPCObject rpcObject)
	{
		DevRpc[] devRPCArr = new DevRpc[1];
		String stringRpcValue = "";
		String stringRpcName = "";
		if (rpcObject == null)
		{
			stringRpcValue = "";
			stringRpcName = "";
		}
		else
		{
			stringRpcValue = rpcObject.toRPC();
			stringRpcName = rpcObject.getClass().getSimpleName();
		}
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		devRPCArr[0].rpcArr = new Rpc[1];
		devRPCArr[0].rpcArr[0] = new Rpc();
		devRPCArr[0].rpcArr[0].rpcId = "1";
		devRPCArr[0].rpcArr[0].rpcName = stringRpcName;
		devRPCArr[0].rpcArr[0].rpcValue = stringRpcValue;
		return devRPCArr;
	}

	/**
	 * 根据得到DevRPC[]对象调用corba接口通知后台
	 * 
	 * @author wangsenbo
	 * @date Apr 19, 2011
	 * @param
	 * @return List<DevRpcCmdOBJ>
	 */
	public List<DevRpcCmdOBJ> getDevRPCResponse(DevRpc[] devRPCArr,int rpcType)
	{
		logger.debug("getDevRPCResponse(devRPCArr)");
		if (devRPCArr == null)
		{
			logger.error("devRPCArr == null");
			return null;
		}
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(getGw_type());
		devRPCRep = devRPCManager.execRPC(devRPCArr,rpcType);
		return devRPCRep;
	}

	/**
	 * 获取V3所需要的认证信息
	 * 
	 * @param device_id
	 * @return
	 */
	public SNMPV3PARAM getAuthInfoForV3(String device_id)
	{
		String sql = "select * from sgw_security";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select auth_passwd, auth_protocol, privacy_passwd, privacy_protocol, security_level," +
					" security_model, security_username, snmp_r_passwd, snmp_w_passwd from sgw_security";
		}
		sql += " where device_id='" + device_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		HashMap map = DataSetBean.getRecord(sql);
		SNMPV3PARAM param = new SNMPV3PARAM();
		if (map != null && !map.isEmpty())
		{
			param.authPasswd = (String) map.get("auth_passwd");
			param.authProtocol = (String) map.get("auth_protocol");
			param.privPasswd = (String) map.get("privacy_passwd");
			param.privProtocol = (String) map.get("privacy_protocol");
			param.securityLevel = Integer.parseInt((String) map.get("security_level"));
			param.securityModel = Integer.parseInt((String) map.get("security_model"));
			param.securityName = (String) map.get("security_username");
			param.snmp_r_word = (String) map.get("snmp_r_passwd");
			param.snmp_w_word = (String) map.get("snmp_w_passwd");
		}
		return param;
	}

	/**
	 * @author lizhaojun zhaixf
	 * @param request
	 * @return String html
	 */
	public static String getDeviceHtml(HttpServletRequest request, boolean needFilter)
	{
		String gather_id = request.getParameter("gather_id");
//		String device_id = request.getParameter("device_id");
		String softwareversion = request.getParameter("softwareversion");
		String hguser = request.getParameter("hguser");
		String telephone = request.getParameter("telephone");
		String serialnumber = request.getParameter("serialnumber");
		String gw_type = request.getParameter("gw_type");
		String vendor_id = request.getParameter("vendor_id");
		String device_model_id = request.getParameter("device_model_id");
		String city_id = request.getParameter("city_id");
		String selDispStyle = "checkbox";
		if (needFilter == true)
		{
			selDispStyle = "radio";
		}
		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();
		String tmpSql = "select * from tab_gw_device a inner join gw_device_model c on a.device_model_id=c.device_model_id";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select a.device_serialnumber, a.device_id, a.devicetype_id, a.oui " +
					"from tab_gw_device a inner join gw_device_model c on a.device_model_id=c.device_model_id";
		}
		if (!user.isAdmin())
		{
			tmpSql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
		}
		// 按用户查询方式
		if ((null != hguser && !"".equals(hguser))
				|| (null != telephone && !"".equals(telephone)))
		{
			tmpSql += " inner join tab_egwcustomer d on a.device_id = d.device_id ";
		}
		tmpSql += " where a.device_status=1 ";
		if (null != gw_type && !"".equals(gw_type))
		{
			tmpSql += " and a.gw_type = " + gw_type;
		}
		if (!user.isAdmin())
		{
			tmpSql += " and b.res_type=1 and b.area_id=" + area_id + " ";
		}
		// 按用户查询方式
		if (null != hguser && !"".equals(hguser))
		{
			tmpSql += " and d.username = '" + hguser + "'";
		}
		if (null != telephone && !"".equals(telephone))
		{
			tmpSql += " and d.phonenumber like '%" + telephone + "%'";
		}
		if (gather_id != null && !gather_id.equals(""))
		{
			tmpSql += " and a.gather_id ='" + gather_id + "'";
		}
		if (device_model_id != null && !device_model_id.equals("-1"))
		{
			tmpSql += " and a.device_model_id ='" + device_model_id + "' ";
		}
		if (null != vendor_id && !"-1".equals(vendor_id))
		{
			tmpSql += " and c.vendor_id='" + vendor_id + "'";
		}
		if (null != softwareversion && !"".equals(softwareversion))
		{
			tmpSql += " and c.softwareversion='" + softwareversion + "'";
		}
		if (serialnumber != null && !"".equals(serialnumber))
		{
			if (serialnumber.length() > 5)
			{
				tmpSql += " and a.dev_sub_sn ='"
						+ serialnumber.substring(serialnumber.length() - 6, serialnumber
								.length()) + "'";
			}
			tmpSql += " and a.device_serialnumber like '%" + serialnumber + "'";
		}
		if (city_id != null && !"".equals(city_id) && !"-1".equals(city_id))
		{
			Map<String, String> cityMap = CityDAO.getCityIdPidMap();
			if (!"-1".equals(cityMap.get(city_id)))
			{
				List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
				String citys = StringUtils.weave(list);
				list = null;
				tmpSql += " and a.city_id in (" + citys + ")";
			}
			cityMap = null;
		}
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "";
		if (fields == null)
		{
			serviceHtml += "<span>无符合条件的设备</span>";
		}
		else
		{
			while (fields != null)
			{
				serviceHtml += "<input type=\"" + selDispStyle + "\" dev_serial=\""
						+ fields.get("device_serialnumber")
						+ "\" id=\"device_id\" name=\"device_id\" value=\""
						+ (String) fields.get("device_id") + "\"";
				if (needFilter)
				{
					serviceHtml += " onclick=\"filterByDevIDAndDevTypeID("
							+ (String) fields.get("device_id") + ","
							+ (String) fields.get("devicetype_id") + ")\">";
				}
				else
				{
					serviceHtml += "\">";
				}
				serviceHtml += "&nbsp;" + (String) fields.get("oui") + "-"
						+ (String) fields.get("device_serialnumber") + "<br>";
				fields = cursor.getNext();
			}
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}

	public String getGw_type() {
		return gw_type;
	}
	
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

}
