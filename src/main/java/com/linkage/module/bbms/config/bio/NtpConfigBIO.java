package com.linkage.module.bbms.config.bio;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Performance.Data;
import Performance.SNMPV3PARAM;
import Performance.createDataV3;
import Performance.setDataReturn;
import Performance.setDataV3;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterfaceV3;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.gw.DeviceDAO;
import com.linkage.module.gwms.dao.gw.TimeNtpDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.TimeNtpOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;
import com.linkage.module.gwms.util.strategy.StrategyXml;

/**
 * @author Jason(3412)
 * @date 2009-10-21
 */
public class NtpConfigBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(NtpConfigBIO.class);
	//设备表 tab_gw_device
	private DeviceDAO deviceDao;
	//时区NTP
	private TimeNtpDAO timeNtpDao;
	
	/**
	 * 采集NTP数据
	 * 
	 * @param configType：'2'为snmp方式采集，其他用tr069
	 * @author Jason(3412)
	 * @date 2009-10-22
	 * @return String '1|时区偏移(时区名称)|NTP服务器1|NTP服务器2|使能状态' or '-1|失败原因'
	 */
	public String gatherNtp(String deviceId, String configType){
		logger.debug("gatherNtp({},{})", deviceId, configType);
		if("2".equals(configType)){
			// snmp方式
			return gatherNtpSnmp(deviceId);
		}else{
			return gatherNtpTr069(deviceId);
		}
		
	}
	
	
	/**
	 * 通过TR069方式采集设备的NTP服务器，时区信息
	 * 
	 * @param 设备ID
	 * @author Jason(3412)
	 * @date 2009-10-21
	 * @return String '1|时区偏移(时区名称)|NTP服务器1|NTP服务器2|使能状态' or '-1|失败原因'
	 */
	public String gatherNtpTr069(String deviceId) {
		logger.debug("gatherNtpTr069({})", deviceId);
		StringBuffer strNtp = new StringBuffer();
		// 如果找不到设备信息，就返回
		if (false == StringUtil.IsEmpty(deviceId)) {
			// 调用采集模块(6表示Time结点)
			int iret = new SuperGatherCorba(LipossGlobals.getGw_Type(deviceId)).getCpeParams(deviceId, ConstantClass.GATHER_TIME);
			if(1 == iret){// 采集成功
				// 从数据库中获取
				TimeNtpOBJ timeNtpObj = timeNtpDao.queryTimeNTpObj(deviceId);
				strNtp.append("1");
				strNtp.append("|");
				strNtp.append(timeNtpObj.getTimezone() + "(" + timeNtpObj.getTimezoneName() + ")");
				strNtp.append("|");
				strNtp.append(timeNtpObj.getNtpServer1());
				strNtp.append("|");
				strNtp.append(timeNtpObj.getNtpServer2());
				strNtp.append("|");
				strNtp.append(timeNtpObj.getEnable());
			}else{// 采集失败
				logger.warn(Global.G_Fault_Map.get(iret).getFaultReason());
				strNtp.append("-1");
				strNtp.append("|");
				strNtp.append(Global.G_Fault_Map.get(iret));
			}
		}else{
			logger.warn("gatherNtpTr069 deviceId is null");
			strNtp.append("-1");
			strNtp.append("|");
			strNtp.append("设备ID为空");
		}
		return strNtp.toString();
	}

	/**
	 * 通过snmp方式采集设备的NTP服务器信息
	 * 
	 * @param 设备ID
	 * @author Jason(3412)
	 * @date 2009-10-21
	 * @return String '1|0|NTP服务器1|NTP服务器2' or '-1|失败原因'
	 */
	public String gatherNtpSnmp(String deviceId) {
		logger.debug("gatherNtpSnmp({})", deviceId);
		StringBuffer strNtp = new StringBuffer();
		if(false == StringUtil.IsEmpty(deviceId)){
			String oid_1 = "1.3.6.1.4.1.25506.8.22.2.1.1.1.36";
			Map devInfoMap = deviceDao.getDevInfoMap(deviceId);
			Data[] dataList = SnmpGatherInterfaceV3.getInstance()
					.getDataListIMDV3(oid_1, (HashMap)devInfoMap);
			if (dataList == null){
				logger.warn("dataList is null 采集失败");
				return "-1|采集失败";
			}else{
				strNtp.append("1");
				strNtp.append("|");
				strNtp.append("0");
				strNtp.append("|");
				for (int i = 0; i < dataList.length; i++) {
					Data data = dataList[i];
					String ip = data.index;
					ip = ip.substring(0, ip.length() - 2);
					// resultMap.put("server_" + (i + 1), ip);
					strNtp.append(ip);
					strNtp.append("|");
					if(i == 2){
						// 只需要获取第一和第二个NTP服务器
						strNtp.deleteCharAt(strNtp.length()-1);
						break;
					}
				}
				strNtp.append("|");
				strNtp.append("0");
			}
		}else{
			logger.warn("gatherNtpSnmp deviceId is null");
			strNtp.append("-1");
			strNtp.append("|");
			strNtp.append("设备ID为空");
		}
		return strNtp.toString();
	}

	/**
	 * 通过TR069方式配置设备的NTP服务器，时区信息，入策略调用预读的方式
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-10-21
	 * @return int  1表示成功; -1表示策略入库失败; -2表示调用预读模块失败; 
	 */
	public String configNtpTr069(long accoid, TimeNtpOBJ timeNtpObj) {
		logger.debug("configNtpTr069({},{})", accoid, timeNtpObj);
		//获取配置参数(XML)字符串
		String strategyXmlParam = StrategyXml.timeNtp2Xml(timeNtpObj);
		/** 入策略表，调预读 */
		//立即执行
		int strategyType = 0;
		//NTP配置的service_id
		int ntpServiceId = ConstantClass.TIME;
		StrategyOBJ strategyObj = new StrategyOBJ();
		// id,acc_oid,time,type,device_id,oui,device_serialnumber,username,sheet_para,service_id,task_id,order_id,sheet_type
		// 策略ID
		strategyObj.createId();
		// 策略配置时间
		strategyObj.setTime(TimeUtil.getCurrentTime());
		// 用户id
		strategyObj.setAccOid(accoid);
		// 立即执行
		strategyObj.setType(strategyType);
		// 设备ID
		strategyObj.setDeviceId(timeNtpObj.getDeviceId());
		// QOS serviceId
		strategyObj.setServiceId(ntpServiceId);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		strategyObj.setTempId(ntpServiceId);
		strategyObj.setIsLastOne(1);
		// 参数
		strategyObj.setSheetPara(strategyXmlParam);
		// 入策略表
		if(deviceDao.addStrategy(strategyObj)){
			//调用预读
			if(true == CreateObjectFactory.createPreProcess(Global.GW_TYPE_ITMS).processOOBatch(String.valueOf(strategyObj.getId()))){
				logger.debug("调预读成功");
				return "1|调用后台成功|" + strategyObj.getId();
			}else{
				logger.warn("调用预读失败");
				return "-1|调用后台失败";
			}
		}else{
			logger.warn("策略入库失败");
			return "-1|策略入库失败";
		}
	}

	/**
	 * 通过snmp方式配置设备的NTP服务器，返回配置结果字符串
	 * 
	 * @param 设备ID,是否启用,
	 * @author Jason(3412)
	 * @date 2009-10-21
	 * @return String
	 */
	public String configNtpSnmp(String deviceId, String enable,
			String ntpServer1, String ntpServer2) {
		logger.debug("configNtpSnmp({},{},{},{),{}", new Object[]{deviceId, enable, ntpServer1, ntpServer2});

		SNMPV3PARAM v3param = SnmpGatherInterfaceV3.getInstance().getAuthInfoForV3(deviceId);

		String oid_1 = "1.3.6.1.4.1.25506.8.22.2.1.1.1.36";
		Map devInfoMap = deviceDao.getDevInfoMap(deviceId);
		if(null != devInfoMap){
			String loopback_ip = StringUtil.getStringValue(devInfoMap.get("loopback_ip"));
			Data[] dataList = SnmpGatherInterfaceV3.getInstance()
			.getDataListIMDV3(oid_1, (HashMap)devInfoMap);
			if (dataList != null) {
				// 原来已有实例，删除先
				for (int i = 0; i < dataList.length; i++) {
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
					setDataReturn[] setDataReturnArr = SnmpGatherInterfaceV3
							.getInstance().setWayV3(setDataV3_arr,
									setDataV3_arr.length, (HashMap)devInfoMap);
		
					boolean ret = true;
					if (null == setDataReturnArr
							|| 0 == setDataReturnArr.length) {
						logger.warn("删除原有NTPServer实例失败!");
						return "-1|删除原有NTPServer实例失败!";
					} else {
						for (int j = 0; j < setDataReturnArr.length; j++) {
							String rst = setDataReturnArr[0].result;
							ret = ret
									&& Boolean.parseBoolean(rst.toLowerCase());
						}
					}
					if (!ret) {
						logger.warn("删除原有NTPServer实例失败!");
						return "-1|删除原有NTPServer实例失败!";
					} else {
						logger.debug("删除NTPServer实例" + ip + "成功!");
					}
				}
			}
			if ("1".equals(enable)) {// 状态是启用，则创建新server
				createDataV3 cd = null;
				if (false == StringUtil.IsEmpty(ntpServer1)) {
					cd = new createDataV3();
					cd.loopback_ip = loopback_ip;
					cd.oid = oid_1 + "." + ntpServer1.trim() + ".3";
					cd.port = "161";
					cd.readComm = "";
					cd.type = 2;
					cd.v3param = v3param;
					cd.value = "4";
					cd.writeComm = "";
					logger.debug("cd.oid=" + cd.oid);
				}

				createDataV3 cd2 = null;
				if (false == StringUtil.IsEmpty(ntpServer2)) {
					cd2 = new createDataV3();
					cd2.loopback_ip = loopback_ip;
					cd2.oid = oid_1 + "." + ntpServer2.trim() + ".3";
					cd2.port = "161";
					cd2.readComm = "";
					cd2.type = 2;
					cd2.v3param = v3param;
					cd2.value = "4";
					cd2.writeComm = "";
					logger.debug("cd2.oid=" + cd2.oid);
				}

				createDataV3[] setDataV3_arr = null;
				if (cd != null && cd2 != null) {
					setDataV3_arr = new createDataV3[2];
					setDataV3_arr[0] = cd;
					setDataV3_arr[1] = cd2;
				} else if (cd != null) {
					setDataV3_arr = new createDataV3[1];
					setDataV3_arr[0] = cd;
				} else if (cd2 != null) {
					setDataV3_arr = new createDataV3[1];
					setDataV3_arr[0] = cd2;
				} else {
					return "-1|没有NTPServer数据";
				}
				logger.debug("setDataV3_arr.length="
						+ setDataV3_arr.length);
				setDataReturn[] dataList2 = SnmpGatherInterfaceV3.getInstance()
						.createOidNodeV3(setDataV3_arr, setDataV3_arr.length,
								(HashMap)devInfoMap);
				if (null == dataList2 || 0 == dataList2.length) {
					logger.warn("创建新NTPServer实例失败!");
					return "-1|创建新NTPServer实例失败!";
				}

				boolean ret2 = true;
				for (int i = 0; i < dataList2.length; i++) {
					setDataReturn data = dataList2[i];
					String rst = dataList2[0].result;
					ret2 = ret2 && Boolean.parseBoolean(rst.toLowerCase());
					logger.debug("[" + i + "]loopback_ip="+ data.loopback_ip);
					logger.debug("[" + i + "]result=" + data.result);
				}
				if (ret2) {
					return "1|启用成功";
				} else {
					return "-1|启用失败";
				}
			} else {// 禁用
				logger.debug("禁用");
				return "1|禁用成功";
			}
		}else{
			logger.warn("deviceId获取的设备信息为空");
			return "-1|无相关设备信息";
		}
	}

	
	/** DAO setter*/

	public void setDeviceDao(DeviceDAO deviceDao) {
		this.deviceDao = deviceDao;
	}


	public void setTimeNtpDao(TimeNtpDAO timeNtpDao) {
		this.timeNtpDao = timeNtpDao;
	}

}
