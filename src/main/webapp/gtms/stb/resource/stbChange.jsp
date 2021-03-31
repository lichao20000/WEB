<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>更换机顶盒</title>
<link href="<s:url value="/css3/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css3/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	// 根据业务账号或者mac地址去获取列表
	function getMacList() {
		var servAccount = $("input[name=servAccount]").val();
		var oldMac = $("input[name=oldMac]").val();
		if ("" == oldMac && "" == servAccount) {
			alert("业务账号与MAC地址不可同时为空");
			return;
		}
		if(oldMac.indexOf(":")>-1){
		　　alert('MAC地址中不可包含冒号');
			return;
		}

		var url = "<s:url value='/gtms/stb/resource/stbChange!getMacList.action'/>";
		$.post(url, {
			deviceMac : oldMac,
			servAccount : servAccount
		}, function(ajax) {
			$("input[@name='userId']").val("");
			$("input[@name='checkServAccount']").val("");
			$("input[@name='checkOldMac']").val("");
			$("input[@name='checkCityId']").val("");
			$("input[@name='deviceMac']").val("");
			$("div[@id='div_user']").html("");
			$("div[@id='div_user']").append(ajax);
		});
		document.all("tr_userinfo").style.display="";
		document.all("newMac").style.display="";
	}

	function trim(str) {
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	function change() {
		var deviceMac = $("input[name=deviceMac]").val();
		var servAccount = $("input[name=checkServAccount]").val();
		var oldMac = $("input[name=checkOldMac]").val();
		var cityId = $("input[name=checkCityId]").val();
		if ("" == deviceMac) {
			alert("新MAC地址不可为空！");
			return;
		}
		if (deviceMac.length != 12) {
			alert("新MAC地址必须为12位！");
			return;
		}
		if(deviceMac.indexOf(":")>-1){
		　　alert('新MAC地址中不可包含冒号');
			return;
		}
		if ("" == oldMac) {
			alert("旧MAC地址不可为空！");
			return;
		}
		if ("" == servAccount) {
			alert("业务账号不可为空！");
			return;
		}
		var url = "<s:url value='/gtms/stb/resource/stbChange!change.action'/>";
		$.post(url, {
			deviceMac : deviceMac,
			oldMac : oldMac,
			cityId : cityId,
			servAccount : servAccount
		}, function(ajax) {
			alert(ajax);
		});
	}

	//针对选中用户时需要提交的数据
	function userOnclick(user_id,cpe_mac,serv_account,city_id){
		$("input[@name='userId']").val(user_id);
		$("input[@name='checkServAccount']").val(serv_account);
		$("input[@name='checkOldMac']").val(cpe_mac);
		$("input[@name='checkCityId']").val(city_id);
	}
</SCRIPT>
<style>
span {
	position: static;
	border: 0;
}
</style>
</head>
<body>
	<form name="frm" id="frm" target="dataForm">
		<!-- 隐藏需要提交项 -->
		<input type="hidden" value="" name="userId">
		<input type="hidden" value="" name="checkServAccount">
		<input type="hidden" value="" name="checkOldMac">
		<input type="hidden" value="" name="checkCityId">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">更换机顶盒</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /><font color="red">*</font></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<TABLE width="100%" class="querytable" align="center">
						<tr>
							<th colspan="2" class="title_1">更换机顶盒</th>
						</tr>
						<tr>
							<TD align="right" class="column" width="15%">业务账号</TD>
							<TD width="35%"><input type="text" id="servAccount"
								name="servAccount" class="bk" value=""> <font
								color="red">*业务账号支持模糊匹配</font></TD>
						</tr>
						<tr>
							<TD align="right" class="column" width="15%">旧MAC地址</TD>
							<TD width="35%"><input type="text" id="oldMac" name="oldMac"
								class="bk" value=""> <font color="red">*MAC地址支持后匹配，不可包含冒号</font></TD>
						</tr>
						<tr>
							<td colspan="2" align="right" class="foot" width="100%">
								<div align="right">
									<input type ='button' onclick="javascript:getMacList();" name="changeButton"
										id="changeButton" style="CURSOR: hand" value='下一步'></input>
								</div>
							</td>
						</tr>
					</TABLE>
				</td>
			</tr>
			<tr style="display: none" id="tr_userinfo">
				<td>
					<div id="div_user" style="width: 100%; z-index: 1; top: 100px">
					</div>
				</td>
			</tr>
			<tr>
<!-- 			<td><iframe id="dataForm" name="dataForm" height="0" -->
<!-- 					frameborder="0" scrolling="no" width="100%" src=""></iframe></td> -->
<!-- 			</tr> -->
			<tr style="display: none" id="newMac">
				<td>
					<TABLE width="100%" class="querytable" align="center">
						<tr>
							<TD align="right" class="column" width="15%">MAC地址</TD>
							<TD width="35%"><input type="text" id="deviceMac"
								name="deviceMac" class="bk" value=""> <font color="red">*新MAC地址必须为12位</font>
							</TD>
						</tr>
						<tr>
							<td colspan="2" align="right" class="foot" width="100%">
								<div align="right">
									<input type='button' onclick="javascript:change();" name="changeButton"
										id="changeButton" style="CURSOR: hand" value='更 换'></input>
								</div>
							</td>
						</tr>
					</TABLE>
				</td>
			</tr>
		</table>
	</form>
</body>