<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BSS模拟工单</title>
<%%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	
	//显示工单参数信息
	function showSheet() {
		var _servTypeId = $("select[@name='servTypeId']");
		var _operateType = $("select[@name='operateType']");
		if ('-1' == _servTypeId.val()) {
			alert("请选择业务类型");
			_servTypeId.focus();
			return;
		}
		if ('-1' == _operateType.val()) {
			alert("请选择操作类型");
			_operateType.focus();
			return;
		}
		$("tr[@id='ifr']").css("display", "");
		document.mainfrm.submit();
	}
	var hasUsername = 0;
	function checkUserInfo() {
		var _username = $("input[@name='username']").val();
		var url = "<s:url value='/itms/service/simulateSheet4cq!checkUsername.action'/>";
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
	
	function change_select(){
		var servTypeId = $("#servTypeId").val();
		var operateType = $("#operateType");
		if(servTypeId=="22"){
			operateType.empty();
			operateType.append("<option value='"+1+"'>开户</option>");
			operateType.append("<option value='"+3+"'>销户</option>");
			operateType.append("<option value='"+7+"'>更改账号</option>");
			operateType.append("<option value='"+15+"'>信息修改</option>");
			operateType.append("<option value='"+20+"'>解除绑定</option>");
			operateType.append("<option value='"+6+"'>更改设备ID</option>");
			operateType.append("<option value='"+9+"'>更改模式</option>");
			/* operateType.append("<option value='"+10+"'>更改密码</option>"); */
			operateType.append("<option value='"+18+"'>修改客户信息</option>");
			operateType.append("<option value='"+19+"'>修改终端连接数</option>");
		}
		else if(servTypeId=="21"){
			operateType.empty();
			operateType.append("<option value='"+1+"'>开户</option>");
			operateType.append("<option value='"+3+"'>销户</option>");
			operateType.append("<option value='"+15+"'>信息修改</option>");
			operateType.append("<option value='"+16+"'>调整组播参数</option>");
		}
		else if(servTypeId=="14"){
			operateType.empty();
			operateType.append("<option value='"+1+"'>开户</option>");
			operateType.append("<option value='"+3+"'>销户</option>");
			operateType.append("<option value='"+15+"'>信息修改</option>");
			operateType.append("<option value='"+12+"'>修改用户鉴权密码</option>");
		}
		else if(servTypeId=="26"){
			operateType.empty();
			operateType.append("<option value='1iptv'>stb_iptv开户</option>");
			operateType.append("<option value='1ims'>stb_ims开户</option>");
			operateType.append("<option value='3iptv'>stb_iptv销户</option>");
			operateType.append("<option value='3ims'>stb_ims销户</option>");
			operateType.append("<option value='"+6+"'>更改设备id</option>");
		}
	}
</script>
</head>

<body>
	<form name="mainfrm"
		action="<s:url value='/itms/service/simulateSheet4cq!showSheet.action'/>"
		target="dataForm">

		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">BSS模拟工单</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> BSS模拟工单</td>
						</tr>
					</table>
				</td>
			</tr>
			
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr id="dis" >
				<td>
					<table class="querytable">
						<TR>
							<th colspan="4">BSS模拟工单</th>
						</tr>

						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" width="20%">业务类型</TD>
							<TD width="30%">
							<select name="servTypeId" id="servTypeId" class=bk onchange="change_select()">
								<option value="-1">==请选择业务类型==</option>
								<option value="22">上网业务</option>
								<option value="21">IPTV业务</option>
								<option value="14">VOIP业务</option>
								<!-- <option value="26">STB业务</option> -->
							</select>&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" width="20%">操作类型</TD>
							<TD width="30%"><select name="operateType" id="operateType" class=bk>
									<option value="-1">==请选择操作类型==</option>
									<option value="1">开户</option>
									<option value="3">销户</option>
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
			<tr id="ifr" style="display: " >
				<td><iframe id="dataForm" name="dataForm" height=""
						frameborder="0" scrolling="yes" width="100%" src="" style="height: 400px;"></iframe></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>