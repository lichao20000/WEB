package com.linkage.module.liposs.performance.bio.snmpGather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.liposs.performance.bio.Flux_Map_Instance;
import com.linkage.module.liposs.performance.dao.ConfigFluxDao;

/**
 * 初始化OID
 * 
 * @author Administrator
 */
public class FluxConfigInit {
	private static Logger log = LoggerFactory.getLogger(FluxConfigInit.class);
	private ConfigFluxDao cfd;//
	// 装载flux_oid_map中的oid
	private HashMap<String, HashMap<String, HashMap<String, ArrayList<PortJudgeAttr>>>> deviceOIDMap = new HashMap<String, HashMap<String, HashMap<String, ArrayList<PortJudgeAttr>>>>();

	// deviceOIDMap的锁【第一次载入时，同步】
	// private Object deviceOIDMapObject = new Object();

	/**
	 * 处理Map
	 * 
	 * @param map
	 * @param str
	 * @return
	 */
	private Map<String, Integer> dualMap(Map<String, Integer> map, String str) {
		log.debug("dualMap({},{})", new Object[] { map, str });

		if (map.get(str) == null) {
			map.put(str, 1);
		} else {
			map.put(str, map.get(str) + 1);
		}
		return map;
	}

	/**
	 * 获取某个设备的端口信息
	 * 
	 * @param device_id
	 *            设备id
	 * @param serial
	 *            设备型号id
	 * @return
	 */
	public List<FluxPortInfo> getDevicePortList(String device_id, String serial) {
		log.debug("getDevicePortList({},{})",
				new Object[] { device_id, serial });

		// 采集端口详细信息，使用v1版本的32位计数器来采（据说采端口描述等信息，随便用什么版本采都一样，要验证）
		List<PortJudgeAttr> oidList = getOIDList(serial, "1", "32", "2");
		List<FluxPortInfo> portList = ReadFluxConfigPortInfo.getDeviceInfo(
				device_id, oidList);
		portList = getWayofBaseInfo(portList);

		// clear
		oidList = null;
		return portList;

	}

	/**
	 * 获取采集方式getway
	 * 
	 * @param list
	 * @return
	 */
	public List<FluxPortInfo> getWayofBaseInfo(List<FluxPortInfo> list) {
		log.debug("getWayofBaseInfo({})", new Object[] { list });

		Map<String, Integer> ipMap = new HashMap<String, Integer>();// IPMap
		Map<String, Integer> nameMap = new HashMap<String, Integer>();// Name
		Map<String, Integer> descMap = new HashMap<String, Integer>();// Descr
		Map<String, Integer> ndMap = new HashMap<String, Integer>();// NameDefined
		String tmp;
		for (FluxPortInfo fpi : list) {
			// IP
			tmp = fpi.getIfportip();
			if (checkIP(tmp, true)) {
				ipMap = dualMap(ipMap, tmp);
			}
			// Name
			tmp = fpi.getIfname();
			if (checkString(tmp)) {
				nameMap = dualMap(nameMap, tmp);
			}
			// Descr
			tmp = fpi.getIfdescr();
			if (checkString(tmp)) {
				descMap = dualMap(descMap, tmp);
			}
			// NameDefined
			tmp = fpi.getIfnamedefined();
			if (checkString(tmp)) {
				ndMap = dualMap(ndMap, tmp);
			}
		}

		for (FluxPortInfo fpi : list) {

			// DESC
			tmp = fpi.getIfdescr();
			if (checkString(tmp)) {
				if (descMap.get(tmp) == 1) {
					fpi.setGetway(2);
					fpi.setPort_info(fpi.getIfdescr());
					continue;
				}
			}
			// IP
			tmp = fpi.getIfportip();
			if (checkIP(tmp, true)) {
				if (ipMap.get(tmp) == 1) {
					fpi.setGetway(5);
					fpi.setPort_info(fpi.getIfportip());
					continue;
				}
			}

			// Name
			tmp = fpi.getIfname();
			if (checkString(tmp)) {
				if (nameMap.get(tmp) == 1) {
					fpi.setGetway(3);
					fpi.setPort_info(fpi.getIfname());
					continue;
				}
			}
			// NameDefined
			tmp = fpi.getIfnamedefined();
			if (checkString(tmp)) {
				if (ndMap.get(tmp) == 1) {
					fpi.setGetway(4);
					fpi.setPort_info(fpi.getIfnamedefined());
					continue;
				}
			}

			// 以端口索引为准
			fpi.setGetway(1);
			fpi.setPort_info(fpi.getIfindex());
		}
		// clear
		ipMap = null;
		nameMap = null;
		descMap = null;
		ndMap = null;
		return list;
	}

	/**
	 * @param serial
	 * @param snmpversion
	 * @param counternum
	 * @param oidtype
	 * @return
	 */
	public List<PortJudgeAttr> getOIDList(String serial, String snmpversion,
			String counternum, String oidtype) {
		log.debug("getOIDList({},{},{},{})", new Object[] { serial,
				snmpversion, counternum, oidtype });

		InitOIDMap();
		HashMap<String, HashMap<String, ArrayList<PortJudgeAttr>>> tempSerialMap = null;
		ArrayList<PortJudgeAttr> list = new ArrayList<PortJudgeAttr>();
		HashMap<String, ArrayList<PortJudgeAttr>> tempVersionAndCounterMap = null;
		// 有这个设备型号的OID
		if ((null != deviceOIDMap.get(serial))
				&& (0 != (deviceOIDMap.get(serial)).size())) {
			tempSerialMap = deviceOIDMap.get(serial);
			// 有这个版本计数器的OID
			if (null != tempSerialMap.get(snmpversion + "_" + counternum)) {
				tempVersionAndCounterMap = tempSerialMap.get(snmpversion + "_"
						+ counternum);
				// 有这个类型的OID
				if (null != tempVersionAndCounterMap.get(oidtype)) {
					list = tempVersionAndCounterMap.get(oidtype);
				}
			}
		}
		// 没有这个设备型号的OID，取用默认的OID(即serial为0)
		else if (!"0".equals(serial)) {
			serial = "0";
			// 获取默认的oid
			tempSerialMap = deviceOIDMap.get(serial);
			// 有这个版本计数器的OID
			if (null != tempSerialMap.get(snmpversion + "_" + counternum)) {
				tempVersionAndCounterMap = tempSerialMap.get(snmpversion + "_"
						+ counternum);
				// 有这个类型的OID
				if (null != tempVersionAndCounterMap.get(oidtype)) {
					list = tempVersionAndCounterMap.get(oidtype);
				}
			}
		}

		// clear
		tempVersionAndCounterMap = null;
		tempSerialMap = null;
		return list;
	}

	private void InitOIDMap() {
		log.debug("InitOIDMap()");

		if (0 == deviceOIDMap.size()) {
			loadOID();
		}
	}

	/**
	 * 装载系统OID
	 */
	@SuppressWarnings("unchecked")
	private void loadOID() {
		log.debug("loadOID()");
		List<Map> list = cfd.getOIDMapList();
		// 存放相同设备型号的不同版本、计数器的oid容器
		HashMap<String, HashMap<String, ArrayList<PortJudgeAttr>>> deviceModelOID = null;
		// 存放相同设备型号、相同版本、计数器的oid容器
		HashMap<String, ArrayList<PortJudgeAttr>> deviceModelVersionCounterOID = null;
		ArrayList<PortJudgeAttr> typeOID = null;
		PortJudgeAttr pja = null;

		String model = "";
		String snmpversion = "";
		String counternum = "";
		String oid_type = "";
		String oid_desc = "";
		String oid_name = "";
		String oid_order = "";
		String oid_value = "";
		String temp_model = "";
		String temp_snmpversion = "";
		String temp_counternum = "";
		for (Map m : list) {
			model = (String) m.get("model");
			snmpversion = String.valueOf(((Number) m.get("snmpversion"))
					.intValue());
			counternum = String.valueOf(((Number) m.get("counternum"))
					.intValue());
			oid_type = String.valueOf(((Number) m.get("oid_type")).intValue());
			oid_desc = (String) m.get("oid_desc");
			oid_name = (String) m.get("oid_name");
			oid_order = String
					.valueOf(((Number) m.get("oid_order")).intValue());
			oid_value = (String) m.get("oid_value");
			pja = new PortJudgeAttr();
			pja.setName(oid_name);
			pja.setDesc(oid_desc);
			pja.setValue(oid_value);
			pja.setOrder(oid_order);
			pja.setModel(model);
			// 不相同的serial
			if (!temp_model.equals(model)) {
				temp_model = model;
				temp_snmpversion = snmpversion;
				temp_counternum = counternum;
				deviceModelOID = new HashMap<String, HashMap<String, ArrayList<PortJudgeAttr>>>();
				deviceModelVersionCounterOID = new HashMap<String, ArrayList<PortJudgeAttr>>();
				typeOID = new ArrayList<PortJudgeAttr>();
				typeOID.add(pja);
				deviceModelVersionCounterOID.put(oid_type, typeOID);
				deviceModelOID.put(snmpversion + "_" + counternum,
						deviceModelVersionCounterOID);
				deviceOIDMap.put(temp_model, deviceModelOID);
			}
			// 相同的设备serial，不同的版本或计数器
			else if (temp_model.equals(model)
					&& (!temp_snmpversion.equals(snmpversion) || (!temp_counternum
							.equals(counternum)))) {
				// 版本
				if (!temp_snmpversion.equals(snmpversion)) {
					temp_snmpversion = snmpversion;
				}

				// 计数器
				if (!temp_counternum.equals(counternum)) {
					temp_counternum = counternum;
				}

				deviceModelOID = deviceOIDMap.get(temp_model);
				deviceModelVersionCounterOID = new HashMap<String, ArrayList<PortJudgeAttr>>();
				typeOID = new ArrayList<PortJudgeAttr>();
				typeOID.add(pja);
				deviceModelVersionCounterOID.put(oid_type, typeOID);
				deviceModelOID.put(snmpversion + "_" + counternum,
						deviceModelVersionCounterOID);
				deviceOIDMap.put(temp_model, deviceModelOID);
			}
			// 相同的设备serial，相同的版本，相同计数器的情况
			else if (temp_model.equals(model)
					&& temp_snmpversion.equals(snmpversion)
					&& temp_counternum.equals(counternum)) {
				deviceModelOID = deviceOIDMap.get(temp_model);
				deviceModelVersionCounterOID = deviceModelOID.get(snmpversion
						+ "_" + counternum);
				// 包含这个类型
				if (deviceModelVersionCounterOID.containsKey(oid_type)) {
					typeOID = deviceModelVersionCounterOID.get(oid_type);
				} else {
					typeOID = new ArrayList<PortJudgeAttr>();
				}
				typeOID.add(pja);
				deviceModelVersionCounterOID.put(oid_type, typeOID);
				deviceModelOID.put(snmpversion + "_" + counternum,
						deviceModelVersionCounterOID);
				deviceOIDMap.put(temp_model, deviceModelOID);
			}
		}
	}

	/**
	 * 过滤设备端口
	 * 
	 * @param device_id
	 * @param portList
	 * @return
	 */
	public List<FluxPortInfo> filterPort(String device_id,
			List<FluxPortInfo> portList) {
		log.warn("[{}] begin filterport 端口数：{}", device_id, portList.size());
		List<FluxPortInfo> returnList = new ArrayList<FluxPortInfo>();
		// 装载这个设备对应的过滤规则表
		Map<String, List<String>> filterRule = cfd.initFilterRule(device_id);
		log.warn("过滤规则size:{}", filterRule.size());
		for (FluxPortInfo fpi : portList) {
			// 端口没有被过滤掉
			if (!isFilter(fpi, filterRule)) {
				returnList.add(fpi);
			}
		}
		// clear
		portList = null;
		log.warn("[{}] end filterport 端口数：", device_id, returnList.size());
		return returnList;
	}

	/**
	 * 判断是否能过滤掉
	 * 
	 * @param fpi
	 * @param filterRule
	 * @return
	 */
	private boolean isFilter(FluxPortInfo fpi,
			Map<String, List<String>> filterRule) {
		log.debug("isFilter({},{})", new Object[] { fpi, filterRule });

		boolean result = false;
		String value = "";
		// 根据端口索引来过滤
		value = fpi.getIfindex();
		if (isFilter(value, 4, filterRule)) {
			return true;
		}

		// 根据端口别名来过滤
		value = fpi.getIfnamedefined();
		if (isFilter(value, 3, filterRule)) {
			return true;
		}

		// 根据端口描述来过滤
		value = fpi.getIfdescr();
		if (isFilter(value, 2, filterRule)) {
			return true;
		}

		// 根据端口名字来过滤
		value = fpi.getIfname();
		if (isFilter(value, 1, filterRule)) {
			return true;
		}

		return result;
	}

	/**
	 * 判断这个设备这个端口类型是否能过滤
	 * 
	 * @param device_id
	 * @param iftype
	 * @return
	 */
	public boolean isFilterPortByPortType(String device_id, int iftype) {
		log.debug("isFilterPortByPortType({},{})", new Object[] { device_id,
				iftype });

		return cfd.isFilterPortByPortType(device_id, iftype);
	}

	/**
	 * 判断指定值是否能匹配过滤规则指定过滤类型
	 * 
	 * @param value
	 *            指定值
	 * @param type
	 *            指定过滤类型 1:ifname 2:ifdescr 3:ifnamedefined 4:ifindex 5:iftype
	 * @param filterRule
	 *            过滤规则
	 * @return
	 */
	private boolean isFilter(String value, int type,
			Map<String, List<String>> filterRule) {
		log.debug("isFilter({},{},{})",
				new Object[] { value, type, filterRule });

		boolean result = false;
		if (null == filterRule || filterRule.isEmpty()) {
			return result;
		}
		List<String> valueList = filterRule.get(type);
		if (null == valueList || valueList.isEmpty()) {
			return result;
		}
		for (String tempValue : valueList) {
			switch (type) {
			case 5:
				if (tempValue.equalsIgnoreCase(value.trim())) {
					result = true;
				}
				break;
			case 4:
				String[] arr = new String[2];
				arr = tempValue.split(",");
				long start = 0;
				long end = 0;
				long ifindex = 0;
				try {
					start = Long.parseLong(arr[0]);
				} catch (NumberFormatException ex) {
				}

				try {
					end = Long.parseLong(arr[1]);
				} catch (NumberFormatException ex1) {
				}

				try {
					ifindex = Long.parseLong(value);
				} catch (NumberFormatException ex2) {
				}

				if (ifindex >= start && ifindex <= end) {
					result = true;
				}

				break;

			case 1:

			case 2:

			case 3:

			default:
				if (value.toLowerCase().indexOf(tempValue.toLowerCase()) > -1) {
					result = true;
				}

				break;
			}
		}

		return result;
	}

	/**
	 * 检查整数
	 * 
	 * @param num
	 * @return
	 */
	public boolean checkInt(String num) {
		log.debug("checkInt({})", new Object[] { num });

		try {
			Integer.parseInt(num);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * CheckIP
	 * 
	 * @param ip
	 *            :需要检查的IP
	 * @param flg
	 *            ：是否需要去除0.0.0.0
	 * @return
	 */
	public boolean checkIP(String ip, boolean flg) {
		log.debug("checkIP({})", new Object[] { ip, flg });

		// IP为空
		if (ip == null || "null".equalsIgnoreCase(ip) || "".equals(ip)) {
			return false;
		}
		// 去除0.0.0.0
		String[] ip_tmp = ip.split("\\.");
		int n = ip_tmp.length;
		if (flg) {
			if (ip.trim().equals("0.0.0.0")) {
				return false;
			}
		}
		if (n != 4) {
			return false;
		}
		for (int i = 0; i < n; i++) {
			if (!checkInt(ip_tmp[i])) {
				return false;
			} else if (Integer.parseInt(ip_tmp[i]) < 0
					|| Integer.parseInt(ip_tmp[i]) > 255) {
				return false;
			}
		}
		return true;
	}

	/**
	 * checkString
	 * 
	 * @param str
	 * @return
	 */
	public boolean checkString(String str) {
		log.debug("checkString({})", new Object[] { str });

		if (str == null || "".equals(str.trim())
				|| "null".equalsIgnoreCase(str)) {
			return false;
		}
		if ("NODATA".equalsIgnoreCase(str) || "error".equalsIgnoreCase(str)) {
			return false;
		}
		return true;
	}

	/**
	 * 根据flux_oid_map表中的oid_type为1的性能oid来确定采集参数
	 * 
	 * @param perfList
	 * @return
	 */
	public String getGetarg(List<PortJudgeAttr> perfList) {
		log.debug("getGetarg({})", new Object[] { perfList });

		String getarg = "0000000000000";
		for (PortJudgeAttr oidObj : perfList) {
			int num = Integer.parseInt(oidObj.getOrder());
			getarg = getarg.substring(0, num) + "1" + getarg.substring(num + 1);
		}

		return getarg;
	}

	/**
	 * 转换实际带宽
	 * 
	 * @param value
	 * @return
	 */
	public long ParseRealSpeed(long value) {
		log.debug("ParseRealSpeed({})", new Object[] { value });

		long r_value = 0;
		if (value > 0) {
			int TriZeroCount = 0;
			r_value = value;
			if (r_value % 1000 == 0) {
				TriZeroCount++;
				r_value /= 1000;
				// 处理剩余的最多12个0
				for (int j = 0; j < 4; j++) {
					if ((r_value % 1000) == 0) {
						TriZeroCount++;
						r_value /= 1000;
					} else {
						break;
					}
				}
			}
			for (int i = 0; i < TriZeroCount; i++)
				r_value *= 1000;
		}
		return r_value;
	}

	/**
	 * 根据告警参数统计设置了多少个阈值
	 * 
	 * @param alarmInstance
	 * @return
	 */
	public int getConfignumByAlarm(Flux_Map_Instance alarmInstance) {
		log.debug("getConfignumByAlarm({})", new Object[] { alarmInstance });

		int countNum = 0;
		// 端口流入带宽利用率阈值一操作符
		if (alarmInstance.getIfinoct_maxtype() != 0) {
			countNum++;
		}
		// 端口流出带宽利用率阈值一操作符
		if (alarmInstance.getIfoutoct_maxtype() != 0) {
			countNum++;
		}
		// 端口流入丢包率阈值
		if (alarmInstance.getIfindiscardspps_max() >= 0) {
			countNum++;
		}
		// 端口流出丢包率阈值
		if (alarmInstance.getIfoutdiscardspps_max() >= 0) {
			countNum++;
		}
		// 端口流入错包率阈值
		if (alarmInstance.getIfinerrorspps_max() >= 0) {
			countNum++;
		}
		// 端口流出错包率阈值
		if (alarmInstance.getIfouterrorspps_max() >= 0) {
			countNum++;
		}
		// 端口流入利用率阈值二操作符
		if (alarmInstance.getIfinoct_mintype() != 0) {
			countNum++;
		}
		// 端口流出利用率阈值二操作符
		if (alarmInstance.getIfoutoct_mintype() != 0) {
			countNum++;
		}
		// 动态阈值一操作符
		if (alarmInstance.getOvermax() != 0) {
			countNum++;
		}
		// 动态阈值二操作符
		if (alarmInstance.getOvermin() != 0) {
			countNum++;
		}
		// 判断是否配置流入突变告警操作
		if (alarmInstance.getIntbflag() != 0) {
			countNum++;
		}
		// 是否配置流出突变告警操作
		if (alarmInstance.getOuttbflag() != 0) {
			countNum++;
		}
		return countNum;
	}

	public ConfigFluxDao getCfd() {
		return cfd;
	}

	public void setCfd(ConfigFluxDao cfd) {
		this.cfd = cfd;
	}

}
