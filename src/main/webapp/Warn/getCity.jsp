<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%-- 
 * 创建日期 2007-3-6
 * Administrator liuli
--%>
<%
request.setCharacterEncoding("GBK");

// ===============================采集点选中的修改========================================================
Map m_GaherIdNameMap = deviceAct.getGatherIdNameMap();
List m_ProcessesList = curUser.getUserProcesses();

String strChildList = "";
String gather_id = request.getParameter("gather_id");
for(int k=0;k<m_ProcessesList.size();k++){
	String strChecked = "";
	if(gather_id.indexOf((String)m_ProcessesList.get(k))!= -1){
		strChecked="checked";		
	}	
	strChildList += "<tr><td><input type='checkbox'  name=Mycheckbox1 value=" + m_ProcessesList.get(k) + "   "+strChecked+">"+ m_GaherIdNameMap.get(m_ProcessesList.get(k)) + "</td></tr><br>";	
}
//=======================================================================================
%>
<HTML>
<HEAD>
<TITLE> 取得事件信息</TITLE>
<META NAME="Generator" CONTENT="EditPlus">
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb_2312-80">
<META NAME="Description" CONTENT="取得子串">
</HEAD>
<BODY BGCOLOR="#FFFFFF">
<FORM name=frm >
<SPAN ID="child"><%=strChildList%></SPAN>
</FORM>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.all("device_info1").innerHTML = child.innerHTML;
//-->
</SCRIPT>
</BODY>
</HTML>