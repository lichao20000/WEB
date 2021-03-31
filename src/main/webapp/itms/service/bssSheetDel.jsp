<%@ include file="../../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BSS��������</title>
<%%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function checkLoid() {
		var loid = $("input[@name='loid']").val();
		if ("" == loid) {
			alert("������LOID");
			loid.focus();
			return false;
		}
		var url = "<s:url value='/itms/service/BssSheetDelACT!initLoid.action'/>";
		$.post(url, {
			loid : loid
		}, function(ajax) {
			var result = parseInt(ajax);
			if (ajax > 0) {
				$("tr[@id='dis']").css("display", "");
			} else {
				$("tr[@id='dis']").css("display", "none");
				$("tr[@id='ifr']").css("display", "none");
				alert("��������ȷ��LOID");
				return false;
			}
		});
	}
	//��ʾ����������Ϣ
	function showSheet() {
		var _servTypeId = $("select[@name='servTypeId']");
		if ('-1' == _servTypeId.val()) {
			alert("��ѡ��ҵ������");
			_servTypeId.focus();
			return;
		}
		$("tr[@id='ifr']").css("display", "");
		document.mainfrm.submit();
	}
	
	var hasUsername = 0;
	function checkUserInfo() {
		var _username = $("input[@name='username']").val();
		var url = "<s:url value='/itms/service/simulateSheet!checkUsername.action'/>";
		$.post(url, {
			username : _username
		}, function(ajax) {
			var relt = ajax.split("#");
			if (relt[0] != "1") {
				isHasUsername = 0;
				$("div[@id='usernameDiv']").html(
						"<font color=red>" + relt[1] + "</font>");
			} else {
				hasUsername = 1;
			}
		});
	}
	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "dataForm" ];

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array();
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
					//����û����������NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//����û����������IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i]);
				tempobj.style.display = "block";
			}
		}
	}

	$(function() {
		//setValue();
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>
<%@ include file="../../../head.jsp"%>
<%@ include file="../../../toolbar.jsp"%>
<body>
	<form name="mainfrm"
		action="<s:url value='/itms/service/BssSheetDelACT!showSheet.action'/>"
		target="dataForm">

		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">BSS��������</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<TR>
							<th colspan="4">BSS��������</th>
						</tr>

						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" width="20%">LOID</TD>
							<TD><INPUT type="text" name="loid" class=bk
								value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" width="20%"></TD>
							<TD><button onclick="checkLoid()">&nbsp;��֤LOID&nbsp;</button></TD>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr id="dis" style="display: none;">
				<td>
					<table class="querytable">
						<TR>
							<th colspan="4">BSS��������</th>
						</tr>

						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" width="20%">ҵ������</TD>
							<TD width="30%"><select name="servTypeId" class=bk>
									<option value="-1">==��ѡ��ҵ������==</option>
										<!-- <option value="20">�û�����</option> -->
										<option value="15">VOIPҵ��(H248)</option>
										<option value="14">VOIPҵ��(SIP)</option>
										<!--<option value="22">���ҵ��</option> -->
										<option value="21">IPTVҵ��</option>
										<!--<option value="25">������ҵ��</option>  -->
							</select>&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" width="20%">��������</TD>
							<TD width="30%"><select name="operateType" class=bk
								onchange="" disabled>
									<option value="3" selected>����</option>
							</select>&nbsp; <font color="#FF0000">* </font></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="showSheet()">&nbsp;ģ�⹤��&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr id="ifr" style="display: ">
				<td><iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src="" style="height: 400px;"></iframe></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../../foot.jsp"%>