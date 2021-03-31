package com.linkage.liposs.buss.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import RemoteDB.FluxData;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.liposs.buss.util.FluxDataUtil;
import com.linkage.liposs.buss.util.PingTest;
import com.linkage.liposs.webtopo.snmpgather.InterfaceReadInfo;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;

/**
 * 
 * @author liuw
 * @version 1.0
 * @since 2007-8-10
 * @category 首页子页面逻辑处理类
 * 
 */
public class NetMaintenanceSub {
	private DataSource dao;

	private JdbcTemplate jt;

	private ArrayList list = null;

	private String device_IP = "";

	private String device_Model = "";

	private String port_num = "";

	private String iso_ver = "";

	private String device_name = "";

	private String cur_stat = "";

	private PingTest pingTest;

	private FluxDataUtil fluxDataUtil;

	private static Logger log = LoggerFactory.getLogger(NetMaintenanceSub.class);

	/**
	 * GET设备信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public NetMaintenanceSub getDeviceInfo(String deviceId) {
		Date currDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String today = formatter.format(currDate);
		java.sql.Date start = java.sql.Date.valueOf(today);
		String queryDate = start.toString().replaceAll("-", "");
		String tableID = deviceId + "_" + queryDate;
		jt = new JdbcTemplate(dao);
		List list;
		// 设备信息
		String deviceSQL = "select device_model_id as device_model,loopback_ip,device_name,os_version from tab_gw_device where device_id='"
				+ deviceId + "'";
		// 端口数量
		String portNumSQL = "select count(*) from flux_interfacedeviceport where device_id='"
				+ deviceId + "'";
		// 当前状态
		String currStatSQL = "select severity from tab_taskplan_data where device_id='"
				+ deviceId + "' and table_id='" + tableID + "'";
		PrepareSQL psql1 = new PrepareSQL(deviceSQL);
		PrepareSQL psql2 = new PrepareSQL(portNumSQL);
		PrepareSQL psql3 = new PrepareSQL(currStatSQL);
		list = jt.queryForList(psql1.getSQL());
		int portNum = jt.queryForInt(psql2.getSQL());
		int curStat = jt.queryForInt(psql3.getSQL());
		for (int i = 0; i < list.size(); i++) {
			this.setDevice_IP((String) ((Map) list.get(i)).get("loopback_ip"));
			this.setDevice_Model((String) ((Map) list.get(i))
					.get("device_model"));
			this.setIso_ver((String) ((Map) list.get(i)).get("os_version"));
			this
					.setDevice_name((String) ((Map) list.get(i))
							.get("device_name"));
			this.setPort_num(String.valueOf(portNum));
			this.setCur_stat(String.valueOf(curStat));
		}
		return this;
	}

	/**
	 * 根据不同的告警状态,获取告警统计信息 状态分为 告警日志：<=1,提示告警：2,一般告警：3,严重告警：>=4
	 * 
	 * @return HashMap 按照SQL查询顺序,把结果put到map里面
	 */
	public HashMap getAlamerInfo(String deviceName) {
		HashMap<Integer, Integer> alamMap = new HashMap<Integer, Integer>();
		String[] alamSQL = new String[4];
		alamSQL[0] = "select count(1) from tab_faultalarm_current where sourcename='"
				+ deviceName + "' and severity <=1";
		alamSQL[1] = "select count(1) from tab_faultalarm_current where sourcename='"
				+ deviceName + "' and severity =2";
		alamSQL[2] = "select count(1) from tab_faultalarm_current where sourcename='"
				+ deviceName + "' and severity =3";
		alamSQL[3] = "select count(1) from tab_faultalarm_current where sourcename='"
				+ deviceName + "' and severity >=4 ";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			alamSQL[0] = "select count(*) from tab_faultalarm_current where sourcename='"
					+ deviceName + "' and severity <=1";
			alamSQL[1] = "select count(*) from tab_faultalarm_current where sourcename='"
					+ deviceName + "' and severity =2";
			alamSQL[2] = "select count(*) from tab_faultalarm_current where sourcename='"
					+ deviceName + "' and severity =3";
			alamSQL[3] = "select count(*) from tab_faultalarm_current where sourcename='"
					+ deviceName + "' and severity >=4 ";
		}
		for (int i = 0; i < 4; i++) {
			PrepareSQL psql = new PrepareSQL(alamSQL[i]);
			alamMap.put(i, jt.queryForInt(psql.getSQL()));
		}
		return alamMap;
	}

	/**
	 * 通过设备名称查询对应的告警详细信息
	 * 
	 * @param deviceName
	 * @return ArrayList 将jt查询的结果进行处理,然后放入一个Map中,再将Map存放在ArrayList中,便于页面遍历结果显示
	 */
	public ArrayList getAlarmParticularInfo(String deviceName) {
		String particularSQL = "select sourceip,severity,displaystring,creator_time,currenttime,alarmcount from tab_faultalarm_current where sourcename='"
				+ deviceName + "'  order by severity desc";
		HashMap<String, String> infoMap = null;
		ArrayList<HashMap> reList = new ArrayList<HashMap>();
		list = new ArrayList();
		jt = new JdbcTemplate(dao);
		PrepareSQL psql = new PrepareSQL(particularSQL);
		list = (ArrayList) jt.queryForList(psql.getSQL());
		for (int i = 0; i < list.size(); i++) {
			infoMap = new HashMap<String, String>();
			int create = Integer.parseInt(String.valueOf(((Map) list.get(i))
					.get("creator_time")));
			int current = Integer.parseInt(String.valueOf(((Map) list.get(i))
					.get("currenttime")));
			String alarmSeverity = String.valueOf(((Map) list.get(i))
					.get("severity"));
			String alarmDesc = (String) ((Map) list.get(i))
					.get("displaystring");
			String alarmCnt = String.valueOf(((Map) list.get(i))
					.get("alarmcount"));
			String sourceiIp = String.valueOf(((Map) list.get(i))
					.get("sourceip"));
			infoMap.put("creator_time", this.getStringTime(create));
			infoMap.put("currenttime", this.getStringTime(current));
			infoMap.put("severity", alarmSeverity);
			infoMap.put("displaystring", alarmDesc);
			infoMap.put("alarmcount", alarmCnt);
			infoMap.put("sourceip", sourceiIp);
			reList.add(infoMap);
		}
		return reList;
	}

	/**
	 * 输入时间(int) 返回 月-日 时:分:秒
	 * 
	 * @param dataTime
	 * @return
	 */
	public String getStringTime(int dataTime) {
		String strTime = "";
		String[] tmpTime = StringUtils.secondToDateStr(dataTime);
		strTime = tmpTime[1] + "-" + tmpTime[2] + " " + tmpTime[3] + ":"
				+ tmpTime[4] + ":" + tmpTime[5];
		return strTime;
	}

	/**
	 * 获取系统信息 调用CORBA接口获取设备数据
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public HashMap getSysInfo(String deviceId, String user, String pwd) {
		list = new ArrayList();
		HashMap<Integer, String> sysMap = new HashMap<Integer, String>();
		String deviceInfo = "";
		InterfaceReadInfo di;
		try {
			di = (InterfaceReadInfo) Class.forName(
					"com.linkage.liposs.webtopo.snmpgather.ReadDeviceInfo")
					.newInstance();
			di.setDevice_ID(deviceId);
			di.setAccountInfo(user, pwd);
			list = di.getDeviceInfo();
		} catch (InstantiationException e) {
			log.error("CORBA实例异常");
		} catch (IllegalAccessException e) {
			log.error("CORBA数据异常");
		} catch (ClassNotFoundException e) {
			log.error("CORBA类没发现异常");
		}
		if (list.size() > 0) {
			for (int k = 0; k < list.size(); k++) {
				String[] str = (String[]) list.get(k);
				for (int z = 0; z < str.length; z++) {
					deviceInfo = str[1];
				}
				sysMap.put(k, deviceInfo);
			}
		}
		return sysMap;
	}

	/**
	 * 获取端口信息 调用CORBA接口获取端口数据
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public ArrayList getPortInfo(String deviceId, String user, String pwd) {
		list = new ArrayList();
		InterfaceReadInfo di;
		try {
			di = (InterfaceReadInfo) Class.forName(
					"com.linkage.liposs.webtopo.snmpgather.ReadDevicePort")
					.newInstance();
			di.setDevice_ID(deviceId);
			di.setAccountInfo(user, pwd);
			list = di.getDeviceInfo();
		} catch (InstantiationException e) {
			log.error("CORBA实例异常");
		} catch (IllegalAccessException e) {
			log.error("CORBA数据异常");
		} catch (ClassNotFoundException e) {
			log.error("CORBA类没发现异常");
		}
		return list;
	}

	/**
	 * 获取接口速率／错包／丢包
	 * 
	 * @return
	 */
	public ArrayList getPortSpeed(String deviceId, String user, String pwd) {
		String index;
		String ifdescr;
		String ifname;
		String ifnamedefined;
		String ifportip;
		String inoctets;
		String outoctets;
		String ifinerrorspps;
		String ifouterrorspps;
		String ifindiscardspps;
		String ifoutdiscardspps;
		ArrayList<String> tmpList;
		ArrayList<ArrayList> relist = new ArrayList<ArrayList>();
		fluxDataUtil = new FluxDataUtil();// --TODO 该类应该spring注入,目前没有
		FluxData[] fluxData = fluxDataUtil.getDeviceFlux(deviceId, user, pwd);
		for (int j = 0; j < fluxData.length; j++) {
			tmpList = new ArrayList<String>();
			index = fluxData[j].index;
			ifdescr = fluxData[j].ifdescr;
			ifname = fluxData[j].ifname;
			ifnamedefined = fluxData[j].ifnamedefined;
			ifportip = fluxData[j].ifportip;
			ifouterrorspps = fluxData[j].ifouterrorspps;
			ifindiscardspps = fluxData[j].ifindiscardspps;
			ifoutdiscardspps = fluxData[j].ifoutdiscardspps;

			ifinerrorspps = fluxData[j].ifinerrorspps;

			int avail_data = ifinerrorspps.length();

			if (avail_data > 30) {
				inoctets = "0";
				outoctets = "0";
			} else {
				inoctets = fluxData[j].ifinoctetsbps;

				if (inoctets.length() >= 6 && inoctets.length() <= 9) {
					inoctets = StringUtils.formatString((Double
							.parseDouble(inoctets) / (1000 * 1000)), 2)
							+ "M";
				} else if (inoctets.length() >= 10) {
					inoctets = StringUtils.formatString((Double
							.parseDouble(inoctets) / (1000 * 1000 * 1000)), 2)
							+ "G";
				} else {
					inoctets = fluxData[j].ifinoctetsbps;
				}

				outoctets = fluxData[j].ifoutoctetsbps;

				if (outoctets.length() >= 6 && outoctets.length() <= 9) {
					outoctets = StringUtils.formatString((Double
							.parseDouble(outoctets) / (1000 * 1000)), 2)
							+ "M";
				} else if (outoctets.length() >= 10) {

					outoctets = StringUtils.formatString((Double
							.parseDouble(outoctets) / (1000 * 1000 * 1000)), 2)
							+ "G";
				} else {
					outoctets = fluxData[j].ifoutoctetsbps;
				}
			}

			tmpList.add(index);
			tmpList.add(ifdescr);
			tmpList.add(ifname);
			tmpList.add(ifnamedefined);
			tmpList.add(ifportip);

			tmpList.add(inoctets);
			tmpList.add(outoctets);
			tmpList.add(ifindiscardspps);
			tmpList.add(ifoutdiscardspps);
			tmpList.add(ifinerrorspps);
			tmpList.add(ifouterrorspps);
			relist.add(tmpList);
		}
		return relist;
	}

	/**
	 * 获取网络延时
	 * 
	 * @return
	 */
	public ArrayList getNetDelay(String ip) {
		ArrayList<ArrayList> reList = new ArrayList<ArrayList>();
		ArrayList<String> tempList = null;
		list = new ArrayList();
		list = (ArrayList) pingTest.getParticularDataList(ip);
		ArrayList listResult = (ArrayList) list.get(0);
		HashMap map_pn2 = (HashMap) list.get(1);
		HashMap map_pn3 = (HashMap) list.get(2);
		HashMap map_pn4 = (HashMap) list.get(3);
		HashMap map_packetset = (HashMap) list.get(4);
		HashMap map_max_min = (HashMap) list.get(5);

		for (int i = 0; i < listResult.size(); i++) {
			tempList = new ArrayList<String>();
			String packetid = String.valueOf(((Map) listResult.get(i))
					.get("packetid"));
			String packetsize = String.valueOf(map_packetset.get(packetid));
			String sentpacketcount = String.valueOf(((Map) listResult.get(i))
					.get("sentpacketcount"));
			String recvdpacketcount = String.valueOf(((Map) listResult.get(i))
					.get("recvdpacketcount"));
			String pn1 = String.valueOf(((Map) listResult.get(i))
					.get("belowlimitpacketcount"));
			String pn2 = String.valueOf(map_pn2.get(packetid));
			String pn3 = String.valueOf(map_pn3.get(packetid));
			String pn4 = String.valueOf(map_pn4.get(packetid));
			String averageoftimedelay = String
					.valueOf(((Map) listResult.get(i))
							.get("averageoftimedelay"));
			String maxoftimedelay = String.valueOf(map_max_min
					.get(((Map) listResult.get(i)).get("packetid") + "max"));
			String minoftimedelay = String.valueOf(map_max_min
					.get(((Map) listResult.get(i)).get("packetid") + "min"));
			double dou_timedelaypar = (100 * Integer.parseInt(pn1) * 1.00)
					/ Integer.parseInt(sentpacketcount);

			double dou_losepacket = (100 * (Integer.parseInt(sentpacketcount) - Integer
					.parseInt(recvdpacketcount)) * 1.00)
					/ Integer.parseInt(sentpacketcount);

			if (pn1 == null || pn1.equals("null") || pn1.equals("")) {
				pn1 = "0";
			}
			if (pn2 == null || pn2.equals("null") || pn2.equals("")) {
				pn2 = "0";
			}
			if (pn3 == null || pn3.equals("null") || pn3.equals("")) {
				pn3 = "0";
			}
			if (pn4 == null || pn4.equals("null") || pn4.equals("")) {
				pn4 = "0";
			}
			tempList.add(packetsize);
			tempList.add(sentpacketcount);
			tempList.add(recvdpacketcount);
			tempList.add(pn1);
			tempList.add(pn2);
			tempList.add(pn3);
			tempList.add(pn4);
			tempList.add(StringUtils.formatString(Double
					.parseDouble(averageoftimedelay), 2));
			tempList.add(maxoftimedelay);
			tempList.add(minoftimedelay);
			tempList.add(StringUtils.formatString(dou_timedelaypar, 2) + "%");
			tempList.add(StringUtils.formatString(dou_losepacket, 2) + "%");
			reList.add(tempList);
		}
		return reList;
	}

	/**
	 * 获取当前日期属于一年的第几周
	 * 
	 * @return
	 */
	public int getCurrentWeek() {
		Calendar c = Calendar.getInstance();
		int currentWeek = c.get(Calendar.WEEK_OF_YEAR);
		return currentWeek;

	}

	public void setDao(DataSource dao) {
		this.dao = dao;
	}

	public String getCur_stat() {
		return cur_stat;
	}

	public void setCur_stat(String cur_stat) {
		this.cur_stat = cur_stat;
	}

	public String getDevice_IP() {
		return device_IP;
	}

	public void setDevice_IP(String device_IP) {
		this.device_IP = device_IP;
	}

	public String getDevice_Model() {
		return device_Model;
	}

	public void setDevice_Model(String device_Model) {
		this.device_Model = device_Model;
	}

	public String getIso_ver() {
		return iso_ver;
	}

	public void setIso_ver(String iso_ver) {
		this.iso_ver = iso_ver;
	}

	public String getPort_num() {
		return port_num;
	}

	public void setPort_num(String port_num) {
		this.port_num = port_num;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public PingTest getPingTest() {
		return pingTest;
	}

	public void setPingTest(PingTest pingTest) {
		this.pingTest = pingTest;
	}

	public FluxDataUtil getFluxDataUtil() {
		return fluxDataUtil;
	}

	public void setFluxDataUtil(FluxDataUtil fluxDataUtil) {
		this.fluxDataUtil = fluxDataUtil;
	}

}
