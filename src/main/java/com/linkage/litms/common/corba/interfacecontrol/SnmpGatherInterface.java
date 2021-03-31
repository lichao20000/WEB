package com.linkage.litms.common.corba.interfacecontrol;

import java.util.HashMap;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.omg.CORBA.IntHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Performance.SNMPV3PARAM;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class SnmpGatherInterface extends BaseInterface
{
	private static Logger log = LoggerFactory.getLogger(SnmpGatherInterface.class);
	static protected SnmpGatherInterface Instance = null;
	private SnmpGatherInterface()
	{
		SetProcessName("SnmpGather");
		InitProcessList();
	}
	static public SnmpGatherInterface GetInstance()
	{
		if (Instance == null)
			{
				Instance = new SnmpGatherInterface();
			}
		return Instance;
	}
	public Performance.Data[] GetDataArrayAllIMD(String device_id, String readcom,
			String[] oidslist, int oidlength)
	{
		log.debug("begin GetDataArrayAllIMD device_id:" + device_id + "  oidlength:"
				+ oidlength + "     " + new DateTimeUtil().getLongDate());
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetDataArrayAllIMD to SnmpGather device_id:" + device_id
						+ "  oidlength:" + oidlength + "     "
						+ new DateTimeUtil().getLongDate());
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataArrayAllIMD(device_ip, readcom, oidslist, oidlength);
				log.debug("end GetDataArrayAllIMD to SnmpGather device_id:" + device_id
						+ "  oidlength:" + oidlength + "  size:" + snmpGatherData.length
						+ "     " + new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		log.debug("end GetDataArrayAllIMD device_id:" + device_id + "  oidlength:"
				+ oidlength + "  size:" + snmpGatherData.length + "     "
				+ new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	public Performance.Data[] GetDataArrayAllIMDv2(String device_id, String readcom,
			String[] oidslist, int oidlength)
	{
		log.debug("begin GetDataArrayAllIMDv2 device_id:" + device_id + "  oidlength:"
				+ oidlength + "     " + new DateTimeUtil().getLongDate());
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetDataArrayAllIMDv2 to SnmpGather device_id:"
						+ device_id + "  oidlength:" + oidlength + "     "
						+ new DateTimeUtil().getLongDate());
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataArrayAllIMDv2(device_ip, readcom, oidslist, oidlength);
				log.debug("end GetDataArrayAllIMDv2 to SnmpGather device_id:" + device_id
						+ "  oidlength:" + oidlength + "  size:" + snmpGatherData.length
						+ "     " + new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		log.debug("end GetDataArrayAllIMDv2 device_id:" + device_id + "  oidlength:"
				+ oidlength + "  size:" + snmpGatherData.length + "     "
				+ new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	public Performance.Data[] GetDataArrayIMD(String device_id, String readcom,
			String[] oidlist, int oidlength)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataArrayIMD(device_ip, readcom, oidlist, oidlength);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	public Performance.Data[] GetFillDataArrayIMD(String device_id, String readcom,
			String[] oidlist, int oidlength)
	{
		log.debug("begin GetFillDataArrayIMD device_id" + device_id + "  oidLength:"
				+ oidlength + "   " + new DateTimeUtil().getLongDate());
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetFillDataArrayIMD to SnmpGather device_id" + device_id
						+ "  oidLength:" + oidlength + "   "
						+ new DateTimeUtil().getLongDate());
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetFillDataArrayIMD(device_ip, readcom, oidlist, oidlength);
				log.debug("end GetFillDataArrayIMD to SnmpGather device_id" + device_id
						+ "  oidLength:" + oidlength + "   size:" + snmpGatherData.length
						+ "   " + new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		log.debug("end GetFillDataArrayIMD device_id" + device_id + "  oidLength:"
				+ oidlength + "   size:" + snmpGatherData.length + "   "
				+ new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	public Performance.Data[] GetDataArrayIMDv2(String device_id, String readcom,
			String[] oidlist, int oidlength)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataArrayIMDv2(device_ip, readcom, oidlist, oidlength);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	public Performance.Data[] GetFillDataArrayIMDv2(String device_id, String readcom,
			String[] oidlist, int oidlength)
	{
		log.debug("begin GetFillDataArrayIMDv2 device_id" + device_id + "  oidLength:"
				+ oidlength + "   " + new DateTimeUtil().getLongDate());
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetFillDataArrayIMDv2 to SnmpGather device_id"
						+ device_id + "  oidLength:" + oidlength + "   "
						+ new DateTimeUtil().getLongDate());
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetFillDataArrayIMDv2(device_ip, readcom, oidlist, oidlength);
				log.debug("end GetFillDataArrayIMD to SnmpGather device_id" + device_id
						+ "  oidLength:" + oidlength + "   size:" + snmpGatherData.length
						+ "   " + new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		log.debug("end GetFillDataArrayIMD device_id" + device_id + "  oidLength:"
				+ oidlength + "   size:" + snmpGatherData.length + "   "
				+ new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	public Performance.Data[] GetDataIMD(String device_id, String readcom, String oid)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataIMD(device_ip, readcom, oid);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	public Performance.Data[] GetDataIMDv2(String device_id, String readcom, String oid)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataIMDv2(device_ip, readcom, oid);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	public Performance.Data[] GetDataListIMD(IntHolder dataType, String device_id,
			String readcom, String oid)
	{
		log.debug("begin GetDataListIMD device_id:" + device_id + "  oid:" + oid + "     "
				+ new DateTimeUtil().getLongDate());
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetDataListIMD to SnmpGather device_id:" + device_id
						+ "  oid:" + oid + "     " + new DateTimeUtil().getLongDate());
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataListIMD(dataType, device_ip, readcom, oid);
				log.debug("begin GetDataListIMD to SnmpGather device_id:" + device_id
						+ "  oid:" + oid + "  size:" + snmpGatherData.length + "     "
						+ new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		log.debug("begin GetDataListIMD device_id:" + device_id + "  oid:" + oid
				+ "  size:" + snmpGatherData.length + "     "
				+ new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	public Performance.Data[] GetDataListPortV2(IntHolder dataType, String device_id,
			String readcom, String oid)
	{
		log.debug("begin GetDataListPortV2 device_id:" + device_id + "  oid:" + oid
				+ "     " + new DateTimeUtil().getLongDate());
		PrepareSQL psql = new PrepareSQL("select snmp_udp from tab_deviceresource where device_id='"
				+ device_id + "'");
		psql.getSQL();
		Map portnumMap = DataSetBean
				.getRecord("select snmp_udp from tab_deviceresource where device_id='"
						+ device_id + "'");
		String port = (String) portnumMap.get("snmp_udp");
		int portnum = Integer.parseInt(port);
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetDataListPortV2 to SnmpGather device_id:" + device_id
						+ "  oid:" + oid + "     " + new DateTimeUtil().getLongDate());
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataListPortV2(dataType, device_ip, readcom, oid, portnum);
				log.debug("begin GetDataListPortV2 to SnmpGather device_id:" + device_id
						+ "  oid:" + oid + "  size:" + snmpGatherData.length + "     "
						+ new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		log.debug("begin GetDataListPortV2 device_id:" + device_id + "  oid:" + oid
				+ "  size:" + snmpGatherData.length + "     "
				+ new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	// ***************************************************************************************
	//
	// 增加BBMS V1版本(主要设备资源表发生改变)
	//
	// **************************************************************************************
	public Performance.Data[] GetBBMSDataListPortV1(IntHolder dataType, String device_id,
			String readcom, String oid)
	{
		log.debug("begin GetDataListPortV1 device_id:" + device_id + "  oid:" + oid
				+ "     " + new DateTimeUtil().getLongDate());
		PrepareSQL psql = new PrepareSQL("select snmp_udp,gather_id,loopback_ip,device_id from tab_gw_device where device_id='"
				+ device_id + "'");
		psql.getSQL();
		HashMap deviceInfo = DataSetBean
				.getRecord("select snmp_udp,gather_id,loopback_ip,device_id from tab_gw_device where device_id='"
						+ device_id + "'");
		if (deviceInfo == null){
			return new Performance.Data[0];
		}
		
		String port = (String) deviceInfo.get("snmp_udp");
		int portnum = Integer.parseInt(port);
		
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) {
			return new Performance.Data[0];
		}
		if (process.Manager == null)
		{
			process.InitManager();
		}
		Performance.SNMPV3PARAM param = getAuthInfoForV3(device_id);
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = param.snmp_r_word;
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetDataListPortV1 to SnmpGather device_id:" + device_id
						+ "  oid:" + oid + "     " + new DateTimeUtil().getLongDate());
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataListPort(dataType, device_ip, readcom, oid, portnum);
				log.debug("begin GetDataListPortV1 to SnmpGather device_id:" + device_id
						+ "  oid:" + oid + "  size:" + snmpGatherData.length + "     "
						+ System.currentTimeMillis());
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		log.debug("begin GetDataListPortV1 device_id:" + device_id + "  oid:" + oid
				+ "  size:" + snmpGatherData.length + "     "
				+ new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	// ***************************************************************************************
	//
	// 增加BBMS V2版本(主要设备资源表发生改变)
	//
	// **************************************************************************************
	public Performance.Data[] GetBBMSDataListPortV2(IntHolder dataType, String device_id,
			String readcom, String oid)
	{
		log.debug("begin GetDataListPortV2 device_id:" + device_id + "  oid:" + oid
				+ "     " + new DateTimeUtil().getLongDate());
		PrepareSQL psql = new PrepareSQL("select snmp_udp,gather_id,loopback_ip,device_id from tab_gw_device where device_id='"
				+ device_id + "'");
		psql.getSQL();
		HashMap deviceInfo = DataSetBean
				.getRecord("select snmp_udp,gather_id,loopback_ip,device_id from tab_gw_device where device_id='"
						+ device_id + "'");
		if (deviceInfo == null){
			return new Performance.Data[0];
		}
		
		String port = (String) deviceInfo.get("snmp_udp");
		int portnum = Integer.parseInt(port);
		
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null){
			return new Performance.Data[0];
		}
		if (process.Manager == null)
			{
				process.InitManager();
			}
		Performance.SNMPV3PARAM param = getAuthInfoForV3(device_id);
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = param.snmp_r_word;
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetDataListPortV2 to SnmpGather device_id:" + device_id
						+ "  oid:" + oid + "     " + new DateTimeUtil().getLongDate());
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataListPortV2(dataType, device_ip, readcom, oid, portnum);
				log.debug("begin GetDataListPortV2 to SnmpGather device_id:" + device_id
						+ "  oid:" + oid + "  size:" + snmpGatherData.length + "     "
						+ new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		log.debug("begin GetDataListPortV2 device_id:" + device_id + "  oid:" + oid
				+ "  size:" + snmpGatherData.length + "     "
				+ new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	// ***************************************************************************************
	//
	// 增加SNMP V3
	//
	// ***************************************************************************************
	public Performance.Data[] GetDataListPortV3(IntHolder dataType, String device_id,
			String readcom, String oid)
	{
		log.error("BEN================>>>>begin GetDataListPortV3 device_id:" + device_id
				+ "  oid:" + oid + "     " + new DateTimeUtil().getLongDate());
		// Map portnumMap = DataSetBean.getRecord("select snmp_udp from
		// tab_gw_device where device_id='" + device_id + "'");
		// String port = (String)portnumMap.get("snmp_udp");
		// int portnum = Integer.parseInt(port);
		// HashMap deviceInfo = getDeviceInfoForV3(device_id);
		PrepareSQL psql = new PrepareSQL("select gather_id,loopback_ip from tab_gw_device where device_id='"
				+ device_id + "'");
		psql.getSQL();
		HashMap deviceInfo = DataSetBean
				.getRecord("select gather_id,loopback_ip from tab_gw_device where device_id='"
						+ device_id + "'");
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				log
						.error("BEN============>>>>>>>>begin GetDataListPortV3 to SnmpGather device_id:"
								+ device_id
								+ "  oid:"
								+ oid
								+ "     "
								+ new DateTimeUtil().getLongDate());
				// port_num先初始为0
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataListPortV3(dataType, device_ip, oid,
								getAuthInfoForV3(device_id), 0);
				log
						.error("BEN============>>>>begin GetDataListPortV3 to SnmpGather device_id:"
								+ device_id
								+ "  oid:"
								+ oid
								+ "  size:"
								+ snmpGatherData.length
								+ "     "
								+ new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		log.error("BEN=========>>>>>begin GetDataListPortV2 device_id:" + device_id
				+ "  oid:" + oid + "  size:" + snmpGatherData.length + "     "
				+ new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	/**
	 * 获取V3所需要的认证信息
	 *
	 * @param device_id
	 * @return
	 */
	public Performance.SNMPV3PARAM getAuthInfoForV3(String device_id)
	{
		String sql = "select * from sgw_security where device_id='" + device_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select auth_passwd, auth_protocol, privacy_passwd, privacy_protocol, security_level, " +
					" security_model, security_username, snmp_r_passwd, snmp_w_passwd from sgw_security where device_id='" + device_id + "'";
		}
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
	// ***************************************************************************************
	//
	// END SNMP V3
	//
	// ***************************************************************************************
	public Performance.Data[] GetDataListIMDv2(IntHolder dataType, String device_id,
			String readcom, String oid)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataListIMDv2(dataType, device_ip, readcom, oid);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	public Performance.Data[] GetSpecialDataListIMDv2(String device_id, String readcom,
			String oid, String[] value)
	{
		log.debug("begin GetSpecialDataListIMDv2 device_id:" + device_id + "  oid:" + oid
				+ "     " + new DateTimeUtil().getLongDate());
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetSpecialDataListIMDv2 to SnmpGather device_id:"
						+ device_id + "  oid:" + oid + "     "
						+ new DateTimeUtil().getLongDate());
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetSpecialDataListIMDv2(device_ip, readcom, oid, value);
				log.debug("begin GetSpecialDataListIMDv2 to SnmpGather device_id:"
						+ device_id + "  oid:" + oid + " size:" + snmpGatherData.length
						+ "     " + new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		log.debug("begin GetSpecialDataListIMDv2 device_id:" + device_id + "  oid:" + oid
				+ " size:" + snmpGatherData.length + "     "
				+ new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	public Performance.Data[] GetSpecialDataListIMD(String device_id, String readcom,
			String oid, String[] value)
	{
		log.debug("begin GetSpecialDataListIMD device_id:" + device_id + "  oid:" + oid
				+ "     " + new DateTimeUtil().getLongDate());
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetSpecialDataListIMD to SnmpGather device_id:"
						+ device_id + "  oid:" + oid + "     "
						+ new DateTimeUtil().getLongDate());
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetSpecialDataListIMD(device_ip, readcom, oid, value);
				log.debug("begin GetSpecialDataListIMD to SnmpGather device_id:"
						+ device_id + "  oid:" + oid + " size:" + snmpGatherData.length
						+ "     " + new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		log.debug("begin GetSpecialDataListIMD device_id:" + device_id + "  oid:" + oid
				+ " size:" + snmpGatherData.length + "     "
				+ new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	public Performance.Data[] GetNextRouteInfo(String device_id, String readcom,
			String oid)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetNextRouteInfo(device_ip, readcom, oid);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	public Performance.Data[] GetNextRouteInfoV2(String device_id, String readcom,
			String oid)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_ro_community");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		Performance.Data[] snmpGatherData;
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetNextRouteInfoV2(device_ip, readcom, oid);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	/**
	 * 广东SNMP设备,恢复出厂设置,重启 v1
	 *
	 * @param setData_arr
	 * @param length
	 * @param device_id
	 * @return
	 */
	public Performance.setDataReturn[] SetWayV1(Performance.setData[] setData_arr,
			int length, String device_id)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.setDataReturn[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.setDataReturn[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		Performance.setDataReturn[] snmpGatherData;
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.SetWayV1(setData_arr, length);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.setDataReturn[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	/**
	 * 广东SNMP设备,恢复出厂设置,重启 v2
	 *
	 * @param setData_arr
	 * @param length
	 * @param device_id
	 * @return
	 */
	public Performance.setDataReturn[] SetWayV2(Performance.setData[] setData_arr,
			int length, String device_id)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.setDataReturn[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.setDataReturn[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		Performance.setDataReturn[] snmpGatherData;
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.SetWayV2(setData_arr, length);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.setDataReturn[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	/**
	 * 广东SNMP设备,恢复出厂设置,重启 v1后 再去到设备get
	 *
	 * @param setData_arr
	 * @param length
	 * @param device_id
	 * @return
	 */
	public Performance.Data[] GetDataIMDPort(Performance.setData[] setData_arr,
			int length, String device_id)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		Performance.Data[] snmpGatherData;
		String ip = setData_arr[0].loopback_ip;
		String readcom = setData_arr[0].readComm;
		String oid = setData_arr[0].oid;
		int port = Integer.parseInt(setData_arr[0].port);
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataIMDPortv1(ip, readcom, oid, port);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	/**
	 * 广东SNMP设备,恢复出厂设置,重启 v2后 再去到设备get
	 *
	 * @param setData_arr
	 * @param length
	 * @param device_id
	 * @return
	 */
	public Performance.Data[] GetDataIMDPortv2(Performance.setData[] setData_arr,
			int length, String device_id)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		Performance.Data[] snmpGatherData;
		String ip = setData_arr[0].loopback_ip;
		String readcom = setData_arr[0].readComm;
		String oid = setData_arr[0].oid;
		int port = Integer.parseInt(setData_arr[0].port);
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.GetDataIMDPortv2(ip, readcom, oid, port);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.Data[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	/**
	 * 广东SNMP设备升级
	 *
	 * @param setData_arr
	 * @param length
	 * @param device_id
	 * @return
	 */
	public Performance.setDataReturn[] SetNetSnmpWayV1(Performance.setData[] setData_arr,
			int length, String device_id)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.setDataReturn[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.setDataReturn[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		Performance.setDataReturn[] snmpGatherData;
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.SetNetSnmpWayV1(setData_arr, length);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.setDataReturn[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	/**
	 * 广东SNMP设备,恢复出厂设置,重启 v2
	 *
	 * @param setData_arr
	 * @param length
	 * @param device_id
	 * @return
	 */
	public Performance.setDataReturn[] SetNetSnmpWayV2(Performance.setData[] setData_arr,
			int length, String device_id)
	{
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.setDataReturn[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.setDataReturn[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		Performance.setDataReturn[] snmpGatherData;
		try
			{
				snmpGatherData = ((Performance.PerformanceManager) process.Manager)
						.SetNetSnmpWayV2(setData_arr, length);
			} catch (Exception e)
			{
				e.printStackTrace();
				snmpGatherData = new Performance.setDataReturn[0];
				process.Manager = null;
			}
		return snmpGatherData;
	}
	public Performance.Data[] GetDataListPortFull(IntHolder dataType, String device_id,
			String readcom, String oid)
	{
		log.debug("begin GetDataListPortFull device_id:" + device_id + "  oid:" + oid
				+ "     " + new DateTimeUtil().getLongDate());
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null){
			return new Performance.Data[0];
		}
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null){
			return new Performance.Data[0];
		}
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_r_passwd");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		String snmp_version = (String) deviceInfo.get("snmp_version");
		String snmpPortStr = (String) deviceInfo.get("snmp_udp");
		int snmpPort = Integer.parseInt(snmpPortStr);
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetDataListPortFull to snmpGather  one device_id:"
						+ device_id + "  oid:" + oid + "  snmp_version:"+snmp_version+"     "
						+ new DateTimeUtil().getLongDate());
				if ("v1".equalsIgnoreCase(snmp_version))
					{
						snmpGatherData = ((Performance.PerformanceManager) process.Manager)
								.GetDataListPort(dataType, device_ip, readcom, oid,
										snmpPort);
					}
				else if ("v2".equalsIgnoreCase(snmp_version))
					{
						snmpGatherData = ((Performance.PerformanceManager) process.Manager)
								.GetDataListPortV2(dataType, device_ip, readcom, oid,
										snmpPort);
					}
				else
					{
						SNMPV3PARAM param = getSnmpV3Param(deviceInfo);
						snmpGatherData = ((Performance.PerformanceManager) process.Manager)
								.GetDataListPortV3(dataType, device_ip, oid, param,
										snmpPort);
					}
				log.warn("end GetDataListPortFull to snmpGather one device_id:"
						+ device_id + "  oid:" + oid + "  snmp_version:"+snmp_version+ "    result_size:"+snmpGatherData.length+"     "
						+ new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				process.Manager = null;
				// 重绑
				if (process == null) return new Performance.Data[0];
				if (process.Manager == null)
					{
						process.InitManager();
					}
				try
					{
						log
								.debug("begin GetDataListPortFull to snmpGather  two device_id:"
										+ device_id
										+ "  oid:"
										+ oid
										+ "  snmp_version:"+snmp_version+ "     "
										+ new DateTimeUtil().getLongDate());
						if ("v1".equalsIgnoreCase(snmp_version))
							{
								snmpGatherData = ((Performance.PerformanceManager) process.Manager)
										.GetDataListPort(dataType, device_ip, readcom,
												oid, snmpPort);
							}
						else if ("v2".equalsIgnoreCase(snmp_version))
							{
								snmpGatherData = ((Performance.PerformanceManager) process.Manager)
										.GetDataListPortV2(dataType, device_ip, readcom,
												oid, snmpPort);
							}
						else
							{
								SNMPV3PARAM param = getSnmpV3Param(deviceInfo);
								snmpGatherData = ((Performance.PerformanceManager) process.Manager)
										.GetDataListPortV3(dataType, device_ip, oid,
												param, snmpPort);
							}
						log.debug("end GetDataListPortFull to snmpGather two device_id:"
								+ device_id + "  oid:" + oid + "  snmp_version:"+snmp_version+ "    result_size:"+snmpGatherData.length+"     "
								+ new DateTimeUtil().getLongDate());
					} catch (Exception e1)
					{
						log.error("GetDataListPortFull device_id:" + device_id);
						snmpGatherData = new Performance.Data[0];
						process.Manager = null;
					}
			}
		log.warn("end GetDataListPortFull device_id:" + device_id + "  oid:" + oid
				+ "     " + new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	/**
	 * GetFillDataArrayIMDFull 支持v1/v2/v3参数
	 *
	 * @param device_id
	 * @param readcom
	 * @param oidlist
	 * @param oidlength
	 * @return
	 */
	public Performance.Data[] GetFillDataArrayIMDFull(String device_id, String readcom,
			String[] oidlist, int oidlength)
	{
		log.debug("begin GetFillDataArrayIMDFull device_id:" + device_id + "  oidSize:"
				+ oidlist.length + "     " + new DateTimeUtil().getLongDate());
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_r_passwd");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		String snmp_version = (String) deviceInfo.get("snmp_version");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetFillDataArrayIMDFull to snmpGather one device_id:"
						+ device_id + "  oidSize:" + oidlist.length + "  snmp_version:"
						+ snmp_version + "     " + new DateTimeUtil().getLongDate());
				if ("v1".equalsIgnoreCase(snmp_version))
					{
						snmpGatherData = ((Performance.PerformanceManager) process.Manager)
								.GetFillDataArrayIMD(device_ip, readcom, oidlist,
										oidlength);
					}
				else if ("v2".equalsIgnoreCase(snmp_version))
					{
						snmpGatherData = ((Performance.PerformanceManager) process.Manager)
								.GetFillDataArrayIMDv2(device_ip, readcom, oidlist,
										oidlength);
					}
				else
					{
						SNMPV3PARAM param = getSnmpV3Param(deviceInfo);
						snmpGatherData = ((Performance.PerformanceManager) process.Manager)
								.GetFillDataArrayIMDV3(device_ip, oidlist, param,
										oidlength);
					}
				log.debug("end GetFillDataArrayIMDFull to snmpGather one device_id:"
						+ device_id + "  oidSize:" + oidlist.length + "  snmp_version:"
						+ snmp_version + "     " + new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				process.Manager = null;
				// 重绑
				if (process == null) return new Performance.Data[0];
				if (process.Manager == null)
					{
						process.InitManager();
					}
				try
					{
						log
								.debug("begin GetFillDataArrayIMDFull to snmpGather two device_id:"
										+ device_id
										+ "  oidSize:"
										+ oidlist.length
										+ "  snmp_version:"
										+ snmp_version
										+ "     "
										+ new DateTimeUtil().getLongDate());
						if ("v1".equalsIgnoreCase(snmp_version))
							{
								snmpGatherData = ((Performance.PerformanceManager) process.Manager)
										.GetFillDataArrayIMD(device_ip, readcom, oidlist,
												oidlength);
							}
						else if ("v2".equalsIgnoreCase(snmp_version))
							{
								snmpGatherData = ((Performance.PerformanceManager) process.Manager)
										.GetFillDataArrayIMDv2(device_ip, readcom,
												oidlist, oidlength);
							}
						else
							{
								SNMPV3PARAM param = getSnmpV3Param(deviceInfo);
								snmpGatherData = ((Performance.PerformanceManager) process.Manager)
										.GetFillDataArrayIMDV3(device_ip, oidlist, param,
												oidlength);
							}
						log
								.debug("end GetFillDataArrayIMDFull to snmpGather two device_id:"
										+ device_id
										+ "  oidSize:"
										+ oidlist.length
										+ "  snmp_version:"
										+ snmp_version
										+ "     "
										+ new DateTimeUtil().getLongDate());
					} catch (Exception e1)
					{
						log.error("GetFillDataArrayIMDFull device_id:" + device_id);
						snmpGatherData = new Performance.Data[0];
						process.Manager = null;
					}
			}
		log.debug("end GetFillDataArrayIMDFull device_id:" + device_id + "  oidSize:"
				+ oidlist.length + "     " + new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	/**
	 * GetDataArrayAllIMDFull v1、v2、v3参数设置
	 *
	 * @param device_id
	 * @param readcom
	 * @param oidslist
	 * @param oidlength
	 * @return
	 */
	public Performance.Data[] GetDataArrayAllIMDFull(String device_id, String readcom,
			String[] oidslist, int oidlength)
	{
		log.debug("begin GetDataArrayAllIMDFull device_id:" + device_id + "  oidSize:"
				+ oidslist.length + "     " + new DateTimeUtil().getLongDate());
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_r_passwd");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		String snmp_version = (String) deviceInfo.get("snmp_version");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetDataArrayAllIMDFull to snmpGather  one device_id:"
						+ device_id + "  oid_size:" + oidslist.length + "  snmp_version:"
						+ snmp_version + "    " + new DateTimeUtil().getLongDate());
				if ("v1".equalsIgnoreCase(snmp_version))
					{
						snmpGatherData = ((Performance.PerformanceManager) process.Manager)
								.GetDataArrayAllIMD(device_ip, readcom, oidslist,
										oidlength);
					}
				else if ("v2".equalsIgnoreCase(snmp_version))
					{
						snmpGatherData = ((Performance.PerformanceManager) process.Manager)
								.GetDataArrayAllIMDv2(device_ip, readcom, oidslist,
										oidlength);
					}
				else
					{
						SNMPV3PARAM param = getSnmpV3Param(deviceInfo);
						snmpGatherData = ((Performance.PerformanceManager) process.Manager)
								.GetDataArrayAllIMDV3(device_ip, oidslist, param,
										oidlength);
					}
				log.debug("end GetDataArrayAllIMDFull to snmpGather  one device_id:"
						+ device_id + "  oid_size:" + oidslist.length + "  snmp_version:"
						+ snmp_version + "  result_size:" + snmpGatherData.length
						+ "     " + new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				process.Manager = null;
				// 重绑
				if (process == null) return new Performance.Data[0];
				if (process.Manager == null)
					{
						process.InitManager();
					}
				try
					{
						log
								.debug("begin GetDataArrayAllIMDFull to snmpGather  two device_id:"
										+ device_id
										+ "  oid_size:"
										+ oidslist.length
										+ "  snmp_version:"
										+ snmp_version
										+ "     "
										+ new DateTimeUtil().getLongDate());
						if ("v1".equalsIgnoreCase(snmp_version))
							{
								snmpGatherData = ((Performance.PerformanceManager) process.Manager)
										.GetDataArrayAllIMD(device_ip, readcom, oidslist,
												oidlength);
							}
						else if ("v2".equalsIgnoreCase(snmp_version))
							{
								snmpGatherData = ((Performance.PerformanceManager) process.Manager)
										.GetDataArrayAllIMDv2(device_ip, readcom,
												oidslist, oidlength);
							}
						else
							{
								SNMPV3PARAM param = getSnmpV3Param(deviceInfo);
								snmpGatherData = ((Performance.PerformanceManager) process.Manager)
										.GetDataArrayAllIMDV3(device_ip, oidslist, param,
												oidlength);
							}
						log
								.debug("end GetDataArrayAllIMDFull to snmpGather  two device_id:"
										+ device_id
										+ "  oid_size:"
										+ oidslist.length
										+ "  result_size:"
										+ snmpGatherData.length
										+ "     " + new DateTimeUtil().getLongDate());
					} catch (Exception e1)
					{
						log.error("GetDataArrayAllIMDFull device_id:" + device_id);
						snmpGatherData = new Performance.Data[0];
						process.Manager = null;
					}
			}
		log.debug("end GetDataArrayAllIMDFull device_id:" + device_id + "  oidSize:"
				+ oidslist.length + "     " + new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	/**
	 * GetDataListIMD v1、v2、v3版本的配置
	 *
	 * @param dataType
	 * @param device_id
	 * @param readcom
	 * @param oid
	 * @return
	 */
	public Performance.Data[] GetDataListIMDFull(IntHolder dataType, String device_id,
			String readcom, String oid)
	{
		log.debug("begin GetDataListIMDFull device_id:" + device_id + "  oid:" + oid
				+ "     " + new DateTimeUtil().getLongDate());
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data[0];
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data[0];
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		readcom = (String) deviceInfo.get("snmp_r_passwd");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		String snmp_version = (String) deviceInfo.get("snmp_version");
		Performance.Data[] snmpGatherData;
		try
			{
				log.debug("begin GetDataListIMDFull to snmpGather  one device_id:"
						+ device_id + "  oid:" + oid + "  snmp_version:" + snmp_version
						+ "     " + new DateTimeUtil().getLongDate());
				if ("v1".equalsIgnoreCase(snmp_version))
					{
						snmpGatherData = ((Performance.PerformanceManager) process.Manager)
								.GetDataListIMD(dataType, device_ip, readcom, oid);
					}
				else if ("v2".equalsIgnoreCase(snmp_version))
					{
						snmpGatherData = ((Performance.PerformanceManager) process.Manager)
								.GetDataListIMDv2(dataType, device_ip, readcom, oid);
					}
				else
					{
						SNMPV3PARAM param = getSnmpV3Param(deviceInfo);
						snmpGatherData = ((Performance.PerformanceManager) process.Manager)
								.GetDataListIMDV3(dataType, device_ip, oid, param);
					}
				log.debug("end GetDataListIMDFull to snmpGather  one device_id:"
						+ device_id + "  oid:" + oid + "   snmp_version:" + snmp_version
						+ "   size:" + snmpGatherData.length + "     "
						+ new DateTimeUtil().getLongDate());
			} catch (Exception e)
			{
				e.printStackTrace();
				process.Manager = null;
				// 重绑
				if (process == null) return new Performance.Data[0];
				if (process.Manager == null)
					{
						process.InitManager();
					}
				try
					{
						log.debug("begin GetDataListIMDFull to snmpGather two device_id:"
								+ device_id + "  oid:" + oid + "  snmp_version:"
								+ snmp_version + "     "
								+ new DateTimeUtil().getLongDate());
						if ("v1".equalsIgnoreCase(snmp_version))
							{
								snmpGatherData = ((Performance.PerformanceManager) process.Manager)
										.GetDataListIMD(dataType, device_ip, readcom, oid);
							}
						else if ("v2".equalsIgnoreCase(snmp_version))
							{
								snmpGatherData = ((Performance.PerformanceManager) process.Manager)
										.GetDataListIMDv2(dataType, device_ip, readcom,
												oid);
							}
						else
							{
								SNMPV3PARAM param = getSnmpV3Param(deviceInfo);
								snmpGatherData = ((Performance.PerformanceManager) process.Manager)
										.GetDataListIMDV3(dataType, device_ip, oid, param);
							}
						log.debug("end GetDataListIMDFull to snmpGather two device_id:"
								+ device_id + "  oid:" + oid + "  snmp_version:"
								+ snmp_version + "       size:" + snmpGatherData.length
								+ "     " + new DateTimeUtil().getLongDate());
					} catch (Exception e1)
					{
						log.error("GetDataListIMDFull fail device_id:" + device_id);
						snmpGatherData = new Performance.Data[0];
						process.Manager = null;
					}
			}
		log.debug("end GetDataListIMDFull device_id:" + device_id + "  oid:" + oid
				+ "     " + new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	/**
	 * 通过设备的ip地址、精细oid直接功过snmpGet获取值 ，该方法自动判别v1，v2，v3
	 *
	 * @param device_id
	 *            设备id
	 * @param oid
	 *            获取指标的oid
	 * @return Performance.Data
	 */
	public Performance.Data getDataIMDFull(String device_id, String oid)
	{
		log.debug("begin GetDataListIMDFull device_id:" + device_id + "  oid:" + oid
				+ "     " + new DateTimeUtil().getLongDate());
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null) return new Performance.Data();
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null) return new Performance.Data();
		if (process.Manager == null)
			{
				process.InitManager();
			}
		// 由于webTopo不能得到read community，因此需要重新赋值
		String readcom = (String) deviceInfo.get("snmp_r_passwd");
		String device_ip = (String) deviceInfo.get("loopback_ip");
		String snmp_version = (String) deviceInfo.get("snmp_version");
		Performance.Data snmpGatherData;
		try
			{
				log.debug("begin GetDataListIMDFull to snmpGather  one device_id:"
						+ device_id + "  oid:" + oid + "  snmp_version:" + snmp_version
						+ "     " + new DateTimeUtil().getLongDate());
				if ("v1".equalsIgnoreCase(snmp_version))
					{
						Performance.Data[] tmp = ((Performance.PerformanceManager) process.Manager)
								.GetDataIMD(device_ip, readcom, oid);
						snmpGatherData = tmp.length == 0 ? new Performance.Data()
								: tmp[0];
					}
				else if ("v2".equalsIgnoreCase(snmp_version))
					{
						Performance.Data[] tmp = ((Performance.PerformanceManager) process.Manager)
								.GetDataIMDv2(device_ip, readcom, oid);
						snmpGatherData = tmp.length == 0 ? new Performance.Data()
								: tmp[0];
					}
				else
					{
						SNMPV3PARAM param = getSnmpV3Param(deviceInfo);
						Performance.Data[] tmp = ((Performance.PerformanceManager) process.Manager)
								.GetDataIMDV3(device_ip, oid, param);
						snmpGatherData = tmp.length == 0 ? new Performance.Data()
								: tmp[0];
					}
			} catch (Exception e)
			{
				e.printStackTrace();
				process.Manager = null;
				// 重绑
				if (process == null) return new Performance.Data();
				if (process.Manager == null)
					{
						process.InitManager();
					}
				try
					{
						log.debug("begin GetDataListIMDFull to snmpGather  one device_id:"
								+ device_id + "  oid:" + oid + "  snmp_version:"
								+ snmp_version + "     "
								+ new DateTimeUtil().getLongDate());
						if ("v1".equalsIgnoreCase(snmp_version))
							{
								Performance.Data[] tmp = ((Performance.PerformanceManager) process.Manager)
										.GetDataIMD(device_ip, readcom, oid);
								snmpGatherData = tmp.length == 0 ? new Performance.Data()
										: tmp[0];
							}
						else if ("v2".equalsIgnoreCase(snmp_version))
							{
								Performance.Data[] tmp = ((Performance.PerformanceManager) process.Manager)
										.GetDataIMDv2(device_ip, readcom, oid);
								snmpGatherData = tmp.length == 0 ? new Performance.Data()
										: tmp[0];
							}
						else
							{
								SNMPV3PARAM param = getSnmpV3Param(deviceInfo);
								Performance.Data[] tmp = ((Performance.PerformanceManager) process.Manager)
										.GetDataIMDV3(device_ip, oid, param);
								snmpGatherData = tmp.length == 0 ? new Performance.Data()
										: tmp[0];
							}
					} catch (Exception e1)
					{
						log.error("GetDataListIMDFull fail device_id:" + device_id);
						snmpGatherData = new Performance.Data();
						process.Manager = null;
					}
			}
		log.debug("end GetDataListIMDFull device_id:" + device_id + "  oid:" + oid
				+ "     " + new DateTimeUtil().getLongDate());
		return snmpGatherData;
	}
	/**
	 * 获取v3参数
	 *
	 * @param deviceInfo
	 * @return
	 */
	private SNMPV3PARAM getSnmpV3Param(HashMap deviceInfo)
	{
		SNMPV3PARAM param = new SNMPV3PARAM();
		param.authPasswd = (String) deviceInfo.get("auth_passwd");
		param.authProtocol = (String) deviceInfo.get("auth_protocol");
		param.privPasswd = (String) deviceInfo.get("privacy_passwd");
		param.privProtocol = (String) deviceInfo.get("privacy_protocol");
		param.securityLevel = Integer.parseInt((String) deviceInfo.get("security_level"));
		param.securityModel = Integer.parseInt((String) deviceInfo.get("security_model"));
		param.securityName = (String) deviceInfo.get("security_username");
		param.snmp_r_word = (String) deviceInfo.get("snmp_r_passwd");
		param.snmp_w_word = (String) deviceInfo.get("snmp_w_passwd");
		log.warn(param.authPasswd + ";" + param.authProtocol + ";" + param.privPasswd
				+ ";" + param.privProtocol + ";" + param.securityLevel + ";"
				+ param.securityModel + ";" + param.securityName + ";"
				+ param.snmp_r_word + ";" + param.snmp_w_word);
		return param;
	}
}
