<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%-- 
 * 创建日期 2007-3-6
 * Administrator liuli
--%>
<%
request.setCharacterEncoding("GBK");
String gather1=  request.getParameter("gather_id");

String strChildList = "";
Map m_GaherIdNameMap = deviceAct.getGatherIdNameMap();
List m_ProcessesList = curUser.getUserProcesses();
for(int k=0;k<m_ProcessesList.size();k++){
	
	if(null!=gather1){

		String[] gather2 =gather1.split(";");
		ArrayList gatherl = new ArrayList();
		
		for(int i=gather2.length-1;i>0;i--){
			gatherl.add(gather2[i]);
		}
		
		if(gatherl.contains(m_ProcessesList.get(k)))
			strChildList += "<tr><td><input type='checkbox' value=" + m_ProcessesList.get(k) + " name=Mycheckbox1 checked>"+ m_GaherIdNameMap.get(m_ProcessesList.get(k)) + "</td></tr><br>";	
		else 
			strChildList += "<tr><td><input type='checkbox' value=" + m_ProcessesList.get(k) + " name=Mycheckbox1>"+ m_GaherIdNameMap.get(m_ProcessesList.get(k)) + "</td></tr><br>";	
	}else 
	strChildList += "<tr><td><input type='checkbox' value=" + m_ProcessesList.get(k) + " name=Mycheckbox1>"+ m_GaherIdNameMap.get(m_ProcessesList.get(k)) + "</td></tr><br>";	
}


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