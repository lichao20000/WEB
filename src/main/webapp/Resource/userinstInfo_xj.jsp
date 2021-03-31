<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.Map"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="UserInstAct" scope="request"
	class="com.linkage.litms.resource.UserInstAct" />

<%
	request.setCharacterEncoding("GBK");
	String stroffset = request.getParameter("offset");
	String device_serialnumber = request.getParameter("device_serialnumber");
	
	String strData = "";
	
	String sql = "select * from tab_user_dev";
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql = "select oui, serialnumber from tab_user_dev";
	}
	
	if (device_serialnumber != null){
		sql += " where serialnumber like '%" + device_serialnumber + "%'";
	}
	
	String strBar="";
	int pagelen = 2;
	int offset;
	if (stroffset == null)
		offset = 1;
	else
		offset = Integer.parseInt(stroffset);

	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
	psql.getSQL();
	UserInstAct.initPage(sql, offset, pagelen);
	Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);

	strBar = UserInstAct.getPageBar();
	Map fields = cursor.getNext();
	
	//oui对应信息
	Map venderMap = DeviceAct.getOUIDevMap();

	if (fields == null) {
		strData = "<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'><TR><TD class=column COLSPAN=10 HEIGHT=30>该系统没有符合要求的设备资源</TD></TR></TABLE>";
	} else {
		strData += "<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'><TR>"
		+ "<TH>设备厂商</TH><TH nowrap>设备序列号</TH><TH nowrap>型号</TH><TH nowrap>软件版本</TH></TR>";

		while (fields != null) {
			strData += "<TR bgcolor=#FFFFFF>";
			strData += "<TD class=column2 width='20%'>"
					+ venderMap.get(fields.get("oui")) + "</TD>";
			strData += "<TD class=column2 width='35%'>"
				+ (String) fields.get("serialnumber")
				+ "</TD>";
			strData += "<TD class=column2 width='10%'></TD>";
			strData += "<TD class=column2 width='15%'></TD>";
			strData += "</TR>";
			fields = cursor.getNext();
		}

		strBar = strBar.replaceAll("\"", "'");

		strData += "<TR bgcolor=#FFFFFF><TD class=column COLSPAN=10 align=right>"
				+ strBar + "</TD></TR></TBALE>";

	}
%>
<script LANGUAGE="JavaScript">
parent.document.all("userTable").innerHTML = "<%=strData%>";
parent.document.all("dispTr").style.display="";
</script>




