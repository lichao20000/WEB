<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>模拟工单操作日志</title>
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
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/report/bsslogQuery!bsslogQuery.action' />";
		mainForm.submit();
	}

	function ToExcel(loid, bussinessacount, startOpenDate, operationuser,
			bssaccount) {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/report/bsslogQuery!getExcel.action'/>?"
				+ "loid="
				+ loid
				+ "&bussinessacount="
				+ bussinessacount
				+ "&startOpenDate="
				+ startOpenDate
				+ "&operationuser="
				+ operationuser;
		+"&bssaccount=" + bssaccount;
		mainForm.submit();
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
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
	<form id="selectForm" name="selectForm" action="" target="dataForm">
		<table>
			<input name="id" type="hidden" value="">
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								模拟工单操作日志</td>
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
							<th colspan="4">模拟工单操作日志</th>
						</tr>

						<TR>
							<TD class="column" align="right" width="15%">LOID</TD>
							<TD width="35%"><INPUT TYPE="text" NAME="loid" maxlength=50
								class=bk value=""></TD>
							<TD class=column width="15%" align='right'>业务类型</TD>
							<TD width="35%"><SELECT name="bussinessacount" class=bk>
									<option value="">==请选择==</option>
									<option value="10">宽带</option>
									<option value="11">IPTV</option>
									<option value="14">VOIP</option>
							</SELECT></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>业务帐号</TD>
							<TD width="35%"><INPUT TYPE="text" NAME="bssaccount"
								maxlength=50 class=bk value=""></TD>
							<TD class=column width="15%" align='right'>操作时间</TD>
							<TD width="35%"><input type="text" name="startOpenDate"
								readonly class=bk value="<s:property value="startOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></TD>
						</TR>
						<TR>
							<TD class="column" align="right" width="15%">操作人</TD>
							<TD width="35%"><INPUT TYPE="text" NAME="operationuser"
								maxlength=50 class=bk value=""></TD>
							<TD class="column" align="right" width="15%"></TD>
							<TD width="35%"></TD>
						</TR>

						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;查&nbsp询&nbsp;</button>
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
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>