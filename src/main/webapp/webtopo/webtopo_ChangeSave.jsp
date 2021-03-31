<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.MCControlManager"%>
<%
request.setCharacterEncoding("GBK");
String serial = request.getParameter("serial");
String icon   = request.getParameter("iconSelect");
String sql = "update tp_devicetype_icon_map set icon_name='" + icon + "' where serial=" + serial;
int flag = DataSetBean.executeUpdate(sql);
String result = "";
if(flag > 0 ){
	result = "parent.success(true)";
	MCControlManager mc = new MCControlManager(user.getAccount(),user.getPasswd());
	mc.InformControl(1);
}else{
	flag = DataSetBean.executeUpdate("insert into tp_devicetype_icon_map(serial,icon_name) values(" + serial + ",'" + icon + "')");
	if(flag > 0 ){
		result = "parent.success(true)";
		MCControlManager mc = new MCControlManager(user.getAccount(),user.getPasswd());
		mc.InformControl(1);
	}else{
		result = "parent.success(false)";
	}
}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	<%=result%>
//-->
</SCRIPT>