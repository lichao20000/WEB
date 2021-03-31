<!-- 
	
	该页面已经不用了，里面有错误也不再维护

 -->

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page
	import="com.linkage.litms.common.database.Cursor,com.linkage.litms.common.util.DateTimeUtil,com.linkage.litms.Global"%>
<%@page import="java.util.Map,java.util.List"%>

<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%@ include file="../head.jsp"%>

<%
	request.setCharacterEncoding("GBK");
	Map cityMapAll = DeviceAct.getCityMap_All();
	Map serv_type_map = Global.Serv_type_Map;
	Map oper_type_map = Global.Oper_type_Id_Name_Map;
	
	//方法getSheetList_bss已经废弃了
	//List list = DeviceAct.getSheetList_bss(request);
	List list = null;
	
	
	String strBar = String.valueOf(list.get(0));
	Cursor cursor = (Cursor) list.get(1);
	Map fields = cursor.getNext();
	String strData = "";

	if (null != fields) {
		String receive_date = null;
		DateTimeUtil dateTimeUtil = null;
		String resultStr = null;
		String result = null;

		while (null != fields) {
			String device_serialnumber = (String) fields
					.get("device_serialnumber");
			receive_date = (String) fields.get("receive_date");
			if (receive_date != null && !receive_date.equals("")) {
				dateTimeUtil = new DateTimeUtil(Long
						.parseLong(receive_date) * 1000);
				receive_date = dateTimeUtil.getLongDate();
				dateTimeUtil = null;
			}
			resultStr = (String) fields.get("result");
			if ("0".equals(resultStr.split("\\|\\|\\|")[0])) {
				result = "成功";
			} else if ("1".equals(resultStr.split("\\|\\|\\|")[0])) {
				result = resultStr.split("\\|\\|\\|")[2];
			}
			strData += "<TR align=left>";
			strData += "<TD class=column>" + fields.get("username")
					+ "</TD>";
			strData += "<TD class=column>" + fields.get("oui")
					+ "</TD>";
			strData += "<TD class=column>" + device_serialnumber
					+ "</TD>";
			strData += "<TD class=column>"
					+ cityMapAll.get(fields.get("city_id")) + "</TD>";
			strData += "<TD class=column>"
					+ serv_type_map.get(fields.get("serv_type_id"))
					+ "</TD>";
			strData += "<TD class=column>"
					+ oper_type_map.get(fields.get("oper_type_id"))
					+ "</TD>";
			strData += "<TD class=column>" + fields.get("e_id")
					+ "</TD>";
			strData += "<TD class=column>" + fields.get("e_username")
					+ "</TD>";
			strData += "<TD class=column>" + receive_date + "</TD>";
			strData += "<TD class=column>" + result + "</TD>";

			//out.println("<TD class=column><a href=\"DeviceInformStat_submit.jsp?device_serialnumber="+ fields.get("device_serialnumber") + "\">查看交互记录</a></TD>");
			//out.println("<TD class=column><a href=javascript:// onclick=\"detail('"+ device_serialnumber +"', '" + start_day +"', '" + start_time +"' , '" + end_day +"' , '" + end_time +"')\">查看交互记录</a></TD>");
			strData += "</TR>";
			fields = cursor.getNext();
		}
		strData += "<TR><TD class=column COLSPAN=10 align=right nowrap>" + strBar + "</TD></TR>";
	} else {
		strData = "<TR align=left><TD class=column colspan=10>系统没有符合条件的设备!</TD></TR>";
	}
%>

<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
					id="ssidTable">
					<TR>
						<TH colspan="10">BSS原始工单统计信息</TH>
					</TR>
					<TR >
						<td class="green_title2" nowrap>用户宽带账号</td>
						<td class="green_title2" nowrap>设备OUI</td>
						<td class="green_title2" nowrap>设备序列号</td>
						<td class="green_title2" nowrap>属地</td>
						<td class="green_title2" nowrap>业务类型</td>
						<td class="green_title2" nowrap>操作类型</td>
						<td class="green_title2" nowrap>企业ID</td>
						<td class="green_title2" nowrap>企业账号</td>
						<td class="green_title2" nowrap>工单接收时间</td>
						<td class="green_title2" nowrap>执行结果</td>
					</TR>
					<%=strData%>
				</TABLE>
				</TD>
			</TR>
		</TABLE>

		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm NAME="childFrm" SRC=""
			STYLE="display: none"></IFRAME> &nbsp;</TD>
	</TR>
</TABLE>

<%@ include file="../foot.jsp"%>

<%
	DeviceAct = null;
	//CLEAR
	fields = null;
	cursor = null;
%>






