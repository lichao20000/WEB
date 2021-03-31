<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>客户资料录入/编辑界面</title>
<%
	/**
		 * 客户资料录入/编辑界面
		 * 
		 * @author 陈仲民(5243)
		 * @version 1.0
		 * @since 2008-06-04
		 * @category
		 */
%>
<link href="<s:url value='/css/css_green.css'/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript"
	src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript"
	src="<s:url value='/Js/jQeuryExtend-linkage.js'/>"></script>
<script type="text/javascript"
	src="<s:url value='/Js/jQeuryCheckForm-linkage.js'/>"></script>
<script type="text/javascript"
	src="<s:url value='/Js/CheckFormForm.js'/>"></script>
<script type="text/javascript">
//初始化

$(function(){
	
	$("input[@name='customer_name']").val('<s:property value="customerMap.customer_name" />');
	$("input[@name='customer_id']").val('<s:property value="customerMap.customer_id" />');
	$("input[@name='customer_account']").val('<s:property value="customerMap.customer_account" />');
	$("input[@name='customer_pwd']").val($.trim('<s:property value="customerMap.customer_pwd" />'));
	$("select[@name='customer_type']").val('<s:property value="customerMap.customer_type" />');
	$("select[@name='customer_size']").val('<s:property value="customerMap.customer_size" />');
	$("input[@name='customer_address']").val($.trim('<s:property value="customerMap.customer_address" />'));
	$("input[@name='linkman']").val($.trim('<s:property value="customerMap.linkman" />'));
	$("input[@name='linkphone']").val('<s:property value="customerMap.linkphone" />');
	$("select[@name='customer_state']").val('<s:property value="customerMap.customer_state" />');
	$("select[@name='city_id']").val('<s:property value="customerMap.city_id" />');
	$("select[@name='office_id']").val('<s:property value="customerMap.office_id" />');
	$("select[@name='zone_id']").val('<s:property value="customerMap.zone_id" />');
	$("input[@name='oui']").val('<s:property value="customerMap.oui" />');
	$("input[@name='device_serialnumber']").val('<s:property value="customerMap.device_sn" />');
	$("input[@name='email']").val('<s:property value="customerMap.email" />'.trim());
})

//提交验证
function checkForm(){
	if ($("input[@name='customer_name']").val() == ""){
		alert("请输入客户名称！");
		$("input[@name='customer_name']").focus();
		return;
	}
	//if ($("input[@name='customer_account']").val() == ""){
	//	alert("请输入客户ID！");
	//	$("input[@name='customer_account']").focus();
	//	return;
	//}
	if ($("input[@name='linkman']").val() == ""){
		alert("请输入联系人！");
		$("input[@name='linkman']").focus();
		return;
	}
	if ($("input[@name='linkphone']").val() == ""){
		alert("请输入联系电话！");
		$("input[@name='linkphone']").focus();
		return;
	}
	
	if (!IsNumber($("input[@name='linkphone']").val(),"联系电话")){
		return;
	}
	
	if ($("input[@name='email']").val() != ""){
		if (!($.checkEmail($("input[@name='email']").val()))){
			alert("email地址错误");
			return;
		}
	}
	
	document.frm.submit();
}
</script>
</head>

<body>
<form name="frm" action="<s:url value='/bbms/CustomerInfo!editCustomer.action'/>" method="post">
<table border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						客户资料
					</td>
					<td>
						&nbsp;<img src="../images/attention_2.gif" width="15" height="12">&nbsp;	
					</td>
				</tr>
			</table>
			</td>
		</tr>
	<tr>
		<td>
		<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
			bgcolor="#999999">
			<tr>
				<th colspan="4">编辑</th>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column width="15%" nowrap align="right">客户ID</td>
				<td width="35%"><input type="text" name="customer_id" value="" class="bk" readonly class="bk">&nbsp;<font color="red">*</font></td>
				<td class=column nowrap></td>
				<td></td>
			</tr>
			<tr bgcolor="#FFFFFF" STYLE="display:none">
				<td class=column width="15%" nowrap align="right">客户账号</td>
				<td width="35%"><input type="text" name="customer_account" value="" class="bk" readonly class="bk"></td>
				<td class=column nowrap align="right">客户密码</td>
				<td><input type="text" name="customer_pwd" value="" class="bk"></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column width="15%" nowrap align="right">客户名称</td>
				<td width="35%"><input type="text" name="customer_name" value="" class="bk">&nbsp;<font color="red">*</font></td>
				<td class=column nowrap align="right">属地</td>
				<td>
					<select name="city_id" class="bk">
						<option value="-1">==请选择==</option>
						<s:iterator value="cityList">
							<option value="<s:property value="city_id" />">==<s:property value="city_name" />==</option>
						</s:iterator>
					</select>
					&nbsp;<font color="red">*</font>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align="right">联系人</td>
				<td><input type="text" name="linkman" value="" class="bk">&nbsp;<font color="red">*</font></td>
				<td class=column nowrap align="right">联系电话</td>
				<td ><input type="text" name="linkphone" value="" class="bk" maxlength="20">&nbsp;<font color="red">*</font></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align="right">E-mail</td>
				<td><input type="text" name="email" value="" class="bk"></td>
				<td class=column nowrap align="right">客户状态</td>
				<td>
					<select name="customer_state" class="bk">
						<option value="1">==开通==</option>
						<option value="2">==暂停==</option>
						<option value="3">==销户==</option>
					</select>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align="right">客户类型</td>
				<td>
					<select name="customer_type" class="bk">
						<option value="-1">==请选择==</option>
						<option value="1">==企业单位==</option>
						<option value="2">==事业单位==</option>
					</select>
				</td>
				<td class=column nowrap align="right">客户规模</td>
				<td>
					<select name="customer_size" class="bk">
						<option value="-1">==请选择==</option>
						<option value="0">==小==</option>
						<option value="1">==中==</option>
						<option value="2">==大==</option>
					</select>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align="right">局向</td>
				<td>
					<select name="office_id" class="bk">
						<option value="-1">==请选择==</option>
						<s:iterator value="officeList">
							<option value="<s:property value="office_id" />">==<s:property value="office_name" />==</option>
						</s:iterator>
					</select>
				</td>
				<td class=column nowrap align="right">小区</td>
				<td>
					<select name="zone_id" class="bk">
						<option value="-1">==请选择==</option>
						<s:iterator value="zoneList">
							<option value="<s:property value="zone_id" />">==<s:property value="zone_name" />==</option>
						</s:iterator>
					</select>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align="right">客户地址</td>
				<td colspan='3'><input type="text" name="customer_address" value="" class="bk" class="bk" size="80" maxlength=80></td>
				
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="4" align="right" class="green_foot">
					<input type="button" value=" 保 存 " onclick="checkForm()">
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="20">&nbsp;</td>
	</tr>
</table>
</form>
</body>
</html>
