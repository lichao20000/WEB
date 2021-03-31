<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ include file="/toolbar.jsp"%>


<html>
<head>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<lk:res />

<SCRIPT type="text/javascript">
	$(function() {
		dyniframesize();
		Init();
	});

	function Init() {
		// 初始化厂家
		change_select("vendor", "-1");
		// 普通方式提交
		var form = document.getElementById("mainForm");
		form.action = "<s:url value='/itms/resource/softUpgradRecordQuery!getSoftUpgradRecordQuery.action'/>";
		form.submit();
	}

	//查询表单厂商型号关联
	function change_select(type, selectvalue) {
		switch (type) {
		case "vendor":
			var url = "<s:url value='/itms/resource/softUpgradRecordQuery!getVendors.action'/>";
			$.post(url, {},
					function(ajax) {
						parseMessage(ajax, $("select[@name='vendor']"),
								selectvalue);
						parseMessage(ajax, $("select[@name='vendor_add']"),
								selectvalue);
					});
			break;
		case "deviceModel":
			var url = "<s:url value='/itms/resource/softUpgradRecordQuery!getDeviceModels.action'/>";
			var vendorId = $("select[@name='vendor']").val();
			if ("-1" == vendorId) {
				$("select[@name='device_model']").html(
						"<option value='-1'>==请先选择终端厂家==</option>");
				break;
			}
			$.post(url, {
				vendor : vendorId
			}, function(ajax) {
				parseMessage(ajax, $("select[@name='device_model']"),
						selectvalue);
			});
			break;
		default:
			alert("未知查询选项！");
			break;
		}
	}

	//新增表单厂商型号版本关联
	function change_select_add(type, selectvalue) {
		switch (type) {
		case "deviceModel":
			var url = "<s:url value='/itms/resource/softUpgradRecordQuery!getDeviceModels.action'/>";
			var vendorId = $("select[@name='vendor_add']").val();
			if ("-1" == vendorId) {
				$("select[@name='device_model_add']").html(
						"<option value='-1'>==请先选择终端厂家==</option>");
				$("select[@name='currentVersion_add']").html(
						"<option value='-1'>==请先选择终端型号==</option>");
				break;
			}
			$.post(url, {
				vendor : vendorId
			}, function(ajax) {
				parseMessage(ajax, $("select[@name='device_model_add']"),
						selectvalue);
				$("select[@name='currentVersion_add']").html(
						"<option value='-1'>==请先选择终端型号==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value='/itms/resource/softUpgradRecordQuery!getDevicetypes.action'/>";
			var deviceModelId = $("select[@name='device_model_add']").val();
			if ("-1" == deviceModelId) {
				$("select[@name='currentVersion_add']").html(
						"<option value='-1'>==请先选择终端型号==</option>");
				break;
			}
			$.post(url, {
				device_model_add : deviceModelId
			}, function(ajax) {
				parseMessage(ajax, $("select[@name='currentVersion_add']"),
						selectvalue);
			});
			break;
		default:
			alert("未知查询选项！");
			break;
		}
	}

	//解析查询设备型号返回值的方法
	function parseMessage(ajax, field, selectvalue) {
		var flag = true;
		if ("" == ajax) {
			option = "<option value='-1' selected>==此项没有记录==</option>";
			field.html(option);
			return;
		}
		var lineData = ajax.split("#");
		if (!typeof (lineData) || !typeof (lineData.length)) {
			return false;
		}
		field.html("");

		option = "<option value='-1' selected>==请选择==</option>";
		field.append(option);
		for (var i = 0; i < lineData.length; i++) {
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

	// 查询列表
	function doQuery() {
		// 普通方式提交
		var form = document.getElementById("mainForm");
		form.action = "<s:url value='/itms/resource/softUpgradRecordQuery!getSoftUpgradRecordQuery.action'/>";
		form.submit();
	}

	// 新增表单保存
	function save() {
		// 必须项
		var vendor_add = trim($("select[@name='vendor_add']").val());
		var device_model_add = trim($("select[@name='device_model_add']").val());
		var currentVersion_add = trim($("select[@name='currentVersion_add']")
				.val());
		var targetVersion_add = trim($("input[@name='targetVersion_add']")
				.val());
		var upgradeRange_add = trim($("input[@name='upgradeRange_add']").val());
		var upgradeMethod_add = trim($("input[@name='upgradeMethod_add']")
				.val());
		var starttime_add = trim($("input[@name='starttime_add']").val());
		var endtime_add = trim($("input[@name='endtime_add']").val());

		// 非必须项
		var file_path_add = $("input[@name='file_path_add']").val();
		var deviceCount_add = trim($("input[@name='deviceCount_add']").val());
		var fileName = file_path_add
				.substring(file_path_add.lastIndexOf("\\") + 1);
		$("input[@name='fileName']").val(fileName);
		if ("" == vendor_add || "-1" == vendor_add) {
			alert("请选择终端厂家！");
			return;
		}
		if ("" == device_model_add || "-1" == device_model_add) {
			alert("请选择终端型号！");
			return;
		}
		if ("" == currentVersion_add || "-1" == currentVersion_add) {
			alert("请选择现有版本！");
			return;
		}
		if ("" == targetVersion_add) {
			alert("请输入目标版本！");
			return;
		}
		if ("" == upgradeRange_add) {
			alert("请输入升级范围！");
			return;
		}
		if ("" == upgradeMethod_add) {
			alert("请输入升级方式！");
			return;
		}
		if ("" == starttime_add) {
			alert("请选择升级开始时间！");
			return;
		}
		if ("" == endtime_add) {
			alert("请选择升级结束时间！");
			return;
		}

		if(!$("tr[@id='title']").is(":hidden")){
			if ("" == trim($("input[@name='title']").val())) {
				alert("请输入公告标题！");
				return;
			}
			if ("" == trim($("textarea[@name='content']").val())) {
				alert("请输入公告内容！");
				return;
			}
		}
		if("" != deviceCount_add && !checkDevCount(deviceCount_add)){
			alert("终端数量请输入数字！");
			return 
		}
		var url = "<s:url value='/itms/resource/softUpgradRecordQuery!addSoftUpgradRecord.action'/>";
		document.addForm.action = url;
		document.addForm.submit();
		hideNews();
		showAddPart(false);

	}

	/*------------------------------------------------------------------------------
	 //函数名:		trim
	 //参数  :	str 待检查的字符串
	 //功能  :	根据传入的参数进行去掉左右的空格
	 //返回值:		trim（str）
	 //说明  :	
	 //描述  :	Create 2015-6-4 of By yinlei3
	 ------------------------------------------------------------------------------*/
	function trim(str) {
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}

	//添加
	function doAdd() {

		clearData();
		showAddPart(true);
		hideNews();
	}

	// 隐藏页面下面的添加区域
	function showAddPart(tag) {
		if (tag)
			$("table[@id='addTable']").show();
		else
			$("table[@id='addTable']").hide();
	}

	// 显示公告
	function showNews() {

		$("tr[@id='title']").show();
		$("tr[@id='content']").show();

	}

	// 隐藏公告
	function hideNews() {

		$("tr[@id='title']").hide();
		$("tr[@id='content']").hide();

	}

	function checkDevCount(str) {
		var pattern = /^\d+$/;
		return pattern.test(str);
	}

	/** 点击新增时，清楚缓存 */
	function clearData() {
		$("input[@name='resetButton']").click();
		document.getElementsByName("vendor_add")[0].value = -1;
		$("select[@name='device_model_add']").html(
				"<option value='-1'>==请先选择终端厂商==</option>");
		$("select[@name='currentVersion_add']").html(
				"<option value='-1'>==请先选择终端型号==</option>");
	}

	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "dataForm" ]

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
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
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(window).resize(function() {
		dyniframesize();
	});
</SCRIPT>
</head>

<body>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
		<TR>
			<TD>
				<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
					target="dataForm">
					<table width="98%" height="30" border="0" align="center"
						cellpadding="0" cellspacing="0" class="green_gargtd">
						<tr>
							<td width="162">
								<div align="center" class="title_bigwhite">软件升级记录查询</div>
							</td>
							<td><img src="/itms/images/attention_2.gif" width="15"
								height="12"></td>
						</tr>
					</table>
					<!-- 查询part begin -->
					<TABLE border=0 cellspacing=0 cellpadding=0 width="98%"
						align="center">
						<tr>
							<td bgcolor=#999999>
								<table border=0 cellspacing=1 cellpadding=2 width="100%"
									align="center">
									<tr>
										<th colspan="4" id="gwShare_thTitle">软件升级记录查询</th>
									</tr>
									<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
										<TD align="center" class=column width="15%">终端厂家:</TD>
										<TD align="left" width="35%"><select name="vendor"
											class="bk" onchange="change_select('deviceModel','-1')">
												<option value="-1">==请选择==</option>
										</select></TD>
										<TD align="center" class=column width="15%">终端型号:</TD>
										<TD width="35%"><select name="device_model" class="bk">
												<option value="-1">==请先选择终端厂家==</option>
										</select></TD>
									</TR>
									<TR bgcolor=#ffffff>
										<td class="column" width='15%' align="center">升级开始起始时间:</td>
										<td width='35%' align="left"><input type="text"
											name="starttime" class='bk' readonly
											value="<s:property value='starttime'/>"> <img
											name="shortDateimg"
											onClick="WdatePicker({el:document.mainForm.starttime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="选择"></td>
										<td class="column" width='15%' align="center">升级开始截止时间:</td>
										<td width='35%' align="left"><input type="text"
											name="endtime" class='bk' readonly
											value="<s:property value='endtime'/>"> <img
											name="shortDateimg"
											onClick="WdatePicker({el:document.mainForm.endtime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="选择"></td>
									</TR>
									<tr bgcolor="#FFFFFF">
										<td colspan="4" align="right" class="green_foot" width="100%">
											<input type="button" class=jianbian
											onclick="javascript:doQuery()" name="queryButton"
											value=" 查 询 " /> <ms:hasAuth authCode="ShowDevPwd">
												<input type="button" class=jianbian
													onclick="javascript:doAdd()" name="addButto" value=" 新 增 " />
											</ms:hasAuth>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</FORM> <!-- 查询part end --> <!-- 展示结果part begin -->
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
								name="dataForm" height="0" frameborder="0" scrolling="no"
								width="100%" src=""></iframe></TD>
					</TR>
				</TABLE> <!-- 展示结果part end --> <!-- 添加和编辑part begin -->
				<FORM id="addForm" name="addForm" method="post" action=""
					target="addResultForm" enctype="multipart/form-data">
					<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
						align="center" id="addTable" style="display: none">
						<TR>
							<TD bgcolor=#999999>
								<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
									id="allDatas">
									<TR>
										<TH colspan="4" align="center"><SPAN id="actLabel">添加</SPAN><SPAN
											id="DeviceTypeLabel"></SPAN>软件升级</TH>
									</TR>
									<TR bgcolor="#FFFFFF" id="vendor_idID">
										<TD class=column align="center" width="15%">终端厂家</TD>
										<TD width="85%"><select name="vendor_add" class="bk"
											onChange="change_select_add('deviceModel','-1')"></select>&nbsp;
											<font color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF" id="device_ModelID">
										<TD class=column align="center" width="15%">终端型号</TD>
										<TD width="85%"><select name="device_model_add"
											class="bk" onChange="change_select_add('devicetype','-1')">
												<option value="-1">==请选择终端厂家==</option>
										</select>&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">现有版本</TD>
										<TD width="85%"><select name="currentVersion_add"
											class="bk">
												<option value="-1">==请选择终端型号==</option>
										</select>&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">目标版本</TD>
										<TD width="85%"><input type="text"
											name="targetVersion_add" value="" size="20" maxlength="40"
											class="bk" />&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">升级范围</TD>
										<TD width="85%"><input type="text"
											name="upgradeRange_add" value="" size="20" maxlength="40"
											class="bk" />&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">终端数量</TD>
										<TD width="85%"><input type="text" name="deviceCount_add"
											value="" size="20" maxlength="40" class="bk" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">升级原因</TD>
										<TD width="85%"><textarea rows="3" cols="60"
												name="upgradeReason_add"></textarea></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">升级方式</TD>
										<TD width="85%"><input type="text"
											name="upgradeMethod_add" value="" size="20" maxlength="40"
											class="bk" />&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor=#FFFFFF>
										<td class="column" width="15%" align="center">升级开始时间:</td>
										<td width="85%" align="left"><input type="text"
											name="starttime_add" class='bk' readonly
											value="<s:property value='starttime'/>"> <img
											name="shortDateimg"
											onClick="WdatePicker({el:document.addForm.starttime_add,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="选择"> &nbsp; <font color="#FF0000">*</font></td>
									</TR>
									<TR bgcolor="#FFFFFF">
										<td class="column" width="15%" align="center">升级结束时间:</td>
										<td width="85%" align="left"><input type="text"
											name="endtime_add" class='bk' readonly
											value="<s:property value='endtime'/>"> <img
											name="shortDateimg"
											onClick="WdatePicker({el:document.addForm.endtime_add,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="选择"> &nbsp; <font color="#FF0000">*</font></td>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">终端厂家联系方式</TD>
										<TD width="85%"><input type="text" name="contactWay_add"
											value="" size="20" maxlength="40" class="bk" /></TD>
									</TR>
									<input type="hidden" name="fileName" value="" />
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">附件上传</TD>
										<TD width="85%"><input type="file" name="file_path_add"
											id="file_path" size="35" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">是否发公告</TD>
										<TD width="85%"><input type="radio" width="22%"
											name="ipType" value="0" onclick="showNews()">是 <input
											type="radio" width="23%" name="ipType" value="1" checked
											onclick="hideNews()">否</TD>
									</TR>
									<TR bgcolor="#FFFFFF" style="display: none" id="title"">
										<TD class=column align="center" width="15%">公告标题</TD>
										<TD width="85%"><input type="text" name="title" value=""
											size="20" maxlength="40" class="bk" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF" style="display: none" id="content">
										<TD class=column align="center" width="15%">公告内容</TD>
										<TD width="85%"><textarea rows="3" cols="60"
												name="content"></textarea></TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
						<TR>
							<TD bgcolor=#999999>
								<TABLE border=0 cellspacing=1 cellpadding=0 width="100%">
									<TR bgcolor="#FFFFFF">
										<TD align="right" CLASS=green_foot><INPUT TYPE="button"
											name="saveButton" onclick="javascript:save()" value=" 确 定 "
											class=jianbian>&nbsp;&nbsp;<INPUT TYPE="reset"
											name="resetButton" value=" 重 写 " class=jianbian></TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</FORM> <!-- 添加和编辑part end -->
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=20><iframe id="addResultForm" name="addResultForm"
					height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
		</TR>

	</TABLE>
</body>

<%@ include file="/foot.jsp"%>


</html>