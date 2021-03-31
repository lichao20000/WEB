package com.linkage.litms.paramConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.IntHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Performance.SNMPV3PARAM;
import Performance.setDataReturn;
import Performance.setDataV3;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterfaceV3;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * DHCP信息配置（snmp方式）
 * 
 * @author 陈仲民(5243)
 * @since 2008-06-26
 * @version 1.0
 */
public class DHCPConfig
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(DHCPConfig.class);
	String loopback_ip;
	String port;
	/**
	 * 获取DHCP信息
	 * 
	 * @param device_id
	 * @return 1：成功 0：失败
	 */
	public String[] getDHCPInfo(String device_id)
	{
		String[] dhcpInfo = new String[10];
		// DHCP服务状态
		dhcpInfo[0] = getSnmpInfoBIO(device_id, "313");
		// DHCP地址池预留地址的起始地址\结束地址
		dhcpInfo[1] = walkSnmpInfoBIO(device_id, "344");
		// DHCP地址池列表信息
		dhcpInfo[3] = walkSnmpInfoBIO(device_id, "339");
		// DHCP地址池网段信息
		dhcpInfo[4] = walkSnmpInfoBIO(device_id, "340");
		// DHCP地址池掩码信息
		dhcpInfo[5] = walkSnmpInfoBIO(device_id, "341");
		// DHCP地址池起始地址
		dhcpInfo[6] = walkSnmpInfoBIO(device_id, "342");
		// DHCP地址池结束地址
		dhcpInfo[7] = walkSnmpInfoBIO(device_id, "343");
		// 地址池是否启用
		dhcpInfo[8] = walkSnmpInfoBIO(device_id, "345");
		// 返回信息
		Map<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("-1", "暂不支持该属性采集");
		errorMap.put("-2", "没有配置采集oid");
		errorMap.put("-3", "无法采集到数据");
		String tmpHtml = "";
		if ("-1".equals(dhcpInfo[0]) || "-2".equals(dhcpInfo[0])
				|| "-3".equals(dhcpInfo[0]))
			{
				tmpHtml += "DHCP服务状态：" + errorMap.get(dhcpInfo[0]) + "<br>";
				dhcpInfo[0] = "";
			}
		else
			{
				tmpHtml += "DHCP服务状态：获取成功<br>";
			}
		if ("-1".equals(dhcpInfo[1]) || "-2".equals(dhcpInfo[1])
				|| "-3".equals(dhcpInfo[1]))
			{
				tmpHtml += "DHCP地址池预留地址信息：" + errorMap.get(dhcpInfo[1]) + "<br>";
				dhcpInfo[1] = "";
				dhcpInfo[2] = "";
			}
		else
			{
				tmpHtml += "DHCP地址池预留地址信息：获取成功<br>";
			}
		if ("-1".equals(dhcpInfo[8]) || "-2".equals(dhcpInfo[8])
				|| "-3".equals(dhcpInfo[8]))
			{
				tmpHtml += "地址池是否启用：" + errorMap.get(dhcpInfo[8]) + "<br>";
				dhcpInfo[8] = "";
			}
		else
			{
				tmpHtml += "地址池是否启用：获取成功<br>";
			}
		if ("-1".equals(dhcpInfo[3]) || "-2".equals(dhcpInfo[3])
				|| "-3".equals(dhcpInfo[3]))
			{
				tmpHtml += "DHCP地址池列表信息：" + errorMap.get(dhcpInfo[3]) + "<br>";
				dhcpInfo[3] = "";
			}
		else
			{
				tmpHtml += "DHCP地址池列表信息：获取成功<br>";
			}
		if ("-1".equals(dhcpInfo[4]) || "-2".equals(dhcpInfo[4])
				|| "-3".equals(dhcpInfo[4]))
			{
				tmpHtml += "DHCP地址池网段信息：" + errorMap.get(dhcpInfo[4]) + "<br>";
				dhcpInfo[4] = "";
			}
		else
			{
				tmpHtml += "DHCP地址池网段信息：获取成功<br>";
			}
		if ("-1".equals(dhcpInfo[5]) || "-2".equals(dhcpInfo[5])
				|| "-3".equals(dhcpInfo[5]))
			{
				tmpHtml += "DHCP地址池掩码信息：" + errorMap.get(dhcpInfo[5]) + "<br>";
				dhcpInfo[5] = "";
			}
		else
			{
				tmpHtml += "DHCP地址池掩码信息：获取成功<br>";
			}
		if ("-1".equals(dhcpInfo[6]) || "-2".equals(dhcpInfo[6])
				|| "-3".equals(dhcpInfo[6]))
			{
				tmpHtml += "DHCP地址池起始地址：" + errorMap.get(dhcpInfo[6]) + "<br>";
				dhcpInfo[6] = "";
			}
		else
			{
				tmpHtml += "DHCP地址池起始地址：获取成功<br>";
			}
		if ("-1".equals(dhcpInfo[7]) || "-2".equals(dhcpInfo[7])
				|| "-3".equals(dhcpInfo[7]))
			{
				tmpHtml += "DHCP地址池结束地址：" + errorMap.get(dhcpInfo[7]) + "<br>";
				dhcpInfo[7] = "";
			}
		else
			{
				tmpHtml += "DHCP地址池结束地址：获取成功<br>";
			}
		dhcpInfo[9] = tmpHtml;
		return dhcpInfo;
	}
	/**
	 * 配置DHCP信息
	 * 
	 * @param device_id
	 * @param value
	 * @return 1：成功 0：失败
	 */
	public int[] setDHCPInfo(String device_id, List<String[]> value)
	{
		logger.debug("===================snmp set start!========================");
		int[] ret = new int[9];
		// DHCP服务状态
		ret[0] = setParamValue(device_id, ConfigDevice.getParaArr("313", device_id), value.get(0)[0]);
		// DHCP地址池预留地址
		ret[1] = setSnmpInfo(device_id, "344", value.get(1));
		// 地址池是否启用
		ret[2] = setSnmpInfo(device_id, "345", value.get(2));
		// DHCP地址池列表信息
		ret[3] = setSnmpInfo(device_id, "339", value.get(3));
		// DHCP地址池网段信息
		ret[4] = setSnmpInfo(device_id, "340", value.get(4));
		// DHCP地址池掩码信息
		ret[5] = setSnmpInfo(device_id, "341", value.get(5));
		// DHCP地址池起始地址
		ret[6] = setSnmpInfo(device_id, "342", value.get(6));
		// DHCP地址池结束地址
		ret[7] = setSnmpInfo(device_id, "343", value.get(7));
		// 租期
		ret[8] = -1;
		logger.debug("===================snmp set end!========================");
		return ret;
	}
	/**
	 * 采集SNMP数据
	 * 
	 * @param device_id
	 * @param oid_type
	 * @return 成功则返回具体值 -1：暂不支持该属性采集 -2：没有配置采集oid -3：无法采集到数据
	 */
	private String getSnmpInfoBIO(String device_id, String oid_type)
	{
		logger.debug("====================get性能===================" + device_id
				+ "////" + oid_type);
		String sql = "select oid ,oid_para_type,unit from tab_gw_model_oper_oid a,tab_gw_device b where a.device_model=b.device_model_id and b.device_id='"
				+ device_id + "' and a.oid_type=" + oid_type;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields == null)
			{
				return "-1";
			}
		else
			{
				Object oid = fields.get("oid");
				if ((oid == null) || (oid.toString().trim().equals("")))
					{
						return "-2";
					}
				Performance.Data da = new Performance.Data();
				try
					{
						da = SnmpGatherInterface.GetInstance().getDataIMDFull(device_id,
								oid.toString());
						logger.debug("*****************" + da.dataDou + "===="
								+ da.dataStr);
					}
				catch (Exception e)
					{
						e.printStackTrace();
					}
				if ((da == null) || (da.dataStr == null))
					{
						return "-3";
					}
				logger.debug("*********GETINDex" + da.index);
				return da.dataStr;
			}
	}
	/**
	 * 通过walk获取性能
	 * 
	 * @param device_id
	 *            设备id
	 * @param oid_type
	 *            oid的type类型
	 * @param user
	 *            用户信息给tr069用的
	 * @param gatherType
	 *            采集方式
	 * @return 成功则返回具体值 -1：暂不支持该属性采集 -2：没有配置采集oid -3：无法采集到数据
	 */
	private String walkSnmpInfoBIO(String device_id, String oid_type)
	{
		logger.debug("====================walk性能===================" + device_id
				+ "////" + oid_type);
		String sql = "select oid ,oid_para_type,unit from tab_gw_model_oper_oid a,tab_gw_device b where a.device_model=b.device_model_id and b.device_id='"
				+ device_id + "' and a.oid_type=" + oid_type + " order by sequence";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		String restring = "";
		if (fields == null)
			{
				return "-1";
			}
		else
			{
				while (fields != null)
					{
						Object oid = fields.get("oid");
						if ((oid == null) || (oid.toString().trim().equals("")))
							{
								return "-2";
							}
						IntHolder ih = new IntHolder();
						Performance.Data[] da = SnmpGatherInterface.GetInstance()
								.GetDataListPortFull(ih, device_id, "", oid.toString());
						if ((da == null) || (da.length == 0))
							{
								return "-3";
							}
						int j = 0;
						for (Performance.Data d : da)
							{
								j++;
								restring += ((ih.value == 1) ? (d.dataDou) : (d.dataStr));
								if (j < da.length && da.length != 1)
									{
										restring += "<br>";
									}
								logger.debug("+++++++++++++++index+++++++++++++"
										+ d.index);
							}
						fields = cursor.getNext();
						if (fields != null){
							restring += ";";
						}
					}
				return restring;
			}
	}
	/**
	 * 设置对应节点的值
	 * @param device_id
	 * @param oid_type
	 * @param value
	 * @return 0：失败  1：成功
	 */
	private int setSnmpInfo(String device_id, String oid_type, String[] value)
	{
		logger.debug("====================set性能===================" + device_id
				+ "////" + oid_type);
		String sql = "select oid ,oid_para_type,unit from tab_gw_model_oper_oid a,tab_gw_device b where a.device_model=b.device_model_id and b.device_id='"
				+ device_id + "' and a.oid_type=" + oid_type + " order by sequence";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		int ret = 0;
		if (fields == null)
			{
				return -1;
			}
		else
			{
				while (fields != null)
					{
						Object oid = fields.get("oid");
						if ((oid == null) || (oid.toString().trim().equals("")))
							{
								return -1;
							}
						IntHolder ih = new IntHolder();
						Performance.Data[] da = SnmpGatherInterface.GetInstance()
								.GetDataListPortFull(ih, device_id, "", oid.toString());
						if ((da == null) || (da.length == 0))
							{
								return -2;
							}
						int j = 0;
						for (Performance.Data d : da)
							{
								logger.debug("+++++++++++++++index+++++++++++++"
										+ d.index);
								if (value.length > j)
									{
										logger.debug("+++++++++++++++value+++++++++++++"
												+ value[j]);
										if (!"".equals(value[j])){
											ret = setParamValue(device_id, oid + "." +d.index,
													value[j]);
										}
										else{
											ret = -1;
										}
										j++;
									}
							}
						fields = cursor.getNext();
					}
			}
		logger.debug("+++++++++++++++ret+++++++++++++"
				+ ret);
		return ret;
	}
	/**
	 * 设置节点值，明确节点的索引和值
	 * @param device_id
	 * @param oid
	 * @param value
	 * @return 0：失败  1：成功
	 */
	private int setParamValue(String device_id, String oid, String value)
	{
		logger.debug("set===================" + device_id + "--" + oid + "--"
				+ value);
		// 设备信息
		HashMap devInfoMap = getDevInfo(device_id);
		// 参数
		setDataV3 d1 = new setDataV3();
		d1.loopback_ip = loopback_ip;
		d1.port = port;
		d1.oid = oid;
		d1.value = value;
		d1.readComm = "";
		d1.writeComm = "";
		d1.v3param = getAuthInfoForV3(device_id);
		setDataV3[] setDataV3_arr = new setDataV3[1];
		setDataV3_arr[0] = d1;
		// 调用接口中方法
		setDataReturn[] setDataReturnArr = SnmpGatherInterfaceV3.getInstance().setWayV3(
				setDataV3_arr, setDataV3_arr.length, devInfoMap);
		// 返回信息
		boolean ret = true;
		if (null == setDataReturnArr || 0 == setDataReturnArr.length)
			{
				logger.warn("set===================" + oid + " 失败！无返回值");
				return 0;
			}
		else
			{
				for (int j = 0; j < setDataReturnArr.length; j++)
					{
						String rst = setDataReturnArr[0].result;
						ret = Boolean.parseBoolean(rst.toLowerCase());
					}
			}
		if (!ret)
			{
				logger.warn("set===================" + oid + " 失败！返回失败值");
				return 0;
			}
		else
			{
				logger.warn("set===================" + oid + " 成功");
				return 1;
			}
	}
	/**
	 * 获取V3所需要的认证信息
	 * 
	 * @param device_id
	 * @return
	 */
	private SNMPV3PARAM getAuthInfoForV3(String device_id)
	{
		String sql = "select * from sgw_security";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select auth_passwd, auth_protocol, privacy_passwd, privacy_protocol, security_level, security_model," +
					" security_username, snmp_r_passwd, snmp_w_passwd from sgw_security";
		}
		sql += " where device_id='" + device_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		HashMap map = DataSetBean.getRecord(sql);
		SNMPV3PARAM param = new SNMPV3PARAM();
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
	 * 获取设备信息
	 * 
	 * @param device_id
	 * @return
	 */
	private HashMap getDevInfo(String device_id)
	{
		String sqlDevInfo = "select device_name, gather_id, loopback_ip, port, path, acs_username, acs_passwd, device_id, device_serialnumber, oui from "
				+ " tab_gw_device where device_id='" + device_id + "'";
		PrepareSQL psql = new PrepareSQL(sqlDevInfo);
		psql.getSQL();
		Map devInfoMap = DataSetBean.getRecord(sqlDevInfo);
		loopback_ip = (String) devInfoMap.get("loopback_ip");
		port = (String) devInfoMap.get("port");
		return (HashMap) devInfoMap;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
	}
}
