package com.linkage.litms.uss;

import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;

public class GetFluxInfo extends AllTrTds {

	public GetFluxInfo() {
		UssLog.log("-------------------------GetFluxInfo-------------------------------------");
	}
	
	public String getFluxInfo(String customerID, String username, long startTime, long endTime) {

		String temp = "";
		String device_id = com.linkage.litms.uss.DBUtil.getDeviceID(username);
		String html = "";
		
		
		
		DateTimeUtil dtu = new DateTimeUtil(startTime * 1000);
		int month = dtu.getMonth();
		int year = dtu.getYear();
//		int day = dtu.getDay();
		
		String tableName = "flux_day_stat_" + year + "_" + month;
		//String tableName = "flux_hour_stat_" + year + "_" + month + "_" + day;
		
		String whereCond = " where device_id='" + device_id + "' and collecttime>"
				+ startTime + " and collecttime<" + endTime + " ";
//		String selectContent = "max(collecttime),device_id,port_info,getway,sum(ifinoctets) ifinoctets,convert(numeric(16,0)," +
//				"sum(ifoutoctets)*8/sum(deltatime)) ifoutoctetsbps,convert(numeric(18,0)," +
//				"sum(ifinerrors)/sum(deltatime)) ifinerrorspps ,convert(numeric(18,0)," +
//				"sum(ifouterrors)/sum(deltatime)) ifouterrorspps,convert(numeric(18,0)," +
//				"sum(ifindiscards)/sum(deltatime)) ifindiscardspps ,convert(numeric(18,0)," +
//				"sum(ifoutdiscards)/sum(deltatime)) ifoutdiscardspps,convert(numeric(18,0)," +
//				"sum(ifinucastpkts)/sum(deltatime)) ifinucastpktspps,convert(numeric(18,0)," +
//				"sum(ifoutucastpkts)/sum(deltatime)) ifoutucastpktspps,"+
//				"convert(numeric(18,0),avg(ifinoctetsbpsmax)) ifinoctetsbps";
		String curDate = dtu.getDate();
		
		if (null == device_id) {
			html += tr(td("该业务用户没有对应的设备", "", "10", ""));
			return retHTML(html, username, curDate, customerID);
		}
		//检查表名是否存在
		String chkTableSQL = "select name from sysobjects where name = '"+tableName+"'";
		if (DBUtil.GetDB() == Global.DB_ORACLE)
		{// oracle
			chkTableSQL = "select table_name as name from user_tables where table_name='"
					+ StringUtil.getUpperCase(tableName) + "'";
		}
		else if (DBUtil.GetDB() == Global.DB_SYBASE)
		{// sybase
			chkTableSQL = "select name from sysobjects where name='" + tableName
					+ "'";
		}
		PrepareSQL psql = new PrepareSQL(chkTableSQL);
		psql.getSQL();
		Map<String, String> chkTableMap = DataSetBean.getRecord(chkTableSQL);
		
		UssLog.log("[GetFluxInfo]------chkTableMap------:" + chkTableMap + "; tableName:"+tableName);
		
		if (null == chkTableMap) {
			html += tr(td("该用户暂时没有流量信息", "", "10", ""));
			return retHTML(html, username, curDate, customerID);
		}
		
		String portNames = getPortNames(device_id);
		if (null == portNames) {
			html = tr(td("无法采集到端口！（该设备未配置流量或配置失败）"));
			return retHTML(html, username, curDate, customerID);
		} 
		String[] portNamesArr = portNames.split(",");
		for (String portName : portNamesArr) {
			if ("".equals(temp)) {
				temp += " select * from " + tableName + whereCond + " and port_info='" + portName.split("\\|")[0]
						+ "' and getway=" + portName.split("\\|")[1];
			} else {
				temp += " union all select * from " + tableName + whereCond + " and port_info='"
						+ portName.split("\\|")[0] + "' and getway=" + portName.split("\\|")[1];
			}
		}
		String fluxSQL = "select * from (" + temp + ") fluxdata order by device_id";

		UssLog.log("[GetFluxInfo-getFluxInfo]------fluxSQL------:" + fluxSQL);
		psql = new PrepareSQL(fluxSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(fluxSQL);
		
		String resultHTML = generalHTML(cursor, username, curDate, customerID);
		return resultHTML;
	}

	/**
	 * 检查该业务用户对应的设备是否已进行过流量的配置 检测flux_interfacedeviceport表中是否有该设备对应的端口数据
	 * 并返回所有的端口名称
	 * 
	 * @param device_id
	 * @return
	 */
	private String getPortNames(String device_id) {
		String portNames = null;
		StringBuilder portNamesBuild = new StringBuilder();
		String portSQL = "select * from flux_interfacedeviceport where device_id='" + device_id
				+ "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			portSQL = "select port_info, getway from flux_interfacedeviceport where device_id='" + device_id
					+ "'";
		}
		PrepareSQL psql = new PrepareSQL(portSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(portSQL);
		Map<String, String> portMap = cursor.getNext();

		if (null == portMap) {
			return null;
		} else {
			while (null != portMap) {
				portNamesBuild.append(portMap.get("port_info")).append("|").append(portMap.get("getway")).append(",");
				portMap = cursor.getNext();
			}
		}
		portNames = portNamesBuild.toString();

		if (null != portNames) {
			portNames = portNames.substring(0, portNames.length() - 1);
		}
		
		UssLog.log("[GetFluxInfo-getPortNames]------PortNames------:" + portNames);
		
		return portNames;
	}

	/**
	 * 根据数据生成HTML
	 * 
	 * @param cursor
	 * @return
	 */
	private String generalHTML(Cursor cursor, String username, String curDate, String customerID) {
		String html = "";

		Map<String, String> fluxMap = cursor.getNext();

		html += th(td("端口名称"), td("流入速率/bps"), td("流入错包率"), td("流入丢包率"), td("流入广播包速率"),
				td("流出速率/bps"), td("流出错包率"), td("流出丢包率"), td("流出广播包速率"));

		if (null == fluxMap) {
			html += tr(td("未采集到流量数据，设备可能没有配置", "", "10", ""));
		} else {
			while (null != fluxMap) {

				html += tr(td(fluxMap.get("port_info")), td(fluxMap.get("ifinoctetsbps")), td(fluxMap
						.get("ifinerrorspps")), td(fluxMap.get("ifindiscardspps")), td(fluxMap
						.get("ifinucastpktspps")), td(fluxMap.get("ifoutoctetsbps")), td(fluxMap
						.get("ifouterrorspps")), td(fluxMap.get("ifoutdiscardspps")), td(fluxMap
						.get("ifoutucastpktspps")));

				fluxMap = cursor.getNext();
			}
		}
		
		return retHTML(html, username, curDate, customerID);
	}


	/**
	 * 最终返回的HTML
	 * @param html
	 * @param username
	 * @param curDate
	 * @return
	 */
	private String retHTML(String html, String username, String curDate, String customerID) {
		html += tr(td("<input class='bottom1' type='button' value=' 返 回 ' onclick='doReturn("+customerID+")'>","right", "10", "","tdd_A"));
		String returnhtml = BuildHTML.getComplete("用户<font color='red'>“" + username
				+ "”</font>的流量数据（<font color='red'>" + curDate + "</font>）", 10, html);
		
		return returnhtml;
	}


}

