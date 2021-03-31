package com.linkage.litms.uss;

import java.util.HashMap;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.gw.EventLevelLefDAO;
import com.linkage.module.gwms.util.StringUtil;

public class GetWarnInfo extends AllTrTds {

	public GetWarnInfo() {
		UssLog.log("-------------------------GetWarnInfo-------------------------------------");
	}
	
	public String getWarnInfo(String customerID, String username, long startTime, long endTime) {
		String html = "";
		String deviceInfo = com.linkage.litms.uss.DBUtil.getDeviceInfo(username);

		//获得头信息
		//html += CommonMtd.getTitleTable("客户相关信息");
		
		DateTimeUtil dtu = new DateTimeUtil(startTime * 1000);
		int week = dtu.getWeekOfYear();
		int year = dtu.getYear();
		String curDate = dtu.getDate();

		String chkTableSQL = "select name from sysobjects where name = 'event_raw_" + year + "_" + week+"'";
		if (DBUtil.GetDB() == Global.DB_ORACLE)
		{// oracle
			chkTableSQL = "select table_name as name from user_tables where table_name='"
					+ StringUtil.getUpperCase("event_raw_" + year + "_" + week) + "'";
		}
		else if (DBUtil.GetDB() == Global.DB_SYBASE)
		{// sybase
			chkTableSQL = "select name from sysobjects where name = 'event_raw_" + year + "_" + week+"'";
		}
		PrepareSQL psql = new PrepareSQL(chkTableSQL);
		psql.getSQL();
		Map<String, String> chkTableMap = DataSetBean.getRecord(chkTableSQL);

		UssLog.log("[GetWarnInfo-getWarnInfo]------chkTableSQL------:" + chkTableSQL);
		
		if (null == deviceInfo) {
			html += tr(td("该业务用户没有对应的设备", "", "10", ""));
			return retHTML(html, username, curDate, customerID);
		}
		
		if (null == chkTableMap) {
			html += tr(td("该用户暂时没有告警信息", "", "10", ""));
			return retHTML(html, username, curDate, customerID);
		}
		
		String device_name = deviceInfo.split(",")[0];
		String gather_id = deviceInfo.split(",")[1];
		
		String warnSQL = "select a.gather_id,a.serialno, a.createtime,a.subserialno,a.creatorname,"
				+ "a.sourceip,a.sourcename,a.displaystring,a.severity,a.city,a.devicecoding "
				+ " from event_raw_" + year + "_" + week + " a where a.createtime>= " + startTime
				+ " and a.createtime<=" + endTime + " and a.gather_id='" + gather_id
				+ "' and a.sourcename='" + device_name + "' order by a.createtime desc";
		psql = new PrepareSQL(warnSQL);
		psql.getSQL();
		UssLog.log("[GetWarnInfo-getWarnInfo]------warnSQL------:" + warnSQL);

		Cursor cursor = DataSetBean.getCursor(warnSQL);

		String resultHTML = generalHTML(cursor, username, curDate, customerID);

		return resultHTML;
	}

	/**
	 * 根据数据生成HTML
	 * 
	 * @param cursor
	 * @return
	 */
	private String generalHTML(Cursor cursor, String username, String curDate, String customerID) {
		String html = "";

		Map<String, String> TitleMap = new HashMap<String, String>();
		EventLevelLefDAO eventLevelLefDao = new EventLevelLefDAO();
		TitleMap = eventLevelLefDao.getWarnLevel();
//		TitleMap.put("5", "紧急告警");
//		TitleMap.put("4", "严重告警");
//		TitleMap.put("3", "一般告警");
//		TitleMap.put("2", "提示告警");
//		TitleMap.put("1", "正常日志");
//		TitleMap.put("0", "自动清除");
		
		Map<String, String> warnMap = cursor.getNext();

		html += th(td("ID"), td("设备名称"), td("属地"), td("设备IP地址"), td("创建时间"), td("等级"), td("告警源"),
				td("告警内容","","","25%"));

		if (null == warnMap) {
			html += tr(td("该用户暂时没有告警信息", "", "10", ""));
		} else {
			while (null != warnMap) {

				html += tr(td(warnMap.get("serialno")), td(warnMap.get("sourcename")), 
						td(warnMap.get("city_name")), td(warnMap.get("sourceip")), 
						td(StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", Long.parseLong(warnMap.get("createtime")))),
						td(TitleMap.get(warnMap.get("severity"))), td(warnMap.get("creatorname")), 
						td(warnMap.get("displaystring")));
				
				warnMap = cursor.getNext();
			}
		}

		return retHTML(html, username, curDate, customerID);
	}

	/**
	 * 最终返回的HTML
	 * 
	 * @param html
	 * @param username
	 * @param curDate
	 * @return
	 */
	private String retHTML(String html, String username, String curDate, String customerID) {
		html += tr(td("<input class='bottom1' type='button' value=' 返 回 ' onclick='doReturn("+customerID+")'>","right", "10", "","tdd_A"));
		String returnhtml = BuildHTML.getComplete("用户<font color='red'>“" + username
				+ "”</font>的告警数据（<font color='red'>" + curDate + "</font>）", 10, html);

		return returnhtml;
	}

}
