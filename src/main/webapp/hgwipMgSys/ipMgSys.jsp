<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>For ip Manager</title>
<%
	/**
			 * ip��ַ����������
			 * 
			 * @author ��־��(5194) tel:13701409234
			 * @version 1.0
			 * @since 2007-11-06
			 * @category ipmg
			 */
%>
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
		$("#isall").bind("click",function(){
		if($(this).attr("checked"))
		{
			$("#ip").attr("disabled","disabled");
			$("#ip").attr("value","");
		}
		else
		{
			$("#ip").attr("disabled","");
			$("#ip").focus();
		}
		});
	});
	/**
	*��ʾ����ip��ַ��ҳ��
	*@ 
	*/
	function showAdd()
	{
	window.open("<s:url value="/hgwipMgSys/addNewAddress.jsp"/>",null,"height=180px,width=340px,top=300px,left=350px");
	}
	/**
	*ɾ������
	*@ 
	*/
	function del( subnet, netMaskLen, subnetGrp)
	{
	if(window.confirm("ȷ��Ҫɾ�������ε�ַ��"))
	{
		$.ajax({ type: "POST", 
			url: "<s:url value="/hgwipMgSys/ipMg!del.action"/>", 
			data: {"subnet":subnet,"netMaskLen":netMaskLen,"subnetGrp":subnetGrp},
			success:
				function(data)
				{
					if(data==0)
					{
						alert("ɾ�����γɹ���");
						window.location.href="<s:url value="/hgwipMgSys/ipMg.action"/>";
					}
					else if(data==-3)
					{
						alert("�Բ������ݿ�����쳣�������²����������Ժ����ԣ�");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
	}
	}
</script>
</head>
<body>
<br>

<form action="<s:url value="/hgwipMgSys/ipMg.action"/>" method="post">
<table width="98%" border="0" align="center" cellpadding="5" cellspacing="0">
	<tr>
		<td class=text>
		<table width="100%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">IP��ַ����</td>
				<td align="left">
				<table width="151" border="0" align="right" cellpadding="0"
					cellspacing="1">
					<tr>
						<s:if test="admin">
							<td>
							<div align="center"><span> <a
								href="javascript:showAdd();">����IP��ַ��Դ</a></span></div>
							</td>
						</s:if>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<table width="100%" border=0 align="center" cellpadding="2"	cellspacing="1" bgcolor=#999999>
		    <TR>
				<TH  colspan="2" align="center"><B>IP����</B></TH>
			</TR>
			<tr bgcolor="#FFFFFF">
				<td class=text width="60%">&nbsp;&nbsp; IP��ַ: <input type="text" name="ip"
					value="<s:property value="ip"/>" id="ip"> <input
					name="isall" type="checkbox" value="checkbox"
					<s:property value="(ip=='')||(ip==null)?'checked':''"/> id="isall">
				ȫ��</td>
				<td class=text width="70%">����״̬: <input name="cutStatus" type="radio"
					value="1" <s:property value="cutStatus==1?'checked':''"/>>
				�ѻ��� <input name="cutStatus" type="radio" value="0"
					<s:property value="cutStatus==0?'checked':''"/>> δ���� <input
					name="cutStatus" type="radio" value="3"
					<s:property value="(cutStatus==null)||(cutStatus==3)?'checked':''"/>>
				ȫ��</td>				
			</tr>
			<tr class="green_foot">
			 <td colspan=2 align=right>
			 <input name="submit" type="submit" value="�� ѯ">
			 </td>
			</tr>
		</table>
		
		<br>
		<table width="100%" border=0 cellpadding="2" cellspacing="1" bgcolor=#999999>
			<tr class="green_title">
				<td class=text>IP��ַ</td>
				<td class=text>��������</td>
				<td class=text>��������</td>
				<s:if test="admin">
					<td class=text>����ʱ��</td>
					<td class=text>��ע</td>
				</s:if>
				<s:else>
					<td class=text>��ߵ�ַ</td>
					<td class=text>��͵�ַ</td>
					<td class=text>��;</td>
				</s:else>
				<td class=text>��ϸ</td>
				<s:if test="admin">
					<td class=text>ɾ��</td>
				</s:if>
			</tr>
			<s:iterator value="ipInfos">
				<tr bgcolor="#FFFFFF">
					<td class=text><s:property value="subnet" /></td>
					<td class=text><s:property value="netmask" /></td>
					<td class=text><s:property value="childcount" /></td>
					<s:if test="admin">
						<td class=text><s:date name="assigntime" format="yyyy/MM/dd" /></td>
						<td class=text><s:property value="subnetcomment" /></td>
					</s:if>
					<s:else>
						<td class=text><s:property value="highaddr" /></td>
						<td class=text><s:property value="lowaddr" /></td>
						<td class=text><s:property value="purpose" /></td>
					</s:else>
					<td class=text>
					
					<div align="center"><a href="<s:url value="/hgwipMgSys/getMenu.action" includeParams="all"><s:param name="subnet_len"><s:property value="subnet+'/'+inetmask+'/'+subnetgrp+'/'+userStat"/></s:param></s:url>"><img
						src="<s:url value="/hgwipMgSys/images/zoo2.gif"/>" width="14"
						height="13" border="0"></a></div>
					</td>
					<s:if test="admin">
						<td class=text>
						<div align="center"><a
							href="javascript:del('<s:property value="subnet"/>','<s:property value="inetmask"/>','<s:property value="subnetgrp"/>');"><img
							src="<s:url value="/hgwipMgSys/images/del.gif"/>" width="14"
							height="12" border="0"></a></div>
						</td>
					</s:if>
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