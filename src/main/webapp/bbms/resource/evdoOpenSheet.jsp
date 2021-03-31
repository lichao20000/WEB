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
	var _uimImsi = $("input[@name='uimImsi']");
	var _dataEsn = $("input[@name='dataEsn']");
	var _serialCode = $("select[@name='serialCode']");
	var _vci = $("input[@name='vci']");
	var _vpi = $("input[@name='vpi']");
	var _vlanid = $("input[@name='vlanid']");
	var _custType = $("select[@name='custType']");
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
	//用户账号或专线号
	if(!IsNull(_username.val(), "用户账号或专线号")){
		_username.focus();
		return false;
	}
	//PVC和VLAN
	if('1' == _orderType.val() || '6' == _orderType.val() || 1 == _orderType.val() || 6 == _orderType.val()){
		if(!IsNumber(_vpi.val(),"vpi")){
			_vpi.focus();
			return false;
		}
		if(!IsNumber(_vci.val(),"vci")){
			_vci.focus();
			return false;
		}
	}else{
		if(!IsNumber(_vlanid.val(),"VlanID")){
			_vlanid.focus();
			return false;
		}
	}
	//数据卡ESN号码
	if(!IsNull(_dataEsn.val(), "数据卡ESN号码")){
		_dataEsn.focus();
		return false;
	}
	//UIM卡IMSI号码
	if(!IsNull(_uimImsi.val(), "UIM卡IMSI号码")){
		_uimImsi.focus();
		return false;
	}
	//套餐
	if('' == _serialCode.val() || '-1' == _serialCode.val()){
		alert("请选择套餐");
		_serialCode.focus();
		return false;
	}
	//客户ID
	if(!IsNull(_customerId.val(), "客户ID")){
		_customerId.focus();
		return false;
	}
	//属地
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("请选择属地");
		_cityId.focus();
		return false;
	}
	//行业类别
	if('' == _custType.val() || '-1' == _custType.val()){
		alert("请选择行业类别");
		_custType.focus();
		return false;
	}
/**
	//客户名称
	if(!IsNull(_customerId.val(), "客户名称")){
		_customerId.focus();
		return false;
	}
	**/
}

//-->
</script>
</head>
<body>
<form name="frm" action="sendBssSheet!sendSheet.action" onsubmit="return CheckForm();" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="green_title">
						<TD colspan="4">
						<input type="hidden" name="servTypeId" value='<s:property value="servTypeId" />'>
						<input type="hidden" name="operateType" value='<s:property value="operateType" />'>
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<TD><font size="2">业务相关区域</font></TD>
								<TD align="right"><IMG SRC="images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
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
					
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>用户帐户/专线号</TD>
						<TD>
							<INPUT TYPE="text" NAME="username" maxlength=50 class=bk 
								value="">&nbsp;
							<font color="#FF0000">* </font>
						</TD>
						<TD class=column align='right' nowrap>用户密码</TD>
						<TD><input type='text' name='passwd' class=bk
							value="<s:property value="passwd"/>" maxlength=50>&nbsp;
						</TD>
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align="right" id="id_pvc1">VPI/VCI</TD>
						<TD id="id_pvc2"><INPUT TYPE="text" NAME="vpi"
							value="<s:property value="vpi"/>" maxlength=3 size=5 class=bk>/
						<INPUT TYPE="text" NAME="vci" value="<s:property value="vci"/>"
							maxlength=3 size=5 class=bk>&nbsp; <font color="#FF0000">* ADSL方式时必须</font>
						</TD>
						<TD class=column align="right" id="id_vlan1" >VlanID</TD>
						<TD id="id_vlan2" ><INPUT TYPE="text"
							NAME="vlanid" class=bk value="<s:property value="vlanid"/>">&nbsp;
						<font color="#FF0000">*</font></TD>
					</TR>
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align='right' nowrap>IP地址</TD>
						<TD><input type='text' name='ip' class=bk
							value="<s:property value="ip"/>">&nbsp; 
						</TD>
						<TD class=column align='right' nowrap>掩码</TD>
						<TD><INPUT TYPE='text' NAME='mask' class=bk
							value="<s:property value="mask"/>">&nbsp; 
						</TD>
					</TR>
					<TR id="id_static2" bgcolor="#FFFFFF">
						<TD class=column align='right' nowrap>网关</TD>
						<TD><input type='text' name='gateway' class=bk
							value="<s:property value="gateway"/>">&nbsp;
						</TD>
						<TD class=column align='right' nowrap>DNS</TD>
						<TD><INPUT TYPE='text' NAME='dns' class=bk
							value="<s:property value="dns"/>">&nbsp; 
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">最大下行速率</TD>
						<TD><INPUT TYPE="text" NAME="maxDownSpeed" class=bk
							maxlength=20 value="<s:property value="maxDownSpeed"/>">
						&nbsp;</TD>
						<TD class=column align="right">最大上行速率</TD>
						<TD><INPUT TYPE="text" NAME="maxUpSpeed" class=bk
							maxlength=20 value="<s:property value="maxUpSpeed"/>">
						&nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">允许用户上网数</TD>
						<TD><INPUT TYPE="text" NAME="maxUserNum" maxlength=5 class=bk
							value="<s:property value="maxUserNum"/>"></TD>
						<TD class=column align="right">套餐类型</TD>
						<TD>
							<select NAME="serialCode" class="bk">
								<option value="-1">==请选择==</option>
								<option value="TC001">==XTa==</option>
								<option value="TC002">==XTa+==</option>
								<option value="TC003">==XTb==</option>
								<option value="TC004">==XTb+==</option>
								<option value="TC005">==其他-A系统==</option>
								<option value="TC006">==其他-A系统==</option>
							</select>
							&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<!-- 设备信息 -->
					<TR class="green_title">
						<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<TD><font size="2">设备相关区域</font></TD>
								<TD align="right"><IMG SRC="images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align='right' nowrap>OUI</TD>
						<TD><input type='text' name='oui' class=bk
							value="<s:property value="oui"/>" maxlength=50>
						</TD>
						<TD class=column align='right' nowrap>序列号</TD>
						<TD><INPUT TYPE='text' NAME='devSn' class=bk
							value="<s:property value="devSn"/>" maxlength=50>
						</TD>
					</TR>
					<!-- 
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align='right' nowrap>终端类型</TD>
						<TD><input type='text' name='ip' class=bk
							value="<s:property value="ip"/>">&nbsp;</TD>
						<TD class=column align='right' nowrap>设备型号</TD>
						<TD><INPUT TYPE='text' NAME='mask' class=bk
							value="<s:property value="mask"/>">&nbsp;</TD>
					</TR>
					 -->
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">数据卡ESN号码</TD>
						<TD><INPUT TYPE="text" NAME="dataEsn" class=bk
							value="<s:property value="dataEsn"/>" maxlength=50>
						&nbsp;<font color="#FF0000">* </font></TD>
						<TD class=column align="right">数据卡型号</TD>
						<TD><INPUT TYPE="text" NAME="dataModel" class=bk
							value="<s:property value="email"/>" maxlength=50> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">UIM卡IMSI号码</TD>
						<TD><INPUT TYPE="text" NAME="uimImsi" class=bk
							value="<s:property value="uimImsi"/>" maxlength=50>
						&nbsp;<font color="#FF0000">* </font></TD>
						<TD class=column align="right">UIM卡ICCID号码</TD>
						<TD><INPUT TYPE="text" NAME="uimIccid" class=bk
							value="<s:property value="uimIccid"/>" maxlength=50>
						&nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">UIM卡手机号码</TD>
						<TD><INPUT TYPE="text" NAME="uimMobile" class=bk
							value="<s:property value="uimMobile"/>" maxlength=50>
						&nbsp;</TD>
						<TD class=column align="right">工作模式</TD>
						<TD><select NAME="workMode" class=bk>
								<option value="2">==备用模式==</option>
								<option value="1">==主用模式==</option>
							</select>
						&nbsp;</TD>
					</TR>
					<TR class="green_title">
						<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<TD><font size="2">客户相关区域</font></TD>
								<TD align="right"><IMG SRC="images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>客户ID</TD>
						<TD >
							<INPUT TYPE="text" NAME="customerId" maxlength=50 class=bk 
								value="">&nbsp;
							<font color="#FF0000">* </font>
						</TD>
						<TD class=column align="right">属地</TD>
						<TD><select name="cityId" class="bk">
							<option value="-1">==请选择==</option>
							<s:iterator value="cityList">
								<option value="<s:property value="city_id" />">==<s:property
									value="city_name" />==</option>
							</s:iterator>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">客户名称</TD>
						<TD><INPUT TYPE="text" NAME="customerName" maxlength=50
							class=bk value="<s:property value="customerName"/>">
						&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">行业类别</TD>
						<TD>
							<select name="custType" class="bk">
								<option value="-1">==请选择==</option>
								<s:iterator value="custTypeList">
								<option value="<s:property value="cust_type_name" />">==<s:property
									value="cust_type_name" />==</option>
								</s:iterator>
							</select>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">客户地址</TD>
						<TD colspan="3"><INPUT TYPE="text" NAME="customerAddr" class=bk
							value="<s:property value="customerAddr"/>" maxlength=200 size=100>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">企业账号</TD>
						<TD><INPUT TYPE="text" NAME="customerAccount" class=bk
							value="<s:property value="customerAccount"/>" maxlength=50>
						&nbsp;</TD>
						<TD class=column align="right">企业密码</TD>
						<TD><INPUT TYPE="text" NAME="customerPasswd" class=bk
							value="<s:property value="customerPasswd"/>" maxlength=50>
						&nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">联系人姓名</TD>
						<TD><INPUT TYPE="text" NAME="linkman" class=bk
							value="<s:property value="linkman"/>" maxlength=50>
						&nbsp;</TD>
						<TD class=column align="right">联系电话</TD>
						<TD><INPUT TYPE="text" NAME="linkphone" class=bk
							value="<s:property value="linkphone"/>" maxlength=50>
						&nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">联系人手机</TD>
						<TD><INPUT TYPE="text" NAME="mobile" class=bk
							value="<s:property value="mobile"/>" maxlength=50> &nbsp;</TD>
						<TD class=column align="right">联系人email</TD>
						<TD><INPUT TYPE="text" NAME="email" class=bk
							value="<s:property value="email"/>" maxlength=50> &nbsp;</TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class="green_foot">
							<INPUT TYPE="submit" value=" 发送工单 " class="btn">
							<input type="hidden" name="devType" value="">
							<input type="hidden" name="devModel" value="">
							<input type="hidden" name="officeId" value="">
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