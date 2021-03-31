<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.*" %>
<%@ page import="java.text.SimpleDateFormat,com.linkage.module.gwms.dao.tabquery.CityDAO,java.util.*" %>
<%--
	zhaixf(3412) 2008-04-22
	JSDX_ITMS-BUG-YHJ-20080421-001
--%>
<jsp:useBean id="HGWUserInfoAct" scope="request" class="com.linkage.litms.resource.HGWUserInfoAct"/>
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");
response.setHeader("Content-disposition","attachment; filename=user.xls" );
ArrayList list = new ArrayList();
list.clear();
//所有HGW用户列表
list = HGWUserInfoAct.getHGWUsersCursorExcel(request);
Cursor cursor = (Cursor)list.get(0);
Map map = cursor.getNext();
%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<HTML>
<HEAD>
<TITLE>导出HGW用户信息</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gb2312">
<style>
TD{
  FONT-FAMILY: "宋体", "Tahoma"; FONT-SIZE: 12px;
}
</style>
</HEAD>

<BODY>
<TABLE border=1 cellspacing=0 cellpadding=2 borderColorLight="#000000" borderColorDark="#FFFFFF" width="90%">
<TR>
	<TD><B>用户帐号</B></TD>	
	<TD><B>属地</B></TD>
	<TD><B>用户来源</B></TD>
	<TD><B>绑定设备</B></TD>
	<TD><B>受理时间</B></TD>
	<TD><B>竣工状态</B></TD>
</TR>
<%
String os = "";
String city = "";
Map	cityMap = CityDAO.getCityIdCityNameMap();
while (map != null) {
	os = "";

	if(map.get("device_serialnumber") == null || map.get("device_serialnumber").equals(""))
		os = "-";
	else
		os = map.get("oui") + "-" + map.get("device_serialnumber");
		
	String user_type_id = (String)map.get("user_type_id");
	String tmp = "手工添加";
	if(user_type_id != null && !user_type_id.equals("")){
		if(user_type_id.equals("1")){
			tmp = "现场安装";
		} else if(user_type_id.equals("2")){
				tmp = "BSS工单";
		} else if(user_type_id.equals("3")){
			tmp = "手工添加";
		} else if(user_type_id.equals("4")){
			tmp = "BSS同步";
		} else {
			tmp = "其它";
		}
	}
	Calendar time=Calendar.getInstance(); 
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	String dealdate = "";
	if( map.get("dealdate") != null && !map.get("dealdate").equals("")
			&& !map.get("dealdate").equals("0")){
		time.setTimeInMillis((Long.parseLong((String)map.get("dealdate")))*1000);
		dealdate = df.format(time.getTime());
	}else{
		dealdate = "-";
	}

	//String onlinedate = "";
	//if( map.get("onlinedate") != null && !map.get("onlinedate").equals("")
	//		&& !map.get("onlinedate").equals("0")){
	//	time.setTimeInMillis((Long.parseLong((String)map.get("onlinedate")))*1000);
	//	onlinedate = df.format(time.getTime());
	//}else{
	//	onlinedate = "-";
	//}

	String opmode = "";
	if("1".equals(map.get("opmode"))){
		opmode = "<font color='blue'>已竣工</font>";
	}else{
		opmode = "未竣工";
	}
	
	//city
	if(map.get("city_id") != null) {
		city = (String)cityMap.get(map.get("city_id"));
	} else {
		city = "-";
	}
	
%>
<TR >
	<TD><%=(String)map.get("username")%></TD>
	<TD><%=city%></TD> 
	<TD><%=tmp%></TD>
	<TD><%=os%></TD>
	<TD><%=dealdate%></TD>
	<TD><%=opmode%></TD>
</TR>
<%	
	map = cursor.getNext();
}
%>
</TABLE>
</BODY>
</HTML>