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
	com.linkage.litms.resource.VPNAct act = new VPNAct();
	Cursor cursor = act.getParamCursor(device_model,vpnType);
	Map fields = cursor.getNext();
	String param = null;
	if(fields != null){
		while(fields != null){
		    strBuffer.append("<tr bgcolor='#ffffff'>");
		    strBuffer.append("<td nowrap>" + fields.get("para_code") + "</td>");
			strBuffer.append("<td nowrap>" + fields.get("device_model") + "</td>");
			strBuffer.append("<td nowrap>" + fields.get("os_version") + "</td>");
			strBuffer.append("<td nowrap>" + fields.get("para_name") + "</td>");
			strBuffer.append("<td width='240'>" + fields.get("remark") + "</td>");
			strBuffer.append("<td width='340'>" + fields.get("sample_value") + "</td>");
			param = "'" + fields.get("para_code") + "'";
			param += ",'" + fields.get("device_model") + "'";
			param += ",'" + fields.get("os_version") + "'";
			param += ",'" + vpnType + "'";//fields.get("serv_type")

			strBuffer.append("<td nowrap> <a href='javascript:;' onclick=\"delParm(" + param + ")\">ɾ��</a></td>");
			strBuffer.append("</tr>");
			fields = cursor.getNext();
		}
	}else{
	    strBuffer.append("<tr bgcolor='#ffffff'>");
		strBuffer.append("<td colspan=6>������</td>");
		strBuffer.append("</tr>");
	}
%>
<%@page import="com.linkage.litms.resource.VPNAct"%>
<div id="dataDiv">
<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
  <TR>
	<TD bgcolor=#000000>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"  bordercolorlight="#000000" bordercolordark="#FFFFFF">
			<TR>
				<TH align="center" nowrap>�������</TH>
				<TH align="center" nowrap>�豸�ͺ�</TH>
				<TH align="center" nowrap>����汾</TH>
				<TH align="center" nowrap>��������</TH>
				<TH align="center" nowrap>������ע</TH>
				<TH align="center" nowrap>ʾ����ע</TH>
				<TH align="center" nowrap>����</TH>
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