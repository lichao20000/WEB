<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>For ip Manager</title>
<%
	/**
			 * ip地址管理的入口类
			 * 
			 * @author 王志猛(5194) tel:13701409234
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
	*显示增加ip地址的页面
	*@ 
	*/
	function showAdd()
	{
	window.open("<s:url value="/hgwipMgSys/addNewAddress.jsp"/>",null,"height=180px,width=340px,top=300px,left=350px");
	}
	/**
	*删除网段
	*@ 
	*/
	function del( subnet, netMaskLen, subnetGrp)
	{
	if(window.confirm("确定要删除该网段地址！"))
	{
		$.ajax({ type: "POST", 
			url: "<s:url value="/hgwipMgSys/ipMg!del.action"/>", 
			data: {"subnet":subnet,"netMaskLen":netMaskLen,"subnetGrp":subnetGrp},
			success:
				function(data)
				{
					if(data==0)
					{
						alert("删除网段成功！");
						window.location.href="<s:url value="/hgwipMgSys/ipMg.action"/>";
					}
					else if(data==-3)
					{
						alert("对不起，数据库操作异常！请重新操作，或者稍后再试！");
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
				<td width="162" align="center" class="title_bigwhite">IP地址管理</td>
				<td align="left">
				<table width="151" border="0" align="right" cellpadding="0"
					cellspacing="1">
					<tr>
						<s:if test="admin">
							<td>
							<div align="center"><span> <a
								href="javascript:showAdd();">新增IP地址资源</a></span></div>
							</td>
						</s:if>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<table width="100%" border=0 align="center" cellpadding="2"	cellspacing="1" bgcolor=#999999>
		    <TR>
				<TH  colspan="2" align="center"><B>IP分配</B></TH>
			</TR>
			<tr bgcolor="#FFFFFF">
				<td class=text width="60%">&nbsp;&nbsp; IP地址: <input type="text" name="ip"
					value="<s:property value="ip"/>" id="ip"> <input
					name="isall" type="checkbox" value="checkbox"
					<s:property value="(ip=='')||(ip==null)?'checked':''"/> id="isall">
				全部</td>
				<td class=text width="70%">划分状态: <input name="cutStatus" type="radio"
					value="1" <s:property value="cutStatus==1?'checked':''"/>>
				已划分 <input name="cutStatus" type="radio" value="0"
					<s:property value="cutStatus==0?'checked':''"/>> 未划分 <input
					name="cutStatus" type="radio" value="3"
					<s:property value="(cutStatus==null)||(cutStatus==3)?'checked':''"/>>
				全部</td>				
			</tr>
			<tr class="green_foot">
			 <td colspan=2 align=right>
			 <input name="submit" type="submit" value="查 询">
			 </td>
			</tr>
		</table>
		
		<br>
		<table width="100%" border=0 cellpadding="2" cellspacing="1" bgcolor=#999999>
			<tr class="green_title">
				<td class=text>IP地址</td>
				<td class=text>子网掩码</td>
				<td class=text>子网个数</td>
				<s:if test="admin">
					<td class=text>分配时间</td>
					<td class=text>备注</td>
				</s:if>
				<s:else>
					<td class=text>最高地址</td>
					<td class=text>最低地址</td>
					<td class=text>用途</td>
				</s:else>
				<td class=text>详细</td>
				<s:if test="admin">
					<td class=text>删除</td>
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