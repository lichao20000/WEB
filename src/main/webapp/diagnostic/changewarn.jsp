<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.common.database.*,
				  com.linkage.litms.software.*"%>
<%
request.setCharacterEncoding("GBK");
String strSQL = null;
String strMsg=null;

String oui = request.getParameter("oui");
String device_serialnumber = request.getParameter("device_serialnumber");
long time = Long.parseLong((String)request.getParameter("time")); 
String eventcode = request.getParameter("eventcode"); 
String commandkey = request.getParameter("commandkey"); 

if(eventcode == null){
eventcode="";
}
if(commandkey==null||commandkey.equals("null")){
commandkey="";
}
strSQL = "update tab_event set auto_check = 2  where oui= '" + oui
       + "' and device_serialnumber = '" + device_serialnumber
       + "' and time=" + time +" and eventcode= '" + eventcode
       + "' and commandkey= '"+commandkey+"'";
DataSetBean st = new DataSetBean();
int result = st.executeUpdate(strSQL);
if(result != -1){
  strMsg = "1";
}else{
  strMsg = "0";
}
 
%>
<%@ include file="../head.jsp"%>
<form name="frm" action="nowWarn_query.jsp" method = "post"></form>
<SCRIPT LANGUAGE="JavaScript">
<!--
	var flag = "<%=strMsg%>";
	if(flag == 0){
		alert("更新失败！");
	}else{
		alert("更新成功！");
		
	}
	// window.location.replace("nowWarn_query.jsp");
   // document.frm.submit();
   // window.location.replace("nowWarn_query.jsp?1=1");
    window.location="nowWarn_query.jsp?t="+new Date().getTime();
    
//-->
</SCRIPT>


<%@ include file="../toolbar.jsp"%>

<%@ include file="../foot.jsp"%>

