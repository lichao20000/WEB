<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
request.setCharacterEncoding("GBK");
String strSQL = "select domain_id,domain_name from tab_domain order by domain_id";
Cursor cursor = DataSetBean.getCursor(strSQL);
Map fields = cursor.getNext();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<HTML>
<HEAD>
<TITLE>综合网管 - 选择设备组</TITLE>
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
<!--
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

function AddToList(_name){
	var srcData = gc_name.value.split(/,/);
	var IsUni = true;
	for (var i=0;i<srcData.length;i++){
		if (_name==srcData[i])	
			IsUni=false;
	}
	if (IsUni){
		if (gc_name.value.length!=0 && gc_name.value.charAt(gc_name.value.length - 1) != ","){
			gc_name.value += ",";
		}
		gc_name.value += _name;
	}
}

function HandleSubmit(){
	var _R = new Object()
	_R.oid=getOID();
	_R.name=gc_name.value;
	window.returnValue=_R;
	//alert(_R.oid+"\n"+_R.name)
	window.close();
}

function getOID(){
	sname = gc_name.value;
	arr_name = sname.split(",");
	var oTD = MyContacts.getElementsByTagName("TD");
	var oids = "";
	for(var i=0;i<arr_name.length;i++){
		for(var j=0;j<oTD.length;j++){
			if(oTD[j].innerText == arr_name[i]){
				if(oids == "") oids = oTD[j].oid;
				else oids += ","+oTD[j].oid;
				break;
			}
		}
	}

	return oids;
}

function init(){
	var oMyObject = window.dialogArguments;
	if(typeof(oMyObject) == "object"){
		gc_name.value = oMyObject.name;
	}
}
//-->
</SCRIPT>
</HEAD>
<BODY onload="init();">
<table border=0 cellpadding=0 cellspacing=0 width="100%">
	<tr>
	<td colspan=5 bgcolor="#0A6CCE" valign=bottom><IMG SRC="images/sys_banner.gif" WIDTH="136" HEIGHT="42" BORDER="0" ALT=""></td>
	</tr>
	<tr>
	<td colspan=5 bgcolor="#4B92D9" style="height:20px;border-top:1px solid #87b3d0">&nbsp;</td>
	</tr>
	<tr><td colspan=5 style="height:18px;">&nbsp;</td></tr>
	<tr>
		<td><img src="../images/spacer.gif" height=1 width=12></td>
		<td valign=top>
			<table border=0 cellpadding=0 cellspacing=0 width=210>
				<tr><td bgcolor=#A0C6E5 style="padding:4px;"><img src="../system/images/ldims4_1_2.gif" border=0 hspace="1">&nbsp;设备组列表</td></tr>
				<tr><td>
				<div class="JJ" style="width:230px;height:260px;">
					<table border=0 cellpadding=0 cellspacing=0 width=100% class="OTab" id="MyContacts">
						<%
						if(fields != null){
							while(fields != null){
								out.println("<tr><td onmouseover=\"DoHL()\" onmouseout=\"DoLL()\" onclick=\"AddToList('"+fields.get("domain_name")+"');\" oid='"+fields.get("domain_id")+"',><font class=\"DD\">"+fields.get("domain_name")+"</font></td></tr>");

								fields = cursor.getNext();
							}
						}
						else{
							out.println("<tr><td><font class=\"DD\">系统没有设备组选择</font></td></tr>");
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
				<tr><td style="padding:4px;"><font class="BB">设备组：</font></td></tr>
				<tr><td>
				<TEXTAREA NAME="gc_name" ROWS="8" COLS="30" class=bk></TEXTAREA>
				<TEXTAREA NAME="gc_oid" ROWS="8" COLS="30" class=bk style="display:none"></TEXTAREA>
				</td></tr>
			</table>
		</td>
		<td><img src="../images/spacer.gif" height=1 width=12></td>
	</tr>
	<tr><td></td><td colspan=3 height="32" valign="bottom"><hr size=1 width=100%></td></tr>
	<tr><td><td colspan=3 height="32" valign="bottom" align="right">
		<input class="btn" type="button" value=" 确 定 " class="Bsbttn" onClick="HandleSubmit();"> 
		&nbsp;<input class="btn" type="button" value=" 取 消 " class="Bsbttn" onClick="window.close();">
	</td></tr>
</table>
</BODY>
</HTML>

