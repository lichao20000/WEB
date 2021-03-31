<%@ include file="../timelater.jsp"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page contentType="text/html;charset=GBK"%>
<%--
	zhaixf(3412) 2008-05-08
	req:XJDX_ITMS-BUG-20080506-XXF-001
--%>
<%
request.setCharacterEncoding("GBK");
//从配置文件中取出属地信息，该信息在工程加载时已读入内存，其生命周期为application
Map city_Map = CityDAO.getCityIdCityNameMap();
String curCity_ID = curUser.getCityId();

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

String resOui = request.getParameter("oui");
String resVender = request.getParameter("vender");
String customer_id = request.getParameter("customer_id");
String sql = "select device_id,city_id,oui,device_serialnumber,cpe_allocatedstatus "
			+ " from tab_gw_device where oui like '%" + resOui.trim() 
			+ "%' ";
if(resVender.trim().length()>5){
		sql += " and dev_sub_sn ='" + resVender.trim().substring(resVender.trim().length()-6, resVender.trim().length()) + "'";
	}
sql += " and device_serialnumber like '%" + resVender.trim() + "'";
			
String gw_type = request.getParameter("gw_type");
String note = "";	//企业网关提示信息
if(gw_type!=null && gw_type.equals("1")){
	sql +=" and gw_type=1 ";
}else {
	sql +=" and gw_type=2 ";
	//sql +=" and customer_id='"+customer_id+"'";
	note = "这里只列出所选客户的设备信息";
}
//sql += " and cpe_allocatedstatus=0 ";


String pcityId = CityDAO.getCityIdPidMap().get(curCity_ID);
if (null != pcityId && !pcityId.equals("-1")){
	sql += " and city_id in('"+pcityId+"','"+curCity_ID+"','00') ";
}
String search = "&oui=" + resOui + "&vender=" + resVender;


if ("".equals(totalStr)){

	String sqlTotal = "select count(1) As total from tab_gw_device where oui like '%" + resOui.trim() 
				+ "%' ";
	// teledb
	if (DBUtil.GetDB() == 3) {
		sqlTotal = "select count(*) As total from tab_gw_device where oui like '%" + resOui.trim()
				+ "%' ";
	}
	if(resVender.trim().length()>5){
		sql += " and dev_sub_sn ='" + resVender.trim().substring(resVender.trim().length()-6, resVender.trim().length()) + "'";
	}
	sql += " and device_serialnumber like '%" + resVender.trim() + "'";
	if(gw_type.equals("1")){
		sqlTotal +=" and gw_type=1 ";
	}else {
		sqlTotal +=" and gw_type=2 ";
		sqlTotal +=" and gw_type=2 ";
		sqlTotal +=" and customer_id='"+customer_id+"'";
	}
	//sqlTotal += " and cpe_allocatedstatus=0 ";
	if (null != pcityId && !pcityId.equals("-1")){
		sqlTotal += " and city_id in('"+pcityId+"','"+curCity_ID+"','00') ";
	}

	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sqlTotal);
	psql.getSQL();
	Cursor cursorTotal = DataSetBean.getCursor(sqlTotal);
	Map fieldsTotal = cursorTotal.getNext();
	if (fieldsTotal != null){
		totalStr = (String)fieldsTotal.get("total");
	}
	
	if (totalStr != null && !"".equals(totalStr)){
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

Map venderMap = DeviceAct.getOUIDevMap();
if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>没有查询到设备资源</TD></TR>";
} else {
	String device_id = null;

	String city_id = null;
	String city_name = null;
	String vender = null;
	String oui = null;
	String serialnumber = null;
	String cpe_allocatedstatus = null;
	
    while (fields != null) {
		device_id = (String)fields.get("device_id");
		city_id = (String)fields.get("city_id");
		city_name = (String)city_Map.get(city_id);
		city_name = city_name == null ? "&nbsp;" : city_name;
		vender = (String)venderMap.get(fields.get("oui"));
		oui = (String)fields.get("oui");
		serialnumber = (String)fields.get("device_serialnumber");
		cpe_allocatedstatus = (String)fields.get("cpe_allocatedstatus");
		
		strData += "<TR>";
		strData += "<TD class=column2>" + device_id + "</TD>";
		strData += "<TD class=column2>" + vender + "</TD>";
		strData += "<TD class=column2>" + city_name + "</TD>";
		strData += "<TD class=column2>" + serialnumber + "</TD>";
		strData += "<TD class=column2>" + (cpe_allocatedstatus.equals("1")? "是" : "否" )+ "</TD>";
		strData += "<TD class=column2 nowrap><a href='#' onclick=selDevice('" + oui + "','" + serialnumber + "','"+device_id+"')>选择</a></TD>";
		strData += "</TR>";
        fields = cursor.getNext();
    }
    strData += "<TR><TD class=column2 colspan=6 align=right>" + strHTML + "</TD></TR>";
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
		<input type="hidden" name="oui" value="<%=resOui%>">
		<input type="hidden" name="vender" value="<%=resVender%>">
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
							<TH>属地</TH>
							<TH>设备序列号</TH>
							<TH>是否绑定</TH>
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

function selDevice(id,vender,device_id)
{
	window.returnValue = id + '/' + vender + '/' + device_id;
	window.close();
}
</script>
</html>