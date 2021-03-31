<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*,java.util.*"%>
<jsp:useBean id="group" scope="request" class="com.linkage.litms.system.Group"/>

<%
request.setCharacterEncoding("GBK");
/*
String strSQL = "select group_oid,group_name from tab_group order by group_oid";
Cursor cursor = DataSetBean.getCursor(strSQL);
*/
Cursor cursor = group.getGroupsAll();
Map fields = cursor.getNext();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE>LITMS---e家终端综合管理系统</TITLE>
<META content="text/html;charset=gb2312" http-equiv="Content-Type">
<style>
.JJ {
	background-color:#FFFFFF;
	border:1px solid #A0C6E5;
	padding:4px;
	overflow-x:hidden;
	overflow-y:auto;
	vertical-align:top;
	width:170px
}
.DD	{COLOR:#000099}

SELECT,OPTION,TEXTAREA		{PADDING-LEFT:2px}
TR.HL	{BACKGROUND-COLOR:#e9f2f8;CURSOR:hand}
TR.SL	{BACKGROUND-COLOR:#C1CDD8}
</style>
<SCRIPT LANGUAGE="JavaScript">
function DoHL(){
	var e=window.event.srcElement;
	while (e.tagName!="TR"){e=e.parentNode;}
	if (e.className!='SL') e.className='HL';
}
function DoLL(){
	var e=window.event.srcElement;
	while (e.tagName!="TR"){e=e.parentNode;}
	if (e.className!='SL')	e.className='';
}

function AddToList(per_name,group_oid){
	var srcData = document.getElementsByName("gg_name")[0].value.split(/,/);
	var IsUni = true;
	for (var i=0;i<srcData.length;i++){
		if (per_name==srcData[i])	
			IsUni=false;
	}
	if (IsUni){
		if (document.getElementsByName("gg_name")[0].value.length!=0 && document.getElementsByName("gg_name")[0].value.charAt(gg_name.value.length - 1) != ","){
			document.getElementsByName("gg_name")[0].value += ",";
		}
		document.getElementsByName("gg_name")[0].value += per_name;
      	document.getElementsByName("gg_oid")[0].value += group_oid;

	}
}

function HandleSubmit(){
	var _R = new Object()
	_R.oid=document.getElementsByName("gg_oid")[0].value;
	_R.name=document.getElementsByName("gg_name")[0].value;
	if (window.opener != undefined) {
        window.opener.deviceResult1(_R);
    }
    else {
        window.returnValue = _R;
    }
	window.close();
}



function init(){
	var oMyObject = window.dialogArguments;
	if(typeof(oMyObject) == "object"){
		gg_name.value = oMyObject.name;
	}
}
</SCRIPT>
</HEAD>
<BODY onload="init();">
<table border=0 cellpadding=0 cellspacing=0 width="100%">
	<!--<tr>
	<td colspan=5 bgcolor="#0A6CCE" valign=bottom><IMG SRC="images/sys_banner.gif" WIDTH="136" HEIGHT="42" BORDER="0" ALT=""></td>
	</tr>
	<tr>
	<td colspan=5 bgcolor="#4B92D9" style="height:20px;border-top:1px solid #87b3d0">&nbsp;</td>
	</tr> -->
	<tr><td colspan=5 style="height:18px;">&nbsp;</td></tr>
	<tr>
		<td><img src="images/spacer.gif" height=1 width=12></td>
		<td valign=top>
			<table border=0 cellpadding=0 cellspacing=0 width=210>
				<tr><td bgcolor=#E1EEEE style="padding:4px;"><img src="images/ldims4_1_2.gif" border=0 hspace="1">&nbsp;用户组列表</td></tr>
				<tr><td>
				<div class="JJ" style="width:230px;height:260px;">
					<table border=0 cellpadding=0 cellspacing=0 width=100% class="OTab" id="MyContacts">
						<%
						if(fields != null){
							while(fields != null){
								out.println("<tr><td onmouseover=\"DoHL()\" onmouseout=\"DoLL()\" onclick=\"AddToList('"+fields.get("group_name")+"','"+fields.get("group_oid")+"');\" oid='"+fields.get("group_oid")+"',><font class=\"DD\">"+fields.get("group_name")+"</font></td></tr>");

								fields = cursor.getNext();
							}
						}
						else{
							out.println("<tr><td><font class=\"DD\">系统没有用户组选择</font></td></tr>");
						}
						%>
					</table>
				</div>
				</td></tr>
			</table>
		</td>
		<td valign=middle align=center></td>
		<td valign=top>
			<table border=0 cellpadding=0 cellspacing=0 width=230>
				<tr><td style="padding:4px;"><font class="BB">用户组：</font></td></tr>
				<tr><td>
				<TEXTAREA NAME="gg_name" ROWS="8" COLS="30" class=bk></TEXTAREA>
				<TEXTAREA NAME="gg_oid" ROWS="8" COLS="30" class=bk style="display:none"></TEXTAREA>
				</td></tr>
			</table>
		</td>
		<td><img src="images/spacer.gif" height=1 width=12></td>
	</tr>
	<tr><td></td><td colspan=3 height="32" valign="bottom"><hr size=1 width=100%></td></tr>
	<tr><td><td colspan=3 height="32" valign="bottom" align="right">
		<input class="btn" type="button" value=" 确 定 " class="Bsbttn" onClick="HandleSubmit();"> 
		&nbsp;<input class="btn" type="button" value=" 取 消 " class="Bsbttn" onClick="window.close();">
	</td></tr>
</table>
</BODY>
</HTML>
