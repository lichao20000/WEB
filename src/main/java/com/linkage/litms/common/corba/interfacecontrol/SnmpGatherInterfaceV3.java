package com.linkage.litms.common.corba.interfacecontrol;

import java.util.HashMap;

import com.linkage.commons.db.DBUtil;
import org.omg.CORBA.IntHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * @author Bruce(工号) tel：12345678
 * @version 1.0
 * @since May 27, 2008
 * @category com.linkage.litms.common.corba.interfacecontrol 版权：南京联创科技 网管科技部
 * 
 */
public class SnmpGatherInterfaceV3 extends BaseInterface {
	private static Logger log = LoggerFactory.getLogger(SnmpGatherInterfaceV3.class);
    private static SnmpGatherInterfaceV3 instance = null;

    private SnmpGatherInterfaceV3() {
	SetProcessName("SnmpGather");
	InitProcessList();
    }

    public static SnmpGatherInterfaceV3 getInstance() {
	if (instance == null) {
	    instance = new SnmpGatherInterfaceV3();
	}
	return instance;
    }

    /**
     * 获取V3所需要的认证信息
     * 
     * @param device_id
     * @return
     */
    public Performance.SNMPV3PARAM getAuthInfoForV3(String device_id) {
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
	Performance.SNMPV3PARAM param = new Performance.SNMPV3PARAM();
	param.authPasswd = (String) map.get("auth_passwd");
	param.authProtocol = (String) map.get("auth_protocol");
	param.privPasswd = (String) map.get("privacy_passwd");
	param.privProtocol = (String) map.get("privacy_protocol");
	param.securityLevel = Integer.parseInt((String) map
		.get("security_level"));
	param.securityModel = Integer.parseInt((String) map
		.get("security_model"));
	param.securityName = (String) map.get("security_username");
	param.snmp_r_word = (String) map.get("snmp_r_passwd");
	param.snmp_w_word = (String) map.get("snmp_w_passwd");

	return param;
    }

    /**
     * 获取索引信息
     * 
     * @param oid
     * @param devInfo
     * @return Performance.Data
     */
    public Performance.Data getDataIMDV3Single(String oid, HashMap devInfo) {

	Performance.Data[] dataList = getDataIMDV3(oid, devInfo);
	if (dataList == null)
	    return null;
	if (dataList.length < 1)
	    return null;
	return dataList[0];
    }

    /**
     * 获取索引信息
     * 
     * @return Performance.Data[]
     */
    public Performance.Data[] getDataIMDV3(String oid, HashMap devInfo) {

	if (devInfo == null) {
	    return null;
	}
	String device_id = (String) devInfo.get("device_id");

	GatherProcess process = SearchProcessByObject(devInfo);
	if (process == null) {
	    return null;
	}
	if (process.Manager == null) {
	    process.InitManager();
	}
	Performance.Data[] dataList = null;
	try {
	    dataList = ((Performance.PerformanceManager) process.Manager)
		    .GetDataIMDV3((String) devInfo.get("loopback_ip"), oid,
			    getAuthInfoForV3(device_id));
	} catch (Exception ex) {
	    ex.printStackTrace();
	    process.Manager = null;
	}
	return dataList;
    }

    /**
     * 
     * @param oid
     * @param devInfo
     * @return
     */
    public Performance.Data[] getDataListIMDV3(String oid, HashMap devInfo) {

	if (devInfo == null) {
	    return null;
	}
	String device_id = (String) devInfo.get("device_id");

	GatherProcess process = SearchProcessByObject(devInfo);
	if (process == null) {
	    return null;
	}
	if (process.Manager == null) {
	    process.InitManager();
	}
	Performance.Data[] dataList = null;
	try {
	    dataList = ((Performance.PerformanceManager) process.Manager)
		    .GetDataListIMDV3(new IntHolder(), (String) devInfo
			    .get("loopback_ip"), oid,
			    getAuthInfoForV3(device_id));
	} catch (Exception ex) {
	    ex.printStackTrace();
	    process.Manager = null;
	}
	return dataList;
    }

    /**
     * setWayV3
     * 
     * @param setDataV3_arr
     * @param length
     * @param devInfo
     * @return
     */
    public Performance.setDataReturn[] setWayV3(
	    Performance.setDataV3[] setDataV3_arr, int length, HashMap devInfo) {

	if (devInfo == null) {
	    return new Performance.setDataReturn[0];
	}
	GatherProcess process = SearchProcessByObject(devInfo);
	if (process == null) {
	    return new Performance.setDataReturn[0];
	}
	if (process.Manager == null) {
	    process.InitManager();
	}
	Performance.setDataReturn[] snmpGatherData = null;

	try {
	    snmpGatherData = ((Performance.PerformanceManager) process.Manager)
		    .SetWayV3(setDataV3_arr, length);
	} catch (Exception e) {
	    e.printStackTrace();
	    snmpGatherData = new Performance.setDataReturn[0];
	    process.Manager = null;
	}
	return snmpGatherData;
    }

    /**
     * 同时创建多个索引
     * @param setDataListV3_arr
     * @param length
     * @param devInfo
     * @return
     */
    public Performance.setDataReturn[] createOidNodeListV3 (Performance.createOidNodeList[] setDataListV3_arr, int length, HashMap devInfo) {

	if (devInfo == null) {
	    return new Performance.setDataReturn[0];
	}
	GatherProcess process = SearchProcessByObject(devInfo);
	if (process == null) {
	    return new Performance.setDataReturn[0];
	}
	if (process.Manager == null) {
	    process.InitManager();
	}
	Performance.setDataReturn[] snmpGatherData = null;

	try {
	    snmpGatherData = ((Performance.PerformanceManager) process.Manager)
		    .CreateOidNodeListV3(setDataListV3_arr, length);
	} catch (Exception e) {
	    e.printStackTrace();
	    snmpGatherData = new Performance.setDataReturn[0];
	    process.Manager = null;
	}
	return snmpGatherData;
    }
    /**
     * 创建索引
     * @param setDataV3_arr
     * @param length
     * @param devInfo
     * @return
     */
    public Performance.setDataReturn[] createOidNodeV3(
	    Performance.createDataV3[] setDataV3_arr, int length, HashMap devInfo) {

		if (devInfo == null) {
		    return new Performance.setDataReturn[0];
		}
		GatherProcess process = SearchProcessByObject(devInfo);
		if (process == null) {
		    return new Performance.setDataReturn[0];
		}
		if (process.Manager == null) {
		    process.InitManager();
		}
		Performance.setDataReturn[] snmpGatherData = null;

		try {
		    snmpGatherData = ((Performance.PerformanceManager) process.Manager)
			    .CreateOidNodeV3(setDataV3_arr, length);
		} catch (Exception e) {
		    e.printStackTrace();
		    snmpGatherData = new Performance.setDataReturn[0];
		    process.Manager = null;
		}
		return snmpGatherData;
    }

    /**
     * 获取节点下一个实例
     * 
     * @param oid
     * @param v3param
     * @param devInfo
     * @return
     */
    public Performance.Data[] getNextRouteInfoV3(String oid,
	    Performance.SNMPV3PARAM v3param, HashMap devInfo) {

	if (devInfo == null || oid == null) {
	    return null;
	}
	GatherProcess process = SearchProcessByObject(devInfo);
	if (process == null) {
	    log.warn("process is null!!");
	    return null;
	}
	if (process.Manager == null) {
	    process.InitManager();
	}
	Performance.Data[] dataList = null;

	try {
	    dataList = ((Performance.PerformanceManager) process.Manager)
		    .GetNextRouteInfoV3((String) devInfo.get("loopback_ip"),
			    v3param, oid);
	} catch (Exception e) {
	    e.printStackTrace();
	    process.Manager = null;
	}
	return dataList;
    }

    /**
     * 获取节点下一个实例(single)
     * 
     * @param oid
     * @param v3param
     * @param devInfo
     * @return
     */
    public Performance.Data getNextRouteInfoV3Single(String oid,
	    Performance.SNMPV3PARAM v3param, HashMap devInfo) {
	Performance.Data[] dataList = getNextRouteInfoV3(oid, v3param, devInfo);
	if (dataList == null)
	    return null;
	if (dataList.length < 1)
	    return null;
	return dataList[0];
    }
}
