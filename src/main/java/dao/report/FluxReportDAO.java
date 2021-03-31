package dao.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;

/**
 * 设备流量统计功能
 * 
 * @author czm
 * @since 2008-07-14
 * @version 1.0
 * 
 */
@SuppressWarnings("unchecked")
public class FluxReportDAO {
	// jdbc模板
	private JdbcTemplate jt;
	// 设备名称
	private Map<String, String> deviceName = new HashMap<String, String>();
	// 设备端口
	private Map<String, String> portName = new HashMap<String, String>();

	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}

	/**
	 * 查询设备厂商
	 * 
	 * @return
	 */
	public String getVendorList() {
		String ret = "<select name='vendor_id' class='bk' onchange=showChild('device_model')>";

		String sql = "select * from tab_vendor";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select vendor_id, vendor_add from tab_vendor";
		}

		PrepareSQL psql = new PrepareSQL(sql);
		List list = jt.queryForList(psql.getSQL());
		if (list != null && list.size() > 0) {
			ret += "<option value='-1'>==请选择==</option>";
			Map map;
			for (int i = 0; i < list.size(); i++) {
				map = (Map) list.get(i);
				ret += "<option value='" + map.get("vendor_id") + "'>=="
						+ map.get("vendor_add") + "==</option>";
			}
		} else {
			ret += "<option value='-1'>==没有可选择的厂商==</option>";
		}
		ret += "</select>";
		return ret;
	}

	/**
	 * 查询设备型号
	 * 
	 * @param vendor_id
	 * @return
	 */
	// public String getModelList(String vendor_id)
	// {
	// String ret = "<select name='devicetype_id' class='bk'
	// onchange=showChild('device')>";
	// List list = jt.queryForList("select * from tab_devicetype_info where
	// oui='"
	// + vendor_id + "'");
	// if (list != null && list.size() > 0)
	// {
	// ret += "<option value='-1'>==请选择==</option>";
	// Map map;
	// for (int i = 0; i < list.size(); i++)
	// {
	// map = (Map) list.get(i);
	// ret += "<option value='" + map.get("devicetype_id") + "'>=="
	// + map.get("device_model") + "("
	// + map.get("softwareversion") + ")" + "==</option>";
	// }
	// }
	// else
	// {
	// ret += "<option value='-1'>==没有可选择的设备型号==</option>";
	// }
	// ret += "</select>";
	// return ret;
	// }
	public String getModelList(String vendor_id) {
		String ret = "<select name='device_model_id' class='bk' onchange=showChild('device')>";
		PrepareSQL psql = new PrepareSQL("select a.device_model_id, a.device_model from gw_device_model a where a.vendor_id='"
				+ vendor_id + "'");
		psql.getSQL();
		List list = jt
				.queryForList("select a.device_model_id, a.device_model from gw_device_model a where a.vendor_id='"
						+ vendor_id + "'");
		if (list != null && list.size() > 0) {
			ret += "<option value='-1'>==请选择==</option>";
			Map map;
			for (int i = 0; i < list.size(); i++) {
				map = (Map) list.get(i);
				ret += "<option value='" + map.get("device_model_id") + "'>=="
						+ map.get("device_model") + "==</option>";
			}
		} else {
			ret += "<option value='-1'>==没有可选择的设备型号==</option>";
		}
		ret += "</select>";
		return ret;
	}

	/**
	 * 查询设备信息
	 * 
	 * @param devicetype_id
	 * @return
	 */
	public String getDeviceList(String vendor_id, String device_model_id) {
		String ret = "<select name='device_id' class='bk' onchange=showChild('port')>";
		String sql = "select * from tab_gw_device where vendor_id='" + vendor_id + "' and device_model_id='" + device_model_id + "'";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select device_id, device_name, loopback_ip from tab_gw_device where vendor_id='" + vendor_id + "' and device_model_id='" + device_model_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List list = jt.queryForList(sql);
		if (list != null && list.size() > 0) {
			ret += "<option value='-1'>==请选择==</option>";
			Map map;
			for (int i = 0; i < list.size(); i++) {
				map = (Map) list.get(i);
				ret += "<option value='" + map.get("device_id") + "'>=="
						+ map.get("device_name") + "(" + map.get("loopback_ip")
						+ ")" + "==</option>";
			}
		} else {
			ret += "<option value='-1'>==没有可选择的设备==</option>";
		}
		ret += "</select>";
		return ret;
	}

	/**
	 * 先初始化获得端口对应的名称 对于指定设备
	 * 
	 * @param device_id
	 * @return
	 */
	private Map<String, String> getDevicePortDescMap(String device_id) {
		String portDescSQL = "select port_name, port_desc from tab_port_name_map "
				+ "where device_model_id=(select device_model_id from tab_gw_device "
				+ "where device_id='" + device_id + "')";
		Map<String, String> devicePortDescMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(portDescSQL);
		psql.getSQL();
		List list = jt.queryForList(portDescSQL);
		if (list != null && list.size() > 0) {
			Map map;
			String port_name = "";
			String port_desc = "";
			for (int i = 0; i < list.size(); i++) {
				map = (Map) list.get(i);
				port_name = (String) map.get("port_name");
				port_desc = (String) map.get("port_desc");

				devicePortDescMap.put(port_name, port_desc);
			}
		}
		return devicePortDescMap;
	}

	/**
	 * 查询指定设备的端口信息
	 * 
	 * @param device_id
	 * @return
	 */
	public String getPortInfo(String device_id) {
		// 先初始化获得端口对应的名称
		Map<String, String> devicePortDescMap = getDevicePortDescMap(device_id);
		String ret = "";

		String sql = "select * from flux_interfacedeviceport where device_id='" + device_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select ifindex, ifname, ifdescr, ifnamedefined, port_info, getway " +
					"from flux_interfacedeviceport where device_id='" + device_id + "'";
		}

		// 查询设备端口信息
		PrepareSQL psql = new PrepareSQL(sql);
		List list = jt.queryForList(psql.getSQL());
		if (list != null && list.size() > 0) {
			Map map;
			String ifindex = "";
			String ifname = "";
			String ifdescr = "";
			String ifnamedefined = "";
			String portinfo = "";
			String getway = "";
			String key = "";
			String view = "";
			for (int i = 0; i < list.size(); i++) {
				map = (Map) list.get(i);
				ifindex = (String) map.get("ifindex");
				ifname = (String) map.get("ifname");
				ifdescr = (String) map.get("ifdescr");
				ifnamedefined = (String) map.get("ifnamedefined");
				portinfo = (String) map.get("port_info");
				getway = map.get("getway").toString();
				// 生成返回信息
				key = device_id + "##" + portinfo + "##" + getway;

				if (null != devicePortDescMap.get(ifdescr)
						&& !"".equals(devicePortDescMap.get(ifdescr))) {
					view = ifindex + " | " + devicePortDescMap.get(ifdescr)
							+ " | " + ifnamedefined + " | " + ifdescr + " | "
							+ ifname;
				} else {
					view = ifindex + " | " + ifnamedefined + " | " + ifdescr
							+ " | " + ifname;
				}
				ret += "<input type='checkbox' name='devicePort' value='" + key
						+ "' view='" + view + "'>" + view + "<br>";
			}
		} else {
			ret += "没有可以选择的端口 (该设备没有进行流量配置，或没有配置成功)";
		}
		return ret;
	}

	/**
	 * 统计设备流量（日报表）
	 * 
	 * @param day
	 *            统计时间
	 * @param devicePortList
	 *            统计的设备端口，形式如："device_id##portinfo##getway"，多个以分号分割
	 * @param kind
	 *            统计类型
	 * @return
	 */
	public String getFluxReportInfo(String day, String devicePortList,
			String devicePortListInfo, String[] kind) {
		// 初始化信息
		initNameMap(devicePortList, devicePortListInfo);
		// 标题信息
		Map<String, String> titleMap = new HashMap<String, String>();
		titleMap.put("ifinoctetsbps", "流入速率/bps");
		titleMap.put("ifinerrorspps", "流入错包率");
		titleMap.put("Ifindiscardspps", "流入丢包率");
		titleMap.put("Ifinucastpktspps", "流入广播包速率");
		titleMap.put("ifoutoctetsbps", "流出速率/bps");
		titleMap.put("ifouterrorspps", "流出错包率");
		titleMap.put("Ifoutdiscardspps", "流出丢包率");
		titleMap.put("Ifoutucastpktspps", "流出广播包速率");
		// 记录统计设备的端口数
		Map<String, String> portNumMap = new HashMap<String, String>();
		// 返回信息
		String html = "";
		// 统计时间
		DateTimeUtil dt = new DateTimeUtil(day);
		long start = dt.getLongTime();
		long end = start + 86400;
		// 表名
		String tableName = "flux_day_stat_" + dt.getYear() + "_"
				+ dt.getMonth();
		// 解析设备端口信息，生成查询条件语句
		String[] where_str;
		String num = "";
		if (!"".equals(devicePortList)) {
			String[] devicePort = devicePortList.split(";");
			where_str = new String[devicePort.length];
			for (int i = 0; i < devicePort.length; i++) {
				String[] port = devicePort[i].split("##");
				if (port.length == 3) {
					where_str[i] = " where device_id='" + port[0]
							+ "' and port_info='" + port[1] + "' and getway="
							+ port[2] + " and collecttime>" + start
							+ " and collecttime<" + end + " ";
					// 记录每个设备统计的端口数
					num = portNumMap.get(port[0]);
					if (num != null && !"".equals(num)) {
						portNumMap.put(port[0], String.valueOf(Integer
								.parseInt(num) + 1));
					} else {
						portNumMap.put(port[0], "1");
					}
				} else {
					where_str[i] = "";
				}
			}
		} else {
			return "没有选择统计的设备端口！";
		}
		// 组成sql语句
		String tmp = "";
		for (int j = 0; j < where_str.length; j++) {
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				if ("".equals(tmp)) {
					tmp += " select device_id, port_info, getway, ifinoctetsbps, ifinerrorspps, Ifindiscardspps, Ifinucastpktspps, " +
							" ifoutoctetsbps, ifouterrorspps, Ifoutdiscardspps, Ifoutucastpktspps from " + tableName + where_str[j];
				} else {
					tmp += " union all  select device_id, port_info, getway, ifinoctetsbps, ifinerrorspps, Ifindiscardspps, Ifinucastpktspps, " +
							" ifoutoctetsbps, ifouterrorspps, Ifoutdiscardspps, Ifoutucastpktspps from " + tableName + where_str[j];
				}
			}
			else {
				if ("".equals(tmp)) {
					tmp += " select * from " + tableName + where_str[j];
				} else {
					tmp += " union all  select * from " + tableName + where_str[j];
				}
			}
		}
		String sql = "select * from (" + tmp + ") fluxdata order by device_id";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select device_id, port_info, getway, ifinoctetsbps, ifinerrorspps, Ifindiscardspps, Ifinucastpktspps, " +
					" ifoutoctetsbps, ifouterrorspps, Ifoutdiscardspps, Ifoutucastpktspps from (" + tmp + ") fluxdata order by device_id";
		}
		// 解析统计类型
		String kindStr = "#";
		String titleStr = "";
		if (kind.length > 0) {
			for (int i = 0; i < kind.length; i++) {
				kindStr += kind[i] + "#";
				titleStr += "<th>" + titleMap.get(kind[i]) + "</th>";
			}
		} else {
			return "没有选择统计类型！";
		}
		// 生成表
		html += "<table border=0 cellspacing=1 cellpadding=2 width='100%' align='center' bgcolor='#000000'>";
		// 生成表格标题
		html += "<tr bgcolor='#FFFFFF'><th>设备名称</th><th>端口名称</th>" + titleStr
				+ "</tr>";
		// 查询数据，生成表格
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List list = jt.queryForList(sql);
		Map map = null;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map = (Map) list.get(i);
				html += "<tr bgcolor='#FFFFFF'>"
						+ getDataline(map, kind, portNumMap) + "</tr>";
			}
		} else {
			html += "<tr bgcolor='#FFFFFF'><td colspan='10'>没有查询到统计数据</td></tr>";
		}
		html += "</table>";
		return html;
	}

	/**
	 * 生成行数据
	 * 
	 * @param map
	 * @param kind
	 * @return
	 */
	private String getDataline(Map map, String[] kind,
			Map<String, String> portNumMap) {
		String html = "";
		// 生成设备名称
		String device_id = (String) map.get("device_id");
		String portnum = portNumMap.get(device_id);
		if (portnum != null && !"".equals(portnum)) {
			html += "<td rowspan='" + portnum + "'>"
					+ deviceName.get(device_id) + "</td>";
			// 从map中移除，下次就不写设备列
			portNumMap.remove(device_id);
		}
		// 端口名称
		String portinfo = (String) map.get("port_info");
		String getway = map.get("getway").toString();
		String key = device_id + "##" + portinfo + "##" + getway;
		html += "<td>" + portName.get(key) + "</td>";
		// 生成数据
		Object tmp;
		for (int i = 0; i < kind.length; i++) {
			tmp = map.get(kind[i]);
			if (tmp != null) {
				html += "<td>" + tmp + "</td>";
			} else {
				html += "<td>0</td>";
			}
		}
		return html;
	}

	/**
	 * 初始化名称数据
	 * 
	 * @param devicePortList，形式如："device_id##portinfo##getway"，多个以分号分割
	 * @param devicePortListInfo，形式如："==device_name==port_name"，多个以分号分割
	 */
	private void initNameMap(String devicePortList, String devicePortListInfo) {
		// 解析名称
		String[] valueInfo = devicePortList.split(";");
		String[] nameInfo = devicePortListInfo.split(";");
		for (int i = 0; i < valueInfo.length; i++) {
			String[] valueArr = valueInfo[i].split("##");
			String[] nameArr = new String[3];
			// 解析名称
			if (nameInfo.length > i) {
				nameArr = nameInfo[i].split("==");
				if (nameArr.length < 3) {
					nameArr = new String[3];
				}
			}
			// 设备名称
			deviceName.put(valueArr[0], nameArr[1]);
			// 端口名称
			portName.put(valueInfo[i], nameArr[2]);
		}
	}
}
