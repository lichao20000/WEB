<%--
�������-�߼���ѯ-X_COM_PASSWD��
Linkage Technology (NanJing) Co., Ltd
Copyright 2008-2012. All right reserved.
Author: Alex.Yan(yanhj@lianchuang.com)
Version: 1.0.0
Date: 2009-7-9
Desc:ά������.
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='../../Js/inmp/jquery.js'/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="../../Js/inmp/commFunction.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<s:property value="gw_type"/>';

<!--
$(function(){
	init();
});
//get data
function init() {
	
	parent.unblock1();

	var xcompasswd = $("input[@name='xcompasswd']");

	document.all("operResult").style.display = "";
	$("div[@id='operResult']").html("");
	$("div[@id='operResult']").html("���ݲɼ�:");
	$("div[@id='operResult']").append("<s:property value='result'/>");
	
	xcompasswd.val("<s:property value='xcompasswd'/>");

	setTimeout("clearResult()", 3000);
	
	<%-- ��ǰ�û�����˳�������Ա���밴ť��תҳ��󣬼�¼����Ȩ����־ --%>
	superAuthLog('ShowDevPwd',"�鿴[<s:property value='deviceSn'/>]�豸�ĵ���ά������[<s:property value='xcompasswd'/>]");
}

//change value
function chgmode()
{
	var mode = $("select[@name='mode']");
	var xcompasswd = $("input[@name='xcompasswd']");
	var randompasswd = $("input[@name='randompasswd']");
	if (mode.val() == "0")
	{
		xcompasswd.val("");
		xcompasswd.attr("readonly", false);
	} 
	else if (mode.val() == "1")
	{
		xcompasswd.val("telecomadmin");
		xcompasswd.attr("readonly", true);
	}
	else {
		xcompasswd.val(randompasswd.val());
		xcompasswd.attr("readonly", true);
	}
}

//config.
function CheckPost() {
	var deviceId = $("input[@name='deviceId']").val();
	var xcompasswd = $("input[@name='xcompasswd']").val();
	var strategyType = $("select[@name='strategyType']").val();
	
	if (xcompasswd.length < 8 || xcompasswd.length > 20)
	{
		alert("���볤�ȱ���8-20λ");
		return false;
	}

	var url = "<s:url value='/inmp/diagnostics/xcompasswdACT!config.action'/>";
	$.post(url,{
		deviceId:deviceId,
		xcompasswd:xcompasswd,
		strategyType:strategyType,
		gw_type:gw_type
	},function(ajax){
		document.all("operResult").style.display = ""
		$("div[@id='operResult']").html("");
		$("div[@id='operResult']").html("֪ͨ��̨:");
		$("div[@id='operResult']").append("<s:property value='result'/>");
	});

	setTimeout("clearResult()", 3000);
}

function clearResult() {
	$("div[@id='operResult']").html("");
	document.all("operResult").style.display = "none"
}

//-->
</SCRIPT>

<BODY>

<FORM NAME="frm" METHOD="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor=#ffffff>
		<td align=right>
			<div id="operResult" style='width:20%;display:none;background-color:#33CC00'></div>
		</td>
	</tr>
	<TR>
		<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
				<TR align="left">
					<TH colspan="1">����</TH>
					<TH colspan="1">���Է�ʽ</TH>
					<TH colspan="1">����</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD width="40%" align="center">
						<SELECT NAME="mode" onchange="chgmode()">
							<OPTION VALUE="0" SELECTED>�ֹ�����
							<OPTION VALUE="1">Ĭ������
							<OPTION VALUE="2">�������
						</SELECT>&nbsp;
						<INPUT TYPE="text" NAME="xcompasswd"  value= "0" size="20" maxlength="20">
						&nbsp;<font color="red">*</font></TD>
					<TD class=column align="center" width="20%">
						<SELECT NAME="strategyType">
							<OPTION VALUE="0" SELECTED>����ִ��
							<!-- <OPTION VALUE="1">��һ������ϵͳ -->
							<OPTION VALUE="2">�����ϱ�
							<!-- <OPTION VALUE="3">�������� -->
							<OPTION VALUE="4">�´�����ϵͳ
							<OPTION VALUE="5">�豸����
						</SELECT>&nbsp;<font color="red">*</font></TD>
					<TD class=column>���뽨��������ɣ���Ϊ��ȫ�����볤��8-20λ.</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="3" class="green_foot" align=right>
					<INPUT NAME="deviceId" TYPE="hidden"  value="<s:property value='deviceId'/>">
					<INPUT NAME="randompasswd" TYPE="hidden"  value="<s:property value='randompasswd'/>">
					<input type="button" value=" �� �� " class="jianbian" onclick="javascript:CheckPost();">&nbsp;&nbsp;<input type="reset" value=" �� �� " class="jianbian"></TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>

</BODY>


