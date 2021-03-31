package com.linkage.litms.uss;

import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;

public class GetReportInfo extends AllTrTds {

	public GetReportInfo() {
		UssLog.log("-------------------------GetReportInfo-------------------------------------");
	}
	
	public String getReportInfo(String customerID, String username, long startTime, long endTime) {
		String html = "";
		String device_id = DBUtil.getDeviceID(username);
		
		//获得头信息
		//html += CommonMtd.getTitleTable("客户相关信息");
		
		DateTimeUtil dtu = new DateTimeUtil(startTime * 1000);
		String curDate = dtu.getDate();

		if (null == device_id) {
			html += tr(td("该业务用户没有对应的设备", "", "10", ""));
			return retHTML(html, username, curDate, customerID);
		}
		
		String reportSQL = "select file_path,build_time from tab_rrct_report where device_id='"
				+ device_id + "' and build_time >= " + startTime*1000 + " and build_time <=" + endTime*1000;
		PrepareSQL psql = new PrepareSQL(reportSQL);
		psql.getSQL();
		UssLog.log("[GetReportInfo-getReportInfo]------reportSQL------:" + reportSQL);

		Cursor cursor = DataSetBean.getCursor(reportSQL);

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
		int index = 1;
		String build_time_mili = "";
		String build_time_sec = "";
		Map<String, String> reportMap = cursor.getNext();
		
		html += th(td("序号"), td("用户名"), td("文件路径"), td("报告生成时间"), td("操作"));
		
		if (null == reportMap) {
			html += tr(td("该客户暂时没有运行报告", "", "10", ""));
		} else {
			while (null != reportMap) {
				build_time_mili = reportMap.get("build_time");
				build_time_sec = build_time_mili.substring(0, build_time_mili.length() - 3);
				
				html += tr(td(String.valueOf(index++)), td(username),
						td(reportMap.get("file_path")),
						td(StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", Long.parseLong(build_time_sec))), 
						td("<font color='blue'><a href=javascript:// onclick=\"doDownload('"+ reportMap.get("file_path") +"')\">下载</a></font>","","","20%"));

				reportMap = cursor.getNext();
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
				+ "”</font>的运行报告（<font color='red'>" + curDate + "</font>）", 10, html);

		return returnhtml;
	}
}
