<%@ include file="../../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BSS销户工单</title>
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
			alert("请输入LOID");
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
				alert("请输入正确的LOID");
				return false;
			}
		});
	}
	//显示工单参数信息
	function showSheet() {
		var _servTypeId = $("select[@name='servTypeId']");
		if ('-1' == _servTypeId.val()) {
			alert("请选择业务类型");
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
	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "dataForm" ];

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array();
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
					//如果用户的浏览器是NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//如果用户的浏览器是IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
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
							<td width="162" align="center" class="title_bigwhite">BSS销户工单</td>
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
							<th colspan="4">BSS销户工单</th>
						</tr>

						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" width="20%">LOID</TD>
							<TD><INPUT type="text" name="loid" class=bk
								value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" width="20%"></TD>
							<TD><button onclick="checkLoid()">&nbsp;验证LOID&nbsp;</button></TD>
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
							<th colspan="4">BSS销户工单</th>
						</tr>

						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" width="20%">业务类型</TD>
							<TD width="30%"><select name="servTypeId" class=bk>
									<option value="-1">==请选择业务类型==</option>
										<!-- <option value="20">用户资料</option> -->
										<option value="15">VOIP业务(H248)</option>
										<option value="14">VOIP业务(SIP)</option>
										<!--<option value="22">宽带业务</option> -->
										<option value="21">IPTV业务</option>
										<!--<option value="25">机顶盒业务</option>  -->
							</select>&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" width="20%">操作类型</TD>
							<TD width="30%"><select name="operateType" class=bk
								onchange="" disabled>
									<option value="3" selected>销户</option>
							</select>&nbsp; <font color="#FF0000">* </font></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="showSheet()">&nbsp;模拟工单&nbsp;</button>
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