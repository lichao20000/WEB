<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>用户信息管理</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
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
	function add() {
		var url = "<s:url value='/gtms/stb/resource/userMessage!time.action'/>";
		window
				.open(url, "",
						"left=20,top=20,width=900,height=600,resizable=no,scrollbars=yes");
	}
	$(function() {
		//setValue();
		dyniframesize();
	});
	$(function() {
		gwShare_change("city", "-1");
	});
	function gwShare_change(type, selectvalue) {
		switch (type) {
		case "city":
			var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNextChild.action'/>";
			$.post(url, {}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='gwShare_cityId']"),
						selectvalue);
				$("select[@name='cityId']").html(
						"<option value='-1'>==请先选择属地==</option>");
			});
			break;
		case "cityid":
			var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNext.action'/>";
			var cityId = $("select[@name='gwShare_cityId']").val();
			if ("-1" == cityId) {
				$("select[@name='gwShare_cityId']").html(
						"<option value='-1'>==请先选择属地==</option>");
				break;
			}
			$.post(url, {
				citynext : cityId
			}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='citynext']"),
						selectvalue);
				$("select[@name='cityId']").html(
						"<option value='-1'>==请先选择属地==</option>");
			});
			break;
		default:
			alert("未知查询选项！");
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
	$(window).resize(function() {
		dyniframesize();
	});

	function query() {
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("正在统计，请稍等....");
		$("button[@name='button']").attr("disabled", true);
		var cityId = $.trim($("select[@name='city_id']").val());
		document.frm.submit();

	}
</script>
</head>

<body>
	<form name="frm" id="frm" method="post"
		action="<s:url value='/gtms/stb/resource/userMessage!query.action'/>"
		target="dataForm">

		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								用户信息管理</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> 开始时间和结束时间为用户开户的日期<font color="red">*</font></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">用户信息查询</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
							<TD align="right" class=column width="15%">业务账号：</TD>
							<TD align="left" width="35%"><INPUT TYPE="text"
								NAME="servaccount"  id="servaccount" maxlength=30 class=bk size=20></TD>
							<TD align="right" class=column width="15%">平台类型：</TD>
							<TD width="35%" nowrap><s:select list="platformTypeList"
									name="platformType" headerKey="-1" headerValue="请选择平台类型"
									listKey="platform_id" listValue="platform_name" cssClass="bk"></s:select></TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
							<TD align="right" class=column width="15%">MAC地址：</TD>
							<TD align="left" width="35%"><INPUT TYPE="text" NAME="MAC"
								maxlength=30 class=bk size=20></TD>
							<TD align="right" class=column width="15%">用户分组：</TD>
							<TD width="35%" nowrap><s:select list="userGroupIDList"
									name="userGroupID" headerKey="-1" headerValue="请选择用户分组"
									listKey="group_id" listValue="group_name" cssClass="bk"></s:select></TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
							<TD align="right" class="column" width="15%">属 地</TD>
							<TD align="left" width="35%"><select name="gwShare_cityId" id="gwShare_cityId"
								class="bk" onchange="gwShare_change('cityid','-1')">
									<option value="-1">==请选择==</option>
							</select></TD>
							<TD align="right" class="column" width="15%">下级属 地</TD>
							<TD align="left" width="35%"><select name="citynext" 
								class="bk">
									<option value="-1">请先选择属地</option>
							</select></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>开始时间</TD>
							<TD width="35%"><input type="text" name="starttime" readonly
								value="<s:property value='starttime'/>"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" /></TD>
							<TD class=column width="15%" align='right'>结束时间</TD>
							<TD width="35%"><input type="text" name="Invalidtime" readonly
								value="<s:property value='endtime'/>"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" /></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>接入账号</TD>
							<TD width="35%"><INPUT TYPE="text" NAME="stbuser"
								maxlength=30 class=bk size=20></TD>
								<TD class=column width="15%" align='right'>手机号码</TD>
							<TD width="35%"><INPUT TYPE="text" NAME="iptvBindPhone"
								maxlength=30 class=bk size=20></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class="foot" width="100%">
								<button onclick="query()">&nbsp;查 询&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
								<!-- <button onclick="add()">&nbsp;添 加&nbsp;</button> -->
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td><iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
