package com.linkage.litms.uss;

import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

public class CommonMtd extends AllTrTds {

	/**
	 * 根据CustomerId获得用户下拉框
	 * @param customerID
	 * @return
	 */
	public String getDeviceListHTML(String customerID) {
		String html = "";
		String username = "";
		String[] options;
		String[] values;
		StringBuilder optionsBu = new StringBuilder();
		String deviceSQL = "select username from tab_egwcustomer where customer_id='" + customerID
				+ "'";

		PrepareSQL psql = new PrepareSQL(deviceSQL);
		psql.getSQL();
		
		Cursor cursor = DataSetBean.getCursor(deviceSQL);
		Map<String, String> aRecMap = cursor.getNext();
		if (null == aRecMap) {
			options = new String[1];
			values = new String[1];
			options[0] = "该客户没有业务用户";
			values[0] = "-1";
			html += select("usernameList", options, values, "");
			return html;
		}

		while (null != aRecMap) {
			username = aRecMap.get("username");
			optionsBu.append(username).append(",");
			aRecMap = cursor.getNext();
		}
		options = optionsBu.toString().split(",");
		values = options;

		html += select("usernameList", options, values, "userNameSelect");
		return html;
	}

	/**
	 * 获得标题头
	 * @return
	 */
	public static String getTitleTable(String title) {
		
		String html = "";
		html += "<TABLE cellSpacing=0 cellPadding=0 width=775 align=center border=0><TR><TD valign=top background=../images/title.jpg height=60>"
			+ "<TABLE cellSpacing=0 cellPadding=0 width=775 border=0><TR><TD width=59 height=40>&nbsp;</TD>"
			+ "<TD class=font1 width=701>"+title+"</TD></TR></TABLE></TD></TR></TABLE>";
		return html;
		
	}
}
