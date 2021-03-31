<%--
山西联通机顶盒手工工单
Author: lsr
Version: 1.0.0
Date: 2020-01-10
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<!DOCTYPE html>
<HEAD>
<title>机顶盒手工工单下发</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../../css/easyform.css" rel="stylesheet" type="text/css">
<link href="../../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery-1.11.3.min.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="../../Js/jquery.serializeExtend-1.0.1.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
	var loginCityId = "-1";
	var isExist = false;
	String.prototype.replaceAll = function(oldStr, newStr) {
		return this.replace(new RegExp(oldStr, "gm"), newStr);
	}

	// IE 兼容assign方法
	if (typeof Object.assign != 'function') {
		Object.assign = function(target) {
			'use strict';
			if (target == null) {
				throw new TypeError(
						'Cannot convert undefined or null to object');
			}

			target = Object(target);
			for (var index = 1; index < arguments.length; index++) {
				var source = arguments[index];
				if (source != null) {
					for ( var key in source) {
						if (Object.prototype.hasOwnProperty.call(source, key)) {
							target[key] = source[key];
						}
					}
				}
			}
			return target;
		};
	}

	$(function() {
		var gw_type = '<s:property value="gw_type" />';
		loginCityId = '<s:property value="cityId" />';
		$("#cityId option[value='00']").prop("selected", true);
		setTime();
	});

	//检查LOID是否存在
	function checkLoid() {
		if ("" == $("input[name='userID']").val().trim()) {
			alert("业务账号不可为空！");
			$("input[name='userID']").focus();
			return false;
		}

		//业务触发
		var url = "<s:url value='/itms/service/bssSheetByHand4GSDX_stb!checkStbServAccount.action'/>";
		$.post(url, {
			"userID" : $("input[name='userID']").val().trim()
		}, function(ajax) {
			if ("000" == ajax) {
				alert("唯一标识可以使用。");
			} else {
				isExist = true;
				var context = $.parseJSON(ajax);
				var source = $("#net-info-template").html();
				// 赋值给资料用户 属地，设备类型
				var userInfo = context;
				//$("#cityId option[value='" + userInfo.cityId + "']").prop("selected", true);
				// $("select[name='deviceType']").val(userInfo.deviceType);
				$('#jirRuBssSheet').fillData(userInfo);
			}
		});
	}

	//业务销户提交
	function delBusiness() {
		if ("" == $("input[name='userID']").val().trim()) {
			alert("唯一标识不可为空。");
			$("input[name='userID']").focus();
			return false;
		}
		if ("-1" == $("select[name='cityId']").val()) {
			alert("属地不可为空。");
			$("input[name='cityId']").focus();
			return false;
		}

		var stbUserID = "";
		var stbUserPwd = "";
		var serverInfoObj = {};
		var userInfoObj = $('#jirRuBssSheet').getJsonData();
		// 获取机顶盒用户信息
		var stbInfoObj = $('#stbSheet').getJsonData();
		stbInfoObj.action = 3;
		stbUserID = stbInfoObj.userID;

		if (stbUserID == null || stbUserID == '') {
			alert("机顶盒账号值不能为空。");
			return false;
		}

		var r = confirm("确定要删除机顶盒业务吗 ？");
		if (r == false) {
			return;
		}

		//业务触发
		var url = "<s:url value='/itms/service/bssSheetByHand4GSDX_stb!stbDoBusiness.action'/>";
		$.post(url, stbInfoObj, function(ajax) {
			alert(ajax);
			$("button[name='delBtn']").attr("disabled", true);
			reset();
		});
		//灰化按钮
		$("button[name='delBtn']").attr("disabled", true);
	}

	//开户业务提交
	function doBusiness() {
		if ("" == $("input[name='userID']").val().trim()) {
			alert("唯一标识不可为空。");
			$("input[name='userID']").focus();
			return false;
		}
		if ("-1" == $("select[name='cityId']").val()) {
			alert("属地不可为空。");
			$("select[name='cityId']").focus();
			return false;
		}
		if ("" == $("input[name='password']").val().trim()) {
			alert("业务密码不可以为空");
			$("input[name='password']").focus();
			return false;
		}

		if ("" == $("input[name='macAddress']").val().trim()) {
			alert("MAC地址不可以为空");
			$("input[name='macAddress']").focus();
			return false;
		}

		if ("-1" == $("select[name='platform']").val()) {
			alert("终端接入类型不可为空。");
			$("select[name='platform']").focus();
			return false;
		}

		var stbUserID = "";
		var stbUserPwd = "";
		var serverInfoObj = {};
		var userInfoObj = $('#jirRuBssSheet').getJsonData();
		if (isExist) {
			userInfoObj.action = 2;
		} else {
			userInfoObj.action = 1;
		}

		var r = confirm("确定要提交机顶盒业务吗 ？");
		if (r == false) {
			return;
		}
		//业务触发
		var url = "<s:url value='/itms/service/bssSheetByHand4GSDX_stb!stbDoBusiness.action'/>";
		$.post(url, userInfoObj, function(ajax) {
			alert(ajax);
			//灰化按钮
			$("button[name='subBtn']").attr("disabled", false);
			checkLoid();
		});
		//灰化按钮
		$("button[name='subBtn']").attr("disabled", true);
	}
</script>
</HEAD>
<body>
	<FORM NAME="frm" action="" method="post">
		<TABLE id="myTable" style="width:100%;border: 0;cellpadding:0;cellspacing:0">
			<th id="thHeader"></th>
			<TR>
				<TD style="height:20">&nbsp;</TD>
			</TR>
			<TR>
				<TD>
					<TABLE style="width:98%;border:0;cellspacing:0;cellpadding:0;align:center">
						<th id="secHeader"></th>
						<TR>
							<TD colspan="4">
								<table width="100%" height="30" border="0" cellspacing="0"
									cellpadding="0" class="green_gargtd">
									<tr>
										<td width="162">
											<div align="center" class="title_bigwhite">手工工单</div>
										</td>
										<td><img src="../../images/attention_2.gif" width="15"
											height="12"></td>
									</tr>
								</table>
							</TD>
						</TR>

						<TR>
							<TD colspan="4" bgcolor="#999999">
								<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
									<tr align="left">

										<td colspan="4" class="green_title_left">用户资料工单</td>
									</tr>
									<tbody id="jirRuBssSheet">
										<TR bgcolor="#FFFFFF">
											<input type="hidden" id="userServTypeId"
												name="userServTypeId" value="20">
											<input type="hidden" id="userOperateId" name="userOperateId"
												value="1">
											<input type="hidden" name="cmdId" value="FROMWEB-0000001">
											<input type="hidden" name="authUser" value="itms">
											<input type="hidden" name="authPwd" value="123">
											<input type="hidden" name="userType" class=bk value="1">
											<TD class=column align="right" nowrap width="15%">受理时间：</TD>
											<TD width="35%"><input type="text" name="dealtime"
												class=bk value=""> <font color="#FF0000">*</font></TD>
											<TD width="15%" class=column align="right">属地:</TD>
											<TD width="35%"><s:select list="cityList" name="cityId"
													headerKey="-1" headerValue="请选择属地" listKey="city_id"
													listValue="city_name" value="cityId" cssClass="bk"></s:select>
												<font color="#FF0000">*</font></TD>
										</TR>
										<TR bgcolor="#FFFFFF">
											<TD width="15%" class=column align="right">业务账号:</TD>
											<TD width="35%"><input type="text" id="userID"
												name="userID" class=bk value="">&nbsp;
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<button type="button" name="subButton" onclick="checkLoid()">检测唯一标识是否存在</button>
											</TD>
											<TD class=column align="right" nowrap width="15%">业务密码:</TD>
											<TD width="35%"><input type="text" name="password"
												class=bk value=""> <font color="#FF0000">*</font></TD>
										</TR>
										<TR bgcolor="#FFFFFF">
											<TD class=column align="right" nowrap width="15%">mac地址:</TD>
											<TD width="35%"><input type="text" name="macAddress"
												class=bk value=""></TD>
											<TD class=column align="right" nowrap width="15%">平台:</TD>
											<TD width="35%"><select name='platform' class='bk'>
													<option value='-1'>请选择平台</option>
													<option value='1'>华为平台</option>
													<option value='2'>中兴平台</option>
											</select></TD>
										</TR>
									</tbody>
									<TR align="left" id="doBiz">
										<TD colspan="4" class=foot align="right" nowrap>
											<button type="button" name="subBtn" onclick="doBusiness()">开&nbsp;&nbsp;户
											</button>
											<button type="button" name="subBtn" onclick="delBusiness()">销&nbsp;&nbsp;户
											</button>
											<button type="button" name="subBtn" onclick="reset()">重&nbsp;&nbsp;置</button>
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</FORM>
</body>
<script type="text/javascript">
	function setTime() {
		var dstr = "";
		var d = new Date();

		dstr += d.getFullYear();
		if (d.getMonth() + 1 < 10) {
			dstr += '0';
			dstr += d.getMonth() + 1;
		} else {
			dstr += d.getMonth() + 1;
		}

		if (d.getDate() < 10) {
			dstr += '0';
			dstr += d.getDate();
		} else {
			dstr += d.getDate();
		}

		if (d.getHours() < 10) {
			dstr += '0';
			dstr += d.getHours();
		} else {
			dstr += d.getHours();
		}

		if (d.getMinutes() < 10) {
			dstr += '0';
			dstr += d.getMinutes();
		} else {
			dstr += d.getMinutes();
		}

		dstr += "00";
		$("input[name='dealtime']").val(dstr);
	}

	function reset() {
		cleanValue();
	}

	function cleanValue() {
		setTime();
		$("select[name='cityId']").val(loginCityId);
		$("input[name='mac']").val("");
		$("select[name='platform']").val("");
		$("input[name='stbUserID']").val("");
		$("input[name='stbUserPwd']").val("");
	}
</script>
</html>
<%@include file="../../../foot.jsp"%>