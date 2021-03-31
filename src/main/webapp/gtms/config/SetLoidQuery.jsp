<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>回填LOID查询</title>
	<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
		type="text/css">
	<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
		type="text/css">
	<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
	<script type="text/javascript"
        src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
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

	$(function() {
		init();
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});

	function init() {
		if($("input[@name='startTime']").val()==''){
			$("input[@name='startTime']").val($.now("-", false) + " 00:00:00");
		}
		if($("input[@name='endTime']").val()==''){
			$("input[@name='endTime']").val($.now("-", false) + " 23:59:59");
		}
	}
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/gtms/config/setLoidAction!initSetLoid.action'/>"
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
								回填LOID详细信息</td>
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
							<th colspan="4">回填LOID查询</th>
						</tr>

						<TR>
							<TD class=column width="15%" align='right'>开始时间</TD>
							<TD width="35%"><input type="text" name="startTime" readonly
								class=bk value="<s:property value="startTime" />"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></TD>
							<TD class=column width="15%" align='right'>结束时间</TD>
							<TD width="35%"><input type="text" name="endTime" readonly
								class=bk value="<s:property value="endTime" />"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>LOID</TD>
							<TD width="35%"><input type="text" id="loid" name="loid" /></TD>
							<TD class=column width="15%" align='right'>设备序列号</TD>
							<TD width="35%"><input type="text" id="deviceNumber"
								name="deviceNumber" /></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>状态</TD>
							<TD width="35%"><select name="statu" class="bk">
									<option value="">==请选择==</option>
									<option value="0">未做</option>
									<option value="1">成功</option>
							</select></TD>
							<TD class=column width="15%" align='right'></TD>
							<TD width="35%"></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;查&nbsp;询&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
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
<%@ include file="../../foot.jsp"%>