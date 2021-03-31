<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strData = "";
String stroffset = request.getParameter("offset");
int pagelen = 20;
String totalStr = "";
int total = 0;
int offset;
if (stroffset == null)
	offset = 1;
else
	offset = Integer.parseInt(stroffset);
	

String device_serialnumber = request.getParameter("device_serialnumber");

String sql = "select a.* from tab_gw_device a where a.gw_type=2 ";
// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select a.device_id, a.device_id_ex, a.city_id, a.vendor_id, a.device_serialnumber, a.loopback_ip" +
			" from tab_gw_device a where a.gw_type=2 ";
}
if(device_serialnumber.length()>5){
	sql += " and a.dev_sub_sn ='" + device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length()) + "'";
}
sql += " and a.device_serialnumber like '%" + device_serialnumber 
			+ "'";
			
if(!user.isAdmin()){
	sql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
}

String search = "&device_serialnumber=" + device_serialnumber;
if ("".equals(totalStr)){
	String sqlTotal = "select count(1) As total from tab_gw_device a where a.gw_type=2 ";
	// teledb
	if (DBUtil.GetDB() == 3) {
		sqlTotal = "select count(*) As total from tab_gw_device a where a.gw_type=2 ";
	}
	if(device_serialnumber.length()>5){
		sql += " and a.dev_sub_sn ='" + device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length()) + "'";
	}
	sql += " and a.device_serialnumber like '%" + device_serialnumber 
				+ "'";
				
	if(!user.isAdmin()){
		sqlTotal += " inner join tab_gw_res_area b on a.device_id = b.res_id";
	}

	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sqlTotal);
	psql.getSQL();
	Cursor cursorTotal = DataSetBean.getCursor(sqlTotal);
	Map fieldsTotal = cursorTotal.getNext();
	if (fieldsTotal != null){
		totalStr = (String)fieldsTotal.get("total");
	}
	
	if (totalStr != null && !totalStr.equals("")){
		total = Integer.parseInt(totalStr);
	}
}


int first, next, prev, last;
String strColor = "#535353";
String strHTML = "";
int totalPage = (int) Math.ceil((double) total / pagelen);
int curPage = (int) Math.floor((double) offset / pagelen + 1);

first = 1;
next = offset + pagelen;
prev = offset - pagelen;
last = (totalPage - 1) * pagelen + 1;

if (offset > pagelen)
	strHTML += "<A HREF=\"?offset=" + first + search + "\" target=\"view\">首页</A>&nbsp;";
else
	strHTML += "<FONT COLOR=" + strColor + ">首页</FONT>&nbsp;";

if (prev > 0)
	strHTML += "<A HREF=\"?offset=" + prev + search + "\" target=\"view\">前页</A>&nbsp;";
else
	strHTML += "<FONT COLOR=" + strColor + ">前页</FONT>&nbsp;";

if (next <= total)
	strHTML += "<A HREF=\"?offset=" + next + search + "\" target=\"view\">后页</A>&nbsp;";
else
	strHTML += "<FONT COLOR=" + strColor + ">后页</FONT>&nbsp;";

if (totalPage != 0 && curPage < totalPage)
	strHTML += "<A HREF=\"?offset=" + last + search + "\" target=\"view\">尾页</A>&nbsp;";
else
	strHTML += "<FONT COLOR=" + strColor + ">尾页</FONT>&nbsp;";

strHTML += "  页次：<b>" + curPage + "</b>/<b>" + totalPage + "</b>页 <b>";
strHTML += pagelen + "</b>条/页 共<b>" + total + "</b>条";



Cursor cursor = DataSetBean.getCursor(sql,offset,pagelen);

Map fields = cursor.getNext();
Map city_Map = DeviceAct.getCityMap(request);
Map venderMap = DeviceAct.getOUIDevMap();

if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>没有查询到设备资源</TD></TR>";
} else {
	String device_id = null;
	String device_id_ex = null;
	String city_id = null;
	String city_name = null;
	String vender = null;
	String oui = null;
	String serialnumber = null;
	
    while (fields != null) {
		device_id = (String)fields.get("device_id");
		device_id_ex = (String)fields.get("device_id_ex");
		city_id = (String)fields.get("city_id");
		city_name = (String)city_Map.get(city_id);
		city_name = city_name == null ? "&nbsp;" : city_name;
		
		vender = (String)venderMap.get(fields.get("vendor_id"));
		
		serialnumber = (String)fields.get("device_serialnumber");
		
		strData += "<TR>";
		strData += "<TD class=column2>" + device_id_ex + "</TD>";
		strData += "<TD class=column2>" + vender + "</TD>";
		strData += "<TD class=column2>" + (String)fields.get("loopback_ip") + "</TD>";
		strData += "<TD class=column2>" + city_name + "</TD>";
		
		strData += "<TD class=column2>" + serialnumber + "</TD>";
		strData += "<TD class=column2 nowrap><a href='#' onclick=selDevice('" + device_id + "','" + serialnumber + "')>选择</a></TD>";
		strData += "</TR>";
        fields = cursor.getNext();
    }
    strData = strData + "<TR><TD class=column2 colspan=6 align=right>" + strHTML + "</TD></TR>";
}
%>
<html>
<head>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>
</head>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>

	<FORM NAME="frm" METHOD="post">
		<input type="hidden" name="offset" value="<%=stroffset%>">
		<input type="hidden" name="device_serialnumber" value="<%=device_serialnumber%>">
		<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						设备资源
					</td>
				</tr>
			</table>
			</td>
		</tr>
			<TR>
				<TD bgcolor=#000000>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
						<TR>
							<TH>设备编码</TH>
							<TH>设备厂商</TH>
							<TH>设备域名</TH>
							<TH>属地</TH>
							<TH>设备序列号</TH>
							<TH>操作</TH>
						</TR>
						<%=strData%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</FORM>	
</TD></TR>
<TR><TD HEIGHT=20 align="center"><div id="_process"></div></TD></TR>
</TABLE>
<IFRAME name=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;
<div id="debug" style="display:"></div>
<%@ include file="../foot.jsp"%>
</body>

<script language="javascript">
window.name = "view";

function selDevice(id,serialnumber)
{
	window.returnValue = id + '/' + serialnumber;
	window.close();
}
</script>
</html>