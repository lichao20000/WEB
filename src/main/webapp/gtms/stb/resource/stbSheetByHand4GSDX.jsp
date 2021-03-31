<%--
ɽ����ͨ�������ֹ�����
Author: lsr
Version: 1.0.0
Date: 2020-01-10
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<!DOCTYPE html>
<HEAD>
<title>�������ֹ������·�</title>
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

	// IE ����assign����
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

	//���LOID�Ƿ����
	function checkLoid() {
		if ("" == $("input[name='userID']").val().trim()) {
			alert("ҵ���˺Ų���Ϊ�գ�");
			$("input[name='userID']").focus();
			return false;
		}

		//ҵ�񴥷�
		var url = "<s:url value='/itms/service/bssSheetByHand4GSDX_stb!checkStbServAccount.action'/>";
		$.post(url, {
			"userID" : $("input[name='userID']").val().trim()
		}, function(ajax) {
			if ("000" == ajax) {
				alert("Ψһ��ʶ����ʹ�á�");
			} else {
				isExist = true;
				var context = $.parseJSON(ajax);
				var source = $("#net-info-template").html();
				// ��ֵ�������û� ���أ��豸����
				var userInfo = context;
				//$("#cityId option[value='" + userInfo.cityId + "']").prop("selected", true);
				// $("select[name='deviceType']").val(userInfo.deviceType);
				$('#jirRuBssSheet').fillData(userInfo);
			}
		});
	}

	//ҵ�������ύ
	function delBusiness() {
		if ("" == $("input[name='userID']").val().trim()) {
			alert("Ψһ��ʶ����Ϊ�ա�");
			$("input[name='userID']").focus();
			return false;
		}
		if ("-1" == $("select[name='cityId']").val()) {
			alert("���ز���Ϊ�ա�");
			$("input[name='cityId']").focus();
			return false;
		}

		var stbUserID = "";
		var stbUserPwd = "";
		var serverInfoObj = {};
		var userInfoObj = $('#jirRuBssSheet').getJsonData();
		// ��ȡ�������û���Ϣ
		var stbInfoObj = $('#stbSheet').getJsonData();
		stbInfoObj.action = 3;
		stbUserID = stbInfoObj.userID;

		if (stbUserID == null || stbUserID == '') {
			alert("�������˺�ֵ����Ϊ�ա�");
			return false;
		}

		var r = confirm("ȷ��Ҫɾ��������ҵ���� ��");
		if (r == false) {
			return;
		}

		//ҵ�񴥷�
		var url = "<s:url value='/itms/service/bssSheetByHand4GSDX_stb!stbDoBusiness.action'/>";
		$.post(url, stbInfoObj, function(ajax) {
			alert(ajax);
			$("button[name='delBtn']").attr("disabled", true);
			reset();
		});
		//�һ���ť
		$("button[name='delBtn']").attr("disabled", true);
	}

	//����ҵ���ύ
	function doBusiness() {
		if ("" == $("input[name='userID']").val().trim()) {
			alert("Ψһ��ʶ����Ϊ�ա�");
			$("input[name='userID']").focus();
			return false;
		}
		if ("-1" == $("select[name='cityId']").val()) {
			alert("���ز���Ϊ�ա�");
			$("select[name='cityId']").focus();
			return false;
		}
		if ("" == $("input[name='password']").val().trim()) {
			alert("ҵ�����벻����Ϊ��");
			$("input[name='password']").focus();
			return false;
		}

		if ("" == $("input[name='macAddress']").val().trim()) {
			alert("MAC��ַ������Ϊ��");
			$("input[name='macAddress']").focus();
			return false;
		}

		if ("-1" == $("select[name='platform']").val()) {
			alert("�ն˽������Ͳ���Ϊ�ա�");
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

		var r = confirm("ȷ��Ҫ�ύ������ҵ���� ��");
		if (r == false) {
			return;
		}
		//ҵ�񴥷�
		var url = "<s:url value='/itms/service/bssSheetByHand4GSDX_stb!stbDoBusiness.action'/>";
		$.post(url, userInfoObj, function(ajax) {
			alert(ajax);
			//�һ���ť
			$("button[name='subBtn']").attr("disabled", false);
			checkLoid();
		});
		//�һ���ť
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
											<div align="center" class="title_bigwhite">�ֹ�����</div>
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

										<td colspan="4" class="green_title_left">�û����Ϲ���</td>
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
											<TD class=column align="right" nowrap width="15%">����ʱ�䣺</TD>
											<TD width="35%"><input type="text" name="dealtime"
												class=bk value=""> <font color="#FF0000">*</font></TD>
											<TD width="15%" class=column align="right">����:</TD>
											<TD width="35%"><s:select list="cityList" name="cityId"
													headerKey="-1" headerValue="��ѡ������" listKey="city_id"
													listValue="city_name" value="cityId" cssClass="bk"></s:select>
												<font color="#FF0000">*</font></TD>
										</TR>
										<TR bgcolor="#FFFFFF">
											<TD width="15%" class=column align="right">ҵ���˺�:</TD>
											<TD width="35%"><input type="text" id="userID"
												name="userID" class=bk value="">&nbsp;
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<button type="button" name="subButton" onclick="checkLoid()">���Ψһ��ʶ�Ƿ����</button>
											</TD>
											<TD class=column align="right" nowrap width="15%">ҵ������:</TD>
											<TD width="35%"><input type="text" name="password"
												class=bk value=""> <font color="#FF0000">*</font></TD>
										</TR>
										<TR bgcolor="#FFFFFF">
											<TD class=column align="right" nowrap width="15%">mac��ַ:</TD>
											<TD width="35%"><input type="text" name="macAddress"
												class=bk value=""></TD>
											<TD class=column align="right" nowrap width="15%">ƽ̨:</TD>
											<TD width="35%"><select name='platform' class='bk'>
													<option value='-1'>��ѡ��ƽ̨</option>
													<option value='1'>��Ϊƽ̨</option>
													<option value='2'>����ƽ̨</option>
											</select></TD>
										</TR>
									</tbody>
									<TR align="left" id="doBiz">
										<TD colspan="4" class=foot align="right" nowrap>
											<button type="button" name="subBtn" onclick="doBusiness()">��&nbsp;&nbsp;��
											</button>
											<button type="button" name="subBtn" onclick="delBusiness()">��&nbsp;&nbsp;��
											</button>
											<button type="button" name="subBtn" onclick="reset()">��&nbsp;&nbsp;��</button>
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