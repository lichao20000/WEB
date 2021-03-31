<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>For VBras/Bras Manager</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
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
		
		$("#ip").bind("propertychange","input",function(){
			if($(this).val()!=""){
				$("#isall").attr("checked","");
			}
			else{
				$("#isall").attr("checked","checked");
			}
		});
	});
	/**
	*显示Bras/VBras地址的页面
	*@ 
	*/
	function showAdd()
	{
	window.open("<s:url value="/hgwipMgSys/addNewBrasVbras.jsp"/>",null,"height=180px,width=340px,top=300px,left=350px");
	}
	/**
	*删除VBras、Bras网段
	*@ 
	*/
	function del(ip, netMask)
	{
	if(window.confirm("确定要删除该Bras/VBras地址！"))
	{
		$.ajax({ type: "POST", 
			url: "<s:url value="/hgwipMgSys/vbrasMg!delVbras.action"/>", 
			data: {"ip":ip,"netMask":netMask},
			success:
				function(data)
				{
					if(data==0)
					{
						alert("删除网段成功！");
						window.location.href="<s:url value="/hgwipMgSys/vbrasMg.action"/>";
					}
					else if(data==-3)
					{
						alert("对不起，数据库操作异常！请重新操作，或者稍后再试！");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
	}
	}
	
	/*检查IP输入是否合法*/
	function check(){
		//为空查询全部，允许
		if(""==$("#ip").val()){
			return true;
		}
		if((!$.checkIP($("#ip").attr("value")))||($("#ip").attr("value").split(".").length!=4))
		{
			alert("输入的ip不合法，请重新输入！");
			return false;
		}
		return true;
	}
	
</script>
</head>
<body>
<br>

<form action="<s:url value="/hgwipMgSys/vbrasMg.action"/>" method="post" onsubmit="return check();">
<table width="98%" border="0" align="center" cellpadding="5" cellspacing="0">
	<tr>
		<td class=text>
		<table width="100%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">BRAS/VBRAS地址管理</td>
				<td align="left">
				<table width="151" border="0" align="right" cellpadding="0"
					cellspacing="1">
					<tr><td>
						<div align="center"><span> <a
							href="javascript:showAdd();">新增BRAS/VBRAS地址</a></span></div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<table width="100%" border=0 align="center" cellpadding="2"	cellspacing="1" bgcolor=#999999>
		    <TR>
				<TH  colspan="2" align="center"><B>BRAS/VBRAS地址分配</B></TH>
			</TR>
			<tr bgcolor="#FFFFFF">
				<td class=text width="60%">&nbsp;&nbsp; IP地址: 
				<input type="text" name="ip" id="ip" value="<s:property value="ip"/>" id="ip"> 
				<input name="isall" type="checkbox" value="checkbox"
					<s:property value="(ip=='')||(ip==null)?'checked':''"/> id="isall">
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
				<td class=text>地区</td>
				<td class=text>BRAS名称/VBRAS名称</td>
				<td class=text>IP地址</td>
				<td class=text>子网掩码</td>
				<td class=text>BRAS/VBRAS</td>
				<td class=text>删除</td>
			</tr>
			<s:iterator value="brasList">
				<tr bgcolor="#FFFFFF">
					<td class=text><s:property value="cityName" /></td>
					<td class=text><s:property value="name" /></td>
					<td class=text><s:property value="ip" /></td>
					<td class=text><s:property value="netmask" /></td>
					<td class=text><s:property value="type" /></td>
					<td class=text>
					<div align="center"><a
						href="javascript:del('<s:property value="ip"/>','<s:property value="netmask"/>');"><img
						src="<s:url value="/hgwipMgSys/images/del.gif"/>" width="14"
						height="12" border="0"></a></div>
					</td>
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