<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>For VBras/Bras State</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.blockUI.js"/>"></script>
<script type="text/javascript">
$(function(){
	$().ajaxStart($.blockUI).ajaxStop($.unblockUI);
	
	//��ȡ�����б�
	$.ajax({ type: "POST", 
		url: "<s:url value="/hgwipMgSys/vbrasMg!initCityList.action"/>", 
		data: {},
		success:
			function(data)
			{
				$("select[@name='city']").html(data);
			},
		erro:function(xmlR,msg,other){alert(msg);}
	});
	
	
});
</script>
</head>
<body>
<br>

<form action="<s:url value="/hgwipMgSys/vbrasMg!brasState.action"/>" method="post">
<table width="98%" border="0" align="center" cellpadding="5" cellspacing="0">
	<tr>
		<td class=text>
		<table width="100%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">BRAS/VBRASͳ��</td>
			</tr>
		</table>
		<table width="100%" border=0 align="center" cellpadding="2"	cellspacing="1" bgcolor=#999999>
		    <TR>
				<TH  colspan="2" align="center"><B>BRAS/VBRASͳ��</B></TH>
			</TR>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>���أ�</td>
				<td width="80%" align=left>
					<select name="city" id="city">
						<option value="00">==ʡ����==</option>
						<s:iterator value="cityList">
						<option value="<s:property value="city_id"/>">==<s:property
									value="city_name" />==</option>
						</s:iterator>
					</select>
				</td>
			</tr>
			<tr class="green_foot">
			 <td colspan=2 align=right>
			 <input name="submit" type="submit" value="ͳ��">
			 </td>
			</tr>
		</table>
		
		<br>
		<table width="100%" border=0 cellpadding="2" cellspacing="1" bgcolor=#999999>
			<tr class="green_title">
				<td class=text>������</td>
				<td class=text>BRAS</td>
				<td class=text>VBRAS</td>
			</tr>
			<s:iterator value="brasStateList">
				<tr bgcolor="#FFFFFF">
					<td class=text><s:property value="cityName" /></td>
					<td class=text><s:property value="BRASNUM" /></td>
					<td class=text><s:property value="VBRASNUM" /></td>
				</tr>
			</s:iterator>

		</table>

		</td>
	</tr>
</table>
</form>
<br>
<br>
<%@ include file="../foot.jsp"%>