<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String vpnType = request.getParameter("vpnType");
	String device_model = request.getParameter("device_model");
	if(vpnType == null)
	    vpnType = "1";
	if(device_model == null || device_model.equals("")){
		device_model = "-1";
	}
	StringBuffer strBuffer = new StringBuffer();
	com.linkage.litms.resource.VPNAct act = new com.linkage.litms.resource.VPNAct();
	Cursor cursor = act.getServTypeCursor(device_model,vpnType);
	Map mapTm = act.getServTmMap(device_model,vpnType);
	Map fields = cursor.getNext();
	String param = null;
	String key = null;
	if(fields != null){
		while(fields != null){
			key = (String)fields.get("servcode") + "#" + (String)fields.get("os_version") + "#" + fields.get("device_model");
		    strBuffer.append("<tr bgcolor='#ffffff'>");
			strBuffer.append("<td valign=top nowrap>" + fields.get("device_model") + "</td>");
			strBuffer.append("<td valign=top nowrap>" + fields.get("os_version") + "</td>");
			strBuffer.append("<td valign=top width='240'>" + fields.get("remark") + "</td>");
			strBuffer.append("<td width='240'>" + mapTm.get(key) + "</td>");
			
			param = "'" + fields.get("servcode") + "'";
			param += ",'" + fields.get("device_model") + "'";
			param += ",'" + fields.get("os_version") + "'";

			strBuffer.append("<td valign=top nowrap><a href='javascript:;' onclick=\"editParm(" + param + ")\">编辑</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:;' onclick=\"delParm(" + param + ")\">删除</a></td>");
			strBuffer.append("</tr>");
			fields = cursor.getNext();
		}
	}else{
	    strBuffer.append("<tr bgcolor='#ffffff'>");
		strBuffer.append("<td colspan=7 align=center><b>无数据</b></td>");
		strBuffer.append("</tr>");
	}
%>
<div id="dataDiv">
<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
  <TR>
	<TD bgcolor=#000000>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"  bordercolorlight="#000000" bordercolordark="#FFFFFF">
			<TR>
				<TH align="center" nowrap>设备型号</TH>
				<TH align="center" nowrap>软件版本</TH>
				<TH align="center" nowrap>服务模版</TH>
				<TH align="center" nowrap>详细信息</TH>
				<TH align="center" nowrap>操作</TH>
			</TR>
			<%=strBuffer.toString()%>
		</TABLE>
	</TD>
  <TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
<!--
	if(parent.document.all("DataList") != null)
		parent.document.all("DataList").innerHTML = dataDiv.innerHTML;
//-->
</SCRIPT>