<%--
模拟BSS工单 EVDO开户工单
Author: Jason
Version: 1.0.0
Date: 2009-10-13
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="../../Js/jsDate/WdatePicker.js"></script>
<title>BSS模拟工单</title>
<script type="text/javascript">
<!--//

function CheckForm(){
	var _servTypeId = $("input[@name='servTypeId']");
	var _operateType = $("input[@name='operateType']");
	var _username = $("input[@name='username']");
	var _customerId = $("input[@name='customerId']");
	var _cityId = $("select[@name='cityId']");
	var _orderType = $("select[@name='orderType']");
	var _dealdate = $("input[@name='dealdate']");

	//业务类型
	if('' == _servTypeId.val() || '-1' == _servTypeId.val()){
		alert("请选择业务类型");
		_servTypeId.focus();
		return false;
	}
	//操作类型
	if('' == _operateType.val() || '-1' == _operateType.val()){
		alert("请选择操作类型");
		_operateType.focus();
		return false;
	}
	//订单方式
	if('' == _orderType.val() || '-1' == _orderType.val()){
		alert("请选择订单类型");
		_orderType.focus();
		return false;
	}
	//受理时间
	if(!IsNull(_dealdate.val(), "受理时间")){
		_dealdate.focus();
		return false;
	}
	//客户ID
	if(!IsNull(_customerId.val(), "客户ID")){
		_customerId.focus();
		return false;
	}
	//用户账号或专线号
	if(!IsNull(_username.val(), "用户账号或专线号")){
		_username.focus();
		return false;
	}
	//属地
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("请选择属地");
		_cityId.focus();
		return false;
	}
	//alert(_servTypeId.val());
	//return false;
}

//-->
</script>
</head>
<body>
<form name="frm" action="sendBssSheet!sendSheet.action" onsubmit="return CheckForm();" method="post" target="_self">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<input type="hidden" name="servTypeId" value='<s:property value="servTypeId" />'>
				<input type="hidden" name="operateType" value='<s:property value="operateType" />'>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">订单类型</TD>
						<TD width="30%"><select name="orderType" class="bk">
							<option value="-1">==请选择==</option>
							<option value="1">==普通ADSL==</option>
							<option value="2">==普通LAN==</option>
							<option value="3">==普通光纤==</option>
							<option value="4">==专线LAN==</option>
							<option value="5">==专线光纤==</option>
							<option value="6">==专线ADSL==</option>
							</select> &nbsp; <font color="#FF0000">*</font>
						</TD>
						<TD class=column align="right" width="20%">工单受理时间</TD>
						<TD width="30%"><input type="text" name="dealdate"
							value='<s:property value="dealdate" />' readonly class=bk>
						<img name="shortDateimg"
							onclick="new WdatePicker(document.frm.dealdate,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
							src="../../images/search.gif" width="15" height="12" border="0"
							alt="选择">&nbsp; <font color="#FF0000">*</font></TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>客户ID</TD>
						<TD >
							<INPUT TYPE="text" NAME="customerId" maxlength=50 class=bk 
								value="">&nbsp;
							<font color="#FF0000">* </font>
						</TD>
						<TD class=column align="right">属地</TD>
						<TD>
							<select name="cityId" class="bk">
							<option value="-1">==请选择==</option>
							<s:iterator value="cityList">
								<option value="<s:property value="city_id" />">==<s:property value="city_name" />==</option>
							</s:iterator>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>用户帐户/专线号</TD>
						<TD colspan="3">
							<INPUT TYPE="text" NAME="username" maxlength=50 class=bk 
								value="">&nbsp;
							<font color="#FF0000">* </font>
						</TD>
					</TR>
					
					<TR>
						<TD colspan="4" align="right" class="green_foot">
							<INPUT TYPE="submit" value=" 发送工单 " class="btn">
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
</body>
</html>