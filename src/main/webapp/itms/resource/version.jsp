<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>版本统计查询</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		document.selectForm.submit();
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
		for ( var i = 0; i < iframeids.length; i++) {
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
		dyniframesize();
		Init();
	});

	$(window).resize(function() {
		dyniframesize();
	});

	function Init() {
		gwShare_change_select("vendor", "-1");
	}

	function gwShare_change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value='/itms/resource/gwDeviceQuery!getVendor.action'/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendor']"),selectvalue);
			});
			break;
		case "deviceModel":
			var url = "<s:url value='/itms/resource/gwDeviceQuery!getDeviceModel.action'/>";
			var vendorId = $("select[@name='vendor']").val();
			$("select[@name='devicetype']").html("<option value='-1'>==请先选择设备型号==</option>");
			if("-1"==vendorId){
				$("select[@name='devicemodel']").html("<option value='-1'>==请先选择设备厂商==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='devicemodel']"),selectvalue);
			});
			break;
		}		
	}

	function gwShare_parseMessage(ajax, field, selectvalue) {
		var flag = true;
		if ("" == ajax) {
			return;
		}
		var lineData = ajax.split("#");
		if (!typeof (lineData) || !typeof (lineData.length)) {
			return false;
		}
		field.html("");

		option = "<option value='-1' selected>==请选择==</option>";
		field.append(option);
		for ( var i = 0; i < lineData.length; i++) {
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			if (selectvalue == xValue) {
				flag = false;
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"' selected>==" + xText
						+ "==</option>";
			} else {
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"'>==" + xText
						+ "==</option>";
			}
			try {
				field.append(option);
			} catch (e) {
				alert("设备型号检索失败！");
			}
		}
		if (flag) {
			field.attr("value", "-1");
		}
	}

	function gaoji(val) {
		var tempVal = $("input[name='temval']").val();
		if (1 == val) {
			$("input[name='temval']").val(val);
			$("button[name='gaojibtn1']").css("display", "none");
			$("button[name='jiandan']").css("display", "");
			$("tr[@id='gaoji1']").css("display", "");
			$("tr[@id='gaoji2']").css("display", "");
		}
		if (2 == val) {
			$("input[name='temval']").val(val);
			$("button[name='jiandan']").css("display", "none");
			$("button[name='gaojibtn1']").css("display", "");
			$("tr[@id='gaoji1']").css("display", "none");
			$("tr[@id='gaoji2']").css("display", "none");
		}
	}

	function ToExcel() {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/resource/VersionQuery!queryVersionDevExcel.action' />";
		mainForm.submit();
		mainForm.action = "<s:url value='/itms/resource/VersionQuery!queryVersionDev.action' />";
	}

	function OpenDetail(softwareversion) {
		var vendor = $.trim($("select[@name='vendor']").val());
		var devicemodel = $.trim($("select[@name='devicemodel']").val());
		var versionSpecification = $.trim($(
				"select[@name='versionSpecification']").val());
		var specName = $.trim($("select[@name='specName']").val());
		var deviceType = $.trim($("select[@name='deviceType']").val());
		var accessWay = $.trim($("select[@name='accessWay']").val());
		var voiceAgreement = $.trim($("select[@name='voiceAgreement']").val());
		var zeroconf = $.trim($("select[@name='zeroconf']").val());
		var mbbndwidth = $.trim($("select[@name='mbbndwidth']").val());
		var ipvsix = $.trim($("select[@name='ipvsix']").val());
		var temval = $.trim($("input[@name='temval']").val());
		var page = "<s:url value='/itms/resource/VersionQuery!queryDetail.action'/>?vendor="
				+ vendor
				+ "&devicemodel="
				+ devicemodel
				+ "&versionSpecification="
				+ versionSpecification
				+ "&specName="
				+ specName
				+ "&deviceType="
				+ deviceType
				+ "&accessWay="
				+ accessWay
				+ "&voiceAgreement="
				+ voiceAgreement
				+ "&zeroconf="
				+ zeroconf
				+ "&mbbndwidth="
				+ mbbndwidth
				+ "&ipvsix="
				+ ipvsix
				+ "&temval="
				+ temval
				+ "&softwareversion=" + softwareversion;
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/resource/VersionQuery!queryVersionDev.action'/>"
		target="dataForm">
		<input type="hidden" name="temval" value="2" />
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								版本统计查询</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> 版本统计查询</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">版本统计查询</th>
						</tr>

						<TR>
							<TD class=column width="15%" align='right'>设备厂商</TD>
							<TD width="35%"><select name="vendor" class="bk"
								onchange="gwShare_change_select('deviceModel','-1')">
							</select></TD>

							<TD class=column width="15%" align='right'>设备型号</TD>
							<TD width="35%"><select name="devicemodel" class="bk"
								onchange="gwShare_change_select('devicetype','-1')">
									<option value="">==请选择厂商==</option>
							</select></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>版本规范</TD>
							<TD width="35%"><select name="versionSpecification"
								class="bk">
									<option value="">===请选择===</option>
									<option value="0">否</option>
									<option value="1">是</option>
									<option value="1">是</option>
									<option value="0">否</option>
							</select></TD>
							<TD class=column width="15%" align='right'>终端规格</td>
							<TD width="35%"><select name="specName" class="bk">
									<option value="">===请选择===</option>
									<option value="1">家庭网关</option>
									<option value="2">政企网关</option>
							</select></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>设备类型</td>
							<TD width="35%"><select name="deviceType" class="bk">
									<option value="">===请选择===</option>
									<option value="2">E8C</option>
									<option value="1">E8B</option>
							</select></TD>
							<TD class=column width="15%" align='right'>接入方式</td>
							<TD width="35%"><select name="accessWay" class="bk">
									<option value="">===请选择===</option>
									<option value="1">ADSL</option>
									<option value="2">LAN</option>
									<option value="3">EPON</option>
									<option value="4">GPON</option>
							</select></TD>
						</TR>
						<TR id="gaoji1" style="display: none;">
							<TD class=column width="15%" align='right'>语音协议</td>
							<TD width="35%"><select name="voiceAgreement" class="bk">
									<option value="">===请选择===</option>
									<option value="1">软交换SIP</option>
									<option value="2">IMS SIP</option>
									<option value="3">H.248</option>
							</select></TD>
							<TD class=column width="15%" align='right'>机顶盒零配置</td>
							<TD width="35%"><select name="zeroconf" class="bk">
									<option value="">===请选择===</option>
									<option value="1">支持</option>
									<option value="2">不支持</option>

							</select></TD>
						</TR>
						<TR id="gaoji2" style="display: none;">
							<TD class=column width="15%" align='right'>百兆带宽</td>
							<TD width="35%"><select name="mbbndwidth" class="bk">
									<option value="">===请选择===</option>
									<option value="1">支持</option>
									<option value="2">不支持</option>
							</select></TD>
							<TD class=column width="15%" align='right'>IPv6</td>
							<TD width="35%"><select name="ipvsix" class="bk">
									<option value="">===请选择===</option>
									<option value="1">支持</option>
									<option value="2">不支持</option>
							</select></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="gaoji('1')" id="button" name="gaojibtn1">高
									级 查 询</button>
								<button onclick="gaoji('2')" id="button" name="jiandan"
									style="display: none;">简 单 查 询</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<button onclick="query()" id="button" name="button">查 询
								</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td height="80%"><iframe id="dataForm" name="dataForm"
						height="100%" frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>