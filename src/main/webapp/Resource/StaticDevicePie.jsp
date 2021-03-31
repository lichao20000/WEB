<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="com.linkage.litms.common.util.FormUtil"%>
<%@ page import="com.linkage.litms.performance.GeneralNetPerf"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");
//采集点
String gatherList = DeviceAct.getGatherList(session, "", "", true);

//设备属地
String city_id = curUser.getCityId();
SelectCityFilter City = new SelectCityFilter(request);
String city_name = City.getNameByCity_id(city_id);
String strCityList = City.getSelfAndNextLayer(false, city_id, "");

String strDeviceVendor = ""; //设备厂商
Cursor cursor_vendor = GeneralNetPerf.getDeviceVendor();
strDeviceVendor = FormUtil.createListBoxForm(cursor_vendor,
		"vendor_id", "vendor_name", false, "", "", "&type=self");
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var $E = document.all;

function CheckForm(){
	if($E("city_id").value == -1){
		alert("请选择属地！");
		return false;
	}
	return true;
}
function QueryInstance(){
	if(!CheckForm()) return ;

	var page = "StaticDevicePieData.jsp";
	var param = "?city_id=" + $E("city_id").value;
	param += "&tt=" + new Date();
	$E("childFrm").src = page + param;
}
function SetQueryData(htmlData){
	$E("divQueryData").innerHTML = htmlData;
}

function subscribe_to(){
	//格式化参数
	var param = "?";
	param += "city_id="+document.all("city_id").value;
	document.all("url").value = "/Report/frame/treeview/template/StaticDevicePie_template.jsp"+ param;

	var page = "../Report/frame/treeview/addNodeTemplate.jsp?tt="+ new Date().getTime();
	var height = 400;
	var width = 400;
	var left = (screen.width-width)/2;
	var top  = (screen.height-height)/2;
	var w = window.open(page,"ss","left="+left+",top="+top+",width="+width+",height="+height+",resizable=yes,scrollbars=no,toolbar=no");
}
//-->
</SCRIPT>
<table width="100%" border="0" cellspacing="0" cellpadding="0" align=center>
  <tr><td HEIGHT=20>&nbsp;&nbsp;</td></tr>
  <TR><TD>
		<table width="95%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					定制报表
				</td>
			</tr>
		</table>
	</TD></TR>
	<TR><TD>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="95%" align=center valign=middle bgcolor="#999999">
			<TR>
				<TH colspan="4">
					设备资源统计报表
				</TH>
			</TR>
			
			<tr bgcolor="#FFFFFF">
				<TD class="" width=180 align=center>
					属地
				</TD>
				<TD class="">
					<%=strCityList%>
				</TD>
				
			</tr>	
			<tr bgcolor="#FFFFFF" class=green_foot>
				<td nowrap align=right colspan=4>
					<input type=button value=" 查 询 " onclick="QueryInstance()">
				</td>
			</tr>
		</TABLE>
		<input type="hidden" id="url" name="url" value=""/>
	
	</TD></TR>
	<TR><TD height="20">&nbsp;
</TD></TR>

	<TR><TD>
			<div id=divQueryData></div>
    </TD></TR>
</TABLE>

<BR>

<iframe id="childFrm" name="childFrm" style="display:none"></iframe>
<%@ include file="../foot.jsp"%>