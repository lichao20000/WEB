<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil" %>
<%@ page import="com.linkage.litms.common.util.StringUtils" %>
<%@ page import="com.linkage.litms.common.database.QueryPage" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%
String start_time = request.getParameter("start_time");
String end_time = request.getParameter("end_time");
String city_id = request.getParameter("city_id");
String username = request.getParameter("username");
StringBuilder htmlStr = new StringBuilder();

//厂商
Map venderMap = DeviceAct.getOUIVendorMap();

//属地
Map cityMap = CityDAO.getCityIdCityNameMap();

String sql = "select a.*,b.oui,b.device_serialnumber "
	+ " from tab_outlineuser a,tab_hgwcustomer b "
	+ " where a.username = b.username and b.user_state = '1'";
// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select a.username, a.outdate, a.city_id, b.oui, b.device_serialnumber "
			+ " from tab_outlineuser a,tab_hgwcustomer b "
			+ " where a.username = b.username and b.user_state = '1'";
}

//查询条件：时间
if (start_time != null && !"".equals(start_time)){
	//DateTimeUtil dateTime = new DateTimeUtil(start_time);
	//long s_time = dateTime.getLongTime();
	
    
	sql += " and a.outdate > " + start_time;
}
if (end_time != null && !"".equals(end_time)){
	sql += " and a.outdate <= " + end_time;
}
//查询条件：用户帐号
if (username != null && !"".equals(username)){
	sql += " and a.username = '" + username + "'";
}

//属地条件查询，若是“00”即省中心则不需要
//String city = curUser.getCityId(); 
String city = city_id;
if (city != null && !"00".equals(city)){
	ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city);
	String cityStr = city + "','" + StringUtils.weave(cityArray,"','");
	sql += " and a.city_id in ('" + cityStr + "')";
}

sql += " order by a.outdate desc";

com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(sql);
Map fields = cursor.getNext();

if (fields == null){
	htmlStr.append("<table width='100%' border=0 cellspacing=1 cellpadding=2 bgcolor=#999999><tr BGCOLOR=#ffffff><td class=column>系统中没有符合条件的数据！</td></tr></table>");
}
else{
	htmlStr.append("<table width='100%' border=0 cellspacing=1 cellpadding=2 bgcolor=#999999><tr><TH nowrap>用户帐号</TH><TH nowrap>时间</TH><TH nowrap>属地</TH><TH nowrap>厂商OUI</TH><TH nowrap>设备序列号</TH></tr>");
	while (fields != null){
		String name = (String)fields.get("username");
		String outTime = (String)fields.get("outdate");
		String oui = (String)fields.get("oui");
		String device_serialnumber = (String)fields.get("device_serialnumber");
		
		//时间处理
		DateTimeUtil date = new DateTimeUtil();
		long now = new DateTimeUtil().getLongTime();
		String font = "";
		
		String cityName = (String)cityMap.get((String)fields.get("city_id"));
		
		//根据不同的时间显示不同的颜色
		if (outTime != null && !"".equals(outTime)){
			date = new DateTimeUtil(Long.parseLong(outTime)*1000);
			
			long outTimeLong = Long.parseLong(outTime);
			if ((now - outTimeLong) > 604800){
				font = "<font color=#FF0000>";
			}
			else if ((now - outTimeLong) > 259200){
				font = "<font color=#FF7F50>";
			}
			else if ((now - outTimeLong) > 86400){
				font = "<font color=#FF7F50>";
			}
			else {
				font = "<font color=#000000>";
			}
		}
		
		htmlStr.append("<tr BGCOLOR=#ffffff><td class=column>" + font + name + "</font></td>");
		htmlStr.append("<td class=column>" + font + date.getLongDate() + "</font></td>");
		htmlStr.append("<td class=column>" + font + cityName + "</font></td>");
		htmlStr.append("<td class=column>" + font + venderMap.get(oui) + "</font></td>");
		htmlStr.append("<td class=column>" + font + device_serialnumber + "</font></td></tr>");
		
		fields = cursor.getNext();
	}
	
	htmlStr.append("<tr BGCOLOR=#ffffff><td colspan='5' class=column>");
	
	htmlStr.append("<a href=javascript:queryDataForPrint('");
    htmlStr.append(username);
    htmlStr.append("','");
    htmlStr.append(city_id);
    htmlStr.append("','");
    htmlStr.append(start_time);
    htmlStr.append("','");
    htmlStr.append(end_time);
    htmlStr.append("');><img src='../images/print.gif'></img></a>&nbsp;&nbsp;&nbsp;");
	
    htmlStr.append("<a href=javascript:queryDataForExcel('");
    htmlStr.append(username);
    htmlStr.append("','");
    htmlStr.append(city_id);
    htmlStr.append("','");
    htmlStr.append(start_time);
    htmlStr.append("','");
    htmlStr.append(end_time);
    htmlStr.append("');><img src='../images/excel.gif'></img></a>&nbsp;&nbsp;&nbsp;");
    
    htmlStr.append("<a href=javascript:queryDataForPdf('");
    htmlStr.append(username);
    htmlStr.append("','");
    htmlStr.append(city_id);
    htmlStr.append("','");
    htmlStr.append(start_time);
    htmlStr.append("','");
    htmlStr.append(end_time);
    htmlStr.append("');><img src='../images/pdf.gif'></img></a>&nbsp;&nbsp;&nbsp;");
    
    htmlStr.append("</td></tr>");
	
	htmlStr.append("</table>");
}

%>

<script type="text/javascript">
<!--
parent.document.all("showResult").innerHTML = "<%=htmlStr.toString()%>";
parent.document.all("showResult").style.display="";
//-->
</script>

