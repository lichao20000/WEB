<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>for add new Ip address</title>
<%
	/**
		 * ����ip��ַ����
		 * 
		 * @author ��־��(5194) tel:13701409234
		 * @version 1.0
		 * @since 2007-11-06
		 * @category ipmg
		 */
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">

<STYLE type="text/css">
.tr {
	line-height: 22px;
	height: 22px;
}

tr {
	height: 14px;
}

td tr .table {
	border-width: 1px;
	border-style: solid;
	border-collapse: collapse;
	border-spacing: 0px;
}
</STYLE>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
	<script type="text/javascript"
	src="<s:url value="/Js/jquery.blockUI.js"/>"></script>
<script type="text/javascript">
$(function(){
	$().ajaxStart($.blockUI).ajaxStop($.unblockUI);
	$("#netMaskLen").bind("change",function(){
	var va = $(this).attr("value");
		$.ajax({ type: "POST", 
			url: "<s:url value="/hgwipMgSys/ipMg!getNtMk.action"/>", 
			data: "netMaskLen="+va,
			success:
				function(data)
				{
					$("#netMask").attr("value",data);
				},
			erro:function(xmlR,msg,other){alert(msg);}});
	});
});
function submit()
{
	if((!$.checkIP($("#ipAdr").attr("value")))||($("#ipAdr").attr("value").split(".").length!=4))
	{
		alert("�����ip���Ϸ������������룡");
		return ;
	}
	$("#wait").show();
	$.ajax({ type: "POST", 
			url: "<s:url value="/hgwipMgSys/ipMg!add.action"/>", 
			data: {"ipAdr":$("#ipAdr").val(),"netMaskLen":$("#netMaskLen").val(),"netMask":$("#netMask").val(),"comment":encodeURIComponent($("#comment").val())},
			success:
				function(data)
				{
					if(data==0)
					{
						alert("����IP��ַ�ɹ���");
						window.opener.location.href="<s:url value="/hgwipMgSys/ipMg.action"/>";
						window.close();
					}
					else if(data==-1)
					{
						alert("����Ĳ����д��������²���!");
					}
					else if(data==-2)
					{
						alert("�����Ѿ����ڣ�����������ip��ַ��������");
					}
					else if(data==-3)
					{
						alert("�Բ������ݿ�����쳣�������²����������Ժ����ԣ�");
					}
					else
					{
						alert("ϵͳ�����������Ժ�������");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
</script>
</head>
<body>
<table width="98%" border="0" align="center" cellpadding="5" cellspacing="0" class="green_gargtd">
	<tr class="title_bigwhite">
		<td colspan="2">����IP��ַ��Դ</td>
	</tr>
</table>
<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1" bgcolor=#999999>
	<tr bgcolor="#FFFFFF">
		<td width="20%" align=right>������ַ��</td>
		<td width="80%" align=left><input type="text" name="ipAdr" id="ipAdr" /></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="20%" align=right>����λ����</td>
		<td width="80%" align=left><select name="netMaskLen" id="netMaskLen">
			<option value="8">8λ</option>
			<option value="9">9λ</option>
			<option value="10">10λ</option>
			<option value="11">11λ</option>
			<option value="12">12λ</option>
			<option value="13">13λ</option>
			<option value="14">14λ</option>
			<option value="15">15λ</option>
			<option value="16">16λ</option>
			<option value="17">17λ</option>
			<option value="18">18λ</option>
			<option value="19">19λ</option>
			<option value="20">20λ</option>
			<option value="21">21λ</option>
			<option value="22">22λ</option>
			<option value="23">23λ</option>
			<option value="24">24λ</option>
			<option value="25">25λ</option>
			<option value="26">26λ</option>
			<option value="27">27λ</option>
			<option value="28">28λ</option>
			<option value="29">29λ</option>
			<option value="30">30λ</option>
		</select></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="20%" align=right>�������룺</td>
		<td width="80%" align=left><input type="text" name="netMask" id="netMask" readonly /></td>
	</tr>
	<tr bgcolor="#FFFFFF"><td width="20%" align=right>��ע��</td>
	<td width="80%" align=left>
	<textarea rows="3" cols="20" name="comment" id="comment"></textarea>
	</td></tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="2" class="green_foot" align=right><input type="button" name="����"
			value="����" onclick="submit();" /></td>
	</tr>
</table>
</body>
</html>
<%@ include file="../foot.jsp"%>