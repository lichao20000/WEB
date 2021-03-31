package com.linkage.litms.paramConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * dao for wan config.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com) Gongsj (gongsj@lianchuang.com)
 * @version 2.0, Dec 23, 2008
 * @see
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class ParamInfoDAO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ParamInfoDAO.class);

	/**
	 * 取得IOR
	 * 
	 * @param gather_id
	 * @return ior
	 */
	public static String getIOR(String gather_id) {
		logger.debug("getIOR({})", gather_id);

		String ior = null;
		String getIorSQL = "select ior from tab_ior where object_name='ACS_"
				+ gather_id + "' and object_poa='ACS_Poa_" + gather_id + "'";
		PrepareSQL psql = new PrepareSQL(getIorSQL);
		psql.getSQL();
		Map<String, String> map = DataSetBean.getRecord(getIorSQL);
		if (null != map) {
			ior = (String) map.get("ior");
		}

		return ior;
	}

	/**
	 * 获取WAN
	 * 
	 * @param device_id
	 * @return
	 */
	public WanObj[] getWAN(String device_id) {
		logger.debug("WanObj[] getWAN({})", device_id);

		WanObj[] wanObj = null;

		String sql = "select * from gw_wan where device_id='" + device_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select wan_id, gather_time, access_type, wan_conn_num from gw_wan where device_id='" + device_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		if (cursor != null) {
			int size = cursor.getRecordSize();
			logger.debug("size={}", size);
			if (size == 0) {
				return wanObj;
			}

			wanObj = new WanObj[size];
			Map map = null;
			for (int i = 0; i < size; i++) {
				map = cursor.getRecord(i);
				if (map != null) {
					wanObj[i] = new WanObj();
					wanObj[i].setDevId(device_id);
					wanObj[i].setWanId((String) map.get("wan_id"));
					wanObj[i].setGatherTime((String) map.get("gather_time"));
					wanObj[i].setAccessType((String) map.get("access_type"));
					wanObj[i].setWanConnNum((String) map.get("wan_conn_num"));
				}
			}
		}

		return wanObj;
	}

	/**
	 * 根据设备设置相关WAN(WAN)
	 * 
	 * @param device_id
	 * @param wanObj
	 * @return
	 *            <li>true: 成功</li>
	 *            <li>false: 失败</li>
	 */
	public boolean setWAN(String device_id, WanObj[] wanObj) {
		logger.debug("boolean setWAN({}, wanObj)", device_id);
		boolean flag = true;

		// check real
		if (wanObj == null || wanObj.length == 0) {
			logger.debug("wanObj == null || wanObj.length == 0");
			flag = false;
			return flag;
		}

		// check db
		WanObj[] wanObjOld = this.getWAN(device_id);
		if (wanObjOld == null || wanObjOld.length == 0) {
			ArrayList<String> list = new ArrayList<String>();
			String sql = "insert into gw_wan"
					+ " (device_id,wan_id,gather_time,access_type,wan_conn_num"
					+ ") values (";
			for (int i = 0; i < wanObj.length; i++) {
				PrepareSQL psql = new PrepareSQL(sql + "'" + wanObj[i].getDevId() + "',"
						+ wanObj[i].getWanId() + ","
						+ wanObj[i].getGatherTime() + ",'"
						+ wanObj[i].getAccessType() + "',"
						+ wanObj[i].getWanConnNum() + ")");
				psql.getSQL();
				list.add(sql + "'" + wanObj[i].getDevId() + "',"
						+ wanObj[i].getWanId() + ","
						+ wanObj[i].getGatherTime() + ",'"
						+ wanObj[i].getAccessType() + "',"
						+ wanObj[i].getWanConnNum() + ")");
			}

			// into db.
			int[] code = DataSetBean.doBatch(list);
			if (code != null) {
				for (int j = 0; j < code.length; j++) {
					if (code[j] < 0) {
						flag = false;
						break;
					}
				}
			}

			// clear
			code = null;
			list = null;
			sql = null;
		} else {
			logger.debug("real == db");
			flag = true;
		}

		logger.debug("setWAN={}", flag);
		return flag;
	}

	/**
	 * 获取WANConnection:PVC
	 * 
	 * @param device_id
	 * @return
	 */
	public PvcObj[] getWANConn(String device_id, String wan_id) {
		logger.debug("PvcObj[] getWANConn({}, {})", device_id, wan_id);

		PvcObj[] pvcObj = null;

		String sql = "select * from gw_wan_conn where device_id='" + device_id
				+ "' and wan_id=" + wan_id;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select wan_conn_id, gather_time, ip_conn_num, ppp_conn_num, vpi_id, vci_id, vlan_id " +
					"from gw_wan_conn where device_id='" + device_id
					+ "' and wan_id=" + wan_id;
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		if (cursor != null) {
			int size = cursor.getRecordSize();
			logger.debug("size={}", size);
			if (size == 0) {
				return pvcObj;
			}

			pvcObj = new PvcObj[size];
			Map map = null;
			for (int i = 0; i < size; i++) {
				map = cursor.getRecord(i);
				if (map != null) {
					pvcObj[i] = new PvcObj();
					pvcObj[i].setDevice_id(device_id);
					pvcObj[i].setWan_id(wan_id);
					pvcObj[i].setWan_conn_id((String) map.get("wan_conn_id"));
					pvcObj[i].setGather_time((String) map.get("gather_time"));
					pvcObj[i].setIp_conn_num((String) map.get("ip_conn_num"));
					pvcObj[i].setPpp_conn_num((String) map.get("ppp_conn_num"));
					pvcObj[i].setVpi_id((String) map.get("vpi_id"));
					pvcObj[i].setVci_id((String) map.get("vci_id"));
					pvcObj[i].setVlan_id((String) map.get("vlan_id"));
				}
			}
		}

		return pvcObj;
	}

	/**
	 * 根据WAN设置相关PVC(WANConnection)
	 * 
	 * @param device_id
	 * @param wan_id
	 * @param pvcObj
	 * @return
	 *            <li>true: 成功</li>
	 *            <li>false: 失败</li>
	 */
	public boolean setWANConn(String device_id, String wan_id, PvcObj[] pvcObj) {
		logger.debug("boolean setWANConn({},{}, pvcObj)", device_id, wan_id);
		boolean flag = true;

		// check real
		if (pvcObj == null || pvcObj.length == 0) {
			logger.debug("pvcObj == null || pvcObj.length == 0");
			flag = false;
			return flag;
		}

		ArrayList<String> list = new ArrayList<String>();
		// clear old.
		String sql = "delete from gw_wan_conn where device_id='" + device_id
				+ "' and wan_id=" + wan_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		list.add(sql);
		// add new.
		sql = "insert into gw_wan_conn"
				+ " (device_id,wan_id,wan_conn_id,gather_time,ip_conn_num,ppp_conn_num"
				+ ",vpi_id,vci_id,vlan_id" + ") values (";
		for (int i = 0; i < pvcObj.length; i++) {
			list.add(sql
					+ "'"
					+ pvcObj[i].getDevice_id()
					+ "',"
					+ pvcObj[i].getWan_id()
					+ ","
					+ pvcObj[i].getWan_conn_id()
					+ ","
					+ pvcObj[i].getGather_time()
					+ ","
					+ pvcObj[i].getIp_conn_num()
					+ ","
					+ pvcObj[i].getPpp_conn_num()
					+ ","
					+ (pvcObj[i].getVpi_id() == null ? "NULL" : "'"
							+ pvcObj[i].getVpi_id() + "'")
					+ ","
					+ pvcObj[i].getVci_id()
					+ ","
					+ (pvcObj[i].getVlan_id() == null ? "NULL" : "'"
							+ pvcObj[i].getVlan_id() + "'") + ")");
		}

		// into db.
		int[] code = DataSetBean.doBatch(list);
		if (code != null) {
			for (int j = 0; j < code.length; j++) {
				if (code[j] < 0) {
					flag = false;
					break;
				}
			}
		}

		// clear
		code = null;
		list = null;
		sql = null;

		logger.debug("setWANConn={}", flag);
		return flag;
	}

	/**
	 * 删除WANConnection:PVC
	 * 
	 * @param device_id
	 * @param wan_id
	 * @param wan_conn_id
	 * @return
	 *            <li>true: 成功</li>
	 *            <li>false: 失败</li>
	 */
	public boolean delWANConn(String device_id, String wan_id,
			String wan_conn_id) {
		logger.debug("boolean delWANConn({},{}, {})", new Object[] { device_id,
				wan_id, wan_conn_id });
		boolean flag = true;

		ArrayList<String> list = new ArrayList<String>();
		String sql = "delete from gw_wan_conn where device_id='" + device_id
				+ "' and wan_id=" + wan_id + " and wan_conn_id=" + wan_conn_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		list.add(sql);
		sql = "delete from gw_wan_conn_session where device_id='" + device_id
				+ "' and wan_id=" + wan_id + " and wan_conn_id=" + wan_conn_id;
		PrepareSQL psql1 = new PrepareSQL(sql);
		psql1.getSQL();
		list.add(sql);

		// into db.
		int[] code = DataSetBean.doBatch(list);
		if (code != null) {
			for (int j = 0; j < code.length; j++) {
				if (code[j] < 0) {
					flag = false;
					break;
				}
			}
		}

		// clear
		code = null;
		list = null;
		sql = null;

		logger.debug("delWANConn={}", flag);
		return flag;
	}

	/**
	 * 根据device_id获取WAN
	 * 
	 * @param device_id
	 * @return
	 */
	public ConnObj[] getWANConnSession(String device_id, String wan_id,
			String wan_conn_id) {
		logger.debug("ConnObj[] getWANConnSession({}, {}, {})", new Object[] {
				device_id, wan_id, wan_conn_id });

		ConnObj[] connObj = null;

		String sql = "select * from gw_wan_conn_session where device_id='"
				+ device_id + "' and wan_id=" + wan_id + " and wan_conn_id="
				+ wan_conn_id;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select wan_conn_sess_id, sess_type, gather_time, enable, name, conn_type, serv_list, bind_port, " +
					"username, password, ip_type, ip, mask, gateway, dns_enab, dns  " +
					"from gw_wan_conn_session where device_id='"
					+ device_id + "' and wan_id=" + wan_id + " and wan_conn_id="
					+ wan_conn_id;
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		if (cursor != null) {
			int size = cursor.getRecordSize();
			logger.debug("size={}", size);
			if (size == 0) {
				return connObj;
			}

			connObj = new ConnObj[size];
			Map map = null;
			NodeObj[] nodeObj = null;
			for (int i = 0; i < size; i++) {
				map = cursor.getRecord(i);
				if (map != null) {
					connObj[i] = new ConnObj();
					connObj[i].setDevice_id(device_id);
					connObj[i].setWan_id(wan_id);
					connObj[i].setWan_conn_id(wan_conn_id);
					connObj[i].setWan_conn_sess_id((String) map
							.get("wan_conn_sess_id"));
					connObj[i].setSess_type((String) map.get("sess_type"));
					connObj[i].setGather_time((String) map.get("gather_time"));
					nodeObj = new NodeObj[ConnObj.NodeList.length];
					for (int j = 0; j < ConnObj.NodeList.length; j++) {
						nodeObj[j] = new NodeObj();
						nodeObj[j].setName(ConnObj.NodeList[j]);
						nodeObj[j].setValue((String) map
								.get(ConnObj.NodeList[j]));
					}

					connObj[i].setNodeObj(nodeObj);
				}
			}
		}

		return connObj;
	}

	/**
	 * 根据PVC设置相关连接（session）
	 * 
	 * @param device_id
	 * @return
	 */
	public boolean setWANConnSession(String device_id, String wan_id,
			String wan_conn_id, ConnObj[] connObj) {
		logger.debug("boolean setWANConnSession({},{},{}, pvcObj)",
				new Object[] { device_id, wan_id, wan_conn_id });
		boolean flag = true;

		// check real
		if (connObj == null || connObj.length == 0) {
			logger.debug("connObj == null || connObj.length == 0");
			flag = false;
			return flag;
		}

		ArrayList<String> list = new ArrayList<String>();
		String temp1 = "";
		String temp2 = "";
		NodeObj[] nodeObj = null;
		// clear old.
		String sql = "delete from gw_wan_conn_session where device_id='"
				+ device_id + "' and wan_id=" + wan_id + " and wan_conn_id="
				+ wan_conn_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		list.add(sql);
		// add new.
		sql = "insert into gw_wan_conn_session"
				+ " (device_id,wan_id,wan_conn_id,wan_conn_sess_id,sess_type,gather_time";

		for (int i = 0; i < connObj.length; i++) {
			temp2 = "'" + connObj[i].getDevice_id() + "',"
					+ connObj[i].getWan_id() + ","
					+ connObj[i].getWan_conn_id() + ","
					+ connObj[i].getWan_conn_sess_id() + ","
					+ connObj[i].getSess_type() + ","
					+ connObj[i].getGather_time();
			nodeObj = connObj[i].getNodeObj();

			for (int j = 0; j < nodeObj.length; j++) {
				temp1 += "," + nodeObj[j].getName();
				// string
				if (nodeObj[j].getParamType().equals("1")) {
					temp2 += ",'" + nodeObj[j].getValue() + "'";
				} else { // numeric
					temp2 += "," + nodeObj[j].getValue();
				}
			}
			temp2 = sql + temp1 + ") values (" + temp2 + ")";
			temp2 = temp2.replaceAll("'null'", "NULL");
			temp2 = temp2.replaceAll("''", "NULL");
			PrepareSQL psql2 = new PrepareSQL(temp2);
			psql2.getSQL();
			list.add(temp2);
		}

		// into db.
		int[] code = DataSetBean.doBatch(list);
		if (code != null) {
			for (int j = 0; j < code.length; j++) {
				if (code[j] < 0) {
					flag = false;
					break;
				}
			}
		}

		// clear
		code = null;
		list = null;
		sql = null;
		temp1 = null;
		temp2 = null;

		logger.debug("setWANConnSession={}", flag);
		return flag;
	}

	/**
	 * 
	 * @param device_id
	 * @param wan_id
	 * @param wan_conn_id
	 * @param wan_conn_sess_id
	 * @return
	 *            <li>true: 成功</li>
	 *            <li>false: 失败</li>
	 */
	public boolean delWANConnSession(String device_id, String wan_id,
			String wan_conn_id, String wan_conn_sess_id, String sess_type) {
		logger.debug("boolean delWANConnSession({},{},{},{},{})", new Object[] {
				device_id, wan_id, wan_conn_id, wan_conn_sess_id, sess_type });
		boolean flag = true;

		String sql = "delete from gw_wan_conn_session where device_id='"
				+ device_id + "' and wan_id=" + wan_id + " and wan_conn_id="
				+ wan_conn_id + " and wan_conn_sess_id=" + wan_conn_sess_id + " and sess_type=" + sess_type;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		if (DataSetBean.executeUpdate(sql) < 0) {
			flag = false;
		}

		logger.debug("delWANConnSession={}", flag);
		return flag;
	}

	/**
	 * 根据连接ID(wan_conn_sess_id)更改连接配置
	 * 
	 * @param device_id
	 * @param wan_id
	 * @param wan_conn_id
	 * @param wan_conn_sess_id
	 * @param nodeObj
	 * @return
	 */
	public boolean setWANConnSessionNode(String device_id, String wan_id,
			String wan_conn_id, String wan_conn_sess_id, NodeObj[] nodeObj) {
		logger.debug("boolean setWANConnSession({},{},{}, pvcObj)",
				new Object[] { device_id, wan_id, wan_conn_id });
		boolean flag = true;

		// check real
		if (nodeObj == null || nodeObj.length == 0) {
			logger.debug("nodeObj == null || nodeObj.length == 0");
			flag = false;
			return flag;
		}

		// clear old.
		String sql = "update gw_wan_conn_session set gather_time="
				+ (new Date()).getTime() / 1000;

		for (int j = 0; j < nodeObj.length; j++) {
			if (null != nodeObj[j].getName()
					&& !"".equals(nodeObj[j].getName())
					&& null != nodeObj[j].getValue()
					&& !"".equals(nodeObj[j].getValue())) {
				// string
				if (nodeObj[j].getParamType().equals("1")) {
					sql += "," + nodeObj[j].getName() + " = '"
							+ nodeObj[j].getValue() + "'";
				} else { // numeric
					sql += "," + nodeObj[j].getName() + " ="
							+ nodeObj[j].getValue();
				}
			}

		}
		sql += " where device_id='" + device_id + "' and wan_id=" + wan_id
				+ " and wan_conn_id=" + wan_conn_id + " and wan_conn_sess_id="
				+ wan_conn_sess_id;

		sql = sql.replaceAll("'null'", "NULL");
		sql = sql.replaceAll("''", "NULL");
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		// into db.
		if (DataSetBean.executeUpdate(sql) < 0) {
			flag = false;
		}

		// clear
		sql = null;

		logger.debug("setWANConnSessionNode={}", flag);
		return flag;
	}

	/**
	 * add new wan.
	 * 
	 * @param wlanObj
	 * @return
	 *            <li>true: 成功</li>
	 *            <li>false: 失败</li>
	 */
	public boolean addWan(PvcObj pvcObj, ConnObj connObj) {
		logger.debug("boolean addWan({}, {})", pvcObj, connObj);
		boolean flag = true;
		// check real
		if (connObj == null) {
			logger.debug("connObj == null");
			flag = false;

			return flag;
		}

		String sql = "", temp1 = "", temp2 = "";
		ArrayList<String> list = new ArrayList<String>();
		if (pvcObj == null || pvcObj.getWan_conn_id() == null) {
			logger.debug("pvcObj == null");
		} else {
			sql = "insert into gw_wan_conn"
					+ " (device_id,wan_id,wan_conn_id,gather_time,ip_conn_num,ppp_conn_num"
					+ ",vpi_id,vci_id,vlan_id" + ") values (";
			sql += "'" + pvcObj.getDevice_id() + "'," + pvcObj.getWan_id()
					+ "," + pvcObj.getWan_conn_id() + ","
					+ pvcObj.getGather_time() + "," + pvcObj.getIp_conn_num()
					+ "," + pvcObj.getPpp_conn_num() + "," + "'"
					+ pvcObj.getVpi_id() + "'" + "," + pvcObj.getVci_id() + ","
					+ "'" + pvcObj.getVlan_id() + "'" + ")";
			sql = sql.replaceAll("'null'", "NULL");
			sql = sql.replaceAll("''", "NULL");
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			list.add(sql);
		}

		// add new.
		// add new.
		sql = "insert into gw_wan_conn_session"
				+ " (device_id,wan_id,wan_conn_id,wan_conn_sess_id,sess_type,gather_time";
		temp2 = "'" + connObj.getDevice_id() + "'," + connObj.getWan_id() + ","
				+ connObj.getWan_conn_id() + ","
				+ connObj.getWan_conn_sess_id() + "," + connObj.getSess_type()
				+ "," + connObj.getGather_time();
		NodeObj[] nodeObj = connObj.getNodeObj();

		for (int j = 0; j < nodeObj.length; j++) {
			if (null != nodeObj[j].getName() && !"null".equals(nodeObj[j].getName())) {
				temp1 += "," + nodeObj[j].getName();
			}
			// string
			if (nodeObj[j].getParamType().equals("1")) {
				temp2 += ",'" + nodeObj[j].getValue() + "'";
			} else { // numeric
				temp2 += "," + nodeObj[j].getValue();
			}
		}
		temp2 = temp2.replaceAll("'null'", "NULL");
		temp2 = temp2.replaceAll("''", "NULL");
		sql = sql + temp1 + ") values (" + temp2 + ")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		list.add(sql);

		// into db.
		int[] code = DataSetBean.doBatch(list);
		if (code != null) {
			for (int j = 0; j < code.length; j++) {
				if (code[j] < 0) {
					flag = false;
					break;
				}
			}
		}

		// clear
		sql = null;

		return flag;
	}
}
