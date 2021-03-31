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
//提交验证
function checkForm(){

	if ($("input[@name='customer_id']").val() == ""){
		alert("请输入客户ID！");
		$("input[@name='customer_id']").focus();
		return;
	}
	if ($("input[@name='customer_name']").val() == ""){
		alert("请输入客户名称！");
		$("input[@name='customer_name']").focus();
		return;
	}
	
	//if ($("input[@name='customer_account']").val() == ""){
	//	alert("请输入客户账号！");
	//	$("input[@name='customer_account']").focus();
	//	return;
	//}
	if ($("input[@name='linkman']").val() == ""){
		alert("请输入联系人！");
		$("input[@name='linkman']").focus();
		return;
	}
	if ($("input[@name='linkphone']").val() == ""){
		alert("请输入联系人电话！");
		$("input[@name='linkphone']").focus();
		return;
	}
	if ($("select[@name='city_id']").val() == "-1"){
		alert("请选择属地！");
		$("select[@name='city_id']").focus();
		return;
	}
	
	if (!IsNumber($("input[@name='linkphone']").val(),"联系人电话")){
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

function confirmCustomerId(){
	var customer_id = $("input[@name='customer_id']").val();
	if ( customer_id == ""){
		alert("请输入客户ID！");
		$("input[@name='customer_id']").focus();
		return;
	}
	var url = "<s:url value='/bbms/CustomerInfo!comfirmCustomerId.action'/>";
	$.post(url,{
		customer_id:customer_id
	},function(mesg){
		if("true"==mesg){
			alert("该客户ID已经存在，请确认输入是否正确!");
			$("input[@name='customer_id']").focus();
		}else{
			alert("该客户ID不存在，可以添加!");
		}
	});
}

</script>
</head>

<body>
<form name="frm" action="<s:url value='/bbms/CustomerInfo!addCustomer.action'/>" method="post">
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
						&nbsp;<img src="../images/attention_2.gif" width="15" height="12">	
						&nbsp;带<font color="red">*</font>必须填写
					</td>
					<td align="right">
						
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
				<th colspan="4">新增</th>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column width="15%" nowrap align='right'>客户ID</td>
				<td width="35%"><input type="text" name="customer_id" value="" class="bk" class="bk">&nbsp;<font color="red">*客户ID必须与CRM一致</font></td>
				<td class=column nowrap colspan="2"><input type="button" value="检查客户ID" onclick="confirmCustomerId()" style="border: 1px solid #999999;cursor:hand"></td>
			</tr>
			<!--  <tr bgcolor="#FFFFFF">
				<td class=column width="15%" nowrap>客户账号：</td>
				<td width="35%"><input type="text" name="customer_account" value="" class="bk" maxlength=20>&nbsp;<font color="red">*</font></td>
				<td class=column nowrap>客户密码：</td>
				<td><input type="text" name="customer_pwd" value="123456" class="bk" maxlength=16>&nbsp;<font color="red">*</font></td>
			</tr>
			-->
			<tr bgcolor="#FFFFFF">
				<td class=column width="15%" nowrap align='right'>客户名称</td>
				<td width="35%"><input type="text" name="customer_name" value="" class="bk" size="40" maxlength=50>&nbsp;<font color="red">*</font></td>
				<td class=column nowrap align='right'>属地</td>
				<td>
					<select name="city_id" class="bk">
						<option value="-1">==请选择==</option>
						<s:iterator value="cityList">
							<option value="<s:property value="city_id" />">==<s:property value="city_name" />==</option>
						</s:iterator>
					</select>&nbsp;<font color="red">*</font>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align='right'>联系人</td>
				<td><input type="text" name="linkman" value="" class="bk" maxlength=20>&nbsp;<font color="red">*</font></td>
				<td class=column nowrap align='right'>联系电话</td>
				<td ><input type="text" name="linkphone" value="" class="bk" maxlength="20" maxlength=20>&nbsp;<font color="red">*</font></td>
			</tr>
			
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align='right'>E-mail</td>
				<td><input type="text" name="email" value="" class="bk" maxlength=40></td>
				<td class=column nowrap align='right'>客户状态</td>
				<td>
					<select name="customer_state" class="bk">
						<option value="1">==开通==</option>
						<!-- <option value="2">==暂停==</option>
						<option value="3">==销户==</option> -->
					</select>
				</td>
			</tr>
			
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align='right'>客户类型</td>
				<td>
					<select name="customer_type" class="bk">
						<option value="-1">==请选择==</option>
						<option value="1" selected>==企业单位==</option>
						<option value="2">==事业单位==</option>
					</select>
				</td>
				<td class=column nowrap align='right'>客户规模</td>
				<td>
					<select name="customer_size" class="bk">
						<option value="0">==小==</option>
						<option value="1">==中==</option>
						<option value="2">==大==</option>
					</select>
				</td>
			</tr>
			
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align='right'>局向</td>
				<td>
					<select name="office_id" class="bk">
						<option value="-1">==请选择==</option>
						<s:iterator value="officeList">
							<option value="<s:property value="office_id" />">==<s:property value="office_name" />==</option>
						</s:iterator>
					</select>
				</td>
				<td class=column nowrap align='right'>小区</td>
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
				<td class=column nowrap align='right'>客户地址</td>
				<td colspan="3"><input type="text" name="customer_address" value="" class="bk" size="80" maxlength=80></td>
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
<%@ include file="../foot.jsp"%>
</html>
